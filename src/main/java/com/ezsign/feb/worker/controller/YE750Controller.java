package com.ezsign.feb.worker.controller;

import java.io.File;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.worker.service.YE750Service;
import com.ezsign.feb.worker.vo.YE750Response;
import com.ezsign.feb.worker.vo.YE750VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.HttpUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.vo.DefaultResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@SuppressWarnings("NonAsciiCharacters")
@Controller
@Api(tags = "YE750Controller", description = "연말정산 영수증 관리")
@RequestMapping("/febworker/")
public class YE750Controller extends BaseController {

	Logger logger = Logger.getLogger(this.getClass());
	
    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "ye750Service")
    private YE750Service ye750Service;

//    @ApiOperation(value = "생성테스트", produces = "application/json")
//    @RequestMapping(method = RequestMethod.POST, value = "createYE750")
//    @ResponseBody
//    public ResponseEntity<DefaultResponse> createYE750Pdf(
//            @RequestParam String 계약ID, @RequestParam String 사용자ID,
//            HttpServletRequest request) throws Exception {
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
//
//        HttpStatus status = HttpStatus.OK;
//
//        DefaultResponse response = new DefaultResponse();
//
//        try {
//            System.out.println(":::::::::::::::::::: createYE750Pdf :::::::::::::::::::");
//            SessionVO loginVO = SessionUtil.getSession(request);
//            if (loginVO == null) {
//                status = HttpStatus.UNAUTHORIZED;
//            } else {
//                ye750Service.createYE750Pdf(loginVO.getBizId(), 계약ID, 사용자ID, request);
//                response.success = true;
//            }
//        } catch (Exception ex) {
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//            logService.error(ex.getMessage(), new Throwable(ex));
//            ex.printStackTrace();
//        }
//
//        return new ResponseEntity<>(response, responseHeaders, status);
//    }

    @ApiOperation(value = "연말정산 영수증 리스트 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getYE750List.do")
    @ResponseBody
    public ResponseEntity<YE750Response> getYE750List(
            @RequestParam String 계약ID, @RequestParam String 사용자ID,
            HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YE750Response response = new YE750Response();

        try {
            System.out.println(":::::::::::::::::::: getYE750List :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	
            	YE750VO ye750VO = new YE750VO();
                ye750VO.set계약ID(계약ID);                
            	ye750VO.set사용자ID(사용자ID);
                
                ye750Service.getYE750List(loginVO.getBizId(), 계약ID, 사용자ID, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "연말정산 영수증 다운로드 및 조회")
    @RequestMapping(method = RequestMethod.GET, value = "getYE750Pdf.do")
    @ResponseBody
    public void getYE750Pdf(
            @RequestParam("id") String id, @RequestParam(required = false) String type,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            System.out.println(":::::::::::::::::::: getYE750Pdf :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                DefaultResponse defaultResponse = new DefaultResponse();
                response.setContentType("application/json; charset=UTF-8");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write(new ObjectMapper().writeValueAsString(defaultResponse));
            } else {
                String[] filePath = ye750Service.getYE750FilePath(id);

                if (filePath == null || filePath.length != 2) {
                    DefaultResponse defaultResponse = new DefaultResponse();
                    defaultResponse.message = "파일이 존재하지 않습니다.";

                    response.setContentType("application/json; charset=UTF-8");
                    response.setStatus(HttpStatus.OK.value());
                    response.getWriter().write(new ObjectMapper().writeValueAsString(defaultResponse));
                } else {
                    File file = new File(filePath[0]);

                    if(file.exists() && file.isFile()){                    
	                    if ("pdf".equals(type)) {
	                        HttpUtil.setResponsePdfView(response, file);
	                    } else {
	                        HttpUtil.setResponseFile(request, response, file, filePath[1]);
	                    }
                    }else{                    	
                    	logger.error("# 연말정산 영수증 다운로드 및 조회  =>[ " +file.getAbsolutePath()+ " ] 파일을 찾을 수 없습니다.");                    	
                    }
                }
            }
        } catch (Exception ex) {
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();

            DefaultResponse defaultResponse = new DefaultResponse();
            defaultResponse.message = ex.getLocalizedMessage();

            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(defaultResponse));
        }
    }
    
    @ApiOperation(value = "연말정산 영수증 생성", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "createYE750Pdf.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> createYE750Pdf(@RequestParam String 계약ID, @RequestParam(required = false) String 사용자ID, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        response.success = false;

        try {
            System.out.println(":::::::::::::::::::: createYE750Pdf :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {

            	YE000VO ye000VO = new YE000VO();
            	ye000VO.set계약ID(계약ID);
            	
            	if (StringUtil.isNotNull(사용자ID)) {
            		ye000VO.set사용자ID(사용자ID);
            	}

            	ye000VO.setBizId(loginVO.getBizId());
            	ye000VO.set사용여부("1"); // 연말정산 대상자
            	ye000VO.setStartPage(0);
            	ye000VO.setEndPage(99999);

            	response = ye750Service.createYE750ReceiptPdf(ye000VO);
                response.success = true;

            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
                
        return new ResponseEntity<>(response, responseHeaders, status);
    }
    
    
    @ApiOperation(value = "연말정산 영수증 zip 파일을 생성", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET, value = "makeYE750ZipDocument.do")
	@ResponseBody
	public ResponseEntity<JSONObject> makeYE750ZipDocument(@RequestParam(value="계약ID", required = false) String 계약ID,
															@RequestParam(value="파일ID", required = false) String 파일ID,				
																HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		Map<String,String> resultMap = null;
		
		try {
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
												
				//연말정산 영수증 zip 파일 생성
				resultMap = ye750Service.makeYE750ZipDocument(계약ID, 파일ID, loginVO);
				
				if(resultMap == null){					
					success = false;
					message = "연말정산 영수증  파일 정보가 없습니다.";					
				}else{
					
					if(StringUtils.equals("false",resultMap.get("success"))){						
						success = false;
						message = resultMap.get("message");						
					}else{		
						
						File zipFile = new File(resultMap.get("zipFilePath"));
						
						if(zipFile.exists() && zipFile.isFile()){
							success = true;							
							jsonObject.put("출력물파일구분명", resultMap.get("출력물파일구분명"));
						}else{
							success = false;
							message = "연말정산 " +resultMap.get("출력물파일구분명")+ " 파일이 없습니다.";
						}

					}
				}
				
			}
			
		} catch (Exception ex) {						
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.error(ex.getMessage(), ex);
			throw ex;
			
		}
		

		jsonObject.put("success", success);
		jsonObject.put("message", message);
		jsonObject.put("zipFilePath", resultMap.get("zipFilePath"));
				
		logger.debug("### jsonObject.toString() :  " + jsonObject.toString());
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
		
	}
      
    @ApiOperation(value = "연말정산 영수증 디폴트 사용자정보 조회", produces = "application/json")
   	@RequestMapping(method = RequestMethod.GET, value = "getYE750UserId.do")
   	@ResponseBody
   	public ResponseEntity<JSONObject> getYE750UserId(@RequestParam(value="계약ID", required = false) String 계약ID,			
   																HttpServletRequest request) throws Exception
   	{
    	JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
				
		try {
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
				YE750VO ye750VO = new YE750VO();
				ye750VO.set계약ID(계약ID);
				ye750VO = ye750Service.getYE750UserId(ye750VO);
				
				if(ye750VO != null){		
					success = true;
					
					jsonObject.put("계약ID", ye750VO.get계약ID());
					jsonObject.put("사용자ID", ye750VO.get사용자ID());	
					jsonObject.put("empName", ye750VO.getEmpName());					
				}

			}
			
		} catch (Exception ex) {			
			logger.error(ex.getMessage(), ex);			
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			throw ex;
			
		}	
		
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
   	}
    
    
    @ApiOperation(value = "연말정산 영수증 메일 발송", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET, value = "sendMailYE750.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendMailYE750(@RequestParam(value="계약ID", required = false) String 계약ID,
															@RequestParam(value="파일ID", required = false) String 파일ID,				
																HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		Map<String,String> resultMap = null;
		
		try {

			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
												
				//연말정산 영수증 메일 발송
				resultMap = ye750Service.sendMailYE750(계약ID, 파일ID, loginVO);
				
				if(resultMap == null){					
					success = false;
					message = "연말정산 영수증  파일 정보가 없습니다.";					
				}else{
					
					if(StringUtils.equals("false",resultMap.get("success"))){						
						success = false;
						message = resultMap.get("message");						
					}else{								
						success = true;	
						message = resultMap.get("message");
					}
				}
				
			}
			
			
		} catch (Exception ex) {						
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.error(ex.getMessage(), ex);
			throw ex;
			
		}
		
		jsonObject.put("success", success);
		jsonObject.put("message", message);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}    
    
}

