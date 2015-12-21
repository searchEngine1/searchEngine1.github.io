/**
 * 
 */
package il.ac.shenkar.model.dao;


import il.ac.shenkar.model.dao.impl.mysql.SearchEngineDAO;
import il.ac.shenkar.model.dao.logic.ISearchEngineDAO;

/**
 * @author webTech
 *
 */
public class MySqlDAOFactory extends DAOFactory {

	private MySqlDAOFactory() {
		
	}
	
	private static final class SingletonHolder {
		private static final MySqlDAOFactory INSTANCE = new MySqlDAOFactory();
	}
	
	public static MySqlDAOFactory getInstance() {
		return SingletonHolder.INSTANCE;
	}

	@Override
	public ISearchEngineDAO getSearchEngineDAO() {
		return new SearchEngineDAO();
	}

}
