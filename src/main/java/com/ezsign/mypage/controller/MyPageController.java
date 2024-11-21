package com.ezsign.mypage.controller;


import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.metamodel.StaticMetamodel;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;

import com.ezsign.bbs.service.BbsService;
import com.ezsign.bbs.vo.BbsContentsVO;
import com.ezsign.biz.service.BizService;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.emp.service.EmpService;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.system.dao.YS030DAO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.user.dao.UserDAO;
import com.ezsign.user.service.UserService;
import com.ezsign.user.service.UserSnsService;
import com.ezsign.user.vo.UserSnsVO;
import com.ezsign.user.vo.UserVO;



@Controller
@RequestMapping("/mypage/")
public class MyPageController extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "bizService")
	private BizService bizService;
	
	@Resource(name = "empService")
	private EmpService empService;

	@Resource(name = "bbsService")
	private BbsService bbsService;
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name = "userSnsService")
	private UserSnsService userSnsService;
	
	@Resource(name="userDAO")
	private UserDAO userDAO;
	
	@Resource(name="ys030DAO")
	private YS030DAO ys030DAO;
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST} , value = "getMyPage.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getMyPageCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception
	{
		// 마이페이지
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getMyPage :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				vo.setStartPage(0);
				vo.setEndPage(10);
				
				// 개인정보
				List<EmpVO> result= empService.getEmpList(vo, loginVO);
				
				// 문의게시판(기업관리자 문의)
				BbsContentsVO bbsContentsVO = new BbsContentsVO();
				bbsContentsVO.setBizId(loginVO.getBizId());
				bbsContentsVO.setBbsId("");
				bbsContentsVO.setUserId(loginVO.getUserId());
				bbsContentsVO.setStartPage(0);
				bbsContentsVO.setEndPage(5);
				bbsContentsVO.setSortName("A.INS_DATE");
				bbsContentsVO.setSortOrder("DESC");
				
				List<BbsContentsVO> bbsList = bbsService.getBbsContentsList(bbsContentsVO);
				
				UserSnsVO userSnsVO = new UserSnsVO();
				userSnsVO.setUserId(loginVO.getLoginId());
				List<UserSnsVO> userSnsList = userSnsService.getUserSnsList(userSnsVO);
				
				//SNS로그인 계정 확인
				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
				jsonObject.put("sns", userSnsList);
				jsonObject.put("bbs", bbsList);
				
				String uuid = UUID.randomUUID().toString();
				String naverLoginUrl = "https://nid.naver.com/oauth2.0/authorize?";
				naverLoginUrl += "response_type=code";
				naverLoginUrl += "&client_id=" + UserSnsService.NAVER_CLIENT_ID;
				naverLoginUrl += "&redirect_uri=" + UserSnsService.NAVER_LOGIN_REDIRECT_URI;
				naverLoginUrl += "&state=" + uuid;

				String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?";
				kakaoLoginUrl += "&client_id=" + UserSnsService.KAKAO_CLIENT_ID;
				kakaoLoginUrl += "&redirect_uri=" + UserSnsService.KAKAO_LOGIN_REDIRECT_URI;
				kakaoLoginUrl += "&response_type=code";
				
				String googleLoginUrl = "";
				googleLoginUrl = "https://accounts.google.com/o/oauth2/v2/auth?";
				googleLoginUrl += "client_id=" + UserSnsService.GOOGLE_CLIENT_ID;
				googleLoginUrl += "&nonce=" + uuid;
				googleLoginUrl += "&response_type=code";
				googleLoginUrl += "&redirect_uri="+UserSnsService.GOOGLE_LOGIN_REDIRECT_URI;
				googleLoginUrl += "&scope=openid%20profile%20email";
				googleLoginUrl += "&access_type=offline";
				googleLoginUrl += "&prompt=consent";

				request.getSession().removeAttribute("uuid");
				request.getSession().setAttribute("uuid", uuid);
				
				jsonObject.put("naverUrl", naverLoginUrl);
				jsonObject.put("kakaoUrl", kakaoLoginUrl);
				jsonObject.put("googleUrl", googleLoginUrl);
				
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST} , value = "getMyPageFlutter.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getMyPageFlutterCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception
	{
		// 마이페이지
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getMyPageFlutter :::::::::::::::::::");

			//SessionVO loginVO = SessionUtil.getSession(request);
			
			if (vo.getUserId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+vo.getBizId());
//				vo.setBizId(loginVO.getBizId());
//				vo.setUserId(loginVO.getUserId());
				vo.setStartPage(0);
				vo.setEndPage(10);
				SessionVO loginVO = new SessionVO();
				
				// 개인정보
				List<EmpVO> result= empService.getEmpList(vo, loginVO);
				
				// 문의게시판(기업관리자 문의)
				BbsContentsVO bbsContentsVO = new BbsContentsVO();
				bbsContentsVO.setBizId(vo.getBizId());
				bbsContentsVO.setBbsId("");
				bbsContentsVO.setUserId(vo.getUserId());
				bbsContentsVO.setStartPage(0);
				bbsContentsVO.setEndPage(5);
				bbsContentsVO.setSortName("A.INS_DATE");
				bbsContentsVO.setSortOrder("DESC");
				
				List<BbsContentsVO> bbsList = bbsService.getBbsContentsList(bbsContentsVO);
				
				UserSnsVO userSnsVO = new UserSnsVO();
				userSnsVO.setUserId(vo.getLoginId());
				List<UserSnsVO> userSnsList = userSnsService.getUserSnsList(userSnsVO);
				
				//SNS로그인 계정 확인
				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
				jsonObject.put("sns", userSnsList);
				jsonObject.put("bbs", bbsList);
				
				String uuid = UUID.randomUUID().toString();
				String naverLoginUrl = "https://nid.naver.com/oauth2.0/authorize?";
				naverLoginUrl += "response_type=code";
				naverLoginUrl += "&client_id=" + UserSnsService.NAVER_CLIENT_ID;
				naverLoginUrl += "&redirect_uri=" + UserSnsService.NAVER_LOGIN_REDIRECT_URI;
				naverLoginUrl += "&state=" + uuid;

				String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?";
				kakaoLoginUrl += "&client_id=" + UserSnsService.KAKAO_CLIENT_ID;
				kakaoLoginUrl += "&redirect_uri=" + UserSnsService.KAKAO_LOGIN_REDIRECT_URI;
				kakaoLoginUrl += "&response_type=code";
				
				String googleLoginUrl = "";
				googleLoginUrl = "https://accounts.google.com/o/oauth2/v2/auth?";
				googleLoginUrl += "client_id=" + UserSnsService.GOOGLE_CLIENT_ID;
				googleLoginUrl += "&nonce=" + uuid;
				googleLoginUrl += "&response_type=code";
				googleLoginUrl += "&redirect_uri="+UserSnsService.GOOGLE_LOGIN_REDIRECT_URI;
				googleLoginUrl += "&scope=openid%20profile%20email";
				googleLoginUrl += "&access_type=offline";
				googleLoginUrl += "&prompt=consent";

				request.getSession().removeAttribute("uuid");
				request.getSession().setAttribute("uuid", uuid);
				
				jsonObject.put("naverUrl", naverLoginUrl);
				jsonObject.put("kakaoUrl", kakaoLoginUrl);
				jsonObject.put("googleUrl", googleLoginUrl);
				
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 비밀번호 변경
	@RequestMapping(method = RequestMethod.POST , value = "pwdChange.do")
	@ResponseBody
	public ResponseEntity<String> pwdChangeCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		responseHeaders.add("Access-Control-Allow-Origin", "*");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: pwdChange :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
		
				System.out.println("userId=>"+vo.getUserId());
				
				int result= userService.updUserPwd(vo);
				
				if(result == 1){
					success = true;
					jsonObject.put("message", "비밀번호가 변경 되었습니다.");
				} else {
					jsonObject.put("message", "이전 비밀번호가 일치하지 않습니다.");
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
	}
	
	// 비밀번호 변경
		@RequestMapping(method = RequestMethod.POST , value = "pwdChangeFlutter.do")
		@ResponseBody
		public ResponseEntity<String> pwdChangeFlutterCtrl(@RequestBody UserVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
		{
			
			JSONObject jsonObject = new JSONObject();
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
			responseHeaders.add("Access-Control-Allow-Origin", "*");
			
			HttpStatus status = HttpStatus.OK;
			boolean success = false;
			
			try {
				System.out.println(":::::::::::::::::::: pwdChangeFlutter :::::::::::::::::::");

				//SessionVO loginVO = SessionUtil.getSession(request);
				
				if (vo.getUserId() == null) {
					status = HttpStatus.UNAUTHORIZED;
				} else {
			
					System.out.println("userId=>"+vo.getUserId());
					
					int result= userService.updUserPwd(vo);
					
					if(result == 1){
						success = true;
						jsonObject.put("message", "비밀번호가 변경 되었습니다.");
					} else {
						jsonObject.put("message", "이전 비밀번호가 일치하지 않습니다.");
					}
				}
			} catch (Exception ex) {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				logService.error(ex.getMessage(), new Throwable(ex));
				throw ex;
			}
			jsonObject.put("success", success);
			return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
		}
	
	// 기업정보 조회
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST} , value = "getMyPageBizInfo.do")
	@ResponseBody
	public ResponseEntity<String> getMyPageBizInfoCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		responseHeaders.add("Access-Control-Allow-Origin", "*");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getMyPageBizInfo :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				// 기업정보 보회
				BizVO bizVO = new BizVO();
				bizVO.setBizId(loginVO.getBizId());
				bizVO.setStartPage(0);
				bizVO.setEndPage(1);
				List<BizVO> bizList = bizService.getBizList(bizVO);
				
				// 기업담당자 정보 조회
				EmpVO empVO = new EmpVO();
				empVO.setBizId(loginVO.getBizId());
				empVO.setUserId(loginVO.getUserId());
				EmpVO empResultVO = empService.getEmp(empVO, loginVO);
				
				// 사용자 정보 조회
				UserVO userVO = new UserVO();
				userVO.setUserId(empResultVO.getLoginId());
				UserVO userResultVO = userDAO.loginProcess(userVO);
				
				jsonObject.put("total", bizList.size());
				jsonObject.put("bizInfo", bizList);
				jsonObject.put("empInfo", empResultVO);
				jsonObject.put("userInfo", userResultVO);
				success = true;
				
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
	}
	
	// 기업정보 수정
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.GET}, value = "updMyPageBizInfo.do")
	@ResponseBody
	public ResponseEntity<String> updMyPageBizInfoCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		responseHeaders.add("Access-Control-Allow-Origin", "*");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: updMyPageBizInfo :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
		
				System.out.println("userId=>"+vo.getUserId());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				int result = 0;
				
				BizVO bizVO = new BizVO();
				bizVO.setBizId(vo.getBizId());
				bizVO.setOwnerName(vo.getOwnerName());
				bizVO.setBusinessNo(vo.getBusinessNo());
				bizVO.setPinNo(vo.getPinNo());
				bizVO.setAddr1(vo.getAddr1());
				bizVO.setAddr2(vo.getAddr2());
				bizVO.setCompanyTelNum(vo.getCompanyTelNum());
				bizVO.setCompanyFaxNum(vo.getCompanyFaxNum());
				
				// 기업정보 수정
				bizService.updBiz(bizVO);
				
				// 사업장정보 본점 수정
				if(StringUtil.isNotNull(vo.get계약ID())) {
					YS030VO keyDataVO = new YS030VO();
					keyDataVO.setBizId(vo.getBizId());
					keyDataVO.set계약ID(vo.get계약ID());
					keyDataVO.set지점여부("0");
					keyDataVO.setStartPage(vo.getStartPage());
					keyDataVO.setEndPage(vo.getEndPage());
					List<YS030VO> ys030List = ys030DAO.getYS030List(keyDataVO);
					
					if(ys030List.size() > 0) {
						YS030VO ys030VO = ys030List.get(0);
						keyDataVO.set계약ID(ys030VO.get계약ID());
						keyDataVO.set사업장ID(ys030VO.get사업장ID());
						keyDataVO.set대표자명(bizVO.getOwnerName());
						keyDataVO.set사업자등록번호(bizVO.getBusinessNo());
						keyDataVO.set회사주소1(bizVO.getAddr1());
						keyDataVO.set회사주소2(bizVO.getAddr2());
						keyDataVO.set회사전화1(bizVO.getCompanyTelNum());
						keyDataVO.set회사팩스1(bizVO.getCompanyFaxNum());
						keyDataVO.set법인등록번호_개인식별번호(bizVO.getPinNo());
						ys030DAO.updYS030(keyDataVO);
					}
				}
				
				// 담당자 정보 수정
				empService.updEmp(vo);
				
				// 사용자 정보 수정
				UserVO userVO = new UserVO();
				userVO.setUserId(vo.getLoginId());
				userVO.setUserName(vo.getEmpName());
				userService.updUser(userVO);
				
				result++;
				
				if(result>0) success = true;
				
				jsonObject.put("total", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
	}

}
