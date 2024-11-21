package com.ezsign.attend.controller;

import com.ezsign.attend.service.AttendPlaceService;
import com.ezsign.attend.vo.AttendPlaceVO;
import com.ezsign.attend.vo.AttendPropVO;
import com.ezsign.attend.vo.AttendResponse;
import com.ezsign.attend.vo.ZipcodeVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.vo.DefaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Controller
@Api(tags = "AttendPlaceController", description = "출퇴근 사무실 관리")
@RequestMapping("/attend/")
public class AttendPlaceController extends BaseController {

    public static final String ZIPCODE_API_KEY = "401652aa7e13e7fcc1534740910675";
    public static final String ZIPCODE_API_URL = "https://biz.epost.go.kr/KpostPortal/openapi"; // 요청 URL

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "attendPlaceService")
    private AttendPlaceService attendPlaceService;

    @ApiOperation(value = "사무실등록", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "insAttendPlace.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> insAttendPlace(
    		@ModelAttribute AttendPlaceVO vo,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
            System.out.println(":::::::::::::::::::: insAttendPlace :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                //AttendPlaceVO vo = new AttendPlaceVO();
                //vo.setBizId(loginVO.getBizId());
            	if(vo.getBizId() == null){
					vo.setBizId(loginVO.getBizId());
				}
                //vo.setPlaceName(placeName);
                //vo.setPlaceAddr(placeAddr);
                //vo.setLatitude(latitude);
                //vo.setLongitude(longitude);
                attendPlaceService.insAttendPlace(vo);

                response.success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "사무실수정", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "updAttendPlace.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> updAttendPlace(
    		@ModelAttribute AttendPlaceVO vo,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
            System.out.println(":::::::::::::::::::: updAttendPlace :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                //AttendPlaceVO vo = new AttendPlaceVO();
                //vo.setBizId(loginVO.getBizId());
            	if(vo.getBizId() == null){
					vo.setBizId(loginVO.getBizId());
				}
                //vo.setPlaceId(placeId);
                //vo.setPlaceName(placeName);
                //vo.setPlaceAddr(placeAddr);
                //vo.setLatitude(latitude);
                //vo.setLongitude(longitude);

                if (attendPlaceService.updAttendPlace(vo) > 0) {
                    response.success = true;
                }
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "사무실삭제", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "delAttendPlace.do")
    @ResponseBody
    public ResponseEntity<DefaultResponse> delAttendPlace(
    		@ModelAttribute AttendPlaceVO vo,
            HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        DefaultResponse response = new DefaultResponse();

        try {
            System.out.println(":::::::::::::::::::: delAttendPlace :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                //AttendPlaceVO vo = new AttendPlaceVO();
                //vo.setBizId(loginVO.getBizId());
            	if(vo.getBizId() == null){
					vo.setBizId(loginVO.getBizId());
				}
                //vo.setPlaceId(placeId);

                if (attendPlaceService.delAttendPlace(vo) > 0) {
                    response.success = true;
                }
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }

    @ApiOperation(value = "주소찾기", produces = "application/json")
    @RequestMapping(method = RequestMethod.POST, value = "getZipcodeList.do")
    @ResponseBody
    public ResponseEntity<AttendResponse> zipcodeList(String addrSearchValue,
                                                      HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        AttendResponse response = new AttendResponse();
        try {
            System.out.println(":::::::::::::::::::: getZipcodeList :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                // 요청 URL 생성
                StringBuilder queryUrl = new StringBuilder();
                queryUrl.append(ZIPCODE_API_URL);
                queryUrl.append("?regkey=");
                queryUrl.append(ZIPCODE_API_KEY);
                queryUrl.append("&target=postNew"); // 서비스 종류 (지번/도로명, 새우편번호 지번/도로명)
                queryUrl.append("&query=" + URLEncoder.encode(addrSearchValue, "EUC-KR"));
                queryUrl.append("&countPerPage=50&currentPage=1");

                Document document = Jsoup.connect(queryUrl.toString()).get();

                String errorCode = document.select("error_code").text();
                String message = document.select("message").text();
                List<ZipcodeVO> list = new ArrayList<ZipcodeVO>();

                if (null == errorCode || "".equals(errorCode)) {
                    Elements elements = document.select("item");
                    ZipcodeVO zipcodeVO = null;

                    for (Element element : elements) {
                        zipcodeVO = new ZipcodeVO();
                        zipcodeVO.setPostcd(element.select("postcd").text());
                        zipcodeVO.setAddress(element.select("address").text());
                        zipcodeVO.setAddrjibun(element.select("addrjibun").text());
                        list.add(zipcodeVO);
                    }
                }

                response.zipcodes = list;
                response.success = true;
                response.total = Integer.parseInt(document.select("totalCount").text());
            }

        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            ex.printStackTrace();
        }

        return new ResponseEntity<>(response, responseHeaders, status);
    }
}
