import scala.collection.mutable

class Game() :
  var turn = 0
  var gameStart: Boolean = turn == 0
  var players  = mutable.Buffer[Player]() // this is where the problem because this means no player in the player buffer
  var numTurn = 0
 // var currentPlayer = players(numTurn)      temporarily fixed
  var table = Table()
  var deck =  Deck()

  // the rounds + turn
  def addPlayer(player: Player) =
    players += player

  def playTurn(input: String) =
    if players.size >= 2 then
      var currentPlayer = players(numTurn)
      val commands = Command(input)
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