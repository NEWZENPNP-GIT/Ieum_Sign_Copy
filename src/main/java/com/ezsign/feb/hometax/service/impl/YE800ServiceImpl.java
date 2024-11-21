package com.ezsign.feb.hometax.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.feb.hometax.dao.YC200DAO;
import com.ezsign.feb.hometax.dao.YE800DAO;
import com.ezsign.feb.hometax.service.YE800Service;
import com.ezsign.feb.hometax.vo.ARecordVO;
import com.ezsign.feb.hometax.vo.BRecordVO;
import com.ezsign.feb.hometax.vo.CRecordVO;
import com.ezsign.feb.hometax.vo.DRecordVO;
import com.ezsign.feb.hometax.vo.ERecordVO;
import com.ezsign.feb.hometax.vo.FRecordDataVO;
import com.ezsign.feb.hometax.vo.FRecordVO;
import com.ezsign.feb.hometax.vo.GRecordVO;
import com.ezsign.feb.hometax.vo.HRecordVO;
import com.ezsign.feb.hometax.vo.IRecordVO;
import com.ezsign.feb.hometax.vo.YC200VO;
import com.ezsign.feb.hometax.vo.YE800VO;
import com.ezsign.feb.house.vo.YE102VO;
import com.ezsign.feb.house.vo.YE103VO;
import com.ezsign.feb.house.vo.YE105VO;
import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.special.vo.YE401VO;
import com.ezsign.feb.special.vo.YE402VO;
import com.ezsign.feb.special.vo.YE403VO;
import com.ezsign.feb.special.vo.YE404VO;
import com.ezsign.feb.special.vo.YE408VO;
import com.ezsign.feb.system.dao.YS030DAO;
import com.ezsign.feb.worker.dao.YE003DAO;
import com.ezsign.feb.worker.dao.YE052DAO;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.feb.worker.vo.YE052VO;
import com.ezsign.framework.exception.AngullarProcessException;
import com.ezsign.framework.hometax.RecordFileUtil;
import com.ezsign.framework.hometax.RecordProcUtil;
import com.ezsign.framework.hometax.RecordVOUtil;
import com.ezsign.framework.hometax.RecordValidator;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;

@Service("ye800Service")
public class YE800ServiceImpl implements YE800Service {

	private final String NEW_LINE = System.getProperty("line.separator");
//	private final String NEW_LINE = "\r\n";
	private Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "codeDAO")
	private CodeDAO codeDAO;

	@Resource(name = "ye800DAO")
	private YE800DAO ye800DAO;
	
	/* 전자신고기준표 */
	@Resource(name = "yc200DAO")
	private YC200DAO yc200DAO;
	
	/* 사업장정보 */
	@Resource(name = "ys030DAO")
	private YS030DAO ys030DAO;
	
	/* 특별소득공제 보험료 (건강,고용) */
	@Resource(name = "ye052DAO")
	private YE052DAO ye052DAO;
	
	/* 원천징수 정보 */
	@Resource(name = "ye003DAO")
	private YE003DAO ye003DAO;
	
	
	
	// 연말정산_전자신고대상 사업장 조회
	@Override
	public List<YE800VO> getYE800List(YE800VO vo) throws Exception {
		List<YE800VO> list = ye800DAO.getYE800List(vo);
		return list;
	}
	
	
	/**
	 * 전자매체 문서 작성 
	 *
	 * @param vo
	 * @throws Exception
	 */
	@Override
	public Map<String,Object> makeElecDocument(List<YE800VO> ye800List, SessionVO loginVO, String 계약년도) throws Exception {
		
		Map<String,Object> resultMap = null;
				
//		logger.info("# vo.get계약ID() : " + ye800List.get(0).get계약ID() );
//		logger.info("# vo.get사업장ID() : " + ye800List.get(0).get사업장ID() );	
//		logger.info("# vo.get전자신고ID() : " + ye800List.get(0).get전자신고ID() );parsing
//		logger.info("# vo.get제출대상구분코드 : " + ye800List.get(0).get제출대상구분코드() );
//		logger.info("# vo.get제출년월일() : " + ye800List.get(0).get제출년월일() );
		logger.info("# 작업자ID : " + loginVO.getUserId() );
		logger.info("# 계약년도 : " + 계약년도 );
		logger.info("# loginVO.getBizId : " + loginVO.getBizId() );
		logger.info("# loginVO.getBizName : " + loginVO.getBizName() );
		
		String 계약ID = ye800List.get(0).get계약ID();
		String 제출대상구분코드 = ye800List.get(0).get제출대상구분코드();
		String 제출년월일 = ye800List.get(0).get제출년월일();
//		String 귀속년도 = loginVO.getFebYear();
		
		logger.info("# 계약ID : " + 계약ID );
		logger.info("# 제출대상구분코드 : " + 제출대상구분코드 );
		logger.info("# 제출년월일 : " + 제출년월일 );
		
		
		boolean stepChk = false;
		StringBuffer buffer = new StringBuffer();

		try{
			
			
			/* 전자신고  레코드 정보 조회  */
			//전자신고기준표 레코드 정보를 조회한다.
			YC200VO yc200VO = new YC200VO();
			yc200VO.set레코드("A");
			yc200VO.set년도(계약년도);
			yc200VO.set신고구분("근로소득");
				
			//A 레코드 필드 리스트
			List<YC200VO> aRecordFieldList = yc200DAO.getYC200List(yc200VO);
			
			//B 레코드 필드 리스트
			yc200VO.set레코드("B");		
			List<YC200VO> bRecordFieldList = yc200DAO.getYC200List(yc200VO);
			
			//C 레코드 필드 리스트
			yc200VO.set레코드("C");
			List<YC200VO> cRecordFieldList = yc200DAO.getYC200List(yc200VO);
			
			//D 레코드 필드 리스트
			yc200VO.set레코드("D");
			List<YC200VO> dRecordFieldList = yc200DAO.getYC200List(yc200VO);
			
			//E 레코드 필드 리스트
			yc200VO.set레코드("E");
			List<YC200VO> eRecordFieldList = yc200DAO.getYC200List(yc200VO);
			
			//F 레코드 필드 리스트
			yc200VO.set레코드("F");
			List<YC200VO> fRecordFieldList = yc200DAO.getYC200List(yc200VO);
			
			//G 레코드 필드 리스트
			yc200VO.set레코드("G");
			List<YC200VO> gRecordFieldList = yc200DAO.getYC200List(yc200VO);
			
			//H 레코드 필드 리스트
			yc200VO.set레코드("H");
			List<YC200VO> hRecordFieldList = yc200DAO.getYC200List(yc200VO);
			
			//I 레코드 필드 리스트
			yc200VO.set레코드("I");
			List<YC200VO> iRecordFieldList = yc200DAO.getYC200List(yc200VO);
			///////////////////////////////////////////////////////////////////////////
			
			
			//A 레코드 정보 조회 
			YE800VO param800VO = new YE800VO();
			param800VO.set계약ID(계약ID);
			ARecordVO aRecordInfo = ye800DAO.getARecordInfo(param800VO);
			
			if(aRecordInfo == null){
				throw new AngullarProcessException("제출 사업자 정보가 없습니다.");
			}
			
			//A 레코드 필수여부 체크 
			resultMap = RecordValidator.getinstance().aRecordValidator(aRecordInfo);
			
			//A 레코드 필수값 체크가 성공
			if(StringUtils.equals(String.valueOf(resultMap.get("result")), "1")){
				
				//VO 정보를 자리수에 맞게 셋팅한다. 
				RecordVOUtil.getinstance().setARecordInfo(aRecordFieldList, aRecordInfo, 제출년월일, ye800List.size());
								
				//ARecordVO 정보를 파싱한다. 
				String aRecordData = RecordProcUtil.classParsing("A", aRecordInfo);
				buffer.append(aRecordData+NEW_LINE);

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
				for(YE800VO req800VO : ye800List){
					req800VO.set귀속년도(계약년도);
					/* 사업장정보 조회 */
					/*YS030VO ys030VO = new YS030VO();
					ys030VO.set계약ID(req800VO.get계약ID());
					ys030VO.set사업장ID(req800VO.get사업장ID());
					ys030VO.setBizId(req800VO.getBizId());
							
					ys030VO = ys030DAO.getYS030Detail(ys030VO);
					
					if(ys030VO == null){
						throw new AngullarProcessException("사업장ID ["+req800VO.get사업장ID()+"] 정보가 없습니다.");
					}*/
					
					//B 레코드 정보 조회  (조회조건 : 계약ID, 사업장ID)
					BRecordVO bRecordInfo = ye800DAO.getBRecordInfo(req800VO);
					
					if(bRecordInfo == null){
						throw new AngullarProcessException("B레코드 - 사업장ID ["+req800VO.get사업장ID()+"] 원천징수의무자 정보가 없습니다.");
					}
					
					//B 레코드 필수여부 체크 
					resultMap = RecordValidator.getinstance().bRecordValidator(bRecordInfo);
					
					//B 레코드 필수값 체크가 실패했을때
					if(StringUtils.equals(String.valueOf(resultMap.get("result")), "0")){						
						String resultMessage = String.valueOf(resultMap.get("resultMessage"));						
						throw new AngullarProcessException(bRecordInfo.getB6()+"("+ bRecordInfo.getB5() +") : " + resultMessage);
					}
					
					//B 레코드 정보를 셋팅한다. (bRecordInfo VO 정보 셋팅)
					bRecordIdx += 1;
					
					//VO 정보를 자리수에 맞게 셋팅한다. 
					RecordVOUtil.getinstance().setBRecordInfo(bRecordFieldList, bRecordInfo, bRecordIdx, 제출대상구분코드);
					
					//BRecordVO 정보를 파싱한다. 
					String bRecordData = RecordProcUtil.classParsing("B", bRecordInfo);
					buffer.append(bRecordData+NEW_LINE);
					
					//근로소득자 사용자 아이디 정보를 조회한다. (사업장 기준),(조회조건 : 계약ID, 사업장ID)					
					List<Map<String,Object>> empList = ye800DAO.getEmpList(req800VO);		
					
					int cRecordIdx = 0;  //C레코드 일련번호
					for(Map<String,Object> empMap : empList){
						String 사용자ID = String.valueOf(empMap.get("사용자ID"));
						String 종전근무처수 = String.valueOf(empMap.get("종전근무처수"));
						
						logger.info("# 사용자ID : " + 사용자ID + " /  종전근무처수 : " + 종전근무처수 );
						
						//C 레코드 정보 조회 						
						req800VO.set사용자ID(사용자ID);						
						CRecordVO cRecordInfo = ye800DAO.getCRecordInfo(req800VO);
						
						if(cRecordInfo == null){
							throw new AngullarProcessException("C레코드 - 사업장ID ["+req800VO.get사업장ID()+"] 주(현)근무처 정보가 없습니다.");
						}
						
						//C 레코드 필수여부 체크  
						resultMap = RecordValidator.getinstance().cRecordValidator(cRecordInfo);
						
						//C 레코드 필수값 체크가 실패했을때
						if(StringUtils.equals(String.valueOf(resultMap.get("result")), "0")){						
							String resultMessage = String.valueOf(resultMap.get("resultMessage"));						
							throw new AngullarProcessException("C레코드 - [사업장 : "+StringUtils.trimToEmpty(cRecordInfo.getC20())+" - 근로자  : "+StringUtils.trimToEmpty(cRecordInfo.getC11())+ "] : " + resultMessage);
						}
						
						//C 레코드 정보를 셋팅한다. (cRecordInfo VO 정보 셋팅)
						cRecordIdx += 1;
						
						//VO 정보를 자리수에 맞게 셋팅한다. 
						RecordVOUtil.getinstance().setCRecordInfo(cRecordFieldList, cRecordInfo, cRecordIdx);
						
						//CRecordVO 정보를 파싱한다. 
						String cRecordData = RecordProcUtil.classParsing("C", cRecordInfo);
						buffer.append(cRecordData+NEW_LINE);						
						
						//- D레코드  -////////////////////////////////////////////////////////////////////////////////////////////////
						//종(전)근무처수가 0보다 크면 종(전)근무처정보를 조회한다.
						if(StringUtil.strPaserInt(종전근무처수)> 0){
							
							//D 레코드 정보 조회 	
							List<DRecordVO> dRecordList = ye800DAO.getDRecordInfo(req800VO);
							
							if(dRecordList == null || dRecordList.size() == 0){
								throw new AngullarProcessException("D레코드 - [사업장 : "+StringUtils.trimToEmpty(cRecordInfo.getC20())+" - 근로자  : "+StringUtils.trimToEmpty(cRecordInfo.getC11())+ "] 종(전)근무처 정보가 없습니다.");
							}
							
							int dRecordIdx = 0;
							for(DRecordVO dRecordInfo : dRecordList){
								
								//D 레코드 필수여부 체크  
								resultMap = RecordValidator.getinstance().dRecordValidator(dRecordInfo);
								
								//D 레코드 필수값 체크가 실패했을때
								if(StringUtils.equals(String.valueOf(resultMap.get("result")), "0")){						
									String resultMessage = String.valueOf(resultMap.get("resultMessage"));						
									throw new AngullarProcessException("D레코드 - [사업장 : "+StringUtils.trimToEmpty(cRecordInfo.getC20())+" - 근로자  : "+StringUtils.trimToEmpty(cRecordInfo.getC11())+ "] : " + resultMessage);
								}
								
								//D 레코드 정보를 셋팅한다. (dRecordInfo VO 정보 셋팅)
								dRecordIdx += 1;
								
								//VO 정보를 자리수에 맞게 셋팅한다. 
								RecordVOUtil.getinstance().setDRecordInfo(dRecordFieldList, dRecordInfo, cRecordIdx, dRecordIdx);
								
								//DRecordVO 정보를 파싱한다. 
								String dRecordData = RecordProcUtil.classParsing("D", dRecordInfo);
								buffer.append(dRecordData+NEW_LINE);	
								
							}
							
						}
						//- D레코드  -////////////////////////////////////////////////////////////////////////////////////////////////
						
						
						/****************** E 레코드 생성 ******************/						
						createERecordInfo(eRecordFieldList, req800VO, cRecordInfo, cRecordIdx, buffer);						
						/****************** E 레코드 생성 ******************/
						
						
						/****************** F 레코드 생성 ******************/
						createFRecordInfo(fRecordFieldList, req800VO, cRecordInfo, cRecordIdx, buffer);
						/****************** F 레코드 생성 ******************/
						
						
						/****************** G 레코드 생성 ******************/
						createGRecordInfo(gRecordFieldList, req800VO, cRecordInfo, cRecordIdx, buffer);
						/****************** G 레코드 생성 ******************/
						
						/****************** H 레코드 생성 ******************/
						createHRecordInfo(hRecordFieldList, req800VO, cRecordInfo, cRecordIdx, buffer);
						/****************** H 레코드 생성 ******************/
						
						/****************** I 레코드 생성 ******************/
						createIRecordInfo(iRecordFieldList, req800VO, cRecordInfo, cRecordIdx, buffer);
						/****************** I 레코드 생성 ******************/
					}
				}
				
				
			}

			//전자문서 파일을 작성한다.
			//근로소득 (C)
			String fileName = "C"+aRecordInfo.getA9().substring(0, 7)+"."+aRecordInfo.getA9().substring(7);		    
			String filePath = RecordFileUtil.fileWrite(fileName, buffer);
			
		    
		    // 파일정보를 등록한다.
		    YE800VO ye800VO = new YE800VO();
		    ye800VO.set계약ID(계약ID);
		    ye800VO.set사용여부("1");
		    ye800VO.set제출대상구분코드(제출대상구분코드);
		    ye800VO.set구분자코드("C");
		    
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

		    resultMap.put("fileName", fileName);
		    resultMap.put("filePath", filePath + File.separator + fileName);
		    
		    
		}catch(AngullarProcessException ex){
			resultMap = new HashMap<String,Object>();
			
			resultMap.put("result", "0");
			resultMap.put("resultMessage", ex.getMessage());
		}
				
		
		return resultMap;
	}
	
	
	/**
	 * I 레코드 정보를 생성한다.
	 * 
	 * @param iRecordFieldList
	 * @param ye800VO
	 * @param cRecordInfo
	 * @param cRecordIdx
	 * @param buffer
	 * @throws Exception
	 */
	private void createIRecordInfo(List<YC200VO> iRecordFieldList, YE800VO ye800VO,CRecordVO cRecordInfo,int cRecordIdx, StringBuffer buffer) throws Exception {
		
		//해당 연도 기부명세 
		List<IRecordVO> iRecordList = ye800DAO.getIRecordList(ye800VO);
				
		if(iRecordList != null && iRecordList.size() > 0){
			
			int iRecordIdx = 1;
			for(IRecordVO iRecordVO : iRecordList){
				
				iRecordVO.setI3(cRecordInfo.getC3());
				iRecordVO.setI4(String.valueOf(cRecordIdx));
				iRecordVO.setI5(cRecordInfo.getC5());		
				iRecordVO.setI6(cRecordInfo.getC13());				
				iRecordVO.setI20(String.valueOf(iRecordIdx));   //일련번호
				
				//VO 정보를 자리수에 맞게 셋팅한다. 
				RecordVOUtil.getinstance().setIRecordInfo(iRecordFieldList, iRecordVO);
				String iRecordData = RecordProcUtil.classParsing("I", iRecordVO);
				buffer.append(iRecordData+NEW_LINE);
				
				iRecordIdx++;
			}
			
		}
		
	}
	
	
	
	
	/**
	 * H 레코드 정보를 생선한다.
	 * 
	 * @param gRecordFieldList
	 * @param ye800VO
	 * @param cRecordInfo
	 * @param cRecordIdx
	 * @param buffer
	 * @throws Exception
	 */
	private void createHRecordInfo(List<YC200VO> hRecordFieldList, YE800VO ye800VO,CRecordVO cRecordInfo,int cRecordIdx, StringBuffer buffer) throws Exception {
	
		//기부금 조정명세 
		List<YE408VO> ye408List = ye800DAO.getYe408List(ye800VO);
		
		HRecordVO hRecordVO = null;
		
		if(ye408List != null && ye408List.size() > 0){
			
			int hRecordIdx = 1;
			for(YE408VO ye408VO : ye408List){
				hRecordVO = new HRecordVO();
				
				hRecordVO.setH3(cRecordInfo.getC3());
				hRecordVO.setH4(String.valueOf(cRecordIdx));
				hRecordVO.setH5(cRecordInfo.getC5());
				hRecordVO.setH6(cRecordInfo.getC13());
				hRecordVO.setH7(cRecordInfo.getC12());
				hRecordVO.setH8(cRecordInfo.getC11());
				
				
				hRecordVO.setH9(ye408VO.get기부금종류코드());
				hRecordVO.setH10(ye408VO.get기부년도());							
				hRecordVO.setH11(ye408VO.get기부금액());
				hRecordVO.setH12(String.valueOf(ye408VO.get전년도까지공제금액()));
//				hRecordVO.setH13(String.valueOf(ye408VO.get공제대상기부금()));
				hRecordVO.setH13(String.valueOf(ye408VO.get해당연도공제금액() + ye408VO.get소멸금액() + ye408VO.get이월금액()));
				hRecordVO.setH15(String.valueOf(ye408VO.get해당연도공제금액()));
				hRecordVO.setH16(String.valueOf(ye408VO.get소멸금액()));
				hRecordVO.setH17(String.valueOf(ye408VO.get이월금액()));
				hRecordVO.setH18(String.valueOf(hRecordIdx));
				
				//VO 정보를 자리수에 맞게 셋팅한다. 
				RecordVOUtil.getinstance().setHRecordInfo(hRecordFieldList, hRecordVO);
				String hRecordData = RecordProcUtil.classParsing("H", hRecordVO);
				buffer.append(hRecordData+NEW_LINE);
				
				hRecordIdx++;
			}
		}
			
	}
	
	
	
	
	/**
	 * G 레코드 정보를 생성한다.
	 * 
	 * @param gRecordFieldList
	 * @param ye800VO
	 * @param cRecordInfo
	 * @param cRecordIdx
	 * @param buffer
	 * @throws Exception
	 */
	private void createGRecordInfo(List<YC200VO> gRecordFieldList, YE800VO ye800VO,CRecordVO cRecordInfo,int cRecordIdx, StringBuffer buffer) throws Exception {
		
		//월세액 세액공제명세 
		List<YE105VO> ye105List = ye800DAO.getYe105List(ye800VO);
		//거주자간 주택임차차입금원리금상환액 - 금전소비대차 계약내용
		List<YE102VO> ye102List = ye800DAO.getYe102List(ye800VO);
		//거주자간 주택임차차입금원리금상환액 - 임대차 계약내용
		List<YE103VO> ye103List = ye800DAO.getYe103List(ye800VO);
		
		GRecordVO gRecordVO = null;
		
		//for문 인덱스 정보
		int forIdx = 0;
		
		if(ye105List != null && ye105List.size() > 0){
			forIdx = ye105List.size();
		}
		if(ye102List != null && ye102List.size() > 0){
			if(forIdx < ye102List.size()){
				forIdx = ye102List.size();
			}
		}
		if(ye103List != null && ye103List.size() > 0){
			if(forIdx < ye103List.size()){
				forIdx = ye103List.size();
			}
		}
		
		boolean writeChk = false;
		int gRecordIdx = 1;
		for(int i = 0 ; i < forIdx ; i++){
			
			// 월세액 세액공제명세  3건이상이면 레코드를 작성한다.
			if((i+1) > 3 && ((i+1) % 3) == 1){
				
				//VO 정보를 자리수에 맞게 셋팅한다. 
				RecordVOUtil.getinstance().setGRecordInfo(gRecordFieldList, gRecordVO, gRecordIdx);
				
				String gRecordData = RecordProcUtil.classParsing("G", gRecordVO);
				buffer.append(gRecordData+NEW_LINE);
				
				writeChk = true;
				gRecordIdx++;
			}
			
			if(((i+1) % 3) == 1){
				gRecordVO = new GRecordVO();
				
				gRecordVO.setG3(cRecordInfo.getC3());
				gRecordVO.setG4(String.valueOf(cRecordIdx));
				gRecordVO.setG5(cRecordInfo.getC5());
				gRecordVO.setG6(cRecordInfo.getC13());
				
				writeChk = false;
			}
			
			//첫번째항목 
			if(((i+1) % 3) == 1){
				
				//월세액
				if(ye105List != null && i < ye105List.size()){
					gRecordVO.setG7(ye105List.get(i).get임대인성명_상호());
					gRecordVO.setG8(ye105List.get(i).get개인식별번호());
					gRecordVO.setG9(ye105List.get(i).get유형코드());
					gRecordVO.setG10(RecordProcUtil.strPad(ye105List.get(i).get계약면적()));
					gRecordVO.setG11(ye105List.get(i).get임대차_주소지_물건지());
					gRecordVO.setG12(ye105List.get(i).get임대차_계약개시일());
					gRecordVO.setG13(ye105List.get(i).get임대차_계약종료일());
					gRecordVO.setG14(String.valueOf(ye105List.get(i).get연간_월세액()));
					gRecordVO.setG15(String.valueOf(ye105List.get(i).get세액공제액()));
				}
				
				//거주자간 주택임차차입금원리금상환액 - 금전소비대차 계약내용
				if(ye102List != null && i < ye102List.size()){
					gRecordVO.setG16(ye102List.get(i).get대주성명());					
					gRecordVO.setG17(ye102List.get(i).get개인식별번호());
					gRecordVO.setG18(ye102List.get(i).get금전소비대차_계약개시일());
					gRecordVO.setG19(ye102List.get(i).get금전소비대차_계약종료일());
					gRecordVO.setG20(String.valueOf(ye102List.get(i).get차입금_이자율()));
					gRecordVO.setG21(String.valueOf(ye102List.get(i).get원리금상환액계()));
					gRecordVO.setG22(String.valueOf(ye102List.get(i).get원금()));
					gRecordVO.setG23(String.valueOf(ye102List.get(i).get이자()));
					gRecordVO.setG24(String.valueOf(ye102List.get(i).get소득공제금액()));
				}
				
				//거주자간 주택임차차입금원리금상환액 - 임대차 계약내용
				if(ye103List != null && i < ye103List.size()){				
					gRecordVO.setG25(ye103List.get(i).get임대인성명_상호());
					gRecordVO.setG26(ye103List.get(i).get개인식별번호());
					gRecordVO.setG27(ye103List.get(i).get유형코드());
					gRecordVO.setG28(RecordProcUtil.strPad(ye103List.get(i).get계약면적()));
					gRecordVO.setG29(ye103List.get(i).get임대차_주소지_물건지());
					gRecordVO.setG30(ye103List.get(i).get임대차_계약개시일());
					gRecordVO.setG31(ye103List.get(i).get임대차_계약종료일());
					gRecordVO.setG32(String.valueOf(ye103List.get(i).get전세보증금()));					
				}
				
			}
			//두번째항목 
			else if(((i+1) % 3) == 2){
					
				//월세액
				if(ye105List != null && i < ye105List.size()){
					gRecordVO.setG33(ye105List.get(i).get임대인성명_상호());
					gRecordVO.setG34(ye105List.get(i).get개인식별번호());
					gRecordVO.setG35(ye105List.get(i).get유형코드());
					gRecordVO.setG36(RecordProcUtil.strPad(ye105List.get(i).get계약면적()));
					gRecordVO.setG37(ye105List.get(i).get임대차_주소지_물건지());
					gRecordVO.setG38(ye105List.get(i).get임대차_계약개시일());
					gRecordVO.setG39(ye105List.get(i).get임대차_계약종료일());
					gRecordVO.setG40(String.valueOf(ye105List.get(i).get연간_월세액()));
					gRecordVO.setG41(String.valueOf(ye105List.get(i).get세액공제액()));
				}
				
				//거주자간 주택임차차입금원리금상환액 - 금전소비대차 계약내용
				if(ye102List != null && i < ye102List.size()){
					gRecordVO.setG42(ye102List.get(i).get대주성명());					
					gRecordVO.setG43(ye102List.get(i).get개인식별번호());
					gRecordVO.setG44(ye102List.get(i).get금전소비대차_계약개시일());
					gRecordVO.setG45(ye102List.get(i).get금전소비대차_계약종료일());
					gRecordVO.setG46(String.valueOf(ye102List.get(i).get차입금_이자율()));
					gRecordVO.setG47(String.valueOf(ye102List.get(i).get원리금상환액계()));
					gRecordVO.setG48(String.valueOf(ye102List.get(i).get원금()));
					gRecordVO.setG49(String.valueOf(ye102List.get(i).get이자()));
					gRecordVO.setG50(String.valueOf(ye102List.get(i).get소득공제금액()));
				}
				
				//거주자간 주택임차차입금원리금상환액 - 임대차 계약내용
				if(ye103List != null && i < ye103List.size()){
				
					gRecordVO.setG51(ye103List.get(i).get임대인성명_상호());
					gRecordVO.setG52(ye103List.get(i).get개인식별번호());
					gRecordVO.setG53(ye103List.get(i).get유형코드());					
					gRecordVO.setG54(RecordProcUtil.strPad(ye103List.get(i).get계약면적()));
					gRecordVO.setG55(ye103List.get(i).get임대차_주소지_물건지());
					gRecordVO.setG56(ye103List.get(i).get임대차_계약개시일());
					gRecordVO.setG57(ye103List.get(i).get임대차_계약종료일());
					gRecordVO.setG58(String.valueOf(ye103List.get(i).get전세보증금()));
					
				}
				
			}
			//세번째항목 
			else if(((i+1) % 3) == 0){
					
				//월세액
				if(ye105List != null && i < ye105List.size()){
					gRecordVO.setG59(ye105List.get(i).get임대인성명_상호());
					gRecordVO.setG60(ye105List.get(i).get개인식별번호());
					gRecordVO.setG61(ye105List.get(i).get유형코드());
					gRecordVO.setG62(RecordProcUtil.strPad(ye105List.get(i).get계약면적()));
					gRecordVO.setG63(ye105List.get(i).get임대차_주소지_물건지());
					gRecordVO.setG64(ye105List.get(i).get임대차_계약개시일());
					gRecordVO.setG65(ye105List.get(i).get임대차_계약종료일());
					gRecordVO.setG66(String.valueOf(ye105List.get(i).get연간_월세액()));
					gRecordVO.setG67(String.valueOf(ye105List.get(i).get세액공제액()));
				}
				
				//거주자간 주택임차차입금원리금상환액 - 금전소비대차 계약내용
				if(ye102List != null && i < ye102List.size()){
					gRecordVO.setG68(ye102List.get(i).get대주성명());					
					gRecordVO.setG69(ye102List.get(i).get개인식별번호());
					gRecordVO.setG70(ye102List.get(i).get금전소비대차_계약개시일());
					gRecordVO.setG71(ye102List.get(i).get금전소비대차_계약종료일());
					gRecordVO.setG72(String.valueOf(ye102List.get(i).get차입금_이자율()));
					gRecordVO.setG73(String.valueOf(ye102List.get(i).get원리금상환액계()));
					gRecordVO.setG74(String.valueOf(ye102List.get(i).get원금()));
					gRecordVO.setG75(String.valueOf(ye102List.get(i).get이자()));
					gRecordVO.setG76(String.valueOf(ye102List.get(i).get소득공제금액()));
				}
				
				//거주자간 주택임차차입금원리금상환액 - 임대차 계약내용
				if(ye103List != null && i < ye103List.size()){
				
					gRecordVO.setG77(ye103List.get(i).get임대인성명_상호());
					gRecordVO.setG78(ye103List.get(i).get개인식별번호());
					gRecordVO.setG79(ye103List.get(i).get유형코드());					
					gRecordVO.setG80(RecordProcUtil.strPad(ye103List.get(i).get계약면적()));
					gRecordVO.setG81(ye103List.get(i).get임대차_주소지_물건지());
					gRecordVO.setG82(ye103List.get(i).get임대차_계약개시일());
					gRecordVO.setG83(ye103List.get(i).get임대차_계약종료일());
					gRecordVO.setG84(String.valueOf(ye103List.get(i).get전세보증금()));
					
				}
				
			}
		}
		
		
		if(forIdx > 0 && !writeChk){
			//VO 정보를 자리수에 맞게 셋팅한다. 
			RecordVOUtil.getinstance().setGRecordInfo(gRecordFieldList, gRecordVO, gRecordIdx);
			
			String gRecordData = RecordProcUtil.classParsing("G", gRecordVO);
			buffer.append(gRecordData+NEW_LINE);	
			
		}
	}
	
	/**
	 * F 레코드 정보를 생성한다.
	 * 
	 * @param eRecordFieldList
	 * @param ye800VO
	 * @param cRecordInfo
	 * @param cRecordIdx
	 * @param buffer
	 * @throws Exception
	 */
	private void createFRecordInfo(List<YC200VO> fRecordFieldList, YE800VO ye800VO,CRecordVO cRecordInfo,int cRecordIdx, StringBuffer buffer) throws Exception {
		
		// 연금･저축등 소득·세액 공제명세 리스트
		List<FRecordDataVO> fRecordDataList = ye800DAO.getFRecordList(ye800VO);
		FRecordVO fRecordVO = null;
		
		if(fRecordDataList != null && fRecordDataList.size() > 0){
			int fIdx = 1;
			int fRecordIdx = 1;
			boolean writeChk = false;
			for(FRecordDataVO fRecordDataVO : fRecordDataList){
				
				// 연금･저축등 소득·세액  15건이상이면 레코드를 작성한다.
				if(fIdx > 15 && (fIdx % 15) == 1){
					
					//VO 정보를 자리수에 맞게 셋팅한다.
					RecordVOUtil.getinstance().setFRecordInfo(fRecordFieldList, fRecordVO, fRecordIdx);
					String fRecordData = RecordProcUtil.classParsing("F", fRecordVO);
					buffer.append(fRecordData+NEW_LINE);	
					
					writeChk = true;
					fRecordIdx++;
				}
				
				if((fIdx % 15) == 1){
					fRecordVO = new FRecordVO();
					
					fRecordVO.setF3(cRecordInfo.getC3());
					fRecordVO.setF4(String.valueOf(cRecordIdx));
					fRecordVO.setF5(cRecordInfo.getC5());
					fRecordVO.setF6(cRecordInfo.getC13());
					
					writeChk = false;
				}
				
				//F 레코드 반복구간
				loopFRecordVO(ye800VO, fRecordDataVO, fRecordVO, fIdx);	
				
				fIdx++;
			}
			
			if(!writeChk){
				//VO 정보를 자리수에 맞게 셋팅한다.
				RecordVOUtil.getinstance().setFRecordInfo(fRecordFieldList, fRecordVO, fRecordIdx);
				String fRecordData = RecordProcUtil.classParsing("F", fRecordVO);
				buffer.append(fRecordData+NEW_LINE);					
			}
		}
	}
	
	
	
	/**
	 * F 레코드 반복구간 처리
	 * 
	 * @param ye800VO
	 * @param fRecordDataVO
	 * @param fRecordVO
	 * @param fIdx
	 * @throws Exception
	 */
	private void loopFRecordVO(YE800VO ye800VO, FRecordDataVO fRecordDataVO, FRecordVO fRecordVO, int fIdx) throws Exception {
		
		//연금･저축등 소득·세액 공제명세1
		if((fIdx % 15) == 1){
			fRecordVO.setF7(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF8(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF9(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF10(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF11(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF12(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF13(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF14(StringUtil.nvl(fRecordDataVO.get투자구분(),""));
		}
		//연금･저축등 소득·세액 공제명세2
		else if((fIdx % 15) == 2){
			fRecordVO.setF15(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF16(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF17(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF18(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF19(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF20(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF21(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF22(StringUtil.nvl(fRecordDataVO.get투자구분(),""));
		}
		//연금･저축등 소득·세액 공제명세3
		else if((fIdx % 15) == 3){
			fRecordVO.setF23(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF24(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF25(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF26(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF27(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF28(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF29(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF30(StringUtil.nvl(fRecordDataVO.get투자구분(),""));
		}
		//연금･저축등 소득·세액 공제명세4
		else if((fIdx % 15) == 4){
			fRecordVO.setF31(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF32(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF33(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF34(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF35(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF36(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF37(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF38(StringUtil.nvl(fRecordDataVO.get투자구분(),""));
		}
		
		//연금･저축등 소득·세액 공제명세5
		else if((fIdx % 15) == 5){
			fRecordVO.setF39(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF40(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF41(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF42(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF43(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF44(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF45(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF46(StringUtil.nvl(fRecordDataVO.get투자구분(),""));
		}
		//연금･저축등 소득·세액 공제명세6
		else if((fIdx % 15) == 6){
			fRecordVO.setF47(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF48(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF49(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF50(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF51(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF52(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF53(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF54(StringUtil.nvl(fRecordDataVO.get투자구분(),""));
		}
		//연금･저축등 소득·세액 공제명세7
		else if((fIdx % 15) == 7){
			fRecordVO.setF55(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF56(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF57(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF58(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF59(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF60(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF61(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF62(StringUtil.nvl(fRecordDataVO.get투자구분(),""));
		}
		//연금･저축등 소득·세액 공제명세8
		else if((fIdx % 15) == 8){
			fRecordVO.setF63(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF64(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF65(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF66(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF67(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF68(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF69(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF70(StringUtil.nvl(fRecordDataVO.get투자구분(),""));
		}
		//연금･저축등 소득·세액 공제명세9
		else if((fIdx % 15) == 9){
			fRecordVO.setF71(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF72(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF73(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF74(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF75(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF76(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF77(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF78(StringUtil.nvl(fRecordDataVO.get투자구분(),""));
		}
		//연금･저축등 소득·세액 공제명세10
		else if((fIdx % 15) == 10){
			fRecordVO.setF79(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF80(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF81(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF82(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF83(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF84(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF85(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF86(StringUtil.nvl(fRecordDataVO.get투자구분(),""));
		}
		//연금･저축등 소득·세액 공제명세11
		else if((fIdx % 15) == 11){
			fRecordVO.setF87(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF88(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF89(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF90(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF91(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF92(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF93(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF94(StringUtil.nvl(fRecordDataVO.get투자구분(),""));
		}
		//연금･저축등 소득·세액 공제명세12
		else if((fIdx % 15) == 12){
			fRecordVO.setF95(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF96(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF97(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF98(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF99(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF100(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF101(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF102(StringUtil.nvl(fRecordDataVO.get투자구분(),""));
		}
		//연금･저축등 소득·세액 공제명세13
		else if((fIdx % 15) == 13){
			fRecordVO.setF103(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF104(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF105(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF106(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF107(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF108(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF109(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF110(StringUtil.nvl(fRecordDataVO.get투자구분(),""));
		}
		//연금･저축등 소득·세액 공제명세14
		else if((fIdx % 15) == 14){
			fRecordVO.setF111(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF112(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF113(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF114(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF115(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF116(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF117(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF118(StringUtil.nvl(fRecordDataVO.get투자구분(),""));
		}
		//연금･저축등 소득·세액 공제명세15
		else if((fIdx % 15) == 0){
			fRecordVO.setF119(StringUtil.nvl(fRecordDataVO.get소득공제구분(),""));
			fRecordVO.setF120(StringUtil.nvl(fRecordDataVO.get금융기관코드(),""));
			fRecordVO.setF121(StringUtil.nvl(fRecordDataVO.get금융기관상호(),""));
			fRecordVO.setF122(StringUtil.nvl(fRecordDataVO.get계좌번호(),""));
			fRecordVO.setF123(StringUtil.nvl(fRecordDataVO.get납입금액(),""));
			fRecordVO.setF124(StringUtil.nvl(fRecordDataVO.get소득세액공제금액(),""));
			fRecordVO.setF125(StringUtil.nvl(fRecordDataVO.get투자연도(),"0000"));
			fRecordVO.setF126(StringUtil.nvl(fRecordDataVO.get투자구분(),""));			
		}
		
	}
	
	/**
	 * E 레코드 정보를 생성한다.
	 * 
	 * @param ye800VO
	 * @param cRecordInfo
	 * @param cRecordIdx
	 * @throws Exception
	 */
	private void createERecordInfo(List<YC200VO> eRecordFieldList, YE800VO ye800VO,CRecordVO cRecordInfo,int cRecordIdx, StringBuffer buffer) throws Exception {
				
		//부양가족 정보 조회
		List<YE001VO> ye001List = ye800DAO.getYe001List(ye800VO);
		ERecordVO eRecordVO = null;
		
		int eRecordIdx = 1;
		int dIdx = 1;
		boolean writeChk = false;
		
		if(ye001List != null && ye001List.size() > 0){
			for(YE001VO ye001VO : ye001List){
				
				//부양가족 5건이상이면 레코드를 작성한다.
				if(dIdx > 5 && (dIdx % 5) == 1){					
					//VO 정보를 자리수에 맞게 셋팅한다.
					RecordVOUtil.getinstance().setERecordInfo(eRecordFieldList, eRecordVO, eRecordIdx);
					String eRecordData = RecordProcUtil.classParsing("E", eRecordVO);
					buffer.append(eRecordData+NEW_LINE);	
					
					writeChk = true;
					eRecordIdx++;
				}
				
				// 부양가족이 5명이 넘을때 새로 E 레코드 새로생성 
				if((dIdx % 5) == 1){
					eRecordVO = new ERecordVO();
					
					eRecordVO.setE3(cRecordInfo.getC3());
					eRecordVO.setE4(String.valueOf(cRecordIdx));
					eRecordVO.setE5(cRecordInfo.getC5());
					eRecordVO.setE6(cRecordInfo.getC13());
					
					writeChk = false;
				}
	
				//E 레코드 반복구간
				loopERecordVO(ye800VO, ye001VO, eRecordVO, dIdx);			
				dIdx++;
			}
			
			
			if(!writeChk){
				//VO 정보를 자리수에 맞게 셋팅한다.
				RecordVOUtil.getinstance().setERecordInfo(eRecordFieldList, eRecordVO, eRecordIdx);			
				String eRecordData = RecordProcUtil.classParsing("E", eRecordVO);
				buffer.append(eRecordData+NEW_LINE);	
			}
		}
	}
	
	
	
	/**
	 * E 레코드 반복구간생성
	 * 
	 * @param ye800VO
	 * @param eRecordVO
	 * @param dRecordIdx
	 */
	private void loopERecordVO(YE800VO ye800VO, YE001VO ye001VO, ERecordVO eRecordVO, int dIdx) throws Exception {
		
		/* 보험료  */
		YE401VO ye401VO = new YE401VO();
		ye401VO.set계약ID(ye800VO.get계약ID());
		ye401VO.set사용자ID(ye800VO.get사용자ID());
		ye401VO.set부양가족ID(ye001VO.get부양가족ID());
						
		List<YE401VO> ye401List = ye800DAO.getYe401List(ye401VO);
		
		/* 의료비 */
		YE402VO ye402VO = new YE402VO();
		ye402VO.set계약ID(ye800VO.get계약ID());
		ye402VO.set사용자ID(ye800VO.get사용자ID());
		ye402VO.set부양가족ID(ye001VO.get부양가족ID());
						
		List<YE402VO> ye402List = ye800DAO.getYe402List(ye402VO);
		
		/* 교육비 */
		YE403VO ye403VO = new YE403VO();
		ye403VO.set계약ID(ye800VO.get계약ID());
		ye403VO.set사용자ID(ye800VO.get사용자ID());
		ye403VO.set부양가족ID(ye001VO.get부양가족ID());
		
		List<YE403VO> ye403List = ye800DAO.getYe403List(ye403VO);		

		/* 신용카드 */
		YE108VO ye108VO = new YE108VO();
		ye108VO.set계약ID(ye800VO.get계약ID());
		ye108VO.set사용자ID(ye800VO.get사용자ID());
		ye108VO.set부양가족ID(ye001VO.get부양가족ID());
		
		List<YE108VO> ye108List = ye800DAO.getYe108List(ye108VO);
		
		/* 기부금 */
		YE404VO ye404VO = new YE404VO();
		ye404VO.set계약ID(ye800VO.get계약ID());
		ye404VO.set사용자ID(ye800VO.get사용자ID());
		ye404VO.set부양가족ID(ye001VO.get부양가족ID());
		
		List<YE404VO> ye404List = ye800DAO.getYe404List(ye404VO);
		
		//인적사항 1
		if((dIdx % 5) == 1){
			eRecordVO.setE7(ye001VO.get가족관계());
			eRecordVO.setE8(ye001VO.get내외국인());
			eRecordVO.setE9(ye001VO.get성명().toUpperCase());
			
			String 개인식별번호 = SecurityUtil.getinstance().aesDecode(ye001VO.get개인식별번호());
			eRecordVO.setE10(개인식별번호);
			eRecordVO.setE11(ye001VO.get기본공제());
			eRecordVO.setE12(ye001VO.get장애인());
			eRecordVO.setE13(ye001VO.get부녀자());
			eRecordVO.setE14(ye001VO.get경로우대());
			eRecordVO.setE15(ye001VO.get한부모());
			eRecordVO.setE16(ye001VO.get출산입양());
			eRecordVO.setE17(ye001VO.get자녀());
			eRecordVO.setE18(ye001VO.get교육비공제());
			
			//소득자 본인 일때만
			if(StringUtils.equals("0", ye001VO.get가족관계())){								
				/* 특별소득공제 보험료 조회 */
				YE000VO ye000VO = new YE000VO();
				ye000VO.set계약ID(ye800VO.get계약ID());
				ye000VO.set사용자ID(ye800VO.get사용자ID());
				List<YE000VO> ye003List = ye003DAO.getYE003List(ye000VO);

					
				
				int 건강보험료_추가납입금액 = 0;
				int 장기요양보험료_추가납입금액 = 0;
				int 고용보험료_추가납입금액 = 0;		
				//국세청 PDF 정보 조회
				YE052VO ye052VO = new YE052VO();
				ye052VO.set계약ID(ye800VO.get계약ID());
				ye052VO.set사용자ID(ye800VO.get사용자ID());
				
				List<YE052VO> ye052List = ye052DAO.getYE052List(ye052VO);
				for(YE052VO vo : ye052List){					
					//건강보험료
					if(StringUtils.equals("1",vo.get보험료구분())){
						건강보험료_추가납입금액 += vo.get추가납입금액();
					}
					//장기요양보험료
					else if(StringUtils.equals("2",vo.get보험료구분())){
						장기요양보험료_추가납입금액 += vo.get추가납입금액();
					}
					//고용보험료
					else if(StringUtils.equals("3",vo.get보험료구분())){
						고용보험료_추가납입금액 += vo.get추가납입금액();
					}
				}
				
				int 건강보험료 = 0;
				int 장기요양보험료 = 0;
				int 고용보험료 = 0;		
				if(ye003List != null & ye003List.size() > 0){
					for(YE000VO rstVO : ye003List){
						건강보험료 += StringUtil.strPaserInt(rstVO.get건강보험료());
						장기요양보험료 += StringUtil.strPaserInt(rstVO.get장기요양보험료());
						고용보험료 += StringUtil.strPaserInt(rstVO.get고용보험료());
					}
										
					//건강보험료 - 기타
					eRecordVO.setE35(String.valueOf(건강보험료+장기요양보험료+건강보험료_추가납입금액+장기요양보험료_추가납입금액));
					//고용보험료 - 기타
					eRecordVO.setE36(String.valueOf(고용보험료+고용보험료_추가납입금액));
				}
								
				
				//건강보험료 - 국세청
				eRecordVO.setE19(String.valueOf(건강보험료+장기요양보험료));
			}
			
			/* 보험료  */
			if(ye401List != null){
				for(YE401VO vo : ye401List){
					
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//보장성
						if(StringUtils.equals("1", vo.get보험구분코드())){
							eRecordVO.setE21(String.valueOf(vo.get납입금액() + StringUtil.strPaserInt(eRecordVO.getE21()) ));
						}					
						//장애인보장성
						else if(StringUtils.equals("2", vo.get보험구분코드())){
							eRecordVO.setE22(String.valueOf(vo.get납입금액()) + StringUtil.strPaserInt(eRecordVO.getE22()) );
						}
					}else{
						//보장성
						if(StringUtils.equals("1", vo.get보험구분코드())){
							eRecordVO.setE37(String.valueOf(vo.get납입금액() + StringUtil.strPaserInt(eRecordVO.getE37()) ));
						}					
						//장애인보장성
						else if(StringUtils.equals("2", vo.get보험구분코드())){
							eRecordVO.setE38(String.valueOf(vo.get납입금액() + StringUtil.strPaserInt(eRecordVO.getE38()) ));
						}
					}
				}
			}
			
			/* 의료비 */
			if(ye402List != null){
				for(YE402VO vo : ye402List){
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//장애인
						if(StringUtils.equals("1", ye001VO.get장애인()) || StringUtils.equals("2", ye001VO.get장애인()) || StringUtils.equals("3", ye001VO.get장애인())){
							eRecordVO.setE25(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE25()) ));
						}else{							
							//난임
							if(StringUtils.equals("3", vo.get의료비유형())){
								eRecordVO.setE24(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE24()) ));
							}else{
								eRecordVO.setE23(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE23()) ));
							}
						}
						
						
					}else{
						//장애인
						if(StringUtils.equals("1", ye001VO.get장애인()) || StringUtils.equals("2", ye001VO.get장애인()) || StringUtils.equals("3", ye001VO.get장애인())){
							eRecordVO.setE41(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE41()) ));
						}else{
							//난임
							if(StringUtils.equals("3", vo.get의료비유형())){
								eRecordVO.setE40(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE40()) ));
							}else{
								eRecordVO.setE39(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE39()) ));
							}
						}
					}
				}
			}
			
			/* 교육비 */
			if(ye403List != null){						
				for(YE403VO vo : ye403List){
					
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//장애인
						if(StringUtils.equals("5", vo.get공제종류코드())){
							eRecordVO.setE27(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비() + StringUtil.strPaserInt(eRecordVO.getE27()) ));
						}else{
							eRecordVO.setE26(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비() + StringUtil.strPaserInt(eRecordVO.getE26()) ));
						}							
					}else{
						//장애인
						if(StringUtils.equals("5", vo.get공제종류코드())){
							eRecordVO.setE43(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비() + StringUtil.strPaserInt(eRecordVO.getE43()) ));
						}else{
							eRecordVO.setE42(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비() + StringUtil.strPaserInt(eRecordVO.getE42()) ));
						}
					}
					
				}
			}
			
			/* 신용카드 */		
			if(ye108List != null){	
				for(YE108VO vo : ye108List){
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						eRecordVO.setE28(String.valueOf(vo.get신용카드() + StringUtil.strPaserInt(eRecordVO.getE28()) ));
						eRecordVO.setE29(String.valueOf(vo.get직불_선불카드() + StringUtil.strPaserInt(eRecordVO.getE29()) ));
						eRecordVO.setE30(String.valueOf(vo.get현금영수증()  + StringUtil.strPaserInt(eRecordVO.getE30())  ));
						eRecordVO.setE31(String.valueOf(vo.get도서공연()+vo.get도서공연_직불카드()+vo.get도서공연_현금영수증()  + StringUtil.strPaserInt(eRecordVO.getE31()) ));
						eRecordVO.setE32(String.valueOf(vo.get전통시장() + StringUtil.strPaserInt(eRecordVO.getE32()) ));
						eRecordVO.setE33(String.valueOf(vo.get대중교통() + StringUtil.strPaserInt(eRecordVO.getE33())));						
					}else{
						eRecordVO.setE44(String.valueOf(vo.get신용카드() + StringUtil.strPaserInt(eRecordVO.getE44())));
						eRecordVO.setE45(String.valueOf(vo.get직불_선불카드() + StringUtil.strPaserInt(eRecordVO.getE45())));
						eRecordVO.setE46(String.valueOf(vo.get도서공연()+vo.get도서공연_직불카드()+vo.get도서공연_현금영수증() + StringUtil.strPaserInt(eRecordVO.getE46())));
						eRecordVO.setE47(String.valueOf(vo.get전통시장() + StringUtil.strPaserInt(eRecordVO.getE47())));
						eRecordVO.setE48(String.valueOf(vo.get대중교통() + StringUtil.strPaserInt(eRecordVO.getE48())));
					}
				}
			}
			
			/* 기부금 */
			if(ye404List != null){	
				for(YE404VO vo : ye404List){
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						eRecordVO.setE34(String.valueOf(vo.get기부명세_공제대상기부금()+vo.get기부명세_기부장려금()+vo.get기부명세_기타() + StringUtil.strPaserInt(eRecordVO.getE34()) ));
					}else{
						eRecordVO.setE49(String.valueOf(vo.get기부명세_공제대상기부금()+vo.get기부명세_기부장려금()+vo.get기부명세_기타() + StringUtil.strPaserInt(eRecordVO.getE49()) ));
					}
				}
			}
		}
		//인적사항 2
		else if((dIdx % 5) == 2){
			eRecordVO.setE50(ye001VO.get가족관계());
			eRecordVO.setE51(ye001VO.get내외국인());
			eRecordVO.setE52(ye001VO.get성명().toUpperCase());
			
			String 개인식별번호 = SecurityUtil.getinstance().aesDecode(ye001VO.get개인식별번호());
			eRecordVO.setE53(개인식별번호);
			eRecordVO.setE54(ye001VO.get기본공제());
			eRecordVO.setE55(ye001VO.get장애인());
			eRecordVO.setE56(ye001VO.get부녀자());
			eRecordVO.setE57(ye001VO.get경로우대());
			eRecordVO.setE58(ye001VO.get한부모());
			eRecordVO.setE59(ye001VO.get출산입양());
			eRecordVO.setE60(ye001VO.get자녀());
			eRecordVO.setE61(ye001VO.get교육비공제());
					
			/* 보험료  */
			if(ye401List != null){
				for(YE401VO vo : ye401List){
					
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//보장성
						if(StringUtils.equals("1", vo.get보험구분코드())){
							eRecordVO.setE64(String.valueOf(vo.get납입금액() + StringUtil.strPaserInt(eRecordVO.getE64())));
						}					
						//장애인보장성
						else if(StringUtils.equals("2", vo.get보험구분코드())){
							eRecordVO.setE65(String.valueOf(vo.get납입금액() + StringUtil.strPaserInt(eRecordVO.getE65())));
						}
					}else{
						//보장성
						if(StringUtils.equals("1", vo.get보험구분코드())){
							eRecordVO.setE80(String.valueOf(vo.get납입금액() + StringUtil.strPaserInt(eRecordVO.getE80())));
						}					
						//장애인보장성
						else if(StringUtils.equals("2", vo.get보험구분코드())){
							eRecordVO.setE81(String.valueOf(vo.get납입금액() + StringUtil.strPaserInt(eRecordVO.getE81())));
						}
					}
				}
			}
			
			/* 의료비 */
			if(ye402List != null){
				for(YE402VO vo : ye402List){
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//장애인
						if(StringUtils.equals("1", ye001VO.get장애인()) || StringUtils.equals("2", ye001VO.get장애인()) || StringUtils.equals("3", ye001VO.get장애인())){
							eRecordVO.setE68(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE68()) ));
						}else{							
							//난임
							if(StringUtils.equals("3", vo.get의료비유형())){
								eRecordVO.setE67(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE67())));
							}else{
								eRecordVO.setE66(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE66())));
							}
						}
						
						
					}else{
						//장애인
						if(StringUtils.equals("1", ye001VO.get장애인()) || StringUtils.equals("2", ye001VO.get장애인()) || StringUtils.equals("3", ye001VO.get장애인())){
							eRecordVO.setE84(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE84())));
						}else{
							//난임
							if(StringUtils.equals("3", vo.get의료비유형())){
								eRecordVO.setE83(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE83())));
							}else{
								eRecordVO.setE82(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE82())));
							}
						}
					}
				}
			}
			
			/* 교육비 */
			if(ye403List != null){						
				for(YE403VO vo : ye403List){
					
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//장애인
						if(StringUtils.equals("5", vo.get공제종류코드())){
							eRecordVO.setE70(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비()  + StringUtil.strPaserInt(eRecordVO.getE70()) ));
						}else{
							eRecordVO.setE69(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비() + StringUtil.strPaserInt(eRecordVO.getE69()) ));
						}							
					}else{
						//장애인
						if(StringUtils.equals("5", vo.get공제종류코드())){
							eRecordVO.setE86(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비()  + StringUtil.strPaserInt(eRecordVO.getE86()) ));
						}else{
							eRecordVO.setE85(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비() + StringUtil.strPaserInt(eRecordVO.getE85()) ));
						}
					}
					
				}
			}
			
			/* 신용카드 */		
			if(ye108List != null){	
				for(YE108VO vo : ye108List){
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						eRecordVO.setE71(String.valueOf(vo.get신용카드() + StringUtil.strPaserInt(eRecordVO.getE71()) ));
						eRecordVO.setE72(String.valueOf(vo.get직불_선불카드() + StringUtil.strPaserInt(eRecordVO.getE72())));
						eRecordVO.setE73(String.valueOf(vo.get현금영수증() + StringUtil.strPaserInt(eRecordVO.getE73())));
						eRecordVO.setE74(String.valueOf(vo.get도서공연()+vo.get도서공연_직불카드()+vo.get도서공연_현금영수증() + StringUtil.strPaserInt(eRecordVO.getE74()) ));
						eRecordVO.setE75(String.valueOf(vo.get전통시장() + StringUtil.strPaserInt(eRecordVO.getE75())));
						eRecordVO.setE76(String.valueOf(vo.get대중교통() + StringUtil.strPaserInt(eRecordVO.getE76())));						
					}else{
						eRecordVO.setE87(String.valueOf(vo.get신용카드() + StringUtil.strPaserInt(eRecordVO.getE87())));
						eRecordVO.setE88(String.valueOf(vo.get직불_선불카드() + StringUtil.strPaserInt(eRecordVO.getE88())));
						eRecordVO.setE89(String.valueOf(vo.get도서공연()+vo.get도서공연_직불카드()+vo.get도서공연_현금영수증() + StringUtil.strPaserInt(eRecordVO.getE89()) ));
						eRecordVO.setE90(String.valueOf(vo.get전통시장() + StringUtil.strPaserInt(eRecordVO.getE90())));
						eRecordVO.setE91(String.valueOf(vo.get대중교통() + StringUtil.strPaserInt(eRecordVO.getE91())));
					}
				}
			}
			
			/* 기부금 */
			if(ye404List != null){	
				for(YE404VO vo : ye404List){
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						eRecordVO.setE77(String.valueOf(vo.get기부명세_공제대상기부금()+vo.get기부명세_기부장려금()+vo.get기부명세_기타() + StringUtil.strPaserInt(eRecordVO.getE77()) ));
					}else{
						eRecordVO.setE92(String.valueOf(vo.get기부명세_공제대상기부금()+vo.get기부명세_기부장려금()+vo.get기부명세_기타() + StringUtil.strPaserInt(eRecordVO.getE92()) ));
					}
				}
			}
		}
		//인적사항 3
		else if((dIdx % 5) == 3){
			eRecordVO.setE93(ye001VO.get가족관계());
			eRecordVO.setE94(ye001VO.get내외국인());
			eRecordVO.setE95(ye001VO.get성명().toUpperCase());
			
			String 개인식별번호 = SecurityUtil.getinstance().aesDecode(ye001VO.get개인식별번호());
			eRecordVO.setE96(개인식별번호);
			eRecordVO.setE97(ye001VO.get기본공제());
			eRecordVO.setE98(ye001VO.get장애인());
			eRecordVO.setE99(ye001VO.get부녀자());
			eRecordVO.setE100(ye001VO.get경로우대());
			eRecordVO.setE101(ye001VO.get한부모());
			eRecordVO.setE102(ye001VO.get출산입양());
			eRecordVO.setE103(ye001VO.get자녀());
			eRecordVO.setE104(ye001VO.get교육비공제());
					
			/* 보험료  */
			if(ye401List != null){
				for(YE401VO vo : ye401List){
					
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//보장성
						if(StringUtils.equals("1", vo.get보험구분코드())){
							eRecordVO.setE107(String.valueOf(vo.get납입금액() + StringUtil.strPaserInt(eRecordVO.getE107()) ));
						}					
						//장애인보장성
						else if(StringUtils.equals("2", vo.get보험구분코드())){
							eRecordVO.setE108(String.valueOf(vo.get납입금액() + StringUtil.strPaserInt(eRecordVO.getE108())));
						}
					}else{
						//보장성
						if(StringUtils.equals("1", vo.get보험구분코드())){
							eRecordVO.setE123(String.valueOf(vo.get납입금액()  + StringUtil.strPaserInt(eRecordVO.getE123()) ));
						}					
						//장애인보장성
						else if(StringUtils.equals("2", vo.get보험구분코드())){
							eRecordVO.setE124(String.valueOf(vo.get납입금액()  + StringUtil.strPaserInt(eRecordVO.getE124()) ));
						}
					}
				}
			}
			
			/* 의료비 */
			if(ye402List != null){
				for(YE402VO vo : ye402List){
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//장애인
						if(StringUtils.equals("1", ye001VO.get장애인()) || StringUtils.equals("2", ye001VO.get장애인()) || StringUtils.equals("3", ye001VO.get장애인())){
							eRecordVO.setE111(String.valueOf(vo.get지출액()  + StringUtil.strPaserInt(eRecordVO.getE111()) ));
						}else{							
							//난임
							if(StringUtils.equals("3", vo.get의료비유형())){
								eRecordVO.setE110(String.valueOf(vo.get지출액()  + StringUtil.strPaserInt(eRecordVO.getE110()) ));
							}else{
								eRecordVO.setE109(String.valueOf(vo.get지출액()  + StringUtil.strPaserInt(eRecordVO.getE109()) ));
							}
						}
						
						
					}else{
						//장애인
						if(StringUtils.equals("1", ye001VO.get장애인()) || StringUtils.equals("2", ye001VO.get장애인()) || StringUtils.equals("3", ye001VO.get장애인())){
							eRecordVO.setE127(String.valueOf(vo.get지출액()  + StringUtil.strPaserInt(eRecordVO.getE127()) ));
						}else{
							//난임
							if(StringUtils.equals("3", vo.get의료비유형())){
								eRecordVO.setE126(String.valueOf(vo.get지출액()  + StringUtil.strPaserInt(eRecordVO.getE126()) ));
							}else{
								eRecordVO.setE125(String.valueOf(vo.get지출액()  + StringUtil.strPaserInt(eRecordVO.getE125())  ));
							}
						}
					}
				}
			}
			
			/* 교육비 */
			if(ye403List != null){						
				for(YE403VO vo : ye403List){
					
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//장애인
						if(StringUtils.equals("5", vo.get공제종류코드())){
							eRecordVO.setE113(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비()  + StringUtil.strPaserInt(eRecordVO.getE113()) ));
						}else{
							eRecordVO.setE112(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비()  + StringUtil.strPaserInt(eRecordVO.getE112()) ));
						}							
					}else{
						//장애인
						if(StringUtils.equals("5", vo.get공제종류코드())){
							eRecordVO.setE129(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비()  + StringUtil.strPaserInt(eRecordVO.getE129()) ));
						}else{
							eRecordVO.setE128(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비()  + StringUtil.strPaserInt(eRecordVO.getE128()) ));
						}
					}
					
				}
			}
			
			/* 신용카드 */		
			if(ye108List != null){	
				for(YE108VO vo : ye108List){
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						eRecordVO.setE114(String.valueOf(vo.get신용카드()  + StringUtil.strPaserInt(eRecordVO.getE114()) ));
						eRecordVO.setE115(String.valueOf(vo.get직불_선불카드() + StringUtil.strPaserInt(eRecordVO.getE115())));
						eRecordVO.setE116(String.valueOf(vo.get현금영수증() + StringUtil.strPaserInt(eRecordVO.getE116())));
						eRecordVO.setE117(String.valueOf(vo.get도서공연()+vo.get도서공연_직불카드()+vo.get도서공연_현금영수증() + StringUtil.strPaserInt(eRecordVO.getE117())));
						eRecordVO.setE118(String.valueOf(vo.get전통시장() + StringUtil.strPaserInt(eRecordVO.getE118())));
						eRecordVO.setE119(String.valueOf(vo.get대중교통() + StringUtil.strPaserInt(eRecordVO.getE119())));						
					}else{
						eRecordVO.setE130(String.valueOf(vo.get신용카드() + StringUtil.strPaserInt(eRecordVO.getE130())));
						eRecordVO.setE131(String.valueOf(vo.get직불_선불카드() + StringUtil.strPaserInt(eRecordVO.getE131()) ));
						eRecordVO.setE132(String.valueOf(vo.get도서공연()+vo.get도서공연_직불카드()+vo.get도서공연_현금영수증() + StringUtil.strPaserInt(eRecordVO.getE132()) ));
						eRecordVO.setE133(String.valueOf(vo.get전통시장() + StringUtil.strPaserInt(eRecordVO.getE133()) ));
						eRecordVO.setE134(String.valueOf(vo.get대중교통() + StringUtil.strPaserInt(eRecordVO.getE134()) ));
					}
				}
			}
			
			/* 기부금 */
			if(ye404List != null){	
				for(YE404VO vo : ye404List){
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						eRecordVO.setE120(String.valueOf(vo.get기부명세_공제대상기부금()+vo.get기부명세_기부장려금()+vo.get기부명세_기타()  + StringUtil.strPaserInt(eRecordVO.getE120()) ));
					}else{
						eRecordVO.setE135(String.valueOf(vo.get기부명세_공제대상기부금()+vo.get기부명세_기부장려금()+vo.get기부명세_기타() + StringUtil.strPaserInt(eRecordVO.getE135()) ));
					}
				}
			}
		}
		//인적사항 4
		else if((dIdx % 5) == 4){
			eRecordVO.setE136(ye001VO.get가족관계());
			eRecordVO.setE137(ye001VO.get내외국인());
			eRecordVO.setE138(ye001VO.get성명().toUpperCase());
			
			String 개인식별번호 = SecurityUtil.getinstance().aesDecode(ye001VO.get개인식별번호());
			eRecordVO.setE139(개인식별번호);
			eRecordVO.setE140(ye001VO.get기본공제());
			eRecordVO.setE141(ye001VO.get장애인());
			eRecordVO.setE142(ye001VO.get부녀자());
			eRecordVO.setE143(ye001VO.get경로우대());
			eRecordVO.setE144(ye001VO.get한부모());
			eRecordVO.setE145(ye001VO.get출산입양());
			eRecordVO.setE146(ye001VO.get자녀());
			eRecordVO.setE147(ye001VO.get교육비공제());
					
			/* 보험료  */
			if(ye401List != null){
				for(YE401VO vo : ye401List){
					
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//보장성
						if(StringUtils.equals("1", vo.get보험구분코드())){
							eRecordVO.setE150(String.valueOf(vo.get납입금액()  + StringUtil.strPaserInt(eRecordVO.getE150()) ));
						}					
						//장애인보장성
						else if(StringUtils.equals("2", vo.get보험구분코드())){
							eRecordVO.setE151(String.valueOf(vo.get납입금액()  + StringUtil.strPaserInt(eRecordVO.getE151()) ));
						}
					}else{
						//보장성
						if(StringUtils.equals("1", vo.get보험구분코드())){
							eRecordVO.setE166(String.valueOf(vo.get납입금액()  + StringUtil.strPaserInt(eRecordVO.getE166()) ));
						}					
						//장애인보장성
						else if(StringUtils.equals("2", vo.get보험구분코드())){
							eRecordVO.setE167(String.valueOf(vo.get납입금액()  + StringUtil.strPaserInt(eRecordVO.getE167()) ));
						}
					}
				}
			}
			
			/* 의료비 */
			if(ye402List != null){
				for(YE402VO vo : ye402List){
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//장애인
						if(StringUtils.equals("1", ye001VO.get장애인()) || StringUtils.equals("2", ye001VO.get장애인()) || StringUtils.equals("3", ye001VO.get장애인())){
							eRecordVO.setE154(String.valueOf(vo.get지출액()  + StringUtil.strPaserInt(eRecordVO.getE154()) ));
						}else{							
							//난임
							if(StringUtils.equals("3", vo.get의료비유형())){
								eRecordVO.setE153(String.valueOf(vo.get지출액()  + StringUtil.strPaserInt(eRecordVO.getE153()) ));
							}else{
								eRecordVO.setE152(String.valueOf(vo.get지출액()  + StringUtil.strPaserInt(eRecordVO.getE152()) ));
							}
						}
						
						
					}else{
						//장애인
						if(StringUtils.equals("1", ye001VO.get장애인()) || StringUtils.equals("2", ye001VO.get장애인()) || StringUtils.equals("3", ye001VO.get장애인())){
							eRecordVO.setE170(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE170()) ));
						}else{
							//난임
							if(StringUtils.equals("3", vo.get의료비유형())){
								eRecordVO.setE169(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE169()) ));
							}else{
								eRecordVO.setE168(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE168()) ));
							}
						}
					}
				}
			}
			
			/* 교육비 */
			if(ye403List != null){						
				for(YE403VO vo : ye403List){
					
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//장애인
						if(StringUtils.equals("5", vo.get공제종류코드())){
							eRecordVO.setE156(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비()  + StringUtil.strPaserInt(eRecordVO.getE156()) ));
						}else{
							eRecordVO.setE155(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비()  + StringUtil.strPaserInt(eRecordVO.getE155()) ));
						}							
					}else{
						//장애인
						if(StringUtils.equals("5", vo.get공제종류코드())){
							eRecordVO.setE172(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비()  + StringUtil.strPaserInt(eRecordVO.getE172()) ));
						}else{
							eRecordVO.setE171(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비()  + StringUtil.strPaserInt(eRecordVO.getE171()) ));
						}
					}
					
				}
			}
			
			/* 신용카드 */		
			if(ye108List != null){	
				for(YE108VO vo : ye108List){
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						eRecordVO.setE157(String.valueOf(vo.get신용카드()  + StringUtil.strPaserInt(eRecordVO.getE157()) ));
						eRecordVO.setE158(String.valueOf(vo.get직불_선불카드()  + StringUtil.strPaserInt(eRecordVO.getE158()) ));
						eRecordVO.setE159(String.valueOf(vo.get현금영수증()  + StringUtil.strPaserInt(eRecordVO.getE159()) ));
						eRecordVO.setE160(String.valueOf(vo.get도서공연()+vo.get도서공연_직불카드()+vo.get도서공연_현금영수증()  + StringUtil.strPaserInt(eRecordVO.getE160()) ));
						eRecordVO.setE161(String.valueOf(vo.get전통시장()  + StringUtil.strPaserInt(eRecordVO.getE161()) ));
						eRecordVO.setE162(String.valueOf(vo.get대중교통() + StringUtil.strPaserInt(eRecordVO.getE162()) ));						
					}else{
						eRecordVO.setE173(String.valueOf(vo.get신용카드() + StringUtil.strPaserInt(eRecordVO.getE173()) ));
						eRecordVO.setE174(String.valueOf(vo.get직불_선불카드()  + StringUtil.strPaserInt(eRecordVO.getE174()) ));
						eRecordVO.setE175(String.valueOf(vo.get도서공연()+vo.get도서공연_직불카드()+vo.get도서공연_현금영수증()  + StringUtil.strPaserInt(eRecordVO.getE175()) ));
						eRecordVO.setE176(String.valueOf(vo.get전통시장()  + StringUtil.strPaserInt(eRecordVO.getE176()) ));
						eRecordVO.setE177(String.valueOf(vo.get대중교통() + StringUtil.strPaserInt(eRecordVO.getE177()) ));
					}
				}
			}
			
			/* 기부금 */
			if(ye404List != null){	
				for(YE404VO vo : ye404List){
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						eRecordVO.setE163(String.valueOf(vo.get기부명세_공제대상기부금()+vo.get기부명세_기부장려금()+vo.get기부명세_기타()  + StringUtil.strPaserInt(eRecordVO.getE163()) ));
					}else{
						eRecordVO.setE178(String.valueOf(vo.get기부명세_공제대상기부금()+vo.get기부명세_기부장려금()+vo.get기부명세_기타() + StringUtil.strPaserInt(eRecordVO.getE178()) ));
					}
				}
			}
		}
		//인적사항 5
		else if((dIdx % 5) == 0){
			eRecordVO.setE179(ye001VO.get가족관계());
			eRecordVO.setE180(ye001VO.get내외국인());
			eRecordVO.setE181(ye001VO.get성명().toUpperCase());
			
			String 개인식별번호 = SecurityUtil.getinstance().aesDecode(ye001VO.get개인식별번호());
			eRecordVO.setE182(개인식별번호);
			eRecordVO.setE183(ye001VO.get기본공제());
			eRecordVO.setE184(ye001VO.get장애인());
			eRecordVO.setE185(ye001VO.get부녀자());
			eRecordVO.setE186(ye001VO.get경로우대());
			eRecordVO.setE187(ye001VO.get한부모());
			eRecordVO.setE188(ye001VO.get출산입양());
			eRecordVO.setE189(ye001VO.get자녀());
			eRecordVO.setE190(ye001VO.get교육비공제());
					
			/* 보험료  */
			if(ye401List != null){
				for(YE401VO vo : ye401List){
					
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//보장성
						if(StringUtils.equals("1", vo.get보험구분코드())){
							eRecordVO.setE193(String.valueOf(vo.get납입금액() + StringUtil.strPaserInt(eRecordVO.getE193()) ));
						}					
						//장애인보장성
						else if(StringUtils.equals("2", vo.get보험구분코드())){
							eRecordVO.setE194(String.valueOf(vo.get납입금액() + StringUtil.strPaserInt(eRecordVO.getE194()) ));
						}
					}else{
						//보장성
						if(StringUtils.equals("1", vo.get보험구분코드())){
							eRecordVO.setE209(String.valueOf(vo.get납입금액() + StringUtil.strPaserInt(eRecordVO.getE209()) ));
						}					
						//장애인보장성
						else if(StringUtils.equals("2", vo.get보험구분코드())){
							eRecordVO.setE210(String.valueOf(vo.get납입금액() + StringUtil.strPaserInt(eRecordVO.getE210()) ));
						}
					}
				}
			}
			
			/* 의료비 */
			if(ye402List != null){
				for(YE402VO vo : ye402List){
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//장애인
						if(StringUtils.equals("1", ye001VO.get장애인()) || StringUtils.equals("2", ye001VO.get장애인()) || StringUtils.equals("3", ye001VO.get장애인())){
							eRecordVO.setE197(String.valueOf(vo.get지출액()));
						}else{							
							//난임
							if(StringUtils.equals("3", vo.get의료비유형())){
								eRecordVO.setE196(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE196())  ));
							}else{
								eRecordVO.setE195(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE195()) ));
							}
						}
						
						
					}else{
						//장애인
						if(StringUtils.equals("1", ye001VO.get장애인()) || StringUtils.equals("2", ye001VO.get장애인()) || StringUtils.equals("3", ye001VO.get장애인())){
							eRecordVO.setE213(String.valueOf(vo.get지출액()  + StringUtil.strPaserInt(eRecordVO.getE213()) ));
						}else{
							//난임
							if(StringUtils.equals("3", vo.get의료비유형())){
								eRecordVO.setE212(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE212())  ));
							}else{
								eRecordVO.setE211(String.valueOf(vo.get지출액() + StringUtil.strPaserInt(eRecordVO.getE211()) ));
							}
						}
					}
				}
			}
			
			/* 교육비 */
			if(ye403List != null){						
				for(YE403VO vo : ye403List){
					
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						//장애인
						if(StringUtils.equals("5", vo.get공제종류코드())){
							eRecordVO.setE199(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비() + StringUtil.strPaserInt(eRecordVO.getE199()) ));
						}else{
							eRecordVO.setE198(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비() + StringUtil.strPaserInt(eRecordVO.getE198()) ));
						}							
					}else{
						//장애인
						if(StringUtils.equals("5", vo.get공제종류코드())){
							eRecordVO.setE215(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비() + StringUtil.strPaserInt(eRecordVO.getE215()) ));
						}else{
							eRecordVO.setE214(String.valueOf(vo.get공납금()+vo.get교복구입비()+vo.get체험학습비() + StringUtil.strPaserInt(eRecordVO.getE214()) ));
						}
					}
					
				}
			}
			
			/* 신용카드 */		
			if(ye108List != null){	
				for(YE108VO vo : ye108List){
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						eRecordVO.setE200(String.valueOf(vo.get신용카드() + StringUtil.strPaserInt(eRecordVO.getE200()) ));
						eRecordVO.setE201(String.valueOf(vo.get직불_선불카드() + StringUtil.strPaserInt(eRecordVO.getE201()) ));
						eRecordVO.setE202(String.valueOf(vo.get현금영수증() + StringUtil.strPaserInt(eRecordVO.getE202()) ));
						eRecordVO.setE203(String.valueOf(vo.get도서공연()+vo.get도서공연_직불카드()+vo.get도서공연_현금영수증() + StringUtil.strPaserInt(eRecordVO.getE203()) ));
						eRecordVO.setE204(String.valueOf(vo.get전통시장() + StringUtil.strPaserInt(eRecordVO.getE204()) ));
						eRecordVO.setE205(String.valueOf(vo.get대중교통() + StringUtil.strPaserInt(eRecordVO.getE205()) ));						
					}else{
						eRecordVO.setE216(String.valueOf(vo.get신용카드() + StringUtil.strPaserInt(eRecordVO.getE216()) ));
						eRecordVO.setE217(String.valueOf(vo.get직불_선불카드() + StringUtil.strPaserInt(eRecordVO.getE217()) ));
						eRecordVO.setE218(String.valueOf(vo.get도서공연()+vo.get도서공연_직불카드()+vo.get도서공연_현금영수증() + StringUtil.strPaserInt(eRecordVO.getE218()) ));
						eRecordVO.setE219(String.valueOf(vo.get전통시장()  + StringUtil.strPaserInt(eRecordVO.getE219()) ));
						eRecordVO.setE220(String.valueOf(vo.get대중교통()  + StringUtil.strPaserInt(eRecordVO.getE220()) ));
					}
				}
			}
			
			/* 기부금 */
			if(ye404List != null){	
				for(YE404VO vo : ye404List){
					//국세청
					if(StringUtils.equals("0", vo.get자료구분코드()) || StringUtils.equals("1", vo.get자료구분코드())){
						eRecordVO.setE206(String.valueOf(vo.get기부명세_공제대상기부금()+vo.get기부명세_기부장려금()+vo.get기부명세_기타()  + StringUtil.strPaserInt(eRecordVO.getE206()) ));
					}else{
						eRecordVO.setE221(String.valueOf(vo.get기부명세_공제대상기부금()+vo.get기부명세_기부장려금()+vo.get기부명세_기타()  + StringUtil.strPaserInt(eRecordVO.getE221()) ));
					}
				}
			}
		}
	}
	
	
}
