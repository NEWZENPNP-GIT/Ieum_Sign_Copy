package com.ezsign.annual.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import jxl.Sheet;
import jxl.Workbook;

import com.ezsign.annual.dao.AnnualVacationDAO;
import com.ezsign.annual.service.AnnualVacationService;
import com.ezsign.annual.vo.AnnualConfigVO;
import com.ezsign.annual.vo.AnnualTypeVO;
import com.ezsign.annual.vo.AnnualVacationLogVO;
import com.ezsign.annual.vo.AnnualVacationStatVO;
import com.ezsign.annual.vo.AnnualVacationVO;
import com.ezsign.annual.vo.VacationRequestVO;
import com.ezsign.approval.dao.ApprovalDAO;
import com.ezsign.approval.vo.ApprovalVO;
import com.ezsign.code.dao.CodeDAO;
import com.ezsign.content.service.ContentService;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.ExcelUtil;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.paystub.service.PaystubService;
import com.ezsign.paystub.vo.PaystubDataVO;
import com.ezsign.paystub.vo.PaystubVO;
import com.ezsign.user.dao.UserDAO;

@Service("annualVacationService")
public class AnnualVacationServiceImpl extends AbstractServiceImpl implements AnnualVacationService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name="codeDAO")
	private CodeDAO codeDAO;
	
	@Resource(name="annualVacationDAO")
	private AnnualVacationDAO annualVacationDAO;

	@Resource(name="approvalDAO")
	private ApprovalDAO approvalDAO;
	
	@Resource(name="empDAO")
	private EmpDAO empDAO;

	@Resource(name="userDAO")
	private UserDAO userDAO;

	@Override
	public AnnualVacationVO getAnnualVacation(VacationRequestVO vo) throws Exception {
		AnnualVacationVO result = new AnnualVacationVO();
		// 연차관리
		AnnualVacationVO avVO = new AnnualVacationVO();
		avVO.setBizId(vo.getBizId());
		avVO.setUserId(vo.getUserId());
		System.out.println("[getAnnualVacation]  사용자ID -> "+avVO.getUserId());
		
		result = annualVacationDAO.getAnnualVacation(avVO);
		
		
		return result;
	}
	
	@Override
	public List<AnnualVacationVO> getAnnualVacationList(AnnualVacationVO vo)  throws Exception {
		List<AnnualVacationVO> list = null;
		
		list = annualVacationDAO.getAnnualVacationList(vo);
		
		return list;
	}

	@Override
	public int getAnnualVacationListCount(AnnualVacationVO vo)  throws Exception {
		
		return annualVacationDAO.getAnnualVacationListCount(vo);
	}
	

	@Override
	public int insAnnualVacation(AnnualVacationVO vo)  throws Exception {
		int result = 0;
		EmpVO empVO = new EmpVO();
		empVO.setBizId(vo.getBizId());
		empVO.setStartPage(0);
		empVO.setEndPage(10000);
		List<EmpVO> empList = empDAO.getEmpList(empVO);
		
		for(int i=0;i<empList.size();i++) {
			EmpVO dataVO = empList.get(i);
			
			if(!dataVO.getEmpType().equals("8")&&!dataVO.getEmpType().equals("9")) {
				AnnualVacationVO avVO = new AnnualVacationVO();
				avVO.setBizId(dataVO.getBizId());
				avVO.setUserId(dataVO.getUserId());
				avVO.setAnnualYear(vo.getAnnualYear());
				AnnualVacationVO resultAnnualVO = annualVacationDAO.getAnnualVacation(avVO);
				
				AnnualConfigVO annualConfigVO = new AnnualConfigVO();
				annualConfigVO.setBizId(dataVO.getBizId());
				AnnualConfigVO resultAnnualConfigVO = annualVacationDAO.getAnnualConfig(annualConfigVO);
				
				float totalDay = 0;
				String annualCreateCode = "1";
				// 연차일수 계산(입사일 기준)
				totalDay = Float.parseFloat(String.valueOf(annualDay(dataVO.getJoinDate())));
				if(resultAnnualConfigVO!=null) {
					annualCreateCode = resultAnnualConfigVO.getAnnualCreateCode();
					// 연차일수 계산(회계년도 기준)
					if(annualCreateCode.equals("2")) {
						totalDay = annualAccountDay(dataVO.getJoinDate());
					}
				}
				
				// 신규 생성
				AnnualVacationVO insAnnualVacationVO = new AnnualVacationVO();
				insAnnualVacationVO.setServiceId(SERVICE_ID);
				insAnnualVacationVO.setBizId(dataVO.getBizId());
				insAnnualVacationVO.setUserId(dataVO.getUserId());
				insAnnualVacationVO.setAnnualYear(vo.getAnnualYear());
				insAnnualVacationVO.setTotalDay(totalDay);
				insAnnualVacationVO.setAddDay(0);
				insAnnualVacationVO.setDelDay(0);
				insAnnualVacationVO.setUseDay(0);
				
				if(resultAnnualVO==null) {
					annualVacationDAO.insAnnualVacation(insAnnualVacationVO);
					result++;
				}	
			}
		}
		
		return result;
	}


	@Override
	public int updAnnualVacation(AnnualVacationVO vo)  throws Exception {
		int result = 0;
		
		String updData = vo.getAnnualYear();
		String[] updList = StringUtil.split(updData, "|");
		for(int i=0;i<updList.length;i++) {
			String[] updAnnualVacation = StringUtil.split(updList[i], "-");
			if(updAnnualVacation.length>4) {
				AnnualVacationVO updAnnualVacationVO = new AnnualVacationVO();
				updAnnualVacationVO.setServiceId(SERVICE_ID);
				updAnnualVacationVO.setBizId(vo.getBizId());
				updAnnualVacationVO.setUserId(updAnnualVacation[1]);
				updAnnualVacationVO.setAnnualYear(updAnnualVacation[0]);
				updAnnualVacationVO.setTotalDay(Float.parseFloat(updAnnualVacation[2]));
				updAnnualVacationVO.setAddDay(Float.parseFloat(updAnnualVacation[3]));
				updAnnualVacationVO.setDelDay(Float.parseFloat(updAnnualVacation[4]));
				
				annualVacationDAO.updAnnualVacation(updAnnualVacationVO);
				
				AnnualVacationLogVO insAnnualVacationLogVO = new AnnualVacationLogVO();
				insAnnualVacationLogVO.setServiceId(SERVICE_ID);
				insAnnualVacationLogVO.setBizId(vo.getBizId());
				insAnnualVacationLogVO.setUserId(updAnnualVacation[1]);
				insAnnualVacationLogVO.setTotalDay(Float.parseFloat(updAnnualVacation[2]));
				insAnnualVacationLogVO.setAddDay(Float.parseFloat(updAnnualVacation[3]));
				insAnnualVacationLogVO.setDelDay(Float.parseFloat(updAnnualVacation[4]));
				insAnnualVacationLogVO.setComments(updAnnualVacation[0]+"년도의 연차일수가 변경되었습니다.");
				
				annualVacationDAO.insAnnualVacationLog(insAnnualVacationLogVO);
				
				result++;
			}
		}
		
		return result;
	}
	
	// 연차일수 계산 (입사일자 기준)
    public static int annualDay(String joinDate) {
    	int day = 0;
    	try {
    		long betweenDay = DateUtil.diffOfDate(joinDate)+1;
    		double betweenYear = Double.parseDouble(Long.toString(betweenDay))/365;
    		
    		if(betweenYear<1) {
    			day = Math.round(betweenDay/30);
    		} else {
    			double date = Math.floor(Math.round(betweenYear / 2));
    			day = 15+Integer.parseInt(String.valueOf(Math.round(date)));
    		}
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	return day;
    }

	// 연차일수 계산 (회계년도 기준)
    public static float annualAccountDay(String joinDate) {
    	
    	String currentYear = DateUtil.getYear();
    	float day = 0;
    	try {
    		long betweenDay = DateUtil.diffOfDate(currentYear+"0101", joinDate)+1;
    		double betweenYear = Double.parseDouble(Long.toString(betweenDay))/365;

    		String joinYear = StringUtil.substring(joinDate, 0, 4);

    		System.out.println("nowDate.getYear()=>"+currentYear+"0101");
    		System.out.println("joinDate=>"+joinDate);
    		System.out.println("betweenYear=>"+betweenYear);
    		System.out.println("joinYear=>"+joinYear);

    		if(joinYear.equals(Integer.toString(Integer.parseInt(currentYear)-1))) {
        		// 이전해년도
        		long betweenDayBefore = DateUtil.diffOfDate(currentYear+"0101", joinDate);
    			int dayBefore = Math.round(betweenDayBefore/30);
    			day = 15*dayBefore/12;

        		System.out.println("1betweenDayBefore=>"+betweenDayBefore);
        		System.out.println("1dayBefore=>"+dayBefore);
        		System.out.println("1day=>"+day);
        		
    		} else if(joinYear.equals(currentYear)) {
        		// 당해년도
        		betweenDay = DateUtil.diffOfDate(joinDate)+1;
    			day = Math.round(betweenDay/30);
        		System.out.println("2betweenDay=>"+betweenDay);
        		System.out.println("2day=>"+day);
    		} else {
    			double date = Math.floor(Math.round(betweenYear / 2));
    			day = 15+Integer.parseInt(String.valueOf(Math.round(date)));
        		System.out.println("3date=>"+date);
        		System.out.println("3day=>"+day);
    		}
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	return day;
    }
    
    

	@Override
	public VacationRequestVO getVacationRequest(VacationRequestVO vo) throws Exception {
		VacationRequestVO result = new VacationRequestVO();
		// 연차관리
		
		result = annualVacationDAO.getVacationRequest(vo);
		
		
		return result;
	}
	

	@Override
	public List<VacationRequestVO> getVacationRequestList(VacationRequestVO vo) throws Exception {
		List<VacationRequestVO> result = new ArrayList();
		
		System.out.println("[getVacationRequestList]  사용자ID -> "+vo.getUserId());
		
		// 연차결재
		result = annualVacationDAO.getVacationRequestList(vo);
		
		return result;
	}


	@Override
	public List<VacationRequestVO> getVacationRequestCompleteList(VacationRequestVO vo) throws Exception {
		List<VacationRequestVO> result = new ArrayList();
		
		System.out.println("[getVacationRequestCompleteList]  사용자ID -> "+vo.getUserId());
		
		// 연차결재
		result = annualVacationDAO.getVacationRequestCompleteList(vo);
		
		return result;
	}

	@Override
	public List<VacationRequestVO> getVacationTypeSumList(VacationRequestVO vo)  throws Exception {
		List<VacationRequestVO> result = new ArrayList();
		
		result = annualVacationDAO.getVacationTypeSumList(vo);
		
		return result;
	}
	
	@Override
	public boolean requestAnnualVacation(VacationRequestVO vo) throws Exception {
		boolean result = false;
		
		System.out.println("[requestAnnualVacation]  사용자ID -> "+vo.getUserId());
		
		// 결재등록
		String approvalId = codeDAO.getTableKey("TBL_APPROVAL");
				
		for(int i=0;i<vo.getApprovalList().size();i++) {
			ApprovalVO approvalVO = vo.getApprovalList().get(i);

			ApprovalVO aVO = new ApprovalVO();
			aVO.setApprovalId(approvalId);
			aVO.setApprovalType(approvalVO.getApprovalType());
			aVO.setSignSeq(approvalVO.getSignSeq());
			aVO.setBizId(vo.getBizId());
			aVO.setUserId(approvalVO.getUserId());
			// 1차결재가 결재대기상태로 변경
			if(i==0) aVO.setSignType(1);
			else aVO.setSignType(0);
			aVO.setSignComments("");
			
			approvalDAO.insApproval(aVO);
		}
		
		// 사용일수 증가
		AnnualVacationVO avVO = new AnnualVacationVO();
		avVO.setServiceId(AnnualVacationService.SERVICE_ID);
		avVO.setBizId(vo.getBizId());
		avVO.setUserId(vo.getUserId());
		avVO.setUseDay(vo.getVacationDay());
		int updAnnualVacation = annualVacationDAO.updAnnualVacationUseDay(avVO);
		
		// 연차요청
		vo.setApprovalId(approvalId);
		annualVacationDAO.insVacationRequest(vo);
		
		if(updAnnualVacation>0) result = true;
		
		return result;
	}
	
	@Override
	public boolean acceptAnnualVacation(VacationRequestVO vo) throws Exception {
		boolean result = false;
		
		System.out.println("[acceptAnnualVacation]  사용자ID -> "+vo.getUserId());
		
		VacationRequestVO reqVO = annualVacationDAO.getVacationRequest(vo);
		
		int statusCode = 0;
		if(StringUtil.isNotNull(reqVO.getStatusCode())) {
			statusCode = Integer.parseInt(reqVO.getStatusCode());
		}
		if(statusCode>1) return false;
		
		// 결재자정보 조회
		EmpVO empVO = new EmpVO();
		empVO.setBizId(vo.getBizId());
		empVO.setUserId(vo.getUserId());
		empVO.setStartPage(0);
		empVO.setEndPage(99999);
		List<EmpVO> empList = empDAO.getEmpList(empVO);
		
		if(empList.size()>0) {
			EmpVO userVO = empList.get(0);

			// 결재선 리스트 조회
			ApprovalVO approvalVO = new ApprovalVO();
			approvalVO.setApprovalId(reqVO.getApprovalId());
			List<ApprovalVO> approvalList = approvalDAO.getApprovalList(approvalVO);

			System.out.println("acceptAnnualVacation =1=");
			
			int curSignSeq = 0;
			int nextSignSeq = 0;
			String nextUserId = "";
			for(int i=0;i<approvalList.size();i++) {
				ApprovalVO signVO = approvalList.get(i);
				if(signVO.getUserId().equals(userVO.getUserId())) {
					curSignSeq = signVO.getSignSeq();
					// 결재대기 상태가 아닌경우
					if(signVO.getSignType()!=1) return false;
				}
				if(nextSignSeq==0 && curSignSeq>0 && signVO.getSignSeq()==(curSignSeq+1)) {
					nextSignSeq=signVO.getSignSeq();
					nextUserId=signVO.getUserId();
				}
			}
			
			System.out.println("acceptAnnualVacation =2=");

			// 결재정보 상태변경
			approvalVO.setBizId(vo.getBizId());
			approvalVO.setUserId(userVO.getUserId());
			approvalVO.setSignType(3);
			approvalVO.setSignComments(vo.getComments());
			int updApproval = approvalDAO.updApproval(approvalVO);

			System.out.println("acceptAnnualVacation =3=");
			
			int updVacationRequest = 0;
			// 다음 결재자정보 상태변경
			if(nextSignSeq>0) {
				approvalVO.setBizId(vo.getBizId());
				approvalVO.setUserId(nextUserId);
				approvalVO.setSignType(1);
				approvalVO.setSignComments("");
				int updNextApproval = approvalDAO.updApproval(approvalVO);

				System.out.println("acceptAnnualVacation =4=");
				
				// 휴가신청정보 결재상태 변경 
				// 결재중처리
				VacationRequestVO vrVO = new VacationRequestVO();
				vrVO.setRequestId(vo.getRequestId());
				vrVO.setStatusCode("1");
				updVacationRequest = annualVacationDAO.updVacationRequest(vrVO);	

				System.out.println("acceptAnnualVacation =5=");
				
			} else {
				// 최종결재이므로 최종처리
				
				// 휴가신청정보 결재상태 변경
				// 최종결재인경우 완료처리
				VacationRequestVO vrVO = new VacationRequestVO();
				vrVO.setRequestId(vo.getRequestId());
				vrVO.setStatusCode("9");
				updVacationRequest = annualVacationDAO.updVacationRequest(vrVO);
				
				System.out.println("acceptAnnualVacation =6=");
				
			}
			
			//System.out.println("curSignSeq=>"+curSignSeq);			
			//System.out.println("nextSignSeq=>"+nextSignSeq);		
			//System.out.println("nextUserId=>"+nextUserId);

			System.out.println("acceptAnnualVacation =7=");
			
			if(updVacationRequest>0&&updApproval>0) result = true;
			
		}
		
		return result;
	}

	@Override
	public boolean acceptAllAnnualVacation(List<VacationRequestVO> list) throws Exception {
		boolean result = false;
		
		for(int idx=0;idx<list.size();idx++) {
			VacationRequestVO vo = list.get(idx);
			
			System.out.println("[acceptAnnualVacation]  사용자ID -> "+vo.getUserId());
	
			VacationRequestVO reqVO = annualVacationDAO.getVacationRequest(vo);
	
			int statusCode = 0;
			if(StringUtil.isNotNull(reqVO.getStatusCode())) {
				statusCode = Integer.parseInt(reqVO.getStatusCode());
			}
			if(statusCode>1) return false;
			
			// 결재자정보 조회
			EmpVO empVO = new EmpVO();
			empVO.setBizId(vo.getBizId());
			empVO.setUserId(vo.getUserId());
			empVO.setStartPage(0);
			empVO.setEndPage(99999);
			List<EmpVO> empList = empDAO.getEmpList(empVO);
			
			if(empList.size()>0) {
				EmpVO userVO = empList.get(0);
	
				// 결재선 리스트 조회
				ApprovalVO approvalVO = new ApprovalVO();
				approvalVO.setApprovalId(reqVO.getApprovalId());
				List<ApprovalVO> approvalList = approvalDAO.getApprovalList(approvalVO);
				
				int curSignSeq = 0;
				int nextSignSeq = 0;
				String nextUserId = "";
				for(int i=0;i<approvalList.size();i++) {
					ApprovalVO signVO = approvalList.get(i);
					if(signVO.getUserId().equals(userVO.getUserId())) {
						curSignSeq = signVO.getSignSeq();
						// 결재대기 상태가 아닌경우
						if(signVO.getSignType()!=1) return false;
					}
					if(nextSignSeq==0 && curSignSeq>0 && signVO.getSignSeq()==(curSignSeq+1)) {
						nextSignSeq=signVO.getSignSeq();
						nextUserId=signVO.getUserId();
					}
				}
	
				// 결재정보 상태변경
				approvalVO.setBizId(vo.getBizId());
				approvalVO.setUserId(userVO.getUserId());
				approvalVO.setSignType(3);
				approvalVO.setSignComments(vo.getComments());
				int updApproval = approvalDAO.updApproval(approvalVO);
	
				int updVacationRequest = 0;
				// 다음 결재자정보 상태변경
				if(nextSignSeq>0) {
					approvalVO.setBizId(vo.getBizId());
					approvalVO.setUserId(nextUserId);
					approvalVO.setSignType(1);
					approvalVO.setSignComments("");
					int updNextApproval = approvalDAO.updApproval(approvalVO);
					
					// 휴가신청정보 결재상태 변경 
					// 결재중처리
					VacationRequestVO vrVO = new VacationRequestVO();
					vrVO.setRequestId(vo.getRequestId());
					vrVO.setStatusCode("1");
					updVacationRequest = annualVacationDAO.updVacationRequest(vrVO);	
					
				} else {
					// 최종결재이므로 최종처리
					
					// 휴가신청정보 결재상태 변경
					// 최종결재인경우 완료처리
					VacationRequestVO vrVO = new VacationRequestVO();
					vrVO.setRequestId(vo.getRequestId());
					vrVO.setStatusCode("9");
					updVacationRequest = annualVacationDAO.updVacationRequest(vrVO);
				}
				
				//System.out.println("curSignSeq=>"+curSignSeq);			
				//System.out.println("nextSignSeq=>"+nextSignSeq);		
				//System.out.println("nextUserId=>"+nextUserId);
				
				if(updVacationRequest>0&&updApproval>0) result = true;
				
			}
		}
		
		return result;
	}
	
	@Override
	public boolean rejectAnnualVacation(VacationRequestVO vo) throws Exception {
		boolean result = false;
		
		System.out.println("[rejectAnnualVacation]  사용자ID -> "+vo.getUserId());

		VacationRequestVO reqVO = annualVacationDAO.getVacationRequest(vo);
		
		int statusCode = 0;
		if(StringUtil.isNotNull(reqVO.getStatusCode())) {
			statusCode = Integer.parseInt(reqVO.getStatusCode());
		}
		if(statusCode>1) return false;
		
		// 결재자정보 조회
		EmpVO empVO = new EmpVO();
		empVO.setBizId(vo.getBizId());
		empVO.setUserId(vo.getUserId());
		empVO.setStartPage(0);
		empVO.setEndPage(99999);
		List<EmpVO> empList = empDAO.getEmpList(empVO);
		
		if(empList.size()>0) {
			EmpVO userVO = empList.get(0);
			
			// 휴가신청정보 결재상태 변경
			VacationRequestVO vrVO = new VacationRequestVO();
			vrVO.setRequestId(vo.getRequestId());
			vrVO.setStatusCode("5");
			int updVacationRequest = annualVacationDAO.updVacationRequest(vrVO);
			
			// 결재정보 상태변경
			ApprovalVO approvalVO = new ApprovalVO();
			approvalVO.setApprovalId(reqVO.getApprovalId());
			approvalVO.setBizId(vo.getBizId());
			approvalVO.setUserId(vo.getUserId());
			approvalVO.setSignType(5);
			approvalVO.setSignComments(vo.getComments());
			int updApproval = approvalDAO.updApproval(approvalVO);
			
			// 사용일수 차감
			AnnualVacationVO avVO = new AnnualVacationVO();
			avVO.setServiceId(AnnualVacationService.SERVICE_ID);
			avVO.setBizId(vo.getBizId());
			avVO.setUserId(reqVO.getUserId());
			avVO.setUseDay(-1*reqVO.getVacationDay());
			int updAnnualVacation = annualVacationDAO.updAnnualVacationUseDay(avVO);
			
			if(updVacationRequest>0&&updApproval>0&&updAnnualVacation>0) result = true;
			
		}
		
		return result;
	}
	
	@Override
	public boolean cancelAnnualVacation(VacationRequestVO vo) throws Exception {
		boolean result = false;
		
		System.out.println("[cancelAnnualVacation]  사용자ID -> "+vo.getUserId());
		
		VacationRequestVO reqVO = annualVacationDAO.getVacationRequest(vo);
		
		int statusCode = 0;
		if(StringUtil.isNotNull(reqVO.getStatusCode())) {
			statusCode = Integer.parseInt(reqVO.getStatusCode());
		}
		if(statusCode>0) return false;
		
		// 휴가신청정보 결재상태 변경
		VacationRequestVO vrVO = new VacationRequestVO();
		vrVO.setRequestId(vo.getRequestId());
		vrVO.setStatusCode("3");
		int updVacationRequest = annualVacationDAO.updVacationRequest(vrVO);

		// 사용일수 차감
		AnnualVacationVO avVO = new AnnualVacationVO();
		avVO.setServiceId(AnnualVacationService.SERVICE_ID);
		avVO.setBizId(vo.getBizId());
		avVO.setUserId(reqVO.getUserId());
		avVO.setUseDay(-1*vo.getVacationDay());
		int updAnnualVacation = annualVacationDAO.updAnnualVacation(avVO);
		
		if(updVacationRequest>0&&updAnnualVacation>0) result = true;
		
		return result;
	}

	@Override
	public AnnualConfigVO getAnnualConfig(AnnualConfigVO vo) throws Exception {
		AnnualConfigVO result = new AnnualConfigVO();
		
		result = annualVacationDAO.getAnnualConfig(vo);
		
		return result;
	}

	@Override
	public int saveAnnualConfig(AnnualConfigVO vo)  throws Exception {
		AnnualConfigVO resultVO = new AnnualConfigVO();
		int result = 0;
		
		// 설정조회
		resultVO = annualVacationDAO.getAnnualConfig(vo);
		if(resultVO!=null) {
			result = annualVacationDAO.updAnnualConfig(vo);
		} else {
			annualVacationDAO.insAnnualConfig(vo);
			result++;
		}
		
		
		return result;
	}
	

	@Override
	public void insAnnualType(AnnualTypeVO vo) throws Exception {
		
		annualVacationDAO.insAnnualType(vo);
		
	}


	@Override
	public List<AnnualTypeVO> getAnnualTypeList(AnnualTypeVO vo) throws Exception {
		List<AnnualTypeVO> list = null;
		
		list = annualVacationDAO.getAnnualTypeList(vo);
		
		return list;
	}

	@Override
	public int saveAnnualType(List<AnnualTypeVO> list)  throws Exception {
		List<AnnualTypeVO> searchList = null;
		int result = 0;
		
		for(int i=0;i<list.size();i++) {
			AnnualTypeVO vo = list.get(i);
			searchList = annualVacationDAO.getAnnualTypeList(vo);
			if(searchList!=null&&searchList.size()>0) {
				annualVacationDAO.updAnnualType(vo);
			} else {
				annualVacationDAO.insAnnualType(vo);
			}
		}
		
		return result;
	}
	
	// 연차사용내역 조회
	@Override
	public List<VacationRequestVO> getVacationHistoryList(VacationRequestVO vo) throws Exception {
		
		List<VacationRequestVO> list = null;
		
		list = annualVacationDAO.getVacationHistoryList(vo);
		
		return list;
	}
	
	@Override
	public int getVacationHistoryListCount(VacationRequestVO vo)  throws Exception {
		
		return annualVacationDAO.getVacationHistoryListCount(vo);
	}

	// 통계조회
	@Override
	public List<AnnualVacationStatVO> getAnnualStatMonthList(AnnualVacationStatVO vo) throws Exception {
		
		List<AnnualVacationStatVO> list = null;
		
		list = annualVacationDAO.getAnnualStatMonthList(vo);
		
		return list;
	}

	@Override
	public AnnualVacationVO sendAnnualVacationExcel(String bizId, String xlsPath, FileVO fileVO) throws Exception {
		List<AnnualVacationVO> annualList = new ArrayList<AnnualVacationVO>();
		AnnualVacationVO result = new AnnualVacationVO();
		System.out.println("[sendAnnualVacationExcel]  기업id -> " + bizId);
		
		try {
			
			// 연차기준정보 파일 등록
			String filePath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
			
			File file = new File(xlsPath);
			if (!file.exists()) {
				System.out.println("[sendAnnualVacationExcel] XLS파일이 존재하지 않습니다.");
				return null;
			}
			
			// 엑셀파일 정보 읽기
			Workbook workbook = Workbook.getWorkbook(file);
			
			Sheet[] sheets = workbook.getSheets();
			
			for (int s=0; s < sheets.length; s++) {
				Sheet sheet = sheets[s];
				int colNum = sheet.getColumns();
				int rowNum = sheet.getRows();
				
				System.out.println("sheet=>" + sheet.getName() + "_" + colNum + "_" + rowNum);
				
				// 일반 엑셀 컬럼값 맵핑
				for (int r=3; r < rowNum; r++) {
					int colCnt = colNum;
					System.out.println("max col count=>"+colCnt);
					
					String empNo = ExcelUtil.getCellValue(sheet.getCell(1, r));
					String empName = ExcelUtil.getCellValue(sheet.getCell(2, r));
					String joinDate = ExcelUtil.getCellValue(sheet.getCell(3, r));
					String leaveDate = ExcelUtil.getCellValue(sheet.getCell(4, r));
					String annualYear = ExcelUtil.getCellValue(sheet.getCell(5, r));
					String totalDay = ExcelUtil.getCellValue(sheet.getCell(6, r));
					String useDay = ExcelUtil.getCellValue(sheet.getCell(5, r));
					
					if (!StringUtil.isNotNull(empNo)) break;
					System.out.println("[sendAnnualVacationExcel] 조회->" + empNo);	
					
					EmpVO empVO = new EmpVO();
					empVO.setBizId(bizId);
					empVO.setEmpNo(empNo);
					EmpVO resultVO = empDAO.getEmpNo(empVO);
					if (resultVO == null) { 
						
						System.out.println("등록된 임직원이 아닙니다.");
						result.setCode("99");
						result.setEmpName(empName);
						result.setExcelRow(r);
						result.setExcelSheet(sheet.getName());
						result.setMessage("등록된 임직원이 아닙니다.");
						return result;
					}
					
					if(!StringUtil.isNotNull(empName) || !StringUtil.isNotNull(joinDate)
						|| !StringUtil.isNotNull(annualYear) || !StringUtil.isNotNull(totalDay) ) {
						System.out.println(empVO.getEmpName()+"님의 필수입력항목이 없습니다.");
						result.setEmpName(empVO.getEmpName());
						result.setExcelRow(r);
						result.setExcelSheet(sheet.getName());
						return result;
					}
					
					result.setEmpName(resultVO.getEmpName());
					System.out.println("[sendAnnualVacationExcel] 대상->" + resultVO.getEmpName());
					
					AnnualVacationVO annualVacationVO = new AnnualVacationVO();
					annualVacationVO.setServiceId(AnnualVacationService.SERVICE_ID);
					annualVacationVO.setBizId(bizId);
					annualVacationVO.setBizName(resultVO.getBizName());
					annualVacationVO.setUserId(resultVO.getUserId());
					annualVacationVO.setAnnualYear(annualYear);
					annualVacationVO.setTotalDay(Double.parseDouble(totalDay));
					annualVacationVO.setUseDay(Double.parseDouble(useDay));
					
					annualList.add(annualVacationVO);
				}
			}
			
			int count = 0;
			// 연차기준정보 등록
			for (int i=0; i < annualList.size(); i++) {
				AnnualVacationVO annualVacationVO = annualList.get(i);
				
				System.out.println("[sendAnnualVacationExcel] 사원 -> " + annualVacationVO.getBizId() +"/" + annualVacationVO.getUserId());
				
				// 연차기준정보 등록
				annualVacationDAO.insAnnualVacation(annualVacationVO);
				
				count++;
				
			}
			
			System.out.println("[sendAnnualVacationExcel] 처리건수->"+count);
			result.setCode(Integer.toString(count));
			workbook.close();
			
			file.delete();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}


}
