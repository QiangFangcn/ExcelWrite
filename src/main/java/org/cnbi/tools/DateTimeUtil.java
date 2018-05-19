package org.cnbi.tools;

public class DateTimeUtil {
	
	/**
	 * 间隔时间
	 * @param l 间隔时间
	 * @return
	 */
	public static String distanceTime(long l){
      long day=distanceDay(l),
	    hour=(distanceHour(l)-day*24),
	    min=(distanceMinute(l)-day*24*60-hour*60),
	    s=(l/1000-day*24*60*60-hour*60*60-min*60),
      ms = l%1000;
      
     String t = (day>0 ? day+"天" : "") + 
 		(hour > 0 ? hour+"小时" : "") + 
 		(min > 0 ? min+"分" : "") + 
 		(s > 0 ? s+"秒" : "") + 
 		(ms > 0 ? ms+"毫秒" : "");
      return	l > 0 ? t : "0秒" ;
	}
	
	public static long distanceDay(long l){
		return l/(24*60*60*1000);
	}
	
	public static long distanceHour(long l){
		return l/(60*60*1000);
	}
	
	public static long distanceMinute(long l){
		return l/(60*1000);
	}
	
	public static long distanceSecond(long l){
		return l/1000;
	}
	
}
