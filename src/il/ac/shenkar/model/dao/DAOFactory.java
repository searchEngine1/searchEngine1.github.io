/**
 * 
 */
package il.ac.shenkar.model.dao;

import il.ac.shenkar.model.dao.logic.ISearchEngineDAO;

/**
 * @author webTech
 *
 */
public abstract class DAOFactory {

	public static enum Type {
		oracle,
		db2,
		mssql,
		mySQL
	}
	
	public static DAOFactory getDAOFactory(Type which) {
		switch (which) {
		case mySQL:
			return MySqlDAOFactory.getInstance();
		default:
			return null;
		}
	}
	
	public abstract ISearchEngineDAO getSearchEngineDAO();
}
