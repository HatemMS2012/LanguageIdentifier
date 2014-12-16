package hms.languageidentification.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hms.languageidentification.LanguageProfile;

public class CorpusHandler {
	
	
	private static int MIN_OCURR = 2000;
	
	
	public static void generateNGramForCorpus(String corpusFilePath, String ngramTargetFile){
		ngramMapUnsorted.clear();
		generateNGrams(new File(corpusFilePath));
		saveToFile(ngramTargetFile,MIN_OCURR);
	
		
	}

	
	public static void generateNGramForCorpuses(String corpusFileDir,String distDir){
		
		String [] corpusFileNames = new File(corpusFileDir).list();
		
		for(String corpusFileName : corpusFileNames){
			
			System.out.println("Dealing with " + corpusFileName);
			generateNGramForCorpus(corpusFileDir+corpusFileName,distDir+corpusFileName);
			System.out.println("DONE with " + corpusFileName);
		}
		
	}
	
	public static void main(String[] args) {
	
	
	
		generateNGramForCorpuses("C:/Development/LanduageDatasets/LeipzigCorpus/training/small training/corpusFiles/", "./resources/processed/");
	}
			
	
	

	private static Map<String, Integer> ngramMapUnsorted = new HashMap<String, Integer>();
	
	
	public static  Map<String, Integer> generateNGrams(String str, int length) {
		
	
		Pattern pattern = Pattern.compile("^_?[^0-9\\?!\\-_/,;.:§%?\"'|~^°(){} \\[\\] ]*_?$");
		char[] chars = str.toCharArray();
	  
	    final int resultCount = chars.length - length + 1;
	    
   
	    for (int i = 0; i < resultCount; i++) {
	        
	        String ngram =  new String(chars, i, length);
	        Matcher matcher = pattern.matcher(ngram);
	        
	        if(!matcher.find()){

	        	continue;
	        }
	        
	        ngram =  ngram.replace(" ", "_");
	        
	        Integer nGramCount = ngramMapUnsorted.get(ngram);
	        
	        if(nGramCount!=null){

	        	ngramMapUnsorted.put(ngram, nGramCount+1);
	        }
	        else{
	        	ngramMapUnsorted.put(ngram, 1);
	        }
	        
//	        System.out.println(ngram + ":" + ngramMapUnsorted.get(ngram));
	    }
	    
	    return ngramMapUnsorted;
	}
	
	public static void generateNGrams(File corpusFile) {

		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(corpusFile));
			BufferedReader br = new BufferedReader(isr);
			
			String line;
			
			while ((line = br.readLine()) != null) {
				String[] lineStr = line.split("\t");
				line = lineStr[1].toLowerCase();
				for (int i = MIN_NGRAM; i <=MAX_NGRAM; i++) {
					generateNGrams(line,i);

				}
			}

			br.close();
			isr.close();
			

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	private static int MIN_NGRAM = 2 ;
	private static int MAX_NGRAM = 3 ;
	
	public static void saveToFile(String outputFilePath, int minOccur){
		
		
		PrintWriter out;
		try {
			out = new PrintWriter(new File(outputFilePath));
			for(Entry<String, Integer> entry : ngramMapUnsorted.entrySet()){
				
				if(entry.getValue() >= minOccur){
					out.print(entry.getKey().trim().replace(" ", "") + ";" + entry.getValue() + "\n");
				}
				
			}
			
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
	}
	
	
	
}
