package set1;

import static org.junit.Assert.*;

import java.nio.charset.Charset;

import org.junit.Test;

public class BreakRKXORTest {

	@Test
	public void testBestKeysize() {
		String hex = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20";
		int keysizes[] = BreakRKXOR.bestKeysizes(Set1Functions.hexToBytes(hex),3);
		
		assertEquals(12,keysizes[0]);
		assertEquals(5,keysizes[1]);
		assertEquals(7,keysizes[2]);
	}
	
	@Test
	public void testTransposeBlocks() {
		byte cryptotext[] = "hownowbrowncow".getBytes(Charset.forName("US-ASCII"));
		byte transposed[][] = BreakRKXOR.transposeBlocks(cryptotext,3);
		
		assertEquals(3,transposed.length);
		assertArrayEquals("hnbwo".getBytes(Charset.forName("US-ASCII")),transposed[0]);
		assertArrayEquals("oornw".getBytes(Charset.forName("US-ASCII")),transposed[1]);
		assertArrayEquals("wwoc".getBytes(Charset.forName("US-ASCII")),transposed[2]);
	}

}
