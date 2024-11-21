package com.ezsign.framework.hometax;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ezsign.feb.hometax.vo.PaymentARecordVO;
import com.ezsign.feb.hometax.vo.PaymentBRecordVO;
import com.ezsign.feb.hometax.vo.PaymentCRecordVO;
import com.ezsign.feb.hometax.vo.YC200VO;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;

public class PaymentRecordVOUtil {

	private static PaymentRecordVOUtil instance = null;
	
	public static PaymentRecordVOUtil getinstance(){
    	
    	if(instance == null){
    		instance = new PaymentRecordVOUtil();
    	}
    	
    	return instance;
	}
	
	/**
	 * A 레코드 정보를 셋팅한다.
	 * 
	 * @param aRecordFieldList
	 * @param aRecordInfo
	 * @param 제출년월일
	 * @param b레코드수
	 * @throws Exception
	 */
	public void setARecordInfo(List<YC200VO> aRecordFieldList, PaymentARecordVO aRecordInfo, String 제출년월일,int b레코드수) throws Exception{
		
		Class<?> cls = aRecordInfo.getClass();
		
		/*
		 * X : 문자  (좌측부터 입력하고 빈공간은 공백으로)
		 * 9 : 숫자  (우측부터 입력하고 빈공간은 '0'으로)
		 */		
		for(YC200VO rstVO : aRecordFieldList){
				
			String filedValue = "";
			Field filed = cls.getDeclaredField(rstVO.get번호());
			boolean accessible = filed.isAccessible();
			filed.setAccessible(true);
				
			if(StringUtils.isNotEmpty(rstVO.get기본값())){				
				filedValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl(rstVO.get기본값()));	
			}else{
				
				//제출연월일
				if(StringUtils.equals(filed.getName(), "A4")){
					filedValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), 제출년월일 );
				}
				//신고의무자수 (B 레코드 수)
				else if(StringUtils.equals(filed.getName(), "A14")){
					filedValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), String.valueOf(b레코드수));
				}
				
				else{
					filedValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl((String)filed.get(aRecordInfo)));
				}
									
			}

			filed.set(aRecordInfo, filedValue);
			filed.setAccessible(accessible);
				
		}
		
	}
	
	/**
	 * B 레코드 정보를 셋팅한다.
	 * 
	 * @param bRecordFieldList
	 * @param bRecordInfo
	 * @param 일련번호
	 * @param 제출대상구분코드
	 * @throws Exception
	 */
	public void setBRecordInfo(List<YC200VO> bRecordFieldList, PaymentBRecordVO bRecordInfo, int 일련번호, String 제출대상구분코드) throws Exception{
		
		Class<?> cls = bRecordInfo.getClass();
		
		for(YC200VO rstVO : bRecordFieldList){
			
			String fieldValue = "";
			Field field = cls.getDeclaredField(rstVO.get번호());
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
				
			if(StringUtils.isNotEmpty(rstVO.get기본값())){				
				fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl(rstVO.get기본값()));	
			}else{
				
				//일련번호 (B레코드 일련번호 - 1부터 순차부여)
				if(StringUtils.equals(field.getName(), "B4")){
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(),String.valueOf(일련번호));
				}				
				else{																		
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl((String)field.get(bRecordInfo)));
				}
									
			}

			field.set(bRecordInfo, fieldValue);
			field.setAccessible(accessible);
			
		}
		
	}
	
	/**
	 * C 레코드 정보를 셋팅한다.
	 * 
	 * @param cRecordFieldList
	 * @param cRecordInfo
	 * @param 일련번호
	 * @throws Exception
	 */
	public void setCRecordInfo(List<YC200VO> cRecordFieldList, PaymentCRecordVO cRecordInfo, int 일련번호) throws Exception{
		
		Class<?> cls = cRecordInfo.getClass();
		
		for(YC200VO rstVO : cRecordFieldList){
			
			String fieldValue = "";								
			Field field = cls.getDeclaredField(rstVO.get번호());
			
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
				
			if(StringUtils.isNotEmpty(rstVO.get기본값())){				
				fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl(rstVO.get기본값()));	
			}else{
				
				//일련번호 (원천징수의무자별로 1부터 순차 부여)
				if(StringUtils.equals(field.getName(), "C4")){
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(),String.valueOf(일련번호));
				}
				//개인식별번호를 복호화한다.
				else if(StringUtils.equals(field.getName(), "C6")){
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), SecurityUtil.getinstance().aesDecode((String)field.get(cRecordInfo)));
				}
				else{																		
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl((String)field.get(cRecordInfo)));
				}
			}
			
			field.set(cRecordInfo, fieldValue);
			field.setAccessible(accessible);
		}
		
	}
	
}
