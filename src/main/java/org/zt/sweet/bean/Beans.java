package org.zt.sweet.bean;

import java.beans.PropertyDescriptor;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Zhoutao
 * @date 2014-7-10
 */
public class Beans {

	protected static Log log;

	/**
	 * 
	 */
	public Beans() {
		log = LogFactory.getLog(getClass());
	}

	/**
	 * 获取对象中的所有非空字段，并生成json串返回 忽略为空的字段
	 * 
	 * @author Zhoutao
	 * @date 2014-7-10
	 * @recently Zhoutao
	 * @param obj
	 * @return json 字符串 eg:{"jobType":"DM","slCode":3565,"usrCode":35}
	 */
	public static String describe(Object obj) {
		JSON json = (JSON) JSON.toJSON(obj);
		return json.toString();
	}

	/**
	 * 将一个 JavaBean 对象转化为一个 Map,字段名作为key，字段值作为value
	 * 
	 * @author Zhoutao
	 * @date 2014-7-30
	 * @recently Zhoutao
	 * @param bean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(Object bean) {
		if (bean instanceof Map) {
			return (Map<String, Object>) bean;
		}
		Object object = JSON.toJSON(bean);
		if (object instanceof Map) {
			return (Map<String, Object>) object;
		}
		return null;
	}

	/**
	 * map 转 javabean
	 * 
	 * @author Zhoutao
	 * @date 2014-7-30
	 * @recently Zhoutao
	 * @param <T>
	 * @param map
	 * @param clazz
	 * @return
	 */
	public static <T> T toBean(Map<String, Object> map, Class<T> clazz) {
		JSONObject jsonObject = new JSONObject(map);
		return JSON.toJavaObject(jsonObject, clazz);
	}

	/**
	 * 复制属性 调用apache的工具类，进行封装，去除了反射异常
	 * 
	 * @see BeanUtilsBean#copyProperties
	 * @author Zhoutao
	 * @date 2014-8-6
	 * @recently Zhoutao
	 * @param dest
	 * @param orig
	 */
	public static void copyProperties(Object dest, Object orig) {
		try {
			org.apache.commons.beanutils.PropertyUtils.copyProperties(dest,
					orig);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("copyProperties() error:" + dest.getClass() + "'", e);
			}
		}
	}

	/**
	 * 复制原始对象中非null字段到目标对象
	 *
	 *
	 * @author Zhoutao
	 * @date 2014-8-6
	 * @recently Zhoutao
	 * @param dest
	 * @param orig
	 */
	public static void copyNotNullProperties(Object dest, Object orig) {
		try {
			copyProperties(dest, orig, false, false);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("copyProperties() error:" + dest.getClass() + "'", e);
			}
		}
	}

	/**
	 * 复制原始对象中字段到目标对象中的空字段 目标对象中的字段如果已经有值,不进行覆盖
	 * 
	 * @author Zhoutao
	 * @date 2014-8-6
	 * @param dest
	 * @param orig
	 */
	public static void copyToNullProperties(Object dest, Object orig) {
		try {
			copyProperties(dest, orig, true, false);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("copyProperties() error:" + dest.getClass() + "'", e);
			}
		}
	}

	/**
	 * 
	 * 复制原始对象中非null字段到目标对象中的null字段 NN:not null property (非null字段)
	 * 
	 * @author Zhoutao
	 * @date 2014-8-6
	 * @recently Zhoutao
	 * @param dest
	 * @param orig
	 */
	public static void copyNNToNullProperties(Object dest, Object orig) {
		try {
			copyProperties(dest, orig, false, false);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("copyProperties() error:" + dest.getClass() + "'", e);
			}
		}
	}

	/**
	 * 
	 * 复制原始对象中非null字段到目标对象中的非null字段 NN:not null property (非null字段)
	 * 
	 * @author Zhoutao
	 * @date 2014-8-6
	 * @recently Zhoutao
	 * @param dest
	 * @param orig
	 */
	public static void copyNNToNNProperties(Object dest, Object orig) {
		try {
			copyProperties(dest, orig, false, true);
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("copyProperties() error:" + dest.getClass() + "'", e);
			}
		}
	}

	/**
	 * 复制标准的javabean对象
	 * 
	 * @author Zhoutao
	 * @date 2014-8-6
	 * @recently Zhoutao
	 * @param dest
	 * @param orig
	 * @param copyNull
	 *            是否复制null字段到目标对象
	 * @param copyToNotNull
	 *            如果目标对象字段值不为null，是否允许复制到目标字段.true:不允许，false:允许
	 * @throws Exception
	 */
	public static void copyProperties(Object dest, Object orig,
			boolean copyNull, boolean copyToNotNull) throws Exception {

		if (dest == null) {
			throw new IllegalArgumentException("No destination bean specified");
		}
		if (orig == null) {
			throw new IllegalArgumentException("No origin bean specified");
		}
		PropertyDescriptor[] origDescriptors = PropertyUtils
				.getPropertyDescriptors(orig);
		for (int i = 0; i < origDescriptors.length; i++) {
			String name = origDescriptors[i].getName();
			if (PropertyUtils.isReadable(orig, name)
					&& PropertyUtils.isWriteable(dest, name)) {
				try {
					Object value = PropertyUtils.getSimpleProperty(orig, name);
					if (value == null && !copyNull) {
						continue;
					}
					if (copyToNotNull) {
						Object destFieldValue = PropertyUtils
								.getSimpleProperty(dest, name);
						if (destFieldValue == null) {
							continue;
						}
					}

					PropertyUtils.setSimpleProperty(dest, name, value);
				} catch (NoSuchMethodException e) {
					if (log.isDebugEnabled()) {
						log.debug("Error writing to '" + name + "' on class '"
								+ dest.getClass() + "'", e);
					}
				}
			}
		}

	}

}
