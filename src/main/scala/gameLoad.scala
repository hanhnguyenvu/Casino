import scala.collection.mutable
import scala.io.Source

object gameLoad:
  def loadGameFromFile(filename: String) =
    val source = Source.fromFile(filename)
    try {
      val game = Game()
      val content = source.mkString
      val contentLines = content.split("\n")
      if contentLines.last.startsWith("End") then
        throw new IllegalArgumentException("Cannot continue because the game is over already.")
      else
        val playersInfo = content.split("\n").dropRight(6)
        val dealer  = contentLines(1).split(":")(1).trim
        val tableInfo = contentLines(contentLines.indexWhere(_.startsWith("Table")))
        val deckInfo = contentLines(contentLines.indexWhere(_.startsWith("Deck")))
        val turnIndex = contentLines.indexWhere(_.startsWith("Turns:"))
        val saver = contentLines(contentLines.indexWhere(_.startsWith("Saver:"))).split(":")(1).trim
        val lastIndex = contentLines.indexWhere(_.startsWith("Last"))

        val playernames = getPlayersNames(playersInfo)
        val playerscores = getPlayersScores(playersInfo)
        val playerhand = getPlayerHands(playersInfo,game)
        val playerpile = getPlayerPiles(playersInfo,game)
        val tableCards = getTable(tableInfo,game)
        val deckCards = getDeck(deckInfo,game)

        if turnIndex != -1 then
          val turn = contentLines(turnIndex).split(":")(1).trim.toInt
          val lastcapturer = contentLines(lastIndex).split(":")(1).trim
          game.turn = turn
          game.numTurn = playernames.indexOf(saver)
          for i <- playernames.indices do
            val player = Player(playernames(i), game)
            game.addPlayer(player)
            player.score = playerscores(i)
            player.hand = playerhand(i)
            player.pile = playerpile(i)
            player.wantsToSave = false

          if playernames.contains(dealer) then
            game.dealerIndex = playernames.indexOf(dealer)
            game.players(game.dealerIndex).isDealer = true

          game.table.cardsOnTable = tableCards
          game.deck.remainings = deckCards

          if playernames.contains(lastcapturer) then game.setLastCapturingPlayer(Player(lastcapturer,game))
          game
        else Game()
    }
    catch {
      case e: IllegalArgumentException =>
        println(s"An unexpected error occurred: ${e.getMessage}")
        val game = Game()
        game
    }
    finally
      source.close()

  def getPlayersNames(playerLines: Array[String]): mutable.Buffer[String] =
    val eachplayerin4 = playerLines.drop(2)
    var playernames = mutable.Buffer[String]()
    var i = 0
    for i <- eachplayerin4.indices do
      if i % 4 == 0 then
        playernames += eachplayerin4(i).split(":")(0).trim
    playernames

  def getPlayersScores(playerLines: Array[String]): mutable.Buffer[Int] =
    val eachplayerin4 = playerLines.drop(2)
    var playerscores = mutable.Buffer[Int]()
    var i = 0
    for i <- eachplayerin4.indices do
      if i % 4 == 0 then
        playerscores += eachplayerin4(i).split(":")(1).trim.toInt
    playerscores

  def getPlayerHands(playerLines: Array[String],game:Game) =
    val eachPlayerIn4 = playerLines.drop(2)
    var buffer = mutable.Buffer[mutable.Buffer[Cards]]()

    for i <- eachPlayerIn4.indices do
      if i % 4 == 1 then
        var a = eachPlayerIn4(i).split(":")(1).trim
        if a.contains(", ") then
          var cards = a.split(", ").toBuffer
          var eachBuffer = mutable.Buffer[Cards]()
          for p <- cards do
            if p.contains(" of ") then
              var pname = p.split(" of ")(0).trim
              var psuit = p.split(" of ")(1).trim
              eachBuffer += Cards(psuit, pname, game)
          buffer += eachBuffer
        else
          if a.contains(" of ") then
            var pname = a.split(" of ")(0).trim
            var psuit = a.split(" of ")(1).trim
            buffer += mutable.Buffer(Cards(psuit, pname, game))
          else
            buffer += mutable.Buffer()
    buffer

  def getPlayerPiles(playerLines: Array[String],game:Game) =
    val eachPlayerIn4 = playerLines.drop(2)
    var buffer = mutable.Buffer[mutable.Buffer[Cards]]()

    for i <- eachPlayerIn4.indices do
      if i % 4 == 2 then
        var a = eachPlayerIn4(i).split(":")(1).trim
        if a.contains(", ") then
          var cards = a.split(", ").toBuffer
          var eachBuffer = mutable.Buffer[Cards]()
          for p <- cards do
            if p.contains(" of ") then
              var pname = p.split(" of ")(0).trim
              var psuit = p.split(" of ")(1).trim
              eachBuffer += Cards(psuit, pname, game)
          buffer += eachBuffer
        else
          if a.contains(" of ") then
            var pname = a.split(" of ")(0).trim
            var psuit = a.split(" of ")(1).trim
            buffer += mutable.Buffer(Cards(psuit, pname, game))
          else
            buffer += mutable.Buffer()
    buffer

  def getTable(tableLines: String,game:Game) =
    var table = tableLines.split(":")(1).trim
    var eachcard = table.split(", ")
    var buffer = mutable.Buffer[Cards]()
    for card <- eachcard do
      if card.split(" of ").length == 2 then
        val name = card.split(" of ")(0).trim
        val suit = card.split(" of ")(1).trim
        buffer += Cards(suit,name,game)
      else buffer = mutable.Buffer[Cards]()
    buffer

  def getDeck(deckLines: String,game:Game) =
    var deck = deckLines.split(":")(1).trim
    var eachcard = deck.split(", ")
    var buffer = mutable.Buffer[Cards]()
    for card <- eachcard do
      if card.split(" of ").length == 2 then
        val name = card.split(" of ")(0).trim
        val suit = card.split(" of ")(1).trim
        buffer += Cards(suit,name,game)
      else buffer = mutable.Buffer[Cards]()
    buffer