package set1;

import java.util.HashMap;
import java.util.PriorityQueue;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Set1Ciphers {
	private static final int NUM_BEST_STRINGS = 3;
	private static final HashMap<Character,Float> CWEIGHTS;
	private static final float NONASCII_PENALTY = 1f;
	
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
		public final byte[] str;
		public final float score;
		public final byte[] key;
		private int lineNum;

		public StrScore(byte[] str, float score, byte[] key, int lineNum) {
			this.str = str.clone();
			this.score = score;
			this.key = key.clone();
			this.lineNum = lineNum;
		}
		
		public StrScore(byte[] str, float score, byte[] key) {
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
			w = CWEIGHTS.get(s.charAt(i));
			if (w != null) {
				total += w;
			}
			// Penalize non-printable / non-ASCII characters
			else if (c > 126 || (c < 32 && c != 9 && c != 0xA && c != 0xD)) {
				total -= NONASCII_PENALTY;
			}
		}
		return total;
	}

	public static float calcScore(byte[] s) {
		return calcScore(new String(s));
	}
	
	public static StrScore[] charDecHex(String secret) {
		return charDecHex(secret, NUM_BEST_STRINGS);
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
	public static StrScore[] charDecHex(String hexSecret, int topN) {
		return charDec(Set1Functions.hexToBytes(hexSecret),topN);
	}
	
	public static StrScore[] charDecHexLines(String filename, int topN) {
		PriorityQueue<StrScore> pq = new PriorityQueue<StrScore>(256, Collections.reverseOrder());
		StrScore tempResults[] = null;
		StrScore results[] = new StrScore[topN];
		int lineCount = 0;
		String line = null;
		
		try (BufferedReader br =
				new BufferedReader(new FileReader(filename))) {
			while ((line = br.readLine()) != null) {
				tempResults = charDecHex(line);
				for (StrScore ss : tempResults) {
					ss.setLineNum(lineCount);
					pq.add(ss);
				}
				lineCount++;
			}
			//System.out.println(lineCount);
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

	/**
	 * This function takes in a String that has been encrypted
	 * using a single-character XOR. It returns the topN most likely messages
	 * with their score and encoding key.
	 * @param cryptotext The single-character XOR encoded String.
	 * @param topN The number of desired results.
	 * @return returns an array StrScore of size topN which contains up to
	 * top N results.
	 *
	 */
	public static StrScore[] charDec(String cryptotext, int topN) {
		return charDec(cryptotext.getBytes(Charset.forName("US-ASCII")),topN);
	}

	public static StrScore[] charDec(byte[] cryptotext, int topN) {
		if (topN < 1)
			throw new IllegalArgumentException("Must request at least top 1");
		StrScore results[] = new StrScore[topN];
		PriorityQueue<StrScore> pq = new PriorityQueue<StrScore>(256, Collections.reverseOrder());
		
		byte[] key = null;
		byte[] decoded = null;
		StrScore curRes = null;
		float score = -1.0f;
		byte b = 0;
		
		
		for (short i = 0; i < 256; i++) {
			b = (byte)i;
			decoded = Set1Functions.bytesXOR(cryptotext, new byte[]{b});
			score = calcScore(decoded);
			key = new byte[] {b};
			curRes = new StrScore(decoded, score, key);
			
			pq.add(curRes);
		}
		
		for (int i = 0; i < topN; i++) {
			results[i] = pq.remove();
		}
		
		return results;
	}
	
	public static byte[] aes128ECBDecode(byte[] cryptotext, byte[] key) {
		byte[] decoded = null;
		Cipher cipher = null;
		SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			decoded = cipher.doFinal(cryptotext);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return decoded;
		
	}
	
	public static byte[] aes128ECBEncode(byte[] cryptotext, byte[] key) {
		byte[] encoded = null;
		Cipher cipher = null;
		SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			encoded = cipher.doFinal(cryptotext);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return encoded;
	}
		public static byte[] aes128ECBDecodeNoPadding(byte[] cryptotext, byte[] key) {
		byte[] decoded = null;
		Cipher cipher = null;
		SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
		try {
			cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			decoded = cipher.doFinal(cryptotext);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return decoded;
		
	}
	
	public static byte[] aes128ECBEncodeNoPadding(byte[] cryptotext, byte[] key) {
		byte[] encoded = null;
		Cipher cipher = null;
		SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
		try {
			cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			encoded = cipher.doFinal(cryptotext);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return encoded;
	}

	/*
	 * Detects the first byte array that has been encrypted with AES ECB mode
	 */
	public static byte[] detectAES128ECB(byte[][] cryptotexts) {
		byte[] block1, block2;
		for (int i = 0; i < cryptotexts.length; i++) {
			for (int j = 0; (j+1)*16+15 < cryptotexts[i].length; j++) {
				for (int k = j+1; k*16+15 < cryptotexts[i].length; k++) {
					block1 = Arrays.copyOfRange(cryptotexts[i], j*16, (j+1)*16);
					block2 = Arrays.copyOfRange(cryptotexts[i], k*16, (k+1)*16);
					if (Arrays.equals(block1, block2)) {
						System.out.println("Line#: " + i);
						System.out.println("Repeated Block: " + Set1Functions.bytesToHex(block1));
						return cryptotexts[i];
					}
				}
			}
		}
		return null;
	}
	
	public static byte[] detectAES128ECBHexFile(String filename) {
		ArrayList<byte[]> list = new ArrayList<byte[]>();
		String line = null;
		try (BufferedReader br =
				new BufferedReader(new FileReader(filename))) {
			while ((line = br.readLine()) != null) {
				list.add(Set1Functions.hexToBytes(line));
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}	
		return detectAES128ECB(list.toArray(new byte[0][]));
	}
}
