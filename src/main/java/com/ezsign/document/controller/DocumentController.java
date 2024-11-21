package com.ezsign.document.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ezsign.content.service.ContentService;
import com.ezsign.contact.service.ContactService;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.document.service.DocumentService;
import com.ezsign.document.vo.DocumentCommentVO;
import com.ezsign.document.vo.DocumentDetailVO;
import com.ezsign.document.vo.DocumentLogVO;
import com.ezsign.document.vo.DocumentMasterVO;
import com.ezsign.document.vo.DocumentRequestVO;
import com.ezsign.document.vo.DocumentResponseVO;
import com.ezsign.document.vo.DocumentSettingVO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.HttpUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/document")
public class DocumentController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "documentService")
	private DocumentService documentService;

	@Resource(name = "contentService")
	private ContentService contentService;

	@Resource(name = "contactService")
	private ContactService contactService;

	// 계약서등록
	@RequestMapping(method = {RequestMethod.POST} , value = "saveDocument.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveDocumentCtrl(@ModelAttribute("DocumentRequestVO") DocumentRequestVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: saveDocument :::::::::::::::::::");
		JSONObject jsonObject		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total		  = 0;
		boolean success   = false;
		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) status = HttpStatus.UNAUTHORIZED;
			else {

				String bizId = loginVO.getBizId();
				if (vo.documentMaster.getBizId() == null) vo.documentMaster.setBizId(loginVO.getBizId());

				vo.documentMaster.setUserName(loginVO.getUserName());
				String serverName = request.getServerName();

				if (StringUtils.isNotEmpty(serverName)) vo.setDomainName(request.getScheme() + "://" + request.getServerName());
				else 									vo.setDomainName(System.getProperty("system.open.url"));

				String tempPath		  = MultipartFileUtil.getSystemTempPath();
				List<FileVO> fileList = MultipartFileUtil.getFileAddList(request, tempPath, true);
				success				  = documentService.saveDocument(vo, fileList);
				jsonObject.put("code", vo.code);
				jsonObject.put("message", vo.message);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	//계약서 진행
	@RequestMapping(method = {RequestMethod.POST} , value = "forwardDocument.do")
	@ResponseBody
	public ResponseEntity<JSONObject> forwardDocumentCtrl(@ModelAttribute("DocumentDetailVO") DocumentDetailVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: forwardDocument :::::::::::::::::::");
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result	   = false;
		boolean success	   = false;
		HttpStatus status  = HttpStatus.OK;
		int total 		   = 0;
		String contactType = vo.getContactType();
		try {
			vo.setDomainName(request.getScheme() + "://" + request.getServerName());
			String tempPath 	  = MultipartFileUtil.getSystemTempPath();
			List<FileVO> fileList = MultipartFileUtil.getFileAddList(request, tempPath, true);

			if 		(contactType.equals("P"))  success = documentService.forwardDocument(vo, fileList);
			else if (contactType.equals("C")) {
				// 전달받은 파일리스트
                for (FileVO fileVO : fileList) {
                    if (fileVO.getFileKey().contains("imageStamp")) {
                        // 파일 경로 생성
                        String imgPath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
                        // 직인이미지 존재시 복사 및 DB처리
                        if (com.jarvis.common.util.StringUtil.isNotNull(imgPath)) {

                            // 년월 형식 생성 (yyyyMM)
                            String szMonthPath = MultipartFileUtil.SEPERATOR + DateUtil.getTimeStamp(14);
                            // 년월일 형식 생성 (yyyyMMdd)
                            String szDayPath = MultipartFileUtil.SEPERATOR + DateUtil.getTimeStamp(3);
                            // 회사 bizId
                            String bizIdPath = MultipartFileUtil.SEPERATOR + vo.getBizId();
                            // 파일 저장 이름
                            String RecvUserPath = MultipartFileUtil.SEPERATOR + vo.getRecvUserId();

                            String imgSavePath = MultipartFileUtil.getSystemPath() + "temp/file" + szMonthPath + szDayPath + bizIdPath + RecvUserPath + "_stamp." + fileVO.getFileExt();
                            // 디렉토리 생성
                            com.jarvis.common.util.FileUtil.createDirectory(MultipartFileUtil.getSystemPath() + "temp/file" + szMonthPath + szDayPath + bizIdPath);
                            // 파일 복사
                            if (com.jarvis.common.util.FileUtil.fileCopy(imgPath, imgSavePath)) vo.setRecvSign(szMonthPath + szDayPath + bizIdPath + RecvUserPath + "_stamp." + fileVO.getFileExt());
                        }
                    }
                }
				result = documentService.forwardDocument(vo, fileList);
				if (result) success = true;
			}
		} catch (Exception ex) { logService.error(ex.getMessage(), new Throwable(ex)); }
		jsonObject.put("code", vo.getCode());
		jsonObject.put("message", vo.getMessage());
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = {RequestMethod.POST} , value = "requestSign.do")
	@ResponseBody
	public ResponseEntity<JSONObject> requestSignCtrl(@ModelAttribute("DocumentDetailVO") DocumentDetailVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: requestSignCtrl :::::::::::::::::::");
		JSONObject jsonObject	    = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result 	  = false;
		boolean success	  = false;
		HttpStatus status = HttpStatus.OK;
		int total		  = 0;
		try {
			vo.setDomainName(request.getScheme() + "://" + request.getServerName());
			String tempPath = MultipartFileUtil.getSystemTempPath();
			List<FileVO> fileList = MultipartFileUtil.getFileAddList(request, tempPath, true);
			success			= documentService.requestSign(vo, fileList);
		} catch (Exception ex) { logService.error(ex.getMessage(), new Throwable(ex)); }
		jsonObject.put("code", vo.getCode());
		jsonObject.put("message", vo.getMessage());
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	//슈퍼관리자 최종파일 업로드
	@RequestMapping(method = {RequestMethod.POST} , value = "saveDocumentComplete.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveDocumentCompleteCtrl(@ModelAttribute("DocumentMasterVO") DocumentMasterVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: saveDocumentComplete :::::::::::::::::::");
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total		  = 0;
		boolean success	  = false;
		try {
			vo.setDomainName(request.getScheme() + "://" + request.getServerName());
			String tempPath		  = MultipartFileUtil.getSystemTempPath();
			List<FileVO> fileList = MultipartFileUtil.getFileAddList(request, tempPath, true);
			documentService.saveDocumentComplete(vo, fileList);
			success = true;
		} catch (Exception ex) { logService.error(ex.getMessage(), new Throwable(ex)); }
		jsonObject.put("code", vo.getCode());
		jsonObject.put("message", vo.getMessage());
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 계약서조회
	@RequestMapping(method = {RequestMethod.POST} , value = "getDocument.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getDocumentCtrl(@ModelAttribute("documentMasterVO") DocumentMasterVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: getDocument :::::::::::::::::::");
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total		  = 0;
		boolean success	  = false;
		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) status = HttpStatus.UNAUTHORIZED;
			else {
				vo.setBizId(loginVO.getBizId());
				DocumentResponseVO result = documentService.getDocument(vo);

				jsonObject.put("master", result.documentMaster);
				jsonObject.put("detail", result.documentDetail);
				jsonObject.put("file", result.documentFile);
				jsonObject.put("comment", result.documentComment);
				jsonObject.put("log", result.documentLog);

				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = {RequestMethod.POST} , value = "saveDocumentComment.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveDocumentCommentCtrl(@ModelAttribute("DocumentCommentVO") DocumentCommentVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: saveDocumentComment :::::::::::::::::::");

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total		  = 0;
		boolean success	  = false;
		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) status = HttpStatus.UNAUTHORIZED;
			else {
				documentService.saveDocumentComment(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 작성자 계약정보리스트
	@RequestMapping(method = {RequestMethod.POST} , value = "getDocumentMasterWriteList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getDocumentMasterWriteListCtrl(@ModelAttribute("documentMasterVO") DocumentMasterVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: getDocumentMasterWriteList :::::::::::::::::::");
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total 		  = 0;
		boolean success   = false;
		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) status = HttpStatus.UNAUTHORIZED;
			else {
				List<DocumentMasterVO> result = documentService.getDocumentMasterWriteList(vo);
				total   = documentService.getDocumentMasterWriteListCount(vo);
				jsonObject.put("data", result);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("total", total);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = {RequestMethod.POST} , value = "getDocumentMasterList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getDocumentMasterListCtrl(@ModelAttribute("documentMasterVO") DocumentMasterVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: getDocumentMasterList :::::::::::::::::::");
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total 		  = 0;
		boolean success   = false;
		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) status = HttpStatus.UNAUTHORIZED;
			else {
				List<DocumentMasterVO> result = documentService.getDocumentMasterList(vo);
				total   = documentService.getDocumentMasterListCount(vo);
				jsonObject.put("data", result);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("total", total);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 전자계약 상세내역 / 진행상태 로그 표시
	@RequestMapping(method = {RequestMethod.POST} , value = "getDocumentLogList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getDocumentLogListCtrl(@ModelAttribute("documentLogVO") DocumentLogVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: getDocumentLogList :::::::::::::::::::");
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total		  = 0;
		boolean success	  = false;
		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) status = HttpStatus.UNAUTHORIZED;
			else {
				List<DocumentLogVO> result = documentService.getDocumentLogList(vo);
				jsonObject.put("data", result);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("total", total);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	//완료문서 조회
	@RequestMapping(method = RequestMethod.GET , value = "getDocumentView.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getDocumentViewCtrl(@ModelAttribute("documentMasterVO") DocumentMasterVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: getDocumentView :::::::::::::::::::");
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) status = HttpStatus.UNAUTHORIZED;
			else {
				DocumentMasterVO result = documentService.getDocuView(vo);

				if (result != null) {
					jsonObject.put("success", true);
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 중요문서 선택
	@RequestMapping(method = {RequestMethod.POST} , value = "saveDocumentSetting.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveDocumentSettingCtrl(@ModelAttribute("DocumentSettingVO") DocumentSettingVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: saveDocumentSetting :::::::::::::::::::");
		JSONObject jsonObject		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total		  = 0;
		boolean success	  = false;
		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) status = HttpStatus.UNAUTHORIZED;
			else {
				documentService.saveDocumentSetting(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("total", total);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 본인인증-인증비밀번호 변경
	@RequestMapping(method = {RequestMethod.POST} , value = "saveAuthCode.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveAuthCodeCtrl(@ModelAttribute("DocumentMasterVO") DocumentMasterVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: saveAuthCode :::::::::::::::::::");
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total		  = 0;
		boolean success	  = false;
		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) status = HttpStatus.UNAUTHORIZED;
			else {
				String bizId = loginVO.getBizId();
				vo.setBizId(bizId);
				vo.setAuthCode(SecurityUtil.encrypt(vo.getAuthCode()));
				total   = documentService.updDocumentMaster(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("total", total);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 본인인증-인증비밀번호 확인
	@RequestMapping(method = {RequestMethod.POST} , value = "checkAuthCode.do")
	@ResponseBody
	public ResponseEntity<JSONObject> checkAuthCodeCtrl(@ModelAttribute("DocumentMasterVO") DocumentMasterVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: checkAuthCode :::::::::::::::::::");
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total		  = 0;
		boolean success	  = false;
		try {
			List<DocumentMasterVO> list = documentService.getDocumentNonce(vo);
            success = SecurityUtil.encrypt(vo.getAuthCode()).equals(list.get(0).getAuthCode());
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("total", total);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 수신자 계약서 조회
	@RequestMapping(method = RequestMethod.GET , value = "recvDocument.do")
	public ModelAndView recvDocumentCtrl(@RequestParam("id") String id, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: recvDocument :::::::::::::::::::");
		logger.info("digitNonce =>"+id);
		JSONObject jsonObject = new JSONObject();
		ModelAndView view 	  = new ModelAndView();

		String viewURL    = "";
		String[] data     = id.split("_");
		String digitNonce = data[0];
		String recvUserId = data[1];
		String recvType   = data[2];

		logger.info("[recvDocument] digitNonce : "+digitNonce+", recvUserId : "+recvUserId);

		/************************************* digitNonce로 계약서 조회**********************************/
		DocumentMasterVO vo = new DocumentMasterVO();
		vo.setDigitNonce(digitNonce);
		vo.setUserId(recvUserId);
		List<DocumentMasterVO> result = documentService.getDocumentNonce(vo);

		if (result == null || result.isEmpty()) {
			logger.error("[recvDocument] 문서정보가 존재하지 않습니다.");
			view.setViewName("/document/document_sign_step_none");
			return view;
		}
		/************************************* digitNonce로 계약서 조회**********************************/


		/************************************* 수신자 조회**********************************/
		DocumentDetailVO detailVO = new DocumentDetailVO();
		detailVO.setRecvUserId(recvUserId);
		detailVO.setDocuId(result.get(0).getDocuId());
		detailVO.setRecvType(recvType);

		List<DocumentDetailVO> detailResult = documentService.getDocumentDetailList(detailVO);

		if (detailResult==null || detailResult.isEmpty()) {
			logger.error("[recvDocument] 수신자 정보가 존재하지 않습니다.");
			viewURL = "/document/document_sign_step_none";
			return view;
		}

		result.get(0).setDocumentDetailList(detailResult);
		/************************************* 수신자 조회**********************************/;


		//유효기간 체크
        jsonObject.put("userName", result.get(0).getUserName());
        jsonObject.put("recvUserName", detailResult.get(0).getRecvUserName());
        jsonObject.put("documentName", result.get(0).getDocuName());
        jsonObject.put("statusCode", result.get(0).getDocuStatus());
        jsonObject.put("phoneNum", detailResult.get(0).getRecvPhone());
        jsonObject.put("authType", result.get(0).getAuthType());
        jsonObject.put("bizId",result.get(0).getBizId());
        jsonObject.put("mailAddr",detailResult.get(0).getRecvEmail());
        jsonObject.put("contactType", detailResult.get(0).getContactType());

        if (StringUtil.isNotNull(result.get(0).getExpireDate()) && DateUtil.diffOfDate(result.get(0).getExpireDate(), DateUtil.getTimeStamp(3)) < 0) {
            // 문서 유통기한이 지난경우 별도 페이지 표시
            viewURL = "/document/document_sign_step_expire";
        } else {
            //인증이 필요하지 않을경우 -> 바로 계약서 조회 화면으로
            if (result.get(0).getAuthType().equals("N")) {
                view.setViewName("redirect:/document/documentView.do?id="+id);
                return view;
            }
            view.addObject("document", jsonObject.toString());
            viewURL = "/document/document_sign_step_1";
        }
        view.setViewName(viewURL);
		return view;
	}

	//참조/수신자 계약서 조회
	@RequestMapping(method = RequestMethod.GET , value = "documentView.do")
	public ModelAndView documentViewCtrl(@RequestParam("id") String id, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: documentView :::::::::::::::::::");
		logger.info("digitNonce =>"+id);
		net.sf.json.JSONObject jsonObject = new JSONObject();
		ModelAndView view 				  = new ModelAndView();
		boolean success = false;
		int total		= 0;
		String viewPage = "/document/document_sign_step_none";
		try {
			// ID로 계약서 정보 조회
			DocumentMasterVO vo = new DocumentMasterVO();
			String[] data 		= id.split("_");
			String digitNonce 	= data[0];
			String recvUserId 	= data[1];
			String recvType 	= data[2];

			vo.setDigitNonce(digitNonce);
			vo.setUserId(recvUserId);
			vo.setRecvType(recvType);

			DocumentResponseVO responseVO = documentService.getDocumentView(vo, false);

			if (responseVO == null || responseVO.documentMaster == null || responseVO.documentDetail == null) {
				logger.error("[documentView] 전자문서 정보가 존재하지 않습니다.");
				view.setViewName("/document/document_sign_step_none");
				return view;
			}
			String docuStatus = responseVO.documentMaster.getDocuStatus();
			String recvStatus = responseVO.documentDetail.get(0).getRecvStatus();

			if (recvType.equals("T")) {
				// 참조자
				viewPage = "/document/document_sign_step_2_view";
			} else {
				switch(docuStatus) {
					case "1":
						// 발송
						viewPage = "/document/document_sign_step_2";
						break;
					case "2":
						// 휴지통
						viewPage = "/document/document_sign_step_none";
						break;
					case "3":
						// 반려
						viewPage = "/document/document_sign_step_2_view";
						break;
					case "4":
						// 검토중
						if (recvStatus.equals("4") || recvStatus.equals("3")) {
							viewPage = "/document/document_sign_step_2_view";
						} else {
							viewPage = "/document/document_sign_step_2";
						}
						break;
					case "5":
						// 검토완료
						viewPage = "/document/document_sign_step_2_view";
						break;
					case "7":
						// 서명요청
						if (recvStatus.equals("9") || recvStatus.equals("8")) {
							viewPage = "/document/document_sign_step_2_view";
						} else {
							viewPage = "/document/document_sign_step_2";
						}
						break;
					case "8":
						// 서명중
						if (recvStatus.equals("9") || recvStatus.equals("8")) {
							viewPage = "/document/document_sign_step_2_view";
						} else {
							viewPage = "/document/document_sign_step_2";
						}
						break;
					case "9":
						// 서명완료
						viewPage = "/document/document_sign_step_2_view";
						break;
					case "Z":
						// 계약완료
						viewPage = "/document/document_sign_step_2_view";
						break;
					default:
						viewPage = "/document/document_sign_step_none";
						break;
				}
			}

			jsonObject.put("master",  responseVO.documentMaster);
			jsonObject.put("detail",  responseVO.documentDetail.get(0));
			jsonObject.put("data", responseVO.documentPage);
			jsonObject.put("file", responseVO.documentFile);

			total = responseVO.documentPage.size();
			success = true;
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		view.addObject("success", success);
		view.addObject("total", total);
		view.addObject("result", jsonObject.toString());
		view.setViewName(viewPage);
		return view;
	}

	// 전자서명 완료
	@RequestMapping(method=RequestMethod.GET, value="goDocumentComplete.do")
	public String goDocumentCompleteCtrl(HttpServletRequest request, Model model) {
		logger.info(":::::::::::::::::::: goDocumentComplete :::::::::::::::::::");
		return "document/document_sign_step_3_complete";
	}

	// 정정요청 완료
	@RequestMapping(method=RequestMethod.GET, value="goDocumentRejectComplete.do")
	public String goDocumentRejectCompleteCtrl() {
		logger.info(":::::::::::::::::::: goDocumentRejectComplete :::::::::::::::::::");
		return "document/document_sign_step_3_reject_complete";
	}

	// 검토요청 완료
	@RequestMapping(method=RequestMethod.GET, value="goDocumentReviewComplete.do")
	public String goDocumentReviewCompleteCtrl() {
		logger.info(":::::::::::::::::::: goDocumentReviewComplete :::::::::::::::::::");
		return "document/document_sign_step_3_review_complete";
	}

	// 정정요청 팝업
	@RequestMapping(method=RequestMethod.GET, value="goDocumentRejectPad.do")
	public String goRejectPadCtrl(@RequestParam("id") String id, Model model) throws Exception {
		logger.info(":::::::::::::::::::: goDocumentRejectPad :::::::::::::::::::");
		DocumentMasterVO vo = new DocumentMasterVO();
		String[] data		= id.split("_");
		String digitNonce 	= data[0];
		String recvUserId 	= data[1];
		String recvType 	= data[2];

		vo.setDigitNonce(digitNonce);
		vo.setUserId(recvUserId);

		List<DocumentMasterVO> documentList = documentService.getDocumentNonce(vo);
		if (documentList != null) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("document", documentList.get(0));
			model.addAttribute("result", jsonObject);
			model.addAttribute("recvUserId", recvUserId);
			model.addAttribute("recvType", recvType);
		}
		return "document/document_sign_step_2_reject";
	}

	// 정정요청 팝업(검토용)
	@RequestMapping(method=RequestMethod.GET, value="goDocumentReviewRejectPad.do")
	public String goReviewRejectPadCtrl(@RequestParam("id") String id, Model model) throws Exception {
		logger.info(":::::::::::::::::::: goDocumentReviewRejectPad :::::::::::::::::::");
		DocumentMasterVO vo = new DocumentMasterVO();
		String[] data 		= id.split("_");
		String digitNonce 	= data[0];
		String recvUserId 	= data[1];
		String recvType		= data[2];

		vo.setDigitNonce(digitNonce);
		vo.setUserId(recvUserId);

		List<DocumentMasterVO> documentList = documentService.getDocumentNonce(vo);
		if (documentList != null) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("document", documentList.get(0));
			model.addAttribute("result", jsonObject);
			model.addAttribute("recvUserId", recvUserId);
			model.addAttribute("recvType", recvType);
		}
		return "document/document_sign_step_2_review_reject";
	}

	// 수기 전자서명 팝업
	@RequestMapping(method=RequestMethod.GET, value="goDocumentSignPad.do")
	public String goSignPadCtrl() {
		logger.info(":::::::::::::::::::: goSignPad :::::::::::::::::::");
		return "document/document_sign_step_2_sign";
	}

	// 직인 팝업
	@RequestMapping(method=RequestMethod.GET, value="goDocumentSignCustomer.do")
	public String goSignCustomerPadCtrl() {
		logger.info(":::::::::::::::::::: goSignCustomer :::::::::::::::::::");
		return "document/document_sign_step_2_customer";
	}

	// 수기전자서명 팝업 (검토완료 시 팝업)
	@RequestMapping(method=RequestMethod.GET, value="goDocumentReviewCommentPad.do")
	public String goReviewCommentPadCtrl() {
		logger.info(":::::::::::::::::::: goDocumentReviewCommentPad :::::::::::::::::::");
		return "document/document_sign_step_2_review_comment";
	}

	// 비밀번호 인증 팝업
	@RequestMapping(method=RequestMethod.GET, value="goDocumentPasswordPad.do")
	public String goPasswordPadCtrl() {
		logger.info(":::::::::::::::::::: goDocumentReviewCommentPad :::::::::::::::::::");
		return "document/document_sign_step_1_password";
	}

	//완료문서 다운로드
	@RequestMapping(method = RequestMethod.GET , value = "downDocumentPdf.do")
	@ResponseBody
	public ResponseEntity<String> downDocumentPdfCtrl(@ModelAttribute("ContentVO") ContentVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info(":::::::::::::::::::: downDocumentPdf :::::::::::::::::::");
		HttpStatus status = HttpStatus.OK;
		try {
			ContentVO contentVO    = new ContentVO();
			contentVO 			   = contentService.getContent(vo);
			String contentFileName = contentVO.getFileTitle();

			File file = new File(contentFileName);
			HttpUtil.setResponseFile(request, response, file, contentVO.getOrgFileName());
			if (!file.exists()) status = HttpStatus.NO_CONTENT;
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity(status);
	}

	//슈퍼관리자가 원본문서 다운로드 
	@RequestMapping(method = RequestMethod.GET , value = "downOrgPdf.do")
	@ResponseBody
	public ResponseEntity<String> downDocumentPdfCtrl(@RequestParam("docuId") String docuId, @RequestParam("bizId") String bizId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info(":::::::::::::::::::: downOrgPdf :::::::::::::::::::");
		HttpStatus status = HttpStatus.OK;
		try {
			DocumentMasterVO documentMasterVO = new DocumentMasterVO();
			documentMasterVO.setDocuId(docuId);
			documentMasterVO.setBizId(bizId);
			documentMasterVO.setStartPage(0);
			documentMasterVO.setEndPage(1);
			DocumentResponseVO resVO = documentService.getDocument(documentMasterVO);

			ContentVO contentVO 	 = new ContentVO();
			ContentVO paramContentVO = new ContentVO();
			paramContentVO.setFileId(resVO.getDocumentMaster().getOrgFileId());
			contentVO = contentService.getContent(paramContentVO);

			String contentFileName = contentVO.getFileTitle();

			File file = new File(contentFileName);
			HttpUtil.setResponseFile(request, response, file, contentVO.getOrgFileName());
			if (!file.exists()) status = HttpStatus.NO_CONTENT;
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity(status);
	}

	// 문서가 존재하지 않습니다.
	@RequestMapping(method=RequestMethod.GET, value="goDocumentStepNone.do")
	public String goDocumentStepNoneCtrl() {
		logger.info(":::::::::::::::::::: goDocumentStepNone :::::::::::::::::::");
		return "document/document_sign_step_none";
	}

	//선택된 완료 계약서 다운로드
	@RequestMapping(method = RequestMethod.GET, value = "downloadDocumentSelectZip.do")
	@ResponseBody
	public ResponseEntity<JSONObject> downloadDocumentSelectZipCtrl(@ModelAttribute("DocumentMasterVO") DocumentMasterVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info(":::::::::::::::::::: downloadDocumentSelectZip :::::::::::::::::::");

		List<DocumentMasterVO> list = new ArrayList<DocumentMasterVO>();
		HttpStatus status = HttpStatus.OK;
		int result 		  = 0;
		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) status = HttpStatus.UNAUTHORIZED;
			else {
				vo.setBizId(loginVO.getBizId());
				String multiData   = vo.getMultiData();
				logger.info("bizId : "+vo.getBizId());
				String zipFilePath = MultipartFileUtil.getSystemTempPath() + vo.getBizId() + MultipartFileUtil.SEPERATOR;
				String zipFileName = "[전자계약]_"+"[" + DateUtil.getTimeStamp(7) +"]"+".zip";

				FileUtil.createFile(zipFilePath, zipFileName, "");

				if (StringUtil.isNotNull(multiData)) {
					String[] params = StringUtil.split(multiData, "|");
                    for (String s : params) {
                        String[] param = StringUtil.split(s, "-");
                        if (param.length > 1 && StringUtil.isNotNull(param[0])) {
                            DocumentMasterVO paramVO = new DocumentMasterVO();
                            paramVO.setBizId(vo.getBizId());
                            paramVO.setUserId(loginVO.getLoginId());
                            paramVO.setDocuId(param[0]);
                            paramVO.setConFileId(param[1]);
                            paramVO.setRUserName(param[2]);
                            paramVO.setDocuName(param[3]);
                            paramVO.setInsDate(param[4]);
                            paramVO.setPdfFile(zipFilePath + zipFileName);
                            list.add(paramVO);
                        }
                    }
				}

				vo.setPdfFile(zipFilePath+zipFileName);
				result = documentService.downloadDocumentSelectZip(list, loginVO);
				if (result == 0) status = HttpStatus.NOT_MODIFIED;
				File file = new File(zipFilePath+zipFileName);
				HttpUtil.setResponseFile(request, response, file, zipFileName);
				if (!file.exists()) status = HttpStatus.NO_CONTENT;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity(status);
	}

}
