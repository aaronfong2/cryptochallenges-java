package set1;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileReader;

public class Set1Ciphers {
	private static final int NUM_BEST_STRINGS = 3;
	private static final HashMap<Character,Float> CWEIGHTS;
	
	static {
		// Weights based on Wikipedia's "Letter Frequency" article
		CWEIGHTS = new HashMap<Character,Float>();
		CWEIGHTS.put('e', 0.12702f);
		CWEIGHTS.put('t', 0.09056f);
		CWEIGHTS.put('a', 0.08167f);
		CWEIGHTS.put('o', 0.07507f);
		CWEIGHTS.put('i', 0.06966f);
		CWEIGHTS.put('n', 0.06749f);
		CWEIGHTS.put('s', 0.06327f);
		CWEIGHTS.put('h', 0.06094f);
		CWEIGHTS.put('r', 0.05987f);
		CWEIGHTS.put('d', 0.04253f);
		CWEIGHTS.put('l', 0.04025f);
		CWEIGHTS.put('c', 0.02782f);
		CWEIGHTS.put('u', 0.02758f);
	}
	
	public static class StrScore implements Comparable<StrScore>{
		public final String str;
		public final float score;
		public final String key;
		private int lineNum;

		public StrScore(String str, float score, String key, int lineNum) {
			this.str = str;
			this.score = score;
			this.key = key;
			this.lineNum = lineNum;
		}
		
		public StrScore(String str, float score, String key) {
			this(str,score,key,-1);
		}
		
		@Override
		public int compareTo(StrScore ss) {
			// Assume not null
			return ((Float)this.score).compareTo(ss.score);
		}
		
		public int getLineNum() {
			return lineNum;
		}

		public void setLineNum(int lineNum) {
			this.lineNum = lineNum;
		}
	}

	public static float calcScore(String s) {
		Float w = null;
		float total = 0;
		char c = 0;
		s = s.toLowerCase();
		
		for (int i = 0; i <  s.length(); i++) {
			c = s.charAt(i);
			// Return 0 if any non-ascii characters found
			if (c > 126) return 0;
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
		StrScore curRes = null;
		float score = -1.0f;
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
	
	public static StrScore[] charDecLines(String filename, int topN) {
		PriorityQueue<StrScore> pq = new PriorityQueue<StrScore>(256, Collections.reverseOrder());
		StrScore tempResults[] = null;
		StrScore results[] = new StrScore[topN];
		int lineCount = 0;
		String line = null;
		
		try (BufferedReader br =
				new BufferedReader(new FileReader(filename))) {
			while ((line = br.readLine()) != null) {
				tempResults = charDec(line);
				for (StrScore ss : tempResults) {
					ss.setLineNum(lineCount);
					pq.add(ss);
				}
				lineCount++;
			}
			System.out.println(lineCount);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
		
		for (int i = 0; i < topN; i++) {
			results[i] = pq.remove();
		}
		
		return results;
	}

	/*
	public static void swap(Object[] array, int index0, int index1) {
		Object temp = array[index0];
		array[index0] = array[index1];
		array[index1] = temp;
	}
	*/


}
