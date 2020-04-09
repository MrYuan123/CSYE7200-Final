import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.collection.mutable.ArrayBuffer

class ReadData extends Serializable {
  def readFile(file: File): Array[(LocalDate, Double)] = {
    val formatter = DateTimeFormatter.ofPattern("d-MMM-yy")
    val lines = scala.io.Source.fromFile(file).getLines().toSeq
    lines.tail.map { line =>
      val cols = line.split(',')
      val date = LocalDate.parse(cols(0), formatter)
      val value = cols(4).toDouble
      (date, value)
    }.reverse.toArray
  }

  def readStocks(file: File): Array[(LocalDate, Double)] = {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val lines = scala.io.Source.fromFile(file).getLines().toSeq
    lines.tail.map { line =>
      val cols = line.split(',')
      val date = LocalDate.parse(cols(0), formatter)
      val value = cols(4).toDouble
      (date, value)
    }.reverse.toArray
  }

  def trimToRegion(history: Array[(LocalDate, Double)], start: LocalDate, end: LocalDate)
  : Array[(LocalDate, Double)] = {
    history.filter(x => (x._1.isEqual(start) || x._1.isAfter(start))).filter(x =>(x._1.isEqual(end) || x._1.isBefore(end)))
  }

  def filterByDate(history: Array[(LocalDate, Double)], dates: Array[LocalDate]):Array[(LocalDate, Double)] = {
    val filled = new ArrayBuffer[(LocalDate, Double)]()
    for(item <- history){
      if(dates.contains(item._1)){
        filled.+=(item)
      }
    }
    filled.toArray
  }

  def twoWeekReturns(history: Array[(LocalDate, Double)]): Array[Double] = {
    history.sliding(10).map { window =>
      val next = window.last._2
      val prev = window.head._2
      (next - prev) / prev
    }.toArray
  }

  def readStocksAndFactors(): (Seq[Array[Double]], Seq[Array[Double]]) = {
    val start = LocalDate.of(2018, 1, 8)
    val end = LocalDate.of(2020, 1, 10)

    val stocksDir = new File("/Users/leonardyuan/spark-2.4.5/test_folder/simple-project/data/stocks/")
    val files = stocksDir.listFiles()
    val allStocks = files.iterator.flatMap { file =>
      try {
        Some(readFile(file))
      } catch {
        case e: Exception => None
      }
    }
    val rawStocks = allStocks.filter(_.size >= 10)

    val factorsPrefix = "/Users/leonardyuan/spark-2.4.5/test_folder/simple-project/data/factors/"
    val rawFactors = Array("CrudeOil_Historical_Data.csv", "GLD_Historical_Data.csv", "TLT_Historical_Data.csv" ).
      map(x => new File(factorsPrefix + x)).
      map(readFile)

    val stocks = rawStocks.map(trimToRegion(_, start, end))

    val factors = rawFactors.map(trimToRegion(_, start, end))

    val stocksReturns = stocks.map(twoWeekReturns).toArray.toSeq
    val factorsReturns = factors.map(twoWeekReturns)
    (stocksReturns, factorsReturns)
  }
}