package com.ezsign.framework.util;


import java.net.URLDecoder;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ezsign.framework.vo.CookieVO;

public class CookieUtil {
	
	private static String SAMPLE_OPEN_KEY = "eyJBVVRIX0tFWSI6ImI2ZDgyMWVhNmVhMTRlM2JhNWIxMjQyZTk5ZGE0NmE3IiwiUk5EX0tFWSI6IlBXNkc1ektmZUNOUmZpQnpVdUJoV2N0azJyZDhIajVpKzR0UDVqUWcxeUE9IiwiVVNFUl9JRCI6ImRzNWNtbUBuZXd6ZW5wbnAuY29tIn0%3D";
	

	public static void main(String[] args) throws Exception {
		String encode_data = URLDecoder.decode(SAMPLE_OPEN_KEY);
		System.out.println(encode_data);
	}
	
	/**
	 * 쿠키에서 사용자 정보를 구해서 리턴 해준다.
	 *
	 * @param request HttpServletRequest
	 * @param sessionName 세션 명
	 * @return 사용자 정보가 있을 경우 정보가 담긴 bean 객체를 리턴하고 없을 경우에는 null을 리턴 한다.
	 */
	public static CookieVO getCookie(HttpServletRequest request) {
		CookieVO result = new CookieVO();
		
		// KEY BASE64 복호화
		String SESSION_DATA = request.getParameter("OPEN_KEY");
		
		if (StringUtil.isNull(SESSION_DATA)) SESSION_DATA = getCookie(request, "OPEN_KEY");

		if (StringUtil.isNull(SESSION_DATA)) {
			System.out.println("[getSessionCookie] OPEN_KEY 전달값이 없음.");
			return null;
		}
		
        String ENC_DATA = new String(Base64.decodeBase64(SESSION_DATA.getBytes()));
        
        // JSON파싱
		JSONParser parser = new JSONParser();
		Object obj;
		try { obj = parser.parse( ENC_DATA ); }
		catch (ParseException e) {
			System.out.println("[getSessionCookie] JSON파싱오류");
			return null;
		}
		JSONObject jsonObj = (JSONObject) obj;
		
		// 정보추출
		String AUTH_KEY = String.valueOf(jsonObj.get("AUTH_KEY"));
		String RND_KEY  = String.valueOf(jsonObj.get("RND_KEY"));
		String USER_ID  = String.valueOf(jsonObj.get("USER_ID"));
		
		if (StringUtil.isNull(AUTH_KEY) || StringUtil.isNull(RND_KEY) || StringUtil.isNull(USER_ID) ) {
			System.out.println("[getSessionCookie] OPEN_KEY의 정보가 없음");
			return null;
		}
		
		// 랜덤키 복호화
		String random_key;
		try { random_key = SecurityUtil.getinstance().aesDecode(RND_KEY); }
		catch (Exception e) {
			System.out.println("[getSessionCookie] 랜덤키 복호화 오류");
			return null;
		}
		if(random_key == null || random_key.length()==25) {
			System.out.println("[getSessionCookie] 랜덤키 길이 또는 복호화값 없음");
			return null;
		}
		
		// 사업자등록번호
		String random_busn_no     = StringUtil.substring(random_key, 0, 10);
		// 생성일시
		String random_create_date = StringUtil.substring(random_key, 10, 24);
		
		result.setAuthKey(AUTH_KEY);
		result.setRndKey(RND_KEY);
		result.setUserId(USER_ID);
		result.setBusnNo(random_busn_no);
		result.setCreateDate(random_create_date);
		
		return result;
	}
	
	public static String getCookie(HttpServletRequest request, String name) {
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookie.getName().equals(name)) return URLDecoder.decode(cookie.getValue());
	        }
	    }
	    return null;
	}
	
}
