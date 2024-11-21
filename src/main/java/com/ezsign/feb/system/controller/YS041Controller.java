package com.ezsign.feb.system.controller;

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

import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.system.service.YS041Service;
import com.ezsign.feb.system.vo.YS041VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/febsystem/")
public class YS041Controller extends BaseController{
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "ys041Service")
	private YS041Service  ys041Service;
	
	// 중간관리자 관리부서 조회
	@RequestMapping(method = RequestMethod.GET , value = "getYS041List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYS041ListCtrl(@ModelAttribute("YS041VO") YS041VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getYS041List :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				List<YS041VO> result = ys041Service.getYS041List(vo);
				
				total = ys041Service.getYS041ListCount(vo);

				jsonObject.put("total", total);
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
	
	// 중간관리자 관리부서 상세조회
	@RequestMapping(method = RequestMethod.GET , value = "getYS041.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYS041Ctrl(@ModelAttribute("YS041VO") YS041VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getYS041 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				YS041VO result = ys041Service.getYS041(vo);
				
				// 중간관리자 관리부서 조회
				List<YS041VO> deptList = ys041Service.getYS041DeptList(vo);

				jsonObject.put("data", result);
				jsonObject.put("deptList", deptList);
				
				if(result != null){
					jsonObject.put("total", 1);
				}else{
					jsonObject.put("total", 0);
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
	
	// 중간관리자 관리부서 입력
	@RequestMapping(method = RequestMethod.POST , value = "insYS041.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insYS041Ctrl(@ModelAttribute("YS041VO") YS041VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		int result = 0;
		
		try {
			System.out.println(":::::::::::::::::::: insYS041 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				result = ys041Service.insYS041(vo);
				
				if (result > 0) {
					message = "관리부서가 등록되었습니다.";
				} else {
					message = "등록된 관리부서 입니다.";
				}
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		jsonObject.put("message", message);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 중간관리자 관리부서 삭제
	@RequestMapping(method = RequestMethod.POST , value = "delYS041.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delYS041Ctrl(@ModelAttribute("YS041VO") YS041VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: delYS041 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				int result = ys041Service.delYS041(vo);
			
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

	// 중간관리자 관리부서 등록
	@RequestMapping(method = RequestMethod.POST , value = "saveYS041.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveYS041Ctrl(@RequestBody List<YS041VO> vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: saveYS041 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());

				ys041Service.saveYS041(vo);
				
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
