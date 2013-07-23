package pl.byMarioUltimate.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
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
import pl.byMarioUltimate.dto.NeuronIDto;
import pl.byMarioUltimate.jpa.WordDetailsEntity;
import pl.byMarioUltimate.jpa.WordEntity;

/**
 * 
 * @author Mariusz Lewandowski; byMario
 */
@Repository
@Scope("prototype")
public class WordDao {

	@Autowired
	protected SessionJdbcTemplate sessionJdbcTemplate;

	protected JdbcTemplate getJdbcTemplate() throws Exception {
		sessionJdbcTemplate.initJdbcTemplate("", "");
		return sessionJdbcTemplate.getJdbcTemplate();
	}

	private static final Logger LOGGER = Logger.getLogger(WordDao.class);

	public List<WordEntity> findById(Integer id) {

		String query = "SELECT id, word FROM ALL_WORDS ";

		if(id != null) {
			query += "where id = " + id;
		}
		List<Object> params = Arrays.asList((Object)id);

		// Object object = null;
		List<WordEntity> objectsList = null;

		Object[] paramsArray = null;
		if(params != null) {
			paramsArray = new Object[params.size()];
			paramsArray = params.toArray(paramsArray);
		}

		try {

			objectsList = getJdbcTemplate().query(query, new RowMapper<WordEntity>() {
				@Override
				public WordEntity mapRow(ResultSet rs, int arg1) throws SQLException {
					WordEntity dto = new WordEntity();
					dto.setId(rs.getInt("ID"));
					dto.setWord(rs.getString("WORD"));

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

	public WordEntity findByWord(String word) {

		String query = "SELECT id, word FROM ALL_WORDS WHERE word like '" + word + "'";

		// if(id != null) {
		// query += "where id = " + id;
		// }
		// List<Object> params = Arrays.asList((Object)id);
		//
		WordEntity object = null;
		// List<WordEntity> objectsList = null;
		//
		// Object[] paramsArray = null;
		// if(params != null) {
		// paramsArray = new Object[params.size()];
		// paramsArray = params.toArray(paramsArray);
		// }

		try {

			object = getJdbcTemplate().queryForObject(query, new RowMapper<WordEntity>() {
				@Override
				public WordEntity mapRow(ResultSet rs, int arg1) throws SQLException {
					WordEntity dto = new WordEntity();
					dto.setId(rs.getInt("ID"));
					dto.setWord(rs.getString("WORD"));
					dto.setNeuronId(rs.getLong("NEURONID"));

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

	@Transactional(propagation = Propagation.NEVER, rollbackFor = Exception.class)
	public void saveAll(List<WordEntity> wordsListToImport) throws Exception {

		// LOGGER.info("saving " + wordsListToImport.size() + " objects...");

		String insertIntoQuery = " INSERT INTO ALL_WORDS (WORD) VALUES ";

		for(WordEntity wordEntity : wordsListToImport) {
			insertIntoQuery += " (\"" + wordEntity.getWord() + "\"), ";
		}

		insertIntoQuery = insertIntoQuery.substring(0, insertIntoQuery.lastIndexOf(","));

		getJdbcTemplate().execute(insertIntoQuery);

		LOGGER.info("saved!");
	}

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
	
	public String findWordByNeuronId(Long neuronId){
		
		String word = null;
		
		String query = "select WORD from ALL_WORDS where NEURONID = " + neuronId;

		try {

			word = getJdbcTemplate().queryForObject(query, String.class);

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		// return (WordEntity)object;
		
		return word;
	}
	
	public void updateWordByNeuron(NeuronIDto neuron) throws Exception{
		
		String updateQuery = " update ALL_WORDS set NEURONID = " + neuron.getId() + " where WORD like \"" + neuron.getWord() + "\"";

//		String values = " ( ";
		
//		values += neuronDto.getNeuronID() +", \"" + neuronDto.getInputMapAsString() + "\", \"" + neuronDto.getLayer() + "\" ) ";
//		for(WordEntity wordEntity : wordsListToImport) {
//			insertIntoQuery += " (\"" + wordEntity.getWord() + "\"), ";
//		}

//		insertIntoQuery += values;

		getJdbcTemplate().update(updateQuery);
		
	}
	

}