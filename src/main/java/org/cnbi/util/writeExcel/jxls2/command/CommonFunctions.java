package org.cnbi.util.writeExcel.jxls2.command;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class CommonFunctions {
	private static CommonFunctions singleton = new CommonFunctions();

	public static CommonFunctions getSingleton() {
		return singleton;
	}

	private CommonFunctions() {
	}

	/**
	 * java.time 的格式化。
	 *
	 * @param temporal
	 *            值
	 * @param pattern
	 *            格式
	 * @return 格式化后的值
	 * @version jdk1.8
	 */
	/*
	 * public String format(TemporalAccessor temporal, String pattern) { return
	 * temporal == null ? null :
	 * DateTimeFormatter.ofPattern(pattern).format(temporal); }
	 */

	/**
	 * 数字的格式化。
	 *
	 * @param number
	 *            值
	 * @param pattern
	 *            格式
	 * @return 格式化后的值
	 */
	public String format(Number number, String pattern) {
		return number == null ? null : new DecimalFormat(pattern).format(number);
	}

	/**
	 * 数字的四舍五入。
	 *
	 * @param number
	 *            值
	 * @param scale
	 *            小数位数
	 * @return 四舍五入后的值
	 */
	public Number round(Number number, int scale) {
		return number == null ? null
				: (number instanceof BigDecimal ? ((BigDecimal) number).setScale(scale, BigDecimal.ROUND_HALF_UP)
						: new BigDecimal(number.toString()).setScale(scale, BigDecimal.ROUND_HALF_UP));
	}

	/**
	 * 合并字符串
	 *
	 * @param items
	 *            合并项
	 * @return 合并后的字符串
	 * @version jdk1.8
	 */
	/*
	 * public String concat(Object... items) { if (items == null || items.length
	 * == 0) return null; return Arrays.stream(items).map(i -> i == null ? "" :
	 * i.toString()).collect(Collectors.joining()); }
	 */

	/**
	 * 转换为整数
	 *
	 * @param str
	 *            字符值
	 * @return 整数
	 */
	public Integer toInt(String str) {
		return str == null ? null : new Integer(str);
	}

	/**
	 * 日期格式化
	 * 
	 * @param date
	 * @param fmt
	 * @return
	 */
	public String dateFmt(Date date, String fmt) {
		if (date == null) {
			return "";
		}
		try {
			SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
			return dateFmt.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * if判断
	 * 
	 * @param b
	 * @param o1
	 * @param o2
	 * @return
	 */
	public Object ifelse(boolean b, Object o1, Object o2) {
		return b ? o1 : o2;
	}

	// 返回第一个不为空的对象
	public Object defaultIfNull(Object... objs) {
		for (Object o : objs) {
			if (o != null)
				return o;
		}
		return null;
	}

	/**
	 * 转换为数字的方法
	 * 
	 * @param number
	 * @return
	 */
	public double parseNumber(String number) {
		double d = 0;
		try {
			d = Double.parseDouble(number);
		} catch (Exception e) {
			d = 0;
		}
		return d;
	}

	public double parseNumber(int number) {
		return (double) number;
	}

	public double parseNumber(float number) {
		return (double) number;
	}

	public double parseNumber(double number) {
		return number;
	}

	public double parseNumber(BigDecimal number) {
		return number.doubleValue();
	}

	public double parseNumber(BigInteger number) {
		return number.doubleValue();
	}

	/**
	 * 计算除100后的分数
	 * 
	 * @param number
	 * @return
	 */
	public Object except100(Object number) {
		if (number == null) {
			return 0;
		}
		if (number instanceof String) {
			double d = 0;
			try {
				d = Double.parseDouble((String) number);
			} catch (Exception e) {
				d = 0;
			}
			return d / 100;
		}
		if (number instanceof Integer) {
			Double d = Double.valueOf(number.toString());
			return d / 100;
		}
		if (number instanceof Float) {
			return (float) number / 100;
		}
		if (number instanceof Double) {
			return (double) number / 100;
		}
		if (number instanceof BigDecimal) {
			double d = ((BigDecimal) number).doubleValue();
			return d / 100;
		}
		if (number instanceof BigInteger) {
			double d = ((BigDecimal) number).doubleValue();
			return d / 100;
		}
		return number;
	}

	/**
	 * 给年月求 上月 本月 下月 剩余月（为了月度分析预测报告写的）
	 * 
	 * @param year
	 * @param month
	 * @param option
	 * @return
	 */
	public static String fetchPeriod(int year, int month, int option) {
		if (option == 0) {

		}
		if (option == -1) {
			year = month == 1 ? year - 1 : year;
			month = month == 1 ? 12 : month - 1;
		}

		if (option == 1) {
			year = month == 12 ? year + 1 : year;
			month = month == 12 ? 1 : month + 1;
		}

		if (option == 2) {
			String period = "";
			period = month >= 10 ? "12" : (month + 2) + "-12";
			return year + "年" + period + "月预测";
		}

		return year + "年" + month + "月";
	}

	public static String fechPeriod2(int month, int option) {
		String s = "";
		if (option == -1) {
			switch (month) {
			case 3:
				s = "1-2月实际";
				break;
			case 6:
				s = "1-5月实际";
				break;
			case 9:
				s = "1-8月实际";
				break;
			case 12:
				s = "1-11月实际";
				break;
			default:
				s = "之前月实际数据（当前为"+month+"月不属于季度预测）";
				break;
			}
		}
		if (option == 1) {
			switch (month) {
			case 3:
				s = "3-12月预测";
				break;
			case 6:
				s = "6-12月预测";
				break;
			case 9:
				s = "9-12月预测";
				break;
			case 12:
				s = "12月预测";
				break;
			default:
				s = "之后月预测数据（当前为"+month+"月不属于季度预测）";
				break;
			}
		}
		return s;
	}

	public static void main(String[] args) {
		int month = 1;
		System.out.println(fechPeriod2(month, -1));
		System.out.println(fechPeriod2(month, 1));
	}

}
