
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature.{HashingTF, Tokenizer, StringIndexer}
import org.apache.spark.ml.tuning.{ParamGridBuilder, CrossValidator}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.SaveMode

case class LabelledDocument(fileName:String, text:String, category:String)

object LogisticRegressionDemo extends App {

  val conf = new SparkConf().setAppName("LrTest")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)
  import sqlContext._
  import sqlContext.implicits._

  val spamText = sc.wholeTextFiles("spam/*")
  val hamText = sc.wholeTextFiles("ham/*")

  val spamDocuments = spamText.map { 
    case (fileName, text) => LabelledDocument(fileName, text, "spam")
  }
  val hamDocuments = hamText.map {
    case (fileName, text) => LabelledDocument(fileName, text, "ham")
  }

  val documentsDF = spamDocuments.union(hamDocuments).toDF
  documentsDF.persist

  val Array(trainDF, testDF) = documentsDF.randomSplit(Array(0.7, 0.3))

  val indexer = new StringIndexer().setInputCol("category").setOutputCol("label")
  val tokenizer = new Tokenizer().setInputCol("text").setOutputCol("words")
  val hasher = new HashingTF().setInputCol("words").setOutputCol("features")
  val lr = new LogisticRegression().setMaxIter(50).setRegParam(0.0)

  val pipeline = new Pipeline().setStages(Array(indexer, tokenizer, hasher, lr))
  val model = pipeline.fit(trainDF)

  val transformedTrain = model.transform(trainDF)
  transformedTrain.persist
  
  val transformedTest = model.transform(testDF)
  transformedTest.persist

  println("in sample misclassified:", transformedTrain.filter($"prediction" !== $"label").count,
    " / ",transformedTrain.count)
  println("out sample misclassified:", transformedTest.filter($"prediction" !== $"label").count,
    " / ",transformedTest.count)

  transformedTrain.select("fileName", "label", "prediction", "probability")
    .write.mode(SaveMode.Overwrite).parquet("transformedTrain.parquet")
  transformedTest.select("fileName", "label", "prediction", "probability")
    .write.mode(SaveMode.Overwrite).parquet("transformedTest.parquet")
}
