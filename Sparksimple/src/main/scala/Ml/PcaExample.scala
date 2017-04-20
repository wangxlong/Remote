package Ml

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println

// $example on$
import org.apache.spark.ml.feature.PCA
import org.apache.spark.mllib.linalg.Vectors
// $example off$
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

object PCAExample {
  
  val jarPath="C:\\Users\\whl\\workspace\\Sparksimple\\target\\Sparksimple-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("PCAExample")
    conf.setMaster("spark://master:7077")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    sc.addJar(jarPath)
    // $example on$
    val data = Array(
      Vectors.sparse(5, Seq((1, 1.0), (3, 7.0))),//通过工厂方法创建
      Vectors.dense(2.0, 0.0, 3.0, 4.0, 5.0),
      Vectors.dense(4.0, 0.0, 0.0, 6.0, 7.0)
    )
    val df = sqlContext.createDataFrame(data.map(Tuple1.apply)).toDF("features")//将Schema设置为features
    println("df-show")
    df.show()
    val pca = new PCA()
      .setInputCol("features")
      .setOutputCol("pcaFeatures")
      .setK(2)
      .fit(df)  //return PCAModel
    println(pca.pc)//输出主成分
    println("pca-show")
    val pcaDF = pca.transform(df)//计算主成分变换后的集合
    println("pcaDf")
    pcaDF.show()
    val result = pcaDF.select("pcaFeatures")
    result.take(1).foreach(println _)
    // $example off$
    sc.stop()
  }
}
// scalastyle:on println
