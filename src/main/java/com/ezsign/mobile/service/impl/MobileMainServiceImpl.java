package com.ezsign.mobile.service.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.ezsign.point.dao.PointDAO;
import com.ezsign.point.vo.PointVO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.annual.dao.AnnualVacationDAO;
import com.ezsign.annual.vo.AnnualVacationVO;
import com.ezsign.attend.vo.AttendVO;
import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.content.dao.CabinetDAO;
import com.ezsign.content.dao.ContentDAO;
import com.ezsign.content.service.ContentService;
import com.ezsign.content.vo.CabinetVO;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.contract.dao.ContractDAO;
import com.ezsign.contract.vo.ContractPersonVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpLogVO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.system.dao.YS010DAO;
import com.ezsign.feb.system.vo.YS010VO;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.mobile.dao.WelmoneyDAO;
//import com.ezsign.mobile.controller.BadPaddingException;
//import com.ezsign.mobile.controller.Base64;
//import com.ezsign.mobile.controller.Cipher;
//import com.ezsign.mobile.controller.IllegalBlockSizeException;
//import com.ezsign.mobile.controller.InvalidAlgorithmParameterException;
//import com.ezsign.mobile.controller.InvalidKeyException;
//import com.ezsign.mobile.controller.IvParameterSpec;
//import com.ezsign.mobile.controller.NoSuchAlgorithmException;
//import com.ezsign.mobile.controller.NoSuchPaddingException;
//import com.ezsign.mobile.controller.SecretKey;
//import com.ezsign.mobile.controller.SecretKeySpec;
//import com.ezsign.mobile.controller.UnsupportedEncodingException;
import com.ezsign.mobile.service.MobileMainService;
import com.ezsign.mobile.vo.MobileMainResponse;
import com.ezsign.paystub.dao.PaystubDAO;
import com.ezsign.paystub.service.PaystubService;
import com.ezsign.paystub.vo.PaystubDataVO;
import com.ezsign.paystub.vo.PaystubMainVO;
import com.ezsign.paystub.vo.PaystubVO;
import com.ezsign.user.dao.DeviceDAO;
import com.ezsign.user.dao.UserDAO;
import com.ezsign.user.vo.UserVO;
import com.ezsign.welmoney.dao.WelMoneyUserDAO;
import com.ezsign.welmoney.vo.WelMoneyUserVO;
import com.jarvis.pdf.util.ControlUtil;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@SuppressWarnings("deprecation")
@Service("mobileMainService")
public class MobileMainServiceImpl extends AbstractServiceImpl implements MobileMainService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name="annualVacationDAO")
	private AnnualVacationDAO annualVacationDAO;

	@Resource(name="paystubDAO")
	private PaystubDAO paystubDAO;

	@Resource(name="contractDAO")
	private ContractDAO contractDAO;

	@Resource(name="ys010DAO")
	private YS010DAO ys010DAO;
	
	@Resource(name="ye000DAO")
	private YE000DAO ye000DAO;
	
	@Resource(name="bizDAO")
	private BizDAO bizDAO;
	
	@Resource(name = "empDAO")
    private EmpDAO empDAO;
	
	@Resource(name = "userDAO")
    private UserDAO userDAO;

	@Resource(name = "pointDAO")
	private PointDAO pointDAO;
	
	@Resource(name = "deviceDAO")
    private DeviceDAO deviceDAO;
	
	@Resource(name = "contentDAO")
    private ContentDAO contentDAO;
	
	@Resource(name = "cabinetDAO")
    private CabinetDAO cabinetDAO;
	
	@Resource(name = "welmoneyDAO")
    private WelmoneyDAO welmoneyDAO;
	
	@Resource(name = "welMoneyUserDAO")
    private WelMoneyUserDAO welMoneyUserDAO;
	
	
	@Resource(name="contentService")
	private ContentService contentService;
	
	
	@Override
	public MobileMainResponse getMobileMain(SessionVO loginVO, String 계약ID) throws Exception {
		MobileMainResponse response = new MobileMainResponse();

		AnnualVacationVO avVO = new AnnualVacationVO();
		avVO.setBizId(loginVO.getBizId());
		avVO.setUserId(loginVO.getUserId());
		response.연차정보 = annualVacationDAO.getAnnualVacation(avVO);
				
		PaystubVO paystubVO = new PaystubVO();
		paystubVO.setBizId(loginVO.getBizId());
		paystubVO.setUserId(loginVO.getUserId());
		response.급여정보 = paystubDAO.getPaystubMain(paystubVO);
		
		int payCount = paystubDAO.getPaystubListCount(paystubVO);
		response.급여정보.setCount(payCount);
		
		response.출퇴근정보 = new AttendVO();
		// 2024.08.23 캔디app에서 아래 쿼리로 인해 속도느림 현상이 발생하였지만,
		// 실제 사용하지 않은쿼리로 파악되어서 주석처리함.
//		ContractPersonVO contractPersonVO = new ContractPersonVO();
//		contractPersonVO.setBizId(loginVO.getBizId());
//		contractPersonVO.setUserId(loginVO.getUserId());
//		response.계약정보 = contractDAO.getContractPersonMain(contractPersonVO);
//
//		contractPersonVO.setUserId(loginVO.getLoginId());
//		int contractCount = contractDAO.getContractPersonListCount(contractPersonVO);
//		response.계약정보.setCount(contractCount);
//
//		if (!계약ID.isEmpty()) {
//		YS010VO ys010VO = new YS010VO();
//		ys010VO.set계약ID(계약ID);
//		List<YS010VO> list =  ys010DAO.getYS010List(ys010VO);
//		response.연말정산정보 = new YS010VO();
//		if(list.size()>0) {
//			response.연말정산정보 = list.get(0);
//		}
//
//		YE000VO selYE000VO = new YE000VO();
//		selYE000VO.set계약ID(계약ID);
//		selYE000VO.setBizId(loginVO.getBizId());
//		selYE000VO.set사용자ID(loginVO.getUserId());
//
//		response.연말대상정보 = ye000DAO.getYE000(selYE000VO);
//		} else {
//			response.연말정산정보 = null;
//			response.연말대상정보 = null;
//		}
//
		//암호 입력대상여부 확인
		BizVO bizVO = new BizVO();
		bizVO.setBizId(loginVO.getBizId());
		BizVO resultVO = bizDAO.getBiz(bizVO);
		if (resultVO.getUsePayPassword().equals("Y")) {
			response.암호입력대상정보 = true;
		} else {
			response.암호입력대상정보 = false;
		}
		return response;
	}
	
	@Override
	public List<PaystubMainVO> getPaystubMobileList(SessionVO loginVO) throws Exception {
		List<PaystubMainVO> response = new ArrayList<PaystubMainVO>();
		
		PaystubVO paystubVO = new PaystubVO();
		paystubVO.setBizId(loginVO.getBizId());
		paystubVO.setUserId(loginVO.getUserId());
		response = paystubDAO.getPaystubMobileList(paystubVO);
		
		return response;
	}

	@Override
	public boolean checkBusinessNumber(String businessNo) throws Exception {
		
		BizVO chkBizVO = new BizVO();
		chkBizVO.setBusinessNo(businessNo);
		chkBizVO.setStartPage(0);
		chkBizVO.setEndPage(10);
		List<BizVO> bizList = bizDAO.getBizList(chkBizVO);
		if(bizList.size()>0) {
			return false;
		}
		return true;
	}
	
	@Override
	public List<BizVO> getBizListGetOne(BizVO vo) throws Exception {
		
		BizVO chkBizVO = new BizVO();
		chkBizVO.setStartPage(0);
		chkBizVO.setEndPage(1);
		List<BizVO> bizList = bizDAO.getBizList(chkBizVO);
		
		return bizList;
	}
	
	/**
     * 캔디캐시 기업회원 가입처리
     */
	@Override
    public int insMobileBizUser(UserVO vo) throws Exception{
    	int result = 0;
        int userType = Integer.parseInt(vo.getUserType());
        String empNo = vo.getEmpNo();
        String userName = vo.getUserName();

        logger.info("insMobileBizUser Param=>" + vo.getUserId() + "-" + vo.getLoginId() + "-" + userType);

        if (userType > 5) {
            // 해당 기업 가입여부체크
            BizVO chkBizVO = new BizVO();
            chkBizVO.setBusinessNo(vo.getBusinessNo());
            chkBizVO.setStartPage(0);
            chkBizVO.setEndPage(10);
            System.out.println("0");
            List<BizVO> bizList = bizDAO.getBizList(chkBizVO);
            if (bizList != null && bizList.size() > 0) {
                return -100;
            }
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
            }
            EmpVO empVO = new EmpVO();
            empVO.setUserId(vo.getUserId());
            empVO.setLoginId(vo.getLoginId());
            empDAO.updEmp(empVO);
            result++;
        }

        String bizId = "";
        // 기업담당자, 노무사 설정
        if (userType > 5) {
            // 기업등록
            BizVO bizVO = new BizVO();
            bizVO.setBizName(vo.getBizName());
            bizVO.setOwnerName(vo.getOwnerName());
            bizVO.setBusinessNo(vo.getBusinessNo());
            bizVO.setFunnel("400");
            bizVO.setResendPoint("0");
            bizVO.setSendPoint("0");
            bizVO.setFormPoint("0");
            bizVO.setScanPoint("0");
			
			if(vo.getLoginId() != null && vo.getLoginId().contains("ieumcheck")){
				bizVO.setFunnel("410"); // 이음체크 구분자 추가
			}
            
            bizDAO.insBiz(bizVO);
            bizId = bizVO.getBizId();
            // 기업서비스등록
            bizDAO.insBizService(bizVO);

			// 기본 양식등록
			bizDAO.insDefaultContractForm(bizVO);

            result++;
            if (StringUtil.isNull(empNo)) {
                empNo = "admin";
            }
            // 담당자정보
            EmpVO insEmpVO = new EmpVO();
            insEmpVO.setBizId(bizVO.getBizId());
            insEmpVO.setEmpNo(empNo);
            insEmpVO.setEmpName("관리자");
            insEmpVO.setEmpType(vo.getUserType());
            insEmpVO.setEMail(vo.getEMail());
            insEmpVO.setPhoneNum(vo.getPhoneNum());
            insEmpVO.setCountryType("82");
            //insEmpVO.setUserDate("19990101");
            insEmpVO.setJoinDate(DateUtil.getTimeStamp(3));
            insEmpVO.setConfirmType("Y");
            insEmpVO.setLoginId(vo.getLoginId());
            insEmpVO.setAddr1(vo.getCompanyAddr());
            empDAO.insEmp(insEmpVO);
            
            // 포인트 충전
            PointVO pointVO = new PointVO();
            pointVO.setBizId(bizVO.getBizId());
            pointVO.setUserId("");
            pointVO.setCurPoint(100);
            pointDAO.insPoint(pointVO);

            result++;
            
            vo.setBizId(bizId);
        }else {
        	bizId = StringUtil.nvl(vo.getBizId(), "");
        }

        if(vo.getUserPwd() != null){
        	//SNS로그인은 password가 빈값으로 들어간다.
	        String userPwd = SecurityUtil.encrypt(vo.getUserPwd());
	        vo.setUserPwd(userPwd);
        }
        
        vo.setUserId(vo.getLoginId());
        vo.setAppType("N");

        if (userType > 5) { // 기업담당자, 노무사 설정
            //vo.setUserName("관리자");
        	vo.setUserName("관리자");
        } else { // 임직원
            vo.setUserName(userName);
        }
        userDAO.insUser(vo);
        
        result++;

        return result;
    }
	
	/*
	 * 캔디캐시 임금명세서 등록
	 * */

	@Override
	public String insMobilePaystub(PaystubVO vo) throws Exception {
		
		String returnValue = "";
		//데이터 인서트
		vo.setServiceId(PaystubService.SERVICE_ID);
		vo.setTransType("N");
		vo.setPayFormType("2");
		
		paystubDAO.insPaystub(vo);
		
		List<PaystubVO> result = paystubDAO.getPaystubList(vo);
		
		if (result.size() > 0) {
			returnValue = result.get(0).getInsDate();
		}
		
		return returnValue;
	}
	
	@Override
	public void insMobilePaystubData(PaystubDataVO vo) throws Exception {
		
		//데이터 인서트
		vo.setServiceId(PaystubService.SERVICE_ID);
		
		paystubDAO.insPaystubData(vo);
		
		
		return;
	}

	@Override
	public void delPaystub(PaystubVO vo) throws Exception {
		
		List<ContentVO> contentList = new ArrayList<ContentVO>();
		
		
		paystubDAO.delPaystub(vo);
		paystubDAO.delPaystubData(vo);
		ContentVO fileVO = new ContentVO();
		fileVO.setFileId(vo.getFileId());
		contentList.add(fileVO);
			
		
		// 급여명세서 파일 및 콘텐츠 삭제처리
		contentService.deleteContent(contentList);
		
		
	}

	@Override
	public String getCandyCashVersion() throws Exception {
		return deviceDAO.getCandyCashVersion();
	}
	
	
	@Override
	public ContractPersonVO getContractView(ContractPersonVO vo, SessionVO loginVO)  throws Exception {
				
		ContractPersonVO result = contractDAO.getContractPerson(vo);
		
		// 콘텐츠에서 정보 추출	
		ContentVO contentVO = new ContentVO();
		contentVO.setFileId(result.getContractId());
		ContentVO resultVO = contentDAO.getContent(contentVO);
		if(resultVO==null) {
			logger.error("[getContractView] 콘텐츠 정보가 존재하지 않습니다.");
			return null;
		}
		
		// 저장위치
		CabinetVO cabinetVO = new CabinetVO();
		cabinetVO.setClassId(resultVO.getClassId());
		cabinetVO.setCabinetId(resultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if(cabinetResultVO==null) {
			logger.error("[getContractView] 캐비넷 정보가 존재하지 않습니다.");
			return null;
		}
		
		String originalPdfPath = cabinetResultVO.getCabinetPath()+resultVO.getFilePath()+resultVO.getFileName();
		String targetPdfName = StringUtil.getUUID()+".pdf";
		String targetPdfPath = MultipartFileUtil.getSystemTempPath()+targetPdfName;
		
		// 사용할 PDF복사
		FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
		if(FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
			ControlUtil.writePDF(null, originalPdfPath, targetPdfPath, false, true, System.getProperty("pdf.font.default"));
			
			String pdf_uri = StringUtil.ReplaceAll(targetPdfPath, MultipartFileUtil.getSystemPath(), "");
			result.setPdfFile(pdf_uri);
		}
		
		if(result != null && StringUtil.isNotNull(loginVO.getIpAddr())){
			List<String> userIdList = new ArrayList();
			userIdList.add(result.getUserId());
			
			EmpLogVO logVO = new EmpLogVO();
			logVO.setInsUserId(loginVO.getUserId());
			logVO.setUserIdList(userIdList);
			logVO.setLogType("002");
			logVO.setIpAddr(loginVO.getIpAddr());
			logVO.setBizId(loginVO.getBizId());
			empDAO.insEmpLog(logVO);
		}
		return result;
	}
	
	/*
	 * 웰머니 로그인 로그 기록
	 * */
	@Override
	public void insWelmoneyLoginLog(String loginId, String ipAddr) throws Exception {
		
		welmoneyDAO.insWelmoneyLoginLog(loginId,ipAddr);
		
	}
	
	/*
	 * 웰머니 로그인 사용기간 조회
	 * */
	@Override
	public boolean welmoneyLoginCheck(String loginId) throws Exception {
		boolean chk = false;
		WelMoneyUserVO vo = new WelMoneyUserVO();
		vo.setUserId(loginId);
		
		WelMoneyUserVO welMoneyUserVO = welMoneyUserDAO.welmoneyLoginCheck(vo);
		
		if(welMoneyUserVO != null){
			chk = true;
		}
		
		return chk;
	}
	
	
}
