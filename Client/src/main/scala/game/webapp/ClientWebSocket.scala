package game.webapp

import common.{Packet, WebSocket}
import monix.reactive.subjects.PublishSubject
import org.scalajs.dom
import upickle.default._

class ClientWebSocket(address: String) extends WebSocket {

  val packetSubject = PublishSubject[Packet]()
  var openCallback: (dom.Event) => Unit = (_: dom.Event) => Unit
  var closeCallback: (dom.Event) => Unit = (_: dom.Event) => Unit

  val socket = new dom.WebSocket(address)
  registerCallbacks(socket)

  override def send(packet: Packet): Unit = {
    socket.send(write(packet))
  }

  def registerCallbacks(socket: dom.WebSocket): Unit = {
    socket.onerror =
      (e: dom.ErrorEvent) => {
        println("error with socket " + e.message)
      }

    socket.onopen =
      (e: dom.Event) => {
        println("connected to server")
        openCallback(e)
      }

    socket.onclose = (e: dom.Event) => {
      closeCallback(e)
    }

    socket.onmessage =
      (e: dom.MessageEvent) => {
        val json = e.data.toString
        try {
          packetSubject.onNext(read[Packet](json))
        } catch {
          case _: Exception => println("received invalid data")
        }
      }
  }
}
