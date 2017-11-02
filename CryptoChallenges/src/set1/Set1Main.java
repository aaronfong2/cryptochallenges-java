package set1;

import static set1.Set1Functions.*;
import static set1.Set1Ciphers.*;

public class Set1Main {

	public static void main(String[] args) {
		String hex = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
		String output = null;
		
		output = hexToB64(hex);
		
		System.out.println("CHALLENGE 1");
		System.out.println("Hex:");
		System.out.println(hex);
		System.out.println("Base64:");
		System.out.println(output);
		
		String array1 = "1c0111001f010100061a024b53535009181c";
		String array2 = "686974207468652062756c6c277320657965";
		try {
			output = hexXOR(array1,array2);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		System.out.println("\nCHALLENGE2");
		System.out.println("Array1:");
		System.out.println(array1);
		System.out.println("Array2:");
		System.out.println(array2);
		System.out.println("XOR:");
		System.out.println(output);

		
		System.out.println("\nCHALLENGE3");
		//System.out.println(hexXOR("ab12ab",(byte)0x11));
		String c3encoded = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";

		System.out.println("Encoded string:");
		System.out.println(c3encoded);
		System.out.println("Top Decodings: key, score, decoded text");
		StrScore scores[] = charDecHex(c3encoded, 5);	
		for (StrScore s: scores) {
			System.out.print(new String(s.key) + ",");
			System.out.print(s.score + ",");
			System.out.println(new String(s.str));
		}

		System.out.println("\nCHALLENGE4");
		String filename = "4.txt";
		scores = charDecHexLines(filename, 5);
		
		System.out.println("Top Decodings: key, score, line#, decoded text");
		for (StrScore s: scores) {
			System.out.print(new String(s.key) + ",");
			System.out.print(s.score + ",");
			System.out.print(s.getLineNum() + ",");
			System.out.println(new String(s.str));
		}
		
		System.out.println("\nCHALLENGE5");
		String line1 = "Burning 'em, if you ain't quick and nimble";
		String line2 = "I go crazy when I hear a cymbal";
		System.out.println("Plaintext:");
		System.out.println(line1 + "\n" + line2);
		System.out.println("Encrypted with \"ICE\"");
		System.out.println(bytesToHex(strXOR(line1,"ICE")));
		System.out.println(bytesToHex(strXOR(line2,"ICE")));

		System.out.println("\nCHALLENGE6");
		String filename2 = "6.txt";
		System.out.println("Possible plaintexts:");
		StrScore decoded[] = BreakRKXOR.decodeFile(filename2, 4);
		for (StrScore s: decoded) {
			System.out.println("Score: " + s.score);
			System.out.println("Key: " + new String(s.key));
			System.out.println("Message: " + new String(s.str));
			System.out.println("----------------------------------------------");
			System.out.println("----------------------------------------------");
		}

		
		
	}

}
