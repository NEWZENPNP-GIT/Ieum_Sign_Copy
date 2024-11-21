package com.ezsign.feb.easyFeb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ezsign.code.service.CodeService;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.feb.easyFeb.service.YE006Service;
import com.ezsign.feb.easyFeb.vo.YE006VO;
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.service.YS031Service;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

import io.swagger.annotations.Api;
import net.sf.json.JSONObject;

@SuppressWarnings("NonAsciiCharacters")
@Controller
@Api(tags = "EasyYearTaxController", description = "손쉬운 연말정산")
@RequestMapping("/easyFeb/admin/")
public class AdminYE006Controller {

	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "ye006Service")
	private YE006Service ye006Service;
	
	@Resource(name = "ys030Service")
	private YS030Service ys030Service;
	
	@Resource(name = "ys031Service")
	private YS031Service ys031Service;
	
	@Resource(name = "codeService")
	private CodeService codeService;

	@RequestMapping(method = RequestMethod.POST , value = "saveYE006.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveAdminYE006Ctrl(@RequestParam("uploadFile") MultipartFile reapExcelDataFile, 
			@RequestParam(value="사용자ID",  required=true) String 사용자ID,
			@RequestParam(value="계약ID",  required=true) String 계약ID,
			HttpServletRequest request) throws Exception {
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";

		try {
			System.out.println(":::::::::::::::::::: saveAdminYE006Ctrl :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null || Integer.parseInt(loginVO.getUserType()) < 5) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
//				String 사용자ID = loginVO.getUserId();
//				String 계약ID = loginVO.getYearContractId();
				String bizId = loginVO.getBizId();
				
				List<YE006VO> ye006VOList = new ArrayList<YE006VO>();
				
				XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
			    XSSFSheet worksheet = workbook.getSheetAt(0);
				
			    DataFormatter dataFormatter = new DataFormatter();
System.out.println("worksheet.getPhysicalNumberOfRows(): " + worksheet.getPhysicalNumberOfRows());
			    for(int i = 3; i < worksheet.getPhysicalNumberOfRows() ;i++) {
			    	
			    	YE006VO ye006VO = new YE006VO();

			        XSSFRow row = worksheet.getRow(i);

			        System.out.println(계약ID);
			        System.out.println(bizId);
			        System.out.println(dataFormatter.formatCellValue(row.getCell(0)));
			        System.out.println(dataFormatter.formatCellValue(row.getCell(1)));
			        System.out.println(dataFormatter.formatCellValue(row.getCell(2)));
			        System.out.println(dataFormatter.formatCellValue(row.getCell(3)));
			        System.out.println(dataFormatter.formatCellValue(row.getCell(4)));
			        System.out.println(dataFormatter.formatCellValue(row.getCell(5)));
			        System.out.println(dataFormatter.formatCellValue(row.getCell(6)));
			        System.out.println(dataFormatter.formatCellValue(row.getCell(7)));
			        System.out.println(dataFormatter.formatCellValue(row.getCell(8)));
			        System.out.println(dataFormatter.formatCellValue(row.getCell(9)));
			        System.out.println(dataFormatter.formatCellValue(row.getCell(10)));
			        System.out.println(dataFormatter.formatCellValue(row.getCell(11)));
			        System.out.println(dataFormatter.formatCellValue(row.getCell(12)));
			        System.out.println(dataFormatter.formatCellValue(row.getCell(13)));
			        
			        if(StringUtils.isBlank(dataFormatter.formatCellValue(row.getCell(0))) || StringUtils.isBlank(dataFormatter.formatCellValue(row.getCell(1)))){
			        	break;
			        }
			        
			        
			        ye006VO.set계약ID(계약ID);
			        ye006VO.setBizId(bizId);
			        ye006VO.set사번(dataFormatter.formatCellValue(row.getCell(0)));
			        ye006VO.set성명(dataFormatter.formatCellValue(row.getCell(1)));
			        ye006VO.setM1금액(Integer.parseInt(StringUtils.replace(dataFormatter.formatCellValue(row.getCell(2)), ",", "")));
			        ye006VO.setM2금액(Integer.parseInt(StringUtils.replace(dataFormatter.formatCellValue(row.getCell(3)), ",", "")));
			        ye006VO.setM3금액(Integer.parseInt(StringUtils.replace(dataFormatter.formatCellValue(row.getCell(4)), ",", "")));
			        ye006VO.setM4금액(Integer.parseInt(StringUtils.replace(dataFormatter.formatCellValue(row.getCell(5)), ",", "")));
			        ye006VO.setM5금액(Integer.parseInt(StringUtils.replace(dataFormatter.formatCellValue(row.getCell(6)), ",", "")));
			        ye006VO.setM6금액(Integer.parseInt(StringUtils.replace(dataFormatter.formatCellValue(row.getCell(7)), ",", "")));
			        ye006VO.setM7금액(Integer.parseInt(StringUtils.replace(dataFormatter.formatCellValue(row.getCell(8)), ",", "")));
			        ye006VO.setM8금액(Integer.parseInt(StringUtils.replace(dataFormatter.formatCellValue(row.getCell(9)), ",", "")));
			        ye006VO.setM9금액(Integer.parseInt(StringUtils.replace(dataFormatter.formatCellValue(row.getCell(10)), ",", "")));
			        ye006VO.setM10금액(Integer.parseInt(StringUtils.replace(dataFormatter.formatCellValue(row.getCell(11)), ",", "")));
			        ye006VO.setM11금액(Integer.parseInt(StringUtils.replace(dataFormatter.formatCellValue(row.getCell(12)), ",", "")));
			        ye006VO.setM12금액(Integer.parseInt(StringUtils.replace(dataFormatter.formatCellValue(row.getCell(13)), ",", "")));

			        ye006VOList.add(ye006VO);   

			    }
System.out.println("ye006VOList: " + ye006VOList);
			    ye006Service.saveYE006List(ye006VOList);
			    
			    success = true;
			    message = "차량 비과세가 저장 되었습니다.";
			}
		}catch (Exception ex) {
System.out.println(ex.toString());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
		jsonObject.put("message", message);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "getYE006List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE006ListCtrl(@RequestParam(value="계약ID",  required=true) String 계약ID,
			@RequestParam(value="사업장ID",  required=false) String 사업장ID,
			@RequestParam(value="부서ID",  required=false) String 부서ID,
			@RequestParam(value="leaveDate",  required=false) String leaveDate,
			@RequestParam(value="empName",  required=false) String empName,
			@RequestParam(value="empNo",  required=false) String empNo,
			@RequestParam(value="positionName",  required=false) String positionName,
			@RequestParam(value="startPage",  required=false) Integer startPage,
			@RequestParam(value="endPage",  required=false) Integer endPage,
			@RequestParam(value="근무상태",  required=false) String 근무상태,
			@RequestParam(value="증빙추가여부",  required=false) String 증빙추가여부, HttpServletRequest request) throws Exception {
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		int total = 0;

		try {
			System.out.println(":::::::::::::::::::: saveYE006Ctrl :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null || Integer.parseInt(loginVO.getUserType()) < 5) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
//				String 사용자ID = loginVO.getUserId();
//				String 계약ID = loginVO.getYearContractId();
				String bizId = loginVO.getBizId();
				
				Map params = new HashMap();
				
				params.put("계약ID", 계약ID);
				params.put("bizId", bizId);
				params.put("사업장ID", 사업장ID);
				params.put("부서ID", 부서ID);
				params.put("leaveDate", leaveDate);
				params.put("empName", empName);
				params.put("empNo", empNo);
				params.put("positionName", positionName);
				params.put("startPage", startPage );
				params.put("endPage", endPage);
				params.put("근무상태", 근무상태 );
				params.put("증빙추가여부", 증빙추가여부);

				List<YE006VO> ye006VOList = ye006Service.getYE006List(params);
				total = ye006Service.getYE006ListCount(params);
			    
				// 사업장조회
				YS030VO ys030VO = new YS030VO();
				ys030VO.set계약ID(계약ID);
				ys030VO.setStartPage(0);
				ys030VO.setEndPage(99999);
				List<YS030VO> ys030List = ys030Service.getYS030List(ys030VO);
				// 부서조회
				YS031VO ys031VO = new YS031VO();
				ys031VO.set계약ID(계약ID);
				ys031VO.setStartPage(0);
				ys031VO.setEndPage(99999);
				List<YS031VO> ys031List = ys031Service.getYS031List(ys031VO);
				// 근무상태
				CodeVO codeVO = new CodeVO();
				codeVO.setGrpCommCode("420");
				List<CodeVO> code420List = codeService.getCodeList(codeVO);

				jsonObject.put("ys030", ys030List);
				jsonObject.put("ys031", ys031List);
				jsonObject.put("code420", code420List);
				
				jsonObject.put("total", total);
				jsonObject.put("data", ye006VOList);
				
			    success = true;
			}
		}catch (Exception ex) {
System.out.println(ex.toString());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "getYE006ExcelList.do")
    @ResponseBody
    public void getYE006ExcelListCtrl(@RequestParam(value="계약ID",  required=true) String 계약ID,
    		@RequestParam(value="사업장ID",  required=false) String 사업장ID,
			@RequestParam(value="부서ID",  required=false) String 부서ID,
			@RequestParam(value="leaveDate",  required=false) String leaveDate,
			@RequestParam(value="empName",  required=false) String empName,
			@RequestParam(value="empNo",  required=false) String empNo,
			@RequestParam(value="positionName",  required=false) String positionName,
			@RequestParam(value="startPage",  required=false) Integer startPage,
			@RequestParam(value="endPage",  required=false) Integer endPage,
			@RequestParam(value="근무상태",  required=false) String 근무상태,
			@RequestParam(value="증빙추가여부",  required=false) String 증빙추가여부, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String today = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		
		SessionVO loginVO = SessionUtil.getSession(request);
		
		String excelFileName = today + "_차량지원금 현황";
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1");
		response.setHeader("Content-Disposition", "attachment; filename=" +
				new String(excelFileName.getBytes("KSC5601"), "8859_1") + ".xls");
		
		String bizId = loginVO.getBizId();
		
		Map params = new HashMap();
		
		params.put("계약ID", 계약ID);
		params.put("bizId", bizId);
		params.put("사업장ID", 사업장ID);
		params.put("부서ID", 부서ID);
		params.put("leaveDate", leaveDate);
		params.put("empName", empName);
		params.put("empNo", empNo);
		params.put("positionName", positionName);
		params.put("startPage", startPage );
		params.put("endPage", endPage);
		params.put("근무상태", 근무상태 );
		params.put("증빙추가여부", 증빙추가여부);
		params.put("selectCase", "excel");
		
		List<YE006VO> ye006VOList = ye006Service.getYE006List(params);
		
		// 워크북 생성
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 워크시트 생성
        HSSFSheet sheet = workbook.createSheet(today + "_차량지원금 현황");
        // 행 생성
        HSSFRow row = sheet.createRow(0);
        row = sheet.createRow(1);
        row = sheet.createRow(2);
        // 쎌 생성
        HSSFCell cell;

        row.createCell(0).setCellValue("사번");
        row.createCell(1).setCellValue("성명");
		row.createCell(2).setCellValue("지원액합계");
		
		row.createCell(3).setCellValue("1월");
		row.createCell(4).setCellValue("2월");
		row.createCell(5).setCellValue("3월");
		row.createCell(6).setCellValue("4월");
		row.createCell(7).setCellValue("5월");
		row.createCell(8).setCellValue("6월");
		row.createCell(9).setCellValue("7월");
		row.createCell(10).setCellValue("8월");
		row.createCell(11).setCellValue("9월");
		row.createCell(12).setCellValue("10월");
		row.createCell(13).setCellValue("11월");
		row.createCell(14).setCellValue("12월");
		row.createCell(15).setCellValue("비과세합계");
		row.createCell(16).setCellValue("비관세초과액");
		
		if(ye006VOList != null && ye006VOList.size() > 0){
			for(int i = 0; i < ye006VOList.size(); i++) {
				YE006VO ye006VO = ye006VOList.get(i);
				row = sheet.createRow(i + 3);
				row.createCell(0).setCellValue(ye006VO.get사번());
		        row.createCell(1).setCellValue(ye006VO.get성명());
				row.createCell(2).setCellValue(ye006VO.get지원액합계());
				row.createCell(3).setCellValue(ye006VO.getM1금액());
				row.createCell(4).setCellValue(ye006VO.getM2금액());
				row.createCell(5).setCellValue(ye006VO.getM3금액());
				row.createCell(6).setCellValue(ye006VO.getM4금액());
				row.createCell(7).setCellValue(ye006VO.getM5금액());
				row.createCell(8).setCellValue(ye006VO.getM6금액());
				row.createCell(9).setCellValue(ye006VO.getM7금액());
				row.createCell(10).setCellValue(ye006VO.getM8금액());
				row.createCell(11).setCellValue(ye006VO.getM9금액());
				row.createCell(12).setCellValue(ye006VO.getM10금액());
				row.createCell(13).setCellValue(ye006VO.getM11금액());
				row.createCell(14).setCellValue(ye006VO.getM12금액());
				row.createCell(15).setCellValue(ye006VO.get비과세합계());
				row.createCell(16).setCellValue(ye006VO.get비과세초과액());
			}
		}
		workbook.write(response.getOutputStream());
        workbook.close();
	}
}





