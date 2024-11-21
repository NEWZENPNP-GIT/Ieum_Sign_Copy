package com.ezsign.feb.report.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.feb.report.service.ReportService;
import com.ezsign.feb.report.vo.YE105ReportVO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/febreport/")
public class YE105Controller {

	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "reportService")
	private ReportService reportService;
	
	// 월세공제명세서
	@RequestMapping(method = RequestMethod.GET , value = "getYE105List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE105ListCtrl(@ModelAttribute("YE105ReportVO") YE105ReportVO ye105VO,
			HttpServletRequest request) throws Exception {
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getYE105ListCtrl :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null || Integer.parseInt(loginVO.getUserType()) < 5) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String bizId = loginVO.getBizId();
				String bizName = loginVO.getBizName();
				
				ye105VO.setBizId(bizId);
				ye105VO.setBizName(bizName);

				Map<String, Object> result = reportService.getYE105List(ye105VO);
				jsonObject.put("list", result.get("list"));
				jsonObject.put("sum", result.get("sum"));
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
}
