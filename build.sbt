val rootName = "WebGame"

name := rootName

version := "1.0"

scalaVersion := "2.12.1"

lazy val common = project in file("Common")

lazy val client = (project in file("Client")).dependsOn(common)

lazy val server = (project in file("Server")).dependsOn(common)

