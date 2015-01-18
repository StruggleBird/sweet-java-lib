package org.zt.sweet.lang;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 这些帮助函数让 Java 的某些常用功能变得更简单
 * 
 * @author zozoh(zozohtnt@gmail.com)
 * @author wendal(wendal1985@gmail.com)
 * @author bonyfish(mc02cxj@gmail.com)
 */
public abstract class Lang {

	public static ComboException comboThrow(Throwable... es) {
		ComboException ce = new ComboException();
		for (Throwable e : es)
			ce.add(e);
		return ce;
	}

	/**
	 * 生成一个未实现的运行时异常
	 * 
	 * @return 一个未实现的运行时异常
	 */
	public static RuntimeException noImplement() {
		return new RuntimeException("Not implement yet!");
	}

	/**
	 * 生成一个不可能的运行时异常
	 * 
	 * @return 一个不可能的运行时异常
	 */
	public static RuntimeException impossible() {
		return new RuntimeException("r u kidding me?! It is impossible!");
	}

	/**
	 * 根据格式化字符串，生成运行时异常
	 * 
	 * @param format
	 *            格式
	 * @param args
	 *            参数
	 * @return 运行时异常
	 */
	public static RuntimeException makeThrow(String format, Object... args) {
		return new RuntimeException(String.format(format, args));
	}

	/**
	 * 根据格式化字符串，生成一个指定的异常。
	 * 
	 * @param classOfT
	 *            异常类型， 需要有一个字符串为参数的构造函数
	 * @param format
	 *            格式
	 * @param args
	 *            参数
	 * @return 异常对象
	 */
	public static <T extends Throwable> T makeThrow(Class<T> classOfT,
			String format, Object... args) {

		try {
			return classOfT.getConstructor(String.class).newInstance(
					String.format(format, args));
		} catch (Exception e2) {
			throw new RuntimeException(e2);
		}
	}

	/**
	 * 将抛出对象包裹成运行时异常，并增加自己的描述
	 * 
	 * @param e
	 *            抛出对象
	 * @param fmt
	 *            格式
	 * @param args
	 *            参数
	 * @return 运行时异常
	 */
	public static RuntimeException wrapThrow(Throwable e, String fmt,
			Object... args) {
		return new RuntimeException(String.format(fmt, args), e);
	}

	/**
	 * 用运行时异常包裹抛出对象，如果抛出对象本身就是运行时异常，则直接返回。
	 * <p>
	 * 如果是 InvocationTargetException，那么将其剥离，只包裹其 TargetException
	 * 
	 * @param e
	 *            抛出对象
	 * @return 运行时异常
	 */
	public static RuntimeException wrapThrow(Throwable e) {
		if (e instanceof RuntimeException)
			return (RuntimeException) e;
		if (e instanceof InvocationTargetException)
			return wrapThrow(((InvocationTargetException) e)
					.getTargetException());
		return new RuntimeException(e);
	}

	/**
	 * 用一个指定可抛出类型来包裹一个抛出对象。这个指定的可抛出类型需要有一个构造函数 接受 Throwable 类型的对象
	 * 
	 * @param e
	 *            抛出对象
	 * @param wrapper
	 *            包裹类型
	 * @return 包裹后对象
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Throwable> T wrapThrow(Throwable e,
			Class<T> wrapper) {
		if (wrapper.isAssignableFrom(e.getClass()))
			return (T) e;
		try {
			return wrapper.getConstructor(Throwable.class).newInstance(e);
		} catch (Exception e2) {
			throw new RuntimeException(e2);
		}
	}

	public static Throwable unwrapThrow(Throwable e) {
		if (e == null)
			return null;
		if (e instanceof InvocationTargetException) {
			InvocationTargetException itE = (InvocationTargetException) e;
			if (itE.getTargetException() != null)
				return unwrapThrow(itE.getTargetException());
		}
		if (e instanceof RuntimeException && e.getCause() != null)
			return unwrapThrow(e.getCause());
		return e;
	}

	/**
	 * 判断两个对象是否相等。 这个函数用处是:
	 * <ul>
	 * <li>可以容忍 null
	 * <li>可以容忍不同类型的 Number
	 * <li>对数组，集合， Map 会深层比较
	 * </ul>
	 * 当然，如果你重写的 equals 方法会优先
	 * 
	 * @param a0
	 *            比较对象1
	 * @param a1
	 *            比较对象2
	 * @return 是否相等
	 */
	public static boolean equals(Object a0, Object a1) {
		if (a0 == a1)
			return true;

		if (a0 == null && a1 == null)
			return true;

		if (a0 == null || a1 == null)
			return false;

		// 简单的判断是否等于
		if (a0.equals(a1))
			return true;

		// 简单类型，变字符串比较，或者正则表达式
		if (a0.getClass().isPrimitive() || a0 instanceof Pattern) {
			return a0.toString().equals(a1.toString());
		}

		// 如果类型就不能互相转换，那么一定是错的
		if (!a0.getClass().isAssignableFrom(a1.getClass())
				&& !a1.getClass().isAssignableFrom(a0.getClass()))
			return false;

		// Map
		if (a0 instanceof Map && a1 instanceof Map) {
			Map<?, ?> m1 = (Map<?, ?>) a0;
			Map<?, ?> m2 = (Map<?, ?>) a1;
			if (m1.size() != m2.size())
				return false;
			for (Entry<?, ?> e : m1.entrySet()) {
				Object key = e.getKey();
				if (!m2.containsKey(key) || !equals(m1.get(key), m2.get(key)))
					return false;
			}
			return true;
		}
		// 数组
		else if (a0.getClass().isArray() && a1.getClass().isArray()) {
			int len = Array.getLength(a0);
			if (len != Array.getLength(a1))
				return false;
			for (int i = 0; i < len; i++) {
				if (!equals(Array.get(a0, i), Array.get(a1, i)))
					return false;
			}
			return true;
		}
		// 集合
		else if (a0 instanceof Collection && a1 instanceof Collection) {
			Collection<?> c0 = (Collection<?>) a0;
			Collection<?> c1 = (Collection<?>) a1;
			if (c0.size() != c1.size())
				return false;

			Iterator<?> it0 = c0.iterator();
			Iterator<?> it1 = c1.iterator();

			while (it0.hasNext()) {
				Object o0 = it0.next();
				Object o1 = it1.next();
				if (!equals(o0, o1))
					return false;
			}

			return true;
		}

		// 一定不相等
		return false;
	}

	/**
	 * 判断一个数组内是否包括某一个对象。 它的比较将通过 equals(Object,Object) 方法
	 * 
	 * @param array
	 *            数组
	 * @param ele
	 *            对象
	 * @return true 包含 false 不包含
	 */
	public static <T> boolean contains(T[] array, T ele) {
		if (null == array)
			return false;
		for (T e : array) {
			if (equals(e, ele))
				return true;
		}
		return false;
	}

	/**
	 * 根据一段文本模拟出一个文本输入流对象
	 * 
	 * @param cs
	 *            文本
	 * @return 文本输出流对象
	 */
	public static Reader inr(CharSequence cs) {
		return new StringReader(cs.toString());
	}

	/**
	 * 较方便的创建一个数组，比如：
	 * 
	 * <pre>
	 * String[] strs = Lang.array("A", "B", "A"); => ["A","B","A"]
	 * </pre>
	 * 
	 * @param eles
	 *            可变参数
	 * @return 数组对象
	 */
	public static <T> T[] array(T... eles) {
		return eles;
	}

	/**
	 * 较方便的创建一个没有重复的数组，比如：
	 * 
	 * <pre>
	 * String[] strs = Lang.arrayUniq("A","B","A");  => ["A","B"]
	 * String[] strs = Lang.arrayUniq();  => null
	 * </pre>
	 * 
	 * 返回的顺序会遵循输入的顺序
	 * 
	 * @param eles
	 *            可变参数
	 * @return 数组对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] arrayUniq(T... eles) {
		if (null == eles || eles.length == 0)
			return null;
		// 记录重复
		HashSet<T> set = new HashSet<T>(eles.length);
		for (T ele : eles) {
			set.add(ele);
		}
		// 循环
		T[] arr = (T[]) Array.newInstance(eles[0].getClass(), set.size());
		int index = 0;
		for (T ele : eles) {
			if (set.remove(ele))
				Array.set(arr, index++, ele);
		}
		return arr;

	}

	/**
	 * 判断一个对象是否为空。它支持如下对象类型：
	 * <ul>
	 * <li>null : 一定为空
	 * <li>数组
	 * <li>集合
	 * <li>Map
	 * <li>其他对象 : 一定不为空
	 * </ul>
	 * 
	 * @param obj
	 *            任意对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null)
			return true;
		if (obj.getClass().isArray())
			return Array.getLength(obj) == 0;
		if (obj instanceof Collection<?>)
			return ((Collection<?>) obj).isEmpty();
		if (obj instanceof Map<?, ?>)
			return ((Map<?, ?>) obj).isEmpty();
		return false;
	}

	/**
	 * 判断一个数组是否是空数组
	 * 
	 * @param ary
	 *            数组
	 * @return null 或者空数组都为 true 否则为 false
	 */
	public static <T> boolean isEmptyArray(T[] ary) {
		return null == ary || ary.length == 0;
	}

	/**
	 * 较方便的创建一个列表，比如：
	 * 
	 * <pre>
	 * List&lt;Pet&gt; pets = Lang.list(pet1, pet2, pet3);
	 * </pre>
	 * 
	 * 注，这里的 List，是 ArrayList 的实例
	 * 
	 * @param eles
	 *            可变参数
	 * @return 列表对象
	 */
	public static <T> ArrayList<T> list(T... eles) {
		ArrayList<T> list = new ArrayList<T>(eles.length);
		for (T ele : eles)
			list.add(ele);
		return list;
	}

	/**
	 * 创建一个 Hash 集合
	 * 
	 * @param eles
	 *            可变参数
	 * @return 集合对象
	 */
	public static <T> Set<T> set(T... eles) {
		Set<T> set = new HashSet<T>();
		for (T ele : eles)
			set.add(ele);
		return set;
	}

	/**
	 * 将多个数组，合并成一个数组。如果这些数组为空，则返回 null
	 * 
	 * @param arys
	 *            数组对象
	 * @return 合并后的数组对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] merge(T[]... arys) {
		Queue<T> list = new LinkedList<T>();
		for (T[] ary : arys)
			if (null != ary)
				for (T e : ary)
					if (null != e)
						list.add(e);
		if (list.isEmpty())
			return null;
		Class<T> type = (Class<T>) list.peek().getClass();
		return list.toArray((T[]) Array.newInstance(type, list.size()));
	}

	/**
	 * 将一个对象添加成为一个数组的第一个元素，从而生成一个新的数组
	 * 
	 * @param e
	 *            对象
	 * @param eles
	 *            数组
	 * @return 新数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] arrayFirst(T e, T[] eles) {
		try {
			if (null == eles || eles.length == 0) {
				T[] arr = (T[]) Array.newInstance(e.getClass(), 1);
				arr[0] = e;
				return arr;
			}
			T[] arr = (T[]) Array.newInstance(eles.getClass()
					.getComponentType(), eles.length + 1);
			arr[0] = e;
			for (int i = 0; i < eles.length; i++) {
				arr[i + 1] = eles[i];
			}
			return arr;
		} catch (NegativeArraySizeException e1) {
			throw Lang.wrapThrow(e1);
		}
	}

	/**
	 * 将一个对象添加成为一个数组的最后一个元素，从而生成一个新的数组
	 * 
	 * @param e
	 *            对象
	 * @param eles
	 *            数组
	 * @return 新数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] arrayLast(T[] eles, T e) {
		try {
			if (null == eles || eles.length == 0) {
				T[] arr = (T[]) Array.newInstance(e.getClass(), 1);
				arr[0] = e;
				return arr;
			}
			T[] arr = (T[]) Array.newInstance(eles.getClass()
					.getComponentType(), eles.length + 1);
			for (int i = 0; i < eles.length; i++) {
				arr[i] = eles[i];
			}
			arr[eles.length] = e;
			return arr;
		} catch (NegativeArraySizeException e1) {
			throw Lang.wrapThrow(e1);
		}
	}

	/**
	 * 将一个数组转换成字符串
	 * <p>
	 * 所有的元素都被格式化字符串包裹。 这个格式话字符串只能有一个占位符， %s, %d 等，均可，请视你的数组内容而定
	 * 
	 * @param fmt
	 *            格式
	 * @param objs
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concatBy(String fmt, T[] objs) {
		StringBuilder sb = new StringBuilder();
		for (T obj : objs)
			sb.append(String.format(fmt, obj));
		return sb;
	}

	/**
	 * 将一个数组转换成字符串
	 * <p>
	 * 所有的元素都被格式化字符串包裹。 这个格式话字符串只能有一个占位符， %s, %d 等，均可，请视你的数组内容而定
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 * 
	 * @param ptn
	 *            格式
	 * @param c
	 *            分隔符
	 * @param objs
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concatBy(String ptn, Object c, T[] objs) {
		StringBuilder sb = new StringBuilder();
		for (T obj : objs)
			sb.append(String.format(ptn, obj)).append(c);
		if (sb.length() > 0)
			sb.deleteCharAt(sb.length() - 1);
		return sb;
	}

	/**
	 * 将一个数组转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 * 
	 * @param c
	 *            分隔符
	 * @param objs
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concat(Object c, T[] objs) {
		StringBuilder sb = new StringBuilder();
		if (null == objs || 0 == objs.length)
			return sb;

		sb.append(objs[0]);
		for (int i = 1; i < objs.length; i++)
			sb.append(c).append(objs[i]);

		return sb;
	}

	/**
	 * 将一个长整型数组转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 * 
	 * @param c
	 *            分隔符
	 * @param vals
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static StringBuilder concat(Object c, long[] vals) {
		StringBuilder sb = new StringBuilder();
		if (null == vals || 0 == vals.length)
			return sb;

		sb.append(vals[0]);
		for (int i = 1; i < vals.length; i++)
			sb.append(c).append(vals[i]);

		return sb;
	}

	/**
	 * 将一个整型数组转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 * 
	 * @param c
	 *            分隔符
	 * @param vals
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static StringBuilder concat(Object c, int[] vals) {
		StringBuilder sb = new StringBuilder();
		if (null == vals || 0 == vals.length)
			return sb;

		sb.append(vals[0]);
		for (int i = 1; i < vals.length; i++)
			sb.append(c).append(vals[i]);

		return sb;
	}

	/**
	 * 将一个数组的部分元素转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 * 
	 * @param offset
	 *            开始元素的下标
	 * @param len
	 *            元素数量
	 * @param c
	 *            分隔符
	 * @param objs
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concat(int offset, int len, Object c,
			T[] objs) {
		StringBuilder sb = new StringBuilder();
		if (null == objs || len < 0 || 0 == objs.length)
			return sb;

		if (offset < objs.length) {
			sb.append(objs[offset]);
			for (int i = 1; i < len && i + offset < objs.length; i++) {
				sb.append(c).append(objs[i + offset]);
			}
		}
		return sb;
	}

	/**
	 * 将一个数组所有元素拼合成一个字符串
	 * 
	 * @param objs
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concat(T[] objs) {
		StringBuilder sb = new StringBuilder();
		for (T e : objs)
			sb.append(e.toString());
		return sb;
	}

	/**
	 * 将一个数组部分元素拼合成一个字符串
	 * 
	 * @param offset
	 *            开始元素的下标
	 * @param len
	 *            元素数量
	 * @param array
	 *            数组
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concat(int offset, int len, T[] array) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(array[i + offset].toString());
		}
		return sb;
	}

	/**
	 * 将一个集合转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 * 
	 * @param c
	 *            分隔符
	 * @param coll
	 *            集合
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concat(Object c, Collection<T> coll) {
		StringBuilder sb = new StringBuilder();
		if (null == coll || coll.isEmpty())
			return sb;
		return concat(c, coll.iterator());
	}

	/**
	 * 将一个迭代器转换成字符串
	 * <p>
	 * 每个元素之间，都会用一个给定的字符分隔
	 * 
	 * @param c
	 *            分隔符
	 * @param coll
	 *            集合
	 * @return 拼合后的字符串
	 */
	public static <T> StringBuilder concat(Object c, Iterator<T> it) {
		StringBuilder sb = new StringBuilder();
		if (it == null || !it.hasNext())
			return sb;
		sb.append(it.next());
		while (it.hasNext())
			sb.append(c).append(it.next());
		return sb;
	}

	/**
	 * 将一个或者多个数组填入一个集合。
	 * 
	 * @param <C>
	 *            集合类型
	 * @param <T>
	 *            数组元素类型
	 * @param coll
	 *            集合
	 * @param objss
	 *            数组 （数目可变）
	 * @return 集合对象
	 */
	public static <C extends Collection<T>, T> C fill(C coll, T[]... objss) {
		for (T[] objs : objss)
			for (T obj : objs)
				coll.add(obj);
		return coll;
	}


	/**
	 * 将数组转换成一个列表。
	 * 
	 * @param array
	 *            原始数组
	 * @return 新列表
	 * 
	 * @see org.nutz.castor.Castors
	 */
	public static <T> List<T> array2list(T[] array) {
		if (null == array)
			return null;
		List<T> re = new ArrayList<T>(array.length);
		for (T obj : array)
			re.add(obj);
		return re;
	}

	/**
	 * 如果是数组或集合取得第一个对象。 否则返回自身
	 * 
	 * @param obj
	 *            任意对象
	 * @return 第一个代表对象
	 */
	public static Object first(Object obj) {
		if (null == obj)
			return obj;

		if (obj instanceof Collection<?>) {
			Iterator<?> it = ((Collection<?>) obj).iterator();
			return it.hasNext() ? it.next() : null;
		}

		if (obj.getClass().isArray())
			return Array.getLength(obj) > 0 ? Array.get(obj, 0) : null;

		return obj;
	}

	/**
	 * 获取集合中的第一个元素，如果集合为空，返回 null
	 * 
	 * @param coll
	 *            集合
	 * @return 第一个元素
	 */
	public static <T> T first(Collection<T> coll) {
		if (null == coll || coll.isEmpty())
			return null;
		return coll.iterator().next();
	}

	/**
	 * 获得表中的第一个名值对
	 * 
	 * @param map
	 *            表
	 * @return 第一个名值对
	 */
	public static <K, V> Entry<K, V> first(Map<K, V> map) {
		if (null == map || map.isEmpty())
			return null;
		return map.entrySet().iterator().next();
	}

	/**
	 * 对Thread.sleep(long)的简单封装,不抛出任何异常
	 * 
	 * @param millisecond
	 *            休眠时间
	 */
	public static void quiteSleep(long millisecond) {
		try {
			if (millisecond > 0)
				Thread.sleep(millisecond);
		} catch (Throwable e) {
		}
	}

	/**
	 * 将字符串，变成数字对象，现支持的格式为：
	 * <ul>
	 * <li>null - 整数 0</li>
	 * <li>23.78 - 浮点 Float</li>
	 * <li>0x45 - 16进制整数 Integer</li>
	 * <li>78L - 长整数 Long</li>
	 * <li>69 - 普通整数 Integer</li>
	 * </ul>
	 * 
	 * @param s
	 *            参数
	 * @return 数字对象
	 */
	public static Number str2number(String s) {
		// null 值
		if (null == s) {
			return 0;
		}
		s = s.toUpperCase();
		// 浮点
		if (s.indexOf('.') != -1) {
			char c = s.charAt(s.length() - 1);
			if (c == 'F' || c == 'f') {
				return Float.valueOf(s);
			}
			return Double.valueOf(s);
		}
		// 16进制整数
		if (s.startsWith("0X")) {
			return Integer.valueOf(s.substring(2), 16);
		}
		// 长整数
		if (s.charAt(s.length() - 1) == 'L' || s.charAt(s.length() - 1) == 'l') {
			return Long.valueOf(s.substring(0, s.length() - 1));
		}
		// 普通整数
		Long re = Long.parseLong(s);
		if (Integer.MAX_VALUE >= re && re >= Integer.MIN_VALUE)
			return re.intValue();
		return re;
	}

	/**
	 * 返回一个集合对象的枚举对象。实际上就是对 Iterator 接口的一个封装
	 * 
	 * @param col
	 *            集合对象
	 * @return 枚举对象
	 */
	public static <T> Enumeration<T> enumeration(Collection<T> col) {
		final Iterator<T> it = col.iterator();
		return new Enumeration<T>() {
			public boolean hasMoreElements() {
				return it.hasNext();
			}

			public T nextElement() {
				return it.next();
			}
		};
	}

	/**
	 * 将枚举对象，变成集合
	 * 
	 * @param enums
	 *            枚举对象
	 * @param cols
	 *            集合对象
	 * @return 集合对象
	 */
	public static <T extends Collection<E>, E> T enum2collection(
			Enumeration<E> enums, T cols) {
		while (enums.hasMoreElements())
			cols.add(enums.nextElement());
		return cols;
	}

	/**
	 * 将字符数组强制转换成字节数组。如果字符为双字节编码，则会丢失信息
	 * 
	 * @param cs
	 *            字符数组
	 * @return 字节数组
	 */
	public static byte[] toBytes(char[] cs) {
		byte[] bs = new byte[cs.length];
		for (int i = 0; i < cs.length; i++)
			bs[i] = (byte) cs[i];
		return bs;
	}

	/**
	 * 将整数数组强制转换成字节数组。整数的高位将会被丢失
	 * 
	 * @param is
	 *            整数数组
	 * @return 字节数组
	 */
	public static byte[] toBytes(int[] is) {
		byte[] bs = new byte[is.length];
		for (int i = 0; i < is.length; i++)
			bs[i] = (byte) is[i];
		return bs;
	}

	/**
	 * 使用当前线程的ClassLoader加载给定的类
	 * 
	 * @param className
	 *            类的全称
	 * @return 给定的类
	 * @throws ClassNotFoundException
	 *             如果无法用当前线程的ClassLoader加载
	 */
	public static Class<?> loadClass(String className)
			throws ClassNotFoundException {
		try {
			return Thread.currentThread().getContextClassLoader()
					.loadClass(className);
		} catch (ClassNotFoundException e) {
			return Class.forName(className);
		}
	}

	/**
	 * 获取基本类型的默认值
	 * 
	 * @param pClass
	 *            基本类型
	 * @return 0/false,如果传入的pClass不是基本类型的类,则返回null
	 */
	public static Object getPrimitiveDefaultValue(Class<?> pClass) {
		if (int.class.equals(pClass))
			return Integer.valueOf(0);
		if (long.class.equals(pClass))
			return Long.valueOf(0);
		if (short.class.equals(pClass))
			return Short.valueOf((short) 0);
		if (float.class.equals(pClass))
			return Float.valueOf(0f);
		if (double.class.equals(pClass))
			return Double.valueOf(0);
		if (byte.class.equals(pClass))
			return Byte.valueOf((byte) 0);
		if (char.class.equals(pClass))
			return Character.valueOf((char) 0);
		if (boolean.class.equals(pClass))
			return Boolean.FALSE;
		return null;
	}

	/**
	 * 获取一个 Type 类型实际对应的Class
	 * 
	 * @param type
	 *            类型
	 * @return 与Type类型实际对应的Class
	 */
	@SuppressWarnings("rawtypes")
	public static Class<?> getTypeClass(Type type) {
		Class<?> clazz = null;
		if (type instanceof Class<?>) {
			clazz = (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			clazz = (Class<?>) pt.getRawType();
		} else if (type instanceof GenericArrayType) {
			GenericArrayType gat = (GenericArrayType) type;
			Class<?> typeClass = getTypeClass(gat.getGenericComponentType());
			return Array.newInstance(typeClass, 0).getClass();
		} else if (type instanceof TypeVariable) {
			TypeVariable tv = (TypeVariable) type;
			Type[] ts = tv.getBounds();
			if (ts != null && ts.length > 0)
				return getTypeClass(ts[0]);
		} else if (type instanceof WildcardType) {
			WildcardType wt = (WildcardType) type;
			Type[] t_low = wt.getLowerBounds();// 取其下界
			if (t_low.length > 0)
				return getTypeClass(t_low[0]);
			Type[] t_up = wt.getUpperBounds(); // 没有下界?取其上界
			return getTypeClass(t_up[0]);// 最起码有Object作为上界
		}
		return clazz;
	}

	/**
	 * 返回一个 Type 的泛型数组, 如果没有, 则直接返回null
	 * 
	 * @param type
	 *            类型
	 * @return 一个 Type 的泛型数组, 如果没有, 则直接返回null
	 */
	public static Type[] getGenericsTypes(Type type) {
		if (type instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) type;
			return pt.getActualTypeArguments();
		}
		return null;
	}

	/**
	 * 强制从字符串转换成一个 Class，将 ClassNotFoundException 包裹成 RuntimeException
	 * 
	 * @param <T>
	 * @param name
	 *            类名
	 * @param type
	 *            这个类型的边界
	 * @return 类对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> forName(String name, Class<T> type) {
		Class<?> re;
		try {
			re = Class.forName(name);
			return (Class<T>) re;
		} catch (ClassNotFoundException e) {
			throw Lang.wrapThrow(e);
		}
	}

	/**
	 * 获取指定字符串的 SHA1 值
	 * 
	 * @param cs
	 *            字符串
	 * @return 指定字符串的 SHA1 值
	 * @see #digest(String, CharSequence)
	 */
	public static String sha1(CharSequence cs) {
		return digest("SHA1", cs);
	}

	/**
	 * 从字符串计算出数字签名
	 * 
	 * @param algorithm
	 *            算法，比如 "SHA1" 或者 "MD5" 等
	 * @param cs
	 *            字符串
	 * @return 数字签名
	 */
	public static String digest(String algorithm, CharSequence cs) {
		return digest(algorithm, Strings.getBytesUTF8(null == cs ? "" : cs),
				null, 1);
	}

	/**
	 * 从字节数组计算出数字签名
	 * 
	 * @param algorithm
	 *            算法，比如 "SHA1" 或者 "MD5" 等
	 * @param bytes
	 *            字节数组
	 * @param salt
	 *            随机字节数组
	 * @param iterations
	 *            迭代次数
	 * @return 数字签名
	 */
	public static String digest(String algorithm, byte[] bytes, byte[] salt,
			int iterations) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);

			if (salt != null) {
				md.update(salt);
			}

			byte[] hashBytes = md.digest(bytes);

			for (int i = 1; i < iterations; i++) {
				md.reset();
				hashBytes = md.digest(hashBytes);
			}

			return fixedHexString(hashBytes);
		} catch (NoSuchAlgorithmException e) {
			throw Lang.wrapThrow(e);
		}
	}

	/** 当前运行的 Java 虚拟机是否是在安卓环境 */
	public static final boolean isAndroid;
	static {
		boolean flag = false;
		try {
			Class.forName("android.Manifest");
			flag = true;
		} catch (Throwable e) {
		}
		isAndroid = flag;
	}

	/**
	 * 将指定的数组的内容倒序排序。注意，这会破坏原数组的内容
	 * 
	 * @param arrays
	 *            指定的数组
	 */
	public static <T> void reverse(T[] arrays) {
		int size = arrays.length;
		for (int i = 0; i < size; i++) {
			int ih = i;
			int it = size - 1 - i;
			if (ih == it || ih > it) {
				break;
			}
			T ah = arrays[ih];
			T swap = arrays[it];
			arrays[ih] = swap;
			arrays[it] = ah;
		}
	}

	public static String simpleMetodDesc(Method method) {
		return String.format("%s.%s(...)", method.getDeclaringClass()
				.getSimpleName(), method.getName());
	}

	public static String fixedHexString(byte[] hashBytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hashBytes.length; i++) {
			sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}

		return sb.toString();
	}

	/**
	 * 一个便利的方法，将当前线程睡眠一段时间
	 * 
	 * @param ms
	 *            要睡眠的时间 ms
	 */
	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			throw Lang.wrapThrow(e);
		}
	}

	/**
	 * 一个便利的等待方法同步一个对象
	 * 
	 * @param lock
	 *            锁对象
	 * @param ms
	 *            要等待的时间 ms
	 */
	public static void wait(Object lock, long ms) {
		if (null != lock)
			synchronized (lock) {
				try {
					lock.wait(ms);
				} catch (InterruptedException e) {
					throw Lang.wrapThrow(e);
				}
			}
	}

	/**
	 * 通知对象的同步锁
	 * 
	 * @param lock
	 *            锁对象
	 */
	public static void notifyAll(Object lock) {
		if (null != lock)
			synchronized (lock) {
				lock.notifyAll();
			}
	}

	public static void runInAnThread(Runnable runnable) {
		new Thread(runnable).start();
	}

	/**
	 * map对象浅过滤,返回值是一个新的map
	 * 
	 * @param source
	 *            原始的map对象
	 * @param prefix
	 *            包含什么前缀,并移除前缀
	 * @param include
	 *            正则表达式 仅包含哪些key(如果有前缀要求,则已经移除了前缀)
	 * @param exclude
	 *            正则表达式 排除哪些key(如果有前缀要求,则已经移除了前缀)
	 * @param map
	 *            映射map, 原始key--目标key (如果有前缀要求,则已经移除了前缀)
	 * @return 经过过滤的map,与原始map不是同一个对象
	 */
	public static Map<String, Object> filter(Map<String, Object> source,
			String prefix, String include, String exclude,
			Map<String, String> keyMap) {
		LinkedHashMap<String, Object> dst = new LinkedHashMap<String, Object>();
		if (source == null || source.isEmpty())
			return dst;

		Pattern includePattern = include == null ? null : Pattern
				.compile(include);
		Pattern excludePattern = exclude == null ? null : Pattern
				.compile(exclude);

		for (Entry<String, Object> en : source.entrySet()) {
			String key = en.getKey();
			if (prefix != null) {
				if (key.startsWith(prefix))
					key = key.substring(prefix.length());
				else
					continue;
			}
			if (includePattern != null && !includePattern.matcher(key).find())
				continue;
			if (excludePattern != null && excludePattern.matcher(key).find())
				continue;
			if (keyMap != null && keyMap.containsKey(key))
				dst.put(keyMap.get(key), en.getValue());
			else
				dst.put(key, en.getValue());
		}
		return dst;
	}

	/**
	 * @return 返回当前程序运行的根目录
	 */
	public static String runRootPath() {
		String cp = Lang.class.getClassLoader().getResource("")
				.toExternalForm();
		if (cp.startsWith("file:")) {
			cp = cp.substring("file:".length());
		}
		return cp;
	}
}
