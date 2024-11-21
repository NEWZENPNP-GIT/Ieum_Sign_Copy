package com.ezsign.framework.validation;

/**
 * @Class Name  : com.nexcos.framework.validation.AfterDefaultValidatorFactory
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

import org.apache.log4j.Logger;


public class AfterDefaultValidatorFactory {
	/**
	 * Description  : BeforeDefaultValidatorFactory를 설정하여 원 로그 레벨로 되돌린다.
	 * @Method Name : setBeforeDefaultValidatorFactory
	 * @param beforeDefaultValidatorFactory
	 */
	public void setBeforeDefaultValidatorFactory(BeforeDefaultValidatorFactory beforeDefaultValidatorFactory) {
		Logger.getRootLogger().setLevel(beforeDefaultValidatorFactory.getOriginalLevel());
		
		Logger logger = Logger.getLogger(this.getClass());
		logger.info("set log level to " + beforeDefaultValidatorFactory.getOriginalLevel());
	}
}
