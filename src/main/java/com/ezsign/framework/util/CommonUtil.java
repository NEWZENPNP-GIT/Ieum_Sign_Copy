/*
 * Copyright yysvip.tistory.com.,LTD.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of yysvip.tistory.com.,LTD. ("Confidential Information").
 */
package com.ezsign.framework.util; 

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * <pre>
 * com.ezsign.framework.util 
 *    |_ CommonUtil.java
 * 
 * </pre>
 * @date : 2019. 1. 22. 오후 3:18:12
 * @version : 
 * @author : soybean0627
 */
public class CommonUtil {

	/**
	 * 계약년도 기준 나이 계산
	 * 
	 * @param 계약년도
	 * @param 개인식별번호
	 * @return
	 */
	public static int getContractAge(int 계약년도, String 개인식별번호){
		
		
		 int 나이 = 0;
         
         switch (개인식별번호.substring(6, 7)) {
             case "1":
             case "2":
             case "5":
             case "6":
             	나이 = 계약년도 - StringUtil.strPaserInt("19" + 개인식별번호.substring(0, 2));
                 break;
             case "3":
             case "4":
             case "7":
             case "8":
             	나이 = 계약년도 - StringUtil.strPaserInt("20" + 개인식별번호.substring(0, 2));
                 break;
             case "9":
             case "0":
             	나이 = 계약년도 - StringUtil.strPaserInt("18" + 개인식별번호.substring(0, 2));
                 break;
         }

         return 나이;
                  
	}

	/**
	 *
	 * 생년월일을 기준으로 현재 나이 계산 
	 *
	 * @param birthYear
	 * @param birthMonth
	 * @param birthDay
	 * @return
	 */
	public static int getAge(int birthYear, int birthMonth, int birthDay){
		Calendar current = Calendar.getInstance();
		int currentYear = current.get(Calendar.YEAR);
		int currentMonth = current.get(Calendar.MONTH) + 1;
		int currentDay = current.get(Calendar.DAY_OF_MONTH);

		int age = currentYear - birthYear;
		
		//생일 안 지난 경우 -1
		if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay){
			age--;
		}

		return age;
	}

}
