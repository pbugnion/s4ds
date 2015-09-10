name := "S4DS"

organization := "s4ds"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.6"

resolvers ++= Seq(
    "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
    "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  "org.scalanlp" %% "breeze" % "0.11.2",
  "org.scalanlp" %% "breeze-natives" % "0.11.2",
  "org.slf4j" % "slf4j-simple" % "1.7.5"
)

initialCommands := """
import s4ds._"""
