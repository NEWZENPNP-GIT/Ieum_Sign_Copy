package com.ezsign.biz.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.service.BizService;
import com.ezsign.biz.vo.BizGrpVO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.biz.vo.CandycashBizVO;
import com.ezsign.content.service.ContentService;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.contract.dao.ContractDAO;
import com.ezsign.contract.service.ContractService;
import com.ezsign.contract.vo.ContractPersonNewVO;
import com.ezsign.contract.vo.ContractPersonVO;
import com.ezsign.dept.dao.DeptDAO;
import com.ezsign.dept.vo.DeptVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.paystub.dao.PaystubDAO;
import com.ezsign.paystub.vo.PaystubVO;
import com.ezsign.point.dao.PointDAO;
import com.ezsign.point.vo.PointLogVO;
import com.ezsign.point.vo.PointVO;
import com.ezsign.user.dao.UserDAO;
import com.ezsign.user.vo.UserGrpVO;
import com.ezsign.user.vo.UserVO;
import com.jarvis.common.util.FileUtil;
import com.jarvis.common.util.StringUtil;


@Service("bizService")
public class BizServiceImpl extends AbstractServiceImpl implements BizService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name="contentService")
	private ContentService contentService;

	@Resource(name="contractService")
	private ContractService contractService;

	@Resource(name="userDAO")
	private UserDAO userDAO;

	@Resource(name="empDAO")
	private EmpDAO empDAO;

	@Resource(name="bizDAO")
	private BizDAO bizDAO;

	@Resource(name="deptDAO")
	private DeptDAO deptDAO;

	@Resource(name="contractDAO")
	private ContractDAO contractDAO;

	@Resource(name="paystubDAO")
	private PaystubDAO paystubDAO;

	@Resource(name="pointDAO")
	private PointDAO pointDAO;

	@Override
	public int getCheckService(BizVO vo)  throws Exception {

		return bizDAO.getCheckService(vo);
	}

	@Override
	public List<BizVO> getBizList(BizVO vo)  throws Exception {
		List<BizVO> list = null;

		list = bizDAO.getBizList(vo);

		return list;
	}

	@Override
	public List<BizVO> getCsBizList(BizVO vo)  throws Exception {
		List<BizVO> list = null;

		list = bizDAO.getCsBizList(vo);

		return list;
	}

	@Override
	public int getBizListCount(BizVO vo)  throws Exception {

		return bizDAO.getBizListCount(vo);
	}

	@Override
	public int getCsBizListCount(BizVO vo)  throws Exception {

		return bizDAO.getCsBizListCount(vo);
	}

	@Override
	public String insBiz(BizVO vo) throws Exception {
		String result = "";
		bizDAO.insBiz(vo);
		result = vo.getBizId();
		return result;
	}

	@Override
	public int updBiz(BizVO vo) throws Exception {

		int result = bizDAO.updBiz(vo);

		//포인트 변경 시 들어오는 거는 제외
		if (vo.getLoginId() != null) {
			if (!vo.getLoginId().equals(vo.getChangeUserId())) {
				// 근로자 정보 변경
				EmpVO empVO = new EmpVO();
				empVO.setUserId(vo.getUserId());
				empVO.setEmpName(vo.getPersonUserName());
				empVO.setEMail(vo.getPersonEMail());
				empVO.setPhoneNum(vo.getPersonUserTelNum());
				empVO.setLoginId(vo.getChangeUserId());
				empVO.setBizId(vo.getBizId());
				empDAO.updEmp(empVO);


				// 로그인정보 변경
				UserVO userVO = new UserVO();
				userVO.setUserId(vo.getLoginId());
				userVO.setChangeUserId(vo.getChangeUserId());

				// 비밀번호 변경
				String userPwd = SecurityUtil.encrypt(vo.getUserPwd());

				UserVO resultUserVO = userDAO.loginProcess(userVO);

				// 로그인정보 존재시 처리
				if(resultUserVO!=null) {
					userDAO.updLoginId(userVO);

					//암호 변경시만 업데이트
					if (!vo.getUserPwd().isEmpty()) {
						UserVO userPwdVO = new UserVO();
						userPwdVO.setUserId(vo.getChangeUserId());
						userPwdVO.setUserPwd(userPwd);
						userDAO.updUserPwd(userPwdVO);
					}
				} else {
					userVO.setUserName(vo.getPersonUserName());
					userVO.setUserPwd(userPwd);
					userVO.setUserType("6");
					userVO.setAppType("N");
					userDAO.insUser(userVO);
				}

			}
		}
		return result;
	}

	@Override
	public int updBizTax(BizVO vo) throws Exception {
		int result = bizDAO.updBizTax(vo);
		return result;
	}

	@Override
	public int delBiz(BizVO vo) throws Exception {
		logger.info(":::: delBiz :::: ");
		int result = 0;
		List<ContentVO> delList = new ArrayList<ContentVO>();

		// 기업정보 자료조회
		BizVO bizVO = new BizVO();
		bizVO.setBizId(vo.getBizId());
		bizVO.setStartPage(0);
		bizVO.setEndPage(10);
		List<BizVO> bizList = bizDAO.getBizList(bizVO);

		if(bizList.size()==0) return result;

		bizVO = bizList.get(0);

		String delData = "";
		ContentVO contentVO = new ContentVO();
		// 계약서양식파일삭제
		ContractPersonNewVO contractNewVO = new ContractPersonNewVO();
		contractNewVO.setBizId(vo.getBizId());
		List<ContractPersonNewVO> contractNewList = contractDAO.getContractPersonNewList(contractNewVO);
		for(int i=0;i<contractNewList.size();i++) {
			ContractPersonNewVO contractNewDelVO = contractNewList.get(i);
			// 계약 원본파일(기업등록파일)
			contentVO.setFileId(contractNewDelVO.getFileId());
			delList.add(contentVO);

			// 계약 계약조건파일(xls)
			contentVO.setFileId(contractNewDelVO.getDataFileId());
			delList.add(contentVO);

			// 계약 전자문서파일(PDF)
			contentVO.setFileId(contractNewDelVO.getContractId());
			delList.add(contentVO);

			// 계약서양식파일 DB처리 조건
			delData += contractNewDelVO.getFileId() + "|";

		}

		//계약서 양식파일 삭제
		if(delList.size()>0) {

			logger.info(":::: delBiz ::::  계약서 양식파일 삭제");
			// 계약서양식파일 DB처리
			ContractPersonNewVO contractNewDelAllVO = new ContractPersonNewVO();
			contractNewDelAllVO.setBizId(vo.getBizId());
			contractNewDelAllVO.setFileId(delData);
			contractService.delContractPersonNew(contractNewDelAllVO);

			logger.info(":::: delBiz ::::  계약서 양식파일 DB삭제");
		}


		// 계약파일삭제
		ContractPersonVO contractPersonVO = new ContractPersonVO();
		contractPersonVO.setBizId(vo.getBizId());
		contractPersonVO.setStartPage(0);
		contractPersonVO.setEndPage(99999);
		List<ContractPersonVO> contractList = contractDAO.getContractPersonList(contractPersonVO);
		if(contractList.size()>0) {
			for(int i=0;i<contractList.size();i++) {
				if(StringUtil.isNotNull(contractList.get(i).getContractId())) {
					ContentVO fileVO = new ContentVO();
					fileVO.setFileId(contractList.get(i).getContractId());
					delList.add(fileVO);
				}
			}
		}

		// 급여파일삭제
		PaystubVO paystubVO = new PaystubVO();
		paystubVO.setBizId(vo.getBizId());
		paystubVO.setStartPage(0);
		paystubVO.setEndPage(99999);
		List<PaystubVO> paystubList = paystubDAO.getPaystubList(paystubVO);
		if(paystubList.size()>0) {
			for(int i=0;i<paystubList.size();i++) {
				PaystubVO delPaystubVO = paystubList.get(i);
				// 급여정보삭제
				paystubDAO.delPaystub(delPaystubVO);
				paystubDAO.delPaystubData(delPaystubVO);

				// 삭제할 파일목록
				ContentVO delPaystubFileVO = new ContentVO();
				delPaystubFileVO.setFileId(delPaystubVO.getFileId());
				delList.add(delPaystubFileVO);
			}
		}

		// 파일삭제
		contentService.deleteContent(delList);

		// 부서정보삭제
		DeptVO deptVO = new DeptVO();
		deptVO.setBizId(vo.getBizId());
		deptDAO.delDept(deptVO);

		// 사용자정보 삭제
		EmpVO empVO = new EmpVO();
		empVO.setBizId(vo.getBizId());
		empVO.setStartPage(0);
		empVO.setEndPage(99999);
		List<EmpVO> empList = empDAO.getEmpList(empVO);
		if(empList.size()>0) {
			for(int i=0;i<empList.size();i++) {
				EmpVO delEmpVO = empList.get(i);
				if(StringUtil.isNotNull(delEmpVO.getLoginId())) {
					UserVO userVO = new UserVO();
					userVO.setUserId(delEmpVO.getLoginId());
					userDAO.delUser(userVO);
				}
			}
		}
		// 인사정보 전체삭제
		empDAO.delEmpAll(empVO);

		// 서비스가입정보삭제
		bizDAO.delBizService(bizVO);

		PointVO pointVO = new PointVO();
		pointVO.setBizId(vo.getBizId());
		pointDAO.delPoint(pointVO);

		// 기업 이미지, 로그 삭제
		delBizLogo(vo);
		delBizStamp(vo);

		// 기업삭제
		bizDAO.delBiz(vo);
		return result;
	}

	@Override
	public void insBizService(BizVO vo) throws Exception {
		bizDAO.insBizService(vo);
		return;
	}

	@Override
	public int addBizGrp(BizGrpVO vo) throws Exception {
		int result = 0;

		// 등록된 기업 권한부여(별도 승인절차 없음)
		BizGrpVO bizGroupVO = new BizGrpVO();
		bizGroupVO.setBizId(vo.getBizId());
		bizGroupVO.setGrpType("B");
		bizGroupVO.setRefId(vo.getRefId());
		bizGroupVO.setStatusCode("RES");
		bizDAO.insBizGrp(bizGroupVO);
		result++;

		return result;
	}


	@Override
	public int addBizGrpList(List<BizGrpVO> list) throws Exception {
		int result = 0;

		for(BizGrpVO vo : list) {
			// 등록된 기업 권한부여(별도 승인절차 없음)
			BizGrpVO bizGroupVO = new BizGrpVO();
			bizGroupVO.setBizId(vo.getBizId());
			bizGroupVO.setGrpType("B");
			bizGroupVO.setRefId(vo.getRefId());
			bizGroupVO.setStatusCode("RES");


			List<BizGrpVO> bizlist = bizDAO.getBizGrpList(bizGroupVO);
			if(bizlist == null || bizlist.size() == 0 || !(vo.getRefId().equals(bizlist.get(0).getId()))) {
				bizDAO.insBizGrp(bizGroupVO);
				result++;
			}

		}

		return result;
	}

	@Override
	public int delBizGrpList(List<BizGrpVO> list) throws Exception {
		int result = 0;

		for(BizGrpVO vo : list) {
			// 등록된 기업 권한부여(별도 승인절차 없음)
			BizGrpVO bizGroupVO = new BizGrpVO();
			bizGroupVO.setBizId(vo.getBizId());
			bizGroupVO.setGrpType("B");
			bizGroupVO.setRefId(vo.getRefId());
			bizGroupVO.setStatusCode("RES");

			bizDAO.delBizGrp(bizGroupVO);
			result++;

		}

		return result;
	}

	@Override
	public String insAdminBiz(BizVO vo) throws Exception {
		String result = "";

		// 해당 기업 가입여부체크
		BizVO chkBizVO = new BizVO();
		chkBizVO.setBusinessNo(vo.getBusinessNo());
		chkBizVO.setStartPage(0);
		chkBizVO.setEndPage(10);
		List<BizVO> bizList = bizDAO.getBizList(chkBizVO);
		if(bizList.size()>0) {
			return "-1";
		}

		// 기업등록
		BizVO bizVO = new BizVO();
		bizVO.setBusinessNo(vo.getBusinessNo());
		bizVO.setBizName(vo.getBizName());
		bizVO.setOwnerName(vo.getOwnerName());
		bizVO.setAddr1(vo.getAddr1());
		bizVO.setCompanyTelNum(vo.getCompanyTelNum());
		bizVO.setFormPoint(vo.getFormPoint());
		bizVO.setScanPoint(vo.getScanPoint());
		bizVO.setSendPoint(vo.getSendPoint());
		bizVO.setResendPoint(vo.getResendPoint());

		//추가 항목 등록
		bizVO.setUseContract(vo.getUseContract());
		bizVO.setPersonUserName(vo.getPersonUserName());
		bizVO.setPersonUserTelNum(vo.getPersonUserTelNum());
		bizVO.setPersonEMail(vo.getPersonEMail());
		bizVO.setOpenVoucherType(vo.getOpenVoucherType());
		bizVO.setVoucherOrder(vo.getVoucherOrder());
		bizVO.setSalesChannel(vo.getSalesChannel());
		bizVO.setOtherGoodsPurchase(vo.getOtherGoodsPurchase());
		bizVO.setContractType(vo.getContractType());
		bizVO.setContractStartDate(vo.getContractStartDate());
		bizVO.setContractEndDate(vo.getContractEndDate());
		bizVO.setGeneralTransDate(vo.getGeneralTransDate());
		bizVO.setChannelId(vo.getChannelId());
		bizVO.setFunnel(vo.getFunnel());
		bizVO.setFunnelYear(vo.getFunnelYear());
		bizVO.setUseElecDoc(vo.getUseElecDoc());
		bizVO.setUsePayStub(vo.getUsePayStub());
		bizVO.setUseJumin(vo.getUseJumin());
		bizVO.setPaymentType(vo.getPaymentType());
		bizVO.setCurPoint(vo.getCurPoint());
		bizVO.setComment(vo.getComment());
		bizVO.setBizMngYn("N");
		bizVO.setMainBizYn("Y");
		bizVO.setMemo(vo.getMemo());

		bizDAO.insBiz(bizVO);

		result = bizVO.getBizId();

		// 기업서비스등록
		bizDAO.insBizService(bizVO);
		//result++;


		// 등록된 기업 권한부여(별도 승인절차 없음)
		/*
		BizGrpVO bizGroupVO = new BizGrpVO();
		bizGroupVO.setBizId(vo.getBizId());
		bizGroupVO.setGrpType("B");
		bizGroupVO.setRefId(bizVO.getBizId());
		bizGroupVO.setStatusCode("RES");
		bizDAO.insBizGrp(bizGroupVO);
		*/
		//result++;


		// 기업 기본제공 근로계약서 양식 및 서식등록
		contractService.setContractNewData(bizVO.getBizId());

		// 근로자 정보 변경
		EmpVO empVO = new EmpVO();
		empVO.setBizId(bizVO.getBizId());
		empVO.setEmpType("6");
		empVO.setEmpNo("admin");
		empVO.setEmpName(vo.getPersonUserName());
		empVO.setEMail(vo.getPersonEMail());
		empVO.setPhoneNum(vo.getPersonUserTelNum());
		empVO.setLoginId(vo.getLoginId());
		empVO.setUserDate("19990101");
		empVO.setJoinDate("19990101");
		empDAO.insEmp(empVO);

		// 로그인정보 변경
		String userPwd = SecurityUtil.encrypt(vo.getUserPwd());
		vo.setUserPwd(userPwd);
		UserVO userVO = new UserVO();
		userVO.setUserId(vo.getLoginId());
		userVO.setUserType("6");
		userVO.setUserName(vo.getPersonUserName());
		userVO.setUserPwd(userPwd);
		userVO.setAppType("N");
		userDAO.insUser(userVO);

		// 포인트 충전
		PointVO pointVO = new PointVO();
		pointVO.setBizId(bizVO.getBizId());
		pointVO.setUserId("");
		pointVO.setCurPoint(bizVO.getCurPoint());
		pointDAO.insPoint(pointVO);

		PointLogVO logVO = new PointLogVO();
		logVO.setBizId(bizVO.getBizId());
		logVO.setUserId(bizVO.getUserId());
		logVO.setPointType("0");
		logVO.setPointPrice(bizVO.getCurPoint());
		logVO.setPointResult(bizVO.getCurPoint());
		logVO.setPaymentAmt(0);
		logVO.setEtcDesc("시스템관리자가 포인트를 조정하였습니다.("+bizVO.getComment()+")");
		pointDAO.insPointLog(logVO);

		return result;
	}

	@Override
	public int insBizGrp(BizGrpVO vo) throws Exception {
		int result = 0;

		// 해당 기업 가입여부체크
		BizVO chkBizVO = new BizVO();
		chkBizVO.setBusinessNo(vo.getBusinessNo());
		chkBizVO.setStartPage(0);
		chkBizVO.setEndPage(10);
		List<BizVO> bizList = bizDAO.getBizList(chkBizVO);
		if(bizList.size()>0) {
			return -100;
		}

		// 기업등록
		BizVO bizVO = new BizVO();
		bizVO.setBusinessNo(vo.getBusinessNo());
		bizVO.setBizName(vo.getBizName());
		bizVO.setOwnerName(vo.getOwnerName());
		bizVO.setAddr1(vo.getAddr1());
		bizVO.setCompanyTelNum(vo.getCompanyTelNum());
		bizVO.setFormPoint(vo.getFormPoint());
		bizVO.setScanPoint(vo.getScanPoint());
		bizVO.setSendPoint(vo.getSendPoint());
		bizVO.setResendPoint(vo.getResendPoint());
		bizVO.setBizMngYn(vo.getBizMngYn());
		bizVO.setMainBizYn(vo.getMainBizYn());
		bizVO.setPersonUserName(vo.getEmpName());
		bizVO.setPersonEMail(vo.getEmail());
		bizVO.setPersonUserTelNum(vo.getPhoneNum());
		bizVO.setUseContract(vo.getUseContract());
		bizVO.setUseElecDoc(vo.getUseElecDoc());
		bizVO.setUsePayStub(vo.getUsePayStub());
		bizDAO.insBiz(bizVO);
		// 기업서비스등록
		bizDAO.insBizService(bizVO);
		result++;

		// 직인이미지 존재시 복사 및 DB처리
		if(StringUtil.isNotNull(vo.getCompanyImage())) {
			String szMonthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getTimeStamp(14);
			String imgSavePath = MultipartFileUtil.getSystemPath()+"images/sign"+szMonthPath+MultipartFileUtil.SEPERATOR+bizVO.getBizId()+".png";
			logger.info(vo.getCompanyImage());
			logger.info(imgSavePath);
			FileUtil.createDirectory(MultipartFileUtil.getSystemPath()+"images/sign"+szMonthPath);
			if(FileUtil.fileCopy(vo.getCompanyImage(), imgSavePath)) {
				bizVO.setCompanyImage(szMonthPath+MultipartFileUtil.SEPERATOR+bizVO.getBizId()+".png");
				bizDAO.updBiz(bizVO);
				result++;
			}
		}


		// 등록된 기업 권한부여(별도 승인절차 없음)
		BizGrpVO bizGroupVO = new BizGrpVO();
		bizGroupVO.setBizId(vo.getBizId());
		bizGroupVO.setGrpType("B");
		bizGroupVO.setRefId(bizVO.getBizId());
		bizGroupVO.setStatusCode("RES");
		bizDAO.insBizGrp(bizGroupVO);
		result++;

		// 기업 중간관리자 등록
		//List<UserGrpVO> userGrpList = vo.getUserGrpList();
		//for(int i=0;i<userGrpList.size();i++) {
		//UserGrpVO userGrpVO = new UserGrpVO();
		//userGrpVO.setUserId(userGrpList.get(i).getUserId());
		//userGrpVO.setGrpType("B");
		//userGrpVO.setRefId(vo.getBizId());
		//userGrpVO.setStatusCode("RES");
		//userDAO.insUserGrp(userGrpVO);
		//}


		// 기업 기본제공 근로계약서 양식 및 서식등록
		contractService.setContractNewData(bizVO.getBizId());

		// 근로자 정보 변경
		/*
		EmpVO empVO = new EmpVO();
		empVO.setBizId(bizVO.getBizId());
		empVO.setEmpType("6");
		empVO.setEmpNo("admin");
		empVO.setEmpName(vo.getEmpName());
		empVO.setEMail(vo.getEmail());
		empVO.setPhoneNum(vo.getPhoneNum());
		empVO.setLoginId(vo.getChangeUserId());
		empVO.setUserDate("19990101");
		empVO.setJoinDate("19990101");
		empDAO.insEmp(empVO);
		*/

		/*
		// 로그인정보 변경
		String userPwd = SecurityUtil.encrypt(vo.getUserPwd());
		vo.setUserPwd(userPwd);
		UserVO userVO = new UserVO();
		userVO.setUserId(vo.getChangeUserId());
		userVO.setUserType("6");
		userVO.setUserName(vo.getEmpName());
		userVO.setUserPwd(userPwd);
		userVO.setAppType("N");
		userDAO.insUser(userVO);
		*/

		// 포인트 충전
		PointVO pointVO = new PointVO();
		pointVO.setBizId(bizVO.getBizId());
		pointVO.setUserId("");
		pointVO.setCurPoint(Integer.parseInt(vo.getCurPoint()));
		pointDAO.insPoint(pointVO);

		PointLogVO logVO = new PointLogVO();
		logVO.setBizId(bizVO.getBizId());
		logVO.setUserId(bizVO.getUserId());
		logVO.setPointType("0");
		logVO.setPointPrice(0);
		logVO.setPointResult(0);
		logVO.setEtcDesc("종사업장 신규등록");
		logVO.setPaymentAmt(0);
		pointDAO.insPointLog(logVO);

		return result;
	}

	@Override
	public int updBizGrp(BizGrpVO vo) throws Exception {
		// 기업변경
		BizVO bizVO = new BizVO();
		bizVO.setBizId(vo.getBizId());
		bizVO.setBusinessNo(vo.getBusinessNo());
		bizVO.setBizName(vo.getBizName());
		bizVO.setOwnerName(vo.getOwnerName());
		bizVO.setAddr1(vo.getAddr1());
		bizVO.setCompanyTelNum(vo.getCompanyTelNum());
		bizVO.setCompanyFaxNum(vo.getCompanyFaxNum());
		bizVO.setFormPoint(vo.getFormPoint());
		bizVO.setScanPoint(vo.getScanPoint());
		bizVO.setSendPoint(vo.getSendPoint());
		bizVO.setResendPoint(vo.getResendPoint());
		bizVO.setBizMngYn(vo.getBizMngYn());
		bizVO.setMainBizYn(vo.getMainBizYn());
		bizVO.setPersonUserName(vo.getEmpName());
		bizVO.setPersonEMail(vo.getEmail());
		bizVO.setPersonUserTelNum(vo.getPhoneNum());
		bizVO.setUseContract(vo.getUseContract());
		bizVO.setUseElecDoc(vo.getUseElecDoc());
		bizVO.setUsePayStub(vo.getUsePayStub());
		bizDAO.updBiz(bizVO);

		// 근로자 정보 변경
		//EmpVO empVO = new EmpVO();
		//empVO.setBizId(vo.getBizId());
		//empVO.setUserId(vo.getUserId());
		//empVO.setEmpName(vo.getEmpName());
		//empVO.setEMail(vo.getEmail());
		//empVO.setPhoneNum(vo.getPhoneNum());
		//empVO.setLoginId(vo.getChangeUserId());
		//empDAO.updEmp(empVO);


		// 로그인정보 변경
		//UserVO userVO = new UserVO();
		//userVO.setUserId(vo.getLoginId());
		//userVO.setChangeUserId(vo.getChangeUserId());

		// 비밀번호 변경
		//String userPwd = SecurityUtil.encrypt(vo.getUserPwd());

		//UserVO resultUserVO = userDAO.loginProcess(userVO);

		// 로그인정보 존재시 처리
		//if(resultUserVO!=null) {
		//userDAO.updLoginId(userVO);

		//UserVO userPwdVO = new UserVO();
		//userPwdVO.setUserId(vo.getChangeUserId());
		//userPwdVO.setUserPwd(userPwd);
		//userDAO.updUserPwd(userPwdVO);
		//} else {
		//userVO.setUserName(vo.getEmpName());
		//userVO.setUserPwd(userPwd);
		//userVO.setUserType("6");
		//userVO.setAppType("N");
		//userDAO.insUser(userVO);
		//}


		// 기업 중간관리자 등록
		//UserGrpVO delUserGrpVO = new UserGrpVO();
		//delUserGrpVO.setGrpType("B");
		//delUserGrpVO.setRefId(vo.getBizId());
		//userDAO.delUserGrp(delUserGrpVO);

		//List<UserGrpVO> userGrpList = vo.getUserGrpList();
		//for(int i=0;i<userGrpList.size();i++) {
		//UserGrpVO userGrpVO = new UserGrpVO();
		//userGrpVO.setUserId(userGrpList.get(i).getUserId());
		//userGrpVO.setGrpType("B");
		//userGrpVO.setRefId(vo.getBizId());
		//userGrpVO.setStatusCode("RES");
		//userDAO.insUserGrp(userGrpVO);
		//}

		return 1;
	}

	@Override
	public int updBizGrpSubAdmin(BizGrpVO vo) throws Exception {
		// 기업변경
		BizVO bizVO = new BizVO();
		bizVO.setBizId(vo.getBizId());
		bizVO.setBusinessNo(vo.getBusinessNo());
		bizVO.setBizName(vo.getBizName());
		bizVO.setOwnerName(vo.getOwnerName());
		bizVO.setAddr1(vo.getAddr1());
		bizVO.setCompanyTelNum(vo.getCompanyTelNum());
		bizVO.setCompanyFaxNum(vo.getCompanyFaxNum());
		bizDAO.updBiz(bizVO);

		// 근로자 정보 변경
		EmpVO empVO = new EmpVO();
		empVO.setUserId(vo.getUserId());
		empVO.setEmpName(vo.getEmpName());
		empVO.setEMail(vo.getEmail());
		empVO.setPhoneNum(vo.getPhoneNum());
		empVO.setLoginId(vo.getChangeUserId());
		empDAO.updEmp(empVO);

		// 로그인정보 변경
		UserVO userVO = new UserVO();
		userVO.setUserId(vo.getLoginId());
		userVO.setChangeUserId(vo.getChangeUserId());
		userDAO.updLoginId(userVO);

		return 1;
	}

	@Override
	public int insBizGrpBiz(BizVO vo) throws Exception {
		int result = 0;

		// 해당 기업 가입여부체크
		BizVO chkBizVO = new BizVO();
		chkBizVO.setBusinessNo(vo.getBusinessNo());
		chkBizVO.setStartPage(0);
		chkBizVO.setEndPage(10);
		List<BizVO> bizList = bizDAO.getBizList(chkBizVO);
		if(bizList.size()>0) {
			return -100;
		}

		String bizId = vo.getBizId();
		// 기업등록
		bizDAO.insBiz(vo);
		// 기업서비스등록
		bizDAO.insBizService(vo);
		result++;

		// 직인이미지 존재시 복사 및 DB처리
		if(StringUtil.isNotNull(vo.getCompanyImage())) {
			String szMonthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getTimeStamp(14);
			String imgSavePath = MultipartFileUtil.getSystemPath()+"images/sign"+szMonthPath+MultipartFileUtil.SEPERATOR+vo.getBizId()+".png";
			logger.info(vo.getCompanyImage());
			logger.info(imgSavePath);
			FileUtil.createDirectory(MultipartFileUtil.getSystemPath()+"images/sign"+szMonthPath);
			if(FileUtil.fileCopy(vo.getCompanyImage(), imgSavePath)) {
				vo.setCompanyImage(szMonthPath+MultipartFileUtil.SEPERATOR+vo.getBizId()+".png");
				bizDAO.updBiz(vo);
				result++;
			}
		}

		// 등록된 기업 권한부여(별도 승인절차 없음)
		BizGrpVO bizGroupVO = new BizGrpVO();
		bizGroupVO.setBizId(bizId);
		bizGroupVO.setGrpType("B");
		bizGroupVO.setRefId(vo.getBizId());
		bizGroupVO.setStatusCode("RES");
		bizDAO.insBizGrp(bizGroupVO);
		result++;

		// 기업 기본제공 근로계약서 양식 및 서식등록
		contractService.setContractNewData(vo.getBizId());

		return result;
	}


	@Override
	public void delBizGrp(BizGrpVO vo) throws Exception {
		// 기업관리 삭제
		vo.setGrpType("B");
		vo.setRefId(vo.getRefId());
		bizDAO.delBizGrp(vo);

		// 기업삭제
		BizVO bizVO = new BizVO();
		bizVO.setBizId(vo.getRefId());
		bizDAO.delBiz(bizVO);

		// 기업서비스 삭제
		bizVO.setBusinessNo(vo.getBusinessNo());
		bizDAO.delBizService(bizVO);

		// 사용자그룹 삭제
		UserGrpVO userGrpVO = new UserGrpVO();
		userGrpVO.setGrpType("B");
		userGrpVO.setRefId(vo.getBizId());
		userDAO.delUserGrp(userGrpVO);
	}

	@Override
	public List<BizGrpVO> getBizGrp(BizGrpVO vo) throws Exception {
		List<BizGrpVO> result = new ArrayList<BizGrpVO>();
		result = bizDAO.getBizGrp(vo);

		for(int i=0;i<result.size();i++) {
			BizGrpVO bizGrpVO = result.get(i);

			// 기업별 관리자
			UserGrpVO userGrpVO = new UserGrpVO();
			userGrpVO.setGrpType("B");
			userGrpVO.setRefId(bizGrpVO.getId());
			List<UserGrpVO> userList = userDAO.getUserGrpList(userGrpVO);

			bizGrpVO.setUserGrpList(userList);

		}


		return result;
	}

	@Override
	public List<BizGrpVO> getBizGrpList(BizGrpVO vo) throws Exception {
		List<BizGrpVO> result = new ArrayList<BizGrpVO>();
		result = bizDAO.getBizGrpList(vo);

		for(int i=0;i<result.size();i++) {
			BizGrpVO bizGrpVO = result.get(i);

			// 기업별 관리자
			UserGrpVO userGrpVO = new UserGrpVO();
			userGrpVO.setGrpType("B");
			userGrpVO.setRefId(bizGrpVO.getId());
			List<UserGrpVO> userList = userDAO.getUserGrpList(userGrpVO);

			bizGrpVO.setUserGrpList(userList);

		}

		return result;
	}

	@Override
	public List<BizGrpVO> getBizGrpCombo(BizGrpVO vo) throws Exception {
		List<BizGrpVO> list = null;

		list = bizDAO.getBizGrpCombo(vo);

		return list;
	}

	@Override
	public int updBizLogo(BizVO vo, FileVO fileVO) throws Exception {
		BizVO bizVO = new BizVO();
		int result = 0;

		String imgPath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
		// 직인이미지 존재시 복사 및 DB처리
		if(StringUtil.isNotNull(imgPath)) {
			String szMonthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getTimeStamp(14);
			String imgSavePath = MultipartFileUtil.getSystemPath()+"images/sign"+szMonthPath+MultipartFileUtil.SEPERATOR+vo.getBizId()+"_logo."+fileVO.getFileExt();
			logger.info(imgPath);
			logger.info(imgSavePath);

			File imgFile = new File(imgPath);
			File imgSaveFile = new File(imgSavePath);

			FileUtil.createDirectory(MultipartFileUtil.getSystemPath()+"images/sign"+szMonthPath);
			//if(FileUtil.fileCopy(imgPath, imgSavePath)) {
			if(Files.copy(imgFile.toPath(), imgSaveFile.toPath(), StandardCopyOption.REPLACE_EXISTING) != null){
				// 회사로그 파일위치 업데이트
				bizVO.setBizId(vo.getBizId());
				bizVO.setCompanyLogo(szMonthPath+MultipartFileUtil.SEPERATOR+vo.getBizId()+"_logo."+fileVO.getFileExt());
				result = bizDAO.updBiz(bizVO);
			}
		}

		return result;
	}

	@Override
	public int delBizLogo(BizVO vo) throws Exception {
		BizVO bizVO = new BizVO();
		int result = 0;
		vo.setStartPage(0);
		vo.setEndPage(10);
		List<BizVO> bizList = bizDAO.getBizList(vo);
		if(bizList.size()>0) {
			bizVO = bizList.get(0);
			// 기존 기업 로그 이미지파일 삭제
			String imgPath = MultipartFileUtil.getSystemPath()+"images/sign"+bizVO.getCompanyLogo();
			FileUtil.deleteFile(imgPath);

			// 이미지파일 위치 초기화
			vo.setCompanyLogo("-");
			bizDAO.updBiz(vo);
		}

		return result;
	}



	@Override
	public int updBizStamp(BizVO vo, FileVO fileVO) throws Exception {
		BizVO bizVO = new BizVO();
		int result = 0;

		String imgPath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
		// 직인이미지 존재시 복사 및 DB처리
		if(StringUtil.isNotNull(imgPath)) {
			String szMonthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getTimeStamp(14);
			String imgSavePath = MultipartFileUtil.getSystemPath()+"images/sign"+szMonthPath+MultipartFileUtil.SEPERATOR+vo.getBizId()+"_stamp."+fileVO.getFileExt();
			logger.info(imgPath);
			logger.info(imgSavePath);
			FileUtil.createDirectory(MultipartFileUtil.getSystemPath()+"images/sign"+szMonthPath);
			if(FileUtil.fileCopy(imgPath, imgSavePath)) {
				// 회사로그 파일위치 업데이트
				bizVO.setBizId(vo.getBizId());
				bizVO.setCompanyImage(szMonthPath+MultipartFileUtil.SEPERATOR+vo.getBizId()+"_stamp."+fileVO.getFileExt());
				result = bizDAO.updBiz(bizVO);
			}
		}

		return result;
	}

	@Override
	public int delBizStamp(BizVO vo) throws Exception {
		BizVO bizVO = new BizVO();
		int result = 0;

		vo.setStartPage(0);
		vo.setEndPage(10);
		List<BizVO> bizList = bizDAO.getBizList(vo);
		if(bizList.size()>0) {
			bizVO = bizList.get(0);
			// 기존 기업 로그 이미지파일 삭제
			String imgPath = MultipartFileUtil.getSystemPath()+"images/sign"+bizVO.getCompanyImage();
			FileUtil.deleteFile(imgPath);

			// 이미지파일 위치 초기화
			vo.setCompanyImage("-");
			bizDAO.updBiz(vo);
		}

		return result;
	}

	@Override
	public List<BizGrpVO> getBizGrpSubAdminList(BizGrpVO vo) throws Exception {
		List<BizGrpVO> result = new ArrayList<BizGrpVO>();
		result = bizDAO.getBizGrpSubAdminList(vo);

		return result;
	}

	// 서비스정보 조회
	@Override
	public List<BizVO> getServiceList(BizVO vo) throws Exception {
		List<BizVO> result = new ArrayList<BizVO>();
		result = bizDAO.getServiceList(vo);

		return result;
	}

	// 기업 서비스 추가
	@Override
	public int insBizServiceAdd(BizVO vo) throws Exception {

		BizVO bizVO = new BizVO();
		int result = 0;
		vo.setStartPage(0);
		vo.setEndPage(1);
		List<BizVO> bizList = bizDAO.getBizList(vo);

		if(bizList!=null&&bizList.size()>0) {
			BizVO resultBizVO = bizList.get(0);
			vo.setBizId(resultBizVO.getBizId());
			int total = bizDAO.getCheckService(vo);
			// 서비스 미가입시만 확인
			if (total == 0) {
				bizVO.setBusinessNo(vo.getBusinessNo());
				bizVO.setServiceType(vo.getServiceType());
				bizDAO.insBizServiceAdd(bizVO);
				result++;
			}
		}

		return result;
	}

	@Override
	public List<BizGrpVO> getBizGrpNameList(BizGrpVO vo) throws Exception {
		List<BizGrpVO> result = new ArrayList<BizGrpVO>();
		result = bizDAO.getBizGrpNameList(vo);

		return result;
	}

	@Override
	public void updateOpenVoucherContractEnd() throws Exception {
		bizDAO.updateOpenVoucherContractEnd();
	}

	@Override
	public void updateOpenVoucherPoint(BizVO vo) throws Exception {

		//로그 먼저 남기고 포인트 0 처리
		PointLogVO logVO = new PointLogVO();
		logVO.setBizId(vo.getBizId());
		logVO.setUserId("SYSTEM");
		logVO.setPointType("3");
		logVO.setPaymentAmt(0);
		logVO.setEtcDesc("비대면바우처 계약종료로 인한 포인트 소멸");
		bizDAO.updatePointLogContractEnd(logVO);

		bizDAO.updatePointContractEnd(vo);
	}

	// 서비스정보 조회
	@Override
	public List<BizVO> getupdateOpenVoucher(BizVO vo) throws Exception {
		List<BizVO> result = new ArrayList<BizVO>();
		result = bizDAO.getupdateOpenVoucher(vo);

		return result;
	}

	// 서비스정보 조회
	@Override
	public List<BizVO> getContractEndList(BizVO vo) throws Exception {
		List<BizVO> result = new ArrayList<BizVO>();
		result = bizDAO.getContractEndList(vo);

		return result;
	}

	// 서비스정보 조회
	@Override
	public List<BizVO> getContractD30List(BizVO vo) throws Exception {
		List<BizVO> result = new ArrayList<BizVO>();
		result = bizDAO.getContractD30List(vo);

		return result;
	}

	// 서비스정보 조회
	@Override
	public List<BizVO> getDownloadExpireList(BizVO vo) throws Exception {
		List<BizVO> result = new ArrayList<BizVO>();
		result = bizDAO.getDownloadExpireList(vo);

		return result;
	}

	@Override
	public List<CandycashBizVO> getCandyCashBizList(CandycashBizVO vo) throws Exception {
		List<CandycashBizVO> result = new ArrayList<CandycashBizVO>();
		result = bizDAO.getCandyCashBizList(vo);

		return result;
	}

	@Override
	public void updCandyCashBizInfo(CandycashBizVO vo) throws Exception {
		bizDAO.updCandyCashBizInfo(vo);
	}

	@Override
	public List<BizVO> getBizEmail(BizVO vo)  throws Exception {

		List<BizVO> list = null;
		list = bizDAO.getBizEmail(vo);
		return list;
	}
}
