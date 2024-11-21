package com.ezsign.window.controller;

import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.service.YW500Service;
import com.ezsign.window.vo.WorkMonth;
import com.ezsign.window.vo.YW500Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("NonAsciiCharacters")
@Controller
@Api(tags = "YW500Controller", description = "연말정산Map")
@RequestMapping("/febworker/")
public class YW500Controller extends BaseController {

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "febMasterService")
    private FebMasterService febMasterService;

    @Resource(name = "yw500Service")
    private YW500Service yw500Service;

    @ApiOperation(value = "연말정산Map 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "yw500.do")
    @ResponseBody
    public ResponseEntity<YW500Response> getYW500(String 계약ID, String 사용자ID, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YW500Response response = new YW500Response();
        response.success = false;

        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));

        try {
            System.out.println(":::::::::::::::::::: getYW500 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                yw500Service.getYW500(loginVO.getBizId(), 계약ID, 사용자ID, response);
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
