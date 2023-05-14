package Effects
import Entitys.EntityStats

/**
 * Egy speciális effect
 * @param value mennyivel növeli a damaget
 */
case class IncreaseDamage(value: Int) extends Effect{

  /**
   * Felülírja az Effect trait apply metódusát.
   */
  override def apply(stats: EntityStats): EntityStats =  stats.copy(defense = stats.defense * value)
}
