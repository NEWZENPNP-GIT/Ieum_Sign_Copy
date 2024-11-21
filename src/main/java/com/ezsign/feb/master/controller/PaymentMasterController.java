package com.ezsign.feb.master.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.ezsign.feb.hometax.service.YP800Service;
import com.ezsign.feb.hometax.vo.YP800VO;
import com.ezsign.feb.master.vo.YP000VO;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/paymaster/")
public class PaymentMasterController extends BaseController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "ys000Service")
	private YS000Service ys000Service;

	@Resource(name = "yp800Service")
	private YP800Service yp800Service;
	
	/**
	 * 간이지급명세서 진행관리
	 * 
	 * @param vo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST, value = "getPaymentStatusMng.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getPaymentStatusMng(@ModelAttribute("YP000VO") YP000VO vo, HttpServletRequest request) throws Exception{
	
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		
		boolean success = false;
		
		try {
			logger.info(":::::::::::::::::::: getPaymentStatusMng :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
				//bizId 기준 계약년도 조회 
				YS000VO ys000VO = new YS000VO();
				ys000VO.setBizId(loginVO.getBizId());				
//				if(StringUtils.isNotEmpty(vo.getFebYear())){
//					ys000VO.setFebYear(vo.getFebYear());
//				}
				
				List<YS000VO> ys000List = ys000Service.getYS000List(ys000VO);

	            if (ys000List != null && ys000List.size() > 0) {
	            	
	            	if(StringUtils.isNotEmpty(vo.getFebYear())){
	            		
	            		for(YS000VO rstVO :  ys000List){
	            			if(StringUtils.equals(vo.getFebYear(), rstVO.getFebYear())){
	            				loginVO.setYearContractId(rstVO.get계약ID());
	    		                loginVO.setFebYear(rstVO.getFebYear());
	    		                
	    		                vo.set계약ID(rstVO.get계약ID());
	            			}
	            		}
	            		
	            	}
	            	
	            }

				YP800VO yp800VO = new YP800VO();
				yp800VO.set계약ID(vo.get계약ID());				
				
				//상반기 정보 조회
				yp800VO.set근무시기("1");
				List<YP800VO> firstPayList = yp800Service.getYP800List(yp800VO);
				
				//하반기 정보 조회
				yp800VO.set근무시기("2");
				List<YP800VO> secondPayList = yp800Service.getYP800List(yp800VO);
				
				if(firstPayList != null && firstPayList.size() > 0){
					jsonObject.put("firstPayInfo", firstPayList.get(0));
				}else{
					jsonObject.put("firstPayInfo","");
				}
				
				if(secondPayList != null && secondPayList.size() > 0){
					jsonObject.put("secondPayInfo", secondPayList.get(0));
				}else{
					jsonObject.put("secondPayInfo","");
				}

				success = true;
				jsonObject.put("ys000List", ys000List);
			}
						
			jsonObject.put("success", success);
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
		
}
