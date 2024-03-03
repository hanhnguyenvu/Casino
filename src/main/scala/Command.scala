class Command (input: String) :
  val commandText = input.trim.toLowerCase
  val action = commandText.takeWhile( _ != ' ' )
  val target  = commandText.drop(action.length).trim

  def askAction (player: Player) = action match
    case "capture" => Some(player.capture(target))
    case "put down" => Some(player.putdown(target))
    case "show" => Some(player.show())
    case "quit" => Some(player.quit())
    case _ => throw new IllegalArgumentException("Needs to choose an action")
