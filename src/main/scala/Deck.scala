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

  def dealFromStart(players: mutable.Buffer[Player],table: Table, game: Game) =
    if game.gameStart then
      shuffled()
      table.cardsOnTable.clear()
      var notDealer = players.filterNot(p => p.isDealer)
      for player<-notDealer do
        player.hand.clear()
        player.hand = remainings.take(4)
        remainings = remainings.drop(4)

      table.cardsOnTable = remainings.take(4)
      remainings = remainings.drop(4)

  def deal(player:Player) =
    if remainings.nonEmpty then
      val drawnCard = remainings.head
      player.hand += drawnCard
      remainings = remainings.drop(1)
