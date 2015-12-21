package il.ac.shenkar.utils;

import il.ac.shenkar.model.dao.DAOFactory;
import il.ac.shenkar.model.dao.logic.ISearchEngineDAO;
import il.ac.shenkar.model.dto.DocDetails;

import java.util.*;  
   
public class ExpressionParser  {  
    // Associativity constants for operators  
    private static final int LEFT_ASSOC  = 0;  
    private static final int RIGHT_ASSOC = 1;  
   
    // Operators  
    private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();  
    static   
    {  
        // Map<"token", []{precendence, associativity}>  
        OPERATORS.put("OR", new int[] { 0, LEFT_ASSOC });  
        OPERATORS.put("NOT", new int[] { 0, LEFT_ASSOC });  
        OPERATORS.put("AND", new int[] { 5, LEFT_ASSOC }); 
        OPERATORS.put("or", new int[] { 0, LEFT_ASSOC });  
        OPERATORS.put("not", new int[] { 0, LEFT_ASSOC });  
        OPERATORS.put("and", new int[] { 5, LEFT_ASSOC });    
    }  
   
    // Test if token is an operator  
    private static boolean isOperator(String token)   
    {  
        return OPERATORS.containsKey(token);  
    }  
   
    // Test associativity of operator token  
    private static boolean isAssociative(String token, int type)   
    {  
        if (!isOperator(token))   
        {  
            throw new IllegalArgumentException("Invalid token: " + token);  
        }  
          
        if (OPERATORS.get(token)[1] == type) {  
            return true;  
        }  
        return false;  
    }  
   
    // Compare precedence of operators.      
    private static final int cmpPrecedence(String token1, String token2)   
    {  
        if (!isOperator(token1) || !isOperator(token2))   
        {  
            throw new IllegalArgumentException("Invalid tokens: " + token1  
                    + " " + token2);  
        }  
        return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];  
    }  
   
    // Convert infix expression format into reverse Polish notation  
    public static String[] infixToRPN(String[] inputTokens)   
    {  
        ArrayList<String> out = new ArrayList<String>();  
        Stack<String> stack = new Stack<String>();  
          
        // For each token  
        for (String token : inputTokens)   
        {  
            // If token is an operator  
            if (isOperator(token))   
            {    
                // While stack not empty AND stack top element   
                // is an operator  
                while (!stack.empty() && isOperator(stack.peek()))   
                {                      
                    if ((isAssociative(token, LEFT_ASSOC)         &&   
                         cmpPrecedence(token, stack.peek()) <= 0) ||   
                        (isAssociative(token, RIGHT_ASSOC)        &&   
                         cmpPrecedence(token, stack.peek()) < 0))   
                    {  
                        out.add(stack.pop());     
                        continue;  
                    }  
                    break;  
                }  
                // Push the new operator on the stack  
                stack.push(token);  
            }   
            // If token is a left bracket '('  
            else if (token.equals("("))   
            {  
                stack.push(token);  //   
            }   
            // If token is a right bracket ')'  
            else if (token.equals(")"))   
            {                  
                while (!stack.empty() && !stack.peek().equals("("))   
                {  
                    out.add(stack.pop());   
                }  
                stack.pop();   
            }   
            // If token is a number  
            else   
            {  
                out.add(token);   
            }  
        }  
        while (!stack.empty())  
        {  
            out.add(stack.pop());   
        }  
        String[] output = new String[out.size()];  
        return out.toArray(output);  
    }  
      
    public static String RPNtoDouble(String[] tokens)  
    {          

        Stack<String> stack = new Stack<String>();  

        // For each token   
        for (String token : tokens)   
        {  
            // If the token is a value push it onto the stack  
            if (!isOperator(token))   
            {  
                stack.push(getDocsIds(token));   

            }  
            else  
            {  
            	StringBuilder result  = new StringBuilder();
                // Token is an operator: pop top two entries  
                String d2 = stack.pop();
                String d1 =  stack.pop();
                
        		StringTokenizer str1 = new StringTokenizer(d1, ",");
        		StringTokenizer str2 = new StringTokenizer(d2, ",");
                switch(token.toUpperCase()) {
                	case "OR" : case "or" :
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
                		break;
                	case "AND" : case "and" :
                		int count =0;
                  		while (str1.hasMoreTokens()) {
                			String temp =str1.nextToken();
                    		while (str2.hasMoreTokens()) {
                    			String x = str2.nextToken();
	                   		     if (x.equals(temp)) {
	                   		    	count++;
	                   		    	if (count == 1) {
	                   		    		result.append(temp);
	                   		    	}else {
	                   		    		result.append(",").append(temp);
	                   		    	}
	                   		    	break;
	                   		     }
                    		}
                    		str2 = new StringTokenizer(d2, ",");
                		}
                		break;
                	case "NOT" : case "not" :
                		boolean exist = false;
                		while (str1.hasMoreTokens()) {
                			String temp =str1.nextToken();
                    		while (str2.hasMoreTokens()) {
	                   		     if (str2.nextToken().equals(temp)) {
	                   		    	exist = true;
	                   		    	break;
	                   		     }
                    		}
                    		if (!exist) {
                    			if (result.length() > 0)
                    				result.append(",").append(temp);
                    			else
                    				result.append(temp);
                    		}
                    		exist = false;
                    		str2 = new StringTokenizer(d2, ",");
                		}
                		break;
                }
                // Push result onto stack  
                stack.push( String.valueOf( result ));                                                  
            }                          
        }          
          
        return stack.pop();  
    }  
    
    public static String getDocsIds(String word) {
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
}  

