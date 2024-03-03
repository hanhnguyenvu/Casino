import scala.collection.mutable

class Table:
  var cardsOnTable = mutable.Buffer[Cards]()
  var isEmpty: Boolean = cardsOnTable.isEmpty
  def isOnTable(card:Cards): Boolean = cardsOnTable.contains(card) 
  