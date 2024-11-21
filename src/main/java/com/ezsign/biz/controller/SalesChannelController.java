package com.ezsign.biz.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.biz.service.SalesChannelService;
import com.ezsign.biz.vo.SalesChannelVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/saleschannel/")
public class SalesChannelController extends BaseController {

	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "salesChannelService")
	private SalesChannelService salesChannelService;
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST} , value = "getSalesChannelList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getSalesChannelList(@ModelAttribute("SalesChannelVO") SalesChannelVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: selectSalesChannel :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				List<SalesChannelVO> result= salesChannelService.getSalesChannelList(vo);
				total = result.size();
				
				jsonObject.put("success", true);
				jsonObject.put("total", total);
				jsonObject.put("data", result);	
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "saveSalesChannel.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveSalesChannelCtrl(@RequestBody List<SalesChannelVO> list, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {			
			System.out.println(":::::::::::::::::::: saveSalesChannel :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				int result = salesChannelService.saveSalesChannel(list);
				
				success = true;
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
}
