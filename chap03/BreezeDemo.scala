
import breeze.linalg._
import breeze.numerics._
import breeze.plot._
import breeze.stats.regression._
import org.jfree.chart.axis.NumberTickUnit
import org.jfree.chart.plot.ValueMarker
import org.jfree.chart.annotations.XYTextAnnotation

object BreezeDemo extends App {

  def simplePlot {
    val x = linspace(-4.0, 4.0, 200)
    val fig = Figure("Sigmoid")
    val plt = fig.subplot(0)
    plt += plot(x, sigmoid(x), name="S(x)")
    plt += plot(x, sigmoid(2.0*x), name="S(2x)")
    plt += plot(x, sigmoid(10.0*x), name="S(10x)")
    plt.yaxis.setTickUnit(new NumberTickUnit(0.1))
    plt.plot.addDomainMarker(new ValueMarker(0.0))
    plt.plot.addRangeMarker(new ValueMarker(1.0))
    plt.xlim = (-4.0, 4.0)
    plt.xlabel = "x"
    plt.ylabel = "f(x)"
    plt.legend = true
  }

  def scatterPlot {
    val data = HWData.load
    val heights = data.heights
    val weights = data.weights

    val leastSquaresResult = leastSquares(DenseMatrix.horzcat(
      DenseMatrix.ones[Double](data.npoints, 1),
      heights.toDenseMatrix.t
    ), weights)
    val leastSquaresCoefficients = leastSquaresResult.coefficients
    val label = f"weight = ${leastSquaresCoefficients(0)}%.4f + ${leastSquaresCoefficients(1)}%.4f *height"
    println("Least squares result: ")
    println(label)
    println(s"residuals = ${leastSquaresResult.rSquared}")
    val dummyHeights = linspace(heights.min, heights.max, 200)
    val fittedWeights = leastSquaresCoefficients(0) :+ (leastSquaresCoefficients(1):*dummyHeights)

    val fig = Figure("Height vs. weight")
    val plt = fig.subplot(0)
    plt += plot(heights, weights, '+', colorcode="black")
    plt += plot(dummyHeights, fittedWeights, colorcode="red")
    plt.plot.addAnnotation(new XYTextAnnotation(label, 175.0, 105.0))

  }

  def advancedScatterExample {
    val fig = new Figure("Advanced scatter example")

    val plt = fig.subplot(0)

    val xs = linspace(0.0, 1.0, 100)
    val sizes = 0.025*rand(100)
    val colorPalette = new GradientPaintScale(0.0, 1.0, PaintScale.MaroonToGold)
    val colors = DenseVector.rand(100).mapValues(colorPalette)

    plt += scatter(xs, xs :^ 2.0, sizes.apply, colors.apply)
  }

  def subplotExample {
    val data = HWData.load

    val fig = new Figure("Subplot example")

    // upper subplot
    var plt = fig.subplot(2, 1, 0)
    plt += plot(data.heights, data.weights, '.')

    // lower subplot
    plt = fig.subplot(2, 1, 1)
    plt += plot(data.heights, data.reportedHeights, '.', colorcode="black")
  }

  // Run one of the following routines:
  //simplePlot
  //advancedScatterExample
  //scatterPlot
  //subplotExample
}
