
import com.mongodb.casbah.Imports._

import Implicits._
import MongoReader._

object Demo extends App {

  val obj = DBObject("name" -> "s4ds", "language" -> "Scala")
  println(s"De-serializing $obj")

  val language = obj.read[Language.Value]("language")
  val name = obj.read[String]("name")

  println(s"Language field: $language")
  println(s"Name field: $name")

}
