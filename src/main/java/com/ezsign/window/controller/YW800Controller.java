package com.ezsign.window.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
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

import com.ezsign.code.service.CodeService;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.system.service.YS030Service;
import com.ezsign.feb.system.service.YS031Service;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.feb.worker.service.YE700Service;
import com.ezsign.feb.worker.vo.YE700VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.service.YW800Service;
import com.ezsign.window.vo.DefaultResponse;
import com.ezsign.window.vo.WorkMonth;
import com.ezsign.window.vo.YW800ConfirmRequest;
import com.ezsign.window.vo.YW800DonationResponse;
import com.ezsign.window.vo.YW800Request;
import com.ezsign.window.vo.YW800Response;
import com.ezsign.window.vo.YW800UpdateRequest;
import com.ezsign.window.vo.YW800VO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@SuppressWarnings("NonAsciiCharacters")
@Controller
@Api(tags = "YW800Controller", description = "연말정산요약")
@RequestMapping("/febworker/")
public class YW800Controller extends BaseController {

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "febMasterService")
    private FebMasterService febMasterService;

    @Resource(name = "yw800Service")
    private YW800Service yw800Service;
    
    @Resource(name = "ye700Service")
	private YE700Service ye700Service;
    
    @Resource(name = "ys030Service")
	private YS030Service ys030Service;

	@Resource(name = "ys031Service")
	private YS031Service ys031Service;

	@Resource(name = "codeService")
	private CodeService codeService;

    @ApiOperation(value = "연말정산요약 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "yw800.do")
    @ResponseBody
    public ResponseEntity<YW800Response> getYW800(
            @RequestParam String 계약ID, @RequestParam String 사용자ID,
            HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YW800Response response = new YW800Response();
        response.success = false;

        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));

        try {
            System.out.println(":::::::::::::::::::: getYW800 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                yw800Service.getYW800(loginVO.getBizId(), 계약ID, 사용자ID, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "연말정산요약 기부금계산결과 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "donationYW800.do")
    @ResponseBody
    public ResponseEntity<YW800DonationResponse> getDonationYW800(
            @RequestParam String 계약ID, @RequestParam String 사용자ID,  @RequestParam String 세액공제구분코드,
            HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YW800DonationResponse response = new YW800DonationResponse();
        response.success = false;

        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));

        try {
            System.out.println(":::::::::::::::::::: getDonationYW800 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                yw800Service.getDonationYW800(계약ID, 사용자ID, 세액공제구분코드, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "연말정산요약 저장", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "yw800.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> postYW800(@RequestBody YW800Request body, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        response.success = false;

        try {
            System.out.println(":::::::::::::::::::: postYW800 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                yw800Service.saveYW800(loginVO.getBizId(), loginVO.getUserId(), body);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "연말정산요약 적용", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "updYW800.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> updYW800(@RequestBody YW800UpdateRequest body, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        response.success = false;

        try {
            System.out.println(":::::::::::::::::::: updYW800 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                yw800Service.updYW800(loginVO.getBizId(), loginVO.getUserId(), body, request);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "연말정산요약 확정", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "confirmYW800.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> confirmYW800(@RequestBody YW800ConfirmRequest body, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        response.success = false;

        try {
            System.out.println(":::::::::::::::::::: confirmYW800 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                int userType = Integer.parseInt(loginVO.getUserType());
                if (userType == 1) {
                    yw800Service.confirmYW800(loginVO.getBizId(), loginVO.getUserId(), "2", body, request);
                } else {
                    yw800Service.confirmYW800(loginVO.getBizId(), loginVO.getUserId(), "3", body, request);
                }
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "연말정산요약 최종 확정", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "finalYW800.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> finalYW800(@RequestBody YW800ConfirmRequest body, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        response.success = false;

        try {
            System.out.println(":::::::::::::::::::: finalYW800 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null || Integer.parseInt(loginVO.getUserType()) <= 1) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                yw800Service.confirmYW800(loginVO.getBizId(), loginVO.getUserId(), "4", body, request);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
    
    // 연말정산 요약 리스트 조회
 	@ApiOperation(value = "연말정산 요약 리스트 조회", produces = "application/json")
 	@RequestMapping(method = RequestMethod.GET, value = "getYW080WorkerList.do")
 	@ResponseBody
 	public ResponseEntity<JSONObject> getYW080WorkerListCtrl(@ModelAttribute("YE700VO") YE700VO vo,
 			HttpServletRequest request) throws Exception {
 		JSONObject jsonObject = new JSONObject();
 		HttpHeaders responseHeaders = new HttpHeaders();
 		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

 		HttpStatus status = HttpStatus.OK;
 		boolean success = false;
 		int total = 0;

 		try {
 			System.out.println(":::::::::::::::::::: getYW080WorkerList :::::::::::::::::::");
 			SessionVO loginVO = SessionUtil.getSession(request);

 			if (loginVO == null) {
 				status = HttpStatus.UNAUTHORIZED;
 			} else {
 				System.out.println("bizId=>" + loginVO.getBizId());
 				vo.setBizId(loginVO.getBizId());

 				// 연말정산 요약 리스트 조회
 				List<YE700VO> result = ye700Service.getYE700WorkerList(vo);
 				total = ye700Service.getYE700WorkerListCount(vo);

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
 				CodeVO codeVO1 = new CodeVO();
 				codeVO1.setGrpCommCode("420");
 				List<CodeVO> code420List = codeService.getCodeList(codeVO1);

 				// 마감상태
 				CodeVO codeVO2 = new CodeVO();
 				codeVO2.setGrpCommCode("451");
 				List<CodeVO> code451List = codeService.getCodeList(codeVO2);
 				
 				jsonObject.put("ys030", ys030List);
 				jsonObject.put("ys031", ys031List);
 				jsonObject.put("code420", code420List);
 				jsonObject.put("code451", code451List);

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
 	
 	@ApiOperation(value = "연말정산요약 선택 및 일괄 저장", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "saveAllYW800.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> saveAllYW800(@RequestBody List<YW800Request> list, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        response.success = false;

        try {
            System.out.println(":::::::::::::::::::: saveAllYW800 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	
            	// 연말정산요약 선택 및 일괄 저장
                yw800Service.saveAllYW800(loginVO.getBizId(), loginVO.getUserId(), list);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
 	
 	@ApiOperation(value = "연말정산요약 선택 및 일괄 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "getYW800List.do")
    @ResponseBody
    public ResponseEntity<String> getYW800List(@RequestBody List<YW800VO> list , HttpServletRequest request) throws Exception {
        
 		HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        String json = "";
        List<YW800Response> reponseList = new ArrayList<>();
        try {
            System.out.println(":::::::::::::::::::: getYW800List :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	
            	List<YW800VO> yw000List = new ArrayList<>();
            	for (int i = 0 ; i < list.size(); i++) {
            		
            		// 리턴 객체
            		YW800Response response = new YW800Response();
                    response.success = false;
                    
                    YW800VO yw800VO = new YW800VO();
                    yw800VO= list.get(i);
            		
            		// 근로자 근무년월 설정
            		YE000VO work = new YE000VO();
            		work.set계약ID(yw800VO.get계약ID());
            		work.set사용자ID(yw800VO.get사용자ID());
            		
            		response.계약ID = yw800VO.get계약ID();
            		response.사용자ID = yw800VO.get사용자ID();
            		response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));
            		
            		// 근로자 연말정산 요약 설정
            		yw800Service.getYW800(loginVO.getBizId(), yw800VO.get계약ID(), yw800VO.get사용자ID(), response);
            		response.success = true;
            		
            		reponseList.add(response);
            		
            	}
            	
            	// json to string (json null 값 오류 처리)
            	json = new ObjectMapper().writeValueAsString(reponseList);
            	
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(json, responseHeaders, status);
    }
 	
 	@ApiOperation(value = "연말정산요약 마감 최종 확정", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "confirmFinalAllYW800.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> confirmFinalAllYW800(@RequestBody List<YW800VO> list, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");        
        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        response.success = false;

        try {
            System.out.println(":::::::::::::::::::: confirmFinalAllYW800 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	
            	// 연말정산요약 마감 최종 확정
            	yw800Service.confirmAllYW800(loginVO.getUserId(), list);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
}
