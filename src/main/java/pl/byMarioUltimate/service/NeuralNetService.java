package pl.byMarioUltimate.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pl.byMarioUltimate.dao.NeuronDao;
import pl.byMarioUltimate.dao.WordDao;
import pl.byMarioUltimate.dto.NeuralNetDto;
import pl.byMarioUltimate.dto.NeuronDto;
import pl.byMarioUltimate.dto.NeuronIDto;
import pl.byMarioUltimate.jpa.NeuronEntity;

/**
 * 
 * @author Mariusz Lewandowski; byMario
 */
@Service
@Scope("session")
public class NeuralNetService {

	@Autowired
	NeuralNetDto netDto;

	// @Autowired
	// NeuronService neuronService;

	// @Autowired
	// UserCommunicationService userCommunicationService;

	@Autowired
	NeuronDao neuronDao;

	@Autowired
	WordDao wordDao;

	List<NeuronIDto> allInputNeurons = new ArrayList<NeuronIDto>();

	List<String> learnedWords = new ArrayList<String>();

	/**
	 * 
	 * @author Mariusz Lewandowski; byMario
	 */
	public void initNet() {

		netDto = new NeuralNetDto();

		List<NeuronEntity> neuronsInLayer = null;
		NeuronDto neuronDto = null;

		// reading existing neurons from DB
		for(String layer : netDto.getNetStructure().keySet()) {

			neuronsInLayer = neuronDao.getNeuronByLayer(layer);

			if(neuronsInLayer != null) {
				for(NeuronEntity neuronEntity : neuronsInLayer) {

					if(NeuralNetDto.INPUT_LAYER.equals(layer)) {
						String word = wordDao.findWordByNeuronId(neuronEntity.getId());
						neuronDto = new NeuronIDto(neuronEntity.getId(), neuronEntity.getConnectionFrom(), neuronEntity.getLayer(), word);
					}
					else {
						neuronDto = new NeuronDto(neuronEntity.getId(), neuronEntity.getConnectionFrom(), neuronEntity.getLayer());
					}
					netDto.getNetStructure().get(layer).add(neuronDto);
					netDto.neuronsCount++;
				}
			}
		}

		System.out.println("TOTAL NEURONS: " + netDto.neuronsCount);

	}

	public void initRawNet() {

		netDto = new NeuralNetDto();
	}

	public void initDummyNet(int lInSize, int lHSize) {

		NeuronIDto nIn;
		NeuronDto nH;

		for(int i = 0; i < lInSize; i++) {
			nIn = new NeuronIDto(++netDto.neuronsCount, NeuralNetDto.INPUT_LAYER, null);
			netDto.getInputLayer().add(nIn);
		}

		for(int i = 0; i < lHSize; i++) {
			nH = new NeuronDto(++netDto.neuronsCount, NeuralNetDto.HIDDEN_LAYER);
			netDto.getHiddeLayer().add(nH);
		}

		System.out.println(netDto.getInputLayer().size());
		System.out.println(netDto.getHiddeLayer().size());

		createDummyConnections();

	}

	/**
	 * 
	 * @author Mariusz Lewandowski; byMario
	 */
	private void createDummyConnections() {

		for(NeuronDto nH : netDto.getHiddeLayer()) {
			for(NeuronDto nIn : netDto.getInputLayer()) {
				nH.getIdInputWeightMap().put(nIn.getId(), getInitWeight());
				nIn.getIdInputWeightMap().put(nH.getId(), getInitWeight());
			}
		}
	}

	public void processDummySentence(String sentence) {

		List<String> words = Arrays.asList(sentence.split(" "));

		for(String sentenceWord : words) {

			// NeuronIDto nIn = netDto.findNeuronIByWord(sentenceWord.trim());

			for(NeuronDto nIn : netDto.getInputLayer()) {
				if(((NeuronIDto)nIn).getWord() == null) {
					((NeuronIDto)nIn).setWord(sentenceWord);
					System.out.println(((NeuronIDto)nIn).getWord() + nIn.getId());
					for(NeuronDto nH : netDto.getHiddeLayer()) {
						if(nH.getOutputConnections().isEmpty()) {
							nH.getOutputConnections().add(nIn.getId());
							break;
						}
					}
					break;
				}else{
					if(((NeuronIDto)nIn).getWord().equalsIgnoreCase(sentenceWord)){
						break;
					}
				}
			}
		}
	}

	public void dummyProcess(String sentence) {

		// List<NeuronIDto> processNinList = new ArrayList<NeuronIDto>();
		Map<Long, Double> nInValueMap = new HashMap<Long, Double>();
		Map<Long, Double> nHValueMap = new HashMap<Long, Double>();
		
		Map<Double, String> outputMap = new TreeMap<Double, String>(Collections.reverseOrder());

		List<String> words = Arrays.asList(sentence.split(" "));

		for(String sentenceWord : words) {
			nInValueMap.put(netDto.findNeuronIByWord(sentenceWord.trim()).getId(), 1d);
		}

		System.out.println(nInValueMap);

		
		System.out.println("------------------");
		System.out.println("OUTPUT:");
		for(NeuronDto nH : netDto.getHiddeLayer()) {
			nH.process(nInValueMap);
			// nHValueMap.putAll(nH.getIdOutputValueMap());
			System.out.println(nH.getValue() + " : " + nH.getOutputConnections());
			if(!(nH.getOutputConnections().isEmpty()) && nH.getValue()>0.5) {
				outputMap.put(nH.getValue(), ((NeuronIDto)netDto.findNeuronById((Long)nH.getOutputConnections().toArray()[0])).getWord());
				System.out.println(((NeuronIDto)netDto.findNeuronById((Long)nH.getOutputConnections().toArray()[0])).getWord());
			}
		}
		
		for(String string : outputMap.values()) {
			System.out.println(string);
		}

//		for(NeuronDto nIn : netDto.getInputLayer()) {
//			nIn.process(nHValueMap);
//			System.out.println(nIn.getIdOutputValueMap().get(nIn.getId()));
//		}

	}

	/**
	 * @param sentence
	 * @author Mariusz Lewandowski; byMario
	 */
	public void updateNet(String sentence) {

		boolean areAnyNewNin = false;
		List<String> words = Arrays.asList(sentence.split(" "));

		List<NeuronIDto> newCreatedNin = new ArrayList<NeuronIDto>();
		List<NeuronDto> neurons = new ArrayList<NeuronDto>();

		// for(String learnedWord : learnedWords) {
		for(String sentenceWord : words) {

			NeuronIDto nIn = netDto.findNeuronIByWord(sentenceWord.trim());

			if(nIn == null) {
				nIn = new NeuronIDto(++netDto.neuronsCount, NeuralNetDto.INPUT_LAYER, sentenceWord.trim());
				areAnyNewNin = true;
				netDto.getInputLayer().add(nIn);

			}// else{
				// create new Nin, Nh, No
				// create new connections

			// NeuronIDto newCreatedNi = new NeuronIDto(++netDto.neuronsCount,
			// NeuralNetDto.INPUT_LAYER, sentenceWord);

			// newCreatedNin.add(newCreatedNi);
			// allNeurons.add(newCreatedNi);
			// }
			neurons.add(nIn);

		}

		if(areAnyNewNin) {

			NeuronDto newCreatedNh = new NeuronDto(++netDto.neuronsCount, NeuralNetDto.HIDDEN_LAYER);
			neurons.add(newCreatedNh);
			netDto.getHiddeLayer().add(newCreatedNh);

		}

		createConnections(neurons);

	}

	/**
	 * 
	 * @author Mariusz Lewandowski; byMario
	 * @param allNeurons
	 */
	private void createConnections(List<NeuronDto> allNeurons) {

		// get all Nin outputs as map<id, weight>
		// add map for all Nh as input
		// get all Nh outputs as map<id, weight>
		// add map for all Nin as input

		Map<Long, Double> idWeightNinOutputMap = new HashMap<Long, Double>();

		Set<Long> nInIdList = new HashSet<Long>();
		Set<Long> nHIdList = new HashSet<Long>();

		// creating lists of IDs of input and hidden neurons
		for(NeuronDto neuronDto : allNeurons) {

			// for all Nin
			if(neuronDto instanceof NeuronIDto) {

				nInIdList.add(neuronDto.getId());

				nHIdList.addAll(neuronDto.getOutputConnections());

				// neuronDto.getOutputConnections();
				// idWeightNinOutputMap.put(neuronDto.getId(), arg1)
			}
			else {
				nHIdList.add(neuronDto.getId());

			}
		}

		for(Long nHId : nHIdList) {
			// powyzsza petla po tylko niektore nH biora udzial w 'tym zdaniu'
			// for all Nh
			// if(!(neuronDto instanceof NeuronIDto)){

			NeuronDto neuronHById = netDto.findNeuronById(nHId);

			for(Long nInId : nInIdList) {
				// zamienic - najpierw dodawanie a potem sprawdzanie bo zdarza
				// sie ze na liscie polaczen do stworzenia sa juz polaczenia
				// stworzone, ale najpierw sprawdza czyt full i niestety dodaje
				// nowy Nh, wtedy rosnie bez konca

				if(neuronHById.isFull()) {
					// create new neuronH

					NeuronDto newCreatedNh = new NeuronDto(++netDto.neuronsCount, NeuralNetDto.HIDDEN_LAYER);
					neuronHById.getOutputConnections().add(newCreatedNh.getId());

					neuronHById = newCreatedNh;
					neuronHById.getIdInputWeightMap().put(nHId, null);

					allNeurons.add(newCreatedNh);
					netDto.getHiddeLayer().add(neuronHById);

				}
				else {
					neuronHById.getIdInputWeightMap().put(nInId, null);
					neuronHById.getOutputConnections().add(nInId);
				}

				NeuronIDto neuronIById = (NeuronIDto)netDto.findNeuronById(nInId);

				if(neuronIById.isFull()) {
					// // create new neuronH
					//
					NeuronDto newCreatedNh = new NeuronDto(++netDto.neuronsCount, NeuralNetDto.HIDDEN_LAYER);

					// for(Entry<Long, Double> nInSingleinput :
					// neuronIById.getIdInputWeightMap().entrySet()) {
					newCreatedNh.getIdInputWeightMap().putAll(neuronIById.getIdInputWeightMap());
					newCreatedNh.getOutputConnections().add(nInId);
					// }

					neuronIById.getIdInputWeightMap().clear();
					neuronIById.getIdInputWeightMap().put(newCreatedNh.getId(), null);
					// neuronHById.getOutputConnections().add(newCreatedNh.getId());
					//
					// neuronHById = newCreatedNh;
					// neuronHById.getIdInputWeightMap().put(nHId, null);
					//
					// allNeurons.add(newCreatedNh);
					netDto.getHiddeLayer().add(newCreatedNh);
					//
					//
				}
				// else {
				neuronIById.getIdInputWeightMap().put(neuronHById.getId(), null);
				neuronIById.getOutputConnections().add(neuronHById.getId());
				// }
			}

		}

		System.out.println(netDto.getNetStructure());
		System.out.println(netDto.neuronsCount);
	}

	/**
	 * @param userSentence
	 * @author Mariusz Lewandowski; byMario
	 */
	public void process(String userSentence) {

	}

	/**
	 * @return
	 * @author Mariusz Lewandowski; byMario
	 */
	public String getResponse() {

		String sentence = "";

		for(NeuronDto neuron : netDto.getInputLayer()) {
			sentence += ((NeuronIDto)neuron).getWord() + " ";
		}

		if(sentence.isEmpty()) {
			sentence = "!@#$%^";
		}

		return sentence;
	}

	public Long getNeuronsAmount() {

		return netDto.neuronsCount;
	}
	
	private Double getInitWeight(){
		
		// devided by 10 because we need as small weight as possible
		return (Math.random()-0.5) / 10;
		
	}
	
	
	

}
