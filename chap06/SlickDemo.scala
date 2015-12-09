import java.sql.Date

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.meta._

object SlickDemo {


  val db = Database.forURL(
    "jdbc:mysql://127.0.0.1:3306/test", 
    driver="com.mysql.jdbc.Driver"
  )

  def createTable:Unit = {
    db withSession {
      implicit session =>
        if(! MTable.getTables("transactions").list.isEmpty) {
          Tables.transactions.ddl.drop
        }
        Tables.transactions.ddl.create
    }
  }

  def insertFecData:Unit = {
    val fecData = FECData.loadOhio
    val transactions = fecData.transactions

    db withSession {
      implicit session => 

        val transactionBatches = transactions.grouped(100000)

        for (batch <- transactionBatches) {
          Tables.transactions ++= batch.toList
        }
    }
  }

  def queryData:List[Transaction] = {
    db withSession {
      implicit session => Tables.transactions.take(5).list
    }
  }

  def queryTotalDonations:Map[String, Long] = {
    db withSession {
      implicit session => 
        val grouped = Tables.transactions.groupBy(_.candidate)
        val aggregated = grouped.map { 
          case (candidate, group) => (candidate -> group.map(_.amount).sum)
        }
        aggregated.list.toMap.mapValues { _.getOrElse(0L) }
    }
  }

  def main(args:Array[String]) {
    createTable
    insertFecData
    queryData.foreach { println }
    queryTotalDonations.foreach { println }
  }




}
