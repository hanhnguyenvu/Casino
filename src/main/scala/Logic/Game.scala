package Logic


import scala.collection.mutable

class Game():
  var isValid = true
  var turn = 0
  var gameStart: Boolean = turn == 0
  var players  = mutable.Buffer[Player]()
  var numTurn = 0

  var table = Table(this)
  var deck =  Deck(this)
  var dealerIndex: Int = 0

  var lastCapturingPlayer: Option[Player] = None
  def setLastCapturingPlayer(player: Player): Unit =  //find the last person to capture so that the bonus points are added at the end of the game
    lastCapturingPlayer = Some(player)

  def addPlayer(player: Player) =
    players += player

  def playTurn(input: String) =    //make the game flows properly, assigning the turns to the right player
    if players.size >= 2 then
      var currentPlayer = players(numTurn)
      val commands = Command(input)
      commands.askAction(currentPlayer)
      if !currentPlayer.isDealer then
        if commands.action != "hand" && commands.action != "pile" && isValid then
          if currentPlayer.hand.size == 3 then this.deck.deal(currentPlayer)
          if numTurn + 1 < players.size  then
            numTurn += 1
            turn += 1
          else numTurn = 0
      else
        if (commands.action != "hand" && commands.action != "pile") || commands.action == "deal" then
          numTurn = (numTurn + 1)%players.size

    else
      numTurn = 0
      print("Not enough players in the game. Cannot start game.")

  def endGame = players.filterNot(p => p.isDealer).forall(p => p.hand.isEmpty)
  var endRound = players.exists(p => p.score >= 16)

  def saved = players.exists(p => p.wantsToSave)
