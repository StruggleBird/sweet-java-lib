package org.zt.sweet.lang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

/**
 * 日期工具类
 * 
 * @author Ternence
 * @date 2014-2-27
 */
public abstract class DateTimes {

	/**
	 * 一个月的最大天数，{@value}
	 */
	protected static final int DAY_31 = 31;
	/**
	 * 标准日期格式yyyy-MM-dd HH:mm:ss的年-月-日字符长度
	 */
	protected static final int NUM_10 = 10;

	/**
	 * 标准日期格式
	 */
	public static final String STANDARD = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 简写日期格式
	 */
	public static final String COMPACT = "yyMMdd HH:mm:ss";
	/**
	 * 非标准日期格式,下划线间隔
	 */
	public static final String FILENAME = "yyMMdd_HHmmss";
	/**
	 * 简写年月日格式
	 */
	public static final String SIMPLE = "yyMMdd";
	/**
	 * 标准年月日格式
	 */
	public static final String DATE = "yyyy-MM-dd";
	/**
	 * 标准年月日加简写的时分日期格式
	 */
	public static final String QUERY_STR = "yyyy-MM-ddHHmm";
	/**
	 * 非标准年(两位)月日时分秒及毫秒日期格式,无符号,紧凑版
	 */
	public static final String FORMAT_NO_CONNECTOR = "yyMMddHHmmssSS";

	/**
	 * 年
	 */
	public static final char YEAR_TYPE = 'y';

	/**
	 * 月
	 */
	public static final char MONTH_TYPE = 'M';

	/**
	 * 日
	 */
	public static final char DAY_TYPE = 'd';

	/**
	 * 时
	 */
	public static final char HOUR_TYPE = 'H';

	/**
	 * 分
	 */
	public static final char MINUTE_TYPE = 'm';

	/**
	 * 秒
	 */
	public static final char SECOND_TYPE = 's';

	/**
	 * 开始时间
	 */
	public static final String START_TIME = " 00:00:00";

	/**
	 * 结束时间
	 */
	public static final String END_TIME = " 23:59:59";

	/**
	 * 格式化为标准日期格式(yyyy-MM-dd HH:mm:ss)的日期
	 * 
	 * @param strDate
	 *            字符串日期
	 * @return 标准日期
	 */
	public static Date getStandardStringDate(String strDate) {
		return getStringDate(strDate, STANDARD);
	}

	/**
	 * 格式化为自定义的日期格式
	 * 
	 * @param strDate
	 *            字符串日期
	 * @param strType
	 *            自定义的日期格式
	 * @return 自定义的日期
	 */
	public static Date getStringDate(String strDate, String strType) {
		Date date = null;

		SimpleDateFormat objSDF = new SimpleDateFormat(strType);
		try {
			date = objSDF.parse(strDate);
		} catch (Exception e) {
		}

		return date;
	}

	/**
	 * 获得字符串时间戳
	 * 
	 * @param objGC
	 *            Calender日期
	 * @param strType
	 *            日期格式，比如"yyyy-MM-dd HH:mm:ss"
	 * @return 按照strType格式化的日期字符串
	 */
	public static String getDateString(GregorianCalendar objGC, String strType) {
		if (objGC == null) {
			return "";
		}
		Date objDate = objGC.getTime();
		return getDateString(objDate, strType);
	}

	/**
	 * 获取标准格式的日期字符串
	 * 
	 * @param objGC
	 *            Calender日期
	 * @return 标准日期格式字符串
	 */
	public static String getStandardDate(GregorianCalendar objGC) {
		return getDateString(objGC, STANDARD);
	}

	/**
	 * 获取非标准格式日期字符串，例如"yyMMdd_HH:mm:ss"
	 * 
	 * @param objGC
	 *            Calender日期
	 * @return 非标准格式日期字符串
	 */
	public static String getFileNameDate(GregorianCalendar objGC) {
		return getDateString(objGC, FILENAME);
	}

	/**
	 * 获取简写格式日期字符串，例如："yyMMdd HH:mm:ss"
	 * 
	 * @param objGC
	 *            Calender日期
	 * @return 简写格式日期字符串
	 */
	public static String getCompactDate(GregorianCalendar objGC) {
		return getDateString(objGC, COMPACT);
	}

	/**
	 * 获取指定格式的日期字符串
	 * 
	 * @param objDate
	 *            java.utils.Date日期
	 * @param strType
	 *            日期格式
	 * @return 指定格式的日期字符串
	 */
	public static String getDateString(Date objDate, String strType) {
		if (objDate == null) {
			return "";
		}
		SimpleDateFormat objSDF = new SimpleDateFormat(strType);
		return objSDF.format(objDate);
	}

	/**
	 * 获取标准日期格式的字符串，例如"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param objDate
	 *            java.utils.Date日期
	 * @return 标准日期格式的字符串
	 */
	public static String getStandardDate(Date objDate) {
		return getDateString(objDate, STANDARD);
	}

	/**
	 * 获取简写格式日期字符串，例如："yyMMdd HH:mm:ss"
	 * 
	 * @param objGC
	 *            java.utils.Date日期
	 * @return 简写格式日期字符串
	 */
	public static String getCompactDate(Date objDate) {
		return getDateString(objDate, COMPACT);
	}

	/**
	 * 获取非标准格式日期字符串，例如"yyMMdd_HH:mm:ss"
	 * 
	 * @param objGC
	 *            java.utils.Date日期
	 * @return 非标准格式日期字符串
	 */
	public static String getFileNameDate(Date objDate) {
		return getDateString(objDate, FILENAME);
	}

	/**
	 * 获取系统当前日期，按照指定格式返回日期字符串
	 * 
	 * @param strType
	 *            日期格式
	 * @return 指定格式日期字符串
	 */
	public static String getSysdateString(String strType) {
		Date objDate = new Date();
		return getDateString(objDate, strType);
	}

	/**
	 * 获取系统当前日期，按照标准格式返回日期字符串
	 * 
	 * @return 标准格式日期字符串
	 */
	public static String getStandardSysdate() {
		Date objDate = new Date();
		return getDateString(objDate, STANDARD);
	}

	/**
	 * 简单的日期类型(YYMMDD)
	 * 
	 * @return 简单日期字符串
	 */
	public static String getSimpleSysdate() {
		Date objDate = new Date();
		return getDateString(objDate, SIMPLE);
	}

	/**
	 * 标准格式化日期 ,精确到天
	 * 
	 * @param strDate
	 *            日期字符串
	 * @return 标准格式化日期字符串
	 */
	public static String getStringStandardDate(String strDate) {
		return getDateString(strDate, DATE);
	}

	/**
	 * 获取当前系统日期，按照简单格式(yyMMdd HH:mm:ss)返回日期字符串
	 * 
	 * @return 简单格式日期字符串
	 */
	public static String getCompactSysdate() {
		Date objDate = new Date();
		return getDateString(objDate, COMPACT);
	}

	/**
	 * 获取当前系统日期，按照非标准格式(yyMMdd_HH:mmss)返回日期字符串
	 * 
	 * @return
	 */
	public static String getFileNameSysdate() {
		Date objDate = new Date();
		return getDateString(objDate, FILENAME);
	}

	/**
	 * 获取标准日期(yyyy-MM-dd HH:mm:ss)
	 * 
	 * @param strDate
	 *            日期字符串
	 * @return 标准日期
	 */
	public static Date getStandardDate(String strDate) {
		return getStringDate(strDate, STANDARD);
	}

	/**
	 * 获取标准日期格式字符串
	 * 
	 * @param strDate
	 *            日期字符串
	 * @return 标准日期格式字符串
	 */
	public static String getStandardDateForDB(String strDate) {
		if (StringUtils.isBlank(strDate)) {
			return null;
		}
		Date objDate = getStandardDate(strDate);
		return getStandardDate(objDate);
	}

	/**
	 * 获取指定格式的日期字符串
	 * 
	 * @param strDate
	 *            日期字符串(2010-01-01 12:30:50.0)
	 * @return 指定格式的日期字符串
	 */
	public static String getDateString(String strDate, String strType) {
		if (StringUtils.isBlank(strDate)) {
			return null;
		}
		Date objDate = getStandardDate(strDate);
		return getDateString(objDate, strType);
	}

	/**
	 * 获取标准开始日期, yyyy-MM-dd 00:00:00
	 * 
	 * @param strBeginDate
	 * @return 标准开始日期, yyyy-MM-dd 00:00:00
	 */
	public static String getStandardBeginDate(String strBeginDate) {
		String beginDate = strBeginDate;
		return beginDate.substring(0, NUM_10) + START_TIME;
	}

	/**
	 * 获取标准结束日期, yyyy-MM-dd 23:59:59
	 * 
	 * @param strEndDate
	 * @return 标准结束日期, yyyy-MM-dd 23:59:59
	 */
	public static String getStandardEndDate(String strEndDate) {
		String beginDate = strEndDate;
		return beginDate.substring(0, NUM_10) + END_TIME;
	}

	/**
	 * 获取标准日期字符串
	 * 
	 * @param strDate
	 * @return 标准日期字符串
	 */
	public static String getStandardDateString(String strDate) {

		SimpleDateFormat objFormat = new SimpleDateFormat(STANDARD);

		Date objDate = null;

		for (int i = 0; i < 2; i++) {
			try {
				objDate = objFormat.parse(strDate);

				objFormat.applyPattern(STANDARD);

			} catch (ParseException e) {
				objFormat.applyPattern(DATE);
			}
		}

		if (null != objDate) {
			return objFormat.format(objDate);
		}

		return null;
	}

	/**
	 * 获取指定天数的日期字符串
	 * 
	 * @param day
	 *            day of month
	 * @return 指定天数的日期字符串
	 */
	public static String getConfirmDayDateString(Integer day) {
		Calendar objCal = new GregorianCalendar();
		objCal.set(Calendar.DATE, day);
		return getStandardDate(objCal.getTime());
	}

	/**
	 * 获取指定月的日期字符串
	 * 
	 * @param day
	 *            day of month
	 * @return 指定月的日期字符串
	 */
	public static String getConfirmMonthDayDateString(Integer day) {
		Calendar objCal = Calendar.getInstance();
		Date date = new Date();
		objCal.setTime(date);
		objCal.set(Calendar.MONTH, objCal.get(Calendar.MONTH) - 1);
		if (day != DAY_31) {
			objCal.set(Calendar.DAY_OF_MONTH, day);
		} else {
			objCal.set(Calendar.DAY_OF_MONTH,
					objCal.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		return getStandardDate(objCal.getTime());
	}

	/**
	 * 获取当月月份字符串
	 * 
	 * @param strEndDate
	 *            日期字符串
	 * @return 当月月份字符串
	 */
	@SuppressWarnings("deprecation")
	public static String getMonthDayDateString(String strEndDate) {

		Date objDate = getStringDate(strEndDate, STANDARD);

		return String.valueOf(objDate.getMonth());

	}

	/**
	 * 将指定日期，替换成指定几号的终点时分(23：59：59)
	 * 
	 * @param strDate
	 *            日期字符串
	 * @param strDay
	 *            指定的天
	 * @return 标准日期字符串，时分秒为23:59:59
	 */
	public static String getEndPointByDateString(final String strDate,
			final String strDay) {
		String day = strDay;
		if (StringUtils.isBlank(day) || day.length() > 2) {
			return null;
		}

		if (strDay.length() == 1) {
			day = "0" + day;
		}

		return getStandardEndDate(day.substring(0, 8) + day);

	}

	/**
	 * 取两个时间差(秒)
	 * 
	 * @param strD1
	 *            String
	 * @param strD2
	 *            String
	 * @return int 时间差
	 */
	public static int getTimeDiff(String strD1, String strD2) {
		SimpleDateFormat objFM = new SimpleDateFormat(DATE);

		Date date1 = null;
		Date date2 = null;
		try {
			date1 = objFM.parse(strD1);
			date2 = objFM.parse(strD2);
			return (int) ((date1.getTime() - date2.getTime()) / 10000);
		} catch (Exception ex) {
		}
		return 0;

	}
}
