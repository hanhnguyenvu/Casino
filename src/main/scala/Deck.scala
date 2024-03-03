import scala.collection.mutable
import scala.util.Random

class Deck:
  val suits = mutable.Buffer("Hearts","Diamonds","Clubs","Spades")
  val names = mutable.Buffer("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A")
  var deck : mutable.Buffer[Cards] =
    for {s <- suits
         n <- names}
    yield
      new Cards(s,n)
  def shuffled = Random.shuffle(deck)
  var remainings = shuffled
  def dealFromStart(players: List[Player],table: Table, game: Game) = //haven't included the dealing method for each player
    if game.gameStart then
      for player<-players do
        player.hand = shuffled.take(4)
        remainings = shuffled.drop(4)
      for _ <- 1 until 4 do
        table.cardsOnTable = remainings.take(4)
        remainings.drop(4)

  def deal(player:Player) =
    if deck.nonEmpty then
      val drawnCard = remainings.head
      player.hand += drawnCard
      remainings.drop(0)
