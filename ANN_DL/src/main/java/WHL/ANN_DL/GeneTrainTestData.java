package WHL.ANN_DL;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

public class GeneTrainTestData {
	static public void start(int inputDim, int nTrain, int nTest) {
		String sInTrain = "TrainData.txt";
		String sInTest = "TestData.txt";
		Random rand = new Random();
		PrintStream psTrain, psTest;

		try {
			psTrain = new PrintStream(new FileOutputStream(sInTrain));
			for (int i = 0; i < nTrain; i++) {
				for (int j = 0; j < inputDim; j++) {
					psTrain.print(rand.nextGaussian());		//高斯随机分布采样得到的随机数
					if (j != inputDim-1)
						psTrain.print(" ");
				}
				psTrain.print("\n");
			}
			psTrain.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			psTest = new PrintStream(new FileOutputStream(sInTest));
			for (int i = 0; i < nTest; i++) {
				for (int j = 0; j < inputDim; j++) {
					psTest.print(rand.nextGaussian());
					if (j != inputDim-1)
						psTest.print(" ");
				}
				psTest.print("\n");
			}
			psTest.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
