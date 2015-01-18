/**
 * 
 */
package org.zt.sweet.collection;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Ternence
 * @create 2015年1月17日
 */
public class MapsTest {

	@Test
	public void testCreate() {
		Map<String, Object> map = Maps.newMap("{a:'111',b:'222',c:{c1:'333'}}");
		Assert.assertTrue(map.containsKey("a"));
		Assert.assertEquals("222", map.get("b"));
		Assert.assertTrue(map.containsKey("c"));
		Map innerMap = (Map) map.get("c");
		Assert.assertEquals("333", innerMap.get("c1"));

	}
}
