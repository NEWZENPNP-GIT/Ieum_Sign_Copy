package com.ezsign.feb.worker.controller;

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

import com.ezsign.code.service.CodeService;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.service.YE900Service;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.service.YS031Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.feb.worker.service.YE000Service;
import com.ezsign.feb.worker.service.YE001Service;
import com.ezsign.feb.worker.service.YE040Service;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.feb.worker.vo.YE040VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.vo.YW413Request;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/febworker/")
public class YE001Controller extends BaseController{
	
	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "codeService")
	private CodeService codeService;

	@Resource(name = "ys030Service")
	private YS030Service ys030Service;

	@Resource(name = "ys031Service")
	private YS031Service ys031Service;
	
	@Resource(name = "ye900Service")
	private YE900Service ye900Service;
	
	@Resource(name = "ye001Service")
	private YE001Service ye001Service;
	
	@Resource(name = "ye040Service")
	private YE040Service ye040Service;
	
	@Resource(name = "ys000Service")
	private YS000Service ys000Service;

	@RequestMapping(method = RequestMethod.GET , value = "getYE001List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE001ListCtrl(@ModelAttribute("YE001VO") YE001VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		int total = 0;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getYE001List :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				// 시스템관리자만 개인식별번호 조회를 위해서 제공
				int userType = Integer.parseInt(loginVO.getUserType());
				// 본인만 조회 후 부양가족은 하위로 조회
				vo.set가족관계("0");
				
				List<YE001VO> result= ye001Service.getYE001List(vo, userType);
				
				total = ye001Service.getYE001ListCount(vo);
				
				// 사업장조회
				YS030VO ys030VO = new YS030VO();
				ys030VO.set계약ID(vo.get계약ID());
				ys030VO.setStartPage(0);
				ys030VO.setEndPage(99999);
				List<YS030VO> ys030List = ys030Service.getYS030List(ys030VO);
				// 부서조회
				YS031VO ys031VO = new YS031VO();
				ys031VO.set계약ID(vo.get계약ID());
				ys031VO.setStartPage(0);
				ys031VO.setEndPage(99999);
				List<YS031VO> ys031List = ys031Service.getYS031List(ys031VO);
				// 근무상태
				CodeVO codeVO = new CodeVO();
				codeVO.setGrpCommCode("420");
				List<CodeVO> code420List = codeService.getCodeList(codeVO);				

				jsonObject.put("ys030", ys030List);
				jsonObject.put("ys031", ys031List);
				jsonObject.put("code420", code420List);
				
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

	@RequestMapping(method = RequestMethod.GET , value = "getYE001.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE001Ctrl(@ModelAttribute("YE001VO") YE001VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		int total = 0;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getYE001 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				// 시스템관리자만 개인식별번호 조회를 위해서 제공
				int userType = Integer.parseInt(loginVO.getUserType());
				vo.setStartPage(0);
				vo.setEndPage(99999);
				
				List<YE001VO> result= ye001Service.getYE001(vo, userType);
				
				// 총급여 근로소득금액
				YE040VO ye040VO = new YE040VO();
				ye040VO.set계약ID(vo.get계약ID());
				ye040VO.set사용자ID(vo.get사용자ID());
				YE040VO resultYE040VO = ye040Service.getYE040(ye040VO);
				
				// 관리자정정사유
				YE900VO ye900VO = new YE900VO();
				ye900VO.set계약ID(vo.get계약ID());
				ye900VO.set사용자ID(vo.get사용자ID());
				ye900VO.set사유구분("W413");
				YE900VO resultYE900VO = ye900Service.getYE900(ye900VO);
				String 관리자정정사유 = "";
				if(resultYE900VO!=null) {
					관리자정정사유 = resultYE900VO.get정정사유();
				}
				
				jsonObject.put("관리자정정사유", 관리자정정사유);
				jsonObject.put("ye040", resultYE040VO);
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

	@RequestMapping(method = RequestMethod.POST , value = "saveYE001.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveYE001Ctrl(@RequestBody YW413Request body, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: saveYE001 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				body.관리자정정사유.set작업자ID(loginVO.getUserId());
								
				ye001Service.saveYE001(body);
				
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


	@RequestMapping(method = RequestMethod.POST , value = "insYE001.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insYE001Ctrl(@ModelAttribute("YE001VO") YE001VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: insYE001 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				ye001Service.insYE001(vo);
				
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


	@RequestMapping(method = RequestMethod.POST , value = "updYE001.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updYE001Ctrl(@ModelAttribute("YE001VO") YE001VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: updYE001 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				int result = ye001Service.updYE001(vo);
			
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
	
	@RequestMapping(method = RequestMethod.POST , value = "delYE001.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delYE001Ctrl(@ModelAttribute("YE001VO") YE001VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: delYE001 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				int result = ye001Service.delYE001(vo);
			
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

	
	/**
	 * 부양가족 나이를 일괄 집계한다.
	 * 
	 * @param vo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET , value = "allFamilyAge.do")
	@ResponseBody
	public ResponseEntity<JSONObject> allFamilyAge(@ModelAttribute("YE001VO") YE001VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {				
				// 계약년도 조회
				YS000VO ys000VO = new YS000VO();
				ys000VO.setBizId(loginVO.getBizId());
				ys000VO.set계약ID(vo.get계약ID());
				List<YS000VO> ys000VOList = ys000Service.getYS000List(ys000VO);				
				String 계약년도 = "";
				if(ys000VOList != null && ys000VOList.size() > 0) {					
					계약년도 = ys000VOList.get(0).getFebYear();
				}
								
				int result = ye001Service.allFamilyAge(vo, 계약년도);
			
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
	
		
}
