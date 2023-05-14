package Items

/**
 * Item egy trait (interface)
 */
trait Item {

  /**
   * name metódus az Item nevét adja meg
   */
  def name: String

  /**
   * maxStackSize metódus pozitívnak kell lennie, megadja hogy egy adott típusú itemből egy inventory slotban mennyit rakhatsz maximum
   */
  def maxStackSize: Int
}
