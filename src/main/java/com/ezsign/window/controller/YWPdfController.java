package com.ezsign.window.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dreamsecurity.verify.DSTSPDFSig;
import com.epapyrus.api.ExportCustomFile;
import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.house.vo.YE101VO;
import com.ezsign.feb.house.vo.YE104VO;
import com.ezsign.feb.house.vo.YE105VO;
import com.ezsign.feb.house.vo.YE106VO;
import com.ezsign.feb.house.vo.YE107VO;
import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.house.vo.YE109VO;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.service.YE004Service;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE004VO;
import com.ezsign.feb.other.vo.YE201VO;
import com.ezsign.feb.pension.vo.YE301VO;
import com.ezsign.feb.pension.vo.YE302VO;
import com.ezsign.feb.special.vo.YE401VO;
import com.ezsign.feb.special.vo.YE402VO;
import com.ezsign.feb.special.vo.YE403VO;
import com.ezsign.feb.special.vo.YE404VO;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.service.YS010Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.worker.service.YE000Service;
import com.ezsign.feb.worker.service.YE001Service;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.feb.worker.vo.YE051VO;
import com.ezsign.feb.worker.vo.YE052VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.CommonUtil;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.service.YWPdfService;
import com.ezsign.window.vo.DefaultResponse;
import com.ezsign.window.vo.WorkMonth;
import com.ezsign.window.vo.YWPdfSaveRequest;
import com.ezsign.window.vo.YWPdfUploadResponse;
import com.ezsign.window.vo.YWPdfUploadResponse.ManItem;
import com.ezsign.window.vo.YWPdfZipUploadResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@SuppressWarnings("NonAsciiCharacters")
@Controller
@Api(tags = "YWPdfController", description = "PDF 업로드")
@RequestMapping("/febworker/")
public class YWPdfController extends BaseController {

	private Logger logger = Logger.getLogger(this.getClass());
	
    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "febMasterService")
    private FebMasterService febMasterService;

    @Resource(name = "ys000Service")
    private YS000Service ys000Service;

    @Resource(name = "ys010Service")
    private YS010Service ys010Service;

    @Resource(name = "ye001Service")
    private YE001Service ye001Service;

    @Resource(name = "ye004Service")
    private YE004Service ye004Service;
    
    @Resource(name = "ye000Service")
    private YE000Service ye000Service;

    @Resource(name = "ywPdfService")
    private YWPdfService ywPdfService;

    
    
    private String 계약년도;
    private String 국세청PDF유형코드 = "1";

    private String 계약ID;
    private String PDF본인_사용자ID = null;
    private String 근로자_사용자ID = null;
    
    
    private List<YE001VO> 부양가족 = null;
    private List<YE001VO> 추가부양가족 = null;

    private int userType;

    
    
    /**
     *
     * 사용자 연말정산 정보 삭제
     *
     * @param 계약ID
     * @param 사용자ID
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "ywDelete.do")
    @ResponseBody
    public ResponseEntity<JSONObject> ywDelete(
    					@RequestParam String 계약ID, @RequestParam(required = false) String 사용자ID,
          				HttpServletRequest request) throws Exception {
    	
    	JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		
		try {
			
			logger.debug(":::::::::::::::::::: ywAllDelete :::::::::::::::::::");
						
			logger.debug("# 계약ID : " + 계약ID );
			logger.debug("# 사용자ID : " + 사용자ID );
			
			Map<String,String> resultMap = ywPdfService.deleteData(계약ID, 사용자ID, "ywDelete.do");
			
			if(StringUtils.equals(resultMap.get("result"), "success")){
				success = true;
				message = "삭제되었습니다.";
			}else{
				success = false;
				message = StringUtils.defaultString(resultMap.get("resultMessage"), "삭제에 실패했습니다.");
			}
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			success = false;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
		
		jsonObject.put("success", success);
		jsonObject.put("message", message);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);		
    	
    }
    
    
    @ApiOperation(value = "PDF Zip 업로드", produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA)
    @RequestMapping(method = RequestMethod.POST, value = "ywPdfZipUpload.do")
    @ResponseBody
    public ResponseEntity<YWPdfZipUploadResponse> postYWPdfZipUpload(
            @RequestParam String 계약ID, @RequestParam MultipartFile file,
            HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YWPdfZipUploadResponse response = new YWPdfZipUploadResponse(); 
        response.result = new ArrayList<>();

        this.계약ID = 계약ID;
        String zipPath = null;
        
        int successCnt = 0;
        int failCnt = 0;
        List<File> listPdf = null;			//PDF 파일 리스트 
        
        try {
        	logger.debug(":::::::::::::::::::: postYWPdfZipUpload :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            boolean success = false;
            
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                userType = StringUtil.strPaserInt(loginVO.getUserType());

                String szSavePath = MultipartFileUtil.getSystemTempPath() + "hometax/pdf";
                List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);

                Map<String, String> resultItem = null;
                if (resultFileList.size() != 1) {
                    resultItem = new HashMap<>();
                    
                    resultItem.put("fileInfo", "");
                    resultItem.put("message", "연말정산 PDF Zip File Count" + resultFileList.size() + " Fail");
                    
                    logger.error("# 연말정산 PDF Zip File Count => " + resultFileList.size() + " Fail");
                    response.result.add(resultItem);
                    
                } else {
                    // 전달받은 파일리스트
                    FileVO fileVO = resultFileList.get(0);
                    zipPath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
                    logger.debug("# fileVO : " + fileVO);

//                    List<File> listPdf = FileUtil.extractZipFile(zipPath, null);
                    listPdf = FileUtil.extractZipFile(zipPath, null);
                    if (listPdf == null || listPdf.size() == 0) {
                        resultItem = new HashMap<>();
                        
                        resultItem.put("fileInfo", fileVO.getFileStreOriNm());
                        resultItem.put("message", fileVO.getFileStreOriNm() + " 파일 압축에 실패했습니다.");
                        
                        logger.error("# " + fileVO.getFileStreOriNm() + "파일 압축 해제에 실패했습니다.");
                        
                        response.result.add(resultItem);
                    } else {
                    	
                        String message = "";
                        
                        for (File pdf : listPdf) {
                        	
                        	this.PDF본인_사용자ID = null;
                        	this.부양가족 = null;
                        	this.추가부양가족 = null;
                        	                            
                            success = false;
                            message = "";

                            File xml = null;
                            try {
                            	
                                if (!pdf.exists()) {
                                    message = "PDF 파일이 존재하지 않습니다.";
                                } else {                           
                                	
                                	//파일 확장자 검사
                            		String pdfPath = pdf.getPath();
                            		String fileExt = pdfPath.substring(pdfPath.lastIndexOf(".")+1).toUpperCase();
                                	
                            		if(!StringUtils.equals("PDF", fileExt)){
                            			message = "PDF 파일만 가능합니다.";
                            		}else{                                	
                            			
                            			String xmlPath = pdf.getPath() + ".xml";
                    					ExportCustomFile export = new ExportCustomFile();
                    					// 데이터 추출
                    					export.NTS_GetFile(pdf.getPath(), null, "XML", xmlPath, false);
                    					int v_ret = export.NTS_GetLastError();
                    					if (v_ret == 1) {

                    						xml = new File(xmlPath);
                    						if (!xml.exists()) {
                    							message = "데이터 추출에 실패하였습니다.";
                    						} else {
                    							/*YS010VO ys010 = new YS010VO();
                                            	ys010.set계약ID(계약ID);
                                            	List<YS010VO> result = ys010Service.getYS010List(ys010);
                                            	if (result.size() > 0) {
                                                		국세청PDF유형코드 = result.get(0).get국세청PDF유형코드();
                                            	}*/

                    							YS000VO ys000 = new YS000VO();
                    							ys000.setBizId(loginVO.getBizId());
                    							ys000.set계약ID(계약ID);
                    							List<YS000VO> listYS000 = ys000Service.getYS000List(ys000);
                    							String 계약년도 = "";
                    							if (listYS000.size() > 0) {
                    								계약년도 = listYS000.get(0).getFebYear();
                    							}

//                                        		this.계약년도 = parseXMLYear(xml);
                    							Document doc = parseXML(xml);
                    							if(doc != null){
                                        	
                    								this.계약년도 = doc.getElementsByTagName("att_year").item(0).getTextContent();
                    								logger.debug("# 귀속연도: " + this.계약년도);
                                        	
                    								if (!계약년도.equals(this.계약년도)) {
                    									message = "당해년도 연말정산 PDF문서가 아닙니다.";
                    								} else {
                                            	
                    									/* 편리한 연말정산 */
                    									if(doc.getElementsByTagName("att_Resid") != null 
                    											&& doc.getElementsByTagName("att_Resid").item(0) != null 
                    											&& StringUtils.isNotEmpty(doc.getElementsByTagName("att_Resid").item(0).getTextContent())){

                    										logger.debug("### PDF Zip 업로드 => 편리한 연말정산 saveXmlData ###");     
                                            		
                    										//PDF 본인 주민번호
                    										String resid = StringUtils.defaultString(doc.getElementsByTagName("att_Resid").item(0).getTextContent());
                    									
                    										if(StringUtils.isNotEmpty(resid)){
                    											String 개인식별번호 = SecurityUtil.getinstance().aesEncode(resid);
                    										
                    											YE000VO ye000VO = new YE000VO();
                    											ye000VO.set계약ID(계약ID);
                    											ye000VO.set개인식별번호(개인식별번호);
                    						                
                    											ye000VO= ye000Service.getYE000DataDetail(ye000VO);
                    						        		
                    											if(ye000VO != null){
                    												this.근로자_사용자ID = ye000VO.get사용자ID();     
                    						        			
                    												국세청PDF유형코드 = "2";
                    												//부양가족 여부를 미리 체크해서 셋팅한다.
                    												saveYe001(loginVO, doc);
                    												
                    												//부양가족 등록후 1초 텀
                    												Thread.sleep(1000);
                    												
                    												saveXmlData2(loginVO, doc);
                            									
                    												logger.debug("# 근로자_사용자ID : "  + this.근로자_사용자ID );
                    												logger.debug("# PDF본인_사용자ID : "  + this.PDF본인_사용자ID ); 
                	                                    		         
                    												success = true;
                    												message = "성공";
                	                                    		
                    											}else{
                    												message = "존재하지 않는 사용자입니다.";
                    											}

                    										}else{
                    											message = "존재하지 않는 사용자입니다.";
                    										}
                    									
                    									}
                    									/* 연말정산 간소화 */
                    									else{
                    										
                    										//연말정산 간소화 일때만 PDF 진본성 검증 
                    										logger.debug("### PDF Zip 업로드 => 연말정산 간소화  saveXmlData ###");
                    										
                    										/* 진본성 검증 초기화 */
                    										boolean pdf성공여부 = false;
                    										DSTSPDFSig dstsPdfsig = new DSTSPDFSig();
                                                			try{                    		
                                                				dstsPdfsig.init(pdf.getPath());
                                                				dstsPdfsig.tokenParse();     
                                                    		
                                                				pdf성공여부 = true;
                                                			}catch(Exception ex){
                                                				pdf성공여부 = false;
                                                				logger.error(ex.getMessage(), ex);
                                                			}
                                                			
                                                			if(pdf성공여부){
                                                				
                                                				/* 전자문서 검증 */
                                                				if (!dstsPdfsig.tokenVerify()) {
                                                					message = dstsPdfsig.getTstVerifyFailInfo();
                                                				} else {
                                                					
                                                					//PDF 본인 주민번호
                            										NodeList list = doc.getElementsByTagName("form");
                            										for (int i = 0; i < list.getLength(); i++) {
                            											Node node = list.item(i);
                            											if (node.getNodeType() == Node.ELEMENT_NODE) {
                            												Element element = (Element) node;
                            							                
                            												//건강보험료
                            												if(StringUtils.equals(element.getAttribute("form_cd"), "O101M")){
                            							                	
                            													NodeList sublist = element.getElementsByTagName("man");
                            							                	
                            													for (int j = 0; j < sublist.getLength(); j++) {
                            														Node nData = sublist.item(j);

                            														if (nData.getNodeType() == Node.ELEMENT_NODE) {
                            															Element eData = (Element) nData;
                            							                             
                            															//PDF 본인 주민번호
                            															String resid = eData.getAttribute("resid");
                            							                             
                            															if(StringUtils.isNotEmpty(resid)){
                            																String 개인식별번호 = SecurityUtil.getinstance().aesEncode(resid);
                                                 										
                            																YE000VO ye000VO = new YE000VO();
                            																ye000VO.set계약ID(계약ID);
                            																ye000VO.set개인식별번호(개인식별번호);
                                                 						                
                            																ye000VO= ye000Service.getYE000DataDetail(ye000VO);
                                                 						        		
                            																if(ye000VO != null){
                            																	this.근로자_사용자ID = ye000VO.get사용자ID();     
                                                 						        			
                            																	국세청PDF유형코드 = "1";
                            																	saveXmlData(loginVO, doc);
                                                         									
                            																	logger.debug("# 근로자_사용자ID : "  + this.근로자_사용자ID );
                            																	logger.debug("# PDF본인_사용자ID : "  + this.PDF본인_사용자ID ); 
                                             	                                    		         
                            																	success = true;
                            																	message = "성공";
                                             	                                    		
                            																}else{
                            																	message = "존재하지 않는 사용자입니다.";
                            																}

                            															}else{
                            																message = "존재하지 않는 사용자입니다.";
                            															}
                            							                             
                            														}
                            							                         
                            													}
                            												}
                            							                
                            											}
                            							            
                            										}                                                					
                                                					
                                                				}
                                                			
                    										}//if(pdf성공여부){
                                                			else{
                                                				message = "국세청 PDF 분석에 실패했습니다.\n국세청 PDF를 다시 받아서 올려주십시오.";
                                                			} 

                    									}

                    								} //if (!계약년도.equals(this.계약년도)) {
                        						
                                            
                    							}else{
                    								message = "XML 파싱에 실패하였습니다.";
                    							}

                    						}// if (!xml.exists()) {
                        				
                    					} else if (v_ret == 0) {
                    						message = "연말정산 표준 전자문서가 아닙니다.";
                    					} else if (v_ret == -1) {
                    						message = "비밀번호가 맞지 않습니다.";
                    					} else if (v_ret == -2) {
                    						message = "PDF문서가 아니거나 손상된 문서입니다.";
                    					} else {
                    						message = "데이터 추출에 실패하였습니다.";
                    					}
                    				
                            		}// if(!StringUtils.equals("PDF", fileExt)){
                            		
                                }// if (!pdf.exists()) {
                                                                
                            } catch (Exception ex) {
                               	logger.error(ex.getMessage(), ex);
                               	message = ex.getMessage();                                
                            } finally{
                            
                               	if (xml != null && xml.exists()) {
                               		xml.delete();
                               	}
                               	
                            }

                            /*
                            if(!success){
                            	failCnt++;
                            	resultItem = new HashMap<>();
                            
                            	resultItem.put("fileInfo", pdf.getName());
                                resultItem.put("message", message);
                                
                                response.result.add(resultItem);
                            }else{
                            	successCnt++;
                            	
                            	resultItem = new HashMap<>();
                                
                            	message = "성공 [" +successCnt+ "] 건 / 실패 [" +failCnt+ "] 건이 처리되었습니다.";
                            	
                            	resultItem.put("fileInfo", "");
                                resultItem.put("message", message);
                                
                                response.result.add(resultItem);
                            }
                            */
                            
                            //로그등록
                            if (StringUtils.isNotEmpty(this.PDF본인_사용자ID)) {
								YE004VO ye004 = new YE004VO();
								ye004.set계약ID(계약ID);
								ye004.set사용자ID(this.PDF본인_사용자ID);
								if (success) {
									ye004.set처리여부("1");
									ye004.set처리내용("성공");
								} else {
									ye004.set처리여부("F");
									ye004.set처리내용(message);
								}
								ye004.set국세청PDF유형(국세청PDF유형코드);
								ye004Service.insUpdYE004(ye004);
							}
                            
                            //작업이 실패하면은 현재작업 중지
                            if(!success){
                            	failCnt++;
                            	
                            	resultItem = new HashMap<>();
                                
                            	resultItem.put("fileInfo", pdf.getName());
                                resultItem.put("message", message);
                                
                                response.result.add(resultItem);
                            	
                            	break;
                            }else{
                            	successCnt++;
                            }                            
                                                        
                        } //for (File pdf : listPdf) {
                        
                        
                    }

                }
            }
            
            response.success = success;
            
            if(response.result != null && response.result.size() == 0){
            	
            	Map<String, String> resultItem = new HashMap<>();
                
            	String message = "성공 [" +successCnt+ "] 건 / 실패 [" +failCnt+ "] 건이 처리되었습니다.";
            	
            	resultItem.put("fileInfo", "");
                resultItem.put("message", message);
                
                response.result.add(resultItem);
                
            }
            
            
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        	logger.error(ex.getMessage(), ex);
        } finally{
        	
        	//압축파일삭제
        	if(StringUtils.isNotEmpty(zipPath)){   
        		File zipFile = new File(zipPath);
        		
        		if(zipFile != null && zipFile.exists()){
        			zipFile.delete();
        		}
        	}
        	
        	//pdf 파일 삭제
        	if(listPdf != null && listPdf.size() > 0){
        		for (File pdf : listPdf) {
        			if(pdf != null && pdf.exists()){
        				pdf.delete();
        			}
        		}
        	}
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    /**
     * PDF 부양가족 정보를 체크해서 셋팅한다.
     * 
     * @param loginVO
     * @param doc
     * @return
     * @throws Exception
     */
    private boolean saveYe001(SessionVO loginVO, Document doc) throws Exception {
    	
    	boolean result = false;
    	List<YE001VO> listYE001 = new ArrayList<>();  	 //부양가족
    	
    	NodeList list = doc.getElementsByTagName("form");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                
                if(StringUtils.equals("A101Y",element.getAttribute("form_cd"))) {
                	
                	//부양가족정보 파싱 
            		parseA001(listYE001, element.getChildNodes());                                               
            		
            		//부양가족 정보 수정 
            		if(StringUtils.isNotEmpty(this.계약ID) && StringUtils.isNotEmpty(this.근로자_사용자ID)){
        		  	
            			boolean insertChk = false;
            			//현재 근로자 사용자ID가 부양가족 정보에 있는경우에만 등록한다.
            			for(YE001VO ye001VO : listYE001){                        		
            				if(StringUtils.equals(ye001VO.get사용자ID(),this.근로자_사용자ID)){
            					insertChk = true;
            				}                        		
            			}
        		  	
            			if(insertChk){
            				if(this.부양가족 == null){
            					this.부양가족 = new ArrayList<YE001VO>();
            				}                        	
            				ywPdfService.saveYE001(계약ID, this.근로자_사용자ID, listYE001, this.부양가족);
            			}
        		  } 
            		
                }                
                
            }           
        }
    	
    	return result;
    	
    }
    
    /**
     *
     * 국세청 PDF 업로드 및 파싱 처리 
     *
     * @param 계약ID
     * @param 사용자ID
     * @param file
     * @param password
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "PDF 업로드", produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA)
//    @RequestMapping(method = RequestMethod.POST, value = "ywPdfUpload.do")
    @RequestMapping(value = "ywPdfUpload.do")
    @ResponseBody
    public ResponseEntity<YWPdfUploadResponse> postYWPdfUpload(
            //@RequestParam String REQ_계약ID, @RequestParam(required = false) String REQ_사용자ID,
    		@RequestParam String 계약ID, @RequestParam(required = false) String 사용자ID,
            @RequestParam MultipartFile file, @RequestParam(required = false) String password,
            HttpServletRequest request) throws Exception {        
    	
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YWPdfUploadResponse response = new YWPdfUploadResponse();
        response.success = false;
        

        File pdf = null;
        File xml = null;
        boolean xmlDelChk = true;
        
        try {
        	logger.debug(":::::::::::::::::::: postYWPdfUpload :::::::::::::::::::");
        	logger.debug("# Request 사용자ID : " + 사용자ID );
        	
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {

            	this.계약ID = 계약ID;
            	this.PDF본인_사용자ID = null;
            	this.부양가족 = null;
            	this.추가부양가족 = null;
            	this.근로자_사용자ID = StringUtils.defaultString(사용자ID, loginVO.getUserId());
            	            	
                userType = StringUtil.strPaserInt(loginVO.getUserType());

                String szSavePath = MultipartFileUtil.getSystemTempPath() + "hometax/pdf";
                List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);

                if (resultFileList.size() != 1) {
                    response.message = "연말정산 PDF File Count : " + resultFileList.size();                    
                } else {
                    // 전달받은 파일리스트
                    FileVO fileVO = resultFileList.get(0);
                    String pdfPath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();

                    logger.debug("# fileVO : " + fileVO);
                    pdf = new File(pdfPath);
                    if (!pdf.exists()) {
                        response.message = "파일이 존재하지 않습니다.";
                    } else {                 
                    	
                    	//파일 확장자 검사
                		String fileExt = pdfPath.substring(pdfPath.lastIndexOf(".")+1).toUpperCase();
                    	
                		if(!StringUtils.equals("PDF", fileExt)){
                			response.message = "PDF 파일만 가능합니다.";
                		}else{ 
                			
                			String xmlPath = pdfPath + ".xml";
        					ExportCustomFile export = new ExportCustomFile();
        					// 데이터 추출
        					export.NTS_GetFile(pdfPath, password, "XML", xmlPath, false);
        					int v_ret = export.NTS_GetLastError();
        					if (v_ret == 1) {
        						xml = new File(xmlPath);
            				
        						if (!xml.exists()) {
        							response.message = "데이터 추출에 실패하였습니다.";
        						} else {
        							logger.debug("# PDF 파일 XML INFO : " + xml.getAbsolutePath());                        	
        							
        							/* 연말정산_기초설정 조회 */
        							/*YS010VO ys010 = new YS010VO();
                                	ys010.set계약ID(계약ID);
                                	List<YS010VO> result = ys010Service.getYS010List(ys010);
                                	if (result != null && result.size() > 0) {
                                    	국세청PDF유형코드 = result.get(0).get국세청PDF유형코드();
                                	}*/

        							/* 연말정산_계약정보 조회 */
        							YS000VO ys000 = new YS000VO();
        							ys000.setBizId(loginVO.getBizId());
        							ys000.set계약ID(계약ID);
        							List<YS000VO> listYS000 = ys000Service.getYS000List(ys000);
        							String 계약년도 = "";
        							if (listYS000 != null && listYS000.size() > 0) {
                                    	계약년도 = listYS000.get(0).getFebYear();
        							}

//                            		this.계약년도 = parseXMLYear(xml);
        							Document doc = parseXML(xml);
                            
        							if(doc != null){
                            	
        								boolean pdf성공여부 = false;
        								this.계약년도 = doc.getElementsByTagName("att_year").item(0).getTextContent();
        								logger.debug("# 귀속연도: " + this.계약년도);
                            	
                            	
        								if (!계약년도.equals(this.계약년도)) {
        									response.message = "당해년도 연말정산 PDF문서가 아닙니다.";
        								} else {

        									/* 편리한 연말정산 */
        									if(doc.getElementsByTagName("att_Resid") != null 
        											&& doc.getElementsByTagName("att_Resid").item(0) != null 
        											&& StringUtils.isNotEmpty(doc.getElementsByTagName("att_Resid").item(0).getTextContent())){
                                			
        										logger.debug("### PDF 업로드 => 편리한 연말정산  parseXMLSum2 ###");
        										pdf성공여부 = true;
        										국세청PDF유형코드 = "2";
        										response.인별항목 = parseXMLSum2(doc); 
                                		
        									}
        									/* 연말정산 간소화 */
        									else{
        										
        										//연말정산 간소화 일때만 진본성 검증을 한다.        										
        										/* 진본성 검증 초기화 */        			                		
        			                			DSTSPDFSig dstsPdfsig = new DSTSPDFSig();
        			                			try{                    		
        			                				dstsPdfsig.init(pdfPath);
        			                				dstsPdfsig.tokenParse();     
        			                    		
        			                				pdf성공여부 = true;
        			                			}catch(Exception ex){
        			                				pdf성공여부 = false;
        			                				logger.error(ex.getMessage(), ex);
        			                			}
        			                    	
        			                			if(pdf성공여부){

        			                				/* 전자문서 검증 */
        			                				if (!dstsPdfsig.tokenVerify()) {
        			                					pdf성공여부 = false;
        			                					response.message = dstsPdfsig.getTstVerifyFailInfo();
        			                				} else {
//        			                    			if(true){
        			                					
        			                					logger.debug("### PDF 업로드 => 연말정산 간소화  parseXMLSum ###");
        		                                		
                										국세청PDF유형코드 = "1";
                										response.인별항목 = parseXMLSum(doc);
        			                					
        			                				}
        			                    		
        			                			} //if(pdf성공여부){
        			                			else{
        			                				response.success = false;
        			                				response.message = "국세청 PDF 분석에 실패했습니다.\n국세청 PDF를 다시 받아서 올려주십시오.";
        			                			}

        									}
        									
        									
        									if(pdf성공여부){
        										
	        									logger.debug("# 근로자_사용자ID : "  + this.근로자_사용자ID );
	        									logger.debug("# PDF본인_사용자ID : "  + this.PDF본인_사용자ID );                                        
	                                		
	        									if (StringUtils.isEmpty(this.PDF본인_사용자ID)) {
	        										response.message = "존재하지 않는 사용자입니다.";
	        									} else if (loginVO.getUserId().equals(this.근로자_사용자ID) && !this.근로자_사용자ID.equals(this.PDF본인_사용자ID)) {
	        										response.message = "본인의 PDF가 아닙니다.";
	        									} else if (StringUtils.isNotEmpty(this.근로자_사용자ID) && !this.근로자_사용자ID.equals(this.PDF본인_사용자ID)) {
	        										response.message = "선택하신 근로자의 PDF가 아닙니다.";
	        									} else {
	                                    	
	        										YE000VO work = new YE000VO();
	        										work.set계약ID(계약ID);
	        										work.set사용자ID(StringUtils.defaultString(this.PDF본인_사용자ID,this.근로자_사용자ID));
	        										response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));
	
	        										response.xmlPath = xmlPath;
	        										response.계약ID = 계약ID;
	        										response.사용자ID = this.PDF본인_사용자ID;
	        										response.부양가족 = 부양가족;
	
	        										if(추가부양가족 == null || 추가부양가족.size() == 0){
	        											response.추가부양가족 = null;
	        										}else{
	        											response.추가부양가족 = 추가부양가족;
	        										}
	                                        
	        										logger.debug("# response.추가부양가족 :  " + response.추가부양가족 );
	                                        
	        										response.success = true;
	        										xmlDelChk = false;
	        									}
        									}
                                	
        								} // if (!계약년도.equals(this.계약년도))
                            	
        							}else{
        								response.message = "XML 파싱에 실패하였습니다.";
        							}

        						}
        					} else if (v_ret == 0) {
        						response.message = "연말정산 표준 전자문서가 아닙니다.";
        					} else if (v_ret == -1) {
        						response.message = "비밀번호가 맞지 않습니다.";
        					} else if (v_ret == -2) {
        						response.message = "PDF문서가 아니거나 손상된 문서입니다.";
        					} else {
        						response.message = "데이터 추출에 실패하였습니다.";
        					}
        					
                		} //if(!StringUtils.equals("PDF", fileExt)){
                        
                    } //if (!pdf.exists()) {
                }

                if (!response.success) {
                    YE004VO ye004 = new YE004VO();
                    ye004.set계약ID(계약ID);
                    ye004.set처리여부("F");
                    ye004.set처리내용(response.message);
                    ye004.set국세청PDF유형(국세청PDF유형코드);
                    
                    if (StringUtils.isNotEmpty(this.근로자_사용자ID)) {
                        ye004.set사용자ID(this.근로자_사용자ID);
                        ye004Service.insUpdYE004(ye004);
                    } else if (StringUtils.isNotEmpty(this.PDF본인_사용자ID)) {
                        ye004.set사용자ID(this.PDF본인_사용자ID);
                        ye004Service.insUpdYE004(ye004);
                    }
                }
            }
            
            
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        	logger.error("# 계약ID : " + 계약ID);
        	logger.error("# 근로자_사용자ID : " + this.근로자_사용자ID);
        	logger.error("# PDF본인_사용자ID : " + this.PDF본인_사용자ID);
        	
        	
            logger.error(ex.getMessage(), new Throwable(ex));
            response.message = ex.getLocalizedMessage();

        } finally{

        	if (pdf != null && pdf.exists()) {
                pdf.delete();
            }
        	if(xmlDelChk){
	            if (xml != null && xml.exists()) {
	                xml.delete();
	            }
        	}
        	
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    /**
     *
     * 국세청 PDF 정보 DB 저장 처리
     *
     * @param body
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "PDF 저장", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "ywPdfSave.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> postYWPdfSave(@RequestBody YWPdfSaveRequest body, HttpServletRequest request)
            throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        response.success = false;

        this.계약ID = body.계약ID;
        this.PDF본인_사용자ID = body.사용자ID;

        File xml = null;
        try {
        	logger.debug(":::::::::::::::::::: postYWPdfSave :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	
                xml = new File(body.xmlPath);
                if (!xml.exists()) {
                    response.message = "XML 파일이 없습니다.";
                } else {
                	
                	/* 연말정산_기초설정 조회 */
                    /*YS010VO ys010 = new YS010VO();
                    ys010.set계약ID(계약ID);
                    List<YS010VO> result = ys010Service.getYS010List(ys010);
                    if (result.size() > 0) {
                        	국세청PDF유형코드 = result.get(0).get국세청PDF유형코드();
                    }*/

                    /* 연말정산_계약정보 조회 */
                    YS000VO ys000 = new YS000VO();
                    ys000.setBizId(loginVO.getBizId());
                    ys000.set계약ID(계약ID);
                    List<YS000VO> listYS000 = ys000Service.getYS000List(ys000);
                    String 계약년도 = "";
                    if (listYS000.size() > 0) {
                        계약년도 = listYS000.get(0).getFebYear();
                    }

//                    this.계약년도 = parseXMLYear(xml);
                    Document doc = parseXML(xml);

                    if(doc != null){

                    	this.계약년도 = doc.getElementsByTagName("att_year").item(0).getTextContent();
                    	logger.debug("# 귀속연도: " + this.계약년도);
                    	
                    	
                    	if (!계약년도.equals(this.계약년도)) {
                            response.message = "당해년도 연말정산 PDF문서가 아닙니다.";
                        } else {
                        	
                        	if (StringUtils.isEmpty(this.PDF본인_사용자ID)) {
                                response.message = "존재하지 않는 사용자입니다.";
                            } else {
                            	                            	
                            	/* 편리한 연말정산 */
                            	if(doc.getElementsByTagName("att_Resid") != null 
                            			&& doc.getElementsByTagName("att_Resid").item(0) != null 
                            			&& StringUtils.isNotEmpty(doc.getElementsByTagName("att_Resid").item(0).getTextContent())){

                            		logger.debug("### PDF 저장 => 편리한 연말정산  saveXmlData ###");                            		
                            		국세청PDF유형코드 = "2";                                     
                            		
                            		saveXmlData2(loginVO, doc);                            		
                            	}
                            	/* 연말정산 간소화 */
                            	else{

                            		logger.debug("### PDF 저장 => 연말정산 간소화  saveXmlData ###");
                            		
                            		국세청PDF유형코드 = "1";
                            		saveXmlData(loginVO, doc);
                            	}
                            	
                            	response.success = true;
                                
                            }
                            
                        }

                    } else{
                    	response.message = "XML 파싱에 실패하였습니다.";
                    }
                    
//                    xml.delete();
                }


                if (this.PDF본인_사용자ID != null) {
                    YE004VO ye004 = new YE004VO();
                    ye004.set계약ID(계약ID);
                    ye004.set사용자ID(this.PDF본인_사용자ID);
                    if (response.success) {
                        ye004.set처리여부("1");
                        ye004.set처리내용("성공");
                    } else {
                        ye004.set처리여부("F");
                        ye004.set처리내용(response.message);
                    }
                    ye004.set국세청PDF유형(국세청PDF유형코드);
                    ye004Service.insUpdYE004(ye004);
                }

            }
        } catch (Exception ex) {
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//            logService.error(ex.getMessage(), new Throwable(ex));
        	logger.error(ex.getMessage(), ex);
            response.message = ex.getMessage();

            
        } finally{
        	if (xml != null) {
                xml.delete();
            }
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    
    /**
     *
     * XML 파일을 파싱한다.
     *
     * @param xml
     * @return
     * @throws Exception
     */
    private Document parseXML(File xml) throws Exception {
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xml);
        
        
        return doc;
    }
    
    /*private String parseXMLYear(File xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xml);

        String att_year = doc.getElementsByTagName("att_year").item(0).getTextContent();
        logger.debug("# 귀속연도: " + att_year);
        return att_year;
    }*/
    
    

    /**
     *
     * 국세청 XML 파싱 처리
     *
     * @param xml
     * @return
     * @throws Exception
     */
//    private List<ManItem> parseXMLSum(File xml) throws Exception {
    private List<ManItem> parseXMLSum(Document doc) throws Exception {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setIgnoringElementContentWhitespace(true);
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document doc = builder.parse(xml);

        List<ManItem> result = new ArrayList<>();
        ManItem total = new ManItem();
        total.이름 = "구분별 합계";
        result.add(total);

        NodeList list = doc.getElementsByTagName("form");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                switch (element.getAttribute("form_cd")) {
                    case "A102Y":
                    case "A102M":
                        // 보장성보험
                        parseASum(result, element.getChildNodes());
                        break;

                    case "B101Y":
                    case "B101D":
                        // 의료비
                        parseBSum(result, element.getChildNodes());
                        break;

                    case "C102Y":
                    case "C102M": // 교육비(유초중고,대학,기타)
                    case "C202Y":
                    case "C202M": // 교육비(직업훈련비)
                    case "C301Y":
                    case "C301M": // 교육비(교복구입비)
                    case "C401Y":
                    case "C401M": // 교육비(학자금대출)
                        parseCSum(result, element.getChildNodes());
                        break;

                    case "D101Y":
                    case "D101M":
                        // 개인연금저축
                        parseDSum(result, element.getChildNodes());
                        break;

                    case "E102Y":
                    case "E102M": // 연금저축
                    case "F102Y":
                    case "F102M": // 퇴직연금
                        parseE_FSum(result, element.getChildNodes());
                        break;

                    case "G106Y":
                    case "G106M": // 신용카드
                    case "G107Y":
                    case "G107M": // 신용카드 (2018년)	
                    case "G206M": // 현금영수증
                    case "G207M": // 현금영수증 (2018년)
                    case "G306Y":
                    case "G306M": // 직불카드
                    case "G307Y":
                    case "G307M": // 직불카드 (2018년)
                        parseGSum(result, element.getChildNodes());
                        break;

                    case "J101Y":
                    case "J101M": // 주택임차차입금 원리금상환액
                    case "J203Y":
                    case "J203M": // 장기주택저당차입금 이자상환액
                    case "J301Y":
                    case "J301M": // 주택마련저축
                        parseJSum(result, element.getChildNodes());
                        break;

                    case "K101M":
                        // 소기업소상공인공제부금
                        parseKSum(result, element.getChildNodes());
                        break;

                    case "L102Y":
                    case "L102D":
                        // 기부금
                        parseLSum(result, element.getChildNodes());
                        break;

                    case "N101Y":
                    case "N101M":
                        // 장기집합투자증권저축
                        parseNSum(result, element.getChildNodes());
                        break;

                    case "O101M":
                        // 건강보험, 장기요양보험
                        parseOSum(result, element.getChildNodes());
                        break;

                    case "P102M":
                        // 국민연금
                        parsePSum(result, element.getChildNodes());
                        break;
                }
            }
        }

        return result;
    }

//    private List<ManItem> parseXMLSum2(File xml) throws Exception {
    private List<ManItem> parseXMLSum2(Document doc) throws Exception {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setIgnoringElementContentWhitespace(true);
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document doc = builder.parse(xml);

        List<ManItem> result = new ArrayList<>();
        List<YE001VO> listYE001 = new ArrayList<>();  	 //부양가족
        List<YE403VO> listYE403 = new ArrayList<>();	 //교육비
        List<YE101VO> listYE101 = new ArrayList<>();	 //주택임차차입금원리금상환액
        List<YE104VO> listYE104 = new ArrayList<>();	 //장기주택저당차입금이자상환액
        List<YE401VO> listYE401 = new ArrayList<>();  	 //보험료
        
        ManItem total = new ManItem();
        total.이름 = "구분별 합계";
        result.add(total);

        NodeList list = doc.getElementsByTagName("form");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                switch (element.getAttribute("form_cd")) {
                    case "A101Y":
                    	
                        //부양가족정보 파싱 
                        parseA001(listYE001, element.getChildNodes());                                               
                        
                        //부양가족 정보 수정 
                        if(StringUtils.isNotEmpty(this.계약ID) && StringUtils.isNotEmpty(this.근로자_사용자ID)){
                        	
                        	boolean insertChk = false;
                        	//현재 근로자 사용자ID가 부양가족 정보에 있는경우에만 등록한다.
                        	for(YE001VO ye001VO : listYE001){                        		
                        		if(StringUtils.equals(ye001VO.get사용자ID(),this.근로자_사용자ID)){
                        			insertChk = true;
                        		}                        		
                        	}
                        	
                        	if(insertChk){
	                        	if(부양가족 == null){
	                        		부양가족 = new ArrayList<YE001VO>();
	                        	}                        	
	                        	ywPdfService.saveYE001(계약ID, this.근로자_사용자ID, listYE001, 부양가족);
                        	}
                        }
                                                
                    	// 공제신고서
                        parseASum2(result, listYE101, listYE104, listYE403, listYE401, false, element.getChildNodes());     
                        
                        break;
                    case "B101Y":
                        // 연금저축등 소득.세액 공제명세
                        break;
                    case "C101Y":
                        // 월세액.거주자간 주택임차차입금 상환액
                        break;
                    case "D101Y":
                        // 의료비 지급명세
                        break;
                    case "E101Y":
                        // 기부금 명세
                        break;
                    case "F101Y":
                        // 신용카드등 소득공제신청서
                    	parseFsum2(result, element.getChildNodes());
                    	
                        break;
                }
            }
        }
        

        return result;
    }
       
    
    /**
     *
     * F101Y-신용카드등 소득공제신청서 (편리한 연말정산)
     *
     * @param listMan
     * @param listNode
     * @throws Exception
     */
    private void parseFsum2(List<ManItem> listMan, NodeList listNode) throws Exception {
    	
        for (int i = 0; i < listNode.getLength(); i++) {
        
        	Node node = listNode.item(i);
        	if (node.getNodeType() == Node.ELEMENT_NODE) {
        		
        		Element element = (Element) node;
        		
        		NodeList sublist = element.getElementsByTagName("data");
        		for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);
                 
                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                    	
                    	Element eData = (Element) nData;
                    	
                    	for(ManItem manItem : listMan){
                    		
                			if(StringUtils.isNotEmpty(manItem.개인식별번호) && StringUtils.isNotEmpty(manItem.이름)){
                			
	                    		//생년월일하고 이름이 같을때
	                    		if(StringUtils.equals(manItem.개인식별번호.substring(0, 6), eData.getAttribute("suptFmlyBhdt").substring(2, 8))
	                    				&& StringUtils.equals(manItem.이름 , eData.getAttribute("suptFmlyFnm"))){
	
//	                    			manItem.신용카드_일반 += StringUtil.strPaserInt(eData.getAttribute("crdcUseAmt"));		//신용카드
	                    			manItem.현금영수증_일반 += StringUtil.strPaserInt(eData.getAttribute("cshptUseAmt"));		//현금영수증
//	                    			manItem.직불카드_일반 += StringUtil.strPaserInt(eData.getAttribute("drtpCardUseAmt"));		//직불,선불카드
	                    			
//	                    			manItem.신용카드_도서공연 += StringUtil.strPaserInt(eData.getAttribute("bppCrdcUseAmtSum"));		//신용카드등사용금액 도서공연사용분
//	                    			manItem.신용카드_전통시장 += StringUtil.strPaserInt(eData.getAttribute("tdmrUseAmt"));		//신용카드등사용금액 전통시장사용분
//	                    			manItem.신용카드_대중교통 += StringUtil.strPaserInt(eData.getAttribute("etcUseAmt"));		//신용카드등사용금액 대중교통이용분
	                    		
	                    			listMan.get(0).지출액합계 += StringUtil.strPaserInt(eData.getAttribute("cshptUseAmt"));
	                    		}
	                    		
                			}else if(StringUtils.equals("구분별 합계", manItem.이름 )){
                				
//                				manItem.신용카드_일반 += StringUtil.strPaserInt(eData.getAttribute("crdcUseAmt"));		//신용카드
                    			manItem.현금영수증_일반 += StringUtil.strPaserInt(eData.getAttribute("cshptUseAmt"));		//현금영수증
//                    			manItem.직불카드_일반 += StringUtil.strPaserInt(eData.getAttribute("drtpCardUseAmt"));		//직불,선불카드
                    			
//                    			manItem.신용카드_도서공연 += StringUtil.strPaserInt(eData.getAttribute("bppCrdcUseAmtSum"));		//신용카드등사용금액 도서공연사용분
//                    			manItem.신용카드_전통시장 += StringUtil.strPaserInt(eData.getAttribute("tdmrUseAmt"));		//신용카드등사용금액 전통시장사용분
//                    			manItem.신용카드_대중교통 += StringUtil.strPaserInt(eData.getAttribute("etcUseAmt"));		//신용카드등사용금액 대중교통이용분
                    			
                    			listMan.get(0).지출액합계 += StringUtil.strPaserInt(eData.getAttribute("cshptUseAmt"));
                			}

                		}
                    	
                    }
                    
        		}
        		
        	}
        }
    }


    /**
     *
     * A101Y-공제신고서
     *
     * @param listMan
     * @param listYE101 	: 주택임차차입금원리금상환액
     * @param listYE104	    : 장기주택저당차입금이자상환액
     * @param listYE403		: 교육비
     * @param dbSaveChk		: DB 처리여부 
     * @param listNode
     * @throws Exception
     */
    private void parseASum2(List<ManItem> listMan, List<YE101VO> listYE101, List<YE104VO> listYE104, List<YE403VO> listYE403, List<YE401VO> listYE401, boolean dbSaveChk, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resnoEncCntn = element.getAttribute("resnoEncCntn");
                String fnm = element.getAttribute("fnm");
//                YE001VO resultYE001VO = getID(resnoEncCntn, fnm);
//                
//                if (resultYE001VO == null) {
//                    return;
//                }
                
    	        logger.info("# pdf getId uniqueKey : " + SecurityUtil.getinstance().aesEncode(resnoEncCntn));
    	        logger.info("# pdf getId 계약ID : " + 계약ID );
    	        logger.info("# pdf getId 근로자_사용자ID : " + this.근로자_사용자ID );
                
                
                
                YE001VO paramVO = new YE001VO();
                paramVO.set계약ID(계약ID);
                paramVO.set사용자ID(this.근로자_사용자ID);
                paramVO.set개인식별번호(SecurityUtil.getinstance().aesEncode(resnoEncCntn));

                YE001VO resultYE001VO = ye001Service.getYE001ID(paramVO);   

                if(resultYE001VO != null){
                    
                	logger.debug("# 본인 대상자  : " + resultYE001VO.get사용자ID());
            		this.PDF본인_사용자ID = resultYE001VO.get사용자ID();
                	
                	 // 본인인 경우 부양가족 리스트 가져옴
                    YE001VO ye001 = new YE001VO();
                    ye001.set계약ID(계약ID);
                    ye001.set사용자ID(resultYE001VO.get사용자ID());
                    ye001.setStartPage(0);
                    ye001.setEndPage(99);
                    부양가족 = ye001Service.getYE001(ye001, userType);
                    
                }                
                ///////////////////////////////////////////////////////////////
                
                
                ManItem me = new ManItem();
                me.이름 = fnm;
                me.개인식별번호 = resnoEncCntn;
                listMan.add(me);

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;

                        /***** 2019.01.22 추가 */
                        // DB 등록 처리할때만 파싱한다.
                        if(dbSaveChk){
                        	
	                        YE001VO ye001 = null;
	                       
	                        if(StringUtils.isNotEmpty(eData.getAttribute("txprDscmNoCntn")) && StringUtils.isNotEmpty(eData.getAttribute("txprNm"))){
	                        	
	                        	if(ye001 == null){
	                         		ye001 = getID(eData.getAttribute("txprDscmNoCntn"), eData.getAttribute("txprNm"));
	                         	}
	                         	
	                         	//재검색해도 사용자 정보가 없는경우는 오류를 발생한다.
	                         	if(ye001 == null){
	                         		throw new Exception("[" + eData.getAttribute("txprNm") + "] 사용자 정보가 없습니다.");
	                         	}
	                         	
	                        }
	                        
	                        
	                        //보험료 
	                        //보장성보험 (납입금액)_국세청     
	                        if(StringUtils.isNotEmpty(eData.getAttribute("cvrgInscUseNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("cvrgInscUseNtsAmt")) > 0){
	                        	YE401VO vo = new YE401VO();
        	    	   			vo.setDbMode("C");
        	                    vo.set계약ID(계약ID);
        	                    vo.set사용자ID(ye001.get사용자ID());
        	                    vo.set부양가족ID(ye001.get부양가족ID());
        	                    vo.set자료구분코드("0");
        	                                        
        	                	vo.set보험구분코드("1");   // 보장성
        	                	vo.set납입금액(StringUtil.strPaserInt(eData.getAttribute("cvrgInscUseNtsAmt")));
        	                    vo.set차감금액(0);
        	
        	                    listYE401.add(vo);
	                        }
	                        //보장성보험 (납입금액)_기타자료
	                        if(StringUtils.isNotEmpty(eData.getAttribute("cvrgInscUseEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("cvrgInscUseEtcAmt")) > 0){
	                        	YE401VO vo = new YE401VO();
        	    	   			vo.setDbMode("C");
        	                    vo.set계약ID(계약ID);
        	                    vo.set사용자ID(ye001.get사용자ID());
        	                    vo.set부양가족ID(ye001.get부양가족ID());
        	                    vo.set자료구분코드("3");
        	                                        
        	                	vo.set보험구분코드("1");   // 보장성
        	                	vo.set납입금액(StringUtil.strPaserInt(eData.getAttribute("cvrgInscUseEtcAmt")));
        	                    vo.set차감금액(0);
        	
        	                    listYE401.add(vo);
	                        }
	                        //장애인전용보장성보험 (납입금액)_국세청
	                        if(StringUtils.isNotEmpty(eData.getAttribute("dsbrEuCvrgUseNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("dsbrEuCvrgUseNtsAmt")) > 0){
	                        	YE401VO vo = new YE401VO();
        	    	   			vo.setDbMode("C");
        	                    vo.set계약ID(계약ID);
        	                    vo.set사용자ID(ye001.get사용자ID());
        	                    vo.set부양가족ID(ye001.get부양가족ID());
        	                    vo.set자료구분코드("0");
        	                    
        	                    vo.set보험구분코드("2");   //장애인전용보장성보험
        	                    vo.set납입금액(StringUtil.strPaserInt(eData.getAttribute("dsbrEuCvrgUseNtsAmt")));
        	                    vo.set차감금액(0);
        	
        	                    listYE401.add(vo);
	                        }
	                        //장애인전용보장성보험 (납입금액)_기타자료
	                        if(StringUtils.isNotEmpty(eData.getAttribute("dsbrEuCvrgUseEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("dsbrEuCvrgUseEtcAmt")) > 0){
	                        	YE401VO vo = new YE401VO();
        	    	   			vo.setDbMode("C");
        	                    vo.set계약ID(계약ID);
        	                    vo.set사용자ID(ye001.get사용자ID());
        	                    vo.set부양가족ID(ye001.get부양가족ID());
        	                    vo.set자료구분코드("3");
        	                    
        	                    vo.set보험구분코드("2");   //장애인전용보장성보험
        	                    vo.set납입금액(StringUtil.strPaserInt(eData.getAttribute("dsbrEuCvrgUseEtcAmt")));
        	                    vo.set차감금액(0);
        	
        	                    listYE401.add(vo);
	                        }
	                        //보험료 end                     
	                        
	                        
	                        //교육비
	                        // 교육비-소득자본인 (납입금액)_국세청
	                        if(StringUtils.isNotEmpty(eData.getAttribute("scxpsPrsUseNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("scxpsPrsUseNtsAmt")) > 0){	                        		                         	
	                         	YE403VO ye403VO = new YE403VO();
	                        	ye403VO.setDbMode("C");
	                        	ye403VO.set계약ID(계약ID);
	                        	ye403VO.set사용자ID(ye001.get사용자ID());
	                        	ye403VO.set부양가족ID(ye001.get부양가족ID());
	                        	ye403VO.set자료구분코드("0");
	                        	ye403VO.set공제종류코드("1");
	                        	
	                        	ye403VO.set공납금(StringUtil.strPaserInt(eData.getAttribute("scxpsPrsUseNtsAmt")));
	                        	ye403VO.set공납금_차감금액(0);
		                        ye403VO.set체험학습비(0);
		                        ye403VO.set체험학습비_차감금액(0);
		                        ye403VO.set교복구입비(0);
		                        ye403VO.set교복구입비_차감금액(0);
		                        		                        	
		                        listYE403.add(ye403VO); 	                         	
	                        }
	                        //교육비-소득자본인 (납입금액)_기타자료
	                        if(StringUtils.isNotEmpty(eData.getAttribute("scxpsPrsUseEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("scxpsPrsUseEtcAmt")) > 0){
	                        	YE403VO ye403VO = new YE403VO();
	                        	ye403VO.setDbMode("C");
	                        	ye403VO.set계약ID(계약ID);
	                        	ye403VO.set사용자ID(ye001.get사용자ID());
	                        	ye403VO.set부양가족ID(ye001.get부양가족ID());
	                        	ye403VO.set자료구분코드("3");	                        	
	                        	ye403VO.set공제종류코드("1");
	                        	
	                        	ye403VO.set공납금(StringUtil.strPaserInt(eData.getAttribute("scxpsPrsUseEtcAmt")));
	                        	ye403VO.set공납금_차감금액(0);
		                        ye403VO.set체험학습비(0);
		                        ye403VO.set체험학습비_차감금액(0);
		                        ye403VO.set교복구입비(0);
		                        ye403VO.set교복구입비_차감금액(0);
		                        		                        	
		                        listYE403.add(ye403VO); 		                        	
	                        }
	                        //교육비-취학전아동 (납입금액)_국세청
	                        if(StringUtils.isNotEmpty(eData.getAttribute("scxpsKidUseNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("scxpsKidUseNtsAmt")) > 0){
	                        	YE403VO ye403VO = new YE403VO();
	                        	ye403VO.setDbMode("C");
	                        	ye403VO.set계약ID(계약ID);
	                        	ye403VO.set사용자ID(ye001.get사용자ID());
	                        	ye403VO.set부양가족ID(ye001.get부양가족ID());
	                        	ye403VO.set자료구분코드("0");	                        	
	                        	ye403VO.set공제종류코드("2");
	                        	
	                        	ye403VO.set공납금(StringUtil.strPaserInt(eData.getAttribute("scxpsKidUseNtsAmt")));
	                        	ye403VO.set공납금_차감금액(0);
		                        ye403VO.set체험학습비(0);
		                        ye403VO.set체험학습비_차감금액(0);
		                        ye403VO.set교복구입비(0);
		                        ye403VO.set교복구입비_차감금액(0);
		                        		                        	
		                        listYE403.add(ye403VO); 
	                        }
	                        //교육비-취학전아동 (납입금액)_기타자료
	                        if(StringUtils.isNotEmpty(eData.getAttribute("scxpsKidUseEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("scxpsKidUseEtcAmt")) > 0){
	                        	YE403VO ye403VO = new YE403VO();
	                        	ye403VO.setDbMode("C");
	                        	ye403VO.set계약ID(계약ID);
	                        	ye403VO.set사용자ID(ye001.get사용자ID());
	                        	ye403VO.set부양가족ID(ye001.get부양가족ID());
	                        	ye403VO.set자료구분코드("3");	                        	
	                        	ye403VO.set공제종류코드("2");
	                        	
	                        	ye403VO.set공납금(StringUtil.strPaserInt(eData.getAttribute("scxpsKidUseEtcAmt")));
	                        	ye403VO.set공납금_차감금액(0);
		                        ye403VO.set체험학습비(0);
		                        ye403VO.set체험학습비_차감금액(0);
		                        ye403VO.set교복구입비(0);
		                        ye403VO.set교복구입비_차감금액(0);
		                        		                        	
		                        listYE403.add(ye403VO); 
	                        }
	                        //교육비-초.중.고등학교 (납입금액)_국세청
	                        if(StringUtils.isNotEmpty(eData.getAttribute("scxpsStdUseNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("scxpsStdUseNtsAmt")) > 0){
	                        	YE403VO ye403VO = new YE403VO();
	                        	ye403VO.setDbMode("C");
	                        	ye403VO.set계약ID(계약ID);
	                        	ye403VO.set사용자ID(ye001.get사용자ID());
	                        	ye403VO.set부양가족ID(ye001.get부양가족ID());
	                        	ye403VO.set자료구분코드("0");	                        	
	                        	ye403VO.set공제종류코드("3");
	                        	
	                        	ye403VO.set공납금(StringUtil.strPaserInt(eData.getAttribute("scxpsStdUseNtsAmt")));
	                        	ye403VO.set공납금_차감금액(0);
		                        ye403VO.set체험학습비(0);
		                        ye403VO.set체험학습비_차감금액(0);
		                        ye403VO.set교복구입비(0);
		                        ye403VO.set교복구입비_차감금액(0);
		                        		                        	
		                        listYE403.add(ye403VO); 
	                        }
	                        //교육비-초.중.고등학교 (납입금액)_기타자료
	                        if(StringUtils.isNotEmpty(eData.getAttribute("scxpsStdUseEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("scxpsStdUseEtcAmt")) > 0){
	                        	YE403VO ye403VO = new YE403VO();
	                        	ye403VO.setDbMode("C");
	                        	ye403VO.set계약ID(계약ID);
	                        	ye403VO.set사용자ID(ye001.get사용자ID());
	                        	ye403VO.set부양가족ID(ye001.get부양가족ID());
	                        	ye403VO.set자료구분코드("3");	                        	
	                        	ye403VO.set공제종류코드("3");
	                        	
	                        	ye403VO.set공납금(StringUtil.strPaserInt(eData.getAttribute("scxpsStdUseEtcAmt")));
	                        	ye403VO.set공납금_차감금액(0);
		                        ye403VO.set체험학습비(0);
		                        ye403VO.set체험학습비_차감금액(0);
		                        ye403VO.set교복구입비(0);
		                        ye403VO.set교복구입비_차감금액(0);
		                        		                        	
		                        listYE403.add(ye403VO); 
	                        }
	                        //교복비-초.중.고등학교 (납입금액)_국세청
	                        if(StringUtils.isNotEmpty(eData.getAttribute("shufPrchUseNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("shufPrchUseNtsAmt")) > 0){
	                        	YE403VO ye403VO = new YE403VO();
	                        	ye403VO.setDbMode("C");
	                        	ye403VO.set계약ID(계약ID);
	                        	ye403VO.set사용자ID(ye001.get사용자ID());
	                        	ye403VO.set부양가족ID(ye001.get부양가족ID());
	                        	ye403VO.set자료구분코드("0");	                        	
	                        	ye403VO.set공제종류코드("3");
	                        	
	                        	ye403VO.set공납금(0);
	                        	ye403VO.set공납금_차감금액(0);
		                        ye403VO.set체험학습비(0);
		                        ye403VO.set체험학습비_차감금액(0);
		                        ye403VO.set교복구입비(StringUtil.strPaserInt(eData.getAttribute("shufPrchUseNtsAmt")));
		                        ye403VO.set교복구입비_차감금액(0);
		                        		                        	
		                        listYE403.add(ye403VO); 
	                        }
	                        //교복비-초.중.고등학교 (납입금액)_기타자료
	                        if(StringUtils.isNotEmpty(eData.getAttribute("shufPrchUseEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("shufPrchUseEtcAmt")) > 0){
	                        	YE403VO ye403VO = new YE403VO();
	                        	ye403VO.setDbMode("C");
	                        	ye403VO.set계약ID(계약ID);
	                        	ye403VO.set사용자ID(ye001.get사용자ID());
	                        	ye403VO.set부양가족ID(ye001.get부양가족ID());
	                        	ye403VO.set자료구분코드("3");	                        	
	                        	ye403VO.set공제종류코드("3");
	                        	
	                        	ye403VO.set공납금(0);
	                        	ye403VO.set공납금_차감금액(0);
		                        ye403VO.set체험학습비(0);		                        
		                        ye403VO.set체험학습비_차감금액(0);
		                        ye403VO.set교복구입비(StringUtil.strPaserInt(eData.getAttribute("shufPrchUseEtcAmt")));
		                        ye403VO.set교복구입비_차감금액(0);
		                        		                        	
		                        listYE403.add(ye403VO);
	                        }
	                        //체험학습비-초.중.고등학교 (납입금액)_국세청
	                        if(StringUtils.isNotEmpty(eData.getAttribute("exprnStudUseNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("exprnStudUseNtsAmt")) > 0){
	                        	YE403VO ye403VO = new YE403VO();
	                        	ye403VO.setDbMode("C");
	                        	ye403VO.set계약ID(계약ID);
	                        	ye403VO.set사용자ID(ye001.get사용자ID());
	                        	ye403VO.set부양가족ID(ye001.get부양가족ID());
	                        	ye403VO.set자료구분코드("0");	                        	
	                        	ye403VO.set공제종류코드("3");
	                        	
	                        	ye403VO.set공납금(0);
	                        	ye403VO.set공납금_차감금액(0);
		                        ye403VO.set체험학습비(StringUtil.strPaserInt(eData.getAttribute("exprnStudUseNtsAmt")));		                        
		                        ye403VO.set체험학습비_차감금액(0);
		                        ye403VO.set교복구입비(0);
		                        ye403VO.set교복구입비_차감금액(0);
		                        		                        	
		                        listYE403.add(ye403VO);
	                        }
	                        //체험학습비-초.중.고등학교 (납입금액)_기타자료
	                        if(StringUtils.isNotEmpty(eData.getAttribute("exprnStudUseEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("exprnStudUseEtcAmt")) > 0){
	                        	YE403VO ye403VO = new YE403VO();
	                        	ye403VO.setDbMode("C");
	                        	ye403VO.set계약ID(계약ID);
	                        	ye403VO.set사용자ID(ye001.get사용자ID());
	                        	ye403VO.set부양가족ID(ye001.get부양가족ID());
	                        	ye403VO.set자료구분코드("3");	                        	
	                        	ye403VO.set공제종류코드("3");
	                        	
	                        	ye403VO.set공납금(0);
	                        	ye403VO.set공납금_차감금액(0);
		                        ye403VO.set체험학습비(StringUtil.strPaserInt(eData.getAttribute("exprnStudUseEtcAmt")));		                        
		                        ye403VO.set체험학습비_차감금액(0);
		                        ye403VO.set교복구입비(0);
		                        ye403VO.set교복구입비_차감금액(0);
		                        
		                        listYE403.add(ye403VO);
	                        }
	                        //교육비-대학생(대학원불포함)(납입금액)_국세청
	                        if(StringUtils.isNotEmpty(eData.getAttribute("scxpsUndUseNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("scxpsUndUseNtsAmt")) > 0){
	                        	YE403VO ye403VO = new YE403VO();
	                        	ye403VO.setDbMode("C");
	                        	ye403VO.set계약ID(계약ID);
	                        	ye403VO.set사용자ID(ye001.get사용자ID());
	                        	ye403VO.set부양가족ID(ye001.get부양가족ID());
	                        	ye403VO.set자료구분코드("0");	                        	
	                        	ye403VO.set공제종류코드("4");
	                        	
	                        	ye403VO.set공납금(StringUtil.strPaserInt(eData.getAttribute("scxpsUndUseNtsAmt")));
	                        	ye403VO.set공납금_차감금액(0);
		                        ye403VO.set체험학습비(0);		                        
		                        ye403VO.set체험학습비_차감금액(0);
		                        ye403VO.set교복구입비(0);
		                        ye403VO.set교복구입비_차감금액(0);
		                        
		                        listYE403.add(ye403VO);
	                        }
	                        //교육비-대학생(대학원불포함)(납입금액)_기타자료
	                        if(StringUtils.isNotEmpty(eData.getAttribute("scxpsUndUseEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("scxpsUndUseEtcAmt")) > 0){
	                        	YE403VO ye403VO = new YE403VO();
	                        	ye403VO.setDbMode("C");
	                        	ye403VO.set계약ID(계약ID);
	                        	ye403VO.set사용자ID(ye001.get사용자ID());
	                        	ye403VO.set부양가족ID(ye001.get부양가족ID());
	                        	ye403VO.set자료구분코드("3");	                        	
	                        	ye403VO.set공제종류코드("4");
	                        	
	                        	ye403VO.set공납금(StringUtil.strPaserInt(eData.getAttribute("scxpsUndUseEtcAmt")));
	                        	ye403VO.set공납금_차감금액(0);
		                        ye403VO.set체험학습비(0);		                        
		                        ye403VO.set체험학습비_차감금액(0);
		                        ye403VO.set교복구입비(0);
		                        ye403VO.set교복구입비_차감금액(0);
		                        
		                        listYE403.add(ye403VO);
	                        }
	                        //교육비-장애인(납입금액)_국세청
	                        if(StringUtils.isNotEmpty(eData.getAttribute("scxpsDsbrUseNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("scxpsDsbrUseNtsAmt")) > 0){
	                        	YE403VO ye403VO = new YE403VO();
	                        	ye403VO.setDbMode("C");
	                        	ye403VO.set계약ID(계약ID);
	                        	ye403VO.set사용자ID(ye001.get사용자ID());
	                        	ye403VO.set부양가족ID(ye001.get부양가족ID());
	                        	ye403VO.set자료구분코드("0");	                        	
	                        	ye403VO.set공제종류코드("5");
	                        	
	                        	ye403VO.set공납금(StringUtil.strPaserInt(eData.getAttribute("scxpsDsbrUseNtsAmt")));
	                        	ye403VO.set공납금_차감금액(0);
		                        ye403VO.set체험학습비(0);		                        
		                        ye403VO.set체험학습비_차감금액(0);
		                        ye403VO.set교복구입비(0);
		                        ye403VO.set교복구입비_차감금액(0);
		                        
		                        listYE403.add(ye403VO);
	                        }
	                        //교육비-장애인(납입금액)_기타자료
	                        if(StringUtils.isNotEmpty(eData.getAttribute("scxpsDsbrUseEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("scxpsDsbrUseEtcAmt")) > 0){
	                        	YE403VO ye403VO = new YE403VO();
	                        	ye403VO.setDbMode("C");
	                        	ye403VO.set계약ID(계약ID);
	                        	ye403VO.set사용자ID(ye001.get사용자ID());
	                        	ye403VO.set부양가족ID(ye001.get부양가족ID());
	                        	ye403VO.set자료구분코드("3");	                        	
	                        	ye403VO.set공제종류코드("5");
	                        	
	                        	ye403VO.set공납금(StringUtil.strPaserInt(eData.getAttribute("scxpsDsbrUseEtcAmt")));
	                        	ye403VO.set공납금_차감금액(0);
		                        ye403VO.set체험학습비(0);		                        
		                        ye403VO.set체험학습비_차감금액(0);
		                        ye403VO.set교복구입비(0);
		                        ye403VO.set교복구입비_차감금액(0);
		                        
		                        listYE403.add(ye403VO);
	                        }
	                        /////교육비 end
	                        	                        	
	                        
	                        //주택임차차입금 대출기관차입 원리금상환액_국세청, 주택임차차입금 대출기관차입 원리금상환액_기타자료
	                        if((StringUtils.isNotEmpty(eData.getAttribute("brwOrgnBrwnHsngTennLnpbSrmNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("brwOrgnBrwnHsngTennLnpbSrmNtsAmt")) > 0)
	                        		|| (StringUtils.isNotEmpty(eData.getAttribute("brwOrgnBrwnHsngTennLnpbSrmEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("brwOrgnBrwnHsngTennLnpbSrmEtcAmt")) > 0) ){
	                        	
	                        	if(ye001 == null){
	                        		ye001 = getID(eData.getAttribute("txprDscmNoCntn"), eData.getAttribute("txprNm"));
	                        	}
	                        	
	                        	YE101VO ye101VO = new YE101VO();
	                        	ye101VO.setDbMode("C");
	                        	ye101VO.set계약ID(계약ID);
	                        	ye101VO.set사용자ID(ye001.get사용자ID());
	                        	ye101VO.set차입구분("1");		//대출기관
	                        	ye101VO.set국세청자료(StringUtil.strPaserInt(eData.getAttribute("brwOrgnBrwnHsngTennLnpbSrmNtsAmt")));
	                        	ye101VO.set차감금액(0);
	                        	ye101VO.set기타자료(StringUtil.strPaserInt(eData.getAttribute("brwOrgnBrwnHsngTennLnpbSrmEtcAmt")));
	                        	
	                        	listYE101.add(ye101VO);
	                        }
	   
	                        //주택임차차입금 거주자차입 원리금상환액_국세청, 주택임차차입금 거주자차입 원리금상환액_기타자료
	                        if((StringUtils.isNotEmpty(eData.getAttribute("rsdtBrwnHsngTennLnpbSrmNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("rsdtBrwnHsngTennLnpbSrmNtsAmt")) > 0)
	                        		|| (StringUtils.isNotEmpty(eData.getAttribute("rsdtBrwnHsngTennLnpbSrmEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("rsdtBrwnHsngTennLnpbSrmEtcAmt")) > 0) ){
	                        	
	                        	if(ye001 == null){
	                        		ye001 = getID(eData.getAttribute("txprDscmNoCntn"), eData.getAttribute("txprNm"));
	                        	}
	                        	
	                        	YE101VO ye101VO = new YE101VO();
	                        	ye101VO.setDbMode("C");
	                        	ye101VO.set계약ID(계약ID);
	                        	ye101VO.set사용자ID(ye001.get사용자ID());
	                        	ye101VO.set차입구분("2");		//거주자차입
	                        	ye101VO.set국세청자료(StringUtil.strPaserInt(eData.getAttribute("rsdtBrwnHsngTennLnpbSrmNtsAmt")));
	                        	ye101VO.set차감금액(0);
	                        	ye101VO.set기타자료(StringUtil.strPaserInt(eData.getAttribute("rsdtBrwnHsngTennLnpbSrmEtcAmt")));
	                        	
	                        	listYE101.add(ye101VO);                        	
	                        }
	                        
	                        
	                        //장기주택저당차입금 2011년 이전 차입분 중 15년 미만 이자상환액_국세청, 장기주택저당차입금 2011년 이전 차입분 중 15년 미만 이자상환액_기타자료 (600만원)
	                        if((StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr15BlwItrNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr15BlwItrNtsAmt")) > 0)
	                        		|| (StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr15BlwItrEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr15BlwItrEtcAmt")) > 0) ){
	                        	
	                        	if(ye001 == null){
	                        		ye001 = getID(eData.getAttribute("txprDscmNoCntn"), eData.getAttribute("txprNm"));
	                        	}
	                        	
	                        	YE104VO ye104VO = new YE104VO();
	                            
	                        	ye104VO.setDbMode("C");
	                        	ye104VO.set계약ID(계약ID);
	                        	ye104VO.set사용자ID(ye001.get사용자ID());
	                        	ye104VO.set구분코드("1");
	                        	ye104VO.set국세청자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr15BlwItrNtsAmt")));
	                        	ye104VO.set차감금액(0);
	                        	ye104VO.set기타자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr15BlwItrEtcAmt")));
	                            
	                            listYE104.add(ye104VO);                        	
	                        }
	                        
	                        //장기주택저당자입금 2011년 이전 차입분 중 15년~ 29년  이자상환액_국세청, 장기주택저당자입금 2011년 이전 차입분 중 15년~ 29년  이자상환액_기타자료 (1000만원)
	                        if((StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr29ItrNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr29ItrNtsAmt")) > 0)
	                        		|| (StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr29ItrEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr29ItrEtcAmt")) > 0) ){
	                        	
	                        	if(ye001 == null){
	                        		ye001 = getID(eData.getAttribute("txprDscmNoCntn"), eData.getAttribute("txprNm"));
	                        	}
	                        	
	                        	YE104VO ye104VO = new YE104VO();
	                            
	                        	ye104VO.setDbMode("C");
	                        	ye104VO.set계약ID(계약ID);
	                        	ye104VO.set사용자ID(ye001.get사용자ID());
	                        	ye104VO.set구분코드("2");
	                        	ye104VO.set국세청자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr29ItrNtsAmt")));
	                        	ye104VO.set차감금액(0);
	                        	ye104VO.set기타자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr29ItrEtcAmt")));
	                            
	                            listYE104.add(ye104VO);                        
	                        }
	                        
	                        //장기주택저당자입금 2011년 이전 차입분 중 30년 이상  이자상환액_국세청, 장기주택저당자입금 2011년 이전 차입분 중 30년 이상  이자상환액_기타자료 (1500만원)
	                        if((StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbY30OverItrNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbY30OverItrNtsAmt")) > 0)
	                        		|| (StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbY30OverItrEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbY30OverItrEtcAmt")) > 0) ){
	                        	
	                        	if(ye001 == null){
	                        		ye001 = getID(eData.getAttribute("txprDscmNoCntn"), eData.getAttribute("txprNm"));
	                        	}
	                        	
	                        	YE104VO ye104VO = new YE104VO();
	                            
	                        	ye104VO.setDbMode("C");
	                        	ye104VO.set계약ID(계약ID);
	                        	ye104VO.set사용자ID(ye001.get사용자ID());
	                        	ye104VO.set구분코드("3");
	                        	ye104VO.set국세청자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbY30OverItrNtsAmt")));
	                        	ye104VO.set차감금액(0);
	                        	ye104VO.set기타자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbY30OverItrEtcAmt")));
	                            
	                            listYE104.add(ye104VO);                        	
	                        }
	                                                
	                        //장기주택저당자입금 2012년 이후 고정금리이거나 비거치 상환대출 이자상환액_국세청, 장기주택저당자입금 2012년 이후 고정금리이거나 비거치 상환대출 이자상환액_기타자료 (1500만원)
	                        if((StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr2012AfthY15OverItrNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2012AfthY15OverItrNtsAmt")) > 0)
	                        		|| (StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr2012AfthY15OverItrEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2012AfthY15OverItrEtcAmt")) > 0) ){
	                        	
	                        	if(ye001 == null){
	                        		ye001 = getID(eData.getAttribute("txprDscmNoCntn"), eData.getAttribute("txprNm"));
	                        	}
	                        	
	                        	YE104VO ye104VO = new YE104VO();
	                            
	                        	ye104VO.setDbMode("C");
	                        	ye104VO.set계약ID(계약ID);
	                        	ye104VO.set사용자ID(ye001.get사용자ID());
	                        	ye104VO.set구분코드("4");
	                        	ye104VO.set국세청자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2012AfthY15OverItrNtsAmt")));
	                        	ye104VO.set차감금액(0);
	                        	ye104VO.set기타자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2012AfthY15OverItrEtcAmt")));
	                            
	                            listYE104.add(ye104VO);                        	
	                        }
	                        
	                        //장기주택저당자입금 2012년 이후 기타대출 이자상환액_국세청, 장기주택저당자입금 2012년 이후 기타대출 이자상환액_기타자료 (500만원)
	                        if((StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr2012EtcBrwItrNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2012EtcBrwItrNtsAmt")) > 0)
	                        		|| (StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr2012EtcBrwItrEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2012EtcBrwItrEtcAmt")) > 0) ){
	                        	
	                        	if(ye001 == null){
	                        		ye001 = getID(eData.getAttribute("txprDscmNoCntn"), eData.getAttribute("txprNm"));
	                        	}
	                        	
	                        	YE104VO ye104VO = new YE104VO();
	                            
	                        	ye104VO.setDbMode("C");
	                        	ye104VO.set계약ID(계약ID);
	                        	ye104VO.set사용자ID(ye001.get사용자ID());
	                        	ye104VO.set구분코드("5");
	                        	ye104VO.set국세청자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2012EtcBrwItrNtsAmt")));
	                        	ye104VO.set차감금액(0);
	                        	ye104VO.set기타자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2012EtcBrwItrEtcAmt")));
	                            
	                            listYE104.add(ye104VO);                        	
	                        }
	                        
	                        //장기주택저당차입금 2015년 이후(15년이상) 고정금리이면서 비거치상환대출 이자상환액_국세청, 장기주택저당차입금 2015년 이후(15년이상) 고정금리이면서 비거치상환대출 이자상환액_기타자료 (1800만원)
	                        if((StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr2015AfthFxnIrItrNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthFxnIrItrNtsAmt")) > 0)
	                        		|| (StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr2015AfthFxnIrItrEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthFxnIrItrEtcAmt")) > 0) ){
	                        	
	                        	if(ye001 == null){
	                        		ye001 = getID(eData.getAttribute("txprDscmNoCntn"), eData.getAttribute("txprNm"));
	                        	}
	                        	
	                        	YE104VO ye104VO = new YE104VO();
	                            
	                        	ye104VO.setDbMode("C");
	                        	ye104VO.set계약ID(계약ID);
	                        	ye104VO.set사용자ID(ye001.get사용자ID());
	                        	ye104VO.set구분코드("6");
	                        	ye104VO.set국세청자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthFxnIrItrNtsAmt")));
	                        	ye104VO.set차감금액(0);
	                        	ye104VO.set기타자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthFxnIrItrEtcAmt")));
	                            
	                            listYE104.add(ye104VO);                        	
	                        }
	                        
	                        //장기주택저당차입금 2015년 이후(15년이상) 고정금리이거나 비거치상환대출 이자상환액_국세청, 장기주택저당차입금 2015년 이후(15년이상) 고정금리이거나 비거치상환대출 이자상환액_기타자료 (1500만원)
	                        if((StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr2015AfthY15OverItrAmtItrNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthY15OverItrAmtItrNtsAmt")) > 0)
	                        		|| (StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr2015AfthY15OverItrAmtItrEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthY15OverItrAmtItrEtcAmt")) > 0) ){
	                        	
	                        	if(ye001 == null){
	                        		ye001 = getID(eData.getAttribute("txprDscmNoCntn"), eData.getAttribute("txprNm"));
	                        	}
	                        	
	                        	YE104VO ye104VO = new YE104VO();
	                            
	                        	ye104VO.setDbMode("C");
	                        	ye104VO.set계약ID(계약ID);
	                        	ye104VO.set사용자ID(ye001.get사용자ID());
	                        	ye104VO.set구분코드("7");
	                        	ye104VO.set국세청자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthY15OverItrAmtItrNtsAmt")));
	                        	ye104VO.set차감금액(0);
	                        	ye104VO.set기타자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthY15OverItrAmtItrEtcAmt")));
	                            
	                            listYE104.add(ye104VO);                        	
	                        }
	                        
	                        //장기주택저당차입금 2015년 이후(15년이상) 기타대출 이자상환액_국세청, 장기주택저당차입금 2015년 이후(15년이상) 기타대출 이자상환액_기타자료 (500만원)
	                        if((StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr2015AfthEtcBrwItrNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthEtcBrwItrNtsAmt")) > 0)
	                        		|| (StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr2015AfthEtcBrwItrEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthEtcBrwItrEtcAmt")) > 0) ){
	                        	
	                        	if(ye001 == null){
	                        		ye001 = getID(eData.getAttribute("txprDscmNoCntn"), eData.getAttribute("txprNm"));
	                        	}
	                        	
	                        	YE104VO ye104VO = new YE104VO();
	                            
	                        	ye104VO.setDbMode("C");
	                        	ye104VO.set계약ID(계약ID);
	                        	ye104VO.set사용자ID(ye001.get사용자ID());
	                        	ye104VO.set구분코드("8");
	                        	ye104VO.set국세청자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthEtcBrwItrNtsAmt")));
	                        	ye104VO.set차감금액(0);
	                        	ye104VO.set기타자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthEtcBrwItrEtcAmt")));
	                            
	                            listYE104.add(ye104VO);                        	
	                        }
	                        
	                        //장기주택저당차입금 2015년 이후(10년~15년) 고정금리이거나 비거치상환대출 이자상환액_국세청, 장기주택저당차입금 2015년 이후(10년~15년) 고정금리이거나 비거치상환대출 이자상환액_기타자료 (300만원)
	                        if((StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr2015AfthYr15BlwItrNtsAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthYr15BlwItrNtsAmt")) > 0)
	                        		|| (StringUtils.isNotEmpty(eData.getAttribute("lthClrlLnpbYr2015AfthYr15BlwItrEtcAmt")) && StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthYr15BlwItrEtcAmt")) > 0) ){
	                        	
	                        	if(ye001 == null){
	                        		ye001 = getID(eData.getAttribute("txprDscmNoCntn"), eData.getAttribute("txprNm"));
	                        	}
	                        	
	                        	YE104VO ye104VO = new YE104VO();
	                            
	                        	ye104VO.setDbMode("C");
	                        	ye104VO.set계약ID(계약ID);
	                        	ye104VO.set사용자ID(ye001.get사용자ID());
	                        	ye104VO.set구분코드("9");
	                        	ye104VO.set국세청자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthYr15BlwItrNtsAmt")));
	                        	ye104VO.set차감금액(0);
	                        	ye104VO.set기타자료(StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthYr15BlwItrEtcAmt")));
	                            
	                            listYE104.add(ye104VO);                        	
	                        }
                                                
                        }
                        
                        /* 2019.01.22 추가 *****/
                        
                        // 연금 보험료별 반복 구간
                        String strNpHthrWaInfeeAmt = eData.getAttribute("npHthrWaInfeeAmt");
                        // 특별 소득 공제별 반복 구간
                        String strHthrHifeAmt = eData.getAttribute("hthrHifeAmt");
                        // 그밖의 소득 공제별 반복 구간
                        String strYr2000BefNtplPnsnSvngUseAmt = eData.getAttribute("yr2000BefNtplPnsnSvngUseAmt");
                        // 세액 감면 및 공제별 반복 구간
                        String strSctcHpUseAmt = eData.getAttribute("sctcHpUseAmt");
                        // 국세청, 기타자료 항목구분 개별 반복 구간
                        String txprDscmNoCntn = eData.getAttribute("txprDscmNoCntn");

                        // 연금 보험료별 반복 구간
                        if (strNpHthrWaInfeeAmt != null) {
                            // 국민연금 종근무지 납입금액
                            int npHthrWaInfeeAmt = StringUtil.strPaserInt(strNpHthrWaInfeeAmt);
                            // 국민연금 주근무지 납입금액
                            int npHthrMcurWkarInfeeAmt = StringUtil.strPaserInt(eData.getAttribute("npHthrMcurWkarInfeeAmt"));

                            int 국민연금 = npHthrWaInfeeAmt + npHthrMcurWkarInfeeAmt;

                            // 본인
                            listMan.get(1).국민연금_직장 += 국민연금;
                            listMan.get(1).지출액합계 += 국민연금;

                            // 합계
                            listMan.get(0).국민연금_직장 += 국민연금;
                            listMan.get(0).지출액합계 += 국민연금;
                        }
                        
                        //국민연금보험료 외 공적연금 종(전)근무지보험료 금액
                        String strHthrPblcPnsnInfeeAmt = eData.getAttribute("hthrPblcPnsnInfeeAmt");
                        //국민연금보험료 외 공적연금 주(현)근무지보험료 금액
                        String strMcurPblcPnsnInfeeAmt = eData.getAttribute("mcurPblcPnsnInfeeAmt");
                        
                        if(StringUtils.isNotEmpty(strMcurPblcPnsnInfeeAmt)){
                        	
                        	int hthrPblcPnsnInfeeAmt = StringUtil.strPaserInt(strHthrPblcPnsnInfeeAmt);
                        	int mcurPblcPnsnInfeeAmt = StringUtil.strPaserInt(strMcurPblcPnsnInfeeAmt);
                        	int 국민연금외_공적연금 = hthrPblcPnsnInfeeAmt + mcurPblcPnsnInfeeAmt;
                        	
                        	// 본인
                        	listMan.get(1).국민연금외_공적연금 += 국민연금외_공적연금;
                        	listMan.get(1).지출액합계 += 국민연금외_공적연금;       
                        	// 합계
                        	listMan.get(0).국민연금외_공적연금 += 국민연금외_공적연금;
                            listMan.get(0).지출액합계 += 국민연금외_공적연금;
                        }


                        // 특별 소득 공제별 반복 구간
                        if (strHthrHifeAmt != null) {
                            // 건강보험 종근무지 납입금액
                            int hthrHifeAmt = StringUtil.strPaserInt(strHthrHifeAmt);
                            // 건강보험 주근무지 납입금액
                            int mcurHifeAmt = StringUtil.strPaserInt(eData.getAttribute("mcurHifeAmt"));

                            // 주택임차차입금 대출기관차입 원리금상환액
                            int brwOrgnBrwnHsngTennLnpbSrmAmt = StringUtil.strPaserInt(eData.getAttribute("brwOrgnBrwnHsngTennLnpbSrmAmt"));
                            // 주택임차차입금 거주자차입 원리금상환액
                            int rsdtBrwnHsngTennLnpbSrmAmt = StringUtil.strPaserInt(eData.getAttribute("rsdtBrwnHsngTennLnpbSrmAmt"));
                            // 장기주택저당차입금 2011년 이전 차입분 중 15년 미만 이자상환액
                            int lthClrlLnpbYr15BlwItrAmt = StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr15BlwItrAmt"));
                            // 장기주택저당자입금 2011년 이전 차입분 중 15년~ 29년  이자상환액
                            int lthClrlLnpbYr29ItrAmt = StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr29ItrAmt"));
                            // 장기주택저당자입금 2011년 이전 차입분 중 30년 이상  이자상환액
                            int lthClrlLnpbY30OverItrAmt = StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbY30OverItrAmt"));
                            // 장기주택저당자입금 2012년 이후 고정금리이거나 비거치 상환대출 이자상환액
                            int lthClrlLnpbYr2012AfthY15OverItrAmt = StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2012AfthY15OverItrAmt"));
                            // 장기주택저당자입금 2012년 이후 기타대출 이자상환액
                            int lthClrlLnpbYr2012EtcBrwItrAmt = StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2012EtcBrwItrAmt"));
                            // 장기주택저당차입금 2015년 이후(15년이상) 고정금리이면서 비거치상환대출 이자상환액
                            int lthClrlLnpbYr2015AfthFxnIrItrAmt = StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthFxnIrItrAmt"));
                            // 장기주택저당차입금 2015년 이후(15년이상) 고정금리이거나 비거치상환대출 이자상환액
                            int lthClrlLnpbYr2015AfthY15OverItrAmtItrAmt = StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthY15OverItrAmtItrAmt"));
                            // 장기주택저당차입금 2015년 이후(15년이상) 기타대출 이자상환액
                            int lthClrlLnpbYr2015AfthEtcBrwItrAmt = StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthEtcBrwItrAmt"));
                            // 장기주택저당차입금 2015년 이후(10년~15년) 고정금리이거나 비거치상환대출 이자상환액
                            int lthClrlLnpbYr2015AfthYr15BlwItrAmt = StringUtil.strPaserInt(eData.getAttribute("lthClrlLnpbYr2015AfthYr15BlwItrAmt"));

                            int 건강보험 = hthrHifeAmt + mcurHifeAmt;
                            int 주택자금 = brwOrgnBrwnHsngTennLnpbSrmAmt + rsdtBrwnHsngTennLnpbSrmAmt +
                                    lthClrlLnpbYr15BlwItrAmt + lthClrlLnpbYr29ItrAmt + lthClrlLnpbY30OverItrAmt +
                                    lthClrlLnpbYr2012AfthY15OverItrAmt + lthClrlLnpbYr2012EtcBrwItrAmt +
                                    lthClrlLnpbYr2015AfthFxnIrItrAmt + lthClrlLnpbYr2015AfthY15OverItrAmtItrAmt + lthClrlLnpbYr2015AfthEtcBrwItrAmt + lthClrlLnpbYr2015AfthYr15BlwItrAmt;

                            // 본인
                            listMan.get(1).건강보험_보수월액 += 건강보험;
                            listMan.get(1).주택자금 += 주택자금;
                            listMan.get(1).지출액합계 += 건강보험 + 주택자금;

                            // 합계
                            listMan.get(0).건강보험_보수월액 += 건강보험;
                            listMan.get(0).주택자금 += 주택자금;
                            listMan.get(0).지출액합계 += 건강보험 + 주택자금;
                        }

                        // 그밖의 소득 공제별 반복 구간
                        if (strYr2000BefNtplPnsnSvngUseAmt != null) {
                            // 개인연금저축(2000년 이전 가입) 납입금액
                            int yr2000BefNtplPnsnSvngUseAmt = StringUtil.strPaserInt(strYr2000BefNtplPnsnSvngUseAmt);
                            // 소기업 소상인 공제부금 납입금액
                            int smceSbizUseAmt = StringUtil.strPaserInt(eData.getAttribute("smceSbizUseAmt"));
                            // 주택마련저축 소득공제 합계
                            int hsngPrptSvngIncUseAmtSum = StringUtil.strPaserInt(eData.getAttribute("hsngPrptSvngIncUseAmtSum"));
                            // 장기집합투자증권저축 납입금액
                            int ltrmCniSsUseAmt = StringUtil.strPaserInt(eData.getAttribute("ltrmCniSsUseAmt"));

                            // 본인
                            listMan.get(1).개인연금저축_연금저축_퇴직연금 += yr2000BefNtplPnsnSvngUseAmt;
                            listMan.get(1).소기업소상공인공제부금 += smceSbizUseAmt;
                            listMan.get(1).주택마련저축 += hsngPrptSvngIncUseAmtSum;
                            listMan.get(1).장기집합투자증권저축 += ltrmCniSsUseAmt;
                            listMan.get(1).지출액합계 += yr2000BefNtplPnsnSvngUseAmt + smceSbizUseAmt + hsngPrptSvngIncUseAmtSum + ltrmCniSsUseAmt;

                            // 합계
                            listMan.get(0).개인연금저축_연금저축_퇴직연금 += yr2000BefNtplPnsnSvngUseAmt;
                            listMan.get(0).소기업소상공인공제부금 += smceSbizUseAmt;
                            listMan.get(0).주택마련저축 += hsngPrptSvngIncUseAmtSum;
                            listMan.get(0).장기집합투자증권저축 += ltrmCniSsUseAmt;
                            listMan.get(0).지출액합계 += yr2000BefNtplPnsnSvngUseAmt + smceSbizUseAmt + hsngPrptSvngIncUseAmtSum + ltrmCniSsUseAmt;
                        }

                        // 세액 감면 및 공제별 반복 구간
                        if (strSctcHpUseAmt != null) {
                            // 연금계좌-과학기술인공제 (납입금액)
                            int sctcHpUseAmt = StringUtil.strPaserInt(strSctcHpUseAmt);
                            // 연금계좌-과학기술인공제 (납입금액)
                            int rtpnUseAmt = StringUtil.strPaserInt(eData.getAttribute("rtpnUseAmt"));
                            // 연금계좌-과학기술인공제 (납입금액)
                            int pnsnSvngUseAmt = StringUtil.strPaserInt(eData.getAttribute("pnsnSvngUseAmt"));

                            // 본인
                            listMan.get(1).개인연금저축_연금저축_퇴직연금 += sctcHpUseAmt + rtpnUseAmt + pnsnSvngUseAmt;
                            listMan.get(1).지출액합계 += sctcHpUseAmt + rtpnUseAmt + pnsnSvngUseAmt;

                            // 합계
                            listMan.get(0).개인연금저축_연금저축_퇴직연금 += sctcHpUseAmt + rtpnUseAmt + pnsnSvngUseAmt;
                            listMan.get(0).지출액합계 += sctcHpUseAmt + rtpnUseAmt + pnsnSvngUseAmt;
                        }

                        // 국세청, 기타자료 항목구분 개별 반복 구간
                        if (StringUtils.isNotEmpty(txprDscmNoCntn)) {
                            // 성명
                            String txprNm = eData.getAttribute("txprNm");

                            // 보장성보험 (납입금액)_국세청
                            int cvrgInscUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("cvrgInscUseNtsAmt"));
                            // 보장성보험 (납입금액)_기타자료
                            int cvrgInscUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("cvrgInscUseEtcAmt"));

                            // 장애인전용보장성보험 (납입금액)_국세청
                            int dsbrEuCvrgUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("dsbrEuCvrgUseNtsAmt"));
                            // 장애인전용보장성보험 (납입금액)_기타자료
                            int dsbrEuCvrgUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("dsbrEuCvrgUseEtcAmt"));

                            // 의료비-본인.65세이상자.장애인 (납입금액)_국세청
                            int mdxpsPrsUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("mdxpsPrsUseNtsAmt"));
                            // 의료비-본인.65세이상자.장애인 (납입금액)_기타자료
                            int mdxpsPrsUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("mdxpsPrsUseEtcAmt"));
                            // 의료비-난임시술비 (납입금액)_국세청
                            int mdxpsSftSprcdXpnsNtsAmt = StringUtil.strPaserInt(eData.getAttribute("mdxpsSftSprcdXpnsNtsAmt"));
                            // 의료비-난임시술비 (납입금액)_기타자료
                            int mdxpsSftSprcdXpnsEtcAmt = StringUtil.strPaserInt(eData.getAttribute("mdxpsSftSprcdXpnsEtcAmt"));
                            // 의료비-그 밖의 공제대상자 (납입금액)_국세청
                            int mdxpsOthUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("mdxpsOthUseNtsAmt"));
                            // 의료비-그 밖의 공제대상자 (납입금액)_기타자료
                            int mdxpsOthUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("mdxpsOthUseEtcAmt"));

                            // 의료비-안경구입비_국세청
                            int glssPrchNtsAmt = StringUtil.strPaserInt(eData.getAttribute("glssPrchNtsAmt"));
                            // 의료비-안경구입비_기타자료
                            int glssPrchEtcAmt = StringUtil.strPaserInt(eData.getAttribute("glssPrchEtcAmt"));

                            // 교육비-소득자본인 (납입금액)_국세청
                            int scxpsPrsUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("scxpsPrsUseNtsAmt"));
                            // 교육비-소득자본인 (납입금액)_기타자료
                            int scxpsPrsUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("scxpsPrsUseEtcAmt"));
                            // 교육비-취학전아동 (납입금액)_국세청
                            int scxpsKidUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("scxpsKidUseNtsAmt"));
                            // 교육비-취학전아동 (납입금액)_기타자료
                            int scxpsKidUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("scxpsKidUseEtcAmt"));
                            // 교육비-초.중.고등학교 (납입금액)_국세청
                            int scxpsStdUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("scxpsStdUseNtsAmt"));
                            // 교육비-초.중.고등학교 (납입금액)_기타자료
                            int scxpsStdUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("scxpsStdUseEtcAmt"));
                            // 교육비-대학생(대학원불포함)(납입금액)_국세청
                            int scxpsUndUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("scxpsUndUseNtsAmt"));
                            // 교육비-대학생(대학원불포함)(납입금액)_기타자료
                            int scxpsUndUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("scxpsUndUseEtcAmt"));
                            // 교육비-장애인(납입금액)_국세청
                            int scxpsDsbrUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("scxpsDsbrUseNtsAmt"));
                            // 교육비-장애인(납입금액)_기타자료
                            int scxpsDsbrUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("scxpsDsbrUseEtcAmt"));

                            // 교복비-초.중.고등학교 (납입금액)_국세청
                            int shufPrchUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("shufPrchUseNtsAmt"));
                            // 교복비-초.중.고등학교 (납입금액)_기타자료
                            int shufPrchUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("shufPrchUseEtcAmt"));

                            // 체험학습비-초.중.고등학교 (납입금액)_국세청
                            int exprnStudUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("exprnStudUseNtsAmt"));
                            // 체험학습비-초.중.고등학교 (납입금액)_기타자료
                            int exprnStudUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("exprnStudUseEtcAmt"));

                            // 기부금-정치자금 기부금액(납입금액)_국세청
                            int conbPltcFndsUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("conbPltcFndsUseNtsAmt"));
                            // 기부금-정치자금 기부금액(납입금액)_기타자료
                            int conbPltcFndsUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("conbPltcFndsUseEtcAmt"));
                            // 기부금-법정기부금(납입금액)_국세청
                            int conbLglUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("conbLglUseNtsAmt"));
                            // 기부금-법정기부금(납입금액)_기타자료
                            int conbLglUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("conbLglUseEtcAmt"));
                            // 기부금-우리사주조합기부금(납입금액)_국세청
                            int conbEmstAsctUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("conbEmstAsctUseNtsAmt"));
                            // 기부금-우리사주조합기부금(납입금액)_기타자료
                            int conbEmstAsctUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("conbEmstAsctUseEtcAmt"));
                            // 기부금-지정기부금(종교단체) 기부금액_국세청
                            int conbReliOrgAppnUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("conbReliOrgAppnUseNtsAmt"));
                            // 기부금-지정기부금(종교단체) 기부금액_기타자료
                            int conbReliOrgAppnUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("conbReliOrgAppnUseEtcAmt"));
                            // 기부금-지정기부금(종교단체외) 기부금액_국세청
                            int conbReliOrgOthAppnUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("conbReliOrgOthAppnUseNtsAmt"));
                            // 기부금-지정기부금(종교단체외) 기부금액_기타자료
                            int conbReliOrgOthAppnUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("conbReliOrgOthAppnUseEtcAmt"));

                            
                            YE001VO ye001VO = getID(txprDscmNoCntn, txprNm);
                            if (ye001VO == null) {
                                continue;
                            }
               
                            
                            /******* 신용카드, 직불카드, 현금영수증 ******************/
                            //신용카드(전통시장,대중교통제외) 사용금액_국세청
                            int crdcUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("crdcUseNtsAmt"));
                            //신용카드(전통시장,대중교통제외) 사용금액_기타자료
                            int crdcUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("crdcUseEtcAmt"));
                            //직불·선불카드(전통시장·대중교통 사용분 제외)_국세청
                            int drtpCardUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("drtpCardUseNtsAmt"));
                            //직불·선불카드(전통시장·대중교통 사용분 제외)_기타자료
                            int drtpCardUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("drtpCardUseEtcAmt"));                            
                            //도서공연비(신용카드)_국세청
                            int bppCrdcUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("bppCrdcUseNtsAmt"));
                            //도서공연비(신용카드)_기타자료
                            int bppCrdcUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("bppCrdcUseEtcAmt"));
                            //도서공연비(직불카드)_국세청
                            int bppDrtpCardUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("bppDrtpCardUseNtsAmt"));
                            //도서공연비(직불카드)_기타자료
                            int bppDrtpCardUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("bppDrtpCardUseEtcAmt"));
                            //도서공연비(현금영수증)_국세청
                            int bppCshptUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("bppCshptUseNtsAmt"));
                            //도서공연비(현금영수증)_기타자료
                            int bppCshptUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("bppCshptUseEtcAmt"));                                                        
                            //전통시장사용분_국세청
                            int tdmrUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("tdmrUseNtsAmt"));
                            //전통시장사용분_기타자료
                            int tdmrUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("tdmrUseEtcAmt"));
                            //대중교통이용분_국세청
                            int pbtUseNtsAmt = StringUtil.strPaserInt(eData.getAttribute("pbtUseNtsAmt"));
                            //대중교통이용분_기타자료
                            int pbtUseEtcAmt = StringUtil.strPaserInt(eData.getAttribute("pbtUseEtcAmt"));                                                        
                            /******* 신용카드, 직불카드, 현금영수증 ******************/
                            
//                            logger.debug("# 부양가족: " + ye001VO.get사용자ID());

                            int 보험료_일반보장성 = cvrgInscUseNtsAmt + cvrgInscUseEtcAmt;
                            int 보험료_장애인보장성 = dsbrEuCvrgUseNtsAmt + dsbrEuCvrgUseEtcAmt;

                            int 의료비_의료비 = mdxpsPrsUseNtsAmt + mdxpsPrsUseEtcAmt + mdxpsSftSprcdXpnsNtsAmt + mdxpsSftSprcdXpnsEtcAmt + mdxpsOthUseNtsAmt + mdxpsOthUseEtcAmt;
                            int 의료비_안경구입비 = glssPrchNtsAmt + glssPrchEtcAmt;

                            int 교육비_교육비_직업훈련비 = scxpsPrsUseNtsAmt + scxpsPrsUseEtcAmt + scxpsKidUseNtsAmt + scxpsKidUseEtcAmt + scxpsStdUseNtsAmt + scxpsStdUseEtcAmt + scxpsUndUseNtsAmt + scxpsUndUseEtcAmt + scxpsDsbrUseNtsAmt + scxpsDsbrUseEtcAmt;
                            int 교육비_교복구입비 = shufPrchUseNtsAmt + shufPrchUseEtcAmt;
                            int 교육비_체험학습비 = exprnStudUseNtsAmt + exprnStudUseEtcAmt;

                            int 기부금_공제대상 = conbPltcFndsUseNtsAmt + conbPltcFndsUseEtcAmt + conbLglUseNtsAmt + conbLglUseEtcAmt + conbEmstAsctUseNtsAmt + conbEmstAsctUseEtcAmt + conbReliOrgAppnUseNtsAmt + conbReliOrgAppnUseEtcAmt + conbReliOrgOthAppnUseNtsAmt + conbReliOrgOthAppnUseEtcAmt;

                            boolean find = false;
                            for (ManItem man : listMan) {
                                if (txprDscmNoCntn.equals(man.개인식별번호)) {
                                    find = true;
                                    man.보험료_일반보장성 += 보험료_일반보장성;
                                    man.보험료_장애인보장성 += 보험료_장애인보장성;
                                    man.의료비_의료비 += 의료비_의료비;
                                    man.의료비_안경구입비 += 의료비_안경구입비;
                                    man.교육비_교육비_직업훈련비 += 교육비_교육비_직업훈련비;
                                    man.교육비_교복구입비 += 교육비_교복구입비;
                                    man.교육비_체험학습비 += 교육비_체험학습비;
                                    man.기부금_공제대상 += 기부금_공제대상;
//                                    man.지출액합계 += 보험료_일반보장성 + 보험료_장애인보장성 + 의료비_의료비 + 의료비_안경구입비 + 교육비_교육비_직업훈련비 + 교육비_교복구입비 + 교육비_체험학습비 + 기부금_공제대상;
                                    
                                    /******* 신용카드, 직불카드, 현금영수증 ******************/                                    
                                    man.신용카드_일반 += (crdcUseNtsAmt + crdcUseEtcAmt);
                                    man.직불카드_일반 += (drtpCardUseNtsAmt + drtpCardUseEtcAmt);
                                    
                                    man.신용카드_도서공연 += (bppCrdcUseNtsAmt + bppCrdcUseEtcAmt);
                                    man.직불카드_도서공연 += (bppDrtpCardUseNtsAmt + bppDrtpCardUseEtcAmt);
                                    man.현금영수증_도서공연 += (bppCshptUseNtsAmt + bppCshptUseEtcAmt);
                                    
                                    man.신용카드_전통시장 += (tdmrUseNtsAmt + tdmrUseEtcAmt);
                                    man.신용카드_대중교통 += (pbtUseNtsAmt + pbtUseEtcAmt);
//                                    man.지출액합계 += (man.지출액합계  + crdcUseNtsAmt + crdcUseEtcAmt + drtpCardUseNtsAmt + drtpCardUseEtcAmt
//                                    		       + bppCrdcUseNtsAmt + bppCrdcUseEtcAmt + bppDrtpCardUseNtsAmt + bppDrtpCardUseEtcAmt
//                                    		       + bppCshptUseNtsAmt + bppCshptUseEtcAmt + tdmrUseNtsAmt + tdmrUseEtcAmt
//                                    		       + pbtUseNtsAmt + pbtUseEtcAmt);
                                    man.지출액합계 += 보험료_일반보장성 + 보험료_장애인보장성 + 의료비_의료비 + 의료비_안경구입비 + 교육비_교육비_직업훈련비 + 교육비_교복구입비 + 교육비_체험학습비 + 기부금_공제대상
                                    			   + crdcUseNtsAmt + crdcUseEtcAmt + drtpCardUseNtsAmt + drtpCardUseEtcAmt
                                    		       + bppCrdcUseNtsAmt + bppCrdcUseEtcAmt + bppDrtpCardUseNtsAmt + bppDrtpCardUseEtcAmt
                                    		       + bppCshptUseNtsAmt + bppCshptUseEtcAmt + tdmrUseNtsAmt + tdmrUseEtcAmt
                                    		       + pbtUseNtsAmt + pbtUseEtcAmt;
                                    /******* 신용카드, 직불카드, 현금영수증 ******************/
                                }
                            }

                            if (!find) {
                                ManItem man = new ManItem();
                                man.이름 = txprNm;
                                man.개인식별번호 = txprDscmNoCntn;
                                man.보험료_일반보장성 += 보험료_일반보장성;
                                man.보험료_장애인보장성 += 보험료_장애인보장성;
                                man.의료비_의료비 += 의료비_의료비;
                                man.의료비_안경구입비 += 의료비_안경구입비;
                                man.교육비_교육비_직업훈련비 += 교육비_교육비_직업훈련비;
                                man.교육비_교복구입비 += 교육비_교복구입비;
                                man.교육비_체험학습비 += 교육비_체험학습비;
                                man.기부금_공제대상 += 기부금_공제대상;
//                                man.지출액합계 += 보험료_일반보장성 + 보험료_장애인보장성 + 의료비_의료비 + 의료비_안경구입비 + 교육비_교육비_직업훈련비 + 교육비_교복구입비 + 교육비_체험학습비 + 기부금_공제대상;
                                
                                /******* 신용카드, 직불카드, 현금영수증 ******************/                                    
                                man.신용카드_일반 += (crdcUseNtsAmt + crdcUseEtcAmt);
                                man.직불카드_일반 += (drtpCardUseNtsAmt + drtpCardUseEtcAmt);
                                
                                man.신용카드_도서공연 += (bppCrdcUseNtsAmt + bppCrdcUseEtcAmt);
                                man.직불카드_도서공연 += (bppDrtpCardUseNtsAmt + bppDrtpCardUseEtcAmt);
                                man.현금영수증_도서공연 += (bppCshptUseNtsAmt + bppCshptUseEtcAmt);
                                
                                man.신용카드_전통시장 += (tdmrUseNtsAmt + tdmrUseEtcAmt);
                                man.신용카드_대중교통 += (pbtUseNtsAmt + pbtUseEtcAmt);
//                                man.지출액합계 += (man.지출액합계  + crdcUseNtsAmt + crdcUseEtcAmt + drtpCardUseNtsAmt + drtpCardUseEtcAmt
//                                		       + bppCrdcUseNtsAmt + bppCrdcUseEtcAmt + bppDrtpCardUseNtsAmt + bppDrtpCardUseEtcAmt
//                                		       + bppCshptUseNtsAmt + bppCshptUseEtcAmt + tdmrUseNtsAmt + tdmrUseEtcAmt
//                                		       + pbtUseNtsAmt + pbtUseEtcAmt);
                                /******* 신용카드, 직불카드, 현금영수증 ******************/
                                
                                man.지출액합계 += 보험료_일반보장성 + 보험료_장애인보장성 + 의료비_의료비 + 의료비_안경구입비 + 교육비_교육비_직업훈련비 + 교육비_교복구입비 + 교육비_체험학습비 + 기부금_공제대상
                                		       + crdcUseNtsAmt + crdcUseEtcAmt + drtpCardUseNtsAmt + drtpCardUseEtcAmt
                                		       + bppCrdcUseNtsAmt + bppCrdcUseEtcAmt + bppDrtpCardUseNtsAmt + bppDrtpCardUseEtcAmt
                                		       + bppCshptUseNtsAmt + bppCshptUseEtcAmt + tdmrUseNtsAmt + tdmrUseEtcAmt
                                		       + pbtUseNtsAmt + pbtUseEtcAmt;
                                
                                listMan.add(man);
                            }

                            listMan.get(0).보험료_일반보장성 += 보험료_일반보장성;
                            listMan.get(0).보험료_장애인보장성 += 보험료_장애인보장성;
                            listMan.get(0).의료비_의료비 += 의료비_의료비;
                            listMan.get(0).의료비_안경구입비 += 의료비_안경구입비;
                            listMan.get(0).교육비_교육비_직업훈련비 += 교육비_교육비_직업훈련비;
                            listMan.get(0).교육비_교복구입비 += 교육비_교복구입비;
                            listMan.get(0).교육비_체험학습비 += 교육비_체험학습비;
                            listMan.get(0).기부금_공제대상 += 기부금_공제대상;
//                            listMan.get(0).지출액합계 += 보험료_일반보장성 + 보험료_장애인보장성 + 의료비_의료비 + 의료비_안경구입비 + 교육비_교육비_직업훈련비 + 교육비_교복구입비 + 교육비_체험학습비 + 기부금_공제대상;
                            
                            /******* 신용카드, 직불카드, 현금영수증 ******************/                                    
                            listMan.get(0).신용카드_일반 += (crdcUseNtsAmt + crdcUseEtcAmt);
                            listMan.get(0).직불카드_일반 += (drtpCardUseNtsAmt + drtpCardUseEtcAmt);
                            
                            listMan.get(0).신용카드_도서공연 += (bppCrdcUseNtsAmt + bppCrdcUseEtcAmt);
                            listMan.get(0).직불카드_도서공연 += (bppDrtpCardUseNtsAmt + bppDrtpCardUseEtcAmt);
                            listMan.get(0).현금영수증_도서공연 += (bppCshptUseNtsAmt + bppCshptUseEtcAmt);
                            
                            listMan.get(0).신용카드_전통시장 += (tdmrUseNtsAmt + tdmrUseEtcAmt);
                            listMan.get(0).신용카드_대중교통 += (pbtUseNtsAmt + pbtUseEtcAmt);
//                            listMan.get(0).지출액합계 += (listMan.get(0).지출액합계  + crdcUseNtsAmt + crdcUseEtcAmt + drtpCardUseNtsAmt + drtpCardUseEtcAmt
//                            		       + bppCrdcUseNtsAmt + bppCrdcUseEtcAmt + bppDrtpCardUseNtsAmt + bppDrtpCardUseEtcAmt
//                            		       + bppCshptUseNtsAmt + bppCshptUseEtcAmt + tdmrUseNtsAmt + tdmrUseEtcAmt
//                            		       + pbtUseNtsAmt + pbtUseEtcAmt);
                            
                            listMan.get(0).지출액합계 += 보험료_일반보장성 + 보험료_장애인보장성 + 의료비_의료비 + 의료비_안경구입비 + 교육비_교육비_직업훈련비 + 교육비_교복구입비 + 교육비_체험학습비 + 기부금_공제대상
				                            		 + crdcUseNtsAmt + crdcUseEtcAmt + drtpCardUseNtsAmt + drtpCardUseEtcAmt
				                      		       	 + bppCrdcUseNtsAmt + bppCrdcUseEtcAmt + bppDrtpCardUseNtsAmt + bppDrtpCardUseEtcAmt
				                      		       	 + bppCshptUseNtsAmt + bppCshptUseEtcAmt + tdmrUseNtsAmt + tdmrUseEtcAmt
				                      		       	 + pbtUseNtsAmt + pbtUseEtcAmt;
                            
                            /******* 신용카드, 직불카드, 현금영수증 ******************/
                        }
                    }
                }

                // 공제신고서는 man 테그가 본인만 있음
                break;
            }
        }
    }

    private void parseASum(List<ManItem> listMan, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList sublist = element.getElementsByTagName("data");

                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");
                        String acc_no = eData.getAttribute("acc_no");
                        String insu1_resid = eData.getElementsByTagName("insu1_resid").item(0).getTextContent();
                        String insu1_nm = eData.getElementsByTagName("insu1_nm").item(0).getTextContent();

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

//                        logger.debug(dat_cd + "|" + acc_no + "|" + insu1_resid + "|" + insu1_nm + "|" + sum);

                        YE001VO ye001 = getID(insu1_resid, insu1_nm);
                        if (ye001 == null) {
                            continue;
                        }


                        boolean find = false;
                        for (ManItem man : listMan) {
                            if (insu1_resid.equals(man.개인식별번호)) {
                                find = true;
                                if ("G0001".equals(dat_cd)) {
                                    man.보험료_일반보장성 += sum;
                                } else if ("G0002".equals(dat_cd)) {
                                    man.보험료_장애인보장성 += sum;
                                }
                                man.지출액합계 += sum;
                            }
                        }

                        if (!find) {
                            ManItem man = new ManItem();
                            man.이름 = insu1_nm;
                            man.개인식별번호 = insu1_resid;
                            if ("G0001".equals(dat_cd)) {
                                man.보험료_일반보장성 += sum;
                            } else if ("G0002".equals(dat_cd)) {
                                man.보험료_장애인보장성 += sum;
                            }
                            man.지출액합계 += sum;
                            listMan.add(man);
                        }

                        if ("G0001".equals(dat_cd)) {
                            listMan.get(0).보험료_일반보장성 += sum;
                        } else if ("G0002".equals(dat_cd)) {
                            listMan.get(0).보험료_장애인보장성 += sum;
                        }
                        listMan.get(0).지출액합계 += sum;
                    }
                }
            }
        }
    }

    private void parseBSum(List<ManItem> listMan, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

//                logger.debug(ye001.get사용자ID());

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

//                        logger.debug(dat_cd + "|" + sum);

                        boolean find = false;
                        for (ManItem man : listMan) {
                            if (resid.equals(man.개인식별번호)) {
                                find = true;
                                if ("G0026".equals(dat_cd)) {
                                    man.의료비_안경구입비 += sum;
                                } else {
                                    man.의료비_의료비 += sum;
                                }
                                man.지출액합계 += sum;
                            }
                        }

                        if (!find) {
                            ManItem man = new ManItem();
                            man.이름 = name;
                            man.개인식별번호 = resid;
                            if ("G0026".equals(dat_cd)) {
                                man.의료비_안경구입비 += sum;
                            } else {
                                man.의료비_의료비 += sum;
                            }
                            man.지출액합계 += sum;
                            listMan.add(man);
                        }

                        if ("G0026".equals(dat_cd)) {
                            listMan.get(0).의료비_안경구입비 += sum;
                        } else {
                            listMan.get(0).의료비_의료비 += sum;
                        }
                        listMan.get(0).지출액합계 += sum;
                    }
                }
            }
        }
    }

    private void parseCSum(List<ManItem> listMan, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");
                        String edu_cl = eData.getAttribute("edu_cl");

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        boolean find = false;
                        for (ManItem man : listMan) {
                            if (resid.equals(man.개인식별번호)) {
                                find = true;
                                if ("G0024".equals(dat_cd)) {
                                    man.교육비_교복구입비 += sum;
                                } else {
                                    if ("B".equals(edu_cl)) {
                                        man.교육비_체험학습비 += sum;
                                    } else {
                                        man.교육비_교육비_직업훈련비 += sum;
                                    }
                                    // TODO 2018년도 장애인특수 추가해야함
                                }
                                man.지출액합계 += sum;
                            }
                        }

                        if (!find) {
                            ManItem man = new ManItem();
                            man.이름 = name;
                            man.개인식별번호 = resid;
                            if ("G0024".equals(dat_cd)) {
                                man.교육비_교복구입비 += sum;
                            } else {
                                if ("B".equals(edu_cl)) {
                                    man.교육비_체험학습비 += sum;
                                } else {
                                    man.교육비_교육비_직업훈련비 += sum;
                                }
                                // TODO 2018년도 장애인특수 추가해야함
                            }
                            man.지출액합계 += sum;
                            listMan.add(man);
                        }

                        if ("G0024".equals(dat_cd)) {
                            listMan.get(0).교육비_교복구입비 += sum;
                        } else {
                            if ("B".equals(edu_cl)) {
                                listMan.get(0).교육비_체험학습비 += sum;
                            } else {
                                listMan.get(0).교육비_교육비_직업훈련비 += sum;
                            }
                            // TODO 2018년도 장애인특수 추가해야함
                        }
                        listMan.get(0).지출액합계 += sum;
                    }
                }
            }
        }
    }

    private void parseDSum(List<ManItem> listMan, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }


                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
//                        String dat_cd = eData.getAttribute("dat_cd");
//                        String acc_no = eData.getAttribute("acc_no");
//                        String com_cd = eData.getElementsByTagName("com_cd").item(0).getTextContent();

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        boolean find = false;
                        for (ManItem man : listMan) {
                            if (resid.equals(man.개인식별번호)) {
                                find = true;
                                man.개인연금저축_연금저축_퇴직연금 += sum;
                                man.지출액합계 += sum;
                            }
                        }

                        if (!find) {
                            ManItem man = new ManItem();
                            man.이름 = name;
                            man.개인식별번호 = resid;
                            man.개인연금저축_연금저축_퇴직연금 += sum;
                            man.지출액합계 += sum;
                            listMan.add(man);
                        }

                        listMan.get(0).개인연금저축_연금저축_퇴직연금 += sum;
                        listMan.get(0).지출액합계 += sum;
                    }
                }
            }
        }
    }

    private void parseE_FSum(List<ManItem> listMan, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
//                        String dat_cd = eData.getAttribute("dat_cd");
//                        String acc_no = eData.getAttribute("acc_no");
//                        String com_cd = eData.getElementsByTagName("com_cd").item(0).getTextContent();
                        String ddct_bs_ass_amt = eData.getElementsByTagName("ddct_bs_ass_amt").item(0).getTextContent();

                        boolean find = false;
                        for (ManItem man : listMan) {
                            if (resid.equals(man.개인식별번호)) {
                                find = true;
                                man.개인연금저축_연금저축_퇴직연금 += StringUtil.strPaserInt(ddct_bs_ass_amt);
                                man.지출액합계 += StringUtil.strPaserInt(ddct_bs_ass_amt);
                            }
                        }

                        if (!find) {
                            ManItem man = new ManItem();
                            man.이름 = name;
                            man.개인식별번호 = resid;
                            man.개인연금저축_연금저축_퇴직연금 += StringUtil.strPaserInt(ddct_bs_ass_amt);
                            man.지출액합계 += StringUtil.strPaserInt(ddct_bs_ass_amt);
                            listMan.add(man);
                        }

                        listMan.get(0).개인연금저축_연금저축_퇴직연금 += StringUtil.strPaserInt(ddct_bs_ass_amt);
                        listMan.get(0).지출액합계 += StringUtil.strPaserInt(ddct_bs_ass_amt);
                    }
                }
            }
        }
    }

    /**
     *
     * 신용카드,현금영수증,직불카드 처리
     *
     * @param listMan
     * @param listNode
     * @throws Exception
     */
    private void parseGSum(List<ManItem> listMan, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");
                        String use_place_cd = eData.getAttribute("use_place_cd");

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        boolean find = false;
                        for (ManItem man : listMan) {
                            if (resid.equals(man.개인식별번호)) {
                                find = true;
                                if ("G0012".equals(dat_cd)) {
                                    if ("1".equals(use_place_cd)) {
                                        man.신용카드_일반 += sum;
                                    } else if ("2".equals(use_place_cd)) {
                                        man.신용카드_전통시장 += sum;
                                    } else if ("3".equals(use_place_cd)) {
                                        man.신용카드_대중교통 += sum;
                                    } else if ("4".equals(use_place_cd)) {
                                        man.신용카드_도서공연 += sum;
                                    }
                                } else if ("G0013".equals(dat_cd)) {
                                    if ("1".equals(use_place_cd)) {
                                        man.현금영수증_일반 += sum;
                                    } else if ("2".equals(use_place_cd)) {
                                        man.현금영수증_전통시장 += sum;
                                    } else if ("3".equals(use_place_cd)) {
                                        man.현금영수증_대중교통 += sum;
                                    } else if ("4".equals(use_place_cd)) {
                                        man.현금영수증_도서공연 += sum;
                                    }
                                } else if ("G0014".equals(dat_cd)) {
                                    if ("1".equals(use_place_cd)) {
                                        man.직불카드_일반 += sum;
                                    } else if ("2".equals(use_place_cd)) {
                                        man.직불카드_전통시장 += sum;
                                    } else if ("3".equals(use_place_cd)) {
                                        man.직불카드_대중교통 += sum;
                                    } else if ("4".equals(use_place_cd)) {
                                        man.직불카드_도서공연 += sum;
                                    }
                                }
                                // TODO 2018년도 도서공연 추가해야함
                                man.지출액합계 += sum;
                            }
                        }

                        if (!find) {
                            ManItem man = new ManItem();
                            man.이름 = name;
                            man.개인식별번호 = resid;
                            if ("G0012".equals(dat_cd)) {
                                if ("1".equals(use_place_cd)) {
                                    man.신용카드_일반 += sum;
                                } else if ("2".equals(use_place_cd)) {
                                    man.신용카드_전통시장 += sum;
                                } else if ("3".equals(use_place_cd)) {
                                    man.신용카드_대중교통 += sum;
                                } else if ("4".equals(use_place_cd)) {
                                    man.신용카드_도서공연 += sum;
                                }
                            } else if ("G0013".equals(dat_cd)) {
                                if ("1".equals(use_place_cd)) {
                                    man.현금영수증_일반 += sum;
                                } else if ("2".equals(use_place_cd)) {
                                    man.현금영수증_전통시장 += sum;
                                } else if ("3".equals(use_place_cd)) {
                                    man.현금영수증_대중교통 += sum;
                                } else if ("4".equals(use_place_cd)) {
                                    man.현금영수증_도서공연 += sum;
                                }
                            } else if ("G0014".equals(dat_cd)) {
                                if ("1".equals(use_place_cd)) {
                                    man.직불카드_일반 += sum;
                                } else if ("2".equals(use_place_cd)) {
                                    man.직불카드_전통시장 += sum;
                                } else if ("3".equals(use_place_cd)) {
                                    man.직불카드_대중교통 += sum;
                                } else if ("4".equals(use_place_cd)) {
                                    man.직불카드_도서공연 += sum;
                                }
                            }
                            // TODO 2018년도 도서공연 추가해야함
                            man.지출액합계 += sum;
                            listMan.add(man);
                        }

                        if ("G0012".equals(dat_cd)) {
                            if ("1".equals(use_place_cd)) {
                                listMan.get(0).신용카드_일반 += sum;
                            } else if ("2".equals(use_place_cd)) {
                                listMan.get(0).신용카드_전통시장 += sum;
                            } else if ("3".equals(use_place_cd)) {
                                listMan.get(0).신용카드_대중교통 += sum;
                            } else if ("4".equals(use_place_cd)) {
                            	listMan.get(0).신용카드_도서공연 += sum;
                            }
                        } else if ("G0013".equals(dat_cd)) {
                            if ("1".equals(use_place_cd)) {
                                listMan.get(0).현금영수증_일반 += sum;
                            } else if ("2".equals(use_place_cd)) {
                                listMan.get(0).현금영수증_전통시장 += sum;
                            } else if ("3".equals(use_place_cd)) {
                                listMan.get(0).현금영수증_대중교통 += sum;
                            } else if ("4".equals(use_place_cd)) {
                            	listMan.get(0).현금영수증_도서공연 += sum;
                            }
                        } else if ("G0014".equals(dat_cd)) {
                            if ("1".equals(use_place_cd)) {
                                listMan.get(0).직불카드_일반 += sum;
                            } else if ("2".equals(use_place_cd)) {
                                listMan.get(0).직불카드_전통시장 += sum;
                            } else if ("3".equals(use_place_cd)) {
                                listMan.get(0).직불카드_대중교통 += sum;
                            } else if ("4".equals(use_place_cd)) {
                            	listMan.get(0).직불카드_도서공연 += sum;
                            }
                        }
                        // TODO 2018년도 도서공연 추가해야함
                        listMan.get(0).지출액합계 += sum;
                    }
                }
            }
        }
    }

    private void parseJSum(List<ManItem> listMan, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");
//                        String acc_no = eData.getAttribute("acc_no");

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        boolean find = false;
                        for (ManItem man : listMan) {
                            if (resid.equals(man.개인식별번호)) {
                                find = true;
                                if ("G0018".equals(dat_cd)) {
                                    man.주택마련저축 += sum;
                                } else {
                                    man.주택자금 += sum;
                                }
                                man.지출액합계 += sum;
                            }
                        }

                        if (!find) {
                            ManItem man = new ManItem();
                            man.이름 = name;
                            man.개인식별번호 = resid;
                            if ("G0018".equals(dat_cd)) {
                                man.주택마련저축 += sum;
                            } else {
                                man.주택자금 += sum;
                            }
                            man.지출액합계 += sum;
                            listMan.add(man);
                        }

                        if ("G0018".equals(dat_cd)) {
                            listMan.get(0).주택마련저축 += sum;
                        } else {
                            listMan.get(0).주택자금 += sum;
                        }
                        listMan.get(0).지출액합계 += sum;
                    }
                }
            }
        }
    }

    private void parseKSum(List<ManItem> listMan, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        boolean find = false;
                        for (ManItem man : listMan) {
                            if (resid.equals(man.개인식별번호)) {
                                find = true;
                                man.소기업소상공인공제부금 += sum;
                                man.지출액합계 += sum;
                            }
                        }

                        if (!find) {
                            ManItem man = new ManItem();
                            man.이름 = name;
                            man.개인식별번호 = resid;
                            man.소기업소상공인공제부금 += sum;
                            man.지출액합계 += sum;
                            listMan.add(man);
                        }

                        listMan.get(0).소기업소상공인공제부금 += sum;
                        listMan.get(0).지출액합계 += sum;

                        return;
                    }
                }
            }
        }
    }

    private void parseLSum(List<ManItem> listMan, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
//                        String dat_cd = eData.getAttribute("dat_cd");
//                        String busnid = eData.getAttribute("busnid");
//                        String trade_nm = eData.getAttribute("trade_nm");
//                        String donation_cd = eData.getAttribute("donation_cd");
                        String sbdy_apln_sum = eData.getElementsByTagName("sbdy_apln_sum").item(0).getTextContent();

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        int count = eData.getElementsByTagName("amt") == null ? 0 : eData.getElementsByTagName("amt").getLength();

                        if (count == 0) {
                            // 1건으로 처리
                            count = 1;
                        }

                        boolean find = false;
                        for (ManItem man : listMan) {
                            if (resid.equals(man.개인식별번호)) {
                                find = true;
                                man.기부금_공제대상 += sum;
                                man.기부금_기부장려 += StringUtil.strPaserInt(sbdy_apln_sum);
                                man.지출액합계 += sum;
                            }
                        }

                        if (!find) {
                            ManItem man = new ManItem();
                            man.이름 = name;
                            man.개인식별번호 = resid;
                            man.기부금_공제대상 += sum;
                            man.기부금_기부장려 += StringUtil.strPaserInt(sbdy_apln_sum);
                            man.지출액합계 += sum;
                            listMan.add(man);
                        }

                        listMan.get(0).기부금_공제대상 += sum;
                        listMan.get(0).기부금_기부장려 += StringUtil.strPaserInt(sbdy_apln_sum);
                        listMan.get(0).지출액합계 += sum;
                    }
                }
            }
        }
    }

    private void parseNSum(List<ManItem> listMan, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");
                        String secu_no = eData.getAttribute("secu_no");
                        String com_cd = eData.getElementsByTagName("com_cd").item(0).getTextContent();

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        boolean find = false;
                        for (ManItem man : listMan) {
                            if (resid.equals(man.개인식별번호)) {
                                find = true;
                                man.장기집합투자증권저축 += sum;
                                man.지출액합계 += sum;
                            }
                        }

                        if (!find) {
                            ManItem man = new ManItem();
                            man.이름 = name;
                            man.개인식별번호 = resid;
                            man.장기집합투자증권저축 += sum;
                            man.지출액합계 += sum;
                            listMan.add(man);
                        }

                        listMan.get(0).장기집합투자증권저축 += sum;
                        listMan.get(0).지출액합계 += sum;
                    }
                }
            }
        }
    }

    private void parseOSum(List<ManItem> listMan, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
//                        String dat_cd = eData.getAttribute("dat_cd");
                        Element eSum = (Element) eData.getElementsByTagName("sum").item(0);
                        String hi_ntf = eSum.getAttribute("hi_ntf");
                        String hi_pmt = eSum.getAttribute("hi_pmt");
                        String ltrm_ntf = eSum.getAttribute("ltrm_ntf");
                        String ltrm_pmt = eSum.getAttribute("ltrm_pmt");

                        boolean find = false;
                        for (ManItem man : listMan) {
                            if (resid.equals(man.개인식별번호)) {
                                find = true;
                                man.건강보험_보수월액 += StringUtil.strPaserInt(hi_ntf);
                                man.건강보험_소득월액 += StringUtil.strPaserInt(hi_pmt);
                                man.장기요양보험_보수월액 += StringUtil.strPaserInt(ltrm_ntf);
                                man.장기요양보험_소득월액 += StringUtil.strPaserInt(ltrm_pmt);
                                man.지출액합계 += man.건강보험_보수월액 + man.건강보험_소득월액 + man.장기요양보험_보수월액 + man.장기요양보험_소득월액;
                            }
                        }

                        if (!find) {
                            ManItem man = new ManItem();
                            man.이름 = name;
                            man.개인식별번호 = resid;
                            man.건강보험_보수월액 += StringUtil.strPaserInt(hi_ntf);
                            man.건강보험_소득월액 += StringUtil.strPaserInt(hi_pmt);
                            man.장기요양보험_보수월액 += StringUtil.strPaserInt(ltrm_ntf);
                            man.장기요양보험_소득월액 += StringUtil.strPaserInt(ltrm_pmt);
                            man.지출액합계 += man.건강보험_보수월액 + man.건강보험_소득월액 + man.장기요양보험_보수월액 + man.장기요양보험_소득월액;
                            listMan.add(man);
                        }

                        listMan.get(0).건강보험_보수월액 += StringUtil.strPaserInt(hi_ntf);
                        listMan.get(0).건강보험_소득월액 += StringUtil.strPaserInt(hi_pmt);
                        listMan.get(0).장기요양보험_보수월액 += StringUtil.strPaserInt(ltrm_ntf);
                        listMan.get(0).장기요양보험_소득월액 += StringUtil.strPaserInt(ltrm_pmt);
                        listMan.get(0).지출액합계 += listMan.get(0).건강보험_보수월액 + listMan.get(0).건강보험_소득월액
                                + listMan.get(0).장기요양보험_보수월액 + listMan.get(0).장기요양보험_소득월액;
                        return;
                    }
                }
            }
        }
    }

    private void parsePSum(List<ManItem> listMan, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
//                        String dat_cd = eData.getAttribute("dat_cd");
                        Element eSum = (Element) eData.getElementsByTagName("sum").item(0);
                        String ntf = eSum.getAttribute("ntf");
                        String pmt = eSum.getAttribute("pmt");

                        boolean find = false;
                        for (ManItem man : listMan) {
                            if (resid.equals(man.개인식별번호)) {
                                find = true;
                                man.국민연금_직장 += StringUtil.strPaserInt(ntf);
                                man.국민연금_지역 += StringUtil.strPaserInt(pmt);
                                man.지출액합계 += man.국민연금_직장 + man.국민연금_지역;
                            }
                        }

                        if (!find) {
                            ManItem man = new ManItem();
                            man.이름 = name;
                            man.개인식별번호 = resid;
                            man.국민연금_직장 += StringUtil.strPaserInt(ntf);
                            man.국민연금_지역 += StringUtil.strPaserInt(pmt);
                            man.지출액합계 += man.국민연금_직장 + man.국민연금_지역;
                            listMan.add(man);
                        }

                        listMan.get(0).국민연금_직장 += StringUtil.strPaserInt(ntf);
                        listMan.get(0).국민연금_지역 += StringUtil.strPaserInt(pmt);
                        listMan.get(0).지출액합계 += listMan.get(0).국민연금_직장 + listMan.get(0).국민연금_지역;
                        return;
                    }
                }
            }
        }
    }

    /**
    *
    * 국세청 PDF 파일 정보 DB 저장 - 편리한 연말정산
    *
    * @param loginVO
    * @param doc
    * @throws Exception
    */
   private void saveXmlData2(SessionVO loginVO, Document doc) throws Exception {
   	
   	   List<ManItem> listManItem = new ArrayList<>();   	 
   	   
       
       List<YE001VO> listYE001 = new ArrayList<>();  	 //부양가족
       
       List<YE101VO> listYE101 = new ArrayList<>();		 //주택임차차입금원리금상환액
       List<YE104VO> listYE104 = new ArrayList<>();		 //장기주택저당차입금이자상환액
       List<YE106VO> listYE106 = new ArrayList<>();      //개인연금저축
       List<YE107VO> listYE107 = new ArrayList<>();      //주택마련저축
       List<YE108VO> listYE108 = new ArrayList<>();  	 //신용카드
       List<YE109VO> listYE109 = new ArrayList<>();      //장기집합투자증권저축
       
       List<YE301VO> listYE301 = new ArrayList<>();      //퇴직연금계좌
       List<YE302VO> listYE302 = new ArrayList<>();		 //연금저축계좌
              
       List<YE401VO> listYE401 = new ArrayList<>();  	 //보험료
       List<YE402VO> listYE402 = new ArrayList<>();   	 //의료비
       List<YE403VO> listYE403 = new ArrayList<>();	 	 //교육비
       List<YE404VO> listYE404 = new ArrayList<>();		 //기부금
       List<YE105VO> listYE105 = new ArrayList<>();		 //월세액공제
      

       ManItem total = new ManItem();
       total.이름 = "구분별 합계";
       listManItem.add(total);

       NodeList list = doc.getElementsByTagName("form");
       for (int i = 0; i < list.getLength(); i++) {
           Node node = list.item(i);
           if (node.getNodeType() == Node.ELEMENT_NODE) {
               Element element = (Element) node;
               switch (element.getAttribute("form_cd")) {
                   case "A101Y":
                       // 공제신고서
                       parseASum2(listManItem, listYE101, listYE104, listYE403, listYE401, true, element.getChildNodes());
                       
                       //부양가족정보 파싱 
                       parseA001(listYE001, element.getChildNodes()); 
                            
                       //보험료
                       //parseYE401(listYE401, listManItem);
                       
                       break;
                   case "B101Y":
                       // 연금저축등 소득.세액 공제명세
                	   parseB2(listYE106, listYE107, listYE109,listYE301, listYE302, element.getChildNodes());                	                   	   
                       break;
                   case "C101Y":
                       // 월세액.거주자간 주택임차차입금 상환액
                	   parseC2(listYE105, listYE001, element.getChildNodes());
                       break;
                   case "D101Y":
                       // 의료비 지급명세
                	   parseD2(listYE402, listYE001, element.getChildNodes());                	   
                       break;
                   case "E101Y":
                       // 기부금 명세
                	   parseE2(listYE404, listYE001, element.getChildNodes());                	   
                       break;
                   case "F101Y":
                       // 신용카드등 소득공제신청서
                	   parseF2(listYE108, listYE001, element.getChildNodes());
//                	   parseFsum2(listManItem, element.getChildNodes());
                       break;
               }
           }
       }
   	
        if (this.PDF본인_사용자ID != null) {
            ywPdfService.saveYWPdf2(계약ID, this.PDF본인_사용자ID, loginVO.getUserId(),this.계약년도, listManItem, listYE108, listYE001, listYE402, listYE401,
            		listYE106, listYE107, listYE109, listYE301, listYE302,
            		listYE404, listYE101, listYE104, listYE403, listYE105);
        }
   }
   
   
   /**
    *
    * 국세청 PDF 파일 정보 DB 저장 - 편리한 연말정산 (기부금)	
    *
    * @param listYE404
    * @param listYE001
    * @param listNode
    * @throws Exception
    */
   private void parseE2(List<YE404VO> listYE404, List<YE001VO> listYE001, NodeList listNode) throws Exception {
	   
	   /*
		* 부양가족 정보 셋팅
		*/
	   Map<String,YE001VO> hashMap = new HashMap<String,YE001VO>();
	   for(YE001VO ye001VO : listYE001){
				
		   if(StringUtils.isNotEmpty(ye001VO.getResId()) && StringUtils.isNotEmpty(ye001VO.get성명())) {
				
			   String resid = ye001VO.getResId();
			   String name = ye001VO.get성명();
			   YE001VO ye001 = getID(resid, name);
			   if (ye001 == null) {
				   continue;
			   }
	            
			   hashMap.put(ye001VO.getResId(), ye001);
	            
			}
			
	   }
		
	   for (int i = 0; i < listNode.getLength(); i++) {
		     
		   	Node node = listNode.item(i);
		   	if (node.getNodeType() == Node.ELEMENT_NODE) {
		    		
		    		Element element = (Element) node;
		    		
		    		NodeList sublist = element.getElementsByTagName("data");
		    		for (int j = 0; j < sublist.getLength(); j++) {
		               Node nData = sublist.item(j);
		             
		               if (nData.getNodeType() == Node.ELEMENT_NODE) {
		                	
			               	Element eData = (Element) nData;
			                   	
			               	if(eData != null){
			                   	
			               		YE001VO ye001VO = hashMap.get(eData.getAttribute("resnoEncCntn"));
			               		
			               		if(ye001VO != null){
				               		YE404VO ye404 = new YE404VO();
			                        ye404.setDbMode("C");
			                        ye404.set계약ID(계약ID);
			                        ye404.set사용자ID(ye001VO.get사용자ID());
			                        ye404.set부양가족ID(ye001VO.get부양가족ID());
			                        
			                        if(StringUtils.equals("01", eData.getAttribute("yrsMateClCd"))){ //국세청자료
			                        	ye404.set자료구분코드("0");
			                        }else{
			                        	ye404.set자료구분코드("3");
			                        }
			                        			                        
			                        ye404.set기부코드(eData.getAttribute("conbNm"));  // 10:법정기부금, 20:정치자금기부금, 40:종교단체외지정기부금, 41:종교단체지정기부금, 42:우리사주조합기부금
			                        ye404.set기부내용("1");
			                        ye404.set기부처_사업자등록번호(eData.getAttribute("bsnoEncCntn"));
			                        ye404.set상호(eData.getAttribute("coplNm"));
			                        ye404.set기부명세_건수(StringUtil.strPaserInt(eData.getAttribute("dntScnt")));
			                        ye404.set기부명세_공제금액(0);
			                        ye404.set기부명세_공제대상기부금(StringUtil.strPaserInt(eData.getAttribute("ddcTrgtConbAmt")));
			                        ye404.set기부명세_기부장려금(StringUtil.strPaserInt(eData.getAttribute("conbSumAmt")));
			                        ye404.set기부명세_기타(StringUtil.strPaserInt(eData.getAttribute("ddcExclConbEtcAmt")));
			                        
			                        listYE404.add(ye404);
			               		}
		                        
			               	}
			               	
		               }
		               
		    		}
		    		
		   	}
		   	
	   }
   }
     
	               		
   /**
    *
    * 국세청 PDF 파일 정보 DB 저장 - 편리한 연말정산 (연금,저축 등 소득,세액)
    *
    * @param listYE106	: 개인연금저축
    * @param listYE107	: 주택마련저축
    * @param listYE109	: 장기집합투자증권저축
    * @param listYE301	: 퇴직연금계좌
    * @param listYE301	: 연금저축계좌
    * @param listManItem
    * @throws Exception
    */
   private void parseB2(List<YE106VO> listYE106, List<YE107VO> listYE107, List<YE109VO> listYE109, List<YE301VO> listYE301, List<YE302VO> listYE302, NodeList listNode) throws Exception {
	   
	   for (int i = 0; i < listNode.getLength(); i++) {
		     
	    	Node node = listNode.item(i);
	    	if (node.getNodeType() == Node.ELEMENT_NODE) {
	     		
	     		Element element = (Element) node;
	     		
	     		String resnoEncCntn = element.getAttribute("resnoEncCntn");		//주민번호
                String fnm = element.getAttribute("fnm");						//성명	
	     		
	   			YE001VO ye001 = getID(resnoEncCntn, fnm);
	   			if (ye001 == null) {
	               continue;
	   			}
                
	     		
	     		
	     		NodeList sublist = element.getElementsByTagName("data");
	     		for (int j = 0; j < sublist.getLength(); j++) {
	                Node nData = sublist.item(j);
	              
	                if (nData.getNodeType() == Node.ELEMENT_NODE) {
	                 	
		               	Element eData = (Element) nData;
		               	
		               	//퇴직연금
		               	if(StringUtils.isNotEmpty(eData.getAttribute("rtpnAccRtpnCl"))){
//		               		logger.debug("# 퇴직연금 구간  : " + eData.getAttribute("rtpnAccRtpnCl") );

		               		YE301VO vo = new YE301VO();
	                        vo.setDbMode("C");
	                        vo.set계약ID(계약ID);
	                        vo.set사용자ID(ye001.get사용자ID());
	                        vo.set자료구분코드("0");
	                        	                        
	                        if(StringUtils.defaultString(eData.getAttribute("rtpnAccRtpnCl")).indexOf("과학기술인공제") > -1){
	                        	vo.set퇴직연금구분코드("12");
	                        }else{
	                        	vo.set퇴직연금구분코드("11");
	                        }
	                        
	                        vo.set금융회사등명칭(eData.getAttribute("rtpnFnnOrgnCd"));
	                        vo.set계좌번호_증권번호(eData.getAttribute("rtpnAccAccno"));
	                        vo.set납입금액(StringUtil.strPaserInt(eData.getAttribute("rtpnAccPymAmt")));
	                        vo.set차감금액(0);
	                        
	                        listYE301.add(vo);
	                        
		               	} 		               	
		               	//연금저축
		               	else if(StringUtils.isNotEmpty(eData.getAttribute("pnsnSvngAccPnsnSvngCl"))){
//		               		logger.debug("# 연금저축 구간  : " + eData.getAttribute("pnsnSvngAccPnsnSvngCl") );
		               		
		               		if(StringUtils.defaultString(eData.getAttribute("pnsnSvngAccPnsnSvngCl")).indexOf("개인연금저축") > -1){
		               			
		               			YE106VO vo = new YE106VO();
		                        vo.setDbMode("C");
		                        vo.set계약ID(계약ID);
		                        vo.set사용자ID(ye001.get사용자ID());
		                        vo.set자료구분코드("0");
		                        vo.set연금저축구분("21");
		                        vo.set금융회사등명칭(eData.getAttribute("pnsnSvngFnnOrgnCd"));
		                        vo.set계좌번호_증권번호(eData.getAttribute("pnsnSvngAccAccno"));
		                        vo.set납입금액(StringUtil.strPaserInt(eData.getAttribute("pnsnSvngAccPymAmt")));
		                        vo.set차감금액(0);
		                        listYE106.add(vo);
		               			
		               		}else{
		               			
		               			YE302VO vo = new YE302VO();
		                        vo.setDbMode("C");
		                        vo.set계약ID(계약ID);
		                        vo.set사용자ID(ye001.get사용자ID());
		                        vo.set자료구분코드("0");
		                        vo.set연금저축구분코드("22");
		                        vo.set금융회사등명칭(eData.getAttribute("pnsnSvngFnnOrgnCd"));
		                        vo.set계좌번호_증권번호(eData.getAttribute("pnsnSvngAccAccno"));
		                        vo.set납입금액(StringUtil.strPaserInt(eData.getAttribute("pnsnSvngAccPymAmt")));
		                        vo.set차감금액(0);
		                        listYE302.add(vo);
		                        
		               		}

		               	}		               	
		               	//주택마련 
		               	else if(StringUtils.isNotEmpty(eData.getAttribute("hsngPrptSvngSvngCl"))){
//		               		logger.debug("# 주택마련 구간  : " + eData.getAttribute("hsngPrptSvngSvngCl") );
		               		
		               		YE107VO vo = new YE107VO();
	                        vo.setDbMode("C");
	                        vo.set계약ID(계약ID);
	                        vo.set사용자ID(ye001.get사용자ID());
	                        vo.set자료구분코드("0");
	                        
	                        if(StringUtils.defaultString(eData.getAttribute("hsngPrptSvngSvngCl")).indexOf("근로자주택마련저축") > -1){
	                        	vo.set주택마련저축구분("34");
		               		}else if(StringUtils.defaultString(eData.getAttribute("hsngPrptSvngSvngCl")).indexOf("주택청약종합저축") > -1){
		               			vo.set주택마련저축구분("32");
		               		}else{
		               			vo.set주택마련저축구분("31");
		               		}
	                        
	                        vo.set금융회사등명칭(eData.getAttribute("hsngPrptSvngFnnOrgnCd"));
	                        vo.set계좌번호_증권번호(eData.getAttribute("hsngPrptSvngAccno"));
	                        vo.set가입일자("");
	                        vo.set납입금액(StringUtil.strPaserInt(eData.getAttribute("hsngPrptSvngPymAmt")));
	                        vo.set차감금액(0);
	                        listYE107.add(vo);

		               	}
		               	//장기집합투자증권저축
		               	else if(StringUtils.isNotEmpty(eData.getAttribute("ltrmCniSsFnnOrgnCd"))){
//		               		logger.debug("# 장기집합투자증권저축 구간  #");
		               		
		               		YE109VO vo = new YE109VO();
	                        vo.setDbMode("C");
	                        vo.set계약ID(계약ID);
	                        vo.set사용자ID(ye001.get사용자ID());
	                        vo.set자료구분코드("0");
	                        vo.set취급기관명(eData.getAttribute("ltrmCniSsFnnOrgnCd"));
	                        vo.set계좌번호_증권번호(eData.getAttribute("ltrmCniSsAccno"));
	                        vo.set납입금액(StringUtil.strPaserInt(eData.getAttribute("ltrmCniSsPymAmt")));
	                        vo.set차감금액(0);
	                        listYE109.add(vo);
	                        
		               	}
		               	
		               	
	                }
	                
	     		}
	     		
	    	}
	    	
	   }
	   
   }
   
   
   /**
    *
    * 국세청 PDF 파일 정보 DB 저장 - 편리한 연말정산 (보험료)
    *
    * @param listYE401
    * @param listManItem
    * @throws Exception
    */
   /*private void parseYE401(List<YE401VO> listYE401, List<ManItem> listManItem) throws Exception {
	   
	   //보험료 
	   if(listManItem != null && listManItem.size() > 0){

	   		for(ManItem manItem : listManItem){
	   		
	   			if(StringUtils.isNotEmpty(manItem.개인식별번호) && StringUtils.isNotEmpty(manItem.이름)){
	   				
		   			String resid = manItem.개인식별번호;
		   			String name = manItem.이름;
		   			YE001VO ye001 = getID(resid, name);
		   			if (ye001 == null) {
		               continue;
		   			}
		   		
		   			//보험료 - 보장성
	                if(manItem.보험료_일반보장성 > 0){
	                	YE401VO vo = new YE401VO();
	    	   			vo.setDbMode("C");
	                    vo.set계약ID(계약ID);
	                    vo.set사용자ID(ye001.get사용자ID());
	                    vo.set부양가족ID(ye001.get부양가족ID());
	                    vo.set자료구분코드("0");
	                                        
	                	vo.set보험구분코드("1");   // 보장성
	                	vo.set납입금액(manItem.보험료_일반보장성);
	                    vo.set차감금액(0);
	
	                    listYE401.add(vo);
	                }
	                                               
	                //보험료 장애인
	                if(manItem.보험료_장애인보장성 > 0){
	                	YE401VO vo = new YE401VO();
	    	   			vo.setDbMode("C");
	                    vo.set계약ID(계약ID);
	                    vo.set사용자ID(ye001.get사용자ID());
	                    vo.set부양가족ID(ye001.get부양가족ID());
	                    vo.set자료구분코드("0");
	                    
	                    vo.set보험구분코드("2");   //장애인전용보장성보험
	                    vo.set납입금액(manItem.보험료_장애인보장성);
	                    vo.set차감금액(0);
	
	                    listYE401.add(vo);
	                    
	                }
	                
	   			}
                
	   		}
	   }
	   
   }*/
   
   
   /**
    * 월세액 세액공제 
    * 
    * @param listYE105
    * @param listYE001
    * @param listNode
    * @throws Exception
    */
   private void parseC2(List<YE105VO> listYE105, List<YE001VO> listYE001, NodeList listNode) throws Exception {
	   	
		
		//월세액 세액공제
	    for (int i = 0; i < listNode.getLength(); i++) {
	    	
	    	Node node = listNode.item(i);
	    	if (node.getNodeType() == Node.ELEMENT_NODE) {
	     		
	     		Element element = (Element) node;
	     		
	     		NodeList sublist = element.getElementsByTagName("data");
	     		for (int j = 0; j < sublist.getLength(); j++) {
	                Node nData = sublist.item(j);
	             
	                
	                if (nData.getNodeType() == Node.ELEMENT_NODE) {
	                 	
		               	Element eData = (Element) nData;
		                   	
		               	if(eData != null){
		               		
		               		if(StringUtils.isNotEmpty(eData.getAttribute("useAmt")) && StringUtil.strPaserInt(eData.getAttribute("useAmt")) > 0){
		               			
		               			String resid = StringUtils.defaultString(eData.getAttribute("txprDscmNoEncCntn"));
			               		String name = StringUtils.defaultString(eData.getAttribute("lsorFnm"));
			               		YE001VO ye001VO = getID(resid, name);
			               		if (ye001VO == null) {
			               			continue;
			               		}
			               				                   		
		                   		YE105VO vo = new YE105VO();
		                   		
		                   		vo.setDbMode("C");
		                        vo.set계약ID(계약ID);
		                        vo.set사용자ID(ye001VO.get사용자ID());
		                        vo.set임대인성명_상호(StringUtils.defaultString(eData.getAttribute("lsorFnm")));
		                        vo.set개인식별번호(StringUtils.defaultString(eData.getAttribute("txprDscmNoEncCntn")));
		                        
		                        if(StringUtils.equals("단독주택", eData.getAttribute("hsngTypeClCd"))){
		                        	vo.set유형코드("1");
		                        }else if(StringUtils.equals("다가구", eData.getAttribute("hsngTypeClCd"))){
		                        	vo.set유형코드("2");
		                        }else if(StringUtils.equals("다세대주택", eData.getAttribute("hsngTypeClCd"))){
		                        	vo.set유형코드("3");
		                        }else if(StringUtils.equals("연립주택", eData.getAttribute("hsngTypeClCd"))){
		                        	vo.set유형코드("4");
		                        }else if(StringUtils.equals("아파트", eData.getAttribute("hsngTypeClCd"))){
		                        	vo.set유형코드("5");
		                        }else if(StringUtils.equals("오피스텔", eData.getAttribute("hsngTypeClCd"))){
		                        	vo.set유형코드("6");
		                        }else if(StringUtils.equals("고시원", eData.getAttribute("hsngTypeClCd"))){
		                        	vo.set유형코드("7");
		                        }else{
		                        	vo.set유형코드("8");
		                        }
		                        
		                        vo.set계약면적(StringUtils.defaultString(eData.getAttribute("hsngCtrSfl")));		                        
		                        vo.set임대차_주소지_물건지(StringUtils.defaultString(eData.getAttribute("mmrLsrnCtrpAdr")));
		                        vo.set임대차_계약개시일(StringUtils.defaultString(eData.getAttribute("mmrCtrTermStrtDt")));
		                        vo.set임대차_계약종료일(StringUtils.defaultString(eData.getAttribute("mmrCtrTermEndDt")));		                        
		                        vo.set연간_월세액(StringUtil.strPaserInt(eData.getAttribute("useAmt")));
		                        vo.set공제대상금액(0);
		                        
		                        listYE105.add(vo);
		               		}

		               	}
		               	
	                }
	     		}
	     		
	    	}
	    }
		
		
   }
   
  
   
   
   /**
    *
	* 국세청 PDF 파일 정보 DB 저장 - 편리한 연말정산 (의료비)
	*
	* @param listYE402
	* @param listYE001
	* @param listNode
	* @throws Exception
	*/
   private void parseD2(List<YE402VO> listYE402, List<YE001VO> listYE001, NodeList listNode) throws Exception {
	   
	   /*
		 * 부양가족 정보 셋팅
		 */
		Map<String,YE001VO> hashMap = new HashMap<String,YE001VO>();
		for(YE001VO ye001VO : listYE001){
				
			if(StringUtils.isNotEmpty(ye001VO.getResId()) && StringUtils.isNotEmpty(ye001VO.get성명())) {
				
				String resid = ye001VO.getResId();
	            String name = ye001VO.get성명();
	            YE001VO ye001 = getID(resid, name);
	            if (ye001 == null) {
	                continue;
	            }
	            
	            hashMap.put(ye001VO.getResId(), ye001);
	            
			}
			
		}
		
		//의료비 정보
	    for (int i = 0; i < listNode.getLength(); i++) {
	     
	    	Node node = listNode.item(i);
	    	if (node.getNodeType() == Node.ELEMENT_NODE) {
	     		
	     		Element element = (Element) node;
	     		
	     		NodeList sublist = element.getElementsByTagName("data");
	     		for (int j = 0; j < sublist.getLength(); j++) {
	                Node nData = sublist.item(j);
	              
	                if (nData.getNodeType() == Node.ELEMENT_NODE) {
	                 	
		               	Element eData = (Element) nData;
		                   	
		               	if(eData != null){
		                   	
		               		YE001VO ye001VO = hashMap.get(eData.getAttribute("resnoEncCntn"));
		                   			
		                   	if(ye001VO != null){
		                   		
		                   		YE402VO vo = new YE402VO();
		                        vo.setDbMode("C");
		                        vo.set계약ID(계약ID);
		                        vo.set사용자ID(ye001VO.get사용자ID());
		                        vo.set부양가족ID(ye001VO.get부양가족ID());
		                        
		                        //1:국세청장이 제공하는 의료비 자료
		                        if(StringUtils.equals("1", eData.getAttribute("mdxpsPrfClCd"))){
		                        	vo.set자료구분코드("0");		//국세청PDF
		                        }else{
		                        	vo.set자료구분코드("3");		//기타
		                        }		                        		                        		                        
		                        vo.set의료비증빙코드(StringUtils.defaultString(eData.getAttribute("mdxpsPrfClCd")));	
		                   		
		                        if ("0".equals(ye001VO.get가족관계()) || !"4".equals(ye001VO.get장애인())
		                                || StringUtil.strPaserInt(ye001VO.get나이()) >= 65) {
		                            vo.set공제종류코드("1");
		                        } else {
		                            vo.set공제종류코드("2");
		                        }
		                   		
		                        vo.set지급처_사업자등록번호(StringUtils.defaultString(eData.getAttribute("bsnoEncCntn")));
		                        vo.set상호(StringUtils.defaultString(eData.getAttribute("plymNm")));
		                        vo.set건수(StringUtil.strPaserInt(eData.getAttribute("scnt")));
		                        
		                        //난임시술비 해당여부
		                        if (StringUtils.equals("여", eData.getAttribute("yn2"))) {
		                            vo.set의료비유형("3");
		                        } else {
		                            vo.set의료비유형("1");
		                        }
		                        
		                        vo.set지출액(StringUtil.strPaserInt(eData.getAttribute("useAmt")));
		                        vo.set차감금액(0);
		                        		         
		                        listYE402.add(vo);
		                        
		                   	}
		                   	
	                   	}
		               	
		               	
	                }
	                 
	     		}
	     			
	    	}
	     		
	    }
		
		
   }
   
   
   /**
	 *
	 * 국세청 PDF 파일 정보 DB 저장 - 편리한 연말정산 (신용카드)
	 *
	 * @param listMan
	 * @param listNode
	 * @throws Exception
	 */
	private void parseF2(List<YE108VO> listYE108, List<YE001VO> listYE001, NodeList listNode) throws Exception {
 	
		/*
		 * 부양가족 정보 셋팅
		 */
		Map<String,YE001VO> hashMap = new HashMap<String,YE001VO>();
		for(YE001VO ye001VO : listYE001){
				
			if(StringUtils.isNotEmpty(ye001VO.getResId()) && StringUtils.isNotEmpty(ye001VO.get성명())) {
				
				String resid = ye001VO.getResId();
	            String name = ye001VO.get성명();
	            YE001VO ye001 = getID(resid, name);
	            if (ye001 == null) {
	                continue;
	            }
	            				
	            hashMap.put(ye001VO.getResId().substring(0, 6)+"|"+ye001VO.get성명(), ye001);	            
			}
			
		}
		
		
		//신용카드 정보
	     for (int i = 0; i < listNode.getLength(); i++) {
	     
	     		Node node = listNode.item(i);
	     		if (node.getNodeType() == Node.ELEMENT_NODE) {
	     		
	     			Element element = (Element) node;
	     		
	     			NodeList sublist = element.getElementsByTagName("data");
	     			for (int j = 0; j < sublist.getLength(); j++) {
	                 Node nData = sublist.item(j);
	              
	                 if (nData.getNodeType() == Node.ELEMENT_NODE) {
	                 	
		                   	Element eData = (Element) nData;
		                   	
		                   	
		                   	if(eData != null){
		                   	
		                   		YE001VO ye001VO = hashMap.get(eData.getAttribute("suptFmlyBhdt").substring(2, 8)+"|"+eData.getAttribute("suptFmlyFnm"));
			                   	
		                   				                   		
		                   		YE108VO ye108VO = new YE108VO();				
		        				
		        	            ye108VO.setDbMode("C");
		        	            ye108VO.set계약ID(계약ID);
		        	            ye108VO.set사용자ID(ye001VO.get사용자ID());
		        	            ye108VO.set부양가족ID(ye001VO.get부양가족ID());
		        	            
		        	            if(StringUtils.equals("국세청자료", eData.getAttribute("yrsMateClCd"))){
		        	            	ye108VO.set자료구분코드("0");
		        	            }else{
		        	            	ye108VO.set자료구분코드("3");
		        	            }		        	            
		        	            ye108VO.set기간구분코드("1");
			                   	
		        	            ye108VO.set신용카드(StringUtil.strPaserInt(eData.getAttribute("crdcUseAmt")));			//신용카드
			                   	ye108VO.set현금영수증(StringUtil.strPaserInt(eData.getAttribute("cshptUseAmt")));		//현금영수증
			                   	ye108VO.set직불_선불카드(StringUtil.strPaserInt(eData.getAttribute("drtpCardUseAmt")));		//직불,선불카드	                   	
			                   	ye108VO.set전통시장(StringUtil.strPaserInt(eData.getAttribute("tdmrUseAmt")));				//신용카드등사용금액 전통시장사용분
			                   	ye108VO.set대중교통(StringUtil.strPaserInt(eData.getAttribute("etcUseAmt")));				//신용카드등사용금액 대중교통이용분
			                   	ye108VO.set도서공연(StringUtil.strPaserInt(eData.getAttribute("bppCrdcUseAmtSum")));		//신용카드등사용금액 도서공연사용분
		        	            
			                   	listYE108.add(ye108VO);
		                   	}
	                 	
	                 }
	                 
	     			}
	     		
	     		}
	     }
	     	    	     
	 }
		
   
   /**
    *
    * 국세청 PDF 파일 정보 DB 저장 - 편리한 연말정산 (부양가족 정보)
    *
    * @param listYE001
    * @param listNode
    * @throws Exception
    */
   private void parseA001(List<YE001VO> listYE001, NodeList listNode) throws Exception {
	
		Map<String,YE001VO> hashMap = new HashMap<String,YE001VO>();
		
	   //부양가족 정보
       for (int i = 0; i < listNode.getLength(); i++) {
       
       		Node node = listNode.item(i);
       		if (node.getNodeType() == Node.ELEMENT_NODE) {
       		
       			Element element = (Element) node;
       		
       			NodeList sublist = element.getElementsByTagName("data");
       			for (int j = 0; j < sublist.getLength(); j++) {
                   Node nData = sublist.item(j);
                
                   if (nData.getNodeType() == Node.ELEMENT_NODE) {
                   	
	                   	Element eData = (Element) nData;
	                
	                   	if(eData != null){
	                   		
	                   		if(eData.getAttribute("suptFmlyRltClCd") != null && StringUtils.isNotEmpty(eData.getAttribute("suptFmlyRltClCd"))){
	                   			
	                   			YE001VO ye001VO = hashMap.get(eData.getAttribute("txprDscmNoCntn"));
	                   			
	                   			if(ye001VO == null){
	                   				ye001VO = new YE001VO();
	                   			}
	                   				                   				                   			
	                   			ye001VO.set성명(eData.getAttribute("txprNm"));
	                   			ye001VO.set가족관계(eData.getAttribute("suptFmlyRltClCd"));
	                   				                   			
	                   			// 내.외국인 구분
	                   			if(StringUtils.equals(eData.getAttribute("nnfClCd"),"내국인")){
	                   				ye001VO.set내외국인("1");
	                   			}else{
	                   				ye001VO.set내외국인("2");
	                   			}
	                   			
	                   			// 기본공제
	                   			if(StringUtils.equals(eData.getAttribute("bscDdcClCd"),"Y")){
	                   				ye001VO.set기본공제("1");
	                   			}else{
	                   				ye001VO.set기본공제("2");
	                   			}
	                   			
	                   			//부녀자 
	                   			if(StringUtils.equals(eData.getAttribute("wmnDdcClCd"),"Y")){
	                   				ye001VO.set부녀자("1");
	                   			}else{
	                   				ye001VO.set부녀자("2");
	                   			}	                   			
	                   			
	                   			//한부모
	                   			if(StringUtils.equals(eData.getAttribute("snprntFmlyDdcClCd"),"Y")){
	                   				ye001VO.set한부모("1");
	                   			}else{
	                   				ye001VO.set한부모("2");
	                   			}	
	                   			
	                   			//경로우대
	                   			if(StringUtils.equals(eData.getAttribute("sccDdcClCd"),"Y")){
	                   				ye001VO.set경로우대("1");
	                   			}else{
	                   				ye001VO.set경로우대("2");
	                   			}	
	                   			
	                   			//장애인
	                   			if(StringUtils.equals(eData.getAttribute("dsbrDdcClCd"),"N")){
	                   				ye001VO.set장애인("4");
	                   			}else{
	                   				ye001VO.set장애인(eData.getAttribute("dsbrDdcClCd"));
	                   			}	
	                   				                   			
	                   			//자녀
	                   			if(StringUtils.equals(eData.getAttribute("chldDdcClCd"),"Y")){
	                   				ye001VO.set자녀("1");
	                   			}else{
	                   				ye001VO.set자녀("2");
	                   			}	
	                   			
	                   			//출산입양 (해당없음 : "N", 첫째 : "1", 둘째 : "2", 셋째이상 : "3")
	                   			if(StringUtils.equals(eData.getAttribute("chbtAtprDdcClCd"),"N")){
	                   				ye001VO.set출산입양("4");
	                   			}else{
	                   				ye001VO.set출산입양(eData.getAttribute("chbtAtprDdcClCd"));
	                   			}		                   			
	                   				      
	                   			
	                   			int 나이 = 0;
	                   			String txprDscmNoCntn = eData.getAttribute("txprDscmNoCntn");
	                   			switch (txprDscmNoCntn.substring(6, 7)) {
	                   				case "1":
	                   				case "2":
	                   				case "5":
	                   				case "6":
	                   					나이 = StringUtil.strPaserInt(계약년도) - StringUtil.strPaserInt("19" + txprDscmNoCntn.substring(0, 2));
	                   					break;
	                   				case "3":
	                   				case "4":
	                   				case "7":
	                   				case "8":
	                   					나이 = StringUtil.strPaserInt(계약년도) - StringUtil.strPaserInt("20" + txprDscmNoCntn.substring(0, 2));
	                   					break;
	                   				case "9":
	                   				case "0":
	                   					나이 = StringUtil.strPaserInt(계약년도) - StringUtil.strPaserInt("18" + txprDscmNoCntn.substring(0, 2));
	                   					break;
	                   			}
	                   			ye001VO.set개인식별번호(SecurityUtil.getinstance().aesEncode(txprDscmNoCntn));
	                   			ye001VO.set나이(String.valueOf(나이));	                   			
	                   			ye001VO.setResId(txprDscmNoCntn);
	                   			
	                   			//본인일 경우
	                   			if(StringUtils.equals(eData.getAttribute("suptFmlyRltClCd"), "0") && StringUtils.isEmpty(this.PDF본인_사용자ID)){
	                   				
	                   		        String 개인식별번호 = SecurityUtil.getinstance().aesEncode(txprDscmNoCntn);

	                   		        YE001VO vo = new YE001VO();
	                   		        vo.set계약ID(this.계약ID);
	                   		        vo.set사용자ID(this.근로자_사용자ID);
	                   		        vo.set개인식별번호(개인식별번호);

	                   		        YE001VO result = ye001Service.getYE001ID(vo);
	                   		        
	                   		        if(result != null){
	                   		        	ye001VO.set사용자ID(result.get사용자ID());
	                   		        	this.PDF본인_사용자ID = result.get사용자ID();
	                   		        }
	                   				
//		                            YE001VO ye001 = getID(txprDscmNoCntn, ye001VO.get성명());
//		                            if(ye001 != null){
//		                            	this.PDF본인_사용자ID = ye001.get사용자ID();
//		                            }
	                   			}
	                            
	                   			hashMap.put(eData.getAttribute("txprDscmNoCntn"), ye001VO);
	                   			
	                   		}
	                   		
	                   	}
                   }
                   
       			}
       			
       		}
       		
       }
       
       //해쉬맵 파싱
       for(Object key : hashMap.keySet()) {     	   
    	   YE001VO vo = hashMap.get(key);
    	   
    	   if(vo != null){
    		   listYE001.add(vo);
    	   }
    	   
       }
   }

   
    /**
     *
     * 국세청 PDF 파일 정보 DB 저장 - 연말정산 간소화
     *
     * @param loginVO
     * @param xml
     * @throws Exception
     */
//    private void parseXML(SessionVO loginVO, File xml) throws Exception {
    private void saveXmlData(SessionVO loginVO, Document doc) throws Exception {
    
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        factory.setIgnoringElementContentWhitespace(true);
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document doc = builder.parse(xml);

        YE051VO ye051 = new YE051VO();	//국민연금
        List<YE052VO> listYE052 = new ArrayList<>();	//특별소득공제 보험료
        List<YE101VO> listYE101 = new ArrayList<>();	//주택임차차입금원리금상환액
        List<YE104VO> listYE104 = new ArrayList<>();	//장기주택저당차입금이자상환액
        List<YE106VO> listYE106 = new ArrayList<>();	//개인연금저축
        List<YE107VO> listYE107 = new ArrayList<>();	//주택마련저축
        List<YE108VO> listYE108 = new ArrayList<>();	//신용카드
        List<YE109VO> listYE109 = new ArrayList<>();	//장기집합투자증권저축
        YE201VO ye201 = new YE201VO();					//소기업소상공인공제부금
        List<YE301VO> listYE301 = new ArrayList<>();	//퇴직연금계좌
        List<YE302VO> listYE302 = new ArrayList<>();	//연금저축계좌
        List<YE401VO> listYE401 = new ArrayList<>();
        List<YE402VO> listYE402 = new ArrayList<>();	//의료비
        List<YE403VO> listYE403 = new ArrayList<>();	//교육비
        List<YE404VO> listYE404 = new ArrayList<>();	//기부금

        NodeList list = doc.getElementsByTagName("form");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                switch (element.getAttribute("form_cd")) {
                    case "A102Y":
                    case "A102M":
                        // 보장성보험
                        parseA(listYE401, element.getChildNodes());
                        break;

                    case "B101Y":
                    case "B101D":
                        // 의료비
                        parseB(listYE402, element.getChildNodes());
                        break;

                    case "C102Y":
                    case "C102M":
                        // 교육비(유초중고,대학,기타)
                        parseC_1(listYE403, element.getChildNodes());
                        break;
                    case "C301Y":
                    case "C301M": // 교육비(교복구입비)
                        parseC_3(listYE403, element.getChildNodes());
                        break;

                    case "C202Y":
                    case "C202M": // 교육비(직업훈련비)
                    case "C401Y":
                    case "C401M": // 교육비(학자금대출)
                        parseC_24(listYE403, element.getChildNodes());
                        break;

                    case "D101Y":
                    case "D101M":
                        // 개인연금저축
                        parseD(listYE106, element.getChildNodes());
                        break;

                    case "E102Y":
                    case "E102M":
                        // 연금저축
                        parseE(listYE302, element.getChildNodes());
                        break;

                    case "F102Y":
                    case "F102M":
                        // 퇴직연금
                        parseF(listYE301, element.getChildNodes());
                        break;

                    case "G106Y":
                    case "G106M": // 신용카드
                    case "G107Y": // 신용카드 (2018년)
                    case "G107M": // 신용카드 (2018년)	
                    case "G206M": // 현금영수증
                    case "G207M": // 현금영수증 (2018년)
                    case "G306Y":
                    case "G306M": // 직불카드
                    case "G307Y": // 직불카드 (2018년)
                    case "G307M": // 직불카드 (2018년)
                        parseG(listYE108, element.getChildNodes());
                        break;

                    case "J101Y":
                    case "J101M":
                        // 주택임차차입금 원리금상환액
                        parseJ_1(listYE101, element.getChildNodes());
                        break;
                    case "J203Y":
                    case "J203M":
                        // 장기주택저당차입금 이자상환액
                        // TODO 장기주택저당차입금 이자상환액 확인 필요!
                        parseJ_2(listYE104, element.getChildNodes());
                        break;
                    case "J301Y":
                    case "J301M":
                        // 주택마련저축
                        parseJ_3(listYE107, element.getChildNodes());
                        break;

                    case "K101M":
                        // 소기업소상공인공제부금
                        parseK(ye201, element.getChildNodes());
                        break;

                    case "L102Y":
                    case "L102D":
                        // 기부금
                        parseL(listYE404, element.getChildNodes());
                        break;

                    case "N101Y":
                    case "N101M":
                        // 장기집합투자증권저축
                        parseN(listYE109, element.getChildNodes());
                        break;

                    case "O101M":
                        // 건강보험, 장기요양보험
                        parseO(listYE052, element.getChildNodes());
                        break;

                    case "P102M":
                        // 국민연금
                        parseP(ye051, element.getChildNodes());
                        break;
                }
            }
        }

        if (this.PDF본인_사용자ID != null) {
            ywPdfService.saveYWPdf(계약ID, this.PDF본인_사용자ID, loginVO.getUserId(), ye051, listYE052, listYE101, listYE104, listYE106, listYE107,
                    listYE108, listYE109, ye201, listYE301, listYE302, listYE401, listYE402, listYE403, listYE404,this.계약년도);
        }
    }

    
    private void parseA(List<YE401VO> listVO, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList sublist = element.getElementsByTagName("data");

                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");
                        String acc_no = eData.getAttribute("acc_no");
                        String insu1_resid = eData.getElementsByTagName("insu1_resid").item(0).getTextContent();
                        String insu1_nm = eData.getElementsByTagName("insu1_nm").item(0).getTextContent();

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        YE001VO ye001 = getID(insu1_resid, insu1_nm);
                        if (ye001 == null) {
                            continue;
                        }

                        YE401VO vo = new YE401VO();
                        vo.setDbMode("C");
                        vo.set계약ID(계약ID);
                        vo.set사용자ID(ye001.get사용자ID());
                        vo.set부양가족ID(ye001.get부양가족ID());
                        vo.set자료구분코드("0");

                        if ("G0001".equals(dat_cd)) {
                            vo.set보험구분코드("1");
                        } else if ("G0002".equals(dat_cd)) {
                            vo.set보험구분코드("2");
                        }

                        vo.set납입금액(sum);
                        vo.set차감금액(0);

                        listVO.add(vo);
                    }
                }
            }
        }
    }

    private void parseB(List<YE402VO> listVO, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");
                        String busnid = eData.getAttribute("busnid");
                        String trade_nm = eData.getAttribute("trade_nm");

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        int count = eData.getElementsByTagName("amt") == null ? 0 : eData.getElementsByTagName("amt").getLength();

                        if (count == 0) {
                            // 1건으로 처리
                            count = 1;
                        }

                        YE402VO vo = new YE402VO();
                        vo.setDbMode("C");
                        vo.set계약ID(계약ID);
                        vo.set사용자ID(ye001.get사용자ID());
                        vo.set부양가족ID(ye001.get부양가족ID());
                        vo.set자료구분코드("0");
                        vo.set의료비증빙코드("1");

                        if ("0".equals(ye001.get가족관계()) || !"4".equals(ye001.get장애인())
                                || StringUtil.strPaserInt(ye001.get나이()) >= 65) {
                            vo.set공제종류코드("1");
                        } else {
                            vo.set공제종류코드("2");
                        }

                        vo.set지급처_사업자등록번호(busnid);
                        vo.set상호(trade_nm);
                        vo.set건수(count);

                        if ("G0026".equals(dat_cd)) {
                            vo.set의료비유형("2");
                        } else {
                            vo.set의료비유형("1");
                        }

                        vo.set지출액(sum);
                        vo.set차감금액(0);

                        listVO.add(vo);
                    }
                }
            }
        }
    }

    private void parseC_1(List<YE403VO> listVO, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");
                        String edu_tp = eData.getAttribute("edu_tp");
                        String edu_cl = eData.getAttribute("edu_cl");

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());


                        /*boolean find = false;
                        
                        for (YE403VO vo : listVO) {
                            if (ye001.get부양가족ID().equals(vo.get부양가족ID())) {
                                find = true;
                                // 부양가족이 장애인이고
                                // 교육비종류가 H:사회복지시설, J:장애인재활교육기관, K:발달재활서비스제공기관(만18세미만) 이면
                                // 공제종류를 장애인으로 몰아줌
                                if (!"4".equals(ye001.get장애인()) && ("H".equals(edu_tp) || "J".equals(edu_tp)
                                        || ("K".equals(edu_tp) && StringUtil.strPaserInt(ye001.get나이()) < 18))) {
                                    vo.set공제종류코드("5");
                                }

                                if ("B".equals(edu_cl)) {
                                    vo.set체험학습비(vo.get체험학습비() + sum);
                                } else {
                                    vo.set공납금(vo.get공납금() + sum);
                                }
                            }
                            // TODO 2018년도 장애인특수 추가해야함
                        }*/

//                        if (!find) {
                            YE403VO vo = new YE403VO();
                            vo.setDbMode("C");
                            vo.set계약ID(계약ID);
                            vo.set사용자ID(ye001.get사용자ID());
                            vo.set부양가족ID(ye001.get부양가족ID());
                            vo.set자료구분코드("0");
                     
                            //현재 나이 계산
//                            int nowAge = CommonUtil.getAgeResid(resid);
                            int nowAge = CommonUtil.getContractAge(StringUtil.strPaserInt(this.계약년도), resid);
                           
                            if ("0".equals(ye001.get가족관계())) {
                                vo.set공제종류코드("1");
                            } else if ("G0006".equals(dat_cd)) {
                                vo.set공제종류코드("4");
                            } else if ("G0004".equals(dat_cd) || "G0008".equals(dat_cd)) {
                                if ("1".equals(edu_tp) || "8".equals(edu_tp)){
                                    vo.set공제종류코드("2");
                                }else if ("9".equals(edu_tp) || "F".equals(edu_tp) || "G".equals(edu_tp)){                                 	
                                	if(nowAge < 7){
                                		vo.set공제종류코드("2");
                                	}else{
                                		vo.set공제종류코드("3");
                                	}                                
                                } else {
                                    vo.set공제종류코드("3");
                                }
                            }
                            /* 유치원, 보육시설 : 취학전아동으로 등록 */
                            else if("G0007".equals(dat_cd)){
                            	if ("1".equals(edu_tp) || "8".equals(edu_tp)){
                            		vo.set공제종류코드("2");
                            	}else if ("9".equals(edu_tp) || "F".equals(edu_tp) || "G".equals(edu_tp)){
                                    if(nowAge < 7){
                                    	vo.set공제종류코드("2");
                                    }                                    
                            	}
                            }
                            
                            // 부양가족이 장애인이고
                            // 교육비종류가 H:사회복지시설, J:장애인재활교육기관, K:발달재활서비스제공기관(만18세미만)
                            if (!"4".equals(ye001.get장애인()) && ("H".equals(edu_tp) || "J".equals(edu_tp)
                                    || ("K".equals(edu_tp) && StringUtil.strPaserInt(ye001.get나이()) < 18))) {
                                vo.set공제종류코드("5");
                            }                            
                            
                            if ("B".equals(edu_cl)) {
                                vo.set공납금(0);
                                vo.set체험학습비(sum);
                            } else {
                                vo.set공납금(sum);
                                vo.set체험학습비(0);
                            }
                            vo.set공납금_차감금액(0);
                            vo.set체험학습비_차감금액(0);

                            vo.set교복구입비(0);
                            vo.set교복구입비_차감금액(0);

                            // TODO 2018년도 장애인특수 추가해야함

                            listVO.add(vo);
//                        }
                    }
                }
            }
        }
    }

    private void parseC_3(List<YE403VO> listVO, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        boolean find = false;
                        for (YE403VO vo : listVO) {
                            if (ye001.get부양가족ID().equals(vo.get부양가족ID())) {
                                find = true;
                                vo.set교복구입비(vo.get교복구입비() + sum);
                            }
                        }

                        if (!find) {
                            YE403VO vo = new YE403VO();
                            vo.setDbMode("C");
                            vo.set계약ID(계약ID);
                            vo.set사용자ID(ye001.get사용자ID());
                            vo.set부양가족ID(ye001.get부양가족ID());
                            vo.set자료구분코드("0");
                            vo.set공제종류코드("3");
                            vo.set공납금(0);
                            vo.set공납금_차감금액(0);
                            vo.set교복구입비(sum);
                            vo.set교복구입비_차감금액(0);
                            vo.set체험학습비(0);
                            vo.set체험학습비_차감금액(0);

                            listVO.add(vo);
                        }
                    }
                }
            }
        }
    }

    private void parseC_24(List<YE403VO> listVO, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }


                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        boolean find = false;
                        for (YE403VO vo : listVO) {
                            if (ye001.get부양가족ID().equals(vo.get부양가족ID())) {
                                find = true;
                                vo.set공납금(vo.get공납금() + sum);
                            }
                        }

                        if (!find) {
                            YE403VO vo = new YE403VO();
                            vo.set계약ID(계약ID);
                            vo.set사용자ID(ye001.get사용자ID());
                            vo.setDbMode("C");
                            vo.set부양가족ID(ye001.get부양가족ID());
                            vo.set자료구분코드("0");
                            vo.set공제종류코드("1");
                            vo.set공납금(sum);
                            vo.set공납금_차감금액(0);
                            vo.set교복구입비(0);
                            vo.set교복구입비_차감금액(0);
                            vo.set체험학습비(0);
                            vo.set체험학습비_차감금액(0);

                            listVO.add(vo);
                        }
                    }
                }
            }
        }
    }

    private void parseD(List<YE106VO> listVO, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");
                        String acc_no = eData.getAttribute("acc_no");
                        String com_cd = eData.getElementsByTagName("com_cd").item(0).getTextContent();

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        YE106VO vo = new YE106VO();
                        vo.setDbMode("C");
                        vo.set계약ID(계약ID);
                        vo.set사용자ID(ye001.get사용자ID());
                        vo.set자료구분코드("0");
                        vo.set연금저축구분("21");
                        vo.set금융회사등명칭(com_cd);
                        vo.set계좌번호_증권번호(acc_no);
                        vo.set납입금액(sum);
                        vo.set차감금액(0);
                        listVO.add(vo);
                    }
                }
            }
        }
    }

    private void parseE(List<YE302VO> listVO, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");
                        String acc_no = eData.getAttribute("acc_no");
                        String com_cd = eData.getElementsByTagName("com_cd").item(0).getTextContent();
                        String ddct_bs_ass_amt = eData.getElementsByTagName("ddct_bs_ass_amt").item(0).getTextContent();

                        YE302VO vo = new YE302VO();
                        vo.setDbMode("C");
                        vo.set계약ID(계약ID);
                        vo.set사용자ID(ye001.get사용자ID());
                        vo.set자료구분코드("0");
                        vo.set연금저축구분코드("22");
                        vo.set금융회사등명칭(com_cd);
                        vo.set계좌번호_증권번호(acc_no);
                        vo.set납입금액(StringUtil.strPaserInt(ddct_bs_ass_amt));
                        vo.set차감금액(0);
                        listVO.add(vo);
                    }
                }
            }
        }
    }

    private void parseF(List<YE301VO> listVO, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");
                        String acc_no = eData.getAttribute("acc_no");
                        String com_cd = eData.getElementsByTagName("com_cd").item(0).getTextContent();
                        String pension_cd = eData.getElementsByTagName("pension_cd").item(0).getTextContent();
                        String ddct_bs_ass_amt = eData.getElementsByTagName("ddct_bs_ass_amt").item(0).getTextContent();

                        YE301VO vo = new YE301VO();
                        vo.setDbMode("C");
                        vo.set계약ID(계약ID);
                        vo.set사용자ID(ye001.get사용자ID());
                        vo.set자료구분코드("0");
                        vo.set퇴직연금구분코드(pension_cd);
                        vo.set금융회사등명칭(com_cd);
                        vo.set계좌번호_증권번호(acc_no);
                        vo.set납입금액(StringUtil.strPaserInt(ddct_bs_ass_amt));
                        vo.set차감금액(0);
                        listVO.add(vo);
                    }
                }
            }
        }
    }

    private void parseG(List<YE108VO> listVO, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }


                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");
                        String use_place_cd = eData.getAttribute("use_place_cd");

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        boolean find = false;
                        for (YE108VO vo : listVO) {
                            if (ye001.get부양가족ID().equals(vo.get부양가족ID())) {
                                find = true;
                                if ("1".equals(use_place_cd)) {
                                    if ("G0012".equals(dat_cd)) {
                                        vo.set신용카드(vo.get신용카드() + sum);
                                    } else if ("G0013".equals(dat_cd)) {
                                        vo.set현금영수증(vo.get현금영수증() + sum);
                                    } else if ("G0014".equals(dat_cd)) {
                                        vo.set직불_선불카드(vo.get직불_선불카드() + sum);
                                    }
                                } else if ("2".equals(use_place_cd)) {
                                    vo.set전통시장(vo.get전통시장() + sum);
                                } else if ("3".equals(use_place_cd)) {
                                    vo.set대중교통(vo.get대중교통() + sum);
                                } else if ("4".equals(use_place_cd)) {
                                    vo.set도서공연(vo.get도서공연() + sum);
                                }
                                
                                // TODO 2018년도 도서공연 추가해야함
                            }
                        }

                        if (!find) {
                            YE108VO vo = new YE108VO();
                            vo.setDbMode("C");
                            vo.set계약ID(계약ID);
                            vo.set사용자ID(ye001.get사용자ID());
                            vo.set부양가족ID(ye001.get부양가족ID());
                            vo.set자료구분코드("0");
                            vo.set기간구분코드("1");
                            if ("1".equals(use_place_cd)) {
                                if ("G0012".equals(dat_cd)) {
                                    vo.set신용카드(sum);
                                    vo.set현금영수증(0);
                                    vo.set직불_선불카드(0);
                                } else if ("G0013".equals(dat_cd)) {
                                    vo.set신용카드(0);
                                    vo.set현금영수증(sum);
                                    vo.set직불_선불카드(0);
                                } else if ("G0014".equals(dat_cd)) {
                                    vo.set신용카드(0);
                                    vo.set현금영수증(0);
                                    vo.set직불_선불카드(sum);
                                }
                                vo.set전통시장(0);
                                vo.set대중교통(0);
                                vo.set도서공연(0);
                            } else if ("2".equals(use_place_cd)) {
                                vo.set신용카드(0);
                                vo.set현금영수증(0);
                                vo.set직불_선불카드(0);
                                vo.set전통시장(sum);
                                vo.set대중교통(0);
                                vo.set도서공연(0);
                            } else if ("3".equals(use_place_cd)) {
                                vo.set신용카드(0);
                                vo.set현금영수증(0);
                                vo.set직불_선불카드(0);
                                vo.set전통시장(0);
                                vo.set대중교통(sum);
                                vo.set도서공연(0);
                            } else if ("4".equals(use_place_cd)) {
                                vo.set신용카드(0);
                                vo.set현금영수증(0);
                                vo.set직불_선불카드(0);
                                vo.set전통시장(0);
                                vo.set대중교통(0);
                                vo.set도서공연(sum);
                            }

                            // TODO 2018년도 도서공연 추가해야함
//                            vo.set도서공연(0);

                            listVO.add(vo);
                        }
                    }
                }
            }
        }
    }

    private void parseJ_1(List<YE101VO> listVO, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");
                        String trade_nm = eData.getAttribute("trade_nm");
                        
                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        YE101VO vo = new YE101VO();
                        vo.setDbMode("C");
                        vo.set계약ID(계약ID);
                        vo.set사용자ID(ye001.get사용자ID());
                        vo.set차입구분("1");
                        vo.set취급기관(trade_nm);
                        vo.set국세청자료(sum);
                        vo.set차감금액(0);
                        vo.set기타자료(0);
                        listVO.add(vo);
                    }
                }
            }
        }
    }

    private void parseJ_2(List<YE104VO> listVO, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
                        String dat_cd = eData.getAttribute("dat_cd");
                        String start_dt = eData.getElementsByTagName("start_dt").item(0).getTextContent();
                        String repay_years = eData.getElementsByTagName("repay_years").item(0).getTextContent();
                        String fixed_rate_debt = eData.getElementsByTagName("fixed_rate_debt").item(0).getTextContent();
                        String not_defer_debt = eData.getElementsByTagName("not_defer_debt").item(0).getTextContent();

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        String 구분코드;
                        int startYear = StringUtil.strPaserInt(start_dt.substring(0, 4));
                        int years = StringUtil.strPaserInt(repay_years);
                        if (startYear <= 2011) {
                            if (years < 15) {
                                구분코드 = "1";
                            } else if (years < 30) {
                                구분코드 = "2";
                            } else {
                                구분코드 = "3";
                            }
                        } else if (startYear < 2015) {
                            if (years < 15) {
                                continue;
                            }

                            if (!"0".equals(fixed_rate_debt) || !"0".equals(not_defer_debt)) {
                                구분코드 = "4";
                            } else {
                                구분코드 = "5";
                            }
                        } else {
                            if (years >= 15) {
                                if (!"0".equals(fixed_rate_debt) && !"0".equals(not_defer_debt)) {
                                    구분코드 = "6";
                                } else if (!"0".equals(fixed_rate_debt) || !"0".equals(not_defer_debt)) {
                                    구분코드 = "7";
                                } else {
                                    구분코드 = "8";
                                }
                            } else if (years >= 10) {
                                구분코드 = "9";
                            } else {
                                continue;
                            }
                        }

                        boolean find = false;
                        for (YE104VO vo : listVO) {
                            if (구분코드.equals(vo.get구분코드())) {
                                find = true;
                                vo.set국세청자료(vo.get국세청자료() + sum);
                            }
                        }

                        if (!find) {
                            YE104VO vo = new YE104VO();
                            vo.setDbMode("C");
                            vo.set계약ID(계약ID);
                            vo.set사용자ID(ye001.get사용자ID());
                            vo.set구분코드(구분코드);
                            vo.set국세청자료(sum);
                            vo.set차감금액(0);
                            vo.set기타자료(0);
                            listVO.add(vo);
                        }
                    }
                }
            }
        }
    }

    private void parseJ_3(List<YE107VO> listVO, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
//                        String dat_cd = eData.getAttribute("dat_cd");
                        String acc_no = eData.getAttribute("acc_no");
                        String saving_gubn = eData.getElementsByTagName("saving_gubn").item(0).getTextContent();
                        String reg_dt = eData.getElementsByTagName("reg_dt").item(0).getTextContent();
                        String com_cd = eData.getElementsByTagName("com_cd").item(0).getTextContent();

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        YE107VO vo = new YE107VO();
                        vo.setDbMode("C");
                        vo.set계약ID(계약ID);
                        vo.set사용자ID(ye001.get사용자ID());
                        vo.set자료구분코드("0");
                        vo.set주택마련저축구분("3" + saving_gubn);
                        vo.set금융회사등명칭(com_cd);
                        vo.set계좌번호_증권번호(acc_no);
                        vo.set가입일자(reg_dt);
                        vo.set납입금액(sum);
                        vo.set차감금액(0);
                        listVO.add(vo);
                    }
                }
            }
        }
    }

    private void parseK(YE201VO vo, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
//                        String dat_cd = eData.getAttribute("dat_cd");

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        vo.setDbMode("C");
                        vo.set계약ID(계약ID);
                        vo.set사용자ID(ye001.get사용자ID());
                        vo.set국세청_납입금액(sum);
                        vo.set국세청_차감금액(0);
                        vo.set기타_납입금액(0);

                        return;
                    }
                }
            }
        }
    }

    private void parseL(List<YE404VO> listYE404, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
//                        String dat_cd = eData.getAttribute("dat_cd");
                        String busnid = eData.getAttribute("busnid");
                        String trade_nm = eData.getAttribute("trade_nm");
                        String donation_cd = eData.getAttribute("donation_cd");

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());
                        int sbdy_apln_sum = StringUtil.strPaserInt(eData.getElementsByTagName("sbdy_apln_sum").item(0).getTextContent());
                        int conb_sum = StringUtil.strPaserInt(eData.getElementsByTagName("conb_sum").item(0).getTextContent());

                        int count = eData.getElementsByTagName("amt") == null ? 0 : eData.getElementsByTagName("amt").getLength();

                        if (count == 0) {
                            // 1건으로 처리
                            count = 1;
                        }

                        YE404VO ye404 = new YE404VO();
                        ye404.setDbMode("C");
                        ye404.set계약ID(계약ID);
                        ye404.set사용자ID(ye001.get사용자ID());
                        ye404.set부양가족ID(ye001.get부양가족ID());
                        ye404.set자료구분코드("0");
                        ye404.set기부코드(donation_cd);
                        ye404.set기부내용("1");
                        ye404.set기부처_사업자등록번호(busnid);
                        ye404.set상호(trade_nm);
                        ye404.set기부명세_건수(count);
                        ye404.set기부명세_공제금액(conb_sum);
                        ye404.set기부명세_공제대상기부금(sum);
                        ye404.set기부명세_기부장려금(sbdy_apln_sum);
                        ye404.set기부명세_기타(0);
                        listYE404.add(ye404);
                    }
                }
            }
        }
    }

    private void parseN(List<YE109VO> listVO, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
//                        String dat_cd = eData.getAttribute("dat_cd");
                        String secu_no = eData.getAttribute("secu_no");
                        String com_cd = eData.getElementsByTagName("com_cd").item(0).getTextContent();

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        YE109VO vo = new YE109VO();
                        vo.setDbMode("C");
                        vo.set계약ID(계약ID);
                        vo.set사용자ID(ye001.get사용자ID());
                        vo.set자료구분코드("0");
                        vo.set취급기관명(com_cd);
                        vo.set계좌번호_증권번호(secu_no);
                        vo.set납입금액(sum);
                        vo.set차감금액(0);
                        listVO.add(vo);
                    }
                }
            }
        }
    }

    private void parseO(List<YE052VO> listVO, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
//                        String dat_cd = eData.getAttribute("dat_cd");
                        Element eSum = (Element) eData.getElementsByTagName("sum").item(0);
                        String hi_ntf = eSum.getAttribute("hi_ntf");
                        String hi_pmt = eSum.getAttribute("hi_pmt");
                        String ltrm_ntf = eSum.getAttribute("ltrm_ntf");
                        String ltrm_pmt = eSum.getAttribute("ltrm_pmt");


                        YE052VO vo = new YE052VO();
                        vo.setDbMode("C");
                        vo.set계약ID(계약ID);
                        vo.set사용자ID(ye001.get사용자ID());
                        vo.set보험료구분("1");
                        vo.set국세청_납입금액(StringUtil.strPaserInt(hi_ntf) + StringUtil.strPaserInt(hi_pmt));
                        vo.set국세청_차감금액(0);
                        vo.set추가납입금액(0);
                        listVO.add(vo);

                        vo = new YE052VO();
                        vo.set계약ID(계약ID);
                        vo.set사용자ID(ye001.get사용자ID());
                        vo.setDbMode("C");
                        vo.set보험료구분("2");
                        vo.set국세청_납입금액(StringUtil.strPaserInt(ltrm_ntf) + StringUtil.strPaserInt(ltrm_pmt));
                        vo.set국세청_차감금액(0);
                        vo.set추가납입금액(0);
                        listVO.add(vo);

                        return;
                    }
                }
            }
        }
    }

    private void parseP(YE051VO vo, NodeList listNode) throws Exception {
        for (int i = 0; i < listNode.getLength(); i++) {
            Node node = listNode.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String resid = element.getAttribute("resid");
                String name = element.getAttribute("name");
                YE001VO ye001 = getID(resid, name);
                if (ye001 == null) {
                    continue;
                }

                NodeList sublist = element.getElementsByTagName("data");
                for (int j = 0; j < sublist.getLength(); j++) {
                    Node nData = sublist.item(j);

                    if (nData.getNodeType() == Node.ELEMENT_NODE) {
                        Element eData = (Element) nData;
//                        String dat_cd = eData.getAttribute("dat_cd");

                        int sum = StringUtil.strPaserInt(eData.getElementsByTagName("sum").item(0).getTextContent());

                        vo.setDbMode("C");
                        vo.set계약ID(계약ID);
                        vo.set사용자ID(ye001.get사용자ID());
                        vo.set보험료구분("1");
                        vo.set국세청_납입금액(sum);
                        vo.set국세청_차감금액(0);
                        vo.set추가납입금액(0);

                        return;
                    }
                }
            }
        }
    }

    private YE001VO getID(String resid, String name) throws Exception {
//        SecurityUtil securityUtil = new SecurityUtil();
        String 개인식별번호 = SecurityUtil.getinstance().aesEncode(resid);

        if (부양가족 != null) {
            for (YE001VO item : 부양가족) {
                if (개인식별번호.equals(item.get개인식별번호()) || resid.equals(item.get개인식별번호())) {
                    return item;
                }
            }
        }

        YE001VO vo = new YE001VO();
        vo.set계약ID(계약ID);
        vo.set사용자ID(this.근로자_사용자ID);
        vo.set개인식별번호(개인식별번호);

        YE001VO result = ye001Service.getYE001ID(vo);
        
        if (result == null) {
            // PDF에 있는 주민등록번호로 부양가족 조회가 안된경우
            if (추가부양가족 == null) {
                추가부양가족 = new ArrayList<>();
            } else {
                for (YE001VO item : 추가부양가족) {
                    if (resid.equals(item.get개인식별번호())) {
                        return null;
                    }
                }
            }

            YE001VO ye001 = new YE001VO();

            ye001.set계약ID(계약ID);
            ye001.set사용자ID(this.근로자_사용자ID);

            switch (resid.substring(6, 7)) {
                case "5":
                case "6":
                case "7":
                case "8":
                    ye001.set내외국인("2");
                    break;
                default:
                    ye001.set내외국인("1");
                    break;
            }
            ye001.set성명(name);
            ye001.set개인식별번호(resid);

            int year = StringUtil.strPaserInt(계약년도);
            int 나이 = 0;
            switch (resid.substring(6, 7)) {
                case "1":
                case "2":
                case "5":
                case "6":
                    나이 = year - StringUtil.strPaserInt("19" + resid.substring(0, 2));
                    break;
                case "3":
                case "4":
                case "7":
                case "8":
                    나이 = year - StringUtil.strPaserInt("20" + resid.substring(0, 2));
                    break;
                case "9":
                case "0":
                    나이 = year - StringUtil.strPaserInt("18" + resid.substring(0, 2));
                    break;
            }
            ye001.set나이(String.valueOf(나이));

            ye001.set기본공제("2");
            ye001.set부녀자("2");
            ye001.set한부모("2");
            if (나이 >= 70) {
                ye001.set경로우대("1");
            } else {
                ye001.set경로우대("2");
            }
            ye001.set장애인("4");
            ye001.set자녀("2");
            ye001.set출산입양("4");

            추가부양가족.add(ye001);
            
            logger.debug("# PDF getID() 추가부양가족   : " + 추가부양가족.size() );
            
        } else if ("0".equals(result.get가족관계())) {
               	
       		this.PDF본인_사용자ID = result.get사용자ID();

            // 본인인 경우 부양가족 리스트 가져옴
            YE001VO ye001 = new YE001VO();
            ye001.set계약ID(계약ID);
            ye001.set사용자ID(result.get사용자ID());
            ye001.setStartPage(0);
            ye001.setEndPage(9999);
            부양가족 = ye001Service.getYE001(ye001, userType);
            
            logger.debug("# PDF getID() 부양가족   : " + 부양가족.size() );
        }

        return result;
    }
}
