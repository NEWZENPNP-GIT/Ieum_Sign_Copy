package com.ezsign.popup.controller;

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
import com.ezsign.contract.service.ContractService;
import com.ezsign.contract.vo.ContractPersonNewVO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.popup.service.ExcelUploadService;
import com.ezsign.popup.vo.ExcelUploadRequest;
import com.ezsign.popup.vo.ExcelUploadResponse;
import com.ezsign.popup.vo.ExcelUploadVO;
import com.ezsign.window.vo.DefaultResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@Api(tags = "ExcelUploadController", description = "엑셀 업로드 관리")
@RequestMapping("/popup/")
public class ExcelUploadController extends BaseController {

	private Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "excelUploadService")
    private ExcelUploadService excelUploadService;

    @Resource(name = "contractService")
    private ContractService contractService;
    
    @ApiOperation(value = "엑셀 컬럼 매핑 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "getExcelMapping.do")
    @ResponseBody
    public ResponseEntity<ExcelUploadResponse> getExcelMapping(
            @RequestParam String excelType,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        ExcelUploadResponse response = new ExcelUploadResponse();

        try {
        	logger.debug(":::::::::::::::::::: getExcelMapping :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                ExcelUploadVO excelUploadVO = new ExcelUploadVO();
                excelUploadVO.setBizId(loginVO.getBizId());
                excelUploadVO.setExcelType(excelType);
                
                response.excelMappings = excelUploadService.getExcelMappingList(excelUploadVO);

                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logger.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "엑셀 컬럼 계약조건 매핑 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "getExcelMappingContract.do")
    @ResponseBody
    public ResponseEntity<ExcelUploadResponse> getExcelMappingContract(
            @RequestParam String excelType, @RequestParam String id,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        ExcelUploadResponse response = new ExcelUploadResponse();

        try {
        	logger.debug(":::::::::::::::::::: getExcelMappingContract :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                ExcelUploadVO excelUploadVO = new ExcelUploadVO();
                excelUploadVO.setBizId(loginVO.getBizId());
                excelUploadVO.setExcelType(excelType);
                excelUploadVO.setId(id);
                response.excelMappings = excelUploadService.getExcelMappingContractList(excelUploadVO);

                ContractPersonNewVO newVO = new ContractPersonNewVO();
                newVO.setBizId(loginVO.getBizId());
                newVO.setFileId(id);
				List<ContractPersonNewVO> resultNewList = contractService.getContractPersonNewList(newVO);
				if(resultNewList.size()>0) {
	                response.sampleExcelFile = resultNewList.get(0).getDataFileId();
				}
				

                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logger.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
    
    @ApiOperation(value = "저장된 컬럼 매핑 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "getExcelMapMasterList.do")
    @ResponseBody
    public ResponseEntity<ExcelUploadResponse> getExcelMapMasterList(
            @RequestParam String excelType,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        ExcelUploadResponse response = new ExcelUploadResponse();

        try {
        	logger.debug(":::::::::::::::::::: getExcelMapMasterList :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                ExcelUploadVO excelUploadVO = new ExcelUploadVO();
                excelUploadVO.setBizId(loginVO.getBizId());
                excelUploadVO.setExcelType(excelType);
                
                response.excelMappings = excelUploadService.getExcelMapMasterList(excelUploadVO);

                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logger.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
    
    @ApiOperation(value = "저장된 컬럼 매핑 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "getExcelMapDetailList.do")
    @ResponseBody
    public ResponseEntity<ExcelUploadResponse> getExcelMapDetailList(
            @RequestParam String mapId,
            @RequestParam String excelType,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        ExcelUploadResponse response = new ExcelUploadResponse();

        try {
        	logger.debug(":::::::::::::::::::: getExcelMapDetailList :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                ExcelUploadVO excelUploadVO = new ExcelUploadVO();
                excelUploadVO.setBizId(loginVO.getBizId());
                excelUploadVO.setMapId(mapId);
                excelUploadVO.setExcelType(excelType);
                
                response.excelMappings = excelUploadService.getExcelMapDetailList(excelUploadVO);

                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logger.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "저장된 컬럼 매핑 삭제", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "delExcelMapping.do")
    @ResponseBody
    public ResponseEntity<ExcelUploadResponse> delExcelMapping(
            @RequestParam String mapId,
            @RequestParam String excelType,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        ExcelUploadResponse response = new ExcelUploadResponse();

        try {
        	logger.debug(":::::::::::::::::::: delExcelMapping :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                ExcelUploadVO excelUploadVO = new ExcelUploadVO();
                excelUploadVO.setBizId(loginVO.getBizId());
                excelUploadVO.setMapId(mapId);
                excelUploadVO.setExcelType(excelType);
                excelUploadService.deleteExcelMap(excelUploadVO);
                
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logger.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = "엑셀 컬럼 매핑정보 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "saveExcelMapping.do")
    @ResponseBody
    public ResponseEntity<ExcelUploadResponse> saveExcelMapping(
    								@RequestParam Map<String, Object> paramMap,
    								//@RequestBody Map<String, Object> paramMap,
    								HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        ExcelUploadResponse response = new ExcelUploadResponse();

        try {
        	logger.debug(":::::::::::::::::::: saveExcelMapping :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            
            if (loginVO == null) {
            	status = HttpStatus.UNAUTHORIZED;
            } else {
            	String fileId = (String)paramMap.get("id");
            	String mapId = (String)paramMap.get("mapId");
            	String mapName = (String)paramMap.get("mapName");
            	String excelType = (String)paramMap.get("excelType");
            	String mappingData = (String) paramMap.get("mappingData");

            	Map<String, String> classMap = new HashMap<String, String>();

                JSONObject jsonObject = JSONObject.fromObject(mappingData);               
                Map<String, String> mapData = (Map<String, String>)JSONObject.toBean(jsonObject, java.util.HashMap.class, classMap);
                
            	ExcelUploadVO excelUploadVO = new ExcelUploadVO();
            	excelUploadVO.setBizId(loginVO.getBizId());
            	excelUploadVO.setMapId(mapId);
            	excelUploadVO.setExcelType(excelType);
            	
            	if(StringUtil.isNotNull(mapId)) {
            		if(excelUploadService.selectExcelMappingCnt(excelUploadVO) > 0){
            			excelUploadService.deleteExcelMap(excelUploadVO);
            		}
                } else {
                	mapId = DateUtil.getTimeStamp(7);                	
                }
            	
            	List<ExcelUploadVO> mapList = new ArrayList<ExcelUploadVO>();
            	for( Map.Entry<String,String> entry : mapData.entrySet() ) {

                    logger.info("#columnId:"+entry.getKey());
                    logger.info("#mappingOrder:"+String.valueOf(entry.getValue()));
                    
                	ExcelUploadVO mapVO = new ExcelUploadVO();
                	mapVO.setBizId(loginVO.getBizId());                	
                	mapVO.setMapId(mapId);
                	mapVO.setMapName(mapName);
                	mapVO.setExcelType(excelType);
                	mapVO.setFileId(fileId);
                    String columnId = entry.getKey();
                    String mappingOrder = String.valueOf(entry.getValue());

                    mapVO.setColumnId(columnId);
                    mapVO.setMappingOrder(StringUtil.strPaserInt(mappingOrder));
                    
                    mapList.add(mapVO);
                	
            	}
            	if(mapList.size()>0)
            		excelUploadService.insertExcelMap(mapList);

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
     * 근로자정보    엑셀파일 등록 
     *
     * @param body
     * @param request
     * @return
     */
    @ApiOperation(value = "근로자 엑셀 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "saveExcelType1.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> saveExcelType1(
            @RequestBody ExcelUploadRequest<EmpVO> body,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
        	logger.debug(":::::::::::::::::::: saveExcelType1 (TBL_EMP) :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {

        		Map<String,String> resultMap = excelUploadService.saveExcelType1(loginVO.getBizId(), body);
        		
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
        	
            logger.error(ex.getMessage(), new Throwable(ex));
            
            response.success = false;            
            response.message = ex.getCause().getLocalizedMessage();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    /**
     *
     * 계약조건  엑셀파일 등록 
     *
     * @param body
     * @param request
     * @return
     */
    @ApiOperation(value = "계약조건 엑셀 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "saveExcelTypeC.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> saveExcelTypeC(
            @RequestBody ExcelUploadRequest<HashMap<String, String>> body,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
        	logger.debug(":::::::::::::::::::: saveExcelTypeC (TBL_CONTRACT_FORM) :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {

        		Map<String,String> resultMap = excelUploadService.saveExcelTypeC(loginVO.getBizId(), body.getId(), body.getData());
        		
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
        	
            logger.error(ex.getMessage(), new Throwable(ex));
            
            response.success = false;            
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
            logger.error(ex.getMessage(), new Throwable(ex));

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

		        int colNumNull = -1;
		        for (int c = 0; c < colNum; c++) {
		            String headerName = sheet.getRow(1).getCell(c) == null ? "" : sheet.getRow(1).getCell(c).toString();
	            	if(StringUtil.isNotNull(headerName)) {
	            		break;
	            	}
	            	colNumNull = c;
		        }
		        
		        JSONArray headers = new JSONArray();
		        List<String> headerNames = new ArrayList<>();
		        for (int c = 0; c < colNum; c++) {
		            String headerName = sheet.getRow(1).getCell(c) == null ? "" : sheet.getRow(1).getCell(c).toString();
		            logger.debug("# header name : " + c + ": " + headerName);
		            headerNames.add(headerName);

	                // 헤드값이 정의되어있지 않는경우 하위값도 제외
	                if(c > colNumNull) {
			    		
			            JSONObject header = new JSONObject();
			            header.put("name", headerName);
			            headers.add(header);
	                }
		        }

	            logger.info("# colNum null  ==> " + colNumNull);
		        
		        JSONArray rows = new JSONArray();
		        
		        for (int r = 3; r < rowNum; r++) {
		            JSONObject row = new JSONObject();
		            for (int c = 0; c < colNum ; c++) {
		
		            	String key = headerNames.get(c);
		                String value;
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
		                // 헤드값이 정의되어있지 않는경우 하위값도 제외
		                if(c > colNumNull) {
			                row.put(key, value);
		                }
		            }
		            
		            logger.debug("# Row Info  ==> " + row);
		            rows.add(row);
		        }
		        
		        jsonObject.put("headers", headers);
		        jsonObject.put("rows", rows);
	
	        }
        }
        
        return true;
    }
    
}
