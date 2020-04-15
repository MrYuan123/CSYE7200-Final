import org.scalatest.FlatSpec

class ComputerSessionSpec  extends FlatSpec{

  behavior of "factorMatrix"
  it should "      " in {
    val array = Array.range(1,10)
    val double_array = array.map(x=>x.toDouble)
    val seq_array:Seq[Array[Double]] =  Seq(double_array)
    val seq2 = seq_array ++ seq_array

  }

}
