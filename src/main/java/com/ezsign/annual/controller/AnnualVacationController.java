package com.ezsign.annual.controller;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
import com.ezsign.dept.vo.DeptVO;
import com.ezsign.feb.master.vo.YE000VO;

import net.sf.json.JSONObject;

import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.paystub.service.PaystubService;
import com.ezsign.paystub.vo.PaystubVO;
import com.jarvis.common.util.StringUtil;
import com.ezsign.annual.service.AnnualVacationService;
import com.ezsign.annual.vo.AnnualConfigVO;
import com.ezsign.annual.vo.AnnualTypeVO;
import com.ezsign.annual.vo.AnnualVacationStatVO;
import com.ezsign.annual.vo.AnnualVacationVO;
import com.ezsign.annual.vo.VacationRequestVO;
import com.ezsign.approval.service.ApprovalService;
import com.ezsign.approval.vo.ApprovalVO;


@Controller
@RequestMapping("/vacation/")
public class AnnualVacationController extends BaseController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "annualVacationService")
	private AnnualVacationService annualVacationService;

	@Resource(name = "approvalService")
	private ApprovalService approvalService;	
	
	@RequestMapping(method = RequestMethod.GET , value = "getAnnualVacationList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getAnnualVacationListCtrl(@ModelAttribute("AnnualVacationVO") AnnualVacationVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: getAnnualVacationList :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				logger.info("searchKey=>"+vo.getSearchKey());
				logger.info("searchValue=>"+vo.getSearchValue());
				vo.setBizId(loginVO.getBizId());
					
				// 연차내역
				List<AnnualVacationVO> list = annualVacationService.getAnnualVacationList(vo);
	
				int total = annualVacationService.getAnnualVacationListCount(vo);
				
				if(list != null && list.size() != 0){
					jsonObject.put("total", total);
					jsonObject.put("data", list);
				}else{
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
				result = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		
		//logger.info(jsonObject.toString());
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	@RequestMapping(method = RequestMethod.GET , value = "getAnnualVacation.do")
	@ResponseBody
	public ResponseEntity<String> getAnnualVacationCtrl(@ModelAttribute("VacationRequestVO") VacationRequestVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		int auth = 0;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: viewAnnualVacation :::::::::: ["+vo.getBizId()+"]["+vo.getUserId() + "] :::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				// 연차내역
				AnnualVacationVO avVO = annualVacationService.getAnnualVacation(vo);
				jsonObject.put("data", avVO);
				
				// 결재내역
				if(avVO!=null) {
					
					VacationRequestVO vrVO = annualVacationService.getVacationRequest(vo);
					jsonObject.put("vacation", vrVO);
					
					ApprovalVO aVO = new ApprovalVO();
					aVO.setApprovalId(vo.getApprovalId());
					List<ApprovalVO> approvalList = approvalService.getApprovalList(aVO);
					jsonObject.put("approval", approvalList);
					
					result = true;
				}
				
				jsonObject.put("auth", auth);
			}
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		
		logger.info(jsonObject.toString());
		
		return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
	}
	
	
	@RequestMapping(method = RequestMethod.POST , value = "insAnnualVacation.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insAnnualVacationCtrl(@ModelAttribute("AnnualVacationVO") AnnualVacationVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: insAnnualVacation :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				logger.info("annualYear=>"+vo.getAnnualYear());
				vo.setBizId(loginVO.getBizId());
				
				// 연차생성
				int total = annualVacationService.insAnnualVacation(vo);
				
				jsonObject.put("total", total);
				result = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		
		//logger.info(jsonObject.toString());
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "updAnnualVacation.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updAnnualVacationCtrl(@ModelAttribute("AnnualVacationVO") AnnualVacationVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: updAnnualVacation :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				logger.info("annualYear=>"+vo.getAnnualYear());
				vo.setBizId(loginVO.getBizId());
				
				// 연차내역
				int total = annualVacationService.updAnnualVacation(vo);
				
				jsonObject.put("total", total);
				result = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		
		//logger.info(jsonObject.toString());
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	@RequestMapping(method = RequestMethod.POST , value = "requestAnnualVacation.do")
	@ResponseBody
	public ResponseEntity<String> requestAnnualVacationCtrl(@RequestBody VacationRequestVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		String message = "";
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: requestAnnualVacation ::::::::::: ["+vo.getUserId() + "] ::::::::::::::::");
			
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				// 사용일수 체크
				AnnualVacationVO avVO = annualVacationService.getAnnualVacation(vo);
				
				if(avVO!=null) {

					double useDay = avVO.getTotalDay()+avVO.getAddDay()-avVO.getDelDay()-avVO.getUseDay()-vo.getVacationDay();
					if(useDay>=0) {
						result = annualVacationService.requestAnnualVacation(vo);
					} else {
						message = "잔여일수가 부족합니다.";
					}
				} else {
					message = "연차정보가 존재하지 않습니다.";
				}
			}
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		jsonObject.put("message", message);
		
		return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "acceptAnnualVacation.do")
	@ResponseBody
	public ResponseEntity<String> acceptAnnualVacationCtrl(@ModelAttribute("VacationRequestVO") VacationRequestVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		String message = "";
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: acceptAnnualVacation ::::::::::: ["+vo.getUserId() + "] ::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				
				result = annualVacationService.acceptAnnualVacation(vo);
			}
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		jsonObject.put("message", message);
		
		return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "acceptAllAnnualVacation.do")
	@ResponseBody
	public ResponseEntity<String> acceptAllAnnualVacationCtrl(@RequestBody List<VacationRequestVO> list, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		String message = "";
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: acceptAllAnnualVacation ::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				for(int i=0;i<list.size();i++) {
					VacationRequestVO vo = list.get(i);
					vo.setBizId(loginVO.getBizId());
					vo.setUserId(loginVO.getUserId());
					list.set(i, vo);
				}
				
				result = annualVacationService.acceptAllAnnualVacation(list);
			}
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		jsonObject.put("message", message);
		
		return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "rejectAnnualVacation.do")
	@ResponseBody
	public ResponseEntity<String> rejectAnnualVacationCtrl(@ModelAttribute("VacationRequestVO") VacationRequestVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		String message = "";
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: rejectAnnualVacation ::::::::: ["+vo.getUserId() + "] ::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				
				result = annualVacationService.rejectAnnualVacation(vo);
			}
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		jsonObject.put("message", message);
		
		return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
	}


	@RequestMapping(method = RequestMethod.POST , value = "cancelAnnualVacation.do")
	@ResponseBody
	public ResponseEntity<String> cancelAnnualVacationCtrl(@ModelAttribute("VacationRequestVO") VacationRequestVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		String message = "";
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: cancelAnnualVacation ::::::::: ["+vo.getUserId() + "] :::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				
				result = annualVacationService.cancelAnnualVacation(vo);
			}
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		jsonObject.put("message", message);
		
		return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET , value = "getVacationRequestList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getVacationRequestListCtrl(@ModelAttribute("VacationRequestVO") VacationRequestVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: getVacationRequestList :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
					
				// 연차신청내역
				List<VacationRequestVO> list = annualVacationService.getVacationRequestList(vo);
	
				if(list != null && list.size() != 0){
					jsonObject.put("total", list.size());
					jsonObject.put("data", list);
				}else{
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
				result = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		
		//logger.info(jsonObject.toString());
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	@RequestMapping(method = RequestMethod.GET , value = "getVacationRequestCompleteList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getVacationRequestCompleteListCtrl(@ModelAttribute("VacationRequestVO") VacationRequestVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: getVacationRequestCompleteList :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
					
				// 연차신청내역
				List<VacationRequestVO> list = annualVacationService.getVacationRequestCompleteList(vo);
	
				if(list != null && list.size() != 0){
					jsonObject.put("total", list.size());
					jsonObject.put("data", list);
				}else{
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
				result = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		
		//logger.info(jsonObject.toString());
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	@RequestMapping(method = RequestMethod.GET , value = "getVacationTypeSumList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getVacationTypeSumListCtrl(@ModelAttribute("VacationRequestVO") VacationRequestVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: getVacationTypeSumList :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
					
				// 연차내역
				List<VacationRequestVO> list = annualVacationService.getVacationTypeSumList(vo);
	
				if(list != null && list.size() != 0){
					jsonObject.put("total", list.size());
					jsonObject.put("data", list);
				}else{
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
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

	


	@RequestMapping(method = RequestMethod.GET , value = "getAnnualConfig.do")
	@ResponseBody
	public ResponseEntity<String> getAnnualConfigCtrl(@ModelAttribute("AnnualConfigVO") AnnualConfigVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean success = false;
		int auth = 0;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: getAnnualConfig :::::::::: ["+vo.getBizId()+"] :::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				AnnualConfigVO result = annualVacationService.getAnnualConfig(vo);
				jsonObject.put("data", result);
				success = true;
			}
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		
		logger.info(jsonObject.toString());
		
		return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "saveAnnualConfig.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveAnnualConfigCtrl(@ModelAttribute("AnnualConfigVO") AnnualConfigVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: saveAnnualConfig :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setServiceId(annualVacationService.SERVICE_ID);
				vo.setBizId(loginVO.getBizId());
				
				int total = annualVacationService.saveAnnualConfig(vo);
				
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



	@RequestMapping(method = RequestMethod.GET , value = "getAnnualTypeList.do")
	@ResponseBody
	public ResponseEntity<String> getAnnualTypeListCtrl(@ModelAttribute("AnnualTypeVO") AnnualTypeVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean success = false;
		int auth = 0;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: getAnnualTypeList :::::::::: ["+vo.getBizId()+"] :::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				List<AnnualTypeVO> result = annualVacationService.getAnnualTypeList(vo);
				jsonObject.put("data", result);
				success = true;
			}
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		
		logger.info(jsonObject.toString());
		
		return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "insAnnualType.do")
	@ResponseBody
	public ResponseEntity<String> insAnnualTypeCtrl(@ModelAttribute("AnnualTypeVO") AnnualTypeVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean success = false;
		int auth = 0;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: insAnnualType :::::::::: ["+vo.getBizId()+"] :::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				annualVacationService.insAnnualType(vo);
				success = true;
			}
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		
		logger.info(jsonObject.toString());
		
		return new ResponseEntity<String>(jsonObject.toString(), responseHeaders, status);
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "saveAnnualType.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveAnnualTypeCtrl(@RequestBody List<AnnualTypeVO> list, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: saveAnnualType :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				for(int i=0;i<list.size();i++) {
					AnnualTypeVO configVO = list.get(i);
					configVO.setBizId(loginVO.getBizId());
					list.set(i, configVO);
				}
				
				int total = annualVacationService.saveAnnualType(list);
				
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

	// 연차사용내역 조회
	@RequestMapping(method = RequestMethod.GET , value = "getVacationHistoryList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getVacationHistoryListCtrl(@ModelAttribute("VacationRequestVO") VacationRequestVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean success = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: getVacationHistoryList :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
					
				// 연차사용내역
				List<VacationRequestVO> list = annualVacationService.getVacationHistoryList(vo);
	
				int total = annualVacationService.getVacationHistoryListCount(vo);
				
				if(list != null && list.size() > 0){
					jsonObject.put("total", total);
					jsonObject.put("data", list);
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
	

	@RequestMapping(method = RequestMethod.GET , value = "getAnnualStatMonthList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getAnnualStatMonthListCtrl(@ModelAttribute("AnnualVacationStatVO") AnnualVacationStatVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: getAnnualStatMonthList :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
					
				// 연차내역
				List<AnnualVacationStatVO> list = annualVacationService.getAnnualStatMonthList(vo);
				
				if(list != null && list.size() != 0){
					jsonObject.put("total", list.size());
					jsonObject.put("data", list);
				}else{
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
				result = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		
		//logger.info(jsonObject.toString());
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST, value = "sendAnnualVacationExcel.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendAnnualVacationExcelCtrl(@ModelAttribute("AnnualVacationVO") AnnualVacationVO vo, HttpServletRequest request)
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean result = false;
		
		logger.info(":::::: sendAnnualVacationExcel :::::: ");
		
		try {
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setBizId(loginVO.getBizId());
				vo.setServiceId(AnnualVacationService.SERVICE_ID);
				logger.info("bizId : " + vo.getBizId());
				
				// 파일생성
				String szSavePath = MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = resultFileList.size();
				logger.info("연차기준정보 XLS File Count : "+resultFileList.size());
				
				if (total == 1) {
					
					// 전달받은 파일리스트
					for (int i=0; i < resultFileList.size(); i++) {
						FileVO fileVO = resultFileList.get(i);
						String xlsPath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
						
						File file = new File(xlsPath);
						if (!file.exists()) {
							logger.info("[sendAnnualVacationExcel] XLS파일이 존재하지 않습니다.");
							jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
							result = false;
						} else {
							// DATABASE 처리
							AnnualVacationVO resultAnnualVacationVO = annualVacationService.sendAnnualVacationExcel(vo.getBizId(), xlsPath, fileVO);
							if (resultAnnualVacationVO == null) {
								jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
								result = false;
							} else {
								if(StringUtil.isNotNull(resultAnnualVacationVO.getCode())) {
									String resultCode = resultAnnualVacationVO.getCode();
									if (!"99".equals(resultCode)) {
										jsonObject.put("message", resultAnnualVacationVO.getCode()+"건의 급여정보를 등록하였습니다.");
										result = true;
									} else if ("99".equals(resultCode)){
										jsonObject.put("message", resultAnnualVacationVO.getMessage() + "\r\n" + resultAnnualVacationVO.getExcelSheet() + " Sheet " + resultAnnualVacationVO.getExcelRow() + "번째 줄 " + resultAnnualVacationVO.getEmpName()+"님의 연차정보를 등록해주세요.");
									}
								} else {
									jsonObject.put("message", resultAnnualVacationVO.getExcelSheet() + " Sheet " + resultAnnualVacationVO.getExcelRow() + "번째 줄 " + resultAnnualVacationVO.getEmpName()+"님의 급여정보에서 필수항목이 누락된 데이터가 존재합니다.");
									result = false;
								}
							}
							file.delete();
						}
					}
				} else {
					logger.info("XLS 파일이 잘못 전달되었습니다.");
					jsonObject.put("message", "XLS파일이 존재하지 않습니다.");
					result = false;
				}
				
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
		}
		jsonObject.put("success", result);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	

	
}
