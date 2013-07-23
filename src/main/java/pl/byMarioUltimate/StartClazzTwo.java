package pl.byMarioUltimate;

import org.springframework.stereotype.Service;

/**
 * 
 * @author Mariusz Lewandowski; byMario
 */
@Service
public class StartClazzTwo implements Runnable {

	// List<Thread> tl;
	// @PostConstruct
	// public void onInit() {
	// tl = new ArrayList<Thread>();
	// System.out.println("Hello from a thread! " +
	// Thread.currentThread().getName());
	// Thread t;
	// // for(int i = 0; i < 2; i++) {
	//
	// // (new Thread(this)).start();
	// t= new Thread(new HelloRunnable());
	//
	// tl.add(t);
	//
	//
	// // }
	//
	// System.out.println(tl.size());
	// for(Thread th : tl) {
	// th.start();
	// }
	//
	//
	//
	// }
	//
	// public class HelloRunnable implements Runnable {
	//
	// public void run() {
	// try {
	// Thread.sleep(1000);
	// System.out.println("Hello from a thread! " +
	// Thread.currentThread().getName());
	// }
	// catch(InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	//
	// }

	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

		// try {
		// while(processNet()) {
		//
		// //
		// System.out.println("*******************************************************");
		// Thread.sleep(1000);
		// // processNet();
		//
		// //
		// System.out.println("*******************************************************");
		// // System.out.println("Hello from a thread! T " +
		// // Thread.currentThread().getName());
		// // System.out.println("-------");
		// }
		// }
		// catch(InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		trainNet();
	}

	// for XOR

	// I I | O
	// ----+--
	// 0 0 | 0
	// 0 1 | 1
	// 1 0 | 1
	// 1 1 | 0

	static int inputNeuronsCount = 2;
	static int hiddenNeuronsCount = 2;
	static int outputNeuronsCount = 1;

	static Double momentum = 0.1;
	static Double E = 0.9; // learning rate

	static Double posStep = 1.2;
	static Double negStep = 0.5;
	static Double updateValueMax = 50d;
	static Double updateValueMin = 0.0001;

	static Double errorConst = 0.002;

	// array: [node number][inputValues*weights sum, outputValue, nodeDelta]
	public static Double[][] inputNodes = new Double[inputNeuronsCount + 1][3]; // properties
																				// of
																				// input
																				// layer
																				// nodes
	public static Double[][] hiddenNodes = new Double[hiddenNeuronsCount + 1][3]; // properties
																					// of
																					// hidden
																					// layer
																					// nodes
	public static Double[][] outputNodes = new Double[outputNeuronsCount][3]; // properties
																				// of
																				// output
																				// layer
																				// nodes

	public static Double[] idealOutput = new Double[outputNodes.length];

	// array:
	// [2nd layer node number]
	// [1st layer node number - input from]
	// [weight, gradient, weightDelta, updateValue]
	static Double[][][] hiddenWeights = new Double[hiddenNodes.length][inputNodes.length][4]; // connections
																								// properties
																								// from
																								// input
																								// to
																								// hidden
																								// layer
	static Double[][][] outputWeights = new Double[outputNodes.length][hiddenNodes.length][4]; // connections
																								// properties
																								// from
																								// hidden
																								// to
																								// output
																								// layer

	static void setInputValues(Double inputOne, Double inputTwo) {
		// +1 - bias
		// System.out.println("INPUT");
		if(inputOne == null || inputTwo == null) {
			for(int i = 0; i < inputNodes.length - 1; i++) {
				// inputNodes[i][0] = null;
				inputNodes[i][1] = getRandomBit().doubleValue();
			}
			// inputNodes[i][1] = 1d;
			// System.out.println("INPUT neuron " + (i + 1) + " out value: " +
			// inputNodes[i][1]);
		}
		else {

			inputNodes[0][1] = inputOne;
			inputNodes[1][1] = inputTwo;
		}
		// bias always has value = 1
		// inputNodes[inputNeuronsCount][0] = 1d;
		inputNodes[inputNeuronsCount][1] = 1d;
		// System.out.println("BIAS " + inputNodes[inputNeuronsCount][1]);

		idealOutput[0] = Double.parseDouble(String.valueOf(inputNodes[0][1].intValue() ^ inputNodes[1][1].intValue()));
		// System.out.println("IDEAL: " + idealOutput[0]);
	}

	public boolean trainNet() {

		// init();
		setInputValues(null, null);

		Integer iteration = 0;

		Double output;
		// System.out.println("************************");
		// System.out.println("backProp");
		// System.out.println("************************");
		Double error = 0.0;
		do {
			iteration++;
			// System.out.println("Iteration " + iteration);
//			output = runNet();
			error += checkErrorGradientMomentum();
//			if(iteration > 3) break; //do not delete this It might be usefull later for for not quite perfect learning
		}
		while(error >= errorConst || iteration > 1000);

		// System.out.println("Iterations " + iteration);
		// System.out.println(outputNodes[0][1].toString());

		System.out.println("iO: " + idealOutput[0] + " itT: " + iteration);
		// System.out.print("iO: " + idealOutput[0] + " itT: " + iteration);

		// System.out.println("RESULT: " + outputNodes[0][1].toString());

		// for(int i = 0; i < inputNodes.length - 1; i++) {
		// System.out.println("neuron " + (i + 1) + " out value: " +
		// inputNodes[i][1]);
		// }
		return true;

	}

	public static Double runNet() {

		// setInputValues();

		// System.out.println("iterate");
		processLayer(inputNodes, hiddenWeights, hiddenNodes);
		hiddenNodes[hiddenNeuronsCount][1] = 1d;

		// System.out.println("OUTPUT");
		processLayer(hiddenNodes, outputWeights, outputNodes);
		// System.out.println("OUT: " + outputNodes[0][1]);

		// for(Double outputValue : outputNodes[0][1]) {
		// System.out.println(outputNodes[0][1].toString());
		// }

		return outputNodes[0][1];
	}

	public static void processLayer(Double[][] layerFrom, Double[][][] connectionProp, Double[][] layerTo) {
		// public static Double[] processLayer(Double[][] weights, Double[]
		// values) {

		// Double[][] tempOutputValues = new Double[weights.length];

		for(int i = 0; i < layerTo.length; i++) {

			Double sum = 0d;

			for(int j = 0; j < layerFrom.length; j++) {

				if(connectionProp[i][j][0] == null) {
					// initial weight setup
					connectionProp[i][j][0] = getRandomDouble();
				}
				//
				// if(connectionProp[i][j][0].isNaN()){
				// System.out.println();
				// }
				sum += connectionProp[i][j][0] * layerFrom[j][1];
			}

			layerTo[i][0] = sum;
			layerTo[i][1] = sigmoidal(sum);
			if(layerTo[i][1].isNaN()) {
				// System.out.println("neuron " + (i + 1) + " out value: " +
				// sum);
			}
		}

		return;

	}

	public boolean checkErrorGradientCalculation() {

		int idealOutputInt = inputNodes[0][1].intValue() ^ inputNodes[1][1].intValue();

		Double idealOutput = Double.parseDouble(String.valueOf(idealOutputInt));

		Double error = outputNodes[0][1] - idealOutput;
		System.out.println("ERR: " + error);
		if(Math.abs(error) > 0.002) {

			gradientCalculation();
			return true;
		}
		else {
			return false;
		}
	}

	public Double checkErrorGradientMomentum() {

		// Double idealOutput =
		// Double.parseDouble(String.valueOf(idealOutputInt));
		Double temp = 0d;

		// for(int i = 0; i < outputNodes.length; i++) {
		// temp += Math.pow(outputNodes[i][1] - idealOutput[i], 2);
		// }
		// System.out.println("checking gradient");

		// Double error = temp/outputNodes.length;
//		Double error = outputNodes[0][1] - idealOutput[0];
		// System.out.println("ERR: " + error);

//		if(Math.abs(error) > errorConst) {
			// System.out.println("ERR: "+error);
		return	gradientCalculation();

			// gradientMomentum(error);
//			return true;
//		}
//		else {
//			return false;
//		}
	}

	/**
	 * 
	 * @author Mariusz Lewandowski; byMario
	 */
	private double gradientCalculation() {

		Double error = 0.0;
		// nodeDelta for output
		for(Double[] outputNode : outputNodes) {
			
			error += Math.pow(outputNode[1] - idealOutput[0], 2);
			
			if(outputNode[0] != null) {
				outputNode[2] = (outputNode[1] - idealOutput[0]) * (sigmoidalDerivative(outputNode[0]));
				// if(outputNode[2]==null){
				// System.out.println(outputNode[2]);
				// }
			}
		}

		// nodeDelta for hidden
		// for(Double[] hiddenNode : hiddenNodes) {
		for(int i = 0; i < hiddenNodes.length; i++) {

			if(hiddenNodes[i][0] != null) {

				// sum of weights between this H and O's
				Double weightsTimesNodeDeltaSum = 0d;
				for(int j = 0; j < outputNodes.length; j++) {

					if(outputNodes[j][2] != null) {
						weightsTimesNodeDeltaSum += outputWeights[j][i][0] * outputNodes[j][2];
					}
				}

				hiddenNodes[i][2] = sigmoidalDerivative(hiddenNodes[i][0]) * weightsTimesNodeDeltaSum;
			}
		}

		// gradient and weight
		for(int i = 0; i < outputWeights.length; i++) {

			for(int j = 0; j < hiddenNodes.length; j++) {

				Double prevGradient = outputWeights[i][j][1];

				if(prevGradient == null) {
					prevGradient = 0d;
				}

				outputWeights[i][j][1] = hiddenNodes[j][1] * outputNodes[i][2];
				// System.out.println(outputWeights[i][j][1]);

				if(outputWeights[i][j][2] == null) {
					outputWeights[i][j][2] = 0d;
				}
				if(outputWeights[i][j][3] == null) {
					outputWeights[i][j][3] = 0.05;
				}

				Double tempOldWeightDelta = outputWeights[i][j][2];

				// new weightDelta
				// System.out.println("GR "+outputWeights[i][j][1]);

				// gradient descent
				// outputWeights[i][j][2] = (-E * outputWeights[i][j][1]);

				// momentum
				// outputWeights[i][j][2] = (-E * outputWeights[i][j][1]) +
				// momentum * tempOldWeightDelta;
				// outputWeights[i][j][0] += outputWeights[i][j][2];

				// RPROP
				rProp(prevGradient, outputWeights[i][j]);

				// if((prevGradient * outputWeights[i][j][1]) > 0) {
				//
				// outputWeights[i][j][3] = posStep * outputWeights[i][j][3];
				//
				// if(outputWeights[i][j][3] > updateValueMax) {
				// outputWeights[i][j][3] = updateValueMax;
				// }
				//
				// outputWeights[i][j][2] = -sign(outputWeights[i][j][1]) *
				// outputWeights[i][j][3];
				// outputWeights[i][j][0] += outputWeights[i][j][2];
				//
				// }
				// else if((prevGradient * outputWeights[i][j][1]) < 0) {
				//
				// outputWeights[i][j][3] = negStep * outputWeights[i][j][3];
				//
				// if(outputWeights[i][j][3] < updateValueMin) {
				// outputWeights[i][j][3] = updateValueMin;
				// }
				//
				// outputWeights[i][j][1] = 0d;
				//
				// }
				// else {
				// outputWeights[i][j][2] = -sign(outputWeights[i][j][1]) *
				// outputWeights[i][j][3];
				// outputWeights[i][j][0] += outputWeights[i][j][2];
				// }
			}
		}

		for(int i = 0; i < hiddenWeights.length - 1; i++) {

			for(int j = 0; j < inputNodes.length; j++) {

				Double prevGradient = hiddenWeights[i][j][1];

				if(prevGradient == null) {
					prevGradient = 0d;
				}

				// if(inputNodes[i][2] != null) {
				hiddenWeights[i][j][1] = inputNodes[j][1] * hiddenNodes[i][2];
				// System.out.println(hiddenWeights[i][j][1]);
				// }
				// if(hiddenWeights[i][j][1].isNaN()){
				// System.err.println();
				// }

				if(hiddenWeights[i][j][2] == null) {
					hiddenWeights[i][j][2] = 0d;
				}
				if(hiddenWeights[i][j][3] == null) {
					hiddenWeights[i][j][3] = 0.05;
				}

				Double tempOldWeightDelta = hiddenWeights[i][j][2];

				// new weightDelta
				// hiddenWeights[i][j][2] = (E * hiddenWeights[i][j][1]);

				// gradient descent
				// outputWeights[i][j][2] = (-E * outputWeights[i][j][1]);

				// momentum
				// hiddenWeights[i][j][2] = (E * hiddenWeights[i][j][1]) +
				// momentum * tempOldWeightDelta;
				// new weight
				// hiddenWeights[i][j][0] += hiddenWeights[i][j][2];

				// RPROP
				rProp(prevGradient, hiddenWeights[i][j]);

			}
		}
		return error;

	}

	private static void rProp(Double prevGradient, Double[] nauronWeights) {

		if((prevGradient * nauronWeights[1]) > 0) {

			nauronWeights[3] = posStep * nauronWeights[3];

			if(nauronWeights[3] > updateValueMax) {
				nauronWeights[3] = updateValueMax;
			}

			nauronWeights[2] = -sign(nauronWeights[1]) * nauronWeights[3];
			nauronWeights[0] += nauronWeights[2];

		}
		else if((prevGradient * nauronWeights[1]) < 0) {

			nauronWeights[3] = negStep * nauronWeights[3];

			if(nauronWeights[3] < updateValueMin) {
				nauronWeights[3] = updateValueMin;
			}

			nauronWeights[1] = 0d;

		}
		else {
			nauronWeights[2] = -sign(nauronWeights[1]) * nauronWeights[3];
			nauronWeights[0] += nauronWeights[2];
		}
	}

	private static Double sign(Double x) {

		if(x > 0) {
			return 1d;
		}
		else if(x < 0) {
			return -1d;
		}
		else {
			return 0d;
		}

	}

	private static void rProp(Double error) {

		// nodeDelta for output
		for(Double[] outputNode : outputNodes) {
			if(outputNode[0] != null) {
				outputNode[2] = (error) * (sigmoidalDerivative(outputNode[0]));
				// if(outputNode[2]==null){
				// System.out.println(outputNode[2]);
				// }
			}
		}

		// nodeDelta for hidden
		// for(Double[] hiddenNode : hiddenNodes) {
		for(int i = 0; i < hiddenNodes.length; i++) {

			if(hiddenNodes[i][0] != null) {

				// sum of weights between this H and O's
				Double weightsTimesNodeDeltaSum = 0d;
				for(int j = 0; j < outputNodes.length; j++) {

					if(outputNodes[j][2] != null) {
						weightsTimesNodeDeltaSum += outputWeights[j][i][0] * outputNodes[j][2];
					}
				}

				hiddenNodes[i][2] = sigmoidalDerivative(hiddenNodes[i][0]) * weightsTimesNodeDeltaSum;
			}
		}

		// gradient and weight
		for(int i = 0; i < outputWeights.length; i++) {

			for(int j = 0; j < hiddenNodes.length; j++) {

				// if(outputNodes[i][2] != null) {
				outputWeights[i][j][1] = hiddenNodes[j][1] * outputNodes[i][2];
				// }

				if(outputWeights[i][j][2] == null) {
					outputWeights[i][j][2] = 0d;
				}

				Double tempOldWeightDelta = outputWeights[i][j][2];
				// new weightDelta
				outputWeights[i][j][2] = (E * outputWeights[i][j][1]) + momentum * tempOldWeightDelta;
				// new weight
				outputWeights[i][j][0] += outputWeights[i][j][2];

			}
		}

		for(int i = 0; i < hiddenWeights.length - 1; i++) {

			for(int j = 0; j < inputNodes.length; j++) {
				// if(inputNodes[i][2] != null) {
				hiddenWeights[i][j][1] = inputNodes[j][1] * hiddenNodes[i][2];
				// }
				// if(hiddenWeights[i][j][1].isNaN()){
				// System.err.println();
				// }

				if(hiddenWeights[i][j][2] == null) {
					hiddenWeights[i][j][2] = 0d;
				}

				Double tempOldWeightDelta = hiddenWeights[i][j][2];
				// new weightDelta
				hiddenWeights[i][j][2] = (E * hiddenWeights[i][j][1]) + momentum * tempOldWeightDelta;
				// new weight
				hiddenWeights[i][j][0] += hiddenWeights[i][j][2];

			}
		}

	}

	public static Double sigmoidal(Double x) {

		// sigmoidal function
		return 1 / (1 + Math.pow(Math.E, -(x)));
	}

	public static Double sigmoidalDerivative(Double x) {
		Double temp = (sigmoidal(x) * (1 - sigmoidal(x)));

		if(temp.isInfinite() || temp.isNaN()) {
			System.err.println("kuku");
			temp = Double.MAX_VALUE;
		}

		// System.out.println("SD: "+temp);
		return temp;
	}

	private static Double getRandomDouble() {

		// devided by 10 because we need as small weight as possible
		return (Math.random() - 0.5) / 10;

	}

	private static Integer getRandomBit() {

		if(Math.random() < 0.5) {
			return 0;
		}
		else {
			return 1;
		}

	}

	/**
	 * 
	 * @author Mariusz Lewandowski; byMario
	 */
	public Double processUser(Double inputOne, Double inputTwo) {
		// TODO Auto-generated method stub
		setInputValues(inputOne, inputTwo);
		
		Double output = runNet();
		
		return output;
	}

}
