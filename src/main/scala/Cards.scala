
class Cards(val suit: String, val name: String) :
  var game = textBased.game

  def realName = name match {
    case "A"|"a"|"Ace"|"ace" => "Ace"
    case "2"|"Two"|"two" => "2"
    case "3"|"Three"|"three" => "3"
    case "4"|"Four"|"four" => "4"
    case "5"|"Five"|"five" => "5"
    case "6"|"Six"|"six" => "6"
    case "7"|"Seven"|"seven" => "7"
    case "8"|"Eight"|"eight" => "8"
    case "9"|"Nine"|"nine" => "9"
    case "10"|"Ten"|"ten" => "10"
    case "J"|"Jack"|"jack" => "Jack"
    case "Q"|"Queen"|"queen" => "Queen"
    case "K"|"King"|"king" => "King"
    case _ => throw new IllegalArgumentException("Invalid card name")}

  def realSuitName = suit match {
    case "Hearts"|"hearts" => "Hearts"
    case "Diamonds"|"diamonds" => "Diamonds"
    case "Clubs"|"clubs" => "Clubs"
    case "Spades"|"spades" => "Spades"
    case _ => throw new IllegalArgumentException("Invalid suit")}

  def value: Int = realName match {
    case "Ace" =>
      if this.isInHand then 14
      else 1
    case "2" =>
      if this.realSuitName == "Spades" then 15
      else 2
    case "3" => 3
    case "4" => 4
    case "5" => 5
    case "6" => 6
    case "7" => 7
    case "8" => 8
    case "9" => 9
    case "10" =>
      if this.realSuitName == "Diamonds" then 16
      else 10
    case "Jack" => 11
    case "Queen" => 12
    case "King" => 13
  }

  def isInHand: Boolean = game.players.exists(p=>p.isInHand(this))

  def isOnTable: Boolean = game.table.isOnTable(this)
  
  override def toString = s"${this.realName} of ${this.realSuitName}"


