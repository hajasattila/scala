package Items
/**
 * Placabel interface ami egy speciális item
 */
trait Placable extends Item {
  val canBeMined: Boolean = true
  /**
   * Item nevét visszaadó metódus
   *
   * @return item neve
   */
  override def name: String = this.name

  /**
   * Item stack méretét visszaadó metódus
   *
   * @return item stack méret
   */
  override def maxStackSize: Int = this.maxStackSize
}