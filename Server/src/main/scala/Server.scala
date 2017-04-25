import java.net.InetSocketAddress

import common.{Packet, WebSocket}
import org.java_websocket.{WebSocket => NativeWebSocket}
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer

import scala.collection.JavaConverters._
import upickle.default._

import scala.util.{Failure, Success, Try}

class Server(port: Int,
             onPacket: (WebSocket, Packet) => Unit,
             onOpen: WebSocket => Unit,
             onClose: WebSocket => Unit) extends WebSocketServer(new InetSocketAddress(port)) {

  override def onError(conn: NativeWebSocket, ex: Exception): Unit = {
    ex.printStackTrace()
  }

  override def onMessage(conn: NativeWebSocket, message: String): Unit = {
    try {
      parsePacket(message) match {
        case Success(p) => onPacket(ServerWebSocket(conn), p)
        case Failure(exception) => exception.printStackTrace()
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  def parsePacket(message: String): Try[Packet] = {
    Try(read[Packet](message))
  }

  override def onOpen(conn: NativeWebSocket, handshake: ClientHandshake): Unit = {
    println("opened new connection")

    onOpen(ServerWebSocket(conn))
  }

  override def onClose(conn: NativeWebSocket, code: Int, reason: String, remote: Boolean): Unit = {
    println("closed connection")

    onClose(ServerWebSocket(conn))
  }
}

case class ServerWebSocket(conn: NativeWebSocket) extends WebSocket {
  override def send(packet: Packet): Unit = if (!conn.isClosed) conn.send(write(packet)) else println("tried to send while close")
}