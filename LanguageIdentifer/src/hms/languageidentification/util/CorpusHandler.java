package hms.languageidentification.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import hms.languageidentification.LanguageProfile;



public class CorpusHandler {


	private static int MIN_NGRAM = 1 ; //min n-gram length
	private static int MAX_NGRAM = 3 ; //max n-gram length
	private static int MIN_OCURR = 1000; //min occurance of an n-gram
	private static Map<String, Integer> ngramMapUnsorted = new HashMap<String, Integer>();

	
	/**
	 * Generates n-gram for a given corpus. 
	 * @param corpusFilePath The path of the corpus file.
	 * @param ngramTargetFile The path of the n-gram file 
	 */
	public static void generateNGramForCorpus(String corpusFilePath, String ngramTargetFile){
		ngramMapUnsorted.clear();
		generateNGrams(new File(corpusFilePath));
		saveToFile(ngramTargetFile,MIN_OCURR);
		
	}

	/**
	 * Generates n-grams from corpus files stored in a given directory
	 * @param corpusFileDir The directory of corpus file
	 * @param distDir The output directory where the n-gram file are saved
	 */
	public static void generateNGramForCorpuses(String corpusFileDir,String distDir){
		
		String [] corpusFileNames = new File(corpusFileDir).list();
		
		for(String corpusFileName : corpusFileNames){
			
			System.out.println("Dealing with " + corpusFileName);
			generateNGramForCorpus(corpusFileDir+corpusFileName,distDir+corpusFileName);
			System.out.println("DONE with " + corpusFileName);
		}
		
	}
	
	public static void main(String[] args) {
	
		String corpusFileDir = "C:/Development/LanduageDatasets/LeipzigCorpus/training/small training/corpusFiles/" ;
		String ngramFileDestDir = "./13grams/";
		generateNGramForCorpuses(corpusFileDir, ngramFileDestDir );
	}
			
	/**
	 * Generate n-grams for a corpus file
	 * @param corpusFile
	 */
	public static void generateNGrams(File corpusFile) {

		LanguageProfile lp = new LanguageProfile();
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(corpusFile));
			BufferedReader br = new BufferedReader(isr);
			
			String line;
			
			while ((line = br.readLine()) != null) {
				String[] lineStr = line.split("\t");
				line = lineStr[1].toLowerCase();
				
				Map<String, Integer> lineNGrams = lp.generateAllNGrams(line, MIN_NGRAM, MAX_NGRAM);

				ngramMapUnsorted.putAll(lineNGrams);
			}

			br.close();
			isr.close();
			

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	

	/**
	 * Save the n-grams to a file 
	 * @param outputFilePath
	 * @param minOccur The minimum accepted occurrence count for an n-gram
	 */
	public static void saveToFile(String outputFilePath, int minOccur){
		
		//First sort the n-gram according to their occurrences
		Map<String, Integer> sortedNgramMap = CollectionsUtil.sortByComparator(ngramMapUnsorted, false);
		
		PrintWriter out;
		try {
			out = new PrintWriter(new File(outputFilePath));
			for(Entry<String, Integer> entry : sortedNgramMap.entrySet()){
				
				if(entry.getValue() >= minOccur){
					out.print(entry.getKey().trim().replace(" ", "") + ";" + entry.getValue() + "\n");
				}
				
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
				
		
	}
	
	
	
}
