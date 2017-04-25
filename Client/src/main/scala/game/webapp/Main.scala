package game.webapp

import java.util.concurrent.TimeUnit

import common.entities.{PlayerInfo, Position, RabbitInfo, Rectangle}
import common.{Packet, WebSocket}
import org.scalajs.dom.raw.{CanvasRenderingContext2D, HTMLCanvasElement}
import common.utils.RxUtils._
import monix.execution.Scheduler.Implicits.global

import scala.collection.mutable.ArrayBuffer
import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.{document, window}
import game.webapp.entities.{Player, Rabbit}
import org.scalajs.dom.html.{Button, Input}
import upickle.default._

import scala.concurrent.duration.Duration
import scala.util.Random

object Main extends JSApp {

  val canvas = new GameCanvas("game")
  val screenBounds = Rectangle(1920, 1080)
  val clickHandler = new ClickHandler(canvas)

  var game: Option[Game] = None

  def main(): Unit = {
    init()
  }

  def init(): Unit = {
    val nicknameTextbox = dom.document.getElementById("nickname").asInstanceOf[Input]
    val nicknameButton = dom.document.getElementById("submit-nickname").asInstanceOf[Button]
    nicknameTextbox.addEventListener("keyup", (e: dom.KeyboardEvent) => {
      if (e.keyCode == 13) { // enter
        onNameEntered(nicknameTextbox)
      }
    })

    nicknameButton.addEventListener("click", (e: dom.Event) => {
      onNameEntered(nicknameTextbox)
    })

    canvas.scaleToScreen()
    window.addEventListener("resize", (e: dom.Event) => {
      canvas.scaleToScreen()
    })
  }

  def onNameEntered(nicknameTextbox: Input): Unit = {
    val introBox = dom.document.getElementById("intro-box")
    val nickname = nicknameTextbox.value
    initGame(nickname)
    introBox.parentNode.removeChild(introBox) // this is madness
  }

  def initGame(nickname: String): Unit = {
    val server = joinGameserver(nickname)

    val canvasElement = dom.document.getElementById("game")
    canvasElement.setAttribute("style", canvasElement.getAttribute("style") + "cursor: none;")

    window.requestAnimationFrame((d: Double) => gameLoop(d, server))
  }

  def requestNextFrame(server: WebSocket): Unit = window.requestAnimationFrame((d: Double) => gameLoop(d, server))

  def joinGameserver(name: String): WebSocket = {
    val webSocket = new ClientWebSocket("ws://localhost:9003")

    webSocket.packetSubject.sub(onPacket(_, webSocket))

    webSocket.openCallback = (e: dom.Event) => {
      webSocket.send(Packet.JoinRequest(name))
    }

    webSocket
  }
  
  def onPacket(packet: Packet, socket: WebSocket): Unit = {
    packet match {
      case Packet.JoinAccepted(playerInfo, rabbitInfo) =>
        game = Some(new Game(screenBounds, socket, clickHandler, playerInfo, rabbitInfo))

      case p =>
        game.foreach(_.onPacket(p))
    }
  }

  def gameLoop(d: Double, socket: WebSocket): Unit = {
    val ctx = canvas.context.asInstanceOf[CanvasRenderingContext2D]
    ctx.clearRect(0, 0, canvas.width, canvas.height)

    for (g <- game) {
      g.update()
      g.draw(ctx)
    }
    requestNextFrame(socket)
  }

}
