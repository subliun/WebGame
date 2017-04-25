package game.webapp

import java.util.concurrent.TimeUnit

import common.{Packet, WebSocket}
import common.entities.{PlayerInfo, Position, RabbitInfo, Rectangle}
import common.utils.Timer
import game.webapp.entities.{Player, Rabbit, Scoreboard}
import monix.reactive.Observable
import org.scalajs.dom.raw.{CanvasRenderingContext2D, HTMLCanvasElement}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration.Duration
import common.utils.RxUtils._
import monix.execution.Ack.Continue
import org.scalajs.dom

class Game(screenBounds: Rectangle, socket: WebSocket, clickHandler: ClickHandler, playerInfo: PlayerInfo, rabbitInfo: RabbitInfo) {
  var me: Player = new Player(playerInfo)
  var players = new ArrayBuffer[Player]() :+ me //includes me

  val scoreboard = new Scoreboard(me)
  val rabbit: Rabbit = new Rabbit(rabbitInfo)

  var positionSendTimer = new Timer(delay = 50) // in millis

  def update(): Unit = {
    if (positionSendTimer.finished()) {
      socket.send(Packet.PlayerMove(me.info.id, me.info.position))
      positionSendTimer.reset()
    }


    println("moving rabbit " + rabbit.info.speed)
    rabbit.move(screenBounds)
  }

  def draw(ctx: CanvasRenderingContext2D): Unit = {
    rabbit.draw(ctx)

    for (player <- players) {
      player.draw(ctx)
    }

    scoreboard.draw(ctx, players)
  }

  var clicked = false

  clickHandler.mouseMoveSubject.sub(onMouseMove)
  def onMouseMove(p: Position): Unit = {
    me.info.position = p
  }

  clickHandler.clickSubject.sub((p: Position) => {
    println(s"rabbit: ${rabbit.info.position}   me: $p  iscolliding: {${rabbit.isColliding(p)}")
    if (rabbit.isColliding(p)) {
      try {
        socket.send(Packet.RabbitCaught(me.info.id))
      } catch {
        case e: Exception => e.printStackTrace()
      }
    }
  })

  def onPacket(packet: Packet): Unit = {
    packet match {
      case Packet.PlayerJoin(info) =>
        players += new Player(info)

      case Packet.PlayerLeave(id) =>
        players = players.filterNot(_.info.id == id)

      case Packet.PlayerMove(id, pos) =>
        players.find(_.info.id == id).foreach(_.info.position = pos)

      case Packet.RabbitCaught(catcher) =>
        players.find(_.info.id == catcher).foreach(_.info.score += 1)

      case Packet.RabbitMove(info) =>
        println("rabbit move " + info)
        rabbit.info.position = info.position
        rabbit.info.angle = info.angle
        rabbit.info.angleChange = info.angleChange
        rabbit.info.image = info.image

      case _ =>
        println("invalid packet received" + packet)
    }
  }
}
