/**
 * 
 */
package org.zt.sweet.lang;

import java.lang.reflect.Constructor;

/**
 * 构造器，用于构造任何java对象
 * 
 * @author Ternence
 * @create 2015年1月18日
 */
public class Builder<T> {

	private TypeExtractor typeExtractor;
	private Class<T> clazz;

	private Builder(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * 包裹一个类
	 * 
	 * @param clazz
	 *            类
	 * @return Builder
	 */
	public static <T> Builder<T> me(Class<T> clazz, TypeExtractor typeExtractor) {
		return null == clazz ? null : new Builder<T>(clazz)
				.setTypeExtractor(typeExtractor);
	}

	/**
	 * 生成一个对象的 Builder
	 * 
	 * @param obj
	 *            对象。
	 * @return Builder， 如果 对象 null，则返回 null
	 */
	@SuppressWarnings("unchecked")
	public static <T> Builder<T> me(T obj) {
		if (obj == null)
			return null;
		if (obj instanceof Class<?>)
			return (Builder<T>) me((Class<?>) obj);
		return (Builder<T>) me(obj.getClass());
	}

	/**
	 * 包裹一个类
	 * 
	 * @param clazz
	 *            类
	 * @return Builder
	 */
	public static <T> Builder<T> me(Class<T> clazz) {
		return null == clazz ? null : new Builder<T>(clazz);
	}

	/**
	 * @return
	 * @create 2015年1月18日
	 */
	private Builder<T> setTypeExtractor(TypeExtractor typeExtractor) {
		this.typeExtractor = typeExtractor;
		return this;
	}

	/**
	 * 获取包裹在内部的类型
	 * 
	 * @return
	 * @create 2015年1月18日
	 */
	public Class<T> getInnerClass() {
		return clazz;
	}
	
	

	/**
	 * @return the typeExtractor
	 */
	public TypeExtractor getTypeExtractor() {
		return typeExtractor;
	}

	/**
	 * 根据构造函数参数，创建一个对象。
	 * 
	 * @param args
	 *            构造函数参数
	 * @return 新对象
	 */
	public T build(Object... args) {
		try {
			if (args == null || args.length == 0) {
				return clazz.newInstance();
			} else {
				Class<?>[] parameterTypes = new Class<?>[args.length];
				for (int i = 0; i < args.length; i++) {
					Object arg = args[i];
					parameterTypes[i] = arg.getClass();
				}
				Constructor<T> constructor = clazz
						.getConstructor(parameterTypes);
				return constructor.newInstance(args);
			}
		} catch (Exception e) {
			Lang.wrapThrow(e);
		}
		return null;
	}

}
