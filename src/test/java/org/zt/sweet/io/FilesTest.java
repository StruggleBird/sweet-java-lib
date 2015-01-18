/**
 * 
 */
package org.zt.sweet.io;

import java.io.File;

import org.junit.Test;

/**
 * @author Ternence
 * @create 2015年1月18日
 */
public class FilesTest {

	@Test
	public void testLsAll() {
		File d = new File("F:\\github\\sweet-java-lib\\trunk\\target");
		File[] files = Files.lsAll(d, ".*jar");

		files = Files.lsAll(d, ".*", true);
		for (File file : files) {
			System.out.println(file);
		}

	}

}
