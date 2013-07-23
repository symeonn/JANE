package pl.byMarioUltimate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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
public class NeuronService {

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


	public List<NeuronIDto> findNeuronByWord(String word) {

		List<NeuronIDto> inputNeurons = new ArrayList<NeuronIDto>();
		List<NeuronIDto> inputNeuronsToAdd = new ArrayList<NeuronIDto>();
		List<NeuronDto> hiddenNeuronsToAdd = new ArrayList<NeuronDto>();


			boolean neuronFound = false;

			for(NeuronDto neuron : netDto.getInputLayer()) {
				NeuronIDto neuronI = (NeuronIDto)neuron;
				if(word.equalsIgnoreCase(neuronI.getWord())) {
					inputNeurons.add(neuronI);
					neuronFound = true;
					break;
				}
			}

			if(!neuronFound) {
				// NeuronIDto createdNewNeuronI =
				// neuronService.createNewNeuronI(netDto.neuronsCount + 1,
				// NeuralNetDto.INPUT_LAYER, word);
				NeuronIDto createdNewNeuronI = new NeuronIDto(++netDto.neuronsCount, null, NeuralNetDto.INPUT_LAYER, word);
				inputNeuronsToAdd.add(createdNewNeuronI);
				NeuronDto createdNewNeuron = new NeuronDto(++netDto.neuronsCount, null, NeuralNetDto.HIDDEN_LAYER);
				// createdNewNeuron.s
				hiddenNeuronsToAdd.add(createdNewNeuron);

			}

		netDto.getInputLayer().addAll(inputNeuronsToAdd);
		netDto.getHiddeLayer().addAll(hiddenNeuronsToAdd);

//		netDto.neuronsCount += inputNeuronsToAdd.size() + hiddenNeuronsToAdd.size();

		inputNeurons.addAll(inputNeuronsToAdd);

		return inputNeurons;
	}



}
