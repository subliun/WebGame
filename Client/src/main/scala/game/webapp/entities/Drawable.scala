package game.webapp.entities

import org.scalajs.dom.raw.CanvasRenderingContext2D

trait Drawable {
  def draw(ctx: CanvasRenderingContext2D): Unit
}
