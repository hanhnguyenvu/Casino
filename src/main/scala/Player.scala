import scala.collection.mutable

class Player (val name: String) :
  var playerQuit: Boolean = false
  var game = Game()
  var table = game.table 
  var hand: mutable.Buffer[Cards] = mutable.Buffer()
  var score: Int = 0
  var cardsTakenFromTable: mutable.Buffer[Cards] = mutable.Buffer()
  var cardsNum = cardsTakenFromTable.size
  var SpadesNum = cardsTakenFromTable.count(card => card.realSuitName == "Spades")

  def capture(cards: String): Unit =
    var splitCards = cards.split(',')  //split the cards (just in case players want to capture many cards simultaneously
    var totalValue = 0
    for card <- splitCards do                                                          // thís is to check if the player has a valid card to trade those
      if table.cardsOnTable.exists(c => c.name == card && c.isOnTable) then
        var theCards = table.cardsOnTable.filter(c => c.name == card && c.isOnTable)
        totalValue += theCards.head.value
    if this.hand.exists(card => card.value == totalValue) then
      for card <- splitCards do
        if table.cardsOnTable.exists(c => c.name == card && c.isOnTable) then
          var chosenCard = table.cardsOnTable.filter(c => c.name == card)
          if chosenCard.size == 1 then
            hand.append(chosenCard.head)
            score += chosenCard.head.value                                              //if there is only 1 card with that name on the table
          else
            if chosenCard.head.realName == "10" then
                if chosenCard.exists(c => c.realSuitName == "Diamonds") then             // it is better to trade for D-10 and S-2 because they have more points
                  var theCard = chosenCard.filter(c => c.realSuitName == "Diamonds").head
                  hand.append(theCard)
                  score += theCard.value
                else
                  var theCard = chosenCard.head
                  hand.append(theCard)
                  score += theCard.value
            else if chosenCard.exists(c => c.realSuitName == "Spades") then               //,or just spades because it has bonus 1 point for the one with most spade
                  var theCard = chosenCard.filter(c => c.realSuitName == "Spades").head
                  hand.append(theCard)
                  score += theCard.value
            else
                  var theCard = chosenCard.head
                  hand.append(theCard)
                  score += theCard.value

  def putdown(card: String): Unit =
    if this.hand.exists(c=>c.name == card) then
      var theCards = this.hand.filter(c=>c.name == card)
      if theCards.size > 1 then 
        this.hand -= theCards.filter(c=>c.realSuitName != "Spades").head             //shouldn't put down spades cards  

  def show(): Unit =
    println(s"$name's hand:") 
    hand.foreach(println)

  def isInHand(card: Cards) : Boolean = hand.contains(card)

  def quit() =
    this.playerQuit = true
    "A player has quit."

  //def save(fileName: String) =
