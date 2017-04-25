package game.webapp

import common.entities.Position
import monix.reactive.subjects.PublishSubject
import org.scalajs.dom

class ClickHandler(canvas: GameCanvas) {

  var mouseDown = false
  val clickSubject = PublishSubject[Position]()
  val mouseMoveSubject = PublishSubject[Position]()

  canvas.htmlCanvas.addEventListener("mousedown", (event: dom.MouseEvent) => {
    mouseDown = true
    clickSubject.onNext(canvas.getCursorPosition(event))
  }, useCapture = false)

  canvas.htmlCanvas.addEventListener("mousemove", (event: dom.MouseEvent) => {
      mouseMoveSubject.onNext(canvas.getCursorPosition(event))
  }, useCapture = false)

  canvas.htmlCanvas.addEventListener("mouseup", (event: dom.MouseEvent) => {
    mouseDown = false
  }, useCapture = false)

}
