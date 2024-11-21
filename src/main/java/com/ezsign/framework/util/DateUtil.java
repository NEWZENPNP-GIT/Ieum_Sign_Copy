package com.ezsign.framework.util;

/**
 * @Class Name  : com.nexcos.framework.util.DateUtil
 * @Description :
 * @Modification Information  
 * @
 * @     수정일                         수정자             수정내용
 * @ -------------     ---------   ---------------------------------
 * @  2015. 8. 10.      유지운                 최초생성
 * 
 * @Company : Nexcos. Inc
 * @Author  : 유지운
 * @Date    : 2015. 8. 10. 오후 4:21:02
 * @version : 1.0
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import java.util.Calendar;

public class DateUtil {

	private static final String DATE_GUBUN = ".";
	private static final String TIME_GUBUN = ":";
    private static DateUtil dateTimeUtilIns = null;

	public static void main(String[] args) {
		try {
			String tempPwd = "309cqr770k";
			String encPwd = SecurityUtil.encrypt(tempPwd);
			System.out.println(encPwd);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
    public static DateUtil getInstance(){  
	    if(dateTimeUtilIns == null) {
	        dateTimeUtilIns = new DateUtil();
	     }
	
	     return dateTimeUtilIns;
    }

	/**
	 * Description  : 현재 날짜의 차이
	 * @Method Name : diffOfDate
	 * @param date : 입력일자
	 * @return long 
	 */
    public static long diffOfDate(String date) throws Exception
    {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
      
      Date beginDate = formatter.parse(date);
      Date endDate = new Date();
      
      long diff = endDate.getTime() - beginDate.getTime();
      long diffDays = diff / (24 * 60 * 60 * 1000);
      
      return diffDays;
    }

	/**
	 * Description  : 설정 날짜의 차이
	 * @Method Name : diffOfDate
	 * @param date : 입력일자
	 * @return long 
	 */
    public static long diffOfDate(String fromdate, String todate) throws Exception
    {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
      
      Date beginDate = formatter.parse(todate);
      Date endDate = formatter.parse(fromdate);
      
      long diff = endDate.getTime() - beginDate.getTime();
      long diffDays = diff / (24 * 60 * 60 * 1000);
      
      return diffDays;
    }
    
	/**
	 * Description  : 설정된 mode에 따른 Time stamp를 반환한다.
	 * @Method Name : getTimeStamp
	 * @param iMode : 날짜변환 모드
	 * @return timeStamp String
	 */
	public static String getTimeStamp(int iMode) {
		String sFormat;
		// if (iMode == 1) sFormat = "E MMM dd HH:mm:ss z yyyy"; // Wed Feb 03
		// 15:26:32 GMT+09:00 1999
		if (iMode == 1)
			sFormat = "MMMM dd, yyyy HH:mm:ss z"; // Jun 03, 2001 15:26:32
													// GMT+09:00
		else if (iMode == 2)
			sFormat = "MM/dd/yyyy";// 02/15/1999
		else if (iMode == 3)
			sFormat = "yyyyMMdd";// 19990215
		else if (iMode == 4)
			sFormat = "HHmmss";// 121241
		else if (iMode == 5)
			sFormat = "dd MMM yyyy";// 15 Jan 1999
		else if (iMode == 6)
			sFormat = "yyyyMMddHHmm"; // 200101011010
		else if (iMode == 7)
			sFormat = "yyyyMMddHHmmss"; // 20010101101052
		else if (iMode == 8)
			sFormat = "HHmmss";
		else if (iMode == 9)
			sFormat = "yyyy.MM.dd";
		else if (iMode == 10)
			sFormat = "yyyy";
		else if(iMode == 11)
			sFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        else if(iMode == 12)
            sFormat = "yyyy-MM-dd HH:mm:ss";
        else if(iMode == 13)
            sFormat = "HH";
        else if(iMode == 14)
            sFormat = "yyyyMM";
		else
			sFormat = "E MMM dd HH:mm:ss z yyyy";// Wed Feb 03 15:26:32
													// GMT+09:00 1999

		Locale locale = new Locale("en", "EN");
		// SimpleTimeZone timeZone = new SimpleTimeZone(32400000, "KST");
		SimpleDateFormat formatter = new SimpleDateFormat(sFormat, locale);


		return formatter.format(new Date());
	}
	
	/**
	 * Description  : Date형을 yyyy.MM.dd형의 String으로 변환.
	 * @Method Name : dateToString
	 * @param date : 날짜 Date형
	 * @return 날짜 String형. null일 경우 null return
	 */
	public static String dateToString(Date date) {
		if (date != null)
			return dateToString(date, "yyyy.MM.dd");
		else
			return null;
	}
	
	/**
	 * Description  : Date형을 원하는 포맷으로 변환하여 스트링으로 전환한다.
	 * @Method Name : dateToString
	 * @param date : 날짜 Date형
	 * @param fmt
	 * @return 날짜 String형. null일 경우 null return
	 */
	public static String dateToString(Date date, String fmt) {
		if (date != null && fmt != null) {
			SimpleDateFormat sfmt = new SimpleDateFormat(fmt, Locale.getDefault());
			return sfmt.format(date);
		} else
			return null;
	}
	
	public static Date StringToDate(String date) {
		if (date != null)
			return StringToDate(date, "yyyyMMddHHmmss");
		else
			return null;
	}
	
	public static Date StringToDate(String date, String fmt) {
		if (date != null) {

			SimpleDateFormat transFormat = new SimpleDateFormat(fmt, Locale.getDefault());
			try {
				return transFormat.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		} else
			return null;
	}
	

	/**
	 * 현재 시간을 돌려준다. - HH:MI:SS 
	 */
	public static String getTimeText(int type, String szTime)
	{
		if(szTime != null && szTime.length() != 6) return ""; 
		
		
		if(szTime != null && szTime.length() == 6){			
			String hour = StringUtil.substring(szTime,0, 2);
			String minute = StringUtil.substring(szTime, 2, 4);
			String second = StringUtil.substring(szTime,4, 6);
		
			switch(type) {
		        case 1:
		            return hour + TIME_GUBUN + minute + TIME_GUBUN+ second;
		        case 2:
		            return  hour + TIME_GUBUN + minute;
		        case 3:    
		            return  hour;
	       }
		}	

		return "";
	}

	/**
	 * 현재 시간을 돌려준다. - HHMISS 
	 */
	public static String getTime(){
		String hour, min, sec;

 		Calendar cal = Calendar.getInstance(Locale.getDefault());

 		StringBuffer buf = new StringBuffer();

 		hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
 		if(hour.length() == 1) hour = "0" + hour;

 		min = Integer.toString(cal.get(Calendar.MINUTE));
 		if(min.length() == 1) min = "0" + min;

 		sec = Integer.toString(cal.get(Calendar.SECOND));
 		if(sec.length() == 1) sec = "0" + sec;

 		buf.append(hour);
 		buf.append(min);
 		buf.append(sec);

		return buf.toString();
	}

	/**
	 * 현재 년월일을 돌려준다. - YYYY.MM.DD
	 * TYPE 1 : YYYY.MM.DD
	 * TYPE 2 : YY.MM.DD
	 * TYPE 3 : MM.DD
	 * TYPE 4 : YYYY.MM
	 * TYPE 5 : YYYY
	 * TYPE 6 : YYYY-MM-DD
	 */
	public static String getDateText(int type, String szdate){
	    return getDateText(type, szdate,DATE_GUBUN);
	}
	public static String getDateText(int type, String szdate,String delimeter)
	{
		String reDate = "";
		
		if(szdate != null && szdate.length() != 8) return ""; 
		
		
		if(szdate != null && szdate.length() == 8){			
			String year = szdate.substring(0, 4);
			String month = szdate.substring(4, 6);
			String day = szdate.substring(6, 8);
		
			switch(type) {
		        case 1:
		            return  year + delimeter + month + delimeter + day;
		        case 2:
		            return  year.substring(2, 4) + delimeter + month + delimeter + day;
		        case 3:    
		            return month + delimeter + day;
		        case 4:
		            return  year + delimeter + month;
		        case 5:
		            return year;
		        case 6:
		        	return  year + "-" + month + "-" + day;
		        	
	       }
		}	
		
		return "";
	}
	
	/**
	 * 특정형태의 날자 타입을 돌려준다.
	 * TYPE 0 : YYYY.MM.DD HH:MI:SS
	 * TYPE 1 : YYYY.MM.DD
	 * TYPE 2 : YY.MM.DD
	 * TYPE 3 : MM.DD
	 * TYPE 4 : YYYY.MM
	 * TYPE 5 : YYYY
	 * TYPE 6 : MM.DD HH:MI
	 * TYPE 7 : HH:MI
	 * TYPE 8 : YYYY-MM-DD
     * @param type
	 * @param dateTime
	 * @return
	 */
	public static String getDateType(int type, String date){
	    return getDateType(type, date, DATE_GUBUN);
	}
	
	public static String getDateType(int type, String date, String delimeter)
	{
		if (date == null) {
			return "";
		}

		if(date.length() == 12) date += "01";
	    else if(date.length() == 10) date += "0101";
        else if(date.length() == 8) date += "010101";
        else if(date.length() == 6) date += "01010101";
        else if(date.length() == 4) date += "0101010101";

        switch(type) {
	        case 0:
	            return getDateText(1,StringUtil.substring(date, 0, 8), delimeter) + " " + getTimeText(1,StringUtil.substring(date, 8, 14));
	        case 1:
	            return getDateText(1,StringUtil.substring(date, 0, 8), delimeter);
	        case 2:
	            return getDateText(2,StringUtil.substring(date, 0, 8), delimeter);
	        case 3:
	            return getDateText(3,StringUtil.substring(date, 0, 8), delimeter);
	        case 4:
	            return getDateText(4,StringUtil.substring(date, 0, 8), delimeter);
	        case 5:
	            return getDateText(5,StringUtil.substring(date, 0, 8), delimeter);
	        case 6:
	            return getDateText(3,StringUtil.substring(date, 0, 8), delimeter) + " " + getTimeText(2,StringUtil.substring(date, 8, 14));
	        case 7:
	            return getTimeText(2,StringUtil.substring(date, 8, 14));
	        case 8:
	            return getDateText(1,StringUtil.substring(date, 0, 8), "-");
        }
        
        return "";
	}

    /**
     * 현재 년도를 돌려준다.
     * @return
     */
	public static String getYear(){
	    String ym = getYearMonth();
	    
	    return ym.substring(0,4);
	} 

	/**
	 * 현재 달을 돌려준다.
	 * @return
	 */
	public static String getMonth(){
	    String ym = getYearMonth();
	    
	    return ym.substring(4,6);
	}
	
	/**
	 * 현재 년월을 돌려준다 - YYYYMM 
	 */
	public static String getYearMonth(){
		String month;

 		Calendar cal = Calendar.getInstance(Locale.getDefault());

 		StringBuffer buf = new StringBuffer();

 		buf.append(Integer.toString(cal.get(Calendar.YEAR)));
 		month = Integer.toString(cal.get(Calendar.MONTH)+1);
 		if(month.length() == 1) month = "0" + month;
 		
 		buf.append(month);
 		
		return buf.toString();
	}
	
	   public static String getDate()
	    {
	        Calendar cal = Calendar.getInstance(Locale.getDefault());
	        StringBuffer buf = new StringBuffer();
	        buf.append(Integer.toString(cal.get(1)));
	        String month = Integer.toString(cal.get(2) + 1);
	        if(month.length() == 1)
	            month = "0" + month;
	        String day = Integer.toString(cal.get(5));
	        if(day.length() == 1)
	            day = "0" + day;
	        buf.append(month);
	        buf.append(day);
	        return buf.toString();
	    }
	   
	   
	   public static String getAddDate( int amount )
	    {
	        Calendar cal = Calendar.getInstance(Locale.getDefault());
	        		 cal.add(Calendar.DATE, amount);
	        StringBuffer buf = new StringBuffer();
	        buf.append(Integer.toString(cal.get(1)));
	        String month = Integer.toString(cal.get(2) + 1);
	        if(month.length() == 1)
	            month = "0" + month;
	        String day = Integer.toString(cal.get(5));
	        if(day.length() == 1)
	            day = "0" + day;
	        buf.append(month);
	        buf.append(day);
	        return buf.toString();
	    }	   
       
	   /**
	    * TimeMillis값을 Date형식으로 바꾸어 준다.
	    * @param time
	    * @return
	    */
	   public static String getTimeMillisDate(long time){
		return getTimeMillisDate(time, DATE_GUBUN);
       }

	   public static String getTimeMillisDate(long time, String delimeter){
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(time);
   
			return getDate(cal, delimeter);
	   }
	   
		/**
		 * 해당 날짜의 년월일을 돌려준다. - YYYYMMDD
		 * @param cal - 해당날짜의 Calendar 객체 
		 */
		public static String getDate(Calendar cal, String delimeter){
			String month, day;

	 		StringBuffer buf = new StringBuffer();

	 		buf.append(Integer.toString(cal.get(Calendar.YEAR)));
	 		if(!delimeter.equals("")) buf.append(delimeter);
	 		month = Integer.toString(cal.get(Calendar.MONTH)+1);
	 		if(month.length() == 1) month = "0" + month;
	 		day = Integer.toString(cal.get(Calendar.DATE));
	 		if(day.length() == 1) day = "0" + day;

	 		buf.append(month);
	 		if(!delimeter.equals("")) buf.append(delimeter);
	 		buf.append(day);
	 		
			return buf.toString();
		}
		
		/**
		 * TimeMillis값을 해당 날짜의 년월일을 돌려준다. - YYYYMMDDHHMI
		 * @param time
		 * @param delimeter
		 * @return
		 */
		public static String getTimeMillisDateYYYYMMDDHHMI(long time, String delimeter){
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(time);
			
			String month, day,hour,min;
			StringBuffer buf = new StringBuffer();
			
			buf.append(Integer.toString(cal.get(Calendar.YEAR)));
	 		if(!delimeter.equals("")) buf.append(delimeter);
	 		month = Integer.toString(cal.get(Calendar.MONTH)+1);
	 		if(month.length() == 1) month = "0" + month;
	 		day = Integer.toString(cal.get(Calendar.DATE));
	 		if(day.length() == 1) day = "0" + day;
	 		hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
	 		if(hour.length() == 1) hour = "0" + hour;
	 		min = Integer.toString(cal.get(Calendar.MINUTE));
	 		if(min.length() == 1) min = "0" + min;
	 		
	 		buf.append(month);
	 		if(!delimeter.equals("")) buf.append(delimeter);
	 		buf.append(day);
	 		if(!delimeter.equals("")) buf.append(delimeter);
	 		buf.append(hour);
	 		if(!delimeter.equals("")) buf.append(delimeter);
	 		buf.append(min);
   
			return buf.toString();
	   }
		

		public static long getCurrentTimeStamp() {
			return System.currentTimeMillis();
		}
		
}
