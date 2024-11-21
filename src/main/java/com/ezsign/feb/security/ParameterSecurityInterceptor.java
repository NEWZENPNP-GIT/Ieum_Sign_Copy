package com.ezsign.feb.security;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

public class ParameterSecurityInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ParameterSecurityInterceptor.class);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		boolean isCorrect = true;

		printAllParameters(request);

		SessionVO loginVO = SessionUtil.getSession(request);
		if (loginVO == null) {
			isCorrect = false;
		} else if (Integer.parseInt(loginVO.getUserType()) < 5) {

			String sessionUserId = loginVO.getUserId();
			String 사용자ID = request.getParameter("사용자ID");
			String userId = request.getParameter("userId");
			String userID = request.getParameter("userID");
			String userid = request.getParameter("userid");

			System.out.println("loginVO.getUserType(): " + loginVO.getUserType());
			System.out.println("사용자ID: " + 사용자ID);
			System.out.println("userId: " + userId);
			System.out.println("userID: " + userID);
			System.out.println("userid: " + userid);
			
			if(사용자ID != null && !StringUtils.equals("undefined", 사용자ID)){
				if (!StringUtils.equals(sessionUserId, 사용자ID)) {
					isCorrect = false;
				}
			}
			if(userId != null && !StringUtils.equals("undefined", userId)){
				if ( !StringUtils.equals(sessionUserId, userId)) {
					isCorrect = false;
				}
			}
			if(userID != null && !StringUtils.equals("undefined", userID)){
				if ( !StringUtils.equals(sessionUserId, userID)) {
					isCorrect = false;
				}
			}
			if(userid != null && !StringUtils.equals("undefined", userid)){
				if ( !StringUtils.equals(sessionUserId, userid)) {
					isCorrect = false;
				}
			}
		}

		if (isCorrect) {
			return true;
		} else {
			SessionUtil.Logout(request);
			//throw new ModelAndViewDefiningException(new ModelAndView("redirect:/"));
			response.sendRedirect("/error/401error.jsp");
            return false;
		}
	}

	private void printAllParameters(HttpServletRequest request) {
//		System.out.println("=======================================================");
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {

			String paramName = parameterNames.nextElement();

//			System.out.print(paramName + ": ");
			String[] paramValues = request.getParameterValues(paramName);

			for (int i = 0; i < paramValues.length; i++) {
				String paramValue = paramValues[i];
//				System.out.print(paramValue);
			}
//			System.out.println("");
		}
//		System.out.println("=======================================================");
	}
}
