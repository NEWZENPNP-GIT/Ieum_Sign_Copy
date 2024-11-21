package com.ezsign.window.controller;

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

import com.ezsign.code.service.CodeService;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.worker.vo.YW900VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.service.YW900Service;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/febworker/")
public class YW900Controller extends BaseController {

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "yw900Service")
    private YW900Service yw900Service;    

	@Resource(name = "codeService")
	private CodeService codeService;
	
	@Resource(name = "ys030Service")
	private YS030Service ys030Service;

    @RequestMapping(method = RequestMethod.GET , value = "yw900.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getYW900List(@ModelAttribute("YW900VO") YW900VO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getYW900List :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				
				//계약ID
				//제출대상구분코드 - 셀렉트박스
				//페이징처리 시작, 끝
				
				System.out.println("계약ID=>"+vo.get계약ID());
				System.out.println("제출대상구분코드=>"+vo.get제출대상구분코드());
				System.out.println("endPage=>"+vo.getEndPage());
				System.out.println("startPage=>"+vo.getStartPage());
				
				List<YW900VO> result= yw900Service.getYW900List(vo);
				
				System.out.println("+++++++++++++++++"+result);
				total = yw900Service.getYW900ListCount(vo);
				

				YS030VO YS030VO = new YS030VO();
				YS030VO.setBizId(loginVO.getBizId());	
				YS030VO.set계약ID(vo.get계약ID());
				YS030VO.setStartPage(vo.getStartPage());
				YS030VO.setEndPage(vo.getEndPage());
				
				
				List<YS030VO> workspace= yw900Service.getYS030List(YS030VO);
				
				// 근무상태
				CodeVO codeVO = new CodeVO();
				codeVO.setGrpCommCode("417");
				List<CodeVO> code417List = codeService.getCodeList(codeVO);
				
				if(result != null && result.size() != 0){
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
					jsonObject.put("workspace", workspace);
					jsonObject.put("code417", code417List);
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
}
