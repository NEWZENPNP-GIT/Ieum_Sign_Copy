package com.ezsign.emp.controller;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ezsign.framework.util.*;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

import com.ezsign.common.controller.BaseController;
import com.ezsign.emp.service.EmpService;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.user.vo.UserVO;
import com.jarvis.common.util.DateUtil;

@Controller
@RequestMapping("/emp/")
public class EmpController extends BaseController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "empService")
	private EmpService empService;

	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "getEmpList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getEmpListCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;

		try {
			logger.info(":::::::::::::::::::: getEmpList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				String ipAddr 			   = request.getHeader("X-Forwarded-For");
				if (ipAddr == null) ipAddr = request.getHeader("Proxy-Client-IP");
				if (ipAddr == null) ipAddr = request.getHeader("WL-Proxy-Client-IP");
				if (ipAddr == null) ipAddr = request.getHeader("HTTP_CLIENT_IP");
				if (ipAddr == null) ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
				if (ipAddr == null) ipAddr = request.getRemoteAddr();

				logger.info("bizId=> " 		 + loginVO.getBizId());
				logger.info("searchKey=> " 	 + vo.getSearchKey());
				logger.info("searchValue=> " + vo.getSearchValue());

				if (!StringUtil.isNotNull(vo.getBizId())) vo.setBizId(loginVO.getBizId());
				if (vo.getBizId().equals("middle"))		  vo.setBizId("");

				loginVO.setIpAddr(ipAddr);
				List<EmpVO> result = empService.getEmpList(vo, loginVO);
				int total 		   = empService.getEmpListCount(vo);

				if (result != null && !result.isEmpty()) {
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
				} else if (result == null) {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
			}
		} catch(Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 로그인시 중복 사번 체크
	@RequestMapping(method = RequestMethod.POST, value = "getDupleEmpNo.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getDupleEmpCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;

		try {
			logger.info(":::::::::::::::::::: getDupleEmp :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				logger.info("bizId=>" + loginVO.getBizId());

				if (vo.getBizId() == null) { vo.setBizId(loginVO.getBizId()); }

				if (loginVO.getUserType().equals("5")) vo.setDeptCode(loginVO.getDeptCode());
				else 								   vo.setDeptCode("");

				List<EmpVO> result = empService.getDupleEmpList(vo);

				if (result != null) {
					jsonObject.put("success", true);
					jsonObject.put("isDupleCnt", result.size());
					jsonObject.put("empNoList", result);
				} else {
					jsonObject.put("success", true);
					jsonObject.put("isDupleCnt", 0);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET, value = "getEmp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getEmpCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		int total = 0;

		try {
			logger.info(":::::::::::::::::::: getEmp :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				String ipAddr 				 = request.getHeader("X-Forwarded-For");
				if (ipAddr == null) { ipAddr = request.getHeader("Proxy-Client-IP"); }
				if (ipAddr == null) { ipAddr = request.getHeader("WL-Proxy-Client-IP");}
				if (ipAddr == null) { ipAddr = request.getHeader("HTTP_CLIENT_IP"); }
				if (ipAddr == null) { ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR"); }
				if (ipAddr == null) { ipAddr = request.getRemoteAddr(); }

				logger.info("bizId=> "  + loginVO.getBizId());
				logger.info("userId=> " + vo.getUserId());

				if (vo.getBizId() == null) { vo.setBizId(loginVO.getBizId()); }

				loginVO.setIpAddr(ipAddr);
				EmpVO result = empService.getEmp(vo, loginVO);

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

	@RequestMapping(method = RequestMethod.GET, value = "getEmpFlutter.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getEmpFlutterCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		int total		  = 0;

		try {
			logger.info(":::::::::::::::::::: getEmp :::::::::::::::::::");
			if (vo.getUserId() == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				String ipAddr 				 = request.getHeader("X-Forwarded-For");
				if (ipAddr == null) { ipAddr = request.getHeader("Proxy-Client-IP"); }
				if (ipAddr == null) { ipAddr = request.getHeader("WL-Proxy-Client-IP");}
				if (ipAddr == null) { ipAddr = request.getHeader("HTTP_CLIENT_IP"); }
				if (ipAddr == null) { ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR"); }
				if (ipAddr == null) { ipAddr = request.getRemoteAddr(); }

				logger.info("bizId=> "  + vo.getBizId());
				logger.info("userId=> " + vo.getUserId());

				SessionVO loginVO = new SessionVO();
				loginVO.setIpAddr(ipAddr);
				EmpVO result 	  = empService.getEmp(vo, loginVO);

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

	@RequestMapping(method = RequestMethod.POST, value = "insEmp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insEmpCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result    = false;
		logger.info(":::::::::::::::::::: insEmp :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				logger.info("bizId=> "  + loginVO.getBizId());
				logger.info("userId=> " + vo.getUserId());

				if (vo.getBizId() == null) { vo.setBizId(loginVO.getBizId()); }

				if (!StringUtil.isNotNull(vo.getEmpType())) { vo.setEmpType("1"); }

				vo.setAddr2("");
				vo.setEmpNonce("");
				vo.setCountryType("81");
				vo.setConfirmType("N");

				// 중간관리자가 아닌경우 입력시 부서 미지정
				if (!vo.getInsUserType().equals("5")) { vo.setDeptCode(""); }

				String empNonce = empService.insEmp(vo);
				String message  = "";
				if (StringUtil.isNotNull(empNonce)) {
					switch (empNonce) {
						case "-1":
							message = "입력하신 인사정보가 기존의 인사정보와 모두 동일합니다.";
							break;
						case "-2":
							message = "인사정보를 수정하였습니다.";
							break;
						case "-3":
							message = "해당 사번의 인사정보가 이미 존재합니다.";
							break;
						case "1":
							message = "인사정보를 등록하였습니다.";
							break;
					}
					jsonObject.put("message", message);
					result = true;
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


	@RequestMapping(method = RequestMethod.POST, value = "updEmp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updEmpCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;

		try {
			logger.info(":::::::::::::::::::: updEmp :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=> "  + loginVO.getBizId());
				logger.info("userId=> " + vo.getUserId());

				if (vo.getBizId() == null) { vo.setBizId(loginVO.getBizId()); }

				//시스템관리자 로그인ID 변경 서비스 추가
				EmpVO resultEmpVO = empService.getEmp(vo, loginVO);

				if (resultEmpVO.getLoginId() != null && vo.getLoginId() != null) {
					if (!(resultEmpVO.getLoginId().equals(vo.getLoginId()))) {
						UserVO userVO = new UserVO();
						userVO.setUserId(resultEmpVO.getLoginId());
						userVO.setChangeUserId(vo.getLoginId());
						empService.updEmpLoginID(userVO);
					}
				}
				int result = empService.updEmp(vo);
				if (result > 0) success = true;
				jsonObject.put("total", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET, value = "delEmp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delEmpCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;

		try {
			logger.info(":::::::::::::::::::: delEmp :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=> "  + vo.getBizId());
				logger.info("userId=> " + vo.getUserId());

				int result = empService.delEmp(vo);

				if (result > 0) success = true;
				jsonObject.put("total", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	@RequestMapping(method = RequestMethod.POST, value = "updEmpType.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updEmpTypeCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;

		try {
			logger.info(":::::::::::::::::::: updEmpType :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=> "  + loginVO.getBizId());
				logger.info("userId=> " + vo.getUserId());

				if (vo.getBizId() == null) { vo.setBizId(loginVO.getBizId()); }

				int result = empService.updEmpType(vo);

				if (result > 0) success = true;
				jsonObject.put("total", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	@RequestMapping(method = RequestMethod.GET, value = "updEmpNonce.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updEmpNonceCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;

		try {
			logger.info(":::::::::::::::::::: updEmpNonce :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=> "  + loginVO.getBizId());
				logger.info("userId=> " + vo.getUserId());
				vo.setBizId(loginVO.getBizId());

				int result = empService.updEmpNonce(vo);

				if (result > 0) success = true;
				jsonObject.put("total", result);
				jsonObject.put("data", vo);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST, value = "sendEmpExcel.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendEmpExcelCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result 	  = false;

		logger.info(":::::: sendEmpExcel :::::: ");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setBizId(loginVO.getBizId());
				logger.info("bizId : "   + vo.getBizId());
				logger.info("smsType : " + vo.getSmsSendType());

				// 파일생성
				String szSavePath 			= MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total 					= resultFileList.size();

				logger.info("인사정보 XLS File Count : " + resultFileList.size());

				if (total == 1) {
					// 전달받은 파일리스트
					for (int i = 0; i < resultFileList.size(); i++) {
						FileVO fileVO  = resultFileList.get(i);
						String xlsPath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();

						File file = new File(xlsPath);
						if (!file.exists()) {
							logger.info("[sendEmpExcel] XLS파일이 존재하지 않습니다.");
							jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
							result = false;
						} else {

							// DATABASE 처리
							// 중간관리자가 아닌경우 부서코드는 빈값
							if (!vo.getInsUserType().equals("5")) { vo.setDeptCode(""); }

							EmpVO resultEmpVO = empService.sendEmpExcel(vo.getBizId(), xlsPath, vo.getSmsSendType(), vo.getDeptCode());
							if (resultEmpVO == null) {
								jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
								result = false;
							} else {
								if (StringUtil.isNotNull(resultEmpVO.getEmpNonce())) {
									if (resultEmpVO.getEmpNonce().equals("-1")) {
										jsonObject.put("message", resultEmpVO.getEmpName() + "님 소속기업의 사업자등록번호를 확인하시기 바랍니다.");
										result = false;
									} else if (resultEmpVO.getEmpNonce().equals("-2")) {
										jsonObject.put("message", resultEmpVO.getEmpName() + "님 생년월일을 8자리로 입력 해주세요.");
										result = false;
									} else if (resultEmpVO.getEmpNonce().equals("-3")) {
										jsonObject.put("message", resultEmpVO.getEmpName() + "님 입사일자를 8자리로 입력 해주세요.");
										result = false;
									} else if (resultEmpVO.getEmpNonce().equals("-4")) {
										jsonObject.put("message", resultEmpVO.getEmpName() + "님 퇴사일자를 8자리로 입력 해주세요.");
										result = false;
									} else if (resultEmpVO.getEmpNonce().equals("-5")) {
										jsonObject.put("message", "휴대폰번호 형식이 올바르지 않습니다. ["+ resultEmpVO.getEmpName()+ "]");
										result = false;
									} else {

										String totCnt   = resultEmpVO.getEmpNonce();
										String insCnt   = resultEmpVO.getInsCount();
										String updCnt   = resultEmpVO.getUpdCount();
										String dupCnt   = resultEmpVO.getDupCount();
										String checkCnt = resultEmpVO.getCheckCount();
										String updY 	= resultEmpVO.getDuplicateEmpRow() + "행의 인사정보를 수정하였습니다. \n";
										String checkY 	= resultEmpVO.getCheckEmpRow() + "행의 사번, 이름이 기존의 사번,이름과 다릅니다. 다시 확인해 주시기 바랍니다.";

										if (totCnt.equals(dupCnt)){
											jsonObject.put("message", "입력하신 인사정보가 기존의 인사정보와 모두 동일합니다. \n 다시 확인해 주시기 바랍니다.");
										} else {
											jsonObject.put("message", "총 " + totCnt + " 건의 인사정보 중 " + insCnt +
													" 건 저장, " + updCnt + " 건 수정, " + checkCnt + " 건 실패하였습니다. \n" +
													resultEmpVO.getInsCount() + " 건의 인사정보를 등록하였습니다. \n" +
													(!"0".equals(updCnt) ? updY : "") + (!"0".equals(checkCnt) ? checkY : ""));
										}
										result = true;
									}
								} else {
									jsonObject.put("message", resultEmpVO.getEmpName() + "님의 인사정보에서 필수항목이 누락된 데이터가 존재합니다.");
									result = false;
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
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			//throw ex;
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	@RequestMapping(method = RequestMethod.GET, value = "findLoginId.do")
	@ResponseBody
	public ResponseEntity<JSONObject> findLoginIdCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		int total = 0;

		try {
			logger.info(":::::::::::::::::::: findLoginId :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=> " 		+ loginVO.getBizId());
				logger.info("searchKey=> " 	+ vo.getSearchKey());
				logger.info("searchValue=> " + vo.getSearchValue());

				List<EmpVO> result = empService.getEmpList(vo, loginVO);

				if (result != null && result.size() != 0) {
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
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET, value = "findLoginPwd.do")
	@ResponseBody
	public ResponseEntity<JSONObject> findLoginPwdCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		int total		  = 0;

		try {
			logger.info(":::::::::::::::::::: findLoginPwd :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=> " 		+ loginVO.getBizId());
				logger.info("searchKey=> " 	+ vo.getSearchKey());
				logger.info("searchValue=> " + vo.getSearchValue());
				vo.setBizId(loginVO.getBizId());

				List<EmpVO> result = empService.getEmpList(vo, loginVO);
				total 			   = empService.getEmpListCount(vo);

				if (result != null && result.size() != 0) {
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

	@RequestMapping(method = RequestMethod.POST, value = "sendPaycareEmpExcel.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendPaycareEmpExcelCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result    = false;

		logger.info(":::::: sendPaycareEmpExcel :::::: ");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setBizId(loginVO.getBizId());
				logger.info("bizId : "   + vo.getBizId());
				logger.info("smsType : " + vo.getSmsSendType());

				// 파일생성
				String szSavePath 			= MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total 					= resultFileList.size();

				logger.info("인사정보 XLS File Count : " + resultFileList.size());

				if (total == 1) {

					// 전달박은 파일리스트
					for (int i = 0; i < resultFileList.size(); i++) {
						FileVO fileVO  = resultFileList.get(i);
						String xlsPath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();

						File file = new File(xlsPath);
						if (!file.exists()) {
							logger.info("[sendPaycareEmpExcel] XLS파일이 존재하지 않습니다.");
							jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
							result = false;
						} else {
							// DATABASE 처리
							EmpVO resultEmpVO = empService.sendPayCareEmpExcel(vo.getBizId(), xlsPath, vo.getSmsSendType());
							if (resultEmpVO == null) {
								jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
								result = false;
							} else {
								if (StringUtil.isNotNull(resultEmpVO.getEmpNonce())) {
									jsonObject.put("message", resultEmpVO.getEmpNonce() + "건의 인사정보를 등록하였습니다.");
									result = true;
								} else {
									jsonObject.put("message", resultEmpVO.getExcelSheet() + " Sheet " + resultEmpVO.getExcelRow() + "번째 줄 " + resultEmpVO.getEmpName() + "님의 인사정보에서 필수항목(성명, 휴대폰번호, 이메일, 입사일자, 생년월일)이 누락된 데이터가 존재합니다.");
									result = false;
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
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	/*나눔 HR 일괄등록*/
	@RequestMapping(method = RequestMethod.POST, value = "sendNanumhrEmpExcel.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendNanumhrEmpExcelCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result 	  = false;

		logger.info(":::::: sendNanumhrEmpExcelCtrl :::::: ");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setBizId(loginVO.getBizId());
				logger.info("bizId : " + vo.getBizId());

				String szSavePath    = System.getProperty("system.meta.path");
				String saveMonthPath = MultipartFileUtil.SEPERATOR + DateUtil.getYearMonth() + MultipartFileUtil.SEPERATOR + DateUtil.getDate() + MultipartFileUtil.SEPERATOR;

				// 업로드 된 파일은 암호화 압축 후 서버 저장(global.property - system.meta.path)
				FileVO resultFile = empService.getZipFileAddList(vo.getBizId(), request, szSavePath + saveMonthPath);

				if (resultFile != null) {
					// 전달박은 파일리스트
					String xlsPath = resultFile.getFileStrePath() + resultFile.getFileStreNm();
					File file 	   = new File(xlsPath);

					if (!file.exists()) {
						logger.info("[sendNanumhrEmpExcelCtrl] XLS파일이 존재하지 않습니다.");
						jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
						result = false;
					} else {
						String metaId 	  = resultFile.getFileKey();
						EmpVO resultEmpVO = null;
						// 엑셀 내부 데이터 DB 처리
						resultEmpVO 	  = empService.sendNanumhrEmpExcel(vo.getBizId(), xlsPath, metaId, "");
						if (resultEmpVO == null) {
							jsonObject.put("message", "[sendNanumhrEmpExcel] XLS파일이 존재하지 않습니다.");
							result = false;
						} else {
							if (StringUtil.isNotNull(resultEmpVO.getEmpNonce()) && !resultEmpVO.getEmpNonce().equals("0")) {
								jsonObject.put("message", resultEmpVO.getEmpNonce() + "건의 인사정보를 등록하였습니다.");
								result = true;
							} else {
								if (StringUtil.isNotNull(resultEmpVO.getMessage())) {
									jsonObject.put("message", resultEmpVO.getMessage());
									result = false;
								} else {
									jsonObject.put("message", "엑셀 파일 업로드 중 문제가 발생하였습니다.");
									result = false;
								}
							}
						}
						file.delete();
					}
				} else {
					logger.info("XLS 파일이 잘못 전달되었습니다.");
					jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
					result = false;
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

	// 기업담당자 조회
	@RequestMapping(method = RequestMethod.GET, value = "getEmpManager.do")
	@ResponseBody
	public ResponseEntity<String> getEmpManagerCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		int total 		  = 0;

		try {
			logger.info(":::::::::::::::::::: getEmpManager :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=> "  + loginVO.getBizId());
				logger.info("userId=> " + vo.getUserId());

				vo.setBizId(loginVO.getBizId());
				vo.setEmpNo("admin");  // 기업담당자
				EmpVO result = empService.getEmpNo(vo);

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
		return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET, value = "getEmpUserGrpList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getEmpUserGrpListCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		int total 		  = 0;

		try {
			logger.info(":::::::::::::::::::: getEmpUserGrpList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=> " 		+ loginVO.getBizId());
				logger.info("searchKey=> " 	+ vo.getSearchKey());
				logger.info("searchValue=> " + vo.getSearchValue());

				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());

				List<EmpVO> result = empService.getEmpUserGrpList(vo);
				total 			   = empService.getEmpUserGrpListCount(vo);

				if (result != null && result.size() != 0) {
					jsonObject.put("total", total);
					jsonObject.put("data", result);
				} else {
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
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


	// 앱 설치 안내 메세지 전송
	@RequestMapping(method = RequestMethod.GET, value = "sendAppInsSms.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendAppSmsCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		int total 		  = 0;
		List<EmpVO> list  = new ArrayList<EmpVO>();

		try {
			logger.info(":::::::::::::::::::: sendAppInsSms :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=> " + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				String multiData = vo.getMultiData();

				if (StringUtil.isNotNull(multiData)) {
					String[] params = StringUtil.split(multiData, "|");
					for (int i = 0; i < params.length; i++) {
						String[] subParams = StringUtil.split(params[i], "_");
						if (subParams.length > 0) {
							EmpVO paramVO = new EmpVO();
							paramVO.setBizId(vo.getBizId());
							// 첫 번째 파라미터를 userId로 설정
							paramVO.setUserId(subParams[0]);
							list.add(paramVO);
						}
					}
				}

				if (list != null && list.size() > 0) {
					total = empService.sendAppInsSms(list);

					if (total > 0) {
						jsonObject.put("total", total);
						success = true;
					} else {
						jsonObject.put("total", total);
					}
				} else {
					status = HttpStatus.NOT_MODIFIED;
				}
				jsonObject.put("success", success);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, value = "getCustomerList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getCustomerListCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		int total 		  = 0;

		try {
			logger.info(":::::::::::::::::::: getCustomerList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				String ipAddr				 = request.getHeader("X-Forwarded-For");
				if (ipAddr == null) { ipAddr = request.getHeader("Proxy-Client-IP"); }
				if (ipAddr == null) { ipAddr = request.getHeader("WL-Proxy-Client-IP");}
				if (ipAddr == null) { ipAddr = request.getHeader("HTTP_CLIENT_IP"); }
				if (ipAddr == null) { ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR"); }
				if (ipAddr == null) { ipAddr = request.getRemoteAddr(); }

				logger.info("bizId=> " 		+ loginVO.getBizId());
				logger.info("searchKey=> " 	+ vo.getSearchKey());
				logger.info("searchValue=> " + vo.getSearchValue());

				if (!StringUtil.isNotNull(vo.getBizId())) { vo.setBizId(loginVO.getBizId()); }

				if (vo.getBizId().equals("middle")) { vo.setBizId(""); }

				// 중간관리자인 경우 insEmpNo(userId) 필터추가
				if (loginVO.getUserType().equals("5")) { vo.setInsEmpNo(loginVO.getUserId()); }

				loginVO.setIpAddr(ipAddr);
				List<EmpVO> result = empService.getCustomerList(vo, loginVO);
				total 			   = empService.getCustomerListCount(vo);

				if (result != null && result.size() != 0) {
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
				} else if (result == null) {
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				} else if (result.size() == 0) {
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

	@RequestMapping(method = RequestMethod.POST, value = "insCustomer.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insCustomerCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;

		try {
			logger.info(":::::::::::::::::::: insCustomer :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=> "  + loginVO.getBizId());
				logger.info("userId=> " + vo.getUserId());

				if (vo.getBizId() == null) { vo.setBizId(loginVO.getBizId()); }

				vo.setLoginId(loginVO.getLoginId());
				// 현재 로그인한 사용자의 userId를 구분값으로 전달
				vo.setInsEmpNo(loginVO.getUserId());
				vo.setUpdEmpNo(loginVO.getUserId());

				success = empService.insCustomer(vo);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 거래처 일괄등록
	@RequestMapping(method = RequestMethod.POST, value = "sendCustomerExcel.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendCustomerExcelCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result 	  = false;
		logger.info(":::::: sendCustomerExcel :::::: ");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setBizId(loginVO.getBizId());
				logger.info("bizId : " 	+ vo.getBizId());
				logger.info("smsType : " + vo.getSmsSendType());

				// 파일생성
				String szSavePath 			= MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total 					= resultFileList.size();
				logger.info("거래처정보 XLS File Count : " + resultFileList.size());

				if (total == 1) {
					// 전달받은 파일리스트
					for (int i = 0; i < resultFileList.size(); i++) {
						FileVO fileVO  = resultFileList.get(i);
						String xlsPath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();

						File file = new File(xlsPath);
						if (!file.exists()) {
							logger.info("[sendEmpExcel] XLS파일이 존재하지 않습니다.");
							jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
							result = false;
						} else {
							// DATABASE 처리
							EmpVO resultEmpVO = empService.sendCustomerExcel(vo.getBizId(), xlsPath, loginVO.getUserId());
							if (resultEmpVO == null) {
								jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
								result = false;
							} else {
								if (StringUtil.isNotNull(resultEmpVO.getEmpNonce())) {
									if (resultEmpVO.getEmpNonce().equals("-1")) {
										jsonObject.put("message", resultEmpVO.getEmpName() + "님의 거래처정보에서 입력하신 사업자등록번호 데이터가 존재하지않습니다.");
										result = false;
									} else if (resultEmpVO.getEmpNonce().equals("-2")) {
										jsonObject.put("message", resultEmpVO.getEmpName() + "님의 거래처 사업자등록번호가 알맞지 않습니다.");
										result = true;
									} else {

										String totCnt = resultEmpVO.getEmpNonce();
										String insCnt = resultEmpVO.getInsCount();
										String updCnt = resultEmpVO.getUpdCount();
										String dupCnt = resultEmpVO.getDupCount();
										String updY   = resultEmpVO.getDuplicateEmpRow() + "행의 소속기업정보가 기존의 소속기업정보와 다릅니다. \n 다시 확인해 주시기 바랍니다.";

										if (totCnt.equals(dupCnt)){
											jsonObject.put("message", "입력하신 거래처 정보가 기존의 거래처 정보와 모두 동일합니다. \n 다시 확인해 주시기 바랍니다.");
										} else {
											jsonObject.put("message", "총 " + totCnt + " 건의 거래처 정보 중 " + insCnt +
													" 건 저장, "  + updCnt + " 건 실패하였습니다. \n" + resultEmpVO.getInsCount() +
													" 건의 거래처 정보를 등록하였습니다. \n" + (!"0".equals(updCnt) ? updY : ""));
										}
										result = true;
									}
								} else {
									jsonObject.put("message", resultEmpVO.getEmpName() + "님의 거래처정보에서 필수항목이 누락된 데이터가 존재합니다.");
									result = false;
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
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			//throw ex;
		}
		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET, value = "getCustomer.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getCustomerCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		int total 		  = 0;
		logger.info(":::::::::::::::::::: getCustomer :::::::::::::::::::");

		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				String ipAddr				 = request.getHeader("X-Forwarded-For");
				if (ipAddr == null) { ipAddr = request.getHeader("Proxy-Client-IP"); }
				if (ipAddr == null) { ipAddr = request.getHeader("WL-Proxy-Client-IP");}
				if (ipAddr == null) { ipAddr = request.getHeader("HTTP_CLIENT_IP"); }
				if (ipAddr == null) { ipAddr = request.getHeader("HTTP_X_FORWARDED_FOR"); }
				if (ipAddr == null) { ipAddr = request.getRemoteAddr();}

				logger.info("bizId=> "  + loginVO.getBizId());
				logger.info("userId=> " + vo.getUserId());

				if (vo.getBizId() == null) { vo.setBizId(loginVO.getBizId()); }

				EmpVO result = empService.getCustomer(vo);

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

	@RequestMapping(method = RequestMethod.POST, value = "updCustomer.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updCustomerCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;

		try {
			logger.info(":::::::::::::::::::: updCustomer :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=> "  + loginVO.getBizId());
				logger.info("userId=> " + vo.getUserId());

				if (vo.getBizId() == null) { vo.setBizId(loginVO.getBizId()); }

				vo.setUpdEmpNo(loginVO.getUserId());
				int result = empService.updCustomer(vo);
				if (result > 0) success = true;
				jsonObject.put("total", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET, value = "delCustomer.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delCustomerCtrl(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success   = false;

		try {
			logger.info(":::::::::::::::::::: delEmp :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=> "  + loginVO.getBizId());
				logger.info("userId=> " + vo.getUserId());
				vo.setBizId(loginVO.getBizId());

				int result = empService.delCustomer(vo);

				if (result > 0) success = true;
				jsonObject.put("total", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 인사 목록 다운로드
	@RequestMapping(method = RequestMethod.POST, value = "excelUserDown.do")
	@ResponseBody
	public ResponseEntity<JSONObject> excelUserDown(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpStatus status = HttpStatus.OK;
		int result 		  = 0;

		try {
			logger.info(":::::::::::::::::::: excelUserDown :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String excelFilePath = MultipartFileUtil.getSystemTempPath() + MultipartFileUtil.SEPERATOR;
				String excelFileName = vo.getBizName()+"_인사정보_"+ com.ezsign.framework.util.DateUtil.getDate()+ ".xls";
				FileUtil.createDirectory(excelFilePath);

				result    = empService.downUserDataExcel(excelFilePath, excelFileName, vo);
				File file = new File(excelFilePath + excelFileName);
				HttpUtil.setResponseFile(request, response, file, excelFileName);

				if (!file.exists())   { status = HttpStatus.NO_CONTENT; }
				else if (result == 0) { status = HttpStatus.NOT_MODIFIED; }
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity(status);
	}

	// 인사 목록 다운로드 (부문 관리자)
	@RequestMapping(method = RequestMethod.POST, value = "excelUserGrpDown.do")
	@ResponseBody
	public ResponseEntity<JSONObject> excelUserGrpDown(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpStatus status = HttpStatus.OK;
		int result		  = 0;

		try {
			logger.info(":::::::::::::::::::: excelUserGrpDown :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String excelFilePath = MultipartFileUtil.getSystemTempPath() + MultipartFileUtil.SEPERATOR;
				String excelFileName = vo.getBizName()+ "_"+ vo.getGroupName() +"_인사정보_"+ com.ezsign.framework.util.DateUtil.getDate()+ ".xls";
				FileUtil.createDirectory(excelFilePath);

				result    = empService.downUserGrpDataExcel(excelFilePath, excelFileName, vo);
				File file = new File(excelFilePath + excelFileName);
				HttpUtil.setResponseFile(request, response, file, excelFileName);

				if (!file.exists())   { status = HttpStatus.NO_CONTENT; }
				else if (result == 0) { status = HttpStatus.NOT_MODIFIED; }
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity(status);
	}

	// 거래처 목록 다운로드
	@RequestMapping(method = RequestMethod.POST, value = "excelCustomerDown.do")
	@ResponseBody
	public ResponseEntity<JSONObject> excelCustomerDown(@ModelAttribute("EmpVO") EmpVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpStatus status = HttpStatus.OK;
		int result		  = 0;

		try {
			logger.info(":::::::::::::::::::: excelCustomerDown :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			String bizName    = request.getParameter("bizName");

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String excelFilePath = MultipartFileUtil.getSystemTempPath() + MultipartFileUtil.SEPERATOR;
				String excelFileName = bizName+"_거래처정보_"+ com.ezsign.framework.util.DateUtil.getDate()+ ".xls";
				FileUtil.createDirectory(excelFilePath);

				result    = empService.downCustomerDataExcel(excelFilePath, excelFileName, vo);
				File file = new File(excelFilePath + excelFileName);
				HttpUtil.setResponseFile(request, response, file, excelFileName);

				if (!file.exists())   { status = HttpStatus.NO_CONTENT; }
				else if (result == 0) { status = HttpStatus.NOT_MODIFIED; }
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity(status);
	}

	// 인사 목록 파일 다운로드
	@RequestMapping(method = RequestMethod.GET, value = "excelUserDownFile.do")
	public void excelUserDownFile(HttpServletRequest request, HttpServletResponse response) {

		logger.info(":::::::::::::::::::: excelUserDownFile :::::::::::::::::::");

		try {
			String fileName = request.getParameter("fileName");
			String filePath = MultipartFileUtil.getSystemTempPath() + MultipartFileUtil.SEPERATOR;
			File file 		= new File(filePath + fileName);

			if (file.exists()) { HttpUtil.setResponseFile(request, response, file, fileName); }

		} catch (Exception ex) { logService.error(ex.getMessage(), new Throwable(ex)); }
	}

	// 거래처 목록 파일 다운로드
	@RequestMapping(method = RequestMethod.GET, value = "excelCustomerDownFile.do")
	public void excelCustomerDownFile(HttpServletRequest request, HttpServletResponse response) {

		logger.info(":::::::::::::::::::: excelCustomerDownFile :::::::::::::::::::");

		try {
			String fileName = request.getParameter("fileName");
			String filePath = MultipartFileUtil.getSystemTempPath() + MultipartFileUtil.SEPERATOR;
			File file 		= new File(filePath + fileName);

			if (file.exists()) { HttpUtil.setResponseFile(request, response, file, fileName); }

		} catch (Exception ex) { logService.error(ex.getMessage(), new Throwable(ex)); }
	}
}

