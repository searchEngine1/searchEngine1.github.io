package il.ac.shenkar.model.connection;

import il.ac.shenkar.common.IAppConstants;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class ConnectionManager {
	public static Connection getConnection() {
		return getConnection(ResourceBundle.getBundle(IAppConstants.DB_MYSQL).getString(IAppConstants.RES_KEY_DATASOURCE));
	}
	
	/* */
	public static Connection getConnection(String datasourceName) {
		Connection connection = null;
		
		try {
			
			Context initContext = new InitialContext();
			Context envContext  = (Context) initContext.lookup("java:/comp/env");
			DataSource dataSource = (DataSource) envContext.lookup(datasourceName);
			connection = dataSource.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}

	public static String getQuery(String queryKey) {
		return ResourceBundle.getBundle(IAppConstants.DB_MYSQL).getString(queryKey);
	}
	
	
	/**
	 * Closes the given resources.
	 * @param connection
	 * @param statement
	 * @param resultSet
	 */
	public static void closeResources(Connection connection, Statement statement, ResultSet resultSet) {
		if (null != connection) {
			try {
				connection.close();
			} catch (SQLException e) {}
		}
		closeStatement(statement);
		if (null != resultSet) {
			try {
				resultSet.close();
			} catch (SQLException e) {}
		}
	}
	
	/**
	 * Closes the given resources.
	 * @param connection
	 * @param statement
	 * @param resultSet
	 */
	public static void closeResources(Connection connection, Statement statement) {
		if (null != connection) {
			try {
				connection.close();
			} catch (SQLException e) {}
		}
		closeStatement(statement);
	}
	
	/**
	 * Closes the given resources.
	 * @param connection
	 * @param statement
	 * @param resultSet
	 */
	public static void closeResources(Connection connection, Statement statement, Statement secondStatement, Statement thirdStatement) {
		if (null != connection) {
			try {
				connection.close();
			} catch (SQLException e) {}
		}
		closeStatement(statement);
		closeStatement(secondStatement);
		closeStatement(thirdStatement);
	}
	
	private static void closeStatement(Statement statement) {
		if (null != statement) {
			try {
				statement.close();
			} catch (SQLException e) {}
		}
	}
	
}
