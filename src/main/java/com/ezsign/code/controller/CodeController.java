package com.ezsign.code.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.ezsign.code.vo.CodeVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;


@Controller
@RequestMapping("/code/")
public class CodeController extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "codeService")
	private CodeService codeService;
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST} , value = "getCodeList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getCodeListCtrl(@ModelAttribute("CodeVO") CodeVO vo, HttpServletRequest request) throws Exception
	{
		// 공통코드  리스트
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getCodeList :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				List<CodeVO> result= codeService.getCodeList(vo);
				
				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
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
