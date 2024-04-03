package Interface

import Logic.{Game, Player}

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
    sb.append(s"Logic.Table: ${game.table.cardsOnTable.mkString(", ")}")
    sb.append("\n")

    sb.append(s"Logic.Deck: ${game.deck.remainings.mkString(", ")}")
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
