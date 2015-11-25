
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.feature.{HashingTF, Tokenizer, StringIndexer}
import org.apache.spark.ml.tuning.{ParamGridBuilder, CrossValidator}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics

import breeze.linalg._
import breeze.plot._
import org.jfree.chart.axis.NumberTickUnit


object ROC extends App {

  val conf = new SparkConf().setAppName("ROC")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)
  import sqlContext._
  import sqlContext.implicits._

  val transformedTest = sqlContext.read.parquet("transformedTest.parquet")

  val labelScores = transformedTest.select("probability", "label").map {
    case Row(probability:Vector, label:Double) => (probability(1), label)
  }

  val bm = new BinaryClassificationMetrics(labelScores, 300)
  val roc = bm.roc.collect
  
  roc.foreach { println }

  val falsePositives = roc.map { _._1 }
  val truePositives = roc.map { _._2 }

  val f = Figure()
  val p = f.subplot(0)
  p += plot(falsePositives, truePositives)
  p.xlabel = "false positives"
  p.ylabel = "true positives"
  p.xlim = (0.0, 0.1)
  p.xaxis.setTickUnit(new NumberTickUnit(0.01))
  p.yaxis.setTickUnit(new NumberTickUnit(0.1))
  f.refresh
  f.saveas("roc.png")
  

}
