##Language Identification/Guessing Liberary in Java

##Introduction
An implementation of a simple method for document language identification. The implemented approach is based on character n-gram model as proposed by Cavnar et el [1]. 

##Usage

**Example**

	public static void main(String[] args) {
		TextLanguageIdentifier li = new TextLanguageIdentifier();
		
		String lang = li.identifyLanguage("wie geht es dir");
		System.out.println(lang);
		
		lang = li.identifyLanguage("it is a nice weather outside");
		System.out.println(lang);
		
	}



## References
N-Gram-Based Text Categorization (1994) by William B. Cavnar , John M. Trenkle Venue:	In Proceedings of SDAIR-94, 3rd Annual Symposium on Document Analysis and Information Retrieval
