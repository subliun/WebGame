package game.webapp.entities

import common.entities.{Position, Rabbit, RabbitCommon, RabbitInfo}
import org.scalajs.dom.raw.CanvasRenderingContext2D

class Rabbit(var info: RabbitInfo) extends RabbitCommon(info) with Drawable {
  var images = (0 until Rabbit.photoNum).map(i => i -> new Image("images/vinal" + i + ".jpg")).toMap

  override def draw(ctx: CanvasRenderingContext2D): Unit = {
    ctx.drawImage(images(info.image).element, info.position.x, info.position.y, info.bounds.width, info.bounds.height)
  }
}
