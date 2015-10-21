
import breeze.linalg._
import breeze.numerics._

class RandomSubsample(val nElems:Int, val nCrossValidation:Int) {

  type CVFunction = (Seq[Int], Seq[Int]) => Double

  require(nElems > nCrossValidation, 
    "nCrossValidation, the number of elements left out, must be < nElems")

  private val indexList = DenseVector.range(0, nElems)

  def mapSamples(nShuffles:Int)(f:CVFunction):DenseVector[Double] = {
    val cvResults = (0 to nShuffles).par.map { i =>
      val shuffledIndices = breeze.linalg.shuffle(indexList)
      val Seq(testIndices, trainingIndices) = split(shuffledIndices, Seq(nCrossValidation))
      f(trainingIndices.toScalaVector, testIndices.toScalaVector)
    }
    DenseVector(cvResults.toArray)
  }
}


