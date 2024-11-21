package com.ezsign.feb.worker.controller;

import java.util.ArrayList;
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

import com.ezsign.code.service.CodeService;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.service.YS031Service;
import com.ezsign.feb.system.service.YS050Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.feb.system.vo.YS050VO;
import com.ezsign.feb.worker.service.YE040Service;
import com.ezsign.feb.worker.vo.YE040VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.vo.WorkMonth;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@Controller
@Api(tags = "YE040Controller", description = "연말정산 소득명세합계")
@RequestMapping("/febworker/")
public class YE040Controller extends BaseController {

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "ys000Service")
	private YS000Service ys000Service;

	@Resource(name = "ye040Service")
	private YE040Service ye040Service;

	@Resource(name = "ys050Service")
	private YS050Service ys050Service;

	@Resource(name = "febMasterService")
	private FebMasterService febMasterService;

	@Resource(name = "ys030Service")
	private YS030Service ys030Service;

	@Resource(name = "ys031Service")
	private YS031Service ys031Service;

	@Resource(name = "codeService")
	private CodeService codeService;

	// 연말정산_소득명세합계 조회
	@ApiOperation(value = "연말정산 소득명세합계 조회", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET, value = "getYE040.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE040Ctrl(@ModelAttribute("YE040VO") YE040VO vo, HttpServletRequest request)
			throws Exception {
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;

		try {
			System.out.println(":::::::::::::::::::: getYE040 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>" + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());

				YE040VO result = ye040Service.getYE040(vo);
				List<YE040VO> ye040list = ye040Service.getYE040List(vo);

				// 계약년도 조회
				YS000VO ys000VO = new YS000VO();
				ys000VO.setBizId(loginVO.getBizId());
				ys000VO.set계약ID(vo.get계약ID());
				List<YS000VO> ys000VOList = ys000Service.getYS000List(ys000VO);
				String 계약년도 = "";
				if (ys000VOList.size() > 0) {
					계약년도 = ys000VOList.get(0).getFebYear();
				}
				jsonObject.put("계약년도", ys000VOList.get(0).getFebYear());

				// 근무년월 조회
				YE000VO work = new YE000VO();
				work.set계약ID(vo.get계약ID());
				work.set사용자ID(vo.get사용자ID());
				WorkMonth workMonthVO = new WorkMonth(febMasterService.getUserWorkMonth(work));

				// 급여항목
				YS050VO ys050VO = new YS050VO();
				ys050VO.set계약ID(vo.get계약ID());
				ys050VO = ys050Service.getYS050(ys050VO);

				jsonObject.put("근무년월", workMonthVO);
				jsonObject.put("ys050", ys050VO);
				jsonObject.put("total", ye040list.size());
				jsonObject.put("sumData", result);
				jsonObject.put("data", ye040list);

				jsonObject.put("근무년월", workMonthVO);
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

	// 연말정산_소득명세합계 입력(수정)
	@ApiOperation(value = "연말정산 소득명세합계 입력", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST, value = "insYE040.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insYE040Ctrl(@ModelAttribute("YE040VO") YE040VO vo, HttpServletRequest request)
			throws Exception {
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			System.out.println(":::::::::::::::::::: insYE040 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>" + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.set작업자ID(loginVO.getUserId());

				ye040Service.insYE040(vo);

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

	// 연말정산 소득명세합계 선택 일괄 조회
	@ApiOperation(value = "연말정산 소득명세합계 선택 일괄 조회", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST, value = "getYE040View.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE040ViewCtrl(@RequestBody List<YE040VO> list, HttpServletRequest request)
			throws Exception {
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;

		try {
			System.out.println(":::::::::::::::::::: getYE040View :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>" + loginVO.getBizId());
				String 계약ID = list.get(0).get계약ID();

				// 선택 및 일괄 소득명세합계 조회
				List<YE040VO> responseList = new ArrayList<>();
				responseList = ye040Service.getYE040View(list);

				// 계약년도 조회
				YS000VO ys000VO = new YS000VO();
				ys000VO.setBizId(loginVO.getBizId());
				ys000VO.set계약ID(계약ID);

				List<YS000VO> ys000VOList = ys000Service.getYS000List(ys000VO);
				String 계약년도 = "";
				if (ys000VOList.size() > 0) {
					계약년도 = ys000VOList.get(0).getFebYear();
				}
				jsonObject.put("계약년도", ys000VOList.get(0).getFebYear());

				// 급여항목
				YS050VO ys050VO = new YS050VO();
				ys050VO.set계약ID(계약ID);
				ys050VO = ys050Service.getYS050(ys050VO);

				jsonObject.put("ys050", ys050VO);
				jsonObject.put("data", responseList);

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

	// 연말정산 소득명세합계 리스트 조회
	@ApiOperation(value = "연말정산 소득명세합계 리스트 조회", produces = "application/json")
	@RequestMapping(method = RequestMethod.GET, value = "getYE040WorkerList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYE040WorkerListCtrl(@ModelAttribute("YE040VO") YE040VO vo,
			HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;

		try {
			System.out.println(":::::::::::::::::::: getYE040WorkerList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>" + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());

				List<YE040VO> result = ye040Service.getYE040WorkerList(vo);
				total = ye040Service.getYE040WorkerListCount(vo);

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

	// 연말정산_소득명세합계 선택 및 일괄 입력
	@ApiOperation(value = "연말정산 소득명세합계 선택 및 일괄 입력", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST, value = "saveYE040.do")
	@ResponseBody
	public ResponseEntity<JSONObject> saveYE040Ctrl(@RequestBody List<YE040VO> list, HttpServletRequest request)
			throws Exception {
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			System.out.println(":::::::::::::::::::: saveYE040 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>" + loginVO.getBizId());

				for (int i = 0; i < list.size(); i++) {
					YE040VO ye040VO = list.get(i);

					ye040VO.setBizId(loginVO.getBizId());
					ye040VO.set작업자ID(loginVO.getUserId());
					list.set(i, ye040VO);
				}

				ye040Service.saveYE040(list);

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
