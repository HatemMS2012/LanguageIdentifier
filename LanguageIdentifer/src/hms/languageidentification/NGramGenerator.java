package hms.languageidentification;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NGramGenerator {

	
	public static Map<String, Integer> ngrams(String str, int length) {
		Pattern pattern = Pattern.compile("^_?[^0-9\\?!\\-_/,(){}]*_?$");
		Map<String, Integer> ngramCountMap = new HashMap<String, Integer>();
		char[] chars = str.toCharArray();
	  
	    final int resultCount = chars.length - length + 1;
	    
   
	    for (int i = 0; i < resultCount; i++) {
	        
	        String ngram =  new String(chars, i, length);
	        Matcher matcher = pattern.matcher(ngram);
	        
	        if(!matcher.find()){
	        	System.err.println(ngram);
	        	continue;
	        }
	        ngram =  ngram.replace(" ", "_");
	        if(ngramCountMap.get(ngram)!=null){
	        	ngramCountMap.put(ngram, ngramCountMap.get(ngram)+1);
	        }
	        else{
	        	ngramCountMap.put(ngram, 1);
	        }
	    }
	    
	    return ngramCountMap;
	}
}
