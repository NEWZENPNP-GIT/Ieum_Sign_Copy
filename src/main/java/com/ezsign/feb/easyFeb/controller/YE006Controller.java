package com.ezsign.feb.easyFeb.controller;

import java.util.HashMap;
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

import com.ezsign.feb.easyFeb.service.YE006Service;
import com.ezsign.feb.easyFeb.vo.YE006VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

import io.swagger.annotations.Api;
import net.sf.json.JSONObject;

@SuppressWarnings("NonAsciiCharacters")
@Controller
@Api(tags = "EasyYearTaxController", description = "손쉬운 연말정산")
@RequestMapping("/easyFeb/")
public class YE006Controller {

	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "ye006Service")
	private YE006Service ye006Service;
	
	@RequestMapping(method = RequestMethod.GET , value = "getYE006.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE006Ctrl(@RequestParam(value="사용자ID",  required=true) String 사용자ID,
			@RequestParam(value="계약ID",  required=true) String 계약ID,
			HttpServletRequest request) throws Exception{
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			System.out.println(":::::::::::::::::::: getYE006Ctrl :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
//				String 사용자ID = loginVO.getUserId();
//				String 계약ID = loginVO.getYearContractId();
				
				Map<String, String> params = new HashMap();
				params.put("사용자ID", 사용자ID);
				params.put("계약ID", 계약ID);

				YE006VO ye006VO = ye006Service.getYE006(params);
				jsonObject.put("data", ye006VO);
				success = true;
			}
		}catch (Exception ex) {
System.out.println(ex.toString());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "saveYE006.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveYE006Ctrl(@RequestParam(value="사용자ID",  required=true) String 사용자ID,
			@RequestParam(value="계약ID",  required=true) String 계약ID,
			@ModelAttribute("YE006VO") YE006VO vo, HttpServletRequest request) throws Exception {
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		String message = "";
		boolean success = false;

		try {
			System.out.println(":::::::::::::::::::: saveYE006 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
System.out.println("사용자ID: " + 사용자ID);
System.out.println("계약ID: " + 계약ID);
System.out.println("YE006VO: " + vo);
//				String 사용자ID = loginVO.getUserId();
//				String 계약ID = loginVO.getYearContractId();
				String bizId = loginVO.getBizId();
				
				vo.set사용자ID(사용자ID);
				vo.set계약ID(계약ID);
				vo.setBizId(bizId);
				
				int result = ye006Service.saveYE006(vo, request, bizId);
				if(result > 0){
					message = "차량 비과세가 저장 되었습니다.";
					success = true;	
				}else{
					message = "차량 비과세가 저장 않았습니다.";
					success = false;
				}
			}
		}catch (Exception ex) {
			message = "차량 비관세가 저장 않았습니다.";
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
		jsonObject.put("message", message);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "delYE006.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delYE006Ctrl(@ModelAttribute("YE006VO") YE006VO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: delYE006 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

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













