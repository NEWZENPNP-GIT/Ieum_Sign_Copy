package com.ezsign.calendar.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

import com.ezsign.biz.service.BizService;
import com.ezsign.calendar.service.CalendarService;
import com.ezsign.calendar.vo.CalendarVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.user.service.UserService;
import com.ezsign.user.vo.UserGrpVO;
import com.ezsign.user.vo.UserJoinCertVO;
import com.ezsign.user.vo.UserVO;


@Controller
@RequestMapping("/calendar/")
public class CalendarController extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "calendarService")
	private CalendarService calendarService;
	
	@RequestMapping(method = RequestMethod.POST , value = "insCalendar.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insCalendarCtrl(@ModelAttribute("CalendarVO") CalendarVO vo, HttpServletRequest request) throws Exception
	{
		// 일정등록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: insCalendar :::::::::::::::::::");
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				calendarService.insCalendar(vo);
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

	@RequestMapping(method = RequestMethod.POST , value = "delCalendar.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delCalendarCtrl(@ModelAttribute("CalendarVO") CalendarVO vo, HttpServletRequest request) throws Exception
	{
		// 일정등록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: delCalendar :::::::::::::::::::");
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				calendarService.delCalendar(vo);
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

	@RequestMapping(method = RequestMethod.POST , value = "updCalendar.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updCalendarCtrl(@ModelAttribute("CalendarVO") CalendarVO vo, HttpServletRequest request) throws Exception
	{
		// 일정등록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: updCalendar :::::::::::::::::::");
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				int total = calendarService.updCalendar(vo);
				jsonObject.put("total", total);
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
	
	@RequestMapping(method = RequestMethod.GET , value = "getCalendarList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getCalendarListCtrl(@ModelAttribute("CalendarVO") CalendarVO vo, HttpServletRequest request) throws Exception
	{
		// 사용자 그룹권한 리스트
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getCalendarList :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				List<CalendarVO> result= calendarService.getCalendarList(vo);
				
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
	
}
