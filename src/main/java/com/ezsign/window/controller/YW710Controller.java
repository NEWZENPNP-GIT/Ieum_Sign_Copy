package com.ezsign.window.controller;

import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.service.YW710Service;
import com.ezsign.window.vo.DefaultResponse;
import com.ezsign.window.vo.WorkMonth;
import com.ezsign.window.vo.YW710MedicalResponse;
import com.ezsign.window.vo.YW710Request;
import com.ezsign.window.vo.YW710Response;

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
@Api(tags = "YW710Controller", description = "특별세액공제")
@RequestMapping("/febworker/")
public class YW710Controller extends BaseController {

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "febMasterService")
    private FebMasterService febMasterService;

    @Resource(name = "yw710Service")
    private YW710Service yw710Service;

    @ApiOperation(value = "특별세액공제 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "yw710.do")
    @ResponseBody
    public ResponseEntity<YW710Response> getYW710(String 계약ID, String 사용자ID, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YW710Response response = new YW710Response();
        response.success = false;

        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));

        response.보험료 = new ArrayList<>();
        response.의료비 = new ArrayList<>();
        response.교육비 = new ArrayList<>();
        response.기부금 = new ArrayList<>();
        response.이월기부금 = new ArrayList<>();
        response.기부금_조정명세 = new ArrayList<>();

        response.보험료_정정사유 = new YE900VO();
        response.의료비_정정사유 = new YE900VO();
        response.교육비_정정사유 = new YE900VO();
        response.기부금_정정사유 = new YE900VO();

        try {
            System.out.println(":::::::::::::::::::: getYW710 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                yw710Service.getYW710(loginVO.getBizId(), 계약ID, 사용자ID, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "특별세액공제 수정", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "yw710.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> postYW710(@RequestBody YW710Request body, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        response.success = false;

        try {
            System.out.println(":::::::::::::::::::: postYW710 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	int userType = Integer.parseInt(loginVO.getUserType());
            	
                yw710Service.saveYW710(loginVO.getBizId(), loginVO.getUserId(), body, userType);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
    
    @ApiOperation(value = "특별세액공제 수정", produces = "application/json")
    @RequestMapping(method = RequestMethod.PUT, value = "yw710.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> putYW710(@RequestBody YW710Request body, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        response.success = false;

        try {
            System.out.println(":::::::::::::::::::: postYW710 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                yw710Service.putYW710(loginVO.getBizId(), loginVO.getUserId(), body);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
    
    @ApiOperation(value = "특별세액공제 의료비계산결과 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "medicalYW710.do")
    @ResponseBody
    public ResponseEntity<YW710MedicalResponse> getMedicalYW710(
            @RequestParam String 계약ID, @RequestParam String 사용자ID,  @RequestParam String 세액공제구분코드,
            HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YW710MedicalResponse response = new YW710MedicalResponse();
        response.success = false;

        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));

        try {
            System.out.println(":::::::::::::::::::: getMedicalYW710 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	yw710Service.getMedicalYW710(계약ID, 사용자ID, 세액공제구분코드, response);
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
