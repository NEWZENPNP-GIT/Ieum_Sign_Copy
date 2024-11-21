package com.ezsign.common.service;

import javax.servlet.http.HttpServletRequest;

import com.ezsign.framework.vo.CookieVO;
import com.ezsign.framework.vo.SessionVO;

public interface CookieService {

	SessionVO getCookieSession(HttpServletRequest request, CookieVO vo) throws Exception;
}
