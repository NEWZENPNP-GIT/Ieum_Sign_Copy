package com.ezsign.window.controller;

import com.ezsign.common.controller.BaseController;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.other.vo.YE201VO;
import com.ezsign.feb.other.vo.YE203VO;
import com.ezsign.feb.other.vo.YE204VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.service.YW670Service;
import com.ezsign.window.vo.DefaultResponse;
import com.ezsign.window.vo.WorkMonth;
import com.ezsign.window.vo.YW670Request;
import com.ezsign.window.vo.YW670Response;
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
@Api(tags = "YW670Controller", description = "그밖에소득공제")
@RequestMapping("/febworker/")
public class YW670Controller extends BaseController {

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "febMasterService")
    private FebMasterService febMasterService;

    @Resource(name = "yw670Service")
    private YW670Service yw670Service;

    @ApiOperation(value = "그밖에소득공제 조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "yw670.do")
    @ResponseBody
    public ResponseEntity<YW670Response> getYW670(String 계약ID, String 사용자ID, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        YW670Response response = new YW670Response();
        response.success = false;

        YE000VO work = new YE000VO();
        work.set계약ID(계약ID);
        work.set사용자ID(사용자ID);
        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));

        response.소기업소상공인공제부금 = new YE201VO();
        response.투자조합출자 = new ArrayList<>();
        response.우리사주조합출연금 = new YE203VO();
        response.우리사주조합출연금벤처 = new YE203VO();
        response.고용유지중소기업근로자 = new YE204VO();

        response.소기업소상공인공제부금_정정사유 = new YE900VO();
        response.투자조합출자_정정사유 = new YE900VO();
        response.우리사주조합출연금_정정사유 = new YE900VO();
        response.고용유지중소기업근로자_정정사유 = new YE900VO();

        try {
            System.out.println(":::::::::::::::::::: getYW670 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                yw670Service.getYW670(계약ID, 사용자ID, response);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "그밖에소득공제 수정", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "yw670.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> postYW670(@RequestBody YW670Request body, HttpServletRequest request) throws Exception {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        response.success = false;

        try {
            System.out.println(":::::::::::::::::::: postYW670 :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);

            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	int userType = Integer.parseInt(loginVO.getUserType());
            	
                yw670Service.saveYW670(loginVO.getUserId(), body, userType);
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
