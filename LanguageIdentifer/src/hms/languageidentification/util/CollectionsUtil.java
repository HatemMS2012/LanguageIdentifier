package hms.languageidentification.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * CollectionsUtil provides collection utility methods
 * @author Hatem Mousselly-Sergieh
 *
 */
public class CollectionsUtil {

	
	/**
	 * Sort a map according to its values in ascending order when asc=true and i descending order otherwise.
	 * @param unsortedMap 
	 * @param asc
	 * @return
	 */
	public static Map<String, Integer> sortUsingComparator(Map<String, Integer> unsortedMap, final boolean asc) {
		 
		// Convert Map to List
		List<Map.Entry<String, Integer>> list = 
			new LinkedList<Map.Entry<String, Integer>>(unsortedMap.entrySet());

		// Sort a list using custom comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				if(asc){
					return (o1.getValue()).compareTo(o2.getValue());
				}
				else {
					return -1*(o1.getValue()).compareTo(o2.getValue());
				}
			}
		});

		// Convert the sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
}
