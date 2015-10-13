
import breeze.linalg._
import breeze.numerics._
import breeze.plot._

object ScatterplotMatrixDemo extends App {

  val data = HWData.load
  val fig = Figure("Scatterplot matrix demo")
  val m = new ScatterplotMatrix(fig)

  // Make a matrix with three columns: the height, weight and
  // reported weight data.
  val featureMatrix = DenseMatrix.horzcat(
    data.heights.toDenseMatrix.t,
    data.weights.toDenseMatrix.t,
    data.reportedWeights.toDenseMatrix.t
  )
  m.plotFeatures(featureMatrix, List("height", "weight", "reportedWeights"))

  fig.saveas("scatterplot_demo.png")
}
