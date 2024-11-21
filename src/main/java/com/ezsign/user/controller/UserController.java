package com.ezsign.user.controller;


import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ezsign.user.vo.*;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

import com.ezsign.biz.service.BizService;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.emp.service.EmpService;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.mail.MailVO;
import com.ezsign.framework.mail.MultiPartEmail;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.user.service.UserService;
import com.ezsign.user.service.UserSnsService;
import com.jarvis.common.util.FileUtil;

@Controller
@RequestMapping("/user/")
public class UserController extends BaseController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "empService")
	private EmpService empService;

	@Resource(name = "bizService")
	private BizService bizService;

	@Resource(name = "userSnsService")
	private UserSnsService userSnsService;

	@RequestMapping(method = RequestMethod.POST , value = "insUser.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insUserCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request) throws Exception
	{
		// 사용자 서비스 가입
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: insUser :::::::::::::::::::");

			String szMonthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getTimeStamp(14);
			String szSavePath = System.getProperty("system.stamp.path")+szMonthPath;
			List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
			int total = resultFileList.size();

			if(total>0) {
				// 전달받은 직인이미지 리스트
				for(int i=0;i<resultFileList.size();i++) {
					FileVO fileVO = resultFileList.get(i);
					String imgPath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
					vo.setCompanyImage(imgPath);
				}
			}

			int result = userService.insUser(vo);

			if(result == -100) {
				jsonObject.put("message", "이미 가입한 회사입니다.");
			} else {
				//sns로그인
				logger.info("SNS계정 연동을 진행합니다.");
				UserSnsVO resSnsVO = userSnsService.insUserSns(vo, request);
				logger.info("sns연동 결과 : "+resSnsVO.getStatusCode());
			}

			request.getSession().removeAttribute("accessToken");
			request.getSession().removeAttribute("refreshToken");
			request.getSession().removeAttribute("snsEmail");
			request.getSession().removeAttribute("snsName");
			request.getSession().removeAttribute("idToken");
			request.getSession().removeAttribute("snsType");

			if(result > 0) success = true;

		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "insUserforFlutter.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insUserFlutterCtrl(@RequestBody UserVO vo, HttpServletRequest request) throws Exception
	{
		return insUserCtrl(vo, request);
	}

	@RequestMapping(method = RequestMethod.GET , value = "getUserList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getUserListCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request) throws Exception
	{
		// 게약목록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: getUserList :::::::::::::::::::");

			List<UserVO> result= userService.getUserList(vo);

			jsonObject.put("total", result.size());
			jsonObject.put("data", result);
			success = true;
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	@RequestMapping(method = RequestMethod.GET , value = "findUserId.do")
	@ResponseBody
	public ResponseEntity<JSONObject> findUserIdCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request) throws Exception
	{
		// 게약목록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: findUserId :::::::::::::::::::");

			List<UserVO> result= userService.findUser(vo);

			jsonObject.put("total", result.size());
			jsonObject.put("data", result);
			success = true;
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	@RequestMapping(method = RequestMethod.GET , value = "findUserPwd.do")
	@ResponseBody
	public ResponseEntity<JSONObject> findUserPwdCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request) throws Exception
	{
		// 게약목록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: findUserPwd :::::::::::::::::::");

			List<UserVO> result= userService.findUser(vo);
			if(result.size()>0) {
				UserVO userVO = result.get(0);
				String userNonce = userService.getUserNonce();

				UserVO nonceVO = new UserVO();
				nonceVO.setUserId(userVO.getUserId());
				nonceVO.setUserNonce(userNonce);
				int nonceCount = userService.updUser(nonceVO);

				String toEmail = "";

				if(Integer.parseInt(userVO.getUserType()) >= 5){ // 기업 중간관리자 이상
					toEmail = userVO.getEMail(); // 인사정보에 등록된 이메일로 전송
				} else{
					toEmail = userVO.getUserId();
				}

				if(nonceCount>0) {
					// 사용자 ID로 변경할 이메일 발송
					String content = "";

					content +="<!DOCTYPE html> ";
					content +="<html> ";
					content +="<head> ";
					content +="<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
					content +="<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
					content +="<title>비밀번호 재설정</title> ";
					content +="</head> ";
					content +="<body> ";
					content +="	<div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
					content +="		<div style=\" padding: 37px 0 0 0;\"> ";
					content +="			<div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
					content +="				<span style=\"font-weight: bold; color:#00a9e9;\">IEUM</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;SIGN</span> ";
					content +="			</div> ";
					content +="			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
					content +="				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">비밀번호 재설정</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br> ";
					content +="				이메일 회신입니다.</span> ";
					content +="			</div> ";
					content +="			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
					content +="				<span >"+userVO.getUserName()+"</span><span>님!<br> ";
					content +="				하단 재설정 버튼 클릭하여 비밀번호를 재설정을 할 수 있습니다.<br> ";
					content +="				비밀번호를 재설정 해주세요. ";
					content +="				</span> ";
					content +="			</div> ";
					content +="			<div style=\"margin-top: 74px; text-align: center;\"> ";
					content +="				<div style=\"width: 255px; height: 43px; padding-top: 15px; margin-bottom: 32px; background-color: #00a9e9; border-radius: 8px; display: inline-block; text-align: center; cursor: pointer;\"> ";
					content +="					<a href=\""+System.getProperty("system.feb.url")+"menu/goUserPwd.do?id="+userNonce+"\" style=\"color: #fff; font-size: 18px; font-weight: bold; font-family: 'tahoma'; text-decoration: none;\">비밀번호 재설정  </a> ";
					content +="				</div> ";
					content +="			</div> ";
					content +="		</div> ";
					content +="		<div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
					content +="			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
					content +="				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
					content +="				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">service@newzenpnp.com</a></span > ";
					content +="				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
					content +="			</div> ";
					content +="		</div> ";
					content +="	</div> ";
					content +="</body> ";
					content +="</html> ";

					MultiPartEmail email = new MultiPartEmail();
					MailVO mailVO = new MailVO();
					mailVO.setFrom("no_reply@newzenpnp.com");
					mailVO.setTo(toEmail);
					mailVO.setCc("");
					mailVO.setBcc("");
					mailVO.setSubject(" [이음싸인] "+userVO.getUserName()+"님의 비밀번호 재설정 이메일입니다.");
					mailVO.setText(content);

					logger.info("IeumSign 비밀번호 재설정 이메일을 발송. email : " + toEmail + " userNonce : " + userNonce);

					success = email.send(mailVO);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "resetUserPwd.do")
	@ResponseBody
	public ResponseEntity<JSONObject> resetUserPwdCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request) throws Exception {
		// 메일로 비밀번호 재설정
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: resetUserPwd :::::::::::::::::::");
			int total = userService.resetUserPwd(vo);
			if (total>0) success = true;
			jsonObject.put("total", total);
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "resetUserPwdByDate.do")
	@ResponseBody
	public ResponseEntity<JSONObject> resetUserPwdByDate(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			// 생년월일 8자리로 비밀번호 초기화
			logger.info(":::::::::::::::::::: resetUserPwd :::::::::::::::::::");
			logger.info("[resetUserPwd] loginId =>"+vo.getUserId()+", passwd(userDate) =>"+vo.getUserPwd());
			int total = 0;
			total 	  = userService.resetUserPwdByDate(vo);
			if (total > 0) success = true;

			jsonObject.put("total", total);
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET , value = "insUserCert.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insUserCertCtrl(@ModelAttribute("UserCertVO") UserCertVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		responseHeaders.add("Access-Control-Allow-Origin", "*");
		HttpStatus status = HttpStatus.OK;
		boolean success	  = false;
		int result		  = 0;

		try {
			logger.info(":::::::::::::::::::: insUserCert :::::::::::::::::::");
			logger.info("reqNum=>"+vo.getReqNum());
			userService.insUserCert(vo);
			success = true;
		} catch (Exception ex) { logService.error(ex.getMessage(), new Throwable(ex)); }
		jsonObject.put("success", success);
		jsonObject.put("total", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	@RequestMapping(method = RequestMethod.POST , value = "insUserMok.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insUserMokCtrl(@ModelAttribute("UserMokVO") UserMokVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		responseHeaders.add("Access-Control-Allow-Origin", "*");
		HttpStatus status = HttpStatus.OK;
		boolean success	  = false;
		int result		  = 0;

		try {
			logger.info(":::::::::::::::::::: insUserMok :::::::::::::::::::");
			userService.insUserMok(vo);
			success = true;
		} catch (Exception ex) { logService.error(ex.getMessage(), new Throwable(ex)); }
		jsonObject.put("success", success);
		jsonObject.put("total", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	@RequestMapping(method = RequestMethod.POST , value = "insUserJoinCert.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insUserJoinCertCtrl(@ModelAttribute("UserJoinCertVO") UserJoinCertVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: insUserJoinCert :::::::::::::::::::");
			int total = 0;

			EmpVO empVO = new EmpVO();
			empVO.setBizId(vo.getBizId());
			empVO.setUserId(vo.getUserId());
			empVO.setStartPage(0);
			empVO.setEndPage(1);

			logger.info(empVO.toString());

			SessionVO loginVO   = new SessionVO();
			List<EmpVO> empList = empService.getEmpList(empVO, loginVO);

			if (!empList.isEmpty()) {
				total = empList.size();
				EmpVO resultEmpVO = empList.get(0);

				String certNo = Integer.toString(StringUtil.getRandomNumber(4));
				vo.setStatusCode("REQ");
				vo.setPhonNo(resultEmpVO.getPhoneNum());
				vo.setCertNo(certNo);
				vo.setReqDate(DateUtil.getTimeStamp(7));
				vo.setBizId(resultEmpVO.getBizId());
				vo.setBizName(resultEmpVO.getBizName());
				vo.setEmpName(resultEmpVO.getEmpName());
				vo.setPhoneNum(resultEmpVO.getPhoneNum());
				userService.insUserJoinCert(vo);

				jsonObject.put("data", resultEmpVO.getUserId());
			}

			jsonObject.put("total", total);
			success = true;
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "insUserJoinCertForFlutter.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insUserJoinCertFlutterCtrl(@RequestBody UserJoinCertVO vo, HttpServletRequest request) throws Exception {
		return insUserJoinCertCtrl(vo, request);
	}

	//동일한 인사정보를 가진 직원 조회
	@RequestMapping(method = RequestMethod.POST , value = "getCertEmp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getCertEmpCtrl(@ModelAttribute("UserJoinCertVO") UserJoinCertVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getCertEmp :::::::::::::::::::");

			EmpVO empVO = new EmpVO();
			empVO.setBizId(vo.getBizId());
			empVO.setUserDate(vo.getUserDate());
			empVO.setEmpName(vo.getEmpName());
			empVO.setPhoneNum(vo.getPhoneNum());
			empVO.setEmpType("1");
			empVO.setStartPage(0);
			empVO.setEndPage(100);
			empVO.setLoginIdIsNull("Y");

			SessionVO loginVO   = new SessionVO();
			List<EmpVO> empList = empService.getEmpList(empVO, loginVO);

			EmpVO joinEmpVO = new EmpVO();
			joinEmpVO.setBizId(vo.getBizId());
			joinEmpVO.setUserDate(vo.getUserDate());
			joinEmpVO.setEmpName(vo.getEmpName());
			joinEmpVO.setPhoneNum(vo.getPhoneNum());
			joinEmpVO.setEmpType("1");
			joinEmpVO.setStartPage(0);
			joinEmpVO.setEndPage(100);
			List<EmpVO> joinEmpList = empService.getEmpList(joinEmpVO, loginVO);

			jsonObject.put("data", empList);
			jsonObject.put("total", empList.size());
			jsonObject.put("join_total", joinEmpList.size());

			success = true;
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	//동일한 인사정보를 가진 직원 조회
	@RequestMapping(method = RequestMethod.POST , value = "getCertEmpforFlutter.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getCertEmpFlutterCtrl(@RequestBody UserJoinCertVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getCertEmp :::::::::::::::::::");

			EmpVO empVO = new EmpVO();
			empVO.setBizId(vo.getBizId());
			empVO.setUserDate(vo.getUserDate());
			empVO.setEmpName(vo.getEmpName());
			empVO.setPhoneNum(vo.getPhoneNum());
			empVO.setEmpType("1");
			empVO.setStartPage(0);
			empVO.setEndPage(100);
			empVO.setLoginIdIsNull("Y");
			empVO.setEndDateIsNull("Y");

			SessionVO loginVO   = new SessionVO();
			List<EmpVO> empList = empService.getEmpList(empVO, loginVO);

			EmpVO joinEmpVO = new EmpVO();
			joinEmpVO.setBizId(vo.getBizId());
			joinEmpVO.setUserDate(vo.getUserDate());
			joinEmpVO.setEmpName(vo.getEmpName());
			joinEmpVO.setPhoneNum(vo.getPhoneNum());
			joinEmpVO.setEmpType("1");
			joinEmpVO.setStartPage(0);
			joinEmpVO.setEndPage(100);
			joinEmpVO.setEndDateIsNull("Y");
			List<EmpVO> joinEmpList = empService.getEmpList(joinEmpVO, loginVO);

			jsonObject.put("data", empList);
			jsonObject.put("total", empList.size());
			jsonObject.put("join_total", joinEmpList.size());

			success = true;
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 인증번호 가져오기
	@RequestMapping(method = RequestMethod.GET , value = "getUserJoinCert.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getUserJoinCertCtrl(@ModelAttribute("UserJoinCertVO") UserJoinCertVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getUserJoinCert :::::::::::::::::::");
			int total = userService.getUserJoinCert(vo);
			jsonObject.put("total", total);
			success = true;
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 사용자 그룹권한관리 등록
	@RequestMapping(method = RequestMethod.POST , value = "insUserGrp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insUserGrpCtrl(@ModelAttribute("UserGrpVO") UserGrpVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: insUserGrp :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				String szMonthPath = MultipartFileUtil.SEPERATOR+DateUtil.getTimeStamp(14);
				String szSavePath  = System.getProperty("system.stamp.path")+szMonthPath;
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = resultFileList.size();

				if (total>0) {
					// 전달받은 직인이미지 리스트
                    for (FileVO fileVO : resultFileList) {
                        String imgPath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
                        vo.setCompanyImage(imgPath);
                    }
				}

				int result 	  = 0;
				boolean trans = false;

				int userType = Integer.parseInt(loginVO.getUserType());
				if (userType == 5) {
					// 기업중간관리자이면서 조직, 사용자만 등록
					if (vo.getGrpType().equals("O") || vo.getGrpType().equals("U")) trans = true;
				} else if(userType > 5 ) {
					// 기업만 승인요청 진행 (현재는 전체 승인완료로 처리)
					if (vo.getGrpType().equals("B")) vo.setStatusCode("REQ");
					else 							 vo.setStatusCode("RES");
					trans = true;
				}

				if (trans) {
					// 기업으로 권한등록
					if (vo.getGrpType().equals("B")) {
						BizVO bizVO = new BizVO();
						bizVO.setBizId(loginVO.getBizId());
						bizVO.setBizName(vo.getBizName());
						bizVO.setBusinessNo(vo.getBusinessNo());
						bizVO.setAddr1(vo.getAddr1());
						bizVO.setCompanyTelNum(vo.getCompanyTelNum());
						bizVO.setCompanyFaxNum(vo.getCompanyFaxNum());
						if (total>0) bizVO.setCompanyImage(vo.getCompanyImage());
						result = userService.insUserGrpBiz(bizVO);
						if (result == -100) jsonObject.put("message", "이미 가입하신 기업입니다.");
						if (result>0) success = true;
					} else {
						userService.insUserGrp(vo);
						success = true;
					}
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 사용자 그룹권한관리 등록
	@RequestMapping(method = RequestMethod.POST , value = "delUserGrp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delUserGrpCtrl(@ModelAttribute("UserGrpVO") UserGrpVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: delUserGrp :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				userService.delUserGrp(vo);
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

	// 사용자 그룹권한 리스트
	@RequestMapping(method = RequestMethod.GET , value = "getUserGrp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getUserGrpCtrl(@ModelAttribute("UserGrpVO") UserGrpVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getUserGrp :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				List<UserGrpVO> result = userService.getUserGrp(vo);

				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
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

	// 사용자 그룹권한 리스트
	@RequestMapping(method = RequestMethod.GET , value = "getUserGrpList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getUserGrpListCtrl(@ModelAttribute("UserGrpVO") UserGrpVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getUserGrpList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				List<UserGrpVO> result = userService.getUserGrpList(vo);
				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
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

	// 사용자 서비스 가입
	@RequestMapping(method = RequestMethod.POST , value = "insUserAdmin.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insUserAdminCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: insUserAdmin :::::::::::::::::::");
			int result = userService.insUserAdmin(vo);
			if (result > 0) success = true;
			if (result == -100) {
				jsonObject.put("errCode", result);
				jsonObject.put("message", "이미 존재하는 사번입니다.");
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 사용자 서비스 가입
	@RequestMapping(method = RequestMethod.POST , value = "delUserLoginInfo.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delUserLoginInfoCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: delUserLoginInfo :::::::::::::::::::");

			int result = userService.delUserLoginInfo(vo);
			if (result > 0) success = true;

		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	//비밀번호 변경일자 조회
	@RequestMapping(method = RequestMethod.POST , value = "getPwdUpdDate.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPwdUpdDateCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getPwdUpdDate :::::::::::::::::::");
			List<UserVO> result = userService.getUserList(vo);
			jsonObject.put("total", result.size());
			if (!result.isEmpty()) jsonObject.put("data", result.get(0));
			success = true;
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

}
