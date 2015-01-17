/**
 * 
 */
package org.zt.sweet.json;

import com.alibaba.fastjson.JSON;

/**
 * Json Tookit,base on fastjson
 * 
 * @author Ternence
 * @create 2015年1月17日
 */
public abstract class Jsons {

	/**
	 * Conversion object to json formart String.
	 * 
	 * @param object
	 * @return
	 * @create 2015年1月17日
	 */
	public static String toString(Object object) {
		return JSON.toJSONString(object);
	}

	/**
	 * Parse a json formart string to Object specified
	 * @param jsonString
	 * @param clazz
	 * @return
	 * @create 2015年1月17日
	 */
	public static <T> T parse(String jsonString, Class<T> clazz) {
		return JSON.parseObject(jsonString, clazz);
	}
}
