package com.ezsign.feb.worker.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.ezsign.feb.master.vo.YP000VO;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.service.YS031Service;
import com.ezsign.feb.system.service.YS050Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.feb.system.vo.YS050VO;
import com.ezsign.feb.worker.service.YP000Service;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

import jxl.common.Logger;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/febworker/")
public class YP000Controller extends BaseController {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name = "ys030Service")
	private YS030Service ys030Service;
	
	@Resource(name = "ys031Service")
	private YS031Service ys031Service;
	
	@Resource(name = "codeService")
	private CodeService codeService;
	
	@Resource(name = "yp000Service")
	private YP000Service yp000Service;
	
	@Resource(name = "ys050Service")
	private YS050Service  ys050Service;
	
	@Resource(name = "ys000Service")
	private YS000Service ys000Service;
		
	// 간이지급명세서_근로자상세정보 조회
	@RequestMapping(method = RequestMethod.POST , value = "getYP000.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYP000(@ModelAttribute("YP000VO") YP000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getYP000 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("@ bizId => " + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				int userType = Integer.parseInt(loginVO.getUserType());
				
				YP000VO result = yp000Service.getYP000(vo, userType);
				
				// 사업장 리스트
				YS030VO ys030VO = new YS030VO();
				ys030VO.set계약ID(vo.get계약ID());
				ys030VO.setStartPage(0);
				ys030VO.setEndPage(100);
				List<YS030VO> ys030List = ys030Service.getYS030List(ys030VO);
				
				// 부서 코드 조회
				YS031VO ys031VO = new YS031VO();
				ys031VO.set계약ID(vo.get계약ID());
				ys031VO.setStartPage(0);
				ys031VO.setEndPage(200);
				List<YS031VO> ys031List = ys031Service.getYS031List(ys031VO);
				
				
				// 국가코드 리스트
				CodeVO codeVO = new CodeVO();
				codeVO.setGrpCommCode("405");
				List<CodeVO> nationCodeList = codeService.getCodeList(codeVO);
				
				// 급여항목
				YS050VO ys050VO = new YS050VO();
				ys050VO.set계약ID(vo.get계약ID());
				ys050VO = ys050Service.getYS050(ys050VO);
				
				// 계약년도 조회
				YS000VO ys000VO = new YS000VO();
				ys000VO.setBizId(loginVO.getBizId());
				ys000VO.set계약ID(vo.get계약ID());
				List<YS000VO> ys000VOList = ys000Service.getYS000List(ys000VO);				
				String 계약년도 = "";
				if(ys000VOList.size()>0) {					
					계약년도 = ys000VOList.get(0).getFebYear();
				}
								
				// 근로자 진행현황코드
				//List<YE901VO> ye901List = febMasterService.getUserStatusDate(vo);
				
				jsonObject.put("total", 1);
				jsonObject.put("data", result);
				jsonObject.put("ys050", ys050VO);
				jsonObject.put("ys030List", ys030List);
				jsonObject.put("deptCodeList", ys031List);
				jsonObject.put("NationCodeList", nationCodeList);
				jsonObject.put("계약년도", 계약년도);
				//jsonObject.put("ye901List", ye901List);
				
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 간이지급명세서_근로자상세정보 입력
	@RequestMapping(method = RequestMethod.POST , value = "saveYP000.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveYP000(@ModelAttribute("YP000VO") YP000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: saveYP000 :::::::::::::::::::");
			logger.info("# 근무시기 : " + vo.get근무시기() );
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("# bizId => " + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				Map<String,Object> resultMap = yp000Service.saveYP000(vo);				
				
				if (resultMap != null && StringUtils.equals((String)resultMap.get("result"), "1")) {
					success = true;
				} else {
					jsonObject.put("message", "근로자 정보가 있습니다. 근로자 정보를 확인해주시기 바랍니다.");
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 간이지급명세서_근로자 배정 미배정 수정
	@RequestMapping(method = RequestMethod.POST , value = "saveYP000Assign.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveYP000Assign(@RequestBody List<YP000VO> list, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: saveYP000Assign :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				yp000Service.saveYP000Assign(list);
			
				success = true;
				
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
		
	// 간이지급명세서_근로자상세정보 리스트 조회
	@RequestMapping(method = RequestMethod.POST , value = "getYP000List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYP000List(@ModelAttribute("YP000VO") YP000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;

		try {
			System.out.println(":::::::::::::::::::: getYP000List :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				List<YP000VO> result = yp000Service.getYP000List(vo);
				total = yp000Service.getYP000ListCount(vo);
				
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
			logger.error(ex.getMessage(), ex);
			throw ex;
		}

		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
		
}
