package com.ezsign.status.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.ezsign.status.dao.StatusDAO;
import com.ezsign.status.service.StatusService;
import com.ezsign.status.vo.StatusVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;


@Service("statusService")
public class StatusServiceImpl extends AbstractServiceImpl implements StatusService {

	Logger logger = Logger.getLogger(this.getClass());
	
	private static int _oneByte = 1024;
	
	@Resource(name="statusDAO")
	private StatusDAO statusDAO;
	
	@Override
	public int downloadStatusExcel(String filePath, String fileName, StatusVO vo) throws Exception {

		String excelFile = filePath + fileName;
		
		int result = 0;
		
		/*
		 * 
		 * 기초정보
		 * 
		 */
		
		SXSSFWorkbook wb = new SXSSFWorkbook(100);		
		SXSSFSheet sheet = wb.createSheet("기초정보");		
		if(sheet==null) {
			return -1;
		}	

		sheet.setColumnWidth(0, 3 * _oneByte);
		sheet.setColumnWidth(1, 10 * _oneByte);
		sheet.setColumnWidth(2, 3 * _oneByte);
		
		//테두리 스타일
		CellStyle headStyle = wb.createCellStyle();
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);
		
		// 엑셀 헤더 생성
		Row rowHeader2 = sheet.createRow(2);
		Cell cell = rowHeader2.createCell(1);
		cell.setCellValue("조회기간 : "+vo.getSearchStartDate()+" ~ "+vo.getSearchEndDate());
		cell.setCellStyle(headStyle);

		int bizCnt = statusDAO.getBizListCount(vo);
		Row rowHeader4 = sheet.createRow(4);
		cell = rowHeader4.createCell(1);
		cell.setCellValue("신규가입 기업");
		cell.setCellStyle(headStyle);
		
		cell = rowHeader4.createCell(2);
		cell.setCellValue(bizCnt); //신규기업 가입 수
		cell.setCellStyle(headStyle);

		int userCnt = statusDAO.getUserListCount(vo);
		Row rowHeader5 = sheet.createRow(5);
		cell = rowHeader5.createCell(1);
		cell.setCellValue("신규가입 근로자");
		cell.setCellStyle(headStyle);
		
		cell = rowHeader5.createCell(2);
		cell.setCellValue(userCnt); //신규근로자 가입 수
		cell.setCellStyle(headStyle);

		Row rowHeader7 = sheet.createRow(7);
		cell = rowHeader7.createCell(1);
		cell.setCellValue("신규가입 기업");
		cell.setCellStyle(headStyle);
		
		List<StatusVO> bizList = statusDAO.getBizList(vo);
		int i=8;
		Row rowHeader = null;
		for(StatusVO bizVO : bizList){
			rowHeader = sheet.createRow(i);
			cell = rowHeader.createCell(1);
			cell.setCellValue(bizVO.getBizName());
			cell.setCellStyle(headStyle);
			i++;
		}

		/*
		 * 
		 * 전송완료문서
		 * 
		 */
		

		SXSSFSheet sheet1 = wb.createSheet("근로계약서 전송 완료(양식변환)");		
		if(sheet1==null) {
			return -1;
		}	

		sheet1.setColumnWidth(0, 3 * _oneByte);
		sheet1.setColumnWidth(1, 10 * _oneByte);
		
		// 엑셀 헤더 생성
		rowHeader2 = sheet1.createRow(2);
		cell = rowHeader2.createCell(1);
		cell.setCellValue("기업명");
		cell.setCellStyle(headStyle);
		
		cell = rowHeader2.createCell(2);
		cell.setCellValue("건수");
		cell.setCellStyle(headStyle);

		List<StatusVO> contractSendList = statusDAO.getContractSendList(vo);

		i=3;
		int sum = 0;
		for(StatusVO bizVO : contractSendList){
			rowHeader = sheet1.createRow(i);
			cell = rowHeader.createCell(1);
			cell.setCellValue(bizVO.getBizName());
			cell.setCellStyle(headStyle);
			
			cell = rowHeader.createCell(2);
			cell.setCellValue(bizVO.getCnt());
			cell.setCellStyle(headStyle);
			
			sum+=Integer.parseInt(bizVO.getCnt());
			i++;
		}
		rowHeader = sheet1.createRow(i);
		cell = rowHeader.createCell(1);
		cell.setCellValue("합계");
		cell.setCellStyle(headStyle);
		
		cell = rowHeader.createCell(2);
		cell.setCellValue(sum);
		cell.setCellStyle(headStyle);
		
		////////////////////////////////////////////////////////////////////////////////////
		
		SXSSFSheet sheet2 = wb.createSheet("근로계약서 전송 완료(직접입력)");		
		if(sheet2==null) {
			return -1;
		}	

		sheet2.setColumnWidth(0, 3 * _oneByte);
		sheet2.setColumnWidth(1, 10 * _oneByte);
		
		// 엑셀 헤더 생성
		rowHeader2 = sheet2.createRow(2);
		cell = rowHeader2.createCell(1);
		cell.setCellValue("기업명");
		cell.setCellStyle(headStyle);
		
		cell = rowHeader2.createCell(2);
		cell.setCellValue("건수");
		cell.setCellStyle(headStyle);

		List<StatusVO> documentSendList = statusDAO.getDocumentSendList(vo);

		i=3;
		sum = 0;
		for(StatusVO bizVO : documentSendList){
			rowHeader = sheet2.createRow(i);
			cell = rowHeader.createCell(1);
			cell.setCellValue(bizVO.getBizName());
			cell.setCellStyle(headStyle);
			
			cell = rowHeader.createCell(2);
			cell.setCellValue(bizVO.getCnt());
			cell.setCellStyle(headStyle);
			
			sum+=Integer.parseInt(bizVO.getCnt());
			i++;
		}
		rowHeader = sheet2.createRow(i);
		cell = rowHeader.createCell(1);
		cell.setCellValue("합계");
		cell.setCellStyle(headStyle);
		
		cell = rowHeader.createCell(2);
		cell.setCellValue(sum);
		cell.setCellStyle(headStyle);

		
		/*
		 * 
		 * 모든문서
		 * 
		 */

		SXSSFSheet sheet3 = wb.createSheet("근로계약서 체결 현황(모든문서)");		
		if(sheet3==null) {
			return -1;
		}	

		sheet3.setColumnWidth(0, 3 * _oneByte);
		sheet3.setColumnWidth(1, 10 * _oneByte);
		
		// 엑셀 헤더 생성
		rowHeader2 = sheet3.createRow(2);
		cell = rowHeader2.createCell(1);
		cell.setCellValue("기업명");
		cell.setCellStyle(headStyle);
		
		cell = rowHeader2.createCell(2);
		cell.setCellValue("건수");
		cell.setCellStyle(headStyle);

		contractSendList = statusDAO.getContractList(vo);

		i=3;
		sum = 0;
		for(StatusVO bizVO : contractSendList){
			rowHeader = sheet3.createRow(i);
			cell = rowHeader.createCell(1);
			cell.setCellValue(bizVO.getBizName());
			cell.setCellStyle(headStyle);
			
			cell = rowHeader.createCell(2);
			cell.setCellValue(bizVO.getCnt());
			cell.setCellStyle(headStyle);
			
			sum+=Integer.parseInt(bizVO.getCnt());
			i++;
		}
		rowHeader = sheet3.createRow(i);
		cell = rowHeader.createCell(1);
		cell.setCellValue("합계");
		cell.setCellStyle(headStyle);
		
		cell = rowHeader.createCell(2);
		cell.setCellValue(sum);
		cell.setCellStyle(headStyle);
		
		// excel파일생성
		try {
			FileOutputStream file = new FileOutputStream(excelFile);
			wb.write(file);
			file.close();
			wb.dispose();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		// 사용자 기록
		/*UserLogVO userLogVO = new UserLogVO();
		userLogVO.setUserId(loginVO.getUserId());
		userLogVO.setLogType("204");
		userDAO.insUserLog(userLogVO);
		*/
		return 0;
	}
	
}
