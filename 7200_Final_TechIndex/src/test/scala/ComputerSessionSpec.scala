import java.time.LocalDate
import java.io.File
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.scalatest.{FlatSpec, Matchers}



import org.scalatest.FlatSpec

class ComputerSessionSpec  extends FlatSpec with Matchers{
  val conf = new SparkConf().setAppName("Spark Scala WordCount Example").setMaster("local[1]")
  val spark = SparkSession.builder().config(conf).appName("CsvExample").master("local").getOrCreate()
  import spark.implicits._
  val cs =  new ComputerSession(spark)


  "factorMatrix" should "transfer the Seq[Array[Double]] into the Array[Array[Double]]" in {

    val array1 = Array.range(1,10).map(x=>x.toDouble)
    val array2 = Array.range(11,20).map(x=>x.toDouble)
    val history:Seq[Array[Double]] = Seq(array1,array2)
    val expected =
      Array(Array(1.0, 11.0), Array(2.0, 12.0), Array(3.0, 13.0),
      Array(4.0, 14.0), Array(5.0, 15.0), Array(6.0, 16.0),
      Array(7.0, 17.0), Array(8.0, 18.0), Array(9.0, 19.0))

    val result = cs.factorMatrix(history)

    result shouldBe(expected)

  }

  "featurize" should "return the squared,squared rooted and itself" in{

    val array = Array.range(1,10).map(x=>x.toDouble)
    val expected = Array(1.0, 4.0, 9.0, 16.0, 25.0, 36.0, 49.0, 64.0, 81.0,
      1.0, 1.4142135623730951, 1.7320508075688772, 2.0,
      2.23606797749979, 2.449489742783178,
      2.6457513110645907, 2.8284271247461903, 3.0,
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0)
    val result = cs.featurize(array)

    result shouldBe(expected)

  }




}