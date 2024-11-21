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
import com.ezsign.point.vo.PointLogVO;
import com.ezsign.point.vo.PointVO;
import com.ezsign.user.dao.DeviceDAO;
import com.ezsign.user.dao.UserDAO;
import com.ezsign.user.service.UserService;
import com.ezsign.user.vo.*;
import com.jarvis.common.util.FileUtil;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("userService")
public class UserServiceImpl extends AbstractServiceImpl implements UserService {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "contentService")
    private ContentService contentService;

    @Resource(name = "contractService")
    private ContractService contractService;

    @Resource(name = "bizDAO")
    private BizDAO bizDAO;

    @Resource(name = "userDAO")
    private UserDAO userDAO;

    @Resource(name = "empDAO")
    private EmpDAO empDAO;

    @Resource(name = "bizTalkDAO")
    private BizTalkDAO bizTalkDAO;

    @Resource(name = "pointDAO")
    private PointDAO pointDAO;

    @Resource(name = "ys000DAO")
    private YS000DAO ys000DAO;

    @Resource(name = "deviceDAO")
    private DeviceDAO deviceDAO;


    public static void main(String[] args) {

    }

    @Override
    public SessionVO loginMobileProcess(DeviceVO deviceVO) throws Exception {
        List<DeviceVO> devices = deviceDAO.getDeviceList(deviceVO);

        if (devices == null || devices.size() == 0) {
            return null;
        }

        logger.info("AUTH TOKEN 일치");

        UserVO vo = new UserVO();
        vo.setUserId(deviceVO.getUserId());
        UserVO userVO = userDAO.loginProcess(vo);
        if (userVO == null) {
            return null;
        }

        logger.info("login result" + userVO.getUserId() + "/" + userVO.getUserType());

        UserLogVO logVO = new UserLogVO();
        logVO.setUserId(vo.getUserId());
        logVO.setLogType("000");
        userDAO.insUserLog(logVO);

        SessionVO loginVO = new SessionVO();
        loginVO.setLoginId(userVO.getUserId());
        loginVO.setUserName(userVO.getUserName());
        loginVO.setUserType(userVO.getUserType());

        EmpVO empVO = new EmpVO();
        empVO.setLoginId(vo.getUserId());
        empVO.setStartPage(0);
        empVO.setEndPage(10);
        List<EmpVO> empList = empDAO.getEmpList(empVO);

        logger.info("size=>" + empList.size());
        EmpVO resultEmpVO = null;
        if (empList.size() > 0) {
            for (EmpVO item : empList) {
                resultEmpVO = item;
                logger.info("사용자정보 : biz[" + resultEmpVO.getBizId() + "], user[" + resultEmpVO.getUserId() + "] ");
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
            logVO.setIpAddr(deviceVO.getLoginIp());
            userDAO.insUserLog(logVO);

            YS000VO ys000VO = new YS000VO();

            ys000VO.setBizId(loginVO.getBizId());
//            ys000VO.setFebYear(Integer.toString(Integer.parseInt(DateUtil.getYear()) - 1));

            List<YS000VO> result = ys000DAO.getYS000List(ys000VO);

            if (result.size() > 0) {
                loginVO.setYearContractId(result.get(0).get계약ID());
                loginVO.setFebYear(result.get(0).getFebYear());
            }

            String curDate = DateUtil.getTimeStamp(7);
            logger.info("접속한 사용자 [" + userVO.getUserName() + "] Connected Time=>" + curDate);
        } else {
            logVO = new UserLogVO();
            logVO.setUserId(vo.getUserId());
            logVO.setLogType("002");
            logVO.setIpAddr(deviceVO.getLoginIp());
            userDAO.insUserLog(logVO);

            loginVO = null;
        }


        return loginVO;
    }

    @Override
    public SessionVO XloginXProcess(UserVO vo) throws Exception {
        SessionVO loginVO = new SessionVO();
        UserVO userVO = new UserVO();

        userVO = userDAO.loginProcess(vo);

        if (userVO == null) return null;
        logger.info("login result" + userVO.getUserId() + "/" + userVO.getUserType());
        
        UserLogVO logVO = new UserLogVO();
        logVO.setUserId(vo.getUserId());
        logVO.setLogType("000");
        userDAO.insUserLog(logVO);

        logger.info(userVO.getUserId() + "패스워드 일치");
        loginVO.setLoginId(userVO.getUserId());
        loginVO.setUserName(userVO.getUserName());
        loginVO.setUserType(userVO.getUserType());

        EmpVO empVO = new EmpVO();
        empVO.setLoginId(vo.getUserId());
        empVO.setStartPage(0);
        empVO.setEndPage(10);
        List<EmpVO> empList = empDAO.getEmpList(empVO);

        logger.info("size=>" + empList.size());
        EmpVO resultEmpVO = new EmpVO();

        if (empList.size() > 0) {
            for (int i = 0; i < empList.size(); i++) {
                resultEmpVO = empList.get(i);
                logger.info("사용자정보 : biz[" + resultEmpVO.getBizId() + "], user[" + resultEmpVO.getUserId() + "] ");
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
                logVO.setIpAddr(vo.getIpAddr());
                userDAO.insUserLog(logVO);

                YS000VO ys000VO = new YS000VO();

                ys000VO.setBizId(loginVO.getBizId());
//                ys000VO.setFebYear(Integer.toString(Integer.parseInt(DateUtil.getYear()) - 1));
//                ys000VO.setFebYear(Integer.toString(Integer.parseInt(DateUtil.getYear())));

                List<YS000VO> result = ys000DAO.getYS000List(ys000VO);

                if (result.size() > 0) {
                    loginVO.setYearContractId(result.get(0).get계약ID());
                    loginVO.setFebYear(result.get(0).getFebYear());
                }

                String curDate = DateUtil.getTimeStamp(7);
                logger.info("접속한 사용자 [" + userVO.getUserName() + "] Connected Time=>" + curDate);

            } else {

                logVO = new UserLogVO();
                logVO.setUserId(vo.getUserId());
                logVO.setLogType("002");
                logVO.setIpAddr(vo.getIpAddr());
                userDAO.insUserLog(logVO);

                loginVO = null;
            }

        return loginVO;
    }


    @Override
    public SessionVO loginProcess(UserVO vo) throws Exception {
        SessionVO loginVO = new SessionVO();
        UserVO userVO = new UserVO();

        userVO = userDAO.loginProcess(vo);

        if (userVO == null) return null;
        logger.info("login result" + userVO.getUserId() + "/" + userVO.getUserType());

        String userPwd = userVO.getUserPwd();
        
        String loginLockYn = userVO.getLoginLockYn();	
        int loginCount = userVO.getLoginCount();	
        
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
        
        String encPwd = SecurityUtil.encrypt(vo.getUserPwd());

        UserLogVO logVO = new UserLogVO();
        logVO.setUserId(vo.getUserId());
        logVO.setLogType("000");
        userDAO.insUserLog(logVO);

        //logger.info(userPwd+"^"+encPwd+"^"+vo.getUserPwd());
        if (userPwd.equals(encPwd)) {
            logger.info(userVO.getUserId() + "패스워드 일치");
            loginVO.setLoginId(userVO.getUserId());
            loginVO.setUserName(userVO.getUserName());
            loginVO.setUserType(userVO.getUserType());

            EmpVO empVO = new EmpVO();
            empVO.setLoginId(vo.getUserId());
            empVO.setStartPage(0);
            empVO.setEndPage(10);
            List<EmpVO> empList = empDAO.getEmpList(empVO);

            logger.info("size=>" + empList.size());
            EmpVO resultEmpVO = new EmpVO();
            if (empList.size() > 0) {
                for (int i = 0; i < empList.size(); i++) {
                    resultEmpVO = empList.get(i);
                    logger.info("사용자정보 : biz[" + resultEmpVO.getBizId() + "], user[" + resultEmpVO.getUserId() + "] ");
                }
            }
            if (resultEmpVO != null && empList.size() > 0) {
                String bizId = resultEmpVO.getBizId();
                String bizName = resultEmpVO.getBizName();
                String deptCode = resultEmpVO.getDeptCode();
                loginVO.setBizId(bizId);
                loginVO.setBizName(bizName);
                loginVO.setDeptCode(deptCode);
                loginVO.setUserId(resultEmpVO.getUserId());
                loginVO.setPhoneNum(resultEmpVO.getPhoneNum());

                logVO = new UserLogVO();
                logVO.setUserId(vo.getUserId());
                logVO.setLogType("001");
                logVO.setIpAddr(vo.getIpAddr());
                userDAO.insUserLog(logVO);

                BizVO paramBizVO = new BizVO();
                paramBizVO.setBizId(bizId);
                BizVO resBizVO = bizDAO.getBiz(paramBizVO);
                
                if(resBizVO != null){
                	loginVO.setUseContract(resBizVO.getUseContract());
                	loginVO.setUseElecDoc(resBizVO.getUseElecDoc());
                	loginVO.setUsePayStub(resBizVO.getUsePayStub());
                	loginVO.setBizMngYn(resBizVO.getBizMngYn());
                	loginVO.setMainBizYn(resBizVO.getMainBizYn());
                    loginVO.setUseContractData(resBizVO.getUseContractData());
                }
                
                YS000VO ys000VO = new YS000VO();

                ys000VO.setBizId(loginVO.getBizId());
//                ys000VO.setFebYear(Integer.toString(Integer.parseInt(DateUtil.getYear()) - 1));
//                ys000VO.setFebYear(Integer.toString(Integer.parseInt(DateUtil.getYear())));

                List<YS000VO> result = ys000DAO.getYS000List(ys000VO);

                if (result.size() > 0) {
                    loginVO.setYearContractId(result.get(0).get계약ID());
                    loginVO.setFebYear(result.get(0).getFebYear());
                }
                
                //로그인 실패 카운트 0으로 초기화	
                UserVO userVOParam = new UserVO();	
                userVOParam.setUserId(vo.getUserId());	
                userVOParam.setLoginCount(0);	
                userDAO.updUser(userVOParam);
                
                String curDate = DateUtil.getTimeStamp(7);
                logger.info("접속한 사용자 [" + userVO.getUserName() + "] Connected Time=>" + curDate);

            } else {

                logVO = new UserLogVO();
                logVO.setUserId(vo.getUserId());
                logVO.setLogType("002");
                logVO.setIpAddr(vo.getIpAddr());
                userDAO.insUserLog(logVO);

                loginVO = null;
            }

        } else {
        	logVO = new UserLogVO();	
            logVO.setUserId(vo.getUserId());	
            logVO.setLogType("003");	
            logVO.setIpAddr(vo.getIpAddr());	
            userDAO.insUserLog(logVO);	
            //로그인 실패 카운트 증가	
            UserVO userVOParam = new UserVO();	
            userVOParam.setUserId(vo.getUserId());	
            userVOParam.setLoginCount(loginCount + 1);	
            userDAO.updUser(userVOParam);	
            loginVO.setLoginFailYn("Y");	
            loginVO.setLoginCount(loginCount + 1);	
        }

        return loginVO;
    }

    @Override
    public void logOutProcess(SessionVO vo) throws Exception {

        UserLogVO logVO = new UserLogVO();
        logVO.setUserId(vo.getLoginId());
        logVO.setLogType("009");
        userDAO.insUserLog(logVO);
    }

    @Override
    public int updUserPwd(UserVO vo) throws Exception {
        int result = 0;
        String userPwd = "";

        UserVO userVO = userDAO.loginProcess(vo);
        String prevUserPwd = SecurityUtil.encrypt(vo.getPrevUserPwd());
        if (!userVO.getUserPwd().equals(prevUserPwd)) return 0;

        // 암호화
        if (StringUtil.isNotNull(vo.getUserPwd())) {
            userPwd = SecurityUtil.encrypt(vo.getUserPwd());
            vo.setUserPwd(userPwd);
        }

        result = userDAO.updUserPwd(vo);

        return result;
    }
    
    @Override
    public Boolean isUserPwd(UserVO vo) throws Exception {

        UserVO userVO = userDAO.loginProcess(vo);
        String prevUserPwd = SecurityUtil.encrypt(vo.getUserPwd());
        if (userVO.getUserPwd().equals(prevUserPwd)) return true;
        else                                         return false;
    }
    
    @Override
    public int updMobileUserPwd(UserVO vo) throws Exception {
        int result     = 0;
        String userPwd = "";

        // 암호화
        if (StringUtil.isNotNull(vo.getUserPwd())) {
            userPwd = SecurityUtil.encrypt(vo.getUserPwd());
            vo.setUserPwd(userPwd);
        }

        result = userDAO.updUserPwd(vo);

        return result;
    }

    @Override
    public int resetUserPwd(UserVO vo) throws Exception {
        int result     = 0;
        String userPwd = "";

        // 암호화
        if (StringUtil.isNotNull(vo.getUserPwd())) {
            userPwd = SecurityUtil.encrypt(vo.getUserPwd());
            vo.setUserPwd(userPwd);
        }

        result = userDAO.resetUserPwd(vo);

        return result;
    }
    
    //사용자 비밀번호를 생년월일 8자리로 변경
    @Override
    public int resetUserPwdByDate(UserVO vo) throws Exception {

        int result     = 0;
        String userPwd = "";

        // 암호화
        if (StringUtil.isNotNull(vo.getUserPwd())) {
            userPwd = SecurityUtil.encrypt(vo.getUserPwd());
            vo.setUserPwd(userPwd);
        }

        result = userDAO.updUserPwd(vo);

        return result;
    }

    @Override
    public int insUser(UserVO vo) throws Exception {

        int result      = 0;
        int userType    = Integer.parseInt(vo.getUserType());
        String empNo    = vo.getEmpNo();
        String userName = vo.getUserName();

        logger.info("insUser Param=>" + vo.getUserId() + "-" + vo.getLoginId() + "-" + userType);

        if (userType > 5) {
            // 해당 기업 가입여부체크
            BizVO chkBizVO = new BizVO();
            chkBizVO.setBusinessNo(vo.getBusinessNo());
            chkBizVO.setStartPage(0);
            chkBizVO.setEndPage(10);
            logger.info("0");
            List<BizVO> bizList = bizDAO.getBizList(chkBizVO);
            if (bizList.size() > 0) { return -100; }
        }

        // 임직원시 회사의 인사정보에 로그인ID 설정
        if (userType <= 5) {
            EmpVO userTypeEmpVO = new EmpVO();
            userTypeEmpVO.setUserId(vo.getUserId());
            userTypeEmpVO.setStartPage(0);
            userTypeEmpVO.setEndPage(10);
            List<EmpVO> empList = empDAO.getEmpList(userTypeEmpVO);
            if (empList.size() > 0) {
                vo.setUserType(empList.get(0).getEmpType());
                vo.setBizId(empList.get(0).getBizId());
            }
            EmpVO empVO = new EmpVO();
            empVO.setUserId(vo.getUserId());
            empVO.setLoginId(vo.getLoginId());
            empVO.setBizId(vo.getBizId());
            empDAO.updEmp(empVO);
            result++;
        }

        // 기업담당자, 노무사 설정
        if (userType > 5) {
            // 기업등록
            BizVO bizVO = new BizVO();
            bizVO.setBizName(vo.getBizName());
            bizVO.setOwnerName(vo.getOwnerName());
            bizVO.setBusinessNo(vo.getBusinessNo());
            bizVO.setAddr1(vo.getCompanyAddr());
            bizVO.setFunnel(vo.getFunnel());
            bizVO.setFunnelYear(vo.getFunnelYear());

            if (vo.getFunnel().equals("200") && vo.getFunnelYear().equals("2022")) { bizVO.setPayPoint("5"); }

            bizVO.setUseElecDoc("Y");
            bizVO.setUsePayStub("Y");
            bizVO.setBizMngYn("N");
            bizVO.setMainBizYn("Y");
            bizVO.setPersonEMail(vo.getEMail());
            bizVO.setPersonUserName(vo.getUserName());
            bizVO.setPersonUserTelNum(vo.getPhoneNum());
            bizDAO.insBiz(bizVO);
            // 기업서비스등록
            bizDAO.insBizService(bizVO);
            result++;

            if (StringUtil.isNull(empNo)) { empNo = "admin"; }

            // 담당자정보
            EmpVO insEmpVO = new EmpVO();
            insEmpVO.setBizId(bizVO.getBizId());
            insEmpVO.setEmpNo(empNo);
            insEmpVO.setEmpName(vo.getUserName());
            insEmpVO.setEmpType(vo.getUserType());
            insEmpVO.setEMail(vo.getEMail());
            insEmpVO.setPhoneNum(vo.getPhoneNum());
            insEmpVO.setCountryType("82");
            insEmpVO.setUserDate("19990101");
            insEmpVO.setJoinDate(DateUtil.getTimeStamp(3));
            insEmpVO.setConfirmType("Y");
            insEmpVO.setLoginId(vo.getLoginId());
            empDAO.insEmp(insEmpVO);

            // 직인이미지 존재시 복사 및 DB처리
            if (StringUtil.isNotNull(vo.getCompanyImage())) {
                String szMonthPath = MultipartFileUtil.SEPERATOR + DateUtil.getTimeStamp(14);
                String imgSavePath = MultipartFileUtil.getSystemPath() + "images/sign" + szMonthPath + MultipartFileUtil.SEPERATOR + bizVO.getBizId() + ".png";
                logger.info(vo.getCompanyImage());
                logger.info(imgSavePath);
                FileUtil.createDirectory(MultipartFileUtil.getSystemPath() + "images/sign" + szMonthPath);
                if (FileUtil.fileCopy(vo.getCompanyImage(), imgSavePath)) {
                    bizVO.setCompanyImage(szMonthPath + MultipartFileUtil.SEPERATOR + bizVO.getBizId() + ".png");
                    bizDAO.updBiz(bizVO);
                }
            }

            // 기업 기본제공 근로계약서 양식 및 서식등록
            contractService.setContractNewData(bizVO.getBizId());

            // 포인트 충전
            PointVO pointVO = new PointVO();
            pointVO.setBizId(bizVO.getBizId());
            pointVO.setUserId("");
            if (vo.getFunnel().equals("200"))      { pointVO.setCurPoint(0); }
            else if (vo.getFunnel().equals("600")) { pointVO.setCurPoint(300); }
            else                                   { pointVO.setCurPoint(100); }

            pointDAO.insPoint(pointVO);
            
            // 포인트 로그 추가
            PointLogVO pointLogVO = new PointLogVO();
			pointLogVO.setBizId(bizVO.getBizId());
			pointLogVO.setUserId("");
			pointLogVO.setPointType("0");
			pointLogVO.setPointPrice(pointVO.getCurPoint());
			pointLogVO.setPointResult(pointVO.getCurPoint());
            if (vo.getFunnel().equals("600")) { pointLogVO.setEtcDesc("뉴젠솔루션 프로모션을 통한 가입으로 300포인트 무료 제공하였습니다."); }
            else                              { pointLogVO.setEtcDesc("최초 가입"); }

			logger.info("최초 가입");
			pointDAO.insPointLog(pointLogVO);

            //로그인 정보 추가
            UserLogVO logVO = new UserLogVO();
            logVO.setUserId(vo.getLoginId());
            logVO.setLogType("001");
            userDAO.insUserLog(logVO);
            
            result++;
        }

        if(vo.getUserPwd() != null){
        	//SNS로그인은 password가 빈값으로 들어간다.
	        String userPwd = SecurityUtil.encrypt(vo.getUserPwd());
	        vo.setUserPwd(userPwd);
        }
        vo.setUserId(vo.getLoginId());
        vo.setAppType("N");

        // 기업담당자, 노무사 설정
        if (userType > 5) { vo.setUserName("관리자"); }
        // 임직원
        else              { vo.setUserName(userName); }
        userDAO.insUser(vo);
        result++;

        logger.info("Success!!");
        return result;
    }


    // 부문 관리자 등록
    @Override
    public int insUserAdmin(UserVO vo) throws Exception {

        int result   = 0;
        int userType = Integer.parseInt(vo.getUserType());
        logger.info("userType=>" + userType);

        // 담당자정보
        EmpVO insEmpVO = new EmpVO();
        insEmpVO.setBizId(vo.getBizId());
        insEmpVO.setEmpNo(vo.getEmpNo());
        insEmpVO.setEmpName(vo.getUserName());
        insEmpVO.setEMail(vo.getEMail());
        insEmpVO.setPhoneNum(vo.getPhoneNum());
        insEmpVO.setCountryType("82");
        insEmpVO.setUserDate("19990101");
        insEmpVO.setJoinDate(DateUtil.getTimeStamp(3));
        insEmpVO.setConfirmType("Y");
        insEmpVO.setLoginId(vo.getLoginId());

        EmpVO resultVO = empDAO.getEmpNo(insEmpVO);

        // 입력 정보의 사번 존재하는지 체크
        EmpVO check  = new EmpVO();
        check.setBizId(vo.getBizId());
        check.setEmpNo(vo.getEmpNo());
        EmpVO dupEmp = empDAO.getEmpNo(check);

        if (resultVO == null && dupEmp == null) {
            insEmpVO.setEmpType(vo.getUserType());
            empDAO.insEmp(insEmpVO);
            result++;
            String userPwd = SecurityUtil.encrypt(vo.getUserPwd());
            vo.setUserId(vo.getLoginId());
            vo.setUserPwd(userPwd);
            vo.setUserName(vo.getUserName());
            vo.setUserType(vo.getUserType());
            vo.setAppType("N");
            userDAO.insUser(vo);
            result++;
        } else { result = -100; }

        return result;
    }

    @Override
    public int updUser(UserVO vo) throws Exception { return userDAO.updUser(vo); }

    @Override
    public List<UserVO> getUserList(UserVO vo) throws Exception { return userDAO.getUserList(vo); }


    @Override
    public List<UserVO> findUser(UserVO vo) throws Exception { return userDAO.findUser(vo); }

    @Override
    public void insUserCert(UserCertVO vo) throws Exception { userDAO.insUserCert(vo); }


    public void insUserMok(UserMokVO vo) throws Exception { userDAO.insUserMok(vo); }

    @Override
    public void insUserJoinCert(UserJoinCertVO vo) throws Exception {
        // 알림톡 전송
        BizTalkKKOVO kkoVO = new BizTalkKKOVO();

        logger.info(vo.getPhoneNum() + "/" + vo.getCertNo());

        String content = "[CANDY BOX * 본인확인]\n";
        content += vo.getBizName() + " " + vo.getEmpName() + "님의 \n";
        content += "본인확인을 위해 [" + vo.getCertNo() + "]를 입력해 주세요.\n";

        kkoVO.setBizId(vo.getBizId());
        kkoVO.setSubject("");
        kkoVO.setContent(content);
        kkoVO.setCallback(System.getProperty("biztalk.kko.callback_number"));
        kkoVO.setRecipientNum(vo.getPhoneNum());
        kkoVO.setSenderKey(System.getProperty("biztalk.candybox.sender_key"));
        kkoVO.setTemplateCode("candybox001");
        kkoVO.setKkoBtnType("");
        kkoVO.setKkoBtnInfo("");
        bizTalkDAO.insBizTalk(kkoVO);

        userDAO.insUserJoinCert(vo);
    }

    @Override
    public int getUserJoinCert(UserJoinCertVO vo) throws Exception {
        int result = 0;
        UserJoinCertVO userJoinCertVO = userDAO.getUserJoinCert(vo);

        if (userJoinCertVO != null) {
            Date curDate = new Date();
            Date reqDate = DateUtil.StringToDate(userJoinCertVO.getReqDate(), "yyyyMMddHHmmss");

            long reqDateTime = reqDate.getTime();
            long curDateTime = curDate.getTime();

            long minute = (curDateTime - reqDateTime) / 60000;
            if (minute <= 3) {
                userDAO.updUserJoinCert(vo);
                result = 1;
            }
        }
        return result;
    }

    @Override
    public int insUserGrpBiz(BizVO vo) throws Exception {
        int result = 0;

        // 해당 기업 가입여부체크
        BizVO chkBizVO = new BizVO();
        chkBizVO.setBusinessNo(vo.getBusinessNo());
        chkBizVO.setStartPage(0);
        chkBizVO.setEndPage(10);
        List<BizVO> bizList = bizDAO.getBizList(chkBizVO);
        if (bizList.size() > 0) {
            return -100;
        }

        String userId = vo.getBizId();
        // 기업등록
        bizDAO.insBiz(vo);
        // 기업서비스등록
        bizDAO.insBizService(vo);
        result++;

        // 직인이미지 존재시 복사 및 DB처리
        if (StringUtil.isNotNull(vo.getCompanyImage())) {
            String szMonthPath = MultipartFileUtil.SEPERATOR + DateUtil.getTimeStamp(14);
            String imgSavePath = MultipartFileUtil.getSystemPath() + "images/sign" + szMonthPath + MultipartFileUtil.SEPERATOR + vo.getBizId() + ".png";
            logger.info(vo.getCompanyImage());
            logger.info(imgSavePath);
            FileUtil.createDirectory(MultipartFileUtil.getSystemPath() + "images/sign" + szMonthPath);
            if (FileUtil.fileCopy(vo.getCompanyImage(), imgSavePath)) {
                vo.setCompanyImage(szMonthPath + MultipartFileUtil.SEPERATOR + vo.getBizId() + ".png");
                bizDAO.updBiz(vo);
                result++;
            }
        }

        // 기업 기본제공 근로계약서 양식 및 서식등록
        contractService.setContractNewData(vo.getBizId());


        // 등록된 기업 권한부여(별도 승인절차 없음)
        UserGrpVO userGroupVO = new UserGrpVO();
        userGroupVO.setUserId(userId);
        userGroupVO.setGrpType("B");
        userGroupVO.setRefId(vo.getBizId());
        userGroupVO.setStatusCode("RES");
        userDAO.insUserGrp(userGroupVO);
        result++;

        return result;
    }

    @Override
    public void insUserGrp(UserGrpVO vo) throws Exception {
        String refList = vo.getRefId();
        String[] list = StringUtil.split(refList, "|");
        for (int i = 0; i < list.length; i++) {
            String data = list[i];
            vo.setRefId(data);
            userDAO.insUserGrp(vo);
        }
    }

    @Override
    public void delUserGrp(UserGrpVO vo) throws Exception {
        String refList = vo.getRefId();
        String[] list = StringUtil.split(refList, "|");
        for (int i = 0; i < list.length; i++) {
            String data = list[i];
            String[] delData = StringUtil.split(data, "-");
            vo.setGrpType(delData[0]);
            vo.setRefId(delData[1]);
            userDAO.delUserGrp(vo);

            BizVO bizVO = new BizVO();
            bizVO.setBizId(delData[1]);
            bizDAO.delBiz(bizVO);
        }
    }

    @Override
    public List<UserGrpVO> getUserGrp(UserGrpVO vo) throws Exception {
        List<UserGrpVO> userList = new ArrayList<UserGrpVO>();
        userList = userDAO.getUserGrp(vo);

        return userList;
    }

    @Override
    public List<UserGrpVO> getUserGrpList(UserGrpVO vo) throws Exception {
        List<UserGrpVO> userList = new ArrayList<UserGrpVO>();
        userList = userDAO.getUserGrpList(vo);

        return userList;
    }

    @Override
    public String getUserNonce() {

        String userNonce = StringUtil.getRandom(10);
        UserVO paramVO = new UserVO();
        paramVO.setUserNonce(userNonce);

        List<UserVO> list = userDAO.getUserList(paramVO);

        if (list.size() > 0) {
            getUserNonce();
        }
        return userNonce;
    }

    @Override
    public int delUserLoginInfo(UserVO vo) throws Exception {
        int result = 0;
        EmpVO empVO = new EmpVO();
        empVO.setUserId(vo.getUserId());
        empVO.setLoginId("");
        empVO.setStartPage(0);
        empVO.setEndPage(10);
        
        List<EmpVO> empList = empDAO.getEmpList(empVO);
        if (empList.size() > 0) {
        	EmpVO resultEmpVO = empList.get(0);
        	vo.setUserId(resultEmpVO.getLoginId());
        	// 계정삭제
        	userDAO.delUser(vo);

            result++;
        	
        	// 로그인ID 초기화
        	empDAO.resetLoginId(empVO);

            result++;
        }
        return result;
    }


}
