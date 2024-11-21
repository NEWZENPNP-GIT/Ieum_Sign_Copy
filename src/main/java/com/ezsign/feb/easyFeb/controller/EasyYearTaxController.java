package com.ezsign.feb.easyFeb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.feb.easyFeb.service.EasyYearTaxService;
import com.ezsign.feb.easyFeb.vo.AttachFile;
import com.ezsign.feb.easyFeb.vo.AttachFileVo;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.worker.service.YE020Service;
import com.ezsign.feb.worker.vo.YE020VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.vo.WorkMonth;
import com.ezsign.window.vo.YW710Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@SuppressWarnings("NonAsciiCharacters")
@Controller
@Api(tags = "EasyYearTaxController", description = "손쉬운 연말정산")
@RequestMapping("/easyFeb/")
public class EasyYearTaxController {

	@Resource(name = "logService")
    private LogService logService;
	
	@Resource(name = "easyYearTaxService")
    private EasyYearTaxService easyYearTaxService;
	
	@Resource(name = "febMasterService")
    private FebMasterService febMasterService;
	
	@Resource(name = "ye020Service")
	private YE020Service ye020Service;
	
	@ApiOperation(value = "보험료 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getInsurance.do")
    @ResponseBody
    public ResponseEntity<YW710Response> getInsurance(String 계약ID, String 사용자ID, HttpServletRequest request) throws Exception {
	
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YW710Response response = new YW710Response();
        response.success = false;
        
        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));
        
        response.보험료 = new ArrayList<>();
        
        try {
            System.out.println(":::::::::::::::::::: getInsurance :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	easyYearTaxService.getInsurance(loginVO.getBizId(), 계약ID, 사용자ID, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
	}
	
	@ApiOperation(value = "의료비 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getMedical.do")
    @ResponseBody
	public ResponseEntity<YW710Response> getMedical(String 계약ID, String 사용자ID, HttpServletRequest request) throws Exception {
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YW710Response response = new YW710Response();
        response.success = false;
        
        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));
        
        response.의료비 = new ArrayList<>();
        
        try {
            System.out.println(":::::::::::::::::::: getMedical :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	easyYearTaxService.getMedical(loginVO.getBizId(), 계약ID, 사용자ID, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
	}
	
	@ApiOperation(value = "교육비 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getEducation.do")
    @ResponseBody
	public ResponseEntity<YW710Response> getEducation(String 계약ID, String 사용자ID, HttpServletRequest request) throws Exception {
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YW710Response response = new YW710Response();
        response.success = false;
        
        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));
        
        response.교육비 = new ArrayList<>();
        
        try {
            System.out.println(":::::::::::::::::::: getEducation :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	easyYearTaxService.getEducation(loginVO.getBizId(), 계약ID, 사용자ID, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
		
		return new ResponseEntity<>(response, responseHeaders, status);
	}
	
	@ApiOperation(value = "기부금 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getDonation.do")
    @ResponseBody
	public ResponseEntity<YW710Response> getDonation(String 계약ID, String 사용자ID, HttpServletRequest request) throws Exception {
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YW710Response response = new YW710Response();
        response.success = false;
        
        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));
        
        response.기부금 = new ArrayList<>();
        
        try {
            System.out.println(":::::::::::::::::::: getDonation :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	easyYearTaxService.getDonation(loginVO.getBizId(), 계약ID, 사용자ID, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
		
		return new ResponseEntity<>(response, responseHeaders, status);
	}
	
	@ApiOperation(value = "보험료 파일 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getInsuranceFile.do")
    @ResponseBody
    public ResponseEntity<AttachFileVo> getInsuranceFile(String 계약ID, String 사용자ID, String 공제구분코드, HttpServletRequest request) throws Exception {
	
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        AttachFileVo response = new AttachFileVo();
        response.success = false;

        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));
        
        response.보험료 = new ArrayList<>();
        
        try {
            System.out.println(":::::::::::::::::::: getInsuranceFile :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	easyYearTaxService.getInsuranceFile(loginVO.getBizId(), 계약ID, 사용자ID, 공제구분코드, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
	}
	
	@ApiOperation(value = "의료비 파일 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getMedicalFile.do")
    @ResponseBody
	public ResponseEntity<AttachFileVo> getMedicalFile(String 계약ID, String 사용자ID, String 공제구분코드, HttpServletRequest request) throws Exception {
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        AttachFileVo response = new AttachFileVo();
        response.success = false;
        
        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));
        
        response.의료비 = new ArrayList<>();
        
        try {
            System.out.println(":::::::::::::::::::: getMedicalFile :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {

            	easyYearTaxService.getMedicalFile(loginVO.getBizId(), 계약ID, 사용자ID, 공제구분코드, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
	}
	
	
	@ApiOperation(value = "교육비 파일 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getEducationFile.do")
    @ResponseBody
	public ResponseEntity<AttachFileVo> getEducationFile(String 계약ID, String 사용자ID, String 공제구분코드, HttpServletRequest request) throws Exception {
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        AttachFileVo response = new AttachFileVo();
        response.success = false;
        
        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));
        
        response.교육비 = new ArrayList<>();
        
        try {
            System.out.println(":::::::::::::::::::: getEducation :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	easyYearTaxService.getEducationFile(loginVO.getBizId(), 계약ID, 사용자ID, 공제구분코드, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
		
		return new ResponseEntity<>(response, responseHeaders, status);
	}	
	
	@ApiOperation(value = "신용카드 파일 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getCreditFile.do")
    @ResponseBody
	public ResponseEntity<AttachFileVo> getCreditFile(String 계약ID, String 사용자ID, String 공제구분코드, HttpServletRequest request) throws Exception {
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        AttachFileVo response = new AttachFileVo();
        response.success = false;
        
        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));
        
        response.신용카드 = new ArrayList<>();
        
        try {
            System.out.println(":::::::::::::::::::: getEducation :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	easyYearTaxService.getCreditFile(loginVO.getBizId(), 계약ID, 사용자ID, 공제구분코드, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
		
		return new ResponseEntity<>(response, responseHeaders, status);
	}
	
	@ApiOperation(value = "월세액 파일 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getMonthlyRentFile.do")
    @ResponseBody
	public ResponseEntity<AttachFileVo> getMonthlyRentFile(String 계약ID, String 사용자ID, String 공제구분코드, HttpServletRequest request) throws Exception {
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        AttachFileVo response = new AttachFileVo();
        response.success = false;
        
        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));
        
        response.월세액공제 = new ArrayList<>();
        
        try {
            System.out.println(":::::::::::::::::::: getEducation :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	easyYearTaxService.getMonthlyRentFile(loginVO.getBizId(), 계약ID, 사용자ID, 공제구분코드, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
		
		return new ResponseEntity<>(response, responseHeaders, status);
	}

	@ApiOperation(value = "기부금 파일 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getDonationFile.do")
    @ResponseBody
	public ResponseEntity<AttachFileVo> getDonationFile(String 계약ID, String 사용자ID, String 공제구분코드, HttpServletRequest request) throws Exception {
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        AttachFileVo response = new AttachFileVo();
        response.success = false;
        
        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));
        
        response.기부금 = new ArrayList<>();
        
        try {
            System.out.println(":::::::::::::::::::: getEducation :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	easyYearTaxService.getDonationFile(loginVO.getBizId(), 계약ID, 사용자ID, 공제구분코드, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
		
		return new ResponseEntity<>(response, responseHeaders, status);
	}
	
	@ApiOperation(value = "부양가족 파일 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getFamilyFile.do")
    @ResponseBody
	public ResponseEntity<AttachFileVo> getFamilyFile(String 계약ID, String 사용자ID, String 공제구분코드, HttpServletRequest request) throws Exception {
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        AttachFileVo response = new AttachFileVo();
        response.success = false;
        
        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));
        
        response.부양가족 = new ArrayList<>();
        
        try {
            System.out.println(":::::::::::::::::::: getEducation :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	easyYearTaxService.getFamilyFile(loginVO.getBizId(), 계약ID, 사용자ID, 공제구분코드, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
		
		return new ResponseEntity<>(response, responseHeaders, status);
	}
	
	// 연말정산_추가제출서류 입력
	@RequestMapping(method = RequestMethod.POST , value = "insYE020.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insYE020Ctrl(@ModelAttribute("attachFile") AttachFile attachFile, HttpServletRequest request) throws Exception
	{

		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
System.out.println("attachFile: " + attachFile);
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		int result = 0;
		
		String bizId = null;
		String 사용자ID = null;
		
		String dbMode = attachFile.getDbMode();
System.out.println("dbMode: " + dbMode);
		try {
			
			System.out.println(":::::::::::::::::::: insYE020 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				bizId = loginVO.getBizId();
				사용자ID = loginVO.getUserId();

				// 파일생성
				String szMonthPath  = bizId;
				String szSavePath = MultipartFileUtil.getSystemTempPath() + szMonthPath;
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);

				int total = resultFileList.size();
				System.out.println("추가제출  File Count : "+resultFileList.size());
			
				if("C".equals(dbMode)){	//파일생성
					// 전달받은 파일리스트
					for(int i=0;i<resultFileList.size();i++) {
						FileVO fileVO = resultFileList.get(i);
						String filePath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();

//						attachFile.setBizId(bizId);
//						attachFile.set사용자ID(사용자ID);
						
						System.out.println("fileVO : " + fileVO);
						File file = new File(filePath);
						System.out.println("filePath : " + filePath);
						if (!file.exists()) {
							System.out.println("[insYE020Ctrl] 파일이 존재하지 않습니다.");
							message = "파일이 존재하지 않습니다.";
						} else {
							if("reg".equals(attachFile.getReg())){
								easyYearTaxService.insAttachFile(bizId, attachFile.get계약ID(), 사용자ID, attachFile, fileVO);
							}else{
								attachFile.get첨부파일().set계약ID(attachFile.get계약ID());
								attachFile.get첨부파일().set사용자ID(사용자ID);
								ye020Service.insYE020(attachFile.get첨부파일(), fileVO);
								
							}
							message = "추가서류가 등록 되었습니다.";
							success = true;
							
							// temp 파일 삭제 
							boolean deleteResult = FileUtil.deleteFile(filePath);
							if(!deleteResult) {
								System.out.println("임시 폴더에 파일이 삭제 되지 않았습니다.");
							}
						}
					}
				}else if("U".equals(dbMode)){ //업데이트
					// 전달받은 파일리스트
					if(resultFileList.size() > 0) {
						for(int i=0;i<resultFileList.size();i++) {
							FileVO fileVO = resultFileList.get(i);
							String filePath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
	
							System.out.println("fileVO : " + fileVO);
							File file = new File(filePath);
							System.out.println("filePath : " + filePath);
							if (!file.exists()) {
								System.out.println("[insYE020Ctrl] 파일이 존재하지 않습니다.");
								message = "파일이 존재하지 않습니다.";
							} else {
								if(!"reg".equals(attachFile.getReg())){
									attachFile.get첨부파일().setBizId(bizId);
									attachFile.get첨부파일().set계약ID(attachFile.get계약ID());
									attachFile.get첨부파일().set사용자ID(사용자ID);
									result = ye020Service.updYE020(attachFile.get첨부파일(), fileVO);
								}else{
									result = easyYearTaxService.uptAttachFile(bizId, attachFile.get계약ID(), 사용자ID, attachFile, fileVO);
								}
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
					}else{
						result = easyYearTaxService.uptAttachFile(bizId, attachFile.get계약ID(), 사용자ID, attachFile, null);
						if(result > 0) {
							message = "추가서류가 수정 되었습니다.";
						} else {
							message = "추가서류가 수정 되지 않았습니다.";
						}
						success = true;
					}

				}else if("D".equals(dbMode)){ //삭제
					
					System.out.println("bizId=>"+loginVO.getBizId());
					result = easyYearTaxService.delAttachFile(bizId, attachFile.get계약ID(), 사용자ID, attachFile);
					
					if(result > 0 ) {
						success = true;
						message = "추가제출 서류가 삭제 되었습니다.";
					} else {
						message = "추가제출 서류가 삭제 되지 않았습니다. ";
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
	
	@RequestMapping(method = RequestMethod.GET , value = "getImageFile.do")
	public void getImageFile(@RequestParam(value = "fileId", required = true) String fileId, HttpServletRequest request, HttpServletResponse response) throws Exception{
System.out.println("getImageFile");		
		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				PrintWriter writer = response.getWriter();
				writer.println("파일이 존재 하지 않습니다.");
				return;
			} else {
				
				YE020VO vo = new YE020VO();
				vo.setBizId(loginVO.getBizId());
				vo.set파일ID(fileId);
				
				String fileName = ye020Service.downloadAttachmentFile(vo);
				String szSavePath = MultipartFileUtil.getSystemTempPath();
				String szFileName = szSavePath + vo.getBizId() +  MultipartFileUtil.SEPERATOR + fileName;
				
				File f = new File(szFileName);
				FileInputStream fin = new FileInputStream(f);
	            ServletOutputStream outStream = response.getOutputStream();
	            response.setContentType("image/jpeg");
	            int i = 0;
	            while (i != -1) {
	                i = fin.read();
	                outStream.write(i);
	            }
	            fin.close();
			}
		} catch (Exception ex) {
			PrintWriter writer = response.getWriter();
			writer.println("파일이 존재 하지 않습니다.");
			return;
		}
	}
	
	@ApiOperation(value = "첨부파일 건수 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getFileCount.do")
    @ResponseBody
	public ResponseEntity<Map> getFileCount(String 계약ID, HttpServletRequest request) throws Exception {
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        Map result = new HashMap();

        try {
            System.out.println(":::::::::::::::::::: getMedical :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	Map data = easyYearTaxService.getFileCount(계약ID, loginVO.getUserId());
            	result.put("success", Boolean.TRUE);
            	result.put("data", data);
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(result, responseHeaders, status);
	}
	
	@ApiOperation(value = "종건근무지 입력 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getWorkplaceCheck.do")
    @ResponseBody
	public ResponseEntity<Map> getWorkplaceCheck(@RequestParam(value="계약ID",  required=true) String 계약ID, HttpServletRequest request) throws Exception {
		
		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        Map result = new HashMap();
        
        try {
            System.out.println(":::::::::::::::::::: getMedical :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	
            	Map params = new HashMap();
            	params.put("사용자ID", loginVO.getUserId());
            	params.put("계약ID", 계약ID);
            	
            	Integer workplaceCount = easyYearTaxService.getWorkplaceCheck(params);
            	
            	result.put("success", Boolean.TRUE);
            	result.put("data", workplaceCount);
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        
        return new ResponseEntity<>(result, responseHeaders, status);
	}
}


