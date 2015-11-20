
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

object MutualInformation extends App {

  def wordFractionInFiles(sc:SparkContext)(fileGlob:String):(RDD[(String, Double)], Long) = {

    // A set of punctuation words that need to be filtered out.
    val wordsToOmit = Set[String](
      "", ".", ",", ":", "-", "\"", "'", ")", 
      "(", "@", "/", "Subject:"
    )

    val messages = sc.wholeTextFiles(fileGlob)
    val nMessages = messages.count()
    val message2Word = messages.flatMapValues { 
      mailBody => mailBody.split("\\s").toSet 
    }

    val message2FilteredWords = message2Word.filter { 
      case(email, word) => ! wordsToOmit(word) 
    }

    val word2Message = message2FilteredWords.map { _.swap }
    val word2NumberMessages = word2Message.mapValues { 
      _ => 1 
    }.reduceByKey { _ + _ }

    val pPresent = word2NumberMessages.mapValues { 
      _ / nMessages.toDouble 
    }
    (pPresent, nMessages)
  }

  def miTerm(
    pXYs:RDD[(String, Double)], 
    pXs:RDD[(String, Double)], 
    pY: Double,
    default: Double // for words absent in PXY
  ):RDD[(String, Double)] = 
    pXs.leftOuterJoin(pXYs).mapValues { 
      case (pX, Some(pXY)) => pXY * math.log(pXY/(pX*pY)) 
      case (pX, None) => default * math.log(default/(pX*pY))
    }

  val conf = new SparkConf().setAppName("lingSpam")
  val sc = new SparkContext(conf)

  val (pPresentGivenSpam, nSpam) = wordFractionInFiles(sc)("spam/*")
  val pAbsentGivenSpam = pPresentGivenSpam.mapValues { 1.0 - _ }
  val (pPresentGivenHam, nHam) = wordFractionInFiles(sc)("ham/*")
  val pAbsentGivenHam = pPresentGivenHam.mapValues { 1.0 - _ }

  val nMessages = nSpam + nHam
  val pSpam = nSpam / nMessages.toDouble
  val pHam = 1.0 - pSpam

  val pPresentAndSpam = pPresentGivenSpam.mapValues { _ * pSpam }
  val pPresentAndHam = pPresentGivenHam.mapValues { _ * pHam }
  val pAbsentAndSpam = pAbsentGivenSpam.mapValues { _ * pSpam }
  val pAbsentAndHam = pAbsentGivenHam.mapValues { _ * pHam }

  pPresentAndSpam.persist
  pPresentAndHam.persist
  pAbsentAndSpam.persist
  pAbsentAndHam.persist

  val pJoined = pPresentAndSpam.fullOuterJoin(pPresentAndHam)
  val pJoinedDefault = pJoined.mapValues {
      case (presentAndSpam, presentAndHam) => 
        (presentAndSpam.getOrElse(0.5/nSpam * pSpam), 
        presentAndHam.getOrElse(0.5/nHam * pHam))
  }
  val pPresent = pJoinedDefault.mapValues { 
    case(presentAndHam, presentAndSpam) => 
      presentAndHam + presentAndSpam 
  }
  pPresent.persist

  val pAbsent = pPresent.mapValues { 1.0 - _ }
  pAbsent.persist

  val miTerms = List(
      miTerm(pPresentAndSpam, pPresent, pSpam, 0.5/nSpam * pSpam),
      miTerm(pPresentAndHam, pPresent, pHam, 0.5/nHam * pHam),
      miTerm(pAbsentAndSpam, pAbsent, pSpam, 0.5/nSpam * pSpam),
      miTerm(pAbsentAndHam, pAbsent, pHam, 0.5/nHam * pHam)
    )


  val mutualInformation = miTerms.reduce { 
    (term1, term2) => term1.join(term2).mapValues { case (l, r) => l + r } 
  }

  mutualInformation.takeOrdered(20)(Ordering.by { - _._2 })
    .foreach { println }

}
