package Effects

/**
 * Duration interface
 */
trait Duration {
  def tick(): Option[Duration]
}