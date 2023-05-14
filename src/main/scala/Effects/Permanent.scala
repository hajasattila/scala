package Effects

case object Permanent extends Duration {
  def tick(): Option[Duration] = Some(this)
}
