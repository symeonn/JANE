package pl.byMarioUltimate.jpa;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 
 * @author Mariusz Lewandowski; Archidoc S.A.
 */
//@Table(name = "ALL_WORDS")
public class WordDetailsEntity {

	public WordDetailsEntity() {
	}

	public WordDetailsEntity(Integer id, Integer dependencyId, Integer weight ) {
		this.id = id;
		this.dependencyId = dependencyId;
		this.weight = weight;

	}

//	@Column(name = "ID")
	private Integer id;

//	@Column(name = "WORD")
	private Integer dependencyId;

	private Integer weight;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WordEntity [id=" + id + ", depId=" + dependencyId + ", weight=" + weight + "]";
	}

	public Integer getDependencyId() {
		return dependencyId;
	}

	public void setDependencyId(Integer dependencyId) {
		this.dependencyId = dependencyId;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

}
