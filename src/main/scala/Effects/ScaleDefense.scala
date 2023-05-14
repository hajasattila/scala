package Effects

import Entitys.EntityStats


/**
 * Egy speciális effect
 *
 * @param percentage a konstans
 */
case class ScaleDefense(percentage: Double) extends Effect{
  /**
   * Felülírja az Effect trait apply metódusát.
   */
  override def apply(stats: EntityStats): EntityStats = stats.copy(defense = (stats.defense * percentage).toInt)
}
