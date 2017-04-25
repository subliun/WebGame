package game.webapp

import common.entities.Position
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.raw.HTMLCanvasElement

class GameCanvas(id: String) {

  val htmlCanvas: HTMLCanvasElement = document.getElementById(id).asInstanceOf[HTMLCanvasElement]

  def width = htmlCanvas.width
  def height = htmlCanvas.height

  def context = htmlCanvas.getContext("2d")

  def scaleToScreen(): Unit = {
    println(s"w: ${htmlCanvas.style.width} h: ${htmlCanvas.style.height}")

    val ratio = htmlCanvas.height / htmlCanvas.width.toDouble

    val newHeight: Int = (ratio * window.innerWidth).toInt
    if (newHeight < window.innerHeight) {

      val styleWidth = window.innerWidth.toInt
      val styleHeight = newHeight

      htmlCanvas.style.width = styleWidth + "px"
      htmlCanvas.style.height = styleHeight + "px"
    } else {
      val ratioHeightwise = htmlCanvas.width / htmlCanvas.height.toDouble

      val styleWidth = (ratioHeightwise * window.innerHeight).toInt
      val styleHeight = window.innerHeight.toInt

      htmlCanvas.style.height = styleHeight + "px"
      htmlCanvas.style.width = styleWidth + "px"
    }
  }

  def getCursorPosition(event: dom.MouseEvent): Position = {
    val rect = htmlCanvas.getBoundingClientRect()
    val scaleX = htmlCanvas.width / rect.width
    val scaleY = htmlCanvas.height / rect.height

    val x = (event.clientX - rect.left) * scaleX
    val y = (event.clientY - rect.top) * scaleY
    Position(x, y)
  }
}
