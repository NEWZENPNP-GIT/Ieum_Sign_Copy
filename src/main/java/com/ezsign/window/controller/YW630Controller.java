package com.ezsign.window.controller;

import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.service.YW630Service;
import com.ezsign.window.vo.DefaultResponse;
import com.ezsign.window.vo.WorkMonth;
import com.ezsign.window.vo.YW630Request;
import com.ezsign.window.vo.YW630Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@SuppressWarnings("NonAsciiCharacters")
@Controller
@Api(tags = "YW630Controller", description = "개인연금저축")
@RequestMapping("/febworker/")
public class YW630Controller extends BaseController {

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "febMasterService")
    private FebMasterService febMasterService;

    @Resource(name = "yw630Service")
    private YW630Service yw630Service;

    @ApiOperation(value = "개인연금저축 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "yw630.do")
    @ResponseBody
    public ResponseEntity<YW630Response> getYW630(String 계약ID, String 사용자ID, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YW630Response response = new YW630Response();
        response.success = false;

        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));

        response.개인연금저축 = new ArrayList<>();
        response.개인연금저축_정정사유 = new YE900VO();

        try {
            System.out.println(":::::::::::::::::::: getYW630 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                yw630Service.getYW630(계약ID, 사용자ID, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "개인연금저축 수정", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "yw630.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> postYW630(@RequestBody YW630Request body, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        response.success = false;

        try {
            System.out.println(":::::::::::::::::::: postYW630 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	int userType = Integer.parseInt(loginVO.getUserType());
            	
                yw630Service.saveYW630(loginVO.getUserId(), body, userType);
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
