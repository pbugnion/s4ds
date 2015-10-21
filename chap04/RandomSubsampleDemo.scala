
import breeze.linalg._
import breeze.linalg.functions.manhattanDistance
import breeze.numerics._
import breeze.stats._

object RandomSubsampleDemo extends App {

  /* Load and pre-process data */
  val data = HWData.load

  val rescaledHeights:DenseVector[Double] =
    (data.heights - mean(data.heights)) / stddev(data.heights)

  val rescaledWeights:DenseVector[Double] =
    (data.weights - mean(data.weights)) / stddev(data.weights)

  val featureMatrix:DenseMatrix[Double] =
    DenseMatrix.horzcat( 
      DenseMatrix.ones[Double](data.npoints, 1), 
      rescaledHeights.toDenseMatrix.t,
      rescaledWeights.toDenseMatrix.t
    )

  val target:DenseVector[Double] =
    data.genders.values.map { gender => if(gender == 'M') 1.0 else 0.0 }

  /* Cross-validation */
  val testSize = 20
  val cvCalculator = new RandomSubsample(data.npoints, testSize)

  val cvErrors = cvCalculator.mapSamples(1000) { (trainingIndices, testIndices) =>
    val regressor = new LogisticRegression(
      data.featureMatrix(trainingIndices, ::).toDenseMatrix,
      data.target(trainingIndices).toDenseVector
    )
    val genderPredictions = regressor.classifyMany(data.featureMatrix(testIndices, ::).toDenseMatrix)
    manhattanDistance(genderPredictions, data.target(testIndices)) / testSize.toDouble
  }

  println(s"Mean classification error: ${mean(cvErrors)}")
}
