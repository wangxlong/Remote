// scalastyle:off println
package spark.Sparksimple

import java.util.Random

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.SparkContext._

/**
  * Usage: GroupByTest [numMappers] [numKVPairs] [KeySize] [numReducers]
  */
//要assembly:assembly导出jar包提交到集群时，要把代码中sparkConf.setMaster("spark://master:7077")注释掉
//然后在spark-submit --master中指定，否则报错，原因不详
// val jarPath  , sc.addJar(jarPath)也要注释掉
object GroupByTest1 {
  
  val jarPath="C:\\Users\\whl\\workspace\\Sparksimple\\target\\Sparksimple-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("romote submit")
    sparkConf.setMaster("spark://master:7077")
    sparkConf.set("spark.executor.memory","1g")
    var numMappers = if (args.length > 0) args(0).toInt else 2
    var numKVPairs = if (args.length > 1) args(1).toInt else 100
    var valSize = if (args.length > 2) args(2).toInt else 100
    var numReducers = if (args.length > 3) args(3).toInt else numMappers

    val sc = new SparkContext(sparkConf)
    sc.addJar(jarPath)
    
    val pairs1 = sc.parallelize(0 until numMappers, numMappers).flatMap { p =>
      val ranGen = new Random
      var arr1 = new Array[(Int, Array[Byte])](numKVPairs)
      for (i <- 0 until numKVPairs) {
        val byteArr = new Array[Byte](valSize)
        ranGen.nextBytes(byteArr)
        arr1(i) = (ranGen.nextInt(Int.MaxValue), byteArr)
      }
      arr1
    }.cache()
    // Enforce that everything has been calculated and in cache
    pairs1.count()

    println(pairs1.groupByKey(numReducers).count())

    sc.stop()
  }
 
}
// scalastyle:on println
