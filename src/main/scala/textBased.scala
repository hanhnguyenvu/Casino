import scala.collection.mutable
import scala.io.StdIn.readLine
//playTurn, run,


object textBased extends App:
  val game = Game()
  val table = game.table
  var gameDeck = game.deck

  println("Welcome to the Casino! How many players would it be?")
  val numPlayers = scala.io.StdIn.readInt()
  var playerNames = mutable.Buffer[String]()

  for i <- 1 to numPlayers do
    print(s"Player ${i}: ")
    val playerName = scala.io.StdIn.readLine()
    playerNames += playerName
    game.addPlayer(Player(playerName,game))

  println("Let's start")
  gameDeck.dealFromStart(game.players, table, game)

  def showTable() =
    println("Table: ")
    table.cardsOnTable.foreach(println)

  def whatCommand() =
    var command = readLine("\nCommand: ")
    this.game.playTurn(command)

  while !game.endGame do
    showTable()
    whatCommand()

//the validating algorithm
