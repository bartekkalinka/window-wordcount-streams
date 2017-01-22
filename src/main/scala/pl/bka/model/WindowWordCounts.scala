package pl.bka.model

case class WindowWordCounts(counts: List[(Int, String)], interval: Long) extends InternalMessage

object WindowWordCounts {
  def zero: WindowWordCounts = WindowWordCounts(List((0, "")), 0L)
}

