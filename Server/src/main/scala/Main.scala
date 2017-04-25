import java.net.InetSocketAddress
import java.util.Collections

import common.{Packet, WebSocket}
import common.entities._
import entities.{Player, Rabbit}

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import upickle.default._

object Main extends App {

  val screenBounds = Rectangle(1920, 1080)

  var players: ArrayBuffer[Player] = new ArrayBuffer() //fix this

  var rabbit = new Rabbit()
  rabbit.onCaught(screenBounds) //initialise rabbit state

  val port = 9003
  val server = new Server(port, onPacket, onOpen, onClose)
  server.start()

  while (true) {
    if (Random.nextInt(50) == 0) {
      rabbit.pickRandomDirection()
      sendToAll(Packet.RabbitMove(rabbit.info))
    }

    /* if (rabbit.info.position.x < 0
      || rabbit.info.position.x > screenBounds.width
      || rabbit.info.position.y < 0
      || rabbit.info.position.y + rabbit.info.bounds.height > screenBounds.height) {
      println("triggered " + rabbit.info.angle)
      rabbit.reverseDirection()
    } */

    rabbit.move(screenBounds)

    Thread.sleep(100)
  }


  def onPacket(conn: WebSocket, packet: Packet): Unit = {
    packet match {
      case Packet.JoinRequest(name) =>
        println(name)
        println(conn)
        if (name.length > PlayerName.MaxLength) {
          conn.send(Packet.JoinDenied(name))
          return
        }

        val newPlayer = new Player(conn, PlayerInfo(PlayerId.genId(), PlayerName(name), 0, Position(0, 0), genColor()))

        conn.send(Packet.JoinAccepted(newPlayer.info, rabbit.info))

        //send player list to new player
        players.foreach(p => conn.send(Packet.PlayerJoin(p.info)))
        println(players)

        //send new player to all players
        val newPlayerPacket = Packet.PlayerJoin(newPlayer.info)
        sendToAll(newPlayerPacket)

        players += newPlayer

      case packet: Packet.PlayerMove =>
        players.find(_.info.id == packet.id).foreach(_.info.position = packet.position)
        sendToAll(packet)

      case packet: Packet.PlayerLeave =>
        players = players.filterNot(_.info.id == packet.id)
        sendToAll(packet)

      case packet: Packet.RabbitCaught =>
        println("RABBIT CAUGHT")
        players.find(_.info.id == packet.catcherId).foreach(_.info.score += 1)
        onRabbitCaught()
        sendToAll(packet)
        sendToAll(Packet.RabbitMove(rabbit.info))

      case _ => println(s"unhandled packet with id ${packet.packetId}")
    }
  }

  def onRabbitCaught(): Unit = {
    rabbit.onCaught(screenBounds)
  }

  def genColor(): Color = {
    Color((255 * Math.random()).toInt, (255 * Math.random()).toInt, (255 * Math.random()).toInt)
  }

  def sendToAll(packet: Packet): Unit = {
    players.foreach(p => p.conn.send(packet))
  }

  def onOpen(conn: WebSocket): Unit = {}

  def onClose(conn: WebSocket): Unit = {
    println(players)
    players.find(_.conn == conn).foreach(player => {
      players -= player
      players.foreach(_.conn.send(Packet.PlayerLeave(player.info.id)))
    })
    println(players)
  }
}


