package org.zt.sweet.compare;


/**
 * 比较器接口
 * 
 * @author Ternence
 * @date 2014年3月19日
 */
public interface IComparator<S, T> {

	/**
	 * 比较方法
	 * 
	 * @param context 比较对象
	 * @return 结果
	 */
	CompareResult<T> compare(CompareContext context);

	/**
	 * 获取默认规则
	 * 
	 * @return 比较规则
	 */
	CompareRule getDefaultRule();

	/**
	 * 对象新建事件<br>
	 * 在判断出新增对象后调用，在实现方法时需要通过source对象生成target对象，并返回值。
	 * 
	 * @param source 来源对象
	 * @return 目标对象
	 */
	T onCreate(S source);

	/**
	 * 对象更新事件<br>
	 * 在对象被更自动或按规则更新属性之后调用，如果某些属性需要手工调整，可以在该实现方法里修改。
	 * 
	 * @param source 来源对象
	 * @param target 目标对象
	 */
	void onUpdate(S source, T target);

	/**
	 * 对象删除实际<br>
	 * 在判断出删除对象后调用，在实现方法时可以修改某些属性，例如：不做物理删除只做逻辑删除时，可以将dr列改为1
	 * 
	 * @param target 目标对象
	 */
	void onDelete(T target);

}
