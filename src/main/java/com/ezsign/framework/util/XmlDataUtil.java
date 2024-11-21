package com.ezsign.framework.util;

import java.text.DecimalFormat;
import java.util.regex.Pattern;


/**
 * @Class Name  : com.newzenpnp.framework.util.TokenUtil
 * @Description :
 * @Modification Information  
 * @
 * @     수정일                         수정자             수정내용
 * @ -------------     ---------   ---------------------------------
 * @  2017. 10. 10.      유지운                 최초생성
 * 
 * @Company : Newzen Pnp. Inc
 * @Author  : 유지운
 * @Date    : 2017. 11. 10. 오후 4:21:02
 * @version : 1.0
 */

import com.jarvis.common.util.StringUtil;

public class XmlDataUtil {


	public static void main(String[] args) {
		String result = "";
		try {
			result = priceToString("20171202");
			
			System.out.println(result);
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static String timeHFormat(String value) {
		String result = "";
		if (StringUtil.isNotNull(value) && value.length() == 4) {
			result = StringUtil.substring(value, 0, 2) + "시 " + StringUtil.substring(value, 2, 4) + "분";
		}
		return result;
	}

	public static String timeFormat(String value) {
		String result = "";
		if (StringUtil.isNotNull(value) && value.length() == 4) {
			result = StringUtil.substring(value, 0, 2) + ":" + StringUtil.substring(value, 2, 4);
		}
		return result;
	}

	public static String dateHFormat(String value) {
		String result = "";
		if (StringUtil.isNotNull(value) && value.length() == 8) {
			result = StringUtil.substring(value, 0, 4) + "년 " + Integer.parseInt(StringUtil.substring(value, 4, 6)) + "월 " + Integer.parseInt(StringUtil.substring(value, 6, 8)) + "일";
		}
		return result;
	}

	public static String dateGFormat(String value) {
		String result = "";
		if (StringUtil.isNotNull(value) && value.length() == 8) {
			result = StringUtil.substring(value, 0, 4) + "-" + Integer.parseInt(StringUtil.substring(value, 4, 6)) + "-" + Integer.parseInt(StringUtil.substring(value, 6, 8));
		}
		return result;
	}
	
	public static String priceWithDecimal (double price) {
	    DecimalFormat formatter = new DecimalFormat("###,###,###.00");
	    return formatter.format(price);
	}

	public static String priceWithoutDecimal (double price) {
	    DecimalFormat formatter = new DecimalFormat("###,###,###.##");
	    return formatter.format(price);
	}

	public static String priceToString(String value) {
		if(value.length()==0||value.equals("0")||value.equals("0.0")) return "";
		double price = Double.parseDouble(value);
	    String toShow = priceWithoutDecimal(price);
	    if (toShow.indexOf(".") > 0) {
	        return priceWithDecimal(price);
	    } else {
	        return priceWithoutDecimal(price);
	    }
	}

	public static String monthType(String value) {
		String result = "";
		if(value.equals("1")) {
			result = "월급";
		} else if(value.equals("2")) {
			result = "일급";
		} else if(value.equals("3")) {
			result = "시급";
		}
		return result;
	}
	
	public static String payType(String value) {
		String result = "";
		if(value.equals("1")) {
			result = "매월";
		} else if(value.equals("2")) {
			result = "매주";
		} else if(value.equals("3")) {
			result = "매일";
		}
		return result;
	}
	
	public static String hourFormat(String value) {
		String result = "";
		if (StringUtil.isNotNull(value) && value.length() >0 && value!="0.0") {
			result = value + "시간";
		}
		return result;
	}
	
	public static String checkType(String value) {
		String result = "";
		if(value.equals("1")) {
			result = "예";
		}
		return result;		
	}
	
	public static String telphoneFormat(String num, String mark) {
	    String formatNum = "";
	    
	    if(num.length()==11){
	        if(mark.equals("Y")){
	            formatNum = num.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-****-$3");
	        }else{
	            formatNum = num.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
	        }
	    }else if(num.length()==8){
	        formatNum = num.replaceAll("(\\d{4})(\\d{4})", "$1-$2");
	    }else{
	        if(num.indexOf("02")==0){
	        	if(mark.equals("Y")){
	                formatNum = num.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "$1-****-$3");
	            }else{
	                formatNum = num.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "$1-$2-$3");
	            }
	        }else{
	        	if(mark.equals("Y")){
	                formatNum = num.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1-***-$3");
	            }else{
	                formatNum = num.replaceAll("(\\d{3})(\\d{3})(\\d{4})", "$1-$2-$3");
	            }
	        }
	    }
	    return formatNum;
		
	}
	
}
