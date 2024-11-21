package com.ezsign.company.controller;

import com.ezsign.biz.service.BizService;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.emp.service.EmpService;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.point.service.PointService;
import com.ezsign.point.vo.PointVO;
import com.ezsign.user.service.UserService;
import com.ezsign.user.service.UserSnsService;
import com.ezsign.user.vo.DeviceVO;
import com.ezsign.user.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.utl.slm.EgovHttpSessionBindingListener;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Properties;
import java.util.UUID;


@Controller
@RequestMapping("/")
public class LoginController extends BaseController {

    Logger logger = Logger.getLogger(this.getClass());

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

    @Resource(name = "ys000Service")
    private YS000Service ys000Service;

    @Resource(name = "empService")
    private EmpService empService;

    @RequestMapping(method = RequestMethod.GET, value = "login.do")
    public ModelAndView login(HttpServletRequest request) {
        logger.info(":::::::::::::::::::: login page loading :::::::::::::::::::");

        ModelAndView mv = new ModelAndView();
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

        String googleLoginUrl = "https://accounts.google.com/o/oauth2/v2/auth?";
        googleLoginUrl += "client_id=" + UserSnsService.GOOGLE_CLIENT_ID;
        googleLoginUrl += "&nonce=" + uuid;
        googleLoginUrl += "&response_type=code";
        googleLoginUrl += "&redirect_uri="+UserSnsService.GOOGLE_LOGIN_REDIRECT_URI;
        googleLoginUrl += "&scope=openid%20profile%20email";
        googleLoginUrl += "&access_type=offline";
        googleLoginUrl += "&prompt=consent";

        request.getSession().removeAttribute("uuid");
        request.getSession().setAttribute("uuid", uuid);
        mv.addObject("naverUrl", naverLoginUrl);
        mv.addObject("kakaoUrl", kakaoLoginUrl);
        mv.addObject("googleUrl", googleLoginUrl);
        mv.setViewName("login/login");

        return mv;
    }

    @ApiOperation(value = "모바일 추가 인증 로그인", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "loginMobile.do")
    @ResponseBody
    public ResponseEntity<JSONObject> loginCtrl(@RequestParam String userId, @RequestParam String deviceId, @RequestParam String authToken, HttpServletRequest request) throws Exception {
        JSONObject jsonObject       = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "Applacation/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        try {
            logger.info(":::::::::::::::::::: loginMobild ::::::::::::::::::: " + deviceId);

            DeviceVO deviceVO = new DeviceVO();
            deviceVO.setUserId(userId);
            deviceVO.setDeviceId(deviceId);
            deviceVO.setAuthToken(authToken);

            SessionVO loginVO = userService.loginMobileProcess(deviceVO);

            if (loginVO != null) {
                SessionUtil.Login(request, loginVO);
                int curPoint = 0;

                // 접속기업의 포인트
                PointVO pVO = new PointVO();
                pVO.setBizId(loginVO.getBizId());
                PointVO pointVO = pointService.getPoint(pVO);

                if (pointVO != null) curPoint = pointVO.getCurPoint();

                logger.info(curPoint);

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


    // 캔디 로그인 [24.10.31 서비스 종료로 인한 주석처리]
  /*   @RequestMapping(method = RequestMethod.POST, value = "loginFlutter.do")
    @ResponseBody
    public ResponseEntity<JSONObject> loginFlutterCtrl(@RequestBody UserVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObject       = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "Applacation/json; charset=UTF-8");
        HttpStatus status = HttpStatus.OK;
        try {
            logger.info(":::::::::::::::::::: login ::::::::::::::::::: " + vo.getUserId());

            SessionVO loginVO = userService.loginProcess(vo);

            if (loginVO != null) {
                if("Y".equals(loginVO.getLoginLockYn())) {
                    jsonObject.put("success", false);
                    jsonObject.put("message", "비밀번호 5회 오류로 계정이 잠겼습니다.\n" + loginVO.getUnlockTime() + "초 뒤에 로그인이 가능합니다.");
                } else {
                    if("Y".equals(loginVO.getLoginFailYn())) {
                        jsonObject.put("success", false);
                        jsonObject.put("message", messageProperties.getProperty("login.fail") + "\n5회 오류시 계정이 잠깁니다.");
                    } else {
                        //로그인 성공시에 암호화 키값 셋팅
                        SessionUtil.Login(request, loginVO);

                        //EMP가 1이 아니면 로그인 실패
                        EmpVO searchEmpVO = new EmpVO();
                        searchEmpVO.setBizId(loginVO.getBizId());
                        searchEmpVO.setUserId(loginVO.getUserId());
                        EmpVO resultVO =  empService.getEmp(searchEmpVO, loginVO);

                        if (resultVO != null && resultVO.getEmpType().equals("1")) {

                            if (!StringUtil.isNotNull(loginVO.getUserName())) loginVO.setUserName("-");
                            jsonObject.put("success", true);
                            jsonObject.put("loginId", loginVO.getLoginId());
                            jsonObject.put("userId", loginVO.getUserId());
                            jsonObject.put("userName", loginVO.getUserName());
                            jsonObject.put("userType", loginVO.getUserType());
                            jsonObject.put("bizId", loginVO.getBizId());
                            jsonObject.put("bizName", loginVO.getBizName());
                            jsonObject.put("phoneNum", loginVO.getPhoneNum());

                            jsonObject.put("message", messageProperties.getProperty("login.success"));

                        } else {
                            jsonObject.put("success", false);
                            jsonObject.put("message", "모바일은 근로자만 접근이 가능합니다.");
                        }
                        EgovHttpSessionBindingListener listener = new EgovHttpSessionBindingListener();
                        request.getSession().setAttribute(loginVO.getLoginId(), listener);
                    }
                }
            } else {
                jsonObject.put("success", false);
                jsonObject.put("message", messageProperties.getProperty("login.fail"));
            }
        } catch (Exception ex) {
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }

    캔디 버전체크 [24.10.31 서비스 종료로 인한 주석처리]
    @RequestMapping(method = RequestMethod.GET, value = "getVersionFlutter.do")
    @ResponseBody
    public ResponseEntity<JSONObject> getVersionFlutter(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObject       = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "Applacation/json; charset=UTF-8");
        HttpStatus status = HttpStatus.OK;
        try {
            logger.info(":::::::::::::::::::: version check ::::::::::::::::::: " );

            jsonObject.put("success", true);
            jsonObject.put("version", "V1.0.0");
            jsonObject.put("message", "버젼 조회 완료.");
        } catch (Exception ex) {
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }

    */


    @RequestMapping(method = RequestMethod.POST, value = "login.do")
    @ResponseBody
    public ResponseEntity<JSONObject> loginCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObject       = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "Applacation/json; charset=UTF-8");
        HttpStatus status = HttpStatus.OK;
        try {
            logger.info(":::::::::::::::::::: login ::::::::::::::::::: " + vo.getUserId());
            SessionVO loginVO = userService.loginProcess(vo);

            if (loginVO != null) {
                if("Y".equals(loginVO.getLoginLockYn())) {
                    jsonObject.put("success", false);
                    jsonObject.put("message", "비밀번호 5회 오류로 계정이 잠겼습니다.\n" + loginVO.getUnlockTime() + "초 뒤에 로그인이 가능합니다.");
                } else {
                    if("Y".equals(loginVO.getLoginFailYn())) {
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
                        BizVO resultBizVO   = new BizVO();
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
                        jsonObject.put("deptCode", loginVO.getDeptCode());
                        jsonObject.put("curPoint", curPoint);
                        jsonObject.put("phoneNum", loginVO.getPhoneNum());
                        // 기업바우처구분
                        jsonObject.put("openVoucherType", resultBizVO.getOpenVoucherType());
                        // 조건엑셀 다운로드 구분
                        jsonObject.put("useContractData", resultBizVO.getUseContractData());

                        // 연말정산 계약ID
                        jsonObject.put("yearContractId", loginVO.getYearContractId());
                        // 연말정산 계약년도
                        jsonObject.put("febYear", loginVO.getFebYear());
                        jsonObject.put("useContractMenu", loginVO.getUseContract());

                        jsonObject.put("useElecDoc", loginVO.getUseElecDoc());
                        jsonObject.put("usePayStub", loginVO.getUsePayStub());

                        jsonObject.put("bizMngYn", loginVO.getBizMngYn());
                        jsonObject.put("mainBizYn", loginVO.getMainBizYn());

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
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }


    @RequestMapping(method = RequestMethod.POST, value = "XloginX.do")
    @ResponseBody
    public ResponseEntity<JSONObject> XloginXCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObject       = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "Applacation/json; charset=UTF-8");
        HttpStatus status = HttpStatus.OK;
        try {
            logger.info(":::::::::::::::::::: XloginX ::::::::::::::::::: " + vo.getUserId());

            SessionVO loginVO = userService.XloginXProcess(vo);

            if (loginVO != null) {
                SessionUtil.Login(request, loginVO);
                int curPoint = 0;

                // 접속기업의 포인트
                PointVO pVO = new PointVO();
                pVO.setBizId(loginVO.getBizId());
                PointVO pointVO = pointService.getPoint(pVO);

                if (pointVO != null) curPoint = pointVO.getCurPoint();

                logger.info(curPoint);

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

            } else {
                jsonObject.put("success", false);
                jsonObject.put("message", messageProperties.getProperty("login.fail"));
            }
        } catch (Exception ex) {
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }

    @RequestMapping(method = RequestMethod.POST, value = "logout.do")
    @ResponseBody
    public ResponseEntity<JSONObject> logoutCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request) throws Exception {
        JSONObject jsonObject       = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        HttpStatus status = HttpStatus.OK;
        try {
            logger.info(":::::::::::::::::::: logout :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO != null) {
                userService.logOutProcess(loginVO);
                SessionUtil.Logout(request);
            }
            jsonObject.put("success", true);
        } catch (Exception ex) {
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }


    @RequestMapping(method = RequestMethod.POST, value = "setUserPwd.do")
    @ResponseBody
    public ResponseEntity<JSONObject> setUserPwdCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObject       = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        HttpStatus status = HttpStatus.OK;
        boolean success   = false;
        try {
            logger.info(":::::::::::::::::::: setUserPwd :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                logger.info("userId=>" + vo.getUserId());

                int result = userService.updUserPwd(vo);

                if (result == 1) success = true;
            }
        } catch (Exception ex) {
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        jsonObject.put("success", success);
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }

    @RequestMapping(method = RequestMethod.GET, value = "getUserList.do")
    @ResponseBody
    public ResponseEntity<JSONObject> getUserListCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request) throws Exception {
        JSONObject jsonObject       = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        HttpStatus status = HttpStatus.OK;
        boolean success   = false;
        try {
            logger.info(":::::::::::::::::::: getUserList :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                logger.info("bizId=>" + loginVO.getBizId());
                vo.setBizId(loginVO.getBizId());

                List<UserVO> result = userService.getUserList(vo);

                jsonObject.put("total", result.size());
                jsonObject.put("data", result);
                success = true;
            }
        } catch (Exception ex) {
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        jsonObject.put("success", success);
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }

    @RequestMapping(method = RequestMethod.POST, value = "getBizAllList.do")
    @ResponseBody
    public ResponseEntity<JSONObject> getBizAllListCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObject       = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        HttpStatus status = HttpStatus.OK;
        boolean success   = false;
        try {
            logger.info(":::::::::::::::::::: getBizAllList :::::::::::::::::::");

            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null || !loginVO.getUserType().equals("9")) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                BizVO bizVO = new BizVO();
                bizVO.setEndPage(1000);
                bizVO.setStartPage(0);
                List<BizVO> result = bizService.getBizList(bizVO);

                if (result != null && !result.isEmpty()) {
                    jsonObject.put("data", result);
                    success = true;
                }
            }
        } catch (Exception ex) {
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        jsonObject.put("success", success);
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }

    @RequestMapping(method = RequestMethod.POST, value = "setBizId.do")
    @ResponseBody
    public ResponseEntity<JSONObject> setBizIdCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObject       = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        responseHeaders.add("Access-Control-Allow-Origin", "*");

        HttpStatus status = HttpStatus.OK;
        boolean success   = false;

        try {
            logger.info(":::::::::::::::::::: setBizId :::::::::::::::::::");

            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                int userType = Integer.parseInt(loginVO.getUserType());
                // 노무사 이상만 처리가능하도록 변경
                if (userType > 5) {
                    loginVO.setBizId(vo.getBizId());
                    SessionUtil.Login(request, loginVO);
                    success = true;

                    BizVO bizVO = new BizVO();
                    bizVO.setBizId(vo.getBizId());
                    bizVO.setEndPage(1);
                    bizVO.setStartPage(0);
                    List<BizVO> result = bizService.getBizList(bizVO);
                    if (result != null && !result.isEmpty()) {
                        jsonObject.put("bizName", result.get(0).getBizName());
                        jsonObject.put("bizId", result.get(0).getBizId());
                        jsonObject.put("useContractMenu", result.get(0).getUseContract());
                        jsonObject.put("useElecDoc", result.get(0).getUseElecDoc());
                        jsonObject.put("usePayStub", result.get(0).getUsePayStub());
                        jsonObject.put("useContractData", result.get(0).getUseContractData());

                        // 접속기업의 포인트
                        int curPoint = 0;
                        PointVO pVO  = new PointVO();
                        pVO.setBizId(vo.getBizId());
                        PointVO pointVO = pointService.getPoint(pVO);

                        if (pointVO != null) curPoint = pointVO.getCurPoint();
                        jsonObject.put("curPoint", curPoint);

                        // 접속기업의 연말정산 당해년도, 계약ID
                        YS000VO ys000VO = new YS000VO();
                        ys000VO.setBizId(vo.getBizId());

                        List<YS000VO> ys000List = ys000Service.getYS000List(ys000VO);

                        if (ys000List != null && !ys000List.isEmpty()) {
                            jsonObject.put("yearContractId", ys000List.get(0).get계약ID());
                            jsonObject.put("febYear", ys000List.get(0).getFebYear());
                        }
                    }
                } else {
                    status = HttpStatus.UNAUTHORIZED;
                }
            }
        } catch (Exception ex) {
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        jsonObject.put("success", success);
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }

    // 세무사랑/케이랩 > 연동관리 > 전자계약/전자근로계약 클릭 시 이동
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST} , value = "exePage.do")
    public String exePageCtrl(HttpServletRequest request, @ModelAttribute("BizVO") BizVO vo, Model model) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        try {
            logger.info(":::::::::::::::::::: exePage :::::::::::::::::::");
            vo.setEndPage(1);
            List<BizVO> bizList = bizService.getBizList(vo);

            if (bizList == null || bizList.isEmpty() || vo.getBusinessNo() == null) return "/member/member_Join_Service_img";
            else                                                                    return "/login/login";

        } catch (Exception ex) {
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST} , value = "introPage.do")
    public String introPageCtrl(HttpServletRequest request, @ModelAttribute("BizVO") BizVO vo, Model model) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        try {
            logger.info(":::::::::::::::::::: exePage :::::::::::::::::::");
            return "/member/member_Join_Service_kclep";
        } catch (Exception ex) {
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST} , value = "introPage2.do")
    public String introPage2Ctrl(HttpServletRequest request, @ModelAttribute("BizVO") BizVO vo, Model model) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        try {
            logger.info(":::::::::::::::::::: exePage :::::::::::::::::::");
            return "/member/member_Join_Service_semu";
        } catch (Exception ex) {
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
    }

}
