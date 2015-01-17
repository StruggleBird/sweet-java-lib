package org.zt.sweet.compare;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.zt.sweet.common.Assert;
import org.zt.sweet.digest.Digests;
import org.zt.sweet.log.Logs;

/**
 * 比对器基类
 * 
 * @param <S>
 *            来源对象类型
 * @param <T>
 *            目标对象类型
 * @author Steven.Deng
 * @date 2014年3月19日
 */
@SuppressWarnings({ "unchecked" })
public abstract class AbstractComparator<S, T> implements IComparator<S, T> {

	/**
	 * 日志
	 */
	private Logger logger = Logs.get(getClass());

	/**
	 * 默认对比规则
	 */
	private CompareRule defaultRule;

	@Override
	public CompareRule getDefaultRule() {
		return defaultRule;
	}

	public void setDefaultRule(CompareRule defaultRule) {
		this.defaultRule = defaultRule;
	}

	@Override
	public CompareResult<T> compare(CompareContext context) {
		CompareRule rule = context.getRule() != null ? context.getRule()
				: getDefaultRule();

		Assert.notNull(rule, "CompareRule can't be null.");

		Object source = context.getSource();
		Object target = context.getTarget();

		Assert.notNull(source, "Source can't be null.");
		Assert.notNull(target, "Target can't be null.");

		// 集合 vs 集合对比
		if (source instanceof Collection && target instanceof Collection) {
			return doCompare((Collection<S>) source, (Collection<T>) target,
					rule);
		}
		// 对象 vs 对象对比
		else if (!(source instanceof Collection)
				&& !(target instanceof Collection)) {
			Collection<S> sourceList = new ArrayList<S>();
			sourceList.add((S) source);

			Collection<T> targetList = new ArrayList<T>();
			targetList.add((T) target);

			return doCompare(sourceList, targetList, rule);

		}
		// 对象 vs 集合对比
		else if (!(source instanceof Collection)
				&& target instanceof Collection) {
			Collection<S> sourceList = new ArrayList<S>();
			sourceList.add((S) source);

			return doCompare(sourceList, (Collection<T>) target, rule);
		}
		// 集合 vs 对象对比
		else if (source instanceof Collection
				&& !(target instanceof Collection)) {

			Collection<T> targetList = new ArrayList<T>();
			targetList.add((T) target);

			return doCompare((Collection<S>) source, targetList, rule);
		}
		// 不支持的对比类型
		else {
			throw new IllegalArgumentException(String.format(
					"Not support compare %s vs %s", source, target));
		}

	}

	@Override
	public void onUpdate(S source, T target) {
	}

	@Override
	public void onDelete(T target) {
	}

	protected CompareResult<T> doCompare(Collection<S> source,
			Collection<T> target, CompareRule rule) {
		logger.info("comparing...");
		long start = System.currentTimeMillis();
		CompareResult<T> result = new CompareResult<T>();

		// 正向扫描，保存需要新增和修改的对象
		if (source != null && source.size() > 0) {
			// 临时目标对象
			T tmpTarget = null;
			for (S s : source) {
				if (isNeedCreate(s, target, rule)) {
					tmpTarget = onCreate(s);
					result.getNewList().add(tmpTarget);
				} else if ((tmpTarget = getUpdateObject(s, target, rule)) != null) {
					// 计算修改之前的hash值
					int beforeUpdateHash = HashCodeBuilder.reflectionHashCode(
							tmpTarget, true);

					// 如果需要自动更新
					if (rule.isAutoUpdate()) {
						copyProperties(s, tmpTarget);
					}

					// 手工更新
					onUpdate(s, tmpTarget);
					// 计算修改之后的hash值
					int afterUpdateHash = HashCodeBuilder.reflectionHashCode(
							tmpTarget, true);
					if (beforeUpdateHash != afterUpdateHash) {
						result.getUpdateList().add(tmpTarget);
					}
				}
			}
		}

		// 反向扫描,保存需要删除的对象
		if (target != null && target.size() > 0) {
			for (T t : target) {
				if (isNeedDelete(source, t, rule)) {
					onDelete(t);
					result.getDeleteList().add(t);
				}
			}
		}
		long end = System.currentTimeMillis();
		logger.info("complete..time-consuming : " + (end - start) + "ms");
		return result;
	}

	/**
	 * 复制属性<br>
	 * 复制名称相同的属性，忽略null值
	 * 
	 * @param source
	 *            来源对象
	 * @param target
	 *            目标对象
	 */
	protected void copyProperties(S source, T target) {
		try {
			PropertyUtils.copyProperties(target, source);
		} catch (Exception e) {
			logger.error("Error occur:", e);
		}
	}

	/**
	 * 获取对比属性值的拼接结果
	 * 
	 * @param obj
	 *            对比对象
	 * @param fields
	 *            对比列
	 * @param joinChar
	 *            链接字符
	 * @param hash
	 *            是否hash
	 * @return 拼接字符串
	 */
	private String getCompareValue(Object obj, String[] fields,
			String joinChar, boolean hash) {
		Assert.notNull(obj, "Object can't be null.");
		Assert.notEmpty(fields, "Compare fields can't be empty.");

		StringBuffer sb = new StringBuffer();
		try {
			for (String field : fields) {
				if (joinChar != null && sb.length() > 0) {
					sb.append(joinChar);
				}
				Object tmp = PropertyUtils.getProperty(obj, field);
				sb.append(tmp == null ? "" : tmp.toString());
			}
		} catch (Exception e) {
			logger.error("Error occur:", e);
		}
		String value = sb.toString();
		return hash ? Digests.md5DigestAsHex(value.getBytes()) : value;
	}

	/**
	 * 判断来源对象和目标对象是否相等
	 * 
	 * @param source
	 *            来源对象
	 * @param target
	 *            目标对象
	 * @param rule
	 *            对比规则
	 * @return 布尔值
	 */
	private boolean equals(Object source, Object target, CompareRule rule) {
		Assert.notNull(rule, "CompareRule can't be null.");
		// 来源属性值
		String sValue = getCompareValue(source, rule.getSourceAttrs(),
				rule.getJoinChar(), rule.isHash());
		// 目标属性值
		String tValue = getCompareValue(target, rule.getTargetAttrs(),
				rule.getJoinChar(), rule.isHash());
		return sValue.equals(tValue);

	}

	/**
	 * 检查来源对象是否需要增加
	 * 
	 * @param s
	 *            来源对象
	 * @param target
	 *            目标对象列表
	 * @param rule
	 *            对比规则
	 * @return 是否新增
	 */
	private boolean isNeedCreate(S s, Collection<T> target, CompareRule rule) {
		if (target != null && target.size() > 0) {
			for (T t : target) {
				if (equals(s, t, rule)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 获取需要更新的对象
	 * 
	 * @param s
	 *            来源对象
	 * @param target
	 *            目标对象列表
	 * @param rule
	 *            对比规则
	 * @return 需要更新的对象
	 */
	private T getUpdateObject(S s, Collection<T> target, CompareRule rule) {
		if (target != null && target.size() > 0) {
			for (T t : target) {
				if (equals(s, t, rule)) {
					return t;
				}
			}
		}
		return null;
	}

	/**
	 * 判断对象是否需要删除
	 * 
	 * @param source
	 *            来源对象列表
	 * @param t
	 *            目标对象
	 * @param rule
	 *            对比规则
	 * @return 布尔值
	 */
	private boolean isNeedDelete(Collection<S> source, T t, CompareRule rule) {
		if (source != null && source.size() > 0) {
			for (S s : source) {
				if (equals(s, t, rule)) {
					return false;
				}
			}
		}
		return true;
	}

}
