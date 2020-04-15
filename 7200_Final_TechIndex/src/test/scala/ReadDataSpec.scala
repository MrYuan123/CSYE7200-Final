import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.scalatest.{FlatSpec, Matchers}

class ReadDataSpec extends FlatSpec with Matchers {

  behavior of "readFile"
  it should "read the file get the date and price" in {

    val appleFile =  new File("data/stocks/AAPL.csv")
    val appleData = new ReadData().readFile(appleFile)
    appleData(0)._1.getClass.getSimpleName shouldBe("LocalDate")
    appleData(0)._2.getClass.getSimpleName shouldBe("double")

  }


  behavior of "trimToRegion"
  it should "get the data in the time we want" in {
    val readData = new ReadData()
    val appleFilename = "data/stocks/AAPL.csv"
    val appleFile  = new File(appleFilename)
    val appleData = readData.readFile(appleFile)

    val formatter = DateTimeFormatter.ofPattern("d-MMM-yy");
    val dateStrN1 = "1-Jan-18";
    val dateStrN2 = "5-Apr-18";
    val dateN1 = LocalDate.parse(dateStrN1, formatter);
    val dateN2 = LocalDate.parse(dateStrN2, formatter)
    val trim = readData.trimToRegion(appleData, dateN1,dateN2)

    dateN1.isBefore( trim(0)._1) shouldBe(true)
    dateN2.isAfter(  trim(60)._1) shouldBe(true)

  }

}


