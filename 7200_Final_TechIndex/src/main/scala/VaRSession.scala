import org.apache.spark.sql.Dataset

class VaRSession extends Serializable {
  def fivePercentVaR(trials: Dataset[Double]): Double = {
    val quantiles = trials.stat.approxQuantile("value", Array(0.05), 0.0)
    quantiles.head
  }

  def fivePercentCVaR(trials: Dataset[Double]): Double = {
    val topLosses = trials.orderBy("value").limit(math.max(trials.count().toInt / 20, 1))
    topLosses.agg("value" -> "avg").first()(0).asInstanceOf[Double]
  }

  def bootstrappedConfidenceInterval(
                                      trials: Dataset[Double],
                                      computeStatistic: Dataset[Double] => Double,
                                      numResamples: Int,
                                      probability: Double): (Double, Double) = {
    val stats = (0 until numResamples).map { i =>
      val resample = trials.sample(true, 1.0)
      computeStatistic(resample)
    }.sorted
    val lowerIndex = (numResamples * probability / 2 - 1).toInt
    val upperIndex = math.ceil(numResamples * (1 - probability / 2)).toInt
    (stats(lowerIndex), stats(upperIndex))
  }
}
