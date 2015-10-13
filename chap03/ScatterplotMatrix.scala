
import breeze.plot._
import breeze.linalg._

class ScatterplotMatrix(val figure:Figure) {
  
  def plotFeatures(featureMatrix:DenseMatrix[Double], labels:List[String]) {
    val ncols = featureMatrix.cols
    require(ncols == labels.size, 
      "Number of columns in feature matrix must match length of labels"
    )
    figure.clear
    figure.subplot(ncols, ncols, 0)
    (0 until ncols) foreach { irow => 
      val p = selectPlot(ncols)(irow, irow)
      plotHistogram(p)(featureMatrix(::, irow), labels(irow))
      (0 until irow) foreach { icol =>
        val p = selectPlot(ncols)(irow, icol)
        plotScatter(p)(
          featureMatrix(::, irow),
          featureMatrix(::, icol),
          labels(irow),
          labels(icol)
        )
      }
    }
  }

  private def plotHistogram(plt:Plot)(data:DenseVector[Double], label:String) = {
    plt += hist(data)
    plt.xlabel = label
  }

  private def plotScatter(plt:Plot)(
    xdata:DenseVector[Double], 
    ydata:DenseVector[Double], 
    xlabel:String, 
    ylabel:String) = {
      plt += plot(xdata, ydata, '.')
      plt.xlabel = xlabel
      plt.ylabel = ylabel
  }
    

  private def selectPlot(ncols:Int)(irow:Int, icol:Int) = {
    figure.subplot(ncols, ncols, (irow)*ncols + icol)
  }

}

