

import com.mongodb.casbah.Imports._

object RepoNumber {

  def extractNumber(obj:DBObject):Option[Int] = {
    val repos = obj.getAs[List[DBObject]]("repos") orElse {
      println("Could not find or parse 'repos' field")
      None
    }
    repos.map { _.size }
  }

  val collection = MongoClient()("github")("users")

  def main(args:Array[String]) {
    val userIterator = collection.find()
    val repoNumbers = userIterator.map { extractNumber }
    val wellFormattedNumbers = repoNumbers.collect { case Some(v) => v }.toList
    val sum = wellFormattedNumbers.reduce { _ + _ }
    val count = wellFormattedNumbers.size
    
    if (count == 0) {
      println("No repos found")
    }
    else {
      val mean = sum.toDouble / count.toDouble
      println(s"Total number of users with repos: $count")
      println(s"Total number of repos: $sum")
      println(s"Mean number of repos: $mean")
    }
  }
}

