/**
 * 
 */
package org.zt.sweet.log;

import org.junit.Test;
import org.slf4j.Logger;

/**
 * @author Ternence
 * @create 2015年1月18日
 */
public class LogsTest {

	@Test
	public void test() {
		Logger log =  Logs.get();
		log.info("test");
	}

}
