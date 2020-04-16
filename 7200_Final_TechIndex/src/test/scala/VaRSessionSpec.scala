import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.scalatest.{FlatSpec, Matchers}


class VaRSessionSpec extends FlatSpec with Matchers{

  val conf = new SparkConf().setAppName("Spark Scala WordCount Example").setMaster("local[1]")
  val spark = SparkSession.builder().config(conf).appName("CsvExample").master("local").getOrCreate()
  import spark.implicits._

  behavior of "bootstrappedConfidenceInterval"
  it should "return the confidence Interval according to paramter" in {


    val vaRSession = new VaRSession()
    val seq:Seq[Int] =  Seq.range(1,10000)
    val seq_double = seq.map(x=>x.toDouble)
    val trials: Dataset[Double] = seq_double.toDS()
    val computeStatistic: Dataset[Double] => Double = vaRSession.fivePercentVaR _
    val numResamples: Int = 100
    val probability: Double = 0.5
    val result = vaRSession.bootstrappedConfidenceInterval(trials,computeStatistic,numResamples,probability)

    result.getClass.getSimpleName shouldBe((1.0,1.0).getClass.getSimpleName)

  }
}
