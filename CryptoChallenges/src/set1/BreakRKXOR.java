package set1;

import java.util.Arrays;
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
			return ((Double)this.hammingDistance).compareTo(arg0.hammingDistance);
		}
	}
	
	public static int[] bestKeysizes(byte[] cryptotext, int numBest) {
		int keysizes[] = new int[numBest];
		PriorityQueue<KeysizeScore> pq= new PriorityQueue<KeysizeScore>();
		KeysizeScore kscore = null;
		double hd = 0;
		byte[] first = null, second = null, third = null, fourth = null;
		
		for (int i = 1; i <= MAX_KEYSIZE && i < cryptotext.length / 3; i++) {
			first = Arrays.copyOfRange(cryptotext, 0, i);
			second = Arrays.copyOfRange(cryptotext,i,2*i);
			third = Arrays.copyOfRange(cryptotext,2*i,3*i);
			fourth = Arrays.copyOfRange(cryptotext,3*i,4*i);
			try {
				hd = Set1Functions.normalizedHammingDistance(first, second);
				hd += Set1Functions.normalizedHammingDistance(first, third);
				hd += Set1Functions.normalizedHammingDistance(first, fourth);
				hd += Set1Functions.normalizedHammingDistance(second, third);
				hd += Set1Functions.normalizedHammingDistance(second, fourth);
				hd += Set1Functions.normalizedHammingDistance(third, fourth);
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
		int modulus = cryptotext.length % keysize;
		
		for (int i = 0; i < keysize; i++) {
			transposed[i] = new byte[cryptotext.length/keysize
			                         + (modulus >= i+1 ? 1 : 0)];
		}
		
		for (int i = 0; i*keysize < cryptotext.length; i++) {
			for (int j = 0; j < keysize; j++) {
				if (i*keysize + j < cryptotext.length)
					transposed[j][i] = cryptotext[i*keysize + j];
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
		int ks = 0;
		float score = 0.0f;
		
		// Find top few key sizes
		int keysizes[] = bestKeysizes(cryptotext, numKeysizes);
		
		for (int i = 0; i < numKeysizes; i++) {
			ks = keysizes[i];
			currKey = new byte[ks];
			score = 0.0f;
			// Split cryptotext into blocks that have been encoded with same letter
			transposed = transposeBlocks(cryptotext, ks);
			for (int j = 0; j < ks; j++) {
				// Decode each block as single char XOR
				StrScore sScore[] = Set1Ciphers.charDec(new String(transposed[j]), 1);
				// Just keep the key chars, recalculate plaintext at the end for simplicity
				currKey[j] = (byte)(sScore[0].key[0]);
				score += sScore[0].score;
			}
			decoded[i] = new StrScore(Set1Functions.bytesXOR(cryptotext, currKey), score, currKey);
		}
		
		return decoded;
	}
	
	/*
	 * Take Base64 encoded file and decode repeating-key XOR
	 */
	public static StrScore[] decodeFile(String filename, int numKeysizes) {
		byte[] cryptotext = Set1Functions.b64FileToBytes(filename);
		StrScore decoded[] = decode(cryptotext, numKeysizes);
		
		return decoded;
	}
	
	
}
