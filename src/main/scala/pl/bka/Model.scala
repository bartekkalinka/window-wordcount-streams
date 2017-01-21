package pl.bka

sealed trait Message

case class WindowWordCounts(counts: List[(Int, String)])

object WindowWordCounts {
  def zero: WindowWordCounts = WindowWordCounts(List((0, "")))
}

