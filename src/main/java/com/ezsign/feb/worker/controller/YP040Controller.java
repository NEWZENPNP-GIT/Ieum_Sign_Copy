package com.ezsign.feb.worker.controller;

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

import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.vo.YP000VO;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.service.YS050Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.system.vo.YS050VO;
import com.ezsign.feb.worker.service.YP040Service;
import com.ezsign.feb.worker.vo.YP040VO;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.vo.WorkMonthPayment;

import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/febworker/")
public class YP040Controller extends BaseController {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name = "ys000Service")
	private YS000Service ys000Service;
	
	@Resource(name = "ys050Service")
	private YS050Service ys050Service;
	
	@Resource(name = "yp040Service")
	private YP040Service yp040Service;
 
	@Resource(name = "febMasterService")
	private FebMasterService febMasterService;
	
	/**
	 * 간이지급명세서_소득명세합계 조회
	 * 
	 * @param vo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "간이지급명세서 소득명세합계 조회", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST, value = "getYP040.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYP040(@ModelAttribute("YP040VO") YP040VO vo, HttpServletRequest request)
			throws Exception {
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			System.out.println(":::::::::::::::::::: getYE040 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>" + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());

				YP040VO result = yp040Service.getYP040(vo);
				List<YP040VO> yp040list = yp040Service.getYP040List(vo);

				// 계약년도 조회
				YS000VO ys000VO = new YS000VO();
				ys000VO.setBizId(loginVO.getBizId());
				ys000VO.set계약ID(vo.get계약ID());
				List<YS000VO> ys000VOList = ys000Service.getYS000List(ys000VO);
				String 계약년도 = "";
				if (ys000VOList.size() > 0) {
					계약년도 = ys000VOList.get(0).getFebYear();
				}
				jsonObject.put("계약년도", 계약년도);

				// 근무년월 조회
				YP000VO work = new YP000VO();
				work.set계약ID(vo.get계약ID());
				work.set사용자ID(vo.get사용자ID());
				work.set근무시기(vo.get근무시기());
				WorkMonthPayment workMonthVO = new WorkMonthPayment(febMasterService.getPaymentUserWorkMonth(work));

				// 급여항목
				YS050VO ys050VO = new YS050VO();
				ys050VO.set계약ID(vo.get계약ID());
				ys050VO = ys050Service.getYS050(ys050VO);

				jsonObject.put("근무년월", workMonthVO);
				jsonObject.put("ys050", ys050VO);
				jsonObject.put("total", yp040list.size());
				jsonObject.put("sumData", result);
				jsonObject.put("data", yp040list);

				jsonObject.put("근무년월", workMonthVO);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.error(ex.getMessage(), ex );
			throw ex;
		}

		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 연말정산_소득명세합계 입력(수정)
	@ApiOperation(value = "간이지급명세서 소득명세합계 입력", produces = "application/json")
	@RequestMapping(method = RequestMethod.POST, value = "insYP040.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insYP040(@ModelAttribute("YP040VO") YP040VO vo, HttpServletRequest request)
			throws Exception {
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			System.out.println(":::::::::::::::::::: insYP040 :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>" + loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.set작업자ID(loginVO.getUserId());

				yp040Service.insYP040(vo);

				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.error(ex.getMessage(), ex );
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
}
