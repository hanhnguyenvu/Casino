import scala.collection.mutable

class Player (val name: String,val game:Game) :
  var playerQuit: Boolean = false
  var table = game.table 
  var hand: mutable.Buffer[Cards] = mutable.Buffer()
  var score: Int = 0
  var cardsTakenFromTable: mutable.Buffer[Cards] = mutable.Buffer()
  var cardsNum = cardsTakenFromTable.size
  var SpadesNum = cardsTakenFromTable.count(card => card.realSuitName == "Spades")
  var pile: mutable.Buffer[Cards] = mutable.Buffer()

  def findBestCombination(card: Cards) =
    val values = card.value
    val possibleCards = game.table.cardsOnTable.filter(_.value <= values)
    if possibleCards.isEmpty then None
    else
      val allCombinations = (1 to possibleCards.size).flatMap(possibleCards.combinations)
      val bestCombinations = allCombinations.find(_.map(_.value).sum == values).toBuffer
      if bestCombinations.nonEmpty then
        val bestCombination = bestCombinations.maxBy(_.size)
        bestCombination
      else None

  def move(card:String) =
    var totalValue = 0
    if this.hand.exists(c => (c.name.toLowerCase.head == card.toLowerCase.head)) then
      val c = this.hand.filter(c => (c.name.toLowerCase.head == card.toLowerCase.head))
      val chosenCard = c.maxBy(_.value)
      val min = c.minBy(_.value)
      val minWithoutSpades = c.filter(c1 => c1.realSuitName != "Spades").minBy(_.value)
      val best = findBestCombination(chosenCard)
      if best.isEmpty then
        if !c.exists(c1 => c1.realSuitName != "Spades") then
          putdown(min.name)
        else
          putdown(minWithoutSpades.name)
      else
        for b <- best do
          score += b.value
          pile += b
          table.cardsOnTable -= b
        hand -= chosenCard
        if best.size == 4 then 
          println("Sweep!")


  def putdown(card: String): Unit =
    if this.hand.exists(c => (c.name.toLowerCase.head == card.toLowerCase.head)) then
      var theCards = this.hand.filter(c => (c.name.toLowerCase.head == card.toLowerCase.head))
      if theCards.size > 1 then
        var c = theCards.filter(c => c.realSuitName != "Spades").head //shouldn't put down spades cards
        this.hand = this.hand.filter(cards => cards != c)
        table.cardsOnTable += c
      else
        this.hand -= theCards.head
        table.cardsOnTable += theCards.head

  def show(): Unit =
    println(s"$name's hand:") 
    hand.foreach(println)
    println(" ")

  def isInHand(card: Cards) : Boolean = hand.contains(card)

  def quit() =
    this.playerQuit = true
    "A player has quit."

  //def save(fileName: String) =
