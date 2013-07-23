package pl.byMarioUltimate.dto;

import java.util.List;
import  pl.byMarioUltimate.dto.NeuronDto;

/**
 *  Input neutron
 *  
 * @author Mariusz Lewandowski; byMario
 */
public class NeuronIDto extends NeuronDto{
	
//	List<Connection> inputConnections;
//	Connection outputConnection;
	String word;

	
	public NeuronIDto(Long neuronID, String connectionsFrom, String layer, String word) {
		super(neuronID, connectionsFrom, layer);	
		this.word = word;
		
	}
	
	public NeuronIDto(Long neuronID, String layer, String word) {
		super(neuronID, layer);	
		this.word = word;
		
	}
	
	
//	public NeuronIDto(List<Double> input, long neuronId) {
//		super(input, neuronId);
//		this.inputList = input;
//		process();
//		System.out.println("CREATE nI, input " + inputList.toString()+ "; waga " + weightList.toString());
		
//	}

//	public NeuronIDto(long neuronId) {
//		super(neuronId);
		
//		weightList.add(1d);
//	}
//	public NeuronIDto() {
		
//	}

	/**
	 * @see pl.byMario.dto.NeuronDto#process()
	 */
//	@Override
//	protected void process() {
//		Map<Double, Double> output = new HashMap<Double, Double>();

//		for(Object input : inputList) {
//			Map<Integer, Double> inputMap = (Map<Integer, Double>)input;
//			
//			for(Entry<Integer, Double> element : inputMap.entrySet()) {
//				output += element.getKey()*element.getValue();
//			}
//			
//		}
		
		
//		TEMPORARY as initInput equals input layer output
		
//		output = (Map<Double, Double>)inputList.get(0);
		
//		outputList.add(inputList.get(0));
		
//		System.out.println("PROCESS nI, output: "+ output.toString());

		
//	}
	
//class Connection{
//		
//		long fromId;
////		long toId;
//		
//		double weight;
//		double value;
//		
//	}
//
//
////	public List<Connection> getInputConnections() {
////		return inputConnections;
////	}
//
//	public void setInputConnections(List<Connection> inputConnections) {
////		this.inputConnections = inputConnections;
//	}
//
//
	public String getWord() {
		return word;
	}


	public void setWord(String word) {
		this.word = word;
	}
//	
	
	
	

}
