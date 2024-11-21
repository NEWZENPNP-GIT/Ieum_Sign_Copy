package com.ezsign.mobile.controller;

import com.ezsign.attend.service.AttendService;
import com.ezsign.attend.vo.AttendVO;
import com.ezsign.bbs.service.BbsService;
import com.ezsign.bbs.vo.BbsContentsVO;
import com.ezsign.biz.service.BizService;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.biz.vo.CandycashBizVO;
import com.ezsign.biztalk.service.BizTalkService;
import com.ezsign.biztalk.vo.BizTalkKKOVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.contract.service.ContractService;
import com.ezsign.contract.vo.ContractPersonVO;
import com.ezsign.emp.service.EmpService;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.CRNValidator;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.HttpUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.mobile.service.MobileContractService;
import com.ezsign.mobile.service.MobileMainService;
import com.ezsign.mobile.vo.MobileCandyCashVO;
import com.ezsign.mobile.vo.MobileMainResponse;
import com.ezsign.paystub.service.PaystubService;
import com.ezsign.paystub.vo.PaystubDataVO;
import com.ezsign.paystub.vo.PaystubMainVO;
import com.ezsign.paystub.vo.PaystubVO;
import com.ezsign.point.service.PointService;
import com.ezsign.point.vo.PointVO;
import com.ezsign.user.service.UserService;
import com.ezsign.user.vo.UserVO;
import com.jarvis.pdf.util.ToolUtil;

import egovframework.com.utl.slm.EgovHttpSessionBindingListener;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

@Controller
@Api(tags = "MobileMainController", description = "모바일화면")
@RequestMapping("/mobile/")
public class MobileMainController extends BaseController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "mobileMainService")
	private MobileMainService mobileMainService;
	
	@Resource(name = "bizTalkService")
	private BizTalkService bizTalkService;
	
	@Resource(name = "userService")
    private UserService userService;
	
	@Resource(name = "messageProperties")
    private Properties messageProperties;
	
	@Resource(name = "pointService")
    private PointService pointService;
	
	@Resource(name = "bizService")
    private BizService bizService;
	
	@Resource(name = "bbsService")
	private BbsService bbsService;
	
	@Resource(name = "empService")
	private EmpService empService;
	
	@Resource(name = "contractService")
	private ContractService contractService;
	
	@Resource(name = "attendService")
    private AttendService attendService;

	@Resource(name = "paystubService")
	private PaystubService paystubService;
	
	@Resource(name = "mobileContractService")
	private MobileContractService mobileContractService;

	@ApiOperation(value = "모바일 메인화면", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET, value = "main.do")
	@ResponseBody
	public ResponseEntity<MobileMainResponse> getMobileMain(String 계약ID, HttpServletRequest request) throws Exception {
		HttpHeaders responseHeaders = new HttpHeaders();
		MobileMainResponse response = new MobileMainResponse();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getMobileMain :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				response = mobileMainService.getMobileMain(loginVO, 계약ID);
				response.success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(response, responseHeaders, status);
	}
	
	@ApiOperation(value = "모바일 메인화면", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET, value = "mainFlutter.do")
	@ResponseBody
	public ResponseEntity<MobileMainResponse> getMobileMainFlutter(@ModelAttribute("SessionVO") SessionVO vo, HttpServletRequest request) throws Exception {
		HttpHeaders responseHeaders = new HttpHeaders();
		MobileMainResponse response = new MobileMainResponse();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getMobileMainFlutter :::::::::::::::::::");
			if (vo.getUserId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				response = mobileMainService.getMobileMain(vo, "");
				response.success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(response, responseHeaders, status);
	}

	@ApiOperation(value = "모바일 급여명세관리 급여년월조회", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getPaystubMobileList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPaystubMobileList(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success	  = false;
		int total		  = 0;
		try {
			logger.info(":::::::::::::::::::: getPaystubMobileList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				List<PaystubMainVO> result = mobileMainService.getPaystubMobileList(loginVO);
				if (result != null && !result.isEmpty()) {
					jsonObject.put("total", result.size());
					jsonObject.put("data", result);
				} else {
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
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
	
	@ApiOperation(value = "캔디캐시사업자번호체크", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "checkBusinessNumber.do")
	@ResponseBody
	public ResponseEntity<JSONObject> checkBusinessNumber(String businessNumber, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = true;
		try {
			logger.info(":::::::::::::::::::: checkBusinessNumber :::::::::::::::::::");
			boolean bCheck = CRNValidator.isValid(businessNumber.replace("-", ""));
			jsonObject.put("description", "");
			if (!bCheck) { jsonObject.put("description", "유효한 사업자 번호가 아닙니다."); success = false; }

			if (!mobileMainService.checkBusinessNumber(businessNumber.replace("-", ""))) { jsonObject.put("description", "이미 등록된 사업자 번호입니다."); success = false; }

		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시유효전화번호체크", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "checkHpNumber.do")
	@ResponseBody
	public ResponseEntity<JSONObject> checkHpNumber(String hpNumber, String certNumber, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = true;
		try {
			logger.info(":::::::::::::::::::: checkHpNumber :::::::::::::::::::");
			BizTalkKKOVO vo = new BizTalkKKOVO();
			vo.setContent("본인인증 화면에 [" + certNumber + "]를 입력해주세요");
			vo.setCallback("0232706285");
			vo.setRecipientNum(hpNumber.replace("-", ""));
			vo.setSendType("sms");
			bizTalkService.sendBizTalkNonBiz(vo);
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시회원가입", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "registerMember.do")
	@ResponseBody
	public ResponseEntity<JSONObject> registerMember(@RequestBody MobileCandyCashVO cashVO, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = true;
		try {
			logger.info(":::::::::::::::::::: registerMember :::::::::::::::::::");
			UserVO vo = new UserVO();
			vo.setBusinessNo(cashVO.getBusinessNumber());
			vo.setBizName(cashVO.getCompanyName());
			vo.setOwnerName(cashVO.getBossName());
			vo.setUserName(cashVO.getBossName());
			vo.setPhoneNum(cashVO.getHpNumber());
			vo.setUserPwd(cashVO.getPassword());
			vo.setUserType("6");
			if (cashVO.getBizType() != null && cashVO.getBizType().equals("bizlounge")) {
				vo.setLoginId(cashVO.getBusinessNumber() + "@bizlounge.com");
			} else if (cashVO.getBizType() != null && cashVO.getBizType().equals("ieumcheck")) {
				vo.setLoginId(cashVO.getBusinessNumber() + "@ieumcheck.co.kr");
			} else {
				vo.setLoginId(cashVO.getBusinessNumber() + "@candycash.com");
			}
			
			int result = mobileMainService.insMobileBizUser(vo);

			if (result == -100) { jsonObject.put("message", "이미 가입한 회사입니다."); success = false; }
			
			request.getSession().removeAttribute("accessToken");
			request.getSession().removeAttribute("refreshToken");
			request.getSession().removeAttribute("snsEmail");
			request.getSession().removeAttribute("snsName");
			request.getSession().removeAttribute("idToken");
			request.getSession().removeAttribute("snsType");
			
			if (result > 0) success = true;

		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시로그인", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "candycashlogin.do")
	@ResponseBody
	public ResponseEntity<JSONObject> loginCtrl(@RequestBody UserVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "Applacation/json; charset=UTF-8");
        HttpStatus status = HttpStatus.OK;
        try {
            logger.info(":::::::::::::::::::: login ::::::::::::::::::: " + vo.getUserId());
            SessionVO loginVO = userService.loginProcess(vo);
            if (loginVO != null) {
            	if ("Y".equals(loginVO.getLoginLockYn())) {
                    jsonObject.put("success", false);
                    jsonObject.put("message", "비밀번호 5회 오류로 계정이 잠겼습니다.\n" + loginVO.getUnlockTime() + "초 뒤에 로그인이 가능합니다.");
                } else {
                    if ("Y".equals(loginVO.getLoginFailYn())) {
                        jsonObject.put("success", false);
                        jsonObject.put("message", messageProperties.getProperty("login.fail") + "\n5회 오류시 계정이 잠깁니다.");
                    } else {
                        //로그인 성공시에 암호화 키값 셋팅
                        SessionUtil.Login(request, loginVO);
                        int curPoint = 0;

                        // 접속기업의 포인트
                        PointVO pVO = new PointVO();
                        pVO.setBizId(loginVO.getBizId());
                        PointVO pointVO = pointService.getPoint(pVO);

                        if (pointVO != null) curPoint = pointVO.getCurPoint();

                        logger.info("# curPoint : " + curPoint);
                        
                        // 바우처 구분 조회 추가(21.11.09)
                        BizVO bizVO = new BizVO();
                        bizVO.setBizId(loginVO.getBizId());
                        bizVO.setEndPage(1); // limit 0이면 결과가 조회되지않아 추가
                        BizVO resultBizVO = new BizVO();
                        List<BizVO> bizlist = bizService.getBizList(bizVO);
                        if (!bizlist.isEmpty()) { for (BizVO value : bizlist) resultBizVO = value; }
                        
                        if (!StringUtil.isNotNull(loginVO.getUserName())) loginVO.setUserName("-");
                        jsonObject.put("success", true);
                        jsonObject.put("loginId", loginVO.getLoginId());
                        jsonObject.put("userId", loginVO.getUserId());
                        jsonObject.put("userName", loginVO.getUserName());
                        jsonObject.put("userType", loginVO.getUserType());
                        jsonObject.put("bizId", loginVO.getBizId());
                        jsonObject.put("bizName", loginVO.getBizName());
                        jsonObject.put("curPoint", curPoint);
                        // 기업바우처구분
                        jsonObject.put("openVoucherType", resultBizVO.getOpenVoucherType());
                        logger.info("resultBizVO.getOpenVoucherType ===>" + resultBizVO.getOpenVoucherType());
                        
                        // 연말정산 계약ID
                        jsonObject.put("yearContractId", loginVO.getYearContractId());
                        // 연말정산 계약년도
                        jsonObject.put("febYear", loginVO.getFebYear());
                        jsonObject.put("useContractMenu", loginVO.getUseContract());
                        
                        jsonObject.put("useElecDoc", loginVO.getUseElecDoc());
                        jsonObject.put("usePayStub", loginVO.getUsePayStub());

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
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	//isCompareBusinessNoNHpNumber
	@ApiOperation(value = "캔디캐시사업번호와휴대전화비교", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "compareBizNumHpNum.do")
	@ResponseBody
	public ResponseEntity<JSONObject> isCompareBusinessNoNHpNumber(String hpNumber, String loginId, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "Applacation/json; charset=UTF-8");
        HttpStatus status = HttpStatus.OK;
        try {
            logger.info(":::::::::::::::::::: compareBusinessNoNHpNumber ::::::::::::::::::: ");
            UserVO user = new UserVO();
            user.setUserId(loginId);
            user.setPhoneNum(hpNumber);
            List<UserVO> result = userService.findUser(user);
            
            if (result == null || result.isEmpty()) {
            	jsonObject.put("success", false);
                jsonObject.put("message", "일치하는 정보가 없습니다.");
            } else {
            	jsonObject.put("success", true);
                jsonObject.put("message", "");
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시공지사항리스트", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getNoticeList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getNoticeList(HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total 		  = 0;
		try {
			logger.info(":::::::::::::::::::: getMobileNoticeList :::::::::::::::::::");
			BbsContentsVO vo = new BbsContentsVO();
			//캔디캐시 공지사항 리스트
			vo.setBbsId("211200000000001");
			vo.setStartPage(0);
			vo.setEndPage(1000);
				
			if (StringUtil.isNull(vo.getSortName())) { vo.setSortName("A.INS_DATE"); vo.setSortOrder("DESC"); }

			List<BbsContentsVO> result= bbsService.getBbsContentsList(vo);
			
			total = bbsService.getBbsContentsListCount(vo);
			
			if (result != null) {
				jsonObject.put("success", true);
				jsonObject.put("total", total);
				jsonObject.put("data", result);
			} else {
				jsonObject.put("success", false);
				jsonObject.put("total", 0);
				jsonObject.put("data", null);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시공지사항조회수 업데이트", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "updateNotice.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updateNotice(@RequestBody BbsContentsVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total 		  = 0;
		try {
			logger.info(":::::::::::::::::::: updateMobileBBSViewCount :::::::::::::::::::");
			vo.setBbsId("211200000000001");
			BbsContentsVO result= bbsService.getBbsContents(vo);
			if (result != null) {
				jsonObject.put("success", true);
				jsonObject.put("total", total);
				jsonObject.put("data", result);
			} else {
				jsonObject.put("success", false);
				jsonObject.put("total", 0);
				jsonObject.put("data", null);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디공지사항리스트", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getNoticeListCandy.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getNoticeListCandy(HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total 		  = 0;
		try {
			logger.info(":::::::::::::::::::: getMobileNoticeList :::::::::::::::::::");
			BbsContentsVO vo = new BbsContentsVO();
			
			//캔디캐시 공지사항 리스트
			vo.setBbsId("180706135108002");
			vo.setStartPage(0);
			vo.setEndPage(1000);
				
			if (StringUtil.isNull(vo.getSortName())) { vo.setSortName("A.INS_DATE"); vo.setSortOrder("DESC"); }

			List<BbsContentsVO> result = bbsService.getBbsContentsList(vo);
			total					   = bbsService.getBbsContentsListCount(vo);
			
			if (result != null) {
				jsonObject.put("success", true);
				jsonObject.put("total", total);
				jsonObject.put("data", result);
			} else {
				jsonObject.put("success", false);
				jsonObject.put("total", 0);
				jsonObject.put("data", null);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디공지사항조회수 업데이트", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "updateNoticeCandy.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updateNoticeCandy(@RequestBody BbsContentsVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total 		  = 0;
		try {
			logger.info(":::::::::::::::::::: updateMobileBBSViewCount :::::::::::::::::::");
			vo.setBbsId("180706135108002");
			BbsContentsVO result= bbsService.getBbsContents(vo);
			
			if (result != null) {
				jsonObject.put("success", true);
				jsonObject.put("total", total);
				jsonObject.put("data", result);
			} else {
				jsonObject.put("success", false);
				jsonObject.put("total", 0);
				jsonObject.put("data", null);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시암호변경", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "resetPassword.do")
	@ResponseBody
	public ResponseEntity<JSONObject> resetPassword(@RequestBody UserVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "Applacation/json; charset=UTF-8");
        HttpStatus status = HttpStatus.OK;
        try {
            logger.info(":::::::::::::::::::: login ::::::::::::::::::: " + vo.getUserId());
            String userPwdText = vo.getUserPwd();
            int	result 		   = userService.updMobileUserPwd(vo);
            
            if (result > 0) resetPwdMoneyCheck(vo.getUserId(), userPwdText);

            if (result == 0) {
            	jsonObject.put("success", false);
                jsonObject.put("message", "변경한 정보가 없습니다.");
            } else {
            	jsonObject.put("success", true);
                jsonObject.put("message", "");
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시암호변경", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "updPassword.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updPassword(@RequestBody UserVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "Applacation/json; charset=UTF-8");
        HttpStatus status = HttpStatus.OK;
        int result		  = 0;
        try {
            logger.info(":::::::::::::::::::: login ::::::::::::::::::: " + vo.getUserId());
            String userPwdText = vo.getUserPwd();
            UserVO prevUserPassVO = new UserVO();
            prevUserPassVO.setUserId(vo.getUserId());
            prevUserPassVO.setUserPwd(vo.getPrevUserPwd());
            
            if (userService.isUserPwd(prevUserPassVO)) {
            	result = userService.updMobileUserPwd(vo);
            } else {
            	jsonObject.put("success", false);
                jsonObject.put("message", "이전 암호가 틀렸습니다.");
            }
            
            if (result > 0) resetPwdMoneyCheck(vo.getUserId(), userPwdText);

            if ( result == 0) {
            	jsonObject.put("success", false);
                jsonObject.put("message", "변경한 정보가 없습니다.");
            } else {
            	jsonObject.put("success", true);
                jsonObject.put("message", "");
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	//머니 체크 비밀번호 변경
	private void resetPwdMoneyCheck(String id, String pw) throws Exception {
		StringBuffer result2  = new StringBuffer();
		String apiUrl 		  = "https://mc114.co.kr/api/exreg/updateuserpw";
        JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("pw", pw);
						
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost 		   = new HttpPost(apiUrl);
		httpPost.addHeader("pnp", "true");

		// JSON 데이터를 추가. 
		httpPost.setEntity(new StringEntity(jsonObject.toString(), ContentType.APPLICATION_JSON));

		// 실행 
		CloseableHttpResponse httpresponse = client.execute(httpPost);

		// 결과 수신
		InputStream inputStream = httpresponse.getEntity().getContent();
		String inputLine  		= null;
		BufferedReader in 		= new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
		
		while ((inputLine = in.readLine()) != null) { result2.append(inputLine); }

        String myResult   = result2.toString();
        JSONParser parser = new JSONParser();
        Object obj 		  = parser.parse(myResult);

        org.json.simple.JSONObject jsonObj = (org.json.simple.JSONObject) obj;

        boolean boolSuccess = (boolean) jsonObj.get("sucess");
        String strData 		= (String) jsonObj.get("data");
        logger.info(":::::::::::::::::::: change result ::::::::::::::::::: ");
        logger.info("sucess : " + boolSuccess);
        logger.info("data:  " + strData);
        logger.info(":::::::::::::::::::: change result ::::::::::::::::::: ");
	}
	
	@ApiOperation(value = "캔디캐시근로자리스트", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getEmployee.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getEmployeeList(EmpVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total 		  = 0;
		try {
			logger.info(":::::::::::::::::::: getEmpList :::::::::::::::::::");
			SessionVO loginVO = new SessionVO();
			loginVO.setBizId(vo.getBizId());
			loginVO.setUserId(vo.getBizId());
			
			vo.setUserId(null);
			vo.setSearchKey("empName");
			vo.setSearchValue("");
			
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
				String ipAddr = request.getHeader("X-Forwarded-For");
		 
		        if (ipAddr == null) ipAddr = request.getHeader("Proxy-Client-IP");
		        if (ipAddr == null) ipAddr = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
		        if (ipAddr == null) ipAddr = request.getHeader("HTTP_CLIENT_IP");
		        if (ipAddr == null) ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
		        if (ipAddr == null) ipAddr = request.getRemoteAddr();

				logger.info("bizId=>"+vo.getBizId());
				logger.info("searchKey=>"+vo.getSearchKey());
				logger.info("searchValue=>"+vo.getSearchValue());

				loginVO.setIpAddr(ipAddr);
				vo.setEndPage(10000);
				vo.setEmpType("1");	//근로자만 조회
				List<EmpVO> result = empService.getEmpList(vo, loginVO);
				total 			   = empService.getEmpListCount(vo);
				
				if (result != null && !result.isEmpty()) {
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
				} else if(result == null){
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				} else if(result.size() == 0){
					jsonObject.put("success", true);
					jsonObject.put("total", 0);
					jsonObject.put("data", result);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시근로자저장", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "insEmp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insEmpCtrl(@RequestBody EmpVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean result 	  = false;
		logger.info(":::::::::::::::::::: insEmp :::::::::::::::::::");

		try {
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				logger.info("userId=>"+vo.getUserId());

				if (!StringUtil.isNotNull(vo.getEmpType())) vo.setEmpType("1");
				
				if (vo.getEMail() == null && vo.getTestEmail() != null) vo.setEMail(vo.getTestEmail());
				
				vo.setEmpNonce("");
				vo.setCountryType("81");
				vo.setConfirmType("N");

				String empNonce = empService.insEmp(vo);
				String message = "";
				if (StringUtil.isNotNull(empNonce)) {
					switch (empNonce) {
						case "-1":
							message = "입력하신 인사정보가 기존의 인사정보와 모두 동일합니다.";
							break;
						case "-2":
							message = "인사정보를 수정하였습니다.";
							break;
						case "-3":
							message = "해당 사번의 인사정보가 이미 존재합니다.";
							break;
						case "1":
							message = "인사정보를 등록하였습니다.";
							break;
					}
					jsonObject.put("message", message);
					result = true;
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시근로자수정", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "updEmp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updEmpCtrl(@RequestBody EmpVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: updEmp :::::::::::::::::::");
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				logger.info("userId=>"+vo.getUserId());
				
				if (vo.getEMail() == null && vo.getTestEmail() != null) vo.setEMail(vo.getTestEmail());
				
				logger.info("userId2=>"+vo.getUserId());
				int result = empService.updEmp(vo);
			
				if (result>0) success = true;
				jsonObject.put("total", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시근로자삭제", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "delEmp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delEmpCtrl(@RequestBody EmpVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: delEmp :::::::::::::::::::");
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				logger.info("userId=>"+vo.getUserId());
				
				int result = empService.delEmp(vo);
				
				if (result > 0) success = true;
				jsonObject.put("total", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시근로자삭제체크", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "getContractListCount.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractListCountCtrl(@RequestBody ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		int total 		  = 0;
		try {
			logger.info(":::::::::::::::::::: getContractListCount :::::::::::::::::::");
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				logger.info("searchKey=>"+vo.getSearchKey());
				logger.info("searchValue=>"+vo.getSearchValue());
				
				//vo의 값을 변경(empNo 에 userID넣기) 쿼리에 맞게 수정
				vo.setEmpNo(vo.getUserId());
				vo.setUserId("");
				total   = contractService.getContractPersonListCount(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("total", total);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시근로자출퇴근관리", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getMobileAttendList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getMobileAttendListCtrl(AttendVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		try {
            logger.info(":::::::::::::::::::: getAttendList :::::::::::::::::::");
            if (vo.getBizId() == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                List<AttendVO> result = attendService.getMobileAttendList(vo);
                
                if (result != null) {
					jsonObject.put("success", true);
					jsonObject.put("total", result.size());
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        }
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시근로자출퇴근관리", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getMobileUserAttendList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getMobileUserAttendListCtrl(AttendVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		try {
            logger.info(":::::::::::::::::::: getAttendList :::::::::::::::::::");
            if (vo.getBizId() == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                List<AttendVO> result = attendService.getMobileUserAttendList(vo);
                if (result != null) {
					jsonObject.put("success", true);
					jsonObject.put("total", result.size());
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        }
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시근로자출퇴근관리저장", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "insAttend.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insAttend(@RequestBody AttendVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
        try {
            logger.info(":::::::::::::::::::: insAttend :::::::::::::::::::");
            if (vo.getBizId() == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	vo.setAttendCode("001");
                attendService.insAttend(vo);
                jsonObject.put("success", true);
            }
        } catch (Exception ex) {
        	jsonObject.put("success", false);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        }
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }
	
	@ApiOperation(value = "캔디캐시근로자출퇴근관리저장", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "updMobileAttend.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updMobileAttend(@RequestBody AttendVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
        try {
            logger.info(":::::::::::::::::::: insAttend :::::::::::::::::::");
            if (vo.getBizId() == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	attendService.updMobileAttend(vo);
                jsonObject.put("success", true);
            }
        } catch (Exception ex) {
        	jsonObject.put("success", false);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        }
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }
	
	@ApiOperation(value = "캔디캐시근로자임금명세서", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getMobilePayStubList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPayStubListCtrl(PaystubVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		try {
			logger.info(":::::::::::::::::::: getPayStubList :::::::::::::::::::");
			if (vo.getBizId() == null) {
                status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				logger.info("searchKey=>"+vo.getSearchKey());
				logger.info("searchValue=>"+vo.getSearchValue());
				
				vo.setEndPage(10000);
				List<PaystubVO> result= paystubService.getPaystubList(vo);
				
				if (result != null) {
					jsonObject.put("success", true);
					jsonObject.put("total", result.size());
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", result);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시근로자임금명세서미리보기", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getPayStubPDFView.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPayStubPDFViewCtrl(PaystubVO vo, @RequestParam(required = false) String type, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		PaystubVO resVO   = null;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getMobilePayStubPDFView :::::::::::::::::::");
			
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				SessionVO loginVO = new SessionVO();
				String ipAddr 	  = request.getHeader("X-Forwarded-For");
				 
		        if (ipAddr == null) ipAddr = request.getHeader("Proxy-Client-IP");
		        if (ipAddr == null) ipAddr = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
		        if (ipAddr == null) ipAddr = request.getHeader("HTTP_CLIENT_IP");
		        if (ipAddr == null) ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
		        if (ipAddr == null) ipAddr = request.getRemoteAddr();

		        
		        loginVO.setBizId(vo.getBizId());
		        loginVO.setUserId(vo.getUserId());
		        loginVO.setIpAddr(ipAddr);
				logger.info("bizId=>"+vo.getBizId());

				resVO = new PaystubVO();
				String szMonthPath = MultipartFileUtil.SEPERATOR+DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+DateUtil.getDate()+MultipartFileUtil.SEPERATOR;
				String szSavePath  = MultipartFileUtil.getSystemPath()+"temp/paystub"+szMonthPath;
				
				vo.setPayType(type);
				ContentVO contentVO 	= paystubService.getPaystubPDFView(vo, loginVO);
				String contentFileName  = contentVO.getFilePath()+contentVO.getFileName();
				String downloadFileName = szSavePath+contentVO.getFileName();
				// 생년월일
				String userPwd = contentVO.getInsDate();
				logger.info("getPayStubPDFCtrl password : "+ userPwd);
				FileUtil.createDirectory(szSavePath);
				File file;
				if ("pdf".equals(type)) {
					
					String originalPdfPath = contentFileName;
					String targetPdfName = StringUtil.getUUID()+".pdf";
					String targetPdfPath = MultipartFileUtil.getSystemTempPath()+targetPdfName;
					
					// 사용할 PDF복사
					FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
					if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
						String pdf_uri = StringUtil.ReplaceAll(targetPdfPath, MultipartFileUtil.getSystemPath(), "");
						resVO.setPdfFile(pdf_uri);
						success = true;
					}
				} else {
					if(ToolUtil.encryptUserPdf(contentFileName, downloadFileName, userPwd)) {
						file = new File(downloadFileName);
						HttpUtil.setResponseFile(request, response, file, contentVO.getOrgFileName());
						if(!file.exists()) status = HttpStatus.INTERNAL_SERVER_ERROR;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("data", resVO);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시근로자임금명세서여부확인", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "isPaystubData.do")
	@ResponseBody
	public ResponseEntity<JSONObject> isPaystubData(PaystubVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		PaystubVO resVO   = null;
		boolean success   = false;
		int result 		  = 0;
		
		try {
			logger.info(":::::::::::::::::::: getMobilePayStubPDFView :::::::::::::::::::");
			
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				result = paystubService.getPaystubListCount(vo);
				jsonObject.put("data", result);
				jsonObject.put("success", true);
			}
		} catch (Exception ex) {
			jsonObject.put("data", 0);
			jsonObject.put("success", false);
			ex.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시임금명세서조회", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getPaystub.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPaystubCtrl(PaystubVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		PaystubVO result  = new PaystubVO();
		try {
			logger.info(":::::::::::::::::::: insPaystub :::::::::::::::::::");
			
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				logger.info("userId=>"+vo.getUserId());
				vo.setEndPage(1);
				result  = paystubService.getPaystub(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		jsonObject.put("data", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시임금명세서저장", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "insPaystub.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insPaystubCtrl(@RequestBody PaystubVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String insDate  = "";
		int resultList  = 0;
		try {
			logger.info(":::::::::::::::::::: insPaystub :::::::::::::::::::");
			
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				logger.info("userId=>"+vo.getUserId());
				resultList = paystubService.getPaystubListCount(vo);
				if (resultList > 0) {
					success = false;
					jsonObject.put("message", "이미 작성된 급여명세서가 있습니다.");
				} else {
					insDate = mobileMainService.insMobilePaystub(vo);
					success = true;
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		jsonObject.put("data", insDate);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시임금명세서저장", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "insPaystubData.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insPaystubDataCtrl(@RequestBody PaystubDataVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: insPaystubData :::::::::::::::::::");
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				logger.info("userId=>"+vo.getUserId());
				mobileMainService.insMobilePaystubData(vo);
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

	@ApiOperation(value = "캔디캐시임금명세서PDF생성", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "createPaystubPDF.do")
	@ResponseBody
	public ResponseEntity<JSONObject> createPaystubPDFCtrl(@RequestBody PaystubVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: createPaystubPDF :::::::::::::::::::");
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				logger.info("userId=>"+vo.getUserId());
				int result    = paystubService.createPaystubPDF(vo);
				String fileId = paystubService.getPaystubFileId(vo);
				
				if (result > 0) {
					jsonObject.put("success", true);
					jsonObject.put("total", result);
					jsonObject.put("data", fileId);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", fileId);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시임금명세서데이터삭제", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "delPaystub.do")
	@ResponseBody
	public ResponseEntity<JSONObject> deletePaystubDataCtrl(@RequestBody PaystubVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: delPaystub :::::::::::::::::::");
			
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				logger.info("userId=>"+vo.getUserId());
				mobileMainService.delPaystub(vo);
				jsonObject.put("success", true);
			}
		} catch (Exception ex) {
			jsonObject.put("success", false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시임금명세서데이터업데이트", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "updTransDatePaystub.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updTransDatePaystubCtrl(@RequestBody PaystubVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: delPaystub :::::::::::::::::::");
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				logger.info("userId=>"+vo.getUserId());
				paystubService.updatePaystub(vo);
				jsonObject.put("success", true);
			}
		} catch (Exception ex) {
			jsonObject.put("success", false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시사업장정보가져오기", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getBizInfo.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBizInfoCtrl(CandycashBizVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getBizInfoCtrl :::::::::::::::::::");
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				jsonObject.put("success", true);
				jsonObject.put("message", "");
				List<CandycashBizVO> result = bizService.getCandyCashBizList(vo);
				
				if (!result.isEmpty()) jsonObject.put("data", result.get(0));
				else  				   jsonObject.put("data", new BizVO());
				
			}
		} catch (Exception ex) {
			jsonObject.put("success", false);
			jsonObject.put("message", ex.toString());
			jsonObject.put("data", new BizVO());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시로그인암호체크", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "isUserPassword.do")
	@ResponseBody
	public ResponseEntity<JSONObject> isUserPasswordCtrl(@RequestBody UserVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getBizInfoCtrl :::::::::::::::::::");
			if (vo.getUserId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				success = userService.isUserPwd(vo);
				jsonObject.put("success", success);
				jsonObject.put("message", "");
			}
		} catch (Exception ex) {
			jsonObject.put("success", false);
			jsonObject.put("message", ex.toString());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시기업정보업데이트", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "saveCandycashBizInfo.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveCandycashBizInfoCtrl(@RequestBody CandycashBizVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		try {
			logger.info(":::::::::::::::::::: saveCandycashBizInfo :::::::::::::::::::");
			
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				vo.setBusinessNo(vo.getBusinessNo().replace("-", ""));
				vo.setHpNumber(vo.getHpNumber().replace("-", ""));
				bizService.updCandyCashBizInfo(vo);
				jsonObject.put("success", true);
				jsonObject.put("message", "");
			}
		} catch (Exception ex) {
			jsonObject.put("success", false);
			jsonObject.put("message", ex.toString());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시기업정보업데이트", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "updMobileBizStamp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updMobileBizStampCtrl(@ModelAttribute("BizVO")BizVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		int total 		  = 0;
		try {
			logger.info(":::::::::::::::::::: updBizStamp :::::::::::::::::::");
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				String szMonthPath  = MultipartFileUtil.SEPERATOR+vo.getBizId();
				String szSavePath = MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				total = resultFileList.size();
				logger.info("기업 직인이미지 File Count : "+resultFileList.size());
				if (total == 1) {
					// 전달받은 파일리스트
                    for (FileVO fileVO : resultFileList) total = bizService.updBizStamp(vo, fileVO);
				}
				success = true;
				jsonObject.put("total", total);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시근로계약리스트", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getContractList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractListCtrl(ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		try {
			logger.info(":::::::::::::::::::: getContractList :::::::::::::::::::");
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				logger.info("searchKey=>"+vo.getSearchKey());
				logger.info("searchValue=>"+vo.getSearchValue());
				vo.setEndPage(9999);
				vo.setSortName("A.INS_DATE");
				vo.setSortOrder("DESC");
				
				List<ContractPersonVO> result= contractService.getContractPersonList(vo);
				int total = result.size();
				
				if (!result.isEmpty()) {
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", true);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			jsonObject.put("success", false);
			jsonObject.put("total", 0);
			jsonObject.put("data", null);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시근로계약저장", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "saveElectronicContract.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveElectronicContract(@RequestBody ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: saveElectronicContract :::::::::::::::::::");
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("getEmpNo=>"+vo.getEmpNo());
				//전자근로계약 저장
				ContractPersonVO result =  mobileContractService.saveElectronicContract(vo);
				result.setBizId(vo.getBizId());
				//전자근로계약 생성
				mobileContractService.generateContractPDF(result);
				success = true;
				jsonObject.put("success", success);
				jsonObject.put("data", result.getContId());
			}
		} catch (Exception ex) {
			jsonObject.put("success", success);
			jsonObject.put("data", null);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@ApiOperation(value = "캔디캐시근로계약미리보기", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getContractView.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractViewCtrl(ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		try {
			logger.info(":::::::::::::::::::: getContractView :::::::::::::::::::");
			SessionVO loginVO = new SessionVO();
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String ipAddr = request.getHeader("X-Forwarded-For");
		        if (ipAddr == null) ipAddr = request.getHeader("Proxy-Client-IP");
		        if (ipAddr == null) ipAddr = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
		        if (ipAddr == null) ipAddr = request.getHeader("HTTP_CLIENT_IP");
		        if (ipAddr == null) ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
		        if (ipAddr == null) ipAddr = request.getRemoteAddr();

		        loginVO.setIpAddr(ipAddr);
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				loginVO.setBizId(vo.getBizId());
				loginVO.setUserId(vo.getUserId());
				ContractPersonVO result = mobileMainService.getContractView(vo, loginVO);
				
				if (result != null) {
					jsonObject.put("success", true);
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@ApiOperation(value = "캔디캐시근로계약삭제", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "delElectronicContract.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delElectronicContract(@RequestBody ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: delContractList :::::::::::::::::::");
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("getEmpNo=>"+vo.getEmpNo());
				//전자근로계약 삭제
				mobileContractService.deleteContractData(vo);
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
	
	//전자문서 전송
	@ApiOperation(value = "캔디캐시근로계약전송", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "sendContract.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendContractCtrl(@RequestBody ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		try {
			logger.info(":::::::::::::::::::: sendContractMulti :::::::::::::::::::");
			if (vo.getBizId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				mobileContractService.sendContractPerson(vo);
				jsonObject.put("success", true);
			}
		} catch (Exception ex) {
			jsonObject.put("success", false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	//버젼정보 체크
	@ApiOperation(value = "캔디캐시버젼정보", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getVersion.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getVersionCtrl(HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		try {
			logger.info(":::::::::::::::::::: getVersion :::::::::::::::::::");
			String versions = mobileMainService.getCandyCashVersion();
			jsonObject.put("success", true);
			jsonObject.put("data", versions);
		} catch (Exception ex) {
			jsonObject.put("success", false);
			jsonObject.put("data", "");
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 웰머니 로그인 log 입력 컨트롤러 추가(21.12.30)
	@ApiOperation(value = "웰머니로그인기록", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "welmoneylog.do")
	@ResponseBody
	public ResponseEntity<JSONObject> welmoneyLoginCtrl(String loginId, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "Applacation/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		try {
			logger.info(":::::::::::::::::::: welmoneylogin ::::::::::::::::::: / " + loginId);
			String ipAddr = request.getHeader("X-Forwarded-For");
			if (ipAddr == null) ipAddr = request.getHeader("Proxy-Client-IP");
			if (ipAddr == null) ipAddr = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
			if (ipAddr == null) ipAddr = request.getHeader("HTTP_CLIENT_IP");
			if (ipAddr == null) ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
			if (ipAddr == null) ipAddr = request.getRemoteAddr();

		   mobileMainService.insWelmoneyLoginLog(loginId, ipAddr);
			jsonObject.put("success", true);
		} catch (Exception ex) {
			jsonObject.put("success", false);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 웰머니 로그인 log 입력 컨트롤러 추가(21.12.30)
	@ApiOperation(value = "웰머니로그인기록", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "welmoneyLoginCheck.do")
	@ResponseBody
	public ResponseEntity<JSONObject> welmoneyLoginCheckCtrl(String loginId, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "Applacation/json; charset=UTF-8");
		boolean chk;
		HttpStatus status = HttpStatus.OK;
		try {
			logger.info(":::::::::::::::::::: welmoneylogin ::::::::::::::::::: / " + loginId);
			chk = mobileMainService.welmoneyLoginCheck(loginId);
		} catch (Exception ex) {
			chk    = false;
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", chk);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
		
}
