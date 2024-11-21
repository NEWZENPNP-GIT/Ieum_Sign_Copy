package com.ezsign.status.controller;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.common.controller.BaseController;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.HttpUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.status.service.StatusService;
import com.ezsign.status.vo.StatusVO;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("/status/")
public class StatusController extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "statusService")
	private StatusService statusService;
	
	
	//contId리스트로 전자문서 리스트 반환
	@RequestMapping(method = RequestMethod.GET , value = "downloadStatusExcel.do")
	@ResponseBody
	public ResponseEntity<JSONObject> downloadStatusExcelCtrl(@ModelAttribute("StatusVO") StatusVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		HttpStatus status = HttpStatus.OK;
		int result = 0;
		
		try {
			System.out.println(":::::::::::::::::::: downloadStatusExcel :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String excelFilePath = MultipartFileUtil.getSystemTempPath() + MultipartFileUtil.SEPERATOR;
				String excelFileName = "사용현황_"+DateUtil.getDate()+".xlsx";
				
				FileUtil.createDirectory(excelFilePath);

				List<String> tmpList = new ArrayList();
				
				result = statusService.downloadStatusExcel(excelFilePath, excelFileName, vo);
				
				File file = new File(excelFilePath + excelFileName);
				HttpUtil.setResponseFile(request, response, file, excelFileName);
				
				if(!file.exists()) {
					status = HttpStatus.NO_CONTENT;
				} else if (result == 0) { 
					status = HttpStatus.NOT_MODIFIED;
				}
			}
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
		return new ResponseEntity(status);
	}
	
}
