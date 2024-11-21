package com.ezsign.dept.controller;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

import com.ezsign.common.controller.BaseController;
import com.ezsign.dept.service.DeptService;
import com.ezsign.dept.vo.DeptVO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;

@Controller
@RequestMapping("/dept/")
public class DeptController extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "deptService")
	private DeptService deptService;
	
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST} , value = "getDeptList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getDeptListCtrl(@ModelAttribute("DeptVO") DeptVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getDeptList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				List<DeptVO> result= deptService.getDeptList(vo);
				
				success = true;
				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "saveDept.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveDeptCtrl(@RequestBody List<DeptVO> list, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {			
			System.out.println(":::::::::::::::::::: saveDept :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				for(int i=0;i<list.size();i++) {
					DeptVO deptVO = list.get(i);
					deptVO.setBizId(loginVO.getBizId());
					list.set(i, deptVO);
					
					System.out.println("size=>"+deptVO.getSubAdminList().size());
				}
				int result = deptService.saveDept(loginVO.getBizId(), list);
				
				success = true;
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

	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST} , value = "getDeptEmpList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getDeptEmpListCtrl(@ModelAttribute("DeptVO") DeptVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getDeptEmpList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				List<EmpVO> noneDeptList = new ArrayList<EmpVO>();
				List<EmpVO> allotDeptList = new ArrayList<EmpVO>();
				
				if(StringUtil.isNotNull(vo.getDeptCode())) {
					allotDeptList = deptService.getDeptEmpList(vo);
				}
				
				vo.setDeptCode("");
				List<EmpVO> result = deptService.getDeptEmpList(vo);
				
				for(int i=0;i<result.size();i++) {
					EmpVO empVO = result.get(i);
					if(StringUtil.isNull(empVO.getDeptCode())) {
						noneDeptList.add(empVO);
					}
				}
				
				success = true;
				jsonObject.put("allotdata", allotDeptList);
				jsonObject.put("nonedata", noneDeptList);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "delMiddle.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delMiddelCtrl(@RequestBody DeptVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			System.out.println(":::::::::::::::::::: delMiddle :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				deptService.delMiddle(loginVO.getBizId(), vo);

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
