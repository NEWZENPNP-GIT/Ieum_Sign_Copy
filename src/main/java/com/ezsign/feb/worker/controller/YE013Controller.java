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
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.service.YS031Service;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.feb.worker.service.YE001Service;
import com.ezsign.feb.worker.service.YE013Service;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.feb.worker.vo.YE013VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/febworker/")
public class YE013Controller extends BaseController {

	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "codeService")
	private CodeService codeService;

	@Resource(name = "ys030Service")
	private YS030Service ys030Service;
	
	@Resource(name = "ys031Service")
	private YS031Service ys031Service;

	@Resource(name = "ye013Service")
	private YE013Service ye013Service;
	
	@Resource(name = "ye001Service")
	private YE001Service ye001Service;
	
	// 연말정산_공제불가회사지원금 저장
	@RequestMapping(method = RequestMethod.GET , value = "getYE013List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE013ListCtrl(@ModelAttribute("YE013VO") YE013VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getYE013List :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				List<YE013VO> result = ye013Service.getYE013List(vo);

				YE013VO ye013Total = ye013Service.getYE013ListCount(vo);
				
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
				CodeVO code1VO = new CodeVO();
				code1VO.setGrpCommCode("420");
				List<CodeVO> code420List = codeService.getCodeList(code1VO);
				
				// 공제구분
				CodeVO code2VO = new CodeVO();
				code2VO.setGrpCommCode("408");
				List<CodeVO> code408List = codeService.getCodeList(code2VO);
				
				// 공제구분상세
				CodeVO code3VO = new CodeVO();
				code3VO.setGrpCommCode("409");	// 의료비
				List<CodeVO> code409List = codeService.getCodeList(code3VO);
				
				CodeVO code4VO = new CodeVO();
				code4VO.setGrpCommCode("410");	// 교육비
				List<CodeVO> code410List = codeService.getCodeList(code4VO);
				
				CodeVO code5VO = new CodeVO();
				code5VO.setGrpCommCode("411");	// 신용카드
				List<CodeVO> code411List = codeService.getCodeList(code5VO);
				
				// 의료증빙코드
				CodeVO code6VO = new CodeVO();
				code6VO.setGrpCommCode("407");
				List<CodeVO> code407List = codeService.getCodeList(code6VO);
				
				// 부양가족 조회
				YE001VO ye001VO = new YE001VO();
				ye001VO.setBizId(vo.getBizId());
				ye001VO.set계약ID(vo.get계약ID());
				ye001VO.set사용자ID(vo.get사용자ID());
				ye001VO.set가족관계("0");
				ye001VO.setStartPage(0);
				ye001VO.setEndPage(99999);
				// 시스템관리자만 개인식별번호 조회를 위해서 제공
				int userType = Integer.parseInt(loginVO.getUserType());
				
				List<YE001VO> ye001List = ye001Service.getYE001List(ye001VO, userType);
				jsonObject.put("ys030", ys030List);
				jsonObject.put("ys031", ys031List);
				jsonObject.put("ye001", ye001List);
				jsonObject.put("code420", code420List);
				jsonObject.put("code408", code408List);
				jsonObject.put("code409", code409List);
				jsonObject.put("code410", code410List);
				jsonObject.put("code411", code411List);
				jsonObject.put("code407", code407List);
				
				jsonObject.put("total", ye013Total.getTotalCnt1());
				jsonObject.put("totalAmt", ye013Total);
				jsonObject.put("data", result);
				
				success = true;
				
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 연말정산_공제불가회사지원금 저장
	@RequestMapping(method = RequestMethod.POST , value = "insYE013.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insYE013Ctrl(@ModelAttribute("YE013VO") YE013VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: insYE013 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.set작업자ID(vo.get사용자ID());
				
				int result = ye013Service.insYE013(vo);
				
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

	// 연말정산_공제불가회사지원금 수정
	@RequestMapping(method = RequestMethod.POST , value = "updYE013.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updYE013Ctrl(@ModelAttribute("YE013VO") YE013VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: updYE013 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				int result = ye013Service.updYE013(vo);
				
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

	// 연말정산_공제불가회사지원금 삭제
	@RequestMapping(method = RequestMethod.POST , value = "delYE013.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delYE013Ctrl(@ModelAttribute("YE013VO") YE013VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: delYE013 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				int result = ye013Service.delYE013(vo);
				
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
	
	// 연말정산_공제불가회사지원금 저장
	@RequestMapping(method = RequestMethod.POST , value = "saveYE013.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveYE013Ctrl(@RequestBody List<YE013VO> list, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: saveYE013 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());				
				
				for(int i=0;i<list.size();i++) {
					YE013VO ye013VO = list.get(i);
					ye013VO.setBizId(loginVO.getBizId());
					ye013VO.set작업자ID(ye013VO.get사용자ID());
					list.set(i, ye013VO);
				}
				ye013Service.saveYE013(list);
				
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
