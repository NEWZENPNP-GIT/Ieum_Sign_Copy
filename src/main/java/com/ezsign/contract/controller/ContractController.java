package com.ezsign.contract.controller;

import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ezsign.biz.service.BizService;
import com.ezsign.contract.vo.*;
import com.ezsign.framework.util.*;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.ezsign.code.service.CodeService;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.contract.dao.ContractDAO;
import com.ezsign.contract.service.ContractService;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.service.EmpService;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.google.gson.Gson;
import com.jarvis.pdf.util.FieldUtil;
import com.jarvis.pdf.vo.FormVO;
import com.jarvis.pdf.vo.PageVO;

import net.sf.json.JSONObject;

import static com.ezsign.framework.util.MultipartFileUtil.getSystemPath;

@Controller
@RequestMapping("/contract/")
public class ContractController extends BaseController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "codeService")
	private CodeService codeService;

	@Resource(name = "contractService")
	private ContractService contractService;

	@Resource(name = "empService")
	private EmpService empService;

	@Resource(name = "contractDAO")
	private ContractDAO contractDAO;


	// 일괄파일 업로드 (엑셀)
	@RequestMapping(method = RequestMethod.POST, value = "sendContractExcel.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendContractExcelCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result    = false;
		logger.info(":::::: sendContractExcel :::::: ");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				logger.info("bizId : " + vo.getBizId());
				logger.info("fileId : " + vo.getContractFileId());
				// 파일생성
				String szSavePath 			= MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total 					= resultFileList.size();
				logger.info("계약정보 XLS File Count : " + resultFileList.size());

				if (total == 1) {
					// 전달받은 파일리스트
                    for (FileVO fileVO : resultFileList) {
                        String xlsPath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
                        File file 	   = new File(xlsPath);
                        if (!file.exists()) {
                            logger.info("[sendContractExcel] XLS파일이 존재하지 않습니다.");
                            jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
                            result = false;
                        } else {
                            // DATABASE 처리
                            String webSocketKey = request.getParameter("webSocketKey");
                            ContractPersonVO resultContractVO = contractService.sendContractExcel(vo.getBizId(), xlsPath, vo.getContractFileId(), webSocketKey);
                            if (resultContractVO == null) {
                                jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
                            } else {
                                if (StringUtil.isNotNull(resultContractVO.getResultMessage())) {
                                    jsonObject.put("message", resultContractVO.getResultMessage());
                                } else if (StringUtil.isNotNull(resultContractVO.getKeepCount())) {
                                    if (resultContractVO.getResultMessage2().equals("")) {
                                        jsonObject.put("message", "총 " + resultContractVO.getOrgFileDataCount() + "건의 정보 중 " + resultContractVO.getKeepCount() + "건의 정보를 등록하였습니다.");
                                    } else {
                                        jsonObject.put("message", "총 " + resultContractVO.getOrgFileDataCount() + "건의 정보 중 " + resultContractVO.getKeepCount() + "건의 정보를 등록하였습니다.\n"
                                                + resultContractVO.getResultMessage2() + " 번 행의 사원번호 또는 생성일자가 올바르지 않습니다.");
                                    }
                                    result = true;
                                } else {
                                    logger.info("정보 등록 중 오류가 발생하였습니다. 업로드하신 양식이 올바른지 확인해주시기 바랍니다.");
                                    jsonObject.put("message", "정보 등록 중 오류가 발생하였습니다. 업로드하신 양식이 올바른지 확인해주시기 바랍니다.");
                                }
                            }
                        }
                    }
				} else {
					logger.info("XLS 파일이 잘못 전달되었습니다.");
					jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			logger.info("정보 등록 중 오류가 발생하였습니다.");
			jsonObject.put("message", "정보 등록 중 오류가 발생하였습니다.");
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// PDF 생성 (개별등록)
	@RequestMapping(method = RequestMethod.POST, value = "createContractPDF.do")
	@ResponseBody
	public ResponseEntity<JSONObject> createContractPersonPDFCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result    = false;
		logger.info(":::::::::::::::::::: createContractPDF :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setBizId(loginVO.getBizId());
				List<ContentVO> resultList = contractService.createContractPDF(vo, loginVO);

				result = true;

				jsonObject.put("total", resultList.size());
				jsonObject.put("data", resultList);
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 계약서 생성
	@RequestMapping(method = RequestMethod.POST, value = "createContractPDFList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> createContractPDFListCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result 	  = false;
		logger.info(":::::::::::::::::::: createContractPDFList :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				String webSocketKey = request.getParameter("webSocketKey");
				List<ContractPersonVO> resultList = contractService.createContractPDFList(vo, loginVO,webSocketKey);
				result = true;

				jsonObject.put("total", resultList.size());
				jsonObject.put("data", resultList);
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 스캔파일 등록
	@RequestMapping(method = RequestMethod.POST, value = "uploadContractFile.do")
	@ResponseBody
	public ResponseEntity<JSONObject> uploadContractFileCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result    = false;
		logger.info(":::::: uploadContractFile :::::: ");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				logger.info("bizId / userId /contractDate : " + vo.getBizId() + "_" + vo.getUserId() + "_" + vo.getContractDate());
				String szSavePath 			= MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total 					= resultFileList.size();
				logger.info("일반문서  File Count : " + resultFileList.size());

				if (total == 1) {
					// 전달받은 파일리스트
                    for (FileVO fileVO : resultFileList) {
                        String uploadPath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
                        File file		  = new File(uploadPath);
                        if (!file.exists()) {
                            logger.info("[uploadContractFile] 전자문서 파일이 존재하지 않습니다.");
                            jsonObject.put("message", "전자문서 파일이 존재하지 않습니다.");
                        } else {
                            long lFileSize = fileVO.getFileSize();
                            if (lFileSize > ContractService.MAX_FILE_SIZE) {
                                jsonObject.put("message", "업로드하신 전자문서 파일이 제한용량[5MB]을 넘겼습니다.");
                            } else {
                                // DATABASE 처리
                                ContractPersonVO resultContractVO = contractService.uploadContractFile(vo, fileVO, loginVO);
                                if (resultContractVO == null) {
                                    jsonObject.put("message", vo.getResultMessage());
                                } else {
                                    jsonObject.put("message", resultContractVO.getResultMessage());
                                    if (resultContractVO.getResultCode().equals("1")) {
                                        result = true;
                                    }
                                }
                            }
                        }
                    }
				} else {
					logger.info("전자문서 파일이 잘못 전달되었습니다.");
					jsonObject.put("message", "업로드 전자문서 파일이 존재하지 않습니다.");
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	
	// pdf 미리보기
	@RequestMapping(method = RequestMethod.GET, value = "getContractView.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractViewCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContractView :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String ipAddr = request.getHeader("X-Forwarded-For");
				if (ipAddr == null) ipAddr = request.getHeader("Proxy-Client-IP");
				if (ipAddr == null) ipAddr = request.getHeader("WL-Proxy-Client-IP");
				if (ipAddr == null) ipAddr = request.getHeader("HTTP_CLIENT_IP");
				if (ipAddr == null) ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
				if (ipAddr == null) ipAddr = request.getRemoteAddr();

				loginVO.setIpAddr(ipAddr);
				logger.info("bizId=>" + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				ContractPersonVO result = contractService.getContractView(vo, loginVO);

				if (result != null) {
					jsonObject.put("success", true);
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 근로계약양식 폼
	@RequestMapping(method = RequestMethod.GET, value = "getContractFormView.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractFormViewCtrl(@ModelAttribute("ContractPersonNewVO") ContractPersonNewVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContractFormView :::::::::::::::::::");

		try {

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				logger.info("bizId=>" + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				ContractPersonNewVO result = contractService.getContractFormView(vo);

				if (result != null) {
					jsonObject.put("success", true);
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 전자문서 리스트 조회
	@RequestMapping(method = RequestMethod.GET, value = "getContractList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractListCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContractList :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>" + loginVO.getBizId());
				logger.info("searchKey=>" + vo.getSearchKey());
				logger.info("searchValue=>" + vo.getSearchValue());
				logger.info("requesterId=>" + vo.getRequesterId());

				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				CodeVO codeVO = new CodeVO();
				codeVO.setGrpCommCode("101");
				List<CodeVO> codeList = codeService.getCodeList(codeVO);

				// 계약서 리스트
				List<ContractPersonVO> result = contractService.getContractPersonList(vo);

				int total = contractService.getContractPersonListCount(vo); //  총계약서 건수

				vo.setStatusCode("Y");
				int completeCount = contractService.getContractPersonListCount(vo); // 계약완료건수

				vo.setStatusCode("REQ");
				vo.setRequesterId(loginVO.getUserId());
				int requestCount = contractService.getContractPersonListCount(vo); // 승인대기건수

				vo.setStatusCode("Y");
				int downloadCount = contractService.getContractPersonDownloadCount(vo); //다운완료건수

				if (result != null && !result.isEmpty()) {
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("completeCount", completeCount);
					jsonObject.put("requestCount", requestCount);
					jsonObject.put("downloadCount", downloadCount);
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("completeCount", 0);
					jsonObject.put("requestCount", 0);
					jsonObject.put("downloadCount", 0);
					jsonObject.put("data", null);
					jsonObject.put("statusList", null);
				}

				if (codeList != null && !codeList.isEmpty()) jsonObject.put("statusList", codeList);
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	@RequestMapping(method = RequestMethod.GET, value = "getContractListFlutter.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractListFlutterCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContractListFlutter :::::::::::::::::::");

		try {


			if (vo.getUserId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>" + vo.getBizId());
				logger.info("searchKey=>" + vo.getSearchKey());
				logger.info("searchValue=>" + vo.getSearchValue());
				logger.info("requesterId=>" + vo.getRequesterId());

				CodeVO codeVO = new CodeVO();
				codeVO.setGrpCommCode("101");
				List<CodeVO> codeList = codeService.getCodeList(codeVO);

				List<ContractPersonVO> result = contractService.getContractPersonList(vo);
				// 2024.08.23 캔디app에서 아래 쿼리로 인해 속도느림 현상이 발생하였지만,
				// 실제 사용하지 않은쿼리로 파악되어서 주석처리함.
				// int total = contractService.getContractPersonListCount(vo); // 총 계약건수
				int total = 0;

				vo.setStatusCode("Y");
				// int completeCount = contractService.getContractPersonListCount(vo); // 계약완료건수
				int completeCount = 0;

				vo.setStatusCode("Y");
				// int downloadCount = contractService.getContractPersonDownloadCount(vo); //다운완료건수
				int downloadCount = 0;
				if (result != null && !result.isEmpty()) {
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("completeCount", completeCount);
					jsonObject.put("requestCount", 0);
					jsonObject.put("downloadCount", downloadCount);
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("completeCount", 0);
					jsonObject.put("requestCount", 0);
					jsonObject.put("downloadCount", 0);
					jsonObject.put("data", null);
					jsonObject.put("statusList", null);
				}

				if (codeList != null && !codeList.isEmpty()) {
					jsonObject.put("statusList", codeList);
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 전자문서 리스트수 조회
	@RequestMapping(method = RequestMethod.POST, value = "getContractListCount.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractListCountCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		int total 		  = 0;

		try {
			logger.info(":::::::::::::::::::: getContractListCount :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>" + loginVO.getBizId());
				logger.info("searchKey=>" + vo.getSearchKey());
				logger.info("searchValue=>" + vo.getSearchValue());
				logger.info("email=>" + vo.getEMail());
				logger.info("bizName=>" + vo.getBizName());

				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				total = contractService.getContractPersonListCount(vo);

				success = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		logger.info("total :  " + total);
		logger.info("success :  " + success);
		jsonObject.put("total", total);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 전자문서 리스트수 조회(부문 관리자)
	@RequestMapping(method = RequestMethod.POST, value = "getContractListGrpCount.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractListGrpCountCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		int total		  = 0;
		logger.info(":::::::::::::::::::: getContractListGrpCount :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>" + loginVO.getBizId());
				logger.info("searchKey=>" + vo.getSearchKey());
				logger.info("searchValue=>" + vo.getSearchValue());
				logger.info("email=>" + vo.getEMail());
				logger.info("bizName=>" + vo.getBizName());
				if (vo.getBizId() == null) { vo.setBizId(loginVO.getBizId()); }
				total = contractService.getContractPersonUserGrpListCount(vo);

				success = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		logger.info("total :  " + total);
		logger.info("success :  " + success);
		jsonObject.put("total", total);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	
	// 전자계약 로그 리스트 조회
	@RequestMapping(method = RequestMethod.GET, value = "getContractLogList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractLogListCtrl(@ModelAttribute("ContractPersonLogVO") ContractPersonLogVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContractLogList :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>" + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());

				List<ContractPersonLogVO> result = contractService.getContractPersonLogList(vo);

				if (result != null && !result.isEmpty()) {
					jsonObject.put("success", true);
					jsonObject.put("total", result.size());
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 전자계약 양식 리스트 조회
	@RequestMapping(method = RequestMethod.GET, value = "getContractNewList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractNewListCtrl(@ModelAttribute("ContractPersonNewVO") ContractPersonNewVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContractNewList :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>" + loginVO.getBizId());

				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				List<ContractPersonNewVO> result = contractService.getContractPersonNewList(vo);

				if (result != null && !result.isEmpty()) {
					jsonObject.put("success", true);
					jsonObject.put("total", result.size());
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	
	// 계약 양식 폼 수정
	@RequestMapping(method = RequestMethod.POST, value = "updContractForm.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updContractFormCtrl(@ModelAttribute("ContractFormVO") ContractFormVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: updContractForm :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>" + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());

				int result = contractService.updContractForm(vo);

				if (result != 0) {
					jsonObject.put("success", true);
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 계약양식 폼 리스트 조회
	@RequestMapping(method = RequestMethod.GET, value = "getContractFormList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractFormListCtrl(@ModelAttribute("ContractFormVO") ContractFormVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContractFormList :::::::::::::::::::");

		try {

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>" + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());

				List<ContractFormVO> result = contractService.getContractFormList(vo);

				if (result != null && !result.isEmpty()) {
					jsonObject.put("success", true);
					jsonObject.put("total", result.size());
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 양식등록하기
	@RequestMapping(method = RequestMethod.POST, value = "contractNewUpload.do")
	@ResponseBody
	public ResponseEntity<JSONObject> contractNewUploadCtrl(@ModelAttribute("ContractPersonNewVO") ContractPersonNewVO vo, HttpServletRequest request) {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result    = false;
		logger.info(":::::: contractNewUpload :::::: ");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo.getBizId() == null) { vo.setBizId(loginVO.getBizId()); }
				// 로그인한 userType 세팅
				if (loginVO.getUserType() != null && !loginVO.getUserType().isEmpty()) vo.setInsUserType(loginVO.getUserType());

				logger.info("bizId : " + vo.getBizId());
				String szSavePath 			= MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total					= resultFileList.size();
				logger.info("전자문서 File Count : " + resultFileList.size());

				if (total == 1) {
					// 전달받은 파일리스트
                    for (FileVO fileVO : resultFileList) {
                        String filePath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();

                        File file = new File(filePath);
                        if (!file.exists()) {
                            logger.info("[contractNewUpload] 원본파일이 존재하지 않습니다.");
                            jsonObject.put("message", "원본파일이 존재하지 않습니다.");
                        } else {
                            // DATABASE 처리
                            ContractPersonNewVO resultVO = contractService.contractNewUpload(vo, fileVO);
                            if (resultVO == null) {
                                jsonObject.put("message", "파일이 존재하지 않습니다.");
                            } else {
                                jsonObject.put("message", resultVO.getMessage());
                                result = true;
                            }
                        }
                    }
				} else {
					logger.info("파일이 잘못 전달되었습니다.");
					jsonObject.put("message", "파일이 존재하지 않습니다.");
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 양식 상태 수정
	@RequestMapping(method = RequestMethod.POST, value = "contractNewUpdateStatus.do")
	@ResponseBody
	public ResponseEntity<JSONObject> contractNewUpdateStatusCtrl(@ModelAttribute("ContractPersonNewVO") ContractPersonNewVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		logger.info(":::::::::::::::::::: contractNewUpdateStatus :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				contractService.contractNewUpdateStatus(vo);
				success = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	
	//양식수정
	@RequestMapping(method = RequestMethod.POST, value = "contractNewUpdateSign.do")
	@ResponseBody
	public ResponseEntity<JSONObject> contractNewUpdateSignCtrl(@ModelAttribute("ContractPersonNewVO") ContractPersonNewVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		logger.info(":::::::::::::::::::: contractNewUpdateSign :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>" + loginVO.getBizId());

				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				contractService.contractNewUpdateSign(vo);
				success = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 변환완료된 양식등록(조건 양식 등록)
	@RequestMapping(method = RequestMethod.POST, value = "contractNewUpdate.do")
	@ResponseBody
	public ResponseEntity<JSONObject> contractNewUpdateCtrl(@ModelAttribute("ContractPersonNewVO") ContractPersonNewVO vo, HttpServletRequest request) {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result    = false;
		logger.info(":::::: contractNewUpdate :::::: ");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				logger.info("bizId : " + vo.getBizId());
				logger.info("fileId : " + vo.getFileId());

				// 파일생성
				String szSavePath = MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = resultFileList.size();
				logger.info("근로계약서 서식파일 File Count : " + resultFileList.size());

				if (total == 3) {
					// 전달받은 파일리스트
					FileVO pdfFileVO   = new FileVO();
					FileVO xlsFileVO   = new FileVO();
					FileVO alterFileVO = new FileVO();

                    for (FileVO fileVO : resultFileList) {
                        String filePath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
                        File file 		= new File(filePath);

                        if (!file.exists()) {
                            logger.info("[contractNewUpload] 업로드파일이 존재하지 않습니다.");
                            jsonObject.put("message", "업로드파일이 존재하지 않습니다.");
                        } else {
                            if 		(fileVO.getFileParamName().equals("pdfFile0"))   pdfFileVO   = fileVO;
                            else if (fileVO.getFileParamName().equals("xlsFile0"))   xlsFileVO   = fileVO;
                            else if (fileVO.getFileParamName().equals("alterFile0")) alterFileVO = fileVO;
                        }
                    }

					// DATABASE 처리
					ContractPersonNewVO resultVO = contractService.contractNewUpdate(vo.getBizId(), vo.getFileId(), pdfFileVO, xlsFileVO, alterFileVO);
					if (resultVO == null) {
						jsonObject.put("message", "파일이 존재하지 않습니다.");
					} else {
						jsonObject.put("message", "파일이 등록되었습니다.");
						result = true;

					}
				} else {
					logger.info("파일이 잘못 전달되었습니다.");
					jsonObject.put("message", "파일이 존재하지 않습니다.");
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 일괄다운로드
	@RequestMapping(method = RequestMethod.GET, value = "downloadContractZip.do")
	@ResponseBody
	public void downloadContractZipCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String contractEmail = request.getParameter("contractEmail");
		vo.setContractEMail(contractEmail);

		try {
			logger.info(":::::::::::::::::::: downloadContractAll :::::::::::::::::::");

			logger.info("======================================");
			logger.info("bizId : " + vo.getBizId());
			logger.info("bizName : " + vo.getBizName());
			logger.info("contractDateFrom : " + vo.getContractDateFrom());
			logger.info("contractDateTo : " + vo.getContractDateTo());
			logger.info("getDownloadYn:" + vo.getDownloadYn());
			logger.info("getLoginType:" + vo.getLoginType());
			logger.info("======================================");

			if (vo.getLoginType() == null) vo.setLoginType("false");

			String zipFilePath = MultipartFileUtil.getSystemTempPath() + vo.getBizId() + MultipartFileUtil.SEPERATOR;
			String zipFileName = vo.getBizName() + "_완료계약문서_" + DateUtil.getTimeStamp(3) + ".zip";
			String zipFileURL  = MultipartFileUtil.getZipFileURL() + vo.getBizId() + MultipartFileUtil.SEPERATOR + zipFileName;

			FileUtil.createFile(zipFilePath, zipFileName, "");
			vo.setPdfFile(zipFilePath + zipFileName);
			int result = contractService.downloadContractZip(vo);
			if (result == 0) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}

			File file = new File(zipFilePath + zipFileName);

			//대용량일 경우 서버에 보관한다.
			if (vo.getDownloadYn().equals("Y")) {
				HttpUtil.setResponseFile(request, response, file, zipFileName);
			} else {
				logger.info("======================================");
				logger.info("===일괄 다운 파일 이메일 발송 로직 시작===");
				logger.info("======================================");
				logger.info("send fileURL : " + zipFileURL);
				contractService.sendEmailWithFile(vo, zipFileURL, zipFileName);
				logger.info("send Email to  : " + vo.getContractEMail());
				logger.info("sms Y/N  : " + vo.getSmsYN());

				if (vo.getSmsYN().equals("Y")) logger.info("send Phone to : " + vo.getPhoneNum());

				logger.info("======================================");
			}
			if (!file.exists()) {
				response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				return;
			}
			response.setStatus(HttpServletResponse.SC_OK);

		} catch (Exception ex) {
			logger.info("======================================");
			logger.info("===일괄 다운 실패 이메일 발송 로직 시작===");
			contractService.sendErrorEmail(vo);
			logger.info("bizName : " + vo.getBizName());
			logger.info("send Email to  : " + vo.getContractEMail());
			logger.info("sms Y/N  : " + vo.getSmsYN());

			if (vo.getSmsYN().equals("Y")) logger.info("send Phone to : " + vo.getPhoneNum());

			logger.info("======================================");
			logService.error(ex.getMessage(), new Throwable(ex));
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}


	//선택된 완료 계약서 다운로드
	@RequestMapping(method = RequestMethod.GET, value = "downloadContractSelectZip.do")
	@ResponseBody
	public ResponseEntity<JSONObject> downloadContractSelectZipCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<ContractPersonVO> list = new ArrayList<>();
		HttpStatus status 		  	= HttpStatus.OK;
		int result 		  		  	= 0;
		logger.info(":::::::::::::::::::: downloadContractSelect :::::::::::::::::::");

		try {

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setBizId(loginVO.getBizId());
				String multiData = vo.getMultiData();

				logger.info("bizId : "+vo.getBizId());
				logger.info("contractDate : "+ vo.getContractDate());
				logger.info("contractDateFrom : " + vo.getContractDateFrom());

				String zipFilePath = MultipartFileUtil.getSystemTempPath() + vo.getBizId() + MultipartFileUtil.SEPERATOR;
				String zipFileName = "[전자문서]_"+"[" + DateUtil.getTimeStamp(7) +"]"+".zip";

				FileUtil.createFile(zipFilePath, zipFileName, "");

				if (StringUtil.isNotNull(multiData)) {
					String[] params = StringUtil.split(multiData, "|");
                    for (String s : params) {
                        String[] param = StringUtil.split(s, "-");
                        if (param.length > 1 && StringUtil.isNotNull(param[0])) {
                            ContractPersonVO paramVO = new ContractPersonVO();
                            paramVO.setBizId(vo.getBizId());
                            paramVO.setUserId(param[0]);
                            paramVO.setContractDate(param[1]);
                            paramVO.setContId(param[2]);
                            paramVO.setContractDateFrom(vo.getContractDateFrom());
                            paramVO.setContractDateTo(vo.getContractDateTo());
                            paramVO.setPdfFile(zipFilePath + zipFileName);
                            paramVO.setFileType("C");
                            list.add(paramVO);
                        }
                    }
				}

				//선택된 전자문서에 대해서 감사추적 인증서 작성
				List<ContractPersonVO> auditTrailCertVO = contractService.getSelectAuditTrailCertificate(list, loginVO);

				if (auditTrailCertVO != null) {
					if (!auditTrailCertVO.isEmpty()) result = contractService.downloadContractSelectZip(auditTrailCertVO, loginVO);
					else 							 status = HttpStatus.NOT_MODIFIED;

					File file = new File(zipFilePath + zipFileName);
					HttpUtil.setResponseFile(request, response, file, zipFileName);

					if 		(!file.exists()) status = HttpStatus.NO_CONTENT;
					else if (result == 0) 	 status = HttpStatus.NOT_MODIFIED;
				}
			}

		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity(status);
	}

	
	// 계약서 양식 삭제
	@RequestMapping(method = RequestMethod.POST , value = "delContractPersonNew.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delContractPersonNewCtrl(@ModelAttribute("ContractPersonNewVO") ContractPersonNewVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: delContractPersonNew :::::::::::::::::::");

		try {

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());

				ContractPersonNewVO result = contractService.delContractPersonNew(vo);

				if (result.getResult() != 0) {
					jsonObject.put("success", true);
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("data", null);
					jsonObject.put("message", result.getMessage());
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 계약서삭제
	@RequestMapping(method = RequestMethod.POST , value = "delContractPerson.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delContractPersonCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: delContractPerson :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());

				int result= contractService.delContractPerson(vo, loginVO);

				if (result > 0) {
					jsonObject.put("success", true);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("message", vo.getResultMessage());
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 슈퍼관리자가 진행중인 전자문서를 선택삭제하는 기능
	@RequestMapping(method = RequestMethod.POST , value = "delContractPersonSelectList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delContractPersonSelectListCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: delContractPersonSelectList :::::::::::::::::::");

		try {
			SessionVO loginVO 			= SessionUtil.getSession(request);
			List<ContractPersonVO> list = new ArrayList<>();
			int result 					= 0;

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String multiData = vo.getMultiData();
				logger.info("multiData : "+multiData);
				if (StringUtil.isNotNull(multiData)) {

					//형식은 userId_contractDate_contId_digitSign|
					String[] params = StringUtil.split(multiData, "|");
					for (int i=0; i<params.length-1; i++) {
						String hashData = params[i];
						String[] param  = StringUtil.split(hashData, "-");
						if (param.length>1 && StringUtil.isNotNull(param[0])) {
							ContractPersonVO paramVO = new ContractPersonVO();
							paramVO.setServiceId(ContractService.SERVICE_ID);
							paramVO.setBizId(vo.getBizId());
							paramVO.setUserId(param[0]);
							paramVO.setContractDate(param[1]);
							paramVO.setContId(param[2]);
							list.add(paramVO);
						}
					}
					if (!list.isEmpty()) result = contractService.delContractPersonSelectList(list, loginVO);
					else 		 		 status = HttpStatus.NOT_MODIFIED;

					if (result > 0) {
						jsonObject.put("success", true);
					} else {
						jsonObject.put("success", false);
						jsonObject.put("message", vo.getResultMessage());
					}
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 계약서삭제
	@RequestMapping(method = RequestMethod.POST , value = "delContractPersonTemp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delContractPersonTempCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result    = false;
		logger.info(":::::::::::::::::::: delContractPersonTemp :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				String webSocketKey = request.getParameter("webSocketKey") ;
				int count = contractService.delContractPersonTemp(vo,webSocketKey);

				if (count < 0) jsonObject.put("message", vo.getResultMessage());

				result = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 계약서 재작성
	@RequestMapping(method = RequestMethod.POST , value = "generateContractPDF.do")
	@ResponseBody
	public ResponseEntity<JSONObject> generateContractPDFCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result    = false;
		logger.info(":::::::::::::::::::: generateContractPDF :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setBizId(loginVO.getBizId());
				List<ContentVO> resultList = contractService.generateContractPDF(vo, loginVO);

				if (resultList!=null) {
					result = true;
					jsonObject.put("total", resultList.size());
					jsonObject.put("data", resultList);
				} else {
					jsonObject.put("message", vo.getResultMessage());
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 전자서명 일괄 서명
	@RequestMapping(method = RequestMethod.POST , value = "setSignHashdDataMulti.do")
	@ResponseBody
	public ResponseEntity<JSONObject> setSignHashdDataMultiCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		ContractPersonVO result 	= null;
		HttpStatus status 			= HttpStatus.OK;
		List<ContractPersonVO> list = new ArrayList<>();
		logger.info(":::::::::::::::::::: setSignHashdDataMulti :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setBizId(loginVO.getBizId());
				String multiData = vo.getMultiData();

				if (StringUtil.isNotNull(multiData)) {
					//형식은 userId_contractDate_contId_digitSign|
					String[] params = StringUtil.split(multiData, "|");
                    for (String hashData : params) {
                        String[] param = StringUtil.split(hashData, "_");
                        if (param.length > 1 && StringUtil.isNotNull(param[0])) {
                            ContractPersonVO paramVO = new ContractPersonVO();
                            paramVO.setServiceId(ContractService.SERVICE_ID);
                            paramVO.setBizId(vo.getBizId());
                            paramVO.setUserId(param[0]);
                            paramVO.setContractDate(param[1]);
                            paramVO.setContId(param[2]);
                            paramVO.setDigitSign(param[3]);
                            list.add(paramVO);
                        }
                    }
					if (!list.isEmpty()) {
						String webSocketKey = request.getParameter("webSocketKey");
						result = contractService.setContractSignHashDataMulti(list, loginVO, webSocketKey);
					} else {
						status = HttpStatus.NOT_MODIFIED;
					}
				}
				jsonObject.put("success", result);
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 전자문서 전송
	@RequestMapping(method = RequestMethod.POST , value = "sendContractMulti.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendContractMultiCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		boolean result 	  = false;
		HttpStatus status = HttpStatus.OK;
		List<ContractPersonVO> list = new ArrayList<>();
		logger.info(":::::::::::::::::::: sendContractMulti :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());
				String multiData = vo.getMultiData();

				if (StringUtil.isNotNull(multiData)) {
					String[] params = StringUtil.split(multiData, "|");
					for (int i=0; i<params.length-1; i++) {
						ContractPersonVO paramVO = new ContractPersonVO();
						paramVO.setContId(params[i]);
						paramVO.setSendType(vo.getSendType());
						paramVO.setAuthType(vo.getAuthType());
						paramVO.setAuthCode(vo.getAuthCode());
						paramVO.setAuthCodeSel(vo.getAuthCodeSel());
						paramVO.setExpireDate(vo.getExpireDate());
						paramVO.setComment(URLDecoder.decode(vo.getComment()));
						list.add(paramVO);
					}
					if (!list.isEmpty()) {
						String webSocketKey = request.getParameter("webSocketKey");
						ContractPersonVO resultVo;
						resultVo = contractService.sendContractPersonMulti(list, loginVO,webSocketKey);

						if (resultVo.getResultCode().equals("100")) result = true;
						else 										jsonObject.put("message", resultVo.getResultMessage());

					} else status = HttpStatus.NOT_MODIFIED;
				}
				jsonObject.put("success", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 전자문서 재전송
	@RequestMapping(method = RequestMethod.POST , value = "sendContractMultiComplete.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendContractMultiCompleteCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		boolean result    = false;
		HttpStatus status = HttpStatus.OK;
		List<ContractPersonVO> list = new ArrayList<>();
		logger.info(":::::::::::::::::::: sendContractMultiComplete :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setBizId(loginVO.getBizId());
				String multiData = vo.getMultiData();

				if (StringUtil.isNotNull(multiData)) {
					String[] params = StringUtil.split(multiData, "|");
                    for (String s : params) {
                        String[] param = StringUtil.split(s, "-");
                        if (param.length > 1 && StringUtil.isNotNull(param[0])) {
                            ContractPersonVO paramVO = new ContractPersonVO();
                            paramVO.setServiceId(ContractService.SERVICE_ID);
                            paramVO.setBizId(vo.getBizId());
                            paramVO.setUserId(param[0]);
                            paramVO.setContractDate(param[1]);
                            paramVO.setContId(param[2]);
                            paramVO.setSendType(vo.getSendType());
                            list.add(paramVO);
                        }
                    }
					if (!list.isEmpty()) result = contractService.sendContractPersonMultiComplete(list, loginVO);
					else 				 status = HttpStatus.NOT_MODIFIED;

				}
				jsonObject.put("success", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	@RequestMapping(method = RequestMethod.GET , value = "recvContractPerson.do")
	public ModelAndView recvContractPersonCtrl(@RequestParam("id") String id, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: recvContractPerson :::::::::::::::::::");
		JSONObject jsonObject = new JSONObject();
		ModelAndView view     = new ModelAndView();
		String viewURL 		  = "";

		// ID로 계약서 정보 조회
		ContractPersonVO vo = new ContractPersonVO();
		vo.setDigitNonce(id);
		ContractPersonVO result = contractService.getContractNonce(vo);

		logger.info("recvContractPerson  id=>"+id);
		view.addObject("id", id);

		if (result!=null) {
			view.addObject("companyName", result.getBizName());
			view.addObject("documentName", result.getContractFileName());

			if (StringUtil.isNotNull(result.getExpireDate()) &&
					DateUtil.diffOfDate(result.getExpireDate(), DateUtil.getTimeStamp(3)) < 0) {
				// 문서 유통기한이 지난경우 별도 페이지 표시
				viewURL = "/contract/contract_sign_step_expire";
			} else if (StringUtil.isNotNull(result.getStatusCode()) &&
					!("3".equals(result.getStatusCode()) || "4".equals(result.getStatusCode())
							|| "P1".equals(result.getStatusCode()) || "P2".equals(result.getStatusCode()))){
				// 문서 상태가 전송중, 서명대기가 아닌경우 (3 : 전송 4: 서명대기 P1 : 정보입력요청 P2 : 정보입력확인)
				logger.info("문서상태가 문서조회아님  Status =>"+result.getStatusCode());
				viewURL = "/contract/contract_sign_step_none";
			} else {
				jsonObject.put("data", result);
				view.addObject("contract", jsonObject.toString());
				//완료된 계약서에도 본인확인을 진행한다.
				viewURL = "/contract/contract_sign_step_1";
			}
		} else {
			viewURL = "/contract/contract_sign_step_none";
		}
		view.setViewName(viewURL);
		return view;
	}


	// 계약서 PDF 보기
	@RequestMapping(method = RequestMethod.GET , value = "contractPersonView.do")
	public ModelAndView contractPersonViewCtrl(@RequestParam("id") String id, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: contractPersonView :::::::::::::::::::");
		JSONObject jsonObject = new JSONObject();
		ModelAndView view     = new ModelAndView();
		boolean success		  = false;
		int total 			  = 0;

		try {
			// 유효기간 이후 시 유효기간 완료 페이지로 이동
			ContractPersonVO voTemp = new ContractPersonVO();
			voTemp.setDigitNonce(id);
			ContractPersonVO result = contractService.getContractNonce(voTemp);

			logger.info("recvContractPerson  id=>"+id);
			view.addObject("id", id);

			if (result!=null) {
				view.addObject("companyName", result.getBizName());
				view.addObject("documentName", result.getContractFileName());

				if (StringUtil.isNotNull(result.getExpireDate()) &&
						DateUtil.diffOfDate(result.getExpireDate(), DateUtil.getTimeStamp(3)) < 0) {
					// 문서 유통기한이 지난경우 별도 페이지 표시
					view.setViewName("/contract/contract_sign_step_expire");
					return view;
				}
			}

			// ID로 계약서 정보 조회
			ContractPersonVO vo = new ContractPersonVO();
			vo.setServiceId(ContractService.SERVICE_ID);
			vo.setDigitNonce(id);
			List<PageVO> dataVO = contractService.getContractPersonPdfView(vo);
			if (dataVO!=null) {
				jsonObject.put("data", dataVO);
				total = dataVO.size();
			}

			// PDF Field객체 가져오기
			if (StringUtil.isNotNull(vo.getPdfFile())) {
				logger.info("Temp PDF File ==="+vo.getPdfFile());
				InputStream is 		= Files.newInputStream(Paths.get(vo.getPdfFile()));
				List<FormVO> formVO = FieldUtil.getAllField(is, false);
				jsonObject.put("form", formVO);
				is.close();
			}

			ContractPersonVO resultContractPersonVO = contractService.getContractNonce(vo);
			if (resultContractPersonVO!=null) {
				jsonObject.put("contract", resultContractPersonVO);
				success = true;
			}

			ContractPersonNewVO resultContractPersonNewVO = contractService.getContractPersonSign(vo);
			if (resultContractPersonNewVO != null) {
				jsonObject.put("signUserType", resultContractPersonNewVO.getSignUserType());
				jsonObject.put("signBizType", resultContractPersonNewVO.getSignBizType());
				jsonObject.put("signCustomerType", resultContractPersonNewVO.getSignCustomerType());
				jsonObject.put("correctionType", resultContractPersonNewVO.getCorrectionType());
			} else { //양식이 제거되었을 경우
				jsonObject.put("signUserType", "1");
				jsonObject.put("signBizType", "1");
				jsonObject.put("signCustomerType", "0");
				jsonObject.put("correctionType", "N");
			}

			jsonObject.put("contractId", id);
			jsonObject.put("id", "sign_2");
			jsonObject.put("resultCode", vo.getResultCode());
			jsonObject.put("resultMessage", vo.getResultMessage());
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		view.addObject("success", success);
		view.addObject("total", total);
		view.addObject("result",jsonObject.toString());
		view.setViewName("/contract/contract_sign_step_2");
		return view;
	}

	@RequestMapping(method = RequestMethod.GET , value = "contractCustomerView.do")
	public ModelAndView contractCustomerViewCtrl(@RequestParam("id") String id, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: contractCustomerView :::::::::::::::::::");
		JSONObject jsonObject = new JSONObject();
		ModelAndView view     = new ModelAndView();
		boolean success       = false;
		int total 			  = 0;

		try {
			// ID로 계약서 정보 조회
			ContractPersonVO vo = new ContractPersonVO();
			vo.setServiceId(ContractService.SERVICE_ID);
			vo.setDigitNonce(id);
			List<PageVO> dataVO = contractService.getContractPersonPdfView(vo);
			if (dataVO!=null) {
				jsonObject.put("data", dataVO);
				total = dataVO.size();
			}

			// PDF Field객체 가져오기
			if (StringUtil.isNotNull(vo.getPdfFile())) {
				logger.info("Temp PDF File ==="+vo.getPdfFile());
				InputStream is = Files.newInputStream(Paths.get(vo.getPdfFile()));
				List<FormVO> formVO = FieldUtil.getAllField(is, false);
				jsonObject.put("form", formVO);
				is.close();
			}

			ContractPersonVO resultContractPersonVO = contractService.getContractNonce(vo);
			if (resultContractPersonVO!=null) {
				jsonObject.put("contract", resultContractPersonVO);
				success = true;
			}

			ContractPersonNewVO resultContractPersonNewVO = contractService.getContractPersonSign(vo);
			if (resultContractPersonNewVO != null) {
				jsonObject.put("signUserType", resultContractPersonNewVO.getSignUserType());
				jsonObject.put("signBizType", resultContractPersonNewVO.getSignBizType());
				jsonObject.put("signCustomerType", resultContractPersonNewVO.getSignCustomerType());
				jsonObject.put("correctionType", resultContractPersonNewVO.getCorrectionType());
			} else { //양식이 제거되었을 경우
				jsonObject.put("signUserType", "1");
				jsonObject.put("signBizType", "1");
				jsonObject.put("signCustomerType", "0");
				jsonObject.put("correctionType", "N");
			}
			jsonObject.put("contractId", id);
			jsonObject.put("id", "sign_2");
			jsonObject.put("resultCode", vo.getResultCode());
			jsonObject.put("resultMessage", vo.getResultMessage());
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		view.addObject("success", success);
		view.addObject("total", total);
		view.addObject("result",jsonObject.toString());
		view.setViewName("/contract/contract_sign_step_2_customer");

		return view;
	}


	//
	@RequestMapping(method = RequestMethod.GET , value = "contractPersonEtcView.do")
	public ModelAndView contractPersonEtcViewCtrl(@RequestParam("id") String id, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: contractPersonEtcView :::::::::::::::::::");
		ModelAndView view = new ModelAndView();
		boolean success   = false;

		try {
			// ID로 계약서 정보 조회
			ContractPersonVO personVo = new ContractPersonVO();
			personVo.setDigitNonce(id);
			ContractPersonVO result   = contractService.getContractNonce(personVo);
			if (result == null) {
				logger.info("전자문서 정보가 존재하지 않음. =>"+id);
				view.addObject("success", success);
				view.setViewName("/contract/contract_sign_step_none");
				return view;
			}

			// 계약서의 양식정보 조회
			ContractPersonNewVO newVO = new ContractPersonNewVO();
			newVO.setContractId(result.getContractFileId());
			ContractPersonNewVO resultNewVO = contractService.getContractPersonSign(result);
			if (resultNewVO == null) {
				logger.info("양식 정보가 존재하지 않음. =>"+id);
				view.addObject("success", success);
				view.setViewName("/contract/contract_sign_step_none");
				return view;
			}

			if (resultNewVO.getUsePre().equals("Y") && (result.getStatusCode().equals("P1") || result.getStatusCode().equals("P2"))){
				if (result.getStatusCode().equals("P1")) {
					//상태코드 변경
					ContractPersonVO contractVO = new ContractPersonVO();
					contractVO.setContId(result.getContId());
					contractVO.setStatusCode("P2");
					contractVO.setDigitNonce(result.getDigitNonce());
					contractVO.setComment(result.getComment());
					contractDAO.updContractPerson(contractVO);

					// 로그테이블 생성
					ContractPersonLogVO logVO = new ContractPersonLogVO();
					logVO.setContId(result.getContId());
					logVO.setServiceId(result.getServiceId());
					logVO.setBizId(result.getBizId());
					logVO.setUserId(result.getUserId());
					logVO.setContractDate(result.getContractDate());
					logVO.setContractId(result.getContractId());
					logVO.setLogType("P2");
					logVO.setLogMessage(result.getEmpName()+"님께서 정보입력 요청을 확인하였습니다.");
					logVO.setInsEmpNo(result.getUserId());
					contractDAO.insContractPersonLog(logVO);
				}
				//전자문서 필드 조회
				ContractFormVO formVO = new ContractFormVO();
				formVO.setContId(result.getContId());
				List<ContractFormVO> formList = contractDAO.getContractFormList(formVO);

				JSONObject resultObject = new JSONObject();
				resultObject.put("bizName", result.getBizName());
				resultObject.put("empName", result.getEmpName());
				resultObject.put("contractFileName", result.getContractFileName());
				resultObject.put("comment", result.getComment());
				view.addObject("contract", resultObject.toString());

				view.addObject("formList", new Gson().toJsonTree(formList).toString());
				view.setViewName(resultNewVO.getPreUrl());
				return view;
			} else if (resultNewVO.getUsePre().equals("Y") && result.getStatusCode().equals("P3")){
				view.setViewName("/contract/contract_sign_step_pre_success");
				return view;
			} else if(resultNewVO.getUseEtc().equals("Y")){
				//설문이 필요한 전자문서일 경우
				view.addObject("success", true);
				JSONObject resultObject = new JSONObject();
				resultObject.put("bizName", result.getBizName());
				resultObject.put("empName", result.getEmpName());
				resultObject.put("contractFileName", result.getContractFileName());
				resultObject.put("comment", result.getComment());
				view.addObject("contract", resultObject.toString());
				view.setViewName(resultNewVO.getEtcUrl());
				logger.info("[contractPersonEtcView] 이동 : "+resultNewVO.getEtcUrl());
				return view;
			} else {
				return new ModelAndView("redirect:/contract/contractPersonView.do?id="+id);
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
	}



	@RequestMapping(method = RequestMethod.GET , value = "getContactPersonAuth.do")
	public ResponseEntity<JSONObject> getContactPersonAuthCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		boolean result    = false;
		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContactPersonAuth :::::::::::::::::::");

		try {
			// ID로 계약서 정보 조회
			ContractPersonVO contractVO = new ContractPersonVO();
			contractVO.setDigitNonce(vo.getContractId());

			ContractPersonVO resultVO = contractService.getContractNonce(contractVO);
			if (resultVO!=null && vo.getUserDate().equals(resultVO.getUserDate())) result = true;

			logger.info("result=>"+result);
			jsonObject.put("success", result);
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 계약서 저장
	@RequestMapping(method = RequestMethod.POST , value = "saveContract.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveContractCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		logger.info(":::::::::::::::::::: saveContract :::::::::::::::::::");

		try {
			vo.setServiceId(ContractService.SERVICE_ID);

			// 파라미터생성
			String strMultiData   = vo.getMultiData();
			String[] arrMultiData = StringUtil.split(strMultiData, "|");

            for (String arrMultiDatum : arrMultiData) {
                int idx = arrMultiDatum.indexOf("-");
                if (idx > -1) {
                    String formId    	  = arrMultiDatum.substring(0, idx);
                    String formValue 	  = arrMultiDatum.substring(idx + 1);
                    ContractFormVO formVO = new ContractFormVO();
                    formVO.setFormDataId(formId);
                    formVO.setFormDataValue(formValue);
                    vo.addForm(formVO);

                    logger.info("param :" + formId + "/" + formValue + ".");
                }
            }

			// 파일생성
			String szSavePath 			= MultipartFileUtil.getSystemTempPath();
			List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
			logger.info("이미지 첨부 File Count : " + resultFileList.size());
			// 전달받은 파일리스트
            for (FileVO fileVO : resultFileList) {
                String imgPath 		  = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
                ContractFormVO formVO = new ContractFormVO();
                formVO.setFormDataId(fileVO.getFileKey());
                formVO.setFormDataImage(imgPath);
                vo.addForm(formVO);
                logger.info("param img :" + fileVO.getFileKey() + "/" + imgPath + ".");
            }
			boolean result = contractService.saveContract(vo);

			if (result) success = true;

			jsonObject.put("message", vo.getResultMessage());
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 서명저장
	@RequestMapping(method = RequestMethod.POST , value = "signContract.do")
	@ResponseBody
	public ResponseEntity<JSONObject> signContractCtrl(@ModelAttribute("UserSignDataVO") UserSignDataVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: signContract :::::::::::::::::::");
			vo.setServiceId(contractService.SERVICE_ID);
			logger.info("signContract : "+vo.getId() + ", "+vo.getKey() + ", " + vo.getNum() + ", " + vo.getLine());

			//digitNonce(ID)를 통해 contId조회
			ContractPersonVO personVO = new ContractPersonVO();
			personVO.setDigitNonce(vo.getId());
			ContractPersonVO resPersonVO = contractService.getContractNonce(personVO);

			//contId를 통해 서명 참여자 조회
			if (resPersonVO != null) {
				ContractPersonNewVO resPersonNewVO = contractService.getContractPersonSign(resPersonVO);
				jsonObject.put("data", resPersonNewVO);
			}
			boolean result = contractService.signContract(vo);

			if (result) success = true;

			jsonObject.put("message", vo.getMessage());
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	@RequestMapping(method = RequestMethod.POST , value = "signContractCustomer.do")
	@ResponseBody
	public ResponseEntity<JSONObject> signContractCustomerCtrl(@ModelAttribute("UserSignDataVO") UserSignDataVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		logger.info(":::::::::::::::::::: signContractCustomer :::::::::::::::::::");

		try {
			vo.setServiceId(contractService.SERVICE_ID);
			logger.info("signContract : "+vo.getId() + ", "+vo.getKey() + ", " + vo.getNum());

			//digitNonce(ID)를 통해 contId조회
			ContractPersonVO personVO = new ContractPersonVO();
			personVO.setDigitNonce(vo.getId());
			ContractPersonVO resPersonVO = contractService.getContractNonce(personVO);
			//contId를 통해 서명 참여자 조회
			if (resPersonVO != null) {
				ContractPersonNewVO resPersonNewVO = contractService.getContractPersonSign(resPersonVO);
				vo.setBizId(resPersonNewVO.getBizId());
				jsonObject.put("data", resPersonNewVO);
			}

			String szSavePath	 		= MultipartFileUtil.getSystemTempPath();
			List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
			int total = resultFileList.size();
			logger.info("기업 직인이미지 File Count : "+resultFileList.size());

			if (total==1) {
				// 전달받은 파일리스트
                for (FileVO fileVO : resultFileList) {
                    String imgPath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
                    // 직인이미지 존재시 복사 및 DB처리
                    if (com.jarvis.common.util.StringUtil.isNotNull(imgPath)) {

                        // 년월 형식 생성 (yyyyMM)
                        String szMonthPath = MultipartFileUtil.SEPERATOR + DateUtil.getTimeStamp(14);
                        // 년월일 형식 생성 (yyyyMMdd)
                        String szDayPath = MultipartFileUtil.SEPERATOR + DateUtil.getTimeStamp(3);
                        // 회사 bizId
                        String bizIdPath = MultipartFileUtil.SEPERATOR + vo.getBizId();

                        logger.info("bizId : " + vo.getBizId());

                        String imgSavePath = MultipartFileUtil.getSystemPath() + "temp/file" + szMonthPath + szDayPath + bizIdPath + bizIdPath + "_stamp." + fileVO.getFileExt();

                        // 디렉토리 생성
                        com.jarvis.common.util.FileUtil.createDirectory(MultipartFileUtil.getSystemPath() + "temp/file" + szMonthPath + szDayPath + bizIdPath);

                        // 파일 복사
                        if (com.jarvis.common.util.FileUtil.fileCopy(imgPath, imgSavePath)) vo.setCustomerSignImg(szMonthPath + szDayPath + bizIdPath + bizIdPath + "_stamp." + fileVO.getFileExt());
                    }
                }
			}
			boolean result = contractService.signContractCustomer(vo);

			if (result) success = true;

			jsonObject.put("message", vo.getMessage());
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	@RequestMapping(method = RequestMethod.GET , value = "rejectContract.do")
	@ResponseBody
	public ResponseEntity<JSONObject> rejectContractCtrl(@ModelAttribute("UserSignDataVO") UserSignDataVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		logger.info(":::::::::::::::::::: rejectContract :::::::::::::::::::");

		try {
			vo.setServiceId(contractService.SERVICE_ID);

			boolean result = contractService.rejectContract(vo);

			if (result) success = true;
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	// 일괄 삭제처리(보관기간이 지난경우)
	@RequestMapping(method = RequestMethod.POST , value = "delContractPersonList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delContractPersonListCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		logger.info(":::::::::::::::::::: delContractPersonList :::::::::::::::::::");

		try {
			int result= contractService.delContractPersonList(vo);

			if (result != 0) {
				success = true;
				jsonObject.put("data", result);
			} else {
				jsonObject.put("data", null);
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	// 수기 전자서명 팝업
	@RequestMapping(method=RequestMethod.GET, value="goSignPad.do")
	public String goSignPadCtrl() {
		logger.info(":::::::::::::::::::: goSignPad :::::::::::::::::::");
		return "contract/contract_sign_step_2_sign";
	}



	// 직인 팝업
	@RequestMapping(method=RequestMethod.GET, value="goSignCustomer.do")
	public String goSignCustomerPadCtrl() {
		logger.info(":::::::::::::::::::: goSignCustomer :::::::::::::::::::");
		return "contract/contract_sign_step_2_customer";
	}



	// 정정요청 팝업
	@RequestMapping(method=RequestMethod.GET, value="goRejectPad.do")
	public String goRejectPadCtrl() {
		logger.info(":::::::::::::::::::: goRejectPad :::::::::::::::::::");
		return "contract/contract_sign_step_2_reject";
	}



	// 전자서명 완료
	@RequestMapping(method=RequestMethod.GET, value="goContractComplete.do")
	public String goContractCompleteCtrl(HttpServletRequest request, Model model) {
		logger.info(":::::::::::::::::::: goContractComplete :::::::::::::::::::");

		model.addAttribute("signBizType", request.getParameter("signBizType"));
		model.addAttribute("signUserType", request.getParameter("signUserType"));
		model.addAttribute("signCustomerType", request.getParameter("signCustomerType"));

		return "contract/contract_sign_step_3_complete";
	}



	// 정정요청 완료
	@RequestMapping(method=RequestMethod.GET, value="goContractRejectComplete.do")
	public String goContractRejectCompleteCtrl() {
		logger.info(":::::::::::::::::::: goContractRejectComplete :::::::::::::::::::");
		return "contract/contract_sign_step_3_reject_complete";
	}



	// 사용자 서명완료시 다운로드 링크
	@RequestMapping(method = RequestMethod.GET , value = "downContractPdf.do")
	public ResponseEntity<JSONObject> downContractPdfCtrl(@RequestParam("id") String id, @RequestParam(required = false) String type, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: downContractPdf :::::::::::::::::::");

		try {

			String szSavePath   = MultipartFileUtil.getSystemTempPath();
			ContractPersonVO vo = new ContractPersonVO();
			vo.setDigitNonce(id);
			ContractPersonVO contractPersonVO = contractService.getContractNonce(vo); // 전송타입 구분을 위한 VO 세팅

			ContentVO contentVO = contractService.downContractPdf(vo);
			String contentFileName = contentVO.getFilePath()+contentVO.getFileName();
			FileUtil.createDirectory(szSavePath);
			File file = new File(contentFileName);

			if ("EMAIL".equals(contractPersonVO.getSendType())) {
				if ("pdf".equals(type)) HttpUtil.setResponsePdfView(response, file);
				else 					HttpUtil.setResponseFile(request, response, file, contentVO.getOrgFileName());
			} else if("KKO".equals(contractPersonVO.getSendType())) {
				// 안드로이드 카카오톡 브라우저 정책 변경으로 인한 임시조치(21.09.29)
				response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(contentVO.getOrgFileName(), "UTF-8"));
				HttpUtil.setResponsePdfView(response, file);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity(status);
	}


	// 전자문서 조건양식 엑셀 파일 데이터 조회
	@RequestMapping(method = RequestMethod.GET , value = "getContractNewFormList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractNewFormListCtrl(@ModelAttribute("ContractFormVO") ContractPersonNewFormVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		logger.info(":::::::::::::::::::: getContractNewFormList :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				List<ContractPersonNewFormVO> result = contractService.getContractPersonNewFormList(vo);

				if (result != null && !result.isEmpty()) {
					success = true;
					jsonObject.put("total", result.size());
					jsonObject.put("data", result);
				} else {
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	@RequestMapping(method = RequestMethod.POST , value = "createContractForm.do")
	@ResponseBody
	public ResponseEntity<JSONObject> createContractFormCtrl(@ModelAttribute("ContractFormVO") ContractFormVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result    = false;
		logger.info(":::::::::::::::::::: createContractForm :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				if (vo.getBizId() == null)vo.setBizId(loginVO.getBizId());

				int count= contractService.createContractForm(vo);

				if (count != 0) {
					result = true;
					jsonObject.put("data", count);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 부문관리자 전자문서 리스트
	@RequestMapping(method = RequestMethod.GET , value = "getContractUserGrpList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractUserGrpListCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContractUserGrpList :::::::::::::::::::");

		try {

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				logger.info("searchKey=>"+vo.getSearchKey());
				logger.info("searchValue=>"+vo.getSearchValue());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());

				CodeVO codeVO = new CodeVO();
				codeVO.setGrpCommCode("101");
				List<CodeVO> codeList = codeService.getCodeList(codeVO);

				List<ContractPersonVO> result = contractService.getContractPersonUserGrpList(vo);
				int total = contractService.getContractPersonUserGrpListCount(vo);

				// 계약완료건수 조회
				vo.setStatusCode("Y");
				int completeCount = contractService.getContractPersonUserGrpListCount(vo);

				//다운완료건수 조회
				vo.setStatusCode("Y");
				int downloadCount = contractService.getContractPersonUserGrpDownloadCount(vo);

				if (result != null && !result.isEmpty()) {
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("completeCount", completeCount);
					jsonObject.put("downloadCount", downloadCount);
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("completeCount", 0);
					jsonObject.put("downloadCount", 0);
					jsonObject.put("data", null);
					jsonObject.put("statusList", null);
				}

				if (codeList != null && !codeList.isEmpty()) jsonObject.put("statusList", codeList);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	@RequestMapping(method = RequestMethod.POST , value = "getContractPersonBizSign.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractPersonBizSignCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContractPersonBizSign :::::::::::::::::::");

		try {
			//회사서명이 필요한 파일인지 조회
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String multiData = vo.getMultiData();

				if (StringUtil.isNotNull(multiData)) {
					String[] params = StringUtil.split(multiData, "|");
					for (int i=0; i<params.length; i++) {
						ContractPersonVO paramVO = new ContractPersonVO();
						paramVO.setContId(params[0]);
						ContractPersonNewVO resVo = contractService.getContractPersonSign(paramVO);

						if (resVo == null) { //양식이 제거되었을 경우
							jsonObject.put("success", true);
							return new ResponseEntity<>(jsonObject, responseHeaders, status);
						}

						if (resVo.getSignBizType().equals("0")) {
							logger.info("contId =>"+paramVO.getContId()+", signBizType =>"+resVo.getSignBizType());
							jsonObject.put("success", false);
							jsonObject.put("message", "선택하신 문서중 전자서명이 필요하지 않은 문서가 존재합니다.\r\n체크를 해제하신 후 진행해주시기 바랍니다.");
							return new ResponseEntity<>(jsonObject, responseHeaders, status);
						}
					}
				}
			}
		} catch(Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			jsonObject.put("success", false);
			jsonObject.put("message", "서명 참여자 확인 중 오류가 발생하였습니다.");
			throw ex;
		}
		jsonObject.put("success", true);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	// 변환완료 전자문서 ID로 서명여부 확인
	@RequestMapping(method = RequestMethod.POST , value = "getContractPersonNewBizSign.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractPersonNewBizSignCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContractPersonNewBizSign :::::::::::::::::::");

		try {
			//회사서명이 필요한 파일인지 조회
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (StringUtil.isNull(vo.getContractId()) && StringUtil.isNotNull(vo.getContId())) {
					//전자문서 PDF ID가 아닌 CONT ID가 넘어왔을 경우 조회
					ContractPersonNewVO contractPersonNewVO = contractDAO.getContractPersonSign(vo);
					jsonObject.put("contractPersonNewVO", contractPersonNewVO);
				} else {
					//변경 완료된 파일로 계약서 정보 조회
					ContractPersonNewVO newVO = new ContractPersonNewVO();

					if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

					newVO.setBizId(vo.getBizId());
					newVO.setContractId(vo.getContractId());
					List<ContractPersonNewVO> contractPersonNewList = contractService.getContractPersonNewList(newVO);

					jsonObject.put("contractPersonNewList", contractPersonNewList);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			jsonObject.put("success", false);
			jsonObject.put("message", "서명 참여자 확인 중 오류가 발생하였습니다.");
			throw ex;
		}
		jsonObject.put("success", true);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	@RequestMapping(method = RequestMethod.POST , value = "getDownloadComplete.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getDownloadCompleteCtrl(@RequestParam("list") List<String> list, @RequestParam(required = false) String bizId, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result 	  = false;
		logger.info(":::::::::::::::::::: getDownloadComplete :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
                for (String s : list) {
                    ContractPersonLogVO vo = new ContractPersonLogVO();

                    if (bizId == null || bizId.trim().isEmpty()) vo.setBizId(loginVO.getBizId());
                    else 										 vo.setBizId(bizId);

                    String value = s.replaceAll("&quot;", "").replace("[", "").replace("]", "");
                    logger.info("받은 데이터 =>" + value);
                    vo.setContId(value);

                    int count = contractService.getDownloadCount(vo);
                    if (count <= 0) {
                        //다운로드 로그가 존재하지 않을 경우
                        jsonObject.put("success", false);
                        return new ResponseEntity<>(jsonObject, responseHeaders, status);
                    } else {
                        result = true;
                    }
                }
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	@RequestMapping(method = RequestMethod.POST , value = "delContractAll.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delContractAllCtrl(@RequestParam("list") List<String> list, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		logger.info(":::::::::::::::::::: delContractAll :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				int result = contractService.delContractAll(list);

				if(result > 0) success = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("total", list.size());
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	//노무첨삭 댓글 조회
	@RequestMapping(method = RequestMethod.POST , value = "getContractPersonNewFormCommentList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractPersonNewFormCommentListCtrl(@ModelAttribute("ContractPersonNewFormCommentVO") ContractPersonNewFormCommentVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContractPersonNewFormCommentList :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("[getContractPersonNewFormCommentList] fileId=>"+vo.getFileId());
				vo.setBizId(loginVO.getBizId());
				List<ContractPersonNewFormCommentVO> result = contractService.getContractPersonNewFormCommentList(vo);

				if (result != null) {
					jsonObject.put("success", true);
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	//노무첨삭 - 댓글추가
	@RequestMapping(method = RequestMethod.POST , value = "insContractPersonNewFormComment.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insContractPersonNewFormCommentCtrl(@ModelAttribute("ContractPersonNewFormCommentVO") ContractPersonNewFormCommentVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result    = false;
		logger.info(":::::::::::::::::::: insContractPersonNewFormComment :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				vo.setUserType(loginVO.getUserType());
				contractService.insContractPersonNewFormComment(vo);
				result = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	//노무첨삭 - 댓글 제거
	@RequestMapping(method = RequestMethod.POST , value = "delContractPersonNewFormComment.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delContractPersonNewFormCommentCtrl(@ModelAttribute("ContractPersonNewFormCommentVO") ContractPersonNewFormCommentVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		logger.info(":::::::::::::::::::: delContractPersonNewFormComment :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("[delContractPersonNewFormComment] commentNo => "+vo.getCommentNo());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				contractService.delContractPersonNewFormComment(vo);
				success = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	//노무첨삭 - 최종파일등록
	@RequestMapping(method = RequestMethod.POST , value = "contractNewFinalUpload.do")
	@ResponseBody
	public ResponseEntity<JSONObject> contractNewFinalUploadCtrl(@ModelAttribute("ContractPersonNewVO") ContractPersonNewVO vo, HttpServletRequest request) {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result    = false;
		logger.info(":::::: contractNewFinalUpload :::::: ");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setBizId(loginVO.getBizId());
				logger.info("bizId : "+vo.getBizId());
				logger.info("getSignBizType : "+vo.getSignBizType());
				logger.info("getSignUserType : "+vo.getSignUserType());
				logger.info("getSignCustomerType : "+vo.getSignCustomerType());
				logger.info("getFileId : "+vo.getFileId());

				// 파일생성
				String szSavePath 			= MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = resultFileList.size();
				logger.info("최종파일 업로드 File Count : "+resultFileList.size());

				if (total==1) {
					// 전달받은 파일리스트
                    for (FileVO fileVO : resultFileList) {
                        String filePath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
                        File file 		= new File(filePath);
                        if (!file.exists()) {
                            logger.info("[contractNewFinalUpload] 원본파일이 존재하지 않습니다.");
                            jsonObject.put("message", "원본파일이 존재하지 않습니다.");
                        } else {
                            // 최종파일 DATABASE 처리
                            ContractPersonNewVO resultVO = contractService.contractNewFinalUpload(vo, fileVO);
                            if (resultVO == null) {
                                jsonObject.put("message", "파일이 존재하지 않습니다.");
                            } else {
                                jsonObject.put("message", resultVO.getMessage());
                                result = true;
                            }
                        }
                    }
				} else {
					logger.info("파일이 잘못 전달되었습니다.");
					jsonObject.put("message", "파일이 존재하지 않습니다.");
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	@RequestMapping(method = RequestMethod.GET , value = "getRemoveContractList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getRemoveContractListCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getRemoveContractList :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo.getDelBizId() == null || vo.getDelBizId().trim().isEmpty()) vo.setBizId(loginVO.getBizId());
				else 															   vo.setBizId(vo.getDelBizId());

				List<ContractPersonVO> result= contractService.getRemoveContractList(vo);
				if (result != null && !result.isEmpty()) {
					jsonObject.put("success", true);
					jsonObject.put("completeCount", result.size());
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("completeCount", 0);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	// 나눔 HR용 급여정보 업로드
	@RequestMapping(method = RequestMethod.POST, value = "sendNanumhrContractExcel.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendNanumhrContractExcelCtrl(@ModelAttribute("EmpVO") ContractPersonVO vo, HttpServletRequest request) {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result 	  = false;
		logger.info(":::::: sendNanumhrContractExcel :::::: ");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setBizId(loginVO.getBizId());
				logger.info("bizId : "+vo.getBizId());

				String szSavePath = System.getProperty("system.meta.path");
				String saveMonthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+DateUtil.getDate()+MultipartFileUtil.SEPERATOR;

				// 업로드 된 파일은 암호화 압축 후 서버 저장(D:/KFORM/META_FILE/.../file)
				FileVO resultFile = empService.getZipFileAddList(vo.getBizId(), request, szSavePath + saveMonthPath);

				if (resultFile != null) {

					// 전달박은 파일리스트
					String xlsPath = resultFile.getFileStrePath()+resultFile.getFileStreNm();

					File file = new File(xlsPath);
					if (!file.exists()) {
						logger.info("[sendNanumhrContractExcel] XLS파일이 존재하지 않습니다.");
						jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
					} else {
						String metaId = resultFile.getFileKey();
						// 엑셀 내부 데이터 DB 처리
						EmpVO resultEmpVO = contractService.sendNanumhrContractExcel(vo.getBizId(), xlsPath, metaId, vo.getFileId());

						if (resultEmpVO == null) {
							jsonObject.put("message", "[sendNanumhrContractExcel] XLS파일이 존재하지 않습니다.");
						} else {
							if(StringUtil.isNotNull(resultEmpVO.getEmpNonce()) && !resultEmpVO.getEmpNonce().equals("0")) {
								jsonObject.put("message", resultEmpVO.getEmpNonce()+"건의 정보를 등록하였습니다.");
								result = true;
							} else {
								if (StringUtil.isNotNull(resultEmpVO.getMessage())) {
									jsonObject.put("message", resultEmpVO.getMessage());
								} else {
									jsonObject.put("message", "엑셀 파일 업로드 중 문제가 발생하였습니다.");
								}
							}
						}
						//엑셀 파일 제거(원본 파일은 압축 및 암호화 후 보관)
						file.delete();
					}
				} else {
					logger.info("XLS 파일이 잘못 전달되었습니다.");
					jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	@RequestMapping(method = {RequestMethod.POST} , value = "saveContractSetting.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveDocumentSettingCtrl(@ModelAttribute("ContractSettingVO") ContractSettingVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		int total		  = 0;
		boolean success   = false;
		logger.info(":::::::::::::::::::: saveDocumentSetting :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				contractService.saveContractSetting(vo);
				success = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("total", total);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 양식 변환관리 리스트 조회
	@RequestMapping(method = RequestMethod.GET , value = "getContractTransformList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractTransformList(@ModelAttribute("ContractPersonNewVO") ContractPersonNewVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContractTransformList :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				if (vo.getBizId() == null) { vo.setBizId(loginVO.getBizId()); }

				List<ContractPersonNewVO> result = contractService.getContractTransformList(vo);
				ContractPersonNewVO statusCnt 	 = contractService.getContractTransformStatusListCount(vo);
				ContractPersonNewVO total 		 = contractService.getContractTransformListCount(vo);

				if (result != null && !result.isEmpty()) {
					jsonObject.put("success", true);
					jsonObject.put("requestCount", statusCnt.getRequestCount());
					jsonObject.put("progressCount", statusCnt.getProgressCount());
					jsonObject.put("previewCount", statusCnt.getPreviewCount());
					jsonObject.put("completeCount", statusCnt.getCompleteCount());
					jsonObject.put("total", total.getCNT());
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	//선택한 기간의 계약조건 다운로드
	@RequestMapping(method = RequestMethod.GET , value = "downContractDataExcel.do")
	@ResponseBody
	public ResponseEntity<JSONObject> downContractDataExcelCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: downContractDataExcel :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String excelFilePath = MultipartFileUtil.getSystemTempPath() + MultipartFileUtil.SEPERATOR;
				String excelFileName = vo.getContractFileName()+"_데이터일괄다운로드_"+DateUtil.getDate()+ ".xlsx";
				FileUtil.createDirectory(excelFilePath);

				int result = contractService.downContractDataExcel(excelFilePath, excelFileName, vo);

				File file = new File(excelFilePath + excelFileName);
				HttpUtil.setResponseFile(request, response, file, excelFileName);

				if 		(!file.exists()) status = HttpStatus.NO_CONTENT;
				else if (result == 0) 	 status = HttpStatus.NOT_MODIFIED;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity(status);
	}


	// 거래처 본인인증 - 비밀번호인증 관련 추가(2023-09-25)
	@RequestMapping(method = {RequestMethod.POST} , value = "checkAuthCode.do")
	@ResponseBody
	public ResponseEntity<JSONObject> checkAuthCodeCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		int total 		  = 0;
		boolean success   = false;
		logger.info(":::::::::::::::::::: checkAuthCode :::::::::::::::::::");

		try {
			ContractPersonVO result = contractService.getContractNonce(vo);

			if (result == null) {
				logger.error("계약서정보가 존재하지 않습니다. digitNonce =>"+vo.getDigitNonce());
				throw new Exception();
			}
			//비밀번호가 올바를경우
			if (SecurityUtil.encrypt(vo.getAuthCode()).equals(result.getAuthCode())) success = true;
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("total", total);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	@RequestMapping(method=RequestMethod.GET, value="goPasswordPad.do")
	public String goPasswordPadCtrl() {
		logger.info(":::::::::::::::::::: goPasswordPad :::::::::::::::::::");
		return "contract/contract_sign_step_1_password";
	}



	// 계약서 미리보기 데이터 입력
	@RequestMapping(method = RequestMethod.POST , value = "createContractPreview.do")
	@ResponseBody
	public ResponseEntity<JSONObject> createContractPreviewCtrl(@ModelAttribute("ContractFormVO") ContractFormVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result	  = false;
		logger.info(":::::::::::::::::::: createContractPreview :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				String contId = contractService.createContractPreview(vo);

				if (contId != null) {
					result = true;
					jsonObject.put("contId", contId);
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 미리보기 입력 데이터로 계약서 생성
	@RequestMapping(method = RequestMethod.POST, value = "createContractPreviewPDF.do")
	@ResponseBody
	public ResponseEntity<JSONObject> createContractPreviewPDFCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result    = false;
		logger.info(":::::::::::::::::::: createContractPreviewPDF :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo.getBizId() == null) { vo.setBizId(loginVO.getBizId()); }

				String BizType		= request.getParameter("contractBizType");
				String UserType		= request.getParameter("contractUserType");
				String CustomerType	= request.getParameter("contractCustomerType");

				ContentVO resContract = contractService.createContractPreviewPDF(vo, loginVO,BizType,UserType,CustomerType);

				if (resContract != null) {
					result = true;
					jsonObject.put("data", resContract);
				}
			}
		} catch (Exception ex) {
			result = false;
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 미리보기 - 수정 요청 에 따라 상태 변경
	@RequestMapping(method = RequestMethod.POST, value = "contractNewReload.do")
	@ResponseBody
	public ResponseEntity<JSONObject> contractNewReloadCtrl(@ModelAttribute("ContractPersonNewVO") ContractPersonNewVO vo, HttpServletRequest request) {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result    = false;
		logger.info(":::::: contractNewReload :::::: ");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				if (loginVO.getUserType() != null && !loginVO.getUserType().isEmpty()) vo.setInsUserType(loginVO.getUserType());


				logger.info("bizId : " + vo.getBizId());
				logger.info("fileId : " + vo.getFileId());
				String message = request.getParameter("message");
				logger.info("message : " + message);

				ContractPersonNewVO resultVO = contractService.contractNewReload(vo, message);

				if (resultVO != null) result = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
		}
		jsonObject.put("success", result);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}



	@RequestMapping(method = RequestMethod.GET, value = "getContractPreview.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractPreviewCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContractPreview :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String ipAddr = request.getHeader("X-Forwarded-For");
				if (ipAddr == null) ipAddr = request.getHeader("Proxy-Client-IP");
				if (ipAddr == null) ipAddr = request.getHeader("WL-Proxy-Client-IP");
				if (ipAddr == null) ipAddr = request.getHeader("HTTP_CLIENT_IP");
				if (ipAddr == null) ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
				if (ipAddr == null) ipAddr = request.getRemoteAddr();

				loginVO.setIpAddr(ipAddr);
				logger.info("bizId=>" + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				ContractPersonVO result = contractService.getContractPreview(vo, loginVO);

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



	@RequestMapping(method = RequestMethod.GET, value = "getContractFormListUpdate.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContractFormListUpdateCtrl(@ModelAttribute("ContractFormVO") ContractFormVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getContractFormListUpdate :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>" + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());

				List<ContractFormVO> result = contractService.getContractFormListUpdate(vo);

				if (result != null && !result.isEmpty()) {
					jsonObject.put("success", true);
					jsonObject.put("total", result.size());
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 미리보기 계약서 삭제 로직
	@RequestMapping(method = RequestMethod.POST , value = "delContractPreview.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delContractPreviewCtrl(@ModelAttribute("ContractPersonVO") ContractPersonVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: delContractPreview :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			List<ContractPersonVO> list = new ArrayList<ContractPersonVO>();
			int result = 0;

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String multiData = vo.getMultiData();
				logger.info("multiData : "+multiData);
				if (StringUtil.isNotNull(multiData)) {
					//형식은 TestPreView_contractDate_contId|
					String[] params = StringUtil.split(multiData, "|");
					for (int i=0; i<params.length-1; i++) {
						String hashData = params[i];
						String[] param = StringUtil.split(hashData, "-");
						if (param.length>1 && StringUtil.isNotNull(param[0])) {
							ContractPersonVO paramVO = new ContractPersonVO();
							paramVO.setServiceId(contractService.SERVICE_ID);
							paramVO.setBizId(vo.getBizId());
							paramVO.setUserId(param[0]);
							paramVO.setContractDate(param[1]);
							paramVO.setContId(param[2]);
							list.add(paramVO);
						}
					}
					if (!list.isEmpty()) result = contractService.delContractPreview(list, loginVO);
					else 				 status = HttpStatus.NOT_MODIFIED;

					if (result > 0) jsonObject.put("success", true);

					else {
						jsonObject.put("success", false);
						jsonObject.put("message", vo.getResultMessage());
					}
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 매뉴얼 가이드 PDF
	@RequestMapping(method = RequestMethod.GET, value = "getManualGuide.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getManualGuideCtrl(HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getManualGuide :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String ipAddr = request.getHeader("X-Forwarded-For");
				if (ipAddr == null) ipAddr = request.getHeader("Proxy-Client-IP");
				if (ipAddr == null) ipAddr = request.getHeader("WL-Proxy-Client-IP");
				if (ipAddr == null) ipAddr = request.getHeader("HTTP_CLIENT_IP");
				if (ipAddr == null) ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
				if (ipAddr == null) ipAddr = request.getRemoteAddr();

				loginVO.setIpAddr(ipAddr);

				ContractPersonVO result = new ContractPersonVO();
				String targerPdfPath	= MultipartFileUtil.getSystemPath()+"data/Ieumsign_서비스가이드.pdf";
				String pdfPath			= StringUtil.ReplaceAll(targerPdfPath,getSystemPath(),"");
				result.setPdfFile(pdfPath);

				jsonObject.put("success", true);
				jsonObject.put("data", result);
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

}

