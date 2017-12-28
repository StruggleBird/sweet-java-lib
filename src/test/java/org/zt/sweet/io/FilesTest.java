/**
 * 
 */
package org.zt.sweet.io;

import java.io.File;

import org.junit.Test;
import org.zt.sweet.utils.Assert;

/**
 * @author Ternence
 * @create 2015年1月18日
 */
public class FilesTest {

	@Test
	public void testLsAll() {
        File d = new File(".");
        if (!d.exists()) {
            return;
        }
		File[] files = Files.lsAll(d, ".*jar");

		files = Files.lsAll(d, ".*", true);
		for (File file : files) {
			System.out.println(file);
		}

	}

    @Test
    public void testIsPicture() {
        Assert.isTrue(Files.isPicture("f:/aa/a.png"));
        Assert.isTrue(Files.isPicture("a.jpg"));
        Assert.isTrue(!Files.isPicture("a"));
        Assert.isTrue(!Files.isPicture(""));

        Assert.isTrue(Files.isPicture("a.png", "png"));
        Assert.isTrue(!Files.isPicture("a.png", "jpg"));
    }

}
