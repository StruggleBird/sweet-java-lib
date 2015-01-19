/**
 * 
 */
package org.zt.sweet.lang;

import java.util.Date;

import org.junit.Test;

/**
 * @author Ternence
 * @create 2015年1月18日
 */
public class DateTimesTest {

	/**
	 * Test method for {@link org.zt.sweet.lang.DateTimes#printDuration(double)}.
	 */
	@Test
	public void testPrintDuration() {
		System.out.println(new Date().getTime());
		String res = DateTimes.printDuration(24*60*1000);
		System.out.println(res);
	}

}
