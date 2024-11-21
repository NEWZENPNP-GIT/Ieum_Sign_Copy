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

import com.ezsign.code.service.CodeService;
import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.hometax.service.YE800Service;
import com.ezsign.feb.hometax.service.YE801Service;
import com.ezsign.feb.hometax.vo.YE800VO;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.jarvis.common.util.DateUtil;

import gov.nts.crypto.FCrypt;
import net.sf.json.JSONObject;

/**
 * 연말정산 전자매체제출 Controller
 * 
 * @author soybean
 *
 */
@Controller
@RequestMapping("/febhometax/")
public class YE800Controller extends BaseController {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name = "codeService")
	private CodeService codeService;

	@Resource(name = "ys030Service")
	private YS030Service ys030Service;
	
	@Resource(name = "ye800Service")
	private YE800Service ye800Service;
	
	@Resource(name = "ye801Service")
	private YE801Service ye801Service;
	
	@Resource(name = "ys000Service")
	private YS000Service ys000Service;
	
	// 연말정산_사업장 전자신고대상 조회
	@RequestMapping(method = RequestMethod.GET , value = "getYE800List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE800Ctrl(@ModelAttribute("YE800VO") YE800VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			logger.info(":::::::::::::::::::: getYE800List :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
				logger.info("# bizId => " + loginVO.getBizId());
				
				// 연말정산_전자신고 대상자사업장 조회
				List<YE800VO> result = ye800Service.getYE800List(vo);
				
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
	 * 
	 * 전자매체 제출 문서 작성
	 * 
	 * @param chk
	 * @param 제출대상구분코드
	 * @param 제출년월일
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST , value = "saveYE800.do")
	@ResponseBody
//	public ResponseEntity<JSONObject> saveYE800(@ModelAttribute("YE800VO") YE800VO vo, @RequestParam(value="chk", required = false) String chk[], HttpServletRequest request) throws Exception
	public ResponseEntity<JSONObject> saveYE800(@RequestParam(value="chk", required = false) String chk[], 
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
			
			logger.info(":::::::::::::::::::: saveYE800 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				if(chk != null){

					logger.debug("# chk.length : " + chk.length);
					
					List<YE800VO> ye800List = new ArrayList<YE800VO>();
					for(String val : chk){
						logger.info("# data : " + val );
						String data[] = val.split("\\|");
						
						
						YE800VO ye800VO = new YE800VO();
						
						ye800VO.set제출대상구분코드(제출대상구분코드);
						ye800VO.set제출년월일(제출년월일);
						ye800VO.set계약ID(data[0]);
						ye800VO.set사업장ID(data[1]);
						ye800VO.setBizId(data[2]);
						
						if(data.length == 4){
							ye800VO.set전자신고ID(data[3]);
						}	
						
						ye800List.add(ye800VO);
					}
					
					if(ye800List != null && ye800List.size() > 0){						
						
						// 계약년도 조회
						YS000VO ys000VO = new YS000VO();
						ys000VO.setBizId(loginVO.getBizId());
						ys000VO.set계약ID(ye800List.get(0).get계약ID());
						List<YS000VO> ys000VOList = ys000Service.getYS000List(ys000VO);				
						String 계약년도 = "";
						if(ys000VOList != null && ys000VOList.size() > 0) {					
							계약년도 = ys000VOList.get(0).getFebYear();
						}						
						
						//근로소득 생성
						Map<String,Object> ye800Map = ye800Service.makeElecDocument(ye800List, loginVO, 계약년도);
						
						if(StringUtils.equals(String.valueOf(ye800Map.get("result")), "0")){
							success = false;
							message = String.valueOf(ye800Map.get("resultMessage"));							
						}else{
							
							//의료비지금명세서 생성
							Map<String,String> ye801Map = ye801Service.makeElecDocument(ye800List, loginVO, 계약년도);
							
							if(ye801Map != null){
								
								if(StringUtils.equals(String.valueOf(ye801Map.get("result")), "0")){
									success = false;
									message = String.valueOf(ye801Map.get("resultMessage"));							
								}else{	
									
									//근로소득 전자문서 정보
									jsonObject.put("ye800FileName", ye800Map.get("fileName"));
									jsonObject.put("ye800FilePath", ye800Map.get("filePath"));
									
									//의료비지금명세서 전자문서 정보
									jsonObject.put("ye801FileName", ye801Map.get("fileName"));
									jsonObject.put("ye801FilePath", ye801Map.get("filePath"));

									success = true;							
									message = "처리되었습니다.";	
								}
								
							}else{
								
								//근로소득 전자문서 정보
								jsonObject.put("ye800FileName", ye800Map.get("fileName"));
								jsonObject.put("ye800FilePath", ye800Map.get("filePath"));
								
								//의료비지금명세서 전자문서 정보
								jsonObject.put("ye801FileName", "");
								jsonObject.put("ye801FilePath", "");

								success = true;							
								message = "처리되었습니다.";	
								
							}							
														
						}
						
					}else{
						success = false;
						message = "선택된 정보가 없습니다.";
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
	 * 전자신고 파일 다운로드
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
//	@RequestMapping(method = RequestMethod.POST , value = "fileDownYE800.do")
	@RequestMapping(value = "fileDownYE800.do")
	public void fileDownYE800(@RequestParam(value="ye800FileName", required = false) String ye800FileName,
							  @RequestParam(value="ye800FilePath", required = false) String ye800FilePath,	 
							  @RequestParam(value="ye801FileName", required = false) String ye801FileName,
							  @RequestParam(value="ye801FilePath", required = false) String ye801FilePath,
							  @RequestParam(value="전자문서비밀번호", required = false) String 전자문서비밀번호,
							  HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
//		logger.info("# ye800FileName : " + ye800FileName );
//		logger.info("# ye800FilePath : " + ye800FilePath );
//		logger.info("# ye801FileName : " + ye801FileName );
//		logger.info("# ye801FilePath : " + ye801FilePath );
//		logger.info("# 전자문서비밀번호 : " + 전자문서비밀번호 );
		
		List<String> documentList = new ArrayList<String>();
		String systemPath = System.getProperty("system.file.path");			//시스템 파일저장 경로
		
		if(StringUtils.isNotEmpty(전자문서비밀번호)){
			
			//파일 암호화
			File fileEncSavePath = new File(systemPath + File.separator + "FEB" + File.separator + "YE800" + File.separator + "enc" + File.separator + DateUtil.getTimeStamp(3));
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
			String strEncFilePath = fileEncSavePath.getAbsolutePath() +File.separator+ ye800FileName;   // 암호화될 파일명
		    
			logger.info("########################### 전자문서 파일 암호호 시작 ###########################");
			
		    // 비밀본호로 파일암호화
		    int nReturn = FCrypt.DSFC_EncryptFile(ye800FilePath, strEncFilePath, 전자문서비밀번호, 0);	
		    logger.info("# 암호화 nReturn : " + nReturn);
		    documentList.add(strEncFilePath);
		    
		    /*
		     * 의료비지급명세서 전자문서
		     */
		    if(StringUtils.isNotEmpty(ye801FilePath)){
		    	strEncFilePath = fileEncSavePath.getAbsolutePath() +File.separator+ ye801FileName;   // 암호화될 파일명
			
		    	nReturn = FCrypt.DSFC_EncryptFile(ye801FilePath, strEncFilePath, 전자문서비밀번호, 0);	
		    	logger.info("# 암호화 nReturn : " + nReturn);
		    	documentList.add(strEncFilePath);
		    }
			
		    logger.info("########################### 전자문서 파일 암호호 종료 ###########################");
		}else{
			documentList.add(ye800FilePath);
			
			if(StringUtils.isNotEmpty(ye801FilePath)){
				documentList.add(ye801FilePath);
			}
		}
		
		String zipFileName = "";		//zip 파일명		
		String zipFilePath = "";  		//zip 파일생성경로
		
		if(documentList != null && documentList.size() > 0){
			
			zipFileName = ye800FileName + ".zip";		//zip 파일명
			zipFilePath = systemPath + File.separator + "FEB" + File.separator + "YE800"+ File.separator +"zip" + File.separator + DateUtil.getTimeStamp(3) + File.separator;  //zip 파일생성경로			
			zipFilePath = zipFilePath.replaceAll("\\\\", "/");
			
//			logger.info("# zipFileName : " + zipFileName);
//			logger.info("# zipFilePath : " + zipFilePath);
			
			File filePath = new File(zipFilePath);		
			if(!filePath.exists()){
				filePath.mkdirs();
			}
			
			
			FileUtil.ZipFile(zipFilePath + zipFileName, documentList);			
		}

				
		BufferedInputStream in = null;
		ServletOutputStream out = null;
		File file = new File(zipFilePath + zipFileName);
		
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
