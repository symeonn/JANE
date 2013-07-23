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
 * @author Mariusz Lewandowski; Archidoc S.A.
 */
@Service
@Scope("prototype")
public class WordWriterService {
	
	private static final Logger LOGGER = Logger.getLogger(WordWriterService.class);
	
	@Autowired
	private WordDao wordDao;

	private String fileParh = "D:\\DRIVE\\slowa\\slowa-win.txt";
	int chunkLimit = 500;
	
	public Integer importFromFile(Integer chunksToSave) {
		
		Integer totalWordsSaved = 0;
		
		WordEntity importedWord = null;
		List<WordEntity> wordsList = new ArrayList<WordEntity>();

		try {
			int i = 0;
			int saveChunks = 0;
			
			String strLine;
			
			File file = new File(fileParh);
			Scanner scanner = new Scanner(file, "Cp1250");
			i=0;
			while((strLine = scanner.nextLine())!=null) {
				
				if  (i>=chunkLimit){
					saveInDb(wordsList);
					i=0;
					saveChunks ++;
					totalWordsSaved += wordsList.size();
//					LOGGER.info(totalWordsSaved);
					if(chunksToSave!=null && saveChunks>=chunksToSave) break; //temporary
				}
				
				importedWord = new WordEntity(null, strLine);
				wordsList.add(importedWord);
				
				i++;
			}
			

		}
		catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return totalWordsSaved;
	}
	
	private void saveInDb(List<WordEntity> wordsListToImport){

		try {
//			LOGGER.info("Saving to DB "+ wordsListToImport.size() + " elements");
			wordDao.saveAll(wordsListToImport);

			wordsListToImport.clear();
//			LOGGER.info();
		}
		catch(Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<WordEntity> findEntity(){
		
		List<WordEntity> we = wordDao.findById(null);
		
		return we;
	}

}
