package game.webapp.entities

import common.entities.{Position, RabbitCommon, RabbitInfo}
import org.scalajs.dom.raw.CanvasRenderingContext2D

class Rabbit(var info: RabbitInfo) extends RabbitCommon(info) with Drawable {
  val image = new Image("vinal.jpeg")

  override def draw(ctx: CanvasRenderingContext2D): Unit = {
    ctx.drawImage(image.element, info.position.x, info.position.y, info.bounds.width, info.bounds.height)
  }
}
