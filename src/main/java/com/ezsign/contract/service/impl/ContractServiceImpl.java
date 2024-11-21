package com.ezsign.contract.service.impl;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;

import com.ezsign.contract.vo.*;
import com.ezsign.websocket.Websocket;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import com.ezsign.contract.dao.ContractDAO;
import com.ezsign.contract.service.ContractService;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpLogVO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.mail.MailVO;
import com.ezsign.framework.mail.MultiPartEmail;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.ExcelUtil;
import com.ezsign.framework.util.ExcelUtil.HashValue;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.ImageUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.PKIUtil;
import com.ezsign.framework.util.ProcUtil;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.util.TSAUtil;
import com.ezsign.framework.util.XmlDataUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.meta.dao.MetaDAO;
import com.ezsign.meta.vo.MetaDataVO;
import com.ezsign.point.dao.PointDAO;
import com.ezsign.point.vo.PointLogVO;
import com.ezsign.point.vo.PointVO;
import com.jarvis.pdf.util.ControlUtil;
import com.jarvis.pdf.util.FieldUtil;
import com.jarvis.pdf.vo.FieldVO;
import com.jarvis.pdf.vo.FormVO;
import com.jarvis.pdf.vo.LineVO;
import com.jarvis.pdf.vo.PageVO;
import com.lowagie.text.pdf.PdfReader;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import jxl.Sheet;
import jxl.Workbook;

import static com.ezsign.framework.util.MultipartFileUtil.getSystemPath;


@Service("contractService")
public class ContractServiceImpl extends AbstractServiceImpl implements ContractService {

	Logger logger = Logger.getLogger(this.getClass());

	private static int _oneByte = 1024;

	@Resource(name = "contentService")
	private ContentService contentService;

	@Resource(name="bizDAO")
	private BizDAO bizDAO;

	@Resource(name="empDAO")
	private EmpDAO empDAO;

	@Resource(name="contentDAO")
	private ContentDAO contentDAO;

	@Resource(name="cabinetDAO")
	private CabinetDAO cabinetDAO;

	@Resource(name="contractDAO")
	private ContractDAO contractDAO;

	@Resource(name="bizTalkDAO")
	private BizTalkDAO bizTalkDAO;

	@Resource(name="pointDAO")
	private PointDAO pointDAO;

	@Resource(name="metaDAO")
	private MetaDAO metaDAO;


	// 일괄파일 업로드 (엑셀)
	@Override
	public ContractPersonVO sendContractExcel(String bizId, String xlsPath, String fileId, String webSocketKey) {
		List<ContractPersonVO> contractList = new ArrayList<>();
		ContractPersonVO result 			= new ContractPersonVO();
		logger.info("[sendContractExcel]  기업id -> "+bizId);
		int rowNum 		 = 0;
		int rowCnt 		 = 0;
		String errString = "";

		try {
			File file = new File(xlsPath);
			if (!file.exists()) {
				logger.info("[sendContractExcel] XLS파일이 존재하지 않습니다.");
				return null;
			}

			Workbook workbook =  Workbook.getWorkbook(file);

			Sheet[] sheets = workbook.getSheets();

			for (int s=0; s<sheets.length; s++) {
				if (s != 0) break;
				Sheet sheet = sheets[s];
				int colNum  = sheet.getColumns();
				rowNum    	= sheet.getRows();
				rowCnt    	= sheet.getRows();

				logger.info("sheet=>"+sheet.getName()+"_"+colNum+"_"+rowNum);

				for (int r=3; r < rowNum; r++) {

					String empNo 		= ExcelUtil.getCellValue(sheet.getCell(1, r));
					String contractDate = ExcelUtil.getCellValue(sheet.getCell(2, r));
					contractDate 		= StringUtil.StringReplace(contractDate);

					if (StringUtil.isNotNull(empNo) && StringUtil.isNotNull(contractDate)) {

						try {
							SimpleDateFormat dateFormatParser = new SimpleDateFormat("yyyyMMdd"); //검증할 날짜 포맷 설정
							dateFormatParser.setLenient(false);   //false일경우 처리시 입력한 값이 잘못된 형식일 시 오류가 발생
							dateFormatParser.parse(contractDate); //대상 값 포맷에 적용되는지 확인
						} catch (Exception e) {
							logger.info("생성일자의 형식이 올바르지 않습니다.");

							if (errString.isEmpty()) errString += r+1;
							else 					 errString += ", "+(r+1);
							continue;
						}

						logger.info("[sendContractExcel] 조회->" + empNo);

						EmpVO empVO = new EmpVO();
						empVO.setBizId(bizId);
						empVO.setEmpNo(empNo);
						empVO.setEmpType("1");

						EmpVO resultVO 		   = null;
						EmpVO resultCustomerVO = null;

						// 엑셀 데이터 인사 목록 조회
						List<EmpVO> resultEmpList = empDAO.getEmpNoCheck(empVO);

						if (resultEmpList.size() == 1) resultVO = empDAO.getEmpNo(empVO);

						empVO.setBusinessNo(empNo);

						// 엑셀 데이터 거래처 목록 조회
						List<EmpVO> resultCustomerList = empDAO.getCustomerCheck(empVO);

						if (resultCustomerList.size() == 1) resultCustomerVO = empDAO.getCustomer(empVO);

						String targetName 	 = "";
						String targetId 	 = "";
						String targetBizName = "";

						// 엑셀 데이터 인사 목록 존재시
						if (resultVO != null) {
							targetName	  = resultVO.getEmpName();
							targetId	  = resultVO.getUserId();
							targetBizName = resultVO.getBizName();
						}

						// 엑셀 데이터 거래처 목록 존재시
						if (resultCustomerVO != null) {
							targetName	  = resultCustomerVO.getCustomerName();
							targetId	  = resultCustomerVO.getCustomerId();
							targetBizName = resultCustomerVO.getBizName();
						}

						if (resultVO != null || resultCustomerVO != null) {

							logger.info("[sendContractExcel] 대상->" + targetName);
							ContractPersonVO contractVO = new ContractPersonVO();
							contractVO.setServiceId(ContractService.SERVICE_ID);
							contractVO.setBizId(bizId);
							contractVO.setUserId(targetId);
							contractVO.setContractDate(contractDate);
							String contractName = XmlDataUtil.dateHFormat(contractDate) + " " + targetBizName + "과 " + targetName+"님의 전자문서";
							contractVO.setContractFileId(fileId);
							contractVO.setContractName(contractName);
							contractVO.setContractType("1");
							contractVO.setContractId("");
							contractVO.setEndDate("");
							contractVO.setKeepDate("");
							contractVO.setDigitSign("");
							contractVO.setDigitNonce("");
							contractVO.setStatusCode("1");
							contractVO.setTransType("N");
							contractVO.setEmpName(targetName);

							ContractPersonNewVO paramNewVO = new ContractPersonNewVO();
							paramNewVO.setBizId(bizId);
							paramNewVO.setContractId(fileId);
							List<ContractPersonNewVO> resNewList = contractDAO.getContractPersonNewList(paramNewVO);

							if (resNewList.isEmpty()) {
								logger.info("양식 정보가 존재하지 않습니다.");

								result.setOrgFileDataCount("0");
								result.setKeepCount("0");

								return result;
							}

							ContractPersonNewFormVO paramVO = new ContractPersonNewFormVO();
							paramVO.setBizId(bizId);
							paramVO.setFileId(resNewList.get(0).getFileId());

							//contractId -> fileId
							List<ContractPersonNewFormVO> resNewFormList = contractDAO.getContractPersonNewFormList(paramVO);

							int formRowCnt = 1;

							logger.info("원본 칼럼 갯수 : "+resNewFormList.size());

							for (ContractPersonNewFormVO tmpData : resNewFormList) logger.info("컬럼명 : "+tmpData.getFormDataName()+", 종류 : "+tmpData.getFormDataId());

							logger.info("엑셀에서 올라온 데이터 : "+colNum);

							if (resNewFormList.size() != colNum-3) {
								result.setResultMessage("문서양식과 엑셀양식이 동일하지 않습니다.");

								result.setOrgFileDataCount(Integer.toString(rowNum-3));
								result.setKeepCount("0");

								workbook.close();
								file.delete();

								return result;
							}

							for (int c=3; c<colNum; c++) {
								String formDataId     = "TXT_"+StringUtil.lpad(Integer.toString(formRowCnt), 5, "0");
								String formDataValue  = ExcelUtil.getCellValue(sheet.getCell(c, r));
								String formDataName   = ExcelUtil.getCellValue(sheet.getCell(c, 1));
								String formDataType   = ExcelUtil.getCellValue(sheet.getCell(c, 0));
								ContractFormVO formVO = new ContractFormVO();
								formVO.setServiceId(ContractService.SERVICE_ID);
								formVO.setBizId(bizId);
								formVO.setUserId(targetId);
								formVO.setEmpName(targetName);
								formVO.setContractDate(contractDate);
								formVO.setFormDataId(formDataId);
								formVO.setFormDataName(formDataName);

								if (formDataType.trim().equalsIgnoreCase("AMT")) {
									formDataValue = formDataValue.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
								} else if (formDataType.trim().equalsIgnoreCase("DATE1")) {
									formDataValue = formDataValue.replaceAll("[^0-9]","");
									formDataValue = formDataValue.replaceAll("(\\d{4})(\\d{1,2})(\\d{1,2})", "$1년 $2월 $3일");
								} else if (formDataType.trim().equalsIgnoreCase("DATE2")) {
									formDataValue = formDataValue.replaceAll("[^0-9]","");
									formDataValue = formDataValue.replaceAll("(\\d{4})(\\d{1,2})(\\d{1,2})", "$1.$2.$3");
								} else if (formDataType.trim().equalsIgnoreCase("DATE3")) {
									formDataValue = formDataValue.replaceAll("[^0-9]","");
									formDataValue = formDataValue.replaceAll("(\\d{4})(\\d{1,2})(\\d{1,2})", "$1-$2-$3");
								} else if (formDataType.trim().equalsIgnoreCase("PHONE")) {
									formDataValue = formDataValue.replaceAll("[^0-9]","");
									formDataValue = formDataValue.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
								} else if (formDataType.trim().equalsIgnoreCase("OR")) {
									formDataValue = formDataValue.replaceAll("[^0-9]","");
									if(formDataValue.length() == 10) {
										formDataValue = formDataValue.replaceAll("(\\d{3})(\\d{2})(\\d{5})", "$1-$2-$3");
									} else if (formDataValue.length() == 8) {
										formDataValue = formDataValue.replaceAll("(\\d{4})(\\d{1,2})(\\d{1,2})", "$1년 $2월 $3일");
									}
								}

								formVO.setFormDataValue(formDataValue);
								contractVO.addForm(formVO);
								formRowCnt++;
							}
							contractList.add(contractVO);
						} else {
							if (errString.isEmpty()) errString += r+1;
							else 					 errString += ", "+(r+1);
						}
					} else { rowCnt--; }

					// 웹소켓 진행률 표시
					int start = r-2;
					int end	  = rowNum -3;
					String message = "엑셀 확인중입니다.\n" + start + "건 / " + end + " 건";
					Websocket.sendMessage(webSocketKey, message);
				}
			}

			int count = 0;
			// 계약정보 등록(등록수정)
			for (int i=0; i<contractList.size(); i++) {

				ContractPersonVO formVO = contractList.get(i);
				result.setEmpName(formVO.getEmpName());

				// 웹소켓 진행률 표시
				int start = i+1;
				int end   = contractList.size();
				String message = "계약정보 등록 중입니다.\n" + start + "건 / " +  end + " 건";
				Websocket.sendMessage(webSocketKey, message);

				// 계약정보 등록
				String insContId = contractDAO.insContractPerson(formVO);

				logger.info("[sendContractExcel] 등록 처리->" + insContId+"/"+formVO.getEmpName() + "/" + formVO.getUserId() + "/"+formVO.getContractDate());
				// 계약건수
				count++;

				// 계약정보 폼 입력항목 등록
				List<ContractFormVO> tranDataList = formVO.getFormList();

				for (int t=0; t<tranDataList.size(); t++) {
					ContractFormVO tranData = tranDataList.get(t);
					tranData.setContId(insContId);
					contractDAO.insContractForm(tranData);
				}
				logger.info("[sendContractExcel] 폼 등록 처리->" + insContId+"/" + formVO.getEmpName() + "/" + formVO.getUserId() + "/"+formVO.getContractDate()+"/"+tranDataList.size());
			}

			logger.info("[sendContractExcel] 데이터 건수 ->"+(rowCnt-3)+"처리건수->"+count);
			result.setOrgFileDataCount(Integer.toString(rowCnt-3));
			result.setKeepCount(Integer.toString(count));
			result.setResultMessage2(errString);
			workbook.close();
			file.delete();

		} catch (Exception ex) { ex.printStackTrace(); }

		return result;
	}


	// PDF 생성 (개별등록)
	@Override
	public List<ContentVO> createContractPDF(ContractPersonVO vo, SessionVO loginVO) throws Exception {
		ContentVO contentVO		   = new ContentVO();
		CabinetVO cabinetVO		   = new CabinetVO();
		List<ContentVO> resultList = new ArrayList<>();
		List<ContentVO> delList    = new ArrayList<>();

		vo.setResultCode("0");

		ContractPersonVO contractPersonVO = new ContractPersonVO();
		contractPersonVO.setBizId(vo.getBizId());
		contractPersonVO.setContractFileId(vo.getContractFileId());
		contractPersonVO.setContractDate("%");
		contractPersonVO.setTransType("N");
		contractPersonVO.setStartPage(0);
		contractPersonVO.setEndPage(10000);

		List<ContractPersonVO> contractList = contractDAO.getContractPersonList(contractPersonVO);

        for (ContractPersonVO resultContractVO : contractList) {

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
                logger.error("[createContractPersonPDF] 콘텐츠 정보가 존재하지 않습니다.");
                vo.setResultCode("200");
                vo.setResultMessage("콘텐츠 정보가 존재하지 않습니다.");
                return null;
            }

            // 저장위치
            cabinetVO.setClassId(resultVO.getClassId());
            cabinetVO.setCabinetId(resultVO.getCabinetId());
            CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
            if (cabinetResultVO == null) {
                logger.error("[createContractPersonPDF] 캐비넷 정보가 존재하지 않습니다.");
                vo.setResultCode("210");
                vo.setResultMessage("캐비넷 정보가 존재하지 않습니다.");
                return null;
            }

            String originalPdfPath = cabinetResultVO.getCabinetPath() + resultVO.getFilePath() + resultVO.getFileName();
            String targetPdfName   = StringUtil.getUUID() + ".pdf";
            String targetPdfPath   = MultipartFileUtil.getSystemTempPath() + targetPdfName;

            // 사용할 PDF복사
            FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
            if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
                List<FieldVO> fieldListVO = new ArrayList<>();

                // 계약서 정보
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

                //파일생성
                boolean bWritePDF = ControlUtil.writePDF(fieldListVO, originalPdfPath, targetPdfPath, false, false, System.getProperty("pdf.font.default"));

                //콘텐츠등록
                if (bWritePDF) {
                    contentVO = new ContentVO();
                    contentVO.setFileType(FileUtil.getFileExt(targetPdfPath));
                    contentVO.setFileName(targetPdfName);
                    contentVO.setOrgFileName(resultContractVO.getEmpName() + "_" + resultVO.getFileTitle() + ".pdf");
                    contentVO.setFileSize(Long.toString(FileUtil.getFileSize(targetPdfPath)));
                    contentVO.setFilePath(MultipartFileUtil.getSystemTempPath());
                    contentVO.setFileTitle(resultContractVO.getEmpName() + "_" + resultVO.getFileTitle());
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
                    if (ContentService.COMPLETED == nResult) {
                        // 상태코드 업데이트 및 보관일자 90일 추가
                        String keepDate = DateUtil.getAddDate(30 * 3);
                        contractPersonVO.setContId(resultContractVO.getContId());
                        String hashData = PKIUtil.getFileHash(targetPdfPath);

                        if (hashData == null || hashData.isEmpty()) {
                            logger.error("[createContractPersonPDF] hash 생성에 실패하였습니다.");
                            vo.setResultCode("300");
                            vo.setResultMessage("hash 생성에 실패하였습니다.");
                            return null;
                        }

                        contractPersonVO.setHashData(hashData);
                        contractPersonVO.setStatusCode("1");
                        contractPersonVO.setContractId(contentVO.getFileId());
                        contractPersonVO.setTransType("Y");
                        contractPersonVO.setKeepDate(keepDate);

                        // 계약정보 업데이트
                        contractDAO.updContractPerson(contractPersonVO);

                        // 로그내역 등록
                        logger.info("로그 등록 contId =>" + resultContractVO.getContId());
                        ContractPersonLogVO logVO = new ContractPersonLogVO();
                        logVO.setContId(resultContractVO.getContId());
                        logVO.setServiceId(SERVICE_ID);
                        logVO.setBizId(resultContractVO.getBizId());
                        logVO.setUserId(resultContractVO.getUserId());
                        logVO.setContractDate(resultContractVO.getContractDate());
                        logVO.setContractId(contentVO.getFileId());
                        logVO.setLogType("1");
                        logVO.setLogMessage(resultContractVO.getEmpName() + "님의 전자문서를 생성하였습니다.");
                        logVO.setInsEmpNo(loginVO.getUserId());
                        contractDAO.insContractPersonLog(logVO);

                        logger.info("[createContractPersonPDF]  [" + contentVO.getFileId() + "]TempFile Delete : " + targetPdfPath);
                        FileUtil.deleteFile(targetPdfPath);

                        resultList.add(contentVO);
                    } else {
                        logger.error("[createContractPersonPDF] tractContent ResulCode : " + nResult);
                    }
                }
            }
        }

		if (!delList.isEmpty()) {
			logger.info("[createContractPersonPDF] 기존 계약서 삭제처리");
			contentService.deleteContent(delList);
		}

		return resultList;
	}

	
	// 계약서 생성
	@Override
	public List<ContractPersonVO> createContractPDFList(ContractPersonVO vo, SessionVO loginVO, String webSocketKey) throws Exception {

		ContentVO contentVO    	   = new ContentVO();
		CabinetVO cabinetVO		   = new CabinetVO();
		List<ContentVO> resultList = new ArrayList<>();
		List<ContentVO> delList    = new ArrayList<>();

		vo.setResultCode("0");

		ContractPersonVO contractPersonVO = new ContractPersonVO();
		contractPersonVO.setBizId(vo.getBizId());
		contractPersonVO.setContractFileId(vo.getContractFileId());
		contractPersonVO.setContractDate("%");
		contractPersonVO.setTransType("N");
		contractPersonVO.setStartPage(0);
		contractPersonVO.setEndPage(10000);

		List<ContractPersonVO> contractList    = contractDAO.getContractPersonList(contractPersonVO);
		List<ContractPersonVO> resContractList = new ArrayList<>();

		for (int i=0; i<contractList.size(); i++) {
			ContractPersonVO resultContractVO = contractList.get(i);

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
				logger.error("[createContractPersonPDF] 콘텐츠 정보가 존재하지 않습니다.");
				vo.setResultCode("200");
				vo.setResultMessage("콘텐츠 정보가 존재하지 않습니다.");
				return null;
			}

			// 저장위치
			cabinetVO.setClassId(resultVO.getClassId());
			cabinetVO.setCabinetId(resultVO.getCabinetId());
			CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
			if (cabinetResultVO == null) {
				logger.error("[createContractPersonPDF] 캐비넷 정보가 존재하지 않습니다.");
				vo.setResultCode("210");
				vo.setResultMessage("캐비넷 정보가 존재하지 않습니다.");
				return null;
			}

			String originalPdfPath = cabinetResultVO.getCabinetPath()+resultVO.getFilePath()+resultVO.getFileName();
			String targetPdfName   = StringUtil.getUUID()+".pdf";
			String targetPdfPath   = MultipartFileUtil.getSystemTempPath()+targetPdfName;

			// 사용할 PDF복사
			FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
			if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
				List<FieldVO> fieldListVO = new ArrayList<>();
				// 계약서 정보
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

					resContractList.add(resultContractVO);

					int nResult = contentService.transContent(contentVO);
					if (ContentService.COMPLETED == nResult) {
						// 상태코드 업데이트 및 보관일자 90일 추가
						String keepDate = DateUtil.getAddDate(30*3);
						contractPersonVO.setContId(resultContractVO.getContId());
						String hashData = PKIUtil.getFileHash(targetPdfPath);

						if (hashData == null || hashData.isEmpty()) {
							logger.error("[createContractPersonPDF] hash 생성에 실패하였습니다.");
							vo.setResultCode("300");
							vo.setResultMessage("hash 생성에 실패하였습니다.");
							return null;
						}

						contractPersonVO.setHashData(hashData);
						contractPersonVO.setStatusCode("1");
						contractPersonVO.setContractId(contentVO.getFileId());
						contractPersonVO.setTransType("Y");
						contractPersonVO.setKeepDate(keepDate);

						// 계약정보 업데이트
						contractDAO.updContractPerson(contractPersonVO);

						// 로그내역 등록
						logger.info("로그 등록 contId =>"+resultContractVO.getContId());
						ContractPersonLogVO logVO = new ContractPersonLogVO();
						logVO.setContId(resultContractVO.getContId());
						logVO.setServiceId(SERVICE_ID);
						logVO.setBizId(resultContractVO.getBizId());
						logVO.setUserId(resultContractVO.getUserId());
						logVO.setContractDate(resultContractVO.getContractDate());
						logVO.setContractId(contentVO.getFileId());
						logVO.setLogType("1");
						logVO.setLogMessage(resultContractVO.getEmpName()+"님의 전자문서를 생성하였습니다.");
						logVO.setInsEmpNo(loginVO.getUserId());
						contractDAO.insContractPersonLog(logVO);

						logger.info("[createContractPersonPDF]  ["+contentVO.getFileId()+"]TempFile Delete : "+targetPdfPath);
						FileUtil.deleteFile(targetPdfPath);
						resultList.add(contentVO);

						// 웹소켓 진행률 표시
						int start = i+1;
						int end   = contractList.size();

						String message = "전자문서 생성중 입니다.\n" + start + "건 / " + end + " 건";
						Websocket.sendMessage(webSocketKey, message);

					} else {
						logger.error("[createContractPersonPDF] tractContent ResulCode : " + nResult);
					}
				}
			}
		}

		if (!delList.isEmpty()) {
			logger.info("[createContractPersonPDF] 기존 계약서 삭제처리");
			contentService.deleteContent(delList);
		}
		return resContractList;
	}


	// 스캔파일 등록
	@Override
	public ContractPersonVO uploadContractFile(ContractPersonVO vo, FileVO fileVO, SessionVO loginVO) throws Exception {
		ContractPersonVO result = new ContractPersonVO();
		logger.info("[uploadContractFile]  기업id -> "+vo.getBizId());

		try {
			result.setResultCode("0");

			EmpVO empVO = new EmpVO();
			EmpVO resultEmpVO;
			empVO.setBizId(vo.getBizId());
			empVO.setUserId(vo.getUserId());
			empVO.setStartPage(0);
			empVO.setEndPage(10);
			List<EmpVO> empList = empDAO.getEmpList(empVO);

			if (empList == null || empList.isEmpty()) {
				logger.error("[uploadContractFile] 사원 정보가 존재하지 않습니다.");
				vo.setResultMessage("사원 정보가 존재하지 않습니다.");
				return null;
			} else { resultEmpVO=empList.get(0); }

			// 스캔파일 등록시 포인트 차감
			PointVO pointVO = new PointVO();
			pointVO.setBizId(vo.getBizId());
			PointVO resultPointVO = pointDAO.getPoint(pointVO);
			if (resultPointVO == null) {
				logger.error("[uploadContractFile] 포인트정보가 존재하지 않습니다.");
				result.setResultMessage("포인트가 없습니다.");
				return null;
			}

			int reducePoint = 0;
			BizGrpVO grpVO  = new BizGrpVO();
			grpVO.setBizId(vo.getBizId());
			List<BizGrpVO> pointList = bizDAO.getBizGrp(grpVO);

			if (pointList == null) {
				logger.error("[uploadContractFile] 기업 조회에 실패하였습니다.");
				vo.setResultMessage("기업 조회에 실패하였습니다.");
				return null;
			} else {
				//현재 보유중인 포인트
				int point = resultPointVO.getCurPoint();
				//스캔파일 등록 차감 포인트
				if (StringUtil.isNotNull(pointList.get(0).getScanPoint())) reducePoint = Integer.parseInt(pointList.get(0).getScanPoint());

				if ((point - reducePoint) < 0) {
					logger.error("[uploadContractFile] 잔여 포인트가 존재하지 않습니다. 현재포인트 : <"+point+">, 차감 포인트 : <"+reducePoint+">");
					vo.setResultMessage("잔여 포인트가 존재하지 않습니다. 현재포인트 : <"+point+">, 차감 포인트 : <"+reducePoint+">");
					return null;
				}
			}

			String filePath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
			File file 		= new File(filePath);

			if (!file.exists()) {
				logger.error("[uploadContractFile] 복사본 파일이 존재하지 않습니다.");
				vo.setResultMessage("복사본 파일이 존재하지 않습니다.");
				return null;
			}

			ContentVO contentVO = new ContentVO();
			contentVO.setFileType(FileUtil.getFileExt(filePath));
			contentVO.setFileName(fileVO.getFileStreNm());
			contentVO.setOrgFileName(fileVO.getFileStreOriNm());
			contentVO.setFileSize(Long.toString(FileUtil.getFileSize(filePath)));
			contentVO.setFilePath(fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR);
			contentVO.setFileTitle(StringUtil.ReplaceAll(fileVO.getFileStreOriNm(), "."+fileVO.getFileExt(), ""));
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
			if (ContentService.COMPLETED == nResult) {
				// 상태코드 업데이트 및 보관일자 3년 추가
				String keepDate = DateUtil.getAddDate(365*3);

				// 계약서 등록(등록수정)
				ContractPersonVO insContractVO = new ContractPersonVO();
				insContractVO.setServiceId(ContractService.SERVICE_ID);
				insContractVO.setBizId(vo.getBizId());
				insContractVO.setUserId(vo.getUserId());
				insContractVO.setContractDate(vo.getContractDate());
				String contractName = XmlDataUtil.dateHFormat(vo.getContractDate()) + " " + resultEmpVO.getBizName() + "과 " + resultEmpVO.getEmpName()+"님의 전자계약";
				insContractVO.setContractFileId(vo.getContractFileId());
				insContractVO.setContractName(contractName);
				insContractVO.setContractType("1");
				insContractVO.setContractId(contentVO.getFileId());
				insContractVO.setEndDate(DateUtil.getTimeStamp(3));
				insContractVO.setKeepDate(keepDate);
				insContractVO.setDigitSign("");
				insContractVO.setDigitNonce("");
				insContractVO.setStatusCode("Y");
				insContractVO.setTransType("Y");
				insContractVO.setInputType("20");
				insContractVO.setEmpName(resultEmpVO.getEmpName());
				String contId = contractDAO.insContractPerson(insContractVO);

				// 로그내역 등록
				ContractPersonLogVO logVO = new ContractPersonLogVO();
				logVO.setContId(contId);
				logVO.setServiceId(ContractService.SERVICE_ID);
				logVO.setBizId(vo.getBizId());
				logVO.setUserId(vo.getUserId());
				logVO.setContractDate(vo.getContractDate());
				logVO.setContractId(contentVO.getFileId());
				logVO.setLogType("Y");
				logVO.setLogMessage(resultEmpVO.getEmpName()+"님의 전자문서 파일을 업로드하였습니다.");
				logVO.setInsEmpNo(loginVO.getUserId());
				contractDAO.insContractPersonLog(logVO);

				logger.info("[createContractPersonPDF]  ["+contentVO.getFileId()+"]TempFile Delete : "+filePath);
				FileUtil.deleteFile(filePath);

				// 포인트 차감
				pointVO.setCurPoint(reducePoint);
				pointDAO.balancePoint(pointVO);

				PointLogVO pointLogVO = new PointLogVO();
				pointLogVO.setBizId(vo.getBizId());
				pointLogVO.setUserId(vo.getUserId());
				pointLogVO.setServiceId(ContractService.SERVICE_ID);
				pointLogVO.setPointType("2");
				pointLogVO.setPointPrice(pointVO.getCurPoint());
				pointLogVO.setPointResult(resultPointVO.getCurPoint()-pointVO.getCurPoint());
				pointLogVO.setEtcDesc("전자문서 업로드 포인트 차감");
				logger.info("스캔파일 등록 <"+reducePoint+">포인트 차감");

				pointDAO.insPointLog(pointLogVO);

				result.setResultMessage("파일이 등록되었습니다.");
				result.setResultCode("1");
			}
		} catch (Exception ex) { ex.printStackTrace(); }

		return result;
	}


	// 계약난수 조회
	@Override
	public ContractPersonVO getContractNonce(ContractPersonVO vo) { return contractDAO.getContractPersonNonce(vo); }


	// pdf 미리보기
	@Override
	public ContractPersonVO getContractView(ContractPersonVO vo, SessionVO loginVO) {

		ContractPersonVO result = contractDAO.getContractPerson(vo);

		// 콘텐츠에서 정보 추출
		ContentVO contentVO = new ContentVO();
		contentVO.setFileId(result.getContractId());
		ContentVO resultVO = contentDAO.getContent(contentVO);
		if (resultVO == null) {
			logger.error("[getContractView] 콘텐츠 정보가 존재하지 않습니다.");
			return null;
		}

		// 저장위치
		CabinetVO cabinetVO = new CabinetVO();
		cabinetVO.setClassId(resultVO.getClassId());
		cabinetVO.setCabinetId(resultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO == null) {
			logger.error("[getContractView] 캐비넷 정보가 존재하지 않습니다.");
			return null;
		}

		String originalPdfPath = cabinetResultVO.getCabinetPath()+resultVO.getFilePath()+resultVO.getFileName();
		String targetPdfName = StringUtil.getUUID()+".pdf";
		String targetPdfPath = MultipartFileUtil.getSystemTempPath()+targetPdfName;

		// 사용할 PDF복사
		FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
		if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
			String pdf_uri = StringUtil.ReplaceAll(targetPdfPath, getSystemPath(), "");
			result.setPdfFile(pdf_uri);
		}

		if (StringUtil.isNotNull(loginVO.getIpAddr())){
			List<String> userIdList = new ArrayList<>();
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


	// 근로계약양식 폼
	@Override
	public ContractPersonNewVO getContractFormView(ContractPersonNewVO vo) {
		List<ContractPersonNewVO> contractFormList = contractDAO.getContractPersonNewList(vo);
		ContractPersonNewVO result 				   = contractFormList.get(0);

		// 콘텐츠에서 정보 추출
		ContentVO contentVO = new ContentVO();
		contentVO.setFileId(result.getContractId());
		ContentVO resultVO = contentDAO.getContent(contentVO);

		if (resultVO == null) {
			logger.error("[getContractView] 콘텐츠 정보가 존재하지 않습니다.");
			return null;
		}

		// 저장위치
		CabinetVO cabinetVO = new CabinetVO();
		cabinetVO.setClassId(resultVO.getClassId());
		cabinetVO.setCabinetId(resultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO == null) {
			logger.error("[getContractView] 캐비넷 정보가 존재하지 않습니다.");
			return null;
		}

		String originalPdfPath = cabinetResultVO.getCabinetPath()+resultVO.getFilePath()+resultVO.getFileName();
		String targetPdfName   = StringUtil.getUUID()+".pdf";
		String targetPdfPath   = MultipartFileUtil.getSystemTempPath()+targetPdfName;

		// 사용할 PDF복사
		FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
		if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
			String pdf_uri = StringUtil.ReplaceAll(targetPdfPath, getSystemPath(), "");
			result.setPdfFile(pdf_uri);
		}
		return result;
	}


	//완료된 계약서 조회
	@Override
	public List<ContractPersonVO> getContractPersonList(ContractPersonVO vo)  throws Exception { return contractDAO.getContractPersonList(vo); }

	
	// 계약 건수 
	@Override
	public int getContractPersonListCount(ContractPersonVO vo) throws Exception { return contractDAO.getContractPersonListCount(vo); }


	// 계약서 다운로드 수
	@Override
	public int getContractPersonDownloadCount(ContractPersonVO vo) { return contractDAO.getContractPersonDownloadCount(vo); }


	// 계약서 다운로드 수 (부문관리자)
	@Override
	public int getContractPersonUserGrpDownloadCount(ContractPersonVO vo) { return contractDAO.getContractPersonUserGrpDownloadCount(vo); }


	// 전자계약 로그 리스트
	@Override
	public List<ContractPersonLogVO> getContractPersonLogList(ContractPersonLogVO vo)  throws Exception { return contractDAO.getContractPersonLogList(vo); }


	// 계약양식 폼 리스트 조회
	@Override
	public List<ContractFormVO> getContractFormList(ContractFormVO vo)  throws Exception {

		List<ContractFormVO> list = contractDAO.getContractFormList(vo);

		for (ContractFormVO rVO : list) {
			if (rVO.getEncodeYn().equals("Y")) {
				try 				{ rVO.setFormDataValue(SecurityUtil.getinstance().aesDecode(rVO.getFormDataValue())); }
				catch (Exception e) { rVO.setFormDataValue(""); }
			}
		}
		return list;
	}

	
	// 계약 양식 폼 수정
	@Override
	public int updContractForm(ContractFormVO vo) throws Exception {
		int result 			  = 0;
		ContractFormVO dataVO = new ContractFormVO();
		String[] formList	  = StringUtil.split(vo.getFormDataValue(), "|");
        for (String s : formList) {
            String[] formValue = StringUtil.split(s, "^");
            if (formValue.length > 1) {
                dataVO.setContId(vo.getContId());
                dataVO.setServiceId(ContractService.SERVICE_ID);
                dataVO.setBizId(vo.getBizId());
                dataVO.setUserId(vo.getUserId());
                dataVO.setContractDate(vo.getContractDate());
                dataVO.setFormDataId(formValue[0]);
                String encodeYn = contractDAO.getEncodeYn(dataVO);

                if (encodeYn.equals("Y")) dataVO.setFormDataValue(SecurityUtil.getinstance().aesEncode(URLDecoder.decode(formValue[1])));
                else 					  dataVO.setFormDataValue(URLDecoder.decode(formValue[1], StandardCharsets.UTF_8.name()));

                List<ContractFormVO> list = contractDAO.getContractFormList(dataVO);

                if (!list.isEmpty()) contractDAO.updContractForm(dataVO);
                else 				 contractDAO.insContractForm(dataVO);

                result++;
            }
        }
		return result;
	}


	// 계약서 양식 삭제
	@Override
	public ContractPersonNewVO delContractPersonNew(ContractPersonNewVO vo) {

		int result 		  = 0;
		String[] fileList = StringUtil.split(vo.getFileId(), "|");

		//해당 양식으로 작성된 계약서가 있을 경우 양식 삭제 불가능
		for (int i=0; i<fileList.length-1; i++) {
			String fileId 			= fileList[i];
			ContractPersonVO dataVO = new ContractPersonVO();
			dataVO.setServiceId(ContractService.SERVICE_ID);
			dataVO.setBizId(vo.getBizId());
			dataVO.setFileId(fileId);
			int count = contractDAO.getContractPersonWriteCount(dataVO);
			if (count > 0) {
				vo.setMessage("해당 양식으로 작성 된 전자문서가 존재합니다.");
				vo.setResult(0);
				return vo;
			}
		}

		for (int i=0; i<fileList.length-1; i++) {
			String fileId 			   = fileList[i];
			ContractPersonNewVO dataVO = new ContractPersonNewVO();
			dataVO.setServiceId(ContractService.SERVICE_ID);
			dataVO.setBizId(vo.getBizId());
			dataVO.setFileId(fileId);

			contractDAO.delContractNew(dataVO);
			result++;
		}
		vo.setResult(result);
		return vo;
	}


	// 전자계약 양식 리스트 조회
	@Override
	public List<ContractPersonNewVO> getContractPersonNewList(ContractPersonNewVO vo)  throws Exception { return contractDAO.getContractPersonNewList(vo); }


	// 양식 변환관리 리스트 조회
	@Override
	public List<ContractPersonNewVO> getContractTransformList(ContractPersonNewVO vo) { return contractDAO.getContractTransformList(vo); }


	// 양식 변환관리 리스트 수 조회
	@Override
	public ContractPersonNewVO getContractTransformListCount(ContractPersonNewVO vo) { return contractDAO.getContractTransformListCount(vo); }


	// 양식 변환관리 리스트 상태 수 조회
	@Override
	public ContractPersonNewVO getContractTransformStatusListCount(ContractPersonNewVO vo) { return contractDAO.getContractTransformStatusListCount(vo); }


	// 양식등록하기
	@Override
	public ContractPersonNewVO contractNewUpload(ContractPersonNewVO vo, FileVO fileVO) {
		ContractPersonNewVO result = new ContractPersonNewVO();
		logger.info("[contractNewUpload]  기업id -> "+vo.getBizId());

		try {
			String bizId = vo.getBizId();

			// 양식등록시 포인트 차감
			PointVO pointVO = new PointVO();
			pointVO.setBizId(bizId);
			PointVO resultPointVO = pointDAO.getPoint(pointVO);
			if (resultPointVO == null) {
				logger.error("[contractNewUpload] 포인트정보가 존재하지 않습니다.");
				result.setMessage("포인트가 없습니다.");
				return result;
			}

			int reducePoint 				= 0;
			String systemAdminUploadMessage = "";
			BizGrpVO grpVO 					= new BizGrpVO();
			grpVO.setBizId(bizId);
			List<BizGrpVO> pointList = bizDAO.getBizGrp(grpVO);

			if (pointList == null) {
				logger.error("[contractNewUpload] 기업 조회에 실패하였습니다.");
				result.setMessage("기업 조회에 실패하였습니다.");
				return result;
			} else {
				//현재 보유중인 포인트
				int point = resultPointVO.getCurPoint();
				//양식 등록 차감 포인트
				if (StringUtil.isNotNull(pointList.get(0).getFormPoint())) {
					reducePoint = Integer.parseInt(pointList.get(0).getFormPoint());
					if ("9".equals(vo.getInsUserType())) {
						reducePoint = 0;
						logger.info("시스템관리자 양식등록 포인트 0으로 세팅");
						systemAdminUploadMessage = "(시스템관리자 등록)";
					}
				}
				if ((point - reducePoint)<0) {
					logger.error("[contractNewUpload] 잔여 포인트가 존재하지 않습니다. 현재포인트 : <"+point+">, 차감 포인트 : <"+reducePoint+">");
					result.setMessage("잔여 포인트가 존재하지 않습니다. 현재포인트 : <"+point+">, 차감 포인트 : <"+reducePoint+">");
					return result;
				}
			}

			String filePath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
			File file 		= new File(filePath);
			if (!file.exists()) {
				logger.error("[contractNewUpload] 복사본 파일이 존재하지 않습니다.");
				return null;
			}
			ContentVO contentVO = new ContentVO();
			contentVO.setFileType(FileUtil.getFileExt(filePath));
			contentVO.setFileName(fileVO.getFileStreNm());
			contentVO.setOrgFileName(fileVO.getFileStreOriNm());
			contentVO.setFileSize(Long.toString(FileUtil.getFileSize(filePath)));
			contentVO.setFilePath(fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR);
			contentVO.setFileTitle(StringUtil.ReplaceAll(fileVO.getFileStreOriNm(), "."+fileVO.getFileExt(), ""));
			contentVO.setClassId(ContractService.UPLOAD_CLASS_ID);
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
				result.setServiceId(ContractService.SERVICE_ID);
				result.setBizId(bizId);
				result.setFileId(contentVO.getFileId());
				result.setContractType("1");
				result.setSignBizType(vo.getSignBizType());
				result.setSignUserType(vo.getSignUserType());
				result.setSignCustomerType(vo.getSignCustomerType());
				result.setFontConvertType(vo.getFontConvertType());
				result.setCorrectionType(vo.getCorrectionType());
				result.setLaborType(vo.getLaborType());
				result.setTransType("N");
				result.setFileTitle(contentVO.getFileTitle());

				//양식추가
				contractDAO.insContractNew(result);

				// 포인트 차감 (자사 양식 등록)
				pointVO.setCurPoint(reducePoint);
				pointDAO.balancePoint(pointVO);

				PointLogVO pointLogVO = new PointLogVO();
				pointLogVO.setBizId(bizId);
				pointLogVO.setUserId("");
				pointLogVO.setServiceId("");
				pointLogVO.setPointType("2");
				pointLogVO.setPointPrice(pointVO.getCurPoint());
				pointLogVO.setPointResult(resultPointVO.getCurPoint()-pointVO.getCurPoint());
				pointLogVO.setEtcDesc("자사양식 업로드 차감" + systemAdminUploadMessage);
				logger.info("자사양식 등록 <"+reducePoint+">포인트 차감");

				pointDAO.insPointLog(pointLogVO);

				result.setMessage("파일이 등록되었습니다.");

				//슈퍼관리자에게 이메일 전송
				sendContractNewUploadEmail(bizId, contentVO.getFileId());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}


	// 기업관리자가 양식 등록 시 슈퍼관리자에게 이메일 전송
	public void sendContractNewUploadEmail(String bizId, String fileId) {

		EmpVO vo = new EmpVO();
		vo.setBizId("171220094226009"); //  운영 시스템관리자 bizID
		vo.setEmpType("9");
		vo.setStartPage(-1);
		vo.setEndPage(-1);
		List<EmpVO> result = empDAO.getEmpList(vo);

		//양식 작성 기업검색
		BizVO bizVO = new BizVO();
		bizVO.setBizId(bizId);
		bizVO.setStartPage(0);
		bizVO.setEndPage(1);
		List<BizVO> bizList = bizDAO.getBizList(bizVO);
		BizVO resBizVO;

		if (!bizList.isEmpty()) { resBizVO = bizList.get(0); }
		else 					{ logger.info("슈퍼관리자가 지정되지 않았습니다."); return; }

		//양식검색
		ContractPersonNewVO newVO = new ContractPersonNewVO();
		newVO.setBizId(bizId);
		newVO.setFileId(fileId);
		List<ContractPersonNewVO> newList = contractDAO.getContractPersonNewList(newVO);
		ContractPersonNewVO resNewVO;

		if (!bizList.isEmpty()) { resNewVO = newList.get(0); }
		else 					{ logger.info("양식이 존재하지 않습니다. fildId =>["+fileId+"]"); return; }

		logger.info("----- 양식등록 메일 전송 -----");
        for (EmpVO empVO : result) {

            String content = "";
            content += "<!DOCTYPE html> ";
            content += "<html> ";
            content += "<head> ";
            content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
            content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
            content += "<title>전자문서</title> ";
            content += "</head> ";
            content += "<body> ";
            content += "	<div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
            content += "		<div style=\" padding: 37px 0 0 0;\"> ";
            content += "			<div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
            content += "				<span style=\"font-weight: bold; color:#00a9e9;\">IEUM</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;SIGN</span> ";
            content += "			</div> ";
            content += "			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
            content += "				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">기업관리자가 양식을 등록</span><span class=\"font_black\" style=\"color:#202020;\">하였습니다.</span>";
            content += "			</div> ";
            content += "			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
            content += "				<span > 기업명 : " + resBizVO.getBizName() + "<br> ";
            content += "				<span > 양식명 : " + resNewVO.getFileTitle() + "<br> ";
            content += "				<span > 글꼴맞춤 요청여부 : " + resNewVO.getFontConvertType() + "<br> ";
            content += "			</div> ";
            content += "		<div style=\"margin: 74px 0 32px; text-align: center;\"> ";
            content += "			<a href=\"" + System.getProperty("system.feb.url") + "\" style=\"display: inline-block; padding: 12px 90px; border-radius: 8px; font-family: 'tahoma'; font-size: 18px; font-weight: bold; text-decoration: none; color: #fff; background-color: #00a9e9;\">로그인하기</a>";
            content += "		</div> ";
            content += "		</div> ";
            content += "		<div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
            content += "			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
            content += "				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
            content += "				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">service@newzenpnp.com</a></span > ";
            content += "				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
            content += "			</div> ";
            content += "		</div> ";
            content += "	</div> ";
            content += "</body> ";
            content += "</html> ";

            MultiPartEmail email = new MultiPartEmail();
            MailVO mailVO 		 = new MailVO();

            // 시스템관리자 이메일이 있는 경우에만 수행
            if (empVO.getEMail() != null && !"".equals(empVO.getEMail())) {
                mailVO.setFrom("no_reply@newzenpnp.com");
                mailVO.setTo(empVO.getEMail());
                mailVO.setCc("");
                mailVO.setBcc("");
                mailVO.setSubject("[뉴젠피앤피] " + resBizVO.getBizName() + "업체에서 양식을 등록하였습니다.");
                mailVO.setText(content);
                logger.info("Ieum Sign 전자문서 슈퍼관리자 알림 이메일을 발송. email : " + empVO.getEMail());
                email.send(mailVO);
            }
        }
	}


	// 서명 참여자 수정
 	@Override
	public ContractPersonNewVO contractNewUpdateSign(ContractPersonNewVO vo) {
		logger.info("[contractNewUpdateSign]  기업id -> "+vo.getBizId());
		ContractPersonNewVO result = new ContractPersonNewVO();

		result.setServiceId(ContractService.SERVICE_ID);
		result.setBizId(vo.getBizId());
		result.setFileId(vo.getFileId());
		result.setSignBizType(vo.getSignBizType());
		result.setSignUserType(vo.getSignUserType());
		result.setSignCustomerType(vo.getSignCustomerType());
		result.setLaborType(vo.getLaborType());
		result.setFontConvertType(vo.getFontConvertType());
		result.setCorrectionType(vo.getCorrectionType());
		result.setFileTitle(vo.getFileTitle());
		result.setUseOtherLayout(vo.getUseOtherLayout());
		result.setUseEtc(vo.getUseEtc());
		result.setEtcUrl(vo.getEtcUrl());
		result.setUsePre(vo.getUsePre());
		result.setPreUrl(vo.getPreUrl());
		result.setViewYn(vo.getViewYn());
		result.setOrgImgYn(vo.getOrgImgYn());
		result.setExpireDay(vo.getExpireDay());

		// 진행상태
		List<ContractPersonNewVO> formList = contractDAO.getContractPersonNewList(result);

		if (!formList.isEmpty()) {
			ContractPersonNewVO newVO = formList.get(0);
			// 댓글이 존재하고 불필요 선택시 진행상태변경
			if (!newVO.getTransType().equals("C")) {
				if (vo.getLaborType().equals("0")) {
					if (newVO.getTransType().equals("A")) result.setTransType("T");
				} else {
					if (StringUtil.isNull(newVO.getLastFileId())) result.setTransType("A");
				}
			}
		}
		contractDAO.updContractNewSign(result);
		return result;
	}


	// 양식 상태 수정
	@Override
	public ContractPersonNewVO contractNewUpdateStatus(ContractPersonNewVO vo) {
		logger.info("[contractNewUpdateSign]  기업id -> "+vo.getBizId());
		ContractPersonNewVO result = new ContractPersonNewVO();

		result.setServiceId(ContractService.SERVICE_ID);
		result.setBizId(vo.getBizId());
		result.setFileId(vo.getFileId());
		result.setTransType(vo.getTransType());

		contractDAO.updContractNewStatus(result);

		logger.info("[contractNewUpdateSign] TransType : " + vo.getTransType());

		// 테스트 미리보기 확인 완료 시
		if (vo.getTransType().equals("C")) sendContractNewUpdateEmailC(vo.getBizId());

		return result;
	}


	// 변환 완료된 양식 등록
	@Override
	public ContractPersonNewVO contractNewUpdate(String bizId, String fileId, FileVO pdffileVO, FileVO xlsfileVO, FileVO alterfileVO) {
		ContractPersonNewVO result = new ContractPersonNewVO();
		logger.info("[contractNewUpdate]  기업id -> "+bizId);
		try {
			String pdffilePath = pdffileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+pdffileVO.getFileStreNm();

			File pdffile = new File(pdffilePath);
			if (!pdffile.exists()) {
				logger.error("[contractNewUpdate] PDF 복사본 파일이 존재하지 않습니다.");
				return null;
			}

			String xlsfilePath = xlsfileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+xlsfileVO.getFileStreNm();

			File xlsfile = new File(xlsfilePath);
			if (!xlsfile.exists()) {
				logger.error("[contractNewUpdate] XLS 복사본 파일이 존재하지 않습니다.");
				return null;
			}

			String alterfilePath = alterfileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+alterfileVO.getFileStreNm();

			File alterfile = new File(alterfilePath);
			if (!alterfile.exists()) {
				logger.error("[contractNewUpdate] 수정 파일이 존재하지 않습니다.");
				return null;
			}

			ContentVO contentVO = new ContentVO();
			contentVO.setFileType(FileUtil.getFileExt(pdffilePath));
			contentVO.setFileName(pdffileVO.getFileStreNm());
			contentVO.setOrgFileName(pdffileVO.getFileStreOriNm());
			contentVO.setFileSize(Long.toString(FileUtil.getFileSize(pdffilePath)));
			contentVO.setFilePath(pdffileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR);
			contentVO.setFileTitle(StringUtil.ReplaceAll(pdffileVO.getFileStreOriNm(), ".pdf", ""));
			contentVO.setClassId(ContractService.UPLOAD_CLASS_ID);
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

				ContentVO xlscontentVO = new ContentVO();
				xlscontentVO.setFileType(FileUtil.getFileExt(xlsfilePath));
				xlscontentVO.setFileName(xlsfileVO.getFileStreNm());
				xlscontentVO.setOrgFileName(xlsfileVO.getFileStreOriNm());
				xlscontentVO.setFileSize(Long.toString(FileUtil.getFileSize(xlsfilePath)));
				xlscontentVO.setFilePath(xlsfileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR);
				xlscontentVO.setFileTitle(StringUtil.ReplaceAll(xlsfileVO.getFileStreOriNm(), ".xls", ""));
				xlscontentVO.setClassId(ContractService.UPLOAD_CLASS_ID);
				xlscontentVO.setFileVersion("0");
				xlscontentVO.setParFileId("");
				xlscontentVO.setEtcDesc("");
				xlscontentVO.setCheckInOut("N");
				xlscontentVO.setCheckUserId("");
				xlscontentVO.setCheckCount("0");
				xlscontentVO.setUseYn("Y");
				xlscontentVO.setDelYn("N");

				nResult = contentService.transContent(xlscontentVO);
				if (ContentService.COMPLETED == nResult) {
					ContentVO altercontentVO = new ContentVO();
					altercontentVO.setFileType(FileUtil.getFileExt(alterfilePath));
					altercontentVO.setFileName(alterfileVO.getFileStreNm());
					altercontentVO.setOrgFileName(alterfileVO.getFileStreOriNm());
					altercontentVO.setFileSize(Long.toString(FileUtil.getFileSize(alterfilePath)));
					altercontentVO.setFilePath(alterfileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR);
					altercontentVO.setFileTitle(StringUtil.ReplaceAll(alterfileVO.getFileStreOriNm(), "."+alterfileVO.getFileExt(), ""));
					altercontentVO.setClassId(ContractService.UPLOAD_CLASS_ID);
					altercontentVO.setFileVersion("0");
					altercontentVO.setParFileId("");
					altercontentVO.setEtcDesc("");
					altercontentVO.setCheckInOut("N");
					altercontentVO.setCheckUserId("");
					altercontentVO.setCheckCount("0");
					altercontentVO.setUseYn("Y");
					altercontentVO.setDelYn("N");
					nResult = contentService.transContent(altercontentVO);
					if (ContentService.COMPLETED == nResult) {

						// 데이터 항목 정의
						List xlsData = ExcelUtil.readExcel(xlsfilePath);
						int rownum   = 1;

						// 각 열에 대한 데이터를 저장하기 위한 맵 초기화
						Map<Integer, String> formDataNames 		   = new HashMap<>();
						Map<Integer, String> formDataDefaultValues = new HashMap<>();
						Map<Integer, String> formDataTypes 		   = new HashMap<>();

						// 엑셀 데이터를 먼저 읽어서 각 행의 데이터를 맵에 저장
                        for (Object xlsDatum : xlsData) {
                            HashValue hv = (HashValue) xlsDatum;
                            // 칼럼 타입 저장
                            if (hv.row == 0 && hv.col > 2) formDataTypes.put(hv.col, hv.value);
                            // 칼럼 명 저장
                            if (hv.row == 1 && hv.col > 2) formDataNames.put(hv.col, hv.value);
                            // 칼럼 예시 저장
                            if (hv.row == 2 && hv.col > 2) formDataDefaultValues.put(hv.col, hv.value);
                        }

						// 저장된 데이터를 바탕으로 객체 생성 및 데이터베이스 작업 수행
						for (Integer col : formDataNames.keySet()) {
							// 칼럼 타입 저장
							String formDataType			= formDataTypes.get(col);
							// 칼럼 명
							String formDataName			= formDataNames.get(col);
							// 칼럼 예시
							String formDataDefaultValue = formDataDefaultValues.get(col);

							// 칼럼 타입에 따른 칼럼 예시 데이터 변경
							if (formDataType.trim().equalsIgnoreCase("AMT")) {
								formDataDefaultValue = formDataDefaultValue.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
							} else if (formDataType.trim().equalsIgnoreCase("DATE1")) {
								formDataDefaultValue = formDataDefaultValue.replaceAll("[^0-9]","");
								formDataDefaultValue = formDataDefaultValue.replaceAll("(\\d{4})(\\d{1,2})(\\d{1,2})", "$1년 $2월 $3일");
							} else if (formDataType.trim().equalsIgnoreCase("DATE2")) {
								formDataDefaultValue = formDataDefaultValue.replaceAll("[^0-9]","");
								formDataDefaultValue = formDataDefaultValue.replaceAll("(\\d{4})(\\d{1,2})(\\d{1,2})", "$1.$2.$3");
							} else if (formDataType.trim().equalsIgnoreCase("DATE3")) {
								formDataDefaultValue = formDataDefaultValue.replaceAll("[^0-9]","");
								formDataDefaultValue = formDataDefaultValue.replaceAll("(\\d{4})(\\d{1,2})(\\d{1,2})", "$1-$2-$3");
							} else if (formDataType.trim().equalsIgnoreCase("PHONE")) {
								formDataDefaultValue = formDataDefaultValue.replaceAll("[^0-9]","");
								formDataDefaultValue = formDataDefaultValue.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
							} else if (formDataType.trim().equalsIgnoreCase("OR")) {
								formDataDefaultValue = formDataDefaultValue.replaceAll("[^0-9]","");
								if (formDataDefaultValue.length() == 10) {
									formDataDefaultValue = formDataDefaultValue.replaceAll("(\\d{3})(\\d{2})(\\d{5})", "$1-$2-$3");
								} else if (formDataDefaultValue.length() == 8) {
									formDataDefaultValue = formDataDefaultValue.replaceAll("(\\d{4})(\\d{1,2})(\\d{1,2})", "$1년 $2월 $3일");
								}
							}

							logger.info("col>" + col + "-" + formDataName);
							ContractPersonNewFormVO formVO = new ContractPersonNewFormVO();
							formVO.setServiceId(ContractService.SERVICE_ID);
							formVO.setBizId(bizId);
							formVO.setFileId(fileId);
							formVO.setFormDataId("TXT_" + StringUtil.lpad(Integer.toString(rownum), 5, "0"));
							formVO.setFormDataDefaultValue(formDataDefaultValue);
							formVO.setFormDataName(formDataName);

							contractDAO.delContractNewForm(formVO);
							contractDAO.insContractNewForm(formVO);
							rownum++;
						}

						result.setServiceId(ContractService.SERVICE_ID);
						result.setBizId(bizId);
						result.setFileId(fileId);
						//변환 완료된 양식 등록
						result.setTransType("P");
						result.setContractId(contentVO.getFileId());
						result.setDataFileId(xlscontentVO.getFileId());
						result.setAlterFileId(altercontentVO.getFileId());

						contractDAO.updContractNew(result);
					}
				}
			}
			//기업관리자에게 메일발송
			sendContractNewUpdateEmailP(bizId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}


	// 일괄다운로드
	@Override
	public int downloadContractZip(ContractPersonVO vo) {

		int result = 0;
		List<ContractPersonVO> list;
		vo.setStatusCode("Y");
		vo.setStartPage(0);
		vo.setEndPage(10000);

		if ("true".equals(vo.getLoginType())) list = contractDAO.getContractPersonUserGrpList(vo);
		else								  list = contractDAO.getContractPersonList(vo);

		List<String> contractList = new ArrayList<>();

        for (ContractPersonVO dataVO : list) {
            ContentVO contentVO = new ContentVO();
            contentVO.setFileId(dataVO.getContractId());

            // 콘텐츠에서 정보 추출
            ContentVO resultContentVO = contentDAO.getContent(contentVO);
            if (resultContentVO == null) {
                logger.error("[downloadContractZip] 콘텐츠 정보가 존재하지 않습니다.");
                return 0;
            }

            // 저장위치
            CabinetVO cabinetVO = new CabinetVO();
            cabinetVO.setClassId(resultContentVO.getClassId());
            cabinetVO.setCabinetId(resultContentVO.getCabinetId());
            CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
            if (cabinetResultVO == null) {
                logger.error("[downloadContractZip] 캐비넷 정보가 존재하지 않습니다.");
                return 0;
            }

            String originalPdfPath = cabinetResultVO.getCabinetPath() + resultContentVO.getFilePath() + resultContentVO.getFileName();
            String targetPdfName   = dataVO.getBizName() + "_" + dataVO.getEmpName() + "_" + dataVO.getEmpNo() + "_" + dataVO.getContId() + "_" + dataVO.getContractFileName() + ".pdf";
            String targetPdfPath   = MultipartFileUtil.getSystemTempPath() + targetPdfName;

            // 사용할 PDF복사
            FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());

            if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) contractList.add(targetPdfPath);

            //감사추적인증서
            ContractPersonNewVO newVO = contractDAO.getContractPersonSign(dataVO);

            boolean makeCert = false;
            if 		(newVO == null) 															makeCert = true;
            else if (newVO.getSignBizType().equals("1") && newVO.getSignUserType().equals("1")) makeCert = true;

            if (makeCert) {
                String originalPdfPathCert = getSystemPath() + "/data/contract/감사추적인증서.pdf";
                String targetPdfNameCert   = dataVO.getBizName() + "_" + dataVO.getEmpName() + "_" + dataVO.getEmpNo() + "_" + dataVO.getContId() + "_감사추적인증서.pdf";
                String targetPdfPathCert   = MultipartFileUtil.getSystemTempPath() + targetPdfNameCert;

                // 사용할 PDF복사
                FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
                if (FileUtil.fileCopy(originalPdfPathCert, targetPdfPathCert)) {
                    List<FieldVO> fieldListVO = new ArrayList<>();
                    // 계약서 정보
                    fieldListVO.add(FieldUtil.setFieldData("cont_id", dataVO.getContId()));
                    fieldListVO.add(FieldUtil.setFieldData("service_id", dataVO.getServiceId()));
                    fieldListVO.add(FieldUtil.setFieldData("biz_id", dataVO.getBizId()));
                    fieldListVO.add(FieldUtil.setFieldData("user_id", dataVO.getUserId()));
                    fieldListVO.add(FieldUtil.setFieldData("contract_date", dataVO.getContractDate()));

                    //감사추적인증서 제작일시 (일괄 다운로드)
                    Date date = new Date();
                    fieldListVO.add(FieldUtil.setFieldData("TXT_00001", DateUtil.dateToString(date, "yyyy/MM/dd/ HH:mm:ss")));
                    //문서종류
                    fieldListVO.add(FieldUtil.setFieldData("TXT_00002", dataVO.getContractFileName()));
                    //문서고유번호
                    fieldListVO.add(FieldUtil.setFieldData("TXT_00003", dataVO.getContId()));
                    //기업명
                    fieldListVO.add(FieldUtil.setFieldData("TXT_00004", dataVO.getBizName()));
                    //작성자
                    fieldListVO.add(FieldUtil.setFieldData("TXT_00005", "뉴젠피앤피"));
                    //수신자
                    fieldListVO.add(FieldUtil.setFieldData("TXT_00006", dataVO.getEmpName()));

                    //감사추적인증서를 위한 히스토리 조회
                    ContractPersonLogVO logVO = new ContractPersonLogVO();
                    logVO.setContId(dataVO.getContId());
                    List<ContractPersonLogVO> resLogVO = contractDAO.getContractPersonLogList(logVO);

                    int idx = 7;

                    for (ContractPersonLogVO formData : resLogVO) {
                        Date logDate 	  = DateUtil.StringToDate(formData.getInsDate(), "yyyyMMddhhmmss");
                        String strLogDate = DateUtil.dateToString(logDate, "yyyy/MM/dd/ HH:mm:ss");

                        fieldListVO.add(FieldUtil.setFieldData("TXT_" + String.format("%05d", idx++), strLogDate));
                        fieldListVO.add(FieldUtil.setFieldData("TXT_" + String.format("%05d", idx++), formData.getInsEmpName()));
                        fieldListVO.add(FieldUtil.setFieldData("TXT_" + String.format("%05d", idx++), formData.getLogName()));
                        fieldListVO.add(FieldUtil.setFieldData("TXT_" + String.format("%05d", idx++), formData.getLogMessage()));
                    }

                    //파일생성
                    boolean bWritePDF = ControlUtil.writePDF(fieldListVO, originalPdfPathCert, targetPdfPathCert, false, true, System.getProperty("pdf.font.default"));

                    if (bWritePDF) {
                        String zipFilePath = MultipartFileUtil.getSystemTempPath() + dataVO.getBizId() + MultipartFileUtil.SEPERATOR;
                        String zipFileName = "[전자문서]_" + "[" + DateUtil.getTimeStamp(7) + "]" + ".zip";

                        ContractPersonVO paramVO = new ContractPersonVO();
                        paramVO.setBizId(dataVO.getBizId());
                        paramVO.setUserId(dataVO.getUserId());
                        paramVO.setContractDate(dataVO.getContractDate());
                        paramVO.setContId(dataVO.getContId());
                        paramVO.setContractDateFrom(dataVO.getContractDateFrom());
                        paramVO.setContractDateTo(dataVO.getContractDateTo());
                        paramVO.setPdfFile(zipFilePath + zipFileName);
                        paramVO.setFileType("A");

                        contractList.add(targetPdfPathCert);
                    }
                }
            }

            // 로그내역 LIST에 등록
            ContractPersonLogVO logVO = new ContractPersonLogVO();
            logVO.setContId(dataVO.getContId());
            logVO.setServiceId(SERVICE_ID);
            logVO.setBizId(dataVO.getBizId());
            logVO.setUserId(dataVO.getUserId());
            logVO.setContractDate(dataVO.getContractDate());
            logVO.setContractId(dataVO.getContractId());
            logVO.setLogType("7");
            logVO.setLogMessage(dataVO.getEmpName() + "님의 전자문서를 다운로드 하였습니다.");
            logVO.setInsEmpNo(vo.getLoginUserParam());
            contractDAO.insContractPersonLog(logVO);
        }
		if (!contractList.isEmpty()) {
			logger.info(vo.getPdfFile());
			result = FileUtil.ZipFile(vo.getPdfFile(), contractList);
		}
		return result;
	}


	// 개별계약서 미계약서 생성건 삭제
	@Override
	public int delContractPersonTemp(ContractPersonVO vo, String webSocketKey) throws Exception {
		int result = 0;

		logger.info("delContractPersonTemp bizId : "+vo.getBizId());
		logger.info("delContractPersonTemp contractFileId : "+vo.getContractFileId());

		try {
			//미 완료된 계약서 조회
			vo.setTransType("N");
			vo.setContractFileId(vo.getContractFileId());

			//sql에서 0>= 일 경우만 페이징 실행
			vo.setStartPage(-1);
			vo.setEndPage(-1);
			List<ContractPersonVO> resultList = contractDAO.getContractPersonList(vo);
			logger.info("미 완료된 계약서 건수 : "+resultList.size());

			//미 완료 계약서 제거
			ContractPersonVO contractPersonVO = new ContractPersonVO();
			contractPersonVO.setBizId(vo.getBizId());
			contractPersonVO.setContractFileId(vo.getContractFileId());
			int cnt = contractDAO.delContractPersonTemp(contractPersonVO);
			logger.info("미 완료 계약서 삭제 건수 : "+cnt);

			//미 완료 계약서 폼 제거
			for (int i=0; i<resultList.size(); i++) {
				ContractPersonVO delContractVO = resultList.get(i);
				// 계약조건정보 삭제
				ContractFormVO contractFormVO = new ContractFormVO();
				contractFormVO.setContId(delContractVO.getContId());
				contractDAO.delContractForm(contractFormVO);

				// 웹소켓 진행률 표시
				int start = i+1;
				int end   = resultList.size();
				String message = "미 완료 계약서 삭제중 입니다.\n" + start + "건 / " + end + " 건";
				Websocket.sendMessage(webSocketKey, message);
			}
		} catch (Exception e) { e.printStackTrace(); }
		return result;
	}


	// 개별계약서 삭제
	@Override
	public int delContractPerson(ContractPersonVO vo, SessionVO loginVO) throws Exception {
		int result = 0;
		logger.info("delContractPerson ContId : "+vo.getContId());

		try {

			ContractPersonVO contractResultVO = contractDAO.getContractPerson(vo);
			if (contractResultVO == null) {
				logger.error("[delContractPerson] 근로계약서정보가 존재하지 않습니다.");
				vo.setResultMessage("근로계약서정보가 존재하지 않습니다.");
				return -4;
			}

			// 접속한 사용자정보
			EmpVO empVO = new EmpVO();
			EmpVO empResultVO;
			empVO.setBizId(contractResultVO.getBizId());
			empVO.setUserId(contractResultVO.getUserId());
			empResultVO = empDAO.getEmp(empVO);

			if (empResultVO == null) { // 인사목록이 없는경우 거래처목록도 검색
				empVO.setCustomerId(contractResultVO.getUserId());
				empResultVO = empDAO.getCustomer(empVO);
			}

			if (empResultVO == null) {
				logger.error("[delContractPerson] 직원정보가 존재하지 않습니다.");
				vo.setResultMessage("직원정보가 존재하지 않습니다.");
				return -3;
			}

			// 전자서명 단계 확인 (계약완료 상태 체크)
			if (contractResultVO.getStatusCode().equals("Y") && !StringUtils.equals("180109161235042", contractResultVO.getBizId())) {
				logger.error("[delContractPerson] 미완료된 문서에 대해서만 삭제가 가능합니다.");
				vo.setResultMessage("미완료된 문서에 대해서만 삭제가 가능합니다.");
				return -5;
			}

			ContractPersonVO contractPersonVO = new ContractPersonVO();
			contractPersonVO.setContId(contractResultVO.getContId());
			contractPersonVO.setStatusCode("D");

			// 계약정보 업데이트
			result = contractDAO.updContractPerson(contractPersonVO);
			// 삭제될 계약서 데이터 백업테이블 이동
			contractDAO.moveContractPersonDel(contractPersonVO);
			// 계약정보 삭제
			contractDAO.delContractPerson(contractPersonVO);

			// 계약조건정보 삭제
			ContractFormVO contractFormVO = new ContractFormVO();
			contractFormVO.setContId(contractResultVO.getContId());
			contractDAO.delContractForm(contractFormVO);

			// 계약서 파일 삭제
			List<ContentVO> contentList = new ArrayList<>();

			ContractPersonLogVO contractPersonLogVO = new ContractPersonLogVO();
			contractPersonLogVO.setContId(contractResultVO.getContId());
			List<ContractPersonLogVO> loglist = contractDAO.getContractPersonLogList(contractPersonLogVO);

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
			// 삭제할 계약서 존재시 삭제처리 실제 파일만 삭제 (DB는 보존)
			if (!contentList.isEmpty()) contentService.deleteContent(contentList);

			// 로그테이블 생성
			ContractPersonLogVO logVO = new ContractPersonLogVO();
			logVO.setContId(contractResultVO.getContId());
			logVO.setServiceId(ContractService.SERVICE_ID);
			logVO.setBizId(contractResultVO.getBizId());
			logVO.setUserId(contractResultVO.getUserId());
			logVO.setContractDate(contractResultVO.getContractDate());
			logVO.setContractId(contractResultVO.getContractId());
			logVO.setLogType("D");
			logVO.setLogMessage(contractResultVO.getEmpName()+"님의 전자문서를 삭제하였습니다.");
			logVO.setInsEmpNo(loginVO.getUserId());
			contractDAO.insContractPersonLog(logVO);

		} catch (Exception e) { e.printStackTrace(); }
		return result;
	}


	// 계약서 재작성
	@Override
	public List<ContentVO> generateContractPDF(ContractPersonVO vo, SessionVO loginVO) throws Exception {
		ContentVO contentVO		   = new ContentVO();
		CabinetVO cabinetVO		   = new CabinetVO();
		List<ContentVO> resultList = new ArrayList<>();
		List<ContentVO> delList	   = new ArrayList<>();

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
		contentVO.setFileId(resultContractVO.getContractId());
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

		String originalPdfPath = cabinetResultVO.getCabinetPath()+resultVO.getFilePath()+resultVO.getFileName();
		String targetPdfName   = StringUtil.getUUID()+".pdf";
		String targetPdfPath   = MultipartFileUtil.getSystemTempPath()+targetPdfName;

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

            for (ContractFormVO formData : formList) {
                // 필드가 null인 경우 빈문자로 update 후 문서생성
                if (StringUtil.isNull(formData.getFormDataValue())) formData.setFormDataValue(" ");

                if (formData.getEncodeYn().equals("Y")) formData.setFormDataValue(SecurityUtil.getinstance().aesDecode(formData.getFormDataValue()));

                fieldListVO.add(FieldUtil.setFieldData(formData.getFormDataId(), formData.getFormDataValue()));
            }

			//파일생성
			boolean bWritePDF = ControlUtil.writePDF(fieldListVO, originalPdfPath, targetPdfPath, false, false, System.getProperty("pdf.font.default"));

			//콘텐츠등록
			if (bWritePDF) {
				String orgFileName = resultVO.getFileTitle();

				if (!orgFileName.contains(resultContractVO.getEmpName())) { orgFileName = resultContractVO.getEmpName() + "_" + orgFileName; }

				contentVO = new ContentVO();
				contentVO.setFileType(FileUtil.getFileExt(targetPdfPath));
				contentVO.setFileName(targetPdfName);
				contentVO.setFileSize(Long.toString(FileUtil.getFileSize(targetPdfPath)));
				contentVO.setFilePath(MultipartFileUtil.getSystemTempPath());
				contentVO.setOrgFileName(orgFileName +".pdf");
				contentVO.setFileTitle(orgFileName);

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
				if (ContentService.COMPLETED == nResult) {

					ContractPersonVO contractPersonVO = new ContractPersonVO();
					contractPersonVO.setBizId(resultContractVO.getBizId());
					contractPersonVO.setContId(resultContractVO.getContId());

					contractPersonVO.setHashData(PKIUtil.getFileHash(targetPdfPath));
					contractPersonVO.setStatusCode("1");
					contractPersonVO.setContractId(contentVO.getFileId());
					contractPersonVO.setTransType("Y");
					// 난수 초기화
					contractPersonVO.setDigitNonce("");
					contractPersonVO.setSendDate("");

					// 계약정보 업데이트
					contractDAO.updContractPerson(contractPersonVO);

					// 계약서 만료일자가 이전에 있는경우 초기화
					if (!"".equals(resultContractVO.getExpireDate())) {
						contractPersonVO.setExpireDate("");
						contractDAO.updContractExpirdDate(contractPersonVO);
					}

					// 로그내역 등록
					ContractPersonLogVO logVO = new ContractPersonLogVO();
					logVO.setContId(resultContractVO.getContId());
					logVO.setServiceId(SERVICE_ID);
					logVO.setBizId(resultContractVO.getBizId());
					logVO.setUserId(resultContractVO.getUserId());
					logVO.setContractDate(resultContractVO.getContractDate());
					logVO.setContractId(contentVO.getFileId());
					logVO.setLogType("1");
					logVO.setLogMessage(resultContractVO.getEmpName()+"님의 전자문서를 생성하였습니다.");
					logVO.setInsEmpNo(loginVO.getUserId());
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


    // 전자서명 일괄 서명
	@Override
	public ContractPersonVO setContractSignHashDataMulti(List<ContractPersonVO> list, SessionVO loginVO, String webSocketKey) throws Exception {

		ContractPersonVO result   = new ContractPersonVO();
		List<FieldVO> fieldListVO = new ArrayList<>();
		result.setResultCode("N");
		int count = 0;

		try {

			for (int i=0; i<list.size(); i++) {
				ContractPersonVO vo = list.get(i);

				ContractPersonVO contractResultVO = contractDAO.getContractPerson(vo);
				if (contractResultVO == null) {
					logger.info("[setContractSignHashDataMulti] 문서정보가 존재하지 않습니다.");
					result.setResultMessage("문서정보가 존재하지 않습니다.");
					return result;
				}

				logger.info("setContractSignHashDataMulti : "+contractResultVO.getUserId() + ", "+contractResultVO.getContractDate());

				//  사용자정보
				EmpVO empVO		   = new EmpVO();
				String receiveName = "";
				empVO.setBizId(contractResultVO.getBizId());
				empVO.setUserId(contractResultVO.getUserId());
				empVO.setCustomerId(contractResultVO.getUserId());
				EmpVO empResultVO	   = empDAO.getEmp(empVO);
				EmpVO customerResultVO = empDAO.getCustomer(empVO);

				if (empResultVO==null && customerResultVO==null) {
					logger.info("[setContractSignHashDataMulti] 수신자 정보가 존재하지 않습니다.");
					result.setResultMessage("수신자 정보가 존재하지 않습니다.");
					return result;
				}

				if (empResultVO != null) 	  receiveName = empResultVO.getEmpName();
				if (customerResultVO != null) receiveName = customerResultVO.getCustomerName();

				BizVO bizVO = new BizVO();
				bizVO.setBizId(contractResultVO.getBizId());
				bizVO.setStartPage(0);
				bizVO.setEndPage(1);
				List<BizVO> bizList = bizDAO.getBizList(bizVO);
				if (bizList==null || bizList.isEmpty()) {
					logger.info("[setContractSignHashDataMulti] 기업정보가 존재하지 않습니다.");
					result.setResultMessage("기업정보가 존재하지 않습니다.");
					return result;
				}

				// 전자서명 단계 확인
				if (!contractResultVO.getStatusCode().equals("1") &&
						!contractResultVO.getStatusCode().equals("2") &&
						!contractResultVO.getStatusCode().equals("5") &&
						!contractResultVO.getStatusCode().equals("P3") &&
						!contractResultVO.getStatusCode().equals("APP")) {
					logger.info("[setContractSignHashDataMulti] 전자서명할 단계가 아닙니다.");
					result.setResultMessage("전자서명할 단계가 아닙니다.");
					return result;
				}

				// 콘텐츠에서 정보 추출
				ContentVO contentVO		  = new ContentVO();
				contentVO.setFileId(contractResultVO.getContractId());
				ContentVO contentResultVO = contentDAO.getContent(contentVO);
				if (contentResultVO==null) {
					logger.info("[setContractSignHashDataMulti] 문서 정보가 존재하지 않습니다.");
					result.setResultMessage("문서정보가 존재하지 않습니다.");
					return result;
				}

				// 저장위치
				CabinetVO cabinetVO = new CabinetVO();
				cabinetVO.setClassId(contentResultVO.getClassId());
				cabinetVO.setCabinetId(contentResultVO.getCabinetId());
				CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
				if (cabinetResultVO==null) {
					logger.info("[setContractSignHashDataMulti]  문서 위치정보가 존재하지 않습니다.");
					result.setResultMessage("문서 위치정보가 존재하지 않습니다.");
					return result;
				}

				String originalPdfPath = cabinetResultVO.getCabinetPath()+contentResultVO.getFilePath()+contentResultVO.getFileName();
				String targetPdfName   = StringUtil.getUUID()+".pdf";
				String targetPdfPath   = MultipartFileUtil.getSystemTempPath()+targetPdfName;

				// 사용할 PDF복사
				FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());

				String bizImage = "";
				// 이미지 생성(회사 직인 찍음)
				if (StringUtil.isNotNull(bizList.get(0).getCompanyImage())&&!bizList.get(0).getCompanyImage().equals("-")) {
					bizImage = getSystemPath()+"images/sign/"+bizList.get(0).getCompanyImage();
				} else {
					bizImage = getSystemPath()+"images/sign/digitsign.png";
				}

				// sign_1 (갑:사업자) 서명란 key값 검색
				List<String> keyList = new ArrayList<>();

				InputStream is = Files.newInputStream(Paths.get(originalPdfPath));
				List<FormVO> formList = FieldUtil.getAllField(is, false);
				is.close();

                for (FormVO formVO : formList) {
                    String formId = formVO.getId();
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

				//파일생성 (서명한 파일로 재 생성)
				boolean bWritePDF = ControlUtil.writePDF(fieldListVO, originalPdfPath, targetPdfPath, false, false, System.getProperty("pdf.font.default"));

				//콘텐츠등록
				if (bWritePDF) {
					contentVO = new ContentVO();
					contentVO.setFileType(FileUtil.getFileExt(targetPdfPath));
					contentVO.setFileName(targetPdfName);
					contentVO.setOrgFileName(receiveName+"_"+contractResultVO.getContractFileName()+"_"+contractResultVO.getContractDate()+".pdf");
					contentVO.setFileSize(Long.toString(FileUtil.getFileSize(targetPdfPath)));
					contentVO.setFilePath(MultipartFileUtil.getSystemTempPath());
					contentVO.setFileTitle(receiveName+"_"+contractResultVO.getContractFileName()+"_"+contractResultVO.getContractDate());
					contentVO.setClassId(SAVE_CLASS_ID);
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

						ContractPersonVO contractPersonVO = new ContractPersonVO();
						contractPersonVO.setContId(contractResultVO.getContId());

						contractPersonVO.setStatusCode("2");
						contractPersonVO.setContractId(contentVO.getFileId());
						contractPersonVO.setTransType("Y");

						// 계약정보 업데이트
						nResult = contractDAO.updContractPerson(contractPersonVO);

						String logMessage = "";
						if (nResult>0) {
							logMessage=bizList.get(0).getBizName()+"의 전자서명을 하였습니다.";
							count++;
						}
						// 로그내역 등록
						ContractPersonLogVO logVO = new ContractPersonLogVO();
						logVO.setContId(vo.getContId());
						logVO.setServiceId(vo.getServiceId());
						logVO.setBizId(vo.getBizId());
						logVO.setUserId(vo.getUserId());
						logVO.setContractDate(vo.getContractDate());
						logVO.setContractId(contentVO.getFileId());
						logVO.setLogType("2");
						logVO.setLogMessage(logMessage);
						logVO.setInsEmpNo(loginVO.getUserId());
						contractDAO.insContractPersonLog(logVO);

						logger.info("["+contentVO.getFileId()+"]TempFile Delete : "+targetPdfPath);
						FileUtil.deleteFile(targetPdfPath);
					} else {
						logger.info("[setContractSignHashDataMulti] tractContent ResulCode : " + nResult);
					}
				}

				// 웹소켓 진행률 표시
				int start = i+1;
				int end   = list.size();
				String message = "서명중 입니다.\n" + start + "건 / " + end + " 건";
				Websocket.sendMessage(webSocketKey, message);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultMessage("전자서명 중 오류가 발생하였습니다.");
		}
		if (list.size() == count) {
			result.setResultCode("Y");
			result.setResultMessage("전자서명이 성공하였습니다.");
		}
		return result;
	}


	//전자문서 전송
	@Override
	public ContractPersonVO sendContractPersonMulti(List<ContractPersonVO> list, SessionVO loginVO, String webSocketKey) {

		ContractPersonVO result   = new ContractPersonVO();
		String receiveName 		  = "";
		String receivePhoneNumber = "";
		String receiveEmail		  = "";
		String sendBizName		  = "";
		String authCode			  = "";
		int count			 	  = 0;

		try {
			result.setResultCode("100");

			for (int i=0; i<list.size(); i++) {

				ContractPersonVO vo = list.get(i);
				ContractPersonVO contractResultVO = contractDAO.getContractPerson(vo);
				if (contractResultVO == null) {	// 계약서 조회
					logger.error("[sendContractPersonMulti] 전자문서 정보가 존재하지 않습니다.");
					result.setResultMessage("전자문서 정보가 존재하지 않습니다.");
					result.setResultCode("400");
					return result;
				}

				if (i == 0) { 	//한번만 처리.
					PointVO pointVO = new PointVO();
					pointVO.setBizId(contractResultVO.getBizId());
					PointVO resultPointVO = pointDAO.getPoint(pointVO);
					if (resultPointVO == null) {	// 포인트 조회
						logger.error("[sendContractPersonMulti] 포인트정보가 존재하지 않습니다.");
						result.setResultMessage("포인트정보가 존재하지 않습니다.");
						result.setResultCode("200");
						return result;
					}

					int reducePoint = 0;
					BizGrpVO grpVO = new BizGrpVO();
					grpVO.setBizId(contractResultVO.getBizId());
					List<BizGrpVO> pointList = bizDAO.getBizGrp(grpVO);
					if (pointList == null){	// 기업 정보 조회
						logger.error("[sendContractPersonMulti] 기업정보가 존재하지 않습니다.");
						result.setResultMessage("기업정보가 존재하지 않습니다.");
						result.setResultCode("202");
						return result;
					} else {
						//현재 보유중인 포인트
						int point = resultPointVO.getCurPoint();
						//전자문서 전송 차감 포인트
						if (StringUtil.isNotNull(pointList.get(0).getSendPoint())) reducePoint = Integer.parseInt(pointList.get(0).getSendPoint());
						if ((point - (reducePoint*list.size()))<0) {	// 포인트 부족할 시
							logger.error("[sendContractPersonMulti] 잔여 포인트가 존재하지 않습니다. 현재포인트 : <"+point+">, 차감 포인트 : <"+(reducePoint*list.size())+">");
							result.setResultMessage("잔여 포인트가 존재하지 않습니다. 현재포인트 : <"+point+">, 차감 포인트 : <"+(reducePoint*list.size())+">");
							result.setResultCode("201");
							return result;
						}
					}
				}

				logger.info("sendContractPersonMulti : "+contractResultVO.getContId()+ ", "+contractResultVO.getUserId() + ", "+contractResultVO.getContractDate());

				// 접속한 사용자정보
				EmpVO empVO = new EmpVO();
				empVO.setBizId(contractResultVO.getBizId());
				empVO.setUserId(contractResultVO.getUserId());
				empVO.setCustomerId(contractResultVO.getUserId());

				EmpVO empResultVO	   = empDAO.getEmp(empVO);
				EmpVO customerResultVO = empDAO.getCustomer(empVO);

				if (empResultVO==null && customerResultVO==null) {
					logger.error("[sendContractPersonMulti] 직원정보가 존재하지 않습니다.");
					result.setResultMessage("직원정보가 존재하지 않습니다.");
					result.setResultCode("300");
					return result;
				}

				if (empResultVO != null) {
					receiveName		   = empResultVO.getEmpName();
					receivePhoneNumber = empResultVO.getPhoneNum();
					receiveEmail	   = empResultVO.getEMail();
					sendBizName		   = empResultVO.getBizName();
				}

				if (customerResultVO != null) {
					receiveName		   = customerResultVO.getCustomerName();
					receivePhoneNumber = customerResultVO.getManagerPhoneNum();
					receiveEmail	   = customerResultVO.getManagerEmail();
					sendBizName		   = customerResultVO.getBizName();

					if (vo.getAuthType().equals("P")) {
						// 수신자의 사업자등록번호로 세팅
						if (vo.getAuthCodeSel().equals("A")) 							 authCode = SecurityUtil.encrypt(customerResultVO.getBusinessNo());
						if (vo.getAuthCode() != null && vo.getAuthCodeSel().equals("M")) authCode = SecurityUtil.encrypt(vo.getAuthCode());
					}
				}

				if (empResultVO!=null) {	// 인사정보
					if (!StringUtil.isNotNull(receiveEmail) && vo.getSendType().equals("EMAIL")) {
						logger.error("[sendContractPersonMulti] "+receiveName+"님의 이메일정보가 없습니다.");
						result.setResultMessage(receiveName+"님의 이메일정보가 없습니다.");
						result.setResultCode("301");
						return result;
					}
					if (!StringUtil.isNotNull(receivePhoneNumber) && vo.getSendType().equals("KKO")) {
						logger.error("[sendContractPersonMulti] "+receiveName+"님의 휴대폰번호 정보가 없습니다.");
						result.setResultMessage(receiveName+"님의 휴대폰번호 정보가 없습니다.");
						result.setResultCode("302");
						return result;
					}
				}

				// 전자서명 단계 확인
				// signBiz의 값이 0일 경우(기업의 서명 필요하지 않음) 전송 가능하도록 수정함.
				ContractPersonNewVO signVO = contractDAO.getContractPersonSign(vo);

				if (signVO == null) { //양식이 제거되었을 경우
					logger.error("[sendContractPersonMulti] 전자문서 전송할 단계가 아닙니다. 양식정보없음. statusCode => "+contractResultVO.getStatusCode());
					result.setResultMessage("전자문서 전송할 단계[양식정보 없음]가 아닙니다.");
					result.setResultCode("401");
					return result;
				} else {
					if(!contractResultVO.getStatusCode().equals("2") &&
							!(contractResultVO.getStatusCode().equals("1") && signVO.getSignBizType().equals("0")) &&
							!(contractResultVO.getStatusCode().equals("1") && signVO.getUsePre().equals("Y")) &&
							!(contractResultVO.getStatusCode().equals("APP") && signVO.getSignBizType().equals("0")) &&
							!(contractResultVO.getStatusCode().equals("APP") && signVO.getUsePre().equals("Y"))) {
						logger.error("[sendContractPersonMulti] 전자문서 전송할 단계가 아닙니다. statusCode => "+contractResultVO.getStatusCode()+", bizType => "+signVO.getSignBizType() + ", UsePre => "+ signVO.getUsePre());
						result.setResultMessage("전자문서 전송할 단계가 아닙니다.");
						result.setResultCode("401");
						return result;
					}
				}
				// 웹소켓 진행률 표시
				int start = i+1;
				int end   = list.size();
				String message = "최종 전자문서 확인중입니다.\n" + start + "건 / " + end + " 건";
				Websocket.sendMessage(webSocketKey, message);
			}

			for (int i=0; i<list.size(); i++) {

				ContractPersonVO vo = list.get(i);
				ContractPersonVO contractResultVO = contractDAO.getContractPerson(vo);
				if (contractResultVO == null) {
					logger.error("[sendContractPersonMulti] 전자문서 정보가 존재하지 않습니다.");
					result.setResultMessage("전자문서 정보가 존재하지 않습니다.");
					result.setResultCode("401");
					return result;
				}

				PointVO pointVO = new PointVO();
				pointVO.setBizId(contractResultVO.getBizId());
				PointVO resultPointVO = pointDAO.getPoint(pointVO);
				if (resultPointVO == null) {
					logger.error("[sendContractPersonMulti] 포인트정보가 존재하지 않습니다.");
					result.setResultMessage("근로계약서정보가 존재하지 않습니다.");
					result.setResultCode("200");
					return result;
				}

				int reducePoint = 0;
				BizGrpVO grpVO = new BizGrpVO();
				grpVO.setBizId(contractResultVO.getBizId());
				List<BizGrpVO> pointList = bizDAO.getBizGrp(grpVO);

				if(pointList == null){
					logger.error("[sendContractPersonMulti] 기업 조회에 실패하였습니다.");
					result.setResultMessage("기업 조회에 실패하였습니다.");
					result.setResultCode("202");
					return result;
				} else {
					//현재 보유중인 포인트
					int point = resultPointVO.getCurPoint();
					//전자문서 전송 차감 포인트
					if (StringUtil.isNotNull(pointList.get(0).getSendPoint())){ reducePoint = Integer.parseInt(pointList.get(0).getSendPoint()); }
				}

				logger.info("sendContractPersonMulti : "+contractResultVO.getContId()+ ", "+contractResultVO.getUserId() + ", "+contractResultVO.getContractDate());

				// 접속한 사용자정보
				EmpVO empVO = new EmpVO();
				EmpVO empResultVO;
				EmpVO customerResultVO;
				empVO.setBizId(contractResultVO.getBizId());
				empVO.setUserId(contractResultVO.getUserId());
				empVO.setCustomerId(contractResultVO.getUserId());

				empResultVO		 = empDAO.getEmp(empVO);
				customerResultVO = empDAO.getCustomer(empVO);

				if (empResultVO != null) {
					receiveName		   = empResultVO.getEmpName();
					receivePhoneNumber = empResultVO.getPhoneNum();
					receiveEmail	   = empResultVO.getEMail();
					sendBizName		   = empResultVO.getBizName();
				}

				if (customerResultVO != null) {
					receiveName		   = customerResultVO.getCustomerName();
					receivePhoneNumber = customerResultVO.getManagerPhoneNum();;
					receiveEmail	   = customerResultVO.getManagerEmail();
					sendBizName		   = customerResultVO.getBizName();

					if (vo.getAuthType().equals("P")) {
						// 수신자의 사업자등록번호로 세팅
						if (vo.getAuthCodeSel().equals("A")) 							 authCode = SecurityUtil.encrypt(customerResultVO.getBusinessNo());
						if (vo.getAuthCode() != null && vo.getAuthCodeSel().equals("M")) authCode = SecurityUtil.encrypt(vo.getAuthCode());
					}
				}

				if (!StringUtil.isNotNull(receiveEmail) && vo.getSendType().equals("EMAIL")) {
					logger.error("[sendContractPersonMulti] "+receiveName+"님의 이메일정보가 없습니다.");
					result.setResultMessage(receiveName+"님의 이메일정보가 없습니다.");
					result.setResultCode("301");
					return result;
				}
				if (!StringUtil.isNotNull(receivePhoneNumber) && vo.getSendType().equals("KKO")) {
					logger.error("[sendContractPersonMulti] "+receiveName+"님의 휴대폰번호 정보가 없습니다.");
					result.setResultMessage(receiveName+"님의 휴대폰번호 정보가 없습니다.");
					result.setResultCode("302");
					return result;
				}

				// 전자서명 단계 확인
				// signBiz의 값이 0일 경우(기업의 서명 필요하지 않음) 전송 가능하도록 수정함.
				ContractPersonNewVO signVO = contractDAO.getContractPersonSign(vo);

				if (signVO == null) { //양식이 제거되었을 경우
					logger.error("[sendContractPersonMulti] 전자문서 전송할 단계가 아닙니다. 양식정보없음. statusCode => "+contractResultVO.getStatusCode());
					result.setResultMessage("전자문서 전송할 단계[양식정보 없음]가 아닙니다.");
					result.setResultCode("401");
					return result;
				} else {
					if(!contractResultVO.getStatusCode().equals("2") &&
							!(contractResultVO.getStatusCode().equals("1") && signVO.getSignBizType().equals("0")) &&
							!(contractResultVO.getStatusCode().equals("1") && signVO.getUsePre().equals("Y")) &&
							!(contractResultVO.getStatusCode().equals("APP") && signVO.getSignBizType().equals("0")) &&
							!(contractResultVO.getStatusCode().equals("APP") && signVO.getUsePre().equals("Y"))) {
						logger.error("[sendContractPersonMulti] 전자문서 전송할 단계가 아닙니다. statusCode => "+contractResultVO.getStatusCode()+", bizType => "+signVO.getSignBizType() + ", UsePre => "+ signVO.getUsePre());
						result.setResultMessage("전자문서 전송할 단계가 아닙니다.");
						result.setResultCode("401");
						return result;
					}
				}

				String statusCode = "3";
				if (contractResultVO.getStatusCode().equals("1") && signVO.getUsePre().equals("Y")){
					//정보입력 요청 전송일 경우
					statusCode = "P1";
				}

				// 계약서 만료일자가 이전에 있는경우 업데이트
				if (!"".equals(contractResultVO.getExpireDate())) contractDAO.updContractExpirdDate(vo);

				ContractPersonVO contractPersonVO = new ContractPersonVO();
				contractPersonVO.setContId(contractResultVO.getContId());

				contractPersonVO.setStatusCode(statusCode);
				contractPersonVO.setSendDate(DateUtil.getTimeStamp(7));
				String digitNonce = StringUtil.getRandom(15);
				contractPersonVO.setDigitNonce(digitNonce);
				contractPersonVO.setSendType(vo.getSendType());
				contractPersonVO.setExpireDate(vo.getExpireDate());
				contractPersonVO.setComment(vo.getComment());
				contractPersonVO.setAuthType(vo.getAuthType());
				contractPersonVO.setAuthCode(authCode);

				// 계약정보 업데이트
				int nResult = contractDAO.updContractPerson(contractPersonVO);

				String logMessage = "";
				if (nResult>0) {
					logMessage=contractResultVO.getEmpName()+"님의 전자문서["+signVO.getFileTitle()+"]를 전송하였습니다.";
					if (contractResultVO.getStatusCode().equals("1") && signVO.getUsePre().equals("Y")){
						//정보입력 요청 전송일 경우
						logMessage=contractResultVO.getEmpName()+"님의 전자문서["+signVO.getFileTitle()+"] 정보입력을 요청하였습니다.";
					}
					count++;
				}
				// 로그내역 등록
				ContractPersonLogVO logVO = new ContractPersonLogVO();
				logVO.setContId(contractResultVO.getContId());
				logVO.setServiceId(contractResultVO.getServiceId());
				logVO.setBizId(contractResultVO.getBizId());
				logVO.setUserId(contractResultVO.getUserId());
				logVO.setContractDate(contractResultVO.getContractDate());
				logVO.setContractId(contractResultVO.getContractId());
				logVO.setInsEmpNo(loginVO.getUserId());

				if (StringUtil.isNotNull(vo.getComment())) {
					logVO.setLogType("C");
					logVO.setLogMessage(vo.getComment());
					contractDAO.insContractPersonLog(logVO);
				}

				logVO.setLogType(statusCode);
				logVO.setLogMessage(logMessage);
				contractDAO.insContractPersonLog(logVO);

				if (count>0) {

					String kkoLinkURL = System.getProperty("biztalk.kko.contract_view_url")+"?id="+digitNonce;

					logger.info("전송타입 : "+vo.getSendType());
					logger.info("receiveName : "+receiveName);
					if (vo.getSendType().equals("EMAIL")) {

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
						content +="				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">"+signVO.getFileTitle()+"</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br> ";
						content +="				발송 이메일입니다.</span> ";
						content +="			</div> ";
						content +="			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
						content +="				<span >"+receiveName+"</span><span>님!<br> ";
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
						mailVO.setTo(receiveEmail);
						mailVO.setCc("");
						mailVO.setBcc("");
						mailVO.setSubject(sendBizName+"와(과) "+receiveName+"님의 "+signVO.getFileTitle()+"가(이) 도착하였습니다.");
						mailVO.setText(content);

						logger.info("Ieum Sign 전자문서 이메일을 발송. email : " + receiveEmail + " digitNonce : " + digitNonce);
						logger.info("Ieum Sign 전자문서 이메일을 발송. email : " + receiveEmail + " digitNonce : " + digitNonce);

						email.send(mailVO);
					} else {
						//(알림톡 전송) 근로자 서명 / 완료 처리를 위한 알림톡
						if (contractResultVO.getStatusCode().equals("1") && signVO.getUsePre().equals("Y")) {
							//정보입력 요청일 경우 비즈톡 문구 다름.
							String content = "["+sendBizName+" * 정보입력 요청]\n\n";
							content += "본 알림은 "+sendBizName+"에서 "+receiveName+"님에게 요청하는 "+signVO.getFileTitle()+"의 정보입력 요청입니다.\n\n";
							content += "요청 항목을 확인하여 정보를 입력해 주시기 바랍니다.\n\n";
							content += "제출된 내용을 확인 후 생성된 "+signVO.getFileTitle()+"를 전송할 예정입니다.\n\n";
							content += "하단의 링크를 클릭하여 정보를 입력해 주시기 바랍니다.\n\n";
							content += kkoLinkURL;

							BizTalkKKOVO kkoVO = new BizTalkKKOVO();
							kkoVO.setSubject("");
							kkoVO.setContent(content);
							kkoVO.setCallback(System.getProperty("biztalk.kko.callback_number"));
							kkoVO.setRecipientNum(receivePhoneNumber);
							kkoVO.setSenderKey(System.getProperty("biztalk.kko.sender_key"));
							kkoVO.setTemplateCode("contract014");
							kkoVO.setKkoBtnType("1");
							kkoVO.setKkoBtnInfo("정보 입력^WL^"+kkoLinkURL+"^"+kkoLinkURL);
							bizTalkDAO.insBizTalk(kkoVO);
						} else if (signVO.getSignUserType().equals("1")) {
							//사용자 서명 존재
							String content = "["+sendBizName+" * 서명 요청]\n\n";
							content += receiveName+"님의 "+signVO.getFileTitle()+"가(이) 도착했습니다.\n";
							content += "본 문서는 "+sendBizName+"에서 "+receiveName+"님에게 발송한 전자문서입니다.\n\n";
							content += "내용을 확인하신 후 서명을 진행해주세요.\n";
							content += "문서의 내용이 협의된 사실과 다를 시 정정요청을 클릭해 수정요청 사항을 작성하고 담당부서에 연락 바랍니다.\n\n";
							content += "최종 전자문서는 전자서명이 완료된 후 알림톡을 통해 배부됩니다.\n\n";
							content += "하단의 링크를 클릭하여 전자문서를 확인하세요.\n\n";
							content += kkoLinkURL;

							BizTalkKKOVO kkoVO = new BizTalkKKOVO();
							kkoVO.setSubject("");
							kkoVO.setContent(content);
							kkoVO.setCallback(System.getProperty("biztalk.kko.callback_number"));
							kkoVO.setRecipientNum(receivePhoneNumber);
							kkoVO.setSenderKey(System.getProperty("biztalk.kko.sender_key"));
							kkoVO.setTemplateCode("contract015");
							kkoVO.setKkoBtnType("1");
							kkoVO.setKkoBtnInfo("전자문서^WL^"+kkoLinkURL+"^"+kkoLinkURL);
							bizTalkDAO.insBizTalk(kkoVO);
						} else {
							//사용자 서명 존재 X
							String content = "[뉴젠피앤피 * 전자문서]\n\n";
							content += receiveName+"님의 "+signVO.getFileTitle()+"가(이) 도착했습니다.\n";
							content += "본 문서는 "+sendBizName+"에서 "+receiveName+"님에게 발송한 전자문서입니다.\n\n";
							content += "내용을 확인하신 후 완료 버튼을 클릭해주세요.\n";
							content += "문서의 내용이 협의된 사실과 다를 시 정정요청을 클릭해 수정요청 사항을 작성하고 담당부서에 연락 바랍니다.\n\n";
							content += "하단의 링크를 클릭하여 전자문서를 확인하세요.\n\n";
							content += kkoLinkURL;

							BizTalkKKOVO kkoVO = new BizTalkKKOVO();
							kkoVO.setSubject("");
							kkoVO.setContent(content);
							kkoVO.setCallback(System.getProperty("biztalk.kko.callback_number"));
							kkoVO.setRecipientNum(receivePhoneNumber);
							kkoVO.setSenderKey(System.getProperty("biztalk.kko.sender_key"));
							kkoVO.setTemplateCode("document002");
							kkoVO.setKkoBtnType("1");
							kkoVO.setKkoBtnInfo("전자문서^WL^"+kkoLinkURL+"^"+kkoLinkURL);
							bizTalkDAO.insBizTalk(kkoVO);
						}
					}

					// 전송일자가 존재하면 포인트 차감이 안됌 (정정 요청시 전송일자 존재하여 포인트 차감x 24.05.02 일단 보류)
					if (!StringUtil.isNotNull(contractResultVO.getSendDate())) {

						// 포인트 차감
						pointVO.setCurPoint(reducePoint);
						pointDAO.balancePoint(pointVO);

						PointLogVO pointLogVO = new PointLogVO();
						pointLogVO.setBizId(contractResultVO.getBizId());
						pointLogVO.setUserId(contractResultVO.getUserId());
						pointLogVO.setServiceId(contractResultVO.getServiceId());
						pointLogVO.setPointType("2");
						pointLogVO.setPointPrice(pointVO.getCurPoint());
						pointLogVO.setPointResult(resultPointVO.getCurPoint()-pointVO.getCurPoint());
						pointLogVO.setEtcDesc("전자문서 전송 포인트 차감");
						logger.info("전자문서 전송 <"+reducePoint+">포인트 차감");

						pointDAO.insPointLog(pointLogVO);
					}
				}
				// 웹소켓 진행률 표시
				int start = i+1;
				int end   = list.size();
				String message = "전자문서 전송중입니다.\n" + start + "건 / " + end + " 건";
				Websocket.sendMessage(webSocketKey, message);
			}
		} catch (Exception e) { e.printStackTrace(); }
		if (list.size() != count) {
			result.setResultCode("500");
			result.setResultMessage("데이터 처리 중 오류가 발생하였습니다.");
		}
		return result;
	}


	// 전자문서 재전송
	@Override
	public boolean sendContractPersonMultiComplete(List<ContractPersonVO> list, SessionVO loginVO) {
		boolean result = false;
		int count	   = 0;

		try {

			for (int i=0; i<list.size(); i++) {
				ContractPersonVO vo = list.get(i);

				ContractPersonVO contractResultVO = contractDAO.getContractPerson(vo);
				if (contractResultVO == null) {
					logger.error("[sendContractPersonMultiComplete] 전자문서 정보가 존재하지 않습니다.");
					return false;
				}

				PointVO pointVO = new PointVO();
				pointVO.setBizId(contractResultVO.getBizId());
				PointVO resultPointVO = pointDAO.getPoint(pointVO);
				if (resultPointVO == null) {
					logger.error("[sendContractPersonMultiComplete] 포인트정보가 존재하지 않습니다.");
					return false;
				}

				int reducePoint = 0;
				BizGrpVO grpVO = new BizGrpVO();
				grpVO.setBizId(contractResultVO.getBizId());
				List<BizGrpVO> pointList = bizDAO.getBizGrp(grpVO);
				if (pointList == null) {
					logger.error("[sendContractPersonMultiComplete] 기업 조회에 실패하였습니다.");
					return result;
				} else {
					//현재 보유중인 포인트
					int point = resultPointVO.getCurPoint();
					//재전송 차감 포인트
					if (StringUtil.isNotNull(pointList.get(0).getResendPoint())) reducePoint = Integer.parseInt(pointList.get(0).getResendPoint());

					if ((point - (reducePoint*list.size())) < 0) {
						logger.info("[sendContractPersonMultiComplete] 잔여 포인트가 존재하지 않습니다. 현재포인트 : <"+point+">, 차감 포인트 : <"+(reducePoint*list.size())+">");
						return result;
					}
				}

				logger.info("sendContractPersonMultiComplete : "+contractResultVO.getUserId() + ", "+contractResultVO.getContractDate());
				// 접속한 사용자정보
				EmpVO empVO		  = new EmpVO();
				empVO.setBizId(contractResultVO.getBizId());
				empVO.setUserId(contractResultVO.getUserId());
				EmpVO empResultVO = empDAO.getEmp(empVO);

				if (empResultVO == null) { // 인사목록에 없는경우 거래처목록도 확인
					empVO.setCustomerId(contractResultVO.getUserId());
					empResultVO = empDAO.getCustomer(empVO);
				}
				if (empResultVO == null) {
					logger.error("[sendContractPersonMultiComplete] 직원정보가 존재하지 않습니다.");
					return false;
				}
			}

            for (ContractPersonVO vo : list) {
                ContractPersonVO contractResultVO = contractDAO.getContractPerson(vo);
                if (contractResultVO == null) {
                    logger.error("[sendContractPersonMultiComplete] 문서정보가 존재하지 않습니다.");
                    return false;
                }

                PointVO pointVO = new PointVO();
                pointVO.setBizId(contractResultVO.getBizId());
                PointVO resultPointVO = pointDAO.getPoint(pointVO);
                if (resultPointVO == null) {
                    logger.error("[sendContractPersonMultiComplete] 포인트정보가 존재하지 않습니다.");
                    return false;
                }

                int reducePoint = 0;
                BizGrpVO grpVO = new BizGrpVO();
                grpVO.setBizId(contractResultVO.getBizId());
                List<BizGrpVO> pointList = bizDAO.getBizGrp(grpVO);

                if (pointList == null) {
                    logger.error("[sendContractPersonMultiComplete] 기업 조회에 실패하였습니다.");
                    return result;
                } else {
                    //현재 보유중인 포인트
                    int point = resultPointVO.getCurPoint();
                    //재전송 차감 포인트
                    if (StringUtil.isNotNull(pointList.get(0).getResendPoint())) {
                        reducePoint = Integer.parseInt(pointList.get(0).getResendPoint());
                    }
                }

                logger.info("sendContractPersonMultiComplete : " + contractResultVO.getUserId() + ", " + contractResultVO.getContractDate());
                // 접속한 사용자정보
                EmpVO empVO = new EmpVO();
                empVO.setBizId(contractResultVO.getBizId());
                empVO.setUserId(contractResultVO.getUserId());

				EmpVO empResultVO 		= empDAO.getEmp(empVO);
                Boolean customerUseFlag = false;

                String empEmail = "";
                String empName  = "";
                String phoneNum = "";


                if (empResultVO == null) { // 인사목록에 없는경우 거래처목록도 확인
                    empVO.setCustomerId(contractResultVO.getUserId());
                    empResultVO = empDAO.getCustomer(empVO);
                    customerUseFlag = true;
                }
                if (empResultVO == null) {
                    logger.error("[sendContractPersonMultiComplete] 직원정보가 존재하지 않습니다.");
                    return false;
                } else {
                    if (customerUseFlag) { // 거래처목록인 경우 거래처테이블의 값 세팅
                        empEmail = empResultVO.getManagerEmail();
                        empName  = empResultVO.getCustomerName();
                        phoneNum = empResultVO.getManagerPhoneNum();
                    } else {
                        empEmail = empResultVO.getEMail();
                        empName  = empResultVO.getEmpName();
                        phoneNum = empResultVO.getPhoneNum();
                    }
                }

                ContractPersonVO contractPersonVO = new ContractPersonVO();
                contractPersonVO.setContId(contractResultVO.getContId());

                //완료된 파일을 전송할 경우 상태코드 값을 변환하지 않음.
                contractPersonVO.setSendDate(DateUtil.getTimeStamp(7));
                String digitNonce = StringUtil.getRandom(15);
                contractPersonVO.setDigitNonce(digitNonce);
                contractPersonVO.setSendType(vo.getSendType());

                // 계약정보 업데이트
                int nResult = contractDAO.updContractPerson(contractPersonVO);

                String logMessage = "";
                if (nResult > 0) {
                    logMessage = contractResultVO.getEmpName() + "님의 전자문서를 전송하였습니다.";
                    count++;
                }
                // 로그내역 등록
                ContractPersonLogVO logVO = new ContractPersonLogVO();
                logVO.setContId(contractResultVO.getContId());
                logVO.setServiceId(contractResultVO.getServiceId());
                logVO.setBizId(contractResultVO.getBizId());
                logVO.setUserId(contractResultVO.getUserId());
                logVO.setContractDate(contractResultVO.getContractDate());
                logVO.setContractId(contractResultVO.getContractId());
                logVO.setLogType("3");
                logVO.setLogMessage(logMessage);
                logVO.setInsEmpNo(loginVO.getUserId());
                contractDAO.insContractPersonLog(logVO);
                if (count > 0) {

                    String kkoLinkURL = System.getProperty("biztalk.kko.contract_download_url") + "?id=" + digitNonce;

                    if (vo.getSendType().equals("EMAIL")) {
                        if (!StringUtil.isNotNull(empEmail)) {
                            logger.error("[sendContractPersonMulti] " + empName + "님의 이메일정보" + empEmail + "가 없습니다.");
                            return false;
                        }
                        // (이메일 전송)완료된 전자문서 재발송
                        String content = "";

                        content += "<!DOCTYPE html> ";
                        content += "<html> ";
                        content += "<head> ";
                        content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
                        content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
                        content += "<title>전자문서</title> ";
                        content += "</head> ";
                        content += "<body> ";
                        content += "	<div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
                        content += "		<div style=\" padding: 37px 0 0 0;\"> ";
                        content += "			<div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
                        content += "				<span style=\"font-weight: bold; color:#00a9e9;\">IEUM</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;SIGN</span> ";
                        content += "			</div> ";
                        content += "			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
                        content += "				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">최종 전자문서</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br> ";
                        content += "				발송 이메일입니다.</span> ";
                        content += "			</div> ";
                        content += "			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
                        content += "				<span >" + empName + "</span><span>님!<br> ";
                        content += "				하단의 '전자문서' 버튼을 클릭하여 전자문서를 확인 해주시기 바랍니다.<br> ";
                        content += "				전달해드린 전자문서는 법적으로 보장되는 파일이므로 다운로드 하여 보관해주시기 바랍니다.<br> ";
                        content += "				</span> ";
                        content += "			</div> ";
                        content += "		<div style=\"margin: 74px 0 32px; text-align: center;\"> ";
                        content += "			<a href=\"" + kkoLinkURL + "\" style=\"display: inline-block; padding: 12px 90px; border-radius: 8px; font-family: 'tahoma'; font-size: 18px; font-weight: bold; text-decoration: none; color: #fff; background-color: #00a9e9;\">전자문서</a>";
                        content += "		</div> ";
                        content += "		</div> ";
                        content += "		<div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
                        content += "			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
                        content += "				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
                        content += "				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">service@newzenpnp.com</a></span > ";
                        content += "				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
                        content += "			</div> ";
                        content += "		</div> ";
                        content += "	</div> ";
                        content += "</body> ";
                        content += "</html> ";

                        MultiPartEmail email = new MultiPartEmail();
                        MailVO mailVO = new MailVO();
                        mailVO.setFrom("no_reply@newzenpnp.com");
                        mailVO.setTo(empEmail);
                        mailVO.setCc("");
                        mailVO.setBcc("");
                        mailVO.setSubject(empResultVO.getBizName() + "와(과) " + empName + "님의 전자문서 원본파일이 도착하였습니다.");
                        mailVO.setText(content);

                        logger.info("Ieum Sign 계약서 이메일을 발송. email : " + empEmail + " digitNonce : " + digitNonce);

                        email.send(mailVO);
                    } else {
                        // (알림톡 전송)완료된 전자문서 재발송
                        String content = "[" + empResultVO.getBizName() + " * 전자문서]\n\n";
                        content += empName + "님의 " + contractResultVO.getContractFileName() + "가(이) 도착했습니다.\n";
                        content += "본 문서는 " + empResultVO.getBizName() + "에서 " + empName + "님에게 발송한 최종 전자문서입니다.\n\n";
                        content += "하단의 링크를 클릭하여 최종 전자문서를 다운로드 받아 보관하세요.\n\n";
                        content += "저장 방법 안내\n\n";
                        content += "1. Android 폰의 경우";
                        content += "\n";
                        content += System.getProperty("system.feb.url") + "manual/android.html";
                        content += " \n\n";
                        content += "2. 아이폰의 경우";
                        content += "\n";
                        content += System.getProperty("system.feb.url") + "manual/ios.html";
                        content += "\n\n";
                        content += kkoLinkURL;

                        logger.info(content);
                        BizTalkKKOVO kkoVO = new BizTalkKKOVO();
                        kkoVO.setSubject("");
                        kkoVO.setContent(content);
                        kkoVO.setCallback(System.getProperty("biztalk.kko.callback_number"));
                        kkoVO.setRecipientNum(phoneNum);
                        kkoVO.setSenderKey(System.getProperty("biztalk.kko.sender_key"));
                        kkoVO.setTemplateCode("contract016");
                        kkoVO.setKkoBtnType("1");
                        kkoVO.setKkoBtnInfo("최종 전자문서^WL^" + kkoLinkURL + "^" + kkoLinkURL);
                        bizTalkDAO.insBizTalk(kkoVO);

                    }

                    // 포인트 차감
                    pointVO.setCurPoint(reducePoint);
                    pointDAO.balancePoint(pointVO);

                    PointLogVO pointLogVO = new PointLogVO();
                    pointLogVO.setBizId(contractResultVO.getBizId());
                    pointLogVO.setUserId(contractResultVO.getUserId());
                    pointLogVO.setServiceId(contractResultVO.getServiceId());
                    pointLogVO.setPointType("2");
                    pointLogVO.setPointPrice(pointVO.getCurPoint());
                    pointLogVO.setPointResult(resultPointVO.getCurPoint() - pointVO.getCurPoint());
                    pointLogVO.setEtcDesc("전자문서 재전송 포인트 차감");
                    logger.info("전자문서 재전송 <" + reducePoint + ">포인트 차감");

                    pointDAO.insPointLog(pointLogVO);
                }
            }
		} catch (Exception e) { e.printStackTrace(); }

		if (list.size() == count) result = true;

		return result;
	}


	// 계약서 PDF 보기
	@Override
	public List<PageVO> getContractPersonPdfView(ContractPersonVO vo) throws Exception {

		List<PageVO> resultVO = new ArrayList<>();
		ContentVO contentVO	  = new ContentVO();
		CabinetVO cabinetVO	  = new CabinetVO();

		vo.setResultCode("0");

		//근로게약서 원본PDF파일
		String fileId = ""; // 근로계약서 최종 콘텐츠ID

		ContractPersonVO contractResultVO = contractDAO.getContractPersonNonce(vo);
		if (contractResultVO == null) {
			vo.setResultCode("101");
			vo.setResultMessage("[getContractPersonPdfView] 최종 전자문서 정보가 존재하지 않습니다.");
			logger.error("[getContractPersonPdfView] 최종 전자문서 정보가 존재하지 않습니다.");
			return null;
		}

		// 최종 계약서ID
		fileId = contractResultVO.getContractId();
		// 콘텐츠에서 정보 추출
		contentVO.setFileId(fileId);
		ContentVO contentResultVO = contentDAO.getContent(contentVO);
		if (contentResultVO == null) {
			vo.setResultCode("103");
			vo.setResultMessage("[getContractPersonPdfView] 콘텐츠 정보가 존재하지 않습니다.");
			logger.error("[getContractPersonPdfView] 콘텐츠 정보가 존재하지 않습니다.");
			return null;
		}

		// 저장위치
		cabinetVO.setClassId(contentResultVO.getClassId());
		cabinetVO.setCabinetId(contentResultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO == null) {
			vo.setResultCode("104");
			vo.setResultMessage("[getContractPersonPdfView] 캐비넷 정보가 존재하지 않습니다.");
			logger.error("[getContractPersonPdfView] 캐비넷 정보가 존재하지 않습니다.");
			return null;
		}

		String originalPdfPath = cabinetResultVO.getCabinetPath()+contentResultVO.getFilePath()+contentResultVO.getFileName();
		String targetPdfName   = StringUtil.getUUID()+".pdf";
		String targetPdfPath   = MultipartFileUtil.getSystemTempPath()+targetPdfName;
		// temp pdf파일 설정
		vo.setPdfFile(targetPdfPath);

		// 사용할 PDF복사
		FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
		if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {

			FileInputStream fin = new FileInputStream(targetPdfPath);
			PdfReader reader    = new PdfReader(fin);
			reader.getNumberOfPages();
			fin.close();

			String exeName = getSystemPath() + "data/mudraw.exe";
			resultVO 	   = ProcUtil.readPDF(exeName, targetPdfPath, MultipartFileUtil.getSystemTempPath());
            for (PageVO pageVO : resultVO) {
                String filePath = StringUtil.ReplaceAll(MultipartFileUtil.getSystemTempPath(), getSystemPath(), "");
                pageVO.setFilePath(filePath);
            }
		}

		//완료된 계약서일 경우 상태코드를 변경하지 않는다.
		if (!contractResultVO.getStatusCode().equals("Y")) {
			vo.setContId(contractResultVO.getContId());
			vo.setStatusCode("4");
			vo.setComment(contractResultVO.getComment());
			int statusResult = contractDAO.updContractPerson(vo);
			if (statusResult == 0) {
				vo.setResultCode("105");
				vo.setResultMessage("[getContractPersonPdfView] 진행상태가 변경이 되지 않습니다.");
			}
		}

		// 로그테이블 생성
		ContractPersonLogVO logVO = new ContractPersonLogVO();
		logVO.setContId(contractResultVO.getContId());
		logVO.setServiceId(contractResultVO.getServiceId());
		logVO.setBizId(contractResultVO.getBizId());
		logVO.setUserId(contractResultVO.getUserId());
		logVO.setContractDate(contractResultVO.getContractDate());
		logVO.setContractId(contractResultVO.getContractId());
		logVO.setLogType("4");
		logVO.setLogMessage(contractResultVO.getEmpName()+"님께서 전자문서를 확인하였습니다.");
		logVO.setInsEmpNo(contractResultVO.getUserId());
		contractDAO.insContractPersonLog(logVO);

		return resultVO;
	}


	// 계약서 저장
	@Override
	public boolean saveContract(ContractPersonVO vo) throws Exception {

		boolean bResult			  = false;
		ContractPersonVO personVO = new ContractPersonVO();
		ContentVO contentVO		  = new ContentVO();
		CabinetVO cabinetVO		  = new CabinetVO();
		List<FieldVO> fieldListVO = new ArrayList<>();

		personVO.setDigitNonce(vo.getDigitNonce());
		ContractPersonVO contractResultVO = contractDAO.getContractPersonNonce(personVO);
		if (contractResultVO == null) {
			vo.setResultMessage("최종 전자문서 정보가 존재하지 않습니다.");
			logger.error("[saveContract] 최종 전자문서 정보가 존재하지 않습니다.");
			return false;
		}

		EmpVO empVO = new EmpVO();
		EmpVO resultEmpVO;
		empVO.setBizId(contractResultVO.getBizId());
		empVO.setUserId(contractResultVO.getUserId());
		empVO.setStartPage(0);
		empVO.setEndPage(10);
		List<EmpVO> empList = empDAO.getEmpList(empVO);
		if (empList == null || empList.isEmpty()) {
			vo.setResultMessage("사원 정보가 존재하지 않습니다.");
			logger.error("[saveContract] 사원 정보가 존재하지 않습니다.");
			return false;
		} else {
			resultEmpVO=empList.get(0);
		}

		// 콘텐츠에서 정보 추출
		contentVO.setFileId(contractResultVO.getContractId());
		ContentVO contentResultVO = contentDAO.getContent(contentVO);
		if (contentResultVO == null) {
			vo.setResultMessage("콘텐츠 정보가 존재하지 않습니다.");
			logger.error("[saveContract] 콘텐츠 정보가 존재하지 않습니다.");
			return false;
		}

		// 저장위치
		cabinetVO.setClassId(contentResultVO.getClassId());
		cabinetVO.setCabinetId(contentResultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO == null) {
			vo.setResultMessage("캐비넷 정보가 존재하지 않습니다.");
			logger.error("[saveContract] 캐비넷 정보가 존재하지 않습니다.");
			return false;
		}

		//양식정보 조회
		ContractPersonNewVO personNewVO = contractDAO.getContractPersonSign(contractResultVO);
		if (personNewVO == null) {
			vo.setResultMessage("양식 정보가 존재하지 않습니다.");
			logger.error("[saveContract] 양식 정보가 존재하지 않습니다.");
			return false;
		}

		String systemTempPath  = MultipartFileUtil.getSystemTempPath();
		String originalPdfPath = cabinetResultVO.getCabinetPath()+contentResultVO.getFilePath()+contentResultVO.getFileName();
		String targetPdfName   = StringUtil.getUUID()+".pdf";
		String targetPdfPath   = systemTempPath+targetPdfName;

		// 사용할 PDF복사
		FileUtil.createDirectory(systemTempPath);
		if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {

			InputStream is = Files.newInputStream(Paths.get(targetPdfPath));
			List<FormVO> formList = FieldUtil.getAllField(is, false);
			is.close();
            for (FormVO formVO : formList) {
                String formId = formVO.getId();

                List<ContractFormVO> dataFormList = vo.getFormList();
                for (ContractFormVO dataFormVO : dataFormList) {
                    if (formId.equals(dataFormVO.getFormDataId())) {
                        FieldVO fieldVO = new FieldVO();
                        fieldVO.setId(formId);
                        if (dataFormVO.getFormDataValue() != null && !dataFormVO.getFormDataValue().isEmpty()) {
                            fieldVO.setValue(dataFormVO.getFormDataValue());
                            //DB처리
                            ContractFormVO contractFormVO = new ContractFormVO();
                            contractFormVO.setContId(contractResultVO.getContId());
                            contractFormVO.setFormDataId(formId);
                            String encodeYn = contractDAO.getEncodeYn(contractFormVO);

                            if (encodeYn.equals("Y")) contractFormVO.setFormDataValue(SecurityUtil.getinstance().aesEncode(dataFormVO.getFormDataValue()));
                            else 					  contractFormVO.setFormDataValue(dataFormVO.getFormDataValue());

                            contractDAO.updContractForm(contractFormVO);
                        }

                        if (dataFormVO.getFormDataImage() != null && !dataFormVO.getFormDataImage().isEmpty()) {
                            String imgFilePath = dataFormVO.getFormDataImage();
                            if (FileUtil.getFileSize(imgFilePath) > 1024) {
                                //사진 원본 첨부 여부가 Y일 경우 resize하지 않음.
                                if (personNewVO.getOrgImgYn().equals("Y")) {
                                    String imgSaveFilePath = MultipartFileUtil.getSystemTempPath() + MultipartFileUtil.getFilenameToSave(MultipartFileUtil.getSystemTempPath(), FileUtil.getRealName(imgFilePath));
                                    logger.info("image path:" + imgFilePath);
                                    logger.info("save image path:" + imgSaveFilePath);
                                    //JPG만 이미지 rotate
                                    ImageUtil.rotatePicture(imgFilePath, imgSaveFilePath, FileUtil.getFileExt(imgFilePath), 1.0f, 0.5);
                                    fieldVO.setImage(imgSaveFilePath);
                                } else {
                                    String imgSaveFilePath = MultipartFileUtil.getSystemTempPath() + MultipartFileUtil.getFilenameToSave(MultipartFileUtil.getSystemTempPath(), FileUtil.getRealName(imgFilePath));
                                    logger.info("image path:" + imgFilePath);
                                    logger.info("save image path:" + imgSaveFilePath);
                                    ImageUtil.resizePicture(imgFilePath, imgSaveFilePath, FileUtil.getFileExt(imgFilePath), 1.0f, 0.5);
                                    fieldVO.setImage(imgSaveFilePath);
                                }
                            }
                        }
                        fieldListVO.add(fieldVO);
                    }
                }
            }

			//파일생성
			boolean bWritePDF = ControlUtil.writePDF(fieldListVO, originalPdfPath, targetPdfPath, false, false, System.getProperty("pdf.font.default"));

			//콘텐츠등록
			if (bWritePDF) {
				// 콘텐츠등록
				contentVO = new ContentVO();
				contentVO.setFileType(FileUtil.getFileExt(targetPdfPath));
				contentVO.setFileName(targetPdfName);
				contentVO.setOrgFileName(resultEmpVO.getEmpName()+"_"+contractResultVO.getContractFileName()+"_"+contractResultVO.getContractDate()+".pdf");
				contentVO.setFileSize(Long.toString(FileUtil.getFileSize(targetPdfPath)));
				contentVO.setFilePath(systemTempPath);
				contentVO.setFileTitle(resultEmpVO.getEmpName()+"_"+contractResultVO.getContractFileName()+"_"+contractResultVO.getContractDate());
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
				if (ContentService.COMPLETED == nResult) {
					logger.info("[saveContract] update FileID : "+nResult + ","+contractResultVO.getContId()+","+contractResultVO.getServiceId()+"/"+contractResultVO.getBizId()+"/"+contractResultVO.getUserId()+"/"+contractResultVO.getContractDate());
					FileUtil.deleteFile(targetPdfPath);
					logger.info("[saveContract] TempFile Deleted!");

					// 변경된 계약서 업데이트
					contractResultVO.setContractId(contentVO.getFileId());
					contractResultVO.setComment(null);

					if (contractResultVO.getStatusCode().equals("P2")) {
						//정보입력(사전)
						contractResultVO.setStatusCode("P3");
						contractResultVO.setSendDate("");
						contractDAO.updContractPerson(contractResultVO);

						// 로그테이블 생성
						ContractPersonLogVO logVO = new ContractPersonLogVO();
						logVO.setContId(contractResultVO.getContId());
						logVO.setServiceId(contractResultVO.getServiceId());
						logVO.setBizId(contractResultVO.getBizId());
						logVO.setUserId(contractResultVO.getUserId());
						logVO.setContractDate(contractResultVO.getContractDate());
						logVO.setContractId(contentVO.getFileId());
						logVO.setLogType("P3");
						logVO.setLogMessage(resultEmpVO.getEmpName()+"님께서 전자문서에 정보를 입력하였습니다.");
						logVO.setInsEmpNo(contractResultVO.getUserId());
						contractDAO.insContractPersonLog(logVO);
					} else {
						//추가수집정보
						contractDAO.updContractPerson(contractResultVO);

						// 로그테이블 생성
						ContractPersonLogVO logVO = new ContractPersonLogVO();
						logVO.setContId(contractResultVO.getContId());
						logVO.setServiceId(contractResultVO.getServiceId());
						logVO.setBizId(contractResultVO.getBizId());
						logVO.setUserId(contractResultVO.getUserId());
						logVO.setContractDate(contractResultVO.getContractDate());
						logVO.setContractId(contentVO.getFileId());
						logVO.setLogType("E");
						logVO.setLogMessage(resultEmpVO.getEmpName()+"님의 전자문서에 추가정보를 입력하였습니다.");
						logVO.setInsEmpNo(contractResultVO.getUserId());
						contractDAO.insContractPersonLog(logVO);
					}
					bResult = true;
				} else {
					vo.setResultMessage("[saveContract] transContent Error Code : " + nResult);
					logger.error("[saveContract] transContent Error Code : " + nResult);
				}
			}
		}
		return bResult;
	}


	// 서명 저장
	@Override
	public boolean signContract(UserSignDataVO vo) throws Exception {

		boolean bResult			  = false;
		ContractPersonVO personVO = new ContractPersonVO();
		ContentVO contentVO		  = new ContentVO();
		CabinetVO cabinetVO		  = new CabinetVO();
		List<FieldVO> fieldListVO = new ArrayList<>();
		List<LineVO> lines		  = new ArrayList<>();

		// PDF 서명정보 객체생성
		String line = vo.getLine();
		if (line.isEmpty()) {
			vo.setMessage("서명 정보가 존재하지 않습니다.");
			logger.error("[signContract] 서명 정보가 존재하지 않습니다.");
			return false;
		}

		String[] lineData = StringUtil.split(line, "|");
		if (lineData.length == 0) {
			vo.setMessage("서명 상세정보가 존재하지 않습니다.");
			logger.error("[signContract] 서명 상세정보가 존재하지 않습니다.");
			return false;
		}

        for (String userLine : lineData) {
            if (!userLine.isEmpty()) {

                String[] userLineData = StringUtil.split(userLine, "-");

                if (userLineData.length == 4) {

                    String lineId    = userLineData[0];
                    String lineWidth = userLineData[1];
                    String lineX     = userLineData[2];
                    String lineY     = userLineData[3];
                    LineVO lineVO    = new LineVO();

                    lineVO.setId(lineId);
                    lineVO.setPen(1);
                    lineVO.setSize(Integer.parseInt(lineWidth));
                    lineVO.setX(Float.parseFloat(lineX));
                    lineVO.setY(Float.parseFloat(lineY));
                    lines.add(lineVO);
                } else {
                    logger.error("[signContract] 사용자 서명 상세정보가 정상적이지 않습니다.." + userLine);
                }
            }
        }

		personVO.setDigitNonce(vo.getId());
		ContractPersonVO contractResultVO = contractDAO.getContractPersonNonce(personVO);
		if (contractResultVO == null) {
			vo.setMessage("최종 전자문서 정보가 존재하지 않습니다.");
			logger.error("[signContract] 최종 전자문서 정보가 존재하지 않습니다.");
			return false;
		}

		EmpVO empVO = new EmpVO();
		EmpVO resultEmpVO;
		empVO.setBizId(contractResultVO.getBizId());
		empVO.setUserId(contractResultVO.getUserId());
		empVO.setStartPage(0);
		empVO.setEndPage(10);

		List<EmpVO> empList = empDAO.getEmpList(empVO);

		if (empList == null || empList.isEmpty()) {
			vo.setMessage("사원 정보가 존재하지 않습니다.");
			logger.error("[signContract] 사원 정보가 존재하지 않습니다.");
			return false;
		} else { resultEmpVO = empList.get(0); }

		// 콘텐츠에서 정보 추출
		contentVO.setFileId(contractResultVO.getContractId());
		ContentVO contentResultVO = contentDAO.getContent(contentVO);
		if (contentResultVO == null) {
			vo.setMessage("콘텐츠 정보가 존재하지 않습니다.");
			logger.error("[signContract] 콘텐츠 정보가 존재하지 않습니다.");
			return false;
		}

		// 저장위치
		cabinetVO.setClassId(contentResultVO.getClassId());
		cabinetVO.setCabinetId(contentResultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO == null) {
			vo.setMessage("캐비넷 정보가 존재하지 않습니다.");
			logger.error("[signContract] 캐비넷 정보가 존재하지 않습니다.");
			return false;
		}

		String systemTempPath  = MultipartFileUtil.getSystemTempPath();
		String originalPdfPath = cabinetResultVO.getCabinetPath()+contentResultVO.getFilePath()+contentResultVO.getFileName();
		String targetPdfName   = StringUtil.getUUID()+".pdf";
		String targetTsaName   = StringUtil.getUUID()+".pdf";
		String targetPdfPath   = systemTempPath+targetPdfName;
		String targetTsaPath   = systemTempPath+targetTsaName;

		// 사용할 PDF복사
		FileUtil.createDirectory(systemTempPath);
		if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {

			// sign_2 (을:근로자) 서명란 key값 검색
			List<String> keyList = new ArrayList<>();

			InputStream is = Files.newInputStream(Paths.get(targetPdfPath));
			List<FormVO> formList = FieldUtil.getAllField(is, false);
			is.close();
            for (FormVO formVO : formList) {
                String formId = formVO.getId();
                logger.info("b1=" + formId);
                if (formId.contains("sign_2")) {
                    keyList.add(formId);
                    logger.info("b2=" + formId);
                }
            }
            for (String lineId : keyList) {
                if (StringUtil.isNotNull(lineId)) {
                    FieldVO fieldVO = new FieldVO();
                    fieldVO.setId(lineId);
                    fieldVO.setLines(lines);
                    fieldListVO.add(fieldVO);
                    logger.info("b3=" + lineId);
                }
            }
			//파일생성
			boolean bWritePDF = ControlUtil.writePDF(fieldListVO, originalPdfPath, targetPdfPath, false, true, System.getProperty("pdf.font.default"));

			//콘텐츠등록
			if (bWritePDF) {
				// TSA발급
				TSAUtil.timestampPdf(targetPdfPath, targetTsaPath, true, false, "newzenpnp", "");
				long tsaFileSize = FileUtil.getFileSize(targetTsaPath);
				if (tsaFileSize > 0) {

					// 콘텐츠등록
					contentVO = new ContentVO();
					contentVO.setFileType(FileUtil.getFileExt(targetTsaPath));
					contentVO.setFileName(targetTsaName);
					contentVO.setOrgFileName(resultEmpVO.getEmpName()+"_"+contractResultVO.getContractFileName()+"_"+contractResultVO.getContractDate()+".pdf");
					contentVO.setFileSize(Long.toString(FileUtil.getFileSize(targetTsaPath)));
					contentVO.setFilePath(systemTempPath);
					contentVO.setFileTitle(resultEmpVO.getEmpName()+"_"+contractResultVO.getContractFileName()+"_"+contractResultVO.getContractDate());
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
					if (ContentService.COMPLETED == nResult) {
						logger.info("[signContract] update FileID : "+nResult + ","+contractResultVO.getContId()+","+contractResultVO.getServiceId()+"/"+contractResultVO.getBizId()+"/"+contractResultVO.getUserId()+"/"+contractResultVO.getContractDate());
						FileUtil.deleteFile(targetPdfPath);
						logger.info("[signContract] TempFile Deleted!");

						// 난수생성 업데이트 진행
						String digitNonce = StringUtil.getRandom(20);

						// 상태코드 업데이트 및 보관일자 3년 추가
						String keepDate = DateUtil.getAddDate(365*3);
						contractResultVO.setContractId(contentVO.getFileId());
						contractResultVO.setTransType("Y");
						contractResultVO.setStatusCode("Y");
						contractResultVO.setEndDate(DateUtil.getTimeStamp(3));
						contractResultVO.setKeepDate(keepDate);
						contractResultVO.setDigitNonce(digitNonce);
						contractResultVO.setComment(null);
						contractDAO.updContractPerson(contractResultVO);

						// 로그테이블 생성
						ContractPersonLogVO logVO = new ContractPersonLogVO();
						logVO.setContId(contractResultVO.getContId());
						logVO.setServiceId(contractResultVO.getServiceId());
						logVO.setBizId(contractResultVO.getBizId());
						logVO.setUserId(contractResultVO.getUserId());
						logVO.setContractDate(contractResultVO.getContractDate());
						logVO.setContractId(contentVO.getFileId());
						logVO.setLogType("Y");
						logVO.setLogMessage(resultEmpVO.getEmpName()+"님의 " + XmlDataUtil.dateHFormat(contractResultVO.getContractDate())+ "일자에 최종 문서를  전송하였습니다.");
						logVO.setInsEmpNo(contractResultVO.getUserId());
						contractDAO.insContractPersonLog(logVO);

						String kkoLinkURL = System.getProperty("biztalk.kko.contract_download_url")+"?id="+digitNonce;
						// 최종계약서 전송 (이메일, 알림톡전송)
						if (contractResultVO.getSendType().equals("EMAIL")) {

							if (StringUtil.isNotNull(resultEmpVO.getEMail())) {

								// 근로계약서 이메일 전송
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
								content +="				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">최종 전자문서</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br> ";
								content +="				발송 이메일입니다.</span> ";
								content +="			</div> ";
								content +="			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
								content +="				<span >"+resultEmpVO.getEmpName()+"</span><span>님!<br> ";
								content +="				하단의 '전자문서' 버튼을 클릭하여 전자문서를 확인 해주시기 바랍니다.<br> ";
								content +="				전달해드린 전자문서는 법적으로 보장되는 파일이므로 다운로드 하여 보관해주시기 바랍니다.<br> ";
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
								mailVO.setTo(resultEmpVO.getEMail());
								mailVO.setCc("");
								mailVO.setBcc("");
								mailVO.setSubject(resultEmpVO.getBizName()+"와(과) "+resultEmpVO.getEmpName()+"님의 전자문서 원본파일이 도착하였습니다.");
								mailVO.setText(content);

								logger.info("Ieum Sign 계약서 이메일을 발송. email : " + resultEmpVO.getEMail() + " digitNonce : " + digitNonce);
								email.send(mailVO);
							} else {
								logger.info("[signContract] "+resultEmpVO.getEmpName()+"님의 이메일정보"+resultEmpVO.getEMail()+"가 없습니다.");
							}
						} else {
							// 알림톡 전송(근로자용 최종계약서)
							String content = "["+resultEmpVO.getBizName()+" * 전자문서]\n\n";
							content += resultEmpVO.getEmpName()+"님의 "+contractResultVO.getContractFileName()+"가(이) 도착했습니다.\n";
							content += "본 문서는 "+resultEmpVO.getBizName()+"에서 "+resultEmpVO.getEmpName()+"님에게 발송한 최종 전자문서입니다.\n\n";
							content += "하단의 링크를 클릭하여 최종 전자문서를 다운로드 받아 보관하세요.\n\n";
							content += "저장 방법 안내\n\n";
							content += "1. Android 폰의 경우";
							content += "\n";
							content += System.getProperty("system.feb.url") + "manual/android.html";
							content += " \n\n";
							content += "2. 아이폰의 경우";
							content += "\n";
							content += System.getProperty("system.feb.url") + "manual/ios.html";
							content += "\n\n";
							content += kkoLinkURL;

							BizTalkKKOVO kkoVO = new BizTalkKKOVO();
							kkoVO.setSubject("");
							kkoVO.setContent(content);
							kkoVO.setCallback(System.getProperty("biztalk.kko.callback_number"));
							kkoVO.setRecipientNum(resultEmpVO.getPhoneNum());
							kkoVO.setSenderKey(System.getProperty("biztalk.kko.sender_key"));
							kkoVO.setTemplateCode("contract016");
							kkoVO.setKkoBtnType("1");
							kkoVO.setKkoBtnInfo("최종 전자문서^WL^"+kkoLinkURL+"^"+kkoLinkURL);
							bizTalkDAO.insBizTalk(kkoVO);
						}
						bResult = true;
					} else {
						vo.setMessage("[signContract] transContent Error Code : " + nResult);
						logger.error("[signContract] transContent Error Code : " + nResult);
					}
				} else {
					vo.setMessage("[signContract] TSA issue Error");
					logger.error("[signContract]TSA issue Error ");
				}
			}
		}
		return bResult;
	}


	//
	@Override
	public boolean signContractCustomer(UserSignDataVO vo) throws Exception {

		boolean bResult			  = false;
		ContractPersonVO personVO = new ContractPersonVO();
		ContentVO contentVO		  = new ContentVO();
		CabinetVO cabinetVO		  = new CabinetVO();
		List<FieldVO> fieldListVO = new ArrayList<>();

		personVO.setDigitNonce(vo.getId());
		ContractPersonVO contractResultVO = contractDAO.getContractPersonNonce(personVO);
		if (contractResultVO==null) {
			vo.setMessage("최종 전자문서 정보가 존재하지 않습니다.");
			logger.error("[signContract] 최종 전자문서 정보가 존재하지 않습니다.");
			return false;
		}

		EmpVO empVO			   = new EmpVO();
		EmpVO resultEmpVO 	   = new EmpVO();
		empVO.setBizId(contractResultVO.getBizId());
		empVO.setUserId(contractResultVO.getUserId());
		empVO.setStartPage(0);
		empVO.setEndPage(10);
		List<EmpVO> empList = empDAO.getEmpList(empVO);
		if (empList==null || empList.isEmpty()) {
			empVO.setCustomerId(contractResultVO.getUserId());
			List<EmpVO> customerList = empDAO.getCustomerList(empVO);
			if (customerList==null || customerList.isEmpty()) {
				vo.setMessage("사원 정보가 존재하지 않습니다.");
				logger.error("[signContract] 사원 정보가 존재하지 않습니다.");
				return false;
			} else {
				resultEmpVO.setEmpName(customerList.get(0).getCustomerName());
				resultEmpVO.setBizName(customerList.get(0).getBizName());
				resultEmpVO.setEMail(customerList.get(0).getManagerEmail());
				resultEmpVO.setPhoneNum(customerList.get(0).getManagerPhoneNum());
			}
		} else { resultEmpVO=empList.get(0); }

		// 콘텐츠에서 정보 추출
		contentVO.setFileId(contractResultVO.getContractId());
		ContentVO contentResultVO = contentDAO.getContent(contentVO);
		if (contentResultVO==null) {
			vo.setMessage("콘텐츠 정보가 존재하지 않습니다.");
			logger.error("[signContract] 콘텐츠 정보가 존재하지 않습니다.");
			return false;
		}

		// 저장위치
		cabinetVO.setClassId(contentResultVO.getClassId());
		cabinetVO.setCabinetId(contentResultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if(cabinetResultVO==null) {
			vo.setMessage("캐비넷 정보가 존재하지 않습니다.");
			logger.error("[signContract] 캐비넷 정보가 존재하지 않습니다.");
			return false;
		}

		String systemTempPath  = MultipartFileUtil.getSystemTempPath();
		String originalPdfPath = cabinetResultVO.getCabinetPath()+contentResultVO.getFilePath()+contentResultVO.getFileName();
		String targetPdfName   = StringUtil.getUUID()+".pdf";
		String targetTsaName   = StringUtil.getUUID()+".pdf";
		String targetPdfPath   = systemTempPath+targetPdfName;
		String targetTsaPath   = systemTempPath+targetTsaName;

		String bizImage = "";
		// 이미지 생성(회사 직인 찍음)
		if(StringUtil.isNotNull(vo.getCustomerSignImg())&&!vo.getCustomerSignImg().equals("-")) {
			bizImage = getSystemPath()+"temp/file/"+vo.getCustomerSignImg();
		} else {
			bizImage = getSystemPath()+"images/sign/digitsign_1.png";
		}

		// 사용할 PDF복사
		FileUtil.createDirectory(systemTempPath);
		if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
			// sign_2 (을:근로자) 서명란 key값 검색
			List<String> keyList = new ArrayList<>();

			InputStream is = Files.newInputStream(Paths.get(targetPdfPath));
			List<FormVO> formList = FieldUtil.getAllField(is, false);
			is.close();
            for (FormVO formVO : formList) {
                String formId = formVO.getId();
                logger.info("b1=" + formId);
                if (formId.contains("sign_2")) {
                    keyList.add(formId);
                    logger.info("b2=" + formId);
                }
            }
            for (String lineId : keyList) {
                if (StringUtil.isNotNull(lineId)) {
                    FieldVO fieldVO = new FieldVO();
                    fieldVO.setId(lineId);
                    fieldVO.setImage(bizImage);
                    fieldListVO.add(fieldVO);
                    logger.info("b3=" + lineId);
                }
            }
			//파일생성
			boolean bWritePDF = ControlUtil.writePDF(fieldListVO, originalPdfPath, targetPdfPath, false, true, System.getProperty("pdf.font.default"));

			//콘텐츠등록
			if (bWritePDF) {
				// TSA발급
				TSAUtil.timestampPdf(targetPdfPath, targetTsaPath, true, false, "newzenpnp", "");
				long tsaFileSize = FileUtil.getFileSize(targetTsaPath);
				if (tsaFileSize>0) {

					// 콘텐츠등록
					contentVO = new ContentVO();
					contentVO.setFileType(FileUtil.getFileExt(targetTsaPath));
					contentVO.setFileName(targetTsaName);
					contentVO.setOrgFileName(resultEmpVO.getEmpName()+"_"+contractResultVO.getContractFileName()+"_"+contractResultVO.getContractDate()+".pdf");
					contentVO.setFileSize(Long.toString(FileUtil.getFileSize(targetTsaPath)));
					contentVO.setFilePath(systemTempPath);
					contentVO.setFileTitle(resultEmpVO.getEmpName()+"_"+contractResultVO.getContractFileName()+"_"+contractResultVO.getContractDate());
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
					if (ContentService.COMPLETED==nResult) {
						logger.info("[signContract] update FileID : "+nResult + ","+contractResultVO.getContId()+","+contractResultVO.getServiceId()+"/"+contractResultVO.getBizId()+"/"+contractResultVO.getUserId()+"/"+contractResultVO.getContractDate());
						FileUtil.deleteFile(targetPdfPath);
						logger.info("[signContract] TempFile Deleted!");

						// 난수생성 업데이트 진행
						String digitNonce = StringUtil.getRandom(20);

						// 상태코드 업데이트 및 보관일자 3년 추가
						String keepDate = DateUtil.getAddDate(365*3);
						contractResultVO.setContractId(contentVO.getFileId());
						contractResultVO.setTransType("Y");
						contractResultVO.setStatusCode("Y");
						contractResultVO.setEndDate(DateUtil.getTimeStamp(3));
						contractResultVO.setKeepDate(keepDate);
						contractResultVO.setDigitNonce(digitNonce);
						contractResultVO.setComment(null);
						contractDAO.updContractPerson(contractResultVO);

						// 로그테이블 생성
						ContractPersonLogVO logVO = new ContractPersonLogVO();
						logVO.setContId(contractResultVO.getContId());
						logVO.setServiceId(contractResultVO.getServiceId());
						logVO.setBizId(contractResultVO.getBizId());
						logVO.setUserId(contractResultVO.getUserId());
						logVO.setContractDate(contractResultVO.getContractDate());
						logVO.setContractId(contentVO.getFileId());
						logVO.setLogType("Y");
						logVO.setLogMessage(resultEmpVO.getEmpName()+"님의 " + XmlDataUtil.dateHFormat(contractResultVO.getContractDate())+ "일자에 최종 문서를  전송하였습니다.");
						logVO.setInsEmpNo(contractResultVO.getUserId());
						contractDAO.insContractPersonLog(logVO);

						String kkoLinkURL = System.getProperty("biztalk.kko.contract_download_url")+"?id="+digitNonce;
						// 최종계약서 전송 (이메일, 알림톡전송)
						if (contractResultVO.getSendType().equals("EMAIL")) {

							if (StringUtil.isNotNull(resultEmpVO.getEMail())) {
								// 근로계약서 이메일 전송
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
								content +="				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">최종 전자문서</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br> ";
								content +="				발송 이메일입니다.</span> ";
								content +="			</div> ";
								content +="			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
								content +="				<span >"+resultEmpVO.getEmpName()+"</span><span>님!<br> ";
								content +="				하단의 '전자문서' 버튼을 클릭하여 전자문서를 확인 해주시기 바랍니다.<br> ";
								content +="				전달해드린 전자문서는 법적으로 보장되는 파일이므로 다운로드 하여 보관해주시기 바랍니다.<br> ";
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
								mailVO.setTo(resultEmpVO.getEMail());
								mailVO.setCc("");
								mailVO.setBcc("");
								mailVO.setSubject(resultEmpVO.getBizName()+"와(과) "+resultEmpVO.getEmpName()+"님의 전자문서 원본파일이 도착하였습니다.");
								mailVO.setText(content);

								logger.info("Ieum Sign 계약서 이메일을 발송. email : " + resultEmpVO.getEMail() + " digitNonce : " + digitNonce);

								email.send(mailVO);
							} else {
								logger.info("[signContract] "+resultEmpVO.getEmpName()+"님의 이메일정보"+resultEmpVO.getEMail()+"가 없습니다.");
							}
						} else {
							// 알림톡 전송(근로자용 최종계약서)
							String content = "["+resultEmpVO.getBizName()+" * 전자문서]\n\n";
							content += resultEmpVO.getEmpName()+"님의 "+contractResultVO.getContractFileName()+"가(이) 도착했습니다.\n";
							content += "본 문서는 "+resultEmpVO.getBizName()+"에서 "+resultEmpVO.getEmpName()+"님에게 발송한 최종 전자문서입니다.\n\n";
							content += "하단의 링크를 클릭하여 최종 전자문서를 다운로드 받아 보관하세요.\n\n";
							content += "저장 방법 안내\n\n";
							content += "1. Android 폰의 경우";
							content += "\n";
							content += System.getProperty("system.feb.url") + "manual/android.html";
							content += " \n\n";
							content += "2. 아이폰의 경우";
							content += "\n";
							content += System.getProperty("system.feb.url") + "manual/ios.html";
							content += "\n\n";
							content += kkoLinkURL;

							BizTalkKKOVO kkoVO = new BizTalkKKOVO();
							kkoVO.setSubject("");
							kkoVO.setContent(content);
							kkoVO.setCallback(System.getProperty("biztalk.kko.callback_number"));
							kkoVO.setRecipientNum(resultEmpVO.getPhoneNum());
							kkoVO.setSenderKey(System.getProperty("biztalk.kko.sender_key"));
							kkoVO.setTemplateCode("contract016");
							kkoVO.setKkoBtnType("1");
							kkoVO.setKkoBtnInfo("최종 전자문서^WL^"+kkoLinkURL+"^"+kkoLinkURL);
							bizTalkDAO.insBizTalk(kkoVO);
						}
						bResult = true;
					} else {
						vo.setMessage("[signContract] transContent Error Code : " + nResult);
						logger.error("[signContract] transContent Error Code : " + nResult);
					}
				} else {
					vo.setMessage("[signContract] TSA issue Error");
					logger.error("[signContract]TSA issue Error ");
				}
			}
		}
		return bResult;
	}



	@Override
	public boolean rejectContract(UserSignDataVO vo) {

		logger.info("rejectContract : "+vo.getId());

		boolean bResult 		  = false;
		ContractPersonVO personVO = new ContractPersonVO();

		personVO.setDigitNonce(vo.getId());
		ContractPersonVO contractResultVO = contractDAO.getContractPersonNonce(personVO);
		if (contractResultVO==null) {
			logger.error("[rejectContract] 최종 전자문서 정보가 존재하지 않습니다.");
			return false;
		}

		logger.info("[rejectContract] contId => "+contractResultVO.getContId());
		// 상태코드 업데이트
		ContractPersonVO contractVO = new ContractPersonVO();
		contractVO.setContId(contractResultVO.getContId());
		contractVO.setStatusCode("5");
		contractVO.setContractId("");
		contractVO.setDigitNonce("");
		int statusResult = contractDAO.updContractPerson(contractVO);

		// 로그테이블 생성
		ContractPersonLogVO logVO = new ContractPersonLogVO();
		logVO.setContId(contractResultVO.getContId());
		logVO.setServiceId(contractResultVO.getServiceId());
		logVO.setBizId(contractResultVO.getBizId());
		logVO.setUserId(contractResultVO.getUserId());
		logVO.setContractDate(contractResultVO.getContractDate());
		logVO.setContractId(contractResultVO.getContractId());
		logVO.setLogType("5");
		logVO.setLogMessage(vo.getMessage());
		logVO.setInsEmpNo(contractResultVO.getUserId());
		contractDAO.insContractPersonLog(logVO);

		if (statusResult>0) bResult = true;

		return bResult;
	}


	// 일괄계약서 삭제
	@Override
	public int delContractPersonList(ContractPersonVO vo) {
		int result = 0;
		logger.info("delContractPersonList");

		try {

			List<ContractPersonVO> contractList = contractDAO.delContractPersonList(vo);

            for (ContractPersonVO personVO : contractList) {
                vo = personVO;

                // 완료된 계약서 제외
                if (!vo.getStatusCode().equals("Y")) {

                    ContractPersonVO contractPersonVO = new ContractPersonVO();
                    contractPersonVO.setContId(vo.getContId());
                    contractPersonVO.setStatusCode("D");
                    // 계약정보 업데이트
                    result = contractDAO.updContractPerson(contractPersonVO);

                    // 계약서 파일 삭제
                    List<ContentVO> contentList = new ArrayList<>();

                    ContractPersonLogVO contractPersonLogVO = new ContractPersonLogVO();

                    contractPersonLogVO.setServiceId(ContractService.SERVICE_ID);
                    contractPersonLogVO.setBizId(vo.getBizId());
                    contractPersonLogVO.setUserId(vo.getUserId());
                    contractPersonLogVO.setContractDate(vo.getContractDate());

                    List<ContractPersonLogVO> loglist = contractDAO.getContractPersonLogList(contractPersonLogVO);

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
                    // 삭제할 계약서 존재시 삭제처리 실제 파일만 삭제 (DB는 보존)
                    if (!contentList.isEmpty()) contentService.deleteContent(contentList);

                    // 로그테이블 생성
                    ContractPersonLogVO logVO = new ContractPersonLogVO();
                    logVO.setContId(vo.getContId());
                    logVO.setServiceId(vo.getServiceId());
                    logVO.setBizId(vo.getBizId());
                    logVO.setUserId(vo.getUserId());
                    logVO.setContractDate(vo.getContractDate());
                    logVO.setContractId(vo.getContractId());
                    logVO.setLogType("D");
                    logVO.setLogMessage(vo.getEmpName() + "님의 전자문서를 보관기간 만료로 인하여 삭제하였습니다.");
                    logVO.setInsEmpNo(vo.getUserId());
                    contractDAO.insContractPersonLog(logVO);

                    // 실제처리건수
                    result++;
                }
            }
		} catch (Exception e) { e.printStackTrace(); }
		return result;
	}



	@Override
	public int downloadContractSelectZip(List<ContractPersonVO> list, SessionVO loginVO) {
		int result 										= 0;
		List<ContractPersonLogVO> ContractPersonLogList = new ArrayList<>();
		List<String> contractList 						= new ArrayList<>();

        for (ContractPersonVO contractPersonVO : list) {
            ContractPersonVO contractVO = contractDAO.getContractPerson(contractPersonVO);
            ContentVO contentVO 		= new ContentVO();
            contentVO.setFileId(contractVO.getContractId());
            // 콘텐츠에서 정보 추출
            ContentVO resultContentVO = contentDAO.getContent(contentVO);

            if (resultContentVO == null) {
                logger.error("[downloadContractSelectZip] 콘텐츠 정보가 존재하지 않습니다.");
                return 0;
            }

            // 저장위치
            CabinetVO cabinetVO = new CabinetVO();
            cabinetVO.setClassId(resultContentVO.getClassId());
            cabinetVO.setCabinetId(resultContentVO.getCabinetId());

            CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);

            if (cabinetResultVO == null) {
                logger.error("[downloadContractSelectZip] 캐비넷 정보가 존재하지 않습니다.");
                return 0;
            }

            //계약서
            if (contractPersonVO.getFileType().equals("C")) {
                String originalPdfPath = cabinetResultVO.getCabinetPath() + resultContentVO.getFilePath() + resultContentVO.getFileName();
                String targetPdfName = contractVO.getBizName() + "_" + contractVO.getEmpName() + "_" + contractVO.getEmpNo() + "_" + contractVO.getContId() + "_" + contractVO.getContractFileName() + ".pdf";
                String targetPdfPath = MultipartFileUtil.getSystemTempPath() + targetPdfName;

                // 사용할 PDF복사
                FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());

                if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) contractList.add(targetPdfPath);

                ContractPersonLogVO logVO = new ContractPersonLogVO();
                logVO.setContId(contractVO.getContId());
                logVO.setServiceId(SERVICE_ID);
                logVO.setBizId(contractVO.getBizId());
                logVO.setUserId(contractVO.getUserId());
                logVO.setContractDate(contractVO.getContractDate());
                logVO.setContractId(contentVO.getFileId());
                logVO.setLogType("7");
                logVO.setLogMessage(contractVO.getEmpName() + "님의 전자문서를 다운로드 하였습니다.");
                logVO.setInsEmpNo(loginVO.getUserId());
                ContractPersonLogList.add(logVO);

            } else {
                String targetPdfName = contractVO.getBizName() + "_" + contractVO.getEmpName() + "_" + contractVO.getEmpNo() + "_" + contractVO.getContId() + "_감사추적인증서.pdf";
                String targetPdfPath = MultipartFileUtil.getSystemTempPath() + targetPdfName;
                logger.info("감사추적인증서 경로 => " + targetPdfPath);
                contractList.add(targetPdfPath);
            }
        }

		if (!contractList.isEmpty()) {
			logger.info(list.get(0).getPdfFile());
			result = FileUtil.ZipFile(list.get(0).getPdfFile(), contractList);
		}

		//로그등록
		logger.info("등록될 로그 수 : "+ContractPersonLogList.size()+"개");

        for (ContractPersonLogVO contractPersonLogVO : ContractPersonLogList) contractDAO.insContractPersonLog(contractPersonLogVO);

		return result;
	}



	@Override
	public int setContractNewData(String bizId) throws Exception {
		int result = 0;

		// 기본 근로계약서 등록
		ContractPersonNewVO contractNewVO = new ContractPersonNewVO();
		ContentVO contractHwpVO = new ContentVO();
		ContentVO contractXlsVO = new ContentVO();
		ContentVO contractPdfVO = new ContentVO();
		// 1. 서식등록(hwp)
		String originalFileTitle = "제공_근로계약서_고객작성";
		String originalFileExt   = "hwp";
		String originalFilePath  = getSystemPath()+"data/contract/"+originalFileTitle+"."+originalFileExt;
		String targetFileName    = StringUtil.getUUID()+"."+originalFileExt;
		String targetFilePath    = MultipartFileUtil.getSystemTempPath()+targetFileName;

		logger.info(originalFilePath);
		logger.info(targetFilePath);
		// 사용할 PDF복사
		FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
		if (FileUtil.fileCopy(originalFilePath, targetFilePath)) {
			contractHwpVO.setFileType(originalFileExt);
			contractHwpVO.setFileName(targetFileName);
			contractHwpVO.setOrgFileName(originalFileTitle+"."+originalFileExt);
			contractHwpVO.setFileSize(Long.toString(FileUtil.getFileSize(targetFilePath)));
			contractHwpVO.setFilePath(MultipartFileUtil.getSystemTempPath());
			contractHwpVO.setFileTitle(originalFileTitle);
			contractHwpVO.setClassId(ContractService.UPLOAD_CLASS_ID);
			contractHwpVO.setFileVersion("0");
			contractHwpVO.setParFileId("");
			contractHwpVO.setEtcDesc("");
			contractHwpVO.setCheckInOut("N");
			contractHwpVO.setCheckUserId("");
			contractHwpVO.setCheckCount("0");
			contractHwpVO.setUseYn("Y");
			contractHwpVO.setDelYn("N");

			int nResult = contentService.transContent(contractHwpVO);

			// 양식등록

			if (ContentService.COMPLETED==nResult) {
				result++;
				contractNewVO.setServiceId(ContractService.SERVICE_ID);
				contractNewVO.setBizId(bizId);
				contractNewVO.setFileId(contractHwpVO.getFileId());
				contractNewVO.setContractType("1");
				//양식 최초등록(제공되는 양식)
				contractNewVO.setTransType("N");

				contractDAO.insContractNew(contractNewVO);

				// 2. 조건파일등록(xls)
				originalFileTitle = "제공_근로계약서_조건양식";
				originalFileExt = "xls";
				originalFilePath = getSystemPath()+"data/contract/"+originalFileTitle+"."+originalFileExt;
				targetFileName = StringUtil.getUUID()+"."+originalFileExt;
				targetFilePath = MultipartFileUtil.getSystemTempPath()+targetFileName;

				logger.info(originalFilePath);
				logger.info(targetFilePath);
				// 사용할 PDF복사
				FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
				if (FileUtil.fileCopy(originalFilePath, targetFilePath)) {
					contractXlsVO.setFileType(originalFileExt);
					contractXlsVO.setFileName(targetFileName);
					contractXlsVO.setOrgFileName(originalFileTitle+"."+originalFileExt);
					contractXlsVO.setFileSize(Long.toString(FileUtil.getFileSize(targetFilePath)));
					contractXlsVO.setFilePath(MultipartFileUtil.getSystemTempPath());
					contractXlsVO.setFileTitle(originalFileTitle);
					contractXlsVO.setClassId(ContractService.UPLOAD_CLASS_ID);
					contractXlsVO.setFileVersion("0");
					contractXlsVO.setParFileId("");
					contractXlsVO.setEtcDesc("");
					contractXlsVO.setCheckInOut("N");
					contractXlsVO.setCheckUserId("");
					contractXlsVO.setCheckCount("0");
					contractXlsVO.setUseYn("Y");
					contractXlsVO.setDelYn("N");

					nResult = contentService.transContent(contractXlsVO);

					// 양식등록
					if (ContentService.COMPLETED==nResult) {

						// 데이터 항목 정의
						List xlsData = ExcelUtil.readExcel(targetFilePath);
						int rownum = 1;
                        for (Object xlsDatum : xlsData) {
                            HashValue hv = (HashValue) xlsDatum;

                            if (hv.row == 1 && hv.col > 2) {
								String formDataName = hv.value;
								logger.info("col>" + hv.col + "-" + formDataName);

								ContractPersonNewFormVO formVO = new ContractPersonNewFormVO();
								formVO.setServiceId(ContractService.SERVICE_ID);
								formVO.setBizId(bizId);
								formVO.setFileId(contractHwpVO.getFileId());
								formVO.setFormDataId("TXT_" + StringUtil.lpad(Integer.toString(rownum), 5, "0"));
								formVO.setFormDataName(formDataName);

								contractDAO.delContractNewForm(formVO);
								contractDAO.insContractNewForm(formVO);
								rownum++;
							}
						}
						result++;
					}
				}

				// 3. 양식등록(pdf)
				originalFileTitle = "제공_근로계약서_전자문서";
				originalFileExt = "pdf";
				originalFilePath = getSystemPath()+"data/contract/"+originalFileTitle+"."+originalFileExt;
				targetFileName = StringUtil.getUUID()+"."+originalFileExt;
				targetFilePath = MultipartFileUtil.getSystemTempPath()+targetFileName;

				logger.info(originalFilePath);
				logger.info(targetFilePath);
				// 사용할 PDF복사
				FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
				if (FileUtil.fileCopy(originalFilePath, targetFilePath)) {
					contractPdfVO.setFileType(originalFileExt);
					contractPdfVO.setFileName(targetFileName);
					contractPdfVO.setOrgFileName(originalFileTitle+"."+originalFileExt);
					contractPdfVO.setFileSize(Long.toString(FileUtil.getFileSize(targetFilePath)));
					contractPdfVO.setFilePath(MultipartFileUtil.getSystemTempPath());
					contractPdfVO.setFileTitle(originalFileTitle);
					contractPdfVO.setClassId(ContractService.UPLOAD_CLASS_ID);
					contractPdfVO.setFileVersion("0");
					contractPdfVO.setParFileId("");
					contractPdfVO.setEtcDesc("");
					contractPdfVO.setCheckInOut("N");
					contractPdfVO.setCheckUserId("");
					contractPdfVO.setCheckCount("0");
					contractPdfVO.setUseYn("Y");
					contractPdfVO.setDelYn("N");

					nResult = contentService.transContent(contractPdfVO);

					// 양식등록
					if (ContentService.COMPLETED==nResult) {
						result++;

						contractNewVO.setServiceId(ContractService.SERVICE_ID);
						contractNewVO.setBizId(bizId);
						contractNewVO.setFileId(contractHwpVO.getFileId());
						//기본 제공양식(완료처리)
						contractNewVO.setTransType("C");
						contractNewVO.setContractId(contractPdfVO.getFileId());
						contractNewVO.setDataFileId(contractXlsVO.getFileId());
						contractNewVO.setFileTitle("제공_근로계약서");
						contractNewVO.setSignBizType("1");
						contractNewVO.setSignUserType("1");
						contractNewVO.setSignCustomerType("0");
						contractNewVO.setLaborType("0");

						contractDAO.updContractNew(contractNewVO);
					}
				}
			}
		}
		return result;
	}



	@Override
	public ContentVO downContractPdf(ContractPersonVO vo) {

		logger.info("downContractPdf : "+vo.getDigitNonce());

		ContractPersonVO contractPersonVO = contractDAO.getContractPersonNonce(vo);
		if (contractPersonVO==null) {
			logger.error("[downContractPdf] 최종 전자문서 정보(nonce)가 존재하지 않습니다.");
			return null;
		}

		ContentVO contentVO = new ContentVO();
		contentVO.setFileId(contractPersonVO.getContractId());
		ContentVO contentResultVO = contentDAO.getContent(contentVO);
		if (contentResultVO==null) {
			logger.error("[downContractPdf] 콘텐츠 정보가 존재하지 않습니다.");
			return null;
		}


		// 저장위치
		CabinetVO cabinetVO = new CabinetVO();
		cabinetVO.setClassId(contentResultVO.getClassId());
		cabinetVO.setCabinetId(contentResultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO==null) {
			logger.error("[downContractPdf] 캐비넷 정보가 존재하지 않습니다.");
			return null;
		}

		// 난수 초기화
		contractPersonVO.setDigitNonce("");
		contentResultVO.setFilePath(cabinetResultVO.getCabinetPath()+contentResultVO.getFilePath());
		contentResultVO.setUpdDate(contractPersonVO.getUserDate());

		return contentResultVO;
	}



	@Override
	public List<ContractPersonNewFormVO> getContractPersonNewFormList(ContractPersonNewFormVO vo) { return contractDAO.getContractPersonNewFormList(vo); }



	@Override
	public int createContractForm(ContractFormVO vo) throws Exception {
		int result			  = 0;
		ContractFormVO dataVO = new ContractFormVO();
		String[] formList 	  = StringUtil.split(vo.getFormDataValue(), "|");

		//사용자|중간관리자 개별등록 미 완료 계약 생성(등록수정)
		ContractPersonVO contractVO = new ContractPersonVO();
		contractVO.setServiceId(ContractService.SERVICE_ID);
		contractVO.setBizId(vo.getBizId());
		contractVO.setUserId(vo.getUserId());
		contractVO.setContractDate(vo.getContractDate());
		contractVO.setContractType("1");
		contractVO.setContractName("");
		contractVO.setContractFileId(vo.getContractFileId());
		contractVO.setStatusCode("1");
		contractVO.setTransType("N");
		//계약서 생성
		String insContId = contractDAO.insContractPerson(contractVO);

        for (String s : formList) {
            String[] formValue = StringUtil.split(s, "^");
            if (formValue.length > 1) {
                dataVO.setContId(insContId);
                dataVO.setServiceId(ContractService.SERVICE_ID);
                dataVO.setBizId(vo.getBizId());
                dataVO.setUserId(vo.getUserId());
                dataVO.setContractDate(vo.getContractDate());
                dataVO.setFormDataId(formValue[0]);
                dataVO.setFormDataName(URLDecoder.decode(formValue[1], StandardCharsets.UTF_8.name()));
                dataVO.setFormDataValue(URLDecoder.decode(formValue[2], StandardCharsets.UTF_8.name()));

                //폼 데이터 추가
                contractDAO.insContractForm(dataVO);
                result++;
            }
        }
		return result;
	}



	@Override
	public List<ContractPersonVO> getContractPersonUserGrpList(ContractPersonVO vo) { return contractDAO.getContractPersonUserGrpList(vo); }



	@Override
	public int getContractPersonUserGrpListCount(ContractPersonVO vo) { return contractDAO.getContractPersonUserGrpListCount(vo); }



	@Override
	public ContractPersonNewVO getContractPersonSign(ContractPersonVO vo) { return contractDAO.getContractPersonSign(vo); }



	@Override
	public int getDownloadCount(ContractPersonLogVO vo) { return contractDAO.getDownloadCount(vo); }



	@Override
	public int delContractAll(List<String> list) throws Exception {
		List<ContractPersonVO> delContList = new ArrayList<>();
		List<ContentVO> delList 		   = new ArrayList<>();

        for (String s : list) {
            ContractPersonVO vo = new ContractPersonVO();
            vo.setContId(s.replaceAll("&quot;", "").replace("[", "").replace("]", ""));
            ContractPersonVO resultVO = contractDAO.getContractPerson(vo);
            ContractPersonVO delContractVO = contractDAO.getContractPerson(vo);
            delContList.add(delContractVO);

            ContentVO delContentVO = new ContentVO();
            delContentVO.setFileId(resultVO.getContractId());
            delList.add(delContentVO);
            logger.info("[delContractAll] 삭제될 파일 CONTID =>[" + delContractVO.getContId() + "], CONTRACT_ID(FILE_ID) =>[" + delContentVO.getFileId() + "]");
        }

        for (ContractPersonVO contractPersonVO : delContList) {
            int contractResult = contractDAO.delContractPerson(contractPersonVO);
            if (contractResult < 0) return -1;
        }

		// 콘텐츠 파일삭제
		int result = contentService.deleteContent(delList);
		logger.info("[delContractAll] deleteContent ==>"+result);

		return result;
	}



	@Override
	public List<ContractPersonNewFormCommentVO> getContractPersonNewFormCommentList(ContractPersonNewFormCommentVO vo) { return contractDAO.getContractPersonNewFormCommentList(vo); }



	@Override
	public void insContractPersonNewFormComment(ContractPersonNewFormCommentVO vo) {

		contractDAO.insContractPersonNewFormComment(vo);

		if (vo.getUserType().equals("7") || vo.getUserType().equals("8") || vo.getUserType().equals("9")){
			//협력사 중간관리자, 협력사관리자, 슈퍼관리자 등록하였을 경우 기업담당자에게 이메일 알림.
			sendContractNewFormCommentUpdateEmail(vo.getBizId());
		}
	}



	@Override
	public void delContractPersonNewFormComment(ContractPersonNewFormCommentVO vo) { contractDAO.delContractPersonNewFormComment(vo); }


	// 동일 기업의 기업 담당자(6)에게 전자문서 변환작업 완료 이메일 전송
	@Override
	public void sendContractNewUpdateEmailP(String bizId) {

		EmpVO vo = new EmpVO();
		vo.setSearchCompany(bizId);
		vo.setEmpType("6");
		vo.setStartPage(-1);
		vo.setEndPage(-1);
		List<EmpVO> result = empDAO.getEmpList(vo);

		logger.info("----- sendContractNewUpdateEmailP -----");
		logger.info("----- 변환작업 완료 테스트 메일 전송 -----");

        for (EmpVO empVO : result) {
            String content = "";

            content += "<!DOCTYPE html> ";
            content += "<html> ";
            content += "<head> ";
            content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
            content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
            content += "<title>전자문서</title> ";
            content += "</head> ";
            content += "<body> ";
            content += "	<div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
            content += "		<div style=\" padding: 37px 0 0 0;\"> ";
            content += "			<div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
            content += "				<span style=\"font-weight: bold; color:#00a9e9;\">IEUM</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;SIGN</span> ";
            content += "			</div> ";
            content += "			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
            content += "				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">전자문서 변환 완료</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br>발송이메일입니다.</span>";
            content += "			</div> ";
            content += "			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
            content += "				<span >" + empVO.getEmpName() + "</span><span>님!<br> ";
            content += "				전자문서 변환 작업이 완료되었습니다.<br> ";
            content += "				발송작업 바랍니다.";
            content += "				</span> ";
            content += "			</div> ";
            content += "		<div style=\"margin: 74px 0 32px; text-align: center;\"> ";
            content += "			<a href=\"" + System.getProperty("system.feb.url") + "\" style=\"display: inline-block; padding: 12px 90px; border-radius: 8px; font-family: 'tahoma'; font-size: 18px; font-weight: bold; text-decoration: none; color: #fff; background-color: #00a9e9;\">로그인하기</a>";
            content += "		</div> ";
            content += "		</div> ";
            content += "		<div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
            content += "			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
            content += "				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
            content += "				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">service@newzenpnp.com</a></span > ";
            content += "				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
            content += "			</div> ";
            content += "		</div> ";
            content += "	</div> ";
            content += "</body> ";
            content += "</html> ";

            MultiPartEmail email = new MultiPartEmail();
            MailVO mailVO = new MailVO();
            mailVO.setFrom("no_reply@newzenpnp.com");
            mailVO.setTo(empVO.getEMail());
            mailVO.setCc("");
            mailVO.setBcc("");
            mailVO.setSubject("[뉴젠피앤피] 전자문서 변환작업이 완료되었습니다. 테스트를 진행해주세요.");
            mailVO.setText(content);

            logger.info("Ieum Sign 전자문서 이메일을 발송. email : " + empVO.getEMail());

            email.send(mailVO);
        }
	}


	// 동일 기업의 기업 담당자(6)에게 전자문서 변환작업 완료 이메일 전송
	@Override
	public void sendContractNewUpdateEmailC(String bizId) {

		EmpVO vo = new EmpVO();
		vo.setSearchCompany(bizId);
		vo.setEmpType("6");
		vo.setStartPage(-1);
		vo.setEndPage(-1);
		List<EmpVO> result = empDAO.getEmpList(vo);

		logger.info("----- sendContractNewUpdateEmailC -----");
		logger.info("----- 변환작업 최종 완료 메일 전송 -----");
        for (EmpVO empVO : result) {
            String content = "";
            content += "<!DOCTYPE html> ";
            content += "<html> ";
            content += "<head> ";
            content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
            content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
            content += "<title>전자문서</title> ";
            content += "</head> ";
            content += "<body> ";
            content += "	<div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
            content += "		<div style=\" padding: 37px 0 0 0;\"> ";
            content += "			<div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
            content += "				<span style=\"font-weight: bold; color:#00a9e9;\">IEUM</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;SIGN</span> ";
            content += "			</div> ";
            content += "			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
            content += "				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">전자문서 변환 완료</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br>발송이메일입니다.</span>";
            content += "			</div> ";
            content += "			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
            content += "				<span >" + empVO.getEmpName() + "</span><span>님!<br> ";
            content += "				전자문서 변환 작업이 완료되었습니다.<br> ";
            content += "				발송작업 바랍니다.";
            content += "				</span> ";
            content += "			</div> ";
            content += "		<div style=\"margin: 74px 0 32px; text-align: center;\"> ";
            content += "			<a href=\"" + System.getProperty("system.feb.url") + "\" style=\"display: inline-block; padding: 12px 90px; border-radius: 8px; font-family: 'tahoma'; font-size: 18px; font-weight: bold; text-decoration: none; color: #fff; background-color: #00a9e9;\">로그인하기</a>";
            content += "		</div> ";
            content += "		</div> ";
            content += "		<div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
            content += "			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
            content += "				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
            content += "				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">service@newzenpnp.com</a></span > ";
            content += "				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
            content += "			</div> ";
            content += "		</div> ";
            content += "	</div> ";
            content += "</body> ";
            content += "</html> ";

            MultiPartEmail email = new MultiPartEmail();
            MailVO mailVO = new MailVO();
            mailVO.setFrom("no_reply@newzenpnp.com");
            mailVO.setTo(empVO.getEMail());
            mailVO.setCc("");
            mailVO.setBcc("");
            mailVO.setSubject("[뉴젠피앤피] 전자문서 변환작업이 최종 완료되었습니다.");
            mailVO.setText(content);

            logger.info("Ieum Sign 전자문서 이메일을 발송. email : " + empVO.getEMail());

            email.send(mailVO);
        }
	}


	//노무첨삭 등록 완료시 기업담당자에게 이메일 발송
	@Override
	public void sendContractNewFormCommentUpdateEmail(String bizId) {

		EmpVO vo = new EmpVO();
		vo.setBizId(bizId);
		vo.setEmpType("6");
		vo.setStartPage(-1);
		vo.setEndPage(-1);
		List<EmpVO> result = empDAO.getEmpList(vo);

        for (EmpVO empVO : result) {
            String content = "";
            content += "<!DOCTYPE html> ";
            content += "<html> ";
            content += "<head> ";
            content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
            content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
            content += "<title>전자문서</title> ";
            content += "</head> ";
            content += "<body> ";
            content += "	<div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
            content += "		<div style=\" padding: 37px 0 0 0;\"> ";
            content += "			<div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
            content += "				<span style=\"font-weight: bold; color:#00a9e9;\">IEUM</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;SIGN</span> ";
            content += "			</div> ";
            content += "			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
            content += "				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">새로운 첨삭 내용</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br>발송이메일입니다.</span>";
            content += "			</div> ";
            content += "			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
            content += "				<span >" + empVO.getEmpName() + "</span><span>님!<br> ";
            content += "				뉴젠 전자문서 새로운 첨삭내용을 확인바랍니다.<br> ";
            content += "				</span> ";
            content += "			</div> ";
            content += "		<div style=\"margin: 74px 0 32px; text-align: center;\"> ";
            content += "			<a href=\"" + System.getProperty("system.feb.url") + "\" style=\"display: inline-block; padding: 12px 90px; border-radius: 8px; font-family: 'tahoma'; font-size: 18px; font-weight: bold; text-decoration: none; color: #fff; background-color: #00a9e9;\">로그인하기</a>";
            content += "		</div> ";
            content += "		</div> ";
            content += "		<div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
            content += "			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
            content += "				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
            content += "				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">service@newzenpnp.com</a></span > ";
            content += "				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
            content += "			</div> ";
            content += "		</div> ";
            content += "	</div> ";
            content += "</body> ";
            content += "</html> ";

            MultiPartEmail email = new MultiPartEmail();
            MailVO mailVO = new MailVO();
            mailVO.setFrom("no_reply@newzenpnp.com");
            mailVO.setTo(empVO.getEMail());
            mailVO.setCc("");
            mailVO.setBcc("");
            mailVO.setSubject("[뉴젠피앤피] 전자문서 새로운 첨삭내용을 확인바랍니다.");
            mailVO.setText(content);

            logger.info("Ieum Sign 전자문서 이메일을 발송. email : " + empVO.getEMail());

            email.send(mailVO);
        }
	}


	//노무첨삭 - 최종파일 등록
	@Override
	public ContractPersonNewVO contractNewFinalUpload(ContractPersonNewVO vo, FileVO fileVO) {
		ContractPersonNewVO result = new ContractPersonNewVO();
		logger.info("[contractNewFinalUpload]  기업id -> "+vo.getBizId());

		try {
			String bizId    = vo.getBizId();
			String filePath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();

			File file = new File(filePath);
			if (!file.exists()) {
				logger.error("[contractNewFinalUpload] 복사본 파일이 존재하지 않습니다.");
				return null;
			}

			ContentVO contentVO = new ContentVO();
			contentVO.setFileType(FileUtil.getFileExt(filePath));
			contentVO.setFileName(fileVO.getFileStreNm());
			contentVO.setOrgFileName(fileVO.getFileStreOriNm());
			contentVO.setFileSize(Long.toString(FileUtil.getFileSize(filePath)));
			contentVO.setFilePath(fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR);
			contentVO.setFileTitle(StringUtil.ReplaceAll(fileVO.getFileStreOriNm(), "."+fileVO.getFileExt(), ""));
			contentVO.setClassId(ContractService.UPLOAD_CLASS_ID);
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
				result.setServiceId(ContractService.SERVICE_ID);
				result.setBizId(bizId);
				result.setFileId(vo.getFileId());
				result.setLastFileId(contentVO.getFileId());
				result.setTransType("T");
				result.setSignBizType(vo.getSignBizType());
				result.setSignUserType(vo.getSignUserType());
				result.setSignUserType(vo.getSignCustomerType());
				//최종파일 DB처리
				contractDAO.updContractNewFinal(result);

				result.setMessage("파일이 등록되었습니다.");
			}
		} catch (Exception ex) { ex.printStackTrace(); }
		return result;
	}



	@Override
	public List<ContractPersonVO> getSelectAuditTrailCertificate(List<ContractPersonVO> list, SessionVO loginVO) {
		List<ContractPersonVO> resultList = new ArrayList<>(list);

        for (ContractPersonVO contractPersonVO : list) {

            ContractPersonVO contractVO = contractDAO.getContractPerson(contractPersonVO);
            ContentVO contentVO 		= new ContentVO();
            contentVO.setFileId(contractVO.getContractId());
            // 콘텐츠에서 정보 추출
            ContentVO resultContentVO = contentDAO.getContent(contentVO);

            if (resultContentVO == null) {
                logger.error("[downloadContractSelectZip] 콘텐츠 정보가 존재하지 않습니다.");
                return null;
            }

            // 저장위치
            CabinetVO cabinetVO = new CabinetVO();
            cabinetVO.setClassId(resultContentVO.getClassId());
            cabinetVO.setCabinetId(resultContentVO.getCabinetId());

            CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);

            if (cabinetResultVO == null) {
                logger.error("[downloadContractSelectZip] 캐비넷 정보가 존재하지 않습니다.");
                return null;
            }

            // 접속한 사용자 정보
            EmpVO empVO = new EmpVO();
            empVO.setBizId(contractVO.getBizId());
            empVO.setUserId(contractVO.getUserId());

			EmpVO empResultVO 		= empDAO.getEmp(empVO);
            Boolean customerUseFlag = false;
            String getEmpNo 		= "";

            // 인사목록이 없을때 거래처목록까지 확인
            if (empResultVO == null) {
                empVO.setCustomerId(contractVO.getUserId());
                empResultVO 	= empDAO.getCustomer(empVO);
                customerUseFlag = true;
            }

            if (empResultVO == null) {
                logger.error("[downloadContractSelectZip] 직원정보가 존재하지 않습니다.");
                return null;
            } else {
                if (customerUseFlag) getEmpNo = empResultVO.getBusinessNo();
                else 				 getEmpNo = empResultVO.getEmpNo();
            }

            ContractPersonNewVO newVO = contractDAO.getContractPersonSign(contractVO);

            boolean makeCert = false;
            if 		(newVO == null) 															makeCert = true;
            else if (newVO.getSignBizType().equals("1") && newVO.getSignUserType().equals("1")) makeCert = true;


            if (makeCert) {

                String originalPdfPath = getSystemPath() + "/data/contract/감사추적인증서.pdf";
                String targetPdfName   = contractVO.getBizName() + "_" + contractVO.getEmpName() + "_" + getEmpNo + "_" + contractVO.getContId() + "_감사추적인증서.pdf";
                String targetPdfPath   = MultipartFileUtil.getSystemTempPath() + targetPdfName;

                // 사용할 PDF복사
                FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
                if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
                    List<FieldVO> fieldListVO = new ArrayList<>();
                    // 계약서 정보
                    fieldListVO.add(FieldUtil.setFieldData("cont_id", contractVO.getContId()));
                    fieldListVO.add(FieldUtil.setFieldData("service_id", contractVO.getServiceId()));
                    fieldListVO.add(FieldUtil.setFieldData("biz_id", contractVO.getBizId()));
                    fieldListVO.add(FieldUtil.setFieldData("user_id", contractVO.getUserId()));
                    fieldListVO.add(FieldUtil.setFieldData("contract_date", contractVO.getContractDate()));

                    //감사추적인증서 제작일시 (선택 다운로드)
                    Date date = new Date();
                    fieldListVO.add(FieldUtil.setFieldData("TXT_00001", DateUtil.dateToString(date, "yyyy/MM/dd/ HH:mm:ss")));
                    //문서종류
                    fieldListVO.add(FieldUtil.setFieldData("TXT_00002", contractVO.getContractFileName()));
                    //문서고유번호
                    fieldListVO.add(FieldUtil.setFieldData("TXT_00003", contractVO.getContId()));
                    //기업명
                    fieldListVO.add(FieldUtil.setFieldData("TXT_00004", contractVO.getBizName()));
                    //작성자
                    fieldListVO.add(FieldUtil.setFieldData("TXT_00005", loginVO.getUserName()));
                    //수신자
                    fieldListVO.add(FieldUtil.setFieldData("TXT_00006", contractVO.getEmpName()));

                    //감사추적인증서를 위한 히스토리 조회
                    ContractPersonLogVO logVO = new ContractPersonLogVO();
                    logVO.setContId(contractVO.getContId());
                    List<ContractPersonLogVO> resLogVO = contractDAO.getContractPersonLogList(logVO);

                    int idx = 7;

                    for (ContractPersonLogVO formData : resLogVO) {
                        Date logDate 	  = DateUtil.StringToDate(formData.getInsDate(), "yyyyMMddhhmmss");
                        String strLogDate = DateUtil.dateToString(logDate, "yyyy/MM/dd/ HH:mm:ss");

                        fieldListVO.add(FieldUtil.setFieldData("TXT_" + String.format("%05d", idx++), strLogDate));
                        fieldListVO.add(FieldUtil.setFieldData("TXT_" + String.format("%05d", idx++), formData.getInsEmpName()));
                        fieldListVO.add(FieldUtil.setFieldData("TXT_" + String.format("%05d", idx++), formData.getLogName()));
                        fieldListVO.add(FieldUtil.setFieldData("TXT_" + String.format("%05d", idx++), formData.getLogMessage().replaceAll("<br>", "\n")));
                    }

                    //파일생성
                    boolean bWritePDF = ControlUtil.writePDF(fieldListVO, originalPdfPath, targetPdfPath, false, true, System.getProperty("pdf.font.default"));

                    if (bWritePDF) {
                        String zipFilePath = MultipartFileUtil.getSystemTempPath() + contractVO.getBizId() + MultipartFileUtil.SEPERATOR;
                        String zipFileName = "[전자문서]_" + "[" + DateUtil.getTimeStamp(7) + "]" + ".zip";

                        ContractPersonVO paramVO = new ContractPersonVO();
                        paramVO.setBizId(contractVO.getBizId());
                        paramVO.setUserId(contractVO.getUserId());
                        paramVO.setContractDate(contractVO.getContractDate());
                        paramVO.setContId(contractVO.getContId());
                        paramVO.setContractDateFrom(contractVO.getContractDateFrom());
                        paramVO.setContractDateTo(contractVO.getContractDateTo());
                        paramVO.setPdfFile(zipFilePath + zipFileName);
                        paramVO.setFileType("A");
                        resultList.add(paramVO);
                    }
                }
            }
        }
		return resultList;
	}


	//삭제 될 전자문서 조회
	@Override
	public List<ContractPersonVO> getRemoveContractList(ContractPersonVO vo) { return contractDAO.getRemoveContractList(vo); }

	/**
	 * Description  : 나눔HR 계약정보 데이터
	 * @Method Name : sendNanumhrEmpExcel
	 * @return EmpVO
	 * @throws Exception
	 */
	@Override
	public EmpVO sendNanumhrContractExcel(String bizId, String xlsPath, String metaId, String fileId) {
		EmpVO result = new EmpVO();

		logger.info("[sendNanumhrContractExcel]  기업id -> " + bizId);
		logger.info("[sendNanumhrContractExcel]  파일경로 -> " + xlsPath);

		try {
			File file = new File(xlsPath);
			if (!file.exists()) {
				result.setMessage("XLS파일이 존재하지 않습니다.");
				logger.info("[sendNanumhrContractExcel] XLS파일이 존재하지 않습니다.");
				return result;
			}

			Workbook workbook = Workbook.getWorkbook(file);
			Sheet[] sheets    = workbook.getSheets();

			if (sheets.length > 1) {
				//worksheet가 여러개일 경우 파일을 분리해서 올려야 함.
				result.setMessage("엑셀에 시트가 여러개 존재합니다. 파일을 분리해주세요.");
				logger.info("엑셀에 시트가 여러개 존재합니다. 파일을 분리해주세요.");
				return result;
			}

			Sheet sheet = sheets[0];
			int colNum  = sheet.getColumns();
			int rowNum  = sheet.getRows();
			//사번 컬럼
			int empNoCulumn = -1;

			logger.info("sheet=>" + sheet.getName() + "_" + colNum + "_" + rowNum);

			// 사번 컬럼 탐색
            label:
            for (int r = 0; r < colNum; r++) {
                String empNoHeader = ExcelUtil.getCellValue(sheet.getCell(r, 0));
                logger.info("header => "+empNoHeader);
				switch (StringUtil.StringReplace(empNoHeader.replaceAll(" ", ""))) {
					case "사번":
						empNoCulumn = r;
						break label;
					case "사원번호":
						empNoCulumn = r;
						break label;
					case "회사번호":
						empNoCulumn = r;
						break label;
					case "EMPNO":
						empNoCulumn = r;
						break label;
				}
			}

            if (empNoCulumn == -1) {
				result.setMessage("사번 컬럼이 존재하지 않습니다.");
				logger.info("[sendNanumhrContractExcel] 사번 컬럼이 존재하지 않습니다.");
				return result;
			}

			int successCount = 0;

			for (int s=1; s<rowNum; s++) {
				Map<MetaDataVO, MetaDataVO> mapList = new HashMap<>();
				String empNoData 					= ExcelUtil.getCellValue(sheet.getCell(empNoCulumn, s));
				if (StringUtil.isNull(empNoData)) {
					result.setMessage(s+"번 row의 사번 데이터가 존재하지 않습니다.");
					logger.info("[sendNanumhrContractExcel] 사번 데이터이 존재하지 않습니다.");
					continue;
				}

				//사번 - dataId
				for (int r=0; r<colNum; r++) {
					MetaDataVO headerVO = new MetaDataVO();
					headerVO.setEmpNo(empNoData);
					headerVO.setDataId(ExcelUtil.getCellValue(sheet.getCell(r, 0)));
					if (StringUtil.isNull(ExcelUtil.getCellValue(sheet.getCell(r, 0)))) {
						result.setMessage((r+1)+"번의 헤더 데이터가 존재하지 않습니다.");
						logger.info("[sendNanumhrContractExcel]("+r+", "+0+")번의 헤더 데이터가 존재하지 않습니다.");
						continue;
					}
					MetaDataVO bodyVO = new MetaDataVO();
					bodyVO.setBizId(bizId);
					bodyVO.setMetaId(metaId);
					bodyVO.setEmpNo(empNoData);
					bodyVO.setDataId(ExcelUtil.getCellValue(sheet.getCell(r, 0)));
					bodyVO.setDataValue(ExcelUtil.getCellValue(sheet.getCell(r, s)));
					mapList.put(headerVO, bodyVO);
				}

				//row별로 insert 진행
				List<MetaDataVO> metaDataList = new ArrayList<>(mapList.values());
				if (!metaDataList.isEmpty()) {
					metaDAO.insMetaDataList(metaDataList);
					successCount ++;
				}
			}
			logger.info("#[sendNanumhrContractExcel] excel -> tbl_meta_data ["+successCount+"]건 성공");

			//tbl_meta_data를 contractPerson형식으로 조회
			ContractPersonVO dataVO = new ContractPersonVO();
			dataVO.setFileId(fileId);
			dataVO.setMetaId(metaId);
			dataVO.setServiceId(ContractService.SERVICE_ID);
			List<ContractPersonVO> resCont = contractDAO.getMetaDataToContractList(dataVO);

			//조회 된 결과로 contractPerson, contractForm에 insert
			successCount = 0;
            for (ContractPersonVO personVO : resCont) {
                //contractPerson insert
                personVO.setContractFileId(fileId);
                String insContId = contractDAO.insContractPerson(personVO);

                //contractForm insert
                ContractFormVO formVO = new ContractFormVO();
                formVO.setBizId(bizId);
                formVO.setContractFileId(fileId);
                formVO.setMetaId(metaId);
                formVO.setContId(insContId);
                contractDAO.insMetaDataToContractFormList(formVO);
                successCount++;
            }

			logger.info("#[sendNanumhrContractExcel] tbl_meta_data -> contractPerson/Form ["+successCount+"]건 성공");
			result.setEmpNonce("엑셀에 업로드 된 "+(rowNum-1)+"건의 데이터 중"+successCount);
			workbook.close();

		} catch (Exception ex) { ex.printStackTrace(); }
		return result;
	}



	@Override
	public List<ContractPersonVO> getContIdContractPerson(String multiData, SessionVO loginVO) {

		List<ContractPersonVO> resContractPersonList = new ArrayList<>();

		if (StringUtil.isNotNull(multiData)) {
			String[] params = StringUtil.split(multiData, "|");
			for (int i=0; i<params.length-1; i++) {
				ContractPersonVO paramVO = new ContractPersonVO();
				paramVO.setBizId(loginVO.getBizId());
				paramVO.setContId(params[i]);
				ContractPersonVO resVO = contractDAO.getContractPerson(paramVO);
				resContractPersonList.add(resVO);
			}
		}
		return resContractPersonList;
	}



	@Override
	public void insContractLog() { contractDAO.insContractLog(); }


	// 개별계약서 삭제
	@Override
	public int delContractPersonSelectList(List<ContractPersonVO> list, SessionVO loginVO) {
		int result = 0;

		for (ContractPersonVO vo : list) {
			logger.info("delContractPerson ContId : "+vo.getContId());

			try {

				ContractPersonVO contractResultVO = contractDAO.getContractPerson(vo);
				if (contractResultVO == null) {
					logger.error("[delContractPerson] 근로계약서정보가 존재하지 않습니다.");
					vo.setResultMessage("근로계약서정보가 존재하지 않습니다.");
					return -4;
				}

				// 접속한 사용자정보
				EmpVO empVO 		   = new EmpVO();
				empVO.setBizId(contractResultVO.getBizId());
				empVO.setUserId(contractResultVO.getUserId());
				empVO.setCustomerId(contractResultVO.getUserId());
				EmpVO empResultVO 	   = empDAO.getEmp(empVO);
				EmpVO customerResultVO = empDAO.getCustomer(empVO);

				if (empResultVO == null && customerResultVO == null) {
					logger.error("[delContractPerson] 직원정보가 존재하지 않습니다.");
					vo.setResultMessage("직원정보가 존재하지 않습니다.");
					return -3;
				}

				// 전자서명 단계 확인 (계약완료 상태 체크)
				if (contractResultVO.getStatusCode().equals("Y") && !StringUtils.equals("180109161235042", contractResultVO.getBizId())) {
					logger.error("[delContractPerson] 미완료된 문서에 대해서만 삭제가 가능합니다.");
					vo.setResultMessage("미완료된 문서에 대해서만 삭제가 가능합니다.");
					return -5;
				}

				ContractPersonVO contractPersonVO = new ContractPersonVO();
				contractPersonVO.setContId(contractResultVO.getContId());

				contractPersonVO.setStatusCode("D");

				// 계약정보 업데이트
				result = contractDAO.updContractPerson(contractPersonVO);
				// 삭제될 계약서 데이터 백업테이블 이동
				contractDAO.moveContractPersonDel(contractPersonVO);
				// 계약정보 삭제
				contractDAO.delContractPerson(contractPersonVO);

				// 계약조건정보 삭제
				ContractFormVO contractFormVO = new ContractFormVO();
				contractFormVO.setContId(contractResultVO.getContId());
				contractDAO.delContractForm(contractFormVO);

				// 계약서 파일 삭제
				List<ContentVO> contentList = new ArrayList<>();

				ContractPersonLogVO contractPersonLogVO = new ContractPersonLogVO();
				contractPersonLogVO.setContId(contractResultVO.getContId());
				List<ContractPersonLogVO> loglist = contractDAO.getContractPersonLogList(contractPersonLogVO);

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
				// 삭제할 계약서 존재시 삭제처리 실제 파일만 삭제 (DB는 보존)
				if (!contentList.isEmpty()) contentService.deleteContent(contentList);

				// 로그테이블 생성
				ContractPersonLogVO logVO = new ContractPersonLogVO();
				logVO.setContId(contractResultVO.getContId());
				logVO.setServiceId(ContractService.SERVICE_ID);
				logVO.setBizId(contractResultVO.getBizId());
				logVO.setUserId(contractResultVO.getUserId());
				logVO.setContractDate(contractResultVO.getContractDate());
				logVO.setContractId(contractResultVO.getContractId());
				logVO.setLogType("D");
				logVO.setLogMessage(contractResultVO.getEmpName()+"님의 전자문서를 삭제하였습니다.");
				logVO.setInsEmpNo(loginVO.getUserId());
				contractDAO.insContractPersonLog(logVO);

			} catch (Exception e) { e.printStackTrace(); }
		}
		return result;
	}



	@Override
	public void saveContractSetting(ContractSettingVO vo) {

        switch (vo.getDbMode()) {
            case "C":
                contractDAO.insContrectSetting(vo);
                break;
            case "U":
                contractDAO.delContrectSetting(vo);
                contractDAO.insContrectSetting(vo);
                break;
            case "D":
                contractDAO.delContrectSetting(vo);
                break;
        }
	}



	@Override
	public int downContractDataExcel(String filePath, String fileName, ContractPersonVO vo) {

		String excelFile = filePath + fileName;
		SXSSFWorkbook wb = new SXSSFWorkbook(100);
		SXSSFSheet sheet = wb.createSheet(vo.getContractFileName() + "_계약조건");

		if (sheet == null) return -1;

		sheet.setColumnWidth(0, 3 * _oneByte);
		sheet.setColumnWidth(1, 10 * _oneByte);
		sheet.setColumnWidth(2, 3 * _oneByte);

		//테두리 스타일
		CellStyle headStyle = wb.createCellStyle();
		headStyle.setBorderTop(BorderStyle.THIN);
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);

		// 엑셀 헤더 생성
		Row rowHeader1 = sheet.createRow(1);
		Cell cell 	   = rowHeader1.createCell(1);

		String contractDateFrom = vo.getContractDateFrom().substring(0,4) + "-" + vo.getContractDateFrom().substring(4,6) + "-"
				+ vo.getContractDateFrom().substring(6);
		String contractDateTo = vo.getContractDateTo().substring(0,4) + "-" + vo.getContractDateTo().substring(4,6) + "-"
				+ vo.getContractDateTo().substring(6);


		cell.setCellValue("조회기간 : "+ contractDateFrom + " ~ " + contractDateTo);
		cell.setCellStyle(headStyle);

		Row rowHeader3 = sheet.createRow(3);

		ContractPersonNewFormVO contractPersonNewFormVO = new ContractPersonNewFormVO();
		contractPersonNewFormVO.setBizId(vo.getBizId());
		contractPersonNewFormVO.setFileId(vo.getFileId());

		List<ContractPersonNewFormVO> newFormResult= contractDAO.getContractPersonNewFormList(contractPersonNewFormVO);

		ContractPersonVO contractPersonVO = new ContractPersonVO();
		contractPersonVO.setBizId(vo.getBizId());
		contractPersonVO.setContractDateTo(vo.getContractDateTo());
		contractPersonVO.setContractDateFrom(vo.getContractDateFrom());
		contractPersonVO.setContractFileId(vo.getContractFileId());
		contractPersonVO.setStartPage(0);
		contractPersonVO.setEndPage(Integer.MAX_VALUE);

		//계약서 리스트
		List<ContractPersonVO> contractPersonVOList = contractDAO.getContractPersonList(contractPersonVO);

		//해더 상태
		cell = rowHeader3.createCell(1);
		cell.setCellValue("수신자명");
		cell.setCellStyle(headStyle);

		String signTypeText = "";
		if 		(vo.getSignUserType().equals("1"))	   signTypeText = "사번";
		else if (vo.getSignCustomerType().equals("1")) signTypeText = "사업자등록번호";
		else										   signTypeText = "사번 / 사업자등록번호";

		cell = rowHeader3.createCell(2);
		cell.setCellValue(signTypeText);
		cell.setCellStyle(headStyle);

		cell = rowHeader3.createCell(3);
		cell.setCellValue("생성일자");
		cell.setCellStyle(headStyle);

		cell = rowHeader3.createCell(4);
		cell.setCellValue("전송일자");
		cell.setCellStyle(headStyle);

		cell = rowHeader3.createCell(5);
		cell.setCellValue("진행상태");
		cell.setCellStyle(headStyle);

		for (int i=0; i<newFormResult.size(); i++) {
			ContractPersonNewFormVO tempVO = newFormResult.get(i);
			cell = rowHeader3.createCell(6 + i);
			cell.setCellValue(tempVO.getFormDataName());
			cell.setCellStyle(headStyle);
		}
		// 엑셀데이터 생성
		for (int i=0; i<contractPersonVOList.size(); i++) {
			Row rowData   = sheet.createRow(4 + i );
			Cell cellData = rowData.createCell(1);
			cellData.setCellValue(contractPersonVOList.get(i).getEmpName());

			Cell cellData2 = rowData.createCell(2);
			cellData2.setCellValue(contractPersonVOList.get(i).getEmpNo());

			Cell cellData3 = rowData.createCell(3);
			String contractDate = contractPersonVOList.get(i).getContractDate();
			if(contractDate != null && !contractDate.isEmpty() && contractDate.length() >= 8) {
				contractDate = contractDate.substring(0, 4) + "-" + contractDate.substring(4, 6) + "-"
						+ contractDate.substring(6);
			}
			cellData3.setCellValue(contractDate);

			Cell cellData4 = rowData.createCell(4);
			String sendDate = contractPersonVOList.get(i).getSendDate();
			if (sendDate != null && !sendDate.isEmpty() && sendDate.length() >= 8) {
				sendDate = sendDate.substring(0, 4) + "-" + sendDate.substring(4, 6) + "-"
						+ sendDate.substring(6, 8);
			}
			cellData4.setCellValue(sendDate);

			Cell cellData5 = rowData.createCell(5);
			cellData5.setCellValue(contractPersonVOList.get(i).getStatusName());

			ContractFormVO searchContractFormVO = new ContractFormVO();
			searchContractFormVO.setContId(contractPersonVOList.get(i).getContId());
			//폼데이터 가지고 오기
			List<ContractFormValueVO> contractFormValueVOList = contractDAO.getContractFormListExcel(searchContractFormVO);

			for (int j = 0; j < contractFormValueVOList.size(); j++) {
				Cell cellValueData = rowData.createCell(6 + j);
				cellValueData.setCellValue(contractFormValueVOList.get(j).getFormDataValue());
			}
		}
		try { // excel파일생성
			FileOutputStream file = new FileOutputStream(excelFile);
			wb.write(file);
			file.close();
			wb.dispose();
		}
		catch (FileNotFoundException e) { e.printStackTrace(); }
		catch (IOException e) 			{ e.printStackTrace(); }

		return 0;
	}



	public void sendEmailWithFile(ContractPersonVO vo, String zipFileURL, String zipFileName) throws Exception {

		String linkURL = zipFileURL;
		String content = "";

		try {
			content += "<!DOCTYPE html> ";
			content += "<html> ";
			content += "<head> ";
			content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
			content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
			content += "<title>전자문서</title> ";
			content += "</head> ";
			content += "<body> ";
			content += "	<div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
			content += "		<div style=\" padding: 37px 0 0 0;\"> ";
			content += "			<div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
			content += "				<span style=\"font-weight: bold; color:#00a9e9;\">IEUM</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;SIGN</span> ";
			content += "			</div> ";
			content += "			<div style=\"padding-left: 50px;  font-size: 30px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
			content += "				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\"> ㈜뉴젠피앤피 </span> ";
			content += "			</div> ";
			content += "			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
			content += "				<span> ";
			content += "					완료계약서 일괄 다운로드 이메일 전송이 완료되었습니다.<br> ";
			content += "					하단의 다운로드를 클릭하여 완료계약서를 다운받아 주시기 바랍니다. ";
			content += "				</span>";
			content += "			</div> ";
			content += "			<div style=\"margin: 74px 0 32px; text-align: center;\"> ";
			content += "				<a href=\"" + linkURL + "\" style=\"display: inline-block; padding: 12px 90px; border-radius: 8px; font-family: 'tahoma'; font-size: 18px; font-weight: bold; text-decoration: none; color: #fff; background-color: #00a9e9;\">다운로드</a>";
			content += "			</div> ";
			content += "		</div> ";
			content += "		<div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
			content += "			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
			content += "				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
			content += "				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">service@newzenpnp.com</a></span > ";
			content += "				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
			content += "			</div> ";
			content += "		</div> ";
			content += "	</div> ";
			content += "</body> ";
			content += "</html> ";

			logger.info("======================================");
			logger.info("===        일괄다운 SMTP 시작       ===");
			logger.info("======================================");

			MultiPartEmail email = new MultiPartEmail();
			MailVO mailVO 		 = new MailVO();
			mailVO.setFrom("no_reply@newzenpnp.com");
			mailVO.setTo(vo.getContractEMail());
			mailVO.setCc("");
			mailVO.setBcc("");
			mailVO.setSubject("[뉴젠피앤피] 전자문서 일괄다운로드가 완료되었습니다.");
			mailVO.setText(content);
			email.send(mailVO);

			logger.info("Ieum Sign 전자문서 일괄다운로드 이메일을 발송. email : " + vo.getContractEMail());

			if (vo.getSmsYN().equals("Y")) {	// SMS 서비스 신청 시 문자 전송

				content  = "["+vo.getBizName()+" * 일괄 다운로드]\n\n";
				content += "요청하신 완료계약서 메일 발송이 완료되었습니다.\n\n";
				content += "이메일을 확인해 주세요.\n";

				BizTalkKKOVO kkoVO = new BizTalkKKOVO();
				kkoVO.setSubject("");
				kkoVO.setContent(content);
				kkoVO.setCallback(System.getProperty("biztalk.kko.callback_number"));
				kkoVO.setRecipientNum(vo.getPhoneNum());
				kkoVO.setSenderKey(System.getProperty("biztalk.kko.sender_key"));
				kkoVO.setTemplateCode("contract018");
				kkoVO.setKkoBtnType("");
				kkoVO.setKkoBtnInfo("");
				bizTalkDAO.insBizTalk(kkoVO);
			}

		} catch (Exception  e){
			e.printStackTrace();
			throw new Exception("Failed to send email with attachment.", e);
		}
	}



	public void sendErrorEmail(ContractPersonVO vo) throws Exception {

		String content		 = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timestamp	 = sdf.format(new Date());

		try {
			content += "<!DOCTYPE html> ";
			content += "<html> ";
			content += "<head> ";
			content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
			content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
			content += "<title>전자문서</title> ";
			content += "</head> ";
			content += "<body> ";
			content += "	<div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
			content += "		<div style=\" padding: 37px 0 0 0;\"> ";
			content += "			<div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
			content += "				<span style=\"font-weight: bold; color:#00a9e9;\">IEUM</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;SIGN</span> ";
			content += "			</div> ";
			content += "			<div style=\"padding-left: 50px;  font-size: 30px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
			content += "				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\"> ㈜뉴젠피앤피 </span> ";
			content += "			</div> ";
			content += "			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
			content += "				<span> ";
			content += "					완료계약서 일괄 다운로드 이메일 전송이 오류로 인해 실패되었습니다.<br> ";
			content += "					오류 시간	: " + timestamp + "<br>";
			content += "					회사명		: " + vo.getBizName() + "<br>";
			content += "					bizID		: " + vo.getBizId() + "<br>";
			content += "					sms유무		: " + vo.getSmsYN() + "<br>";
			content += "					이메일		: " + vo.getContractEMail() + "<br>";
			content += "					메일전송유무	: " + vo.getDownloadYn();
			content += "				</span>";
			content += "			</div> ";
			content += "		</div> ";
			content += "		<div style=\"background-color: #2b2b2b; margin-top: 41px; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
			content += "			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
			content += "				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
			content += "				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">service@newzenpnp.com</a></span > ";
			content += "				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
			content += "			</div> ";
			content += "		</div> ";
			content += "	</div> ";
			content += "</body> ";
			content += "</html> ";

			logger.info("======================================");
			logger.info("===      일괄다운 실패 SMTP 시작     ===");
			logger.info("======================================");

			MultiPartEmail email = new MultiPartEmail();
			MailVO mailVO = new MailVO();
			mailVO.setFrom("no_reply@newzenpnp.com");
			mailVO.setTo("newzenpnp@daum.net");
			mailVO.setCc("");
			mailVO.setBcc("");
			mailVO.setSubject("[뉴젠피앤피] 전자문서 일괄다운로드가 실패하였습니다.");
			mailVO.setText(content);
			email.send(mailVO);

			MailVO mailVO2 = new MailVO();
			mailVO2.setFrom("no_reply@newzenpnp.com");
			mailVO2.setTo("neomail123@naver.com");
			mailVO2.setCc("");
			mailVO2.setBcc("");
			mailVO2.setSubject("[뉴젠피앤피] 전자문서 일괄다운로드가 실패하였습니다.");
			mailVO2.setText(content);
			email.send(mailVO2);

			logger.info("Ieum Sign 전자문서 일괄다운로드 실패 이메일을 발송. email : " + vo.getContractEMail());

		} catch (Exception  e) {
			e.printStackTrace();
			throw new Exception("Failed to send email with attachment.", e);
		}
	}


	// 미리 보기 PDF 입력값 저장
	@Override
	public String createContractPreview(ContractFormVO vo) throws Exception {
		ContractFormVO dataVO = new ContractFormVO();
		String[] formList 	  = StringUtil.split(vo.getFormDataValue(), "|");

		//사용자|중간관리자 개별등록 미 완료 계약 생성(등록수정)
		ContractPersonVO contractVO = new ContractPersonVO();
		contractVO.setServiceId(ContractService.SERVICE_ID);
		contractVO.setBizId(vo.getBizId());
		contractVO.setUserId(vo.getUserId());
		contractVO.setContractDate(vo.getContractDate());
		contractVO.setContractType("1");
		contractVO.setContractName("");
		contractVO.setContractFileId(vo.getContractFileId());
		contractVO.setStatusCode("1");
		contractVO.setTransType("N");

		//계약서 생성
		logger.info("----- contractPerson 생성 -----");
		String insContId = contractDAO.insContractPerson(contractVO);

		for (String s : formList) {

			String[] formValue = StringUtil.split(s, "^");
			if (formValue.length > 1) {
				dataVO.setContId(insContId);
				dataVO.setServiceId(ContractService.SERVICE_ID);
				dataVO.setBizId(vo.getBizId());
				dataVO.setUserId(vo.getUserId());
				dataVO.setContractDate(vo.getContractDate());
				dataVO.setFormDataId(formValue[0]);
				dataVO.setFormDataName(URLDecoder.decode(formValue[1],StandardCharsets.UTF_8.name()));
				dataVO.setFormDataValue(URLDecoder.decode(formValue[2],StandardCharsets.UTF_8.name()));
				logger.info("----- contractForm 데이터 추가 -----");
				//폼 데이터 추가
				contractDAO.insContractForm(dataVO);
			}
		}
		return insContId;
	}


	// 미리보기 입력값으로 새 PDF 생성
	@Override
	public ContentVO createContractPreviewPDF(ContractPersonVO vo, SessionVO loginVO, String BizType, String UserType, String CustomerType) throws Exception {
		ContentVO resContract = null;
		ContentVO contentVO	  = new ContentVO();
		CabinetVO cabinetVO	  = new CabinetVO();

		logger.info("----- [createContractPreviewPDF] 계약서 조회 -----");
		// 미리보기 계약서 조회
		ContractPersonVO resultContractVO = contractDAO.getContractPersonPreview(vo);

		// 콘텐츠에서 정보 추출
		contentVO.setFileId(resultContractVO.getContractFileId());

		logger.info("----- [createContractPreviewPDF] 콘텐츠 조회 -----");
		ContentVO resultVO = contentDAO.getContent(contentVO);
		if (resultVO==null) {
			logger.error("[createContractPersonPDF] 콘텐츠 정보가 존재하지 않습니다.");
			vo.setResultCode("200");
			vo.setResultMessage("콘텐츠 정보가 존재하지 않습니다.");
			return null;
		}

		logger.info("----- [createContractPreviewPDF] 캐비넷 조회 -----");
		// 저장위치
		cabinetVO.setClassId(resultVO.getClassId());
		cabinetVO.setCabinetId(resultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO==null) {
			logger.error("[createContractPersonPDF] 캐비넷 정보가 존재하지 않습니다.");
			vo.setResultCode("210");
			vo.setResultMessage("캐비넷 정보가 존재하지 않습니다.");
			return null;
		}

		logger.info("----- [createContractPreviewPDF] 조회 완료 -----");

		// 경로
		String systemTempPath  = MultipartFileUtil.getSystemTempPath();
		String originalPdfPath = cabinetResultVO.getCabinetPath()+resultVO.getFilePath()+resultVO.getFileName();
		String targetPdfName   = StringUtil.getUUID()+".pdf";
		String targetPdfPath   = systemTempPath + targetPdfName;

		// 사인
		String bizImage 	 = getSystemPath()+"images/sign/digitsign.png";
		String customerImage = getSystemPath()+"images/sign/digitsign_1.png";
		String userImage	 = getSystemPath()+"images/sign/digitsign_2.png";

		logger.info("----- [createContractPreviewPDF] 파일 복사 완료 -----");

		// 사용할 PDF복사
		FileUtil.createDirectory(systemTempPath);
		if(FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {

			// 파일 리스트
			List<FieldVO> fieldListVO = new ArrayList<>();
			// 서명란 key 값 검색
			InputStream is		   = Files.newInputStream(Paths.get(targetPdfPath));
			List<FormVO> formList2 = FieldUtil.getAllField(is, false);
			is.close();

			logger.info("----- [createContractPreviewPDF] 사인 영역 체크 -----");
			// 회사 사인 영역 체크
			checkSignAreas(BizType, "sign_1", bizImage, fieldListVO, formList2);
			// 근로자 사인 영역 체크
			checkSignAreas(UserType, "sign_2", userImage, fieldListVO, formList2);
			// 거래처 사인 영역 체크
			checkSignAreas(CustomerType, "sign_2", customerImage, fieldListVO, formList2);

			logger.info("----- [createContractPreviewPDF] 입력 항목 체크 -----");

			// 계약서 정보
			fieldListVO.add(FieldUtil.setFieldData("cont_id", resultContractVO.getContId()));
			fieldListVO.add(FieldUtil.setFieldData("service_id", resultContractVO.getServiceId()));
			fieldListVO.add(FieldUtil.setFieldData("biz_id", resultContractVO.getBizId()));
			fieldListVO.add(FieldUtil.setFieldData("user_id", resultContractVO.getUserId()));
			fieldListVO.add(FieldUtil.setFieldData("contract_date", resultContractVO.getContractDate()));

			// 계약 입력항목 설정
			ContractFormVO formVO = new ContractFormVO();
			formVO.setContId(resultContractVO.getContId());

			logger.info("----- [createContractPreviewPDF] 엑셀 정보 조회 -----");
			List<ContractFormVO> formList = contractDAO.getContractPreviewFormList(formVO);

			// 엑셀 데이터 저장
            for (ContractFormVO formData : formList) fieldListVO.add(FieldUtil.setFieldData(formData.getFormDataId(), formData.getFormDataValue()));

			logger.info("----- [createContractPreviewPDF] 파일생성 -----");
			boolean bWritePDF = ControlUtil.writePDF(fieldListVO, originalPdfPath, targetPdfPath, false, false, System.getProperty("pdf.font.default"));

			//콘텐츠등록
			if (bWritePDF) {
				contentVO = new ContentVO();
				contentVO.setFileType(FileUtil.getFileExt(targetPdfPath));
				contentVO.setFileName(targetPdfName);
				contentVO.setOrgFileName("TestPreview_" + resultVO.getFileTitle() + ".pdf");
				contentVO.setFileSize(Long.toString(FileUtil.getFileSize(targetPdfPath)));
				contentVO.setFilePath(MultipartFileUtil.getSystemTempPath());
				contentVO.setFileTitle("TestPreview_" + resultVO.getFileTitle());
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
				if (ContentService.COMPLETED==nResult) {
					logger.info("----- [createContractPreviewPDF] 미리보기 생성 완료 -----");
					ContractPersonVO contractPersonVO = new ContractPersonVO();
					contractPersonVO.setBizId(resultContractVO.getBizId());
					contractPersonVO.setContId(resultContractVO.getContId());
					contractPersonVO.setHashData(PKIUtil.getFileHash(targetPdfPath));
					contractPersonVO.setStatusCode("1");
					contractPersonVO.setContractId(contentVO.getFileId());
					contractPersonVO.setTransType("Y");
					// 난수 초기화
					contractPersonVO.setDigitNonce("");
					contractPersonVO.setSendDate("");

					logger.info("----- [createContractPreviewPDF] 계약정보 업데이트 -----");
					contractDAO.updContractPerson(contractPersonVO);
				} else {
					logger.info("----- [createContractPreviewPDF] 미리보기 생성 실패 -----");
				}
				resContract = contentVO;
			}
		}
		return resContract;
	}


	// 양식 수정요청 에 따른 상태 변경 / 메일 전송
	@Override
	public ContractPersonNewVO contractNewReload(ContractPersonNewVO vo, String message) {

		ContractPersonNewVO result = new ContractPersonNewVO();
		logger.info("[contractNewReload]  기업id -> "+vo.getBizId());

		try {
			result.setBizId(vo.getBizId());
			result.setFileId(vo.getFileId());
			result.setContractId("");
			result.setDataFileId("");
			result.setAlterFileId(null);
			result.setTransType("N");
			logger.info("----- [contractNewReload] 양식 수정 요청에 따른 상태 변경 -----");
			contractDAO.updContractNewRe(result);
			logger.info("----- [contractNewReload] 기존 등록 엑셀 데이터 삭제 -----");
			contractDAO.delContractNewFormRe(result);

			sendContractNewReloadEmail(vo.getBizId(), vo.getFileId(),message);
		} catch (Exception ex) { ex.printStackTrace(); }
		return result;
	}


	// 테스트 양식 수정요청 메일 전송
	public void sendContractNewReloadEmail(String bizId, String fileId, String message) {

		EmpVO vo = new EmpVO();
		vo.setBizId("171220094226009"); //  운영 시스템관리자 bizID
		vo.setEmpType("9");
		vo.setStartPage(-1);
		vo.setEndPage(-1);
		List<EmpVO> result = empDAO.getEmpList(vo);

		//양식 작성 기업검색
		BizVO bizVO = new BizVO();
		bizVO.setBizId(bizId);
		bizVO.setStartPage(0);
		bizVO.setEndPage(1);
		List<BizVO> bizList = bizDAO.getBizList(bizVO);
		BizVO resBizVO;

		if (bizList != null && !bizList.isEmpty()) {
			resBizVO = bizList.get(0);
		} else {
			logger.info("----- 슈퍼관리자가 지정되지 않았습니다. -----");
			return;
		}

		//양식검색
		ContractPersonNewVO newVO = new ContractPersonNewVO();
		newVO.setBizId(bizId);
		newVO.setFileId(fileId);
		List<ContractPersonNewVO> newList = contractDAO.getContractPersonNewList(newVO);

		if (bizList.isEmpty()) {
			logger.info("----- 양식이 존재하지 않습니다. fildId =>["+fileId+"] -----");
			return;
		}

		logger.info("----- 테스트 양식 수정요청 메일 전송 -----");
        for (EmpVO empVO : result) {

            String content = "";
            content += "<!DOCTYPE html> ";
            content += "<html> ";
            content += "<head> ";
            content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
            content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
            content += "<title>전자문서</title> ";
            content += "</head> ";
            content += "<body> ";
            content += "	<div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
            content += "		<div style=\" padding: 37px 0 0 0;\"> ";
            content += "			<div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
            content += "				<span style=\"font-weight: bold; color:#00a9e9;\">IEUM</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;SIGN</span> ";
            content += "			</div> ";
            content += "			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
            content += "				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">기업관리자가 테스트 양식 수정 요청을 </span><span class=\"font_black\" style=\"color:#202020;\">하였습니다.</span>";
            content += "			</div> ";
            content += "			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
            content += "				<span >수정요청 내역 : " + message + "<br> ";
            content += "			</div> ";
            content += "		<div style=\"margin: 74px 0 32px; text-align: center;\"> ";
            content += "			<a href=\"" + System.getProperty("system.feb.url") + "\" style=\"display: inline-block; padding: 12px 90px; border-radius: 8px; font-family: 'tahoma'; font-size: 18px; font-weight: bold; text-decoration: none; color: #fff; background-color: #00a9e9;\">로그인하기</a>";
            content += "		</div> ";
            content += "		</div> ";
            content += "		<div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
            content += "			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
            content += "				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
            content += "				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">service@newzenpnp.com</a></span > ";
            content += "				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
            content += "			</div> ";
            content += "		</div> ";
            content += "	</div> ";
            content += "</body> ";
            content += "</html> ";

            MultiPartEmail email = new MultiPartEmail();
            MailVO mailVO 		 = new MailVO();

            // 시스템관리자 이메일이 있는 경우에만 수행
            if (empVO.getEMail() != null && !empVO.getEMail().isEmpty()) {
                mailVO.setFrom("no_reply@newzenpnp.com");
                mailVO.setTo(empVO.getEMail());
                mailVO.setCc("");
                mailVO.setBcc("");
                mailVO.setSubject("[뉴젠피앤피] " + resBizVO.getBizName() + "업체에서 테스트 양식 수정요청을 하였습니다.");
                mailVO.setText(content);
                logger.info("Ieum Sign 전자문서 슈퍼관리자 알림 이메일을 발송. email : " + empVO.getEMail());
                email.send(mailVO);
            }
        }
	}



	private void checkSignAreas(String type, String signId, String image, List<FieldVO> fieldListVO, List<FormVO> formList2) {

		List<String> keyList = new ArrayList<>();
		for (FormVO formVO : formList2) {
			String formId = formVO.getId();
			if (formId.contains(signId)) keyList.add(formId);
		}
		for (String lineId : keyList) {
			if (StringUtil.isNotNull(lineId)) {
				FieldVO fieldVO = new FieldVO();
				fieldVO.setId(lineId);
				fieldVO.setImage("Y".equals(type) ? image : "");
				fieldListVO.add(fieldVO);
			}
		}
		keyList.clear();
	}



	@Override
	public ContractPersonVO getContractPreview(ContractPersonVO vo, SessionVO loginVO) {
		ContractPersonVO result = contractDAO.getContractPersonPreview(vo);

		// 콘텐츠에서 정보 추출
		ContentVO contentVO = new ContentVO();
		contentVO.setFileId(result.getContractId());
		ContentVO resultVO  = contentDAO.getContent(contentVO);
		if (resultVO == null) {
			logger.error("[getContractPreview] 콘텐츠 정보가 존재하지 않습니다.");
			return null;
		}

		// 저장위치
		CabinetVO cabinetVO 	 = new CabinetVO();
		cabinetVO.setClassId(resultVO.getClassId());
		cabinetVO.setCabinetId(resultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO == null) {
			logger.error("[getContractPreview] 캐비넷 정보가 존재하지 않습니다.");
			return null;
		}

		String originalPdfPath = cabinetResultVO.getCabinetPath()+resultVO.getFilePath()+resultVO.getFileName();
		String targetPdfName   = StringUtil.getUUID()+".pdf";
		String targetPdfPath   = MultipartFileUtil.getSystemTempPath()+targetPdfName;

		// 사용할 PDF복사
		FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
		if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
			String pdf_uri = StringUtil.ReplaceAll(targetPdfPath, getSystemPath(), "");
			result.setPdfFile(pdf_uri);
		}

		if (StringUtil.isNotNull(loginVO.getIpAddr())) {
			List<String> userIdList = new ArrayList<>();
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



	@Override
	public List<ContractFormVO> getContractFormListUpdate(ContractFormVO vo) { return contractDAO.getContractPreviewFormList(vo); }


	// 개별계약서 삭제
	@Override
	public int delContractPreview(List<ContractPersonVO> list, SessionVO loginVO) {

		int result = 0;

		for (ContractPersonVO vo : list) {
			logger.info("delContractPreview ContId : "+vo.getContId());

			try {
				ContractPersonVO contractResultVO = contractDAO.getContractPersonPreview(vo);
				if (contractResultVO == null) {
					logger.error("[delContractPreview] 근로계약서정보가 존재하지 않습니다.");
					vo.setResultMessage("근로계약서정보가 존재하지 않습니다.");
					return -4;
				}
				String contId = contractResultVO.getContId();
				String fileId = contractResultVO.getContractId();
				ContractPersonVO contractPersonVO = new ContractPersonVO();
				contractPersonVO.setContId(contId);

				logger.info("----- 계약정보 삭제 -----");
				// 계약정보 삭제
				contractDAO.delContractPerson(contractPersonVO);

				logger.info("----- 계약조건정보 삭제 -----");
				// 계약조건정보 삭제
				ContractFormVO contractFormVO = new ContractFormVO();
				contractFormVO.setContId(contId);
				contractDAO.delContractForm(contractFormVO);

				logger.info("----- 컨텐츠정보 삭제 -----");
				// 콘텐츠 정보 삭제
				ContentVO contentVO = new ContentVO();
				contentVO.setFileId(fileId);
				contentDAO.delContent(contentVO);

			} catch (Exception e) { e.printStackTrace(); }
		}
		return result;
	}


	@Override
	public void delPreview() { contractDAO.delPreview(); }

}
