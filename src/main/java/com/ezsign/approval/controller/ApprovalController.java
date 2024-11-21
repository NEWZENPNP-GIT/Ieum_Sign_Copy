package com.ezsign.approval.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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

import com.ezsign.common.controller.BaseController;
import com.ezsign.contract.dao.ContractDAO;
import com.ezsign.contract.vo.ContractPersonLogVO;
import com.ezsign.contract.vo.ContractPersonVO;
import com.ezsign.dept.vo.DeptVO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.master.vo.YE000VO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.paystub.service.PaystubService;
import com.ezsign.paystub.vo.PaystubVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jarvis.common.util.StringUtil;
import com.ezsign.annual.service.AnnualVacationService;
import com.ezsign.annual.vo.AnnualConfigVO;
import com.ezsign.annual.vo.AnnualTypeVO;
import com.ezsign.annual.vo.AnnualVacationStatVO;
import com.ezsign.annual.vo.AnnualVacationVO;
import com.ezsign.annual.vo.VacationRequestVO;
import com.ezsign.approval.service.ApprovalService;
import com.ezsign.approval.vo.ApprLineDetailVO;
import com.ezsign.approval.vo.ApprLineMasterVO;
import com.ezsign.approval.vo.ApprovalDetailVO;
import com.ezsign.approval.vo.ApprovalMasterVO;


@Controller
@RequestMapping("/approval/")
public class ApprovalController extends BaseController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "approvalService")
	private ApprovalService approvalService;
	
	@Resource(name = "contractDAO")
	private ContractDAO contractDAO;
	
	@RequestMapping(method = RequestMethod.POST , value = "saveApprLine.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveApprLineCtrl(@RequestBody String paramData, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: saveApprLine :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				//
				/*JSONArray jsonArray = JSONArray.fromObject(paramData);*/
				 
			    List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
			    resultMap = JSONArray.fromObject(paramData);
			    String apprLineId = "";
			    ApprLineMasterVO apprLineMasterVO = new ApprLineMasterVO();
			    apprLineMasterVO.setBizId(loginVO.getBizId());
			    apprLineMasterVO.setUserId(loginVO.getUserId());
			    apprLineMasterVO.setApprLineName((String)resultMap.get(0).get("apprLineName"));
		    	apprLineId = approvalService.saveApprLineMaster(apprLineMasterVO);
		    	int index = 1;
		    	for (Map<String, Object> map : resultMap) {
		    		ApprLineDetailVO apprLineDetailVO = new ApprLineDetailVO();
		    		apprLineDetailVO.setApprLineId(apprLineId);
		    		apprLineDetailVO.setApprSeq(String.valueOf(index));
		    		apprLineDetailVO.setInsEmpNo(apprLineMasterVO.getUserId());
		    		apprLineDetailVO.setUpdEmpNo(apprLineMasterVO.getUserId());
		    		apprLineDetailVO.setApprBizId((String)map.get("apprBizId"));
		    		apprLineDetailVO.setApprUserId((String)map.get("apprUserId"));
		    		
		    		approvalService.saveApprLineDetail(apprLineDetailVO);
			        index++;
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
	
	@RequestMapping(method = RequestMethod.GET , value = "getApprLineMaster.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getApprLineMasterCtrl(@ModelAttribute("ApprLineMasterVO") ApprLineMasterVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: getApprLineMaster :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				if(!StringUtil.isNotNull(vo.getBizId())) {
					vo.setBizId(loginVO.getBizId());
				}
				if(!StringUtil.isNotNull(vo.getUserId())) {
					vo.setUserId(loginVO.getUserId());
				}
				List<ApprLineMasterVO> apprLineMasterList = approvalService.getApprLineMaster(vo);
				
				if(apprLineMasterList != null && apprLineMasterList.size() != 0){
					jsonObject.put("success", true);
					jsonObject.put("data", apprLineMasterList);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
		//logger.info(jsonObject.toString());
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "getApprLineDetail.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getApprLineDetailCtrl(@ModelAttribute("ApprLineDetailVO") ApprLineDetailVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: getApprLineDetail :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				List<ApprLineDetailVO> apprLineDetailList = approvalService.getApprLineDetail(vo);
				
				if(apprLineDetailList != null && apprLineDetailList.size() != 0){
					jsonObject.put("success", true);
					jsonObject.put("data", apprLineDetailList);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
		//logger.info(jsonObject.toString());
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "saveAppr.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveApprCtrl(@RequestBody String paramData, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: saveAppr :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				//
				/*JSONArray jsonArray = JSONArray.fromObject(paramData);*/
				 
			    List<Map<String,Object>> resultMap = new ArrayList<Map<String,Object>>();
			    resultMap = JSONArray.fromObject(paramData);
			    List<ApprovalMasterVO> approvalMasterList = new ArrayList<>();
			    String contId = (String) resultMap.get(0).get("contId");
			    String[] contIdList = contId.split(",");
			    for(String refId : contIdList){
			    	ApprovalMasterVO vo = new ApprovalMasterVO();
			    	vo.setRefId(refId);
			    	if(!StringUtil.isNotNull(vo.getRequesterBizId())) {
						vo.setRequesterBizId(loginVO.getBizId());
					}
					if(!StringUtil.isNotNull(vo.getRequesterId())) {
						vo.setRequesterId(loginVO.getUserId());
					}
					vo.setApprStatus("REQ");
					approvalMasterList.add(vo);
			    }
			    String apprId = "";
			    for (ApprovalMasterVO approvalMasterVO : approvalMasterList){
			    	apprId = approvalService.saveApprovalMaster(approvalMasterVO);
			    	int index = 1;
			    	for (Map<String, Object> map : resultMap) {
			    		ApprovalDetailVO approvalDetailVO = new ApprovalDetailVO();
			    		approvalDetailVO.setApprId(apprId);
			    		approvalDetailVO.setApprSeq(String.valueOf(index));
			    		if(index == 1){
			    			approvalDetailVO.setApprStatus("REQ");
			    			SimpleDateFormat dateFormat = new SimpleDateFormat ( "yyyyMMddHHmmss");
			    			Date time = new Date();
			    			String apprDate = dateFormat.format(time);
			    			approvalDetailVO.setApprDate(apprDate);
			    		}
			    		approvalDetailVO.setInsEmpNo(approvalMasterVO.getRequesterId());
			    		approvalDetailVO.setUpdEmpNo(approvalMasterVO.getRequesterId());
			    		approvalDetailVO.setApprBizId(map.get("apprBizId").toString());
			    		approvalDetailVO.setApprUserId(map.get("apprUserId").toString());
			    		
			    		
			    		approvalService.saveApprovalDetail(approvalDetailVO);
				        index++;
				    }
			    	
			    	ContractPersonVO contractVO = new ContractPersonVO();
					contractVO.setContId(approvalMasterVO.getRefId());
					contractVO.setStatusCode("REQ");
					contractDAO.updContractPerson(contractVO);
					
					approvalMasterVO.setApprId(apprId);
					approvalMasterVO.setApprStatus("REQ");
					approvalMasterVO.setApprSeq("1");
					approvalService.insApprovalLog(approvalMasterVO);
			    }
			    approvalService.sendApprovalMail(approvalMasterList);
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
	
	@RequestMapping(method = RequestMethod.POST , value = "updateAppr.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updateApprCtrl(@RequestBody ApprovalMasterVO contId, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: updateAppr :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				//
				/*JSONArray jsonArray = JSONArray.fromObject(paramData);*/
				List<ApprovalMasterVO> approvalMasterList = new ArrayList<>();
			    String[] contIdList = contId.getContId().split(",");
			    for(String refId : contIdList){
			    	ApprovalMasterVO vo = new ApprovalMasterVO();
			    	vo.setRefId(refId);
			    	if(!StringUtil.isNotNull(vo.getRequesterBizId())) {
						vo.setRequesterBizId(loginVO.getBizId());
					}
					if(!StringUtil.isNotNull(vo.getRequesterId())) {
						vo.setRequesterId(loginVO.getUserId());
					}
					vo.setApprStatus(contId.getApprStatus());
					approvalService.updateApprovalDetail(vo);
					
					ApprovalMasterVO approvalMasterVO = approvalService.getApprovalSeq(vo);
					vo.setApprSeq(approvalMasterVO.getApprSeq());
					if(contId.getApprStatus().equals("APP")){
						if(!approvalMasterVO.getMaxSeq().equals(approvalMasterVO.getApprSeq())){
							vo.setApprStatus("REQ");
							
							approvalService.updateApprovalMaster(vo);
							
							ContractPersonVO contractVO = new ContractPersonVO();
							contractVO.setContId(vo.getRefId());
							contractVO.setStatusCode(vo.getApprStatus());
							contractDAO.updContractPerson(contractVO);
							
							approvalService.insApprovalLog(vo);
							approvalMasterList.add(vo);
						}else{
							approvalService.updateApprovalMaster(vo);
							
							ContractPersonVO contractVO = new ContractPersonVO();
							contractVO.setContId(vo.getRefId());
							contractVO.setStatusCode(vo.getApprStatus());
							contractDAO.updContractPerson(contractVO);
							approvalService.insApprovalLog(vo);
						}
					}else{
						approvalService.updateApprovalMaster(vo);
						
						ContractPersonVO contractVO = new ContractPersonVO();
						contractVO.setContId(vo.getRefId());
						contractVO.setStatusCode(vo.getApprStatus());
						contractDAO.updContractPerson(contractVO);
						approvalService.insApprovalLog(vo);
					}
			    }
			    approvalService.sendApprovalMail(approvalMasterList);
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
	
	@RequestMapping(method = RequestMethod.GET , value = "getApprLine.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getApprLineCtrl(@ModelAttribute("ApprovalMasterVO") ApprovalMasterVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			logger.info(":::::::::::::::::::: getApprLineDetail :::::::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				List<ApprovalMasterVO> apprLineList = approvalService.getApprLine(vo);
				
				if(apprLineList != null && apprLineList.size() != 0){
					jsonObject.put("success", true);
					jsonObject.put("data", apprLineList);
				} else {
					jsonObject.put("success", false);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
		//logger.info(jsonObject.toString());
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
}
