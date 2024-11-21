package com.ezsign.feb.hometax.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.feb.hometax.dao.YC200DAO;
import com.ezsign.feb.hometax.dao.YP800DAO;
import com.ezsign.feb.hometax.service.YP800Service;
import com.ezsign.feb.hometax.vo.PaymentARecordVO;
import com.ezsign.feb.hometax.vo.PaymentBRecordVO;
import com.ezsign.feb.hometax.vo.PaymentCRecordVO;
import com.ezsign.feb.hometax.vo.YC200VO;
import com.ezsign.feb.hometax.vo.YP800VO;
import com.ezsign.framework.exception.AngullarProcessException;
import com.ezsign.framework.hometax.PaymentRecordVOUtil;
import com.ezsign.framework.hometax.PaymentRecordValidator;
import com.ezsign.framework.hometax.RecordFileUtil;
import com.ezsign.framework.hometax.RecordProcUtil;
import com.ezsign.framework.vo.SessionVO;

@Service("yp800Service")
public class YP800ServiceImpl implements YP800Service {

	private Logger logger = Logger.getLogger(this.getClass());
	
	private final String NEW_LINE = System.getProperty("line.separator");
	
	@Resource(name = "yp800DAO")
	private YP800DAO yp800DAO;
	
	/* 전자신고기준표 */
	@Resource(name = "yc200DAO")
	private YC200DAO yc200DAO;
	
	
	/**
	 * 전자신고 기초데이타 생성
	 *
	 * @param paramVO
	 * @throws Exception
	 */
	@Override
	public void insertYP800(YP800VO paramVO) throws Exception{
		
		int dataCnt = yp800DAO.getYP800Cnt(paramVO);
		
		//데이타가 없을때만 등록한다.
		if(dataCnt == 0){
			yp800DAO.insYP800(paramVO);
		}
		
	}
	
 
	/**
	 * 간이지급명세서_전자신고대상 사업장 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<YP800VO> getYP800List(YP800VO paramVO) throws Exception {
		List<YP800VO> list = yp800DAO.getYP800List(paramVO);
		return list;
	}
	
	/**
	 * 
	 * 간이지급명세서 전자매체 생성
	 *
	 * @param yp800List
	 * @param loginVO
	 * @param 계약년도
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String,Object> makeElecDocument(List<YP800VO> yp800List, SessionVO loginVO, String 계약년도) throws Exception{
		
		Map<String,Object> resultMap = null;
		
		logger.info("# 작업자ID : " + loginVO.getUserId() );
		logger.info("# 계약년도 : " + 계약년도 );
		logger.info("# loginVO.getBizId : " + loginVO.getBizId() );
		logger.info("# loginVO.getBizName : " + loginVO.getBizName() );
		
		String 계약ID = yp800List.get(0).get계약ID();
		String 제출대상구분코드 = yp800List.get(0).get제출대상구분코드();
		String 제출년월일 = yp800List.get(0).get제출년월일();
		String 근무시기 = yp800List.get(0).get근무시기();
		
		logger.info("# 계약ID : " + 계약ID );
		logger.info("# 제출대상구분코드 : " + 제출대상구분코드 );
		logger.info("# 제출년월일 : " + 제출년월일 );
		logger.info("# 근무시기 : " + 근무시기 );
		
		boolean stepChk = false;
		StringBuffer buffer = new StringBuffer();
		
		try{
			
			/* 전자신고  레코드 정보 조회  */
			//전자신고기준표 레코드 정보를 조회한다.
			YC200VO yc200VO = new YC200VO();
			yc200VO.set레코드("A");
			yc200VO.set년도(계약년도);
			yc200VO.set신고구분("근로소득간이지급명세서");
				
			//A 레코드 필드 리스트
			List<YC200VO> aRecordFieldList = yc200DAO.getYC200List(yc200VO);
			
			//B 레코드 필드 리스트
			yc200VO.set레코드("B");		
			List<YC200VO> bRecordFieldList = yc200DAO.getYC200List(yc200VO);
			
			//C 레코드 필드 리스트
			yc200VO.set레코드("C");
			List<YC200VO> cRecordFieldList = yc200DAO.getYC200List(yc200VO);
			
			if(aRecordFieldList == null || aRecordFieldList.size() == 0){
				throw new AngullarProcessException("A 레코드 필드 정보가 없습니다.");
			}else if(bRecordFieldList == null || bRecordFieldList.size() == 0){
				throw new AngullarProcessException("B 레코드 필드 정보가 없습니다.");
			}else if(cRecordFieldList == null || cRecordFieldList.size() == 0){
				throw new AngullarProcessException("C 레코드 필드 정보가 없습니다.");
			}
			//////////////////////////////////////////////////////
			
			//A 레코드 정보 조회 
			YP800VO param800VO = new YP800VO();
			param800VO.set계약ID(계약ID);
			param800VO.set근무시기(근무시기);
			PaymentARecordVO aRecordInfo = yp800DAO.getARecordInfo(param800VO);
			
			if(aRecordInfo == null){
				throw new AngullarProcessException("제출 사업자 정보가 없습니다.");
			}
			
			//A 레코드 필수여부 체크 
			resultMap = PaymentRecordValidator.getinstance().aRecordValidator(aRecordInfo);
			
			logger.info("# A 레코드 필수여부 체크 : " + resultMap );
			
			//A 레코드 필수값 체크가 성공
			if(StringUtils.equals(String.valueOf(resultMap.get("result")), "1")){
				
				//VO 정보를 자리수에 맞게 셋팅한다. 
				PaymentRecordVOUtil.getinstance().setARecordInfo(aRecordFieldList, aRecordInfo, 제출년월일, yp800List.size());
								
				//ARecordVO 정보를 파싱한다. 
				String aRecordData = RecordProcUtil.classParsing("A", aRecordInfo);
				buffer.append(aRecordData+NEW_LINE);

				logger.info("# aRecordData : " + aRecordData );
				logger.info("# aRecordData.getBytes(KSC5601).length : " + aRecordData.getBytes("KSC5601").length );
				
				/** A 레코드 End *************************************************/				
				
				stepChk = true;
				
			}else{		
				stepChk = false;
				String resultMessage = String.valueOf(resultMap.get("resultMessage"));				
				resultMap.put("resultMessage", aRecordInfo.getA10()+"("+ aRecordInfo.getA9() +") : " + resultMessage);				
			}
			
			//A 레코드가 성공했을때 
			if(stepChk){
				
				int bRecordIdx = 0;  //B레코드 일련번호
				for(YP800VO req800VO : yp800List){					
					
					req800VO.set귀속년도(계약년도);
										
					//B 레코드 정보 조회  (조회조건 : 계약ID, 사업장ID, 근무시기 )
					PaymentBRecordVO bRecordInfo = yp800DAO.getBRecordInfo(req800VO);
					
					if(bRecordInfo == null){
						throw new AngullarProcessException("B레코드 - 사업장ID ["+req800VO.get사업장ID()+"] 원천징수의무자 정보가 없습니다.");
					}
					bRecordInfo.setB9(계약년도);					//귀속년도
					bRecordInfo.setB10(req800VO.get근무시기());   //근무시기
					//B 레코드 필수여부 체크 
					resultMap = PaymentRecordValidator.getinstance().bRecordValidator(bRecordInfo);
					
					logger.info("# B 레코드 필수여부 체크 : " + resultMap );
					
					//B 레코드 필수값 체크가 실패했을때
					if(StringUtils.equals(String.valueOf(resultMap.get("result")), "0")){						
						String resultMessage = String.valueOf(resultMap.get("resultMessage"));						
						throw new AngullarProcessException(bRecordInfo.getB6()+"("+ bRecordInfo.getB5() +") : " + resultMessage);
					}
					
					//B 레코드 정보를 셋팅한다. (bRecordInfo VO 정보 셋팅)
					bRecordIdx += 1;
					
					//VO 정보를 자리수에 맞게 셋팅한다. 
					PaymentRecordVOUtil.getinstance().setBRecordInfo(bRecordFieldList, bRecordInfo, bRecordIdx, 제출대상구분코드);
					
					//BRecordVO 정보를 파싱한다. 
					String bRecordData = RecordProcUtil.classParsing("B", bRecordInfo);
					buffer.append(bRecordData+NEW_LINE);
					
					logger.info("# bRecordData : " + bRecordData );
					logger.info("# bRecordData.getBytes(KSC5601).length : " + bRecordData.getBytes("KSC5601").length );
					
					//근로소득자 사용자 아이디 정보를 조회한다. (사업장 기준),(조회조건 : 계약ID, 사업장ID)					
					List<Map<String,Object>> empList = yp800DAO.getEmpList(req800VO);
					
					int cRecordIdx = 0;  //C레코드 일련번호
					for(Map<String,Object> empMap : empList){
						
						String 사용자ID = String.valueOf(empMap.get("사용자ID"));						
						logger.info("# 사용자ID : " + 사용자ID );
						
						//C 레코드 정보 조회 						
						req800VO.set사용자ID(사용자ID);						
						PaymentCRecordVO cRecordInfo = yp800DAO.getCRecordInfo(req800VO);
						
						if(cRecordInfo == null){
							throw new AngullarProcessException("C레코드 - 사업장ID ["+req800VO.get사업장ID()+"] 소득자 정보가 없습니다.");
						}
						
						//C 레코드 필수여부 체크  
						resultMap = PaymentRecordValidator.getinstance().cRecordValidator(cRecordInfo);
						
						//C 레코드 필수값 체크가 실패했을때
						if(StringUtils.equals(String.valueOf(resultMap.get("result")), "0")){						
							String resultMessage = String.valueOf(resultMap.get("resultMessage"));						
							throw new AngullarProcessException("C레코드 - [사업장 : "+StringUtils.trimToEmpty(cRecordInfo.getC5())+" - 근로자  : "+StringUtils.trimToEmpty(cRecordInfo.getC7())+ "] : " + resultMessage);
						}
						
						//C 레코드 정보를 셋팅한다. (cRecordInfo VO 정보 셋팅)
						cRecordIdx += 1;
						
						//VO 정보를 자리수에 맞게 셋팅한다. 
						PaymentRecordVOUtil.getinstance().setCRecordInfo(cRecordFieldList, cRecordInfo, cRecordIdx);
						
						//CRecordVO 정보를 파싱한다. 
						String cRecordData = RecordProcUtil.classParsing("C", cRecordInfo);
						buffer.append(cRecordData+NEW_LINE);
						
						logger.info("# cRecordData : " + cRecordData );
						logger.info("# cRecordData.getBytes(KSC5601).length : " + cRecordData.getBytes("KSC5601").length );
						
					}
					
				}
			}
			
			logger.info("# buffer => " + buffer.toString() );
			
			
			//전자문서 파일을 작성한다.
			//근로소득 (C)
			String fileName = "SC"+aRecordInfo.getA9().substring(0, 7)+"."+aRecordInfo.getA9().substring(7);		    
			String filePath = RecordFileUtil.fileWrite(fileName, buffer);
			
		    
		    // 파일정보를 등록한다.
		    YP800VO yp800VO = new YP800VO();
		    yp800VO.set계약ID(계약ID);
		    yp800VO.set사용여부("1");
		    yp800VO.set근무시기(근무시기);
		    yp800VO.set제출대상구분코드(제출대상구분코드);
		    yp800VO.set구분자코드("SC");
		    
		    int rstCnt = yp800DAO.getYP800Cnt(yp800VO);

		    yp800VO.set작업자ID(loginVO.getUserId());
		    // 있는경우 사용여부를 '0' 으로 변경한다.
		    if(rstCnt > 0){		    	
		    	yp800DAO.updYP800(yp800VO);
		    }
		    
		    yp800VO.set제출년월일(제출년월일);		    
		    yp800VO.set파일위치(filePath);
		    yp800VO.set파일명(fileName);
		    
		    		    
		    yp800DAO.insYP800(yp800VO);

		    resultMap.put("fileName", fileName);
		    resultMap.put("filePath", filePath + File.separator + fileName);
		    
		}catch(AngullarProcessException ex){
			logger.error("# 간이지급명세서 오류 : " + ex.getMessage() );
			
			resultMap = new HashMap<String,Object>();
			
			resultMap.put("result", "0");
			resultMap.put("resultMessage", ex.getMessage());
		}
		
		return resultMap;
	}
	
}
