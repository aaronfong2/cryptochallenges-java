package set2;

import static org.junit.Assert.*;
import java.security.SecureRandom;
import java.util.Arrays;

import org.junit.Test;

public class Set2FunctionsTest {
	// Blocksize/keysize in bytes
	private static final int AES_BLOCKSIZE = 16;
	private static final int AES128_KEYSIZE = 16;

	@Test
	public void testAes128CBCBlockAligned() {
		byte[] data = "TEST DATA 123456".getBytes();
		byte[] key = new byte[AES128_KEYSIZE];
		byte[] IV = new byte[AES_BLOCKSIZE];
		SecureRandom random = new SecureRandom();
		random.nextBytes(key);
		random.nextBytes(IV);
		byte[] encrypted = Set2Functions.aes128CBCEncrypt(data, key, IV);
		byte[] decrypted = Set2Functions.aes128CBCDecrypt(encrypted, key, IV);
		
		assertArrayEquals(data, decrypted);
	}

}
