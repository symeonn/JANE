package pl.byMarioUltimate.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pl.byMarioUltimate.dao.WordDao;
import pl.byMarioUltimate.jpa.WordEntity;

/**
 * 
 * @author Mariusz Lewandowski; byMario
 */
@Service
@Scope("prototype")
public class UserCommService {
	
	private static final Logger LOGGER = Logger.getLogger(UserCommService.class);
	
	@Autowired
	private WordDao wordDao;

	@Autowired
	private NeuralNetService neuralNetService;

	public void processSentence(String sentence){
		
		//rozbic zdanie
			//sprawdzic czy NETin zawiera slowo
				//jesli zawiera
					//
				//jesli nie zawiera
					//utworzyc
		
		neuralNetService.updateNet(sentence);
		
	}
	
}
