package org.zt.sweet.compare;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 对比规则<br>
 * 为对象比较提供依据
 * 
 * @author Ternence
 * @date 2014年3月19日
 */
public class CompareRule {

	/**
	 * 来源属性数组
	 */
	private String[] sourceAttrs;

	/**
	 * 目标属性数组
	 */
	private String[] targetAttrs;

	/**
	 * 连接字符
	 * 默认为"_"
	 */
	private String joinChar = "_";

	/**
	 * 是否hash<br>
	 * 如果hash属性为true，则将属性的值连接后用MD5做hash
	 */
	private boolean hash;

	/**
	 * 是否自动更新属性<br>
	 * 如果为true，来源和目标对象属性名相同的列会自动被更新，除了为null的列
	 */
	private boolean autoUpdate;

	public String[] getSourceAttrs() {
		return sourceAttrs;
	}

	public void setSourceAttrs(String[] sourceAttrs) {
		this.sourceAttrs = ArrayUtils.clone(sourceAttrs);
	}

	public String[] getTargetAttrs() {
		return targetAttrs;
	}

	public void setTargetAttrs(String[] targetAttrs) {
		this.targetAttrs = ArrayUtils.clone(targetAttrs);
	}

	public String getJoinChar() {
		return joinChar;
	}

	public void setJoinChar(String joinChar) {
		this.joinChar = joinChar;
	}

	public boolean isHash() {
		return hash;
	}

	public void setHash(boolean hash) {
		this.hash = hash;
	}

	public boolean isAutoUpdate() {
		return autoUpdate;
	}

	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

}
