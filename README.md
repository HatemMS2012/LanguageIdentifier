##Language Identification/Guessing Liberary in Java

##Introduction
An implementation of a simple method for document language identification. The implemented approach is based on character n-gram model as proposed by Cavnar et el [1]. 

##Usage

**Example: inside java code**
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
**Example: using lang-identifier.jar
In command line type:
```command
	java -jar lang-identifier.jar "your text"
```

## References
N-Gram-Based Text Categorization (1994) by William B. Cavnar , John M. Trenkle Venue:	In Proceedings of SDAIR-94, 3rd Annual Symposium on Document Analysis and Information Retrieval
