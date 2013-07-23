package pl.byMarioUltimate.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pl.byMarioUltimate.SessionJdbcTemplate;
import pl.byMarioUltimate.dto.NeuronDto;
import pl.byMarioUltimate.jpa.NeuronEntity;
import pl.byMarioUltimate.jpa.WordDetailsEntity;

/**
 * 
 * @author Mariusz Lewandowski; byMario
 */
@Repository
@Scope("session")
public class NeuronDao {

	@Autowired
	protected SessionJdbcTemplate sessionJdbcTemplate;

	protected JdbcTemplate getJdbcTemplate() throws Exception {
		sessionJdbcTemplate.initJdbcTemplate("", "");
		return sessionJdbcTemplate.getJdbcTemplate();
	}

	private static final Logger LOGGER = Logger.getLogger(NeuronDao.class);

	public List<NeuronEntity> findInputByNeuronIds(List<Long> neuronIds) {

		String query = "SELECT id, word FROM NEURONS where neuronId in ";

		String params = " (";
		for(Long neuronId : neuronIds) {
			params += neuronId + ",";
		}
		params = params.substring(0, params.length()-1);
		params += ") ";
		
		query += params;
//		if(id != null) {
//			query += "where id = " + id;
//		}
//		List<Object> params = Arrays.asList((Object)neuronId);

		// Object object = null;
		List<NeuronEntity> objectsList = null;

//		Object[] paramsArray = null;
//		if(params != null) {
//			paramsArray = new Object[params.size()];
//			paramsArray = params.toArray(paramsArray);
//		}

		try {

			objectsList = getJdbcTemplate().query(query, new RowMapper<NeuronEntity>() {
				@Override
				public NeuronEntity mapRow(ResultSet rs, int arg1) throws SQLException {
					NeuronEntity dto = new NeuronEntity();
//					dto.setId(rs.getInt("ID"));
//					dto.setNeuronId(rs.getLong("NEURONID"));

					return dto;
				}
			});

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		// return (WordEntity)object;
		return objectsList;
	}

//	public WordEntity findByWord(String word) {
//
//		String query = "SELECT id, word FROM ALL_WORDS WHERE word like '" + word + "'";
//
//		// if(id != null) {
//		// query += "where id = " + id;
//		// }
//		// List<Object> params = Arrays.asList((Object)id);
//		//
//		WordEntity object = null;
//		// List<WordEntity> objectsList = null;
//		//
//		// Object[] paramsArray = null;
//		// if(params != null) {
//		// paramsArray = new Object[params.size()];
//		// paramsArray = params.toArray(paramsArray);
//		// }
//
//		try {
//
//			object = getJdbcTemplate().queryForObject(query, new RowMapper<WordEntity>() {
//				@Override
//				public WordEntity mapRow(ResultSet rs, int arg1) throws SQLException {
//					WordEntity dto = new WordEntity();
//					dto.setId(rs.getInt("ID"));
//					dto.setWord(rs.getString("WORD"));
//					dto.setNeuronId(rs.getLong("NEURONID"));
//
//					return dto;
//				}
//			});
//
//		}
//		catch(Exception e) {
//			e.printStackTrace();
//		}
//		// return (WordEntity)object;
//		return object;
//	}

//	@Transactional(propagation = Propagation.NEVER, rollbackFor = Exception.class)
//	public void saveAll(List<WordEntity> wordsListToImport) throws Exception {
//
//		// LOGGER.info("saving " + wordsListToImport.size() + " objects...");
//
//		String insertIntoQuery = " INSERT INTO ALL_WORDS (WORD) VALUES ";
//
//		for(WordEntity wordEntity : wordsListToImport) {
//			insertIntoQuery += " (\"" + wordEntity.getWord() + "\"), ";
//		}
//
//		insertIntoQuery = insertIntoQuery.substring(0, insertIntoQuery.lastIndexOf(","));
//
//		getJdbcTemplate().execute(insertIntoQuery);
//
//		LOGGER.info("saved!");
//	}

	/**
	 * @param word
	 * @return
	 * @author Mariusz Lewandowski; Archidoc S.A.
	 */
	public List<WordDetailsEntity> findTable(String word) {
		// TODO Auto-generated method stub

		String query = "SELECT * FROM " + word;// + " WHERE word like '" + word
												// + "'";
		List<WordDetailsEntity> object = null;

		try {

			object = getJdbcTemplate().query(query, new RowMapper<WordDetailsEntity>() {
				@Override
				public WordDetailsEntity mapRow(ResultSet rs, int arg1) throws SQLException {
					WordDetailsEntity dto = new WordDetailsEntity();
					dto.setId(rs.getInt("ID"));
					dto.setDependencyId(rs.getInt("DEPENDENCYID"));
					dto.setWeight(rs.getInt("WEIGHT"));

					return dto;
				}
			});

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		// return (WordEntity)object;
		return object;

	}
	
	public Long getNeuronIdByWord(String word){
		
		String query = "SELECT neuronId FROM ALL_WORDS where WORD = '" + word + "'";

		Long neuronId = null;
		try {
			
			neuronId = getJdbcTemplate().queryForLong(query);
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		// return (WordEntity)object;
		
		return neuronId;
	}
	
	public List<NeuronEntity> getNeuronByLayer(String layer){
		
		String query = "select * from NEURONS where LAYER like '" + layer + "'";
		
		List<NeuronEntity> neuronList = null;
		try {
			
			neuronList = getJdbcTemplate().query(query, new RowMapper<NeuronEntity>() {
				@Override
				public NeuronEntity mapRow(ResultSet rs, int arg1) throws SQLException {
					NeuronEntity entity = new NeuronEntity();
					entity.setId(rs.getLong("ID"));
					entity.setConnectionFrom(rs.getString("CONNECTIONFROM"));
					entity.setLayer(rs.getString("LAYER"));

					return entity;
				}
			});
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		// return (WordEntity)object;
		
		return neuronList;
	}
	
	@Transactional(propagation = Propagation.NEVER, rollbackFor = Exception.class)
	public void save(NeuronDto neuronDto) throws Exception {

		String insertIntoQuery = " insert into NEURONS (ID, CONNECTIONFROM, LAYER) VALUES ";

		String values = " ( ";
		
		values += neuronDto.getId() +", \"" + neuronDto.getInputMapAsString() + "\", \"" + neuronDto.getLayer() + "\" ) ";

		insertIntoQuery += values;

		getJdbcTemplate().execute(insertIntoQuery);

		LOGGER.info("saved!");
	}

}