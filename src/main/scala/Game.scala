
import scala.collection.mutable

class Game():
  var isValid = true
  var turn = 0
  var gameStart: Boolean = turn == 0
  var players  = mutable.Buffer[Player]()
  var numTurn = 0
 // var currentPlayer = players(numTurn)      temporarily fixed
  var table = Table(this)
  var deck =  Deck(this)
  var lastCapturingPlayer: Option[Player] = None
  def setLastCapturingPlayer(player: Player): Unit =
    lastCapturingPlayer = Some(player)

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
      if commands.action != "show" && commands.action != "show pile" && isValid then
        this.deck.deal(currentPlayer)
        if numTurn + 1 < players.size  then
          numTurn += 1
          turn += 1
        else numTurn = 0
    else
      numTurn = 0
      print("Not enough players in the game. Cannot start game.")

  def endGame = players.exists(p => p.score >= 16) || deck.remainings.isEmpty || players.exists(p => p.hand.isEmpty)
  def saved = players.exists(p => p.wantsToSave)
