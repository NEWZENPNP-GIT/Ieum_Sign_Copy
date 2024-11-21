package com.ezsign.framework.util;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ezsign.framework.vo.SessionVO;

@SuppressWarnings("serial")
public class SessionUtil  implements Serializable {

	/** 사용자 정보 세션 명 */
	public static final String SESSION_NAME = "USERINFO";

	/**
	 * 사용자 정보를 파라미터로 받아 세션 명과 같이 Login 함수에 넘겨 준다. 
	 *
	 * @param request HttpServletRequest
	 * @param user 로그인한 사용자 정보가 담긴 bean 객체
	 */
	public static void Login(HttpServletRequest request, SessionVO user) {
		Login(request, user, SESSION_NAME);
	}

	/**
	 * 파라미터로 사용자 정보와 세션 명을 받아 HttpServletRequest에 세션을 등록하는 함수이다.
	 *
	 * @param request HttpServletRequest
	 * @param user 사용자 정보가 담긴 bean 객체
	 * @param sessionName 세션 명
	 */
	public static void Login(HttpServletRequest request, SessionVO user, String sessionName) {
		HttpSession session = request.getSession();
		session.removeAttribute(sessionName);
		
		if(user != null && user.getExpire() != 0 ){
			session.setMaxInactiveInterval(user.getExpire() * 60 * 60);
		} else {
			session.setMaxInactiveInterval(60 * 60 * 2);
		}
		session.setAttribute(sessionName, user);
	}

	/**
	 * 세션에 등록 되어 있는 사용자 정보를 삭제 한다.
	 *
	 * @param request HttpServletRequest
	 */
	public static void Logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session!=null)
			session.invalidate();
	}

	/**
	 * 세션에서 사용자 정보를 구해서 리턴 해준다.
	 *
	 * @param request HttpServletRequest
	 * @return 사용자 정보가 있을 경우 정보가 담긴 bean 객체를 리턴하고 없을 경우에는 null을 리턴 한다.
	 */
	public static SessionVO getSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		SessionVO user = new SessionVO();
		if (session != null) {
			user = (SessionVO) session.getAttribute(SESSION_NAME);
			return user;
		} else {
			return null;
		}
	}

	/**
	 * 세션에서 사용자 정보를 구해서 리턴 해준다.
	 *
	 * @param request HttpServletRequest
	 * @param sessionName 세션 명
	 * @return 사용자 정보가 있을 경우 정보가 담긴 bean 객체를 리턴하고 없을 경우에는 null을 리턴 한다.
	 */
	public static SessionVO getSession(HttpServletRequest request, String sessionName) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			SessionVO user = (SessionVO) session.getAttribute(sessionName);
			return user;
		}else{
			return null;
		}
	}

}
