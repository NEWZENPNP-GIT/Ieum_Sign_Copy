package com.ezsign.user.service;

import com.ezsign.biz.vo.BizVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.user.vo.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface UserSnsService {

	String KAKAO_CLIENT_ID = System.getProperty("kakao.client.id");
	String KAKAO_CLIENT_SECRET = System.getProperty("kakao.client.secret");
	String KAKAO_REDIRECT_URI = System.getProperty("kakao.redirect.url");
	String KAKAO_LOGIN_REDIRECT_URI = System.getProperty("kakao.login.redirect.url");

	String NAVER_CLIENT_ID = System.getProperty("naver.client.id");
	String NAVER_CLIENT_SECRET = System.getProperty("naver.client.secret");
	String NAVER_REDIRECT_URI = System.getProperty("naver.redirect.url");
	String NAVER_LOGIN_REDIRECT_URI = System.getProperty("naver.login.redirect.url");

	String GOOGLE_CLIENT_ID = System.getProperty("google.client.id");
	String GOOGLE_CLIENT_SECRET = System.getProperty("google.client.secret");
	String GOOGLE_REDIRECT_URI = System.getProperty("google.redirect.url");
	String GOOGLE_LOGIN_REDIRECT_URI = System.getProperty("google.login.redirect.url");
	
	final String REQUEST_KAKAO_URL_TOKEN = "https://kauth.kakao.com/oauth/token";
	final String REQUEST_KAKAO_URL_USER_INFO = "https://kapi.kakao.com/v2/user/me";
	final String REQUEST_KAKAO_URL_UNLINK = "https://kapi.kakao.com/v1/user/unlink";

	final String REQUEST_NAVER_URL_TOKEN = "https://nid.naver.com/oauth2.0/token";
	final String REQUEST_NAVER_URL_USER_INFO = "https://openapi.naver.com/v1/nid/me";

	final String REQUEST_GOOGLE_URL_TOKEN = "https://oauth2.googleapis.com/token";
	final String REQUEST_GOOGLE_URL_USER_INFO = "https://www.googleapis.com/oauth2/v3/userinfo";
	final String REQUEST_GOOGLE_URL_UNLINK = "https://oauth2.googleapis.com/revoke";

	UserSnsVO getKakaoAccessToken(String code, String mode);
	
	UserSnsVO getKakaoUserInfo(String accessToken);

	UserSnsVO updKakaoAccessToken(String refreshToken);

	UserSnsVO delKakaoLogin(UserSnsVO vo);

	
	UserSnsVO getNaverAccessToken(String status, String code);
	
	UserSnsVO getNaverUserInfo(String accessToken);
	
	UserSnsVO updNaverAccessToken(String refreshToken);
	
	UserSnsVO delNaverLogin(UserSnsVO vo);

	
	UserSnsVO getGoogleAccessToken(String code, String mode);

	UserSnsVO getGoogleUserInfo(String code, HttpServletRequest request);

	UserSnsVO updGoogleAccessToken(String refreshToken);
	
	UserSnsVO delGoogleLogin(UserSnsVO vo);

	
	public UserSnsVO getUserSns(UserSnsVO userSnsVO);

	public List<UserSnsVO> getUserSnsList(UserSnsVO vo);
	
	UserSnsVO insUserSns(UserVO vo, HttpServletRequest request);

	SessionVO snsLoginProcess(UserSnsVO vo) throws Exception;

	void updUserSnsConnTime(UserSnsVO userSnsVO);

	public void updUserSnsToken(UserSnsVO vo);
}
