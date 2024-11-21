package com.ezsign.feb.hometax.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.hometax.service.YP800Service;
import com.ezsign.feb.hometax.vo.YP800VO;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.jarvis.common.util.DateUtil;

import gov.nts.crypto.FCrypt;
import net.sf.json.JSONObject;

/**
 * 간이지급명세서 전자매체제출 Controller
 * 
 * @author soybean
 *
 */
@Controller
@RequestMapping("/febhometax/")
public class YP800Controller extends BaseController {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name = "yp800Service")
	private YP800Service yp800Service;
	
	@Resource(name = "ys000Service")
	private YS000Service ys000Service;
	
	
	/**
	 * 간이지급명세서 초기데이터 생성
	 * 
	 * @param 계약ID
	 * @param 제출대상구분코드
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST , value = "insertYP800.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insertYP800(
								@RequestParam(value="계약ID", required = false) String 계약ID,
								@RequestParam(value="근무시기", required = false) String 근무시기,
								HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try{
			YP800VO paramVO = new YP800VO();
			paramVO.set계약ID(계약ID);
			paramVO.set근무시기(근무시기);
			paramVO.set구분자코드("SC");
			paramVO.set사용여부("1");
			
			yp800Service.insertYP800(paramVO);
			
			success = true;
		}catch(Exception ex){
			success = false;
			logger.error(ex.getMessage(), ex);
		}
		
		
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	

	/**
	 * 간이지급명세서 전자신고대상 리스트
	 * 
	 * @param vo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST , value = "getYP800List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYP800List(@ModelAttribute("YP800VO") YP800VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			logger.info(":::::::::::::::::::: getYP800List :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
				logger.info("# bizId => " + loginVO.getBizId());
				
				// 연말정산_전자신고 대상자사업장 조회
				List<YP800VO> result = yp800Service.getYP800List(vo);
				
				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
				
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.error(ex.getMessage(), ex);
			throw ex;
		}

		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	/**
	 * 전자매체 제출 문서 작성
	 * 
	 * @param chk
	 * @param 제출대상구분코드
	 * @param 제출년월일
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST , value = "saveYP800.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveYP800(@RequestParam(value="chk", required = false) String chk[],
												@RequestParam(value="근무시기", required = false) String 근무시기,
												@RequestParam(value="제출대상구분코드", required = false) String 제출대상구분코드,
												@RequestParam(value="제출년월일", required = false) String 제출년월일, HttpServletRequest request) throws Exception
	{
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		
		try{
			
			logger.debug("# 근무시기 : " + 근무시기 );
			logger.debug("# 제출대상구분코드 : " + 제출대상구분코드 );
			logger.debug("# 제출년월일 : " + 제출년월일 );
			
			logger.info(":::::::::::::::::::: saveYP800 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
				if(chk != null){

					logger.debug("# chk.length : " + chk.length);
					
					List<YP800VO> yp800List = new ArrayList<YP800VO>();
					for(String val : chk){
						logger.info("# data : " + val );
						String data[] = val.split("\\|");
						
						YP800VO yp800VO = new YP800VO();
						
						yp800VO.set근무시기(근무시기);
						yp800VO.set제출대상구분코드(제출대상구분코드);
						yp800VO.set제출년월일(제출년월일);
						yp800VO.set계약ID(data[0]);
						yp800VO.set사업장ID(data[1]);
						yp800VO.setBizId(data[2]);
						
						if(data.length == 4){
							yp800VO.set전자신고ID(data[3]);
						}	
						
						yp800List.add(yp800VO);
					}
					
					if(yp800List != null && yp800List.size() > 0){
						
						// 계약년도 조회
						YS000VO ys000VO = new YS000VO();
						ys000VO.setBizId(loginVO.getBizId());
						ys000VO.set계약ID(yp800List.get(0).get계약ID());
						List<YS000VO> ys000VOList = ys000Service.getYS000List(ys000VO);				
						String 계약년도 = "";
						if(ys000VOList != null && ys000VOList.size() > 0) {					
							계약년도 = ys000VOList.get(0).getFebYear();
						}					
						
						//간이지급명세서 생성
						Map<String,Object> yp800Map = yp800Service.makeElecDocument(yp800List, loginVO, 계약년도);
					
						if(StringUtils.equals(String.valueOf(yp800Map.get("result")), "0")){
							success = false;
							message = String.valueOf(yp800Map.get("resultMessage"));							
						}else{
							
							//근로소득 전자문서 정보
							jsonObject.put("yp800FileName", yp800Map.get("fileName"));
							jsonObject.put("yp800FilePath", yp800Map.get("filePath"));
							
							
							success = true;							
							message = "처리되었습니다.";	
						}
					}
					
				}else{
					success = false;
					message = "선택된 정보가 없습니다.";
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
	
	/**
	 * 전자매체 제출파일 재생성
	 * 
	 * @param chk
	 * @param 제출대상구분코드
	 * @param 제출년월일
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST , value = "makeFileYP800.do")
	@ResponseBody
	public ResponseEntity<JSONObject> makeFileYP800(@RequestParam(value="근무시기", required = false) String 근무시기,
												@RequestParam(value="제출대상구분코드", required = false) String 제출대상구분코드,
												@RequestParam(value="제출년월일", required = false) String 제출년월일, 												
												@RequestParam(value="계약ID", required = false) String 계약ID,
												@RequestParam(value="사업장ID", required = false) String 사업장ID,
												@RequestParam(value="bizId", required = false) String bizId,
												@RequestParam(value="전자신고ID", required = false) String 전자신고ID,
												@RequestParam(value="febYear", required = false) String febYear,												
												HttpServletRequest request) throws Exception
	{
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		
		try{
			
			logger.debug("# 근무시기 : " + 근무시기 );
			logger.debug("# 제출대상구분코드 : " + 제출대상구분코드 );
			logger.debug("# 제출년월일 : " + 제출년월일 );
			
			logger.info(":::::::::::::::::::: makeFileYP800 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
				List<YP800VO> yp800List = new ArrayList<YP800VO>();
				
				YP800VO yp800VO = new YP800VO();
				
				yp800VO.set근무시기(근무시기);
				yp800VO.set제출대상구분코드(제출대상구분코드);
				yp800VO.set제출년월일(제출년월일);
				yp800VO.set계약ID(계약ID);
				yp800VO.set사업장ID(사업장ID);
				yp800VO.setBizId(bizId);
				yp800VO.setBizId(전자신고ID);
				
				yp800List.add(yp800VO);
						
				//간이지급명세서 생성
				Map<String,Object> yp800Map = yp800Service.makeElecDocument(yp800List, loginVO, febYear);
					
				if(StringUtils.equals(String.valueOf(yp800Map.get("result")), "0")){
					success = false;
					message = String.valueOf(yp800Map.get("resultMessage"));							
				}else{
							
					//근로소득 전자문서 정보
					jsonObject.put("yp800FileName", yp800Map.get("fileName"));
					jsonObject.put("yp800FilePath", yp800Map.get("filePath"));
							
							
					success = true;							
					message = "처리되었습니다.";	
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
	
	/**
	 * 전자신고 파일 다운로드
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
//	@RequestMapping(method = RequestMethod.POST , value = "fileDownYE800.do")
	@RequestMapping(value = "fileDownYP800.do")
	public void fileDownYP800(@RequestParam(value="yp800FileName", required = false) String yp800FileName,
							  @RequestParam(value="yp800FilePath", required = false) String yp800FilePath,	 
							  @RequestParam(value="전자문서비밀번호", required = false) String 전자문서비밀번호,
							  HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		logger.info("# yp800FileName : " + yp800FileName );
		logger.info("# yp800FilePath : " + yp800FilePath );
		logger.info("# 전자문서비밀번호 : " + 전자문서비밀번호 );
		
		List<String> documentList = new ArrayList<String>();
		String systemPath = System.getProperty("system.file.path");			//시스템 파일저장 경로
		
		if(StringUtils.isNotEmpty(전자문서비밀번호)){
			
			//파일 암호화
			File fileEncSavePath = new File(systemPath + File.separator + "FEB" + File.separator + "YP800" + File.separator + "enc" + File.separator + DateUtil.getTimeStamp(3));
			if(!fileEncSavePath.exists()){
				fileEncSavePath.mkdirs();
			}
			
			FCrypt fc = null;
			logger.info("#  암호화 모듈 초기화 시작 #################");
			try{
			 // 초기화
				fc = new FCrypt();
			}catch(Exception ex){
				logger.error(ex.getMessage(), ex);
				throw ex;
			}
			logger.info("#  암호화 모듈 초기화 종료 #################");
			
		    /*
		     * 근로소득 전자문서
		     */
			String strEncFilePath = fileEncSavePath.getAbsolutePath() +File.separator+ yp800FileName;   // 암호화될 파일명
		    
			logger.info("########################### 전자문서 파일 암호호 시작 ###########################");
			
		    // 비밀본호로 파일암호화
		    int nReturn = FCrypt.DSFC_EncryptFile(yp800FilePath, strEncFilePath, 전자문서비밀번호, 0);	
		    logger.info("# 암호화 nReturn : " + nReturn);
		    documentList.add(strEncFilePath);
		    			
		    logger.info("########################### 전자문서 파일 암호호 종료 ###########################");
		}else{
			documentList.add(yp800FilePath);
		}
		
		/*
		String zipFileName = "";		//zip 파일명		
		String zipFilePath = "";  		//zip 파일생성경로
		
		if(documentList != null && documentList.size() > 0){
			
			zipFileName = yp800FileName + ".zip";		//zip 파일명
			zipFilePath = systemPath + File.separator + "FEB" + File.separator + "YE800"+ File.separator +"zip" + File.separator + DateUtil.getTimeStamp(3) + File.separator;  //zip 파일생성경로			
			zipFilePath = zipFilePath.replaceAll("\\\\", "/");
			
			File filePath = new File(zipFilePath);		
			if(!filePath.exists()){
				filePath.mkdirs();
			}
			
			
			FileUtil.ZipFile(zipFilePath + zipFileName, documentList);			
		}
		*/
				
		BufferedInputStream in = null;
		ServletOutputStream out = null;
//		File file = new File(zipFilePath + zipFileName);
		File file = new File(documentList.get(0));
		
		if(file.exists() && file.isFile()){
			
	        
			try{
				in = new BufferedInputStream(new FileInputStream(file));
				
				/*response.setContentType("multipart/form-data;boundary=dkjsei40f9844djs8dviwdf;");					
				response.setHeader("Content-Transfer-Encoding:", "base64");*/
				response.setHeader("Content-Disposition", "attachment;filename=" + file.getName() + ";");
				response.setHeader("Content-Length" , String.valueOf(file.length()) );		
				
				byte b[] = new byte[(int)file.length()];
				int len = 0;
		
				out = response.getOutputStream();
				while((len = in.read(b)) != -1 ){
					out.write(b,0,len);
				}											
				out.flush();
				
			}catch(Exception ex){
				logger.error(ex.getMessage(),ex);
			}finally{
				if(in != null){in.close(); in = null; }
				if(out != null){out.close(); out = null; }
			}

		}else{			
			response.addHeader("success", "false");
			response.addHeader("message", "다운로드 파일이 없습니다.");
		}

	}
}
