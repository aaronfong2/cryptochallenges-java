package set2;

import java.nio.charset.Charset;

import set1.Set1Functions;
import set2.Set2Functions;

public class Set2Main {

	public static void main(String[] args) {
		System.out.println("Set2");
		System.out.println("Challenge 9:");
		System.out.println("Padding \"YELLOW SUBMARINE\" to 20 characters yields:");
		
		byte plaintext[] = "YELLOW SUBMARINE".getBytes(Charset.forName("US-ASCII"));
		System.out.println(new String(Set2Functions.PKCS7Pad(plaintext, 20)));
		
		System.out.println("\n\nChallenge 10:");
		byte cryptotext[] = Set1Functions.b64FileToBytes("10.txt");
		byte key[] = "YELLOW SUBMARINE".getBytes(Charset.forName("US-ASCII"));
		byte IV[] = new byte[Set2Functions.AES_BLOCKSIZE]; // All zero, as desired
		byte decrypted[] = Set2Functions.aes128CBCDecrypt(cryptotext, key, IV);
		System.out.println(new String(decrypted));
		
	}

}
