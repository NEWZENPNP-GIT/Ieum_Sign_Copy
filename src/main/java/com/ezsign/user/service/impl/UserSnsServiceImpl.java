package com.ezsign.user.service.impl;

import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.biztalk.dao.BizTalkDAO;
import com.ezsign.biztalk.vo.BizTalkKKOVO;
import com.ezsign.content.service.ContentService;
import com.ezsign.contract.service.ContractService;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.system.dao.YS000DAO;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.point.dao.PointDAO;
import com.ezsign.point.vo.PointVO;
import com.ezsign.user.dao.DeviceDAO;
import com.ezsign.user.dao.UserDAO;
import com.ezsign.user.dao.UserSnsDAO;
import com.ezsign.user.service.UserService;
import com.ezsign.user.service.UserSnsService;
import com.ezsign.user.vo.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jarvis.common.util.FileUtil;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

@Service("userSnsService")
public class UserSnsServiceImpl extends AbstractServiceImpl implements UserSnsService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "userSnsDAO")
	private UserSnsDAO userSnsDAO;

	@Resource(name = "userDAO")
	private UserDAO userDAO;

	@Resource(name = "empDAO")
	private EmpDAO empDAO;

	@Resource(name = "bizDAO")
	private BizDAO bizDAO;
	
	@Override
	// id로 accessToken 발급
	// https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-code
	public UserSnsVO getKakaoAccessToken(String code, String mode) {
		final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserSnsVO snsVO = new UserSnsVO();

		postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
		postParams.add(new BasicNameValuePair("client_id", UserSnsService.KAKAO_CLIENT_ID));
		if(mode.equals("L")){
			postParams.add(new BasicNameValuePair("redirect_uri", UserSnsService.KAKAO_LOGIN_REDIRECT_URI));
		} else {
			postParams.add(new BasicNameValuePair("redirect_uri", UserSnsService.KAKAO_REDIRECT_URI));
		}
		postParams.add(new BasicNameValuePair("code", code));
		postParams.add(new BasicNameValuePair("client_secret", UserSnsService.KAKAO_CLIENT_SECRET));

		final HttpClient client = HttpClientBuilder.create().build();
		final HttpPost post = new HttpPost(UserSnsService.REQUEST_KAKAO_URL_TOKEN);

		JsonNode returnNode = null;

		try {
			post.setEntity(new UrlEncodedFormEntity(postParams));

			final HttpResponse response = client.execute(post);
			final int responseCode = response.getStatusLine().getStatusCode();

			System.out.println("getAccessToken Host : " + UserSnsService.REQUEST_KAKAO_URL_TOKEN + ", param : " + postParams + ", responseCode : " + responseCode);

			if (responseCode == 200) {
				// JSON 형태 반환값 처리
				ObjectMapper mapper = new ObjectMapper();
				returnNode = mapper.readTree(response.getEntity().getContent());
				snsVO.setAccessToken(returnNode.get("access_token").toString());
				snsVO.setRefreshToken(returnNode.get("refresh_token").toString());
				snsVO.setSuccess("Y");
			}

		} catch (Exception e) {
			snsVO.setSuccess("N");
			snsVO.setMessage(e.getMessage());
			e.printStackTrace();
		}

		return snsVO;
	}

	@Override
	// accessToken으로 사용자 정보 반환
	// https://developers.kakao.com/docs/latest/ko/user-mgmt/rest-api
	public UserSnsVO getKakaoUserInfo(String accessToken) {

		UserSnsVO snsVO = new UserSnsVO();
		try {
			URL url = new URL(UserSnsService.REQUEST_KAKAO_URL_USER_INFO);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);

			int responseCode = conn.getResponseCode();
			System.out.println("getAccessToken Host : " + UserSnsService.REQUEST_KAKAO_URL_USER_INFO + ", param : " + accessToken);
			System.out.println("responseCode : " + responseCode+", responseMessage : "+conn.getResponseMessage());

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line = "";
			String result = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}

			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(result);

			JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
			JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

			String nickname = properties.getAsJsonObject().get("nickname").getAsString();
			String email = kakao_account.getAsJsonObject().get("email").getAsString();
			String snsId = element.getAsJsonObject().get("id").getAsString();

			if(StringUtil.isNotNull(snsId) && StringUtil.isNotNull(email)){
				snsVO.setSnsId(snsId);
				snsVO.setSnsType("K");
				snsVO.setSnsEMail(email);
				snsVO.setSnsName(nickname);
				snsVO.setSuccess("Y");
			} else {
				snsVO.setSuccess("N");
				snsVO.setMessage("필수 정보가 조회되지 않았습니다.");
			}

		} catch (IOException e) {
			snsVO.setSuccess("N");
			snsVO.setMessage("데이터 처리 중 문제가 발생하였습니다.");
			e.printStackTrace();
		}

		return snsVO;
	}
	
	@Override
	public UserSnsVO updKakaoAccessToken(String refreshToken) {
		final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserSnsVO snsVO = new UserSnsVO();

		postParams.add(new BasicNameValuePair("grant_type", "refresh_token"));
		postParams.add(new BasicNameValuePair("client_id", UserSnsService.KAKAO_CLIENT_ID));
		postParams.add(new BasicNameValuePair("refresh_token", refreshToken));
		postParams.add(new BasicNameValuePair("client_secret", UserSnsService.KAKAO_CLIENT_SECRET));

		final HttpClient client = HttpClientBuilder.create().build();
		final HttpPost post = new HttpPost(UserSnsService.REQUEST_KAKAO_URL_TOKEN);

		JsonNode returnNode = null;

		try {
			post.setEntity(new UrlEncodedFormEntity(postParams));

			final HttpResponse response = client.execute(post);
			final int responseCode = response.getStatusLine().getStatusCode();

			System.out.println("getAccessToken Host : " + UserSnsService.REQUEST_KAKAO_URL_TOKEN + ", param : " + postParams + ", responseCode : " + responseCode);

			if (responseCode == 200) {
				// JSON 형태 반환값 처리
				String strJsonData = EntityUtils.toString(response.getEntity());
				JSONParser parser = new JSONParser();
				Object obj = parser.parse( strJsonData );
				JSONObject jsonObj = (JSONObject) obj;
				System.out.println("response : "+jsonObj.toString());
				snsVO.setAccessToken(jsonObj.get("access_token").toString());
				snsVO.setSuccess("Y");
				
				System.out.println("accessstoken : "+jsonObj.get("access_token").toString());
			}

		} catch (Exception e) {
			snsVO.setSuccess("N");
			snsVO.setMessage(e.getMessage());
			e.printStackTrace();
		}

		return snsVO;
	}
	
	@Override
	public UserSnsVO getUserSns(UserSnsVO userSnsVO) {
		return userSnsDAO.getUserSns(userSnsVO);
	}

	@Override
	public void updUserSnsConnTime(UserSnsVO userSnsVO) {
		userSnsDAO.updUserSnsConnTime(userSnsVO);
	}
	
	@Override
	public UserSnsVO getNaverAccessToken(String state, String code) {
		final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserSnsVO snsVO = new UserSnsVO();

		postParams.add(new BasicNameValuePair("client_id", UserSnsService.NAVER_CLIENT_ID));
		postParams.add(new BasicNameValuePair("client_secret", UserSnsService.NAVER_CLIENT_SECRET));
		postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
		postParams.add(new BasicNameValuePair("state", state));
		postParams.add(new BasicNameValuePair("code", code));

		final HttpClient client = HttpClientBuilder.create().build();
		final HttpPost post = new HttpPost(UserSnsService.REQUEST_NAVER_URL_TOKEN);

		JsonNode returnNode = null;

		try {
			post.setEntity(new UrlEncodedFormEntity(postParams));

			final HttpResponse response = client.execute(post);
			final int responseCode = response.getStatusLine().getStatusCode();

			System.out.println("getAccessToken Host : " + UserSnsService.REQUEST_NAVER_URL_TOKEN + ", param : " + postParams + ", responseCode : " + responseCode);

			if (responseCode == 200) {
				// JSON 형태 반환값 처리
				String strJsonData = EntityUtils.toString(response.getEntity());
				JSONParser parser = new JSONParser();
				Object obj = parser.parse( strJsonData );
				JSONObject jsonObj = (JSONObject) obj;
				System.out.println(jsonObj);
				snsVO.setAccessToken(jsonObj.get("access_token").toString());
				snsVO.setRefreshToken(jsonObj.get("refresh_token").toString());
				snsVO.setSuccess("Y");
				
				System.out.println("accessstoken : "+jsonObj.get("access_token").toString());
			}

		} catch (Exception e) {
			snsVO.setSuccess("N");
			snsVO.setMessage(e.getMessage());
			e.printStackTrace();
		}

		return snsVO;
	}
	
	@Override
	public UserSnsVO updNaverAccessToken(String refreshToken) {
		final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserSnsVO snsVO = new UserSnsVO();

		postParams.add(new BasicNameValuePair("grant_type", "refresh_token"));
		postParams.add(new BasicNameValuePair("client_id", UserSnsService.NAVER_CLIENT_ID));
		postParams.add(new BasicNameValuePair("client_secret", UserSnsService.NAVER_CLIENT_SECRET));
		postParams.add(new BasicNameValuePair("refresh_token", refreshToken));

		final HttpClient client = HttpClientBuilder.create().build();
		final HttpPost post = new HttpPost(UserSnsService.REQUEST_NAVER_URL_TOKEN);

		JsonNode returnNode = null;

		try {
			post.setEntity(new UrlEncodedFormEntity(postParams));

			final HttpResponse response = client.execute(post);
			final int responseCode = response.getStatusLine().getStatusCode();

			System.out.println("getAccessToken Host : " + UserSnsService.REQUEST_NAVER_URL_TOKEN + ", param : " + postParams + ", responseCode : " + responseCode);

			if (responseCode == 200) {
				// JSON 형태 반환값 처리
				String strJsonData = EntityUtils.toString(response.getEntity());
				JSONParser parser = new JSONParser();
				Object obj = parser.parse( strJsonData );
				JSONObject jsonObj = (JSONObject) obj;
				
				snsVO.setAccessToken(jsonObj.get("access_token").toString());
				snsVO.setSuccess("Y");
				
				System.out.println("accessstoken : "+jsonObj.get("access_token").toString());
			}

		} catch (Exception e) {
			snsVO.setSuccess("N");
			snsVO.setMessage(e.getMessage());
			e.printStackTrace();
		}

		return snsVO;
	}

	@Override
	public UserSnsVO getNaverUserInfo(String accessToken) {
		UserSnsVO snsVO = new UserSnsVO();
		try {
			URL url = new URL(UserSnsService.REQUEST_NAVER_URL_USER_INFO);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);

			int responseCode = conn.getResponseCode();
			System.out.println("getAccessToken Host : " + UserSnsService.REQUEST_NAVER_URL_USER_INFO + ", param : " + accessToken + ", responseCode : " + responseCode);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line = "";
			String result = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}

			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(result);
			
			System.out.println("element : "+element.toString());
			String resultcode = element.getAsJsonObject().get("resultcode").getAsString();
			String message = element.getAsJsonObject().get("resultcode").getAsString();

			JsonObject properties = element.getAsJsonObject().get("response").getAsJsonObject();

			if(properties.getAsJsonObject().get("email") == null){
				snsVO.setSuccess("N");
				snsVO.setMessage("[네이버로그인] 필수정보(이메일)의 동의가 필요합니다.");
				delNaverLogin(accessToken);
				return snsVO;
			}
			String email = properties.getAsJsonObject().get("email").getAsString();
			String snsId = properties.getAsJsonObject().get("id").getAsString();
			if(properties.getAsJsonObject().get("name") == null){
				snsVO.setSuccess("N");
				snsVO.setMessage("[네이버로그인] 필수정보(이름)의 동의가 필요합니다.");
				delNaverLogin(accessToken);
				return snsVO;
			}
			String snsName = properties.getAsJsonObject().get("name").getAsString();
			
			System.out.println("");
			if(StringUtil.isNotNull(snsId) && StringUtil.isNotNull(email)){
				snsVO.setSnsId(snsId);
				snsVO.setSnsType("N");
				snsVO.setSnsEMail(email);
				snsVO.setSnsName(snsName);
				snsVO.setSuccess("Y");
			} else {
				snsVO.setSuccess("N");
				snsVO.setMessage("[네이버로그인] 필수 정보(ID, 이메일)가 조회되지 않았습니다.");
			}

		} catch (IOException e) {
			snsVO.setSuccess("N");
			snsVO.setSuccess("[네이버로그인] 데이터 처리 중 문제가 발생하였습니다.");
			e.printStackTrace();
		}

		return snsVO;
	}
	
	public boolean delNaverLogin(String accessToken){
		final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		
		postParams.add(new BasicNameValuePair("client_id", UserSnsService.NAVER_CLIENT_ID));
		postParams.add(new BasicNameValuePair("client_secret", UserSnsService.NAVER_CLIENT_SECRET));
		postParams.add(new BasicNameValuePair("access_token", accessToken));
		postParams.add(new BasicNameValuePair("grant_type", "delete"));
		postParams.add(new BasicNameValuePair("service_provider", "NAVER"));

		final HttpClient client = HttpClientBuilder.create().build();
		final HttpPost post = new HttpPost(UserSnsService.REQUEST_NAVER_URL_TOKEN);

		try {
			post.setEntity(new UrlEncodedFormEntity(postParams));

			final HttpResponse response = client.execute(post);
			final int responseCode = response.getStatusLine().getStatusCode();

			System.out.println("getAccessToken Host : " + UserSnsService.REQUEST_NAVER_URL_TOKEN + ", param : " + postParams + ", responseCode : " + responseCode);

			String strJsonData = EntityUtils.toString(response.getEntity());
			JSONParser parser = new JSONParser();
			Object obj = parser.parse( strJsonData );
			JSONObject jsonObj = (JSONObject) obj;
			
			System.out.println("연동해제 결과 : " + strJsonData);
			
			if (responseCode == 200) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public UserSnsVO getGoogleAccessToken(String code, String mode){
		final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserSnsVO snsVO = new UserSnsVO();

		postParams.add(new BasicNameValuePair("code", code));
		postParams.add(new BasicNameValuePair("client_id", UserSnsService.GOOGLE_CLIENT_ID));
		postParams.add(new BasicNameValuePair("client_secret", UserSnsService.GOOGLE_CLIENT_SECRET));
		if(mode.equals("L")){
			postParams.add(new BasicNameValuePair("redirect_uri", UserSnsService.GOOGLE_LOGIN_REDIRECT_URI));
		} else {
			postParams.add(new BasicNameValuePair("redirect_uri", UserSnsService.GOOGLE_REDIRECT_URI));
		}
		postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
		postParams.add(new BasicNameValuePair("access_type", "offline"));
		postParams.add(new BasicNameValuePair("prompt", "consent"));

		final HttpClient client = HttpClientBuilder.create().build();
		final HttpPost post = new HttpPost(UserSnsService.REQUEST_GOOGLE_URL_TOKEN);

		JsonNode returnNode = null;

		try {
			post.setEntity(new UrlEncodedFormEntity(postParams));

			final HttpResponse response = client.execute(post);
			final int responseCode = response.getStatusLine().getStatusCode();

			System.out.println("getAccessToken Host : " + UserSnsService.REQUEST_GOOGLE_URL_TOKEN + ", param : " + postParams + ", responseCode : " + responseCode);

			if (responseCode == 200) {
				// JSON 형태 반환값 처리
				String strJsonData = EntityUtils.toString(response.getEntity());
				JSONParser parser = new JSONParser();
				Object obj = parser.parse( strJsonData );
				JSONObject jsonObj = (JSONObject) obj;
				snsVO.setSuccess("Y");
				System.out.println("jsonObj : "+jsonObj.toString());
				snsVO.setAccessToken(jsonObj.get("access_token").toString());
				snsVO.setIdToken(jsonObj.get("id_token").toString());
				if(jsonObj.get("refresh_token") != null){
					snsVO.setRefreshToken(jsonObj.get("refresh_token").toString());
				}
			}

		} catch (Exception e) {
			snsVO.setSuccess("N");
			snsVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return snsVO;
	}
	
	@Override
	public UserSnsVO getGoogleUserInfo(String code, HttpServletRequest request) {
		UserSnsVO snsVO = new UserSnsVO();
		GoogleIdToken idToken = null;
		
		try{
			final NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
			final JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

			//ID token으로 사용자 정보 조회
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory).setAudience(Collections.singletonList(UserSnsService.GOOGLE_CLIENT_ID)).build();

			idToken = verifier.verify(code);
		}catch (Exception e) {
			snsVO.setSuccess("N");
			snsVO.setSuccess("데이터 처리 중 문제가 발생하였습니다.");
			e.printStackTrace();
			
			return snsVO;
		}
		
		if (idToken != null) {
			Payload payload = idToken.getPayload();
			
			//사용자 고유 값
			String userId = payload.getSubject();
			
			String email = payload.getEmail();
			boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
			String name = (String) payload.get("name");
			
			if(StringUtil.isNotNull(userId) && StringUtil.isNotNull(email)){
				snsVO.setSnsId(userId);
				snsVO.setSnsType("G");
				snsVO.setSnsEMail(email);
				if(StringUtil.isNull(name)){
					snsVO.setSnsName(request.getSession().getAttribute("snsName").toString());
				} else {
					snsVO.setSnsName(name);
				}
				snsVO.setSuccess("Y");
			} else {
				snsVO.setSuccess("N");
				snsVO.setMessage("필수 정보(ID, 이메일)가 조회되지 않았습니다.");
			}
			
			System.out.println(idToken.toString());
		} else {
			snsVO.setSuccess("N");
			snsVO.setMessage("필수 정보가 조회되지 않았습니다.");
		}
		
		return snsVO;
	}
	
	@Override
	public UserSnsVO updGoogleAccessToken(String refreshToken){
		final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserSnsVO snsVO = new UserSnsVO();

		postParams.add(new BasicNameValuePair("client_id", UserSnsService.GOOGLE_CLIENT_ID));
		postParams.add(new BasicNameValuePair("client_secret", UserSnsService.GOOGLE_CLIENT_SECRET));
		postParams.add(new BasicNameValuePair("refresh_token", refreshToken));
		postParams.add(new BasicNameValuePair("grant_type", "refresh_token"));

		final HttpClient client = HttpClientBuilder.create().build();
		final HttpPost post = new HttpPost(UserSnsService.REQUEST_GOOGLE_URL_TOKEN);

		JsonNode returnNode = null;

		try {
			post.setEntity(new UrlEncodedFormEntity(postParams));

			final HttpResponse response = client.execute(post);
			final int responseCode = response.getStatusLine().getStatusCode();

			System.out.println("getAccessToken Host : " + UserSnsService.REQUEST_GOOGLE_URL_TOKEN + ", param : " + postParams + ", responseCode : " + responseCode);

			if (responseCode == 200) {
				// JSON 형태 반환값 처리
				String strJsonData = EntityUtils.toString(response.getEntity());
				JSONParser parser = new JSONParser();
				Object obj = parser.parse( strJsonData );
				JSONObject jsonObj = (JSONObject) obj;
				snsVO.setSuccess("Y");
				System.out.println("jsonObj : "+jsonObj.toString());
				snsVO.setAccessToken(jsonObj.get("access_token").toString());
				snsVO.setIdToken(jsonObj.get("id_token").toString());
			}

		} catch (Exception e) {
			snsVO.setSuccess("N");
			snsVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return snsVO;
	}

	@Override
	public UserSnsVO insUserSns(UserVO vo, HttpServletRequest request) {
		//sns type조회
		Object snsTypeObj = request.getSession().getAttribute("snsType");
		UserSnsVO resSnsVo = new UserSnsVO();
		if(snsTypeObj == null){
			//sns 회원가입 아님.
			resSnsVo.setStatusCode("-500");
			return resSnsVo;
		} 
		String snsType = snsTypeObj.toString();
		switch(snsType){
		case "K":
			String naverRefreshToken = request.getSession().getAttribute("refreshToken").toString();
			//accessToken갱신
			UserSnsVO naverResVO = updKakaoAccessToken(naverRefreshToken);
			if(naverResVO.getSuccess().equals("Y")){
				//사용자정보조회
				naverResVO = getKakaoUserInfo(naverResVO.getAccessToken());
				naverResVO.setUserId(vo.getLoginId());
				
				UserSnsVO resVO = userSnsDAO.getUserSns(naverResVO);
				
				if(resVO == null){
					userSnsDAO.insUserSns(naverResVO);
					resSnsVo.setSnsType(naverResVO.getSnsType());
					resSnsVo.setSnsId(naverResVO.getSnsId());
					resSnsVo.setStatusCode("100");
					return resSnsVo;
				} else {
					resSnsVo.setStatusCode("-200");
					return resSnsVo;
				}
			} else {
				resSnsVo.setStatusCode("-100");
				return resSnsVo;
			}
		case "N":
			String kakaoRefreshToken = request.getSession().getAttribute("refreshToken").toString();
			//accessToken갱신
			UserSnsVO kakaoResVO = updNaverAccessToken(kakaoRefreshToken);
			if(kakaoResVO.getSuccess().equals("Y")){
				//사용자정보조회
				kakaoResVO = getNaverUserInfo(kakaoResVO.getAccessToken());
				kakaoResVO.setUserId(vo.getUserId());
				
				UserSnsVO resVO = userSnsDAO.getUserSns(kakaoResVO);
				
				if(resVO == null){
					userSnsDAO.insUserSns(kakaoResVO);
					resSnsVo.setSnsType(kakaoResVO.getSnsType());
					resSnsVo.setSnsId(kakaoResVO.getSnsId());
					resSnsVo.setStatusCode("100");
					return resSnsVo;
				} else {
					resSnsVo.setStatusCode("-200");
					return resSnsVo;
				}
			}else {
				resSnsVo.setStatusCode("-100");
				return resSnsVo;
			}
		case "G":
			String googleRefreshToken = request.getSession().getAttribute("refreshToken").toString();
			//accessToken갱신
			UserSnsVO googleResVO = updGoogleAccessToken(googleRefreshToken);
			if(googleResVO.getSuccess().equals("Y")){
				//사용자정보조회
				googleResVO = getGoogleUserInfo(googleResVO.getIdToken(), request);
				googleResVO.setUserId(vo.getUserId());
				UserSnsVO resVO = userSnsDAO.getUserSns(googleResVO);
				if(resVO == null){
					userSnsDAO.insUserSns(googleResVO);
					resSnsVo.setSnsType(googleResVO.getSnsType());
					resSnsVo.setSnsId(googleResVO.getSnsId());
					resSnsVo.setStatusCode("100");
					return resSnsVo;
				} else {
					resSnsVo.setStatusCode("-200");
					return resSnsVo;
				}
			} else {
				resSnsVo.setStatusCode("-100");
				return resSnsVo;
			}
		default:
			//sns종류가 올바르지 않음.
			resSnsVo.setStatusCode("-300");
			return resSnsVo;
		}
	}
	
	@Override
    public SessionVO snsLoginProcess(UserSnsVO vo) throws Exception {
        SessionVO loginVO = new SessionVO();
        UserVO userVO = new UserVO();
        UserVO userTmpVO = new UserVO();
        userTmpVO.setUserId(vo.getUserId());
        
        userVO = userDAO.loginProcess(userTmpVO);

        if (userVO == null) return null;
        System.out.println("login result" + userVO.getUserId() + "/" + userVO.getUserType());

        //String userPwd = userVO.getUserPwd();
        
        String loginLockYn = userVO.getLoginLockYn();	
        //int loginCount = userVO.getLoginCount();	
        
        //계정이 잠긴경우	
        if("Y".equals(loginLockYn)) {	
            loginVO.setLoginLockYn(loginLockYn);	
            loginVO.setUnlockTime(userVO.getUnlockTime());	
            return loginVO;	
        }
        
        //계정이 잠금 해제 된 경우
        if("R".equals(loginLockYn)) {	
        	UserVO userVOParam = new UserVO();	
            userVOParam.setUserId(vo.getUserId());	
            userVOParam.setLoginCount(0);	
            userDAO.updUser(userVOParam);
            userVO.setLoginLockYn("N");
        }
        
        //String encPwd = SecurityUtil.encrypt(vo.getUserPwd());

        UserLogVO logVO = new UserLogVO();
        logVO.setUserId(vo.getUserId());
        logVO.setLogType("000");
        userDAO.insUserLog(logVO);

        //System.out.println(userPwd+"^"+encPwd+"^"+vo.getUserPwd());
        //if (userPwd.equals(encPwd)) {
        System.out.println(userVO.getUserId() + "패스워드 일치");
        loginVO.setLoginId(userVO.getUserId());
        loginVO.setUserName(userVO.getUserName());
        loginVO.setUserType(userVO.getUserType());

        EmpVO empVO = new EmpVO();
        empVO.setLoginId(vo.getUserId());
        empVO.setStartPage(0);
        empVO.setEndPage(10);
        List<EmpVO> empList = empDAO.getEmpList(empVO);

        System.out.println("size=>" + empList.size());
        EmpVO resultEmpVO = new EmpVO();
        if (empList.size() > 0) {
            for (int i = 0; i < empList.size(); i++) {
                resultEmpVO = empList.get(i);
                System.out.println("사용자정보 : biz[" + resultEmpVO.getBizId() + "], user[" + resultEmpVO.getUserId() + "] ");
            }
        }
        if (resultEmpVO != null && empList.size() > 0) {
            String bizId = resultEmpVO.getBizId();
            String bizName = resultEmpVO.getBizName();
            loginVO.setBizId(bizId);
            loginVO.setBizName(bizName);
            loginVO.setUserId(resultEmpVO.getUserId());

            logVO = new UserLogVO();
            logVO.setUserId(vo.getUserId());
            logVO.setLogType("001");
            logVO.setIpAddr(null);
            userDAO.insUserLog(logVO);
            
            //로그인 실패 카운트 0으로 초기화	
            UserVO userVOParam = new UserVO();	
            userVOParam.setUserId(vo.getUserId());	
            userVOParam.setLoginCount(0);	
            userDAO.updUser(userVOParam);
            
            userSnsDAO.updUserSnsConnTime(vo);
            
            BizVO paramBizVO = new BizVO();
            paramBizVO.setBizId(bizId);
            BizVO resBizVO = bizDAO.getBiz(paramBizVO);
            
            if(resBizVO != null){
            	loginVO.setUseContract(resBizVO.getUseContract());
            	System.out.println("resBizVO.getUseContract() : "+resBizVO.getUseContract());
            }
            
            String curDate = DateUtil.getTimeStamp(7);
            System.out.println("접속한 사용자 [" + userVO.getUserName() + "] Connected Time=>" + curDate);

        } else {

            logVO = new UserLogVO();
            logVO.setUserId(vo.getUserId());
            logVO.setLogType("002");
            logVO.setIpAddr(null);
            userDAO.insUserLog(logVO);

            loginVO = null;
        }

        return loginVO;
    }

	@Override
	public UserSnsVO delKakaoLogin(UserSnsVO vo) {
		final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserSnsVO snsVO = new UserSnsVO();

		UserSnsVO resVO = userSnsDAO.getUserSns(vo);
		
		if(StringUtil.isNull(resVO.getRefreshToken())){
			snsVO.setSuccess("N");
			snsVO.setMessage("토큰 정보가 존재하지 않습니다. SNS로그인을 시도해주세요.");
			return snsVO;
		}
		
		UserSnsVO kakaoVO = updKakaoAccessToken(resVO.getRefreshToken());
		if(StringUtil.isNull(kakaoVO.getAccessToken()) || kakaoVO.getSuccess().equals("N")){
			snsVO.setSuccess("N");
			snsVO.setMessage("토큰의 유효기간이 만료하였거나 토큰 조회 중 오류가 발생하였습니다. SNS로그인을 시도해주세요.");
			System.out.println("토큰의 유효기간이 만료하였거나 토큰 조회 중 오류가 발생하였습니다. SNS로그인을 시도해주세요.");
			return snsVO;
		} 
		
		try {
			URL url = new URL(UserSnsService.REQUEST_KAKAO_URL_UNLINK);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + kakaoVO.getAccessToken());

			int responseCode = conn.getResponseCode();
			System.out.println("getAccessToken Host : " + UserSnsService.REQUEST_KAKAO_URL_UNLINK + ", param : " + kakaoVO.getAccessToken());
			System.out.println("responseCode : " + responseCode+", responseMessage : "+conn.getResponseMessage());

			if(responseCode == 200){
				snsVO.setSuccess("Y");
				userSnsDAO.delUserSns(resVO);
			} else {
				snsVO.setSuccess("N");
				snsVO.setMessage(conn.getResponseMessage());
			}
		} catch (Exception e) {
			snsVO.setSuccess("N");
			snsVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return snsVO;
	}

	@Override
	public UserSnsVO delNaverLogin(UserSnsVO vo) {
		final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserSnsVO snsVO = new UserSnsVO();

		UserSnsVO resVO = userSnsDAO.getUserSns(vo);
		
		if(StringUtil.isNull(resVO.getRefreshToken())){
			snsVO.setSuccess("N");
			snsVO.setMessage("토큰 정보가 존재하지 않습니다. SNS로그인을 시도해주세요.");
			return snsVO;
		}
		
		UserSnsVO naverVO = updNaverAccessToken(resVO.getRefreshToken());
		if(StringUtil.isNull(naverVO.getAccessToken()) || naverVO.getSuccess().equals("N")){
			snsVO.setSuccess("N");
			snsVO.setMessage("토큰의 유효기간이 만료하였거나 토큰 조회 중 오류가 발생하였습니다. SNS로그인을 시도해주세요.");
			System.out.println("토큰의 유효기간이 만료하였거나 토큰 조회 중 오류가 발생하였습니다. SNS로그인을 시도해주세요.");
			return snsVO;
		} 
		
		postParams.add(new BasicNameValuePair("client_id", UserSnsService.NAVER_CLIENT_ID));
		postParams.add(new BasicNameValuePair("client_secret", UserSnsService.NAVER_CLIENT_SECRET));
		postParams.add(new BasicNameValuePair("access_token", naverVO.getAccessToken()));
		postParams.add(new BasicNameValuePair("grant_type", "delete"));
		postParams.add(new BasicNameValuePair("service_provider", "NAVER"));

		final HttpClient client = HttpClientBuilder.create().build();
		final HttpPost post = new HttpPost(UserSnsService.REQUEST_NAVER_URL_TOKEN);

		try {
			post.setEntity(new UrlEncodedFormEntity(postParams));

			final HttpResponse response = client.execute(post);
			final int responseCode = response.getStatusLine().getStatusCode();

			System.out.println("getAccessToken Host : " + UserSnsService.REQUEST_NAVER_URL_TOKEN + ", param : " + postParams + ", responseCode : " + responseCode);

			String strJsonData = EntityUtils.toString(response.getEntity());
			JSONParser parser = new JSONParser();
			Object obj = parser.parse( strJsonData );
			JSONObject jsonObj = (JSONObject) obj;
			
			System.out.println("연동해제 결과 : " + strJsonData);
			
			if (responseCode == 200) {
				userSnsDAO.delUserSns(resVO);
				snsVO.setSuccess("Y");
			} else {
				snsVO.setSuccess("N");
				snsVO.setMessage("네이버 연결 해제 중 오류가 발생하였습니다.");
				return snsVO;
			}

		} catch (Exception e) {
			snsVO.setSuccess("N");
			snsVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return snsVO;
	}

	@Override
	public UserSnsVO delGoogleLogin(UserSnsVO vo) {
		final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		UserSnsVO snsVO = new UserSnsVO();

		UserSnsVO resVO = userSnsDAO.getUserSns(vo);
		
		if(StringUtil.isNull(resVO.getRefreshToken())){
			snsVO.setSuccess("N");
			snsVO.setMessage("토큰 정보가 존재하지 않습니다. SNS로그인을 시도해주세요.");
			return snsVO;
		}
		
		UserSnsVO googleVO = updGoogleAccessToken(resVO.getRefreshToken());
		if(StringUtil.isNull(googleVO.getAccessToken()) || googleVO.getSuccess().equals("N")){
			snsVO.setSuccess("N");
			snsVO.setMessage("토큰의 유효기간이 만료하였거나 토큰 조회 중 오류가 발생하였습니다. SNS로그인을 시도해주세요.");
			System.out.println("토큰의 유효기간이 만료하였거나 토큰 조회 중 오류가 발생하였습니다. SNS로그인을 시도해주세요.");
			return snsVO;
		} 
		
		postParams.add(new BasicNameValuePair("token", googleVO.getAccessToken()));

		final HttpClient client = HttpClientBuilder.create().build();
		final HttpPost post = new HttpPost(UserSnsService.REQUEST_GOOGLE_URL_UNLINK);

		try {
			post.setEntity(new UrlEncodedFormEntity(postParams));

			final HttpResponse response = client.execute(post);
			final int responseCode = response.getStatusLine().getStatusCode();

			System.out.println("getAccessToken Host : " + UserSnsService.REQUEST_GOOGLE_URL_UNLINK + ", param : " + googleVO.getAccessToken());
			System.out.println("responseCode : " + responseCode+", responseMessage : "+response.getStatusLine().getReasonPhrase());
			
			if (responseCode == 200) {
				userSnsDAO.delUserSns(resVO);
				snsVO.setSuccess("Y");
			} else {
				snsVO.setSuccess("N");
				snsVO.setMessage("구글 연결 해제 중 오류가 발생하였습니다.");
				return snsVO;
			}

		} catch (Exception e) {
			snsVO.setSuccess("N");
			snsVO.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return snsVO;
	}

	@Override
	public List<UserSnsVO> getUserSnsList(UserSnsVO vo) {
		return userSnsDAO.getUserSnsList(vo);
	}

	@Override
	public void updUserSnsToken(UserSnsVO vo) {
		userSnsDAO.updUserSnsToken(vo);
	}
}
