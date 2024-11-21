package com.ezsign.user.controller;


import com.ezsign.biz.service.BizService;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.point.service.PointService;
import com.ezsign.point.vo.PointVO;
import com.ezsign.user.dao.UserSnsDAO;
import com.ezsign.user.service.UserService;
import com.ezsign.user.service.UserSnsService;
import com.ezsign.user.vo.DeviceVO;
import com.ezsign.user.vo.UserSnsVO;
import com.ezsign.user.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.utl.slm.EgovHttpSessionBindingListener;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;


@Controller
@RequestMapping("/sns/")
public class UserSnsController extends BaseController {

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "pointService")
    private PointService pointService;

    @Resource(name = "bizService")
    private BizService bizService;

    @Resource(name = "messageProperties")
    private Properties messageProperties;
    
    @Resource(name = "userSnsService")
    private UserSnsService userSnsService;
    
    @Resource(name = "userSnsDAO")
    private UserSnsDAO userSnsDAO;
    
    @RequestMapping(method = RequestMethod.POST, value = "snsJoinLogin.do")
    @ResponseBody
    public ResponseEntity<JSONObject> snsJoinLoginCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObject = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "Applacation/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        String returnCode = "-400";
        
        try {
            System.out.println(":::::::::::::::::::: snsJoinLogin ::::::::::::::::::: " + vo.getUserId());

            SessionVO loginVO = userService.loginProcess(vo);

            if (loginVO != null) {
            	if("Y".equals(loginVO.getLoginLockYn())) {
                    jsonObject.put("success", false);
                    jsonObject.put("message", "비밀번호 5회 오류로 계정이 잠겼습니다.\n" + loginVO.getUnlockTime() + "초 뒤에 로그인이 가능합니다.");
                } else {
                    if("Y".equals(loginVO.getLoginFailYn())) {
                        jsonObject.put("success", false);
                        //jsonObject.put("message", "비밀먼호 " + loginVO.getLoginCount() + "회 오류입니다.\n5회 오류시 계정이 잠깁니다.");
                        //jsonObject.put("message", "5회 오류시 계정이 잠깁니다.");
                        jsonObject.put("message", messageProperties.getProperty("login.fail") + "\n5회 오류시 계정이 잠깁니다.");
                    } else {
                    	
                    	//sns로그인 연동이 필요한 경우 진행
                        UserSnsVO resSnsVO = userSnsService.insUserSns(vo, request);
                        returnCode = resSnsVO.getStatusCode();
                        if(returnCode.equals("100")){
                        	userSnsService.updUserSnsConnTime(resSnsVO);
                        	resSnsVO.setRefreshToken(request.getSession().getAttribute("refreshToken").toString());
                        	userSnsService.updUserSnsToken(resSnsVO);
                        }
                        //로그인 성공시에 암호화 키값 셋팅
                        //loginVO.setParamEncryptPassword(ConfigUtil.getString("parameter.encrypt.password"));
                        SessionUtil.Login(request, loginVO);
                        int curPoint = 0;

                        // 접속기업의 포인트
                        PointVO pVO = new PointVO();
                        pVO.setBizId(loginVO.getBizId());
                        PointVO pointVO = pointService.getPoint(pVO);

                        if (pointVO != null) curPoint = pointVO.getCurPoint();

                        System.out.println("# curPoint : " + curPoint);

                        if (!StringUtil.isNotNull(loginVO.getUserName())) loginVO.setUserName("-");
                        jsonObject.put("success", true);
                        jsonObject.put("loginId", loginVO.getLoginId());
                        jsonObject.put("userId", loginVO.getUserId());
                        jsonObject.put("userName", loginVO.getUserName());
                        jsonObject.put("userType", loginVO.getUserType());
                        jsonObject.put("bizId", loginVO.getBizId());
                        jsonObject.put("bizName", loginVO.getBizName());
                        jsonObject.put("curPoint", curPoint);
                        // 연말정산 계약ID
                        jsonObject.put("yearContractId", loginVO.getYearContractId());
                        // 연말정산 계약년도
                        jsonObject.put("febYear", loginVO.getFebYear());

                        jsonObject.put("message", messageProperties.getProperty("login.success"));

                        EgovHttpSessionBindingListener listener = new EgovHttpSessionBindingListener();
                        request.getSession().setAttribute(loginVO.getLoginId(), listener);
                        
                    }
                }
            } else {
                jsonObject.put("success", false);
                jsonObject.put("message", messageProperties.getProperty("login.fail"));
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        jsonObject.put("returnCode", returnCode);

        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }
    
    @RequestMapping(value = "kakaoCallback.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView kakaoCallbackCtrl(HttpServletRequest request, @RequestParam String code) throws IOException {
    	
    	System.out.println(":::::::::::::::::::: kakaoCallback :::::::::::::::::::");
    	
    	ModelAndView view = new ModelAndView();
    	view.setViewName("/member/member_Join_callback");
    	
    	UserSnsVO resVO = null;
    	
    	if(StringUtil.isNotNull(code)){
    		resVO = userSnsService.getKakaoAccessToken(code, "J");
    	}
    	
    	if(resVO == null){
    		view.addObject("success", "N");
    		view.addObject("message", "[카카오로그인] 결과값이 존재하지 않습니다.");
			return view;
    	}
    	
    	if( StringUtil.isNull(resVO.getAccessToken())){
    		view.addObject("success", "N");
    		view.addObject("message", "[카카오로그인] 엑세스 토큰 조회에 실패하였습니다.");
			return view;
    	}
    	
    	if( StringUtil.isNull(resVO.getRefreshToken())){
    		view.addObject("success", "N");
    		view.addObject("message", "[카카오로그인] 갱신 토큰 조회에 실패하였습니다.");
			return view;
    	}
    	
    	if(!resVO.getSuccess().equals("Y")){
    		view.addObject("success", "N");
    		view.addObject("message", resVO.getMessage());
			return view;
    	}
    	
    	resVO.setSnsType("K");
		UserSnsVO userInfo = userSnsService.getKakaoUserInfo(resVO.getAccessToken());
		
		//DB처리
		if(!userInfo.getSuccess().equals("Y")){

			view.addObject("success", "N");
    		view.addObject("message", "[카카오로그인] 사용자 정보조회를 실패하였습니다.");
			return view;
        	
		} else {
			UserSnsVO resUserSnsVO = userSnsService.getUserSns(userInfo);
			if(resUserSnsVO == null){
				//캔디계정X, SNS계정X
	        	view.addObject("success", "J");
	        	request.getSession().setAttribute("snsType", "K");
	        	request.getSession().setAttribute("snsEmail", userInfo.getSnsEMail());
	        	request.getSession().setAttribute("snsName", userInfo.getSnsName());
				request.getSession().setAttribute("accessToken", resVO.getAccessToken());
				request.getSession().setAttribute("refreshToken", resVO.getRefreshToken());
				return view;
			} else {
				//캔디계정O, SNS계정O
				
				//refresh token update
				UserSnsVO snsTokenVO = new UserSnsVO();
				snsTokenVO.setUserSnsId(resUserSnsVO.getUserSnsId());
				snsTokenVO.setRefreshToken(resVO.getRefreshToken());
				userSnsService.updUserSnsToken(snsTokenVO);
				
	        	view.addObject("success", "Y");
				return view;
			}
		}
    }
    
    @RequestMapping(value = "naverCallback.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView naverCallbackCtrl(HttpServletRequest request, @RequestParam String state, @RequestParam String code) throws IOException {
    	
    	//1.7 https://developers.naver.com/docs/login/web/ accessToken갱신
    	
    	System.out.println(":::::::::::::::::::: naverCallback :::::::::::::::::::");
    	
    	ModelAndView view = new ModelAndView();
    	view.setViewName("/member/member_Join_callback");
    	
    	UserSnsVO resVO = null;
    	
    	if(StringUtil.isNotNull(code)){
    		System.out.println(request.getSession().getAttribute("uuid"));
    		String sessionCode = request.getSession().getAttribute("uuid").toString();
    		if(sessionCode.equals(state)){
    			resVO = userSnsService.getNaverAccessToken(state, code);
    		} else {
    			view.addObject("success", "N");
    			view.addObject("message", "인증 요청값과 결과값이 동일하지 않습니다.");
				return view;
    		}
    	}
    	
    	if(resVO == null){
    		view.addObject("success", "N");
    		view.addObject("message", "[네이버로그인] 결과값이 존재하지 않습니다.");
			return view;
    	}
    	
    	if( StringUtil.isNull(resVO.getAccessToken())){
    		view.addObject("success", "N");
    		view.addObject("message", "[네이버로그인] 엑세스 토큰 조회에 실패하였습니다.");
			return view;
    	}
    	
    	if( StringUtil.isNull(resVO.getRefreshToken())){
    		view.addObject("success", "N");
    		view.addObject("message", "[네이버로그인] 갱신 토큰 조회에 실패하였습니다.");
			return view;
    	}
    	
    	if(!resVO.getSuccess().equals("Y")){
    		view.addObject("success", "N");
    		view.addObject("message", resVO.getMessage());
			return view;
    	}
    	
    	resVO.setSnsType("N");
		UserSnsVO userInfo = userSnsService.getNaverUserInfo(resVO.getAccessToken());
		
		//DB처리
		if(userInfo == null){
			
			view.addObject("success", "N");
    		view.addObject("message", "[네이버로그인] 사용자 정보조회를 실패하였습니다.");
			return view;
        	
		} else if(userInfo.getSuccess().equals("N")){

			view.addObject("success", "N");
    		view.addObject("message", "[네이버로그인] 사용자 정보조회를 실패하였습니다.");
			return view;
        	
		} else {
			UserSnsVO resUserSnsVO = userSnsService.getUserSns(userInfo);
			if(resUserSnsVO == null){
				//캔디계정X, SNS계정X
	        	view.addObject("success", "J");
	        	request.getSession().setAttribute("snsType", "N");
	        	request.getSession().setAttribute("snsEmail", userInfo.getSnsEMail());
	        	request.getSession().setAttribute("snsName", userInfo.getSnsName());
				request.getSession().setAttribute("accessToken", resVO.getAccessToken());
				request.getSession().setAttribute("refreshToken", resVO.getRefreshToken());
				return view;
			} else {
				//캔디계정O, SNS계정O
				
				//refresh token update
				UserSnsVO snsTokenVO = new UserSnsVO();
				snsTokenVO.setUserSnsId(resUserSnsVO.getUserSnsId());
				snsTokenVO.setRefreshToken(resVO.getRefreshToken());
				userSnsService.updUserSnsToken(snsTokenVO);
				
	        	view.addObject("success", "Y");
				return view;
			}
		}
    		
    }
    
    @RequestMapping(value = "googleCallback.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView googleCallbackCtrl(HttpServletRequest request, @RequestParam String code) throws IOException {
    	
    	System.out.println(":::::::::::::::::::: googleCallback :::::::::::::::::::");
    	
    	ModelAndView view = new ModelAndView();
    	view.setViewName("/member/member_Join_callback");
    	
    	UserSnsVO resVO = null;
    	
    	if(StringUtil.isNotNull(code)){
    		resVO = userSnsService.getGoogleAccessToken(code, "J");
    	}
    	
    	if(resVO == null){
    		view.addObject("success", "N");
    		view.addObject("message", "[구글로그인] 결과값이 존재하지 않습니다.");
			return view;
    	}
    	
    	if( StringUtil.isNull(resVO.getIdToken())){
    		view.addObject("success", "N");
    		view.addObject("message", "[구글로그인] 코드 조회에 실패하였습니다.");
			return view;
    	}

    	if( StringUtil.isNull(resVO.getRefreshToken())){
    		view.addObject("success", "N");
    		view.addObject("message", "[구글로그인] 갱신 토큰 조회에 실패하였습니다.");
			return view;
    	}

    	if( StringUtil.isNull(resVO.getAccessToken())){
    		view.addObject("success", "N");
    		view.addObject("message", "[구글로그인] 접근 토큰 조회에 실패하였습니다.");
			return view;
    	}
    	
    	if(!resVO.getSuccess().equals("Y")){
    		view.addObject("success", "N");
    		view.addObject("message", "[구글로그인]"+resVO.getMessage());
			return view;
    	}
    	
    	resVO.setSnsType("K");
		UserSnsVO userInfo = userSnsService.getGoogleUserInfo(resVO.getIdToken(), request);
		
		System.out.println("userInfo : "+userInfo.toString());
		
		if(!userInfo.getSuccess().equals("Y")){

			view.addObject("success", "N");
    		view.addObject("message", "[구글로그인] "+userInfo.getMessage());
			return view;
        	
		} else {
			UserSnsVO resUserSnsVO = userSnsService.getUserSns(userInfo);
			if(resUserSnsVO == null){
				//캔디계정X, SNS계정X
	        	view.addObject("success", "J");
	        	request.getSession().setAttribute("snsType", "G");
	        	request.getSession().setAttribute("snsEmail", userInfo.getSnsEMail());
	        	request.getSession().setAttribute("snsName", userInfo.getSnsName());
				request.getSession().setAttribute("refreshToken", resVO.getRefreshToken());
				request.getSession().setAttribute("accessToken", resVO.getAccessToken());
				request.getSession().setAttribute("idToken", resVO.getRefreshToken());
				return view;
			} else {
				//캔디계정O, SNS계정O
				
				//refresh token update
				UserSnsVO snsTokenVO = new UserSnsVO();
				snsTokenVO.setUserSnsId(resUserSnsVO.getUserSnsId());
				snsTokenVO.setRefreshToken(resVO.getRefreshToken());
				userSnsService.updUserSnsToken(snsTokenVO);
				
	        	view.addObject("success", "Y");
				return view;
			}
		}
    }
    
    @RequestMapping(value = "kakaoLoginCallback.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView kakaoLoginCallbackCtrl(HttpServletRequest request, @RequestParam String code) throws Exception {
    	
    	System.out.println(":::::::::::::::::::: kakaoLoginCallback :::::::::::::::::::");
    	
    	ModelAndView view = new ModelAndView();
    	view.setViewName("/member/member_login_callback");
    	
    	UserSnsVO resVO = null;
    	
    	if(StringUtil.isNotNull(code)){
    		resVO = userSnsService.getKakaoAccessToken(code, "L");
    	}
    	
    	if(resVO == null){
    		view.addObject("success", "N");
    		view.addObject("message", "[카카오로그인] 결과값이 존재하지 않습니다.");
			return view;
    	}
    	
    	if( StringUtil.isNull(resVO.getAccessToken())){
    		view.addObject("success", "N");
    		view.addObject("message", "[카카오로그인] 엑세스 토큰 조회에 실패하였습니다.");
			return view;
    	}
    	
    	if( StringUtil.isNull(resVO.getRefreshToken())){
    		view.addObject("success", "N");
    		view.addObject("message", "[카카오로그인] 갱신 토큰 조회에 실패하였습니다.");
			return view;
    	}
    	
    	if(!resVO.getSuccess().equals("Y")){
    		view.addObject("success", "N");
    		view.addObject("message", resVO.getMessage());
			return view;
    	}
    	
    	resVO.setSnsType("K");
		UserSnsVO userInfo = userSnsService.getKakaoUserInfo(resVO.getAccessToken());
		
		
		//DB처리
		if(!userInfo.getSuccess().equals("Y")){

			view.addObject("success", "N");
    		view.addObject("message", "[카카오로그인] 사용자 정보조회를 실패하였습니다.");
			return view;
        	
		} else {
			UserSnsVO resUserSnsVO = userSnsService.getUserSns(userInfo);
			if(resUserSnsVO == null){
				
				SessionVO loginVO = SessionUtil.getSession(request);

				if (loginVO == null) {
					//캔디계정X, SNS계정X
		        	view.addObject("success", "J");
		        	request.getSession().setAttribute("snsType", "K");
		        	request.getSession().setAttribute("snsEmail", userInfo.getSnsEMail());
		        	request.getSession().setAttribute("snsName", userInfo.getSnsName());
					request.getSession().setAttribute("accessToken", resVO.getAccessToken());
					request.getSession().setAttribute("refreshToken", resVO.getRefreshToken());
					return view;
				} else {
					//mypage에서 연동을 하였을 경우
					userInfo.setUserId(loginVO.getLoginId());
					userInfo.setRefreshToken(resVO.getRefreshToken());
					userSnsDAO.insUserSns(userInfo);

            		view.addObject("success", "Y");
					view.addObject("type", "MYPAGE");
					return view;
				}
			} else {
				//캔디계정O, SNS계정O
				
				//refresh token update
				UserSnsVO snsTokenVO = new UserSnsVO();
				snsTokenVO.setUserSnsId(resUserSnsVO.getUserSnsId());
				snsTokenVO.setRefreshToken(resVO.getRefreshToken());
				userSnsService.updUserSnsToken(snsTokenVO);
				
				SessionVO loginVO = userSnsService.snsLoginProcess(resUserSnsVO);

	            if (loginVO != null) {
	            	if("Y".equals(loginVO.getLoginLockYn())) {
	            		view.addObject("success", "N");
	            		view.addObject("message", "비밀번호 5회 오류로 계정이 잠겼습니다. " + loginVO.getUnlockTime() + "초 뒤에 로그인이 가능합니다.");
	        			return view;
	                } else {
                        //로그인 성공시에 암호화 키값 셋팅
                        //loginVO.setParamEncryptPassword(ConfigUtil.getString("parameter.encrypt.password"));
                        SessionUtil.Login(request, loginVO);
                        int curPoint = 0;

                        // 접속기업의 포인트
                        PointVO pVO = new PointVO();
                        pVO.setBizId(loginVO.getBizId());
                        PointVO pointVO = pointService.getPoint(pVO);

                        if (pointVO != null) curPoint = pointVO.getCurPoint();

                        System.out.println("# curPoint : " + curPoint);

                        if (!StringUtil.isNotNull(loginVO.getUserName())) loginVO.setUserName("-");
                        view.addObject("loginId", loginVO.getLoginId());
                        view.addObject("userId", loginVO.getUserId());
                        view.addObject("userName", loginVO.getUserName());
                        view.addObject("userType", loginVO.getUserType());
                        view.addObject("bizId", loginVO.getBizId());
                        view.addObject("bizName", loginVO.getBizName());
                        view.addObject("curPoint", curPoint);
                        view.addObject("message", messageProperties.getProperty("login.success"));
                        view.addObject("useContractMenu", loginVO.getUseContract());

                        EgovHttpSessionBindingListener listener = new EgovHttpSessionBindingListener();
                        request.getSession().setAttribute(loginVO.getLoginId(), listener);
                        
        	        	view.addObject("success", "Y");
        				return view;
	                }
	            } else {
	            	view.addObject("success", "N");
	                view.addObject("message", messageProperties.getProperty("login.fail"));
    				return view;
	            }
			}
		}
    }
    
    @RequestMapping(value = "naverLoginCallback.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView naverLoginCallbackCtrl(HttpServletRequest request, @RequestParam String state, @RequestParam String code) throws Exception {
    	
    	System.out.println(":::::::::::::::::::: naverLoginCallback :::::::::::::::::::");
    	
    	ModelAndView view = new ModelAndView();
    	view.setViewName("/member/member_login_callback");
    	
    	UserSnsVO resVO = null;
    	
    	if(StringUtil.isNotNull(code)){
    		System.out.println(request.getSession().getAttribute("uuid"));
    		String sessionCode = request.getSession().getAttribute("uuid").toString();
    		if(sessionCode.equals(state)){
    			resVO = userSnsService.getNaverAccessToken(state, code);
    		} else {
    			view.addObject("success", "N");
    			view.addObject("message", "인증 요청값과 결과값이 동일하지 않습니다.");
				return view;
    		}
    	}
    	
    	if(resVO == null){
    		view.addObject("success", "N");
    		view.addObject("message", "[네이버로그인] 결과값이 존재하지 않습니다.");
			return view;
    	}
    	
    	if( StringUtil.isNull(resVO.getAccessToken())){
    		view.addObject("success", "N");
    		view.addObject("message", "[네이버로그인] 엑세스 토큰 조회에 실패하였습니다.");
			return view;
    	}
    	
    	if( StringUtil.isNull(resVO.getRefreshToken())){
    		view.addObject("success", "N");
    		view.addObject("message", "[네이버로그인] 갱신 토큰 조회에 실패하였습니다.");
			return view;
    	}
    	
    	if(!resVO.getSuccess().equals("Y")){
    		view.addObject("success", "N");
    		view.addObject("message", resVO.getMessage());
			return view;
    	}
    	
    	resVO.setSnsType("N");
		UserSnsVO userInfo = userSnsService.getNaverUserInfo(resVO.getAccessToken());
		
		
		//DB처리
		if(!userInfo.getSuccess().equals("Y")){

			view.addObject("success", "N");
    		view.addObject("message", userInfo.getMessage());
			return view;
        	
		} else {
			UserSnsVO resUserSnsVO = userSnsService.getUserSns(userInfo);
			if(resUserSnsVO == null){
				SessionVO loginVO = SessionUtil.getSession(request);
				if (loginVO == null) {
					//캔디계정X, SNS계정X
		        	view.addObject("success", "J");
		        	request.getSession().setAttribute("snsType", "N");
		        	request.getSession().setAttribute("snsEmail", userInfo.getSnsEMail());
		        	request.getSession().setAttribute("snsName", userInfo.getSnsName());
					request.getSession().setAttribute("accessToken", resVO.getAccessToken());
					request.getSession().setAttribute("refreshToken", resVO.getRefreshToken());
					return view;
				} else {
					//mypage에서 연동을 하였을 경우
					userInfo.setUserId(loginVO.getLoginId());
					userInfo.setRefreshToken(resVO.getRefreshToken());
					userSnsDAO.insUserSns(userInfo);

            		view.addObject("success", "Y");
					view.addObject("type", "MYPAGE");
					return view;
				}
			} else {
				//캔디계정O, SNS계정O
				
				//refresh token update
				UserSnsVO snsTokenVO = new UserSnsVO();
				snsTokenVO.setUserSnsId(resUserSnsVO.getUserSnsId());
				snsTokenVO.setRefreshToken(resVO.getRefreshToken());
				userSnsService.updUserSnsToken(snsTokenVO);
				
				SessionVO loginVO = userSnsService.snsLoginProcess(resUserSnsVO);

	            if (loginVO != null) {
	            	if("Y".equals(loginVO.getLoginLockYn())) {
	            		view.addObject("success", "N");
	            		view.addObject("message", "비밀번호 5회 오류로 계정이 잠겼습니다. " + loginVO.getUnlockTime() + "초 뒤에 로그인이 가능합니다.");
	        			return view;
	                } else {
                        //로그인 성공시에 암호화 키값 셋팅
                        //loginVO.setParamEncryptPassword(ConfigUtil.getString("parameter.encrypt.password"));
                        SessionUtil.Login(request, loginVO);
                        int curPoint = 0;

                        // 접속기업의 포인트
                        PointVO pVO = new PointVO();
                        pVO.setBizId(loginVO.getBizId());
                        PointVO pointVO = pointService.getPoint(pVO);

                        if (pointVO != null) curPoint = pointVO.getCurPoint();

                        System.out.println("# curPoint : " + curPoint);

                        if (!StringUtil.isNotNull(loginVO.getUserName())) loginVO.setUserName("-");
                        view.addObject("loginId", loginVO.getLoginId());
                        view.addObject("userId", loginVO.getUserId());
                        view.addObject("userName", loginVO.getUserName());
                        view.addObject("userType", loginVO.getUserType());
                        view.addObject("bizId", loginVO.getBizId());
                        view.addObject("bizName", loginVO.getBizName());
                        view.addObject("curPoint", curPoint);
                        view.addObject("useContractMenu", loginVO.getUseContract());
                        view.addObject("message", messageProperties.getProperty("login.success"));

                        EgovHttpSessionBindingListener listener = new EgovHttpSessionBindingListener();
                        request.getSession().setAttribute(loginVO.getLoginId(), listener);
                        
        	        	view.addObject("success", "Y");
        				return view;
	                }
	            } else {
	            	view.addObject("success", "N");
	                view.addObject("message", messageProperties.getProperty("login.fail"));
    				return view;
	            }
			}
		}
    }
    
    @RequestMapping(value = "googleLoginCallback.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView googleLoginCallbackCtrl(HttpServletRequest request, @RequestParam String code) throws Exception {
    	
    	System.out.println(":::::::::::::::::::: googleLoginCallback :::::::::::::::::::");
    	
    	ModelAndView view = new ModelAndView();
    	view.setViewName("/member/member_login_callback");
    	
    	UserSnsVO resVO = null;
    	
    	if(StringUtil.isNotNull(code)){
    		resVO = userSnsService.getGoogleAccessToken(code, "L");
    	}
    	
    	if(resVO == null){
    		view.addObject("success", "N");
    		view.addObject("message", "[구글로그인] 결과값이 존재하지 않습니다.");
			return view;
    	}
    	
    	if( StringUtil.isNull(resVO.getIdToken())){
    		view.addObject("success", "N");
    		view.addObject("message", "[구글로그인] 아이디 토큰 조회에 실패하였습니다.");
			return view;
    	}

    	if( StringUtil.isNull(resVO.getRefreshToken())){
    		view.addObject("success", "N");
    		view.addObject("message", "[구글로그인] 갱신 토큰 조회에 실패하였습니다.");
			return view;
    	}
    	
    	if(!resVO.getSuccess().equals("Y")){
    		view.addObject("success", "N");
    		view.addObject("message", resVO.getMessage());
			return view;
    	}
    	
    	resVO.setSnsType("K");
		UserSnsVO userInfo = userSnsService.getGoogleUserInfo(resVO.getIdToken(), request);
		
		
		//DB처리
		if(!userInfo.getSuccess().equals("Y")){

			view.addObject("success", "N");
    		view.addObject("message", "[구글로그인] 사용자 정보조회를 실패하였습니다.");
			return view;
        	
		} else {
			UserSnsVO resUserSnsVO = userSnsService.getUserSns(userInfo);
			if(resUserSnsVO == null){
				//캔디계정X, SNS계정X
				SessionVO loginVO = SessionUtil.getSession(request);
				if (loginVO == null) {
					//캔디계정X, SNS계정X
		        	view.addObject("success", "J");
		        	request.getSession().setAttribute("snsType", "G");
		        	request.getSession().setAttribute("snsEmail", userInfo.getSnsEMail());
		        	request.getSession().setAttribute("snsName", userInfo.getSnsName());
					request.getSession().setAttribute("accessToken", resVO.getAccessToken());
					request.getSession().setAttribute("refreshToken", resVO.getRefreshToken());
					return view;
				} else {
					//mypage에서 연동을 하였을 경우
					userInfo.setUserId(loginVO.getLoginId());
					userInfo.setRefreshToken(resVO.getRefreshToken());
					userSnsDAO.insUserSns(userInfo);

            		view.addObject("success", "Y");
					view.addObject("type", "MYPAGE");
					return view;
				}
			} else {
				//캔디계정O, SNS계정O
				
				//refresh token update
				UserSnsVO snsTokenVO = new UserSnsVO();
				snsTokenVO.setUserSnsId(resUserSnsVO.getUserSnsId());
				snsTokenVO.setRefreshToken(resVO.getRefreshToken());
				userSnsService.updUserSnsToken(snsTokenVO);
				
				SessionVO loginVO = userSnsService.snsLoginProcess(resUserSnsVO);

	            if (loginVO != null) {
	            	if("Y".equals(loginVO.getLoginLockYn())) {
	            		view.addObject("success", "N");
	            		view.addObject("message", "비밀번호 5회 오류로 계정이 잠겼습니다. " + loginVO.getUnlockTime() + "초 뒤에 로그인이 가능합니다.");
	        			return view;
	                } else {
                        //로그인 성공시에 암호화 키값 셋팅
                        //loginVO.setParamEncryptPassword(ConfigUtil.getString("parameter.encrypt.password"));
                        SessionUtil.Login(request, loginVO);
                        int curPoint = 0;

                        // 접속기업의 포인트
                        PointVO pVO = new PointVO();
                        pVO.setBizId(loginVO.getBizId());
                        PointVO pointVO = pointService.getPoint(pVO);

                        if (pointVO != null) curPoint = pointVO.getCurPoint();

                        System.out.println("# curPoint : " + curPoint);

                        if (!StringUtil.isNotNull(loginVO.getUserName())) loginVO.setUserName("-");
                        view.addObject("loginId", loginVO.getLoginId());
                        view.addObject("userId", loginVO.getUserId());
                        view.addObject("userName", loginVO.getUserName());
                        view.addObject("userType", loginVO.getUserType());
                        view.addObject("bizId", loginVO.getBizId());
                        view.addObject("bizName", loginVO.getBizName());
                        view.addObject("curPoint", curPoint);
                        view.addObject("message", messageProperties.getProperty("login.success"));
                        view.addObject("useContractMenu", loginVO.getUseContract());

                        EgovHttpSessionBindingListener listener = new EgovHttpSessionBindingListener();
                        request.getSession().setAttribute(loginVO.getLoginId(), listener);
                        
        	        	view.addObject("success", "Y");
        				return view;
	                }
	            } else {
	            	view.addObject("success", "N");
	                view.addObject("message", messageProperties.getProperty("login.fail"));
    				return view;
	            }
			}
		}
    }
    
    //카카오 연동해제
    @RequestMapping(value = "delKakaoLogin.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<JSONObject> delKakaoLoginCtrl(HttpServletRequest request, @ModelAttribute("UserSnsVO") UserSnsVO vo) throws Exception {
    	
    	JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean result = false;
		
    	System.out.println(":::::::::::::::::::: delKakaoLogin :::::::::::::::::::");
    	
    	try {
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
				result = false;
			} else {
				UserSnsVO resultVO = userSnsService.delKakaoLogin(vo);
		    	jsonObject.put("success", resultVO.getSuccess());
		    	jsonObject.put("message", resultVO.getMessage());
			}
    	} catch (Exception ex) {
    		status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			//throw ex;
	    	jsonObject.put("success", "N");
		}
    	
    	return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }
    
    //네이버 연동해제
    @RequestMapping(value = "delNaverLogin.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<JSONObject> delNaverLoginCtrl(HttpServletRequest request, @ModelAttribute("UserSnsVO") UserSnsVO vo) throws Exception {
    	
    	JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean result = false;
		
    	System.out.println(":::::::::::::::::::: delNaverLogin :::::::::::::::::::");
    	
    	try {
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
				result = false;
			} else {
				UserSnsVO resultVO = userSnsService.delNaverLogin(vo);
		    	jsonObject.put("success", resultVO.getSuccess());
		    	jsonObject.put("message", resultVO.getMessage());
			}
    	} catch (Exception ex) {
    		status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			//throw ex;
	    	jsonObject.put("success", "N");
		}
    	
    	return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }
    
    //구글 연동해제
    @RequestMapping(value = "delGoogleLogin.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<JSONObject> delGoogleLoginCtrl(HttpServletRequest request, @ModelAttribute("UserSnsVO") UserSnsVO vo) throws Exception {
    	
    	JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean result = false;
		
    	System.out.println(":::::::::::::::::::: delGoogleLogin :::::::::::::::::::");
    	
    	try {
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
				result = false;
			} else {
				UserSnsVO resultVO = userSnsService.delGoogleLogin(vo);
		    	jsonObject.put("success", resultVO.getSuccess());
		    	jsonObject.put("message", resultVO.getMessage());
			}
    	} catch (Exception ex) {
    		status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			//throw ex;
	    	jsonObject.put("success", "N");
		}
    	
    	return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }
}
