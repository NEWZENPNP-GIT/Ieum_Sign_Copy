/*
 * Copyright yysvip.tistory.com.,LTD.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of yysvip.tistory.com.,LTD. ("Confidential Information").
 */
package com.ezsign.framework.exception; 

import org.apache.log4j.Logger;

/**
 * <pre>
 * com.ezsign.framework.exception 
 *    |_ AngullarProcessException.java
 * 
 * </pre>
 * @date : 2019. 1. 9. 오전 11:45:04
 * @version : 
 * @author : soybean0627
 */
public class AngullarProcessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8608875043571014080L;
	
	public static final int DEFAULT_ERROR_CODE = 500;
		
	private int errorCode = DEFAULT_ERROR_CODE;
    private String errorMsg;
    
    
	/**
     * 기본 생성자이다.
     */
    public AngullarProcessException() {
    }
    
    
    /**
     * 메시지를 가지는 생성자이다.
     * 
     * @param message 메시지
     */
    public AngullarProcessException(String message) {
        this(message, null);
    }
    
    /**
     * 메시지와 원천(cause) 예외를 가지는 생성자이다.
     * 
     * @param message 메시지
     * @param cause 원천 예외
     */
    public AngullarProcessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 메시지와 에러코드, 에러메시지를 가지는 생성자이다.
     * 
     * @param message 메시지
     * @param errorCode 에러코드
     * @param errorMsg 에러메시지
     */
    public AngullarProcessException(String message, int errorCode, String errorMsg) {
        this(message, null, errorCode, errorMsg);
    }
    
    /**
     * 메시지와 원천(cause), 에러코드, 에러메시지 예외를 가지는 생성자이다.
     * 
     * @param message 메시지
     * @param cause 원천 예외
     * @param errorCode 에러코드
     * @param errorMsg 에러메시지
     */
    public AngullarProcessException(String message, Throwable cause, int errorCode, String errorMsg) {
        super(message, cause);
        setErrorCode(errorCode);
        setErrorMsg(errorMsg);
    }
    
    /**
     * 설정 된 에러코드를 반환한다.
     * @return errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * 에러코드를 설정한다.
     * 
     * @param errorCode 에러코드
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 설정 된 에러메시지를 반환한다.
     * @return errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 에러메시지를 설정한다.
     * @param errorMsg 에러메시지
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    
}
