package com.ezsign.framework.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class XlsDataUtil {



	public static void main(String[] args) {
		
		try {
			readExcel("");
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public static Map<String, List<String>> readExcel(String filePath) throws BiffException, IOException {
	    Map<String, List<String>> map = new HashMap<String, List<String>>(); 
	    Workbook workBook=Workbook.getWorkbook(new File (filePath));
	    String [] sheetNames = workBook.getSheetNames();
	    Sheet sheet=null;
	    for (int sheetNumber =0; sheetNumber < sheetNames.length; sheetNumber++){
	       List<String > fields = new ArrayList<String>();
	       sheet=workBook.getSheet(sheetNames[sheetNumber]);
	       for (int columns=0;columns < sheet.getColumns();columns++){
	           fields.add(sheet.getCell(columns, 0).getContents());                 
	       }
	       map.put(sheetNames[sheetNumber],fields); 
	    }
	    return map;
	}
}
