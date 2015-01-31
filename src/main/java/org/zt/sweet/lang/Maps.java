/**
 * 
 */
package org.zt.sweet.lang;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.zt.sweet.bean.Beans;

import com.alibaba.fastjson.JSON;

/**
 * @author Ternence
 * @create 2015年1月17日
 */
public class Maps {

	public static <K, V> HashMap<K, V> newHashMap(
			Map<? extends K, ? extends V> map) {
		return new HashMap<K, V>(map);
	}

	public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
		return new LinkedHashMap<K, V>();
	}

	public static <K extends Comparable<?>, V> TreeMap<K, V> newTreeMap() {
		return new TreeMap<K, V>();
	}

	public static Map<String, Object> asMap(Object bean, Class<Object> clazz) {
		return Beans.toMap(bean);
	}

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
