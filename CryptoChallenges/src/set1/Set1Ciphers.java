package set1;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Collections;

public class Set1Ciphers {
	private static final int NUM_BEST_STRINGS = 3;
	private static final float MULTIPLIER = 1.0f;
	private static final HashMap<Character,Float> CWEIGHTS;
	static {
		CWEIGHTS = new HashMap<Character,Float>();
		CWEIGHTS.put('e', 24*MULTIPLIER);
		CWEIGHTS.put('t', 18*MULTIPLIER);
		CWEIGHTS.put('a', 16*MULTIPLIER);
		CWEIGHTS.put('o', 15*MULTIPLIER);
		CWEIGHTS.put('i', 15*MULTIPLIER);
		CWEIGHTS.put('n', 14*MULTIPLIER);
		CWEIGHTS.put('s', 13*MULTIPLIER);
		CWEIGHTS.put('r', 12*MULTIPLIER);
		CWEIGHTS.put('h', 12*MULTIPLIER);
		CWEIGHTS.put('d', 9*MULTIPLIER);
		CWEIGHTS.put('l', 8*MULTIPLIER);
		CWEIGHTS.put('u', 6*MULTIPLIER);		
	}
	
	public static class StrScore implements Comparable<StrScore>{
		public final String str;
		public final float score;
		public final String key;
		
		public StrScore(String str, float score, String key) {
			this.str = str;
			this.score = score;
			this.key = key;
		}
		
		@Override
		public int compareTo(StrScore ss) {
			// Assume not null
			return ((Float)this.score).compareTo(ss.score);
		}
	}

	public static int calcScore(String s) {
		Float w = null;
		int total = 0;
		s = s.toLowerCase();
		
		for (int i = 0; i <  s.length(); i++) {
			w = CWEIGHTS.get(s.charAt(i));
			if (w != null) {
				total += w;
			}
		}
		return total;
	}
	
	public static StrScore[] charDec(String secret) {
		return charDec(secret, NUM_BEST_STRINGS);
	}
	/**
	 * This function takes in a hex encoded string that has been encrypted
	 * using a single-character XOR. It returns the topN most likely messages
	 * with their score and encoding key.
	 * @param secret The single-character XOR encoded String.
	 * @param topN The number of desired results.
	 * @return returns an array StrScore of size topN which contains up to
	 * top N results.
	 */
	// topN is how many top results will be returned
	public static StrScore[] charDec(String hexSecret, int topN) {
		if (topN < 1)
			throw new IllegalArgumentException("Must request at least top 1");
		StrScore results[] = new StrScore[topN];
		PriorityQueue<StrScore> pq = new PriorityQueue<StrScore>(256, Collections.reverseOrder());
		
		String key = null;
		String decoded = null;
		StrScore curRes = new StrScore("abc", 1, "a");
		int score = -1;
		byte b = 0;
		
		
		for (short i = 0; i < 256; i++) {
			b = (byte)i;
			decoded = Set1Functions.hexXOR(hexSecret, b);
			score = calcScore(decoded);
			key = new String(new byte[] {b});
			curRes = new StrScore(decoded, score, key);
			
			pq.add(curRes);
		}
		
		for (int i = 0; i < topN; i++) {
			results[i] = pq.remove();
		}
		
		return results;
	}
	
	public static void swap(Object[] array, int index0, int index1) {
		Object temp = array[index0];
		array[index0] = array[index1];
		array[index1] = temp;
	}

}
