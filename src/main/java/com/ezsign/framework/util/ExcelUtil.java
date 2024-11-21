package com.ezsign.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.CellFormat;
import jxl.CellType;
import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class ExcelUtil {

	/**
	 * 
	 */
	public ExcelUtil() {
		super();
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		try {
			ExcelUtil util = new ExcelUtil();
			List resultList = util.readExcel("d:\\하이에어파킹_인사데이터업로드.xls");
			System.out.println(resultList.size());
		} catch(Exception e) {
			
		}
	}

	public static Label createLabel(int x, int y, String name) throws Exception{
		return createLabel(x, y, "center", name);
	}
	
	public static Label createLabel(int x, int y, String align, String name) throws Exception{
	    WritableFont fontFormat = new WritableFont(WritableFont.ARIAL, 
                16,
                WritableFont.BOLD,
                false,
                UnderlineStyle.NO_UNDERLINE,
                Colour.BLACK);
	    WritableCellFormat labelFormat = new WritableCellFormat(fontFormat);
	    labelFormat.setWrap(true);
//	    labelFormat.setBackground(Colour.AQUA);
	    labelFormat.setAlignment(getAlign(align));
	    labelFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

		Label label = new Label(x, y, name,labelFormat);
		
		if(label != null) return label;
		else return new Label(x, y, name);
	}
	
	public static Label createLabel2(int x, int y, String align, String name) throws Exception{
	    WritableFont fontFormat = new WritableFont(WritableFont.ARIAL, 
                10,
                WritableFont.BOLD,
                false,
                UnderlineStyle.NO_UNDERLINE,
                Colour.BLACK);
	    WritableCellFormat labelFormat = new WritableCellFormat(fontFormat);
	    labelFormat.setWrap(true);
//	    labelFormat.setBackground(Colour.AQUA);
	    labelFormat.setAlignment(getAlign(align));
	    labelFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

		Label label = new Label(x, y, name,labelFormat);
		
		if(label != null) return label;
		else return new Label(x, y, name);
	}	

	public static Label createHeader(int x, int y, String name) throws Exception{
		return createHeader(x, y, "center", name);
	}
	
	public static Label createHeader(int x, int y, String align, String name) throws Exception{
	    WritableFont fontFormat = new WritableFont(WritableFont.ARIAL, 
                11,
                WritableFont.BOLD,
                false,
                UnderlineStyle.NO_UNDERLINE,
                Colour.BLACK);
	    WritableCellFormat labelFormat = new WritableCellFormat(fontFormat);
	    labelFormat.setWrap(true);
	    labelFormat.setAlignment(getAlign(align));
	    labelFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
	    labelFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
	    labelFormat.setBackground(Colour.GRAY_25);
		Label label = new Label(x, y, name,labelFormat);
	    	
		if(label != null) return label;
		else return new Label(x, y, name);
	}

	public static  Label createText(int x, int y, String align, String name) throws Exception{
        WritableFont fontFormat = new WritableFont(WritableFont.ARIAL, 
                WritableFont.DEFAULT_POINT_SIZE,
                WritableFont.NO_BOLD,
                false,
                UnderlineStyle.NO_UNDERLINE,
                Colour.BLACK);  
   		WritableCellFormat labelFormat = new WritableCellFormat(fontFormat);
   		labelFormat.setWrap(true);
	    labelFormat.setAlignment(getAlign(align));
	    labelFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
	    labelFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		Label label = new Label(x, y, name,labelFormat);
		
		if(label != null) return label;
		else return new Label(x, y, name);		
	}
	
	public static  Number createNumber(int x, int y, String align, double num) throws Exception{
        WritableFont fontFormat = new WritableFont(WritableFont.ARIAL, 
            WritableFont.DEFAULT_POINT_SIZE,
            WritableFont.NO_BOLD,
            false,
            UnderlineStyle.NO_UNDERLINE,
            Colour.BLACK);  
		WritableCellFormat labelFormat = new WritableCellFormat(fontFormat);
		labelFormat.setWrap(true);
	    labelFormat.setAlignment(getAlign(align));
	    labelFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
	    labelFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
	    Number number = new Number(x, y, num,labelFormat);
		
		if(number != null) return number;
		else return new Number(x, y, num);		
	}
	
	public static  Alignment getAlign(String align){
		if(align.toLowerCase().equals("center")) return Alignment.CENTRE;
		else if(align.toLowerCase().equals("left")) return Alignment.LEFT;
		else if(align.toLowerCase().equals("right")) return Alignment.RIGHT;
		else return Alignment.CENTRE;
	}

	public static void excelInfoSet(HttpServletRequest request, HttpServletResponse response, String fileName){
		response.reset();
		response.setContentType("application/x-msdownload;charset="+request.getCharacterEncoding());
		response.setHeader("Content-Disposition","attachment; filename="+fileName); 
		response.setHeader("Cache-Control", "no-cache");
	}

	public static class HashValue {
		public int sheet;
		public int row;
		public int col;
		public String value;
	}
	
	public static List readExcel(String fileName) {
		List result = new ArrayList();
		try {
			File file = new File(fileName);
			if (!file.exists()) return null;
			
			Workbook workbook =  Workbook.getWorkbook(file);
			
			Sheet[] sheets = workbook.getSheets();
			
			for(int s=0;s<sheets.length;s++) {				
				Sheet sheet = sheets[s];
				int colNum = sheet.getColumns();
				int rowNum = sheet.getRows();
				
				for(int r=0; r < rowNum; r++) {
					for(int c=0;c < colNum; c++) {
						Cell cell = sheet.getCell(c, r);
						HashValue hv = new HashValue();
						
						hv.sheet = s;
						hv.row = r;
						hv.col = c;

						CellType type = cell.getType();
						if (type == CellType.LABEL)
						{
							LabelCell lc = (LabelCell) cell;
							hv.value = lc.getString();
						}						
						else if (type == CellType.NUMBER)
						{
							NumberCell lc = (NumberCell) cell;
							hv.value = Double.toString(lc.getValue());
						}						
						else if (type == CellType.DATE)
						{
							DateCell lc = (DateCell) cell;
							hv.value = DateUtil.dateToString(lc.getDate(), "yyyyMMdd");
						} else {
				    		hv.value =  cell.getContents();
						}
						result.add(hv);
						System.out.println(hv.value);
					}
				}
				
			}
			
			workbook.close();
			
		}catch (Exception e) {
			
		}
		return result;
	}

	public static String getCellValue(Cell cell) {
		String value = "";
		
		HashValue hv = new HashValue();
		
		CellType type = cell.getType();
		
		if (type == CellType.LABEL)
		{
			LabelCell lc = (LabelCell) cell;
			hv.value = lc.getString();
		}
		else if (type == CellType.NUMBER || type == CellType.NUMBER_FORMULA)
		{
			NumberCell lc = (NumberCell) cell;
			hv.value = lc.getNumberFormat().format(lc.getValue());
		}
		else if (type == CellType.DATE)
		{
			DateCell lc = (DateCell) cell;
			hv.value = DateUtil.dateToString(lc.getDate(), "yyyyMMdd");
		} else {
    		hv.value =  cell.getContents();
		}
		value = hv.value;
		//System.out.println("getCellValue=>"+type.toString()+"/"+value);
		
		return value;
	}

	public static String getCellType(Cell cell) {
		String typeStr = "";

		HashValue hv = new HashValue();
		CellType type = cell.getType();

		if (type == CellType.LABEL)
		{
			typeStr = "LABEL";
		}
		else if (type == CellType.NUMBER || type == CellType.NUMBER_FORMULA)
		{
			typeStr = "NUMBER";
		}
		else if (type == CellType.DATE)
		{
			typeStr = "DATE";
		} else {
			typeStr = "STRING";
		}

		return typeStr;
	}
	
}
