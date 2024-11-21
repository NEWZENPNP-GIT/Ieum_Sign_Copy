package com.ezsign.framework.hometax;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ezsign.feb.hometax.vo.ARecordVO;
import com.ezsign.feb.hometax.vo.BRecordVO;
import com.ezsign.feb.hometax.vo.CARecordVO;
import com.ezsign.feb.hometax.vo.CRecordVO;
import com.ezsign.feb.hometax.vo.DRecordVO;
import com.ezsign.feb.hometax.vo.ERecordVO;
import com.ezsign.feb.hometax.vo.FRecordVO;
import com.ezsign.feb.hometax.vo.GRecordVO;
import com.ezsign.feb.hometax.vo.HRecordVO;
import com.ezsign.feb.hometax.vo.IRecordVO;
import com.ezsign.feb.hometax.vo.YC200VO;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;

public class RecordVOUtil {

	private static RecordVOUtil instance = null;
	
	public static RecordVOUtil getinstance(){
    	
    	if(instance == null){
    		instance = new RecordVOUtil();
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
	public void setARecordInfo(List<YC200VO> aRecordFieldList, ARecordVO aRecordInfo, String 제출년월일,int b레코드수) throws Exception{
		
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
	public void setBRecordInfo(List<YC200VO> bRecordFieldList, BRecordVO bRecordInfo, int 일련번호, String 제출대상구분코드) throws Exception{
		
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
				//제출대상기간 코드 (1:연간 합산제출, 2:휴.폐업에 의한 수시제출, 3:수시 분할제출)
				else if(StringUtils.equals(field.getName(), "B16")){
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), 제출대상구분코드);
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
	public void setCRecordInfo(List<YC200VO> cRecordFieldList, CRecordVO cRecordInfo, int 일련번호) throws Exception{
		
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
				else if(StringUtils.equals(field.getName(), "C13")){
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
	
	
	/**
	 * D 레코드 정보를 셋팅한다.
	 * 
	 * @param dRecordFieldList
	 * @param dRecordInfo
	 * @param c레코드일련번호
	 * @param d레코드일련번호
	 * @throws Exception
	 */
	public void setDRecordInfo(List<YC200VO> dRecordFieldList, DRecordVO dRecordInfo, int c레코드일련번호, int d레코드일련번호) throws Exception{
		
		Class<?> cls = dRecordInfo.getClass();
		
		for(YC200VO rstVO : dRecordFieldList){
			
			String fieldValue = "";								
			Field field = cls.getDeclaredField(rstVO.get번호());
			
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
				
			if(StringUtils.isNotEmpty(rstVO.get기본값())){				
				fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl(rstVO.get기본값()));	
			}else{
				
				//일련번호 ('C' 레코드의 일련번호와 동일하게 수록)
				if(StringUtils.equals(field.getName(), "D4")){
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(),String.valueOf(c레코드일련번호));
				}
				//개인식별번호를 복호화한다. (소득자주민등록번호)
				else if(StringUtils.equals(field.getName(), "D6")){
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), SecurityUtil.getinstance().aesDecode((String)field.get(dRecordInfo)));
				}
				//종(전)근무처 일련번호 (C 레코드의 소득자별로 1부터 순차적 부여)
				else if(StringUtils.equals(field.getName(), "D60")){
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(),String.valueOf(d레코드일련번호));
				}
				else{																		
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl((String)field.get(dRecordInfo)));
				}
			}
			
			field.set(dRecordInfo, fieldValue);
			field.setAccessible(accessible);
		}
		
	}
	
	
	/**
	 * E 레코드 정보를 셋팅한다.
	 *  
	 * @param eRecordFieldList
	 * @param eRecordInfo
	 * @param 일련번호
	 * @throws Exception
	 */
	public void setERecordInfo(List<YC200VO> eRecordFieldList, ERecordVO eRecordInfo, int 일련번호) throws Exception{
		
		Class<?> cls = eRecordInfo.getClass();
		
		for(YC200VO rstVO : eRecordFieldList){
			
			String fieldValue = "";								
			Field field = cls.getDeclaredField(rstVO.get번호());
			
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
				
			if(StringUtils.isNotEmpty(rstVO.get기본값())){				
				fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl(rstVO.get기본값()));	
			}else{
				
				//일련번호 (원천징수의무자별로 1부터 순차 부여)
				if(StringUtils.equals(field.getName(), "E222")){
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(),String.valueOf(일련번호));
				}
				else{																		
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl((String)field.get(eRecordInfo)));
				}
			}
			
			field.set(eRecordInfo, fieldValue);
			field.setAccessible(accessible);
		}
		
	}
	

	/**
	 * F 레코드 정보를 셋팅한다.
	 * 
	 * @param fRecordFieldList
	 * @param fRecordInfo
	 * @param 일련번호
	 * @throws Exception
	 */
	public void setFRecordInfo(List<YC200VO> fRecordFieldList, FRecordVO fRecordInfo, int 일련번호) throws Exception{
		
		Class<?> cls = fRecordInfo.getClass();
		
		for(YC200VO rstVO : fRecordFieldList){
			
			String fieldValue = "";								
			Field field = cls.getDeclaredField(rstVO.get번호());
			
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
				
			if(StringUtils.isNotEmpty(rstVO.get기본값())){				
				fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl(rstVO.get기본값()));	
			}else{
				
				//일련번호 (원천징수의무자별로 1부터 순차 부여)
				if(StringUtils.equals(field.getName(), "F127")){
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(),String.valueOf(일련번호));
				}
				else{							
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl((String)field.get(fRecordInfo),""));
				}
			}
			
			field.set(fRecordInfo, fieldValue);
			field.setAccessible(accessible);
		}
		
	}
	
	
	/**
	 * G 레코드 정보를 셋팅한다.
	 * 
	 * @param gRecordFieldList
	 * @param gRecordInfo
	 * @param 일련번호
	 * @throws Exception
	 */
	public void setGRecordInfo(List<YC200VO> gRecordFieldList, GRecordVO gRecordInfo, int 일련번호) throws Exception{
		
		Class<?> cls = gRecordInfo.getClass();
		
		for(YC200VO rstVO : gRecordFieldList){
			
			String fieldValue = "";								
			Field field = cls.getDeclaredField(rstVO.get번호());
			
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
				
			if(StringUtils.isNotEmpty(rstVO.get기본값())){				
				fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl(rstVO.get기본값()));	
			}else{
				
				//일련번호 (원천징수의무자별로 1부터 순차 부여)
				if(StringUtils.equals(field.getName(), "G85")){
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(),String.valueOf(일련번호));
				}
				else{																		
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl((String)field.get(gRecordInfo)));
				}
			}
			
			field.set(gRecordInfo, fieldValue);
			field.setAccessible(accessible);
		}
		
	}
	
	
	/**
	 * H 레코드 정보를 셋팅한다.
	 * 
	 * @param hRecordFieldList
	 * @param hRecordInfo
	 * @throws Exception
	 */
	public void setHRecordInfo(List<YC200VO> hRecordFieldList, HRecordVO hRecordInfo) throws Exception{
		
		Class<?> cls = hRecordInfo.getClass();
		
		for(YC200VO rstVO : hRecordFieldList){
			
			String fieldValue = "";								
			Field field = cls.getDeclaredField(rstVO.get번호());
			
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
				
			if(StringUtils.isNotEmpty(rstVO.get기본값())){				
				fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl(rstVO.get기본값()));	
			}else{																					
				fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl((String)field.get(hRecordInfo)));				
			}
			
			field.set(hRecordInfo, fieldValue);
			field.setAccessible(accessible);
		}
		
	}
	

	/**
	 * I 레코드 정보를 셋팅한다.
	 * 
	 * @param iRecordFieldList
	 * @param iRecordInfo
	 * @throws Exception
	 */
	public void setIRecordInfo(List<YC200VO> iRecordFieldList, IRecordVO iRecordInfo) throws Exception{
		
		Class<?> cls = iRecordInfo.getClass();
		
		for(YC200VO rstVO : iRecordFieldList){
			
			String fieldValue = "";								
			Field field = cls.getDeclaredField(rstVO.get번호());
			
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
				
			if(StringUtils.isNotEmpty(rstVO.get기본값())){				
				fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl(rstVO.get기본값()));	
			}
			//개인식별번호
			else if(StringUtils.equals(field.getName(), "I14")){
				String 개인식별번호 = SecurityUtil.getinstance().aesDecode((String)field.get(iRecordInfo));				
				fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), 개인식별번호);
			}			
			else{																					
				fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl((String)field.get(iRecordInfo)));				
			}
			
			field.set(iRecordInfo, fieldValue);
			field.setAccessible(accessible);
		}
		
	}
	
	
	/**
	 * CA 레코드 정보를 셋팅한다.
	 * 
	 * @param caRecordFieldList
	 * @param caRecordInfo
	 * @throws Exception
	 */
	public void setCARecordInfo(List<YC200VO> caRecordFieldList, CARecordVO caRecordInfo) throws Exception{
		
		Class<?> cls = caRecordInfo.getClass();
		
		for(YC200VO rstVO : caRecordFieldList){
			
			String fieldValue = "";								
			Field field = cls.getDeclaredField(rstVO.get번호());
			
			boolean accessible = field.isAccessible();
			field.setAccessible(true);
				
			if(StringUtils.isNotEmpty(rstVO.get기본값())){				
				fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl(rstVO.get기본값()));	
			}else{
				
				//소득자주민등록번호
				if(StringUtils.equals(field.getName(), "CA11")){
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), SecurityUtil.getinstance().aesDecode((String)field.get(caRecordInfo)));
				}
				//공제대상 주민등록번호
				else if(StringUtils.equals(field.getName(), "CA20")){
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), SecurityUtil.getinstance().aesDecode((String)field.get(caRecordInfo)));
				}				
				else{																		
					fieldValue = RecordProcUtil.ipgFitString(rstVO.get타입(), rstVO.get길이(), StringUtil.nvl((String)field.get(caRecordInfo)));
				}
			}
			
			field.set(caRecordInfo, fieldValue);
			field.setAccessible(accessible);
		}
		
	}
	
}
