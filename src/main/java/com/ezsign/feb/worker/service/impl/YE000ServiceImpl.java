package com.ezsign.feb.worker.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.code.dao.CodeDAO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.service.EmpService;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.system.dao.YS030DAO;
import com.ezsign.feb.system.dao.YS031DAO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.feb.worker.dao.YE001DAO;
import com.ezsign.feb.worker.dao.YE002DAO;
import com.ezsign.feb.worker.dao.YE003DAO;
import com.ezsign.feb.worker.service.YE000Service;
import com.ezsign.feb.worker.service.YE001Service;
import com.ezsign.feb.worker.service.YE003Service;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.feb.worker.vo.YE002VO;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.zipcode.dao.ZipCodeDAO; 

@Service("ye000Service")
public class YE000ServiceImpl implements YE000Service {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "codeDAO")
	private CodeDAO codeDAO;

	@Resource(name = "ye000DAO")
	private YE000DAO ye000DAO;

	@Resource(name = "ye002DAO")
	private YE002DAO ye002DAO;

	@Resource(name = "empDAO")
	private EmpDAO empDAO;

	@Resource(name = "ye003Service")
	private YE003Service ye003Serivce;

	@Resource(name = "ye001Service")
	private YE001Service ye001Service;

	@Resource(name = "empService")
	private EmpService empService;
	
	@Resource(name = "ye001DAO")
	private YE001DAO ye001DAO;
	
	@Resource(name = "zipCodeDAO")
	private ZipCodeDAO zipCodeDAO;
	
	@Resource(name="bizDAO")
	private BizDAO bizDAO;
	
	@Resource(name = "ys031DAO")
	private YS031DAO ys031DAO;
	
	@Resource(name = "ye003DAO")
	private YE003DAO ye003DAO;
	
	@Resource(name = "ys030DAO")
    private YS030DAO ys030DAO;
	
	// 연말정산_근로자상세정보 입력
	@Override
	public Map<String,String> saveYE000(YE000VO vo) throws Exception {
		
//		int result = 0;
		Map<String,String> resultMap = new HashMap<String,String>();
		
		
		String identityCode = "";

		// 데이터 존재 여부확인 (계약ID)
		logger.info("# 계약ID:" + vo.get계약ID());

		try{
			// 부서ID없는경우 부서명으로 설정
			if(StringUtil.isNull(vo.get부서ID())) {

				//근로자 부서정보를 체크한다.
				YS031VO ys031VO = new YS031VO();
				ys031VO.set계약ID(vo.get계약ID());
				ys031VO.set부서명(vo.get부서명());
				
				YS031VO rstYS031VO = ys031DAO.getYS031(ys031VO);
				
				if(rstYS031VO == null){
					/*
					resultMap.put("result","500");				
					resultMap.put("message","[ "+vo.getEmpName()+" ] 근로자의 [ "+vo.get부서명()+" ] 부서정보가 없습니다.");
						
					return resultMap;
					*/
					
					//2019-05-17 부서 정보 자동 등록 					
					YS030VO ys030VO = new YS030VO();
		            ys030VO.set계약ID(vo.get계약ID());
		            ys030VO.set사업자등록번호(vo.getBusinessNo());
	                String 사업장ID = ys030DAO.getYS030ID(ys030VO);	                	                
					
	                if(StringUtils.isNotEmpty(사업장ID)){
	                	ys031VO.set사업장ID(사업장ID);		
	                	
	                	String 부서ID = ys031DAO.insYS031(ys031VO);
	                	vo.set사업장ID(사업장ID);
	                	vo.set부서ID(부서ID);
	                }else{
	                	
	                	resultMap.put("result","500");				
						resultMap.put("message","[ "+ vo.get사업장명()+" ] 사업장 정보가 없습니다.");						
						return resultMap;
	                }

				} else {
					vo.set부서ID(rstYS031VO.get부서ID());
					vo.set사업장ID(rstYS031VO.get사업장ID());
				}
			}
				
			//국가코드가 null이면 셋팅
			if(StringUtils.equals("1", vo.get내외국인구분()) && StringUtils.isEmpty(vo.get국가코드())){
				vo.set국가코드("KR");
			}
			
			//거주지국코드가 null이면 셋팅
			if(StringUtils.equals("1", vo.get거주구분()) && StringUtils.isEmpty(vo.get거주지국코드())){
				vo.set거주지국코드("KR");
			}

			EmpVO empVO = new EmpVO();
			empVO.setBizId(vo.getBizId());
			empVO.setUserId(vo.get사용자ID());
			empVO.setEmpNo(vo.getEmpNo());
			empVO.setEmpName(vo.getEmpName());
			empVO.setPhoneNum(vo.getPhoneNum());
			empVO.setEMail(vo.getEMail());
			empVO.setJoinDate(vo.getJoinDate());
			
			//퇴사날짜가 null이 아닌경우 
			if(StringUtil.isNull(vo.getLeaveDate()) || StringUtils.equals("00000101", vo.getLeaveDate())){
	//			empVO.setLeaveDate("0");
				empVO.setLeaveDate("");
			}else{
				empVO.setLeaveDate(vo.getLeaveDate());
			}
	
			empVO.setPositionName(vo.getPositionName());
			empVO.setAddr1(vo.getAddress1());
			empVO.setAddr2(vo.getAddress2());
			empVO.setZipCode(vo.getZipCode());
			
			// 직원정보 수정
			empService.updEmp(empVO);
	
			// 우편번호 등록
			/*ZipCodeVO zipCodeVO = new ZipCodeVO();
			zipCodeVO.set우편번호(vo.getZipCode());
	
			ZipCodeVO resultZipCodeVO = zipCodeDAO.getZipCode(zipCodeVO);
			
			//우편번호 없는경우 등록
			if(resultZipCodeVO == null) {
				zipCodeDAO.insZipCode(zipCodeVO);
			}*/
					
			// 개인식별코드 암호화
			identityCode = SecurityUtil.getinstance().aesEncode(vo.get개인식별번호());
			vo.set개인식별번호(identityCode);
			
			YE000VO ye000VO = new YE000VO();
			ye000VO.set계약ID(vo.get계약ID());
//			ye000VO.setBizId(vo.getBizId());
			ye000VO.set사용자ID(vo.get사용자ID());
			
			int dataCnt = ye000DAO.getYE000DataCount(ye000VO);
			
			if(dataCnt > 0) {
				// 근로자기본설정 수정
				ye000DAO.updYE000(vo);
			} else {
				// 근로자기본설정 등록
				vo.set진행상태코드("1");
				ye000DAO.insYE000(vo);
			}
	
			YE002VO ye002VO = new YE002VO();
			ye002VO.set계약ID(vo.get계약ID());
			ye002VO.set사용자ID(vo.get사용자ID());
			ye002VO.set근무지구분("1");
			YE002VO resultYE002VO = ye002DAO.getYE002(ye002VO);
			
			if(resultYE002VO != null) {
				// 원천명세(급여항목) 수정
				vo.set원천명세ID(resultYE002VO.get원천명세ID());
//				ye003Serivce.updYE003(vo);
	
				// 기업정보 조회 : 사업장명, 사업자등록번호
				BizVO bizVO = new BizVO();
				bizVO.setBizId(vo.getBizId());
				bizVO.setStartPage(0);
				bizVO.setEndPage(1);
				List<BizVO> bizList = bizDAO.getBizList(bizVO);
				BizVO bizsel = new BizVO();
				if(bizList!=null && bizList.size()>0) {
					bizsel = bizList.get(0);
				}
				
				// 근로자 현근무지 수정
				// 현근무지
				vo.set근무지구분("1");
				vo.set근무시작일(vo.getJoinDate());				
				if(StringUtil.nvl(vo.getLeaveDate(), "").equals("") 
                		|| StringUtils.equals("0", vo.getLeaveDate()) 
                		|| StringUtils.equals("00010101", vo.getLeaveDate()) ){
                	vo.set근무종료일("");
                }else{
                	vo.set근무종료일(vo.getLeaveDate());
                }				
				vo.set감면시작일(vo.get감면기간_FROM());
				vo.set감면종료일(vo.get감면기간_TO());
				String 일련번호 = vo.get일련번호();
				vo.set일련번호(resultYE002VO.get일련번호());
				vo.set사업자등록번호(bizsel.getBusinessNo());
				vo.set회사명(bizsel.getBizName());
				
				//근로자_주/종근무지 수정
				ye002DAO.updYE002(vo);
				vo.set일련번호(일련번호);
				vo.set원천명세ID(resultYE002VO.get원천명세ID());
				
				//원청명세 수정			
				ye003DAO.updYE003(vo);
				
			} else {
				// 원천명세(급여항목) 등록
				String 원천명세ID = ye003DAO.insYE003(vo);
	
				if(StringUtils.isNotEmpty(원천명세ID)){		                	
                	vo.set원천명세ID(원천명세ID);
                }
				
				// 기업정보 조회 : 사업장명, 사업자등록번호
				BizVO bizVO = new BizVO();
				bizVO.setBizId(vo.getBizId());
				bizVO.setStartPage(0);
				bizVO.setEndPage(1);
				List<BizVO> bizList = bizDAO.getBizList(bizVO);
				BizVO bizsel = new BizVO();
				if(bizList!=null && bizList.size()>0) {
					bizsel = bizList.get(0);
				}
				
				// 근로자 현근무지 등록
				// 현근무지
				vo.set근무지구분("1");
				vo.set근무시작일(vo.getJoinDate());
				if(StringUtil.nvl(vo.getLeaveDate(), "").equals("") 
                		|| StringUtils.equals("0", vo.getLeaveDate()) 
                		|| StringUtils.equals("00010101", vo.getLeaveDate()) ){
                	vo.set근무종료일("");
                }else{
                	vo.set근무종료일(vo.getLeaveDate());
                }				
				vo.set감면시작일(vo.get감면기간_FROM());
				vo.set감면종료일(vo.get감면기간_TO());
				vo.set사업자등록번호(bizsel.getBusinessNo());
				vo.set회사명(bizsel.getBizName());
				
				ye002DAO.insYE002(vo);
			}
			
	
			// 부양가족에 본인 등록
			YE001VO ye001IDVO = new YE001VO();
			ye001IDVO.set계약ID(vo.get계약ID());
			ye001IDVO.set사용자ID(vo.get사용자ID());
			ye001IDVO.set가족관계("0");		
			YE001VO resultYE001VO = ye001DAO.getYE001ID(ye001IDVO);
			
			YE001VO ye001VO = new YE001VO();
			ye001VO.set계약ID(vo.get계약ID());
			ye001VO.set사용자ID(vo.get사용자ID());
			ye001VO.set가족관계("0");
			ye001VO.set내외국인(vo.get내외국인구분());
			ye001VO.set성명(vo.getEmpName());
			ye001VO.set개인식별번호(vo.get개인식별번호());
			ye001VO.set나이(vo.get나이());
			ye001VO.set기본공제("1");
			ye001VO.set부녀자("2");
			ye001VO.set한부모("2");
			ye001VO.set경로우대("2");
			ye001VO.set장애인("4");
			ye001VO.set자녀("2");
			ye001VO.set출산입양("4");
			if(resultYE001VO!=null) {
				ye001VO.set부양가족ID(resultYE001VO.get부양가족ID());
				ye001DAO.updYE001(ye001VO);				
			} else {
				ye001DAO.insYE001(ye001VO);				
			}
	
//			result++;
			
			resultMap.put("result", "1");
			
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
			
			throw ex;
		}

//		return result;
		return resultMap;

	}

	// 연말정산_근로자상세정보 입력
	@Override
	public int insYE000(YE000VO vo) throws Exception {

		int result = 0;
		String identityCode = "";

		// 데이터 존재 여부확인 (계약ID)
		logger.info("계약ID:" + vo.get계약ID());

		EmpVO empVO = new EmpVO();
		empVO.setBizId(vo.getBizId());
		empVO.setUserId(vo.get사용자ID());
		empVO.setEmpNo(vo.getEmpNo());
		empVO.setEmpName(vo.getEmpName());
		empVO.setPhoneNum(vo.getPhoneNum());
		empVO.setEMail(vo.getEMail());
		empVO.setJoinDate(vo.getJoinDate());
		empVO.setLeaveDate(vo.getLeaveDate());
		empVO.setPositionName(vo.getPositionName());
		empVO.setAddr1(vo.getAddress1());
		empVO.setAddr2(vo.getAddress2());
		empVO.setZipCode(vo.getZipCode());
		
		// 직원정보 수정
		empService.updEmp(empVO);
		
		// 우편번호 등록
		/*ZipCodeVO zipCodeVO = new ZipCodeVO();
		zipCodeVO.set우편번호(vo.getZipCode());

		ZipCodeVO resultZipCodeVO = zipCodeDAO.getZipCode(zipCodeVO);
		
		if(resultZipCodeVO == null) {
			zipCodeDAO.insZipCode(zipCodeVO);
		}*/
		
		// 개인식별코드 암호화
		identityCode = SecurityUtil.getinstance().aesEncode(vo.get개인식별번호());
		vo.set개인식별번호(identityCode);

		
		YE000VO ye000VO = new YE000VO();
		ye000VO = ye000DAO.getYE000(vo);
		
		if(StringUtil.isNotNull(ye000VO.get개인식별번호())) {
			// 근로자기본설정 수정
			ye000DAO.updYE000(vo);
		} else {
			// 근로자기본설정 등록
			vo.set진행상태코드("1");
			ye000DAO.insYE000(vo);
		}

		// 원천명세(급여항목) 등록
		ye003Serivce.insYE003(vo);

		// 기업정보 조회 : 사업장명, 사업자등록번호
		BizVO bizVO = new BizVO();
		bizVO.setBizId(vo.getBizId());
		bizVO.setStartPage(0);
		bizVO.setEndPage(1);
		List<BizVO> bizList = bizDAO.getBizList(bizVO);
		BizVO bizsel = new BizVO();
		if(bizList!=null && bizList.size()>0) {
			bizsel = bizList.get(0);
		}
		
		// 근로자 현근무지 등록
		// 현근무지
		vo.set근무지구분("1");
		vo.set근무시작일(vo.getJoinDate());
		if(StringUtil.nvl(vo.getLeaveDate(), "").equals("") 
        		|| StringUtils.equals("0", vo.getLeaveDate()) 
        		|| StringUtils.equals("00010101", vo.getLeaveDate()) ){
        	vo.set근무종료일("");
        }else{
        	vo.set근무종료일(vo.getLeaveDate());
        }		
		vo.set감면시작일(vo.get감면기간_FROM());
		vo.set감면종료일(vo.get감면기간_TO());
		vo.set사업자등록번호(bizsel.getBusinessNo());
		vo.set회사명(bizsel.getBizName());
		
		ye002DAO.insYE002(vo);

		// 부양가족에 본인 등록
		YE001VO ye001VO = new YE001VO();
		ye001VO.set계약ID(vo.get계약ID());
		ye001VO.set사용자ID(vo.get사용자ID());
		ye001VO.set가족관계("0");
		ye001VO.set내외국인(vo.get내외국인구분());
		ye001VO.set성명(vo.getEmpName());
		ye001VO.set개인식별번호(vo.get개인식별번호());
		ye001VO.set나이(vo.get나이());
		ye001VO.set기본공제("1");
		ye001VO.set부녀자("2");
		ye001VO.set한부모("2");
		ye001VO.set경로우대("2");
		ye001VO.set장애인("4");
		ye001VO.set자녀("2");
		ye001VO.set출산입양("4");

		ye001DAO.insYE001(ye001VO);
		result++;

		return result;

	}

	// 연말정산_근로자상세정보 수정
	@Override
	public int updYE000(YE000VO vo) throws Exception {
		int result = 0;
		String identityCode = "";

		// 근로자 정보 설정
		EmpVO empVO = new EmpVO();
		empVO.setBizId(vo.getBizId());
		empVO.setUserId(vo.get사용자ID());
		empVO.setEmpNo(vo.getEmpNo());
		empVO.setEmpName(vo.getEmpName());
		empVO.setPhoneNum(vo.getPhoneNum());
		empVO.setEMail(vo.getEMail());
		empVO.setJoinDate(vo.getJoinDate());
		empVO.setLeaveDate(vo.getLeaveDate());
		empVO.setPositionName(vo.getPositionName());
		empVO.setAddr1(vo.getAddress1());
		empVO.setAddr2(vo.getAddress2());
		empVO.setZipCode(vo.getZipCode());
		
		// 직원정보 수정
		empService.updEmp(empVO);

		// 우편번호 등록
		/*ZipCodeVO zipCodeVO = new ZipCodeVO();
		zipCodeVO.set우편번호(vo.getZipCode());

		ZipCodeVO resultZipCodeVO = zipCodeDAO.getZipCode(zipCodeVO);
		
		if(resultZipCodeVO == null) {
			zipCodeDAO.insZipCode(zipCodeVO);
		}*/
		
		// 개인식별코드 암호화
		if (StringUtil.isNotNull(vo.get개인식별번호())) {
			identityCode = SecurityUtil.getinstance().aesEncode(vo.get개인식별번호());
			vo.set개인식별번호(identityCode);
		}

		// 근로자기본설정 수정
		ye000DAO.updYE000(vo);

		// 원천명세(급여항목) 수정
		ye003Serivce.updYE003(vo);


		// 기업정보 조회 : 사업장명, 사업자등록번호
		BizVO bizVO = new BizVO();
		bizVO.setBizId(vo.getBizId());
		bizVO.setStartPage(0);
		bizVO.setEndPage(1);
		List<BizVO> bizList = bizDAO.getBizList(bizVO);
		BizVO bizsel = new BizVO();
		if(bizList!=null && bizList.size()>0) {
			bizsel = bizList.get(0);
		}
		
		// 근로자 현근무지 수정
		vo.set근무시작일(vo.getJoinDate());
		if(StringUtil.nvl(vo.getLeaveDate(), "").equals("") 
        		|| StringUtils.equals("0", vo.getLeaveDate()) 
        		|| StringUtils.equals("00010101", vo.getLeaveDate()) ){
        	vo.set근무종료일("");
        }else{
        	vo.set근무종료일(vo.getLeaveDate());
        }	
		vo.set감면시작일(vo.get감면기간_FROM());
		vo.set감면종료일(vo.get감면기간_TO());
		vo.set사업자등록번호(bizsel.getBusinessNo());
		vo.set회사명(bizsel.getBizName());
		
		ye002DAO.updYE002(vo);

		// 부양가족에 본인 수정
		YE001VO ye001VO = new YE001VO();
		ye001VO.set계약ID(vo.get계약ID());
		ye001VO.set사용자ID(vo.get사용자ID());
		ye001VO.set부양가족ID(vo.get사용자ID());
		ye001VO.set내외국인(vo.get내외국인구분());
		ye001VO.set성명(vo.getEmpName());
		ye001VO.set개인식별번호(vo.get개인식별번호());
		ye001VO.set나이(vo.get나이());

		logger.debug("ye001VO : " + ye001VO);
		ye001DAO.updYE001(ye001VO);

		result++;

		return result;
	}

	// 연말정산_근로자상세정보 삭제
	@Override
	public int delYE000(YE000VO vo) throws Exception {
		int result = 0;

		// 근로자 정보 설정
		EmpVO empVO = new EmpVO();
		empVO.setBizId(vo.getBizId());
		empVO.setUserId(vo.get사용자ID());

		// 직원정보 삭제
		empService.delEmp(empVO);

		// 근로자기본설정 삭제
		ye000DAO.delYE000(vo);

		// 원천명세(급여항목) 삭제
		ye003Serivce.delYE003(vo);

		// 근로자 현근무지 삭제
		ye002DAO.delYE002(vo);

		// 부양가족 본인 및 가족 삭제
		YE001VO ye001VO = new YE001VO();
		ye001VO.set계약ID(vo.get계약ID());
		ye001VO.set사용자ID(vo.get사용자ID());

		ye001Service.delYE001(ye001VO);

		result++;

		return result;
	}

	// 연말정산_근로자상세정보 조회
	@Override
	public YE000VO getYE000(YE000VO vo, int userType) throws Exception {
		String identityCode = "";
		
		YE000VO ye000VO = ye000DAO.getYE000(vo);
		logger.debug("# getYE000 : " + ye000VO);

		// 개인식별코드 복호화
		if (ye000VO != null && (StringUtil.isNotNull(ye000VO.get개인식별번호()))) {
			identityCode = SecurityUtil.getinstance().aesDecode(ye000VO.get개인식별번호());
			ye000VO.set개인식별번호(identityCode);
		}

		return ye000VO;
	}

	// 연말정산_근로자상세정보 리스트 조회
	@Override
	public List<YE000VO> getYE000List(YE000VO vo) throws Exception {
		List<YE000VO> list = ye000DAO.getYE000List(vo);
		return list;
	}

	@Override
	public int getYE000ListCount(YE000VO vo) throws Exception {
		return ye000DAO.getYE000ListCount(vo);
	}

	// 연말정산_근로자 도움 리스트 조회
	@Override
	public List<YE000VO> getWorkerAssistList(YE000VO vo) throws Exception {
		List<YE000VO> list = new ArrayList<YE000VO>();

		// 시스템관리자 전체 조회, 그룹관리자 등록
		EmpVO empVO = new EmpVO();
		empVO.setBizId(vo.getBizId());
		empVO.setUserId(vo.get작업자ID());
		empVO.setStartPage(0);
		empVO.setEndPage(1);
		List<EmpVO> empList = empDAO.getEmpList(empVO);

		if (empList.size() > 0) {
			// 기업관리자 미만인 경우에는 부서권한만 이용가능
			int empType = Integer.parseInt(empList.get(0).getEmpType());
			if (empType > 6) {
				list = ye000DAO.getWorkerAssistList(vo);
			} else {
				list = ye000DAO.getWorkerAssistDeptList(vo);
			}
		}

		return list;
	}
	
	// 연말정산_근로자 배정 미배정 수정
	@Override
	public void saveYE000Assign(List<YE000VO> list) throws Exception {
		for(YE000VO ye000VO: list) {
			if("U".equals(ye000VO.getDbMode())) {
				ye000DAO.updYE000(ye000VO);
			}
		}
	}
	
	

	// 연말정산_근로자_수정여부
	@Override
	public int getYE000ChkEdit(YE000VO vo) throws Exception {
		return ye000DAO.getYE000ChkEdit(vo);
	}

	// 연말정산_근로자_수정여부_관리자용
	@Override
	public int getYE000AdminChkEdit(YE000VO vo) throws Exception {
		return ye000DAO.getYE000AdminChkEdit(vo);
	}
	
	/**
	 * 
	 * <pre>
	 * 1. 개요 : 계약정보 상세조회
	 * 2. 처리내용 : 
	 * </pre>
	 *
	 * @Method Name : getYE000DataDetail
	 * @date : 2019. 1. 23.
	 * @author : soybean0627
	 *
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일						작성자					변경내용  
	 *	-----------------------------------------------------------------------
	 *	2019. 1. 23.				soybean0627					최초 작성 
	 *	-----------------------------------------------------------------------
	 *
	 *  @see com.ezsign.feb.worker.service.YE000Service#getYE000DataDetail(com.ezsign.feb.master.vo.YE000VO)
	 *  @param paramsVO
	 *  @return
	 *  @throws Exception
	 */
	public YE000VO getYE000DataDetail(YE000VO paramsVO) throws Exception{		
		return ye000DAO.getYE000DataDetail(paramsVO);
	}
	
}
