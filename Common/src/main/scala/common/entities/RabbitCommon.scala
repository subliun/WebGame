package common.entities

import common.utils.Timer

class RabbitCommon(info: RabbitInfo) {
  private val moveTimer = new Timer(1000)

  def move(screenBounds: Rectangle): Unit = {
    val scale =
      //prevent the rabbit from freaking out on the first frame
      if (moveTimer.finished()) {
        println("prevented freakout")
        1
      } else {
        moveTimer.timeSinceReset() / 1000.toDouble
      }

    val scaledSpeed = info.speed * scale
    val scaledAngleChange = info.angleChange * scale

    val angle = (Math.PI / 180) * info.angle
    val movementX = scaledSpeed * Math.cos(angle)
    val movementY = scaledSpeed * Math.sin(angle)

    val currX = info.position.x
    val currY = info.position.y

    var newX = currX + movementX
    var newY = currY + movementY

    if (newX < 0) newX = screenBounds.width + newX
    if (newY < 0) newY = screenBounds.height + newY
    if (newX > screenBounds.width) newX = newX - screenBounds.width
    if (newY > screenBounds.height) newY = newY - screenBounds.height

    info.position = Position(newX, newY)

    info.angle += scaledAngleChange
    moveTimer.reset()
  }

  def isColliding(p2: Position): Boolean = {
    val collidingX = info.position.x < p2.x && info.position.x + info.bounds.width >= p2.x
    val collidingY = info.position.y < p2.y && info.position.y + info.bounds.height >= p2.y

    collidingX && collidingY
  }
}
