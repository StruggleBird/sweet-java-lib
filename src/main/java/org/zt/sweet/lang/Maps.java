/**
 * 
 */
package org.zt.sweet.lang;

import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * @author Ternence
 * @create 2015年1月17日
 */
public class Maps {

	/**
	 * Create a new map by one text
	 * 
	 * Can specify inner map string like :{"a":{a1:'a1'}}, so,you well get a map
	 * contains key "a" and value Map object that contains one kv map.
	 * 
	 * @param text
	 * @return
	 * @create 2015年1月17日
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> newMap(String text) {
		return (Map<String, Object>) JSON.parse(text);
	}
}
