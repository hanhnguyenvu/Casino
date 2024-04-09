package Logic
import scala.collection.mutable
import scala.util.Random

class Deck(val game: Game) :
  val suits = mutable.Buffer("Hearts","Diamonds","Clubs","Spades")
  val names = mutable.Buffer("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A")
  var decks : mutable.Buffer[Cards] =
    for {s <- suits
         n <- names}
    yield
      Cards(s,n,game)
  var remainings = decks

  def shuffled() =
    remainings = Random.shuffle(remainings)

  def dealFromStart(players: mutable.Buffer[Player],table: Table, game: Game) =  //deal 4 cards to the players, 1 card a player at a row so that the number of cards in each player's hand is balanced.
    var i = 0
    if game.gameStart then
      shuffled()
      table.cardsOnTable.clear()
      players.foreach(_.hand.clear())
      var notDealer = players.filterNot(p => p.isDealer)
      for _ <- 1 to 4 do
        notDealer.foreach { player =>
          if remainings.nonEmpty then
            player.hand += remainings.head
            remainings = remainings.tail }

      table.cardsOnTable = remainings.take(4)
      remainings = remainings.drop(4)

  def deal(player:Player) =   //deal the card to the player when he ends his turn
    if remainings.nonEmpty then
      val drawnCard = remainings.head
      player.hand += drawnCard
      remainings = remainings.drop(1)
