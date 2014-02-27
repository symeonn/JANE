package pl.byMarioUltimate;

import org.apache.log4j.Logger;

public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class);

	public static void main(String[] argv) {

//		test();

		
//		Double sigmoidalDerivative = NeuralNetwork.sigmoidalDerivative(-0.53);

		// NeuralNetService neuralNetService = new NeuralNetService();
		// neuralNetService.initRawNet();
		// neuralNetService.updateNet("one two three");
		// neuralNetService.updateNet("three four");
		// neuralNetService.updateNet("one five");
		//
		// System.out.println("koniec MAIN");

		// create working set, iterate it and sum and check error
		NeuralNetwork neuralNetwork = new NeuralNetwork(2, 4, 1);

		Double[][] input = new Double[4][2];
		Double[][] idealOutput = new Double[4][1];

		input[0][0] = 0.0;
		input[0][1] = 1.0;
		idealOutput[0][0] = 1.0;

		input[1][0] = 0.0;
		input[1][1] = 0.0;
		idealOutput[1][0] = 0.0;

		input[2][0] = 1.0;
		input[2][1] = 0.0;
		idealOutput[2][0] = 1.0;

		input[3][0] = 1.0;
		input[3][1] = 1.0;
		idealOutput[3][0] = 0.0;

		Double error = 0.0;
		int i = 0;
		try {
			do {
				error = 0.0;

				for(int j = 0; j < idealOutput.length; j++) {

					error += neuralNetwork.trainNet(input[j], idealOutput[j]);
					// if(j==2) break; // for only one training set
				}
				neuralNetwork.rPropWeights(0);
				// error is calculated by MSE
				error = error / idealOutput.length;
				if(i % 10 == 0) {
//					System.out.println(i + " e:" + error);
					// System.out.println(i + " o:"+cc.getNetworkOutput()[0]);
				}
				i++;
			}
			// if i hits max 
			// reset net or run genetic algorythm or smth
			while(i < 100000 && error > 0.002);

			System.out.println("FINAL " + i + " e:" + error);

		}
		catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @author Mariusz Lewandowski; byMario.pl
	 */
	private static void test() {
		short s = (short)5;
		
		System.out.println(s);
//		Integer i = new Integer(5);
//		
//		switch(i) {
//		case 1:
//System.out.println("1");
//
//		case 2:
//System.out.println("2");
//
//			
//		default:
//			System.out.println("default");
//		case 5:
//			System.out.println("5");
//		case 4:
//			System.out.println("4");
//			
//		}
		
		
		Byte i = -8;
		System.out.println(i);
		System.out.println(Integer.toBinaryString(i));
		
		System.out.println((i>>1));
		System.out.println(Integer.toBinaryString(i>>1));
		System.out.println((i<<1));
		System.out.println(Integer.toBinaryString(i<<1));
		System.out.println((i>>>1));
		System.out.println(Integer.toBinaryString(i>>>1));
		
		
		
//		double d = -3.5;
//		System.out.println(Math.ceil(d));
//		System.out.println(Math.floor(d));
//		System.out.println(Math.round(d));
		
		
		
		System.exit(0);
		
	}
}
