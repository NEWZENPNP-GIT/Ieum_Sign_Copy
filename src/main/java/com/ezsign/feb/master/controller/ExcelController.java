package com.ezsign.feb.master.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.service.ExcelService;
import com.ezsign.feb.master.vo.ExcelDataRequest;
import com.ezsign.feb.master.vo.ExcelMappingResponse;
import com.ezsign.feb.master.vo.ExcelMappingVO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YP000VO;
import com.ezsign.feb.special.vo.YE404VO;
import com.ezsign.feb.special.vo.YE405VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.feb.worker.vo.YE013VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.vo.DefaultResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Api(tags = "ExcelController", description = "엑셀 업로드 관리")
@RequestMapping("/febmaster/")
public class ExcelController extends BaseController {

	private Logger logger = Logger.getLogger(this.getClass());
	
    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "excelService")
    private ExcelService excelService;

    @ApiOperation(value = "엑셀 컬럼 매핑 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "getExcelMappingPost.do")
    @ResponseBody
    public ResponseEntity<ExcelMappingResponse> getExcelMappingPost(
            @RequestParam String excelType,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        ExcelMappingResponse response = new ExcelMappingResponse();

        try {
        	logger.debug(":::::::::::::::::::: getExcelMapping :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                ExcelMappingVO excelMappingVO = new ExcelMappingVO();
                excelMappingVO.setBizId(loginVO.getBizId());
                excelMappingVO.setExcelType(excelType);

                response.excelMappings = excelService.getExcelMappingList(excelMappingVO);

                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logger.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
    
    @ApiOperation(value = "엑셀 컬럼 매핑 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getExcelMapping.do")
    @ResponseBody
    public ResponseEntity<ExcelMappingResponse> getExcelMapping(
            @RequestParam String excelType,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        ExcelMappingResponse response = new ExcelMappingResponse();

        try {
        	logger.debug(":::::::::::::::::::: getExcelMapping :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                ExcelMappingVO excelMappingVO = new ExcelMappingVO();
                excelMappingVO.setBizId(loginVO.getBizId());
                excelMappingVO.setExcelType(excelType);

                response.excelMappings = excelService.getExcelMappingList(excelMappingVO);

                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "엑셀 컬럼 매핑 수정", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "insExcelMapping.do")
    @ResponseBody
    public ResponseEntity<ExcelMappingResponse> insExcelMapping(
            @RequestParam String excelType, @RequestParam int columnId, @RequestParam int mappingOrder,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        ExcelMappingResponse response = new ExcelMappingResponse();

        try {
        	logger.debug(":::::::::::::::::::: insExcelMapping :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                ExcelMappingVO excelMappingVO = new ExcelMappingVO();
                excelMappingVO.setBizId(loginVO.getBizId());
                excelMappingVO.setExcelType(excelType);
                excelMappingVO.setColumnId(columnId);
                excelMappingVO.setMappingOrder(mappingOrder);

                response.excelMappings = excelService.insExcelMapping(excelMappingVO);

                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = "엑셀 컬럼 매핑정보 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "saveExcelMapping.do")
    @ResponseBody
    public ResponseEntity<ExcelMappingResponse> saveExcelMapping(
    								@RequestParam Map<String, Object> paramMap,
    								//@RequestBody Map<String, Object> paramMap,
    								HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        ExcelMappingResponse response = new ExcelMappingResponse();

        try {
        	logger.debug(":::::::::::::::::::: insExcelMapping :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            
            if (loginVO == null) {
            	status = HttpStatus.UNAUTHORIZED;
            } else {
            	String excelType = (String)paramMap.get("excelType");
            	String mappingData = (String) paramMap.get("mappingData");

            	Map<String, String> classMap = new HashMap<String, String>();                                                          

                JSONObject jsonObject = JSONObject.fromObject(mappingData);               
                Map<String, String> mapData = (Map<String, String>)JSONObject.toBean(jsonObject, java.util.HashMap.class, classMap);

            	ExcelMappingVO excelMappingVO = new ExcelMappingVO();
            	excelMappingVO.setBizId(loginVO.getBizId());
            	excelMappingVO.setExcelType(excelType);
            	
            	for( Map.Entry<String,String> entry : mapData.entrySet() ) {
                    String mappingOrder = entry.getKey();
                    String columnId = entry.getValue();
                    
                    excelMappingVO.setColumnId(StringUtil.strPaserInt(columnId));
                	excelMappingVO.setMappingOrder(StringUtil.strPaserInt(mappingOrder));
                	
                	 if(excelService.selectExcelMappingCnt(excelMappingVO) == 0){
                     	excelService.insertExcelMapping(excelMappingVO);
                     }else{
                     	excelService.updateExcelMapping(excelMappingVO);
                     }
            	}

            	response.success = true;
            }

        } catch (Exception ex) {
        	response.success = false;
        	
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logger.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    /**
     *
     * 부서 엑셀 파일 정보 등록 
     *
     * @param body
     * @param request
     * @return
     */
    @ApiOperation(value = "부서 엑셀 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "saveExcelType1.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> saveExcelType1(
            @RequestBody ExcelDataRequest<YS031VO> body,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
        	logger.debug(":::::::::::::::::::: saveExcelType1 (YS031) :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                
                Map<String,String> resultMap = excelService.saveExcelType1(body);
        		
        		if(StringUtils.equals(resultMap.get("result"), "1")){
        			response.success = true;
        			response.message = resultMap.get("message");
        		}else{
        			response.success = false;
        			response.message = resultMap.get("message");
        		}
        		
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));

            response.message = ex.getCause().getLocalizedMessage();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    /**
     *
     * 연말정산 > 기초자료설정 > 근로자정보    엑셀파일 등록 
     *
     * @param body
     * @param request
     * @return
     */
    @ApiOperation(value = "근로자 엑셀 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "saveExcelType2.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> saveExcelType2(
            @RequestBody ExcelDataRequest<YE000VO> body,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
        	logger.debug(":::::::::::::::::::: saveExcelType2 (YE000) :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {

        		Map<String,String> resultMap = excelService.saveExcelType2(loginVO.getBizId(), body);
        		
        		if(StringUtils.equals(resultMap.get("result"), "1")){
        			response.success = true;
        			response.message = resultMap.get("message");
        		}else{
        			response.success = false;
        			response.message = resultMap.get("message");
        		}
            }
            
            
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        	
            logService.error(ex.getMessage(), new Throwable(ex));
            
            response.success = false;            
            response.message = ex.getCause().getLocalizedMessage();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    /**
     *
     * 연말정산 > 기초자료설정 > 부양가족부양가족  엑셀파일 등록
     *
     * @param body
     * @param request
     * @return
     */
    @ApiOperation(value = "부양가족 엑셀 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "saveExcelType3.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> saveExcelType3(
            @RequestBody ExcelDataRequest<YE001VO> body,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
        	logger.debug(":::::::::::::::::::: saveExcelType3 (YE001) :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	
            	Map<String,String> resultMap = excelService.saveExcelType3(loginVO.getBizId(), body);
        		
        		if(StringUtils.equals(resultMap.get("result"), "1")){
        			response.success = true;
        			response.message = resultMap.get("message");
        		}else{
        			response.success = false;
        			response.message = resultMap.get("message");
        		}
            }
            
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));

            response.message = ex.getCause().getLocalizedMessage();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    /**
     *
     * 연말정산 > 기초자료설정 > 사내기부금 엑셀등록
     *
     * @param body
     * @param request
     * @return
     */
    @ApiOperation(value = "사내기부금 엑셀 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "saveExcelType4.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> saveExcelType4(
            @RequestBody ExcelDataRequest<YE404VO> body,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
        	logger.debug(":::::::::::::::::::: saveExcelType4 (YE404) :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {     
            	
            	Map<String,String> resultMap = excelService.saveExcelType4(loginVO.getBizId(), loginVO.getUserId(), body);
        		
        		if(StringUtils.equals(resultMap.get("result"), "1")){
        			response.success = true;
        			response.message = resultMap.get("message");
        		}else{
        			response.success = false;
        			response.message = resultMap.get("message");
        		}
        		
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));

            response.message = ex.getCause().getLocalizedMessage();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    /**
     *
     * 연말정산 > 기초자료설정 > 이월기부금 엑셀등록
     *
     * @param body
     * @param request
     * @return
     */
    @ApiOperation(value = "이월기부금 엑셀 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "saveExcelType5.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> saveExcelType5(
            @RequestBody ExcelDataRequest<YE405VO> body,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
        	logger.debug(":::::::::::::::::::: saveExcelType5 (YE405) :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	
            	Map<String,String> resultMap = excelService.saveExcelType5(loginVO.getBizId(), loginVO.getUserId(), body);
        		
        		if(StringUtils.equals(resultMap.get("result"), "1")){
        			response.success = true;
        			response.message = resultMap.get("message");
        		}else{
        			response.success = false;
        			response.message = resultMap.get("message");
        		}
            }
            
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));

            response.message = ex.getCause().getLocalizedMessage();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    /**
     *
     * 연말정산 > 기초자료설정 > 공제불가회사지원금 엑셀등록
     *
     * @param body
     * @param request
     * @return
     */
    @ApiOperation(value = "공제불가 회사지원금 엑셀 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "saveExcelType6.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> saveExcelType6(
            @RequestBody ExcelDataRequest<YE013VO> body,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
        	logger.debug(":::::::::::::::::::: saveExcelType6 (YE013) :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	
            	Map<String,String> resultMap = excelService.saveExcelType6(loginVO, body);
        		
        		if(StringUtils.equals(resultMap.get("result"), "1")){
        			response.success = true;
        			response.message = resultMap.get("message");
        		}else{
        			response.success = false;
        			response.message = resultMap.get("message");
        		}

            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));

            response.message = ex.getCause().getLocalizedMessage();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    /**
     *
     *  연말정산 > 기초자료설정 > 종ㆍ전 근무지  엑셀파일 등록
     *
     * @param body
     * @param request
     * @return
     */
    @ApiOperation(value = "종(전)근무지 엑셀 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "saveExcelType7.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> saveExcelType7(
            @RequestBody ExcelDataRequest<YE000VO> body,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
        	logger.debug(":::::::::::::::::::: saveExcelType7 (YE000) :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                
                Map<String,String> resultMap = excelService.saveExcelType7(loginVO.getBizId(), body);
        		
        		if(StringUtils.equals(resultMap.get("result"), "1")){
        			response.success = true;
        			response.message = resultMap.get("message");
        		}else{
        			response.success = false;
        			response.message = resultMap.get("message");
        		}
        		
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));

            response.message = ex.getCause().getLocalizedMessage();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }


    @ApiOperation(value = "엑셀 업로드", produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA)
    @RequestMapping(method = RequestMethod.POST, value = "uploadExcel.do")
    @ResponseBody
    public ResponseEntity<JSONObject> uploadExcel(
            @RequestParam MultipartFile file,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        JSONObject jsonObject = new JSONObject();
        boolean result = false;

        File excel = null;

        try {
        	logger.debug(":::::: uploadExcel :::::: ");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                // 파일생성
                String szSavePath = MultipartFileUtil.getSystemTempPath() + "excel";
                List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);

                if (resultFileList.size() != 1) {
                    jsonObject.put("message", "Excel File Count : " + resultFileList.size());
                    result = false;
                } else {
                    // 전달받은 파일리스트
                    FileVO fileVO = resultFileList.get(0);
                    String xlsPath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();

                    logger.debug("# fileVO : " + fileVO);
                    excel = new File(xlsPath);
                    if (!excel.exists()) {
                        jsonObject.put("message", "파일이 존재하지 않습니다.");
                        result = false;
                    } else {
                        Workbook workbook = WorkbookFactory.create(excel);
                        result = parseExcel(workbook, jsonObject);

                        if (!result) {
                            jsonObject.put("message", "Excel 형식이 맞지 않습니다.");
                        }

                        excel.delete();
                    }
                }
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));

            jsonObject.put("message", ex.getLocalizedMessage());

            if (excel != null) {
                excel.delete();
            }
        }

        jsonObject.put("success", result);

        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }

    private boolean parseExcel(Workbook workbook, JSONObject jsonObject) {
        
    	if (workbook.getNumberOfSheets() == 0) {
            return false;
        }

        Sheet sheet = workbook.getSheetAt(0);
        
        if(sheet != null){
	        logger.debug("# sheet name: " + sheet.getSheetName());
	
	        int rowNum = sheet.getPhysicalNumberOfRows();
	        logger.debug("# row length: " + rowNum);
	
	        if (rowNum < 3) {	        	
	            return false;	            
	        }else{
	
		        int colNum = sheet.getRow(2).getPhysicalNumberOfCells();
		        logger.debug("# cell length: " + colNum);
		
		        JSONArray headers = new JSONArray();
		        List<String> headerNames = new ArrayList<>();
		        for (int c = 0; c < colNum; c++) {
		            String headerName = sheet.getRow(2).getCell(c) == null ? "" : sheet.getRow(2).getCell(c).toString();
		            logger.debug("# header name : " + c + ": " + headerName);
		            headerNames.add(headerName);
		
		            JSONObject header = new JSONObject();
		            header.put("name", headerName);
		            headers.add(header);
		        }
		
		        jsonObject.put("headers", headers);
		        DataFormatter dataFormatter = new DataFormatter();
		        JSONArray rows = new JSONArray();
		        rowloop:
		        for (int r = 3; r < rowNum; r++) {
		            JSONObject row = new JSONObject();
		//            for (int c = 0; c < colNum && c < sheet.getRow(r).getPhysicalNumberOfCells(); c++) {
		            for (int c = 0; c < colNum ; c++) {
		
		                String value;
		//                if(c == 0){
		//                	if(org.apache.commons.lang3.StringUtils.isBlank(dataFormatter.formatCellValue(sheet.getRow(r).getCell(c)))){
		//                		break rowloop;
		//                	}
		//                }
		                if (sheet.getRow(r) == null || sheet.getRow(r).getCell(c) == null) {
		                    value = "";
		                } else if (sheet.getRow(r).getCell(c).getCellTypeEnum() == CellType.NUMERIC) {
		                    value = String.valueOf((int) Double.parseDouble(sheet.getRow(r).getCell(c).toString()));
		                } else {
		                    value = sheet.getRow(r).getCell(c).toString();
		                }
		                
//		                if(sheet.getRow(r).getCell(c) != null){
//			                logger.debug(" # sheet.getRow(r).getCell(c) : " + sheet.getRow(r).getCell(c) );
//			                logger.debug(" # sheet.getRow(r).getCell(c).getCellTypeEnum() : " + sheet.getRow(r).getCell(c).getCellTypeEnum() );
//			                logger.debug(" # headerNames.get(c) : " + headerNames.get(c) + " / value  : " + value);
//		                }
		                
		                row.put(headerNames.get(c), value);
		            }
		            
		            logger.debug("# Row Info  ==> " + row);
		            rows.add(row);
		        }
		
		        
		        //엑셀 null인값을 제외시킨다.
		        JSONArray resultArray = new JSONArray();
		        if(rows != null){
		        
		        	for(int i = 0 ; i < rows.size() ; i++){
		        		
		        		int nullChk = 0;
		        		JSONObject obj  = (JSONObject)rows.get(i);
		        	
		        		if(obj != null){
			        		
		        			Iterator keyList = obj.keys(); 	
			        		while(keyList.hasNext()){
			        			
			        			String key = keyList.next().toString(); 
			        			String value = obj.getString(key);	
			        			
			        			if(StringUtils.isNotEmpty(value)){
			        				nullChk++;
			        			}
			        		}
			        		
			        		if(nullChk > 0){
			        			resultArray.add(obj);
			        		}
		        		}		        		
		        	}
		        	
		        }
		        
		        
		        jsonObject.put("rows", resultArray);
	
	        }
        }
        
        return true;
    }
    
    /**
    *
    * 연말정산 > 기초자료설정 > 근로자정보  > 원천징수영수증 관리번호  엑셀파일 등록 
    *
    * @param body
    * @param request
    * @return
    */
   @ApiOperation(value = "원천징수영수증 관리번호 엑셀 등록", produces = "application/json")
   @RequestMapping(method = RequestMethod.POST, value = "saveExcelType8.do")
   @ResponseBody
   public ResponseEntity<DefaultResponse> saveExcelType8(
           @RequestBody ExcelDataRequest<YE000VO> body,
           HttpServletRequest request) {
       HttpHeaders responseHeaders = new HttpHeaders();
       responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

       HttpStatus status = HttpStatus.OK;

       DefaultResponse response = new DefaultResponse();

       try {
       	logger.debug(":::::::::::::::::::: saveExcelType8 (YE000) :::::::::::::::::::");
           SessionVO loginVO = SessionUtil.getSession(request);

           if (loginVO == null) {
               status = HttpStatus.UNAUTHORIZED;
           } else {

       		Map<String,String> resultMap = excelService.saveExcelType8(loginVO.getBizId(), body);
       		
       		if(StringUtils.equals(resultMap.get("result"), "1")){
       			response.success = true;
       			response.message = resultMap.get("message");
       		}else{
       			response.success = false;
       			response.message = resultMap.get("message");
       		}
           }
           
           
       } catch (Exception ex) {
           status = HttpStatus.INTERNAL_SERVER_ERROR;
       	
           logger.error(ex.getMessage(), ex );
           
           response.success = false;            
           response.message = ex.getCause().getLocalizedMessage();
       }

       return new ResponseEntity<>(response, responseHeaders, status);
   }
   
   
   
   /**
   *
   * 간이지급명세서 > 근로자정보    엑셀파일 등록 
   *
   * @param body
   * @param request
   * @return
   */
   @ApiOperation(value = "간이지급명세서 근로자 엑셀 등록", produces = "application/json")
   @RequestMapping(method = RequestMethod.POST, value = "saveExcelType9.do")
   @ResponseBody
   public ResponseEntity<JSONObject> saveExcelType9(
		   			@RequestBody ExcelDataRequest<YP000VO> body,
		   			HttpServletRequest request) {

	   JSONObject jsonObject = new JSONObject();
	   
	   HttpHeaders responseHeaders = new HttpHeaders();
	   responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

	   HttpStatus status = HttpStatus.OK;

	   try {
		   logger.debug(":::::::::::::::::::: saveExcelType9 (YP000) :::::::::::::::::::");
		   SessionVO loginVO = SessionUtil.getSession(request);

		   if (loginVO == null) {
			   status = HttpStatus.UNAUTHORIZED;
		   } else {

			   logger.debug("# body.get계약ID() : " + body.get계약ID() );
			   logger.debug("# body.get근무시기() : " + body.get근무시기() );
			   
			   Map<String,Object> resultMap = excelService.saveExcelType9(loginVO.getBizId(), body);
      		
			   if(StringUtils.equals((String)resultMap.get("result"), "1")){
				   jsonObject.put("success", true);
				   jsonObject.put("message", resultMap.get("message"));
				   jsonObject.put("empList", resultMap.get("empList"));	       		   
			   }else{
				   jsonObject.put("success", false);
	       		   jsonObject.put("message", resultMap.get("message"));
	       		   jsonObject.put("empList", "");
			   }
          }

	   } catch (Exception ex) {
		   status = HttpStatus.INTERNAL_SERVER_ERROR;
      	
		   logger.error(ex.getMessage(), ex );
          
		   jsonObject.put("success", false);
   		   jsonObject.put("message", ex.getCause().getLocalizedMessage());
   		   jsonObject.put("empList", "");
	   }

	   return new ResponseEntity<>(jsonObject, responseHeaders, status);
   }
  
  
}
