import org.apache.commons.math3.distribution.ChiSquaredDistribution

class AlgoEvaluate extends Serializable{
  def countFailures(stocksReturns: Seq[Array[Double]], valueAtRisk: Double): Int = {
    var failures = 0
    for (i <- stocksReturns.head.indices) {
      val loss = stocksReturns.map(_(i)).sum
      if (loss < valueAtRisk) {
        failures += 1
      }
    }
    failures
  }

  def kupiecTestStatistic(total: Int, failures: Int, confidenceLevel: Double): Double = {
    val failureRatio = failures.toDouble / total
    val logNumer = (total - failures) * math.log1p(-confidenceLevel) +
      failures * math.log(confidenceLevel)
    val logDenom = (total - failures) * math.log1p(-failureRatio) +
      failures * math.log(failureRatio)
    -2 * (logNumer - logDenom)
  }

  def kupiecTestPValue(
                        stocksReturns: Seq[Array[Double]],
                        valueAtRisk: Double,
                        confidenceLevel: Double): Double = {
    val failures = countFailures(stocksReturns, valueAtRisk)
    val total = stocksReturns.head.length
    val testStatistic = kupiecTestStatistic(total, failures, confidenceLevel)
    1 - new ChiSquaredDistribution(1.0).cumulativeProbability(testStatistic)
  }
}
