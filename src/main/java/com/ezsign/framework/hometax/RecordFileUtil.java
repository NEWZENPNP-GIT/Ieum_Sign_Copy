package com.ezsign.framework.hometax;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;

import com.jarvis.common.util.DateUtil;

public class RecordFileUtil {

	private static Logger logger = Logger.getLogger(RecordFileUtil.class);
	
	/**
	 * 전자문서 파일을 작성한다.
	 * 
	 * @param fileName
	 * @param buffer
	 * @return
	 */
	public static String fileWrite(String fileName, StringBuffer buffer) throws IOException{
		
		String filePath = null;
		
		String systemFilePath = System.getProperty("system.file.path");			
		logger.debug("# systemFilePath : " + systemFilePath );		
		filePath = systemFilePath + File.separator + "FEB" + File.separator + "YE800" + File.separator + "txt" + File.separator + DateUtil.getTimeStamp(3);
		
		File fileSavePath = new File(filePath);		
		if(!fileSavePath.exists()){
			fileSavePath.mkdirs();
		}
		
		
		
		File file = new File(filePath + File.separator + fileName);					
		logger.info("# fileSavePath : " + filePath + File.separator + fileName );
		
//	    FileWriter writer = null;
		BufferedWriter output = null;
	    try {
	         // 기존 파일의 내용에 이어서 쓰려면 true를, 기존 내용을 없애고 새로 쓰려면 false를 지정한다.
//	            writer = new FileWriter(file, false);
//	            writer.write(buffer.toString());
//	            writer.flush();
	            
//	    		output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()),"EUC-KR"));
	    		output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath(),false),"KSC5601"));
	    		output.write(buffer.toString());
	    		output.flush();
	    		
	    }catch(IOException ex){ 
	    	logger.error(ex.getMessage(),ex);
	    	throw ex;
	    }finally{
//	    	if(writer != null){
//	    		writer.close();
//	    		writer = null;
//	    	}
	    	if(output != null){
	    		output.close();
	    		output = null;
	    	}
	    }
		
		return filePath;
	}
}
