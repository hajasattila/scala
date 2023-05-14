package Items

import Effects.{Duration, Effect}

/**
 * Consumable egy interface
 *
 * @param effects az itemen lévő effekt, duration tuple
 */
trait Consumable extends Item{
  val effects: Vector[(Effect, Duration)]
}