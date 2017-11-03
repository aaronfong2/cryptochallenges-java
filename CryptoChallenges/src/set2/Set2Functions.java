package set2;

import java.util.Arrays;

public class Set2Functions {
	public static byte[] PKCS7Pad(byte[] plaintext, int blocksize) {
		if (blocksize <= 0) {
			throw new IllegalArgumentException("Blocksize must be >0");
		}
		else if (blocksize >= 256) {
			throw new IllegalArgumentException("Blocksize must be <256");
		}

		// Use short to prevent overflow problems
		short numPadding = (short)(blocksize - plaintext.length % blocksize);
		if (numPadding != 0) {
			byte paddedPT[] = Arrays.copyOf(plaintext, plaintext.length + numPadding);
			for (int i = plaintext.length; i < paddedPT.length; i++) {
				paddedPT[i] = (byte)numPadding;
			}
			return paddedPT;
		}
		else {
			return Arrays.copyOf(plaintext, plaintext.length);
		}
	}
}
