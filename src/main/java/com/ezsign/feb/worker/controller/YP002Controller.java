package com.ezsign.feb.worker.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.vo.YP000VO;
import com.ezsign.feb.worker.service.YP003Service;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/febworker/")
public class YP002Controller extends BaseController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "yp003Service")
	private YP003Service yp003Service;
	
	@ApiOperation(value = "간이지급명세서 근무지별 원천명세 일괄 처리", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST , value = "saveTaxOverSumPayment.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveTaxOverSumPayment(@RequestBody List<YP000VO> list, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: saveTaxOverSumPayment :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("# bizId : " + loginVO.getBizId() );
				
				yp003Service.saveTaxOverSumPayment(list);
				
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.error(ex.getMessage(), ex );
			throw ex;
		}
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
}
