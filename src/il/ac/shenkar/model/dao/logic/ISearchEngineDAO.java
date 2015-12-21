/**
 * 
 */
package il.ac.shenkar.model.dao.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import il.ac.shenkar.model.dto.DocDetails;
import il.ac.shenkar.model.dto.Word;


/**
 * @author webTech
 *
 */
public interface ISearchEngineDAO extends IDAO<DocDetails> {

	/* Doc Object */
	public static final String	COL_ID				= "id";
	public static final String	COL_LINK			= "link";
	public static final String	COL_SUMMARY 		= "summary";
	public static final String	COL_TOPIC			= "topic";
	public static final String	COL_AUTHOR			= "author";
	public static final String	COL_DATE			= "date";
	public static final String	COL_IS_ACTIVE		= "isActive";
	public static final String	COL_IS_STOP_LIST	= "is_stop_list";

	/* Index doc */
	public static final String	COL_WORD			= "word";
	public static final String 	COL_DOCS 			= "docs";
	
	public DocDetails insertDocDetails(DocDetails docDetails);
	public void updateSummary(String summery, long docId);
	public void insertToIndexTable(List<Word> uniqeWords);
	public List<DocDetails> receiveResultsFromQuery(String query , int isStopList);
	public List<DocDetails> receiveDocList();
	public List<DocDetails> getDocsByIds(String docsIds);
	public List<String> checkSynonyms(List<String> words);
	public List<String> getSynonymsWords(String word);
	public void disableDoc(String doc);
	public List<DocDetails> receiveDocListDisabled();
	public void enableDoc(String doc);
	public boolean checkIfDocsIndexesIsEmpty();
	public Word checkIfWordExistInDB(String word);
	public void updateDocsIdsForExistingWord(Word word);
	public void sortDocsIndexesTable();
	public List<String> getJokerWords(String word);
	public boolean verifyIfStopList(String word);
}
