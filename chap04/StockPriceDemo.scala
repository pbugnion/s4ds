
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io._
import scala.xml.XML
import scala.util._

object StockPriceDemo extends App {

  def urlFor(stockSymbol:String) = (
    "http://dev.markitondemand.com/MODApis/Api/v2/Quote?" +
    s"symbol=${stockSymbol}")

  def fetchStockPrice(stockSymbol:String):Future[BigDecimal] = {
    val url = urlFor(stockSymbol)
    val strResponse = Future { Source.fromURL(url).mkString }
    val xmlResponse = strResponse.map { s => XML.loadString(s) }
    val price = xmlResponse.map { r => BigDecimal((r \ "LastPrice").text) }
    price
  }

  println("Enter symbol at prompt.")
  while (true) {
    val symbol = readLine("> ")
    fetchStockPrice(symbol).onComplete { future =>
      println()
      future match {
        case Success(price) => println(s"$symbol: USD $price")
        case Failure(e) => println(s"Error fetching  $symbol: $e")
      }
      print("> ")
    }
  }

}

