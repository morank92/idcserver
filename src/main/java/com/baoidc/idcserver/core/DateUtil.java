package com.baoidc.idcserver.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class DateUtil {
	
	/**
	 * 根据计算开始和结束时间的差，来判断流量报表的时间段
	 * @param STime
	 * @param ETime
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String,String> isDeafultTime(String STime, String ETime)	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		STime += ":00";
		ETime += ":00";
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd");//天表后缀
		String startGenerate;
		int syear;
		int smonth;
		int sday;
		int shour;
		int eyear;
		int emonth;
		int eday;
		int ehour;
		//转化为日期
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = sdf.parse(STime);
			endDate = sdf.parse(ETime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//计算时间差
		Calendar endCalendar = new GregorianCalendar();
		Calendar startCalendar = new GregorianCalendar();
		startCalendar.setTime(startDate);
		endCalendar.setTime(endDate);
		//获取年月日时
		syear=startCalendar.get(Calendar.YEAR);
		smonth=startCalendar.get(Calendar.MONTH)+1;
		sday=startCalendar.get(Calendar.DAY_OF_MONTH);
		shour=startCalendar.get(Calendar.HOUR_OF_DAY);
		
		eyear=endCalendar.get(Calendar.YEAR);
		emonth=endCalendar.get(Calendar.MONTH)+1;
		eday=endCalendar.get(Calendar.DAY_OF_MONTH);
		ehour=endCalendar.get(Calendar.HOUR_OF_DAY);
		//判断查询范围
		/*if(syear==eyear){//同一年
			if(smonth == emonth){//同一月
				
			}
		}*/
		//开始和结束的时间差单位为毫秒
		long timeDiff = endDate.getTime() - startDate.getTime();
		long dayTime = 24*60*60*1000;//一天的时间
		String sStr = sdfDay.format(startDate);//如20160908
		String sdayStr = "_"+sStr;
		String monthStr = "_"+sStr.substring(0,6);//_201609
		if( timeDiff <= dayTime && timeDiff>0){//天表查询（表名：xxx_20160709）
			if(sday == eday){//在同一天表查询
				map.put(DateTimeStr.FLAGX, "1");//当天查询，x轴时间刻度00:00
				map.put(DateTimeStr.MESSAGE_TIME, "1");
				map.put(DateTimeStr.TABLE_TYPE, sdayStr);//表名后缀相同
				map.put(DateTimeStr.START_TIME, STime);
				map.put(DateTimeStr.END_TIME, ETime);
			}else{
				map.put(DateTimeStr.FLAGX, "2");//跨天分两个表查询，x轴时间刻度00-00 00:00 
				map.put(DateTimeStr.MESSAGE_TIME, "2");
				//第一张表的时间范围
				map.put(DateTimeStr.START_TIME, STime);
				map.put(DateTimeStr.START_TIME_E, STime.substring(0, STime.indexOf(" "))+" 23:59:59");//结束时间
				//第二张表的时间范围
				map.put(DateTimeStr.END_TIME_S, ETime.substring(0, ETime.indexOf(" "))+" 00:00:00");//开始时间
				map.put(DateTimeStr.END_TIME, ETime);
				//两张表名,表名后缀不同
				map.put(DateTimeStr.TABLE_TYPE2, sdayStr);//第一张表
				map.put(DateTimeStr.TABLE_TYPE2_2, "_"+sdfDay.format(endDate));//第二张表
			}
		}
		if(timeDiff>dayTime && timeDiff<7*dayTime){//时间范围长度在大于1小于7天以内，从月表中查询,即每条数据都是一天的平均数
			if(smonth == emonth){//在同一个月中
				map.put(DateTimeStr.FLAGX, "m");//x轴时间刻度00-00 00:00，补后续时间刻度为一小时增加
				map.put(DateTimeStr.MESSAGE_TIME, "1");//同一个月表，查询一张表
				System.out.println("同一月同一月同一月同一月-----"+monthStr);
				map.put(DateTimeStr.TABLE_TYPE, monthStr);//表名后缀相同
				map.put(DateTimeStr.START_TIME, STime);
				map.put(DateTimeStr.END_TIME, ETime);
			}else{//在两个月表中
				map.put(DateTimeStr.FLAGX, "m");//跨天分两个表查询，x轴时间刻度00-00 00:00
				map.put(DateTimeStr.MESSAGE_TIME, "2");//
				//第一张表的时间范围
				map.put(DateTimeStr.START_TIME, STime);
				//结束时间为当月最后一天
				int lastDay = startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				map.put(DateTimeStr.START_TIME_E, STime.substring(0, STime.lastIndexOf("-")+1)+lastDay+" 23:59:59");//结束时间
				//第二张表的时间范围
				map.put(DateTimeStr.END_TIME_S, ETime.substring(0, ETime.lastIndexOf("-")+1)+"01"+" 00:00:00");//开始时间
				map.put(DateTimeStr.END_TIME, ETime);
				//两张表名,表名后缀不同
				map.put(DateTimeStr.TABLE_TYPE2, monthStr);//第一张表
				map.put(DateTimeStr.TABLE_TYPE2_2, "_"+sdfDay.format(endDate).substring(0,6));//第二张表
				
			}
		}
		if(timeDiff < 0){
			map.put(DateTimeStr.TIME_ERROR, "timeInputError");
		}
		return map;
	}
	
	public static int getDaysOfMethod(String source){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date sourceDate = null;
		try {
			sourceDate = sdf.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = new GregorianCalendar();
		if(sourceDate != null){
			calendar.setTime(sourceDate);
		}
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 根据开始时间以及时长算出失效时间
	 * @param startDate
	 * @param duration
	 * @return
	 * @throws Exception
	 */
	public static String getExpireDate(String startDate,int duration){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date sourceDate = null;
		try {
			sourceDate = sdf.parse(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(sourceDate);
		calendar.add(Calendar.MONTH, +duration);
		sourceDate = calendar.getTime();
		
		String expireDate = sdf.format(sourceDate);
		
		return expireDate;
	}
	
	//根据失效日期计算剩余时长
	public static int getBalance(String expireDate,String currentDate,String pattern,TimeType timeType){ 
		
		int balance = 0;
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Calendar expireCalendar = Calendar.getInstance();
		Calendar currentCalendar = Calendar.getInstance();
		try{
			expireCalendar.setTime(sdf.parse(expireDate));
			currentCalendar.setTime(sdf.parse(currentDate));
			long l = expireCalendar.getTimeInMillis() - currentCalendar.getTimeInMillis();
			switch(timeType.getVal()){
			case 0:
				balance = 0;
				break;
			case 1:
				balance = 1;
				break;
			case 2:
				balance = new Long(l/(1000*60*60*24)).intValue(); //返回天数间隔
				break;
			case 3:
				balance = new Long(l/(1000*60*60)).intValue(); //返回小时间隔
				break;
			case 4:
				balance = new Long(l/(1000*60)).intValue();//返回分钟间隔
				break;
			case 5:
				balance = new Long(l/(1000)).intValue();//返回秒间隔
				break;
			default:
				balance = new Long(l/(1000*60*60*24)).intValue();
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return balance;
	}
	
	public static SimpleDateFormat getSdf(String datePattern){
		return new SimpleDateFormat(datePattern);
	}
	
	
	public static String dateToStr(Date date,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	public static String dateToStr2(Date date,String pattern){//第二天凌晨
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_YEAR, +1);//第二天
		
		Date ndate = c.getTime();
		String timeStr = sdf.format(ndate) + " 00:00:00";
		
		return timeStr;
	}

	public static void main(String[] args) throws Exception {
		
		String expireTime = "2017-07-06 00:00:00";
		String currentTime = "2017-07-08 00:00:00";
		System.out.println(getBalance(expireTime, currentTime, "yyyy-MM-dd HH:mm:ss", TimeType.DAY));
		
	}

}
