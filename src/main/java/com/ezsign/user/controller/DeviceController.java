package com.ezsign.user.controller;

import com.ezsign.common.controller.BaseController;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.user.service.DeviceService;
import com.ezsign.user.vo.DeviceVO;
import com.ezsign.window.vo.DefaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@Api(tags = "DeviceController", description = "디바이스 관리")
@RequestMapping("/user/")
public class DeviceController extends BaseController {

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "deviceService")
    private DeviceService deviceService;

    @ApiOperation(value = "디바이스 등록/수정", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "insDevice.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> insDevice(@ModelAttribute DeviceVO vo, HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
            System.out.println(":::::::::::::::::::: insDevice :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                vo.setUserId(loginVO.getLoginId());
                vo.setLoginIp(request.getRemoteAddr());
                deviceService.insDevice(vo);

                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
    
    @ApiOperation(value = "디바이스 등록/수정", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "insDeviceFlutter.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> insDeviceFlutter(@RequestBody DeviceVO vo, HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();
        int userIdCount = 0;

        try {
            System.out.println(":::::::::::::::::::: insDevice :::::::::::::::::::");
            if (vo.getUserId() == null || vo.getUserId().isEmpty()) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                //vo.setUserId(loginVO.getLoginId());
                vo.setLoginIp(request.getRemoteAddr());
                
                DeviceVO searchUserIdDevice = new DeviceVO();
                searchUserIdDevice.setUserId(vo.getUserId());
                List<DeviceVO> deviceList = deviceService.getDeviceList(searchUserIdDevice);
                
                for(DeviceVO device : deviceList) {
                	if (device.getUserId().equals(vo.getUserId())) {
                		userIdCount++;
                	}
                }
                
                if (userIdCount > 0) {
                	deviceService.updDevice(vo);
                } else {
                	deviceService.insDevice(vo);
                }

                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "디바이스 토큰 업데이트", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "updDevice.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> updDevice(
            @RequestParam String deviceId,
            @RequestParam String authToken,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
            System.out.println(":::::::::::::::::::: updDevice :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                DeviceVO vo = new DeviceVO();
                vo.setUserId(loginVO.getLoginId());
                vo.setDeviceId(deviceId);
                vo.setAuthToken(authToken);
                deviceService.updDevice(vo);

                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
}
