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
import com.ezsign.feb.hometax.dao.YE800DAO;
import com.ezsign.feb.hometax.service.YE801Service;
import com.ezsign.feb.hometax.vo.CARecordVO;
import com.ezsign.feb.hometax.vo.YC200VO;
import com.ezsign.feb.hometax.vo.YE800VO;
import com.ezsign.framework.exception.AngullarProcessException;
import com.ezsign.framework.hometax.RecordFileUtil;
import com.ezsign.framework.hometax.RecordProcUtil;
import com.ezsign.framework.hometax.RecordVOUtil;
import com.ezsign.framework.hometax.RecordValidator;
import com.ezsign.framework.vo.SessionVO;

@Service("ye801Service")
public class YE801ServiceImpl implements YE801Service{

	private final String NEW_LINE = System.getProperty("line.separator");
//	private final String NEW_LINE = "\r\n";	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name = "ye800DAO")
	private YE800DAO ye800DAO;
	
	/* 전자신고기준표 */
	@Resource(name = "yc200DAO")
	private YC200DAO yc200DAO;
	
	/**
	 * 의료비 지급명세서 생성
	 *
	 * @param ye800List
	 * @param loginVO
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> makeElecDocument(List<YE800VO> ye800List, SessionVO loginVO, String 계약년도) throws Exception{
		
		Map<String,String> resultMap = new HashMap<String,String>();
		
		String 계약ID = ye800List.get(0).get계약ID();
		String 제출대상구분코드 = ye800List.get(0).get제출대상구분코드();
		String 제출년월일 = ye800List.get(0).get제출년월일();
//		String 귀속년도 = loginVO.getFebYear();
		String 제출자사업자번호 = "";
		
		StringBuffer buffer = new StringBuffer();
		
		try{
			
			/* 전자신고  레코드 정보 조회  */
			//전자신고기준표 레코드 정보를 조회한다.
			YC200VO yc200VO = new YC200VO();
			yc200VO.set레코드("CA");
			yc200VO.set년도(계약년도);
			yc200VO.set신고구분("근로소득");
			
			//CA 레코드 필드 리스트
			List<YC200VO> caRecordFieldList = yc200DAO.getYC200List(yc200VO);
			
			///////////////////////////////////////////
						
			//제줄차 정보 조회  
//			YE800VO param800VO = new YE800VO();
//			param800VO.set계약ID(계약ID);
//			ARecordVO aRecordInfo = ye800DAO.getARecordInfo(param800VO);
			
			int caRecordIdx = 1;
			for(YE800VO req800VO : ye800List){
				req800VO.set귀속년도(계약년도);
								
				//원천징수의무자 정보 조회
//				BRecordVO bRecordInfo = ye800DAO.getBRecordInfo(req800VO);
//				
//				if(bRecordInfo == null){
//					throw new AngullarProcessException("사업장ID ["+req800VO.get사업장ID()+"] 원천징수의무자 정보가 없습니다.");
//				}				
				
				//근로소득자 사용자 아이디 정보를 조회한다. (사업장 기준),(조회조건 : 계약ID, 사업장ID)					
				List<Map<String,Object>> empList = ye800DAO.getEmpList(req800VO);
				
				for(Map<String,Object> empMap : empList){
					String 사용자ID = String.valueOf(empMap.get("사용자ID"));
				
					req800VO.set사용자ID(사용자ID);
					
					//의료비지급명세서 조회
					List<CARecordVO> caRecordList = ye800DAO.getCARecordInfo(req800VO);
					
					if(caRecordList != null && caRecordList.size() > 0){
						제출자사업자번호 = caRecordList.get(0).getCA6();
					}
					
					for(CARecordVO caRecordInfo : caRecordList){
						
//						caRecordInfo.setCA3(aRecordInfo.getA3());	    		//세무서 코드
						caRecordInfo.setCA4(String.valueOf(caRecordIdx));	   	//일련번호
						caRecordInfo.setCA5(제출년월일);
//						caRecordInfo.setCA6(aRecordInfo.getA9());				//제출자 사업자등록번호
//						caRecordInfo.setCA7(aRecordInfo.getA7());				//홈택스 아이디
						
//						caRecordInfo.setCA9(bRecordInfo.getB5());				//원천징수의무자 (사업자등록번호)
//						caRecordInfo.setCA10(bRecordInfo.getB6());			//원천징수의무자 (상호)
						
						caRecordInfo.setCA23(제출대상구분코드);
						
						
						resultMap = RecordValidator.getinstance().caRecordValidator(caRecordFieldList, caRecordInfo);
						
						if(StringUtils.equals("0", resultMap.get("result"))){							
							return resultMap;							
						}else{									
							//VO 정보를 자리수에 맞게 셋팅한다. 
							RecordVOUtil.getinstance().setCARecordInfo(caRecordFieldList, caRecordInfo);
							
							//DRecordVO 정보를 파싱한다. 
							String caRecordData = RecordProcUtil.classParsing("CA", caRecordInfo);
							buffer.append(caRecordData+NEW_LINE);
							
							caRecordIdx++;
						}

					}
					
				}
				
			}

			if(resultMap != null && StringUtils.equals("1", resultMap.get("result"))){
				//전자문서를 작성한다.
				//의료비지급 (CA)
				String fileName = "CA"+제출자사업자번호.substring(0, 7)+"."+제출자사업자번호.substring(7);
				String filePath = RecordFileUtil.fileWrite(fileName, buffer);
		    			
			    // 파일정보를 등록한다.
			    YE800VO ye800VO = new YE800VO();
			    ye800VO.set계약ID(계약ID);
			    ye800VO.set사용여부("1");
			    ye800VO.set제출대상구분코드(제출대상구분코드);
			    ye800VO.set구분자코드("CA");		    
			    
			    int rstCnt = ye800DAO.getYE800Cnt(ye800VO);

			    ye800VO.set작업자ID(loginVO.getUserId());
			    // 있는경우 사용여부를 '0' 으로 변경한다.
			    if(rstCnt > 0){		    	
			    	ye800DAO.updYE800(ye800VO);
			    }
		    
			    ye800VO.set제출년월일(제출년월일);		    
			    ye800VO.set파일위치(filePath);
			    ye800VO.set파일명(fileName);
			    		    
			    ye800DAO.insYE800(ye800VO);

		    
			    resultMap.put("result", "1");
			    resultMap.put("resultMessage", "");
			    resultMap.put("fileName", fileName);
			    resultMap.put("filePath", filePath + File.separator + fileName);
			}
			
		}catch(AngullarProcessException ex){
			resultMap = new HashMap<String,String>();
			
			resultMap.put("result", "0");
			resultMap.put("resultMessage", ex.getMessage());
		}
		
		return resultMap;
	}
	
	
	
	
	
}
