package com.ezsign.document.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.ezsign.contact.dao.ContactDAO;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizGrpVO;
import com.ezsign.content.dao.CabinetDAO;
import com.ezsign.content.dao.ContentDAO;
import com.ezsign.content.dao.ContentWordDAO;

import com.ezsign.content.service.ContentService;
import com.ezsign.content.vo.CabinetVO;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.content.vo.ContentWordVO;
import com.ezsign.document.dao.DocumentDAO;
import com.ezsign.document.service.DocumentService;
import com.ezsign.document.vo.DocumentCommentVO;
import com.ezsign.document.vo.DocumentDetailVO;
import com.ezsign.document.vo.DocumentEformVO;
import com.ezsign.document.vo.DocumentFileVO;
import com.ezsign.document.vo.DocumentLogVO;
import com.ezsign.document.vo.DocumentMasterVO;
import com.ezsign.document.vo.DocumentRequestVO;
import com.ezsign.document.vo.DocumentResponseVO;
import com.ezsign.document.vo.DocumentSettingVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.framework.mail.MailVO;
import com.ezsign.framework.mail.MultiPartEmail;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.ProcUtil;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.util.TSAUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.point.dao.PointDAO;
import com.ezsign.point.vo.PointLogVO;
import com.ezsign.point.vo.PointVO;
import com.ezsign.user.dao.UserDAO;
import com.ezsign.user.vo.UserVO;
import com.jarvis.pdf.util.ControlUtil;
import com.jarvis.pdf.util.FieldUtil;
import com.jarvis.pdf.util.ToolUtil;
import com.jarvis.pdf.vo.FieldVO;
import com.jarvis.pdf.vo.LineVO;
import com.jarvis.pdf.vo.PageVO;
import com.lowagie.text.pdf.PdfReader;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("documentService")
public class DocumentServiceImpl extends AbstractServiceImpl implements DocumentService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "userDAO")
	UserDAO userDAO;

	@Resource(name="empDAO")
	EmpDAO empDAO;

	@Resource(name = "documentDAO")
	DocumentDAO documentDAO;

	@Resource(name = "contentDAO")
	ContentDAO contentDAO;

	@Resource(name = "contentWordDAO")
	ContentWordDAO contentWordDAO;

	@Resource(name = "cabinetDAO")
	CabinetDAO cabinetDAO;

	@Resource(name = "contentService")
	ContentService contentService;

	@Resource(name="pointDAO")
	private PointDAO pointDAO;

	@Resource(name="bizDAO")
	private BizDAO bizDAO;
	@Resource(name="contactDAO")
	private ContactDAO contactDAO;

	// 문서 등록
	private void addDocument(DocumentMasterVO masterVO, List<DocumentDetailVO> detailList, List<FileVO> fileList, List<FieldVO> fieldList, List<ContentWordVO> wordList) throws Exception {
		String docId		= StringUtil.getUUID();
		String orgFileId	= "";
		String tempSaveDate = DateUtil.getTimeStamp(7);

		// 파일(계약서, 첨부파일) 등록
		for (FileVO fileVO : fileList) {

			String filePath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
			File file		= new File(filePath);
			if (!file.exists()) logger.error("[addDocument]  파일이 존재하지 않습니다.");

			String calssId = this.DOCU_ATT_ID;
			String key 	   = fileVO.getFileKey();
			if (key.contains("DOCUMENT_FILE")) {
				calssId = this.DOCU_CON_ID;
				//////////////////////////////////////////////// E-FORM생성 START /////////////////////////////////////////////////////
				if (fieldList != null) {

					String isPath   = filePath;
					String osPath   = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + FileUtil.getFileName(fileVO.getFileStreNm()) + "_X." + FileUtil.getFileExt(fileVO.getFileStreNm());
					InputStream is  = null;
					OutputStream os = null;
					try {
						is = new FileInputStream(isPath);
						os = new FileOutputStream(osPath);

						String formData = "";
						for(FieldVO fieldVO : fieldList) {
							formData += StringUtil.nvl(fieldVO.getId(), "") + "-";
							formData += StringUtil.nvl(Integer.toString(fieldVO.getType()), "4") + "-";
							formData += fieldVO.getX() + "-";
							formData += fieldVO.getY() + "-";
							formData += fieldVO.getWidth() + "-";
							formData += fieldVO.getHeight() + "-";
							formData += StringUtil.nvl(Integer.toString(fieldVO.getPage()), "1") + "-";
							formData += StringUtil.nvl(fieldVO.getName(), "") + "-1-1-1|";
						}
						logger.info(formData);
						if (!formData.isEmpty()) ToolUtil.makePDFField(is, os, "", formData);

						is.close();
						os.close();

						fileVO.setFileStreNm(FileUtil.getFileName(fileVO.getFileStreNm()) + "_X." + FileUtil.getFileExt(fileVO.getFileStreNm()));
						filePath = osPath;

						// 계약서 eform 항목등록
						for (FieldVO fieldVO : fieldList) {
							DocumentEformVO eformVO = new DocumentEformVO();
							eformVO.setDocuId(docId);
							eformVO.setFormId(fieldVO.getId());
							eformVO.setFormName(fieldVO.getName());
							eformVO.setFormType(Integer.toString(fieldVO.getType()));
							eformVO.setFormValue("");

							documentDAO.insDocumentEform(eformVO);
						}
					} catch (Exception e) {
						throw new Exception("[addDocument] 계약서 작업중 오류가 발생하였습니다.\r\n" + e.getMessage());
					} finally {
						try {
							if (is != null) is.close();
							if (os != null) os.close();
						} catch (IOException ioe) { ioe.printStackTrace(); }
					}
				}
				//////////////////////////////////////////////// E-FORM생성 END /////////////////////////////////////////////////////
			}
			ContentVO contentVO = new ContentVO();
			contentVO.setFileType(FileUtil.getFileExt(filePath));
			contentVO.setFileName(fileVO.getFileStreNm());
			contentVO.setOrgFileName(fileVO.getFileStreOriNm());
			contentVO.setFileSize(Long.toString(FileUtil.getFileSize(filePath)));
			contentVO.setFilePath(fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR);
			contentVO.setFileTitle(StringUtil.ReplaceAll(fileVO.getFileStreOriNm(), "." + fileVO.getFileExt(), ""));
			contentVO.setClassId(calssId);
			contentVO.setFileVersion("0");
			contentVO.setParFileId("");
			contentVO.setEtcDesc("");
			contentVO.setCheckInOut("N");
			contentVO.setCheckUserId("");
			contentVO.setCheckCount("0");
			contentVO.setUseYn("Y");
			contentVO.setDelYn("N");

			int nResult = contentService.transContent(contentVO);
			logger.info(nResult);
			if (ContentService.COMPLETED == nResult) {
				if (key.indexOf("DOCUMENT_FILE") >= 0) {
					orgFileId = contentVO.getFileId();
					String hashData = "test";

					masterVO.setHashData(hashData);
					masterVO.setOrgFileId(orgFileId);

					// 계약서 키워드 항목등록
					if (wordList != null) {
						for(ContentWordVO wordVO : wordList) {
							wordVO.setBizId(masterVO.getBizId());
							wordVO.setUserId(masterVO.getUserId());
							wordVO.setFileId(docId);
							contentWordDAO.insContentWord(wordVO);
						}
					}
				} else {
					DocumentFileVO docuFileVO = new DocumentFileVO();
					docuFileVO.setDocuId(docId);
					docuFileVO.setFileId(contentVO.getFileId());
					docuFileVO.setFileName(fileVO.getFileStreOriNm());
					docuFileVO.setFileUserId(masterVO.getUserId());
					if (fileVO.getFileKey().contains("imageStamp")) docuFileVO.setRecvSign("Y");
					else 											docuFileVO.setRecvSign("N");

					documentDAO.insDocumentFile(docuFileVO);
					documentDAO.updDocumentFile(docuFileVO);
				}
			}
		}

		/////////////////// 계약서 master 생성 START ///////////////////
		// 임시저장인경우 제외
		masterVO.setDocuId(docId);

		// 비밀번호인증시
		if (masterVO.getAuthType().equals("P")) {
			String authCode = SecurityUtil.encrypt(masterVO.getAuthCode());
			masterVO.setAuthCode(authCode);
		} else {
			masterVO.setAuthCode("");
		}

		// 유효기간설정시
		if (masterVO.getExpireType().equals("N")) masterVO.setExpireDate("");

		// 임시저장시
		if (masterVO.getTempSaveType().equals("T")) {
			masterVO.setTempSaveDate(tempSaveDate);
			masterVO.setDocuStatus("0");
		} else {
			masterVO.setDocuStatus("1");
		}
		documentDAO.insDocumentMaster(masterVO);

		/////////////// 계약서 마스트정보 키워드 등록 ///////////////
		ContentWordVO masterWordVO = new ContentWordVO();
		masterWordVO.setBizId(masterVO.getBizId());
		masterWordVO.setUserId(masterVO.getUserId());
		masterWordVO.setFileId(docId);
		masterWordVO.setSystemType("S");
		masterWordVO.setWorkType("TBL_DOCUMENT_MASTER");
		masterWordVO.setKeyName("계약명");
		masterWordVO.setKeyWord(masterVO.getDocuName());
		contentWordDAO.insContentWord(masterWordVO);
		masterWordVO.setKeyName("생성자명");
		masterWordVO.setKeyWord(masterVO.getUserName());
		contentWordDAO.insContentWord(masterWordVO);
		/////////////// 계약서 마스트정보 키워드 등록 ///////////////


		/////////////////// 계약서 master 생성 START ///////////////////

		/////////////////// 계약서 detail 생성 START ///////////////////
		for (DocumentDetailVO detailVO : detailList) {
			detailVO.setDocuId(docId);

			// 본인인경우 정보 설정
			if (detailVO.getRecvType().equals("W")) {
				UserVO searchUserVO = new UserVO();
				searchUserVO.setUserId(masterVO.getUserId());
				List<UserVO> userList = userDAO.getUserList(searchUserVO);
				if (userList == null || userList.isEmpty()) throw new Exception("본인정보를 가져오지 못했습니다."+detailVO.getRecvUserName());

				UserVO userVO = userList.get(0);
				detailVO.setRecvType("R");
				detailVO.setRecvUserName(userVO.getUserName());
				detailVO.setRecvPhone(userVO.getPhoneNum());
				detailVO.setRecvEmail(userVO.getEMail());
			}
			documentDAO.insDocumentDetail(detailVO);

			/////////////// 계약서 세부정보 키워드 등록 ///////////////
			ContentWordVO detailWordVO = new ContentWordVO();
			detailWordVO.setBizId(masterVO.getBizId());
			detailWordVO.setUserId(masterVO.getUserId());
			detailWordVO.setFileId(docId);
			detailWordVO.setSystemType("S");
			detailWordVO.setWorkType("TBL_DOCUMENT_MASTER");
			detailWordVO.setKeyName("수신자명");
			detailWordVO.setKeyWord(detailVO.getRecvUserName());
			contentWordDAO.insContentWord(detailWordVO);
			/////////////// 계약서 세부정보 키워드 등록 ///////////////
		}
		/////////////////// 계약서 detail 생성 START ///////////////////

		// 로그생성
		DocumentLogVO insLogVO = new DocumentLogVO();
		insLogVO.setDocuId(docId);
		insLogVO.setLogType("L10");
		insLogVO.setLogMessage("계약서가 생성되었습니다");
		insLogVO.setWorkUserId(masterVO.getUserId());
		insLogVO.setFileId(masterVO.getOrgFileId());
		documentDAO.insDocumentLog(insLogVO);
	}

	// 문서 수정
	private void updDocument(DocumentMasterVO masterVO, List<DocumentDetailVO> detailList, List<DocumentFileVO> fileList, List<FileVO> attachList, List<FieldVO> fieldList, List<ContentWordVO> wordList) throws Exception {
		List<ContentVO> delContentList = new ArrayList();
		String orgFileId 			   = "";
		String tempSaveDate			   = DateUtil.getTimeStamp(7);

		// 파일(계약서, 첨부파일) 등록
		for (FileVO fileVO : attachList) {

			String filePath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
			File file 		= new File(filePath);
			if (!file.exists()) logger.error("[updDocument]  파일이 존재하지 않습니다.");

			String calssId = this.DOCU_ATT_ID;
			String key 	   = fileVO.getFileKey();
			if (key.contains("DOCUMENT_FILE")) {
				calssId = this.DOCU_CON_ID;
				// 계약서 기존 키워드 삭제(사용자등록키워드만)
				ContentWordVO delContentWordVO = new ContentWordVO();
				delContentWordVO.setBizId(masterVO.getBizId());
				delContentWordVO.setFileId(masterVO.getDocuId());
				contentWordDAO.delContentWord(delContentWordVO);

				//////////////////////////////////////////////// E-FORM생성 START /////////////////////////////////////////////////////
				if (fieldList != null) {

					DocumentEformVO  delFormVO = new DocumentEformVO();
					delFormVO.setDocuId(masterVO.getDocuId());
					//기존 데이터 지우고 시작
					documentDAO.delDocumentEform(delFormVO);

					String isPath   = filePath;
					String osPath   = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + FileUtil.getFileName(fileVO.getFileStreNm()) + "_X." + FileUtil.getFileExt(fileVO.getFileStreNm());
					InputStream is  = null;
					OutputStream os = null;
					try {
						is = new FileInputStream(isPath);
						os = new FileOutputStream(osPath);

						String formData = "";
						for (FieldVO fieldVO : fieldList) {
							formData += StringUtil.nvl(fieldVO.getId(), "") + "-";
							formData += StringUtil.nvl(Integer.toString(fieldVO.getType()), "4") + "-";
							formData += fieldVO.getX() + "-";
							formData += fieldVO.getY() + "-";
							formData += fieldVO.getWidth() + "-";
							formData += fieldVO.getHeight() + "-";
							formData += StringUtil.nvl(Integer.toString(fieldVO.getPage()), "1") + "-";
							formData += StringUtil.nvl(fieldVO.getName(), "") + "-1-1-1|";
						}
						logger.info(formData);
						if (!formData.isEmpty()) ToolUtil.makePDFField(is, os, "", formData);

						is.close();
						os.close();

						fileVO.setFileStreNm(FileUtil.getFileName(fileVO.getFileStreNm()) + "_X." + FileUtil.getFileExt(fileVO.getFileStreNm()));
						filePath = osPath;

						// 계약서 eform 항목등록
						for (FieldVO fieldVO : fieldList) {
							DocumentEformVO eformVO = new DocumentEformVO();
							eformVO.setDocuId(masterVO.getDocuId());
							eformVO.setFormId(fieldVO.getId());
							eformVO.setFormName(fieldVO.getName());
							eformVO.setFormType(Integer.toString(fieldVO.getType()));
							eformVO.setFormValue("");

							documentDAO.insDocumentEform(eformVO);
						}
					} catch (Exception e) {
						throw new Exception("[addDocument] 계약서 작업중 오류가 발생하였습니다.\r\n" + e.getMessage());
					} finally {
						try {
							if (is != null) is.close();
							if (os != null) os.close();
						} catch (IOException ioe) { ioe.printStackTrace(); }
					}
				}
				//////////////////////////////////////////////// E-FORM생성 END /////////////////////////////////////////////////////
				ContentVO contentVO = new ContentVO();
				contentVO.setFileId(masterVO.getOrgFileId());
				delContentList.add(contentVO);
			}
			ContentVO contentVO = new ContentVO();
			contentVO.setFileType(FileUtil.getFileExt(filePath));
			contentVO.setFileName(fileVO.getFileStreNm());
			contentVO.setOrgFileName(fileVO.getFileStreOriNm());
			contentVO.setFileSize(Long.toString(FileUtil.getFileSize(filePath)));
			contentVO.setFilePath(fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR);
			contentVO.setFileTitle(StringUtil.ReplaceAll(fileVO.getFileStreOriNm(), "." + fileVO.getFileExt(), ""));
			contentVO.setClassId(calssId);
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
				if (key.contains("DOCUMENT_FILE")) {
					orgFileId 		= contentVO.getFileId();
					String hashData = "test";

					masterVO.setHashData(hashData);
					masterVO.setOrgFileId(orgFileId);

					// 계약서 키워드 등록
					if (wordList != null) {
						for (ContentWordVO wordVO : wordList) {
							wordVO.setBizId(masterVO.getBizId());
							wordVO.setUserId(masterVO.getUserId());
							wordVO.setFileId(masterVO.getDocuId());
							contentWordDAO.insContentWord(wordVO);
						}
					}
				} else {
					DocumentFileVO docuFileVO = new DocumentFileVO();
					docuFileVO.setDocuId(masterVO.getDocuId());
					docuFileVO.setFileId(contentVO.getFileId());
					docuFileVO.setFileName(fileVO.getFileStreOriNm());
					docuFileVO.setFileUserId(masterVO.getUserId());
					if (fileVO.getFileKey().contains("imageStamp")) docuFileVO.setRecvSign("Y");
					else 											docuFileVO.setRecvSign("N");

					documentDAO.insDocumentFile(docuFileVO);
					documentDAO.updDocumentFile(docuFileVO);
				}
			}
		}
		/////////////////// 계약서 file 삭제 START ///////////////////
		if (fileList != null) {
			for (DocumentFileVO fileVO : fileList) {
				if (fileVO.getDbMode().equals("D")) {
					ContentVO contentVO = new ContentVO();
					contentVO.setFileId(fileVO.getFileId());
					delContentList.add(contentVO);
					documentDAO.delDocumentFile(fileVO);
				}
			}
		}
		/////////////////// 계약서 file 삭제 START ///////////////////
		// 기존계약 수신자/참조자 삭제
		DocumentDetailVO delDetailVO = new DocumentDetailVO();
		delDetailVO.setDocuId(masterVO.getDocuId());
		documentDAO.delDocumentDetailById(delDetailVO);

		/////////////////// 계약서 detail 생성 START ///////////////////
		for (DocumentDetailVO detailVO : detailList) {
			if (detailVO.getDbMode().equals("C")) documentDAO.insDocumentDetail(detailVO);

			/////////////// 계약서 세부정보 키워드 등록 ///////////////
			ContentWordVO detailWordVO = new ContentWordVO();
			detailWordVO.setBizId(masterVO.getBizId());
			detailWordVO.setUserId(masterVO.getUserId());
			detailWordVO.setFileId(masterVO.getDocuId());
			detailWordVO.setSystemType("S");
			detailWordVO.setWorkType("TBL_DOCUMENT_MASTER");
			detailWordVO.setKeyName("수신자명");
			detailWordVO.setKeyWord(detailVO.getRecvUserName());
			contentWordDAO.insContentWord(detailWordVO);
			/////////////// 계약서 세부정보 키워드 등록 ///////////////
		}
		/////////////////// 계약서 detail 생성 START ///////////////////

		// 비밀번호인증시 (입력이 없을 경우 update X)
		if (masterVO.getAuthType().equals("P") && StringUtil.isNotNull(masterVO.getAuthCode())) {
			String authCode = SecurityUtil.encrypt(masterVO.getAuthCode());
			masterVO.setAuthCode(authCode);
		} else {
			masterVO.setAuthCode("");
		}

		// 유효기간설정시
		if (masterVO.getExpireType().equals("N")) masterVO.setExpireDate("");

		// 임시저장시
		if (masterVO.getTempSaveType().equals("T")) {
			masterVO.setTempSaveDate(tempSaveDate);
			masterVO.setDocuStatus("0");
		} else {
			masterVO.setDocuStatus("1");
		}
		documentDAO.updDocumentMaster(masterVO);

		/////////////// 계약서 마스트정보 키워드 등록 ///////////////
		ContentWordVO masterWordVO = new ContentWordVO();
		masterWordVO.setBizId(masterVO.getBizId());
		masterWordVO.setUserId(masterVO.getUserId());
		masterWordVO.setFileId(masterVO.getDocuId());
		masterWordVO.setSystemType("S");
		masterWordVO.setWorkType("TBL_DOCUMENT_MASTER");
		masterWordVO.setKeyName("계약명");
		masterWordVO.setKeyWord(masterVO.getDocuName());
		contentWordDAO.insContentWord(masterWordVO);
		masterWordVO.setKeyName("생성자명");
		masterWordVO.setKeyWord(masterVO.getUserName());
		contentWordDAO.insContentWord(masterWordVO);
		/////////////// 계약서 마스트정보 키워드 등록 ///////////////

		// 컨텐츠 파일 삭제
		contentService.deleteContent(delContentList);

		// 로그생성
		DocumentLogVO insLogVO = new DocumentLogVO();
		insLogVO.setDocuId(masterVO.getDocuId());
		insLogVO.setLogType("L15");
		insLogVO.setLogMessage("계약서가 수정되었습니다");
		insLogVO.setWorkUserId(masterVO.getUserId());
		insLogVO.setFileId(masterVO.getOrgFileId());
		documentDAO.insDocumentLog(insLogVO);
	}

	// 문서 삭제
	private void delDocument(DocumentMasterVO masterVO) throws Exception {
		List<ContentVO> delContentList    = new ArrayList<>();
		/////////////////// 계약서 comment 삭제 START ///////////////////
		DocumentCommentVO searchCommentVO = new DocumentCommentVO();
		searchCommentVO.setDocuId(masterVO.getDocuId());
		List<DocumentCommentVO> searchCommentList = documentDAO.getDocumentCommentList(searchCommentVO);
		for (DocumentCommentVO commentVO : searchCommentList) documentDAO.delDocumentComment(commentVO);
		/////////////////// 계약서 comment 삭제 START ///////////////////

		/////////////////// 계약서 file 삭제 START ///////////////////
		DocumentFileVO searchFileVO 		= new DocumentFileVO();
		searchFileVO.setDocuId(masterVO.getDocuId());
		List<DocumentFileVO> searchFileList = documentDAO.getDocumentFileList(searchFileVO);
		for (DocumentFileVO fileVO : searchFileList) {
			ContentVO contentVO = new ContentVO();
			contentVO.setFileId(fileVO.getFileId());
			delContentList.add(contentVO);
			documentDAO.delDocumentFile(fileVO);
		}
		/////////////////// 계약서 file 삭제 START ///////////////////

		/////////////////// 계약서 detail 삭제 START ///////////////////
		DocumentDetailVO searchDetailVO 		= new DocumentDetailVO();
		searchDetailVO.setDocuId(masterVO.getDocuId());
		List<DocumentDetailVO> searchDetailList = documentDAO.getDocumentDetailList(searchDetailVO);
		for (DocumentDetailVO detailVO : searchDetailList) {
			detailVO.setDocuId(masterVO.getDocuId());
			detailVO.setRecvType(detailVO.getRecvType());
			detailVO.setRecvUserId(detailVO.getRecvUserId());
			documentDAO.delDocumentDetail(detailVO);
		}
		/////////////////// 계약서 detail 삭제 START ///////////////////

		/////////////////// 계약서 master 삭제 START ///////////////////

		// 임시저장인경우 제외
		DocumentMasterVO searchMasterVO = masterVO;
		searchMasterVO.setBizId(masterVO.getBizId());
		searchMasterVO.setDocuId(masterVO.getDocuId());
		searchMasterVO.setStartPage(0);
		searchMasterVO.setEndPage(1);
		List<DocumentMasterVO> masterList = documentDAO.getDocumentMasterList(searchMasterVO);
		if (!masterList.isEmpty()) {
			for (DocumentMasterVO mstVO : masterList) {
				ContentVO orgFileVO = new ContentVO();
				orgFileVO.setFileId(mstVO.getOrgFileId());
				delContentList.add(orgFileVO);

				if (StringUtil.isNotNull(mstVO.getConFileId())) {
					ContentVO conFileVO = new ContentVO();
					conFileVO.setFileId(mstVO.getOrgFileId());
					delContentList.add(conFileVO);
				}
			}
		}
		documentDAO.delDocumentMaster(searchMasterVO);
		/////////////////// 계약서 master 삭제 START ///////////////////
		// 컨텐츠 파일 삭제
		contentService.deleteContent(delContentList);
	}

	// 문서발송
	private void sendDocument(DocumentMasterVO masterVO) throws Exception {
		DocumentMasterVO resultMasterVO = new DocumentMasterVO();
		// 수정가능여부 체크
		DocumentMasterVO searchMasterVO = new DocumentMasterVO();
		searchMasterVO.setBizId(masterVO.getBizId());
		searchMasterVO.setDocuId(masterVO.getDocuId());
		searchMasterVO.setStartPage(0);
		searchMasterVO.setEndPage(1);
		List<DocumentMasterVO> masterList = documentDAO.getDocumentMasterList(searchMasterVO);
		if (masterList.isEmpty()) logger.error("[sendDocument] 계약정보가 존재하지 않습니다.");

		resultMasterVO = masterList.get(0);
		resultMasterVO.setDomainName(masterVO.getDomainName());

		String digitNonce = masterVO.getDigitNonce();

		// 발송수단(E, K)
		String sendType = resultMasterVO.getSendType();
		// 발송순서(A, S)
		String sendOrd = resultMasterVO.getSendOrd();
		// 검토여부(S, Y, N)
		String reviewType = resultMasterVO.getReviewType();
		//인증수단(M 휴대폰 본인인증)
		String authType = resultMasterVO.getAuthType();

		DocumentDetailVO searchDetailVO = new DocumentDetailVO();
		searchDetailVO.setDocuId(masterVO.getDocuId());
		searchDetailVO.setRecvType("R");
		List<DocumentDetailVO> detailRList = documentDAO.getDocumentDetailList(searchDetailVO);

		searchDetailVO = new DocumentDetailVO();
		searchDetailVO.setDocuId(masterVO.getDocuId());
		searchDetailVO.setRecvType("T");
		List<DocumentDetailVO> detailTList = documentDAO.getDocumentDetailList(searchDetailVO);

		PointVO pointVO = new PointVO();
		pointVO.setBizId(masterVO.getBizId());
		PointVO resultPointVO = pointDAO.getPoint(pointVO);
		if (resultPointVO == null) {
			logger.error("[sendDocument] 포인트정보가 존재하지 않습니다.");
			throw new Exception("[saveDocument] 포인트정보가 존재하지 않습니다.");
		}

		int reducePoint = 0;
		BizGrpVO grpVO = new BizGrpVO();
		grpVO.setBizId(masterVO.getBizId());
		List<BizGrpVO> pointList = bizDAO.getBizGrp(grpVO);
		if (pointList == null){
			logger.error("[sendDocument] 기업정보가 존재하지 않습니다.");
			throw new Exception("[saveDocument] 기업정보가 존재하지 않습니다.");
		} else {
			//현재 보유중인 포인트
			int point = resultPointVO.getCurPoint();
			//전자문서 전송 차감 포인트
			if (StringUtil.isNotNull(pointList.get(0).getSendPoint())) {
				reducePoint = Integer.parseInt(pointList.get(0).getSendPoint());

				//본인인증수단일 경우 1 포인트 추가 ( 휴대폰인증/ 공인인증서 )
				if (authType.equals("M")||authType.equals("C")||authType.equals("MC")) reducePoint++;

				//검토 진행 여부에 따라 1포인트 추가
				if (reviewType.equals("Y")) reducePoint++;
			}

			if ((point - (reducePoint*detailRList.size())) < 0) {
				logger.error("[sendDocument] 잔여 포인트가 존재하지 않습니다. 현재포인트 : <"+point+">, 차감 포인트 : <"+(reducePoint*detailRList.size())+">");
				throw new Exception("[sendDocument] 잔여 포인트가 존재하지 않습니다. 현재포인트 : <"+point+">, 차감 포인트 : <"+(reducePoint*detailRList.size())+">");
			}
		}

		// 검토여부인경우 검토요청
		if (reviewType.equals("Y")) {
			// 발송순서
			if (sendOrd.equals("A")) {
				for (DocumentDetailVO detailVO : detailRList) {
					sendMessageReview(resultMasterVO, detailVO.getRecvUserId(), detailVO.getRecvType(), detailVO.getRecvUserName(), detailVO.getRecvPhone(), detailVO.getRecvEmail());
				}
			} else {
				for (DocumentDetailVO detailVO : detailRList) {
					if (detailVO.getRecvOrd().equals("1")) {
						sendMessageReview(resultMasterVO, detailVO.getRecvUserId(), detailVO.getRecvType(), detailVO.getRecvUserName(), detailVO.getRecvPhone(), detailVO.getRecvEmail());
					}
				}
			}
		} else if (reviewType.equals("S")) { // 검토 후 서명요청
			// 발송순서
			if (sendOrd.equals("A")) {
				for (DocumentDetailVO detailVO : detailRList) {
					sendMessageReview(resultMasterVO, detailVO.getRecvUserId(), detailVO.getRecvType(), detailVO.getRecvUserName(), detailVO.getRecvPhone(), detailVO.getRecvEmail());
				}
			} else {
				for (DocumentDetailVO detailVO : detailRList) {
					if (detailVO.getRecvOrd().equals("1")) {
						sendMessageReview(resultMasterVO, detailVO.getRecvUserId(), detailVO.getRecvType(), detailVO.getRecvUserName(), detailVO.getRecvPhone(), detailVO.getRecvEmail());
					}
				}
			}

			for (DocumentDetailVO detailVO : detailTList) {
				sendMessageCC(resultMasterVO, detailVO.getRecvUserId(), detailVO.getRecvType(), detailVO.getRecvUserName(), detailVO.getRecvPhone(), detailVO.getRecvEmail());
			}

		} else { // 수신자 발송

			// 발송순서
			if (sendOrd.equals("A")) {
				for (DocumentDetailVO detailVO : detailRList) {
					sendMessageSign(resultMasterVO, detailVO.getRecvUserId(), detailVO.getRecvType(), detailVO.getRecvUserName(), detailVO.getRecvPhone(), detailVO.getRecvEmail());
				}
			} else {
				for (DocumentDetailVO detailVO : detailRList) {
					if (detailVO.getRecvOrd().equals("1")) {
						sendMessageSign(resultMasterVO, detailVO.getRecvUserId(), detailVO.getRecvType(), detailVO.getRecvUserName(), detailVO.getRecvPhone(), detailVO.getRecvEmail());
					}
				}
			}

			for (DocumentDetailVO detailVO : detailTList) {
				sendMessageCC(resultMasterVO, detailVO.getRecvUserId(), detailVO.getRecvType(), detailVO.getRecvUserName(), detailVO.getRecvPhone(), detailVO.getRecvEmail());
			}
		}

		for (int i=0; i<detailRList.size(); i++) {
			// 포인트 차감
			pointVO.setCurPoint(reducePoint);
			pointDAO.balancePoint(pointVO);

			PointLogVO pointLogVO = new PointLogVO();
			pointLogVO.setBizId(masterVO.getBizId());
			pointLogVO.setUserId(masterVO.getUserId());
			pointLogVO.setPointType("2");
			pointLogVO.setPointPrice(pointVO.getCurPoint());
			pointLogVO.setPointResult(resultPointVO.getCurPoint()-pointVO.getCurPoint());
			pointLogVO.setEtcDesc("전자계약 전송 포인트 차감");
			logger.info("전자계약 전송 <"+reducePoint+">포인트 차감");

			pointDAO.insPointLog(pointLogVO);
		}

		for (int i=0; i<detailTList.size(); i++) {
			// 포인트 차감
			pointVO.setCurPoint(reducePoint);
			pointDAO.balancePoint(pointVO);

			PointLogVO pointLogVO = new PointLogVO();
			pointLogVO.setBizId(masterVO.getBizId());
			pointLogVO.setUserId(masterVO.getUserId());
			pointLogVO.setPointType("2");
			pointLogVO.setPointPrice(pointVO.getCurPoint());
			pointLogVO.setPointResult(resultPointVO.getCurPoint()-pointVO.getCurPoint());
			pointLogVO.setEtcDesc("전자계약 전송 포인트 차감");
			logger.info("전자계약 전송 <"+reducePoint+">포인트 차감");

			pointDAO.insPointLog(pointLogVO);
		}
	}

	private boolean sendMessageCC(DocumentMasterVO vo, String recvUserId, String recvType, String userName, String phone_number, String email_address) throws Exception {

		if (vo.getSendType().equals("E") && StringUtil.isNull(email_address)) 	  logger.error(userName + "님의 이메일정보가 없습니다.");
		else if (vo.getSendType().equals("K") && StringUtil.isNull(phone_number)) logger.error(userName + "님의 휴대폰정보가 없습니다.");

		String linkURL = vo.getDomainName() + "/document/recvDocument.do?id=" + vo.getDigitNonce() + "_" + recvUserId + "_" + recvType;

		if (vo.getSendType().equals("E")) {

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
			content += "				<span style=\"font-weight: bold; color:#00a9e9;\">전자계약</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;</span> ";
			content += "			</div> ";
			content += "			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
			content += "				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">"+vo.getDocuName()+"</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br> ";
			content += "				발송 이메일입니다.</span> ";
			content += "			</div> ";
			content += "			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
			content += "				<span >" + userName + "</span><span>님!<br> ";
			content += "				하단의 '전자계약' 버튼을 클릭하여 전자문서를 확인 해주시기 바랍니다.<br> ";
			content += "				전달해드린 전자문서의 참조자로 지정되어 해당 문서를 확인하실 수 있습니다.<br> ";
			content += "				</span> ";
			content += "			</div> ";
			content += "			<div style=\"margin: 74px 0 32px; text-align: center;\"> ";
			content += "				<a href=\"" + linkURL + "\" style=\"display: inline-block; padding: 12px 90px; border-radius: 8px; font-family: 'tahoma'; font-size: 18px; font-weight: bold; text-decoration: none; color: #fff; background-color: #00a9e9;\">전자문서</a>";
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

			MultiPartEmail email = new MultiPartEmail();
			MailVO mailVO = new MailVO();
			mailVO.setFrom("no_reply@newzenpnp.com");
			mailVO.setTo(email_address);
			mailVO.setCc("");
			mailVO.setBcc("");
			mailVO.setSubject(userName + "님의 전자계약서 참조가 도착하였습니다.");
			mailVO.setText(content);

			logger.info("전자계약서비스 이메일을 발송. email : " + email_address + " digitNonce : " + vo.getDigitNonce());

			email.send(mailVO);
		} else if (vo.getSendType().equals("K")) {

			// (알림톡 전송)완료된 전자문서 재발송
			String content = "[전자계약서비스 * 참조]\n\n";
			content += userName + "님이 참조된 전자계약서가 도착했습니다.\n\n";
			content += "아래의 링크를 클릭하여 전자계약서를 확인하세요.\n\n";
			content += linkURL;

			String url = "https://mms.ezsign.co.kr/biztalk/sendMMS.do";

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("bizId", "171129192253020"));
			nvps.add(new BasicNameValuePair("sendType", "003"));
			nvps.add(new BasicNameValuePair("subject", ""));
			nvps.add(new BasicNameValuePair("content", content));
			nvps.add(new BasicNameValuePair("recipientNum", phone_number));
			nvps.add(new BasicNameValuePair("encType", "0"));
			nvps.add(new BasicNameValuePair("senderKey", System.getProperty("biztalk.kko.sender_key")));
			nvps.add(new BasicNameValuePair("templateCode", "openplatform007"));
			nvps.add(new BasicNameValuePair("kkoBtnType", "1"));
			nvps.add(new BasicNameValuePair("kkoBtnInfo", "전자계약서^WL^" + linkURL + "^" + linkURL));
			// UTF-8은 한글
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				logger.info("Send MMS ==>" + phone_number + "/" + response.getStatusLine().getStatusCode());
				// API서버로부터 받은 JSON 문자열 데이터
				logger.info(EntityUtils.toString(response.getEntity()));
				HttpEntity entity = response.getEntity();
				EntityUtils.consume(entity);

				logger.info("전자계약서비스 카카오톡 발송. PHONE NUM : " + phone_number + "/ response code : " + response.getStatusLine().getStatusCode());
				logger.info(content);
				if (response.getStatusLine().getStatusCode() == 200) logger.info("전자문서 발송 성공");

			} finally { response.close(); }
		}
		return true;
	}

	private boolean sendMessageReview(DocumentMasterVO vo, String recvUserId, String recvType, String userName, String phone_number, String email_address) throws Exception {

		if (vo.getSendType().equals("E") && StringUtil.isNull(email_address)) 	  logger.error(userName + "님의 이메일정보가 없습니다.");
		else if (vo.getSendType().equals("K") && StringUtil.isNull(phone_number)) logger.error(userName + "님의 휴대폰정보가 없습니다.");

		String linkURL = vo.getDomainName() + "/document/recvDocument.do?id=" + URLEncoder.encode(vo.getDigitNonce() + "_" + recvUserId + "_" + recvType);

		logger.info("linkURL : " + linkURL);

		if (vo.getSendType().equals("E")) {

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
			content += "				<span style=\"font-weight: bold; color:#00a9e9;\">전자계약</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;</span> ";
			content += "			</div> ";
			content += "			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
			content += "				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">"+vo.getDocuName()+" 검토요청</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br> ";
			content += "				발송 이메일입니다.</span> ";
			content += "			</div> ";
			content += "			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
			content += "				<span >" + userName + "</span><span>님!<br> ";
			content += "				하단의 '전자계약' 버튼을 클릭하여 전자문서를 검토해주세요.<br> ";
			content += "				문서의 내용이 협의된 사실과 다를 시 정정요청을 클릭해 정정요청 사유를 입력해주세요.<br> ";
			content += "				</span> ";
			content += "			</div> ";
			content += "			<div style=\"margin: 74px 0 32px; text-align: center;\"> ";
			content += "				<a href=\"" + linkURL + "\" style=\"display: inline-block; padding: 12px 90px; border-radius: 8px; font-family: 'tahoma'; font-size: 18px; font-weight: bold; text-decoration: none; color: #fff; background-color: #00a9e9;\">전자계약</a>";
			content += "			</div> ";
			content += "		</div> ";
			content += "		<div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
			content += "			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
			content += "				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
			content += "				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:servicer@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">service@newzenpnp.com</a></span > ";
			content += "				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
			content += "			</div> ";
			content += "		</div> ";
			content += "	</div> ";
			content += "</body> ";
			content += "</html> ";

			MultiPartEmail email = new MultiPartEmail();
			MailVO mailVO = new MailVO();
			mailVO.setFrom("no_reply@newzenpnp.com");
			mailVO.setTo(email_address);
			mailVO.setCc("");
			mailVO.setBcc("");
			mailVO.setSubject(userName + "님, 전자계약 검토요청이 도착하였습니다.");
			mailVO.setText(content);

			logger.info("전자계약서비스 이메일을 발송. email : " + email_address + " digitNonce : " + vo.getDigitNonce());
			email.send(mailVO);
		} else if (vo.getSendType().equals("K")) {

			String content = "[전자계약서비스 *  검토 요청]\n\n";
			content += userName + " 님의 전자계약서가 도착했습니다.\n";
			content += "전자계약서의 내용을 확인하신 후 검토를 진행해주시기 바랍니다.\n\n";
			content += "문서의 내용이 협의된 사실과 다를 시 정정요청를 클릭하여 정정사유를 작성해주시기 바랍니다.\n\n";
			content += "아래의 링크를 클릭하여 전자계약서를 확인하세요.\n\n";

			content += linkURL;

			String url = "https://mms.ezsign.co.kr/biztalk/sendMMS.do";
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("bizId", "171129192253020"));
			nvps.add(new BasicNameValuePair("sendType", "003"));
			nvps.add(new BasicNameValuePair("subject", ""));
			nvps.add(new BasicNameValuePair("content", content));
			nvps.add(new BasicNameValuePair("recipientNum", phone_number));
			nvps.add(new BasicNameValuePair("encType", "0"));
			nvps.add(new BasicNameValuePair("senderKey", System.getProperty("biztalk.kko.sender_key")));
			nvps.add(new BasicNameValuePair("templateCode", "openplatform006"));
			nvps.add(new BasicNameValuePair("kkoBtnType", "1"));
			nvps.add(new BasicNameValuePair("kkoBtnInfo", "전자계약서^WL^" + linkURL + "^" + linkURL));
			// UTF-8은 한글
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				logger.info("Send MMS ==>" + phone_number + "/" + response.getStatusLine().getStatusCode());
				// API서버로부터 받은 JSON 문자열 데이터
				logger.info(EntityUtils.toString(response.getEntity()));
				HttpEntity entity = response.getEntity();
				EntityUtils.consume(entity);

				logger.info("전자계약서비스 카카오톡 발송. PHONE NUM : " + phone_number + "/ response code : " + response.getStatusLine().getStatusCode());
				logger.info(content);
				if (response.getStatusLine().getStatusCode() == 200) logger.info("전자문서 발송 성공");
			} finally { response.close(); }
		}
		return true;
	}

	private boolean sendMessageSign(DocumentMasterVO vo, String recvUserId, String recvType, String userName, String phone_number, String email_address) throws Exception {

		if (vo.getSendType().equals("E") && StringUtil.isNull(email_address)) {
			logger.error(userName + "님의 이메일정보가 없습니다.");
			throw new Exception(userName + "님의 이메일정보가 없습니다.");
		} else if (vo.getSendType().equals("K") && StringUtil.isNull(phone_number)) {
			logger.error(userName + "님의 휴대폰정보가 없습니다.");
			throw new Exception(userName + "님의 휴대폰정보가 없습니다.");
		}

		String linkURL = vo.getDomainName() + "/document/recvDocument.do?id=" + URLEncoder.encode(vo.getDigitNonce() + "_" + recvUserId + "_" + recvType);

		if (vo.getSendType().equals("E")) {

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
			content += "				<span style=\"font-weight: bold; color:#00a9e9;\">전자계약</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;</span> ";
			content += "			</div> ";
			content += "			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
			content += "				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">"+vo.getDocuName()+" 서명요청</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br> ";
			content += "				발송 이메일입니다.</span> ";
			content += "			</div> ";
			content += "			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
			content += "				<span >" + userName + "</span><span>님!<br> ";
			content += "				하단의 '전자계약' 버튼을 클릭하여 전자문서에 서명을 진행해주세요.<br> ";
			content += "				문서의 내용이 협의된 사실과 다를 시 정정요청을 클릭해 정정요청 사유를 입력해주세요.<br> ";
			content += "				</span> ";
			content += "			</div> ";
			content += "			<div style=\"margin: 74px 0 32px; text-align: center;\"> ";
			content += "				<a href=\"" + linkURL + "\" style=\"display: inline-block; padding: 12px 90px; border-radius: 8px; font-family: 'tahoma'; font-size: 18px; font-weight: bold; text-decoration: none; color: #fff; background-color: #00a9e9;\">전자계약</a>";
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

			MultiPartEmail email = new MultiPartEmail();
			MailVO mailVO = new MailVO();
			mailVO.setFrom("no_reply@newzenpnp.com");
			mailVO.setTo(email_address);
			mailVO.setCc("");
			mailVO.setBcc("");
			mailVO.setSubject(userName + "님의 전자계약 서명요청이 도착하였습니다.");
			mailVO.setText(content);

			logger.info("전자계약서비스 이메일을 발송. email : " + email_address + " digitNonce : " + vo.getDigitNonce());
			email.send(mailVO);
		} else if (vo.getSendType().equals("K")) {

			String content = "[전자계약서비스 * 서명 요청]\n\n";
			content += userName + " 님의 전자계약서가 도착했습니다.\n";
			content += "전자계약서의 내용을 확인하신 후 서명을 진행해주세요.\n";
			content += "문서의 내용이 협의된 사실과 다를 시 정정요청를 클릭하여 정정사유를 작성해주시기 바랍니다.\n\n";
			content += "최종 전자계약서는 전자서명이 완료된 후 알림톡을 통해 배부됩니다.\n\n";
			content += "하단의 링크를 클릭하여 전자계약서를 확인하세요.\n\n";
			content += linkURL;
			String url = "https://mms.ezsign.co.kr/biztalk/sendMMS.do";

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("bizId", "171129192253020"));
			nvps.add(new BasicNameValuePair("sendType", "003"));
			nvps.add(new BasicNameValuePair("subject", ""));
			nvps.add(new BasicNameValuePair("content", content));
			nvps.add(new BasicNameValuePair("recipientNum", phone_number));
			nvps.add(new BasicNameValuePair("encType", "0"));
			nvps.add(new BasicNameValuePair("senderKey", System.getProperty("biztalk.kko.sender_key")));
			nvps.add(new BasicNameValuePair("templateCode", "openplatform005"));
			nvps.add(new BasicNameValuePair("kkoBtnType", "1"));
			nvps.add(new BasicNameValuePair("kkoBtnInfo", "전자계약서^WL^" + linkURL + "^" + linkURL));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				logger.info("Send MMS ==>" + phone_number + "/" + response.getStatusLine().getStatusCode());
				logger.info(EntityUtils.toString(response.getEntity()));
				HttpEntity entity = response.getEntity();
				EntityUtils.consume(entity);

				logger.info("전자계약서비스 카카오톡 발송. PHONE NUM : " + phone_number + "/ response code : " + response.getStatusLine().getStatusCode());
				logger.info(content);
				if (response.getStatusLine().getStatusCode() == 200) logger.info("전자문서 발송 성공");
			} finally { response.close(); }
		}
		return true;
	}

	private boolean sendMessageComplete(ContentVO contentVO,DocumentMasterVO vo, String recvUserId, String recvType, String userName, String phone_number, String email_address) throws Exception {

		if (vo.getSendType().equals("E") && StringUtil.isNull(email_address)) 	  logger.error(userName + "님의 이메일정보가 없습니다.");
		else if (vo.getSendType().equals("K") && StringUtil.isNull(phone_number)) logger.error(userName + "님의 휴대폰정보가 없습니다.");

		String linkURL = vo.getDomainName() + System.getProperty("biztalk.kko.open.document_download_url") + "?fileId="+vo.getConFileId();

		// PDF 링크 연결 URL
		String tempPath = StringUtil.ReplaceAll(contentVO.getFilePath(), MultipartFileUtil.getSystemPath(), "");
		String linkURL2 = vo.getDomainName() + System.getProperty("system.pdfViewer.path")+"?file=/"+tempPath;

		if (vo.getSendType().equals("E")) {

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
			content += "				<span style=\"font-weight: bold; color:#00a9e9;\">전자계약</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;</span> ";
			content += "			</div> ";
			content += "			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
			content += "				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">"+vo.getDocuName()+" </span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br> ";
			content += "				완료계약서 발송 이메일입니다.</span> ";
			content += "			</div> ";
			content += "			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
			content += "				<span >" + userName + "</span><span>님!<br> ";
			content += "				하단의 '전자계약' 버튼을 클릭하여 전자문서를 다운로드 해주시기 바랍니다.<br> ";
			content += "				전달해드린 전자문서는 법적으로 보장되는 파일이므로 다운로드 하여 보관해주시기 바랍니다.<br> ";
			content += "				</span> ";
			content += "			</div> ";
			content += "			<div style=\"margin: 74px 0 32px; text-align: center;\"> ";
			content += "				<a href=\"" + linkURL + "\" style=\"display: inline-block; padding: 12px 90px; border-radius: 8px; font-family: 'tahoma'; font-size: 18px; font-weight: bold; text-decoration: none; color: #fff; background-color: #00a9e9;\">전자계약</a>";
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

			MultiPartEmail email = new MultiPartEmail();
			MailVO mailVO = new MailVO();
			mailVO.setFrom("no_reply@newzenpnp.com");
			mailVO.setTo(email_address);
			mailVO.setCc("");
			mailVO.setBcc("");
			mailVO.setSubject(userName + "님의 완료 전자계약서 원본이 도착하였습니다.");
			mailVO.setText(content);

			logger.info("전자계약서비스 이메일을 발송. email : " + email_address + " digitNonce : " + vo.getDocuName());
			email.send(mailVO);
		} else if (vo.getSendType().equals("K")) {

			// (알림톡 전송)완료된 전자문서 재발송
			String content = "[전자계약서비스 * 전자계약서]\n\n";
			content += userName + "님의 전자계약서가(이) 도착했습니다.\n\n";
			content += "본 계약서는 뉴젠피앤피의 전자계약서비스에서 " + userName + "님에게 발송한 최종 전자계약서입니다.\n\n";
			content += "하단의 링크를 클릭하여 최종 전자계약서를 다운로드 받아 보관하세요.\n\n";
			content += "저장 방법 안내\n\n";
			content += "1. Android 폰의 경우";
			content += "\n";
			content += System.getProperty("system.feb.url") + "manual/android.html";
			content += " \n\n";
			content += "2. 아이폰의 경우";
			content += "\n";
			content += System.getProperty("system.feb.url") + "manual/ios.html";
			content += "\n\n";
			content += "최종 전자계약서 미리보기";
			content += "\n";
			content += linkURL2;
			content += "\n\n";
			content += "최종 전자계약서 다운로드";
			content += "\n";
			content += linkURL;
			content += " \n\n";
			String url = "https://mms.ezsign.co.kr/biztalk/sendMMS.do";

			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("bizId", "171129192253020"));
			nvps.add(new BasicNameValuePair("sendType", "003"));
			nvps.add(new BasicNameValuePair("subject", ""));
			nvps.add(new BasicNameValuePair("content", content));
			nvps.add(new BasicNameValuePair("recipientNum", phone_number));
			nvps.add(new BasicNameValuePair("encType", "0"));
			nvps.add(new BasicNameValuePair("senderKey", System.getProperty("biztalk.kko.sender_key")));
			nvps.add(new BasicNameValuePair("templateCode", "openplatform003"));
			nvps.add(new BasicNameValuePair("kkoBtnType", "1"));
			nvps.add(new BasicNameValuePair("kkoBtnInfo", "최종 전자계약서^WL^" + linkURL + "^" + linkURL));
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			CloseableHttpResponse response = httpclient.execute(httpPost);
			try {
				logger.info("Send MMS ==>" + phone_number + "/" + response.getStatusLine().getStatusCode());
				logger.info(EntityUtils.toString(response.getEntity()));
				HttpEntity entity = response.getEntity();
				EntityUtils.consume(entity);

				logger.info("전자계약서비스 카카오톡 발송. PHONE NUM : " + phone_number + "/ response code : " + response.getStatusLine().getStatusCode());
				logger.info(content);
				if (response.getStatusLine().getStatusCode() == 200) logger.info("전자문서 발송 성공");
			} finally { response.close(); }
		}
		return true;
	}

	// 뉴젠피앤피 관리자에게 전송
	private boolean sendMessageMasterComplete(DocumentMasterVO vo) throws Exception {
		String email_address = "service@newzenpnp.com";
		String linkURL = vo.getDomainName() + "/html/document/document_popup_add_complete.html?bizId="+vo.getBizId()+"&docuId="+vo.getDocuId();

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
		content += "				<span style=\"font-weight: bold; color:#00a9e9;\">전자계약</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;</span> ";
		content += "			</div> ";
		content += "			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
		content += "				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">"+vo.getDocuName()+" </span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br> ";
		content += "				계약서 작업 발송 이메일입니다.</span> ";
		content += "			</div> ";
		content += "			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
		content += "				하단의 '전자계약' 버튼을 클릭하여 전자양식파일을 등록해주시기 바랍니다.<br> ";
		content += "				</span> ";
		content += "			</div> ";
		content += "			<div style=\"margin: 74px 0 32px; text-align: center;\"> ";
		content += "				<a href=\"" + linkURL + "\" style=\"display: inline-block; padding: 12px 90px; border-radius: 8px; font-family: 'tahoma'; font-size: 18px; font-weight: bold; text-decoration: none; color: #fff; background-color: #00a9e9;\">전자문서등록</a>";
		content += "			</div> ";
		content += "		</div> ";
		content += "	</div> ";
		content += "</body> ";
		content += "</html> ";

		MultiPartEmail email = new MultiPartEmail();
		MailVO mailVO = new MailVO();
		mailVO.setFrom("no_reply@newzenpnp.com");
		mailVO.setTo(email_address);
		mailVO.setCc("");
		mailVO.setBcc("");
		mailVO.setSubject("["+vo.getUserName() + "] 전자문서("+vo.getDocuName()+") 원본변환요청이 왔습니다.");
		mailVO.setText(content);

		logger.info("전자계약서비스 이메일을 발송. email : " + email_address + " digitNonce : " + vo.getDocuName());
		email.send(mailVO);
		return true;
	}

	// 요청자 문서생성
	@Override
	public boolean saveDocument(DocumentRequestVO vo, List<FileVO> fileList) throws Exception {

		switch (vo.dbMode) {
			case "C": {
				String digitNonce = StringUtil.getRandom(15);
				vo.documentMaster.setDigitNonce(digitNonce);

				addDocument(vo.documentMaster, vo.documentDetail, fileList, vo.documentField, vo.documentWord);
				vo.documentMaster.setDomainName(vo.getDomainName());

				// 문서발송
				if (vo.documentMaster.getDocuStatus().equals("1")) sendDocument(vo.documentMaster);

				break;
			}
			case "U": {
				DocumentMasterVO masterVO = new DocumentMasterVO();
				// 수정가능여부 체크
				DocumentMasterVO searchMasterVO = new DocumentMasterVO();
				searchMasterVO.setBizId(vo.documentMaster.getBizId());
				searchMasterVO.setDocuId(vo.documentMaster.getDocuId());
				searchMasterVO.setStartPage(0);
				searchMasterVO.setEndPage(1);
				List<DocumentMasterVO> masterList = documentDAO.getDocumentMasterList(searchMasterVO);
				if (masterList.isEmpty()) {
					logger.error("[saveDocument] 계약정보가 존재하지 않습니다.");
					throw new Exception("[saveDocument] 계약정보가 존재하지 않습니다.");
				}
				masterVO = masterList.get(0);

				String digitNonce = StringUtil.getRandom(15);
				vo.documentMaster.setDigitNonce(digitNonce);

				updDocument(vo.documentMaster, vo.documentDetail, vo.documentFile, fileList, vo.documentField, vo.documentWord);
				vo.documentMaster.setDomainName(vo.getDomainName());
				// 문서발송
				if (vo.documentMaster.getDocuStatus().equals("1")) sendDocument(vo.documentMaster);
				break;
			}
			case "D":
				// 삭제가능여부 체크
				DocumentDetailVO searchDetailVO = new DocumentDetailVO();
				String[] docuIdList = vo.documentMaster.getDocuId().split("-");

				for (String docuid : docuIdList) {
					searchDetailVO.setDocuId(docuid);
					searchDetailVO.setRecvType("R");
					vo.documentMaster.setDocuId(docuid);

					List<DocumentDetailVO> searchDetailList = documentDAO.getDocumentDetailList(searchDetailVO);
					// 서명완료건 여부
					int nSignCount = 0;
					for (DocumentDetailVO detailVO : searchDetailList) {
						if (detailVO.getRecvStatus().equals("6")) nSignCount++;
					}
					if (nSignCount == searchDetailList.size()) {
						vo.code = "-900";
						vo.message = "이미 완료된 계약서입니다.";
						return false;
					} else if (nSignCount > 0) {
						vo.code = "-100";
						vo.message = "진행중인 전자계약서입니다.";
						return false;
					}
					delDocument(vo.documentMaster);
				}
				break;
		}
		vo.code = "0";
		vo.message = "처리되었습니다.";
		return true;
	}

	// 수신자 처리액션
	@Override
	public boolean forwardDocument(DocumentDetailVO vo, List<FileVO> fileList) throws Exception {
		DocumentMasterVO resultMasterVO = new DocumentMasterVO();
		String docuId 					= vo.getDocuId();
		String userId 					= vo.getRecvUserId();
		String signData 				= vo.getRecvSign();
		String complateTSAFileId 		= "";

		logger.info("=============== forwardDocument call =============");
		logger.info("docuId : " + docuId);
		logger.info("userId : " + userId);
		logger.info("signData : " + signData);
		logger.info("=============== forwardDocument call =============");

		// 계약서정보조회
		DocumentMasterVO searchMasterVO = new DocumentMasterVO();
		searchMasterVO.setBizId(vo.getBizId());
		searchMasterVO.setDocuId(docuId);
		searchMasterVO.setStartPage(0);
		searchMasterVO.setEndPage(1);
		List<DocumentMasterVO> masterList = documentDAO.getDocumentMasterList(searchMasterVO);
		if (masterList.isEmpty()) {
			logger.error("[forwardDocument] 계약정보가 존재하지 않습니다.");
			throw new Exception("[forwardDocument] 계약정보가 존재하지 않습니다.");
		}
		resultMasterVO = masterList.get(0);
		resultMasterVO.setDomainName(vo.getDomainName());
		// 현재 처리가능한 상태인지 확인
		String docuStatus = resultMasterVO.getDocuStatus();
		if (docuStatus.equals("9")) {
			vo.setCode("-900");
			vo.setMessage("이미 완료된 문서입니다.");
			throw new Exception("이미 완료된 문서입니다.");
		} else if (docuStatus.equals("2")) {
			vo.setCode("-901");
			vo.setMessage("이미 반려된 문서입니다.");
			throw new Exception("이미 반려된 문서입니다.");
		}

		// 사용자 진행상태
		String recvStatus  = vo.getRecvStatus();
		// digitNonce
		String digitNonce  = resultMasterVO.getDigitNonce();
		// 발송수단(E, K)
		String sendType    = resultMasterVO.getSendType();
		// 발송순서(A, S)
		String sendOrd     = resultMasterVO.getSendOrd();
		// 검토여부(Y, N)
		String reviewType  = resultMasterVO.getReviewType();
		// 인사 / 거래처 구분(P, C)
		String contactType = vo.getContactType();

		// 수신자 대상리스트
		DocumentDetailVO searchDetailVO = new DocumentDetailVO();
		searchDetailVO.setDocuId(docuId);
		searchDetailVO.setRecvType("R");
		searchDetailVO.setSortName("B.RECV_ORD");
		searchDetailVO.setSortOrder("ASC");
		List<DocumentDetailVO> detailRList = documentDAO.getDocumentDetailList(searchDetailVO);
		int nReviewCount = 0, nSignCount   = 0, nTotal = detailRList.size();
		DocumentDetailVO detailUserVO 	   = null;
		DocumentDetailVO detailNextUserVO  = null;
		for (DocumentDetailVO detailVO : detailRList) {
			if (detailUserVO != null && detailNextUserVO == null) detailNextUserVO = detailVO;

			// 현재 사용자 상태정보
			if (detailVO.getRecvUserId().equals(userId)) {
				detailUserVO = detailVO;
			} else {
				// 모든 사용자에 대해서 검토, 서명 count 수 계산
				if 		(detailVO.getRecvStatus().equals("4")) nReviewCount++;	// 검토
				else if (detailVO.getRecvStatus().equals("9")) nSignCount++; 	// 서명
			}
		}

		//현재 사용자 상태코드 체크
		if (detailUserVO != null) {
			//수신자가 검토반려 또는 검토완료 상태일 경우
			if (vo.getRecvStatus().equals("3") || vo.getRecvStatus().equals("4")) {
				//문서가 반려 또는 검토완료 상태일 경우
				if (!(resultMasterVO.getDocuStatus().equals("1") || resultMasterVO.getDocuStatus().equals("4"))) {
					vo.setCode("-902");
					vo.setMessage("이미 완료된 문서입니다.");
					throw new Exception("이미 완료된 문서입니다.");
				}

				if (detailUserVO.getRecvStatus().equals("3") || detailUserVO.getRecvStatus().equals("4")) {
					vo.setCode("-904");
					vo.setMessage("이미 완료된 문서입니다.");
					throw new Exception("이미 완료된 문서입니다.");
				}
			}

			//수신자가 검토반려 또는 검토완료 상태일 경우
			if (detailUserVO.getRecvStatus().equals("8") || detailUserVO.getRecvStatus().equals("9")) {
				if (vo.getRecvStatus().equals("8") || vo.getRecvStatus().equals("9")) {
					//문서가 반려 또는 검토완료 상태일 경우
					vo.setCode("-903");
					vo.setMessage("이미 완료된 문서입니다.");
					throw new Exception("이미 완료된 문서입니다.");
				}
			}
		}

		// 수신자 상태변경
		DocumentDetailVO updDetailVO = new DocumentDetailVO();
		updDetailVO.setDocuId(docuId);
		updDetailVO.setRecvType("R");
		updDetailVO.setRecvUserId(userId);
		updDetailVO.setConfirmDate(DateUtil.getTimeStamp(7));
		updDetailVO.setRecvStatus(recvStatus);
		updDetailVO.setRecvSign(signData);
		documentDAO.updDocumentDetail(updDetailVO);

		// 참조자 대상리스트
		searchDetailVO = new DocumentDetailVO();
		searchDetailVO.setDocuId(docuId);
		searchDetailVO.setRecvType("T");
		List<DocumentDetailVO> detailTList = documentDAO.getDocumentDetailList(searchDetailVO);

		String logType = "D00";
		String logMessage = vo.getRecvMessage();
		switch (vo.getRecvStatus()) {
			case "3":	 // 검토반려
				docuStatus = "3";
				logType = "D25";
				break;
			case "8":	// 서명반려
				docuStatus = "3";
				logType = "D55";
				break;
			case "4":	// 검토완료
				docuStatus = "4";
				logType = "D30";
				logger.info("총 수신자 수 : " + nTotal + ", 총 검토완료자 수 : " + nReviewCount);

				if (nTotal - 1 == nReviewCount) { // => 검토가 전부 완료된 상태
					if (reviewType.equals("S")) docuStatus = "6";
					else {
						// 서명요청 상태변경
						docuStatus = "7";
						// 수신자 발송
						if (sendOrd.equals("A")) { // => 검토가 완료된 상태로 수신자에게 일괄 발송
							// 전체발송
							for (DocumentDetailVO detailRVO : detailRList) {
								sendMessageSign(resultMasterVO, detailRVO.getRecvUserId(), detailRVO.getRecvType(), detailRVO.getRecvUserName(), detailRVO.getRecvPhone(), detailRVO.getRecvEmail());
							}
						} else {
							// 수신자#1 발송
							if (!detailRList.isEmpty()) {
								DocumentDetailVO detailRVO = detailRList.get(0);
								sendMessageSign(resultMasterVO, detailRVO.getRecvUserId(), detailRVO.getRecvType(), detailRVO.getRecvUserName(), detailRVO.getRecvPhone(), detailRVO.getRecvEmail());
							}
						}

						// 참조자 전체 발송
						for (DocumentDetailVO detailTVO : detailTList) {
							sendMessageCC(resultMasterVO, detailTVO.getRecvUserId(), detailTVO.getRecvType(), detailTVO.getRecvUserName(), detailTVO.getRecvPhone(), detailTVO.getRecvEmail());
						}
					}
				} else {
					// 다음 수신자 검토발송
					if (sendOrd.equals("S") && detailNextUserVO != null) {
						sendMessageReview(resultMasterVO, detailNextUserVO.getRecvUserId(), detailNextUserVO.getRecvType(), detailNextUserVO.getRecvUserName(), detailNextUserVO.getRecvPhone(), detailNextUserVO.getRecvEmail());
					}
				}
				break;
			case "9":
				// 서명완료
				docuStatus = "8";
				logType = "D60";

				if (resultMasterVO.getSignType().equals("T") || resultMasterVO.getSignType().equals("C") || resultMasterVO.getSignType().equals("TC")) {
					if (contactType.equals("P")) 	  logMessage = "수기서명 으로 서명하였습니다.";
					else if (contactType.equals("C")) logMessage = "직인 으로 서명하였습니다.";
				}

				if (nTotal - 1 == nSignCount) {
					// 서명완료 상태변경
					docuStatus = "9";

					ContentVO searchContentVO = new ContentVO();
					searchContentVO.setFileId(resultMasterVO.getOrgFileId());
					ContentVO resultContentVO = contentService.getContent(searchContentVO);

					if (resultContentVO == null || StringUtil.isNull(resultContentVO.getFileTitle())) {
						throw new Exception("[forwardDocument] 문서정보를 가져오지 못했습니다.");
					}

					String targetPdfPath = resultContentVO.getFileTitle();
					String targetTsaName = StringUtil.getUUID() + ".pdf";
					String targetTsaPath = MultipartFileUtil.getSystemTempPath() + targetTsaName;
					String targetPdfName = targetTsaName;

					// 수기서명, 직인, 수기서명/직인 일 경우
					if (resultMasterVO.getSignType().equals("T") || resultMasterVO.getSignType().equals("C") || resultMasterVO.getSignType().equals("TC")) {

						String targetSignPath = MultipartFileUtil.getSystemTempPath() + StringUtil.getUUID() + "_X.pdf";
						logger.info("[forwardDocument] targetSignPath : " + targetSignPath);

						searchDetailVO = new DocumentDetailVO();
						searchDetailVO.setDocuId(docuId);
						searchDetailVO.setRecvType("R");
						searchDetailVO.setSortName("B.RECV_ORD");
						searchDetailVO.setSortOrder("ASC");
						detailRList = documentDAO.getDocumentDetailList(searchDetailVO);

						saveDocumentFormData(resultMasterVO, detailRList, targetPdfPath, targetSignPath);

						targetPdfPath = targetSignPath;
						targetPdfName = FileUtil.getRealName(targetSignPath);
					}


					// TSA발급
					if (resultMasterVO.getTsaType().equals("Y")) {

						TSAUtil.timestampPdf(targetPdfPath, targetTsaPath, true, false, "newzenpnp", "");
						long tsaFileSize = FileUtil.getFileSize(targetTsaPath);
						if (tsaFileSize < 10 * 1024) {
							throw new Exception("[forwardDocument] 문서위변조(TSA) 발급중에 문제가 발생하였습니다");
						} else {
							targetPdfPath = targetTsaPath;
							targetPdfName = targetTsaName;
						}

						// 로그생성
						DocumentLogVO insLogVO = new DocumentLogVO();
						insLogVO.setDocuId(vo.getDocuId());
						insLogVO.setLogType("L80");
						insLogVO.setLogMessage("계약서에 문서위변조방지가 생성되었습니다");
						insLogVO.setWorkUserId(userId);
						insLogVO.setFileId(resultMasterVO.getOrgFileId());
						documentDAO.insDocumentLog(insLogVO);
					}

					ContentVO contentVO = new ContentVO();
					contentVO.setFileType(FileUtil.getFileExt(targetPdfPath));
					contentVO.setFileName(targetPdfName);
					contentVO.setOrgFileName(resultMasterVO.getDocuName() + ".pdf");
					contentVO.setFileSize(Long.toString(FileUtil.getFileSize(targetPdfPath)));
					contentVO.setFilePath(MultipartFileUtil.getSystemTempPath());
					contentVO.setFileTitle(resultMasterVO.getDocuName());
					contentVO.setClassId(this.DOCU_CON_ID);
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
						docuStatus = "Z";
						resultMasterVO.setConFileId(contentVO.getFileId());
						for (DocumentDetailVO detailRVO : detailRList) {
							if (resultMasterVO.getConFileId() == null || "".equals(resultMasterVO.getConFileId())) {
								resultMasterVO.setConFileId(resultMasterVO.getOrgFileId());
							}
							contentVO.setFilePath(targetPdfPath);
							sendMessageComplete(contentVO, resultMasterVO, detailRVO.getRecvUserId(), detailRVO.getRecvType(), detailRVO.getRecvUserName(), detailRVO.getRecvPhone(), detailRVO.getRecvEmail());
						}

						//완료시 참조자 에게 전송
						for (DocumentDetailVO detailRVO : detailTList) {
							if (resultMasterVO.getConFileId() == null || "".equals(resultMasterVO.getConFileId())) {
								resultMasterVO.setConFileId(resultMasterVO.getOrgFileId());
							}
							contentVO.setFilePath(targetPdfPath);
							sendMessageComplete(contentVO, resultMasterVO, detailRVO.getRecvUserId(), detailRVO.getRecvType(), detailRVO.getRecvUserName(), detailRVO.getRecvPhone(), detailRVO.getRecvEmail());
						}

						// 로그생성
						DocumentLogVO insLogVO = new DocumentLogVO();
						insLogVO.setDocuId(vo.getDocuId());
						insLogVO.setLogType("L90");
						insLogVO.setLogMessage("완료계약서를 생성하였습니다.");
						insLogVO.setWorkUserId(userId);
						insLogVO.setFileId(contentVO.getFileId());
						documentDAO.insDocumentLog(insLogVO);

						complateTSAFileId = contentVO.getFileId(); // TSA 완료시 fileId 업데이트 변수 지정
					} else {
						throw new Exception("[forwardDocument] 문서위변조(TSA) 발급정보를 저장시 문제가 발생하였습니다.");
					}
				} else {
					// 순차발송 및 다음 수신자 발송
					if (sendOrd.equals("S") && detailNextUserVO != null) {
						sendMessageSign(resultMasterVO, detailNextUserVO.getRecvUserId(), detailNextUserVO.getRecvType(), detailNextUserVO.getRecvUserName(), detailNextUserVO.getRecvPhone(), detailNextUserVO.getRecvEmail());
					}
				}
				break;
		}

		// 계약마스터 상태 변경
		DocumentMasterVO updMasterVO = new DocumentMasterVO();
		updMasterVO.setDocuId(docuId);
		updMasterVO.setDocuStatus(docuStatus);
		updMasterVO.setBizId(vo.getBizId());

		// TSA발급시 계약서 아이디 수정
		if (resultMasterVO.getTsaType().equals("Y")) updMasterVO.setConFileId(complateTSAFileId);
		else 										 updMasterVO.setConFileId(resultMasterVO.getOrgFileId());

		updMasterVO.setCompleteDate(DateUtil.getTimeStamp(7));
		documentDAO.updDocumentMaster(updMasterVO);

		// 로그생성
		DocumentLogVO insLogVO = new DocumentLogVO();
		insLogVO.setDocuId(docuId);
		insLogVO.setLogType(logType);
		insLogVO.setLogMessage(logMessage);
		insLogVO.setWorkUserId(userId);
		documentDAO.insDocumentLog(insLogVO);

		// 파일(계약서, 첨부파일) 등록
		for (FileVO fileVO : fileList) {
			String calssId = this.DOCU_ATT_ID;
			String filePath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();

			File file = new File(filePath);
			if (!file.exists()) {
				logger.error("[forwardDocument]  파일이 존재하지 않습니다.");
				throw new Exception("[forwardDocument]  파일이 존재하지 않습니다.");
			}
			ContentVO contentVO = new ContentVO();
			contentVO.setFileType(FileUtil.getFileExt(filePath));
			contentVO.setFileName(fileVO.getFileStreNm());
			contentVO.setOrgFileName(fileVO.getFileStreOriNm());
			contentVO.setFileSize(Long.toString(FileUtil.getFileSize(filePath)));
			contentVO.setFilePath(fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR);
			contentVO.setFileTitle(StringUtil.ReplaceAll(fileVO.getFileStreOriNm(), "." + fileVO.getFileExt(), ""));
			contentVO.setClassId(calssId);
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
				DocumentFileVO docuFileVO = new DocumentFileVO();
				docuFileVO.setDocuId(docuId);
				docuFileVO.setFileId(contentVO.getFileId());
				docuFileVO.setFileName(fileVO.getFileStreOriNm());
				docuFileVO.setFileUserId(userId);
				if (fileVO.getFileKey().contains("imageStamp")) docuFileVO.setRecvSign("Y");
				else 											docuFileVO.setRecvSign("N");

				documentDAO.insDocumentFile(docuFileVO);
				documentDAO.updDocumentFile(docuFileVO);
			}
		}
		vo.setCode("0");
		vo.setMessage("처리되었습니다.");
		return true;
	}

	@Override
	public boolean requestSign(DocumentDetailVO vo, List<FileVO> fileList) throws Exception {
		DocumentMasterVO resultMasterVO = new DocumentMasterVO();
		String docuId = vo.getDocuId();
		String userId = vo.getRecvUserId();

		logger.info("=============== forwardDocument call =============");
		logger.info("docuId : " + docuId);
		logger.info("userId : " + userId);
		logger.info("=============== forwardDocument call =============");

		// 계약서정보조회
		DocumentMasterVO searchMasterVO = new DocumentMasterVO();
		searchMasterVO.setBizId(vo.getBizId());
		searchMasterVO.setDocuId(docuId);
		searchMasterVO.setStartPage(0);
		searchMasterVO.setEndPage(1);
		List<DocumentMasterVO> masterList = documentDAO.getDocumentMasterList(searchMasterVO);
		if (masterList.isEmpty()) {
			logger.error("[forwardDocument] 계약정보가 존재하지 않습니다.");
			throw new Exception("[forwardDocument] 계약정보가 존재하지 않습니다.");
		}

		resultMasterVO = masterList.get(0);
		resultMasterVO.setDomainName(vo.getDomainName());
		// 현재 처리가능한 상태인지 확인
		String docuStatus = resultMasterVO.getDocuStatus();
		if (docuStatus.equals("9")) {
			vo.setCode("-900");
			vo.setMessage("이미 완료된 문서입니다.");
			throw new Exception("이미 완료된 문서입니다.");
		} else if (docuStatus.equals("2")) {
			vo.setCode("-901");
			vo.setMessage("이미 반려된 문서입니다.");
			throw new Exception("이미 반려된 문서입니다.");
		}

		// 발송순서(A, S)
		String sendOrd = resultMasterVO.getSendOrd();

		// 수신자 대상리스트
		DocumentDetailVO searchDetailVO = new DocumentDetailVO();
		searchDetailVO.setDocuId(docuId);
		searchDetailVO.setRecvType("R");
		searchDetailVO.setSortName("B.RECV_ORD");
		searchDetailVO.setSortOrder("ASC");
		List<DocumentDetailVO> detailRList = documentDAO.getDocumentDetailList(searchDetailVO);

		// 참조자 대상리스트
		searchDetailVO = new DocumentDetailVO();
		searchDetailVO.setDocuId(docuId);
		searchDetailVO.setRecvType("T");
		List<DocumentDetailVO> detailTList = documentDAO.getDocumentDetailList(searchDetailVO);

		// 서명요청 상태변경
		docuStatus = "7";
		// 수신자 발송
		if (sendOrd.equals("A")) { // => 검토가 완료된 상태로 수신자에게 일괄 발송
			// 전체발송
			for (DocumentDetailVO detailRVO : detailRList) {
				sendMessageSign(resultMasterVO, detailRVO.getRecvUserId(), detailRVO.getRecvType(), detailRVO.getRecvUserName(), detailRVO.getRecvPhone(), detailRVO.getRecvEmail());
			}
		} else {
			// 수신자#1 발송
			if (!detailRList.isEmpty()) {
				DocumentDetailVO detailRVO = detailRList.get(0);
				sendMessageSign(resultMasterVO, detailRVO.getRecvUserId(), detailRVO.getRecvType(), detailRVO.getRecvUserName(), detailRVO.getRecvPhone(), detailRVO.getRecvEmail());
			}
		}

		// 계약마스터 상태 변경
		DocumentMasterVO updMasterVO = new DocumentMasterVO();
		updMasterVO.setDocuId(docuId);
		updMasterVO.setDocuStatus(docuStatus);
		updMasterVO.setBizId(vo.getBizId());
		updMasterVO.setCompleteDate(DateUtil.getTimeStamp(7));
		documentDAO.updDocumentMaster(updMasterVO);

		// 로그생성
		DocumentLogVO insLogVO = new DocumentLogVO();
		insLogVO.setDocuId(docuId);
		insLogVO.setLogType("D40");
		insLogVO.setLogMessage("검토 후 서명을 요청하였습니다.");
		insLogVO.setWorkUserId(userId);
		documentDAO.insDocumentLog(insLogVO);

		vo.setCode("0");
		vo.setMessage("처리되었습니다.");
		return true;
	}

	// 사용자데이터 입력 완료계약서 생성
	@Override
	public boolean saveDocumentFormData(DocumentMasterVO vo, List<DocumentDetailVO> detailRList, String originalPdfPath, String targetPdfPath) throws Exception {

		// 계약상세 정보
		if (detailRList.isEmpty()) throw new Exception("[saveDocumentComplete] 계약 상세정보가 존재하지 않습니다.");

		// 계약 입력항목 설정
		DocumentEformVO formVO = new DocumentEformVO();
		formVO.setDocuId(vo.getDocuId());

		// 사인, 직인 갯수 리스트 조회
		List<DocumentEformVO> formList = documentDAO.getDocumentEformList(formVO);
		List<FieldVO> fieldListVO 	   = new ArrayList<>();

		// 사인, 직인 갯수많큼 for문
		for (DocumentEformVO formData : formList) {
			String formId 	 = formData.getFormId(); // 수기서명 / 직인 ID
			int formIdNumber = extractNumberFromFormId(formData.getFormId());

			// 서명영역은 값설정 제외
			if (formData.getFormType().equals("4")) {
				// 전자서명
				for (DocumentDetailVO detailVO : detailRList) {
					String signId = "sign_" + detailVO.getRecvOrd();
					int recvOrdNumber = Integer.parseInt(detailVO.getRecvOrd());
					// P 인사 / C 거래처 구분 (contactType 가져오기)
					String contactType = detailVO.getContactType();

					// 수기서명인 경우만
					if (contactType.equals("P") && formIdNumber == recvOrdNumber && StringUtil.isNotNull(detailVO.getRecvSign())) {

						List<LineVO> lines = new ArrayList();
						String recvSign    = detailVO.getRecvSign();
						String[] lineData  = StringUtil.split(recvSign, "|");

						if (lineData.length == 0) throw new Exception("[saveDocumentFormComplete] 서명정보가 없습니다.");

						for (String userLine : lineData) {
							if (!userLine.isEmpty()) {
								String[] userLineData = userLine.split("-+");

								if (userLineData.length == 4) {
									String lineId 	 = userLineData[0];
									String lineWidth = userLineData[1];
									String lineX 	 = userLineData[2];
									String lineY 	 = userLineData[3];
									LineVO lineVO 	 = new LineVO();
									lineVO.setId(lineId);
									lineVO.setPen(1);
									lineVO.setSize(Integer.parseInt(lineWidth));
									lineVO.setX(Float.parseFloat(lineX));
									lineVO.setY(Float.parseFloat(lineY));
									lines.add(lineVO);
								} else {
									throw new Exception("[saveDocumentFormData] 사용자 서명 상세정보가 정상적이지 않습니다." + userLine);
								}
							}
						}
						FieldVO fieldVO = new FieldVO();
						fieldVO.setId(formId);
						fieldVO.setLines(lines);
						fieldListVO.add(fieldVO);

						// 직인일 경우만
					} else if (contactType.equals("C") && formIdNumber == recvOrdNumber) {
						// 이미지 생성(회사 직인 찍음)
						String bizImage = "";

						if (StringUtil.isNotNull(detailVO.getRecvSign())) {
							bizImage = MultipartFileUtil.getSystemPath() + "temp/file/" + detailVO.getRecvSign();
						} else {
							bizImage = MultipartFileUtil.getSystemPath() + "images/sign/digitsign_1.png";
						}
						FieldVO fieldVO = new FieldVO();
						fieldVO.setId(formId);
						fieldVO.setImage(bizImage);
						fieldListVO.add(fieldVO);
					}
				}
			} else { fieldListVO.add(FieldUtil.setFieldData(formData.getFormId(), formData.getFormValue())); }
		}
		for (FieldVO fieldVO : fieldListVO) logger.info(fieldVO.getId());

		// 파일생성
		boolean bWritePDF    = ControlUtil.writePDF(fieldListVO, originalPdfPath, targetPdfPath, false, true, System.getProperty("pdf.font.default"));
		String targetPdfName = FileUtil.getRealName(targetPdfPath);

		// 콘텐츠 등록
		ContentVO contentVO = new ContentVO();
		contentVO.setFileType(FileUtil.getFileExt(targetPdfPath));
		contentVO.setFileName(targetPdfName);
		contentVO.setOrgFileName(vo.getDocuName() + ".pdf");
		contentVO.setFileSize(Long.toString(FileUtil.getFileSize(targetPdfPath)));
		contentVO.setFilePath(MultipartFileUtil.getSystemTempPath());
		contentVO.setFileTitle(vo.getDocuName());
		contentVO.setClassId(this.DOCU_CON_ID);
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
			vo.setOrgFileId(contentVO.getFileId());
			bWritePDF = true;
		} else {
			bWritePDF = false;
		}
		return bWritePDF;
	}

	// 완료계약서 생성
	@Override
	public void saveDocumentComplete(DocumentMasterVO vo, List<FileVO> fileList) throws Exception {
		String originalPdfPath = "";
		// 파일(계약서, 첨부파일) 등록
		for (FileVO fileVO : fileList) {
			String filePath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();

			File file = new File(filePath);
			if (!file.exists()) throw new Exception("[saveDocumentComplete]  전자문서 양식파일이 존재하지 않습니다.");

			originalPdfPath = filePath;
		}

		// 계약서 정보
		vo.setStartPage(0);
		vo.setEndPage(1);
		List<DocumentMasterVO> masterList = documentDAO.getDocumentMasterList(vo);
		if (masterList.isEmpty()) throw new Exception("[saveDocumentComplete] 계약서 정보가 존재하지 않습니다.");

		DocumentMasterVO masterVO = masterList.get(0);
		masterVO.setDomainName(vo.getDomainName());
		String docuFileId 		  = masterVO.getConFileId();

		// 계약상세 정보
		DocumentDetailVO dtlVO = new DocumentDetailVO();
		dtlVO.setDocuId(vo.getDocuId());
		dtlVO.setRecvType("R");
		List<DocumentDetailVO> detailRList = documentDAO.getDocumentDetailList(dtlVO);
		if (detailRList.isEmpty()) throw new Exception("[saveDocumentComplete] 계약 상세정보가 존재하지 않습니다.");

		String targetPdfName = StringUtil.getUUID() + ".pdf";
		String targetPdfPath = MultipartFileUtil.getSystemTempPath() + targetPdfName;
		String targetTsaName = StringUtil.getUUID() + ".pdf";
		String targetTsaPath = MultipartFileUtil.getSystemTempPath() + targetTsaName;
		// temp pdf파일 설정
		vo.setPdfFile(targetPdfPath);

		// 계약 입력항목 설정
		DocumentEformVO formVO = new DocumentEformVO();
		formVO.setDocuId(vo.getDocuId());
		List<DocumentEformVO> formList = documentDAO.getDocumentEformList(formVO);

		List<FieldVO> fieldListVO = new ArrayList<>();
		for (DocumentEformVO formData : formList) {
			fieldListVO.add(FieldUtil.setFieldData(formData.getFormId(), formData.getFormValue()));
		}

		// 전자서명
		for (DocumentDetailVO detailVO : detailRList) {

			String contactType = detailVO.getContactType();

			String formId = "sign_"+detailVO.getRecvOrd();
			String recvSign = detailVO.getRecvSign();

			// 수기서명, 서명완료, 서명정보가 있는 경우만
			if (contactType.equals("P") && detailVO.getRecvStatus().equals("9") && StringUtil.isNotNull(detailVO.getRecvSign()) ) {
				List<LineVO> lines = new ArrayList();
				String[] lineData  = StringUtil.split(recvSign, "|");

				if (lineData.length == 0) throw new Exception("[saveDocumentComplete] 서명정보가 없습니다.");

				for (int i=0; i<lineData.length; i++) {
					String userLine = lineData[i];

					if (!userLine.isEmpty()) {

						String[] userLineData = StringUtil.split(userLine, "-");

						if (userLineData.length == 4) {

							String lineId 	 = userLineData[0];
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
							throw new Exception("[saveDocumentComplete] 사용자 서명 상세정보가 정상적이지 않습니다."+userLine);
						}
					}
				}

				FieldVO fieldVO = new FieldVO();
				fieldVO.setId(formId);
				fieldVO.setLines(lines);
				fieldListVO.add(fieldVO);

			} else if(contactType.equals("C") && detailVO.getRecvStatus().equals("9") && StringUtil.isNotNull(detailVO.getRecvSign()) ) {

				String bizImage = "";
				// 이미지 생성(회사 직인 찍음)
				if (StringUtil.isNotNull(detailVO.getRecvSign())) {
					bizImage = MultipartFileUtil.getSystemPath()+"images/sign/" +detailVO.getRecvSign();
				} else {
					bizImage = MultipartFileUtil.getSystemPath()+"images/sign/digitsign.png";
				}
				FieldVO fieldVO = new FieldVO();
				fieldVO.setId(formId);
				fieldVO.setImage(bizImage);
				fieldListVO.add(fieldVO);
			}
		}

		// 파일생성
		boolean bWritePDF = ControlUtil.writePDF(fieldListVO, originalPdfPath, targetPdfPath, false, true, System.getProperty("pdf.font.default"));
		// 콘텐츠등록
		if (bWritePDF) {
			if (masterVO.getTsaType().equals("Y")) {
				// TSA발급
				TSAUtil.timestampPdf(targetPdfPath, targetTsaPath, true, true, "newzenpnp", "");
				long tsaFileSize = FileUtil.getFileSize(targetTsaPath);
				if (tsaFileSize < 10 * 1024) {
					throw new Exception("[saveDocumentComplete] 문서위변조(TSA) 발급에 문제가 발생하였습니다.");
				} else {
					targetPdfPath = targetTsaPath;
					targetPdfName = targetTsaName;
				}
			}
			String tempFileName = masterVO.getDocuName();

			ContentVO contentVO = new ContentVO();
			contentVO.setFileType(FileUtil.getFileExt(targetPdfPath));
			contentVO.setFileName(targetPdfName);
			contentVO.setOrgFileName(masterVO.getDocuName() + ".pdf");
			contentVO.setFileSize(Long.toString(FileUtil.getFileSize(targetPdfPath)));
			contentVO.setFilePath(MultipartFileUtil.getSystemTempPath());
			contentVO.setFileTitle(masterVO.getDocuName());
			contentVO.setClassId(this.DOCU_CON_ID);
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
				masterVO.setConFileId(contentVO.getFileId());
				// 완료계약서 등록
				DocumentMasterVO updMasterVO = new DocumentMasterVO();
				updMasterVO.setDocuId(masterVO.getDocuId());
				updMasterVO.setConFileId(contentVO.getFileId());
				updMasterVO.setDocuStatus("Z");
				updMasterVO.setBizId(masterVO.getBizId());
				documentDAO.updDocumentMaster(updMasterVO);

				// 완료계약서 존재시 삭제처리
				if (StringUtil.isNotNull(docuFileId)) {
					ContentVO delContentVO = new ContentVO();
					delContentVO.setFileId(docuFileId);
					List<ContentVO> delContentList = new ArrayList();
					contentService.deleteContent(delContentList);
				}

				// 완료계약서 발송
				for(DocumentDetailVO detailVO : detailRList) {
					contentVO.setFilePath(targetPdfPath);
					sendMessageComplete(contentVO, masterVO, detailVO.getRecvUserId(), detailVO.getRecvType(), detailVO.getRecvUserName(), detailVO.getRecvPhone(), detailVO.getRecvEmail());
				}
			}

		}

	}

	// 수신자/참조자 코멘트
	@Override
	public void saveDocumentComment(DocumentCommentVO vo) throws Exception {
		if 		(vo.getDbMode().equals("C")) documentDAO.insDocumentComment(vo);
		else if (vo.getDbMode().equals("D")) documentDAO.delDocumentComment(vo);
	}

	// 작업자별 계약문서 설정진행
	@Override
	public void saveDocumentSetting(DocumentSettingVO vo) throws Exception {
		switch (vo.getDbMode()) {
			case "C":
				documentDAO.insDocumentSetting(vo);
				break;
			case "U":
				documentDAO.delDocumentSetting(vo);
				documentDAO.insDocumentSetting(vo);
				break;
			case "D":
				documentDAO.delDocumentSetting(vo);
				break;
		}
	}

	@Override
	public DocumentResponseVO getDocument(DocumentMasterVO vo) throws Exception {
		DocumentResponseVO result 		= new DocumentResponseVO();
		DocumentMasterVO searchMasterVO = new DocumentMasterVO();
		searchMasterVO.setBizId(vo.getBizId());
		searchMasterVO.setDocuId(vo.getDocuId());
		searchMasterVO.setStartPage(0);
		searchMasterVO.setEndPage(1);
		List<DocumentMasterVO> masterlist = documentDAO.getDocumentMasterList(vo);
		if (masterlist.isEmpty()) {
			logger.error("[getDocument] 계약정보가 존재하지 않습니다.");
			throw new Exception();
		}

		result.documentMaster = masterlist.get(0);

		// 계약 DETAIL 정보
		DocumentDetailVO searchDetailVO = new DocumentDetailVO();
		searchDetailVO.setDocuId(vo.getDocuId());
		List<DocumentDetailVO> detailList = documentDAO.getDocumentDetailList(searchDetailVO);
		if (!detailList.isEmpty()) result.documentDetail = detailList;

		// 계약 첨부파일 정보
		DocumentFileVO searchFileVO = new DocumentFileVO();
		searchFileVO.setDocuId(vo.getDocuId());
		List<DocumentFileVO> fileList = documentDAO.getDocumentFileList(searchFileVO);
		if (!fileList.isEmpty()) result.documentFile = fileList;

		// 계약 코멘트정보
		DocumentCommentVO searchCommentVO = new DocumentCommentVO();
		searchCommentVO.setDocuId(vo.getDocuId());
		List<DocumentCommentVO> commentList = documentDAO.getDocumentCommentList(searchCommentVO);
		logger.info("commentList.size()"+commentList.size());
		if (!commentList.isEmpty()) result.documentComment = commentList;

		// 계약진행상태
		DocumentLogVO searchLogVO = new DocumentLogVO();
		searchLogVO.setDocuId(vo.getDocuId());
		List<DocumentLogVO> logList = documentDAO.getDocumentLogList(searchLogVO);
		logger.info("logList.size()"+logList.size());
		if (!logList.isEmpty()) result.documentLog = logList;

		return result;
	}

	@Override
	public List<DocumentMasterVO> getDocumentMasterWriteList(DocumentMasterVO vo) throws Exception { return documentDAO.getDocumentMasterWriteList(vo); }

	@Override
	public int getDocumentMasterWriteListCount(DocumentMasterVO vo) { return documentDAO.getDocumentMasterWriteListCount(vo); }

	@Override
	public List<DocumentMasterVO> getDocumentMasterList(DocumentMasterVO vo) throws Exception { return documentDAO.getDocumentMasterList(vo); }

	@Override
	public int getDocumentMasterListCount(DocumentMasterVO vo) { return documentDAO.getDocumentMasterListCount(vo); }

	@Override
	public List<DocumentMasterVO> getDocumentNonce(DocumentMasterVO vo) throws Exception { return documentDAO.getDocumentNonce(vo); }

	@Override
	public List<DocumentDetailVO> getDocumentDetailList(DocumentDetailVO vo) throws Exception { return documentDAO.getDocumentDetailList(vo); }

	@Override
	public List<DocumentEformVO> getDocumentEformList(DocumentEformVO vo) throws Exception { return documentDAO.getDocumentEformList(vo); }

	@Override
	public List<DocumentLogVO> getDocumentLogList(DocumentLogVO vo) throws Exception { return documentDAO.getDocumentLogList(vo); }

	@Override
	public void delDocumentDetail(DocumentDetailVO vo) throws Exception { documentDAO.delDocumentDetail(vo); }

	@Override
	public int updDocumentMaster(DocumentMasterVO vo) throws Exception { return documentDAO.updDocumentMaster(vo); }

	@Override
	public int updDocumentEform(DocumentEformVO vo) throws Exception { return documentDAO.updDocumentEform(vo); }

	@Override
	public int updDocumentFile(DocumentFileVO vo) throws Exception { return documentDAO.updDocumentFile(vo); }

	@Override
	public void insDocumentMaster(DocumentMasterVO vo) throws Exception { documentDAO.insDocumentMaster(vo); }

	@Override
	public void insDocumentLog(DocumentLogVO vo) throws Exception { documentDAO.insDocumentLog(vo); }

	@Override
	public void insDocumentEform(DocumentEformVO vo) throws Exception { documentDAO.insDocumentEform(vo); }

	@Override
	public void insDocumentDetail(DocumentDetailVO vo) throws Exception { documentDAO.insDocumentDetail(vo); }

	@Override
	public DocumentMasterVO getDocuView(DocumentMasterVO vo) throws Exception {
		ContentVO contentVO = new ContentVO();
		contentVO.setFileId(vo.getFileId());
		ContentVO resultVO  = contentDAO.getContent(contentVO);
		if (resultVO == null) { logger.error("[getDocuView] 콘텐츠 정보가 존재하지 않습니다."); return null; }

		CabinetVO cabinetVO 	 = new CabinetVO();
		cabinetVO.setClassId(resultVO.getClassId());
		cabinetVO.setCabinetId(resultVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO == null) { logger.error("[getDocuView] 캐비넷 정보가 존재하지 않습니다."); return null; }

		String originalPdfPath = cabinetResultVO.getCabinetPath() + resultVO.getFilePath() + resultVO.getFileName();
		String targetPdfName   = StringUtil.getUUID() + ".pdf";
		String targetPdfPath   = MultipartFileUtil.getSystemTempPath() + targetPdfName;

		// 사용할 PDF복사
		FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
		if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
			String pdf_uri = StringUtil.ReplaceAll(targetPdfPath, MultipartFileUtil.getSystemPath(), "");
			vo.setPdfFile(pdf_uri);
		}
		return vo;
	}

	@Override
	public DocumentResponseVO getDocumentView(DocumentMasterVO vo) throws Exception { return getDocumentView(vo, true); }

	// 전자계약서 view
	@Override
	public DocumentResponseVO getDocumentView(DocumentMasterVO vo, boolean allFileView) throws Exception {
		DocumentResponseVO result 				  = new DocumentResponseVO();
		List<PageVO> pageList 					  = new ArrayList<PageVO>();
		List<DocumentMasterVO> documentMasterList = null;
		DocumentMasterVO documentMasterVO 		  = new DocumentMasterVO();
		ContentVO contentVO 					  = new ContentVO();
		ContentVO contentResultVO 				  = new ContentVO();
		CabinetVO cabinetVO 					  = new CabinetVO();
		CabinetVO cabinetResultVO 				  = new CabinetVO();

		documentMasterList = documentDAO.getDocumentNonce(vo);
		if (documentMasterList == null || documentMasterList.isEmpty()) { logger.error("[getDocumentView] 계약서 정보가 존재하지 않습니다."); return null; }

		documentMasterVO = documentMasterList.get(0);
		result.documentMaster = documentMasterVO;

		DocumentDetailVO searchDetailVO = new DocumentDetailVO();
		searchDetailVO.setDocuId(documentMasterVO.getDocuId());
		searchDetailVO.setRecvUserId(vo.getUserId());
		searchDetailVO.setRecvType(vo.getRecvType());
		List<DocumentDetailVO> detailList = documentDAO.getDocumentDetailList(searchDetailVO);
		if (detailList == null || detailList.isEmpty()) { logger.error("[getDocumentView] 계약서 정보가 존재하지 않습니다."); return null; }
		result.documentDetail = detailList;

		// 계약 첨부파일 정보
		DocumentFileVO searchFileVO = new DocumentFileVO();
		searchFileVO.setDocuId(documentMasterVO.getDocuId());
		if (!allFileView) searchFileVO.setFileUserId(documentMasterVO.getUserId());

		result.documentFile = documentDAO.getDocumentFileList(searchFileVO);
		// 최종 계약서ID
		String fileId = documentMasterVO.getOrgFileId();
		// 콘텐츠에서 정보 추출
		contentVO.setFileId(fileId);
		contentResultVO = contentDAO.getContent(contentVO);
		if (contentResultVO == null) { logger.error("[getDocumentView] 콘텐츠 정보가 존재하지 않습니다."); return null; }

		// 저장위치
		cabinetVO.setClassId(contentResultVO.getClassId());
		cabinetVO.setCabinetId(contentResultVO.getCabinetId());
		cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO == null) { logger.error("[getDocumentView] 캐비넷 정보가 존재하지 않습니다."); return null; }

		String originalPdfPath = cabinetResultVO.getCabinetPath() + contentResultVO.getFilePath() + contentResultVO.getFileName();
		String targetPdfName   = StringUtil.getUUID() + ".pdf";
		String targetPdfPath   = MultipartFileUtil.getSystemTempPath() + targetPdfName;
		// temp pdf파일 설정
		vo.setPdfFile(targetPdfPath);

		// 사용할 PDF복사
		FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
		if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {

			PdfReader reader 	= null;
			FileInputStream fin = new FileInputStream(targetPdfPath);
			reader 				= new PdfReader(fin);
			int totpage 		= reader.getNumberOfPages();
			fin.close();

			String exeName = MultipartFileUtil.getSystemPath() + "data/mudraw.exe";
			pageList = ProcUtil.readPDF(exeName, targetPdfPath, MultipartFileUtil.getSystemTempPath());
			for (int i = 0; i < pageList.size(); i++) {
				PageVO pageVO = pageList.get(i);
				String filePath = StringUtil.ReplaceAll(MultipartFileUtil.getSystemTempPath(), MultipartFileUtil.getSystemPath(), "");
				pageVO.setFilePath(filePath);
			}
		}

		// 수신또는 참조자 상태코드 update
		DocumentDetailVO updDetailVO = new DocumentDetailVO();
		updDetailVO.setRecvUserId(vo.getUserId());
		updDetailVO.setRecvType(vo.getRecvType());
		updDetailVO.setRecvUserId(vo.getUserId());
		updDetailVO.setRecvStatus("1");
		updDetailVO.setViewDate(DateUtil.getTimeStamp(7));
		documentDAO.updDocumentDetail(updDetailVO);

		// 문서 로그 생성
		DocumentLogVO logVO = new DocumentLogVO();
		logVO.setDocuId(documentMasterVO.getDocuId());
		logVO.setLogType("D20");
		logVO.setLogMessage("");
		logVO.setWorkUserId(vo.getUserId());
		documentDAO.insDocumentLog(logVO);

		result.documentPage = pageList;
		return result;
	}

	@Override
	public int downloadDocumentSelectZip(List<DocumentMasterVO> list, SessionVO loginVO) throws Exception {
		int result = 0;
		List<String> documentList = new ArrayList<>();

		for (int i=0; i<list.size(); i++) {
			DocumentMasterVO dataVO = list.get(i);
			ContentVO contentVO 	= new ContentVO();
			contentVO.setFileId(dataVO.getConFileId());
			// 콘텐츠에서 정보 추출	
			ContentVO resultContentVO = contentDAO.getContent(contentVO);
			if (resultContentVO == null) { logger.error("[downloadDocumentZip] 콘텐츠 정보가 존재하지 않습니다."); return 0; }

			// 저장위치
			CabinetVO cabinetVO = new CabinetVO();
			cabinetVO.setClassId(resultContentVO.getClassId());
			cabinetVO.setCabinetId(resultContentVO.getCabinetId());
			CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
			if (cabinetResultVO == null) { logger.error("[downloadDocumentZip] 캐비넷 정보가 존재하지 않습니다."); return 0; }

			String originalPdfPath = cabinetResultVO.getCabinetPath()+resultContentVO.getFilePath()+resultContentVO.getFileName();
			String targetPdfName   = (i+1)+"_"+dataVO.getRUserName()+"_"+dataVO.getDocuName()+"_"+dataVO.getInsDate()+"_전자계약.pdf";
			String targetPdfPath   = MultipartFileUtil.getSystemTempPath()+targetPdfName;

			// 사용할 PDF복사
			FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
			if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) documentList.add(targetPdfPath);

			// 로그내역 LIST에 등록
			DocumentLogVO logVO = new DocumentLogVO();
			logVO.setDocuId(dataVO.getDocuId());
			logVO.setLogType("D95");
			logVO.setLogMessage(dataVO.getRUserName()+"님의 전자문서를 다운로드 하였습니다.");
			logVO.setWorkUserId(dataVO.getUserId());
			documentDAO.insDocumentLog(logVO);
		}

		if (!documentList.isEmpty()) {
			logger.info(list.get(0).getPdfFile());
			result = FileUtil.ZipFile(list.get(0).getPdfFile(), documentList);
		}
		return result;
	}

	private int extractNumberFromFormId(String formId) {
		String[] parts = formId.split("_");
		if (parts.length == 3) {
			try 							{ return Integer.parseInt(parts[1]); }
			catch (NumberFormatException e) { return -1; }
		} else { return -1; }
	}

}
