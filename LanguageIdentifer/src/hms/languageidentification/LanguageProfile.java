package hms.languageidentification;

import hms.languageidentification.util.CollectionsUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageProfile {
	
	
	private static final String FILE_SPEARTOR = ";" ; //"\\s";
	private static String DEFAULT_CONFIG_FILE = "resources/processed/config.properties";
	private int DEFAULT_MAX_N_GRAME = 3 ; //Currently a maximum of 3-grams is supported
	private int DEFAULT_MIN_N_GRAME = 2 ;
	private String profileLanguage ;
	private Map<String, Integer> allNgramsMap;
	
	
	/**
	 * Create a language profile for a given text
	 * The language profile consist of n-grams and the corresponding counts in the given text
	 * @param text
	 */
	public void createProfile(String text){
		
		text = text.toLowerCase();
	
		Map<String, Integer> ngramsUnsorted = generateAllNGrams(text,DEFAULT_MIN_N_GRAME,DEFAULT_MAX_N_GRAME);
		
		this.allNgramsMap = new LinkedHashMap<String, Integer> (CollectionsUtil.sortByComparator(ngramsUnsorted,false));
		
	}
	
	
	
	/**
	 * Generate an map of n-grams and the corresponding counts from an input text.
	 * @param text
	 * @param length
	 * @return
	 */
	public  Map<String, Integer> generateNGrams(String text, int length) {
		
		Map<String, Integer> ngramCountMap = new HashMap<String, Integer>();
		
		
		Pattern pattern = Pattern.compile("^_?[^0-9\\?!\\-_/,;.:§%?\"'|~^°(){} \\[\\] ]*_?$");
		
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
	 * Generate all n-grams for n = start, ... to n = max from an input text
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
	 * Identify the language of the language profile of the current class instance 
	 * The determination id performed by comparing the profile (i.e., n-grams counts) of the current instance 
	 * to a collection of language profiles (these are normally loaded from  the training set)
	 * @param profiles
	 */
	public Map<String, Integer> identify(Collection<LanguageProfile> profiles){
		
		Map<String, Integer> distanceMap = new HashMap<String, Integer>();
			
		for(LanguageProfile profile : profiles){

			int distance = calculateDistance(profile);
			
			distanceMap.put(profile.getProfileLanguage(), distance);
			
		}
		//Sort according the profile distances in ascending order
		return CollectionsUtil.sortByComparator(distanceMap,true);
	}
	
	
	
	
	/**
	 * Calculates the distance between two language profiles.
	 * The distance is calculated based on the positions of the common n-grams
	 * in both profiles.
	 * @param languageProfile
	 * @return
	 */
	private int calculateDistance(LanguageProfile languageProfile) {
		
		
		Map<String, Integer> toCompareWithNgramMap = languageProfile.getAllNgramsMap();
		
		int distance = 0;
		
		for(Entry<String, Integer> entry : this.allNgramsMap.entrySet()){
			
			String ngram = entry.getKey();
			
			if (!toCompareWithNgramMap.containsKey(ngram)) {
				distance += toCompareWithNgramMap.size();
				continue;
			}
				
			int posInText = getPosition(ngram,this.allNgramsMap);
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
	
	public LanguageProfile loadLanguageProfile(InputStream pathToLanguageNgram){
		
		Map<String, Integer> ngramMapUnsorted = new HashMap<String, Integer>();

		
		LanguageProfile lp = new LanguageProfile();
			
			try {
			
				InputStreamReader isr = new InputStreamReader(pathToLanguageNgram);
			
				BufferedReader br = new BufferedReader(isr);
			
				String line ;
			
				while ((line = br.readLine()) != null) {
					//Ignore comments
					if(line.startsWith("#"))
						continue;
					
					String[] tokens = line.split(FILE_SPEARTOR);
					String ngram = tokens[0];
					int ngramCount = Integer.valueOf(tokens[1]);
					
					ngramMapUnsorted.put(ngram,ngramCount);
					
					
				}
				
				br.close();
				isr.close();
				
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		
			lp.setAllNgramsMap(CollectionsUtil.sortByComparator(ngramMapUnsorted,false));
			
			//Assumption: Language of the profile is indicated in the file name
			//In our such files are named as: language-xxxx-xxx-sentences.csv
//			lp.setProfileLanguage(pathToLanguageNgram.substring(pathToLanguageNgram.lastIndexOf("/")+1, 
//																pathToLanguageNgram.indexOf("_")));
			
			return lp ;
	}

	/**
	 * Load a language profile from a file
	 * @param pathToLanguageNgram
	 * @return
	 */
	public LanguageProfile loadLanguageProfile(String pathToLanguageNgram){
		
		
		InputStream is = null;
		try {
			is = new FileInputStream(new File(pathToLanguageNgram));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return loadLanguageProfile(is);
		
	}

	
	/**
	 * Load language profiles from files from files stored in a given directory
	 * @param configFile
	 * @return
	 */
	public Collection<LanguageProfile> loadLanguageProfiles(String configFile){
	
		Collection<LanguageProfile> lps = new ArrayList<LanguageProfile>();
		
		
		
		InputStream confIs = LanguageProfile.class.getClassLoader().getResourceAsStream(configFile);
			
		InputStreamReader isr = new InputStreamReader(confIs);
		
		BufferedReader br = new BufferedReader(isr);
	
		String languageProfileFileName = null ;
	
		try {
			while ((languageProfileFileName = br.readLine()) != null) {
			
				InputStream lpIs = LanguageProfile.class.getClassLoader().getResourceAsStream(languageProfileFileName);
				LanguageProfile lp = loadLanguageProfile(lpIs);
				lp.setProfileLanguage(languageProfileFileName.substring(languageProfileFileName.lastIndexOf("/")+1,languageProfileFileName.indexOf("_")));
				lps.add(lp);
			}
			
			br.close();
			isr.close();
			confIs.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return lps;
		
	}
	
	/**
	 * Load default language profile
	 * @return
	 */
	public Collection<LanguageProfile> loadLanguageProfiles(){
		return loadLanguageProfiles(DEFAULT_CONFIG_FILE);
	}
	

	public String getProfileLanguage() {
		return profileLanguage;
	}



	public void setProfileLanguage(String name) {
		this.profileLanguage = name;
	}



	public Map<String, Integer> getAllNgramsMap() {
		return allNgramsMap;
	}



	public void setAllNgramsMap(Map<String, Integer> allNgramsMap) {
		this.allNgramsMap = allNgramsMap;
	}



}


