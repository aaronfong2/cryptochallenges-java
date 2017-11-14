package set1;

import set1.Set1Ciphers;
import java.security.SecureRandom;
import static org.junit.Assert.*;


import org.junit.Test;

public class Set1CiphersTest {
	private static final int AES128_KEYSIZE = 16;

	@Test
	public void testAes128ECB() {
		byte data[] = "Hello World! SECRET ABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes();
		SecureRandom keygen = new SecureRandom();
		byte key[] = new byte[AES128_KEYSIZE];

		keygen.nextBytes(key);
		System.out.println(Set1Functions.bytesToHex(key));
		
		byte encoded[] = Set1Ciphers.aes128ECBEncode(data, key);
		System.out.println(Set1Functions.bytesToHex(encoded));
		byte decoded[] = Set1Ciphers.aes128ECBDecode(encoded, key);
		System.out.println(new String(decoded));
		assertArrayEquals(data, decoded);
		
	}

}
