import java.time.LocalDate
import java.io.File

import org.scalatest.FlatSpec

class ComputerSessionSpec  extends FlatSpec{

  behavior of "factorMatrix"
  it should "      " in {
    val readData = new ReadData()
    val factorsPrefix = "data/factors/"

    val factors1: Seq[Array[(LocalDate, Double)]] =
      Array("crudeoil.tsv", "us30yeartreasurybonds.tsv").
        map(x => new File(factorsPrefix + x)).
          map(readData.readFile)

    val factors2: Seq[Array[(LocalDate, Double)]] =
      Array("SNP.csv", "NDX.csv").
        map(x => new File(factorsPrefix + x)).
          map(readData.readFile)

    val factors: Seq[Array[Double]] = (factors1 ++ factors2). map(readData.trimToRegion(_, start, end)).

    val factorsReturns = factors.map(twoWeekReturns)
    val factor:Seq[Array[Double]] =







  }

}
