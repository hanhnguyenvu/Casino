import scala.collection.mutable
import scala.io.StdIn.readLine
import java.io.*


object GameSaver:
  def gameStateToString(game: Game): String =
    val sb = new StringBuilder()

    sb.append("Players:\n")
    sb.append(s"Current dealer: ${game.players(game.dealerIndex).name}\n")
    for player <- game.players do
      sb.append(s"${player.name}: ${player.score}\n")
      sb.append(s"Total: ${player.totalScore}\n")
      sb.append(s"Sweep: ${player.sweep}\n")
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
      val saver = game.players.filter(p=>p.wantsToSave).head
      sb.append(s"Turns: ${game.turn}")
      sb.append(s"\nSaver: ${saver.name}")
      sb.append(s"\nIt's ${saver.name}'s turn now.")
      sb.append("\n")
      sb.append(s"Last person to capture something: ${game.lastCapturingPlayer.getOrElse(Player("_",game)).name}")
    else sb.append("\nEnd")

    sb.toString()

object textBased extends App:
  println("CASINO!")

  def loadGame(): Unit =
    var validFileLoaded = false
    while !validFileLoaded do
      println("Enter the filename to load the game state (_.txt):")
      val filename = scala.io.StdIn.readLine().trim
      try {
        val loadedGame = gameLoad.loadGameFromFile(filename)
        val playerNames = loadedGame.players.map(_.name)
        playGame(loadedGame, playerNames)
        validFileLoaded = true
      } catch {
        case e: FileNotFoundException =>
          println(s"File '$filename' not found. Please make sure the file exists and try again.\n")
        case e: Exception =>
          println("Please try again.\n")}

  println("Do you want to start a new game or load from a file? If yes, enter 'load'. If not, enter anything you want, a new game will start.")
  val response = scala.io.StdIn.readLine().toLowerCase()
  if response.trim == "load" then
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

    val dealer = game.players(game.dealerIndex)

    game.players(game.dealerIndex).isDealer = true

    println("Let's start!")
    gameDeck.dealFromStart(game.players, table, game)
    playGame(game, playerNames)

  def playGame(game: Game,playerNames: mutable.Buffer[String]) =
    val table = game.table
    val dealer = game.players(game.dealerIndex)
    def setDealer() = game.players(game.dealerIndex).isDealer = true


    def startNewRound(): Unit =
      if game.players.exists(p => p.score >= 16) then
        for i <- game.players.indices do
          game.players(i).totalScore += game.players.map(p=>p.score)(i)
        game.players.foreach(p => p.score = 0)
        game.gameStart = true
        game.players(game.dealerIndex).isDealer = false
        game.dealerIndex = (game.dealerIndex + 1) % game.players.size
        game.players(game.dealerIndex).isDealer = true
        game.numTurn = (game.dealerIndex + 1) % game.players.size
        game.deck.shuffled()
        game.deck.dealFromStart(game.players, table, game)
        println("\n---New round---\n")
        game.endRound = false

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
      if !game.endGame && !game.saved then
        println("\nTable: ")
        table.cardsOnTable.foreach(println)
        println("")

    def whatCommand(): Unit =
      while !game.players(game.numTurn).isDealer && game.players(game.numTurn).hand.isEmpty do
        game.numTurn = (game.numTurn + 1)%game.players.size

      if !game.players(game.numTurn).isDealer then
        game.players(game.numTurn).show()
      else
        if game.dealerIndex > 0 then game.players(game.dealerIndex - 1).showpile()
        else game.players.last.showpile()
      var command: String =
        if !game.players(game.numTurn).isDealer then
          readLine(s"It's ${playerNames(game.numTurn)}'s turn. Play some cards (enter just the name of the card, the suit is not needed, either the short or long name will work): ")
        else readLine(s"\nIt's ${playerNames(game.numTurn)}'s turn. ${playerNames(game.numTurn)} is the current dealer. You can only see your hand or pile in this round. Deal for the next player to play.\n")
      try
        game.playTurn(command)
        if !game.players(game.numTurn).isDealer then
          if game.numTurn >= 1 then
            game.players(game.numTurn-1).showpile()
          else game.players.last.showpile()
      catch
        case e: Exception =>
          println(s"Invalid: ${e.getMessage} Please try another command.")
          var isValid = false
          whatCommand()
        case _ => var isValid = true

    while !game.endGame && !game.saved do
        startNewRound()
        showTable()
        whatCommand()


    if game.endGame then
      val lastOption = game.lastCapturingPlayer
      val playerNotInGame = Player("_",game)
      var last = lastOption.getOrElse(playerNotInGame)
      if last.name != playerNotInGame.name && game.endGame then
        for c <- table.cardsOnTable do
          last.score += c.value
        last.pile ++= table.cardsOnTable
        table.cardsOnTable.clear()

      val playerMostCards = game.players.maxBy(_.pile.size)
      playerMostCards.score += 1
      val playerSpades = game.players.maxBy(p=>p.pile.count(c => c.realSuitName == "Spades"))
      playerSpades.score += 2
      if game.players.exists(p=>p.pile.contains(Cards("Diamonds","10",game))) then
        val playersWithD10 = game.players.filter(_.pile.contains(Cards("Diamonds", "10", game)))
        playersWithD10.foreach(_.score += 2)
      if game.players.exists(p=>p.pile.contains(Cards("Spades","2",game))) then
        val playersWithS2 = game.players.filter(_.pile.contains(Cards("Spades", "2", game)))
        playersWithS2.foreach(_.score += 1)
      for p <- game.players do
        var aces = p.pile.count(_.realName == "Ace")
        p.score += (1*aces)
      game.players.foreach(p => p.score += p.sweep*1 )
      for i <- game.players.indices do
          game.players(i).totalScore += game.players(i).score

      val winner = game.players.maxBy(_.totalScore)
      if game.numTurn >= 1 then
        game.players(game.numTurn-1).showpile()
      else game.players.last.showpile()
      println(s"The game has ended. We have our winner. ${winner.name}, congratulations!")
      winner.wantsToSave = true

    if game.saved then saveGamePrompt()

