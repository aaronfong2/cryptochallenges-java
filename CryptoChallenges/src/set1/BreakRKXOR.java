package set1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.PriorityQueue;

import set1.Set1Functions;
import set1.Set1Ciphers.*;


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
	
	public static int[] bestKeysizes(byte[] cryptotext, int numBest) {
		int keysizes[] = new int[numBest];
		PriorityQueue<KeysizeScore> pq= new PriorityQueue<KeysizeScore>();
		KeysizeScore kscore = null;
		double hd = 0;
		byte[] first = null, second = null;
		
		for (int i = 1; i <= MAX_KEYSIZE && i < cryptotext.length / 2; i++) {
			first = Arrays.copyOfRange(cryptotext, 0, i);
			second = Arrays.copyOfRange(cryptotext,i,2*i);
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
	
	public static byte[][] transposeBlocks(byte[] cryptotext, int keysize) {
		byte transposed[][] = new byte[keysize][];
		
		for (int i = 0; i < keysize; i++) {
			transposed[i] = new byte[cryptotext.length/keysize
			                         + (cryptotext.length % keysize > 0 ? 1 : 0)];
		}
		
		for (int i = 0; i < cryptotext.length; i+=keysize) {
			for (int j = 0; j < keysize; j++) {
				if (j+i < cryptotext.length)
					transposed[i][j] = cryptotext[j+i];
				else
					break;
			}
		}
		
		return transposed;
	}
	
	public static StrScore[] decode(byte[] cryptotext, int numKeysizes) {
		StrScore decoded[] = new StrScore[numKeysizes];
		byte transposed[][] = null;
		byte currKey[] = null;
		
		// Find top few key sizes
		int keysizes[] = bestKeysizes(cryptotext, numKeysizes);
		
		for (int i = 0; i < numKeysizes; i++) {
			int ks = keysizes[i];
			currKey = new byte[ks];
			// Split cryptotext into blocks that have been encoded with same letter
			transposed = transposeBlocks(cryptotext, ks);
			for (int j = 0; j < ks; j++) {
				// Decode each block as single char XOR
				StrScore sScore[] = Set1Ciphers.strCharDec(transposed[j], 1);
				// Just keep the key chars, recalculate plaintext at the end for simplicity
				currKey[j] = (byte)(sScore[0].key.charAt(0));
			}
			decoded[i] = new StrScore(new String(Set1Functions.strXOR(cryptotext, currKey)),
										0.0f, new String(currKey));
		}
		
		return decoded;
	}
	
	/*
	 * Take Base64 encoded file and decode repeating-key XOR
	 */
	public static StrScore[] decodeFile(String filename, int numKeysizes) {
		byte[] cryptotext = null;
		StrScore decoded[] = null;
		
		try (BufferedReader br = new BufferedReader(
				new FileReader(filename))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			cryptotext = Base64.getDecoder().decode(sb.toString());
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
		decoded = decode(cryptotext, numKeysizes);
		
		return decoded;
	}
	
	
}
