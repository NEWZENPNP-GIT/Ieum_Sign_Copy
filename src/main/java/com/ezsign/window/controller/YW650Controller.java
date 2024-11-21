package com.ezsign.window.controller;

import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.service.YW650Service;
import com.ezsign.window.vo.DefaultResponse;
import com.ezsign.window.vo.WorkMonth;
import com.ezsign.window.vo.YW650CreditCardResponse;
import com.ezsign.window.vo.YW650Request;
import com.ezsign.window.vo.YW650Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@SuppressWarnings("NonAsciiCharacters")
@Controller
@Api(tags = "YW650Controller", description = "신용카드")
@RequestMapping("/febworker/")
public class YW650Controller extends BaseController {

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "febMasterService")
    private FebMasterService febMasterService;

    @Resource(name = "yw650Service")
    private YW650Service yw650Service;

    @ApiOperation(value = "신용카드 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "yw650.do")
    @ResponseBody
    public ResponseEntity<YW650Response> getYW650(String 계약ID, String 사용자ID, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YW650Response response = new YW650Response();
        response.success = false;

        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));

        response.신용카드 = new ArrayList<>();
        response.신용카드_정정사유 = new YE900VO();

        try {
            System.out.println(":::::::::::::::::::: getYW650 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                yw650Service.getYW650(계약ID, 사용자ID, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "신용카드 수정", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "yw650.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> postYW650(@RequestBody YW650Request body, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        response.success = false;

        try {
            System.out.println(":::::::::::::::::::: postYW650 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	int userType = Integer.parseInt(loginVO.getUserType());
            	
                yw650Service.saveYW650(loginVO.getUserId(), body, userType);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
    
    // 신용카드 계산결과 조회
    @ApiOperation(value = "신용카드 계산결과 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "creditCardYW650.do")
    @ResponseBody
    public ResponseEntity<YW650CreditCardResponse> getCreditCardYW650(
            @RequestParam String 계약ID, @RequestParam String 사용자ID, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YW650CreditCardResponse response = new YW650CreditCardResponse();
        response.success = false;

        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));

        try {
            System.out.println(":::::::::::::::::::: getCreditCardYW650 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	yw650Service.getCreditCardYW650(계약ID, 사용자ID, response);
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
