package Logic

import scala.collection.mutable

class Table(val game:Game):
  var cardsOnTable = mutable.Buffer[Cards]()
  def isEmpty: Boolean = cardsOnTable.isEmpty
  def isOnTable(card:Cards): Boolean = cardsOnTable.contains(card) 
  