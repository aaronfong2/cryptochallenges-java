package set1;

import static org.junit.Assert.*;

import org.junit.Test;

public class Set1FunctionsTest {

	@Test
	public void testHexToBytesEven() {
		String s = "abc123";
		byte array[] = {(byte)0xab,(byte)0xc1,(byte)0x23};
		byte output[] = null;
		
		output = Set1Functions.hexToBytes(s);
		assertArrayEquals(array,output);
	}
	
	@Test
	public void testHexToBytesOdd() {
		String s = "fe2311c";
		byte array[] = {(byte)0xfe,(byte)0x23,(byte)0x11,(byte)0xc0};
		byte output[] = null;
		
		output = Set1Functions.hexToBytes(s);
		assertArrayEquals(array,output);
	}
	
	@Test
	public void testBytesToHex() {
		byte array[] = {(byte)0xae,(byte)0x33,(byte)0xf0};
		String hex = "ae33f0";
		String output = null;
		
		output = Set1Functions.bytesToHex(array);
		assertEquals(hex,output);
	}

	@Test
	public void testHexToB64() {
		String hex = "ab8942";
		String b64 = "q4lC";
		String output = Set1Functions.hexToB64(hex);
		
		assertEquals(b64,output);
	}
	
	@Test
	public void longTestHexToB64() {
		String hex = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
		String b64 = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t";
		String output = Set1Functions.hexToB64(hex);
		
		assertEquals(b64,output);
	}
	
	@Test
	public void testHexXOREven() {
		String array1 = "111111";
		String array2 = "ae0134";
		String answer = "bf1025";
		String output = null;
		try {
			output = Set1Functions.hexXOR(array1, array2);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		
		assertEquals(answer,output);
		
	}
	
	@Test
	public void testHexXOROdd() {
		String array1 = "11111";
		String array2 = "ae013";
		String answer = "bf102";
		String output = null;
		try {
			output = Set1Functions.hexXOR(array1, array2);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		
		assertEquals(answer,output);
		
	}
	
	@Test
	public void testHexXORLong() {
		String array1 = "1c0111001f010100061a024b53535009181c";
		String array2 = "686974207468652062756c6c277320657965";
		String answer = "746865206b696420646f6e277420706c6179";
		String output = null;
		try {
			output = Set1Functions.hexXOR(array1, array2);
		}
		catch (Exception e) {
			fail(e.getMessage());
		}
		
		
		assertEquals(answer,output);
		
	}
	
	@Test
	public void testHexXOR1Byte() {
		String array1 = "7171";
		byte key = '0';
		String answer = "AA";
		String output = Set1Functions.hexXOR(array1, key);
		
		assertEquals(answer,output);
	}

}
