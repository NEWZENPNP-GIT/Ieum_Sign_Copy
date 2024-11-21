package com.ezsign.biztalk.controller;


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
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.biztalk.service.BizTalkService;
import com.ezsign.biztalk.vo.BizTalkKKOVO;

@Controller
@RequestMapping("/biztalk/")
public class BizTalkController extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "bizTalkService")
	private BizTalkService bizTalkService;
	
	@RequestMapping(method = RequestMethod.GET , value = "getBizTalkList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBizTalkListCtrl(@ModelAttribute("BizTalkKKOVO") BizTalkKKOVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getBizTalkList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId() + "/"+vo.getUserId());
				vo.setBizId(loginVO.getBizId());
				vo.setTable("ata_mmt_log_"+StringUtil.substring(vo.getRegDate(), 0, 6));
				
				List<BizTalkKKOVO> result= bizTalkService.getBizTalkList(vo);
				
				if(result != null && result.size() != 0){
					jsonObject.put("success", true);
					jsonObject.put("total", result.size());
					jsonObject.put("data", result);
				}else{
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "sendBizTalk.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendBizTalkCtrl(@ModelAttribute("BizTalkKKOVO") BizTalkKKOVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean result = false;
		
		try {
			System.out.println(":::::::::::::::::::: sendBizTalk :::::::::::::::::::");
			
			System.out.println("bizId=>"+vo.getBizId());

			if(StringUtil.isNotNull(vo.getBizId())) {
				result = bizTalkService.sendBizTalk(vo);
			} else {
				status = HttpStatus.BAD_REQUEST;
			}
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	

}
