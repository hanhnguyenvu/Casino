import org.scalatest.matchers.should._
import org.scalatest.wordspec.AnyWordSpec
import scala.collection.mutable

class CardsSpec extends AnyWordSpec with Matchers {
  "A Cards" when {
    "created" should {
      val game = new Game()
      "have correct suit and name" in {
        val card = Cards("Hearts", "A",game)
        card.suit shouldEqual "Hearts"
        card.name shouldEqual "A"
      }
      "return correct real name" in {
        val card = Cards("Hearts", "Ace",game)
        card.realName shouldEqual "Ace"
      }
      "return correct real suit name" in {
        val card = Cards("hearts", "A",game)
        card.realSuitName shouldEqual "Hearts"
      }
      "have correct value1" in {
        val card = Cards("Hearts", "A",game)
        val player = new Player("John",game)
        game.addPlayer(player)
        player.hand += card
        card.value shouldEqual 14
      }
      "have correct value2" in {
        val card = Cards("Hearts", "A",game)
        val player = new Player("John",game)
        game.addPlayer(player)
        player.hand += card
        player.hand -= card
        card.value shouldEqual 1
      }
      "be in hand if in game" in {
        val game = new Game()
        val player = new Player("John",game)
        game.addPlayer(player)
        val card = Cards("Hearts", "A",game)
        player.hand += card
        card.isInHand shouldEqual true
      }
    }
  }
}

class DeckSpec extends AnyWordSpec with Matchers {
  "A Deck" when {
    "created" should {
      val game = new Game()
      "have correct number of cards" in {
        val deck = new Deck(game)
        deck.decks.length shouldEqual 52
      }
      "have shuffled cards" in {
        val deck1 = new Deck(game)
        val deck2 = new Deck(game)
        deck1.remainings should not equal deck2.remainings
      }
    }
    "dealt from start" should {
      "have correct number of cards in players' hands and on the table" in {
        val game = new Game()
        val table = new Table(game)
        val player1 = new Player("Alice",game)
        val player2 = new Player("Bob",game)
        val players = mutable.Buffer(player1, player2)
        val deck = new Deck(game)

        deck.dealFromStart(players, table, game)

        player1.hand.length shouldEqual 4
        player2.hand.length shouldEqual 4
        table.cardsOnTable.length shouldEqual 4
      }
    }
    "dealt to player" should {
      "have one less card remaining in the deck" in {
        val game = Game()
        val player = new Player("Alice",game)
        val deck = new Deck(game)
        val initialDeckSize = deck.remainings.length

        deck.deal(player)

        deck.remainings.length shouldEqual (initialDeckSize - 1)
      }
    }
  }
}


class TableSpec extends AnyWordSpec with Matchers {
  "A Table" when {
    "created" should {
      "be empty" in {
        val game = new Game()
        val table = new Table(game)
        table.isEmpty shouldBe true
      }
    }
    "cards are added" should {
      "not be empty" in {
        val game = new Game()
        val table = new Table(game)
        val card = new Cards("Hearts", "A", game)
        println(s"Before adding card: isEmpty = ${table.isEmpty}, cardsOnTable = ${table.cardsOnTable}")
        table.cardsOnTable += card
        println(s"After adding card: isEmpty = ${table.isEmpty}, cardsOnTable = ${table.cardsOnTable}")
        table.isEmpty shouldBe false
      }
      "correctly indicate if a card is on the table" in {
        val game = new Game()
        val table = new Table(game)
        val card1 = new Cards("Hearts", "A", game)
        val card2 = new Cards("Clubs", "2", game)
        table.cardsOnTable ++= List(card1, card2)

        table.isOnTable(card1) shouldBe true
        table.isOnTable(card2) shouldBe true

        val card3 = new Cards("Diamonds", "3", game)
        table.isOnTable(card3) shouldBe false
      }
    }
  }
}

class PlayerSpec extends AnyWordSpec with Matchers {
  "A Player" when {
    "putting down a card" should {
      "remove the card from the hand" in {
        val game = new Game()
        val player = new Player("Alice", game)
        val card = new Cards("Hearts", "A", game)
        player.hand += card
        player.putdown("A")
        player.hand should not contain card
      }
    }
    "capturing cards" should {
      "add the captured cards to the hand and increase the score" in {
        val game = new Game()
        val player = new Player("Alice", game)
        val table = game.table
        val card1 = new Cards("Hearts", "A", game)
        val card2 = new Cards("Hearts", "2", game)
        val card3 = new Cards("Hearts", "3", game)
        table.cardsOnTable ++= Seq(card1, card2, card3)
        player.capture("A,2,3")
        player.hand should contain allOf (card1, card2, card3)
        player.score shouldEqual (card1.value + card2.value + card3.value)
      }
      "not add cards to the hand if the total value does not match any card in the hand" in {
        val game = new Game()
        val player = new Player("Alice", game)
        val table = game.table
        val card1 = new Cards("Hearts", "A", game)
        val card2 = new Cards("Hearts", "2", game)
        table.cardsOnTable ++= Seq(card1, card2)
        player.capture("A,2,3")
        player.hand should not contain card1
        player.hand should not contain card2
      }
      "remove the captured cards from the table" in {
        val game = new Game()
        val player = new Player("Alice", game)
        val table = game.table
        val card1 = new Cards("Hearts", "A", game)
        val card2 = new Cards("Hearts", "2", game)
        val card3 = new Cards("Hearts", "3", game)
        table.cardsOnTable ++= Seq(card1, card2, card3)
        player.capture("A,2,3")
        table.cardsOnTable should contain noneOf(card1, card2, card3)
      }
    }
  }
}

class GameSpec extends AnyWordSpec with Matchers {
  "A Game" when {
    "created" should {
      "initialize with an empty player list" in {
        val game = new Game()
        game.players shouldBe empty
      }
    }
    "adding players" should {
      "increase the size of the player list" in {
        val game = new Game()
        val player1 = new Player("Alice", game)
        val player2 = new Player("Bob", game)
        game.addPlayer(player1)
        game.addPlayer(player2)
        game.players should have size 2
      }
    }
    "filling the table" should {
      "add cards to the table if it's empty" in {
        val game = new Game()
        val player1 = new Player("Alice", game)
        val player2 = new Player("Bob", game)
        game.addPlayer(player1)
        game.addPlayer(player2)
        game.fillTable()
        game.table.cardsOnTable should have size 4
      }
      "not add cards to the table if there are not enough players" in {
        val game = new Game()
        game.fillTable()
        game.table.cardsOnTable shouldBe empty
      }
    }
    "playing a turn" should {
      "perform actions and update the turn" in {
        val game = new Game()
        val player1 = new Player("Alice", game)
        val player2 = new Player("Bob", game)
        game.addPlayer(player1)
        game.addPlayer(player2)
        val initialTurn = game.turn
        game.playTurn("capture A")
        game.turn shouldEqual (initialTurn + 1)
      }
      "not increase the turn if there are not enough players" in {
        val game = new Game()
        val player1 = new Player("Alice", game)
        game.addPlayer(player1)
        val initialTurn = game.turn
        game.playTurn("capture A")
        game.turn shouldEqual initialTurn
      }
    }
    "checking end of game" should {
      "return true if a player's score is >= 16" in {
        val game = new Game()
        val player1 = new Player("Alice", game)
        player1.score = 16
        game.addPlayer(player1)
        game.endGame shouldBe true
      }
      "return true if the deck is empty" in {
        val game = new Game()
        game.deck.remainings.clear()
        game.endGame shouldBe true
      }
      "return false if neither player score is >= 16 nor the deck is empty" in {
        val game = new Game()
        val player1 = new Player("Alice", game)
        val player2 = new Player("Bob", game)
        game.addPlayer(player1)
        game.addPlayer(player2)
        game.endGame shouldBe false
      }
    }
  }
}