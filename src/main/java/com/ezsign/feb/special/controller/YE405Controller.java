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
import com.ezsign.feb.special.service.YE405Service;
import com.ezsign.feb.special.vo.YE405VO;
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.service.YS031Service;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/febspecial/")
public class YE405Controller extends BaseController {

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "codeService")
	private CodeService codeService;

	@Resource(name = "ys030Service")
	private YS030Service ys030Service;

	@Resource(name = "ys031Service")
	private YS031Service ys031Service;
	
	@Resource(name = "ye405Service")
	private YE405Service ye405Service;
	
	@RequestMapping(method = RequestMethod.GET , value = "getYE405List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE405ListCtrl(@ModelAttribute("YE405VO") YE405VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getYE405List :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				List<YE405VO> result = ye405Service.getYE405List(vo);

				YE405VO ye405Total = ye405Service.getYE405ListCount(vo);

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
				
				jsonObject.put("total", ye405Total.getTotalCnt1());
				jsonObject.put("totalAmt", ye405Total);
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

	@RequestMapping(method = RequestMethod.POST , value = "saveYE405.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveYE405Ctrl(@RequestBody List<YE405VO> list, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: saveYE405 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				for(int i=0;i<list.size();i++) {
					YE405VO ye405VO = list.get(i); 
					ye405VO.setBizId(loginVO.getBizId());
					ye405VO.set작업자ID(ye405VO.get사용자ID());
					list.set(i, ye405VO);
				}
				
				ye405Service.saveYE405(list);
				
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

	@RequestMapping(method = RequestMethod.POST , value = "insYE405.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insYE405Ctrl(@ModelAttribute("YE405VO") YE405VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: insYE405 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.set작업자ID(vo.get사용자ID());
				
				ye405Service.insYE405(vo);
				
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

	@RequestMapping(method = RequestMethod.POST , value = "updYE405.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updYE405Ctrl(@ModelAttribute("YE405VO") YE405VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: updYE405 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				int result = ye405Service.updYE405(vo);
				
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

	@RequestMapping(method = RequestMethod.POST , value = "delYE405.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delYE405Ctrl(@ModelAttribute("YE405VO") YE405VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: delYE405 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				int result = ye405Service.delYE405(vo);
				
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
