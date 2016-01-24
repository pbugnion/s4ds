name := "S4DS"

organization := "s4ds"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.6"

//resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "1.1.4",
  "com.typesafe.akka" %% "akka-actor" % "2.3.12",
  "org.json4s" %% "json4s-native" % "3.2.10"
)

scalacOptions ++= Seq("-feature", "-language:postfixOps", "-deprecation")

