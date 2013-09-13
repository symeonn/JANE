package pl.byMarioUltimate.controller;

import java.io.File;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import pl.byMarioUltimate.StartClazz;
import pl.byMarioUltimate.NeuralNetwork;
import pl.byMarioUltimate.dao.WordDao;
import pl.byMarioUltimate.jpa.WordEntity;
import pl.byMarioUltimate.service.NeuralNetService;
import pl.byMarioUltimate.service.WordWriterService;

@Controller
@Scope("session")
public class StartController {

	private static final Logger LOGGER = Logger.getLogger(StartController.class);

	private String userSentence;
	private String janeSentence;
	private String one;
	private String two;
	// NeuralNetDto neuralNet;

	@Autowired
	private NeuralNetService netService;
//	@Autowired
	private NeuralNetwork neuralNetwork;
	@Autowired
	private StartClazz startClazz;

	@Autowired
	private WordWriterService wordWriterService;

	public String getUserSentence() {
		return userSentence;
	}

	public void setUserSentence(String userSentence) {
		this.userSentence = userSentence;
	}

	public StartController() {

		neuralNetwork = new NeuralNetwork(2, 4, 1);
		
//		Thread t;
//
//		t = new Thread(new NeuralNetwork(2, 2, 1));
//		t.setName("NET");
//
//		t.start();
//		t.run();
//		while(t.isAlive()) {
//		}

		// neuralNet = new NeuralNetDto();
		// netService.initNet();
		// startClazzTwo = new NeuralNetwork(2,2,1);

		System.out.println("StartController constructor");
		this.userSentence = "napisz zdanie";
		this.janeSentence = "hi!";

	}

	public String clTestNoEvent() {
		testOrmWithService();
		System.out.println("jest wartosc NOevent");
		return null;
	}

	public String startDummyNet() {
		System.out.println("START DUMMY");
		netService.processDummySentence(userSentence);
		netService.dummyProcess(userSentence);

		return null;
	}

	public String initNet() {

		netService.initDummyNet(10, 20);
		// netService.initNet();

		Integer i = null;
		try {
			i = Integer.parseInt(userSentence);

		}
		catch(Exception e) {
			LOGGER.warn("to nie numer");

			// LOGGER.warn(LOGGER.getEffectiveLevel());
			// LOGGER.info("logger INFO");
			// LOGGER.warn("logger WARN");
			// LOGGER.debug("logger DEBUG");

			// tekst = "to nie numer";
		}
		// tekst = "Zapisano s��w: " + wordWriterService.importFromFile(i);

		System.out.println("KONIEC IMPORTU!!");
		return null;
	}

	public synchronized void robocza(String userSentence) {
		try {
			LOGGER.info("xcv INFO");
			LOGGER.warn("xcv WARN");

			Properties props = System.getProperties();
			String path = new File(".").getCanonicalPath();
			String pat = System.getProperty("user.dir");

			// DbUtil abcl = new DbUtil();

			// String tt = abcl.clJava(userSentence);

			// this.tekst = tt;
		}
		catch(Exception e) {
			e.printStackTrace();
			getLogger().error(e.getMessage());
		}

	}

	protected Logger getLogger() {
		return LOGGER;
	}

	public void testOrm() {

		try {

			WordDao dao = new WordDao();
			List<WordEntity> we = dao.findById(1);
			LOGGER.info(we.toString());

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void testOrmWithService() {

		try {
			List<WordEntity> we = wordWriterService.findEntity();

			LOGGER.info(we.toString());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void valueChanged(ValueChangeEvent event) {
		try {
			Integer newValue = Integer.parseInt(event.getNewValue().toString());

		}
		catch(Exception e) {
			// e.printStackTrace();
		}

	}

	public void processUserSentence(ActionEvent event) {

		netService.updateNet(userSentence);

		netService.process(userSentence);

		janeSentence = netService.getResponse();
	}

	public void learn(ActionEvent event) {

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
			while(i < 100000 && error > 0.00002);

			System.out.println("FINAL " + i + " e:" + error);

		}
		catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Thread tt = null;
		// Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		//
		// for(Thread thread : threadSet) {
		//
		// if(thread.getName().equalsIgnoreCase("NET")){
		// tt = thread;
		// }
		// }
		// tt.run();

		// startClazzTwo.processNet();

//		int i = 0;
//		while(i < 10000) {
//			// startClazzTwo.trainNet();
//			System.out.println(i);
//			i++;
//		}
	}

	public void check(ActionEvent event) {

//		Thread tt = null;
//		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
//
//		for(Thread thread : threadSet) {
//
//			if(thread.getName().equalsIgnoreCase("NET")) {
//				tt = thread;
//				tt.run();
//			}
//		}

		Double oneD = Double.parseDouble(one);
		Double twoD = Double.parseDouble(two);

		
		try {
			neuralNetwork.runNet(new Double[]{oneD, twoD});
		}
		catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Double output = neuralNetwork.getNetworkOutput()[0];
//		janeSentence = output.toString();
		Math.round(output);
		janeSentence = String.valueOf(new DecimalFormat("##.##").format(output));
		// Double output = startClazzTwo.processUser(oneD, twoD);

	}

	public void initNetwork(ActionEvent event) {

	}

	public void learning(ActionEvent event) {

		if(netService != null) {
			// netService.process();

		}
	}

	public void process(ActionEvent event) {

		if(netService != null) {
			// netService.processFinal(userSentence);

		}
	}

	public String getJaneSentence() {
		return janeSentence;
	}

	public void setJaneSentence(String janeSentence) {
		this.janeSentence = janeSentence;
	}

	public String getNeuronCount() {

		return "N: " + netService.getNeuronsAmount();
	}

	public synchronized String getOne() {
		return one;
	}

	public synchronized void setOne(String one) {
		this.one = one;
	}

	public synchronized String getTwo() {
		return two;
	}

	public synchronized void setTwo(String two) {
		this.two = two;
	}
}
