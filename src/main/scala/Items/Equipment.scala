package Items

import Effects.{Duration, Effect}

/**
 * Equipment interface
 *
 * @param effects az itemen lévő effekt, duration tuple
 */
trait Equipment extends Item{
  val effects: Vector[(Effect, Duration)]
}
