package com.ezsign.framework.web.servlet;

/**
 * @Class Name  : com.nexcos.framework.web.servlet.ExDispatcherServlet
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

public class ExDispatcherServlet extends DispatcherServlet {

	private static final long serialVersionUID = -7470111116448030830L;
	
	/** 
	 * @Description :
	 * @param request
	 * @param response
	 * @throws Exception
	 *
	 * @see org.springframework.web.servlet.DispatcherServlet#doDispatch(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {        
		super.doDispatch(request, response);
	}
}
