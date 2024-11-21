package com.ezsign.feb.report.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.feb.report.service.ReportService;
import com.ezsign.feb.report.vo.YE700ReportVO;
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/febreport/")
public class YE700Controller {

	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "reportService")
	private ReportService reportService;
	
	@Resource(name = "ys030Service")
	private YS030Service ys030Service;
	
	@RequestMapping(method = RequestMethod.GET , value = "getCodeList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getCodeListCtrl(@RequestParam(value="계약ID",  required=true) String 계약ID, HttpServletRequest request) throws Exception {
	
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			System.out.println(":::::::::::::::::::: getCodeListCtrl :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				// 사업장조회
				YS030VO ys030VO = new YS030VO();
				ys030VO.set계약ID(계약ID);
				ys030VO.setStartPage(0);
				ys030VO.setEndPage(99999);
				List<YS030VO> ys030List = ys030Service.getYS030List(ys030VO);

				jsonObject.put("ys030", ys030List);
				
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", true);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 공제현황
	@RequestMapping(method = RequestMethod.GET , value = "getYE700List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getgetYE700ListCtrl(@ModelAttribute("YE700ReportVO") YE700ReportVO ye700VO,
			HttpServletRequest request) throws Exception {
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getgetYE700ListCtrl :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null || Integer.parseInt(loginVO.getUserType()) < 5) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String bizId = loginVO.getBizId();
				String bizName = loginVO.getBizName();
				
				ye700VO.setBizId(bizId);
				ye700VO.setBizName(bizName);

				Map<String, Object> result = reportService.getYE700List(ye700VO);
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
	
	// 요약표
	@RequestMapping(method = RequestMethod.GET , value = "getYE700SummaryList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE700SummaryListCtrl(@ModelAttribute("YE700ReportVO") YE700ReportVO ye700VO,
			HttpServletRequest request) throws Exception {
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getYE700SummaryListCtrl :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null || Integer.parseInt(loginVO.getUserType()) < 5) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String bizId = loginVO.getBizId();
				String bizName = loginVO.getBizName();
				
				ye700VO.setBizId(bizId);
				ye700VO.setBizName(bizName);

				Map<String, Object> result = reportService.getYE700SummaryList(ye700VO);
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
