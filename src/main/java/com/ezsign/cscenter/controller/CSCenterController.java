package com.ezsign.cscenter.controller;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ezsign.biz.service.BizService;
import com.ezsign.common.controller.BaseController;
import com.ezsign.cscenter.service.CSCenterService;
import com.ezsign.cscenter.vo.CSRequestBizVO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

@Controller
@RequestMapping("/cs/")
public class CSCenterController extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "csCenterService")
	private CSCenterService csCenterService;
	
	// 기업회원가입 이메일 (비대면바우처포함)
	@RequestMapping(method = RequestMethod.POST , value = "requestBiz.do")
	@ResponseBody
	public ResponseEntity<JSONObject> requestBizCtrl(@ModelAttribute("CSRequestBizVO") CSRequestBizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: requestBiz :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setUserId(loginVO.getLoginId());
				// 회원가입 및 담당자 이메일로 회원가입유도 발송
				int result = 0;
				result = csCenterService.requestBiz(vo);
				vo.setCode(Integer.toString(result));				
				if(result==100) {
					vo.setMessage("이미 존재하는 기업입니다.");
				}else{
					csCenterService.insertBizRequst(vo);
				}
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

	// 기업회원가입 완료 이메일 (비대면바우처포함)
	@RequestMapping(method = RequestMethod.POST , value = "completedBiz.do")
	@ResponseBody
	public ResponseEntity<JSONObject> completedBizCtrl(@ModelAttribute("CSRequestBizVO") CSRequestBizVO vo, HttpServletRequest request) throws Exception
	{	// 21.05.27 기업정보 업데이트기능 추가수정		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: completedBiz :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+vo.getBizId());
				int result = csCenterService.completedBiz(vo);
				if(result>0) success = true;
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
