package com.ezsign.feb.master.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.code.vo.CodeVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.service.YE901Service;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.master.vo.YP000VO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("/febmaster/")
public class FebMasterController extends BaseController {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "febMasterService")
	private FebMasterService febMasterService;
	
	@Resource(name = "ye901Service")
	private YE901Service ye901Service;
	
	// 연말정산_관리자_현황관리 화면
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST} , value = "getUserStatusList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getUserStatusListCtrl(@ModelAttribute("YE000VO") YE000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			System.out.println(":::::::::::::::::::: getUserStatusList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				System.out.println("계약ID=>"+vo.get계약ID());
				vo.setBizId(loginVO.getBizId());
				
				if(StringUtils.isEmpty(vo.getSortName())){
					vo.setSortName("B.EMP_NO");
					vo.setSortOrder("ASC");
				}
								
				List<YE000VO> result= febMasterService.getUserStatusList(vo);
				
				int total = febMasterService.getUserStatusListCount(vo);
				
				YE000VO statVO = febMasterService.getUserStatusSum(vo);
				
				if(result != null && result.size() != 0){
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
					jsonObject.put("statistics", statVO);
				}else{
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
					jsonObject.put("statistics", null);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 연말정산_사용자_현황관리_화면
	@RequestMapping(method = RequestMethod.GET , value = "getUserStatusDate.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getUserStatusDateCtrl(@ModelAttribute("YE000VO") YE000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			System.out.println(":::::::::::::::::::: getUserStatusDate :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				List<YE901VO> result= febMasterService.getUserStatusDate(vo);
				
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

	// 연말정산_관리자_일괄_사용자확정
	@RequestMapping(method = RequestMethod.POST , value = "setYE000AllConfirm.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE000AllConfirmCtrl(@ModelAttribute("YE000VO") YE000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			System.out.println(":::::::::::::::::::: setYE000AllConfirm :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				int total = febMasterService.setYE000AllConfirm(vo);
				
				if(total > 0){
					jsonObject.put("success", true);
					jsonObject.put("total", total);
				}else{
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 연말정산_관리자_일괄_사용자확정
	@RequestMapping(method = RequestMethod.POST , value = "setYE000Confirm.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE000ConfirmCtrl(@ModelAttribute("YE000VO") YE000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean result = false;
		
		try {
			System.out.println(":::::::::::::::::::: setYE000Confirm :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				int total = febMasterService.setYE000Confirm(vo);
				
				jsonObject.put("total", total);
				result = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		jsonObject.put("success", result);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 연말정산_사용자_진행현황
	@RequestMapping(method = RequestMethod.GET , value = "getYE901List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE901ListeCtrl(@ModelAttribute("YE901VO") YE901VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			System.out.println(":::::::::::::::::::: getYE901List :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				List<YE901VO> result= ye901Service.getYE901List(vo);
				
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

	/*
	 * 간이지급명세서 yeaTax_payment_include.js 사용자 정보 조회
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST , value = "getPaymentUserStatusList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPaymentUserStatusList(@ModelAttribute("YP000VO") YP000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: getUserStatusList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				logger.info("계약ID=>"+vo.get계약ID());
				vo.setBizId(loginVO.getBizId());
				
				if(StringUtils.isEmpty(vo.getSortName())){
					vo.setSortName("B.EMP_NO");
					vo.setSortOrder("ASC");
				}
								
				List<YP000VO> result= febMasterService.getPaymentUserStatusList(vo);
				
				int total = febMasterService.getPaymentUserStatusListCount(vo);
				
				if(result != null && result.size() != 0){
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
				}else{
//					jsonObject.put("success", false);
					jsonObject.put("success", true);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}

			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
}
