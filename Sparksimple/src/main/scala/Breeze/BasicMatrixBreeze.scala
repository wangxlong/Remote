package Breeze

import breeze.linalg.DenseMatrix



object BasicMatrixBreeze {
  
  def main(args: Array[String]): Unit = {
    
    val matrix1=new DenseMatrix(3,5,Array(1.0,2.0,4.0,0,0,0,0,3.0,0,7.0,4.0,6.0,0,5.0,7.0))
    val matrix2=new DenseMatrix(5,2,Array(-0.4485,0.13302,-0.12523,0.21651,-0.84765,-0.28424,-0.05621,0.76363,-0.56529,-0.1156))
    println(matrix1)
    println(matrix2)
    println(matrix1*matrix2)
  }
}