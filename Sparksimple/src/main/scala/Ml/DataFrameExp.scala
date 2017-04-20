package Ml

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.DataFrame

object DataFrameExp {
   val jarPath="C:\\Users\\whl\\workspace\\Sparksimple\\target\\Sparksimple-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
  
  def main(args: Array[String]): Unit = {
    val path="hdfs://master:9000/spark/data/sample_libsvm_data.txt"
    val sparkconf=new SparkConf()
    sparkconf.setAppName("dataFrameName")
    sparkconf.setMaster("spark://master:7077")
    val sc=new SparkContext(sparkconf)
    sc.addJar(jarPath)
    val sqlcontext=new SQLContext(sc)
    val df:DataFrame=sqlcontext.read.format("libsvm").load(path).cache()
    df.printSchema()
    /*
     * root
 			|-- label: double (nullable = false)
 			|-- features: vector (nullable = false)
     */
    val labelsummary=df.describe("label")
    labelsummary.show()
    df.show(1)
    
    sc.stop()
  }
}