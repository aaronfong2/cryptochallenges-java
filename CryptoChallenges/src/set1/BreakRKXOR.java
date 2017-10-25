package set1;

import java.util.PriorityQueue;

import set1.Set1Ciphers.*;
import set1.Set1Functions.*;


public class BreakRKXOR {
	private static final int MAX_KEYSIZE = 40;
	
	public static class KeysizeScore implements Comparable<KeysizeScore> {
		public final int keysize;
		public final double hammingDistance;
		
		public KeysizeScore(int keysize, double hammingDistance) {
			this.keysize = keysize;
			this.hammingDistance = hammingDistance;
		}

		@Override
		public int compareTo(KeysizeScore arg0) {
			// TODO Auto-generated method stub
			return ((Double)this.hammingDistance).compareTo(arg0.hammingDistance);
		}
	}
	
	public static int[] bestKeysizes(String cryptotext, int numBest) {
		int keysizes[] = new int[numBest];
		PriorityQueue<KeysizeScore> pq= new PriorityQueue<KeysizeScore>();
		KeysizeScore kscore = null;
		double hd = 0;
		String first = null, second = null;
		
		for (int i = 1; i <= MAX_KEYSIZE && i < cryptotext.length() / 2; i++) {
			first = cryptotext.substring(0,i);
			second = cryptotext.substring(i,2*i);
			try {
				hd = Set1Functions.normalizedHammingDistance(first, second);
				kscore = new KeysizeScore(i,hd);
				pq.add(kscore);
			}
			catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		
		for (int i = 0; i < numBest; i++) {
			keysizes[i] = pq.remove().keysize;
		}
		
		return keysizes;
	}
	
	public static String[] transposeBlocks(String cryptotext, int keysize) {
		String transposed[] = new String[keysize];
		StringBuilder sb[] = new StringBuilder[keysize];
		
		for (int i = 0; i < keysize; i++) {
			sb[i] = new StringBuilder();
		}
		
		for (int i = 0; i < cryptotext.length(); i+=keysize) {
			for (int j = 0; j < keysize; j++) {
				if (j+i < cryptotext.length())
					sb[j].append(cryptotext.substring(j+i, j+i+1));
				else
					break;
			}
		}
		for (int i = 0; i < keysize; i++) {
			transposed[i] = sb[i].toString();
		}
		
		return transposed;
	}
}
