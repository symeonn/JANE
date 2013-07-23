package pl.byMarioUltimate;

import org.apache.log4j.Logger;

import pl.byMarioUltimate.service.NeuralNetService;

public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class);

	public static void main(String[] argv) {

//		NeuralNetService neuralNetService = new NeuralNetService();
//		neuralNetService.initRawNet();
//		neuralNetService.updateNet("one two three");
//		neuralNetService.updateNet("three four");
//		neuralNetService.updateNet("one five");
//		
//		System.out.println("koniec MAIN");
		
		StartClazzTwo cc = new StartClazzTwo();
		int i =0;
//		while(i<1000000){
			cc.trainNet();
			System.out.println(i);
			i++;
//		}

	}

}
