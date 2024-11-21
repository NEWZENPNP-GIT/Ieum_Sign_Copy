package com.ezsign.paystub.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ezsign.framework.mail.MailVO;
import com.ezsign.framework.mail.MultiPartEmail;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizGrpVO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.biztalk.dao.BizTalkDAO;
import com.ezsign.biztalk.vo.BizTalkKKOVO;
import com.ezsign.content.dao.CabinetDAO;
import com.ezsign.content.dao.ContentDAO;
import com.ezsign.content.service.ContentService;
import com.ezsign.content.vo.CabinetVO;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpLogVO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.fcm.service.PushSendService;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.ExcelUtil;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.util.XmlNewzenParser;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.paystub.dao.PaystubDAO;
import com.ezsign.paystub.service.PaystubService;
import com.ezsign.paystub.vo.PaystubDataVO;
import com.ezsign.paystub.vo.PaystubVO;
import com.ezsign.point.dao.PointDAO;
import com.ezsign.point.vo.PointLogVO;
import com.ezsign.point.vo.PointVO;
import com.jarvis.pdf.util.ControlUtil;
import com.jarvis.pdf.util.FieldUtil;
import com.jarvis.pdf.vo.FieldVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;


@Service("paystubService")
public class PaystubServiceImpl extends AbstractServiceImpl implements PaystubService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="paystubDAO")
	private PaystubDAO paystubDAO;

	@Resource(name="contentService")
	private ContentService contentService;

	@Resource(name="bizDAO")
	private BizDAO bizDAO;

	@Resource(name="empDAO")
	private EmpDAO empDAO;

	@Resource(name="contentDAO")
	private ContentDAO contentDAO;

	@Resource(name="cabinetDAO")
	private CabinetDAO cabinetDAO;

	@Resource(name="bizTalkDAO")
	private BizTalkDAO bizTalkDAO;
	
	@Resource(name="pushSendService")
	private PushSendService pushSendService;
	
	@Resource(name="pointDAO")
	private PointDAO pointDAO;
	
	@Override
	public List<PaystubVO> getPaystubList(PaystubVO vo) {
		return paystubDAO.getPaystubList(vo);
	}
	
	@Override
	public PaystubVO getPayStubPayDate(PaystubVO vo) {
		return paystubDAO.getPayStubPayDate(vo);
	}
	
	@Override
	public int getPaystubListCount(PaystubVO vo) {
		return paystubDAO.getPaystubListCount(vo);
	}

	@Override
	public int delPaystub(PaystubVO vo)  throws Exception {
		int result 					= 0;
		List<ContentVO> contentList = new ArrayList<>();
		String[] list 				= StringUtil.split(vo.getFileId(), "|");

        for (String s : list) {
            logger.info(s);
            if (s.length() > 10) {
                String[] data = StringUtil.split(s, "-");
                PaystubVO dataVO = new PaystubVO();
                dataVO.setBizId(vo.getBizId());
                dataVO.setUserId(data[0]);
                dataVO.setPayMonth(data[1]);
                dataVO.setPayType(data[2]);
                dataVO.setPayDate(data[3]);
                dataVO.setInsDate(data[4]);
                dataVO.setFileId(data[5]);
                paystubDAO.delPaystub(dataVO);
                paystubDAO.delPaystubData(dataVO);
                ContentVO fileVO = new ContentVO();
                fileVO.setFileId(dataVO.getFileId());
                contentList.add(fileVO);
                result++;
            }
        }
		// 급여명세서 파일 및 콘텐츠 삭제처리
		contentService.deleteContent(contentList);
		return result;
	}

	@Override
	public ContentVO getPaystubPDF(PaystubVO vo) {
		ContentVO contentVO = new ContentVO();
		CabinetVO cabinetVO = new CabinetVO();

		logger.info("getPaystubPDF : "+vo.getFileId());
		// 콘텐츠에서 정보 추출	
		contentVO.setFileId(vo.getFileId());
		ContentVO resultVO = contentDAO.getContent(contentVO);
		if (resultVO == null) return null;
		
		// 저장위치
		String filePath 		  = resultVO.getFilePath();
		cabinetVO.setCabinetId(resultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO == null) return null;

		String userDate = paystubDAO.getPaystubFileUserDate(vo);
		if (userDate.isEmpty()) userDate = System.getProperty("pdf.password.default");
		
		resultVO.setFilePath(cabinetResultVO.getCabinetPath()+filePath);
		resultVO.setInsDate(userDate);
		return resultVO;
	}
	
	@Override
	public ContentVO getPaystubPDFView(PaystubVO vo, SessionVO loginVO) {
		ContentVO contentVO	= new ContentVO();
		CabinetVO cabinetVO	= new CabinetVO();

		logger.info("getPaystubPDF : "+vo.getFileId());
		// 콘텐츠에서 정보 추출	
		contentVO.setFileId(vo.getFileId());
		ContentVO resultVO = contentDAO.getContent(contentVO);
		if (resultVO == null) return null;
		
		// 저장위치
		String filePath = resultVO.getFilePath();
		cabinetVO.setCabinetId(resultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO == null) return null;

		String userDate = paystubDAO.getPaystubFileUserDate(vo);
		if (userDate.isEmpty()) userDate = System.getProperty("pdf.password.default");
		
		resultVO.setFilePath(cabinetResultVO.getCabinetPath()+filePath);
		resultVO.setInsDate(userDate);
		
		vo.setStartPage(0);
		vo.setEndPage(1);
		List<PaystubVO> resEmp = paystubDAO.getPaystubList(vo);
		logger.info("resultSize : "+resEmp.size());
		if (!resEmp.isEmpty() && resEmp.get(0) != null && StringUtil.isNotNull(resEmp.get(0).getUserId())) {
			//급여명세서 조회 로그
			List<String> userIdList = new ArrayList<>();
			userIdList.add(resEmp.get(0).getUserId());

			EmpLogVO logVO = new EmpLogVO();
			logVO.setInsUserId(loginVO.getUserId());
			logVO.setUserIdList(userIdList);
			logVO.setLogType("003");
			logVO.setIpAddr(loginVO.getIpAddr());
			logVO.setBizId(loginVO.getBizId());
			empDAO.insEmpLog(logVO);
		}
		return resultVO;
	}
	
	@Override
	public ContentVO createPaystubXML(ContentVO vo) throws Exception {
		ContentVO contentVO	  = new ContentVO();
		CabinetVO cabinetVO	  = new CabinetVO();
		List<String> talkList = new ArrayList<>();
		
		// 콘텐츠에서 정보 추출
		contentVO.setFileId(vo.getFileId());
		ContentVO resultVO = contentDAO.getContent(contentVO);
		if (resultVO == null) { logger.info("[createPaystubXML] 콘텐츠 정보가 존재하지 않습니다."); return null; }
		
		// 저장위치
		cabinetVO.setClassId(resultVO.getClassId());
		cabinetVO.setCabinetId(resultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);

		if (cabinetResultVO == null) { logger.info("[createPaystubXML] 캐비넷 정보가 존재하지 않습니다."); return null; }
		
		String originalPath = cabinetResultVO.getCabinetPath()+resultVO.getFilePath()+resultVO.getFileName();
		String targetName   = StringUtil.getUUID()+".xml";
		String targetPath   = MultipartFileUtil.getSystemTempPath()+targetName;
		
		logger.info(originalPath);
		logger.info(targetPath);
		// 사용할 PDF복사
		FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
		if (FileUtil.fileCopy(originalPath, targetPath)) {
			// XML데이터 파싱 (급여명세서 데이타)
			String xmlData 		= FileUtil.readFile(targetPath);
			List<BizVO> bizList = XmlNewzenParser.XMLtoCompany(xmlData);
			logger.info("bizList="+bizList.size());
			if (!bizList.isEmpty()) {
				BizVO bizVO = bizList.get(0);
				logger.info("[createPaystubXML]  사업자번호 -> "+bizVO.getBusinessNo());
				// 기업정보
				BizVO empBizVO = new BizVO();
				empBizVO.setBusinessNo(bizVO.getBusinessNo());
				List<BizVO> resultBizList  = bizDAO.getBizList(empBizVO);
				
				if (resultBizList != null && !resultBizList.isEmpty()) {
					BizVO resultBizVO			= resultBizList.get(0);
					String userId 	 			= "";
					String insDate	 			= DateUtil.getTimeStamp(7);
					List<PaystubDataVO> payList = XmlNewzenParser.XMLtoPaystub(xmlData);
					
					logger.info(payList.size());
					
					for (int i=0; i<payList.size(); i++) {
						PaystubDataVO dataVO = payList.get(i);
						
						// 사용자정보
						EmpVO empVO = new EmpVO();
						empVO.setBizId(resultBizVO.getBizId());
						empVO.setEmpNo(dataVO.getUserId());
						EmpVO empResultVO = empDAO.getEmpNo(empVO);
						if (empResultVO != null) {
							String loginId = empResultVO.getLoginId();
							if (!StringUtil.isNotNull(loginId) || loginId.isEmpty()) {
								logger.info("[createPaystubXML] userId=>"+dataVO.getUserId());
								boolean findTalkId = false;

								// 이미 중복된 사용자가 있는지 체크
                                for (String talkUserId : talkList) { if (talkUserId.equals(empResultVO.getUserId())) { findTalkId = true; } }
								
								if (StringUtil.NVL(empResultVO.getPhoneNum())) { logger.info("[createPaystubXML] "+empResultVO.getEmpName()+"님의 휴대폰번호가 없습니다."); findTalkId = false; }
								
								if (!findTalkId) {

									// 난수생성 업데이트 진행 
									String empNonce = StringUtil.getRandom(5);
									EmpVO empNonceVO = new EmpVO();
									empNonceVO.setBizId(resultBizVO.getBizId());
									empNonceVO.setUserId(empResultVO.getUserId());
									empNonceVO.setEmpNonce(empNonce);
									int empNonceCount = empDAO.updEmp(empNonceVO);
									if (empNonceCount > 0) {
										
										// 알림톡 설치 URL전송
										String content = "";
										String bizName = resultBizVO.getBizName();

										if (bizName == null || "".equals(bizName)) bizName = "뉴젠피앤피";
										
										content += bizName + "직원들을 위한 모바일 앱 \"Candy\" 설치 안내입니다. \n";
										content += "아래 설치URL에서 Candy앱 설치를 진행해주시기 바랍니다. \n";
										content += " http://app.ieumsign.co.kr \n";
										
										BizTalkKKOVO kkoVO = new BizTalkKKOVO();
										kkoVO.setSubject("[뉴젠피엔피 * Candy]");
										kkoVO.setContent(content);
										kkoVO.setCallback(System.getProperty("biztalk.kko.callback_number"));
										kkoVO.setRecipientNum(empResultVO.getPhoneNum());
										kkoVO.setSenderKey(System.getProperty("biztalk.kko.sender_key"));
										kkoVO.setTemplateCode("contract003");
										kkoVO.setKkoBtnType("1");
										kkoVO.setKkoBtnInfo("Candy^WL^^");
										bizTalkDAO.insBizTalk(kkoVO);
										talkList.add(empResultVO.getUserId());
									}
								}
							}
							PaystubVO mstVO = new PaystubVO();
							mstVO.setServiceId(dataVO.getServiceId());
							mstVO.setBizId(empResultVO.getBizId());
							mstVO.setUserId(empResultVO.getUserId());
							mstVO.setPayMonth(dataVO.getPayMonth());
							mstVO.setPayType(dataVO.getPayType());
							mstVO.setPayDate(dataVO.getPayDate());
							mstVO.setInsDate(insDate);
							
							// 최초 한번 정의
							if (i == 0 || !userId.equals(empResultVO.getUserId())) paystubDAO.insPaystub(mstVO);

							PaystubDataVO mstDataVO = new PaystubDataVO();
							mstDataVO.setServiceId(dataVO.getServiceId());
							mstDataVO.setBizId(resultBizVO.getBizId());
							mstDataVO.setUserId(empResultVO.getUserId());
							mstDataVO.setPayMonth(dataVO.getPayMonth());
							mstDataVO.setPayType(dataVO.getPayType());
							mstDataVO.setPayDate(dataVO.getPayDate());
							mstDataVO.setInsDate(insDate);
							mstDataVO.setPayTaxType(dataVO.getPayTaxType());
							mstDataVO.setPayTaxCode(dataVO.getPayTaxCode());
							mstDataVO.setPayTaxName(dataVO.getPayTaxName());
							mstDataVO.setPayTaxAmt(dataVO.getPayTaxAmt());
							
							paystubDAO.insPaystubData(mstDataVO);
						} else 		return null;
						// 직원별 데이터 처리 조건
						userId = empResultVO.getUserId();
					}
				} else 		return null;
			}
		}
		return contentVO;
	}

	@Override
	public PaystubVO sendPaystubExcel(String bizId, String xlsPath, FileVO fileVO) {
		List<PaystubVO> paystubList = new ArrayList<>();
		PaystubVO result 			= new PaystubVO();
		logger.info("[sendPaystubExcel]  기업id -> " + bizId);
		try {
			// 급여명세서 파일 등록
			String filePath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
			File file 		= new File(xlsPath);
			if (!file.exists()) { logger.info("[sendPaystubExcel] XLS파일이 존재하지 않습니다."); return null; }
			
			ContentVO contentVO = new ContentVO();
			contentVO.setFileType(FileUtil.getFileExt(filePath));
			contentVO.setFileName(fileVO.getFileStreNm());
			contentVO.setOrgFileName(fileVO.getFileStreOriNm());
			contentVO.setFileSize(Long.toString(FileUtil.getFileSize(filePath)));
			contentVO.setFilePath(fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR);
			contentVO.setFileTitle(StringUtil.ReplaceAll(fileVO.getFileStreOriNm(), "."+fileVO.getFileExt(), ""));
			contentVO.setClassId(PaystubService.SAVE_CLASS_ID);
			contentVO.setFileVersion("0");
			contentVO.setParFileId("");
			contentVO.setEtcDesc("");
			contentVO.setCheckInOut("N");
			contentVO.setCheckUserId("");
			contentVO.setCheckCount("0");
			contentVO.setUseYn("Y");
			contentVO.setDelYn("N");
			
			int nResult = contentService.transContent(contentVO);
			
			if (ContentService.COMPLETED == nResult) {
				result.setServiceId(PaystubService.SERVICE_ID);
				result.setBizId(bizId);
				result.setFileId(contentVO.getFileId());
				logger.info("[sendPaystubExcel] 일반 급여 파일 생성");
			}
			
			// 엑셀파일 정보 읽기
			Workbook workbook = Workbook.getWorkbook(file);
			Sheet[] sheets 	  = workbook.getSheets();

			// 같은 엑셀파일 내에 중복데이터 체크하는 필드 추가
			ArrayList<String> checkPaystubDupList = new ArrayList<>();
			
			for (int s=0; s<sheets.length; s++) {
				
				Sheet sheet = sheets[s];
				int colNum  = sheet.getColumns();
				int rowNum  = sheet.getRows();
				logger.info("sheet=>"+ (s+1) + "/" + sheets.length + "번째 시트_" + sheet.getName() + "_" + colNum + "_" + rowNum);
				
				if (s == 0) { // 첫번째 시트만 가져오게 제어
					
					// 일반 엑셀 컬럼값 맵핑
					for (int r=3; r<rowNum; r++) {
						int colCnt = colNum;
						logger.info("max col count=>"+colCnt);
						
						String empNo	   = ExcelUtil.getCellValue(sheet.getCell(1, r));
						//String payType = ExcelUtil.getCellValue(sheet.getCell(2, r));  payType 테이블 컬럼 삭제 요청(22.03.21 김정인)
						String payType	   = "1";
						String payMonth	   = ExcelUtil.getCellValue(sheet.getCell(2, r));
						String payDate	   = ExcelUtil.getCellValue(sheet.getCell(3, r));
						String empName 	   = ExcelUtil.getCellValue(sheet.getCell(4, r));
						String payFormType = ExcelUtil.getCellValue(sheet.getCell(5, r));
						
						payMonth = StringUtil.StringReplace(payMonth);
						payDate  = StringUtil.StringReplace(payDate);
						
						if (StringUtil.isNull(empNo) || empNo.replaceAll(" ", "").isEmpty()) continue;
						logger.info("[sendPaystubExcel] 조회->" + empNo);	
						
						EmpVO empVO = new EmpVO();
						empVO.setBizId(bizId);
						empVO.setEmpNo(empNo);
						empVO.setEmpType("1");		//근로자만 조회
						EmpVO resultVO = empDAO.getEmpNo(empVO);

						if (resultVO == null) {
							logger.info("등록된 임직원이 아닙니다.");
							result.setPaystubNonce("-100");
							result.setEmpName(empName);
							result.setExcelRow(r);
							result.setExcelSheet(sheet.getName());
							result.setMessage("등록된 임직원이 아닙니다.");
							return result;
						}

						// 같은파일 내에 명세서 중복생성 방지 체크(23.04.27 김정인)
						String checkPaystubDupStr = resultVO.getUserId() + "-" + payMonth + "-" + payType + "-" + payDate;

						if (checkPaystubDupList.contains(checkPaystubDupStr)) {
							logger.info("[" + r + "행]"+"동일한 사번이 존재합니다.");
							result.setPaystubNonce("-200");
							result.setEmpName(empName);
							result.setExcelRow(r);
							result.setExcelSheet(sheet.getName());
							result.setMessage("[" + r + "행]"+"동일한 사번이 존재합니다.");
							return result;
						} else 	checkPaystubDupList.add(checkPaystubDupStr);

						
						if (!StringUtil.isNotNull(empName) || !StringUtil.isNotNull(payMonth) || !StringUtil.isNotNull(payDate)
							|| !StringUtil.isNotNull(payType) ) {
							logger.info(empVO.getEmpName()+"님의 필수입력항목이 없습니다.");
							result.setEmpName(empVO.getEmpName());
							result.setExcelRow(r);
							result.setExcelSheet(sheet.getName());
							return result;
						}

						result.setEmpName(resultVO.getEmpName());
						logger.info("[sendPaystubExcel] 대상->" + resultVO.getEmpName());
						
						PaystubVO paystubVO = new PaystubVO();
						paystubVO.setServiceId(PaystubService.SERVICE_ID);
						paystubVO.setBizName(resultVO.getBizName());
						paystubVO.setBizId(bizId);
						paystubVO.setUserId(resultVO.getUserId());
						paystubVO.setPayMonth(payMonth);
						paystubVO.setPayDate(payDate);
						paystubVO.setPayType(payType);
						paystubVO.setPayFormType(payFormType);
						
						int rownum = 1;
						
						// 계산근거 표시를 위한 공제항목 값 리스트 추가
						Map<String,String> payCalcEvidenceList = new HashMap<>(); 
						for (int i=0; i<colCnt; i++) {
						
							String payTaxType = ExcelUtil.getCellValue(sheet.getCell(i, 0));
							String payTaxName = ExcelUtil.getCellValue(sheet.getCell(i, 1));
							String payTaxAmt = ExcelUtil.getCellValue(sheet.getCell(i, r));

							if ("3".equals(payTaxType) && StringUtil.isNotNull(payTaxAmt)) {
								logger.info("col> " + i + "계산근거" +"-"+ payTaxName);
								payCalcEvidenceList.put(payTaxName,payTaxAmt);							
							}
						}					
						
						for (int i=0; i<colCnt; i++) {
							
							String payTaxType = ExcelUtil.getCellValue(sheet.getCell(i, 0));
							String payTaxName = ExcelUtil.getCellValue(sheet.getCell(i, 1));
							String payTaxAmt  = ExcelUtil.getCellValue(sheet.getCell(i, r));
	
							if (StringUtil.isNotNull(payTaxAmt) && (!("0".equals(payTaxAmt)))) {
							
								PaystubDataVO paystubDataVO = new PaystubDataVO();
								paystubDataVO.setServiceId(PaystubService.SERVICE_ID);
								paystubDataVO.setBizId(bizId);
								paystubDataVO.setBizName(paystubVO.getBizName());
								paystubDataVO.setUserId(resultVO.getUserId());
								paystubDataVO.setPayMonth(paystubVO.getPayMonth());
								paystubDataVO.setPayDate(paystubVO.getPayDate());
								paystubDataVO.setPayType(paystubVO.getPayType());
								
								// 근로일수 등 데이터 DB 처리
								if (i > 5 && i < 13) {
									paystubDataVO.setServiceId(PaystubService.SERVICE_ID);
									paystubDataVO.setBizId(bizId);
									paystubDataVO.setBizName(paystubVO.getBizName());
									paystubDataVO.setUserId(resultVO.getUserId());
									paystubDataVO.setPayMonth(paystubVO.getPayMonth());
									paystubDataVO.setPayDate(paystubVO.getPayDate());
									paystubDataVO.setPayType(paystubVO.getPayType());
									paystubDataVO.setPayTaxType("A01");
									paystubDataVO.setPayTaxCode("300" + (i-5));
									paystubDataVO.setPayTaxName(payTaxName);
									paystubDataVO.setPayTaxAmt(payTaxAmt);
									paystubDataVO.setPayTaxCalcEvidence("");

									logger.info("col> " + i + "근로일수등" +"-"+ payTaxName );
								} else if ("1".equals(payTaxType)) { // 1 : 수당
									paystubDataVO.setPayTaxType(payTaxType);
									paystubDataVO.setPayTaxCode("1" + StringUtil.lpad(Integer.toString(rownum), 3, "0"));
									paystubDataVO.setPayTaxName(payTaxName);
									paystubDataVO.setPayTaxAmt(payTaxAmt);
                                    paystubDataVO.setPayTaxCalcEvidence(payCalcEvidenceList.getOrDefault(payTaxName, ""));

									logger.info("col> " + i + "수당" +"-"+ payTaxName);
								} else if ("2".equals(payTaxType)) { // 2 : 공제
									paystubDataVO.setPayTaxType(payTaxType);
									paystubDataVO.setPayTaxCode("2" + StringUtil.lpad(Integer.toString(rownum), 3, "0"));
									paystubDataVO.setPayTaxName(payTaxName);
									paystubDataVO.setPayTaxAmt(payTaxAmt);
                                    paystubDataVO.setPayTaxCalcEvidence(payCalcEvidenceList.getOrDefault(payTaxName, ""));

									logger.info("col> " + i + "공제" +"-"+ payTaxName);
								}					
								paystubVO.addData(paystubDataVO);
								rownum++;
							}
						}
						paystubList.add(paystubVO);
					}
				} else 		logger.info("2번째 시트부터는 아무일도 하지 않습니다.");
			}

			int count = 0;
			ArrayList<String> seqIds = new ArrayList<>();
			// 급여정보 등록
            for (PaystubVO paystubVO : paystubList) {
                logger.info("[sendPaystubExcel] 처리->" + paystubVO.getBizName());
                logger.info("[sendPaystubExcel] 대상 -> " + paystubVO.getUserId());

                String insDate = DateUtil.getTimeStamp(7);
                paystubVO.setServiceId(PaystubService.SERVICE_ID);
                paystubVO.setBizId(paystubVO.getBizId());
                paystubVO.setBizName(paystubVO.getBizName());
                paystubVO.setUserId(paystubVO.getUserId());
                paystubVO.setPayMonth(paystubVO.getPayMonth());
                paystubVO.setPayDate(paystubVO.getPayDate());
                paystubVO.setTransType("N");
                paystubVO.setInsDate(insDate);
                // 급여명세서 등록
                seqIds.add(paystubDAO.insPaystub(paystubVO));

                //PUSH 알림.
                count++;
                List<PaystubDataVO> tranDataList = paystubVO.getDataList();

                // 급여상세정보
                for (PaystubDataVO tranData : tranDataList) {
                    // 수당 공제 항목 등록
                    if (StringUtil.isNotNull(tranData.getPayTaxType())) {
                        tranData.setInsDate(insDate);
                        paystubDAO.insPaystubData(tranData);
                    }
                }
            }
			
			logger.info("[sendPaystubExcel] 처리건수->"+count);
			result.setPaystubNonce(Integer.toString(count));
			result.setSeqIds(seqIds);
			workbook.close();
			file.delete();
		} catch (Exception ex) { ex.printStackTrace(); }
		return result;
	}


	@Override
	public PaystubVO sendPayCarePaystubExcel(String bizId, String xlsPath, FileVO fileVO) {
		List<PaystubVO> paystubList = new ArrayList<PaystubVO>();
		PaystubVO result = new PaystubVO();
		/*
		logger.info("[sendPayCarePaystubExcel]  기업id -> " + bizId);
		
		try {
			
			// 급여명세서 파일 등록
			String filePath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
			
			File file = new File(xlsPath);
			if (!file.exists()) {
				logger.info("[sendPayCarePaystubExcel] XLS파일이 존재하지 않습니다.");
				return null;
			}
			
			ContentVO contentVO = new ContentVO();
			contentVO.setFileType(FileUtil.getFileExt(filePath));
			contentVO.setFileName(fileVO.getFileStreNm());
			contentVO.setOrgFileName(fileVO.getFileStreOriNm());
			contentVO.setFileSize(Long.toString(FileUtil.getFileSize(filePath)));
			contentVO.setFilePath(fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR);
			contentVO.setFileTitle(StringUtil.ReplaceAll(fileVO.getFileStreOriNm(), "."+fileVO.getFileExt(), ""));
			contentVO.setClassId(PaystubService.SAVE_CLASS_ID);
			contentVO.setFileVersion("0");
			contentVO.setParFileId("");
			contentVO.setEtcDesc("");
			contentVO.setCheckInOut("N");
			contentVO.setCheckUserId("");
			contentVO.setCheckCount("0");
			contentVO.setUseYn("Y");
			contentVO.setDelYn("N");
			
			int nResult = contentService.transContent(contentVO);
			
			if(ContentService.COMPLETED==nResult) {
				result.setServiceId(PaystubService.SERVICE_ID);
				result.setBizId(bizId);
				result.setFileId(contentVO.getFileId());
				
				logger.info("[sendPayCarePaystubExcel] 페이케어 급여 파일 생성");
			}
			
			// 엑셀파일 정보 읽기
			Workbook workbook = Workbook.getWorkbook(file);
			
			Sheet[] sheets = workbook.getSheets();
			
			for (int s=0; s < sheets.length; s++) {
				Sheet sheet = sheets[s];
				int colNum = sheet.getColumns();
				int rowNum = sheet.getRows();
				
				logger.info("sheet=>" + sheet.getName() + "_" + colNum + "_" + rowNum);
				
				// paycare 엑셀 컬럼값 맵핑
				for (int r=2; r < rowNum; r++) {
					int colCnt = colNum;
					logger.info("max col count=>"+colCnt);
					
					String empNo = ExcelUtil.getCellValue(sheet.getCell(17, r));
					String empName = ExcelUtil.getCellValue(sheet.getCell(18, r));
					String payMonth = ExcelUtil.getCellValue(sheet.getCell(3, r));
					String payDate = ExcelUtil.getCellValue(sheet.getCell(7, r));
					String payType = ExcelUtil.getCellValue(sheet.getCell(2, r));
					
					payMonth = StringUtil.StringReplace(payMonth);
					payDate = StringUtil.StringReplace(payDate);
					
					if (!StringUtil.isNotNull(empNo)) break;
					logger.info("[sendPayCarePaystubExcel] 조회->" + empNo);	
					
					EmpVO empVO = new EmpVO();
					empVO.setBizId(bizId);
					empVO.setEmpNo(empNo);
					EmpVO resultVO = empDAO.getEmpNo(empVO);
					if (resultVO == null) { 
						
						logger.info("등록된 임직원이 아닙니다.");
						result.setPaystubNonce("-100");
						result.setEmpName(empName);
						result.setExcelRow(r);
						result.setExcelSheet(sheet.getName());
						result.setMessage("등록된 임직원이 아닙니다.");
						return result;
					}
					
					if(!StringUtil.isNotNull(empName) 
						|| !StringUtil.isNotNull(payMonth) || !StringUtil.isNotNull(payDate)
						|| !StringUtil.isNotNull(payType) ) {
						logger.info(empVO.getEmpName()+"님의 필수입력항목이 없습니다.");
						result.setEmpName(empVO.getEmpName());
						result.setExcelRow(r);
						result.setExcelSheet(sheet.getName());
						return result;
					}
					
					if ("급상여".equals(payType)) {
						payType = "2";
					}else if ("상여".equals(payType)) {
						payType = "3";
					} else {
						payType ="1";
					}
					
					result.setEmpName(resultVO.getEmpName());
					logger.info("[sendPayCarePaystubExcel] 대상->" + resultVO.getEmpName());
					
					PaystubVO paystubVO = new PaystubVO();
					paystubVO.setServiceId(PaystubService.SERVICE_ID);
					paystubVO.setBizName(resultVO.getBizName());
					paystubVO.setBizId(bizId);
					paystubVO.setUserId(resultVO.getUserId());
					paystubVO.setPayMonth(payMonth);
					paystubVO.setPayDate(payDate);
					paystubVO.setPayType(payType);
					
					int rownum = 1;
					
					for (int i=0; i < colCnt; i++) {
						
						String payTaxType = ExcelUtil.getCellValue(sheet.getCell(i, 0));
						String payTaxName = ExcelUtil.getCellValue(sheet.getCell(i, 1));
						String payTaxAmt = ExcelUtil.getCellValue(sheet.getCell(i, r));
						logger.info("col> "+ colCnt +"-"+ payTaxName);
						
						PaystubDataVO paystubDataVO = new PaystubDataVO();
						paystubDataVO.setServiceId(PaystubService.SERVICE_ID);
						paystubDataVO.setBizId(bizId);
						paystubDataVO.setBizName(paystubVO.getBizName());
						paystubDataVO.setUserId(resultVO.getUserId());
						paystubDataVO.setPayMonth(paystubVO.getPayMonth());
						paystubDataVO.setPayDate(paystubVO.getPayDate());
						paystubDataVO.setPayType(paystubVO.getPayType());
						
						if ("1".equals(payTaxType)) { // 1: 수당
							paystubDataVO.setPayTaxType(payTaxType);
							paystubDataVO.setPayTaxCode("1" + StringUtil.lpad(Integer.toString(rownum), 3, "0"));
							paystubDataVO.setPayTaxName(payTaxName);
							paystubDataVO.setPayTaxAmt(payTaxAmt);
						} else if ("2".equals(payTaxType)) { // 2 : 공제
							paystubDataVO.setPayTaxType(payTaxType);
							paystubDataVO.setPayTaxCode("2" + StringUtil.lpad(Integer.toString(rownum), 3, "0"));
							paystubDataVO.setPayTaxName(payTaxName);
							paystubDataVO.setPayTaxAmt(payTaxAmt);
						}
						
						paystubVO.addData(paystubDataVO);
						rownum++;
					}
					paystubList.add(paystubVO);
				}
			}
			
			int count = 0;
			// 급여정보 등록
			for (int i=0; i < paystubList.size(); i++) {
				PaystubVO paystubVO = paystubList.get(i);
				
				logger.info("[sendPayCarePaystubExcel] 처리->" + paystubVO.getBizName());
				
				logger.info("[sendPayCarePaystubExcel] 사원 -> " + paystubVO.getBizId());
				logger.info("[sendPayCarePaystubExcel] 사원 -> " + paystubVO.getUserId());
				
				paystubVO.setServiceId(PaystubService.SERVICE_ID);
				paystubVO.setBizId(paystubVO.getBizId());
				paystubVO.setBizName(paystubVO.getBizName());
				paystubVO.setUserId(paystubVO.getUserId());
				paystubVO.setPayMonth(paystubVO.getPayMonth());
				paystubVO.setPayDate(paystubVO.getPayDate());
				paystubVO.setTransType("N");
				
				// 급여명세서 등록
				paystubDAO.insPaystub(paystubVO);
				count++;
				
				List<PaystubDataVO> tranDataList = paystubVO.getDataList();
				
				// 급여상세정보			
				for (int t=0; t < tranDataList.size(); t++) {
					
					PaystubDataVO tranData = tranDataList.get(t);
					// 수당 공제 항목 등록
					if (StringUtil.isNotNull(tranData.getPayTaxType())) {
						paystubDAO.insPaystubData(tranData);
					}
				}
			}
			
			logger.info("[sendPayCarePaystubExcel] 처리건수->"+count);
			result.setPaystubNonce(Integer.toString(count));
			workbook.close();
			
			file.delete();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		*/
		return result;
	}

	@Override
	public int createPaystubPDF(PaystubVO vo) throws Exception {
		
		PaystubVO paystubVO = new PaystubVO();
		ContentVO contentVO	= new ContentVO();
		CabinetVO cabinetVO	= new CabinetVO();
		int result			= 0;
		boolean useMobile	= false;

		// 콘텐츠에서 정보 추출	
		contentVO.setFileId(PaystubService.FILE_ID);
		ContentVO resultVO = contentDAO.getContent(contentVO);
		if (resultVO == null) { logger.info("[createPaystubPDF] 콘텐츠 정보가 존재하지 않습니다."); return 0; }
		
		// 저장위치
		cabinetVO.setClassId(resultVO.getClassId());
		cabinetVO.setCabinetId(resultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO == null) { logger.info("[createPaystubPDF] 캐비넷 정보가 존재하지 않습니다."); return 0; }
		
		String originalPdfPath = cabinetResultVO.getCabinetPath()+resultVO.getFilePath()+resultVO.getFileName();
		String targetPdfName   = StringUtil.getUUID()+".pdf";
		String targetPdfPath   = MultipartFileUtil.getSystemTempPath()+targetPdfName;
		String fontProperty	   = "pdf.font.nanumsquare";
		
		logger.info("originalPdfPath => " + originalPdfPath);
		logger.info("targetPdfPath => " + targetPdfPath);
		// 사용할 PDF복사
		FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
		paystubVO.setServiceId(PaystubService.SERVICE_ID);
		paystubVO.setBizId(vo.getBizId());
		paystubVO.setSeqId(vo.getSeqId());
		paystubVO.setStartPage(0);
		paystubVO.setEndPage(10000);

		//모바일인 경우 조회조건 추가
		BizVO searchVO = new BizVO();
		searchVO.setBizId(vo.getBizId());
		BizVO bizVO = bizDAO.getBiz(searchVO);
		if (bizVO.getLoginId() != null && (bizVO.getLoginId().contains("@bizlounge.com") || bizVO.getLoginId().contains("@candycash.com"))) {
			paystubVO.setUserId(vo.getUserId());
			paystubVO.setPayMonth(vo.getPayMonth());
			useMobile = true;
		}

		List<PaystubVO> paystubList = paystubDAO.getPaystubList(paystubVO);
		PointVO pointVO 			= new PointVO();
		pointVO.setBizId(vo.getBizId());
		PointVO resultPointVO 		= pointDAO.getPoint(pointVO);

		if (resultPointVO == null) { logger.error("[sendDocument] 포인트정보가 존재하지 않습니다."); return -1; }

		int reducePoint = 0;
		BizGrpVO grpVO  = new BizGrpVO();
		grpVO.setBizId(vo.getBizId());
		List<BizGrpVO> pointList = bizDAO.getBizGrp(grpVO);
		if (pointList == null) {
			logger.error("[sendDocument] 기업정보가 존재하지 않습니다.");
			return -2;
		} else {
			//현재 보유중인 포인트
			int point = resultPointVO.getCurPoint();
			//전자문서 전송 차감 포인트
			if (StringUtil.isNotNull(pointList.get(0).getPayPoint())) reducePoint = Integer.parseInt(pointList.get(0).getPayPoint());

			if ((point - (reducePoint*paystubList.size())) < 0) {
				logger.error("[sendDocument] 잔여 포인트가 존재하지 않습니다. 현재포인트 : <"+point+">, 차감 포인트 : <"+(reducePoint*paystubList.size())+">");
				return -3;
			}
		}


        for (PaystubVO paystubResultVO : paystubList) {
            logger.info("[createPaystubPDF] userId=>" + paystubResultVO.getUserId());
            EmpVO empVO = new EmpVO();
            empVO.setBizId(vo.getBizId());
            empVO.setUserId(paystubResultVO.getUserId());
            EmpVO resultEmpVO = empDAO.getEmp(empVO);
            if (resultEmpVO != null) {
                List<FieldVO> fieldListVO = new ArrayList<>();
                int CalcEvidenceCount 	  = 0; // 계산방법 인덱스 추가

                // 급여대상자 인사정보
                fieldListVO.add(FieldUtil.setFieldData("pay_month", paystubResultVO.getPayMonthPrint()));
                fieldListVO.add(FieldUtil.setFieldData("biz_name", paystubResultVO.getBizName()));
                fieldListVO.add(FieldUtil.setFieldData("emp_no", resultEmpVO.getEmpNo()));
                fieldListVO.add(FieldUtil.setFieldData("emp_name", resultEmpVO.getEmpName()));
                fieldListVO.add(FieldUtil.setFieldData("position_name", resultEmpVO.getPositionName()));
                fieldListVO.add(FieldUtil.setFieldData("dept_name", resultEmpVO.getDeptName()));
                fieldListVO.add(FieldUtil.setFieldData("step_name", resultEmpVO.getStepName()));
                String joinDateFormat = resultEmpVO.getJoinDate();
                joinDateFormat 		  = StringUtil.substring(joinDateFormat, 0, 4) + "-" + StringUtil.substring(joinDateFormat, 4, 6) + "-" + StringUtil.substring(joinDateFormat, 6, 8);
                fieldListVO.add(FieldUtil.setFieldData("join_date", joinDateFormat));
                fieldListVO.add(FieldUtil.setFieldData("pay_day", paystubResultVO.getPayDatePrint()));

                PaystubDataVO paystubDataVO = new PaystubDataVO();

                paystubDataVO.setServiceId(paystubResultVO.getServiceId());
                paystubDataVO.setBizId(paystubResultVO.getBizId());
                paystubDataVO.setUserId(paystubResultVO.getUserId());
                paystubDataVO.setPayMonth(paystubResultVO.getPayMonth());
                paystubDataVO.setPayType(paystubResultVO.getPayType());
                paystubDataVO.setPayDate(paystubResultVO.getPayDate());
                paystubDataVO.setInsDate(paystubResultVO.getInsDate());

                paystubDataVO.setPayTaxType("1");
                List<PaystubDataVO> paystubPaymentListVO   = paystubDAO.getPaystubDataList(paystubDataVO);

                paystubDataVO.setPayTaxType("2");
                List<PaystubDataVO> paystubDeductionListVO = paystubDAO.getPaystubDataList(paystubDataVO);

                paystubDataVO.setPayTaxType("A01"); // 근로일수등 추가(21.11.16) // 사용자 지정 추가 (24.05.30)
                List<PaystubDataVO> paystubWorkListVO 	   = paystubDAO.getPaystubDataList(paystubDataVO);

                // 수당, 공제, 계산근거 항목수에 따라 양식 분기를 위한 위치 조정(23.04.14)
                // 양식 구분에 따른 Content 재조회 및 Form 변경
                if (!("".equals(paystubResultVO.getPayFormType()))) {
                    String formType = paystubResultVO.getPayFormType();

                    // 한 항목이라도 10개 이상인 경우 별지를 첨부한 양식으로 변경
                    if (paystubPaymentListVO.size() > 11 || paystubDeductionListVO.size() > 11 || paystubWorkListVO.size() > 11) formType = Integer.toString(Integer.parseInt(formType) + 4);

                    if (formType.length() == 1) formType = "0" + formType;

                    String changeFormFileId = PaystubService.PAY_FORM_FILE_ID + formType;

                    // 콘텐츠에서 정보 추출
                    contentVO.setFileId(changeFormFileId);
                    resultVO = contentDAO.getContent(contentVO);
                    if (resultVO == null) { logger.info("[createPaystubPDF] 콘텐츠 정보가 존재하지 않습니다."); return 0; }

                    // 저장위치
                    cabinetVO.setClassId(resultVO.getClassId());
                    cabinetVO.setCabinetId(resultVO.getCabinetId());
                    cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
                    if (cabinetResultVO == null) { logger.info("[createPaystubPDF] 캐비넷 정보가 존재하지 않습니다."); return 0; }

                    originalPdfPath = cabinetResultVO.getCabinetPath() + resultVO.getFilePath() + resultVO.getFileName();
                    targetPdfName   = StringUtil.getUUID() + ".pdf";
                    targetPdfPath   = MultipartFileUtil.getSystemTempPath() + targetPdfName;

                    logger.info("change originalPdfPath => " + originalPdfPath);
                    logger.info("change targetPdfPath => " + targetPdfPath);
                }

                //급여 지급내역
                int nPaymentSumAmt   = 0;
                int nDeductionSumAmt = 0;
                if (!("3".equals(paystubResultVO.getPayFormType()) || "4".equals(paystubResultVO.getPayFormType()))) {
                    for (int j=0; j<paystubPaymentListVO.size(); j++) {
                        PaystubDataVO paystubPaymentVO = paystubPaymentListVO.get(j);
                        String payTaxName 			   = paystubPaymentVO.getPayTaxName();
                        String payTaxAmt  			   = StringUtil.nvl(paystubPaymentVO.getPayTaxAmt(), "0");
                        fieldListVO.add(FieldUtil.setFieldData("payment_name_" + j, payTaxName));

                        payTaxAmt = setCommaText(payTaxAmt);
                        fieldListVO.add(FieldUtil.setFieldData("payment_amt_" + j, payTaxAmt));

                        if (StringUtil.isNumber(StringUtil.ReplaceAll(payTaxAmt, ",", ""))) nPaymentSumAmt += Integer.parseInt(StringUtil.ReplaceAll(payTaxAmt, ",", ""));

                        // 급여 게산방식 표기 추가(21.11.16)
                        if (!paystubPaymentVO.getPayTaxCalcEvidence().isEmpty()) {
                            String payTaxCalcName 	  = paystubPaymentVO.getPayTaxName();
                            String payTaxCalcEvidence = paystubPaymentVO.getPayTaxCalcEvidence();
                            fieldListVO.add(FieldUtil.setFieldData("paytaxcalcname_" + CalcEvidenceCount, payTaxCalcName));
                            fieldListVO.add(FieldUtil.setFieldData("paytaxcalcevidence_" + CalcEvidenceCount, payTaxCalcEvidence));
                            CalcEvidenceCount++;
                        }
                    }

                    //급여 공제내역
                    for (int j=0; j<paystubDeductionListVO.size(); j++) {
                        PaystubDataVO paystubDeductionVO = paystubDeductionListVO.get(j);
                        String payTaxName 				 = paystubDeductionVO.getPayTaxName();
                        String payTaxAmt  				 = StringUtil.nvl(paystubDeductionVO.getPayTaxAmt(), "0");
                        fieldListVO.add(FieldUtil.setFieldData("deduction_name_" + j, payTaxName));

                        payTaxAmt = setCommaText(payTaxAmt);
                        fieldListVO.add(FieldUtil.setFieldData("deduction_amt_" + j, payTaxAmt));

                        if (StringUtil.isNumber(StringUtil.ReplaceAll(payTaxAmt, ",", ""))) nDeductionSumAmt += Integer.parseInt(StringUtil.ReplaceAll(payTaxAmt, ",", ""));

                        // 급여 게산방식 표기 추가(21.11.16)
                        if (!paystubDeductionVO.getPayTaxCalcEvidence().isEmpty()) {
                            String payTaxCalcName = paystubDeductionVO.getPayTaxName();
                            String payTaxCalcEvidence = paystubDeductionVO.getPayTaxCalcEvidence();
                            fieldListVO.add(FieldUtil.setFieldData("paytaxcalcname_" + CalcEvidenceCount, payTaxCalcName));
                            fieldListVO.add(FieldUtil.setFieldData("paytaxcalcevidence_" + CalcEvidenceCount, payTaxCalcEvidence));
                            CalcEvidenceCount++;
                        }
                    }
                } else { // 계산방법 상단표기 양식은 계산내역을 항목화 함께 표시
                    for (int j=0; j<paystubPaymentListVO.size(); j++) {
                        PaystubDataVO paystubPaymentVO = paystubPaymentListVO.get(j);
                        String payTaxName 			   = paystubPaymentVO.getPayTaxName();
                        String payTaxAmt  			   = StringUtil.nvl(paystubPaymentVO.getPayTaxAmt(), "0");
                        String payTaxCalcEvidence 	   = paystubPaymentVO.getPayTaxCalcEvidence();
                        fieldListVO.add(FieldUtil.setFieldData("payment_name_" + j, payTaxName));

                        payTaxAmt = setCommaText(payTaxAmt);
                        fieldListVO.add(FieldUtil.setFieldData("payment_amt_" + j, payTaxAmt));

                        if (StringUtil.isNumber(StringUtil.ReplaceAll(payTaxAmt, ",", ""))) nPaymentSumAmt += Integer.parseInt(StringUtil.ReplaceAll(payTaxAmt, ",", ""));

                        fieldListVO.add(FieldUtil.setFieldData("paytaxcalcevidence_" + j, payTaxCalcEvidence));
                    }

                    //급여 공제내역
                    for (int j=0; j<paystubDeductionListVO.size(); j++) {
                        PaystubDataVO paystubDeductionVO = paystubDeductionListVO.get(j);
                        String payTaxName 				 = paystubDeductionVO.getPayTaxName();
                        String payTaxAmt  				 = StringUtil.nvl(paystubDeductionVO.getPayTaxAmt(), "0");
                        String payTaxCalcEvidence 		 = paystubDeductionVO.getPayTaxCalcEvidence();
                        fieldListVO.add(FieldUtil.setFieldData("deduction_name_" + j, payTaxName));

                        payTaxAmt = setCommaText(payTaxAmt);
                        fieldListVO.add(FieldUtil.setFieldData("deduction_amt_" + j, payTaxAmt));

                        if (StringUtil.isNumber(StringUtil.ReplaceAll(payTaxAmt, ",", ""))) nDeductionSumAmt += Integer.parseInt(StringUtil.ReplaceAll(payTaxAmt, ",", ""));

                        fieldListVO.add(FieldUtil.setFieldData("paytaxcalcevidence_dedu_" + j, payTaxCalcEvidence));
                    }
                }

                //근로일수 등 (21.11.17추가) // 24.05.30 사용자 지정 추가
                for (int l=0; l<paystubWorkListVO.size(); l++) {
                    PaystubDataVO paystubWorkVO = paystubWorkListVO.get(l);
                    String workdayName 			= paystubWorkVO.getPayTaxName();
                    String workdayAmt  			= paystubWorkVO.getPayTaxAmt();

                    if (StringUtil.isNotNull(workdayAmt) && StringUtil.isNumber(workdayAmt)) workdayAmt = setCommaText(workdayAmt);
                    fieldListVO.add(FieldUtil.setFieldData("workday_name_" + l, workdayName));
                    fieldListVO.add(FieldUtil.setFieldData("workday_amt_" + l, workdayAmt));
                }

                // 차인지급액 계산
                int nPayMonthAmt = nPaymentSumAmt - nDeductionSumAmt;
                //지급총액
                fieldListVO.add(FieldUtil.setFieldData("payment_sum_amt", StringUtil.toNumFormat(nPaymentSumAmt)));
                //공제총액
                fieldListVO.add(FieldUtil.setFieldData("deduction_sum_amt", StringUtil.toNumFormat(nDeductionSumAmt)));
                //차인지금액
                fieldListVO.add(FieldUtil.setFieldData("pay_month_amt", StringUtil.toNumFormat(nPayMonthAmt)));
                //파일생성
                boolean bWritePDF = ControlUtil.writePDF(fieldListVO, originalPdfPath, targetPdfPath, false, true, System.getProperty(fontProperty));
                //콘텐츠등록
                if (bWritePDF) {
                    contentVO = new ContentVO();
                    contentVO.setFileType(FileUtil.getFileExt(targetPdfPath));
                    contentVO.setFileName(targetPdfName);
                    contentVO.setOrgFileName(resultEmpVO.getEmpName() + "_명세서_" + paystubResultVO.getPayDate() + ".pdf");
                    contentVO.setFileSize(Long.toString(FileUtil.getFileSize(targetPdfPath)));
                    contentVO.setFilePath(MultipartFileUtil.getSystemTempPath());
                    contentVO.setFileTitle(resultEmpVO.getEmpName() + "_명세서_" + paystubResultVO.getPayDate());
                    contentVO.setClassId(PaystubService.SAVE_CLASS_ID);
                    contentVO.setFileVersion("0");
                    contentVO.setParFileId("");
                    contentVO.setEtcDesc("");
                    contentVO.setCheckInOut("N");
                    contentVO.setCheckUserId("");
                    contentVO.setCheckCount("0");
                    contentVO.setUseYn("Y");
                    contentVO.setDelYn("N");

                    int nResult = contentService.transContent(contentVO);
                    if (ContentService.COMPLETED == nResult) {
                        paystubVO.setBizId(paystubDataVO.getBizId());
                        paystubVO.setUserId(paystubDataVO.getUserId());
                        paystubVO.setPayMonth(paystubDataVO.getPayMonth());
                        paystubVO.setPayType(paystubDataVO.getPayType());
                        paystubVO.setPayDate(paystubDataVO.getPayDate());
                        paystubVO.setInsDate(paystubDataVO.getInsDate());
                        paystubVO.setFileId(contentVO.getFileId());
                        paystubVO.setTransType("C");
                        int paystubcount = paystubDAO.updPaystub(paystubVO);
                        logger.info("[createPaystubPDF] update FileID : " + paystubcount + "," + paystubVO.getServiceId() + "/" + paystubVO.getBizId() + "/" + paystubVO.getUserId() + "/" + paystubVO.getPayMonth());
                        FileUtil.deleteFile(targetPdfPath);
                        result++;
                        logger.info("TempFile Deleted!");

                        //완료 이후 PUSH메시지 전송
                        String payMonth = paystubVO.getPayMonth();
                        if (payMonth.length() == 6) payMonth = payMonth.substring(0, 4) + "년" + payMonth.substring(4, 6);

                        try 				{ pushSendService.candySendPushMessage(paystubVO.getBizId(), paystubVO.getUserId(), "{0}님의 " + payMonth + "월의 임금명세서가 전송되었습니다."); }
						catch (Exception e) { logger.info("Message Server Error : " + e.getMessage()); }

                        // 비즈라운지, 캔디캐시의 경우 기존처럼 수행하고, 일반 사용자는 서비스를 분리하여 처리
                        if (useMobile) {
                            // 포인트 차감
                            pointVO.setCurPoint(reducePoint);
                            pointDAO.balancePoint(pointVO);

                            PointLogVO pointLogVO = new PointLogVO();
                            pointLogVO.setBizId(paystubVO.getBizId());
                            pointLogVO.setUserId(paystubVO.getUserId());
                            pointLogVO.setServiceId(paystubDataVO.getServiceId());
                            pointLogVO.setPointType("2");
                            pointLogVO.setPointPrice(pointVO.getCurPoint());
                            pointLogVO.setPointResult(resultPointVO.getCurPoint() - pointVO.getCurPoint());
                            pointLogVO.setEtcDesc("임금명세서 전송 포인트 차감");
                            logger.info("임금명세서 전송 <" + reducePoint + ">포인트 차감");

                            pointDAO.insPointLog(pointLogVO);
                        }
                    } else { logger.info("transContent Error Code : " + nResult); }
                }
            }
        }
		return result;
	}

	private String setCommaText(String payTaxAmt) {
		if (!payTaxAmt.isEmpty() && StringUtil.isNumber(StringUtil.ReplaceAll(payTaxAmt, ",", ""))) payTaxAmt = StringUtil.toNumFormat(Integer.parseInt(StringUtil.ReplaceAll(payTaxAmt, ",", "")));

		return payTaxAmt;
	}

	@Override
	public String getPaystubFileId(PaystubVO vo) {
		return paystubDAO.getPaystubFileId(vo);
	}

	@Override
	public int updatePaystub(PaystubVO vo) {

		CabinetVO cabinetVO = new CabinetVO();
		ContentVO contentVO = new ContentVO();
		//업데이트
		int updateCount = paystubDAO.updPaystub(vo);
		//이메일 전송
		BizVO searchVO  = new BizVO();
		searchVO.setBizId(vo.getBizId());
		BizVO bizVO     = bizDAO.getBiz(searchVO);

		if (bizVO.getLoginId().contains("@bizlounge.com")) {

			contentVO.setFileId(vo.getFileId());
			ContentVO resultVO = contentDAO.getContent(contentVO);
			if (resultVO == null) { logger.info("[createPaystubPDF] 콘텐츠 정보가 존재하지 않습니다."); return 0; }

			// 저장위치
			cabinetVO.setClassId(resultVO.getClassId());
			cabinetVO.setCabinetId(resultVO.getCabinetId());
			CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
			if (cabinetResultVO == null) { logger.info("[createPaystubPDF] 캐비넷 정보가 존재하지 않습니다."); return 0; }

			EmpVO empVO = new EmpVO();
			empVO.setBizId(vo.getBizId());
			empVO.setUserId(vo.getUserId());
			EmpVO resultEmpVO = empDAO.getEmp(empVO);

			String originalPdfPath = cabinetResultVO.getCabinetPath() + resultVO.getFilePath() + resultVO.getFileName();
			String targetPdfName   = resultEmpVO.getEmpName() + "님의" + vo.getPayMonth() +" 월 급여명세서.pdf";
			String targetPdfPath   = MultipartFileUtil.getSystemTempPath() + targetPdfName ;

			FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());

			if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {

				String savedFileFullName = cabinetResultVO.getCabinetPath() + resultVO.getFilePath() + resultVO.getFileTitle() + "." + resultVO.getFileType();

				//타켓 파일을 수정한다.
				try {
					File tmpOrgFile = new File(savedFileFullName);
					//파일 존재 할경우 해당 파일 전송
					if (!tmpOrgFile.exists()) {
						File targetFile = new File(targetPdfPath);
						PDDocument pdd = PDDocument.load(targetFile);
						AccessPermission ap = new AccessPermission();
						StandardProtectionPolicy stpp = new StandardProtectionPolicy(resultEmpVO.getUserDate(), resultEmpVO.getUserDate(), ap);

						// step 4. Setting the length of Encryption key
						stpp.setEncryptionKeyLength(128);

						// step 5. Setting the permission
						stpp.setPermissions(ap);

						// step 6. Protecting the PDF file
						pdd.protect(stpp);

						// step 7. Saving and closing the the PDF Document
						pdd.save(savedFileFullName);
						pdd.close();
						logger.info("PDF Encrypted successfully...");
					}
					// 이메일전송
					String content = "";

					content += "<!DOCTYPE html> ";
					content += "<html> ";
					content += "<head> ";
					content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
					content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
					content += "<title>급여명세서</title> ";
					content += "</head> ";
					content += "<body> ";
					content += "    <div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
					content += "        <div style=\" padding: 37px 0 0 0;\"> ";
					content += "            <div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
					content += "                <span style=\"font-weight: bold; color:#00a9e9;\">BIZ</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\"> LOUNGE</span> ";
					content += "             </div>";
					content += "            <div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
					content += "                <span style=\"color:#00a9e9; letter-spacing: -6.1px;\">" + resultEmpVO.getBizName() + " 에서 임금명세서를 전송</span><span class=\"font_black\" style=\"color:#202020;\">하였습니다.</span>";
					content += "             </div>";
					content += "            <div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
					content += "                <span > 임금명세서암호 : 본인생년월일(YYYYMMDD) ex: 1999년 1월 1일 경우 19990101</span>";
					content += "             </div>";
					content += "        <div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
					content += "            <div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
					content += "                <span style=\"color:#838383\">이 이메일은 이스트웨이브㈜에서 제공하는 발신 전용 이메일입니다.";
					content += "                문의사항이 있으시면 </span> <br><span style=\"color:#3da6ff\">bizcontact@eastwave.kr</span > ";
					content += "                <span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
					content += "            </div> ";
					content += "        </div>";
					content += "     </div>";
					content += "</body> ";
					content += "</html> ";

					MultiPartEmail email = new MultiPartEmail();
					MailVO mailVO = new MailVO();
					mailVO.setFrom("no_reply@newzenpnp.com");
					mailVO.setTo(resultEmpVO.getEMail());
					mailVO.setCc("");
					mailVO.setBcc("");
					mailVO.setSubject("[" + resultEmpVO.getBizName() + "] " + resultEmpVO.getEmpName() + " 님께 임금명세서를 전송 하였습니다.");
					mailVO.addFile(savedFileFullName);
					mailVO.setText(content);

					logger.info("비즈 라운지 임금명세서 이메일을 발송. email : " + resultEmpVO.getEMail());
					email.send(mailVO);
				} catch (Exception e) { e.printStackTrace(); }
			}
		}
		return updateCount;
	}

	@Override
	public PaystubVO getPaystub(PaystubVO vo) {
		
		PaystubVO retValue   = new PaystubVO();
		PaystubDataVO dataVO = new PaystubDataVO();
		dataVO.setBizId(vo.getBizId());
		dataVO.setUserId(vo.getUserId());
		dataVO.setPayType(vo.getPayType());
		dataVO.setPayDate(vo.getPayDate());
		dataVO.setPayMonth(vo.getPayMonth());
		
		List<PaystubVO> listValue   = paystubDAO.getPaystubList(vo);
		if (!listValue.isEmpty()) retValue = listValue.get(0);

		List<PaystubDataVO> results = paystubDAO.getPaystubDataList(dataVO);
		retValue.setDataList(results);
		
		return retValue;
	}

	@Override
	public int sendPaystubPDF(PaystubVO vo) {

		int result = 0;
		// 명세서 리스트 정보 가져온다.(현재 전송대기인 문서만)
		PaystubVO paystubVO = new PaystubVO();

		paystubVO.setServiceId(PaystubService.SERVICE_ID);
		paystubVO.setBizId(vo.getBizId());
		paystubVO.setTransType("C");
		paystubVO.setSeqId(vo.getSeqId());
		paystubVO.setStartPage(0);
		paystubVO.setEndPage(10000);

		List<PaystubVO> paystubList = paystubDAO.getPaystubList(paystubVO);
		PointVO pointVO 	  		= new PointVO();
		pointVO.setBizId(vo.getBizId());
		PointVO resultPointVO 		= pointDAO.getPoint(pointVO);

		if (resultPointVO == null) { logger.error("[sendDocument] 포인트정보가 존재하지 않습니다."); return -1; }

		int reducePoint = 0;
		BizGrpVO grpVO  = new BizGrpVO();
		grpVO.setBizId(vo.getBizId());
		List<BizGrpVO> pointList = bizDAO.getBizGrp(grpVO);
		if (pointList == null) {
			logger.error("[sendDocument] 기업정보가 존재하지 않습니다.");
			return -2;
		} else {
			//현재 보유중인 포인트
			int point = resultPointVO.getCurPoint();
			//전자문서 전송 차감 포인트
			if (StringUtil.isNotNull(pointList.get(0).getPayPoint())) reducePoint = Integer.parseInt(pointList.get(0).getPayPoint());

			if ((point - (reducePoint * paystubList.size())) < 0) {
				logger.error("[sendDocument] 잔여 포인트가 존재하지 않습니다. 현재포인트 : <" + point + ">, 차감 포인트 : <" + (reducePoint * paystubList.size()) + ">");
				return -3;
			}
		}

        for (PaystubVO paystubResultVO : paystubList) {

            PaystubVO paystubStatusVO = new PaystubVO();

            paystubStatusVO.setBizId(paystubResultVO.getBizId());
            paystubStatusVO.setUserId(paystubResultVO.getUserId());
            paystubStatusVO.setPayMonth(paystubResultVO.getPayMonth());
            paystubStatusVO.setPayType(paystubResultVO.getPayType());
            paystubStatusVO.setPayDate(paystubResultVO.getPayDate());
            paystubStatusVO.setInsDate(paystubResultVO.getInsDate());
            paystubStatusVO.setFileId(paystubResultVO.getFileId());
            paystubStatusVO.setTransType("Y");

            // 변환상태가 C인 명세서를 업데이트 후 count
            result = result + paystubDAO.updPaystubStatus(paystubStatusVO);
            //완료 이후 PUSH메시지 전송
            String payMonth = paystubResultVO.getPayMonth();

            if (payMonth.length() == 6) payMonth = payMonth.substring(0, 4) + "년" + payMonth.substring(4, 6);

            try				    { pushSendService.candySendPushMessage(paystubResultVO.getBizId(), paystubResultVO.getUserId(), "{0}님의 " + payMonth + "월의 임금명세서가 전송되었습니다."); }
			catch (Exception e) { logger.info("Message Server Error : " + e.getMessage()); }

            // 포인트 차감
            pointVO.setCurPoint(reducePoint);
            pointDAO.balancePoint(pointVO);

            PointLogVO pointLogVO = new PointLogVO();
            pointLogVO.setBizId(paystubResultVO.getBizId());
            pointLogVO.setUserId(paystubResultVO.getUserId());
            pointLogVO.setServiceId(paystubResultVO.getServiceId());
            pointLogVO.setPointType("2");
            pointLogVO.setPointPrice(pointVO.getCurPoint());
            pointLogVO.setPointResult(resultPointVO.getCurPoint() - pointVO.getCurPoint());
            pointLogVO.setEtcDesc("임금명세서 전송 포인트 차감");
            logger.info("임금명세서 전송 <" + reducePoint + ">포인트 차감");

            pointDAO.insPointLog(pointLogVO);
        }
		return result;
	}

}
