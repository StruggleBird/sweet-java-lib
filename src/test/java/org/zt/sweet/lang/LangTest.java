/**
 * 
 */
package org.zt.sweet.lang;

import org.junit.Assert;
import org.junit.Test;
import org.zt.sweet.testsuit.Bean;

/**
 * @author Ternence
 * @create 2015年1月18日
 */
public class LangTest {

	@Test
	public void testArray() {
		String[] stringArr = Lang.array("a", "b", "c");
		Bean[] beanArr = Lang.array(new Bean("1"), new Bean("2"));
		
		Assert.assertEquals(2, beanArr.length);
	}

}
