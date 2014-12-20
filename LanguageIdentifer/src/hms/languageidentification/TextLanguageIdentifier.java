package hms.languageidentification;

import hms.languageidentification.util.CollectionsUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The TextLanguageIdentifier identifies the language of a given text by
 * comparing its LanguageProfile to a collection of LanguageProfiles
 * corresponding to predefined set of languages. Currently, LanugaugeProfiles of
 * 17 natural languages are available. These include: ara, deu, eng, fra, ita,
 * nld, pol, rus, spa, swe, tur, ukr, urd, bel, fas, dan, cat
 * 
 * @author Hatem Mousselly-Sergieh
 *
 */
public class TextLanguageIdentifier {

	/**
	 * A collection of LanguageProfiles
	 */
	private Collection<LanguageProfile> languageProfiles;
	
	/**
	 * The separator used in the predefined language n-gram files. In our case the n-grams and
	 * the corresponding counts are separated using a tab.
	 */
	private static final String FILE_SPEARTOR = "\t"; 
	
	/**
	 * The location of the predefined configuration file. You can define your own configuration file
	 * and set this variable to refer to it (Not implemented yet @TODO). 
	 * The configuration file contains the paths to predefined language n-gram files
	 */
	private static String DEFAULT_CONFIG_FILE = "resources/processed/config.properties";

	/**
	 * The location where the predefined language n-gram files reside
	 */
	private static String PREDEFINED_LANGUAGE_NGRAMS_FILE_LOCATION = "resources/processed/";
	

	/**
	 * Takes a text and identifies the language in which it is written.
	 * @param text
	 * @return
	 */
	public String identifyLanguage(String text) {

		LanguageProfile lp = new LanguageProfile();

		//Avoid multiples load of the predefined language profiles
		if (languageProfiles == null) {

			languageProfiles = loadLanguageProfiles();

		}

		lp.createProfile(text);

		Map<String, Integer> matchigLanguages = lp.identify(languageProfiles);

		String lang = null;
		
		if(matchigLanguages!=null && matchigLanguages.size() > 0){
			
			lang = matchigLanguages.keySet().iterator().next();
		}
		else{
			System.out.println("Language could not be identified :(");
		}
		return lang; 

	}

	/**
	 * Load a predefined LanguageProfile from an InputSream.
	 * @param langProfInputStream
	 * @return
	 */
	public LanguageProfile loadLanguageProfile(InputStream langProfInputStream) {

		Map<String, Integer> ngramMapUnsorted = new HashMap<String, Integer>();

		LanguageProfile lp = new LanguageProfile();

		try {

			InputStreamReader isr = new InputStreamReader(langProfInputStream);

			BufferedReader br = new BufferedReader(isr);

			String line;

			while ((line = br.readLine()) != null) {
				// Ignore comments
				if (line.startsWith("#"))
					continue;

				String[] tokens = line.split(FILE_SPEARTOR);
				String ngram = tokens[0];
				int ngramCount = Integer.valueOf(tokens[1]);
				ngramMapUnsorted.put(ngram, ngramCount);

			}

			br.close();
			isr.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		lp.setAllNgramsMap(CollectionsUtil.sortUsingComparator(ngramMapUnsorted,false));

		return lp;
	}

	/**
	 * Load language profiles from files from files stored in a given directory
	 * @param configFile
	 * @return
	 */
	public Collection<LanguageProfile> loadLanguageProfiles(String configFile) {

		Collection<LanguageProfile> lps = new ArrayList<LanguageProfile>();

		InputStream confIs = LanguageProfile.class.getClassLoader().getResourceAsStream(configFile);

		InputStreamReader isr = new InputStreamReader(confIs);

		BufferedReader br = new BufferedReader(isr);

		String configLine = null;

		try {
			while ((configLine = br.readLine()) != null) {
				
				String[] configLineElements = configLine.split(",");
				String filePath = configLineElements[0];
				String langEnName = configLineElements[1];
				String langOrgName = configLineElements[2];
				
				InputStream lpIs = LanguageProfile.class.getClassLoader().getResourceAsStream(PREDEFINED_LANGUAGE_NGRAMS_FILE_LOCATION+ filePath);
				LanguageProfile lp = loadLanguageProfile(lpIs);
				lp.setProfileLanguageEnName(langEnName);
				lp.setProfileLanguageOrgName(langOrgName);
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
	public Collection<LanguageProfile> loadLanguageProfiles() {
		return loadLanguageProfiles(DEFAULT_CONFIG_FILE);
	}


	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Enter you text within double quotations...");
		} else {
			String text = args[0];
			TextLanguageIdentifier tli = new TextLanguageIdentifier();
			String lang = tli.identifyLanguage(text);
			System.out.println(lang);

		}
		
		//Sample class usage
//		TextLanguageIdentifier li = new TextLanguageIdentifier();
//		String lang = li.identifyLanguage("la vie en rose");
//		System.out.println(lang);
//		lang = li.identifyLanguage("Einstein blev født i den sydtyske by Ulm");
//		System.out.println(lang);

	}
}
