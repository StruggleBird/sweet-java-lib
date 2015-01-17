/**
 * 
 */
package org.zt.sweet.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs Tookit,base on Slf4j-api
 * @author Ternence
 * @create 2015年1月17日
 */
public abstract class Logs {

	/**
	 * Get slf4j log instance
	 * The easiest way to get log instance
	 * @return
	 * @create 2015年1月17日
	 */
	public static Logger get() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		return LoggerFactory.getLogger(sts[2].getClassName());
	}
	
	public static Logger get(Class<?> clazz) {
        return get(clazz.getName());
    }
	
	public static Logger get(String className) {
		return LoggerFactory.getLogger(className);
    }
}
