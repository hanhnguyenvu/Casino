package Logic

class Cards(val suit: String, val name: String, val game: Game) :

  def realName = name.toLowerCase match {
    case "a"|"ace" => "Ace"
    case "2" => "2"
    case "3" => "3"
    case "4" => "4"
    case "5" => "5"
    case "6" => "6"
    case "7" => "7"
    case "8" => "8"
    case "9" => "9"
    case "10" => "10"
    case "j"|"jack" => "Jack"
    case "q"|"queen" => "Queen"
    case "k"|"king" => "King"
    case _ => throw new IllegalArgumentException("Invalid card name")}

  def realSuitName = suit.toLowerCase match {
    case "hearts" => "Hearts"
    case "diamonds" => "Diamonds"
    case "clubs" => "Clubs"
    case "spades" => "Spades"
    case _ => throw new IllegalArgumentException("Invalid suit")}

  def value: Int = realName match {
    case "Ace" =>
      if this.isInHand then 14
      else 1
    case "2" =>
      if this.realSuitName == "Spades" && this.isInHand then 15
      else 2
    case "3" => 3
    case "4" => 4
    case "5" => 5
    case "6" => 6
    case "7" => 7
    case "8" => 8
    case "9" => 9
    case "10" =>
      if this.realSuitName == "Diamonds" && this.isInHand then 16
      else 10
    case "Jack" => 11
    case "Queen" => 12
    case "King" => 13
  }

  def isInHand: Boolean = game.players.exists(p=>p.isInHand(this))

  def isOnTable: Boolean = game.table.isOnTable(this)
  
  override def toString = s"${this.realName} of ${this.realSuitName}"


