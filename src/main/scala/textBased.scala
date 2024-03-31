import scala.collection.mutable
import scala.io.StdIn.readLine
import java.io.*


object GameSaver:
  def gameStateToString(game: Game): String =
    val sb = new StringBuilder()

    sb.append("Players:\n")
    for player <- game.players do
      sb.append(s"${player.name}: ${player.score}\n")
      sb.append(s"Hand: ${player.hand.mkString(", ")}")
      sb.append("\n")
      sb.append(s"Pile: ${player.pile.mkString(", ")}")
      sb.append("\n")
      sb.append("---\n")
    sb.append(s"Table: ${game.table.cardsOnTable.mkString(", ")}")
    sb.append("\n")

    sb.append(s"Deck: ${game.deck.remainings.mkString(", ")}")
    sb.append("\n")
    if !game.endGame then
      sb.append(s"Turns: ${game.turn}")
      sb.append(s"\nIt's ${game.players(game.turn%game.players.size).name}'s turn now.")
      sb.append("\n")
      sb.append(s"Last person to capture something: ${game.lastCapturingPlayer.getOrElse(Player("_",game)).name}")
    else sb.append("\nEnd")

    sb.toString()

object textBased extends App:
  println("CASINO!")

  def loadGame(): Unit =
    println("Enter the filename to load the game state (_.txt):")
    val filename = scala.io.StdIn.readLine()
    try {
      val loadedGame = gameLoad.loadGameFromFile(filename)
      if loadedGame.isOver then
        println("Cannot continue because the game is over already. A new game will start.")
        startNewGame()
      else
        val playerNames = loadedGame.players.map(_.name)
        playGame(loadedGame, playerNames)
    }
    catch {
      case e: FileNotFoundException =>
        println(s"File '$filename' not found. Please make sure the file exists and try again.")
      case e: Exception =>
        println(s"${e.getMessage}")
        println("Cannot continue because the game is over already. A new game will start.\n")
        startNewGame()
  }

  println("Do you want to start a new game or load from a file? If yes, enter 'load'. If not, enter anything you want, a new game will start.")
  val response = scala.io.StdIn.readLine().toLowerCase()
  if response == "load" then
    loadGame()
  else
    startNewGame()

  def startNewGame() : Unit =
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
    playGame(game, playerNames)

  def playGame(game: Game,playerNames: mutable.Buffer[String]) =
    val table = game.table


    def saveGameToFile(game: Game, filename: String): Unit =
      val content = GameSaver.gameStateToString(game)
      val file = new File(filename)
      val bw = new BufferedWriter(new FileWriter(file))
      bw.write(content)
      bw.close()
      println(s"Game state saved to $filename")

    def saveGamePrompt(): Unit =
      println("Do you want to save the game state? (yes/no)")
      val response = readLine().toLowerCase()
      if response == "yes" || response == "y" then
        println("Enter the filename to save the game state (_.txt):")
        val filename = readLine()
        saveGameToFile(game, filename)
      else
        println("Game state not saved.")

    def showTable() =
      if !game.isOver && !game.saved then
        println("\nTable: ")
        table.cardsOnTable.foreach(println)
        println("")

    def whatCommand(): Unit =
      game.players(game.numTurn).show()
      var command = readLine(s"It's ${playerNames(game.numTurn)}'s turn. Play some cards (enter just the name of the card, the suit is not needed, either the short or long name will work): ")
      try
        game.playTurn(command)
        if game.numTurn >= 1 then
          game.players(game.numTurn-1).showpile()
        else game.players.last.showpile()
      catch
        case e: Exception =>
          println(s"Invalid: ${e.getMessage} Please try another command.")
          var isValid = false
          whatCommand()
        case _ => var isValid = true

    while !game.endGame && !game.saved && !game.isOver do
        showTable()
        whatCommand()

    if !game.players.exists(p => p.hand.nonEmpty) then
      val lastOption = game.lastCapturingPlayer
      val playerNotInGame = Player("_",game)
      var last = lastOption.getOrElse(playerNotInGame)
      if last.name != playerNotInGame.name && game.endGame then
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
    if !game.saved then
      println(s"The game has ended. We have our winner. ${winner.name}, congratulations!")
    saveGamePrompt()

