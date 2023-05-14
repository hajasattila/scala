package Effects

import Entitys.EntityStats

/**
 * Egy adott entitás statjaira hat.
 * interface
 */
trait Effect {

  /**
   * Ez azt adja vissza, hogy mivé alakította át a statokat.
   * @param stats átalakítandó stat
   * @return EntityStats-ot
   */
  def apply(stats: EntityStats): EntityStats

}
