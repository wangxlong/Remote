package WHL.ANN_DL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * 自编码器额实现
 * @author whl
 *
 */
public class ANN {
	
	private static int inputDim = 10;				//输入数据维数
	private int hiddenDim = 50;			//中间数据维数
	private int outputDim = inputDim;			//输出数据维数
	private double learningRate = 0.05;		//学习速率-----------随机梯度下降
	
	private double[][] weightInputHidden; //输入层到隐层的权重矩阵
	private double[][] weightHiddenOutput; //隐层到输出层的权重矩阵
	private double[] hiddenThreshold; //输入层到隐层的偏置
	private double[] outputThreshold; //隐层到输出层的偏置
	
	private double[] hiddenOutput; //隐层的输出
	private double[] outputOutput; //输出层的输出
	
	private double[] desiredOutput; //输出层的期望输出
	private double[] inputValues; //输入
	
	private double[] hiddenOutputError;//隐藏层输出的误差
	
	/**
	 *constructor 
	 */
	public ANN(){
		weightInputHidden = new double[inputDim][hiddenDim];
		weightHiddenOutput = new double[hiddenDim][outputDim];
		hiddenThreshold = new double[hiddenDim];
		outputThreshold = new double[outputDim];
		
		inputValues = new double[inputDim];
		desiredOutput = new double[outputDim];
		hiddenOutput = new double[hiddenDim];
		outputOutput = new double[outputDim];
	    hiddenOutputError=new double[hiddenDim];
	}
	
	/**
	 * 初始化参数，随机赋-0.5~0.5之间的值
	 */
	public void initialize(){
		Random rand = new Random();
		
		for (int i=0; i<inputDim; i++)
			for (int j=0; j<hiddenDim; j++){
				weightInputHidden[i][j] = rand.nextDouble() - 0.5;
			}
		
		for (int i=0; i<hiddenDim; i++)
			for (int j=0; j<outputDim; j++){
				weightHiddenOutput[i][j] = rand.nextDouble() - 0.5;
			}
		
		for (int i=0; i<hiddenDim; i++){
			hiddenThreshold[i] = rand.nextDouble() - 0.5;
		}
		
		for (int i=0; i<outputDim; i++){
			outputThreshold[i] = rand.nextDouble() - 0.5;
		}
	}
	
	/**
	 * sigmod
	 */
	public double sigmoidFunc(double x){
		return 1 / (1 + Math.exp(-x));
	}
	/**
	 * 计算sigmoid导数
	 */
	public double sigmoiddiff(double x){
		
		return Math.exp(-x)/Math.pow(1+Math.exp(-x),2);
	}
	
	/**
	 * 设置成员变量值
	 */
	public void setInputDim(int inputDim){
		this.inputDim = inputDim;
	}
	
	public void setHiddenDim(int hiddenDim){
		this.hiddenDim = hiddenDim;
	}
	
	public void setOutputDim(int outputDim){
		this.outputDim = outputDim;
	}
	
	public void setLearningRate(double learningRate){
		this.learningRate = learningRate;
	}
	
	/**
	 * 返回成员变量
	 */
	public double getLearningRate(){
		return learningRate;
	}
	
	public int getInputDim(){
		return inputDim;
	}
	
	public int getHiddenDim(){
		return hiddenDim;
	}
	
	public int getOutputDim(){
		return outputDim;
	}
	
	/**
	 * 正向计算一次
	 */
	double[] hiddenInput = new double[hiddenDim];
	public void calcOutput(){		
		//计算隐藏层输出
		for (int i=0; i<hiddenDim; i++){
			hiddenInput[i] = 0;
			for (int j=0; j<inputDim; j++){
				hiddenInput[i] += inputValues[j] * weightInputHidden[j][i];
			}
			//减去偏置
			hiddenInput[i] = hiddenInput[i] - hiddenThreshold[i];
			hiddenOutput[i] = sigmoidFunc(hiddenInput[i]);
		}
		
		//计算输出层输出
		double[] outputInput = new double[outputDim];
		for (int i=0; i<outputDim; i++){
			outputInput[i] = 0;
			for (int j=0; j<hiddenDim; j++){
				outputInput[i] += hiddenOutput[j] * weightHiddenOutput[j][i];
			}
			//减去偏置
			outputInput[i] += outputInput[i] - outputThreshold[i];
			outputOutput[i] = sigmoidFunc(outputInput[i]);
		}
	}
	
	/**
	 * 反向计算隐层的误差
	 */
	public void ErrorCalculate(){
		double tmpError;
		for(int i=0;i<hiddenDim;i++){
			tmpError=0;
			for(int j=0;j<outputDim;j++){
				tmpError+=weightHiddenOutput[i][j]*(desiredOutput[j] - outputOutput[j])*sigmoiddiff(hiddenInput[i]);
			}
			hiddenOutputError[i]=tmpError;
			System.out.println("第"+i+"隐层输出误差为："+hiddenOutputError[i]);
		}
		
	}
	
	/**
	 * 反向权值更新
     */
	public void paramUpdate(){
		
		//更新隐藏层到输出层之间的权值
		for (int i=0; i<hiddenDim; i++)
			for (int j=0; j<outputDim; j++){
				weightHiddenOutput[i][j] -= learningRate * (desiredOutput[j] - outputOutput[j])
						 * hiddenOutput[i]; //隐层的第i个神经元与输出层的第j个之间的权重导数等于隐层的第i个神经元的输出乘以输出层的第j个神经元的误差
			}
		
		//更新输出层偏置
		for (int i=0; i<outputDim; i++){
			outputThreshold[i] -= learningRate * (desiredOutput[i] - outputOutput[i]);
			//输出层的第i个神经元的偏置等于输出层的第i个神经元的误差
		}
		
		//更新输入层到隐藏层权值和隐层偏置
		for (int i=0; i<inputDim; i++)
			for (int j=0; j<hiddenDim; j++){
				weightInputHidden[i][j] -= learningRate * hiddenOutputError[j]*inputValues[i];
			}
		
		//更新偏置
		for (int i=0; i<hiddenDim; i++){		
			hiddenThreshold[i] -= learningRate * hiddenOutputError[i];
		}
	}
	
	/**
	 * 训练
	 * @param sIn：训练数据路径
	 */
	
	public void train(String sIn){
		initialize();
		
		BufferedReader br;
		String line;
		StringTokenizer st;
		double temp;
		int n = 0;
		try{
			br = new BufferedReader(new FileReader(new File(sIn)));
			while ((line = br.readLine()) != null){
				int i = 0;
				st = new StringTokenizer(line);
				while (st.hasMoreTokens()){
					temp = Double.parseDouble(st.nextToken());
					inputValues[i] = temp;
					desiredOutput[i] = temp;		//令输入=输出实现AE(autoencoder)算法
					i++;
				}
				calcOutput();
				ErrorCalculate();
				paramUpdate();
				n++;
				System.out.println("第"+n+"次迭代结束.");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试：将测试数据每一行输入计算其降维后的值输出
	 * @param sIn：测试数据路径
	 * @param sOut：测试结果输出路径
	 */
	public void test(String sIn, String sOut){
		BufferedReader psIn;
		PrintStream psOut;
		try{
			psIn = new BufferedReader(new FileReader(new File(sIn)));
		    psOut = new PrintStream(new FileOutputStream(sOut));
		    
		    String line;
		    int n = 0;
		    while ((line = psIn.readLine()) != null){
		    	StringTokenizer st = new StringTokenizer(line);
		    	for (int i=0; i<inputDim; i++){
		    		inputValues[i] = Double.parseDouble(st.nextToken());
		    	}
		    	
		    	calcOutput();
		    	n++;
		    	System.out.println("第"+n+"组数据计算结束。");
		    	
		    	for (int i=0; i<hiddenDim; i++){
		    		psOut.print(hiddenOutput[i]);
		    	}
		    	psOut.print("\n");
		    }
		    psIn.close();
		    psOut.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		//造数据
		GeneTrainTestData.start(inputDim, 100, 10); //第一个参数表示输入的维度，第二个参数表示制造训练集的个数，第三个参数表示测试集的个数
		System.out.println("gen data done.");
		//ANN
		ANN myANN = new ANN();
		String sInTrain = "TrainData.txt";
		String sInTest = "TestData.txt";
		String sOutTest = "TestOutput.txt";
		myANN.train(sInTrain);
		System.out.println("training done.");
		myANN.test(sInTest, sOutTest);
		System.out.println("testing done.");
	}
}
