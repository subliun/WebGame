package game.webapp.entities
import common.entities.PlayerInfo
import org.scalajs.dom.raw.CanvasRenderingContext2D

class Player(val info: PlayerInfo) extends Drawable {
  override def draw(ctx: CanvasRenderingContext2D): Unit = {
    ctx.save()
    ctx.beginPath()
    ctx.fillStyle = info.color.renderString
    ctx.arc(info.position.x, info.position.y, 15, 0, Math.PI * 2)
    ctx.fill()
    ctx.restore()
  }
}
