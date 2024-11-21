package com.ezsign.feb.system.controller;


import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;


@Controller
@RequestMapping("/febsystem/")
public class YS000Controller extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "ys000Service")
	private YS000Service ys000Service;
	
	@RequestMapping(method = RequestMethod.GET , value = "insYS000.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insYS000Ctrl(@ModelAttribute("YS000VO") YS000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: insYS000 :::::::::::::::::::");
						
			System.out.println("bizId=>"+vo.getBizId());
			
			ys000Service.insYS000(vo);
			
			success = true;
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 기업별 계약ID 조회
	@RequestMapping(method = RequestMethod.GET , value = "getYS000.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYS000Ctrl(@ModelAttribute("YS000VO") YS000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getYS000 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				List<YS000VO> result = ys000Service.getYS000List(vo);
				
				if(result != null && result.size() != 0){
					success = true;
					jsonObject.put("total", result.size());
					jsonObject.put("data", result);
				}else{
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
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
