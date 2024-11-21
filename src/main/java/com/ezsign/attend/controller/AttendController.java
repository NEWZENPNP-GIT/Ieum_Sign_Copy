package com.ezsign.attend.controller;

import com.ezsign.attend.service.AttendPlaceService;
import com.ezsign.attend.service.AttendPropService;
import com.ezsign.attend.service.AttendService;
import com.ezsign.attend.vo.*;
import com.ezsign.attend.vo.AttendSumResponse.AttendSum;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.vo.DefaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.util.TextUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@Api(tags = "AttendController", description = "근태관리")
@RequestMapping("/attend/")
public class AttendController extends BaseController {

    private static final String FORMAT = "%02d:%02d";


    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "attendService")
    private AttendService attendService;

    @Resource(name = "attendPropService")
    private AttendPropService attendPropService;

    @Resource(name = "attendPlaceService")
    private AttendPlaceService attendPlaceService;

    @ApiOperation(value = "근태조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getAttendList.do")
    @ResponseBody
    public ResponseEntity<AttendResponse> getAttendList(@ModelAttribute("AttendVO") AttendVO vo, HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        AttendResponse response = new AttendResponse();

        try {
            System.out.println(":::::::::::::::::::: getAttendList :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                int userType = Integer.parseInt(loginVO.getUserType());

                //AttendVO vo = new AttendVO();
                //vo.setBizId(loginVO.getBizId());
                if(vo.getBizId() == null){
					vo.setBizId(loginVO.getBizId());
				}
                if (userType <= 3) {
                    vo.setUserId(loginVO.getUserId());
                }

                //vo.setAttendCode(attendCode);

                if (TextUtils.isEmpty(vo.getStartDate()) || TextUtils.isEmpty(vo.getEndDate())) {
                    vo.setStartDate(DateUtil.getTimeStamp(3));
                    vo.setEndDate(DateUtil.getTimeStamp(3));
                }

                AttendPropVO attendPropVO = new AttendPropVO();
                attendPropVO.setBizId(vo.getBizId());
                attendPropVO.setSearchCompany(vo.getSearchCompany());
                response.attendProp = attendPropService.getAttendProp(attendPropVO);
                response.attends = attendService.getAttendList(vo);
                response.total = attendService.getAttendListCount(vo);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
    
    @ApiOperation(value = "근태조회", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getAttendListFlutter.do")
    @ResponseBody
    public ResponseEntity<AttendResponse> getAttendListFlutter(@ModelAttribute("AttendVO") AttendVO vo, HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        AttendResponse response = new AttendResponse();

        try {
            System.out.println(":::::::::::::::::::: getAttendList :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (vo.getUserId() == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                
                if (TextUtils.isEmpty(vo.getStartDate()) || TextUtils.isEmpty(vo.getEndDate())) {
                    vo.setStartDate(DateUtil.getTimeStamp(3));
                    vo.setEndDate(DateUtil.getTimeStamp(3));
                }

                AttendPropVO attendPropVO = new AttendPropVO();
                attendPropVO.setBizId(vo.getBizId());
                attendPropVO.setSearchCompany(vo.getSearchCompany());
                response.attendProp = attendPropService.getAttendProp(attendPropVO);
                response.attends = attendService.getAttendList(vo);
                response.total = attendService.getAttendListCount(vo);
                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "근태등록 - 출근/외출 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "insAttend.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> insAttend(
    		@ModelAttribute("AttendVO") AttendVO vo,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
            System.out.println(":::::::::::::::::::: insAttend :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	
            	if (vo.getBizId() == null || vo.getBizId().length() == 0) {
            		System.out.println("vo BizID = " + vo.getBizId());
            		System.out.println("loginVO BizID = " + loginVO.getBizId());
            		vo.setBizId(loginVO.getBizId());
            		System.out.println("vo BizID = " + vo.getBizId());
            		System.out.println("loginVO BizID = " + loginVO.getBizId());
            		
            	}
                boolean isSuccess = true;
                AttendVO check = new AttendVO();
                check.setBizId(vo.getBizId());
                check.setUserId(loginVO.getUserId());
                check.setStartDate(DateUtil.getTimeStamp(3));
                check.setEndDate(DateUtil.getTimeStamp(3));
                check.setStartPage(0);
                check.setEndPage(99999);
                List<AttendVO> result = attendService.getAttendList(check);
                if ("001".equals(vo.getAttendCode()) && result != null && result.size() > 0) {
                    response.message = "이미 출근등록을 하셨습니다.";
                    isSuccess = false;
                } else if ("002".equals(vo.getAttendCode())) {
                    if (result == null || result.size() == 0) {
                        response.message = "먼저 출근등록을 하셔야합니다.";
                        isSuccess = false;
                    } else {
                        for (AttendVO resultVo : result) {
                            if ("002".equals(resultVo.getAttendCode()) && resultVo.getDateTo() == null) {
                                response.message = "아직 외출에서 복귀하지 않았습니다.";
                                isSuccess = false;
                                break;
                            }
                        }
                    }
                }

                if (isSuccess) {
                    List<AttendPlaceVO> list = null;
                    if ("001".equals(vo.getAttendCode()) && vo.getLatitude() != 0.0 && vo.getLongitude() != 0.0) {
                    	AttendPropVO attendPropVO = new AttendPropVO();
                        attendPropVO.setBizId(vo.getBizId());
                        attendPropVO.setSearchCompany(vo.getSearchCompany());
                        AttendPropVO prop = attendPropService.getAttendProp(attendPropVO);
                        if (prop != null && "Y".equals(prop.getPlaceYn())) {
                        	AttendPlaceVO attendPlaceVO = new AttendPlaceVO();
                        	attendPlaceVO.setBizId(vo.getBizId());
                        	attendPlaceVO.setSearchCompany(vo.getSearchCompany());
                            list = attendPlaceService.getAttendPlaceList(attendPlaceVO);
                        }
                    }

                    AttendVO insVo = new AttendVO();
                    insVo.setBizId(vo.getBizId());
                    insVo.setUserId(loginVO.getUserId());
                    insVo.setWorkDate(DateUtil.getTimeStamp(3));
                    insVo.setAttendCode(vo.getAttendCode());
                    if (list != null && list.size() > 0) {
                        double distance;
                        for (AttendPlaceVO placeVO : list) {
                            if (placeVO.getLatitude() == 0.0 || placeVO.getLongitude() == 0.0) {
                                continue;
                            }
                            distance = getDistance(vo.getLatitude(), vo.getLongitude(), placeVO.getLatitude(), placeVO.getLongitude());

                            if (vo.getDistance() == 0.0 || distance < vo.getDistance()) {
                            	insVo.setPlaceId(placeVO.getPlaceId());
                            	insVo.setDistance(distance);
                            }
                        }
                    }

                    attendService.insAttend(insVo);

                    response.success = true;
                }
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
    
    @ApiOperation(value = "근태등록 - 출근/외출 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "insAttendFlutter.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> insAttendFlutter(
    		@RequestBody AttendVO vo,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
            System.out.println(":::::::::::::::::::: insAttend :::::::::::::::::::");
            
            
            if (vo.getUserId() == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
            	
            	boolean isSuccess = true;
                AttendVO check = new AttendVO();
                check.setBizId(vo.getBizId());
                check.setUserId(vo.getUserId());
                check.setStartDate(DateUtil.getTimeStamp(3));
                check.setEndDate(DateUtil.getTimeStamp(3));
                check.setStartPage(0);
                check.setEndPage(99999);
                List<AttendVO> result = attendService.getAttendList(check);
                if ("001".equals(vo.getAttendCode()) && result != null && result.size() > 0) {
                    response.message = "이미 출근등록을 하셨습니다.";
                    isSuccess = false;
                } else if ("002".equals(vo.getAttendCode())) {
                    if (result == null || result.size() == 0) {
                        response.message = "먼저 출근등록을 하셔야합니다.";
                        isSuccess = false;
                    } else {
                        for (AttendVO resultVo : result) {
                            if ("002".equals(resultVo.getAttendCode()) && resultVo.getDateTo() == null) {
                                response.message = "아직 외출에서 복귀하지 않았습니다.";
                                isSuccess = false;
                                break;
                            }
                        }
                    }
                }

                if (isSuccess) {
                    List<AttendPlaceVO> list = null;
                    if ("001".equals(vo.getAttendCode()) && vo.getLatitude() != 0.0 && vo.getLongitude() != 0.0) {
                    	AttendPropVO attendPropVO = new AttendPropVO();
                        attendPropVO.setBizId(vo.getBizId());
                        attendPropVO.setSearchCompany(vo.getBizId());
                        AttendPropVO prop = attendPropService.getAttendProp(attendPropVO);
                        if (prop != null && "Y".equals(prop.getPlaceYn())) {
                        	AttendPlaceVO attendPlaceVO = new AttendPlaceVO();
                        	attendPlaceVO.setBizId(vo.getBizId());
                        	attendPlaceVO.setSearchCompany(vo.getBizId());
                            list = attendPlaceService.getAttendPlaceList(attendPlaceVO);
                        }
                    }
                    

                    AttendVO insVo = new AttendVO();
                    insVo.setBizId(vo.getBizId());
                    insVo.setUserId(vo.getUserId());
                    insVo.setWorkDate(DateUtil.getTimeStamp(3));
                    insVo.setAttendCode(vo.getAttendCode());
                    if (list != null && list.size() > 0) {
                        double distance;
                        for (AttendPlaceVO placeVO : list) {
                            if (placeVO.getLatitude() == 0.0 || placeVO.getLongitude() == 0.0) {
                                continue;
                            }
                            
                            distance = getDistance(vo.getLatitude(), vo.getLongitude(), placeVO.getLatitude(), placeVO.getLongitude());

                            if (vo.getDistance() == 0.0 || distance < vo.getDistance()) {
                            	insVo.setPlaceId(placeVO.getPlaceId());
                            	insVo.setDistance(distance);
                            }
                        }
                    }

                    attendService.insAttend(insVo);

                    response.success = true;
                }
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
    
    @ApiOperation(value = "근태등록 - 퇴근/복귀 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "offAttendFlutter.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> offAttendFlutter(
            @RequestBody AttendVO vo,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
            System.out.println(":::::::::::::::::::: offAttend :::::::::::::::::::");
            if (vo.getUserId() == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                
                if (attendService.offAttend(vo) > 0) {
                    response.success = true;
                }
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "근태등록 - 퇴근/복귀 등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "offAttend.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> offAttend(
            @RequestParam String attendId,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
            System.out.println(":::::::::::::::::::: offAttend :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                AttendVO vo = new AttendVO();
                vo.setBizId(loginVO.getBizId());
                vo.setUserId(loginVO.getUserId());
                vo.setAttendId(attendId);

                if (attendService.offAttend(vo) > 0) {
                    response.success = true;
                }
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "근태등록 - 승인/마감 처리", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "updAttend.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> updAttend(
    		@ModelAttribute("AttendVO") AttendVO vo,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
            System.out.println(":::::::::::::::::::: updAttend :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                //AttendVO vo = new AttendVO();
                //vo.setBizId(loginVO.getBizId());
            	if(vo.getBizId() == null){
					vo.setBizId(loginVO.getBizId());
				}
                vo.setUserId(loginVO.getUserId());
                //vo.setAttendIds(attendIds);
                //vo.setWorkDate(workDate);
                //vo.setSignType(signType);
                //vo.setCloseType(closeType);

                if (attendService.updAttend(vo) > 0) {
                    response.success = true;
                }
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "출근부", produces = "application/json")
    @RequestMapping(method = RequestMethod.GET, value = "getAttendSumList.do")
    @ResponseBody
    public ResponseEntity<AttendSumResponse> getAttendSumList(
            @ModelAttribute("AttendVO") AttendVO vo,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;
        AttendSumResponse response = new AttendSumResponse();

        try {
            System.out.println(":::::::::::::::::::: getAttendSumList :::::::::::::::::::");

            SimpleDateFormat transeDate = new SimpleDateFormat("yyyyMM");
            Date tdate = transeDate.parse(vo.getWorkMonth());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(tdate);

            int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            response.attendSums = new ArrayList<>();

            response.attendSumsTotal = new AttendSumResponse.AttendSum();
            response.attendSumsTotal.workMinutes = new int[dayOfMonth];
            for (int i = 0; i < response.attendSumsTotal.workMinutes.length; i++) {
                response.attendSumsTotal.workMinutes[i] = 0;
            }

            response.attendDay = new AttendSumResponse.AttendSum();
            response.attendDay.workMinutes = new int[dayOfMonth];
            for (int i = 0; i < response.attendDay.workMinutes.length; i++) {
                response.attendDay.workMinutes[i] = 0;
            }

            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                //vo.setBizId(loginVO.getBizId());
            	if(vo.getBizId() == null){
					vo.setBizId(loginVO.getBizId());
				}
                if (TextUtils.isEmpty(vo.getWorkMonth())) {
                    vo.setStartDate(DateUtil.getTimeStamp(14) + "01");
                    vo.setEndDate(DateUtil.getTimeStamp(14) + String.valueOf(dayOfMonth));
                } else {
                    vo.setStartDate(vo.getWorkMonth() + "01");
                    vo.setEndDate(vo.getWorkMonth() + String.valueOf(dayOfMonth));
                }

                List<AttendVO> result = attendService.getAttendSumList(vo);
                AttendSumResponse.AttendSum sum = null;

                if (result != null && result.size() > 0) {
                    String empNo = null;
                    for (AttendVO attendVO : result) {
                        if (!attendVO.getEmpNo().equals(empNo)) {
                            empNo = attendVO.getEmpNo();

                            sum = new AttendSumResponse.AttendSum();
                            sum.empNo = empNo;
                            sum.empName = attendVO.getEmpName();
                            sum.positionName = attendVO.getPositionName();
                            sum.workMinutes = new int[dayOfMonth];
                            response.attendSums.add(sum);
                        }

                        sum.workMinutes[Integer.parseInt(attendVO.getWorkDate()) - 1] = attendVO.getWorkMinute();
                        sum.workMinuteSum += attendVO.getWorkMinute();

                        response.attendSumsTotal.workMinutes[Integer.parseInt(attendVO.getWorkDate()) - 1] += attendVO.getWorkMinute();
                        response.attendSumsTotal.workMinuteSum += attendVO.getWorkMinute();
                    }
                }

                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @RequestMapping(method = RequestMethod.GET, value = "getAttendSumExcelList.do")
    @ResponseBody
    public void getAttendSumExcelList(@ModelAttribute("AttendVO") AttendVO vo, HttpServletRequest request, HttpServletResponse response) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        // 워크북 생성
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 워크시트 생성
        HSSFSheet sheet = workbook.createSheet(vo.getWorkMonth() + "_출근부");
        // 행 생성
        HSSFRow row = sheet.createRow(0);
        // 쎌 생성
        HSSFCell cell;

        // 테이블 헤더용 스타일
        CellStyle headStyle = workbook.createCellStyle();
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);

        // 데이터는 가운데 정렬합니다.
        headStyle.setAlignment(HorizontalAlignment.CENTER);

        try {
            System.out.println(":::::::::::::::::::: getAttendSumExcelList :::::::::::::::::::");

            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                AttendSumResponse res = new AttendSumResponse();
                SimpleDateFormat transeDate = new SimpleDateFormat("yyyyMM");
                Date tdate = transeDate.parse(vo.getWorkMonth());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(tdate);

                int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                int[] workMinutes = new int[dayOfMonth];

                // 헤더 정보 구성
                cell = row.createCell(0);
                cell.setCellValue("사번");

                cell = row.createCell(1);
                cell.setCellValue("이름");

                cell = row.createCell(2);
                cell.setCellValue("직급");

                for (int i = 0; i < workMinutes.length; i++) {
                    cell = row.createCell(i + 3);
                    cell.setCellValue(i + 1 + "일");
                }

                cell = row.createCell(workMinutes.length + 3);
                cell.setCellValue("합계");

                //AttendVO vo = new AttendVO();
                //vo.setBizId(loginVO.getBizId());
                if(vo.getBizId() == null){
					vo.setBizId(loginVO.getBizId());
				}
                if (TextUtils.isEmpty(vo.getWorkMonth())) {
                    vo.setStartDate(DateUtil.getTimeStamp(14) + "01");
                    vo.setEndDate(DateUtil.getTimeStamp(14) + String.valueOf(dayOfMonth));
                } else {
                    vo.setStartDate(vo.getWorkMonth() + "01");
                    vo.setEndDate(vo.getWorkMonth() + String.valueOf(dayOfMonth));
                }

                List<AttendVO> result = attendService.getAttendSumList(vo);
                AttendSumResponse.AttendSum sum = null;
                AttendSumResponse.AttendSum sumTotal = null;
                List<AttendSum> attendSums = null;
                if (result != null && result.size() > 0) {

                    attendSums = new ArrayList<>();
                    sumTotal = new AttendSumResponse.AttendSum();
                    sumTotal.workMinutes = new int[dayOfMonth];

                    String empNo = null;
                    for (AttendVO attendVO : result) {
                        if (!attendVO.getEmpNo().equals(empNo)) {
                            empNo = attendVO.getEmpNo();

                            sum = new AttendSumResponse.AttendSum();
                            sum.empNo = empNo;
                            sum.empName = attendVO.getEmpName();
                            sum.positionName = attendVO.getPositionName();
                            sum.workMinutes = new int[dayOfMonth];
                            attendSums.add(sum);
                        }

                        sum.workMinutes[Integer.parseInt(attendVO.getWorkDate()) - 1] = attendVO.getWorkMinute();
                        sum.workMinuteSum += attendVO.getWorkMinute();

                        sumTotal.workMinutes[Integer.parseInt(attendVO.getWorkDate()) - 1] += attendVO.getWorkMinute();
                        sumTotal.workMinuteSum += attendVO.getWorkMinute();
                    }

                    for (int i = 0; i < attendSums.size(); i++) {
                        AttendSum attendSum = attendSums.get(i);
                        row = sheet.createRow(i + 1);
                        cell = row.createCell(0);
                        cell.setCellValue(attendSum.empNo);
                        cell = row.createCell(1);
                        cell.setCellValue(attendSum.empName);
                        cell = row.createCell(2);
                        cell.setCellValue(attendSum.positionName);
                        for (int j = 0; j < attendSum.workMinutes.length; j++) {
                            cell = row.createCell(j + 3);
                            if (attendSum.workMinutes[j] == 0) {
                                cell.setCellValue("00:00");
                            } else {
                                //cell.setCellValue(String.format("%.1f", Float.valueOf(attendSum.workMinutes[j])/60));
                                cell.setCellValue(parseMinute(attendSum.workMinutes[j]));
                            }

                        }
                        cell = row.createCell(attendSum.workMinutes.length + 3);
                        //cell.setCellValue(String.format("%.1f", Float.valueOf(attendSum.workMinuteSum)/60));
                        cell.setCellValue(parseMinute(attendSum.workMinuteSum));
                    }
                }

                if (attendSums != null) {
                    row = sheet.createRow(attendSums.size() + 1);
                } else {
                    row = sheet.createRow(1);
                }

                cell = row.createCell(0);
                cell.setCellValue("합계");
                if (sumTotal != null) {

                    for (int i = 0; i < sumTotal.workMinutes.length; i++) {
                        cell = row.createCell(i + 3);
                        //cell.setCellValue(String.format("%.1f", Float.valueOf(sumTotal.workMinutes[i])/60));
                        cell.setCellValue(parseMinute(sumTotal.workMinutes[i]));
                    }

                    cell = row.createCell(sumTotal.workMinutes.length + 3);
                    //cell.setCellValue(String.format("%.1f", Float.valueOf(sumTotal.workMinuteSum)/60));
                    cell.setCellValue(parseMinute(sumTotal.workMinuteSum));
                } else {
                    for (int i = 0; i < workMinutes.length; i++) {
                        cell = row.createCell(i + 3);
                        cell.setCellValue("00:00");
                    }

                    cell = row.createCell(workMinutes.length + 3);
                    cell.setCellValue("00:00");
                }

                // 입력된 내용 파일로 쓰기
                String fileName = URLEncoder.encode(vo.getBizName() + "_" + vo.getWorkMonth() + "_출근부.xls", "UTF-8");
                response.setContentType("ms-vnd/excel");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                workbook.write(response.getOutputStream());
                workbook.close();
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @RequestMapping(method = RequestMethod.GET, value = "getAttendExcelList.do")
    @ResponseBody
    public void getAttendExcelList(
    		@ModelAttribute("AttendVO") AttendVO vo,
            HttpServletRequest request,
            HttpServletResponse response) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        // 워크북 생성
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 워크시트 생성
        HSSFSheet sheet = workbook.createSheet(vo.getStartDate() + "_근무현황");
        // 행 생성
        HSSFRow row = sheet.createRow(0);
        // 쎌 생성
        HSSFCell cell;

        // 테이블 헤더용 스타일
        CellStyle headStyle = workbook.createCellStyle();
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);

        // 데이터는 가운데 정렬합니다.
        headStyle.setAlignment(HorizontalAlignment.CENTER);

        try {
            System.out.println(":::::::::::::::::::: getAttendExcelList :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                int userType = Integer.parseInt(loginVO.getUserType());

                //AttendVO vo = new AttendVO();
                //vo.setBizId(loginVO.getBizId());
                if(vo.getBizId() == null){
					vo.setBizId(loginVO.getBizId());
				}
                if (userType <= 3) {
                    vo.setUserId(loginVO.getUserId());
                }

                //vo.setAttendCode(attendCode);

                if (TextUtils.isEmpty(vo.getStartDate())) {
                    vo.setStartDate(DateUtil.getTimeStamp(3));
                    vo.setEndDate(DateUtil.getTimeStamp(3));
                } else {
                    vo.setStartDate(vo.getStartDate());
                    vo.setEndDate(vo.getStartDate());
                }

                //vo.setSearchKey(searchKey);
                //vo.setSearchValue(searchValue);
                //vo.setStartPage(startPage);
                //vo.setEndPage(endPage);

                AttendPropVO attendPropVO = new AttendPropVO();
                attendPropVO.setBizId(vo.getBizId());
                attendPropVO.setSearchCompany(vo.getSearchCompany());
                AttendPropVO prop = attendPropService.getAttendProp(attendPropVO);
                boolean isDetail = false;
                int addColumnIdx = 0;
                if (prop != null && "Y".equals(prop.getDetailYn())) {
                    isDetail = true;
                    addColumnIdx = 1;
                }
                List<AttendVO> attends = attendService.getAttendList(vo);

                // 헤더 정보 구성
                cell = row.createCell(0);
                cell.setCellValue("성명");
                cell = row.createCell(1);
                cell.setCellValue("사번");
                cell = row.createCell(2);
                cell.setCellValue("부서명");
                cell = row.createCell(3);
                cell.setCellValue("근무장소");

                if (isDetail) {
                    cell = row.createCell(4);
                    cell.setCellValue("주소");
                }

                cell = row.createCell(4 + addColumnIdx);
                cell.setCellValue("출근시간");
                cell = row.createCell(5 + addColumnIdx);
                cell.setCellValue("퇴근시간");
                cell = row.createCell(6 + addColumnIdx);
                cell.setCellValue("근무시간");
                cell = row.createCell(7 + addColumnIdx);
                cell.setCellValue("승인");

                for (int i = 0; i < attends.size(); i++) {
                    AttendVO attendVO = attends.get(i);
                    row = sheet.createRow(i + 1);
                    cell = row.createCell(0);
                    cell.setCellValue(attendVO.getEmpName());
                    cell = row.createCell(1);
                    cell.setCellValue(attendVO.getEmpNo());
                    cell = row.createCell(2);
                    cell.setCellValue(attendVO.getDeptName());
                    cell = row.createCell(3);
                    cell.setCellValue(attendVO.getPlaceName());

                    if (isDetail) {
                        cell = row.createCell(4);
                        cell.setCellValue(attendVO.getPlaceAddr());
                    }

                    cell = row.createCell(4 + addColumnIdx);
                    if (!TextUtils.isEmpty(attendVO.getDateFrom())) {
                        cell.setCellValue(DateUtil.getDateType(0, attendVO.getDateFrom()));
                    } else {
                        cell.setCellValue("");
                    }
                    cell = row.createCell(5 + addColumnIdx);
                    if (!TextUtils.isEmpty(attendVO.getDateTo())) {
                        cell.setCellValue(DateUtil.getDateType(0, attendVO.getDateTo()));
                    } else {
                        cell.setCellValue("");
                    }
                    cell = row.createCell(6 + addColumnIdx);
                    cell.setCellValue(parseMinute(attendVO.getWorkMinute()));
                    cell = row.createCell(7 + addColumnIdx);
                    if ("1".equals(attendVO.getSignType())) {
                        cell.setCellValue("승인");
                    } else {
                        cell.setCellValue("미승인");
                    }
                }

                // 입력된 내용 파일로 쓰기
                String fileName = URLEncoder.encode(vo.getStartDate() + "_근무현황.xls", "UTF-8");
                response.setContentType("ms-vnd/excel");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                workbook.write(response.getOutputStream());
                workbook.close();
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        } finally {
            try {
                workbook.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;  // mile

        return (dist * 1.609344);  // kilometer
    }


    public static String parseMinute(int minute) {
        long milliseconds = minute * 60000;
        return String.format(FORMAT,
                TimeUnit.MILLISECONDS.toHours(milliseconds),
                TimeUnit.MILLISECONDS.toMinutes(milliseconds) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(milliseconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }
}
