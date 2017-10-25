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
				formatter.format("%x", data[i]);
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

}
