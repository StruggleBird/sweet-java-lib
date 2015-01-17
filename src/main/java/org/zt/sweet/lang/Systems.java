/**
 * 
 */
package org.zt.sweet.lang;

/**
 * @author Ternence
 * @create 2015年1月17日
 */
public class Systems {

	/**
	 * 判断当前系统是否为Windows
	 * 
	 * @return true 如果当前系统为Windows系统
	 */
	public static boolean isWin() {
		try {
			String os = System.getenv("OS");
			return os != null && os.indexOf("Windows") > -1;
		} catch (Throwable e) {
			return false;
		}
	}

}
