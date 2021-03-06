package common.entities

case class RabbitInfo (var position: Position,
                       var angle: Double,
                       var angleChange: Double,
                       var speed: Double,
                       var image: Int,
                       var bounds: Rectangle = Rectangle(100, 100))