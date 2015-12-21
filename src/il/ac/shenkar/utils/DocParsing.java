package il.ac.shenkar.utils;

import il.ac.shenkar.model.dao.DAOFactory;
import il.ac.shenkar.model.dao.logic.ISearchEngineDAO;
import il.ac.shenkar.model.dto.DocDetails;
import il.ac.shenkar.model.dto.Word;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class DocParsing {
	private ArrayList<Word> words;
	private ArrayList<Word> uniqeWords;
	private ISearchEngineDAO searchEngineDAO;
	DocDetails docDetails;
	
	public DocParsing() {
		words = new ArrayList<Word>();
		uniqeWords = new ArrayList<Word>();
		searchEngineDAO  = DAOFactory.getDAOFactory(DAOFactory.Type.mySQL).getSearchEngineDAO();
	}

	public void readFilesFromSource(String fileName) throws IOException {
		// read file
		try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/Tomer/workspace/SearchEngine/inventory/" + fileName + ".txt"))) {
			String line;
			String regex = " \t\n\r\f,.:—;()?![]-\"";
			int lineNumber = 0;
			docDetails = new DocDetails();
			docDetails.setLink(fileName);
			StringBuffer summery = new StringBuffer();
			// read line by line
			while ((line = br.readLine()) != null) {
				if (lineNumber < 3) {
					switch (lineNumber) {
						case 0:
							docDetails.setTopic(line);
							break;
						case 1:
							docDetails.setAuthor(line);
							break;
						case 2:
							docDetails.setDate(line);
							docDetails = searchEngineDAO.insertDocDetails(docDetails);
							break;
					}
					lineNumber++;
				} else {
					
					if (lineNumber < 7) {
						summery.append(line).append("\n");
						lineNumber++;
					}

					// split line into tokens without spaces
					StringTokenizer tokenizer = new StringTokenizer(line, regex);
					// read word by word in each line
					while (tokenizer.hasMoreTokens()) {
						String nextWord = tokenizer.nextToken().toLowerCase();
						words.add(new Word(nextWord,String.valueOf(docDetails.getId())));
					}
				}
			}
			
			searchEngineDAO.updateSummary(summery.toString(), docDetails.getId());
		} catch (IOException e) {
			throw e;
		}
	}
	
	public void sortList() { 
		Collections.sort(words,Word.WordComparator);
	}
	
	public void clearMultiple() {
		uniqeWords.add(new Word(words.get(0).getWord(),words.get(0).getDocs().toString()));
		for (int i=1; i < words.size(); i++ ){
			if (words.get(i).getWord().equals(words.get(i-1).getWord() )) {
				uniqeWords.get(uniqeWords.size()-1).addHitByOne();
				String docId = words.get(i).getDocs().toString();
				if (!checkIfDocExist(docId)) {
					uniqeWords.get(uniqeWords.size()-1).appendDoc(docId);
				}
			} else { 
				uniqeWords.add(new Word(words.get(i).getWord(),words.get(i).getDocs().toString()));
			}
		}
		
	}

	public void insertUniqeWordsDb() {
		List<Word> wordsNotExistsInDb = new ArrayList<Word>() ;
		
		if (searchEngineDAO.checkIfDocsIndexesIsEmpty()) {
			searchEngineDAO.insertToIndexTable(uniqeWords);
		} else { 
			for (Word word : uniqeWords) {
				Word wordIsExist = searchEngineDAO.checkIfWordExistInDB(word.getWord());
				if (null != wordIsExist) { // Word Exist in db
					String d1 = word.getDocs().toString();
					String d2 = wordIsExist.getDocs().toString();
					StringBuilder result = new StringBuilder();
	        		StringTokenizer str1 = new StringTokenizer(d1, ",");
	        		StringTokenizer str2 = new StringTokenizer(d2, ",");
            		result.append(d2);
            		boolean orExist = false;
            		while (str1.hasMoreTokens()) {
            			String temp =str1.nextToken();
                		while (str2.hasMoreTokens()) {
                   		     if (str2.nextToken().equals(temp)) {
   	                   		  	orExist = true;
                   		     }
                		}
                		if (!orExist) {
                			if (result.length() > 0)
                				result.append(",").append(temp);
                			else
                				result.append(temp);
                		}
               		    orExist = false;
                		str2 = new StringTokenizer(d2, ",");
            		}
            		wordIsExist.setDocs(result);
					searchEngineDAO.updateDocsIdsForExistingWord(wordIsExist);
				} else { // Word not exist in db 
					wordsNotExistsInDb.add(word);
				}
			}
			if (wordsNotExistsInDb.size() > 0) {
				searchEngineDAO.insertToIndexTable(wordsNotExistsInDb);
			}
		}
		
		
		/// TODO: ALTER COMMAND 
		
	}
	
	private boolean checkIfDocExist(String docId) {
		String docs = uniqeWords.get(uniqeWords.size()-1).getDocs().toString();
		
		StringTokenizer str = new StringTokenizer(docs, ",");
		while (str.hasMoreTokens()) {
		     if (str.nextToken().equals(docId)) {
		    	 return true;
		     }
		}
		
		return false;

	}

	public ArrayList<Word> getWords() {
		return words;
	}

	public void setWords(ArrayList<Word> words) {
		this.words = words;
	}

	public ArrayList<Word> getUniqeWords() {
		return uniqeWords;
	}

	public void setUniqeWords(ArrayList<Word> uniqeWords) {
		this.uniqeWords = uniqeWords;
	}
	
	
}
