package common

import common.entities.{PlayerId, PlayerInfo, Position, RabbitInfo}

import scala.collection.immutable.IndexedSeq

sealed abstract class Packet(val packetId: Int)

object Packet {
  case class JoinRequest(name: String) extends Packet(1)
  case class JoinAccepted(player: PlayerInfo, rabbit: RabbitInfo) extends Packet(2)
  case class JoinDenied(name: String) extends Packet(3)
  case class PlayerJoin(player: PlayerInfo) extends Packet(4)
  case class PlayerLeave(id: PlayerId) extends Packet(5)
  case class PlayerMove(id: PlayerId, position: Position) extends Packet(6)
  case class RabbitCaught(catcherId: PlayerId) extends Packet(7)
  case class RabbitMove(rabbitInfo: RabbitInfo) extends Packet(8)
}



