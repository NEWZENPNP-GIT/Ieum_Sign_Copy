package com.ezsign.framework.validation;

/**
 * @Class Name  : com.nexcos.framework.validation.BeforeDefaultValidatorFactory
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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class BeforeDefaultValidatorFactory {
	private Level originalLevel;
	
	/**
	 * Description : Constructorof BeforeDefaultValidatorFactory.java class
	 * 로그 레벨이 Debug일 때 Validation rule을 전체 출력하는 문제를 해결하기 위해 임시로 로그 레벨을 Info로 변경 	
	 */
	public BeforeDefaultValidatorFactory() {
		originalLevel = Logger.getRootLogger().getLevel();
		
		Logger.getRootLogger().setLevel(Level.INFO);
		
		Logger logger = Logger.getLogger(this.getClass());
		logger.info("set log level to INFO");
	}
	
	/**
	 * Description  : 로그 레벨 변경 전의 원 로그 레벨을 리턴한다.
	 * @Method Name : getOriginalLevel
	 * @return 로그 레벨 변경 전의 원 로그 레벨
	 */
	public Level getOriginalLevel() {
		return this.originalLevel;
	}
}
