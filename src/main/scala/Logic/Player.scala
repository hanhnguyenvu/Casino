package Logic

import scala.collection.mutable

class Player (val name: String,val game:Game):
  var sweep = 0
  var totalScore = 0
  var isDealer = false
  var wantsToDeal = false
  var wantsToSave = false
  var table = game.table 
  var hand: mutable.Buffer[Cards] = mutable.Buffer()
  var score: Int = 0
  var cardsTakenFromTable: mutable.Buffer[Cards] = mutable.Buffer()
  var cardsNum = cardsTakenFromTable.size
  var SpadesNum = cardsTakenFromTable.count(card => card.realSuitName == "Spades")
  var pile: mutable.Buffer[Cards] = mutable.Buffer()

  def deal() =
    if isDealer then wantsToDeal = true
    else throw new IllegalArgumentException("You are not the dealer.")

  def findBestCombination(card: Cards) =
    val values = card.value
    val possibleCards = game.table.cardsOnTable.filter(_.value <= values)
    if possibleCards.isEmpty then None
    else
      val allCombinations = (1 to possibleCards.size).flatMap(possibleCards.combinations)
      val bestCombinations = allCombinations.filter(_.map(_.value).sum == values).toBuffer
      if bestCombinations.nonEmpty then
        val bestCombination = bestCombinations.maxBy(_.length)
        bestCombination
      else None

  def move(card:String) =
    if !isDealer then
      var totalValue = 0
      var c =
        if card.length > 1 then
          this.hand.filter(c => (c.realName.toLowerCase == card.toLowerCase))
        else this.hand.filter(c => (c.realName.toLowerCase.head == card.toLowerCase.head))
      if c.nonEmpty then
        val chosenCard = c.maxBy(_.value)
        val best = findBestCombination(chosenCard)
        if best.isEmpty then
            val min = c.minBy(_.value)
            putdown(min.name)
        else
          game.setLastCapturingPlayer(this)
          if best.size == table.cardsOnTable.size then
            println("Sweep!")
            sweep += 1
          for b <- best do
            score += b.value
            pile += b
            table.cardsOnTable -= b
          hand -= chosenCard

      else
        throw new IllegalArgumentException("The specified card is not in the player's hand.")
    else
      throw new IllegalArgumentException("You are a dealer in this round. Cannot play cards.")


  def putdown(card: String): Unit =
    if !isDealer then
      var theCards =
        if card.length > 1 then
          this.hand.filter(c => (c.realName.toLowerCase == card.toLowerCase))
        else this.hand.filter(c => (c.realName.toLowerCase.head == card.toLowerCase.head))
      if theCards.size > 1 then
        if theCards.exists( c => c.realName == "10") then
          var d = theCards.filterNot( c => c.realSuitName == "Diamonds")
          var s = d.filterNot( c => c.realSuitName == "Spades")
          if s.isEmpty then
            var s1 = d.filter(c => c.realSuitName == "Spades").head
            this.hand = this.hand.filter(cards => cards != s1)
            table.cardsOnTable += s1
          else
            var another = s.head
            this.hand = this.hand.filter(cards => cards != another)
            table.cardsOnTable += another
        else
          var c = theCards.filter(c => c.realSuitName != "Spades").head
          this.hand = this.hand.filter(cards => cards != c)
          table.cardsOnTable += c
      else
        this.hand -= theCards.head
        table.cardsOnTable += theCards.head
    else throw new IllegalArgumentException("You are a dealer in this round. Cannot play cards.")

  def show(): Unit =
    println(s"$name's hand:")
    hand.foreach(println)
    println(" ")
  def showpile(): Unit =
    println(s"$name's pile:")
    pile.foreach(println)
    println(" ")

  def isInHand(card: Cards) : Boolean = hand.contains(card)

  def save() =
      wantsToSave = true

