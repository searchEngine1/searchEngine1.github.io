/**
 * 
 */
package il.ac.shenkar.model.dao.impl.mysql;

import il.ac.shenkar.common.IAppConstants;
import il.ac.shenkar.model.connection.ConnectionManager;
import il.ac.shenkar.model.dao.logic.ISearchEngineDAO;
import il.ac.shenkar.model.dto.DocDetails;
import il.ac.shenkar.model.dto.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import com.sun.research.ws.wadl.Doc;


/**
 * @author webTech
 */
public class SearchEngineDAO implements ISearchEngineDAO, IAppConstants {

	@Override
	public Set<DocDetails> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocDetails getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocDetails save(DocDetails toSave) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DocDetails[] save(DocDetails[] toSave) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(DocDetails toDelete) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DocDetails insertDocDetails(DocDetails docDetails) {
		Connection connection = null;
		PreparedStatement statement = null;
		String sql = ConnectionManager.getQuery(RES_KEY_INSERT_DOC_DETAILS);
		
		try {
			connection = ConnectionManager.getConnection();
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, docDetails.getTopic());
			statement.setString(2, docDetails.getAuthor());
			statement.setString(3, docDetails.getLink());
			statement.setString(4, docDetails.getDate());
			statement.setString(5, docDetails.getSummary());
			if  (statement.executeUpdate() > 0) { 
	            ResultSet resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                	docDetails.setId(resultSet.getLong(1));
                }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
		
		return docDetails;
	}

	@Override
	public void updateSummary(String summery, long docId) {
		Connection connection = null;
		PreparedStatement statement = null;
		String sql = ConnectionManager.getQuery(RES_KEY_UPDATE_SUMMARY);
		
		try {
			connection = ConnectionManager.getConnection();
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, summery);
			statement.setLong(2, docId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
	
	}

	@Override
	public void insertToIndexTable(List<Word> uniqeWords) {
		Connection connection = null;
		PreparedStatement statement = null;
		String sql = ConnectionManager.getQuery(RES_KEY_INSERT_TO_INDEX_TABLE);
		
		try {
			connection = ConnectionManager.getConnection();
			statement = (PreparedStatement) connection.prepareStatement(sql);
			int isStopList = 0;
			for (Word word : uniqeWords) {
				
				if (checkIfStopList(word.getWord(),connection)) {
					isStopList = 1;
				} else {
					isStopList = 0;
				}
				
				statement.setString(1, word.getWord());
				statement.setInt(2, isStopList);
				statement.setString(3, word.getDocs().toString());
				statement.executeUpdate();
				
			}


		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
				
	}

	
	private boolean checkIfStopList(String word ,Connection connection)  {
		PreparedStatement statement = null;
		int count = 0;
		ResultSet resultSet;
		String sql = ConnectionManager.getQuery(RES_KEY_COUNT_OCCURENCE_IN_STOP_LIST);
		try {
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, word);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {}
			}
		}
		
		return(count > 0) ? true: false;
		
	}

	@Override
	public List<DocDetails> receiveResultsFromQuery(String query, int isStopList) {

		List<DocDetails> docsList = new ArrayList<DocDetails>();
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = ConnectionManager.getQuery(RES_KEY_SELECT_ONE_WORD);
		
		try {
			connection = ConnectionManager.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, query);
			statement.setInt(2, isStopList);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String docs = resultSet.getString(COL_DOCS);
				StringTokenizer str = new StringTokenizer(docs, ",");
				while (str.hasMoreTokens()) {
					DocDetails docDetails = getDocDetailsFromDB(Long.parseLong(str.nextToken()),connection);
					if (null != docDetails) { 
						docsList.add(docDetails);
					}
					
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
		
		return docsList;
		
	}

	public List<DocDetails> receiveDocList(){
		List<DocDetails> docsList = new ArrayList<DocDetails>();
		DocDetails docDetails = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			String sql = ConnectionManager.getQuery(RES_KEY_SELECT_DOCS);
			connection = ConnectionManager.getConnection();
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				docDetails = buildDocDetails(resultSet);
				if (null != docDetails) { 
					docsList.add(docDetails);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
		return docsList;
	}
	
	
	public List<DocDetails> receiveDocListDisabled(){

		List<DocDetails> docsList = new ArrayList<DocDetails>();
		DocDetails docDetails = null;

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			String sql = ConnectionManager.getQuery(RES_KEY_SELECT_DOCS_DISABLED);
			connection = ConnectionManager.getConnection();
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				docDetails = buildDocDetails(resultSet);
				if (null != docDetails) { 
					docsList.add(docDetails);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
		return docsList;
	}
	
	

	@Override
	public void disableDoc(String doc){
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			String sql = ConnectionManager.getQuery(RES_KEY_UPDATE_DISABLE_DOC);
			connection = ConnectionManager.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, doc);
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
	}

	@Override
	public void enableDoc(String doc){
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			String sql = ConnectionManager.getQuery(RES_KEY_UPDATE_ENABLE_DOC);
			connection = ConnectionManager.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, doc);
			statement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
	}
	
	
	
	private DocDetails getDocDetailsFromDB(long docId, Connection connection) {
		
		DocDetails docDetails = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = ConnectionManager.getQuery(RES_KEY_SELECT_DOCS_BY_ID);
		
		try {
			statement = connection.prepareStatement(sql);
			statement.setLong(1, docId);
			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				docDetails = buildDocDetails(resultSet);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			if (null != statement) {
				try {
					statement.close();
				} catch (SQLException e) {}
			}
		}
		
		return docDetails;
	}

	@Override
	public List<DocDetails> getDocsByIds(String docsIds) {
		
		List<DocDetails> docsList = new ArrayList<DocDetails>();
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			StringTokenizer str = new StringTokenizer(docsIds, ",");
			while (str.hasMoreTokens()) {
				DocDetails docDetails = getDocDetailsFromDB(Long.parseLong(str.nextToken()),connection);
				if (null != docDetails)
				docsList.add(docDetails);
			}
		}
		finally {
			if (null != connection) {
				try {
					connection.close();
				} catch (SQLException e) {}
			}
		}
		
		return docsList;
		
	}
	
	
	@Override
	public List<String> checkSynonyms(List<String> words) {
		List<String> temp = new ArrayList<>();
		temp.addAll(words);
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = ConnectionManager.getQuery(RES_KEY_SELECT_SYNONYMS);
		
		try {
			connection = ConnectionManager.getConnection();
			statement = connection.prepareStatement(sql);
			for (String string : temp) {
				statement.setString(1, string);
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					String word = resultSet.getString(COL_WORD);
					words.add(word);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
		return words;
		
	}
	
	@Override
	public List<String> getSynonymsWords(String word) {
		List<String> synonymsWords = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = ConnectionManager.getQuery(RES_KEY_SELECT_SYNONYMS);
		
		try {
			connection = ConnectionManager.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, word);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				synonymsWords.add(resultSet.getString(COL_WORD));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
		return synonymsWords;
	}
	
	
	private DocDetails buildDocDetails(ResultSet resultSet) throws SQLException {
		DocDetails docDetails = new DocDetails();
		
		docDetails.setId(resultSet.getLong(COL_ID));
		docDetails.setAuthor(resultSet.getString(COL_AUTHOR));
		docDetails.setDate(resultSet.getString(COL_DATE));
		docDetails.setLink(resultSet.getString(COL_LINK));
		docDetails.setSummary(resultSet.getString(COL_SUMMARY));
		docDetails.setTopic(resultSet.getString(COL_TOPIC));
		
		return docDetails;
	}

	
	private Word buildWord(ResultSet resultSet) throws SQLException {
		Word word = new Word();
		word.setId(resultSet.getLong(COL_ID));
		word.setWord(resultSet.getString(COL_WORD));
		word.setDocs(new StringBuilder(resultSet.getString(COL_DOCS)));
		
		return word;
	}
	
	@Override
	public boolean checkIfDocsIndexesIsEmpty() {
		Connection connection = null;
		PreparedStatement statement = null;
		int count = 0;
		ResultSet resultSet;
		String sql = ConnectionManager.getQuery(RES_KEY_COUNT_DOCS_INDEXES);
		try {
			connection = ConnectionManager.getConnection();
			statement = (PreparedStatement) connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
		
		return(count == 0) ? true: false;
	}

	@Override
	public Word checkIfWordExistInDB(String word) {
		Word wordObj = null;
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		String sql = ConnectionManager.getQuery(RES_KEY_SELECT_OBJECT_WORD);
		
		try {
			connection = ConnectionManager.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, word);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				wordObj = buildWord(resultSet);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
		
		return wordObj;
		
	}

	@Override
	public void updateDocsIdsForExistingWord(Word word) {
		Connection connection = null;
		PreparedStatement statement = null;
		String sql = ConnectionManager.getQuery(RES_KEY_UPDATE_DOCS_FOR_EXISTING_WORD);
		
		try {
			connection = ConnectionManager.getConnection();
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, word.getDocs().toString());
			statement.setLong(2, word.getId());
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
		
	}

	@Override
	public void sortDocsIndexesTable() {
		Connection connection = null;
		PreparedStatement statement = null;
		String sql = ConnectionManager.getQuery(RES_KEY_SORT_DOCS_INDEXES);
		
		try {
			connection = ConnectionManager.getConnection();
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
		
	}

	@Override
	public List<String> getJokerWords(String word) {
		List<String> jokerWords = new ArrayList<String>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		word = word.replace('*', '%');
		String sql = ConnectionManager.getQuery(RES_KEY_SELECT_JOKER_WORD);
		
		try {
			connection = ConnectionManager.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, word);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				jokerWords.add(resultSet.getString(COL_WORD));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
		return jokerWords;
	}

	@Override
	public boolean verifyIfStopList(String word) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		int isStopList = 0;
		String sql = ConnectionManager.getQuery(RES_KEY_VERIFY_STOP_LIST);
		
		try {
			connection = ConnectionManager.getConnection();
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, word);
			resultSet= statement.executeQuery();
			if (resultSet.next()) {
				isStopList = resultSet.getInt(COL_IS_STOP_LIST);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			ConnectionManager.closeResources(connection, statement);
		}
		return (isStopList == 1) ? true : false;
	}

}


