package pl.byMarioUltimate.controller;

import java.io.File;
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
import pl.byMarioUltimate.StartClazzTwo;
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
//	NeuralNetDto neuralNet;
	
	
	@Autowired
	private NeuralNetService netService;
	@Autowired
	private StartClazzTwo startClazzTwo;
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
		

//		neuralNet = new NeuralNetDto();
//		netService.initNet();
		
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
//		netService.initNet();

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

//			tekst = "to nie numer";
		}
//		tekst = "Zapisano s��w: " + wordWriterService.importFromFile(i);

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

//			DbUtil abcl = new DbUtil();

//			String tt = abcl.clJava(userSentence);

//			this.tekst = tt;
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
		
//		Thread tt = null;
//		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
//		
//		for(Thread thread : threadSet) {
//			
//			if(thread.getName().equalsIgnoreCase("NET")){
//				tt = thread;
//			}
//		}
//		tt.run();
		
//		startClazzTwo.processNet();
		
		int i =0;
		while(i<10000){
			startClazzTwo.trainNet();
			System.out.println(i);
			i++;
		}
	}
	
	public void check(ActionEvent event) {
		
//		Thread tt = null;
//		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
//		
//		for(Thread thread : threadSet) {
//			
//			if(thread.getName().equalsIgnoreCase("NET")){
//				tt = thread;
//			}
//		}
//		tt.run();
		
		Double oneD = Double.parseDouble(one);
		Double twoD = Double.parseDouble(two);
		
		Double output = startClazzTwo.processUser(oneD, twoD);
		
		janeSentence = output.toString();
	}

	public void initNetwork(ActionEvent event) {

	}

	public void learning(ActionEvent event) {

		if(netService != null) {
//			netService.process();

		}
	}
	
	public void process(ActionEvent event) {
		
		if(netService != null) {
//			netService.processFinal(userSentence);
			
		}
	}

	public String getJaneSentence() {
		return janeSentence;
	}

	public void setJaneSentence(String janeSentence) {
		this.janeSentence = janeSentence;
	}

	public String getNeuronCount(){
		
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
