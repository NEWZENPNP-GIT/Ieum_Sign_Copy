package com.ezsign.attend.controller;

import com.ezsign.attend.service.AttendPlaceService;
import com.ezsign.attend.service.AttendPropService;
import com.ezsign.attend.vo.AttendPlaceVO;
import com.ezsign.attend.vo.AttendPropResponse;
import com.ezsign.attend.vo.AttendPropVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.vo.DefaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Api(tags = "AttendPropController", description = "근태 설정 관리")
@RequestMapping("/attend/")
public class AttendPropController extends BaseController {

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "attendPropService")
    private AttendPropService attendPropService;

    @Resource(name = "attendPlaceService")
    private AttendPlaceService attendPlaceService;

    @ApiOperation(value = "설정조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getAttendProp.do")
    @ResponseBody
    public ResponseEntity<AttendPropResponse> getAttendProp(@ModelAttribute AttendPropVO vo, HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        AttendPropResponse response = new AttendPropResponse();

        try {
            System.out.println(":::::::::::::::::::: getAttendProp :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                response.attendProp = attendPropService.getAttendProp(vo);
                AttendPlaceVO attendPlaceVO = new AttendPlaceVO();
                attendPlaceVO.setBizId(vo.getBizId());
                if(vo.getBizId() == null){
                	attendPlaceVO.setBizId(loginVO.getBizId());
				}
                attendPlaceVO.setSearchCompany(vo.getSearchCompany());
                response.attendPlaces = attendPlaceService.getAttendPlaceList(attendPlaceVO);

                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "설정등록/수정", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "insAttendProp.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> insAttendProp(@ModelAttribute AttendPropVO vo, HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
            System.out.println(":::::::::::::::::::: insAttendProp :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                //vo.setBizId(loginVO.getBizId());
            	if(vo.getBizId() == null){
					vo.setBizId(loginVO.getBizId());
				}
                attendPropService.insAttendProp(vo);

                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
}
