enablePlugins(ScalaJSPlugin)

name := "WebGame-client"

version := "1.0"

scalaVersion := "2.12.1"

persistLauncher in Compile := true

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.1",
  "be.doeraene" %%% "scalajs-jquery" % "0.9.1",
  "com.lihaoyi" %%% "upickle" % "0.4.4",
  "io.monix" %%% "monix" % "2.2.3")