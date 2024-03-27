import scala.collection.mutable

class Game():
  var turn = 0
  var gameStart: Boolean = turn == 0
  var players  = mutable.Buffer[Player]()
  var numTurn = 0
 // var currentPlayer = players(numTurn)      temporarily fixed
  var table = Table(this)
  var deck =  Deck(this)

  // the rounds + turn
  def addPlayer(player: Player) =
    players += player

  def fillTable() =
    if table.isEmpty && players.size >= 2 then
      table.cardsOnTable = deck.remainings.take(4)
      deck.remainings.drop(4)

  def playTurn(input: String) =
    if players.size >= 2 then
      var currentPlayer = players(numTurn)
      val commands = Command(input)
      commands.askAction(currentPlayer)
      if commands.action != "show" then
        this.deck.deal(currentPlayer)
        if numTurn < players.size then numTurn += 1
    else
      numTurn = 0
      print("Not enough players in the game. Cannot start game.")

  def endGame = players.exists(p => p.score >= 16)|| deck.remainings.isEmpty

  //erase the quitting player in the game

//  def save(fileName: String) =


  //validating algorithm

// over, not over
// announcement, messageline