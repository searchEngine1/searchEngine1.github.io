package il.ac.shenkar.utils;

import java.util.ArrayList;
import java.util.List;


/** Class KnuthMorrisPratt **/

public class KnuthMorrisPratt {

	/** Failure array **/
	private int[] failure;
	
	/** Constructor **/
	public KnuthMorrisPratt(String pat) {
		/** pre construct failure array for a pattern **/

		failure = new int[pat.length()];

		fail(pat);

	}

	public List<Integer> getWordsLocation(String text, String pat) { 
		/** find match **/
		List<Integer> locations = new ArrayList<Integer>();
		int newPos = 0;
		int x = posMatch(text, pat,newPos );
		while  ( x != -1 ){
			locations.add(x);
			//System.out.println("\nMatch found: index " + x);
			newPos = x+1;
			x = posMatch(text, pat,newPos );
		}
		return locations;
	}
	
	/** Failure function for a pattern **/

	private void fail(String pat) {

		int n = pat.length();

		failure[0] = -1;

		for (int j = 1; j < n; j++) {

			int i = failure[j - 1];

			while ((pat.charAt(j) != pat.charAt(i + 1)) && i >= 0)

				i = failure[i];

			if (pat.charAt(j) == pat.charAt(i + 1))

				failure[j] = i + 1;

			else

				failure[j] = -1;

		}

	}

	/** Function to find match for a pattern **/

	private int posMatch(String text, String pat, int i) {

		int no_search = 0, iterate = 0;
		int j=0;

		int lens = text.length();

		int lenp = pat.length();

		while (i < lens && j < lenp) {
			iterate++;
			if (text.charAt(i) == pat.charAt(j)) {

				i++;

				j++;
				no_search++;
				continue;
			}

			else if (j == 0) {
				i++;
				no_search++;
			}

			else {
				j = failure[j - 1] + 1;

			}
		}
		return ((j == lenp) ? (i - lenp) : -1);

	}
}