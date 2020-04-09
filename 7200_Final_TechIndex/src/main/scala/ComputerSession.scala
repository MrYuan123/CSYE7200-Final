import org.apache.commons.math3.distribution.MultivariateNormalDistribution
import org.apache.commons.math3.random.MersenneTwister
import org.apache.commons.math3.stat.correlation.Covariance
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression
import org.apache.spark.sql.{Dataset, SparkSession}

class ComputerSession(private val spark: SparkSession) extends Serializable{
  import spark.implicits._

  def factorMatrix(histories: Seq[Array[Double]]): Array[Array[Double]] = {
    val mat = new Array[Array[Double]](histories.head.length)
    for (i <- histories.head.indices) {
      mat(i) = histories.map(_(i)).toArray
    }
    mat
  }

  def featurize(factorReturns: Array[Double]): Array[Double] = {
    val squaredReturns = factorReturns.map(x => math.signum(x) * x * x)
    val squareRootedReturns = factorReturns.map(x => math.signum(x) * math.sqrt(math.abs(x)))
    squaredReturns ++ squareRootedReturns ++ factorReturns
  }

  def linearModel(instrument: Array[Double], factorMatrix: Array[Array[Double]])
  : OLSMultipleLinearRegression = {
    val regression = new OLSMultipleLinearRegression()
    regression.newSampleData(instrument, factorMatrix)
    regression
  }

  def computeFactorWeights(
                            stocksReturns: Seq[Array[Double]],
                            factorFeatures: Array[Array[Double]]): Array[Array[Double]] = {
    stocksReturns.map(linearModel(_, factorFeatures)).map(_.estimateRegressionParameters()).toArray
  }

  def trialReturns(
                    seed: Long,
                    numTrials: Int,
                    instruments: Seq[Array[Double]],
                    factorMeans: Array[Double],
                    factorCovariances: Array[Array[Double]]): Seq[Double] = {
    val rand = new MersenneTwister(seed)
    val multivariateNormal = new MultivariateNormalDistribution(rand, factorMeans,
      factorCovariances)

    val trialReturns = new Array[Double](numTrials)
    for (i <- 0 until numTrials) {
      val trialFactorReturns = multivariateNormal.sample()
      val trialFeatures = featurize(trialFactorReturns)
      trialReturns(i) = trialReturn(trialFeatures, instruments)
    }
    trialReturns
  }

  /**
   * Calculate the full return of the portfolio under particular trial conditions.
   */
  def trialReturn(trial: Array[Double], instruments: Seq[Array[Double]]): Double = {
    var totalReturn = 0.0
    for (instrument <- instruments) {
      totalReturn += instrumentTrialReturn(instrument, trial)
    }
    totalReturn / instruments.size
  }

  /**
   * Calculate the return of a particular instrument under particular trial conditions.
   */
  def instrumentTrialReturn(instrument: Array[Double], trial: Array[Double]): Double = {
    var instrumentTrialReturn = instrument(0)
    var i = 0
    while (i < trial.length) {
      instrumentTrialReturn += trial(i) * instrument(i+1)
      i += 1
    }
    instrumentTrialReturn
  }

  def computeTrialReturns(
                           stocksReturns: Seq[Array[Double]],
                           factorsReturns: Seq[Array[Double]],
                           baseSeed: Long,
                           numTrials: Int,
                           parallelism: Int): Dataset[Double] = {
    val factorMat = factorMatrix(factorsReturns)
    val factorCov = new Covariance(factorMat).getCovarianceMatrix().getData()
    val factorMeans = factorsReturns.map(factor => factor.sum / factor.size).toArray
    val factorFeatures = factorMat.map(featurize)
    val factorWeights = computeFactorWeights(stocksReturns, factorFeatures)

    val seeds = (baseSeed until baseSeed + parallelism)
    spark.createDataset(seeds).repartition(parallelism).flatMap(trialReturns(_, numTrials / parallelism, factorWeights, factorMeans, factorCov))
  }
}
