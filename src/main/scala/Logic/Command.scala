package Logic

class Command (input: String) :
  val commandText = input.trim.toLowerCase
  val action = commandText.takeWhile( _ != ' ' )
  val target  = commandText.drop(action.length).trim

  def askAction (player: Player) = action match
    case "play" => Some(player.move(target))
    case "hand" => Some(player.show())
    case "pile" => Some(player.showpile())
    case "save" => Some(player.save())
    case "deal" => Some(player.deal())
    case _ => throw new IllegalArgumentException("Command not found.")

