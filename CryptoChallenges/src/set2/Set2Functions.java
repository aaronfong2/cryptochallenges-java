package set2;

import java.util.Arrays;

import javax.crypto.BadPaddingException;

import set1.Set1Ciphers;
import set1.Set1Functions;

public class Set2Functions {
	// Blocksize in bytes
	public static final int AES_BLOCKSIZE = 16;
	public static final int AES128_KEYSIZE = 16;
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
	
	public static byte[] PKCS7Unpad(byte[] plaintext) throws BadPaddingException {
		// Use short to avoid overflow problems
		short pad = (short)(plaintext[plaintext.length - 1] & 0xFF);
		if (pad <= 0) {
			throw new BadPaddingException();
		}
		// Validate padding
		for (int i = 1; i < pad; i++) {
			if (plaintext[plaintext.length - i - 1] != pad) {
				throw new BadPaddingException();
			}
		}
		return Arrays.copyOf(plaintext, plaintext.length - pad);
	}
	
	public static byte[] aes128CBCEncrypt(byte[] plaintext, byte[] key, byte[] IV) {
		byte[] prevBlock = null;
		byte[] currBlock = null;
		byte[] ptBlock = null;
		byte[] paddedPT = PKCS7Pad(plaintext, AES_BLOCKSIZE);
		byte[] encrypted = new byte[paddedPT.length];
		
		// This should probably throw some exception
		if (IV.length != AES_BLOCKSIZE) {
			return null;
		}
		
		prevBlock = Arrays.copyOf(IV, AES_BLOCKSIZE);
		for (int i = 0; i < paddedPT.length; i+=AES_BLOCKSIZE) {
			// XOR with previous block, then AES encrypt 1 block
			ptBlock = Arrays.copyOfRange(paddedPT, i, i+AES_BLOCKSIZE);
			currBlock = Set1Ciphers.aes128ECBEncodeNoPadding(Set1Functions.bytesXOR(ptBlock, prevBlock), key);
			System.arraycopy(currBlock, 0, encrypted, i, AES_BLOCKSIZE);
			prevBlock = currBlock;
		}

		return encrypted;
	}
	
	public static byte[] aes128CBCDecrypt(byte[] cryptotext, byte[] key, byte[] IV) {
		byte[] decrypted = new byte[cryptotext.length];
		byte[] currBlock = null;
		byte[] prevBlock = null;
		byte[] ptBlock = null;
		
		// This should probably throw some exception
		if (IV.length != AES_BLOCKSIZE || cryptotext.length % AES_BLOCKSIZE != 0) {
			return null;
		}
		
		prevBlock = Arrays.copyOf(IV, AES_BLOCKSIZE);
		for (int i = 0; i < cryptotext.length; i+=AES_BLOCKSIZE) {
			currBlock = Arrays.copyOfRange(cryptotext, i, i+AES_BLOCKSIZE);
			// AES decrypt 1 block, then XOR with previous block
			ptBlock = Set1Functions.bytesXOR(Set1Ciphers.aes128ECBDecodeNoPadding(currBlock, key), prevBlock);
			System.arraycopy(ptBlock, 0, decrypted, i, AES_BLOCKSIZE);
			prevBlock = currBlock;
		}
		
		try {
			return PKCS7Unpad(decrypted);
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
