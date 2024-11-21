package com.ezsign.framework.logger;

/**
 * @Class Name  : com.nexcos.framework.logger.LogService
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
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@SuppressWarnings("deprecation")
@Service("logService")
public class LogService {
	private Logger logger = Logger.getLogger(this.getClass());
	
	 public void debug(String message) {
		 logger.log(LogService.class.getCanonicalName(), Level.DEBUG, message, null);
	 }
	 
	 public void info(String message) {
		 logger.log(LogService.class.getCanonicalName(), Level.INFO, message, null);
	 }
	 
	 public void warn(String message) {		 
		 logger.log(LogService.class.getCanonicalName(), Level.WARN, message, null);
	 }
	 
	 public void error(String message,Throwable throwable) {
		 logger.log(LogService.class.getCanonicalName(), Level.ERROR, message, throwable);
	 }
	 
	 public void fatal(String message,Throwable throwable) {		 
		 logger.log(LogService.class.getCanonicalName(), Level.FATAL, message, throwable);
	 }
}
