package com.ezsign.feb.worker.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.code.service.CodeService;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.service.YS031Service;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.feb.worker.service.YE020Service;
import com.ezsign.feb.worker.vo.YE020VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.HttpUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;


@Controller
@Api(tags = "YE020Controller", description = "추가제출서류")
@RequestMapping("/febworker/")
public class YE020Controller extends BaseController {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "codeService")
	private CodeService codeService;

	@Resource(name = "ye020Service")
	private YE020Service ye020Service;
	
	@Resource(name = "ys030Service")
	private YS030Service ys030Service;
	
	@Resource(name = "ys031Service")
	private YS031Service ys031Service;
	
	// 연말정산추가제출서류 조회
	@ApiOperation(value = "연말정산 추가제출서류 조회", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getYE020List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE020ListCtrl(@ModelAttribute("YE020VO") YE020VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getYE020List :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.set작업자ID(loginVO.getUserId());
				
				List<YE020VO> result = ye020Service.getYE020List(vo);

				total = ye020Service.getYE020ListCount(vo);
				
				// 사업장 리스트
				YS030VO ys030VO = new YS030VO();
				ys030VO.setBizId(vo.getBizId());
				ys030VO.set계약ID(vo.get계약ID());
				ys030VO.setStartPage(0);
				ys030VO.setEndPage(100);
				List<YS030VO> ys030List = ys030Service.getYS030List(ys030VO);
				
				// 부서 코드 조회
				YS031VO ys031VO = new YS031VO();
				ys031VO.set계약ID(vo.get계약ID());
				ys031VO.setStartPage(0);
				ys031VO.setEndPage(200);
				List<YS031VO> ys031List = ys031Service.getYS031List(ys031VO);
				
				// 근무상태
				CodeVO codeVO = new CodeVO();
				codeVO.setGrpCommCode("420");
				List<CodeVO> code420List = codeService.getCodeList(codeVO);
				
				// 추가제출서류 공제구분코드 리스트
				CodeVO codeVO1 = new CodeVO();
				codeVO1.setGrpCommCode("040");
				List<CodeVO> code040List = codeService.getCodeList(codeVO1);
				
				jsonObject.put("ys030", ys030List);
				jsonObject.put("ys031", ys031List);
				jsonObject.put("code420", code420List);
				jsonObject.put("code040", code040List);
				jsonObject.put("total", total);
				jsonObject.put("data", result);
			
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 연말정산 추가제출서류 첨부파일 조회
	@ApiOperation(value = "연말정산 추가제출서류  첨부파일 조회", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getYE020FileList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE020FileListCtrl(@ModelAttribute("YE020VO") YE020VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getYE020FileList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.set작업자ID(loginVO.getUserId());
				
				List<YE020VO> result = ye020Service.getYE020FileList(vo);

				total = ye020Service.getYE020FileListCount(vo);
				
				// 공제구분 리스트
				CodeVO codeVO = new CodeVO();
				codeVO.setGrpCommCode("430");
				List<CodeVO> deductCodeList = codeService.getCodeList(codeVO);
				
				if(result != null){
					success = true;
				}
				
				jsonObject.put("success", success);
				jsonObject.put("total", total);
				jsonObject.put("data", result);
				jsonObject.put("deductCodeList", deductCodeList);
				
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
		
	// 연말정산 추가제출서류 입력
	@ApiOperation(value = "연말정산 추가제출서류  입력", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "insYE020.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insYE020Ctrl(@ModelAttribute("YE020VO") YE020VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		try {
			System.out.println(":::::::::::::::::::: insYE020 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.set작업자ID(loginVO.getUserId());
				
				// 일반 근로자인 경우 본인 등록
				int userType = Integer.parseInt(loginVO.getUserType());
				if(userType < 5) {
					vo.set사용자ID(loginVO.getUserId());
				}
				// 파일생성
				String szMonthPath  = vo.getBizId();
				String szSavePath = MultipartFileUtil.getSystemTempPath() + szMonthPath;
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = resultFileList.size();
				System.out.println("추가제출  File Count : "+resultFileList.size());
			
				// 전달받은 파일리스트
				for(int i=0;i<resultFileList.size();i++) {
					FileVO fileVO = resultFileList.get(i);
					String filePath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();

					System.out.println("fileVO : " + fileVO);
					File file = new File(filePath);
					System.out.println("filePath : " + filePath);
					if (!file.exists()) {
						System.out.println("[insYE020] 파일이 존재하지 않습니다.");
						message = "파일이 존재하지 않습니다.";
					} else {
						ye020Service.insYE020(vo, fileVO);
						message = "추가서류가 등록 되었습니다.";
						success = true;
						
						// temp 파일 삭제 
						boolean deleteResult = FileUtil.deleteFile(filePath);
						if(!deleteResult) {
							System.out.println("임시 폴더에 파일이 삭제 되지 않았습니다.");
						}
					}
				}
				
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("message", message);
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 연말정산 추가제출서류 수정
	@ApiOperation(value = "연말정산 추가제출서류  수정", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "updYE020.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updYE020Ctrl(@ModelAttribute("YE020VO") YE020VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		int result = 0;
		try {
			System.out.println(":::::::::::::::::::: updYE020 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				// 일반 근로자인 경우 본인 등록
				int userType = Integer.parseInt(loginVO.getUserType());
				if(userType < 5) {
					vo.set사용자ID(loginVO.getUserId());
				}
				
				// 파일생성
				String szMonthPath  = vo.getBizId();
				String szSavePath = MultipartFileUtil.getSystemTempPath() + szMonthPath;
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = resultFileList.size();
				System.out.println("추가제출  File Count : "+resultFileList.size());
			
				// 전달받은 파일리스트
				if(resultFileList.size() > 0) {
					for(int i=0;i<resultFileList.size();i++) {
						FileVO fileVO = resultFileList.get(i);
						String filePath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
						
						System.out.println("fileVO : " + fileVO);
						File file = new File(filePath);
						if (!file.exists()) {
							System.out.println("[updYE020] 파일이 존재하지 않습니다.");
							message = "파일이 존재하지 않습니다.";
						} else {
							result = ye020Service.updYE020(vo, fileVO);
							if(result > 0) {
								message = "추가서류가 수정 되었습니다.";
							} else {
								message = "추가서류가 수정 되지 않았습니다.";
							}
							success = true;
							
							// temp 파일 삭제 
							boolean deleteResult = FileUtil.deleteFile(filePath);
							if(!deleteResult) {
								System.out.println("임시 폴더에 파일이 삭제 되지 않았습니다.");
							}
						}
					}
				} else {
					// 첨부파일 수정 없이 내용 수정
					result = ye020Service.updYE020(vo, null);
					if(result > 0) {
						message = "추가서류가 수정 되었습니다.";
					} else {
						message = "추가서류가 수정 되지 않았습니다.";
					}
					success = true;
				}
				
				jsonObject.put("total", result);
				
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("message", message);
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 연말정산 추가제출서류 삭제
	@ApiOperation(value = "연말정산 추가제출서류  삭제", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "delYE020.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delYE020Ctrl(@ModelAttribute("YE020VO") YE020VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		int result = 0;
		
		try {
			System.out.println(":::::::::::::::::::: delYE020 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				result = ye020Service.delYE020(vo);
				
				if(result > 0 ) {
					success = true;
					message = "추가제출 서류가 삭제 되었습니다.";
				} else {
					message = "추가제출 서류가 삭제 되지 않았습니다. ";
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("message", message);
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 연말정산 추가제출서류 처리확인
	@ApiOperation(value = "연말정산 추가제출서류  처리확인", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "updYE020Confirm.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updYE020ConfirmCtrl(@ModelAttribute("YE020VO") YE020VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		int result = 0;
		
		try {
			System.out.println(":::::::::::::::::::: updYE020Confirm :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				System.out.println("처리여부=>"+vo.get처리여부());
				vo.setBizId(loginVO.getBizId());
				vo.set작업자ID(loginVO.getUserId());
				vo.set처리일시(DateUtil.getTimeStamp(7));
				result = ye020Service.updYE020Confirm(vo);
				
				if(result > 0) {
					success = true;
					message = "처리완료하였습니다";
				} else {
					message = "입력값을 확인하여 주시길 바랍니다.";
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		jsonObject.put("message", message);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 연말정산 추가제출서류 첨부파일 확인 처리
	@ApiOperation(value = "연말정산 추가제출서류  첨부파일 확인 처리", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "saveYE020.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveYE020Ctrl(@RequestBody List<YE020VO> list, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: saveYE020 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				String bizId = loginVO.getBizId();
				for(int i=0;i<list.size();i++) {
					YE020VO ye020VO = list.get(i); 
					ye020VO.setBizId(loginVO.getBizId());
					ye020VO.set작업자ID(loginVO.getUserId());
					list.set(i, ye020VO);
				}
				
				ye020Service.saveYE020(list);
				
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
	
	// 연말정산 추가제출서류 첨부파일 가져오기
	@ApiOperation(value = "연말정산 추가제출서류  첨부파일 가져오기", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getYE020File.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE020FileCtrl(@ModelAttribute("YE020VO") YE020VO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getYE020File :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.set파일ID(vo.get파일ID());
				
				String fileName = ye020Service.downloadAttachmentFile(vo);
				String szSavePath = MultipartFileUtil.getSystemTempPath();
				String szFileName = szSavePath + vo.getBizId() +  MultipartFileUtil.SEPERATOR + fileName;
				
//				FileUtil.createDirectory(szSavePath);
//				File file = new File(szFileName);
				
				String filePath =  szFileName.substring(szFileName.indexOf("temp"));
				if(StringUtil.isNotNull(filePath)) {
					success = true;
				}
				jsonObject.put("success", success);
				jsonObject.put("data", filePath);
//				HttpUtil.setResponseFile(request, response, file, fileName);
//				if(!file.exists()) status = HttpStatus.NO_CONTENT;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	

	/**
	 * 추가제출서류 파일을 다운로드한다.
	 * 
	 * @param list
	 * @param request
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping(value = "downFileYE020.do")
//	@ResponseBody
//	public ResponseEntity<JSONObject> downFileYE020Ctrl(@ModelAttribute("YE020VO") YE020VO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
//	{
//		
//		HttpStatus status = HttpStatus.OK;
//		
//		try {
//			System.out.println(":::::::::::::::::::: downFileYE020Ctrl :::::::::::::::::::");
//			SessionVO loginVO = SessionUtil.getSession(request);
//			
//			if (loginVO == null) {
//				status = HttpStatus.UNAUTHORIZED;
//			} else {
//				
//				Log.info("# 파일ID : " + vo.get파일ID());
//				
//				String fileIds[] = null;
//				String contractId = null;				
//				
//				if(vo != null && StringUtils.isNotEmpty(vo.get파일ID())){
//					
//					Log.info("# 파일ID substring : " + vo.get파일ID().substring(0,vo.get파일ID().length()-1) );
//					
//					fileIds = vo.get파일ID().substring(0,vo.get파일ID().length()-1).split(",");
//					contractId = vo.get계약ID();
//					
//					logger.info("# 계약 아이디 : " + contractId );
//					
//					
//					if(fileIds != null && fileIds.length > 0){
//						
//						//추가제출서류 zip 파일 다운로드
//						Map<String,String> resultMap = ye020Service.downloadWorkerFileZip(fileIds, loginVO);
//						
//						if(resultMap == null || StringUtils.isEmpty(resultMap.get("zipFilePatn"))){
//							status = HttpStatus.NOT_MODIFIED;
//						}else{
//							
//							File file = new File(resultMap.get("zipFilePatn"));
//							HttpUtil.setResponseFile(request, response, file, contractId+".zip");
//							if(!file.exists()) {
//								status = HttpStatus.NO_CONTENT;
//							}
//							
//						}
//
//					}
//					
//				}
//				
//			}
//			
//		} catch (Exception ex) {
//			
//			logger.error(ex.getMessage(), ex);
//			
//			status = HttpStatus.INTERNAL_SERVER_ERROR;
//			logService.error(ex.getMessage(), new Throwable(ex));
//			throw ex;
//		}
//		
//		return new ResponseEntity<JSONObject>(status);
//		
//	}
	

	/**
	 *
	 * 추가제출서류 zip 파일을 생성한다.
	 *
	 * @Author		: soybean0627
	 * @Date        : 2019. 1. 4.
	 * @Method Name : makeZipFileDocument 
	 *
	 * @param list
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "추가제출서류 zip 파일을 생성", produces = "application/json")
	@RequestMapping(value = "makeYE020ZipDocument.do")
	@ResponseBody
	public ResponseEntity<JSONObject> makeYE020ZipDocument(@RequestBody List<YE020VO> list, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		Map<String,String> resultMap = null;
		
		try {
			
			logger.debug(":::::::::::::::::::: makeZipFileDocument :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
				if(list != null && list.size() > 0){
				
					//추가제출서류 zip 파일 생성
					resultMap = ye020Service.makeYE020ZipDocument(list, loginVO);
					
					if(resultMap == null){
						status = HttpStatus.OK;
						
						success = false;
						message = "추가제출서류 파일 정보가 없습니다.";
						
					}else{
						
						if(StringUtils.equals("false",resultMap.get("success"))){						
							success = false;
							message = resultMap.get("message");						
						}else{					
							success = true;
						}
					}

				}else{
					status = HttpStatus.OK;
					
					success = false;
					message = "추가제출서류 파일 아이디 정보가 없습니다.";
				}
				
			}
			
		} catch (Exception ex) {
			
			logger.error(ex.getMessage(), ex);
			
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
			
		}
		

		jsonObject.put("success", success);
		jsonObject.put("message", message);
		jsonObject.put("zipFilePath", resultMap.get("zipFilePath"));
				
		logger.info("### jsonObject.toString() :  " + jsonObject.toString());
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
		
	}
	
	
	/**
	 * 해당 경로 파일을 다운로드 한다.
	 *
	 * @param zipFilePatn
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "추가제출서류 파일 다운로드", produces = "application/json")
	@RequestMapping(value = "downloadFileAct.do")
	@ResponseBody
	public ResponseEntity<JSONObject> downloadFileAct(@RequestParam("zipFilePath") String zipFilePath, @RequestParam("downName") String downName, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			
			logger.debug(":::::::::::::::::::: downloadFileAct :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
						
				File file = new File(zipFilePath);
				
				if(file.exists() && file.isFile()){
					HttpUtil.setResponseFile(request, response, file, downName+"_"+loginVO.getBizName()+".zip");
				}else{
					logger.error("# downloadFileAct => [ "+file.getAbsolutePath()+ " ] 파일을 찾을 수 없습니다.");
				}
				
				if(!file.exists()) {
//					status = HttpStatus.NO_CONTENT;
					status = HttpStatus.INTERNAL_SERVER_ERROR;
				}
			}
			
		} catch (Exception ex) {
			
			logger.error(ex.getMessage(), ex);
			
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
			
		}
		
		return new ResponseEntity<JSONObject>(status);
		
	}
	
	// 연말정산 국세청PDF 업로드 추가제출서류 입력
	@ApiOperation(value = "국세청PDF 업로드 추가제출서류 입력", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "insYE020Pdf.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insYE020PdfCtrl(@ModelAttribute("YE020VO") YE020VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		try {
			System.out.println(":::::::::::::::::::: insYE020Pdf :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.set작업자ID(loginVO.getUserId());
				
				// 일반 근로자인 경우 본인 등록
				int userType = Integer.parseInt(loginVO.getUserType());
				if(userType < 5) {
					vo.set사용자ID(loginVO.getUserId());
				}
				// 파일생성
				String szMonthPath  = vo.getBizId();
				String szSavePath = MultipartFileUtil.getSystemTempPath() + szMonthPath;
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = resultFileList.size();
				System.out.println("추가제출  File Count : "+resultFileList.size());
			
				// 전달받은 파일리스트
				for(int i=0;i<resultFileList.size();i++) {
					FileVO fileVO = resultFileList.get(i);
					String filePath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();

					System.out.println("fileVO : " + fileVO);
					File file = new File(filePath);
					System.out.println("filePath : " + filePath);
					if (!file.exists()) {
						System.out.println("[insYE020Pdf] 파일이 존재하지 않습니다.");
						message = "파일이 존재하지 않습니다.";
					} else {
						ye020Service.insYE020(vo, fileVO);
						message = "국세청PDF 업로드가 등록 되었습니다.";
						success = true;
						
						// temp 파일 삭제 
//						boolean deleteResult = FileUtil.deleteFile(filePath);
//						if(!deleteResult) {
//							System.out.println("임시 폴더에 파일이 삭제 되지 않았습니다.");
//						}
					}
				}
				
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("message", message);
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 연말정산 국세청PDF 추가제출서류 조회
	@ApiOperation(value = "국세청PDF 추가제출서류 조회", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET , value = "getYE020Pdf.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE020Pdf(@ModelAttribute("YE020VO") YE020VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getYE020Pdf :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.set작업자ID(loginVO.getUserId());
				
				List<YE020VO> result = ye020Service.getYE020List(vo);

				total = ye020Service.getYE020ListCount(vo);
				
				jsonObject.put("total", total);
				jsonObject.put("data", result);
				
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
}
