package com.ezsign.window.controller;

import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.service.YW700Service;
import com.ezsign.window.vo.DefaultResponse;
import com.ezsign.window.vo.WorkMonth;
import com.ezsign.window.vo.YW700Request;
import com.ezsign.window.vo.YW700Response;
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
@Api(tags = "YW700Controller", description = "연금계좌")
@RequestMapping("/febworker/")
public class YW700Controller extends BaseController {

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "febMasterService")
    private FebMasterService febMasterService;

    @Resource(name = "yw700Service")
    private YW700Service yw700Service;

    @ApiOperation(value = "연금계좌 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "yw700.do")
    @ResponseBody
    public ResponseEntity<YW700Response> getYW700(String 계약ID, String 사용자ID, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YW700Response response = new YW700Response();
        response.success = false;

        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));

        response.퇴직연금계좌 = new ArrayList<>();
        response.연금저축계좌 = new ArrayList<>();

        response.퇴직연금계좌_정정사유 = new YE900VO();
        response.연금저축계좌_정정사유 = new YE900VO();

        try {
            System.out.println(":::::::::::::::::::: getYW700 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                yw700Service.getYW700(계약ID, 사용자ID, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "연금계좌 수정", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "yw700.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> postYW700(@RequestBody YW700Request body, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        response.success = false;

        try {
            System.out.println(":::::::::::::::::::: postYW700 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	int userType = Integer.parseInt(loginVO.getUserType());
            	
                yw700Service.saveYW700(loginVO.getUserId(), body, userType);
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
