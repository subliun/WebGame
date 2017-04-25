name := "WebGame-server"

scalaVersion := "2.12.1"

crossScalaVersions := Seq("2.12.1")

libraryDependencies ++= Seq(
  "org.java-websocket" % "Java-WebSocket" % "1.3.0",
  "com.lihaoyi" %% "upickle" % "0.4.4",
  "io.monix" %% "monix" % "2.2.3")
