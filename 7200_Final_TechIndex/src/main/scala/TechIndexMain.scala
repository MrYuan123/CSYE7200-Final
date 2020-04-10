import java.io.FileWriter

import org.apache.spark.sql.SparkSession

object TechIndexMain {
  def main(args: Array[String]): Unit ={
    val spark = SparkSession.builder().getOrCreate()
    val instance = new ReadData()
    val (stocksReturns, factorsReturns) = instance.readStocksAndFactors()
    println("stockReturns: " + stocksReturns(0).size)
    println("factorReturns: " + factorsReturns(0).size)
    println("<==========================>")

    for(item <- stocksReturns){
      val newstock = Seq(item)
      val numTrials = 100 //10000000
      val parallelism =  10 //1000
      val baseSeed = 1001L
      val computerInstance = new ComputerSession(spark)
      val trials = computerInstance.computeTrialReturns(newstock, factorsReturns, baseSeed, numTrials,
        parallelism) //stocksReturns
      trials.cache()

      val varinstance = new VaRSession()
      val valueAtRisk = varinstance.fivePercentVaR(trials)
      val conditionalValueAtRisk = varinstance.fivePercentCVaR(trials)
      println("VaR 5%: " + valueAtRisk)
      println("CVaR 5%: " + conditionalValueAtRisk)
      val varConfidenceInterval = varinstance.bootstrappedConfidenceInterval(trials, varinstance.fivePercentVaR, 100, .05)
      val cvarConfidenceInterval = varinstance.bootstrappedConfidenceInterval(trials, varinstance.fivePercentCVaR, 100, .05)
      println("VaR confidence interval: " + varConfidenceInterval)
      println("CVaR confidence interval: " + cvarConfidenceInterval)
      println("<=============>")

      val eval = new AlgoEvaluate()
      val evalresult = eval.kupiecTestPValue(newstock, valueAtRisk, 0.05)
      println("Kupiec test p-value: " + evalresult)

      val fw = new FileWriter("/Users/leonardyuan/codebase/CSYE7200-Final/7200_Final_TechIndex/result.txt", true)
      fw.write(valueAtRisk + "," + conditionalValueAtRisk + "," + varConfidenceInterval._1+ "," +
        varConfidenceInterval._2 + "," + cvarConfidenceInterval._1 + "," + cvarConfidenceInterval._2 + "," + evalresult + "\n")
      fw.close()
    }

  }
}
