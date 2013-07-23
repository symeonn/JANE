package pl.byMarioUltimate.jpa;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 
 * @author Mariusz Lewandowski; Archidoc S.A.
 */
@Table(name = "ALL_WORDS")
public class WordEntity {

	public WordEntity() {
	}

	public WordEntity(Integer id, String word) {
		this.id = id;
		this.word = word;

	}

	@Column(name = "ID")
	private Integer id;

	@Column(name = "WORD")
	private String word;
	
	@Column(name = "NEURONID")
	private long neuronId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WordEntity [id=" + id + ", word=" + word + "]";
	}

	public long getNeuronId() {
		return neuronId;
	}

	public void setNeuronId(long neuronId) {
		this.neuronId = neuronId;
	}

}
