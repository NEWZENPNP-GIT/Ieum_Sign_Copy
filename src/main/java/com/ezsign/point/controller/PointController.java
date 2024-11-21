package com.ezsign.point.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.point.service.PointService;
import com.ezsign.point.vo.DistPointVO;
import com.ezsign.point.vo.PointLogVO;
import com.ezsign.point.vo.PointVO;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("/")
public class PointController extends BaseController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "pointService")
	private PointService pointService;

	@RequestMapping(method = RequestMethod.GET , value = "getPoint.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPointCtrl(@ModelAttribute("PointVO") PointVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result = false;
		int point = 0;
		String bizName = "";
		String taxBillBizName = "";
		String taxBillName = "";
		String taxBillTel = "";
		String taxBillEmail = "";

		try {
			logger.info(":::::::::::::::::::: getPoint :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if(StringUtil.isNull(vo.getBizId())) {
					vo.setBizId(loginVO.getBizId());
				}
				PointVO pointVO = pointService.getPoint(vo);
				if(pointVO!=null) {
					point = pointVO.getCurPoint();
					bizName = pointVO.getBizName();
					taxBillBizName = pointVO.getTaxBillBizName();
					taxBillName = pointVO.getTaxBillName();
					taxBillTel = pointVO.getTaxBillTel();
					taxBillEmail = pointVO.getTaxBillEmail();
				}
				jsonObject.put("bizId", vo.getBizId());
				jsonObject.put("bizName", bizName);
				jsonObject.put("taxBillBizName", taxBillBizName);
				jsonObject.put("taxBillName", taxBillName);
				jsonObject.put("taxBillTel", taxBillTel);
				jsonObject.put("taxBillEmail", taxBillEmail);
				result = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);
		jsonObject.put("point", point);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET , value = "getPointLogList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPointLogListCtrl(@ModelAttribute("PointLogVO") PointLogVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;

		try {
			logger.info(":::::::::::::::::::: getPointLogList :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				List<PointLogVO> result = pointService.getPointLogList(vo);
				int total = pointService.getPointLogListCount(vo);

				if(result != null && result.size() != 0){
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
				}else{
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "updPoint.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updPointCtrl(@ModelAttribute("PointVO") PointVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result = false;
		int point = 0;

		try {
			logger.info(":::::::::::::::::::: updPoint :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setUserId(loginVO.getUserId());
				int count = pointService.updPoint(vo);

				if(count>0) result = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "updPointNew.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updPointNewCtrl(@ModelAttribute("PointVO") PointVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result = false;
		int point = 0;

		try {
			logger.info(":::::::::::::::::::: updPoint :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setUserId(loginVO.getUserId());
				int count = pointService.updPointNew(vo);

				if(count>0) result = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method=RequestMethod.GET, value="goPointCharge.do")
	public String goPointChargeCtrl(@ModelAttribute("PointVO") PointVO vo, HttpServletRequest request) {

		String redirectUri = "login/login";

		logger.info(":::::::::::::::::::: goPointCharge :::::::::::::::::::");

		try {

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				redirectUri = "login/login";
			} else {
				logger.info("사용자명=>"+loginVO.getBizId() + " - " + loginVO.getUserName());
				if(StringUtil.isNotNull(vo.getBizId())) {
					redirectUri = "point/myPage_pointCharging";
				}
			}
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
		}

		return redirectUri;
	}

	@RequestMapping(method = RequestMethod.POST , value = "updPointAdjust.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updPointAdjustCtrl(@RequestBody List<DistPointVO> params, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean result = false;
		int point = 0;

		try {
			logger.info(":::::::::::::::::::: updPoint :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				for(int i=0; i<params.size(); i++){
					DistPointVO vo = params.get(i);
					vo.setUserId(loginVO.getUserId());
					int count = pointService.updPointDist(vo);
					if(count>0) result = true;
				}

			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", result);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
}
