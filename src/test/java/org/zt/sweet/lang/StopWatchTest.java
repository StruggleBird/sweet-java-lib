/**
 * 
 */
package org.zt.sweet.lang;

import org.junit.Test;
import org.zt.sweet.utils.StopWatch;

/**
 * @author Ternence
 * @create 2015年1月18日
 */
public class StopWatchTest {

	@Test
	public void testA() {
		StopWatch sw = StopWatch.begin();
		Lang.sleep(1000);
		sw.stop();
		System.out.println(sw.taken());
	}

}
