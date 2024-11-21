package com.ezsign.framework.hometax;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ezsign.framework.util.StringUtil;

public class RecordProcUtil {

	private static Logger logger = Logger.getLogger(RecordProcUtil.class);
	
	
	/**
	 * 클래스 필드를 파싱해서 String으로 리턴한다. 
	 * 
	 * @param recordType
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String classParsing(String recordType, Object obj) throws Exception{
		
		String result = "";
		
		logger.debug("# " +recordType+ " : obj => " + obj);
		
		Class<?> cls = obj.getClass();							
		Field[] fields = cls.getDeclaredFields();

		for(Field field : fields){
			
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
			
			//필드명이 타입하고 같을때
			if(field.getName().indexOf(recordType) > -1){			
				result += field.get(obj);
			}			
			field.setAccessible(accessible);
			
		}
		
		logger.debug("# result : " + result );
		logger.debug("# result.getBytes().length : " + result.getBytes("KSC5601").length);
		
		return result;
	}
	
	
	/**
	 * 해당 문자열을 길이만큼 공백채우기
	 * 
	 * @param 타입
	 * @param 길이
	 * @param 기본값
	 * @return
	 */
	public static String ipgFitString(String 타입, String 길이, String 기본값) throws Exception {
		
		String result = "";		
		기본값 = StringUtil.nvl(기본값).trim();
		
		//숫자 (오른쪽 정렬)
		if(StringUtils.equals("9", 타입)){
			result = ipgFitString(기본값, StringUtil.strPaserInt(길이), "number", "0");
		}
		//문자 (왼쪽정렬)
		else {
			result = ipgFitString(기본값, StringUtil.strPaserInt(길이), "string", "");
		}
	
		return result;
	}
			
	public static String ipgFitString(String value, int size, String type, String fit) throws Exception {
		
		String resultMsg = "";
						
		if(StringUtil.nvl(value,"").getBytes("KSC5601").length > 0){
			
			if(StringUtils.equals("string",type)){
				
				//전문 자리수보다 내용이 큰지 비교한다.
				if(size < StringUtil.nvl(value,"").getBytes("KSC5601").length){
					
					value = lengthLimit(StringUtil.nvl(value,""), size);
					resultMsg += new String(StringUtil.nvl(value,"").getBytes("KSC5601"),"KSC5601");
					
					if(size - StringUtil.nvl(value,"").getBytes("KSC5601").length > 0){
						for(int j = 0 ; j < size - StringUtil.nvl(value,"").getBytes("KSC5601").length ; j++){
							resultMsg += " ";
						}
					}
					
				}else{
					
					resultMsg += new String(StringUtil.nvl(value,"").getBytes("KSC5601"),"KSC5601");
					
					if(size - StringUtil.nvl(value,"").getBytes("KSC5601").length > 0){
						for(int j = 0 ; j < size - StringUtil.nvl(value,"").getBytes("KSC5601").length ; j++){
							resultMsg += " ";
						}
					}
				}
				
			}else if(StringUtils.equals("number",type)){
				
				if(size - StringUtil.nvl(value).length() > 0){
					for(int j = 0 ; j < size - StringUtil.nvl(value).length() ; j++){
						resultMsg += "0";
					}
				}
				resultMsg += StringUtil.nvl(value);
			}
			
			
		}else{
			
			if(StringUtils.equals("string",type)){
				for(int j = 0 ; j < size ; j++){
					resultMsg += " ";
				}
			}else if(StringUtils.equals("number",type)){
				for(int j = 0 ; j < size ; j++){
					resultMsg += "0";
				}
			}
			
		}
		
		
		return resultMsg;
				
	}
		
	/**
	 * String 값을 최대길이만큼 자른다.
	 * 
	 * @param inputStr  : 자를 문자열
	 * @param limit		: 최대길이
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String lengthLimit(String inputStr, int limit) {
        
		String result = "";
		
		try{
			
			if (StringUtils.isEmpty(inputStr)){
	            return "";
			}
	        if (limit <= 0){
	            return inputStr;
	        }
	        
	        byte[] strbyte = null;
	        strbyte = inputStr.getBytes("KSC5601");
	
	        if (strbyte.length <= limit) {
	            return inputStr;
	        }
	        
	        char[] charArray = inputStr.toCharArray();
	        int checkLimit = limit;
	        for ( int i = 0 ; i < charArray.length ; i++ ) {
	            if (charArray[i] < 256) {
	                checkLimit -= 1;
	            }
	            else {
	                checkLimit -= 2;
	            }
	            if (checkLimit <= 0) {
	                break;
	            }
	        }
	        //대상 문자열 마지막 자리가 2바이트의 중간일 경우 제거함
	        byte[] newByte = new byte[limit + checkLimit];
	        for ( int i = 0 ; i < newByte.length ; i++ ) {
	            newByte[i] = strbyte[i];
	        }
	        
	        result = new String(newByte,"EUC-KR");
	        
		}catch(UnsupportedEncodingException ex){
			logger.error(ex.getMessage(), ex);
		}
		
        return result;        
    }
	
	
	/**
	 * 계약면적 자릿수 처리
	 * 
	 * @param 계약면적
	 * @return
	 */
	public static String strPad(String 계약면적){		
		String result = "";
		
		if(StringUtils.isNotEmpty(계약면적)){
			if(계약면적.indexOf(".") > -1){
				String first = 계약면적.substring(0,계약면적.indexOf("."));
				String last = 계약면적.substring(계약면적.indexOf(".")+1);
						
				first = String.format("%03d", Integer.parseInt(first)); 
				last =  lPad(last,2,'0');
			
				result = first + last;
			}else{
				String first = String.format("%03d", Integer.parseInt(계약면적));
				String last = lPad("0",2,'0');
					
				result = first + last;
			}
		}else{
			result = "00000";
		}					

		return result;
	}
	
	public static String lPad(String str, Integer length, char car) {
		return (str + String.format("%" + length + "s", "").replace(" ", String.valueOf(car))).substring(0, length);
	}
	
}
