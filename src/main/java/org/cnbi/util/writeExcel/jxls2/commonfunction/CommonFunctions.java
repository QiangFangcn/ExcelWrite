package org.cnbi.util.writeExcel.jxls2.commonfunction;

import sun.applet.Main;

import java.math.BigDecimal;
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
   * @param temporal 值
   * @param pattern  格式
   * @return 格式化后的值
   * @version jdk1.8
   */
/*  public String format(TemporalAccessor temporal, String pattern) {
    return temporal == null ? null : DateTimeFormatter.ofPattern(pattern).format(temporal);
  }*/

  /**
   * 数字的格式化。
   *
   * @param number  值
   * @param pattern 格式
   * @return 格式化后的值
   */
  public String format(Number number, String pattern) {
    return number == null ? null : new DecimalFormat(pattern).format(number);
  }

  /**
   * 数字的四舍五入。
   *
   * @param number 值
   * @param scale  小数位数
   * @return 四舍五入后的值
   */
  public Number round(Number number, int scale) {
    return number == null ? null : (number instanceof BigDecimal ?
      ((BigDecimal) number).setScale(scale, BigDecimal.ROUND_HALF_UP)
      : new BigDecimal(number.toString()).setScale(scale, BigDecimal.ROUND_HALF_UP));
  }

  /**
   * 合并字符串
   *
   * @param items 合并项
   * @return 合并后的字符串
   * @version jdk1.8
   */
/*  public String concat(Object... items) {
    if (items == null || items.length == 0) return null;
    return Arrays.stream(items).map(i -> i == null ? "" : i.toString()).collect(Collectors.joining());
  }*/

  /**
   * 转换为整数
   *
   * @param str 字符值
   * @return 整数
   */
  public Integer toInt(String str) {
    return str == null ? null : new Integer(str);
  }
  
  
  /**
   *  日期格式化
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
   *  if判断
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
     * @param number
     * @return
     */
  public double parseNumber(String number) {
      return Double.parseDouble(number);
  }

  public double parseNumber(int number) {
      return (double)number;
  }

  public double parseNumber(float number) {
      return (double)number;
  }

  public double parseNumber(double number) {
      return number;
  }

  public double except100(String number) {
      return Double.parseDouble(number)/100;
  }

  public double except100(int number) {
      return (double)number/100;
  }

  public double except100(float number) {
      return (double)number/100;
  }

  public double except100(double number) { return number/100; }

}
