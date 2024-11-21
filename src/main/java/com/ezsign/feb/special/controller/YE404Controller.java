package com.ezsign.feb.special.controller;

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
import com.ezsign.feb.special.service.YE404Service;
import com.ezsign.feb.special.vo.YE404VO;
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.service.YS031Service;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/febspecial/")
public class YE404Controller extends BaseController {

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "codeService")
	private CodeService codeService;

	@Resource(name = "ys030Service")
	private YS030Service ys030Service;

	@Resource(name = "ys031Service")
	private YS031Service ys031Service;
	
	@Resource(name = "ye404Service")
	private YE404Service ye404Service;
	
	@RequestMapping(method = RequestMethod.GET , value = "getYE404List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE404ListCtrl(@ModelAttribute("YE404VO") YE404VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getYE404List :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				List<YE404VO> result = ye404Service.getYE404List(vo);
				
				YE404VO ye404Total = ye404Service.getYE404ListCount(vo);

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
				// 기부금코드
				codeVO.setGrpCommCode("406");
				List<CodeVO> code406List = codeService.getCodeList(codeVO);
				
				jsonObject.put("ys030", ys030List);
				jsonObject.put("ys031", ys031List);
				jsonObject.put("code420", code420List);
				jsonObject.put("code406", code406List);
				
				
				jsonObject.put("total", ye404Total.getTotalCnt1());
				jsonObject.put("totalAmt", ye404Total);
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
	
	@RequestMapping(method = RequestMethod.POST , value = "saveYE404.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveYE404Ctrl(@RequestBody List<YE404VO> list, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: saveYE404 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				for(int i=0;i<list.size();i++) {
					YE404VO ye404VO = list.get(i);
					ye404VO.setBizId(loginVO.getBizId());
					ye404VO.set작업자ID(ye404VO.get사용자ID());
					ye404VO.set부양가족ID(ye404VO.get사용자ID());
					list.set(i, ye404VO);
				}
				
				ye404Service.saveYE404(list);
				
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
	
	@RequestMapping(method = RequestMethod.POST , value = "insYE404.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insYE404Ctrl(@ModelAttribute("YE404VO") YE404VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: insYE404 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.set작업자ID(vo.get사용자ID());
				vo.set부양가족ID(vo.get사용자ID());
				
				int result = ye404Service.insYE404(vo);
				
				if (result > 0 ) {
					success = true;
				} else {
					jsonObject.put("message", "근로자 정보가 없습니다. 근로자 정보를 확인해주시기 바랍니다.");
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "updYE404.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updYE404Ctrl(@ModelAttribute("YE404VO") YE404VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: updYE404 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				int result = ye404Service.updYE404(vo);
				
				if (result > 0) success = true;
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

	@RequestMapping(method = RequestMethod.POST , value = "delYE404.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delYE404Ctrl(@ModelAttribute("YE404VO") YE404VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: delYE404 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				int result = ye404Service.delYE404(vo);
				
				if (result > 0) success = true;
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
