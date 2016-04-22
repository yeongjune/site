package com.base.excel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTime {

	private static final long MILLISECOND_OF_DAY = 24*60*60*1000;
	private static final long MILLISECOND_OF_MINUTE = 60000;
	private static Calendar c;
	
	/**
	 * 如果Date对象d不为null，获得d的Calendar对象，否则获取当前时间的Calendar对象；并设置年月日时分秒（为null的参数则不设置）
	 * @param d
	 * @param year
	 * @param month
	 * @param date
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Calendar calendar(Date d, Integer year, Integer month, Integer date, Integer hour, Integer minute, Integer second){
		Calendar c = Calendar.getInstance();
		if(d!=null)c.setTime(d);
		if(year!=null)c.set(Calendar.YEAR, year);
		if(month!=null)c.set(Calendar.MONTH, month);
		if(date!=null)c.set(Calendar.DATE, date);
		if(hour!=null)c.set(Calendar.HOUR_OF_DAY, hour);
		if(minute!=null)c.set(Calendar.MINUTE, minute);
		if(second!=null)c.set(Calendar.SECOND, second);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}
	/**
	 * 当前日期的Date对象，时分秒毫秒都设为0
	 * @return
	 */
	public static Date date(){
		return calendar(null, null, null, null, 0, 0, 0).getTime();
	}
	/**
	 * 当前指定年月日的Date对象，时分秒毫秒都设为0
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static Date date(Integer year, Integer month, Integer date){
		return calendar(null, year, month, date, 0, 0, 0).getTime();
	}
	/**
	 * 当前指定年月日时分秒的Date对象，毫秒被设为0
	 * @param year
	 * @param month
	 * @param date
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Date date(Integer year, Integer month, Integer date, Integer hour, Integer minute, Integer second){
		return calendar(null, year, month, date, hour, minute, second).getTime();
	}
	/**
	 * 把指定的Date对象的时分秒毫秒设为0
	 * @param d
	 * @return
	 */
	public static Date date(Date d){
		return calendar(d, null, null, null, 0, 0, 0).getTime();
	}
	/**
	 * 设置指定日期对象的 小时、分，秒和毫秒被设为0
	 * @param d
	 * @param h
	 * @param m
	 * @return
	 */
	public static Date date(Date d,Integer h, Integer m){
		return calendar(d, null, null, null, h, m, 0).getTime();
	}
	/**
	 * 设置指定日期对象的 小时、分，秒和毫秒被设为0
	 * @param d
	 * @param h
	 * @param m
	 * @return
	 */
	public static Date date(Date d,String hm){
		if(hm!=null && hm.contains(":")){
			hm = hm.trim();
			Integer h = Integer.valueOf(hm.substring(0, hm.indexOf(":")).trim());
			Integer m = Integer.valueOf(hm.substring(hm.indexOf(":")+1).trim());
			return calendar(d, null, null, null, h, m, 0).getTime();
		}
		return null;
	}
	
	/**
	 * 获得当天开始时间：yyyy-MM-dd 00:00:00.0
	 * @param d
	 * @return
	 */
	public static Date start(Date d){
		return date(d);
	}
	/**
	 * 获得下一天的开始：yyyy-MM-dd 00:00:00.0（也就是当天结束时间：yyyy-MM-dd 23:59:59）
	 * @param d
	 * @return
	 */
	public static Date end(Date d){
		return next(d);
	}
	
	/**
	 * 获得当前日期的后一天的Date对象，时分秒毫秒被设为0
	 * @return
	 */
	public static Date next(){
		c = calendar(null, null, null, null, 0, 0, 0);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}
	/**
	 * 获得指定日期的后一天的Date对象，时分秒毫秒被设为0
	 * @param d
	 * @return
	 */
	public static Date next(Date d){
		c = calendar(d, null, null, null, 0, 0, 0);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}
	/**
	 * 获得指定日期的后一天的Date对象，并设置时、分；秒毫秒被设为0
	 * @param d
	 * @param h
	 * @param m
	 * @return
	 */
	public static Date next(Date d,Integer h, Integer m){
		c = calendar(d, null, null, null, h, m, 0);
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}
	/**
	 * 获得当前日期的前一天的Date对象，时分秒毫秒被设为0
	 * @return
	 */
	public static Date prev(){
		c = calendar(null, null, null, null, 0, 0, 0);
		c.add(Calendar.DATE, -1);
		return c.getTime();
	}
	/**
	 * 获得指定日期的前一天的Date对象，时分秒毫秒被设为0
	 * @param d
	 * @return
	 */
	public static Date prev(Date d){
		c = calendar(d, null, null, null, 0, 0, 0);
		c.add(Calendar.DATE, -1);
		return c.getTime();
	}
	/**
	 * 获得指定日期的前一天的Date对象，并设置时、分；秒毫秒被设为0
	 * @param d
	 * @param h
	 * @param m
	 * @return
	 */
	public static Date prev(Date d,Integer h, Integer m){
		c = calendar(d, null, null, null, h, m, 0);
		c.add(Calendar.DATE, -1);
		return c.getTime();
	}
	
	/**
	 * 获得上个月的今天（时分秒均被设为零）
	 * @return
	 */
	public static Date prevMonth(){
		c = calendar(null, null, null, null, 0, 0, 0);
		c.add(Calendar.MONTH, -1);
		return c.getTime();
	}
	/**
	 * 获得指定日期月份 -1 的日期（时分秒均被设为零）
	 * @param d
	 * @return
	 */
	public static Date prevMonth(Date d){
		c = calendar(d, null, null, null, 0, 0, 0);
		c.add(Calendar.MONTH, -1);
		return c.getTime();
	}
	/**
	 * 获得下个月的今天（时分秒均被设为零）
	 * @return
	 */
	public static Date nextMonth(){
		c = calendar(null, null, null, null, 0, 0, 0);
		c.add(Calendar.MONTH, 1);
		return c.getTime();
	}
	/**
	 * 获得指定日期月份 +1 的日期（时分秒均被设为零）
	 * @param d
	 * @return
	 */
	public static Date nextMonth(Date d){
		c = calendar(d, null, null, null, 0, 0, 0);
		c.add(Calendar.MONTH, 1);
		return c.getTime();
	}
	
	/**
	 * 当前月份的第一天的Date对象，时分秒毫秒被设为0
	 * @return
	 */
	public static Date firstDateOfMonth(){
		return calendar(null, null, null, 1, 0, 0, 0).getTime();
	}
	/**
	 * 指定日期所在月份的第一天的Date对象，时分秒毫秒被设为0
	 * @param d
	 * @return
	 */
	public static Date firstDateOfMonth(Date d){
		return calendar(d, null, null, 1, 0, 0, 0).getTime();
	}
	/**
	 * 当前月份的最后一天的Date对象，时分秒毫秒被设为0
	 * @return
	 */
	public static Date lastDateOfMonth(){
		c = calendar(null, null, null, 1, 0, 0, 0);
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DATE, -1);
		return c.getTime();
	}
	/**
	 * 指定日期所在月份的最后一天的Date对象，时分秒毫秒被设为0
	 * @param d
	 * @return
	 */
	public static Date lastDateOfMonth(Date d){
		c = calendar(d, null, null, 1, 0, 0, 0);
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DATE, -1);
		return c.getTime();
	}
	/**
	 * 指定年月的第一天的Date对象，时分秒毫秒被设为0
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date firstDateOfMonth(Integer year, Integer month){
		return calendar(null, year, month, 1, 0, 0, 0).getTime();
	}
	/**
	 * 指定年月的最后一天的Date对象，时分秒毫秒被设为0
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date lastDateOfMonth(Integer year, Integer month){
		c = calendar(null, year, month, 1, 0, 0, 0);
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DATE, -1);
		return c.getTime();
	}
	/**
	 * 指定日期所在月份的所有日期的Date对象的List集合
	 * @param d
	 * @return
	 */
	public static List<Date> datesOfMonth(Date d){
		return datesBetween(firstDateOfMonth(d), lastDateOfMonth(d));
	}
	/**
	 * 指定年月的所有日期的Date对象的List集合
	 * @param year
	 * @param month
	 * @return
	 */
	public static List<Date> datesOfMonth(Integer year, Integer month){
		return datesBetween(firstDateOfMonth(year, month), lastDateOfMonth(year, month));
	}
	/**
	 * 两个日期时间包括这两个日期的所有日期的Date对象的List集合，
	 * @param s
	 * @param e
	 * @return
	 */
	public static List<Date> datesBetween(Date s, Date e){
		e = e==null?date():date(e);
		List<Date> list = new ArrayList<Date>(); 
		long milliseconds = date(s).getTime();
		while(milliseconds<=e.getTime()){
			list.add(date(new Date(milliseconds)));
			milliseconds += MILLISECOND_OF_DAY;
		}
		return list;
	}
	/**
	 * 两个时间之间间隔的毫秒数
	 * @param s
	 * @param e
	 * @return
	 */
	public static long millisecondsBetween(Date s, Date e){
		if(s==null || e==null)return 0;
		return Math.abs(e.getTime()-s.getTime());
	}
	/**
	 * 两个时间之间间隔的分钟数（小数部分大于0的作为一分钟计算）
	 * @param s
	 * @param e
	 * @return
	 */
	public static long minutesBetween(Date s, Date e){
		if(millisecondsBetween(s, e)%MILLISECOND_OF_MINUTE>0){
			return millisecondsBetween(s, e)/MILLISECOND_OF_MINUTE+1;
		}else{
			return millisecondsBetween(s, e)/MILLISECOND_OF_MINUTE;
		}
	}
	/**
	 * 两个时间之间间隔的分钟数（只取整数部分）
	 * @param s
	 * @param e
	 * @return
	 */
	public static long minutesBetween2(Date s, Date e){
		return millisecondsBetween(s, e)/MILLISECOND_OF_MINUTE;
	}
	/**
	 * 解析 yyyy-MM-dd hh:mm:ss 格式字符串对象，生成Date对象
	 * @param localeString
	 * @return
	 */
	public static Date parse(String localeString){
		localeString = localeString.trim();
		if(localeString.contains("-")){
			Pattern p = Pattern.compile("^\\d{4}-\\d{1,2}-\\d{1,2}( \\d{1,2}:\\d{1,2}:\\d{1,2})?$");
			Matcher m = p.matcher(localeString);
			if(m.matches()){
				Integer year;
				Integer month;
				Integer date;
				Integer hour;
				Integer minute;
				Integer second;
				if(localeString.contains(" ")){
					String[] dateTime = localeString.split(" ");
					String[] ymd = dateTime[0].split("-");
					year = Integer.valueOf(ymd[0]);
					month = Integer.valueOf(ymd[1])-1;
					date = Integer.valueOf(ymd[2]);
					String[] hms = dateTime[1].split(":");
					hour = Integer.valueOf(hms[0]);
					minute = Integer.valueOf(hms[1]);
					second = Integer.valueOf(hms[2]);
					return date(year, month, date, hour, minute, second);
				}else{
					String[] ymd = localeString.split("-");
					year = Integer.valueOf(ymd[0]);
					month = Integer.valueOf(ymd[1])-1;
					date = Integer.valueOf(ymd[2]);
					return date(year, month, date);
				}
			}
		}else{
			Pattern p = Pattern.compile("^\\d{13,}$");
			Matcher m = p.matcher(localeString);
			if(m.matches()){
				long ms = Long.valueOf(localeString);
				return new Date(ms);
			}
		}
		return null;
	}
	/**
	 * 时间对象d精确到指定参数位，i参数使用Calendar的常量（如Calendar.DATE）
	 * @param d
	 * @param i Calendar.YEAR、Calendar.MONTH、Calendar.DATE、Calendar.HOUR_OF_DAY、Calendar.MINUTE
	 * @return
	 */
	public static Date correctTo(Date d, int i){
		switch (i) {
		case Calendar.YEAR:
			return calendar(d, null, 0, 0, 0, 0, 0).getTime();
		case Calendar.MONTH:
			return calendar(d, null, null, 0, 0, 0, 0).getTime();
		case Calendar.DATE:
			return calendar(d, null, null, null, 0, 0, 0).getTime();
		case Calendar.HOUR_OF_DAY:
			return calendar(d, null, null, null, null, 0, 0).getTime();
		case Calendar.MINUTE:
			return calendar(d, null, null, null, null, null, 0).getTime();

		default:
			return calendar(d, null, null, null, null, null, null).getTime();
		}
	}
	/**
	 * 获得指定Date对象的 yyyy-MM-dd hh:mm:ss 格式字符串对象
	 * @param d
	 * @return
	 */
	public static String toLocaleString(Date d){
		c = Calendar.getInstance();
		if(d!=null)c.setTime(d);
		String month = minlength(c.get(Calendar.MONTH)+1, 2);
		String date = minlength(c.get(Calendar.DATE), 2);
		String hour = minlength(c.get(Calendar.HOUR_OF_DAY), 2);
		String minu = minlength(c.get(Calendar.MINUTE), 2);
		String sec = minlength(c.get(Calendar.SECOND), 2);
		return c.get(Calendar.YEAR)+"-"+month+"-"+date+" "+hour+":"+minu+":"+sec;
	}
	/**
	 * 获得指定毫秒值Date对象的 yyyy-MM-dd hh:mm:ss 格式字符串对象
	 * @param d
	 * @return
	 */
	public static String toLocaleString(Long timeMillis){
		c = Calendar.getInstance();
		if(timeMillis!=null)c.setTimeInMillis(timeMillis);
//		String month = minlength(c.get(Calendar.MONTH)+1, 2);
//		String date = minlength(c.get(Calendar.DATE), 2);
//		String hour = minlength(c.get(Calendar.HOUR_OF_DAY), 2);
//		String minu = minlength(c.get(Calendar.MINUTE), 2);
//		String sec = minlength(c.get(Calendar.SECOND), 2);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(c.getTime());
//		return c.get(Calendar.YEAR)+"-"+month+"-"+date+" "+hour+":"+minu+":"+sec;
	}
	/**
	 * 获得当前时间的 yyyy-MM-dd 格式字符串对象
	 * @param d
	 * @return
	 */
	public static String dateString(){
		c = Calendar.getInstance();
		String month = minlength(c.get(Calendar.MONTH)+1, 2);
		String date = minlength(c.get(Calendar.DATE), 2);
		return c.get(Calendar.YEAR)+"-"+month+"-"+date;
	}
	/**
	 * 获得指定Date对象的 yyyy-MM-dd 格式字符串对象
	 * @param d
	 * @return
	 */
	public static String string(Object o){
		if(o==null)return "";
		if(o instanceof Date){
			Date d = (Date) o;
			c = Calendar.getInstance();
			if(d!=null)c.setTime(d);
			String month = minlength(c.get(Calendar.MONTH)+1, 2);
			String date = minlength(c.get(Calendar.DATE), 2);
			return c.get(Calendar.YEAR)+"-"+month+"-"+date;
		}else{
			return o.toString();
		}
	}
	/**
	 * 获得指定Date对象的 yyyy-MM-dd 格式字符串对象
	 * @param d
	 * @return
	 */
	public static String dateString(Date d){
		c = Calendar.getInstance();
		if(d!=null)c.setTime(d);
		String month = minlength(c.get(Calendar.MONTH)+1, 2);
		String date = minlength(c.get(Calendar.DATE), 2);
		return c.get(Calendar.YEAR)+"-"+month+"-"+date;
	}
	/**
	 * 获得指定毫秒值Date对象的 yyyy-MM-dd 格式字符串对象
	 * @param d
	 * @return
	 */
	public static String dateString(Long timeMillis){
		c = Calendar.getInstance();
		if(timeMillis!=null)c.setTimeInMillis(timeMillis);
		String month = minlength(c.get(Calendar.MONTH)+1, 2);
		String date = minlength(c.get(Calendar.DATE), 2);
		return c.get(Calendar.YEAR)+"-"+month+"-"+date;
	}
	/**
	 * 最小长度，不足的在前边用‘0’补齐
	 * @param code
	 * @param min
	 * @return
	 */
	public static String minlength(Object code, Integer min){
		if(code!=null){
			if(min==null || min<=0 || min>8){
				min = 2;
			}
			String tmp = code.toString();
			Integer shortOf = min-tmp.length();
			if(shortOf>0){
				StringBuffer buffer = new StringBuffer();
				for (int i = 0; i < shortOf; i++) {
					buffer.append('0');
				}
				return buffer.toString()+tmp;
			}
			return tmp;
		}
		return "";
	}
	/**
	 * 
	 * @param list
	 */
	public static void show(List<Date> list){
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				System.out.println(toLocaleString(list.get(i)));
			}
		}
	}
	/**
	 * 获得指定Date对象的年月日字符串（格式yyyy-MM-dd）
	 * @param date
	 * @return
	 */
	public static String yearMonthDate(Date date) {
		c = calendar(date, null, null, null, null, null, null);
		return c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DATE);
	}
	/**
	 * 获得指定Date对象的年月日字符串（格式yyyy-MM）
	 * @param date
	 * @return
	 */
	public static String yearMonth(Date date) {
		c = calendar(date, null, null, null, null, null, null);
		return c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH);
	}
	/**
	 * 获得指定Date对象的年份
	 * @param date
	 * @return
	 */
	public static Integer getYear(Date date) {
		c = calendar(date, null, null, null, null, null, null);
		return c.get(Calendar.YEAR);
	}
	/**
	 * 获得指定Date对象的月份
	 * @param date
	 * @return
	 */
	public static Integer getMonth(Date date) {
		c = calendar(date, null, null, null, null, null, null);
		return c.get(Calendar.MONTH);
	}
	/**
	 * 获得指定Date对象在月份中的天
	 * @param date
	 * @return
	 */
	public static Integer getDate(Date date) {
		c = calendar(date, null, null, null, null, null, null);
		return c.get(Calendar.DATE);
	}
	/**
	 * 获得指定Date对象在星期中的日(星期天-星期六：0-6)
	 * @param date
	 * @return
	 */
	public static Integer getDay(Date date) {
		c = calendar(date, null, null, null, null, null, null);
		return c.get(Calendar.DAY_OF_WEEK)-1;
	}
	/**
	 * 获得指定Date对象在24小时中的时
	 * @param date
	 * @return
	 */
	public static Integer getHour(Date date) {
		c = calendar(date, null, null, null, null, null, null);
		return c.get(Calendar.HOUR_OF_DAY);
	}
	/**
	 * 获得指定Date对象在24小时中的分
	 * @param date
	 * @return
	 */
	public static Integer getMinute(Date date) {
		c = calendar(date, null, null, null, null, null, null);
		return c.get(Calendar.MINUTE);
	}
	/**
	 * 获得指定Date对象在24小时中的秒
	 * @param date
	 * @return
	 */
	public static Integer getSecond(Date date) {
		c = calendar(date, null, null, null, null, null, null);
		return c.get(Calendar.SECOND);
	}
	/**
	 * 获得指定Date对象的时分组成的字符串（格式：hh:mm）
	 * @param date
	 * @return
	 */
	public static String hourMinute(Date date) {
		c = calendar(date, null, null, null, null, null, null);
		return c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE);
	}
	
	/**
	 * 从list中匹配最接近target的数据，基本匹配范围(target-before)到(target+after)
	 * @param list
	 * @param target
	 * @param before
	 * @param after
	 * @param status “true”优先匹配小于target的数据，“false”优先匹配大于target的数据
	 * @return
	 */
	public static Date magnetism(List<Date> list, List<Date> list2, long target, long before, long after, boolean status){
		Date index = null;
		Date less_than = null;
		Date greater_than = null;
		if(list!=null && list.size()>0){
			for (int k = 0; k < list.size(); k++) {
				Date d = list.get(k);
				if(d!=null){
					if(isThis(target, before, after, d)){
						if(d.getTime()<target){
							if(less_than==null){
								less_than = d;
							}else{
								if(d.compareTo(less_than)>0){
									less_than = d;
								}
							}
						}else if(d.getTime()>target){
							if(greater_than==null){
								greater_than = d;
							}else{
								if(d.compareTo(greater_than)<0){
									greater_than = d;
								}
							}
						}else{
							index = d;
							break;
						}
					}
				}
			}
		}
		if(list2!=null && list2.size()>0){
			for (int k = 0; k < list2.size(); k++) {
				Date d = list2.get(k);
				if(d!=null){
					if(isThis(target, before, after, d)){
						if(d.getTime()<target){
							if(less_than==null){
								less_than = d;
							}else{
								if(d.compareTo(less_than)>0){
									less_than = d;
								}
							}
						}else if(d.getTime()>target){
							if(greater_than==null){
								greater_than = d;
							}else{
								if(d.compareTo(greater_than)<0){
									greater_than = d;
								}
							}
						}else{
							index = d;
							break;
						}
					}
				}
			}
		}
		if(index==null){
			if(status){
				if(less_than!=null){
					index = less_than;
				}else{
					index = greater_than;
				}
			}else{
				if(greater_than!=null){
					index = greater_than;
				}else{
					index = less_than;
				}
			}
		}
		list.remove(index);
		return index;
	}
	private static boolean isThis(long in,long before, long after, Date d) {
		if(d!=null){
			long l = d.getTime();
			if(in-before<=l && l<=in+after){
				return true;
			}
		}
		return false;
	}

	/**
	 * 学期结束时间
	 * @param d
	 */
	public static Date termEndTime(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		if(c.get(Calendar.MONTH)>=8){
			c.add(Calendar.YEAR, 1);
		}
		c.set(Calendar.MONTH, 6);
		c.set(Calendar.DAY_OF_MONTH, 1);
		d = c.getTime();
		return d;
	}
	/**
	 * 学期结束时间
	 * @param year
	 * @return
	 */
	public static Date termEndTime(String year){
		return termEndTime(Integer.parseInt(year));
	}
	/**
	 * 学期结束时间
	 * @param year
	 * @return
	 */
	public static Date termEndTime(Integer year){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year+1);
		c.set(Calendar.MONTH, 6);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}
	
}
