package com.ezsign.feb.worker.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.code.service.CodeService;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.service.YS031Service;
import com.ezsign.feb.system.service.YS050Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.feb.system.vo.YS050VO;
import com.ezsign.feb.worker.service.YE000Service;
import com.ezsign.feb.worker.service.YE040Service;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.jarvis.common.util.StringUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/febworker/")
public class YE000Controller extends BaseController {

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "codeService")
	private CodeService codeService;
	
	@Resource(name = "ye000Service")
	private YE000Service ye000Service;
	
	@Resource(name = "ys030Service")
	private YS030Service ys030Service;
	
	@Resource(name = "ys031Service")
	private YS031Service ys031Service;
	
	@Resource(name = "ys050Service")
	private YS050Service  ys050Service;
	
	@Resource(name = "febMasterService")
	private FebMasterService febMasterService;
	
	@Resource(name = "ye040Service")
	private YE040Service ye040Service;
	
	@Resource(name = "ys000Service")
	private YS000Service ys000Service;
	
	// 연말정산_근로자상세정보 조회
	@RequestMapping(method = RequestMethod.GET , value = "getYE000.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE000Ctrl(@ModelAttribute("YE000VO") YE000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getYE000 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				int userType = Integer.parseInt(loginVO.getUserType());
				
				YE000VO result = ye000Service.getYE000(vo, userType);
				
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
				List<YE901VO> ye901List = febMasterService.getUserStatusDate(vo);
				
				jsonObject.put("total", 1);
				jsonObject.put("data", result);
				jsonObject.put("ys050", ys050VO);
				jsonObject.put("ys030List", ys030List);
				jsonObject.put("deptCodeList", ys031List);
				jsonObject.put("NationCodeList", nationCodeList);
				jsonObject.put("계약년도", 계약년도);
				jsonObject.put("ye901List", ye901List);
				
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
	
	// 연말정산_근로자상세정보 리스트 조회
	@RequestMapping(method = RequestMethod.GET , value = "getYE000List.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE000ListCtrl(@ModelAttribute("YE000VO") YE000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;

		try {
			System.out.println(":::::::::::::::::::: getYE000List :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				List<YE000VO> result = ye000Service.getYE000List(vo);
				total = ye000Service.getYE000ListCount(vo);
				
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
	
	//  연말정산_근로자상세정보 입력
	@RequestMapping(method = RequestMethod.POST , value = "saveYE000.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveYE000Ctrl(@ModelAttribute("YE000VO") YE000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: saveYE000 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
//				int result = ye000Service.saveYE000(vo);
				Map<String,String> resultMap = ye000Service.saveYE000(vo);
				
				
				if (resultMap != null && StringUtils.equals(resultMap.get("result"), "1")) {
					success = true;
				} else {
					jsonObject.put("message", "근로자 정보가 있습니다. 근로자 정보를 확인해주시기 바랍니다.");
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
	
	//  연말정산_근로자상세정보 입력
	@RequestMapping(method = RequestMethod.POST , value = "insYE000.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insYE000Ctrl(@ModelAttribute("YE000VO") YE000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: insYE000 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				int result = ye000Service.insYE000(vo);
				
				if (result > 0 ) {
					success = true;
				} else {
					jsonObject.put("message", "근로자 정보가 있습니다. 근로자 정보를 확인해주시기 바랍니다.");
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

	// 연말정산_근로자상세정보 수정
	@RequestMapping(method = RequestMethod.POST , value = "updYE000.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updYE000Ctrl(@ModelAttribute("YE000VO") YE000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: updYE000 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				int result = ye000Service.updYE000(vo);
			
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
	
	
	// 연말정산_근로자상세정보 삭제
	@RequestMapping(method = RequestMethod.POST , value = "delYE000.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delYE000Ctrl(@ModelAttribute("YE000VO") YE000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: delYE000 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				int result = ye000Service.delYE000(vo);
			
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
	
	// 연말정산_근로자 도움 리스트 조회
	@RequestMapping(method = RequestMethod.GET , value = "getWorkerAssistList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getWorkerAssistListCtrl(@ModelAttribute("YE000VO") YE000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			System.out.println(":::::::::::::::::::: getWorkerAssistList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.set작업자ID(loginVO.getUserId());
				
				List<YE000VO> result = ye000Service.getWorkerAssistList(vo);
				
				if(result != null && result.size() != 0){
					jsonObject.put("total", result.size());
					jsonObject.put("data", result);
				}else{
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
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
	
	// 연말정산_근로자 배정 미배정 수정
	@RequestMapping(method = RequestMethod.POST , value = "saveYE000Assign.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveYE000AssignCtrl(@RequestBody List<YE000VO> list, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: saveYE000Assign :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				ye000Service.saveYE000Assign(list);
			
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

	@RequestMapping(method = RequestMethod.GET , value = "getYE000ChkEdit.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE000ChkEditCtrl(@ModelAttribute("YE000VO") YE000VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		int result = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getYE000ChkEdit :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				//vo.setBizId(loginVO.getBizId());

				if(4 < StringUtil.strPaserInt(loginVO.getUserType())) {
					//관리자 체크
					result = ye000Service.getYE000AdminChkEdit(vo);
				} else {
					//근로자 체크
					result = ye000Service.getYE000ChkEdit(vo);
					
				}

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

	@RequestMapping(method = RequestMethod.GET , value = "getZipcode.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getZipcodeCtrl(@RequestParam(value="keyword",  required=true) String keyword, HttpServletRequest request) throws Exception{
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		HttpURLConnection con = null;
		
		try{
			System.out.println(":::::::::::::::::::: getZipcodeCtrl :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				keyword = URLEncoder.encode(keyword,"EUC-KR");

				String postUrl = "https://biz.epost.go.kr/KpostPortal/openapi?regkey=401652aa7e13e7fcc1534740910675&target=postNew&query=" + keyword + "&countPerPage=50&currentPage=1";
				URL url = new URL(postUrl);

				con = (HttpURLConnection) url.openConnection();
	            con.setRequestProperty("Accept-language", "ko");
	            con.setRequestMethod("GET");
				
	            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	            String readLine = null;

	            String data = "";
	            while ((readLine = br.readLine()) != null) {
	            	data += readLine;
	            }
	            br.close();
	            
	            jsonObject.put("post", data);
System.out.println("data: " + data);
	            con.disconnect();
	            
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




