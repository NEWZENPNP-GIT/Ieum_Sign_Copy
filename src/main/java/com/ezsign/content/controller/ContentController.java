package com.ezsign.content.controller;


import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.common.controller.BaseController;
import com.ezsign.content.service.ContentService;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.HttpUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.jarvis.pdf.vo.PageVO;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("/content/")
public class ContentController extends BaseController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "contentService")
	private ContentService contentService;

	@RequestMapping(method = RequestMethod.GET , value = "downloadFile.do")
	@ResponseBody
	public ResponseEntity<String> downloadFileCtrl(@ModelAttribute("ContentVO") ContentVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: downloadFile :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				ContentVO contentVO    = contentService.getContent(vo);
				String contentFileName = contentVO.getFileTitle();
				File file 			   = new File(contentFileName);
				HttpUtil.setResponseFile(request, response, file, contentVO.getOrgFileName());
				if (!file.exists()) status = HttpStatus.NO_CONTENT;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
        return new ResponseEntity<>(status);
	}
	
	// PDF이미지 변환 및 이미지 리스트 정보제공
	@RequestMapping(method = {RequestMethod.POST} , value = "getPDFToImage.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPDFToImageCtrl(@ModelAttribute("ContentVO") ContentVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		
		try {
			logger.info(":::::::::::::::::::: getPDFToImage :::::::::::::::::::");
			String domainName = "";
			String serverName = request.getServerName();
			if (StringUtils.isNotEmpty(serverName)) domainName = request.getScheme() + "://" + request.getServerName();
			else 									domainName = System.getProperty("system.open.url");
			
			String tempPath 	  = MultipartFileUtil.getSystemTempPath();
			List<FileVO> fileList = MultipartFileUtil.getFileAddList(request, tempPath, true);
			
			if (fileList.size() == 1) {
				List<PageVO> result = contentService.getPDFToImage(vo, fileList);
				jsonObject.put("data", result);
				success = true;
			} else {
				jsonObject.put("code", "2112");
				jsonObject.put("message", "파일의 갯수("+fileList.size()+")가 하나만 가능합니다.");
			}
			jsonObject.put("domainName", domainName);
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

}
