package com.ezsign.schedule.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.schedule.service.ScheduleService;
import com.ezsign.schedule.vo.ScheduleVO;
import com.ezsign.schedule.vo.TuiCalendarRequestVO;


@Controller
@RequestMapping("/schedule/")
public class ScheduleController extends BaseController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "codeService")
	private CodeService codeService;

	@Resource(name = "scheduleService")
	private ScheduleService scheduleService;

	@RequestMapping(method = RequestMethod.POST , value = "insSchedule.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insScheduleCtrl(@ModelAttribute("ScheduleVO") ScheduleVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: insSchedule :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				logger.info("userId=>"+loginVO.getUserId());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				scheduleService.insSchedule(vo, loginVO.getBizName());
				success = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "delSchedule.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delScheduleCtrl(@ModelAttribute("ScheduleVO") ScheduleVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: delSchedule :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				scheduleService.delSchedule(vo);
				success = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "updSchedule.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updScheduleCtrl(@ModelAttribute("ScheduleVO") ScheduleVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: updSchedule :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				int total = scheduleService.updSchedule(vo);
				jsonObject.put("total", total);
				success = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET , value = "getScheduleList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getScheduleListCtrl(@ModelAttribute("ScheduleVO") ScheduleVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getScheduleList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				List<ScheduleVO> result= scheduleService.getScheduleList(vo);
				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
				success = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET , value = "getMainScheduleList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getMainScheduleListCtrl(@ModelAttribute("TuiCalendarRequestVO") TuiCalendarRequestVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getMainScheduleList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				logger.info("userId=>"+loginVO.getUserId());
				logger.info("유형=>"+vo.get유형());
				logger.info("기간From=>"+vo.get기간From());
				logger.info("기간To=>"+vo.get기간To());

				ScheduleVO scheduleVO = new ScheduleVO();
				scheduleVO.setUserId(loginVO.getUserId());
				scheduleVO.setBizId(loginVO.getBizId());
				scheduleVO.setSearchType(vo.get유형());
				scheduleVO.setSearchDateFrom(vo.get기간From());
				scheduleVO.setSearchDateTo(vo.get기간To());
				List<ScheduleVO> result= scheduleService.getScheduleList(scheduleVO);

				// 스케쥴구분
				CodeVO codeVO = new CodeVO();
				codeVO.setGrpCommCode("011");
				List<CodeVO> codeList = codeService.getCodeList(codeVO);
				jsonObject.put("유형", vo.get유형());
				jsonObject.put("기간From", vo.get기간From());
				jsonObject.put("기간To", vo.get기간To());
				jsonObject.put("data", result);
				jsonObject.put("캘린더", codeList);

				success = true;
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

}
