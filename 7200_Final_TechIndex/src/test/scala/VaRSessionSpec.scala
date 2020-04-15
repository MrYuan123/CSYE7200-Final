import org.apache.spark.sql.Dataset
import org.scalatest.{FlatSpec, Matchers}

class VaRSessionSpec extends FlatSpec with Matchers{
  behavior of "bootstrappedConfidenceInterval"
  it should "return the Confidence Interval " in {
    val vaRSession = new VaRSession()

    val trials: Dataset[Double] =

    val computeStatistic: Dataset[Double] => Double = vaRSession.fivePercentVaR _
    val numResamples: Int = 100
    val probability: Double = 0.5
    val result = vaRSession.bootstrappedConfidenceInterval(trials,computeStatistic,numResamples,probability)

    result shouldBe((-1754.9059171183192,-1751.0657037512767))

  }
}
