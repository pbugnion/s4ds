scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "1.1.4",
  "org.json4s" %% "json4s-native" % "3.2.10",
  "org.mongodb" %% "casbah" % "3.0.0",
  "org.slf4j" % "slf4j-nop" % "1.7.12"
)

scalacOptions ++= Seq("-feature", "-language:postfixOps", "-deprecation")

