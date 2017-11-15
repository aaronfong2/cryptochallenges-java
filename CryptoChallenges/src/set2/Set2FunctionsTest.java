package set2;

import static org.junit.Assert.*;
import java.security.SecureRandom;

import org.junit.Test;

public class Set2FunctionsTest {
	@Test
	public void testAes128CBCBlockAligned() {
		byte[] data = "TEST DATA 123456".getBytes();
		byte[] key = new byte[Set2Functions.AES128_KEYSIZE];
		byte[] IV = new byte[Set2Functions.AES_BLOCKSIZE];
		SecureRandom random = new SecureRandom();
		random.nextBytes(key);
		random.nextBytes(IV);
		byte[] encrypted = Set2Functions.aes128CBCEncrypt(data, key, IV);
		byte[] decrypted = Set2Functions.aes128CBCDecrypt(encrypted, key, IV);
		
		assertArrayEquals(data, decrypted);
	}
	
	@Test
	public void testAes128CBCUnaligned() {
		byte[] data = "123456789123456789123456789".getBytes();
		byte[] key = new byte[Set2Functions.AES128_KEYSIZE];
		byte[] IV = new byte[Set2Functions.AES_BLOCKSIZE];
		SecureRandom random = new SecureRandom();
		random.nextBytes(key);
		random.nextBytes(IV);
		byte[] encrypted = Set2Functions.aes128CBCEncrypt(data, key, IV);
		byte[] decrypted = Set2Functions.aes128CBCDecrypt(encrypted, key, IV);
		
		assertArrayEquals(data, decrypted);
	}

}
