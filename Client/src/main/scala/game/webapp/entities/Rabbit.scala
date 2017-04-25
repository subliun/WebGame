package game.webapp.entities

import common.entities.{Position, RabbitCommon, RabbitInfo}
import org.scalajs.dom.raw.CanvasRenderingContext2D

class Rabbit(var info: RabbitInfo) extends RabbitCommon(info) with Drawable {
  var image = (0, new Image("images/vinal0.jpg"))

  override def draw(ctx: CanvasRenderingContext2D): Unit = {
    if (image._1 != info.image) {
      image = (info.image, new Image(s"images/vinal$image.jpg"))
    }
    ctx.drawImage(image._2.element, info.position.x, info.position.y, info.bounds.width, info.bounds.height)
  }
}
