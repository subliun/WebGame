package entities

import common.entities.{Position, RabbitCommon, RabbitInfo, Rectangle}

import scala.util.Random

class Rabbit(var info: RabbitInfo) extends RabbitCommon(info) {

  def this() {
    this(RabbitInfo(Position.Origin, 0, 0, 600))
  }

  def onCaught(screenBounds: Rectangle): Unit = {
    pickRandomPosition(screenBounds)
    pickRandomDirection()
  }

  def pickRandomDirection(): Unit = {
    info.angle = Random.nextInt(360)
    info.angleChange = (Random.nextInt(20) + 10) * (if (Random.nextBoolean()) -1 else 1)
  }

  def reverseDirection(): Unit = {
    info.angle = Math.abs(180 - info.angle)
    info.angleChange = -info.angleChange
  }

  def pickRandomPosition(screenBounds: Rectangle): Unit = {
    info.position = Position(Random.nextInt(screenBounds.width) - info.bounds.width, Random.nextInt(screenBounds.height) - info.bounds.height)
  }
}
