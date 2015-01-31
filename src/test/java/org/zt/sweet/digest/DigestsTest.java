/**
 * 
 */
package org.zt.sweet.digest;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.zt.sweet.security.Digests;

/**
 * @author Ternence
 * @create 2015年1月18日
 */
public class DigestsTest {

	/**
	 * Test method for {@link org.zt.sweet.security.Digests#md5Digest(byte[])}.
	 */
	@Test
	public void testMd5Digest() {
		Assert.assertTrue(Digests.md5("abc".getBytes()) != null);
		System.out.println(Digests.md5AsHex("abc"));
		System.out.println(Digests.md5AsHexTruncate(new Date().toString(),5));
		Assert.assertTrue(Digests.md5("abc") != null);
	}

	/**
	 * Test method for
	 * {@link org.zt.sweet.security.Digests#md5DigestAsHex(byte[])}.
	 */
	@Test
	public void testMd5DigestAsHex() {
		String hexString = Digests.fixedHexString(Digests.md5("abc"));
		Assert.assertEquals("900150983cd24fb0d6963f7d28e17f72", hexString);
		hexString = Digests.md5AsHex("abc");
		Assert.assertEquals("900150983cd24fb0d6963f7d28e17f72", hexString);
	}

	/**
	 * Test method for
	 * {@link org.zt.sweet.security.Digests#appendMd5DigestAsHex(byte[], java.lang.StringBuilder)}
	 * .
	 */
	@Test
	public void testAppendMd5DigestAsHex() {
		StringBuilder hexString = Digests.appendMd5DigestAsHex(
				"abc".getBytes(), new StringBuilder("0123-"));
		Assert.assertEquals("0123-900150983cd24fb0d6963f7d28e17f72",
				hexString.toString());
	}

}
