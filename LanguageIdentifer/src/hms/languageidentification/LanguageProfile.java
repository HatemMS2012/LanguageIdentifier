package hms.languageidentification;

import hms.languageidentification.util.CollectionsUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * A LanguageProfile generates n-grams for a given text and calculates their number of occurrence in it.
 * It can identify its own language by comparing its LanguageProfile with a collection of predefined LanguageProfiles.
 * 
 * @author Hatem Mousselly-Sergieh
 *
 */
public class LanguageProfile {
	
	

	//N-grams containing any of the symbols defined by this regular expression will be discarded
	private static final String SOTP_SYMBOL_PATTERN = "^_?[^0-9\\?!\\-_/,;.:§%?\"'|~^°(){}\\[\\]]*_?$"; 
	
	private final int DEFAULT_MAX_N_GRAME = 3 ; //In our predefined language n-gram files the max for n is 3
	private final int DEFAULT_MIN_N_GRAME = 1 ;
	private String profileLanguageEnName ; //The name of the identified language in English, e.g., German
	private String profileLanguageOrgName ; //The original name of the identified language, e.g., Deutsch
	private Map<String, Integer> ngramOccurenceMap; //A map of n-grams and the corresponding occurrences
	
	
	/**
	 * Create a LanguageProfile for a given text
	 * The language profile consist of n-grams and the corresponding counts in the given text
	 * @param text
	 */
	public void createProfile(String text){
		
		text = text.toLowerCase();
	
		Map<String, Integer> ngramsUnsorted = generateAllNGrams(text,DEFAULT_MIN_N_GRAME,DEFAULT_MAX_N_GRAME);
		
		this.ngramOccurenceMap = new LinkedHashMap<String, Integer> (CollectionsUtil.sortUsingComparator(ngramsUnsorted,false));
		
	}
	
	
	
	/**
	 * Generate a map of n-grams of a given length and the corresponding counts for a given text
	 * @param text
	 * @param length
	 * @return
	 */
	public  Map<String, Integer> generateNGrams(String text, int length) {
		
		Map<String, Integer> ngramCountMap = new HashMap<String, Integer>();
		
		
		Pattern pattern = Pattern.compile(SOTP_SYMBOL_PATTERN);
		
		char[] chars = text.toCharArray();
	  
	    final int resultCount = chars.length - length + 1;
	    
   
	    for (int i = 0; i < resultCount; i++) {
	        
	        String ngram =  new String(chars, i, length);
	        Matcher matcher = pattern.matcher(ngram);
	        
	        //remove n-grams that math the above defined regular experssion. 
	        if(!matcher.find()){
	        	continue;
	        }

	        //Replace spaces in the n-gram with "_"
	        ngram =  ngram.replace(" ", "_");
	        
	        
	        Integer nGramCount = ngramCountMap.get(ngram);
	        
	        if(nGramCount!=null){

	        	ngramCountMap.put(ngram, nGramCount+1);
	        }
	        else{
	        	ngramCountMap.put(ngram, 1);
	        }
	        
	    }
	    
	    return ngramCountMap;
	}
	
	/**
	 * Generate all n-grams for n = start, ... to n = max for a given text
	 * @param text
	 * @param start
	 * @param max
	 */
	public Map<String, Integer> generateAllNGrams(String text, int start, int max){
	
		Map<String, Integer> allNgramsMapUnsorted = new HashMap<String, Integer>();
		
		for (int i = start; i <= max; i++) {
			
			allNgramsMapUnsorted.putAll(generateNGrams(text, i));
			
		}
		return allNgramsMapUnsorted ;
				
	}
	
	
	
	/**
	 * Identify the language of the current LanguageProfile by comparing the LanguageProfile (i.e., n-grams counts) 
	 * of the current instance to a collection of given LanguageProfiles
	 * @param profiles
	 */
	public Map<String, Integer> identify(Collection<LanguageProfile> profiles){
		
		Map<String, Integer> distanceMap = new HashMap<String, Integer>();
			
		for(LanguageProfile profile : profiles){

			int distance = calculateDistance(profile);
			
			distanceMap.put(profile.getProfileLanguageEnName()+ ": " +profile.getProfileLanguageOrgName(), distance);
			
		}
		//Sort according the profile distances in ascending order
		return CollectionsUtil.sortUsingComparator(distanceMap,true);
	}
	
	
	
	
	/**
	 * Calculating the out-of-place measure between two LanguageProfiles. Basically, the
	 * distance is defined in terms of the positions of the common n-grams in both profiles.
	 * For more information refer to: N-Gram-Based Text Categorization (1994) by Cavnar et al.
	 * @param languageProfile
	 * @return
	 */
	private int calculateDistance(LanguageProfile languageProfile) {
		
		
		Map<String, Integer> toCompareWithNgramMap = languageProfile.getAllNgramsMap();
		
		int distance = 0;
		
		for(Entry<String, Integer> entry : this.ngramOccurenceMap.entrySet()){
			
			String ngram = entry.getKey();
			
			if (!toCompareWithNgramMap.containsKey(ngram)) {
				distance += toCompareWithNgramMap.size();
				continue;
			}
				
			int posInText = getPosition(ngram,this.ngramOccurenceMap);
			int posInCourpus = getPosition(ngram,toCompareWithNgramMap);
			distance += Math.abs (posInText- posInCourpus);

				
		}
		return distance;
	}


	/**
	 * Gets the index of a given n-gram in the n-gram-count map
	 * @param ngram
	 * @param ngramMap
	 * @return
	 */
	private int getPosition(String ngram, Map<String, Integer> ngramMap) {
				
		int pos = 0;
		for (String key : ngramMap.keySet()) {
			if(key.equals(ngram)){
				return pos;
			}
			pos ++ ;
		}
		return -1;
	}
	
	
	
	public String getProfileLanguageEnName() {
		return profileLanguageEnName;
	}

	public void setProfileLanguageEnName(String profileLanguageEnName) {
		this.profileLanguageEnName = profileLanguageEnName;
	}

	public String getProfileLanguageOrgName() {
		return profileLanguageOrgName;
	}

	public void setProfileLanguageOrgName(String profileLanguageOrgName) {
		this.profileLanguageOrgName = profileLanguageOrgName;
	}

	public Map<String, Integer> getAllNgramsMap() {
		return ngramOccurenceMap;
	}


	public void setAllNgramsMap(Map<String, Integer> allNgramsMap) {
		this.ngramOccurenceMap = allNgramsMap;
	}


}


