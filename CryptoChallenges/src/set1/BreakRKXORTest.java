package set1;

import static org.junit.Assert.*;

import org.junit.Test;

public class BreakRKXORTest {

	@Test
	public void testBestKeysize() {
		String hex = "0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20";
		int keysizes[] = BreakRKXOR.bestKeysizes(new String(Set1Functions.hexToBytes(hex)),3);
		
		assertEquals(12,keysizes[0]);
		assertEquals(5,keysizes[1]);
		assertEquals(7,keysizes[2]);
	}
	
	@Test
	public void testTransposeBlocks() {
		String cryptotext = "hownowbrowncow";
		String transposed[] = BreakRKXOR.transposeBlocks(cryptotext,3);
		
		assertEquals(3,transposed.length);
		assertEquals("hnbwo",transposed[0]);
		assertEquals("oornw",transposed[1]);
		assertEquals("wwoc",transposed[2]);
	}

}
