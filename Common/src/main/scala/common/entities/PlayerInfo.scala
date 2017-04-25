package common.entities

import common.WebSocket

import scala.util.Random

case class PlayerId private (id: String)
object PlayerId {
  def genId(): PlayerId = PlayerId(Random.alphanumeric.take(10).mkString)
}

case class PlayerName private (private val name: String) {
  override def toString: String = name
}
object PlayerName {
  val MaxLength = 16
}

case class PlayerInfo(id: PlayerId, name: PlayerName, var score: Int, var position: Position, color: Color)
