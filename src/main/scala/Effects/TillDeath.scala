package Effects


case object TillDeath extends Duration {
  def tick(): Option[Duration] = Some(this)
}
