class Command (input: String) :
  val commandText = input.trim.toLowerCase
  val action = commandText.takeWhile( _ != ' ' )
  val target  = commandText.drop(action.length).trim

  def askAction (player: Player) = action match
    case "play" => Some(player.move(target))
    case "show" => Some(player.show())
    case "show pile" => Some(player.showpile())
    case "save" => Some(player.save())
    case _ => throw new IllegalArgumentException("Command not found.")

