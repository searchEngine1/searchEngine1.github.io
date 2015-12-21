package il.ac.shenkar.services;

import il.ac.shenkar.common.IAppConstants;
import il.ac.shenkar.model.dao.DAOFactory;
import il.ac.shenkar.model.dao.impl.mysql.SearchEngineDAO;
import il.ac.shenkar.model.dao.logic.ISearchEngineDAO;
import il.ac.shenkar.model.dto.DocDetails;
import il.ac.shenkar.utils.DocParsing;
import il.ac.shenkar.utils.ExpressionParser;
import il.ac.shenkar.utils.KnuthMorrisPratt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.catalina.startup.HomesUserDatabase;

import com.sun.jersey.api.view.Viewable;

@Path("/home-page")
public class HomePageService implements IAppConstants {
	
	@GET
	@Path("/show")
	public Response homePage() {

		ResponseBuilder responseBuilder = null;
		try {
			
/*			DocParsing docParsing = new DocParsing();
			docParsing.readFilesFromSource("Avicii - The Nights");
			docParsing.readFilesFromSource("David Guetta - Hey Mama");
			docParsing.readFilesFromSource("Major Lazer - Lean On");
			docParsing.sortList();
			docParsing.clearMultiple();
			docParsing.insertUniqeWordsDb();*/
			
			responseBuilder = Response.ok(new Viewable(HOME_PAGE_RELATIVE_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseBuilder.build();
	}
	@GET
	@Path("/home")
	public Response home() {
		ResponseBuilder responseBuilder = null;

		responseBuilder = Response.ok(new Viewable(HOME_RELATIVE_PATH));
		return responseBuilder.build();

	}
	@GET
	@Path("/admin")
	public Response admin() {
		ResponseBuilder responseBuilder = null;

		responseBuilder = Response.ok(new Viewable(ADMIN_RELATIVE_PATH));
		return responseBuilder.build();

	}
	@GET
	@Path("/login")
	public Response login() {
		ResponseBuilder responseBuilder = null;

		responseBuilder = Response.ok(new Viewable(LOGIN_PAGE_RELATIVE_PATH));
		return responseBuilder.build();

	}
	
	@GET
	@Path("/getFileNames")
	public List<String> getFileNames() {
		
		File folder = new File("C:/Users/Tomer/workspace/SearchEngine/source");
		File[] listOfFiles = folder.listFiles();
		List<String> filesName = new ArrayList<String>();
		;

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String file_name = listOfFiles[i].getName();
				file_name = file_name.replace(".txt", "");
				filesName.add(file_name);
			}
		}

		return filesName;

	}

	@GET
	@Path("/upload")
	public boolean upload(@QueryParam("files") String files) {
		DocParsing docParsing = new DocParsing();
		InputStream inStream = null;
		OutputStream outStream = null;
		String[] fileAfterSplit = files.split(",");
		try {
			for (String file : fileAfterSplit){
			File afile = new File("C:/Users/Tomer/workspace/SearchEngine/source/" + file + ".txt");
			File bfile = new File("C:/Users/Tomer/workspace/SearchEngine/inventory/" + file + ".txt");
			inStream = new FileInputStream(afile);
			outStream = new FileOutputStream(bfile);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {

				outStream.write(buffer, 0, length);

			}

			inStream.close();
			outStream.close();

			// delete the original file
			afile.delete();
			
			
			docParsing.readFilesFromSource(file);
			}
			docParsing.sortList();
			docParsing.clearMultiple();
			docParsing.insertUniqeWordsDb();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}
	
	
	@GET
	@Path("/queries")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DocDetails> docs(@QueryParam("query") String query) {
		String result;
        String[] input = query.split(" ");
        String[] output = null;
        if (input.length > 1) {
	       output = ExpressionParser.infixToRPN(input);  
	
	        // Feed the RPN string to RPNtoDouble to give result  
	        result = ExpressionParser.RPNtoDouble( output );    
        } else {
        	result = getDocsIds(query);
        }
		
		List<DocDetails> docsList = getDataFromDB(result);
		
		return docsList;
		
	}
	
	@GET
	@Path("/words")
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> words(@QueryParam("query") String query) {
		String[] temp =query.split(" ");
		boolean jokerWord = false;
		List<String> words = new ArrayList<String>();
		List<String> jokerWords = new ArrayList<String>();
		boolean isStopList = false;
		ISearchEngineDAO searchEngineDAO  = DAOFactory.getDAOFactory(DAOFactory.Type.mySQL).getSearchEngineDAO();
		
		for (String string : temp) {
			
			if (string.startsWith("\"") && string.endsWith("\"")) {
	    		string = string.replace("\"", "");
	    		isStopList = true;
	    	}
	    	
	      	if (string.contains("*")) {
	      		jokerWord = true;
	    	}
	    	
	    	
			if (!string.matches("[,/!%<>{}()]\"") && !"or".equalsIgnoreCase(string) 
					&&  !"and".equalsIgnoreCase(string) && !"not".equalsIgnoreCase(string)) {
				
				if (jokerWord) {
					jokerWords.addAll(searchEngineDAO.getJokerWords(string));
				} else {
				
					boolean dbStopListCheck = searchEngineDAO.verifyIfStopList(string);
					
					if (isStopList && dbStopListCheck){
						words.add(string);
					}
					
					if (!isStopList && !dbStopListCheck ) {
						words.add(string);
					}
				}
				
			}
			jokerWord = false;
			isStopList = false;
		}
		
		
		words = searchEngineDAO.checkSynonyms(words);
		words.addAll(jokerWords);
		return words;
	}
	
	
    private String getDocsIds(String word) {
    	ISearchEngineDAO searchEngineDAO  = DAOFactory.getDAOFactory(DAOFactory.Type.mySQL).getSearchEngineDAO();
    	StringBuilder docsIds = new StringBuilder();
    	int isStoplist = 0;
    	List<String> synonymsWords = null;
		List<DocDetails> docsDetailsList = new ArrayList<DocDetails>();
		List<DocDetails> docsList = new ArrayList<DocDetails>();
    	if (word.startsWith("\"") && word.endsWith("\"")) {
    		isStoplist = 1;
    		word = word.replace("\"", "");
    	}
    	
      	if (word.contains("*")) {
	    	List<String> jokerWords = searchEngineDAO.getJokerWords(word);
	    	for (String jokerWord : jokerWords) {
	    		docsDetailsList.addAll(searchEngineDAO.receiveResultsFromQuery(jokerWord, 0));
			}
	    	
    	} else {
      	
	    	synonymsWords = searchEngineDAO.getSynonymsWords(word);
	    	for (String synonymsWord : synonymsWords) {
	    		docsDetailsList.addAll(searchEngineDAO.receiveResultsFromQuery(synonymsWord, 0));
			}
	    	docsList = searchEngineDAO.receiveResultsFromQuery(word,isStoplist);
    	}
      	
    	boolean exists = false;
    	for (DocDetails synDocDetails : docsDetailsList) {
			for (DocDetails docDetails : docsList) {
				if (synDocDetails.equals(docDetails)) {
					exists = true;
				}
			}
			
			if (!exists) {
				docsList.add(synDocDetails);
			}
			exists = false;
		}
    	
    	int count =0;
    	for (DocDetails docDetails : docsList) {
    		count++;
    		if (count == 1) {
    			docsIds.append(docDetails.getId());
    		}else {
    			docsIds.append(",").append(docDetails.getId());
    		}
		}
    	
		return docsIds.toString();
	}

    @GET
	@Path("/getDocList")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DocDetails> getDocList() {
		
		ISearchEngineDAO searchEngineDAO  = DAOFactory.getDAOFactory(DAOFactory.Type.mySQL).getSearchEngineDAO();
    	List<DocDetails> docsList = searchEngineDAO.receiveDocList();
		return docsList;

	}
	
	@GET
	@Path("/getdocListEnable")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DocDetails> getdocListEnabled() {
		
    	ISearchEngineDAO searchEngineDAO  = DAOFactory.getDAOFactory(DAOFactory.Type.mySQL).getSearchEngineDAO();
    	List<DocDetails> docsList = searchEngineDAO.receiveDocListDisabled();
		return docsList;

	}
	
	
	
	
	@GET
	@Path("/disableDoc")
	@Produces(MediaType.APPLICATION_JSON)
	public void disableDoc(@QueryParam("docId") String doc) {
		ISearchEngineDAO searchEngineDAO  = DAOFactory.getDAOFactory(DAOFactory.Type.mySQL).getSearchEngineDAO();
		searchEngineDAO.disableDoc(doc);
	}
	@GET
	@Path("/enableDoc")
	@Produces(MediaType.APPLICATION_JSON)
	public void enableDoc(@QueryParam("docId") String doc) {
		ISearchEngineDAO searchEngineDAO  = DAOFactory.getDAOFactory(DAOFactory.Type.mySQL).getSearchEngineDAO();
		searchEngineDAO.enableDoc(doc);
	}


	
	private List<DocDetails> getDataFromDB(String docsIds) {
		ISearchEngineDAO searchEngineDAO  = DAOFactory.getDAOFactory(DAOFactory.Type.mySQL).getSearchEngineDAO();
		List<DocDetails> docsList = searchEngineDAO.getDocsByIds(docsIds);
		return docsList;
		
	}
	
	@GET
	@Path("/getSong")
	@Produces(MediaType.APPLICATION_JSON)
	public DocDetails song(@QueryParam("link") String link) throws IOException {
		DocDetails docDetails = null;
		try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/Tomer/workspace/SearchEngine/inventory/" + link + ".txt"))) {
			String line;
			int lineNumber = 0;
			docDetails = new DocDetails();
			StringBuffer song = new StringBuffer();
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
							break;
					}
					lineNumber++;
				} else {
						song.append(line);
						song.append("\n");
				}

			}
			docDetails.setSummary(song.toString());
			
		} catch (IOException e) {
			throw e;
		}
		
		return docDetails;
		
	}
	
	@GET
	@Path("/markers")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Integer> locations(@QueryParam("text") String text, @QueryParam("pattern") String pattern) {
		KnuthMorrisPratt kmp = new KnuthMorrisPratt(pattern.toLowerCase());
		List<Integer> locations = kmp.getWordsLocation(text.toLowerCase(), pattern.toLowerCase());
		return locations;
		
	}
	
}
