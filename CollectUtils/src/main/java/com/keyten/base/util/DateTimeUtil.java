package com.keyten.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 日期格式处理类
 * @author liym
 *
 */
public class DateTimeUtil {
	
	/**
	 * 根据long类型日期获取格式为yyyy-MM-dd的日期
	 * @param longdate
	 * @return  yyyy-MM-dd
	 */
	public synchronized static String getDateByLong(long longdate)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(longdate);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		String str = format.format(c.getTime());
		return str;
	}
	
	
	/**
	 * 根据long类型日期获取格式为HH:mm:ss的时间
	 * @param longdate
	 * @return
	 */
	public synchronized static String getTimeByLong(long longdate)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(longdate);
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss"); 
		String str = format.format(c.getTime());
		return str;
	}
	
	/**
	 * 根据long类型日期获取格式为yyyy-MM-dd HH:mm:ss的日期时间
	 * @param longdate
	 * @return
	 */
	public synchronized static String getDateTimeByLong(long longdate)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(longdate);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String str = format.format(c.getTime());
		return str;
	}
	/**
     * date2比date1多的天数
     * @param date1    
     * @param date2
     * @return    
     */
    public synchronized static int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
//        System.out.println("判断day2"+ day2 +" - day1 : " + day1 );
        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
//        System.out.println("判断year2"+ year2 +" - year1 : " + year1 );
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2-day1) ;
        }
        else    //不同年
        {
            
            return day2-day1;
        }
    }
	/**
	 * 更正日期格式
	 * 解决日期格式问题：由2014-8-1改为2014-08-01
	 * @param strDate
	 * @return
	 */
	public synchronized static String formatDate(String strDate)
	{
		String resultDate = "";
		String[] arrStr = strDate.split("-");
		if(arrStr.length>0)
		{
			String year = arrStr[0];
			String month = arrStr[1];
			String day = arrStr[2];
			if(month.length()==1)
			{
				month = "0"+month;
			}
			if(day.length() ==1)
			{
				day = "0"+day;
			}
			resultDate = year+"-"+month+"-"+day;
		}
		
		return resultDate;
	}
	
	
	/**
	 * 更正时间格式
	 * 解决日期格式问题：由9:30:8改为09:30:08
	 * @param strDate
	 * @return
	 */
	public synchronized static String formatTime(String strTime)
	{
		String resultDate = "";
		String[] arrStr = strTime.split(":");
		if(arrStr.length>0)
		{
			String hour = arrStr[0];
			String minite = arrStr[1];
			String second = arrStr[2];
			if(hour.length()==1)
			{
				hour = "0"+hour;
			}
			if(minite.length() ==1)
			{
				minite = "0"+minite;
			}
			if(second.length() ==1)
			{
				second = "0"+second;
			}
			resultDate = hour+":"+minite+":"+second;
		}
		
		return resultDate;
	}
	
	/**
	 * 10分钟前 转化为  10:00:00 
	 * @param strTime
	 * @return
	 */
	public synchronized static String minute(String strTime){
		Date now = new Date();
		Date now_strTime = new Date(now.getTime() - Integer.parseInt(strTime)*60*1000); //strTime分钟前的时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");//可以方便地修改日期格式
		String nowTime_strTime = dateFormat.format(now_strTime);
		System.out.println(nowTime_strTime);
		return nowTime_strTime;
	}
	
	/**
	 * 10秒前 转化为  09:59:50
	 * @param strTime
	 * @return
	 */
	public synchronized static String second(String strTime){
		Date now = new Date();
		Date now_strTime = new Date(now.getTime() - Integer.parseInt(strTime)*1000); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		String nowTime_strTime = dateFormat.format(now_strTime);
		System.out.println(nowTime_strTime);
		return nowTime_strTime;
	}
	/**
	 * 将   n小时前、分钟前、秒前、天前转化成   yyyy-MM-dd HH:mm:ss 格式
	 * @param datetime
	 * @return
	 */
	public synchronized static String getNowByDateStr(String plTime){
		//处理发布时间格式
		Calendar now = Calendar.getInstance();
		if(plTime.contains("分钟前")){
			plTime = plTime.substring(0,plTime.indexOf("分钟前"));
			now.add(Calendar.MINUTE, (Integer.parseInt(plTime)*-1));// 分钟之前的时间
			Date before = now.getTime();
			plTime = DateTimeUtil.getDateTimeByLong(before.getTime());
		}else if(plTime.contains("秒前")){
			plTime = plTime.substring(0,plTime.indexOf("秒前"));
			now.add(Calendar.SECOND, (Integer.parseInt(plTime)*-1));// 秒钟之前的时间
			Date before = now.getTime();
			plTime = DateTimeUtil.getDateTimeByLong(before.getTime());
		}else if(plTime.contains("天前")){
			plTime = plTime.substring(0,plTime.indexOf("天前"));
			now.add(Calendar.DATE, (Integer.parseInt(plTime)*-1));// 秒钟之前的时间
			Date before = now.getTime();
			plTime = DateTimeUtil.getDateTimeByLong(before.getTime());
		}else if(plTime.contains("小时前")){
			plTime = plTime.substring(0,plTime.indexOf("小时前"));
			now.add(Calendar.HOUR, (Integer.parseInt(plTime)*-1));// 秒钟之前的时间
			Date before = now.getTime();
			plTime = DateTimeUtil.getDateTimeByLong(before.getTime());
		}
		return plTime;
	}
	/**
	 * 格式化日期(正则匹配)
	 * @param url
	 * @return
	 */
	public synchronized static String fmtDate(String dataDate){
		String[] regexDate = {"\\d{1,2}( )*[\\-\\/\\.]( )*\\d{1,2}( )*[\\-\\/\\.]( )*\\d{4}",
							  "\\d{2,4}( )*[\\/\\.]( )*\\d{1,2}( )*[\\/\\.]( )*\\d{1,2}",
							  "\\d{2,4}( )*-( )*\\d{1,2}( )*-( )*\\d{1,2}",
							  "\\d{2,4}( )*年( )*\\d{1,2}( )*月( )*\\d{1,2}( )*(日)?","\\d{10,13}",
							  "\\d{1,2}-\\d{1,2} \\d{4}","\\d{1,2}-\\d{1,2}","\\d{8}"};
		boolean isPass = false;
		for(int i = 0 ; i<regexDate.length; i++){
			Pattern pattern = Pattern.compile(regexDate[i]);
			Matcher matcher = pattern.matcher(dataDate);
			isPass = matcher.find();
			if(isPass){
				dataDate = matcher.group();
				if(i==0){
					dataDate = dataDate.replaceAll("[\\-\\/\\.]", "-");
					String year = dataDate.substring(dataDate.lastIndexOf("-")+1);
					String dm = dataDate.substring(0,dataDate.lastIndexOf("-"));
					String[] dmArray = dm.split("-");
					if(Integer.parseInt(dmArray[0])>12){
						dataDate = year+"-"+dmArray[1]+"-"+dmArray[0];
					}else
						dataDate = year+"-"+dm;
				}else if(i==1||i==2)
					dataDate = dataDate.replaceAll("[\\-\\/\\.]", "-");
				else if(i==3){
					dataDate = dataDate.replaceAll("[年|月]","-");
					dataDate = dataDate.replaceAll("日","");
				}else if(i==4){
					dataDate = dataDate.length()==13?dataDate:dataDate+"000";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date = new Date(Long.parseLong(dataDate));
					dataDate = sdf.format(date);
				}else if(i==5){
					String[] dtArray = dataDate.split(" ");
					dataDate = dtArray[1]+"-"+dtArray[0];
				}else if(i==6){
					Calendar cal = Calendar.getInstance();
					int year = cal.get(Calendar.YEAR);
					dataDate = year+"-"+dataDate;
				}else if(i==7){
					String year = dataDate.substring(0,4);
					String months = dataDate.substring(4,6);
					String day = dataDate.substring(6);
					dataDate = year + "-" + months + "-" + day;
				}
				String year = dataDate.substring(0,dataDate.indexOf("-"));
				if(year.length()<4){
					dataDate = "20"+year+dataDate.substring(dataDate.indexOf("-"));
				}
				dataDate = formatDate(dataDate).replaceAll(" ","");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					//第一次判断    修改的是   2018-13-10   改为2018-10-13
					Date parse = sdf.parse(dataDate);
					int days = differentDays(parse, new Date());
					if(days<0){
						String[] d = dataDate.split("-");
						dataDate = d[0]+"-"+d[2]+"-"+d[1];
					}
					//第二次判断   将所有大于当前日期的全部置为当前日期
					parse = sdf.parse(dataDate);
					days = differentDays(parse, new Date());
					if(days<0){
						dataDate = sdf.format(new Date());
					}
				} catch (ParseException e) {
				}
				break;
			}
		}
		if(isPass){
				return dataDate;
		}else
			return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}
	/**
	 * 格式化时间(正则匹配)
	 * @param datatime
	 * @return
	 */
	public synchronized static String fmtTime(String datatime){
//		datatime = datatime.replaceAll("( |	)","");//过滤空格和tab
		datatime = datatime.replaceAll("：",":");
		String[] regexDate = {"\\d{1,2}( )*:\\d{1,2}( )*:\\d{1,2}","\\d{1,2}( )*:\\d{1,2}","\\d{10,13}","\\d+分钟前"};
		boolean isPass = false;
		boolean is24Hour = true;
		if(datatime.indexOf("PM")>-1){
			is24Hour = false;
		}
		for(int i = 0 ; i<regexDate.length; i++){
			Pattern pattern = Pattern.compile(regexDate[i]);
			Matcher matcher = pattern.matcher(datatime);
			isPass = matcher.find();
			if(isPass){
				datatime = matcher.group();
				if(i==2){
					datatime = datatime.length()==13?datatime:datatime+"000";
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
					Date date = new Date(Long.parseLong(datatime));
					datatime = sdf.format(date);
				}else if(i==1){
					datatime = datatime + ":00";
				}else if(i==3){
					String time = datatime.substring(0,datatime.indexOf("分钟前"));
					Calendar c = Calendar.getInstance();
					c.add(Calendar.MINUTE,-1*Integer.valueOf(time.trim()));
					SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
					datatime = sdf.format(c.getTime());
				}
				break;
			}
		}
		
		if(isPass){
			if(!is24Hour){
				String hour = datatime.substring(0,datatime.indexOf(":"));
				hour = (Integer.parseInt(hour)+12)+"";
				datatime = hour + datatime.substring(datatime.indexOf(":"));
			}
			datatime = formatTime(datatime);
			return datatime;
		}else{
			return new SimpleDateFormat("HH:mm:ss").format(new Date());
		}
	}
	public static void main(String [] args)
	{
//		DateTimeUtil t = new DateTimeUtil();
//		long longdatetime = Long.parseLong("1417701748000");
//		System.out.println(t.getDateByLong(longdatetime));
//		System.out.println(t.getTimeByLong(longdatetime));
//		System.out.println(t.formatTime("9:3:5"));
		String date = "2018-1-5、07 : 57 ";
		HtmlUtil hutil =  new HtmlUtil();
		String conn = hutil.delHTMLTag(date, "span");
		System.out.println(conn);
		
//		String date1 = t.fmtDate(conn);
//		String time =  t.fmtTime(conn);
//		System.out.println(date1 + "  " +  time +new SimpleDateFormat("HH:mm:ss").format(new Date()));
	}
	
}
