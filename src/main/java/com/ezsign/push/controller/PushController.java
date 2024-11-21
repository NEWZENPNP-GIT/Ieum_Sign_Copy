package com.ezsign.push.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.common.controller.BaseController;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.push.service.PushService;
import com.ezsign.push.util.FCM;
import com.ezsign.push.vo.PushVO;
import com.ezsign.push.vo.ReturnResult;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("/push/")
public class PushController extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "pushService")
	private PushService pushService;
	
	//Push대상 Service Test
	/*
	@RequestMapping(method = RequestMethod.GET , value = "setPushTest.do")
	@ResponseBody
	public ResponseEntity<JSONObject> setPushTest(@ModelAttribute("PushVO") PushVO vo, HttpServletRequest request) throws Exception
	{

		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			
			
			
			System.out.println(":::::::::::::::::::: setPushTest :::::::::::::::::::");
			System.out.println(vo.toString());
			
			PushVO resultVo = new PushVO();
			
			
			if((FCM.PushType.TOKEN).equals(vo.getToType())) {
				//PUSH 대상 : 사용자
				PushVO testUserVo = new PushVO();
				testUserVo.setToType("003");
				testUserVo.setPushType("001");
				testUserVo.setUserId("180706171106001");
				testUserVo.setBookDate("20181207141000");
				testUserVo.setBody("개인푸쉬 테스트입니다.");
				resultVo = pushService.insPushSendList(testUserVo);
			} else if((FCM.PushType.TOPIC).equals(vo.getToType())) {
				//PUSH 대상 : 회사
				PushVO testBizVo = new PushVO();
				testBizVo.setToType("002");
				testBizVo.setPushType("001");
				testBizVo.setBizId("180706171106007");
				testBizVo.setBookDate("20181207141500");
				testBizVo.setBody("회사푸쉬 테스트입니다.");
				resultVo = pushService.insPushSendList(testBizVo);
			} else if((FCM.PushType.CANDY).equals(vo.getToType())) {
				//PUSH 대상 : 앱 전체
				PushVO testAllVo = new PushVO();
				testAllVo.setToType("001");
				testAllVo.setPushType("001");
				testAllVo.setBookDate("20181207142000");
				testAllVo.setBody("전체푸쉬 테스트입니다.");
				resultVo = pushService.insPushSendList(testAllVo);
			}
			
			jsonObject.put("result", resultVo.getResult());
			jsonObject.put("resultCode", resultVo.getResultCode());
			success = true;
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("result", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	*/
	
	//읽지 않은 푸쉬 리스트 가져오기 API
	@RequestMapping(method = RequestMethod.GET , value = "getPushList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPushList(@ModelAttribute("PushVO") PushVO vo, HttpServletRequest request) throws Exception
	{

		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: getPushList :::::::::::::::::::");
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("deviceId=>"+vo.getDeviceId());
				System.out.println("bizId=>"+vo.getBizId());
				
				List<PushVO> result= pushService.getPushList(vo);
				
				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("result", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	
	//읽지 않은 푸쉬 갯수 가져오기 API
	@RequestMapping(method = RequestMethod.GET , value = "getReadCnt.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getReadCnt(@ModelAttribute("PushVO") PushVO vo, HttpServletRequest request) throws Exception {

		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			System.out.println(":::::::::::::::::::: getReadCnt :::::::::::::::::::");
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
				System.out.println("deviceId=>"+vo.getDeviceId());
				System.out.println("bizId=>"+vo.getBizId());
				
				PushVO pushVo = pushService.getReadCnt(vo);
				
				if(null != pushVo && null != pushVo.getNonReadPushCnt()){
					jsonObject.put("result", true);
					jsonObject.put("resultCode", ReturnResult.ResultCode.NOERROR);
					jsonObject.put("data", pushVo.getNonReadPushCnt());
				}else{
					jsonObject.put("result", false);
					jsonObject.put("resultCode", ReturnResult.ResultCode.DB_SELECT_ERROR);
				}
			}
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	//읽지 않은 푸쉬 리스트 중 한개 읽음 처리 API
	@RequestMapping(method = RequestMethod.GET , value = "insPushRead.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insPushRead(@ModelAttribute("PushVO") PushVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		boolean result = false;
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			System.out.println(":::::::::::::::::::: insPushRead :::::::::::::::::::");
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
				boolean checkFlag = true;
				
				if(null == vo) {
					jsonObject.put("resultCode", ReturnResult.ResultCode.PARAMETER_NOTFOUND);
					checkFlag = false;
				}
				
				System.out.println("pushId=>"+vo.getPushId());
				if(null == vo.getPushId() || ("").equals(vo.getPushId())) {
					jsonObject.put("resultCode", ReturnResult.ResultCode.PUSHID_NOTFOUND);
					checkFlag = false;
				}
				
				System.out.println("deviceId=>"+vo.getDeviceId());
				if(null == vo.getDeviceId() || ("").equals(vo.getDeviceId())) {
					jsonObject.put("resultCode", ReturnResult.ResultCode.DEVICEID_NOTFOUND);
					checkFlag = false;
				}
				
				int success = 0;
				if(checkFlag) {
					success = pushService.insPushRead(vo);
				}
				
				if(success > 0) result = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("result", result);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	
	
}
