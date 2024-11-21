package com.ezsign.fcm.controller;

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

import com.ezsign.common.controller.BaseController;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.fcm.service.PushSendService;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/fcm/")
public class FcmController extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "pushSendService")
	private PushSendService pushSendService;

	
	@RequestMapping(method = {RequestMethod.POST} , value = "pushSend.do")
	@ResponseBody
	public ResponseEntity<JSONObject> pushSendCtrl(String bizId, String userId, String message, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean result = false;
		
		try {
			System.out.println(":::::::::::::::::::: pushSend :::::::::::::::::::");
			if (bizId == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
			
				result = pushSendService.candySendPushMessage(bizId, userId, message);
				jsonObject.put("success", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


}
