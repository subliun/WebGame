package common.entities

case class Color(r: Int, g: Int, b: Int) {
  val renderString = s"rgb($r,$g,$b)"
}