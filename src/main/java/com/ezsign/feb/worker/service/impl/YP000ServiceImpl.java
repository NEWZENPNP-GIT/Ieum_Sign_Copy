package com.ezsign.feb.worker.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.master.dao.YP000DAO;
import com.ezsign.feb.master.vo.YP000VO;
import com.ezsign.feb.system.dao.YS030DAO;
import com.ezsign.feb.system.dao.YS031DAO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.feb.worker.dao.YE001DAO;
import com.ezsign.feb.worker.dao.YP002DAO;
import com.ezsign.feb.worker.dao.YP003DAO;
import com.ezsign.feb.worker.service.YP000Service;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.feb.worker.vo.YP002VO;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;

@Service("yp000Service")
public class YP000ServiceImpl implements YP000Service {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name = "yp000DAO")
	private YP000DAO yp000DAO;
	
	@Resource(name = "yp003DAO")
	private YP003DAO yp003DAO;
	
	@Resource(name = "ys031DAO")
	private YS031DAO ys031DAO;
	
	@Resource(name = "empDAO")
	private EmpDAO empDAO;
	
	@Resource(name="bizDAO")
	private BizDAO bizDAO;
	
	@Resource(name = "yp002DAO")
	private YP002DAO yp002DAO;
	
	@Resource(name = "ye001DAO")
	private YE001DAO ye001DAO;
	
	@Resource(name = "ys030DAO")
    private YS030DAO ys030DAO;
	
	// 간이지급명세서_근로자상세정보 조회
	@Override
	public YP000VO getYP000(YP000VO vo, int userType) throws Exception {
			String identityCode = "";
			
			YP000VO yp000VO = yp000DAO.getYP000(vo);
			logger.debug("# getYP000 : " + yp000VO);

			// 개인식별코드 복호화
			if (yp000VO != null && (StringUtil.isNotNull(yp000VO.get개인식별번호()))) {
				identityCode = SecurityUtil.getinstance().aesDecode(yp000VO.get개인식별번호());
				yp000VO.set개인식별번호(identityCode);
			}

			return yp000VO;
	}
	
	// 연말정산_근로자 배정 미배정 수정
	@Override
	public void saveYP000Assign(List<YP000VO> list) throws Exception {
		for(YP000VO yp000VO: list) {
			if("U".equals(yp000VO.getDbMode())) {
				yp000DAO.updYP000(yp000VO);
			}
		}
	}
		
	// 간이지급명세서_근로자상세정보 입력
	@Override
	public Map<String,Object> saveYP000(YP000VO vo) throws Exception {
			
		Map<String,Object> resultMap = new HashMap<String,Object>();
				
		String identityCode = "";

		//데이터 존재 여부확인 (계약ID)
		logger.info("# 계약ID  :  " + vo.get계약ID());
		logger.info("# 근무시기  :  " + vo.get근무시기());

		try{
			//부서ID없는경우 부서명으로 설정
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
						resultMap.put("message","[ "+ vo.getBusinessNo() +" ] 사업장 정보가 없습니다.");						
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
			empDAO.updEmp(empVO);
		
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
				
			YP000VO yp000VO = new YP000VO();
			yp000VO.set계약ID(vo.get계약ID());
//			ye000VO.setBizId(vo.getBizId());
			yp000VO.set사용자ID(vo.get사용자ID());
			yp000VO.set근무시기(vo.get근무시기());
				
			int dataCnt = yp000DAO.getYP000DataCount(yp000VO);
				
			if(dataCnt > 0) {
				// 근로자기본설정 수정
				yp000DAO.updYP000(vo);
			} else {
				// 근로자기본설정 등록
				vo.set진행상태코드("1");
				yp000DAO.insYP000(vo);
			}
		
			
			
			YP002VO yp002VO = new YP002VO();
			yp002VO.set계약ID(vo.get계약ID());
			yp002VO.set사용자ID(vo.get사용자ID());
			yp002VO.set근무지구분("1");
			
			YP002VO resultYP002VO = null;
			String 원천명세ID = "";
			//근무시기가 하반기 인경우 상반기 정보를 조회한다.
			if(StringUtils.equals("2", vo.get근무시기())){
				yp002VO.set근무시기("1");
				resultYP002VO = yp002DAO.getYP002(yp002VO);
				
				//상반기 정보가 있는경우
				if(resultYP002VO != null){
					원천명세ID = resultYP002VO.get원천명세ID();
				}
			}
			
			yp002VO.set근무시기(vo.get근무시기());
			//현재 업데이트 근무지 정보를 조회한다.			
			resultYP002VO = yp002DAO.getYP002(yp002VO);
									
			if(resultYP002VO != null) {
				
				// 원천명세(급여항목) 수정
				vo.set원천명세ID(resultYP002VO.get원천명세ID());
		
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
				vo.set일련번호(resultYP002VO.get일련번호());
				vo.set사업자등록번호(bizsel.getBusinessNo());
				vo.set회사명(bizsel.getBizName());
					
				//근로자_주/종근무지 수정														
				yp002DAO.updYP002(vo);
					
				vo.set일련번호(일련번호);
				vo.set원천명세ID(resultYP002VO.get원천명세ID());
					
				//원청명세 수정					
				yp003DAO.updYP003(vo);
												
			} else {
				
				// 원천명세(급여항목) 등록
				if(StringUtils.isEmpty(원천명세ID)){
					원천명세ID = yp003DAO.getYP003TaxId(); 
				}
				vo.set원천명세ID(원천명세ID);				
				yp003DAO.insYP003(vo);
		
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
															
				//근로자_주/종근무지 등록														
				yp002DAO.insYP002(vo);
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
				
			resultMap.put("result", "1");
				
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
				
			throw ex;
		}
		
		return resultMap;

	}
		
	/**
	 * 간이지급명세서 근로자 정보 리스트 
	 *
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<YP000VO> getYP000List(YP000VO vo) throws Exception {
		List<YP000VO> list = yp000DAO.getYP000List(vo);
		return list;
	}

	/**
	 * 간이지급명세서 근로자 정보 리스트 카운트
	 *
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Override
	public int getYP000ListCount(YP000VO vo) throws Exception {
		return yp000DAO.getYP000ListCount(vo);
	}
	
}
