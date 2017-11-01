package set1;

import java.util.Base64;
import java.util.Base64.*;

import java.util.Formatter;

public class Set1Functions {
		
	public static byte[] hexToBytes(String hex) {
		int strlen = hex.length();
		byte data[] = new byte[(hex.length()+1)/2]; // enough bytes for odd # of chars case
		
		// Deal with 2*(n/2 - 1) digits first and the last 1 or 2 digits separately
		for (int i = 0; i < strlen - 1; i+=2)
			data[i/2] = (byte)(Integer.parseInt(hex.substring(i, i+2),16));
			
		if (strlen % 2 == 0)
			data[strlen/2-1] = (byte)(Integer.parseInt(hex.substring(strlen-2, strlen),16));
		else
			data[strlen/2] = (byte)(Integer.parseInt(hex.substring(strlen-1,strlen),16) << 4);
		
		return data;
	}
	
	public static String bytesToHex(byte[] data) {
		try(Formatter formatter = new Formatter(new StringBuilder(data.length * 2))) {
			for (int i = 0; i < data.length; i ++) {
				formatter.format("%02x", data[i]);
			}
			
			return formatter.toString();
		}
	}
	
	public static String bytesToB64(byte[] data) {
		Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(data);
	}
	
	public static String hexToB64(String hex) {
		return bytesToB64(hexToBytes(hex));
	}
	
	public static String b64ToString(String b64) {
		Decoder decoder = Base64.getDecoder();
		return new String(decoder.decode(b64));	
	}
	
	public static byte[] bytesXOR(byte[] array1, byte[] array2) throws Exception {
		if (array1.length != array2.length)
			throw new Exception("Arrays must be same length");
		
		byte output[] = new byte[array1.length];
		for (int i = 0; i < output.length; i++)
			output[i] = (byte)(array1[i] ^ array2[i]);

		return output;
	}
	
	public static String hexXOR(String array1, String array2) throws Exception {
		String output = bytesToHex(bytesXOR(hexToBytes(array1),hexToBytes(array2)));
		return output.substring(0,array1.length());
	}
	
	public static String hexXOR(String array1, byte key) {
		byte data[] = hexToBytes(array1);
		
		for (int i = 0; i < data.length; i++) {
			data[i] = (byte)(data[i] ^ key);
		}
		
		return new String(data);
	}
	
	public static byte[] strXOR(String plaintext, byte[] key) {
		byte encrypted[] = new byte[plaintext.length()];
		int j = 0;
		
		if (key == null) return null;
		for (int i = 0; i < plaintext.length(); i++) {
			encrypted[i] = (byte)((int)plaintext.charAt(i) ^ (int)key[j]);
			j = (j+1) % key.length;
		}
		
		return encrypted;
	}
	
	public static byte[] strXOR(String plaintext, String key) {
		return strXOR(plaintext, key.getBytes());
	}
	
	public static int count1Bits(byte[] array) {
		int count = 0;
		byte temp = 0;
		for (int i = 0; i < array.length; i++) {
			temp = array[i];
			for (int j = 0; j < 8; j++) {
				if ((temp & 1) == 1)
					count++;
				temp = (byte)(temp >> 1);	
			}
		}
		return count;
	}
	
	public static int hammingDistance(String str1, String str2) throws Exception {
		if (str1.length() != str2.length()) {
			throw new Exception("Strings are different lengths");
		}
		if (str1.length() == 0) return 0;

		byte xor[] = strXOR(str1,str2);
		return count1Bits(xor);
	}
	
	public static int hammingDistance(byte[] array1, byte[] array2) throws Exception {
		byte xor[] = bytesXOR(array1,array2);
		return count1Bits(xor);
	}
	
	public static double normalizedHammingDistance(String str1, String str2) throws Exception {
		int distance = hammingDistance(str1,str2);
		if (str1.length() == 0) return 0;
		
		return (double)distance / str1.length();
	}

	public static double normalizedHammingDistance(byte[] array1, byte[] array2) throws Exception {
		int distance = hammingDistance(array1,array2);
		if (array1.length == 0) return 0;
		
		return (double)distance / array1.length;
	}

}
