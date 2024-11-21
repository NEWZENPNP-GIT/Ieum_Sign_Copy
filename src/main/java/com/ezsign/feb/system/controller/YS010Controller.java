package com.ezsign.feb.system.controller;


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
import com.ezsign.feb.system.service.YS010Service;
import com.ezsign.feb.system.vo.YS010VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("/febsystem/")
public class YS010Controller extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "ys010Service")
	private YS010Service ys010Service;
	
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST} , value = "getYS010List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYS010ListCtrl(@ModelAttribute("YS010VO") YS010VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getYS010List :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				List<YS010VO> result= ys010Service.getYS010List(vo);

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
	
	@RequestMapping(method = RequestMethod.POST , value = "insYS010.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insYS010Ctrl(@ModelAttribute("YS010VO") YS010VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: insYS010 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				ys010Service.insYS010(vo);
				
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


	@RequestMapping(method = RequestMethod.POST , value = "updYS010.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updYS010Ctrl(@ModelAttribute("YS010VO") YS010VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: updYS010 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				int result = ys010Service.updYS010(vo, loginVO.getBizId(), loginVO.getFebYear());
			
				if(result>0) success = true;
				jsonObject.put("total", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 연말정산 근로자 접속기간 체크
		@RequestMapping(method = RequestMethod.GET , value = "getWorkerDateCheck.do")
		@ResponseBody
		public ResponseEntity<JSONObject> getWorkerDateCheckCtrl(@ModelAttribute("YS010VO") YS010VO vo, HttpServletRequest request) throws Exception
		{
			JSONObject jsonObject = new JSONObject();
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
			
			HttpStatus status = HttpStatus.OK;
			boolean success = false;

			int result = 0;
			
			try {
				System.out.println(":::::::::::::::::::: getWorkerDateCheck :::::::::::::::::::");
				SessionVO loginVO = SessionUtil.getSession(request);
				
				if (loginVO == null) {
					status = HttpStatus.UNAUTHORIZED;
				} else {
					System.out.println("bizId=>"+loginVO.getBizId());
					vo.setBizId(loginVO.getBizId());
					
					result = ys010Service.getWorkerDateCheck(vo);

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
