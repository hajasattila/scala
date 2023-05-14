package Effects

case class TicksLeft(ticks: Int) extends Duration {
  def tick(): Option[Duration] =
    if (ticks == 1) None
    else Some(copy(ticks = ticks - 1))
}

