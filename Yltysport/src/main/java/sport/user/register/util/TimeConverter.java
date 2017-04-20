package sport.user.register.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class TimeConverter{
	
	//两个时间间隔的天数
	public static long getDaySub(String beginDateStr,String endDateStr) 
	{ 
		long day=0; 
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		java.util.Date beginDate; 
		java.util.Date endDate; 
		try 
		{ 
			beginDate = format.parse(beginDateStr); 
			endDate= format.parse(endDateStr); 
			day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000); 
			System.out.println("相隔的天数="+day); 
		} catch (ParseException e) 
		{ 
			// TODO 自动生成 catch 块 
			e.printStackTrace(); 
		} 
		return day; 
	}
	//当前时间和过去的某个时间间隔的天数
	public static long getNowDaySub(String postDateStr) 
	{ 
		long day=0; 
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		java.util.Date postDate; 
		java.util.Date nowdate=new java.util.Date();  
		try 
		{ 
			postDate = format.parse(postDateStr);  
			day=(nowdate.getTime()-postDate.getTime())/(24*60*60*1000); 
			System.out.println("相隔的天数="+day); 
		} catch (ParseException e) 
		{ 
			// TODO 自动生成 catch 块 
			e.printStackTrace(); 
		} 
		return day; 
	}
	public static Date stringToDate(String stringDate) throws ParseException{
		SimpleDateFormat dd=new SimpleDateFormat("yyyy-MM-dd HH:mm"); //yyyy-MM-dd HH:mm:ss
		Date date=dd.parse(stringDate); 
		return date;
	}
	//某个时间，加上n天，得到最后的日期
	//参数days表示月份
	public static Date afterDate(Date vip_from,int months){
		//System.out.println("days为："+days);
		//System.out.println("vip_from的值为："+vip_from);
		long vip_to_timestamp=vip_from.getTime()/1000;
		//System.out.println("vip_from_gettime："+vip_to_timestamp);
		vip_to_timestamp=vip_to_timestamp+months*30*24*60*60;
		//System.out.println("vip_to_timestamp："+vip_to_timestamp);
		Date time = vip_from;
		time.setTime(vip_to_timestamp*1000);
		//System.out.println("time的值为："+time);
		return time;
	}
	//参数days表示月份
	public static Date afterDate1(Date vip_from,int days){
		//System.out.println("days为："+days);
		//System.out.println("vip_from的值为："+vip_from);
		long vip_to_timestamp=vip_from.getTime()/1000;
		//System.out.println("vip_from_gettime："+vip_to_timestamp);
		vip_to_timestamp=vip_to_timestamp+days*24*60*60;
		//System.out.println("vip_to_timestamp："+vip_to_timestamp);
		Date time = vip_from;
		time.setTime(vip_to_timestamp*1000);
		//System.out.println("time的值为："+time);
		return time;
	}
	public static String dateToString(Date vip_to){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		return sdf.format(vip_to);
	}
	
	public static String timeformattrv(String timestring){
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");  
		Date date = null;
		try {
			date = (Date) sdf1.parse(timestring);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		return sdf2.format(date);  
	}
}
