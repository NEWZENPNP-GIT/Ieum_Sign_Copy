package com.ezsign.statistics.controller;


import java.util.List;

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

import net.sf.json.JSONObject;

import com.ezsign.code.service.CodeService;
import com.ezsign.common.controller.BaseController;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.statistics.service.MainGraphService;
import com.ezsign.statistics.vo.MainGraphVO;


@Controller
@RequestMapping("/statistics/")
public class MainGraphController extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "codeService")
	private CodeService codeService;
	
	@Resource(name = "mainGraphService")
	private MainGraphService mainGraphService;
	
	@RequestMapping(method = RequestMethod.GET , value = "getMainGraphist.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getMainGraphistCtrl(@ModelAttribute("MainGraphVO") MainGraphVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getMainGraphist :::::::::::::::::::");
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				List<MainGraphVO> result= mainGraphService.getMainGraphist(vo);
				
				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
				success = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	
}
