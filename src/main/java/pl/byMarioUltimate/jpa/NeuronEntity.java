package pl.byMarioUltimate.jpa;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 
 * @author Mariusz Lewandowski; Archidoc S.A.
 */
@Table(name = "NEURONS")
public class NeuronEntity {

	public NeuronEntity() {
	}

	
	@Column(name = "ID")
	private Long id;

	
	@Column(name = "CONNECTIONFROM")
	private String connectionFrom;
	
	
	@Column(name = "LAYER")
	private String layer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getConnectionFrom() {
		return connectionFrom;
	}

	public void setConnectionFrom(String connectionFrom) {
		this.connectionFrom = connectionFrom;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layer) {
		this.layer = layer;
	}


	
}
