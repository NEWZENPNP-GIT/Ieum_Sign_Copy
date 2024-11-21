package com.ezsign.framework.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ezsign.framework.vo.FileVO;
import com.jarvis.common.util.DateUtil;
import com.jarvis.common.util.FileUtil;

public class MultipartFileUtil {
	public static final int BUFFER_SIZE = 8192;
    public static final String SEPERATOR = "/";
    
    public static String getSystemPath() {
		return System.getProperty("system.feb.path");
    }
    
    public static String getSystemTempPath() {
		String monthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+DateUtil.getDate()+MultipartFileUtil.SEPERATOR;
		return getSystemPath()+"temp/file"+monthPath;
    }

    public static String getZipFileURL() {
		String monthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+DateUtil.getDate()+MultipartFileUtil.SEPERATOR;
		// 개발서버 파일 포트 서버1개일떄
		// String tempPath = "https://ieumsign.newzenpnp.co.kr/temp/file"+monthPath;
		// 개발서버 파일 포트 서버2개일떄
		//String tempPath = "https://ieumsign.newzenpnp.co.kr:9443/temp/file"+monthPath;

		// 운영서버 파일 포트 서버1개일떄
		// String tempPath = "https://ieumsign.co.kr/temp/file"+monthPath;
		// 운영서버 파일 포트 서버2개일떄
		String tempPath = "https://ieumsign.co.kr:9443/temp/file"+monthPath;

		return tempPath;
    }

    public static String getFilePath(String subPath) {
		return System.getProperty("system.file.path")+subPath;
    }

	public static List<FileVO> getFileAddList(HttpServletRequest request, String directory, boolean addTimeStamp) throws Exception {
		List<FileVO> fileList = new ArrayList<>();
		MultipartHttpServletRequest multiRequest;
		
		try { multiRequest = (MultipartHttpServletRequest) request; }
		catch (ClassCastException ex) { return fileList; }

		// 파일 추출
		Map<String, MultipartFile> files = multiRequest.getFileMap();
		
		File temp = new File(directory);

		if (!temp.exists()) temp.mkdirs();

		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		MultipartFile file;
		String filenameToSave;
		String originalFilename;
		
		while (itr.hasNext()) {
			Entry<String, MultipartFile> itm = itr.next();
			String key = itm.getKey();
			file = itm.getValue();
			
			originalFilename = file.getOriginalFilename();
			
			if (!"".equals(originalFilename)) {

				// 저장되는 파일명
				if (addTimeStamp) filenameToSave = getFilenameToSave(directory, originalFilename);
				else 			  filenameToSave = originalFilename;

				
				// 경로 미 존재 시 생성함.
				File temp1 = new File(directory);
				if (!temp1.exists()) temp1.mkdirs();

				File realFile = new File(directory + SEPERATOR + filenameToSave);

				if (realFile.exists()) realFile.delete();

				// 파일 저장
				file.transferTo(realFile);

				// 파일 정보를 VO에 설정
				FileVO resultVO = new FileVO();
				resultVO.setFileKey(key);
				resultVO.setFileStrePath(directory);
				resultVO.setFileStreNm(filenameToSave);
				resultVO.setFileStreOriNm(originalFilename);
				resultVO.setFileSize(file.getSize());
				resultVO.setFileExt(FileUtil.getFileExt(originalFilename));
				resultVO.setFileParamName(file.getName());
				fileList.add(resultVO);
				
			} else {
				FileVO resultVO = new FileVO();
				resultVO.setFileKey("");
				resultVO.setFileStrePath("");
				resultVO.setFileStreNm("");
				resultVO.setFileStreOriNm("");
				resultVO.setFileSize(0);
				resultVO.setFileExt("");
				resultVO.setFileParamName("");
				fileList.add(resultVO);
			}
		}
		return fileList;
	}
	
	
	/**
	 * 파일 중복 처리
	 */
	public static String getFilenameToSave(String directory, String fileName) {
		String time = DateUtil.dateToString(Calendar.getInstance().getTime(), "yyyyMMddHHmmssSSS");
		String fileNamePart;
		String fileExtPart;
		int pointInx = fileName.lastIndexOf(".");

		// 확장자가 없는 경우
		if (pointInx == -1) fileExtPart = "";
		// 확장자가 있는 경우
		else 				fileExtPart = fileName.substring(pointInx);

		fileNamePart = time;
		File file = new File(directory + SEPERATOR + fileNamePart + "[" + time + "]" + fileExtPart);

		// 파일 중복 시 index 추가
		if (file.exists()) {
			int i = 1;
			// 무한 루핑 방지를 위해 100까지만 카운팅
			while (i <= 100) {
				file = new File(directory + SEPERATOR + fileNamePart + "[" + time + "-" + i + "]" + fileExtPart);
				if (!file.exists()) break;
				i++;
			}
		}
		return file.getName();
	}


	public static String readBOMFile(String source) {	
		String result   = "";
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(new File(source));
		    UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(fis);
		    System.out.println("Detected BOM: " + ubis.getBOM());
		    InputStreamReader isr = new InputStreamReader(ubis);
		    BufferedReader br = new BufferedReader(isr);
			
			while (true) {
				String data = br.readLine();
				if (data == null) break;
				sb.append(data);
			}
			result = sb.toString();
		} catch (Exception e) { e.printStackTrace(); }
		return result;
	}

}
