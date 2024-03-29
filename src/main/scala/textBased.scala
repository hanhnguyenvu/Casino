import scala.collection.mutable
import scala.io.StdIn.readLine


object textBased extends App:
  val game = Game()
  val table = game.table
  var gameDeck = game.deck

  println("Welcome to the Casino! How many players would it be?")
  var numPlayers = scala.io.StdIn.readInt()
  var playerNames = mutable.Buffer[String]()
  while numPlayers <= 1 do
    println("Not enough players in the game. Cannot start game. Type another number of players.")

    numPlayers = scala.io.StdIn.readInt()

  for i <- 1 to numPlayers do
    print(s"Player ${i}: ")
    val playerName = scala.io.StdIn.readLine()
    playerNames += playerName
    game.addPlayer(Player(playerName,game))

  println("Let's start!")
  gameDeck.dealFromStart(game.players, table, game)

  def showTable() =
    println("\nTable: ")
    table.cardsOnTable.foreach(println)
    println("")

  def whatCommand(): Unit =
    game.players(game.numTurn).show()
    var command = readLine(s"It's ${playerNames(game.numTurn)}'s turn. Play some cards: ")
    try
      this.game.playTurn(command)
      if game.numTurn >= 1 then
        game.players(game.numTurn-1).showpile()
      else game.players.last.showpile()
    catch
      case e: Exception =>
        println(s"An error occurred: ${e.getMessage} Please try another command.")
        var isValid = false
        whatCommand()
      case _ => var isValid = true


  while !game.endGame do
    showTable()
    whatCommand()
  if !game.players.exists(p => p.hand.nonEmpty) then
    val lastOption = game.lastCapturingPlayer
    val playerNotInGame = Player("notInGame",game)
    var last = lastOption.getOrElse(playerNotInGame)
    if last.name != playerNotInGame.name then
      last.pile ++= table.cardsOnTable
      table.cardsOnTable.clear()

    val playerMostCards = game.players.maxBy(_.pile.size)
    playerMostCards.score += 1
    val playerSpades = game.players.maxBy(p=>p.pile.count(c => c.realSuitName == "Spades"))
    playerSpades.score += 2
    val pD10 = game.players.filter(p=>p.pile.contains(Cards("Diamonds","10",game))).head
    pD10.score += 2
    val pS2 = game.players.filter(p=>p.pile.contains(Cards("Spades","2",game))).head
    pS2.score += 1
    for p <- game.players do
      var aces = p.pile.count(_.realName == "Ace")
      p.score += (1*aces)


  val winner = game.players.maxBy(_.score)
  println(s"The game has ended. We have our winner. ${winner.name}, congratulations!")

