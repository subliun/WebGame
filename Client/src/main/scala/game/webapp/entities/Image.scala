package game.webapp.entities

import org.scalajs.dom
import org.scalajs.dom.raw.HTMLImageElement

class Image(src: String) {
  val element = dom.document.createElement("img").asInstanceOf[HTMLImageElement]
  element.src = src
}
