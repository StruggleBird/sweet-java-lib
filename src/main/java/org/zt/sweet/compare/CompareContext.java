package org.zt.sweet.compare;
/**
 * 比较内容对象
 * 
 * @author Ternence
 * @date 2014年3月19日
 */
public class CompareContext {
	/**
	 * 来源
	 */
	private Object source;

	/**
	 * 目标
	 */
	private Object target;

	/**
	 * 对比规则
	 */
	private CompareRule rule;

	/**
	 * 构造函数
	 */
	public CompareContext() {
	}

	/**
	 * 构造函数
	 * 
	 * @param source 来源列表或对象
	 * @param target 目标列表或对象
	 */
	public CompareContext(Object source, Object target) {
		this(source, target, null);
	}

	/**
	 * 构造函数
	 * 
	 * @param source 来源列表或对象
	 * @param target 目标列表或对象
	 * @param rule 对比规则
	 */
	public CompareContext(Object source, Object target, CompareRule rule) {
		this.source = source;
		this.target = target;
		this.rule = rule;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public CompareRule getRule() {
		return rule;
	}

	public void setRule(CompareRule rule) {
		this.rule = rule;
	}

}
