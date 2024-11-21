package com.ezsign.feb.system.controller;


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

import com.ezsign.code.service.CodeService;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;


@Controller
@RequestMapping("/febsystem/")
public class YS030Controller extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "codeService")
	private CodeService codeService;

	@Resource(name = "ys000Service")
	private YS000Service ys000Service;

	@Resource(name = "ys030Service")
	private YS030Service ys030Service;

	// 연말정산_사업장 조회
	@RequestMapping(method = RequestMethod.GET , value = "getYS030List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYS030ListCtrl(@ModelAttribute("YS030VO") YS030VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getYS030List :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				List<YS030VO> result= ys030Service.getYS030List(vo);
				
				total = ys030Service.getYS030ListCount(vo);
				
				// 계약년도 가져오기
				YS000VO ys000VO = new YS000VO();
				ys000VO.setBizId(loginVO.getBizId());
				List<YS000VO> yearData = ys000Service.getYS000List(ys000VO);
				
				// 관할세무서코드
				CodeVO codeVO = new CodeVO();
				codeVO.setGrpCommCode("401");
				List<CodeVO> code401List = codeService.getCodeList(codeVO);
				codeVO.setGrpCommCode("402");
				List<CodeVO> code402List = codeService.getCodeList(codeVO);

				// 기업구분코드(세무대리인, 법인, 개인)
				codeVO.setGrpCommCode("412");
				List<CodeVO> code412List = codeService.getCodeList(codeVO);
				
				jsonObject.put("total", total);
				jsonObject.put("data", result);
				jsonObject.put("yearData", yearData);
				jsonObject.put("code401", code401List);
				jsonObject.put("code402", code402List);
				jsonObject.put("code412", code412List);
				
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

	// 연말정산_사업장 입력
	@RequestMapping(method = RequestMethod.POST , value = "insYS030.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insYS030Ctrl(@ModelAttribute("YS030VO") YS030VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		
		try {
			System.out.println(":::::::::::::::::::: insYS030 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());

				YS030VO ys030VO = new YS030VO();
				ys030VO.set계약ID(vo.get계약ID());
				ys030VO.set종사업자일련번호(vo.get종사업자일련번호());
				List<YS030VO> result= ys030Service.getYS030List(vo);
				if(result.size()>0) {
					message = "종사업자일련번호가 존재합니다.";
				} else {
					ys030Service.insYS030(vo);
					success = true;
				}
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


	@RequestMapping(method = RequestMethod.POST , value = "updYS030.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updYS030Ctrl(@ModelAttribute("YS030VO") YS030VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: updYS030 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				int result = ys030Service.updYS030(vo);
			
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

	@RequestMapping(method = RequestMethod.POST , value = "delYS030.do")
	@ResponseBody
	public ResponseEntity<String> delYS030Ctrl(@ModelAttribute("YS030VO") YS030VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: delYS030 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				int result = ys030Service.delYS030(vo);
			
				if(result>0) success = true;
				jsonObject.put("total", result);
				jsonObject.put("code", vo.getCode());
				jsonObject.put("message", vo.getMessage());
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		
		return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
	}

	// 연말정산_사업장 종사업자 일련번호 자동증가 값
	@RequestMapping(method = RequestMethod.GET , value = "getYS030TaxNumber.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYS030TaxNumberCtrl(@ModelAttribute("YS030VO") YS030VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		String taxNumber;
		
		try {
			System.out.println(":::::::::::::::::::: getYS030TaxNumber :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				taxNumber = ys030Service.getYS030TaxNumber(vo);
				
				jsonObject.put("success", true);
				jsonObject.put("TaxIdentiNumber", taxNumber);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
}
