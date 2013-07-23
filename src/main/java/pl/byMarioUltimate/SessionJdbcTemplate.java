package pl.byMarioUltimate;

import javax.annotation.Resource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
//import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

/**
 * Klasa opakowująca obiekt 'JdbcTemplate' zainicjalizowany parametrami
 * połączenia do bazy związanymi z zalogowanym do INDO użytkownikiem.
 * 
 * @author Marcin Wanke
 * 
 */
@Component
//@Scope("session")
public class SessionJdbcTemplate {

	private JdbcTemplate jdbcTemplate;
	private boolean initialized;

	@Autowired
	private DataSourceTransactionManager transactionManager;

	@Autowired
	private DataSourceTransactionManager generalTransactionManager;

	@Resource(name = "janeDataSource")
	private BasicDataSource dataSource;

	/**
	 * Metoda inicjalizuje sesyjny obiekt realizujący połączenie z bazą
	 * danych na podstawie parametrów użytkownika pobranych podczas logowania
	 * do aplikacji. Każdy użytkownik Indo posiada przypisanego odrębnego
	 * użytkownika bazodanowego. Ten mechanizm jest wykorzystywany do zapisu
	 * historii za pomocą triggerów.
	 * 
	 * @param userName
	 * @param password
	 * @throws Exception
	 */
	public void initJdbcTemplate(String userName, String password) throws Exception {

		/*
		 * "[...]Ten mechanizm jest wykorzystywany do zapisu historii za pomocą triggerów.[...]"
		 * To nie my go wymyśliliśmy tylko .NETy, więc nie sądzcie nas za
		 * to! Przenoszenie jakiejkolwiek logiki biznesowej na BD to zawsze Z�?O
		 * ..
		 */
		if(!initialized) {
			// throw new
			// Exception("Obiekt został już zainicjalizowany. Nie można zmienić parametrów połączenia do BD dla uzytkownika ["
			// + userName + "]");
			// }

			// BasicDataSource bds = dataSource;
			// BasicDataSource sessionBds = new BasicDataSource();
			// sessionBds.setDriverClassName(bds.getDriverClassName());
			// sessionBds.setUrl(bds.getUrl());
			// sessionBds.setUsername(userName);
			// sessionBds.setPassword(password);

			jdbcTemplate = new JdbcTemplate(dataSource);

			transactionManager.setDataSource(dataSource);
			initialized = true;
		}
	}

	public boolean isInicjalized() {
		return initialized;
	}

	public void reset() {
		if(initialized) {
			try {
				((BasicDataSource)jdbcTemplate.getDataSource()).close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			jdbcTemplate = new JdbcTemplate(dataSource);
			transactionManager.setDataSource(dataSource);
			initialized = false;
		}
	}

	public JdbcTemplate getJdbcTemplate() throws Exception {
		if(!initialized) {
			throw new Exception("Obiekt JdbcTemplate nie został zainicjalizowany.");
		}
		return jdbcTemplate;
	}

}
