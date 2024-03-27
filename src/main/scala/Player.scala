import scala.collection.mutable

class Player (val name: String,val game:Game) :
  var playerQuit: Boolean = false
  var table = game.table 
  var hand: mutable.Buffer[Cards] = mutable.Buffer()
  var score: Int = 0
  var cardsTakenFromTable: mutable.Buffer[Cards] = mutable.Buffer()
  var cardsNum = cardsTakenFromTable.size
  var SpadesNum = cardsTakenFromTable.count(card => card.realSuitName == "Spades")

  def capture(card: String): Unit =
    var totalValue = 0
    if table.cardsOnTable.exists(c => (c.name.toLowerCase.head == card.toLowerCase.head) && c.isOnTable) then
      var theCards = table.cardsOnTable.filter(c => (c.name.toLowerCase.head == card.toLowerCase.head) && c.isOnTable)
      totalValue += theCards.head.value
    if this.hand.exists(card => card.value == totalValue) then
      if table.cardsOnTable.exists(c =>(c.name.toLowerCase.head == card.toLowerCase.head) && c.isOnTable) then
        var chosenCard = table.cardsOnTable.filter(c => (c.name.toLowerCase.head == card.toLowerCase.head))
        if chosenCard.size == 1 then
          hand.append(chosenCard.head)
          score += chosenCard.head.value
          table.cardsOnTable -= chosenCard.head //if there is only 1 card with that name on the table
        else
          if chosenCard.head.realName == "10" then
              if chosenCard.exists(c => c.realSuitName == "Diamonds") then             // it is better to trade for D-10 and S-2 because they have more points
                var theCard = chosenCard.filter(c => c.realSuitName == "Diamonds").head
                hand.append(theCard)
                score += theCard.value
                table.cardsOnTable -= theCard
              else
                var theCard = chosenCard.head
                hand.append(theCard)
                score += theCard.value
                table.cardsOnTable -= theCard
          else if chosenCard.exists(c => c.realSuitName == "Spades") then               //,or just spades because it has bonus 1 point for the one with most spade
                var theCard = chosenCard.filter(c => c.realSuitName == "Spades").head
                hand.append(theCard)
                score += theCard.value
                table.cardsOnTable -= theCard
          else
                var theCard = chosenCard.head
                hand.append(theCard)
                score += theCard.value
                table.cardsOnTable -= theCard

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
