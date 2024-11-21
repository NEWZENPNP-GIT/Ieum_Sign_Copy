package com.ezsign.mobile.service.impl;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.biztalk.dao.BizTalkDAO;
import com.ezsign.biztalk.vo.BizTalkKKOVO;
import com.ezsign.content.dao.CabinetDAO;
import com.ezsign.content.dao.ContentDAO;
import com.ezsign.content.service.ContentService;
import com.ezsign.content.vo.CabinetVO;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.contract.dao.ContractDAO;
import com.ezsign.contract.service.ContractService;
import com.ezsign.contract.vo.ContractFormVO;
import com.ezsign.contract.vo.ContractPersonLogVO;
import com.ezsign.contract.vo.ContractPersonVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.mail.MailVO;
import com.ezsign.framework.mail.MultiPartEmail;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.PKIUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.util.XmlDataUtil;
import com.ezsign.mobile.service.MobileContractService;
import com.ezsign.point.dao.PointDAO;
import com.jarvis.pdf.util.ControlUtil;
import com.jarvis.pdf.util.FieldUtil;
import com.jarvis.pdf.vo.FieldVO;
import com.jarvis.pdf.vo.FormVO;

@Service("mobileContractService")
public class MobileContractServiceImpl  implements MobileContractService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name = "contentService")
	private ContentService contentService;
	
	@Resource(name="contractDAO")
	private ContractDAO contractDAO;
	
	@Resource(name="empDAO")
	private EmpDAO empDAO;
	
	@Resource(name="contentDAO")
	private ContentDAO contentDAO;
	
	@Resource(name="cabinetDAO")
	private CabinetDAO cabinetDAO;
	
	@Resource(name="bizDAO")
	private BizDAO bizDAO;
	
	@Resource(name="bizTalkDAO")
	private BizTalkDAO bizTalkDAO;
	
	// 모바일 전자근로계약 저장
	public ContractPersonVO saveElectronicContract(ContractPersonVO vo) throws Exception {
		
		List<ContractPersonVO> contractList = new ArrayList<>();
		ContractPersonVO result 			= new ContractPersonVO();
		String bizId 		= vo.getBizId();
		String contractDate = DateUtil.getTimeStamp(3);		//계약일자
		String contractId   = ContractService.MOBILE_CONTRACT_TEMP_PDF_ID;

		// 근로자 정보 조회
		EmpVO paramEmpVO = new EmpVO();
		paramEmpVO.setBizId(bizId);
		paramEmpVO.setEmpNo(vo.getEmpNo());
		EmpVO resultEmpVO = empDAO.getEmpNo(paramEmpVO);
		
		// 대표자 정보 조회
		EmpVO paramOwnerVO  = new EmpVO();
		paramOwnerVO.setBizId(vo.getBizId());
		paramOwnerVO.setEmpNo(vo.getEmpNo());
		EmpVO resultOwnerVO = empDAO.getEmpNo(paramOwnerVO);
		
		if (resultEmpVO != null) {
			logger.info("[sendContractExcel] 대상->" + resultEmpVO.getEmpName());
			ContractPersonVO contractVO = new ContractPersonVO();
			contractVO.setServiceId(ContractService.SERVICE_ID);
			contractVO.setBizId(bizId);
			contractVO.setUserId(resultEmpVO.getUserId());
			contractVO.setContractDate(contractDate);
			String contractName = XmlDataUtil.dateHFormat(contractDate) + " " + resultEmpVO.getBizName() + "과 " + resultEmpVO.getEmpName()+"님의 전자문서";
			contractVO.setContractFileId(contractId);
			contractVO.setContractName(contractName);
			contractVO.setContractType("1");
			contractVO.setContractId("");
			contractVO.setEndDate("");
			contractVO.setKeepDate("");
			contractVO.setDigitSign("");
			contractVO.setDigitNonce("");
			contractVO.setStatusCode("2");
			contractVO.setTransType("N");
			contractVO.setEmpName(resultEmpVO.getEmpName());

			// Form 데이타 정보를 셋팅한다.
			setFormData(contractVO, resultEmpVO, resultOwnerVO, vo);			
			contractList.add(contractVO);
		}

		// 계약정보 등록(등록수정)
        for (ContractPersonVO formVO : contractList) {
            result.setEmpName(formVO.getEmpName());
            // 계약정보 등록
            String insContId = contractDAO.insContractPerson(formVO);
            result.setContId(insContId);
            logger.info("[sendContractExcel] 등록 처리->" + insContId + "/" + formVO.getEmpName() + "/" + formVO.getUserId() + "/" + formVO.getContractDate());

            // 계약정보 폼 입력항목 등록
            List<ContractFormVO> tranDataList = formVO.getFormList();

            for (ContractFormVO tranData : tranDataList) {
                tranData.setContId(insContId);
                contractDAO.insContractForm(tranData);
            }
            logger.info("[sendContractExcel] 폼 등록 처리->" + insContId + "/" + formVO.getEmpName() + "/" + formVO.getUserId() + "/" + formVO.getContractDate() + "/" + tranDataList.size());
        }
		return result;
	}

	// 전자 PDF 파일 생성
	@SuppressWarnings("static-access")
	@Override
	public List<ContentVO> generateContractPDF(ContractPersonVO vo) throws Exception {
		ContentVO contentVO		   = new ContentVO();
		CabinetVO cabinetVO		   = new CabinetVO();
		List<ContentVO> resultList = new ArrayList<>();
		List<ContentVO> delList    = new ArrayList<>();
		
		logger.info("[generateContractPDF] contId => "+vo.getContId() );
		ContractPersonVO resultContractVO = contractDAO.getContractPerson(vo);
		
		if (resultContractVO.getStatusCode().equals("Y")) {
			logger.error("[generateContractPDF] 완료된 전자문서를 처리할 수 없습니다.");
			vo.setResultMessage("완료된 전자문서를 처리할 수 없습니다.");
			return null;
		}
			
		// 기존 계약서 파일삭제 (계약생성시에만 삭제처리)
		if (StringUtil.isNotNull(resultContractVO.getContractId()) && resultContractVO.getStatusCode().equals("1")) {
			ContentVO fileVO = new ContentVO();
			fileVO.setFileId(resultContractVO.getContractId());
			delList.add(fileVO);
		}

		// 콘텐츠에서 정보 추출	
		contentVO.setFileId(resultContractVO.getContractFileId());
		ContentVO resultVO = contentDAO.getContent(contentVO);
		if (resultVO == null) {
			logger.error("[generateContractPDF] 콘텐츠 정보가 존재하지 않습니다.");
			vo.setResultMessage("콘텐츠 정보가 존재하지 않습니다.");
			return null;
		}
		
		// 저장위치
		cabinetVO.setClassId(resultVO.getClassId());
		cabinetVO.setCabinetId(resultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO == null) {
			logger.error("[generateContractPDF] 캐비넷 정보가 존재하지 않습니다.");
			vo.setResultMessage("캐비넷 정보가 존재하지 않습니다.");
			return null;
		}

		BizVO bizVO = new BizVO();
		bizVO.setBizId(vo.getBizId());
		bizVO.setStartPage(0);
		bizVO.setEndPage(1);
		List<BizVO> bizList = bizDAO.getBizList(bizVO);
		if (bizList == null || bizList.isEmpty()) {
			logger.error("[generateContractPDF] 기업정보가 존재하지 않습니다.");
			vo.setResultMessage("기업정보가 존재하지 않습니다.");
			return null;
		}
		
		String originalPdfPath = cabinetResultVO.getCabinetPath()+resultVO.getFilePath()+resultVO.getFileName();
		String targetPdfName   = StringUtil.getUUID()+".pdf";
		String targetPdfPath   = MultipartFileUtil.getSystemTempPath()+targetPdfName;
		
		if (!(new File(originalPdfPath).exists())) {
			logger.error("[generateContractPDF] PDF 파일정보가 존재하지 않습니다.");
			vo.setResultMessage("PDF 파일정보가 존재하지 않습니다.");
			return null;
		}

		// 사용할 PDF복사
		FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
		if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
			List<FieldVO> fieldListVO = new ArrayList<>();
			// 계약서 정보
			logger.info("[generateContractPDF] 조회 contId =>"+resultContractVO.getContId());
			fieldListVO.add(FieldUtil.setFieldData("cont_id", resultContractVO.getContId()));
			fieldListVO.add(FieldUtil.setFieldData("service_id", resultContractVO.getServiceId()));
			fieldListVO.add(FieldUtil.setFieldData("biz_id", resultContractVO.getBizId()));
			fieldListVO.add(FieldUtil.setFieldData("user_id", resultContractVO.getUserId()));
			fieldListVO.add(FieldUtil.setFieldData("contract_date", resultContractVO.getContractDate()));
			
			// 계약 입력항목 설정 
			ContractFormVO formVO = new ContractFormVO();
			formVO.setContId(resultContractVO.getContId());
			List<ContractFormVO> formList = contractDAO.getContractFormList(formVO);

            for (ContractFormVO formData : formList) fieldListVO.add(FieldUtil.setFieldData(formData.getFormDataId(), formData.getFormDataValue()));

			String bizImage = "";
			// 이미지 생성(회사 직인 찍음)
			if (StringUtil.isNotNull(bizList.get(0).getCompanyImage()) && !bizList.get(0).getCompanyImage().equals("-")) {
				bizImage = MultipartFileUtil.getSystemPath()+"images/sign/"+bizList.get(0).getCompanyImage();
			} else {
				bizImage = MultipartFileUtil.getSystemPath()+"images/sign/digitsign.png";
			}
			
			// sign_1 (갑:사업자) 서명란 key값 검색
			List<String> keyList	 = new ArrayList<>();
			InputStream is 			 = Files.newInputStream(Paths.get(originalPdfPath));
			List<FormVO> pdfFormList = FieldUtil.getAllField(is, false);
			is.close();

            for (FormVO pdfFormVO : pdfFormList) {
                String formId = pdfFormVO.getId();
                if (formId.contains("sign_1")) keyList.add(formId);
            }

            for (String lineId : keyList) {
                if (StringUtil.isNotNull(lineId)) {
                    FieldVO fieldVO = new FieldVO();
                    fieldVO.setId(lineId);
                    fieldVO.setImage(bizImage);
                    fieldListVO.add(fieldVO);
                }
            }
			//////////////////////////////////////////////////////////////////
			
			//파일생성
			boolean bWritePDF = ControlUtil.writePDF(fieldListVO, originalPdfPath, targetPdfPath, false, false, System.getProperty("pdf.font.default"));
			//콘텐츠등록
			if (bWritePDF) {
				contentVO = new ContentVO();
				contentVO.setFileType(FileUtil.getFileExt(targetPdfPath));
				contentVO.setFileName(targetPdfName);
				contentVO.setOrgFileName(resultContractVO.getEmpName()+"_"+resultVO.getFileTitle()+".pdf");
				contentVO.setFileSize(Long.toString(FileUtil.getFileSize(targetPdfPath)));
				contentVO.setFilePath(MultipartFileUtil.getSystemTempPath());
				contentVO.setFileTitle(resultContractVO.getEmpName()+"_"+resultVO.getFileTitle());
				
				contentVO.setClassId(ContractService.SAVE_CLASS_ID);
				contentVO.setFileVersion("0");
				contentVO.setParFileId("");
				contentVO.setEtcDesc("");
				contentVO.setCheckInOut("N");
				contentVO.setCheckUserId("");
				contentVO.setCheckCount("0");
				contentVO.setUseYn("Y");
				contentVO.setDelYn("N");
				
				int nResult = contentService.transContent(contentVO);
				if (contentService.COMPLETED == nResult) {
					ContractPersonVO contractPersonVO = new ContractPersonVO();
					contractPersonVO.setContId(resultContractVO.getContId());
					contractPersonVO.setHashData(PKIUtil.extractFileHashSHA256(targetPdfPath));
					contractPersonVO.setStatusCode("2");
					contractPersonVO.setContractId(contentVO.getFileId());
					contractPersonVO.setTransType("Y");
					// 난수 초기화 
					contractPersonVO.setDigitNonce("");
					contractPersonVO.setSendDate("");
					
					// 계약정보 업데이트
					contractDAO.updContractPerson(contractPersonVO);
					
					// 로그내역 등록
					ContractPersonLogVO logVO = new ContractPersonLogVO();
					logVO.setContId(resultContractVO.getContId());
					logVO.setServiceId(ContractService.SERVICE_ID);
					logVO.setBizId(resultContractVO.getBizId());
					logVO.setUserId(resultContractVO.getUserId());
					logVO.setContractDate(resultContractVO.getContractDate());
					logVO.setContractId(contentVO.getFileId());
					logVO.setLogType("1");
					logVO.setLogMessage(resultContractVO.getEmpName()+"님의 전자문서를 생성하였습니다.");
					logVO.setInsEmpNo(vo.getUserId());
					contractDAO.insContractPersonLog(logVO);
					
					logger.info("[generateContractPDF]  ["+contentVO.getFileId()+"]TempFile Delete : "+targetPdfPath);
					FileUtil.deleteFile(targetPdfPath);
					
					resultList.add(contentVO);
				} else {
					logger.info("[generateContractPDF] tractContent ResulCode : " + nResult);
				}
			}
		}
		
		if (!delList.isEmpty()) {
			logger.info("[generateContractPDF] 기존 전자문서 삭제처리");
			contentService.deleteContent(delList);			
		}
		return resultList;
	}
	
	// 전자문서 전송
	@Override
	public ContractPersonVO sendContractPerson(ContractPersonVO vo) throws Exception {
		ContractPersonVO result = new ContractPersonVO();
		int count = 0;
		try {
			result.setResultCode("100");
			ContractPersonVO contractResultVO = contractDAO.getContractPerson(vo);
				
			if (contractResultVO == null) {
				logger.error("[sendContractPersonMulti] 전자문서 정보가 존재하지 않습니다.");
				result.setResultMessage("전자문서 정보가 존재하지 않습니다.");
				result.setResultCode("400");
				return result;
			}

			logger.info("sendContractPersonMulti : "+contractResultVO.getContId()+ ", "+contractResultVO.getUserId() + ", "+contractResultVO.getContractDate());

			// 접속한 사용자정보
			EmpVO empVO = new EmpVO();
			empVO.setBizId(contractResultVO.getBizId());
			empVO.setUserId(contractResultVO.getUserId());
			EmpVO empResultVO = empDAO.getEmp(empVO);

			if(empResultVO==null) {
				logger.error("[sendContractPersonMulti] 직원정보가 존재하지 않습니다.");
				result.setResultMessage("직원정보가 존재하지 않습니다.");
				result.setResultCode("300");
				return result;
			}

			if(!StringUtil.isNotNull(empResultVO.getEMail()) && vo.getSendType().equals("EMAIL")) {
				logger.error("[sendContractPersonMulti] "+empResultVO.getEmpName()+"님의 이메일정보가 없습니다.");
				result.setResultMessage(empResultVO.getEmpName()+"님의 이메일정보가 없습니다.");
				result.setResultCode("301");
				return result;
			}

			if(!StringUtil.isNotNull(empResultVO.getPhoneNum()) && vo.getSendType().equals("KKO")) {
				logger.error("[sendContractPersonMulti] "+empResultVO.getEmpName()+"님의 휴대폰번호 정보가 없습니다.");
				result.setResultMessage(empResultVO.getEmpName()+"님의 휴대폰번호 정보가 없습니다.");
				result.setResultCode("302");
				return result;
			}

			ContractPersonVO contractPersonVO = new ContractPersonVO();
			contractPersonVO.setServiceId(ContractService.SERVICE_ID);
			contractPersonVO.setBizId(vo.getBizId());
			contractPersonVO.setUserId(vo.getUserId());
			contractPersonVO.setContractDate(vo.getContractDate());
			contractPersonVO.setContId(contractResultVO.getContId());

			contractPersonVO.setStatusCode("3");
			contractPersonVO.setSendDate(DateUtil.getTimeStamp(7));
			String digitNonce = StringUtil.getRandom(15);
			contractPersonVO.setDigitNonce(digitNonce);
			contractPersonVO.setSendType(vo.getSendType());
			contractPersonVO.setExpireDate(vo.getExpireDate());
			contractPersonVO.setComment(vo.getComment());

			// 계약정보 업데이트
			int nResult = contractDAO.updContractPerson(contractPersonVO);

			String logMessage = "";
			if (nResult>0) { logMessage=contractResultVO.getEmpName()+"님의 전자문서[전자근로계약서]를 전송하였습니다."; count++; }
			// 로그내역 등록
			ContractPersonLogVO logVO = new ContractPersonLogVO();
			logVO.setContId(contractResultVO.getContId());
			logVO.setServiceId(contractResultVO.getServiceId());
			logVO.setBizId(contractResultVO.getBizId());
			logVO.setUserId(contractResultVO.getUserId());
			logVO.setContractDate(contractResultVO.getContractDate());
			logVO.setContractId(contractResultVO.getContractId());
			logVO.setInsEmpNo(vo.getUserId());

			if (StringUtil.isNotNull(vo.getComment())) {
				logVO.setLogType("C");
				logVO.setLogMessage(vo.getComment());
				contractDAO.insContractPersonLog(logVO);
			}

			logVO.setLogType("3");
			logVO.setLogMessage(logMessage);
			contractDAO.insContractPersonLog(logVO);

			if (count > 0) {

				String kkoLinkURL = System.getProperty("biztalk.kko.contract_view_url")+"?id="+digitNonce;
				
				logger.info("전송타입 : "+vo.getSendType());
				
				if (vo.getSendType() != null && vo.getSendType().equals("EMAIL")) {
					//(이메일 전송) 근로자 서명 / 완료 처리를 위한 알림톡
					String content = "";
					
					content +="<!DOCTYPE html> ";
					content +="<html> ";
					content +="<head> ";
					content +="<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
					content +="<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
					content +="<title>전자문서</title> ";
					content +="</head> ";
					content +="<body> ";
					content +="	<div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
					content +="		<div style=\" padding: 37px 0 0 0;\"> ";
					content +="			<div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
					content +="				<span style=\"font-weight: bold; color:#00a9e9;\">IEUM</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;SIGN</span> ";
					content +="			</div> ";
					content +="			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
					content +="				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">전자근로계약서</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br> ";
					content +="				발송 이메일입니다.</span> ";
					content +="			</div> ";
					content +="			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
					content +="				<span >"+empResultVO.getEmpName()+"</span><span>님!<br> ";
					content +="				하단의 전자문서를 클릭하여 전자문서 내용을 검토 부탁드립니다.<br> ";
					content +="				수정이 필요한 부분이 있으면 정정요청을 클릭하여 내용 작성 후 담당자와<br>협의해주시기 바랍니다.";
					content +="				</span> ";
					content +="			</div> ";
					content += "		<div style=\"margin: 74px 0 32px; text-align: center;\"> ";
					content += "			<a href=\"" + kkoLinkURL + "\" style=\"display: inline-block; padding: 12px 90px; border-radius: 8px; font-family: 'tahoma'; font-size: 18px; font-weight: bold; text-decoration: none; color: #fff; background-color: #00a9e9;\">전자문서</a>";
					content += "		</div> ";
					content +="		</div> ";
					content +="		<div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
					content +="			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
					content +="				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
					content +="				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">service@newzenpnp.com</a></span > ";
					content +="				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
					content +="			</div> ";
					content +="		</div> ";
					content +="	</div> ";
					content +="</body> ";
					content +="</html> ";
					
					MultiPartEmail email = new MultiPartEmail();
					MailVO mailVO = new MailVO();
					mailVO.setFrom("no_reply@newzenpnp.com");
					mailVO.setTo(empResultVO.getEMail());
					mailVO.setCc("");
					mailVO.setBcc("");
					mailVO.setSubject(empResultVO.getBizName()+"와(과) "+empResultVO.getEmpName()+"님의 전자근로계약서가 도착하였습니다.");
					mailVO.setText(content);
					
					logger.info("IeumSign 전자문서 이메일을 발송. email : " + empResultVO.getEMail() + " digitNonce : " + digitNonce);
					logger.info("IeumSign 전자문서 이메일을 발송. email : " + empResultVO.getEMail() + " digitNonce : " + digitNonce);
					
					email.send(mailVO);
				} else {
					//사용자 서명 존재
					String content = "["+empResultVO.getBizName()+" * 서명 요청]\n\n";
					content += empResultVO.getEmpName()+"님의 전자근로계약서가(이) 도착했습니다.\n";
					content += "본 문서는 "+empResultVO.getBizName()+"에서 "+empResultVO.getEmpName()+"님에게 발송한 전자문서입니다.\n\n";
					content += "내용을 확인하신 후 서명을 진행해주세요.\n";
					content += "문서의 내용이 협의된 사실과 다를 시 정정요청을 클릭해 수정요청 사항을 작성하고 담당부서에 연락 바랍니다.\n\n";
					content += "최종 전자문서는 전자서명이 완료된 후 알림톡을 통해 배부됩니다.\n\n";
					content += "하단의 링크를 클릭하여 전자문서를 확인하세요.\n\n";
					content += kkoLinkURL;

					BizTalkKKOVO kkoVO = new BizTalkKKOVO();
					kkoVO.setSubject("");
					kkoVO.setContent(content);
					kkoVO.setCallback(System.getProperty("biztalk.kko.callback_number"));
					kkoVO.setRecipientNum(empResultVO.getPhoneNum());
					kkoVO.setSenderKey(System.getProperty("biztalk.kko.sender_key"));
					kkoVO.setTemplateCode("contract015");
					kkoVO.setKkoBtnType("1");
					kkoVO.setKkoBtnInfo("전자문서^WL^"+kkoLinkURL+"^"+kkoLinkURL);
					bizTalkDAO.insBizTalk(kkoVO);
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		return result;
	}
	
	private void setFormData(ContractPersonVO contractVO, EmpVO resultEmpVO, EmpVO resultOwnerVO, ContractPersonVO paramVO) {
		
		if (StringUtils.isNotEmpty(paramVO.getOwnerName())) {
			String formDataValue  = paramVO.getOwnerName();
			String formDataName   = "대표자명";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00001");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);								
		}

		if (StringUtils.isNotEmpty(resultEmpVO.getEmpName())) {
			String formDataValue  = resultEmpVO.getEmpName();
			String formDataName   = "근로자명";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00002");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);								
		}
		
		if (StringUtils.isNotEmpty(paramVO.getContractStartDate())) {
			String formDataValue  = paramVO.getContractStartDate();
			String formDataName   = "계약기간 시작일";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00003");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);								
		}

		if (StringUtils.isNotEmpty(paramVO.getContractEndDate())) {
			String formDataValue  = paramVO.getContractEndDate();
			String formDataName   = "계약기간 종료일";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00004");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);								
		}
		
		if (StringUtils.isNotEmpty(paramVO.getWorkPlace())) {
			String formDataValue  = paramVO.getWorkPlace();
			String formDataName   = "근무장소";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00005");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);								
		}
		
		if (StringUtils.isNotEmpty(paramVO.getWorkContent())) {
			String formDataValue  = paramVO.getWorkContent();
			String formDataName   = "업무내용";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00006");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);								
		}
		
		if (StringUtils.isNotEmpty(paramVO.getStartTime())) {
			String formDataValue  = paramVO.getStartTime();
			String formDataName   = "근무 시작시간";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00007");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);								
		}
		
		if (StringUtils.isNotEmpty(paramVO.getEndTime())) {
			String formDataValue  = paramVO.getEndTime();
			String formDataName   = "근무 종료시간";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00008");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);								
		}
		
		if (StringUtils.isNotEmpty(paramVO.getWorkDays())) {
			String formDataValue  = paramVO.getWorkDays();
			String formDataName   = "근무일";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00009");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);	
		}
		
		if (StringUtils.isNotEmpty(paramVO.getBreakTime())) {
			String formDataValue  = paramVO.getBreakTime();
			String formDataName   = "휴게시간";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00010");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);								
		}
		
		if (StringUtils.isNotEmpty(paramVO.getHoliday())) {
			String formDataValue  = paramVO.getHoliday();
			String formDataName   = "휴일";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00011");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);								
		}
		
		if (StringUtils.isNotEmpty(paramVO.getPay())) {
			String formDataValue  = paramVO.getPay();
			String formDataName   = "임금";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00012");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);								
		}
		
		if (StringUtils.isNotEmpty(paramVO.getPaymentDate())) {
			String formDataValue  = paramVO.getPaymentDate();
			String formDataName   = "지급일";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00013");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);								
		}

		if (StringUtils.isNotEmpty(paramVO.getPaymentMethod())) {
			String formDataValue  = paramVO.getPaymentMethod();
			String formDataName   = "지급방법";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00014");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);								
		}
		
		if (StringUtils.isNotEmpty(paramVO.getWriteDate())) {
			String formDataValue  = paramVO.getWriteDate();
			String formDataName   = "작성일";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00015");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);								
		}
		
		//사업주 정보
		if (StringUtils.isNotEmpty(resultOwnerVO.getBizName())) {
			String formDataValue  = resultOwnerVO.getBizName();
			String formDataName   = "사업주 사업체명";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00016");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);	
		}
		
		if (StringUtils.isNotEmpty(paramVO.getPhoneNum())) {
			String formDataValue  = paramVO.getPhoneNum();
			String formDataName   = "사업주 전화번호";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00017");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);	
		}
		
		if (StringUtils.isNotEmpty(paramVO.getCompanyAddr())) {
			String formDataValue  = paramVO.getCompanyAddr();
			String formDataName   = "사업주 주소";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00018");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);	
		}

		//근로자 정보
		if (StringUtils.isNotEmpty(resultEmpVO.getPhoneNum())) {
			String formDataValue  = resultEmpVO.getPhoneNum();
			String formDataName   = "근로자 연락처";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00019");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);	
		}
		
		if (StringUtils.isNotEmpty(resultEmpVO.getAddr1())) {
			String formDataValue  = resultEmpVO.getAddr1() + " " + StringUtil.nvl(resultEmpVO.getAddr2(), "");
			String formDataName   = "근로자 주소";
			ContractFormVO formVO = new ContractFormVO();
			formVO.setServiceId(ContractService.SERVICE_ID);
			formVO.setBizId(paramVO.getBizId());
			formVO.setUserId(resultEmpVO.getUserId());
			formVO.setEmpName(resultEmpVO.getEmpName());
			formVO.setContractDate(contractVO.getContractDate());
			formVO.setFormDataId("TXT_00020");
			formVO.setFormDataName(formDataName);
			formVO.setFormDataValue(formDataValue);
			contractVO.addForm(formVO);	
		}								
	}
	
	// 전자 근로계약 삭제
	public int deleteContractData(ContractPersonVO vo) throws Exception {
		int result = 0;
		try {
			vo.setStatusCode("D");
			// 계약정보 업데이트
			result = contractDAO.updContractPerson(vo);

			// 계약서 파일 삭제
			List<ContentVO> contentList 			= new ArrayList<>();
			ContractPersonLogVO contractPersonLogVO = new ContractPersonLogVO();
			contractPersonLogVO.setServiceId(ContractService.SERVICE_ID);
			contractPersonLogVO.setBizId(vo.getBizId());
			contractPersonLogVO.setUserId(vo.getUserId());
			contractPersonLogVO.setContractDate(vo.getContractDate());

			List<ContractPersonLogVO> loglist = contractDAO.getContractPersonLogList(contractPersonLogVO);

			if (loglist != null && !loglist.isEmpty()) {
                for (ContractPersonLogVO personLogVO : loglist) {
                    String delContractId = personLogVO.getContractId();
                    // 이미 추가된 계약서인지 확인
                    // 계약진행단계에서 동일한 계약서가 존재함
                    boolean bFind = false;
                    for (ContentVO value : contentList) {
                        if (delContractId.equals(value.getFileId())) { bFind = true; break; }
                    }
                    // 목록에서 추기되지 않는 계약서만 삭제 요청
                    if (!bFind) {
                        ContentVO contentVO = new ContentVO();
                        contentVO.setFileId(delContractId);
                        contentList.add(contentVO);
                    }
                }
			}
				
			// 삭제할 계약서 존재시 삭제처리
			// 실제 파일만 삭제 (DB는 보존)
			if (!contentList.isEmpty()) contentService.deleteContent(contentList);

			// 계약서 폼데이타 삭제
			ContractFormVO delFormData = new ContractFormVO();
			delFormData.setContId(vo.getContId());
			contractDAO.delContractForm(delFormData);


			// 로그테이블 생성
			ContractPersonLogVO logVO = new ContractPersonLogVO();
			logVO.setContId(vo.getContId());
			logVO.setServiceId(ContractService.SERVICE_ID);
			logVO.setBizId(vo.getBizId());
			logVO.setUserId(vo.getUserId());
			logVO.setContractDate(vo.getContractDate());
			logVO.setContractId(vo.getContractId());
			logVO.setLogType("D");
			logVO.setLogMessage(vo.getEmpName()+"님의 전자문서를 삭제하였습니다.");
			logVO.setInsEmpNo(vo.getUserId());
			contractDAO.insContractPersonLog(logVO);
			result = 1;
			
		} catch(Exception ex) { logger.error(ex.getMessage(), ex); }
		return result;
	}

}
