package pl.bka.model

case class WindowWordCounts(counts: List[(Int, String)]) extends Message

object WindowWordCounts {
  def zero: WindowWordCounts = WindowWordCounts(List((0, "")))
}

