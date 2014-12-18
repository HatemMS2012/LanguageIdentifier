##Language Identification/Guessing Liberary in Java

##Introduction
An implementation of a simple method for document’s language identification. The implemented approach is based on character n-gram model as proposed by Cavnar et al. [1]. 

For each of the 17 supported languages (ara, deu, eng, fra, ita, nld, pol, rus, spa, swe, tur, ukr, urd, bel, fas, dan, cat) (see [ISO-639-2](http://www.loc.gov/standards/iso639-2/php/English_list.php) for more information about language abbreviations), we created an n-gram model for n=1,2 and 3. 

The training data were obtained from the [Leipzig Corpora Collection](http://corpora.informatik.uni-leipzig.de/). For each language, we selected a 300k dataset consisting of 300,000 sentences taken from newspaper texts, randomly collected text or Wikipedia. 

In this project the class `hms.languageidentification.LanguageProfile` was used to generate n-gram files for each language based on the lang_x_x_x-sentences.txt files of the [Leipzig Corpora Collection](http://corpora.informatik.uni-leipzig.de/).
Each n-gram file lists the n-grams and the corresponding number of occurrences as observed in the training corpus file.
You can this code to extend the number of recognized languages. 

##Usage

**Example I: inside java code**
```java
	public static void main(String[] args) {
		//Create hms.languageidentification.TextLanguageIdentifier object
		TextLanguageIdentifier li = new TextLanguageIdentifier();
		//Call the identifyLanguage(String text) method. 
		String lang = li.identifyLanguage("wie geht es dir");
		//That is all!
		System.out.println(lang);
	}

```

**Example II: using lang-identifier.jar**

In your command line type:
```command
	java -jar lang-identifier.jar "your text"
```

## References
[1] N-Gram-Based Text Categorization (1994) by William B. Cavnar , John M. Trenkle Venue:	In Proceedings of SDAIR-94, 3rd Annual Symposium on Document Analysis and Information Retrieval
