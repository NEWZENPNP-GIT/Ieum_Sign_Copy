package com.ezsign.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class FileUtil {
	
	private static Logger logger = Logger.getLogger(FileUtil.class);
	private static long ONE_DAY = 24*60*60*1000;
	private static int FILE_DELETE_DAYS = Integer.parseInt(System.getProperty("file.delete.days"));
	
	public static boolean setDirectory(String directory) {
		File wantedDirectory = new File(directory);
		if (wantedDirectory.isDirectory())
			return true;
	    
		return wantedDirectory.mkdirs();
	}
	
	public static String getFileName(String file)
	{
		String filename = "";
	
		if (file != null)
		{
	    	int sepIndex = file.lastIndexOf(".");
	        if (sepIndex > 0 )
	        	filename = file.substring(0,sepIndex);
	    }
	    return filename;
	}
	
	/**
	 * Description  : 파일 확장자 구하기
	 * @Method Name : getFileExt
	 * @param fileName : 파일명
	 * @return 확장자
	 */
	public static String getFileExt(String fileName) {
		int lastIndexOf = fileName.lastIndexOf('.');
		String ext = "";
		
		if (lastIndexOf > -1) {
			ext = fileName.substring(lastIndexOf + 1);
		}
		
		return ext;
	}
	public static String getRealName(String fileName) {
		File f = new File(fileName);
		
		return f.getName();
	}
	public static long getFileSize(String fileName) {
		long nFileSize = 0;
		File f = new File(fileName);
		
		nFileSize = f.length();
		
		return nFileSize;
	}
	public static String getFileDir(String filePath)
	{
		String dirName = "";

		if (filePath != null)
		{
			int sepIndex = filePath.lastIndexOf("/");
			if (sepIndex > 0 )
				dirName = filePath.substring(0,sepIndex);
		}
		return dirName;
	}

	public static void createFile(String path, String filename, String msg){
	    OutputStream out=null; 
	    try { 
	      setDirectory(path);
	      out = new FileOutputStream(path+"/"+filename, false); 
	      out.write(msg.getBytes());
		  out.close(); 
	    } catch(Exception ex) { 
	    	logger.error(ex.getMessage(), ex); 
	    }finally{ 
	    	if(out != null){
	    		try{
	    		   out.close();
	    		   out = null;
	    		}catch(Exception e){ 
	    		}
	    	}
	    }
	}

	public static void createDirectory(String path){
      setDirectory(path);
	}
	
	public static boolean deleteFile(String path) {
		boolean result=false;
        File file = new File(path);
        
        if( file.exists() ){
            if(file.delete()){
            	result=true;
            }
        }else{
        	logger.info("[ " +path+ " ] 파일이 존재하지 않습니다.");
        }
        return result;
	}
	
	public static void deleteAllFiles(String path, boolean dirDelete){
		try{
			File tempDir = new File(path);
			if (!tempDir.exists()) {
				tempDir.mkdirs();
				System.out.println("tempPath Directory Created. => " +path);			
			} else {
				File[] tempFile = tempDir.listFiles();
				if(tempFile.length >0){ 
					for (int i = 0; i < tempFile.length; i++) {
						if(tempFile[i].isFile()){				
							tempFile[i].delete();
						}else{
							//재귀함수
							deleteAllFiles(tempFile[i].getPath(), dirDelete);
						}
						// 폴더삭제
						if(dirDelete) tempFile[i].delete();
					}
				}
				// 폴더삭제
				if(dirDelete) tempDir.delete();
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
	
	//date 기준 days 지난 파일 제거
	public static void deleteAllFilesBeforeDate(String path, boolean dirDelete, long date){
		try{
			File tempDir = new File(path);
			if (!tempDir.exists()) {
				tempDir.mkdirs();
				System.out.println("tempPath Directory Created. => " +path);			
			} else {
				File[] tempFile = tempDir.listFiles();
				if(tempFile.length >0){ 
					for (int i = 0; i < tempFile.length; i++) {
						if(tempFile[i].isFile()){	
							//파일의 최종 수정 시간
							Date fileDate = new Date(tempFile[i].lastModified());
							Calendar fileCal = Calendar.getInstance();
							fileCal.setTime(fileDate);
							long diffDate = date - fileCal.getTimeInMillis();
							int diffDay = (int) Math.ceil((float)diffDate/(float)ONE_DAY);
							//삭제 기준일과 비교하여 삭제
							if(diffDay > FILE_DELETE_DAYS){
								tempFile[i].delete();
							}
						} else {
							//재귀함수
							deleteAllFilesBeforeDate(tempFile[i].getPath(), dirDelete, date);
						}
					}
				}
			}
			if(dirDelete) {
				if(tempDir.list().length < 1){
					tempDir.delete();
				}
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
	
	public static boolean fileCopy(String sourcePath, String targetPath){
		File sourceFile = new File(sourcePath);
		
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel inFileChannel = null;
		FileChannel outFileChannel = null;
		
		try {
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(targetPath);
			
			inFileChannel = inputStream.getChannel();
			outFileChannel = outputStream.getChannel();
			
			long size =  inFileChannel.size();
			inFileChannel.transferTo(0, size, outFileChannel);
			return true;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		} finally {
			try {
				if(outFileChannel != null){				
					outFileChannel.close();
					outFileChannel = null;
				}
				if(inFileChannel != null){				
					inFileChannel.close();
					inFileChannel = null;
				}
				if(outputStream != null){				
					outputStream.close();
					outputStream = null;
				}
				if(inputStream != null){				
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException ioe) {}			
		}
	   return false;
	}

	public static boolean copyDirectoryFile(String source, String target) {
		boolean result = true;
		int bufSize 	= 4096;
		byte buf[] 	= new byte[bufSize];
		
		String delimeter = "/";
		if (source.lastIndexOf("\\") > -1) {
			delimeter = "\\";
		}

		setDirectory(target);

		File sourceFile = new File(source);
		if (sourceFile.isDirectory()) {
			File sourceFileList[] = sourceFile.listFiles();		
			for (int i = 0; i < sourceFileList.length; i++) {
				File sFile 			= sourceFileList[i];
				String newSource	= sFile.getPath();
				String newTarget	= target + delimeter + sFile.getName();

				// �����━
				if (sFile.isDirectory()) {
					copyDirectoryFile(newSource, newTarget);
				}
				// ���
				else if (sFile.isFile()) {
					InputStream in = null;
					OutputStream out = null;

					try {
						in 	= new FileInputStream(sFile);
						out = new FileOutputStream(newTarget, true);
						int read 		= 0;
						
						while ((read = in.read(buf, 0, bufSize)) > 0) {
							out.write(buf, 0, read);
						}
						
						in.close();
						out.close();
					} 
					catch (IOException e) {
						result = false;
					}
					finally {
						try {
							if (in != null) { in.close(); }
							if (out != null) {	out.close(); }
						} catch (IOException e) { 
							result = false;
						} 
					}
				}
			}
		}
		else if (sourceFile.isFile()) {
			InputStream in = null;
			OutputStream out = null;

			try {
				String newTarget	= target + delimeter + sourceFile.getName();
				in 	= new FileInputStream(sourceFile);
				out = new FileOutputStream(newTarget, true);
				int read 		= 0;
				
				while ((read = in.read(buf, 0, bufSize)) > 0) {
					out.write(buf, 0, read);
				}
				
				in.close();
				out.close();
			} 
			catch (IOException e) {
				result = false;
			}
			finally {
				try {
					if (in != null) { in.close(); }
					if (out != null) {	out.close(); }
				} catch (IOException e) { 
					result = false;
				} 
			}
		}

		return result;
	}
	
	/**
	 *
	 * ZIP 압축파일을 해제한다.
	 *
	 * @param source
	 * @param target
	 * @return
	 */
	public static List<File> extractZipFile(String source, String target) {
		List<File> fileList = new ArrayList<File>();
		ZipFile zipFile = null;
		int lastIdx = source.lastIndexOf("/");
		if (lastIdx == -1) {
			lastIdx = source.lastIndexOf("\\");
		}
		
		if (target == null || target.equals("")) {
			target = source.substring(0, lastIdx);
		}
		
		try {
			Charset charset = Charset.forName("EUC-KR");			
			zipFile = new ZipFile(source,charset);
			
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			byte buf[] 		= new byte[4096];
			File file 			= null;
			String fileName		= "";
			
			while (entries.hasMoreElements()) {
				
				ZipEntry entry	= (ZipEntry)entries.nextElement();
				logger.info("# entry.getName() : " + entry.getName() );
				
//				fileName		= StringUtil.encodingCharset(entry.getName(), "8859_1|euc-kr");
				fileName = entry.getName();
				
				file = new File(target, fileName);
			    fileList.add(file);
				file.getParentFile().mkdirs();
				
				if (!entry.isDirectory()) {
					BufferedInputStream inputStream 	= null;
					BufferedOutputStream outputStream 	= null;
					
					try {
						inputStream		= new BufferedInputStream(zipFile.getInputStream(entry));
						outputStream	= new BufferedOutputStream(new FileOutputStream(file));
						int len = 0;
						while ((len = inputStream.read(buf)) > 0) {
							outputStream.write(buf, 0, len);
						}

					} 
					catch (Exception ex) {
						logger.error(ex.getMessage(), ex);
					}
					finally {
						if (outputStream != null) {
							outputStream.flush();
							outputStream.close();
							outputStream = null;
						}
						if (inputStream != null) {
							inputStream.close();
							inputStream = null;
						}
					}
				}
			}
			
			if (zipFile != null) {
				zipFile.close();
			}
		} 
		catch (Exception ex) {
			logger.error("[ " +source+ " ] 파일 압축해제에 실패했습니다.");
			logger.error(ex.getMessage(), ex);
		}
		
		return fileList;
	}

	public static int ZipFile(String source, List<String> files) {
		int result = 0;
		byte[] buf = new byte[4096];
		ZipOutputStream out = null;
		FileInputStream in = null;
		try {
		    out = new ZipOutputStream(new FileOutputStream(source),Charset.forName("EUC-KR"));		    
		    
		    for (int i=0; i<files.size(); i++) {
		    	String filePath = files.get(i);
		    	
		        in = new FileInputStream(filePath);
		        Path p = Paths.get(filePath);		        
		        String fileName = p.getFileName().toString();
		        
		        ZipEntry ze = new ZipEntry(fileName);
		        out.putNextEntry(ze);
			      
		        int len;
		        while ((len = in.read(buf)) > 0) {
		            out.write(buf, 0, len);
		        }
				        
		        out.closeEntry();
		        out.flush();
		        
		        in.close();
		        result++;
		    }
		    
		} catch (IOException ex) {
		    logger.error(ex.getMessage(), ex);
		} finally{
			
			try{
				if(out != null){
					out.close();
					out = null;
				}
				if(in != null){
					in.close();
					in = null;
				}
			}catch(IOException iex){				
			}
		}
		
		return result;
	}

	public static String readFile(String source) {	
		String result = "";
		StringBuffer sb = new StringBuffer();
		FileInputStream fis = null;
		BufferedReader br = null;
		try{
			fis = new FileInputStream(new File(source));
			br = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
			
			while(true){
				String data = br.readLine();
				if(data==null) break;
				sb.append(data);
			}
			result = sb.toString();
		}catch(Exception ex){
			logger.error(ex.getMessage(), ex);
		}finally{
			
			try{
				if(fis != null){
					fis.close();
					fis = null;
				}
				if(br != null){
					br.close();
					br = null;
				}
			}catch(IOException ex){
			}
		}
		
		return result;
	}

	public static boolean writeFile(String path, String data){
	    OutputStream out=null; 
	    boolean result=false;
	    try { 
	      //setDirectory(path);
	      out = new FileOutputStream(path, false); 
	      out.write(data.getBytes());
		  out.flush(); 
		  result=true;
	    } catch(Exception ex) { 
	    	logger.error(ex.getMessage(), ex);	      
	    }finally{
	    	try{
	    		if(out != null){	    	
	    		   out.close();
	    		   out = null;
	    		}
	    	}catch(Exception ex){ 
	    	}
	    }
	    return result;
	}

}
