package hms.languageidentification;

import java.util.Collection;
import java.util.Map;

public class TextLanguageIdentifier {

	private Collection<LanguageProfile> languageProfiles ;
	
	public String identifyLanguage(String text){
		
		LanguageProfile lp = new LanguageProfile();
		
		if(languageProfiles == null){		
			
			languageProfiles = lp.loadLanguageProfiles();

		}
		
		lp.createProfile(text);
		
		Map<String, Integer> matchigLanguages = lp.identify(languageProfiles);
		
		return matchigLanguages.keySet().iterator().next();

	}
	
	
	public static void main1(String[] args) {
		TextLanguageIdentifier li = new TextLanguageIdentifier();
		
		String lang = li.identifyLanguage("wie geht es dir");
		System.out.println(lang);
		
		lang = li.identifyLanguage("it is a nice weather outside");
		System.out.println(lang);
		
		lang = li.identifyLanguage("la vie en rose");
		System.out.println(lang);
		
		lang = li.identifyLanguage("اختيرت برلين بعدها عاصمة جمهورية ألمانيا الاتحادية");
		System.out.println(lang);
		
		lang = li.identifyLanguage("El nombre de Berlín parece provenir de las palabras berle o berlin");
		System.out.println(lang);
		
		lang = li.identifyLanguage("Berlino è situata nella parte orientale della Germania");
		System.out.println(lang);
		
		lang = li.identifyLanguage("Kungadömet Preussen och Tyska kejsarriket");
		System.out.println(lang);
		
		lang = li.identifyLanguage("Dünyanın en eski şehirlerinden biri olan İstanbul");
		System.out.println(lang);
		
		lang = li.identifyLanguage("Pierwsze ślady osadnictwa w okolicach Stambułu pochodzą z epoki kamienia i znajdują się w części azjatyckiej miasta");
		System.out.println(lang);
		
		lang = li.identifyLanguage("In het tsaristische Rusland was het schriftelijk gebruik van het Oekraïens");
		System.out.println(lang);
		
		lang = li.identifyLanguage("تاریخ میں شہر نے مکینوں کی ثقافت، زبان اور مذہب کے اعتبار سے کئی نام بدلے جن میں");
		System.out.println(lang);
		
		lang = li.identifyLanguage("После международного признания Турецкой республики в 1923 году, 29 октября того же года столицей государства была провозглашена Анкара");
		System.out.println(lang);
		
		lang = li.identifyLanguage("Найближчою генеалогічно до української є білоруська мова");
		System.out.println(lang);
		
		lang = li.identifyLanguage("enter some nice text");
		System.out.println(lang);
		
	}
	
	public static void main(String[] args) {
		
		if(args.length !=1){
			System.out.println("Enter you text within double qu...");
		}
		else{
			String text = args[0];
			TextLanguageIdentifier tli = new TextLanguageIdentifier();
			String lang = tli.identifyLanguage(text);
			System.out.println(lang);
			
		}
		
	}
}
