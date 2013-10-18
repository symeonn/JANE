package pl.byMarioUltimate;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 
 * @author Mariusz Lewandowski; byMario
 */
@Service
public class NeuralNetwork implements Runnable {

	private int layersCount;
	// private int layersCount;
	// private int layersCount;

	static int inputNeuronsCount;
	static int[] hiddenNeuronsLayersCount;
	static int outputNeuronsCount;

	/**
	 * Helps to escape local minumum if it's to high it will escape from global
	 * minimum
	 */
	static Double momentum = 0.7; // alpha

	/**
	 * Specifies how quickly the weights are going to be updated if it's to high
	 * weights and error are going to go into the extremas
	 */
	static Double E = 0.9; // learning rate, epsilon

	static Double posStep = 1.2;
	static Double negStep = 0.5;
	static Double updateValueMax = 50d;
	static Double updateValueMin = 0.0001;

	static Double errorConst = 0.002;

	public Double[] idealOutputValue;// = new Double[outputNodes.length];

	/**
	 * <pre>
	 * [layer]
	 * [node number]
	 * [inputValues*weights sum, outputValue, nodeDelta]
	 * 
	 * layer==0 - input
	 * layer==last - output 
	 * layer!=0 && layer!=last - hidden(s)
	 * </pre>
	 */
	public static Double[][][] layersNodesProps;

	/**
	 * <pre>
	 * [layer TO connection props]
	 * [node FROM - connection FROM node]
	 * [node TO - connection TO node]
	 * [weight, gradient, weightDelta, updateValue]
	 * </pre>
	 */
	public Double[][][][] connProps;

	public Double[][][][] prevConnProps;

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

	// for XOR

	// I I | O
	// ----+--
	// 0 0 | 0
	// 0 1 | 1
	// 1 0 | 1
	// 1 1 | 0

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

		// trainNet();
	}

	public NeuralNetwork() {
		super();
		buildNetworkStructure(null);
	}

	/**
	 * @param layersCount
	 */
	public NeuralNetwork(int... neuronInLayers) {
		super();

		buildNetworkStructure(neuronInLayers);
		initNodesConnectionsPropsArray();
		init();
	}

	/**
	 * 
	 * @author Mariusz Lewandowski; byMario
	 */
	private void init() {

		idealOutputValue = new Double[layersNodesProps[layersCount - 1].length];
		setBiasValues();
	}

	/**
	 * 
	 * @author Mariusz Lewandowski; byMario
	 */
	private void initNodesConnectionsPropsArray() {

		connProps = new Double[layersCount][][][];
		prevConnProps = new Double[layersCount][][][];

		// setup all nodes conn props starting from output layer
		for(int layerTo = layersCount - 1; layerTo > 0; layerTo--) {
			connProps[layerTo] = new Double[layersNodesProps[layerTo - 1].length][layersNodesProps[layerTo].length][4];
			prevConnProps[layerTo] = new Double[layersNodesProps[layerTo - 1].length][layersNodesProps[layerTo].length][4];
		}

	}

	/**
	 * @param neuronInLayers
	 * @author Mariusz Lewandowski; byMario
	 */
	private void buildNetworkStructure(int[] neuronInLayers) {

		if(neuronInLayers != null) {

			layersCount = neuronInLayers.length;

			hiddenNeuronsLayersCount = new int[neuronInLayers.length - 2];
			layersNodesProps = new Double[neuronInLayers.length][][];

			for(int layer = 0; layer < neuronInLayers.length; layer++) {

				if(layer == 0) {
					inputNeuronsCount = neuronInLayers[layer] + 1; // +1 for
																	// bias
					layersNodesProps[layer] = new Double[inputNeuronsCount][3];
				}
				else if(layer == neuronInLayers.length - 1) {

					outputNeuronsCount = neuronInLayers[layer];
					layersNodesProps[layer] = new Double[outputNeuronsCount][3];

				}
				else {
					hiddenNeuronsLayersCount[layer - 1] = neuronInLayers[layer] + 1; // +1
																						// for
																						// bias
					layersNodesProps[layer] = new Double[hiddenNeuronsLayersCount[layer - 1]][3];
				}
			}
		}
		else {

			inputNeuronsCount = 2;
			hiddenNeuronsLayersCount = new int[] { 2 };
			outputNeuronsCount = 1;
		}

	}

	// array: [node number][inputValues*weights sum, outputValue, nodeDelta]
	// public static Double[][] inputNodes = new Double[inputNeuronsCount +
	// 1][3]; // properties
	// of
	// input
	// layer
	// nodes
	// public static Double[][] hiddenNodes = new Double[hiddenNeuronsCount +
	// 1][3]; // properties
	// public static Double[][] hiddenNodes = new Double[2 + 1][3]; //
	// properties
	// of
	// hidden
	// layer
	// nodes
	// public static Double[][] outputNodes = new Double[outputNeuronsCount][3];
	// // properties
	// of
	// output
	// layer
	// nodes

	// connProps

	// array:
	// [2nd layer node number]
	// [1st layer node number - input from]
	// [weight, gradient, weightDelta, updateValue]
	// static Double[][][] hiddenWeights = new
	// Double[hiddenNodes.length][inputNodes.length][4]; // connections
	// properties
	// from
	// input
	// to
	// hidden
	// layer
	// static Double[][][] outputWeights = new
	// Double[outputNodes.length][hiddenNodes.length][4]; // connections
	// properties
	// from
	// hidden
	// to
	// output
	// layer

	/**
	 * @param idealOutput
	 * @author Mariusz Lewandowski; byMario
	 * @throws Exception
	 */
	private void setIdealOutputValues(Double[] idealOutput) throws Exception {

		if(idealOutput.length != layersNodesProps[layersCount - 1].length) {
			throw new Exception();
		}

		for(int node = 0; node < idealOutput.length; node++) {
			this.idealOutputValue[node] = idealOutput[node];
		}

	}

	void setInputValues(Double[] inputValues) throws Exception {

		// -1 because there is one bias which doesn't have input value
		if(inputValues.length != layersNodesProps[0].length - 1) {
			throw new Exception();
		}

		for(int iNode = 0; iNode < layersNodesProps[0].length - 1; iNode++) {
			layersNodesProps[0][iNode][1] = inputValues[iNode];

		}

		// +1 - bias
		// bias always has value = 1
		// inputNodes[inputNeuronsCount][0] = 1d;
		// inputNodes[inputNeuronsCount][1] = 1d;
		// System.out.println("BIAS " + inputNodes[inputNeuronsCount][1]);

		// idealOutputValue[0] =
		// Double.parseDouble(String.valueOf(inputNodes[0][1].intValue() ^
		// inputNodes[1][1].intValue()));
		// System.out.println("IDEAL: " + idealOutput[0]);
	}

	public void setBiasValues() {
		for(int layer = layersCount - 2; layer >= 0; layer--) { // skipping
																// outputlayer
			layersNodesProps[layer][layersNodesProps[layer].length - 1][1] = 1.0;
		}

	}

	/**
	 * Forward calculation
	 * 
	 * @param input
	 * @throws Exception
	 * @author Mariusz Lewandowski; byMario
	 */
	public void runNet(Double[] input) throws Exception {

		// each time we run the network, we need to setup input values
		setInputValues(input);

		// for each layer, starting input
		for(int layerFrom = 0; layerFrom < layersCount - 1; layerFrom++) {

			int layerTo = layerFrom + 1;

			for(int nodeTo = 0; nodeTo < layersNodesProps[layerTo].length; nodeTo++) {

				if(!(layerTo < layersCount - 1 && nodeTo == layersNodesProps[layerTo].length - 1)) {

					Double sum = 0d;

					for(int nodeFrom = 0; nodeFrom < layersNodesProps[layerFrom].length; nodeFrom++) {

						// initial weight setup
						if(connProps[layerTo][nodeFrom][nodeTo][0] == null) {
							connProps[layerTo][nodeFrom][nodeTo][0] = getRandomDouble();
						}
						// sum = sum + (weight * output value)
						sum += connProps[layerTo][nodeFrom][nodeTo][0] * layersNodesProps[layerFrom][nodeFrom][1];
					}

					// sum that input TO node
					layersNodesProps[layerTo][nodeTo][0] = sum;
					// output of TO node (after activation function)
					layersNodesProps[layerTo][nodeTo][1] = sigmoidal(sum);
				}
			}
		}

	}

	public Double trainNet(Double[] input, Double[] idealOutput) throws Exception {

		setIdealOutputValues(idealOutput);

		// init();
		// setInputValues(null, null);

		Integer iteration = 0;

		Double[] output;
		// System.out.println("************************");
		// System.out.println("backProp");
		// System.out.println("************************");
		Double error = 0.0;
		// do {
		// iteration++;
		// System.out.println("Iteration " + iteration);
		runNet(input);

		// error = gradientCalculation();
		// if(iteration > 3) break; //do not delete this It might be usefull
		// later for for not quite perfect learning
		// }
		// while(error >= errorConst || iteration > 1000);

		// System.out.println("Iterations " + iteration);
		// System.out.println(outputNodes[0][1].toString());

		// System.out.println("iO: " + idealOutput[0] + " itT: " + iteration);
		// System.out.print("iO: " + idealOutput[0] + " itT: " + iteration);

		// System.out.println("RESULT: " + outputNodes[0][1].toString());

		// for(int i = 0; i < inputNodes.length - 1; i++) {
		// System.out.println("neuron " + (i + 1) + " out value: " +
		// inputNodes[i][1]);
		// }
		return gradientCalculation();

	}

	/**
	 * Forward calculation with context
	 * 
	 * @param input
	 * @throws Exception
	 * @author Mariusz Lewandowski; byMario
	 */
	public void runNetWithContext(Double[] input) throws Exception {
		// implement later:

		// Elman Neural Network SRN

		// OR

		// Jordan Neural Network SRN

	}

	/**
	 * gradient optimization technic
	 * 
	 * @author Mariusz Lewandowski; byMario
	 */
	private double gradientCalculation() {

		Double error = nodeDeltaCalculation();

		// gradient and weight

		// starting from output layer
		for(int layerTo = layersCount - 1; layerTo > 0; layerTo--) {

			int layerFrom = layerTo - 1;

			for(int nodeTo = 0; nodeTo < layersNodesProps[layerTo].length; nodeTo++) {

				if(!(layerTo < layersCount - 1 && nodeTo == layersNodesProps[layerTo].length - 1)) {

					for(int nodeFrom = 0; nodeFrom < layersNodesProps[layerFrom].length; nodeFrom++) {

						Double prevGradient = connProps[layerTo][nodeFrom][nodeTo][1];

						if(prevGradient == null) {
							prevGradient = 0.0;
						}
						// new gradient calulated (FROM node output value * TO
						// node's nodeDelta)

						if(connProps[layerTo][nodeFrom][nodeTo][1] == null) {
							connProps[layerTo][nodeFrom][nodeTo][1] = 0.0;
							prevConnProps[layerTo][nodeFrom][nodeTo][1] = 0.0;
						}
						connProps[layerTo][nodeFrom][nodeTo][1] += layersNodesProps[layerFrom][nodeFrom][1] * layersNodesProps[layerTo][nodeTo][2];
						// for first iteration weight change is 0
						if(connProps[layerTo][nodeFrom][nodeTo][2] == null) {
							connProps[layerTo][nodeFrom][nodeTo][2] = 0.0;
						}
						// for first iteration value change is 0
						if(connProps[layerTo][nodeFrom][nodeTo][3] == null) {
							connProps[layerTo][nodeFrom][nodeTo][3] = 0.05;// ????
						}

//						 backPropWithMomentum(connProps[layerTo][nodeFrom][nodeTo]);
						// backProp(connProps[layerTo][nodeFrom][nodeTo]);
//						 rProp(prevGradient,
//						 connProps[layerTo][nodeFrom][nodeTo]);

					}
				}
			}
		}

		return error;

	}

	/**
	 * node delta calculation
	 * 
	 * @return
	 * @author Mariusz Lewandowski; byMario
	 */
	private Double nodeDeltaCalculation() {
		
		Double error = 0.0;

		// nodeDelta calculation
		Double[][] outputNodes = layersNodesProps[layersCount - 1];

		for(int layer = layersCount - 1; layer > 0; layer--) {

			if(layer == 0) { // input layer

			}
			else if(layer == layersCount - 1) { // output layer

				for(int oNode = 0; oNode < outputNodes.length; oNode++) {

					// mean squared error (MSE) - it is not because error is not
					// devided n
					error += Math.pow(outputNodes[oNode][1] - idealOutputValue[oNode], 2);

					// if(outputNodes[oNode][0] != null) {
					outputNodes[oNode][2] = (outputNodes[oNode][1] - idealOutputValue[oNode]) * (sigmoidalDerivative(outputNodes[oNode][0]));
					// if(outputNode[2]==null){
					// System.out.println(outputNode[2]);
					// }
					// }
				}
			}
			else { // hidden layers

				Double[][] hiddenNodes = layersNodesProps[layer];

				for(int hNode = 0; hNode < hiddenNodes.length; hNode++) {

					if(hiddenNodes[hNode][0] != null) {

						// sum of weights between this H and O's
						Double weightsTimesNodeDeltaSum = 0d;
						for(int oNode = 0; oNode < outputNodes.length; oNode++) {

							if(outputNodes[oNode][2] != null) {
								weightsTimesNodeDeltaSum += connProps[layer + 1][hNode][oNode][0] * outputNodes[oNode][2];
								// weightsTimesNodeDeltaSum +=
								// outputWeights[oNode][hNode][0] *
								// outputNodes[oNode][2];
							}
						}

						hiddenNodes[hNode][2] = sigmoidalDerivative(hiddenNodes[hNode][0]) * weightsTimesNodeDeltaSum;
					}
				}
			}
		}
		
		return error;
	}

	public void rPropWeights(int e) {

		for(int layerTo = layersCount - 1; layerTo > 0; layerTo--) {

			int layerFrom = layerTo - 1;

			for(int nodeTo = 0; nodeTo < layersNodesProps[layerTo].length; nodeTo++) {

				if(!(layerTo < layersCount - 1 && nodeTo == layersNodesProps[layerTo].length - 1)) {

					for(int nodeFrom = 0; nodeFrom < layersNodesProps[layerFrom].length; nodeFrom++) {

						// prevConnProps[layerTo][nodeFrom][nodeTo][1] =
						// connProps[layerTo][nodeFrom][nodeTo][1];

						// if(first) {
						// prevConnProps[layerTo][nodeFrom][nodeTo][1] = 0.0;
						// }
						switch(e) {
						case 0:
							System.out.println("G:" + new DecimalFormat("##.########").format(connProps[layerTo][nodeFrom][nodeTo][1]));
//							System.out.println("G:" + connProps[layerTo][nodeFrom][nodeTo][1]);
							prevConnProps[layerTo][nodeFrom][nodeTo][1] = 
							rProp(prevConnProps[layerTo][nodeFrom][nodeTo][1], connProps[layerTo][nodeFrom][nodeTo]);

							break;
						case 1:
							backPropWithMomentum(connProps[layerTo][nodeFrom][nodeTo]);
							break;
							
						case 2:
							backProp(connProps[layerTo][nodeFrom][nodeTo]);
							break;

						default:
							break;
						}

					}
				}
			}
		}

	}

	/**
	 * genetic algorithm optimization technic
	 * 
	 * @author Mariusz Lewandowski; byMario
	 */
	private void geneticAlgorithm() {

	}

	/**
	 * Back propagation gradient calculation algorithm
	 * 
	 * @author Mariusz Lewandowski; byMario
	 */
	private void backProp(Double[] connDetails) {

		connDetails[2] = -E * connDetails[1];
		connDetails[0] += connDetails[2];
		connDetails[1] = 0.0;
		
	}

	/**
	 * Back propagation with momentum gradient calculation algorithm
	 * 
	 * @author Mariusz Lewandowski; byMario
	 */
	private void backPropWithMomentum(Double[] connDetails) {

		Double tempOldWeightDelta = connDetails[2];

		connDetails[2] = (-E * connDetails[1]) + momentum * tempOldWeightDelta;
		connDetails[0] += connDetails[2];
		connDetails[1] = 0.0;

	}

	/**
	 * Resilient propagation gradient calculation algorithm
	 * 
	 * @param prevGradient
	 * @param nauronWeights
	 * @author Mariusz Lewandowski; byMario
	 */
	private Double rProp(Double prevGradient, Double[] nauronWeights) {

		if((prevGradient * nauronWeights[1]) > 0) {

			nauronWeights[3] = posStep * nauronWeights[3];

			if(nauronWeights[3] > updateValueMax) {
				nauronWeights[3] = updateValueMax;
			}
			nauronWeights[2] = -sign(nauronWeights[1]) * nauronWeights[3];
			prevGradient = nauronWeights[1];
//			nauronWeights[1] = 0.0;
			// nauronWeights[0] += nauronWeights[2];

		}
		else if((prevGradient * nauronWeights[1]) < 0) {

			nauronWeights[3] = negStep * nauronWeights[3];

			if(nauronWeights[3] < updateValueMin) {
				nauronWeights[3] = updateValueMin;
			}

//			nauronWeights[1] = 0.0;
			prevGradient = 0.0;

		}
		else {
			nauronWeights[2] = -sign(nauronWeights[1]) * nauronWeights[3];
			prevGradient = nauronWeights[1];

		}
		nauronWeights[1] = 0.0;
		nauronWeights[0] += nauronWeights[2];
		return prevGradient;
	}

	private static Double sign(Double x) {

		if(x > 0) {
			return 1.0;
		}
		else if(x < 0) {
			return -1.0;
		}
		else {
			return 0.0;
		}

	}

	// private static void rProp(Double error) {
	//
	// // nodeDelta for output
	// for(Double[] outputNode : outputNodes) {
	// if(outputNode[0] != null) {
	// outputNode[2] = (error) * (sigmoidalDerivative(outputNode[0]));
	// // if(outputNode[2]==null){
	// // System.out.println(outputNode[2]);
	// // }
	// }
	// }
	//
	// // nodeDelta for hidden
	// // for(Double[] hiddenNode : hiddenNodes) {
	// for(int i = 0; i < hiddenNodes.length; i++) {
	//
	// if(hiddenNodes[i][0] != null) {
	//
	// // sum of weights between this H and O's
	// Double weightsTimesNodeDeltaSum = 0d;
	// for(int j = 0; j < outputNodes.length; j++) {
	//
	// if(outputNodes[j][2] != null) {
	// weightsTimesNodeDeltaSum += outputWeights[j][i][0] * outputNodes[j][2];
	// }
	// }
	//
	// hiddenNodes[i][2] = sigmoidalDerivative(hiddenNodes[i][0]) *
	// weightsTimesNodeDeltaSum;
	// }
	// }
	//
	// // gradient and weight
	// for(int i = 0; i < outputWeights.length; i++) {
	//
	// for(int j = 0; j < hiddenNodes.length; j++) {
	//
	// // if(outputNodes[i][2] != null) {
	// outputWeights[i][j][1] = hiddenNodes[j][1] * outputNodes[i][2];
	// // }
	//
	// if(outputWeights[i][j][2] == null) {
	// outputWeights[i][j][2] = 0d;
	// }
	//
	// Double tempOldWeightDelta = outputWeights[i][j][2];
	// // new weightDelta
	// outputWeights[i][j][2] = (E * outputWeights[i][j][1]) + momentum *
	// tempOldWeightDelta;
	// // new weight
	// outputWeights[i][j][0] += outputWeights[i][j][2];
	//
	// }
	// }
	//
	// for(int i = 0; i < hiddenWeights.length - 1; i++) {
	//
	// for(int j = 0; j < inputNodes.length; j++) {
	// // if(inputNodes[i][2] != null) {
	// hiddenWeights[i][j][1] = inputNodes[j][1] * hiddenNodes[i][2];
	// // }
	// // if(hiddenWeights[i][j][1].isNaN()){
	// // System.err.println();
	// // }
	//
	// if(hiddenWeights[i][j][2] == null) {
	// hiddenWeights[i][j][2] = 0d;
	// }
	//
	// Double tempOldWeightDelta = hiddenWeights[i][j][2];
	// // new weightDelta
	// hiddenWeights[i][j][2] = (E * hiddenWeights[i][j][1]) + momentum *
	// tempOldWeightDelta;
	// // new weight
	// hiddenWeights[i][j][0] += hiddenWeights[i][j][2];
	//
	// }
	// }
	//
	// }

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
	// public Double processUser(Double inputOne, Double inputTwo) {
	// TODO Auto-generated method stub
	// setInputValues(inputOne, inputTwo);

	// Double output = runNet();
	//
	// return output;
	// }

	public Double[] getNetworkOutput() {

		Double[] output = new Double[layersNodesProps[layersCount - 1].length];

		for(int oNode = 0; oNode < layersNodesProps[layersCount - 1].length; oNode++) {
			output[oNode] = layersNodesProps[layersCount - 1][oNode][1];
		}

		return output;
	}
	
	private void testMethod(){
		
	}
}
