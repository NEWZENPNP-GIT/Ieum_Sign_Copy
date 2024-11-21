package com.ezsign.paystub.controller;

import com.ezsign.common.controller.BaseController;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.*;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.paystub.service.PaystubService;
import com.ezsign.paystub.vo.PaystubVO;
import com.jarvis.common.util.StringUtil;
import com.jarvis.pdf.util.ToolUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;


@Controller
@RequestMapping("/paystub/")
public class PaystubController extends BaseController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "paystubService")
	private PaystubService paystubService;

	@RequestMapping(method = RequestMethod.GET , value = "getPayStubPDF.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPayStubPDFCtrl(@ModelAttribute("PaystubVO") PaystubVO vo, @RequestParam(required = false) String type, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getPayStubPDF :::::::::::::::::::");
		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String szMonthPath  	= MultipartFileUtil.SEPERATOR+DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+DateUtil.getDate()+MultipartFileUtil.SEPERATOR;
				String szSavePath 		= MultipartFileUtil.getSystemPath()+"temp/paystub"+szMonthPath;
				ContentVO contentVO 	= paystubService.getPaystubPDF(vo);

				String contentFileName  = contentVO.getFilePath()+contentVO.getFileName();
				String downloadFileName = szSavePath+contentVO.getFileName();
				String userPwd 			= contentVO.getInsDate();			// 생년월일
				logger.info("getPayStubPDFCtrl password : "+ userPwd);
				FileUtil.createDirectory(szSavePath);
				File file;
				if ("pdf".equals(type)) {
					file = new File(contentFileName);
					HttpUtil.setResponsePdfView(response, file);
				} else {
					if (ToolUtil.encryptUserPdf(contentFileName, downloadFileName, userPwd)) {
						file = new File(downloadFileName);
						HttpUtil.setResponseFile(request, response, file, contentVO.getOrgFileName());
						if (!file.exists()) status = HttpStatus.INTERNAL_SERVER_ERROR;
					}
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
        return new ResponseEntity(status);
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "getPayStubPDFFlutter.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPayStubPDFFlutterCtrl(@ModelAttribute("PaystubVO") PaystubVO vo, @RequestParam(required = false) String type, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getPayStubPDF :::::::::::::::::::");
		try {
			if (vo.getUserId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String szMonthPath  	= MultipartFileUtil.SEPERATOR+DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+DateUtil.getDate()+MultipartFileUtil.SEPERATOR;
				String szSavePath 		= MultipartFileUtil.getSystemPath()+"temp/paystub"+szMonthPath;
				ContentVO contentVO 	= paystubService.getPaystubPDF(vo);

				String contentFileName 	= contentVO.getFilePath()+contentVO.getFileName();
				String downloadFileName = szSavePath+contentVO.getFileName();
				String userPwd 			= contentVO.getInsDate();	// 생년월일
				logger.info("getPayStubPDFCtrl password : "+ userPwd);
				FileUtil.createDirectory(szSavePath);
				File file;
				if ("pdf".equals(type)) {
					file = new File(contentFileName);
					HttpUtil.setResponsePdfView(response, file);
				} else {
					if (ToolUtil.encryptUserPdf(contentFileName, downloadFileName, userPwd)) {
						file = new File(downloadFileName);
						HttpUtil.setResponseFile(request, response, file, contentVO.getOrgFileName());
						if (!file.exists()) status = HttpStatus.INTERNAL_SERVER_ERROR;
					}
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
        return new ResponseEntity(status);
	}

	@RequestMapping(method = RequestMethod.GET , value = "getPayStubPDFView.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPayStubPDFViewCtrl(@ModelAttribute("PaystubVO") PaystubVO vo, @RequestParam(required = false) String type, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		PaystubVO resVO   = null;
		boolean success   = false;
		logger.info(":::::::::::::::::::: getPayStubPDFView :::::::::::::::::::");
		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String ipAddr = request.getHeader("X-Forwarded-For");
		        if (ipAddr == null) ipAddr = request.getHeader("Proxy-Client-IP");
		        if (ipAddr == null) ipAddr = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
		        if (ipAddr == null) ipAddr = request.getHeader("HTTP_CLIENT_IP");
		        if (ipAddr == null) ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
		        if (ipAddr == null) ipAddr = request.getRemoteAddr();

		        loginVO.setIpAddr(ipAddr);
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				resVO 					= new PaystubVO();
				String szMonthPath  	= MultipartFileUtil.SEPERATOR+DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+DateUtil.getDate()+MultipartFileUtil.SEPERATOR;
				String szSavePath 		= MultipartFileUtil.getSystemPath()+"temp/paystub"+szMonthPath;
				vo.setPayType(type);
				ContentVO contentVO 	= paystubService.getPaystubPDFView(vo, loginVO);

				String contentFileName 	= contentVO.getFilePath()+contentVO.getFileName();
				String downloadFileName = szSavePath+contentVO.getFileName();
				String userPwd 			= contentVO.getInsDate();	// 생년월일
				logger.info("getPayStubPDFCtrl password : "+ userPwd);
				FileUtil.createDirectory(szSavePath);
				File file;
				if ("pdf".equals(type)) {
                    String targetPdfName = StringUtil.getUUID()+".pdf";
					String targetPdfPath = MultipartFileUtil.getSystemTempPath()+targetPdfName;
					
					// 사용할 PDF복사
					FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
					if (FileUtil.fileCopy(contentFileName, targetPdfPath)) {
						String pdf_uri = StringUtil.ReplaceAll(targetPdfPath, MultipartFileUtil.getSystemPath(), "");
						resVO.setPdfFile(pdf_uri);
						success = true;
					}
				} else {
					if (ToolUtil.encryptUserPdf(contentFileName, downloadFileName, userPwd)) {
						file = new File(downloadFileName);
						HttpUtil.setResponseFile(request, response, file, contentVO.getOrgFileName());
						if(!file.exists()) status = HttpStatus.INTERNAL_SERVER_ERROR;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("data", resVO);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "getPayStubList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPayStubListCtrl(@ModelAttribute("PaystubVO") PaystubVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getPayStubList :::::::::::::::::::");
		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				logger.info("searchKey=>"+vo.getSearchKey());
				logger.info("searchValue=>"+vo.getSearchValue());
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				List<PaystubVO> result = paystubService.getPaystubList(vo);
				int total 			   = paystubService.getPaystubListCount(vo);
				
				if (result != null && !result.isEmpty()) {
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
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
	
	@RequestMapping(method = RequestMethod.GET , value = "getPayStubPayDate.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPayStubPayDateCtrl(@ModelAttribute("PaystubVO") PaystubVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getPayStubPayDate :::::::::::::::::::");
		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				logger.info("searchKey=>"+vo.getSearchKey());
				logger.info("searchValue=>"+vo.getSearchValue());
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				PaystubVO result= paystubService.getPayStubPayDate(vo);
				
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
	
	@RequestMapping(method = RequestMethod.GET , value = "getPayStubListFlutter.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPayStubListFlutterCtrl(@ModelAttribute("PaystubVO") PaystubVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: getPayStubListFlutter :::::::::::::::::::");
		try {
			if (vo.getUserId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				logger.info("searchKey=>"+vo.getSearchKey());
				logger.info("searchValue=>"+vo.getSearchValue());
				List<PaystubVO> result = paystubService.getPaystubList(vo);
				// 2024.08.23 캔디app에서 아래 쿼리로 인해 속도느림 현상이 발생하였지만,
				// 실제 사용하지 않은쿼리로 파악되어서 주석처리함.
				// int total 			   = paystubService.getPaystubListCount(vo);
				int total 			   = 0;
				if (result != null && !result.isEmpty()) {
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
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

	// 삭제
	@RequestMapping(method = RequestMethod.POST , value = "delPayStub.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delPayStubCtrl(@ModelAttribute("PaystubVO") PaystubVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result    = false;
		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: delPayStub :::::::::::::::::::");
		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				int success = paystubService.delPaystub(vo);
				if (success>0) result = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST, value = "sendPaystubExcel.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendPaystubExcelCtrl(@ModelAttribute("PaystubVO") PaystubVO vo, HttpServletRequest request) {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean result    = false;
		logger.info(":::::: sendPaystubExcel :::::: ");
		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				vo.setServiceId(PaystubService.SERVICE_ID);
				logger.info("bizId : " + vo.getBizId());
				logger.info("seerviceId : " + vo.getServiceId());
				
				String szSavePath 			= MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total 					= resultFileList.size();
				logger.info("인사정보 XLS File Count : "+resultFileList.size());
				
				if (total == 1) {
					// 전달받은 파일리스트
                    for (FileVO fileVO : resultFileList) {
                        String xlsPath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
                        File file 	   = new File(xlsPath);
                        if (!file.exists()) {
                            logger.info("[sendPaystubExcel] XLS파일이 존재하지 않습니다.");
                            jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
                        } else {
                            // DATABASE 처리
                            PaystubVO resultPaystubVO = paystubService.sendPaystubExcel(vo.getBizId(), xlsPath, fileVO);
                            if (resultPaystubVO == null) {
                                jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
                            } else {
                                if (StringUtil.isNotNull(resultPaystubVO.getPaystubNonce())) {
                                    String paystubNonce = resultPaystubVO.getPaystubNonce();

                                    if (!"-100".equals(paystubNonce) && !"-200".equals(paystubNonce)) {
                                        jsonObject.put("message", resultPaystubVO.getPaystubNonce() + "건의 급여정보를 등록하였습니다.");
                                        jsonObject.put("seqIds", resultPaystubVO.getSeqIds());
                                        result = true;
                                    } else if ("-100".equals(paystubNonce)) {
										jsonObject.put("message", resultPaystubVO.getMessage() + "\r\n" + resultPaystubVO.getEmpName() + "님의 인사정보를 등록해주세요.");
									} else {
										jsonObject.put("message", resultPaystubVO.getMessage());
									}
                                } else {
									jsonObject.put("message", resultPaystubVO.getExcelSheet() + resultPaystubVO.getEmpName() + "님의 급여정보에서 필수항목이 누락된 데이터가 존재합니다.");
								}
                            }
                            file.delete();
                        }
                    }
				} else {
					logger.info("XLS 파일이 잘못 전달되었습니다.");
					jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
					result = false;
				}
				
			}
		} catch (Exception ex) { status = HttpStatus.INTERNAL_SERVER_ERROR; logService.error(ex.getMessage(), new Throwable(ex)); }
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 임금명세관리 임금정보 문서 등록양식
	@RequestMapping(method = RequestMethod.POST, value = "sendPaycarePaystubExcel.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendPaycarePaystubExcelCtrl(@ModelAttribute("PaystubVO") PaystubVO vo, HttpServletRequest request) {
		JSONObject jsonObject		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean result 	  = false;
		logger.info(":::::: sendPaycarePaystubExcel :::::: ");
		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				vo.setServiceId(PaystubService.SERVICE_ID);
				logger.info("bizId : " + vo.getBizId());
				logger.info("seerviceId : " + vo.getServiceId());
				
				// 파일생성
				String szSavePath 			= MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total 					= resultFileList.size();
				logger.info("인사정보 XLS File Count : "+resultFileList.size());
				if (total == 1) {
					// 전달받은 파일리스트
                    for (FileVO fileVO : resultFileList) {
                        String xlsPath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
                        File file = new File(xlsPath);
                        if (!file.exists()) {
                            logger.info("[sendPaycarePaystubExcel] XLS파일이 존재하지 않습니다.");
                            jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
                            result = false;
                        } else {
                            // DATABASE 처리
                            PaystubVO resultPaystubVO = paystubService.sendPayCarePaystubExcel(vo.getBizId(), xlsPath, fileVO);
                            if (resultPaystubVO == null) {
                                jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
                                result = false;
                            } else {
                                if (StringUtil.isNotNull(resultPaystubVO.getPaystubNonce())) {
                                    String paystubNonce = resultPaystubVO.getPaystubNonce();
                                    if (!"-100".equals(paystubNonce)) { jsonObject.put("message", resultPaystubVO.getPaystubNonce() + "건의 급여정보를 등록하였습니다."); result = true; }
									else 							  { jsonObject.put("message", resultPaystubVO.getMessage() + "\r\n" + resultPaystubVO.getEmpName() + "님의 인사정보를 등록해주세요."); }
                                } else {
                                    jsonObject.put("message", resultPaystubVO.getExcelSheet() + resultPaystubVO.getEmpName() + "님의 급여정보에서 필수항목이 누락된 데이터가 존재합니다.");
                                    result = false;
                                }
                            }
                            file.delete();
                        }
                    }
				} else { logger.info("XLS 파일이 잘못 전달되었습니다."); jsonObject.put("message", "XLS파일이 존재하지 않습니다."); }
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET , value = "createPayStub.do")
	@ResponseBody
	public ResponseEntity<JSONObject> createPayStubCtrl(@ModelAttribute("PaystubVO") PaystubVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result    = false;
		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: createPayStub :::::::::::::::::::");
		try {
			logger.info(":::::::::::::::    ["+vo.getBusinessNo()+"]["+vo.getFileId() + "] :::::::::::::::");
			ContentVO contentVO 	  = new ContentVO();
			contentVO.setFileId(vo.getFileId());
			ContentVO resultContentVO = paystubService.createPaystubXML(contentVO);
			if (resultContentVO!=null) {
				int resultPDF = paystubService.createPaystubPDF(vo);
				if (resultPDF>0) result = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "createPaystubPDF.do")
	@ResponseBody
	public ResponseEntity<JSONObject> createPaystubPDFCtrl(@ModelAttribute("PaystubVO") PaystubVO vo, HttpServletRequest request) {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: createPaystubPDF :::::::::::::::::::");
		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				int result = paystubService.createPaystubPDF(vo);
				
				if (result > 0) {
					jsonObject.put("success", true);
					jsonObject.put("total", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
				}

				if 		(result == -1) jsonObject.put("msg", "포인트정보가 존재하지 않습니다.");
				else if (result == -2) jsonObject.put("msg", "기업정보가 존재하지 않습니다.");
				else if (result == -3) jsonObject.put("msg", "포인트가 부족합니다. 충전 후 사용하시기 바랍니다.");
			}
		} catch (Exception ex) { logService.error(ex.getMessage(), new Throwable(ex)); }
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 임금명세서 관리 > 임금명세서 전송
	@RequestMapping(method = RequestMethod.POST , value = "sendPaystubPDF.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendPaystubPDFCtrl(@ModelAttribute("PaystubVO") PaystubVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		logger.info(":::::::::::::::::::: sendPaystubPDF :::::::::::::::::::");
		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				int result= paystubService.sendPaystubPDF(vo);

				if (result > 0) {
					jsonObject.put("success", true);
					jsonObject.put("total", result);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
				}

				if		(result == -1) jsonObject.put("msg", "포인트정보가 존재하지 않습니다.");
				else if (result == -2) jsonObject.put("msg", "기업정보가 존재하지 않습니다.");
				else if (result == -3) jsonObject.put("msg", "포인트가 부족합니다. 충전 후 사용하시기 바랍니다.");
			}
		} catch (Exception ex) { logService.error(ex.getMessage(), new Throwable(ex)); }
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
}
