package set2;

import java.nio.charset.Charset;

import set2.Set2Functions;

public class Set2Main {

	public static void main(String[] args) {
		System.out.println("Set2");
		System.out.println("Challenge 9:");
		System.out.println("Padding \"YELLOW SUBMARINE\" to 20 characters yeilds:");
		
		byte plaintext[] = "YELLOW SUBMARINE".getBytes(Charset.forName("US-ASCII"));
		System.out.println(new String(Set2Functions.PKCS7Pad(plaintext, 20)));
	}

}
