package common.entities

sealed abstract class Shape

case class Rectangle(width: Int, height: Int) extends Shape

case class Circle(radius: Int) extends Shape