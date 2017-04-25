package game.webapp.entities
import common.entities.Rectangle
import org.scalajs.dom.raw.CanvasRenderingContext2D

import scala.collection.mutable.ArrayBuffer

class Scoreboard(me: Player) {
  val bounds = Rectangle(240, 300)
  val scoreboardCapacity: Int = 5

  def draw(ctx: CanvasRenderingContext2D, players: ArrayBuffer[Player]): Unit = {
    ctx.save()
    ctx.fillStyle = me.info.color.renderString
    ctx.globalAlpha = 0.2
    ctx.fillRect(0, 0, bounds.width, bounds.height)
    ctx.restore()

    val scoreboardPlayers = players.sortBy(_.info.score).take(scoreboardCapacity).reverse
    for (i <- scoreboardPlayers.indices) {
      ctx.save()
      val player = scoreboardPlayers(i)
      val position = i + 1
      ctx.fillStyle = player.info.color.renderString
      ctx.font = "30px Arial"
      val xMargin: Double = 10
      ctx.fillText(s"$position. ${player.info.name} - ${player.info.score}", xMargin, 30 + (40 * i), bounds.width - xMargin * 2)
      ctx.restore()
    }
  }
}
