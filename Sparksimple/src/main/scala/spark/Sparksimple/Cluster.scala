package spark.Sparksimple

import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object Cluster {
  
 val jarPath="C:\\Users\\whl\\workspace\\Sparksimple\\target\\Sparksimple-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
 
 def main(args: Array[String]): Unit = {
      
    val sparkConf = new SparkConf().setAppName("cluster submit")
        sparkConf.setMaster("spark://master:7077")
        
    val sc = new SparkContext(sparkConf)
    // Load and parse the data
    val data = sc.textFile("hdfs://master:9000/spark/input/kmeans_data.txt")
    sc.addJar(jarPath)
    
    val parsedData = data.map(s => Vectors.dense(s.split(' ').map(_.toDouble))).cache()  
    // Cluster the data into two classes using KMeans
    val numClusters = 2
    val numIterations = 20
    val clusters = KMeans.train(parsedData, numClusters, numIterations)
    
    // Evaluate clustering by computing Within Set Sum of Squared Errors
    val WSSSE = clusters.computeCost(parsedData)
    println("Within Set Sum of Squared Errors = " + WSSSE)
    // Save and load model
    //默认是hdfs://master:9000/
    clusters.save(sc, "/home/sparkOutput/")
    val sameModel = KMeansModel.load(sc, "/home/sparkOutput/")

 }
}