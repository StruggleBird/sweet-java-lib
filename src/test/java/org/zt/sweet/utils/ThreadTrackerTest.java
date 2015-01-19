/**
 * 
 */
package org.zt.sweet.utils;

import org.junit.Test;
import org.zt.sweet.log.Logs;
import org.zt.sweet.utils.ThreadTracker;

/**
 * @author Ternence
 * @create 2015年1月18日
 */
public class ThreadTrackerTest {

	@Test
	public void test() {
		ThreadTracker.track("test");
		Logs.get().info("hello");
		System.out.println("");
		ThreadTracker.result();
	}

}
