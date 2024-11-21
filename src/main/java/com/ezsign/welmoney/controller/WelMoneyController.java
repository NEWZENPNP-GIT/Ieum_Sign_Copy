package com.ezsign.welmoney.controller;


import com.ezsign.biz.service.BizService;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.contract.vo.ContractPersonVO;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.menu.vo.MenuVO;
import com.ezsign.point.service.PointService;
import com.ezsign.point.vo.PointVO;
import com.ezsign.user.service.UserService;
import com.ezsign.user.service.UserSnsService;
import com.ezsign.user.vo.DeviceVO;
import com.ezsign.user.vo.UserSnsVO;
import com.ezsign.user.vo.UserVO;
import com.ezsign.welmoney.service.WelMoneyUserService;
import com.ezsign.welmoney.vo.WelMoneyUserVO;

import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.utl.slm.EgovHttpSessionBindingListener;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;


@Controller
@RequestMapping("/welmoney/")
public class WelMoneyController extends BaseController {

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "pointService")
    private PointService pointService;

    @Resource(name = "bizService")
    private BizService bizService;

    @Resource(name = "messageProperties")
    private Properties messageProperties;

    @Resource(name = "ys000Service")
    private YS000Service ys000Service;
    
    @Resource(name = "userSnsService")
    private UserSnsService userSnsService;
    
    @Resource(name = "welMoneyUserService")
    private WelMoneyUserService welMoneyUserService;

    @RequestMapping(method = RequestMethod.GET, value = "login.do")
    public ModelAndView login(HttpServletRequest request) {
        System.out.println(":::::::::::::::::::: welmoney login page loading :::::::::::::::::::");
        
        ModelAndView mv = new ModelAndView();
        
		mv.setViewName("welmoney/welmoneyLogin");
		
        return mv;
    }

    @RequestMapping(method = RequestMethod.POST, value = "login.do")
    @ResponseBody
    public ResponseEntity<JSONObject> loginCtrl(@ModelAttribute("WelMoneyUserVO") WelMoneyUserVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject jsonObject = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "Applacation/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        try {
            System.out.println(":::::::::::::::::::: welmoneyLogin ::::::::::::::::::: " + vo.getUserId());

            SessionVO loginVO = welMoneyUserService.loginProcess(vo);

            if (loginVO != null) {
            	SessionUtil.Login(request, loginVO);
            	jsonObject.put("success", true);
                jsonObject.put("loginId", loginVO.getLoginId());
                jsonObject.put("message", messageProperties.getProperty("login.success"));

                EgovHttpSessionBindingListener listener = new EgovHttpSessionBindingListener();
                request.getSession().setAttribute(loginVO.getLoginId(), listener);
            } else {
                jsonObject.put("success", false);
                jsonObject.put("message", messageProperties.getProperty("login.fail"));
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }

    @RequestMapping(method = RequestMethod.POST, value = "welmoneyLogout.do")
    @ResponseBody
    public ResponseEntity<JSONObject> logoutCtrl(@ModelAttribute("UserVO") UserVO vo, HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;

        try {
            System.out.println(":::::::::::::::::::: logout :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO != null) {
                userService.logOutProcess(loginVO);
                SessionUtil.Logout(request);
            }
            jsonObject.put("success", true);
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }

        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "memberPage.do")
    public String welmoneyMember(HttpServletRequest request) {
        System.out.println(":::::::::::::::::::: welmoney member page loading :::::::::::::::::::");
        
		String redirectUri = "welmoney/welmoneyMember";
		
		return redirectUri;
    }
    
    @RequestMapping(method = RequestMethod.GET , value = "getMemberList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getMemberListCtrl(@ModelAttribute("WelMoneyUserVO") WelMoneyUserVO vo, HttpServletRequest request) throws Exception
	{
		// 게약목록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			System.out.println(":::::::::::::::::::: getMemberList :::::::::::::::::::");
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				List<WelMoneyUserVO> result= welMoneyUserService.getMemberList(vo);
				int total = result.size();
				//int total = welMoneyUserService.getMemberListCount(vo);
				if(result != null && result.size() != 0){
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
				}else{
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
    
    @RequestMapping(method = RequestMethod.GET, value = "memberDetail.do")
    public String welmoneyMemberDetail(HttpServletRequest request) {
        System.out.println(":::::::::::::::::::: welmoney member detail loading :::::::::::::::::::");
        
		String redirectUri = "welmoney/welmoneyMemberDetail";
		
		return redirectUri;
    }
    
    @RequestMapping(method = RequestMethod.POST , value = "insMember.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insMemberCtrl(@ModelAttribute("WelMoneyUserVO") WelMoneyUserVO vo, HttpServletRequest request) throws Exception
	{
		// 게약목록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: insMember :::::::::::::::::::");
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
				success = welMoneyUserService.insMember(vo);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
    
    @RequestMapping(method = RequestMethod.GET , value = "getMember.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getMemberCtrl(@ModelAttribute("WelMoneyUserVO") WelMoneyUserVO vo, HttpServletRequest request) throws Exception
	{
		// 게약목록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		
		try {
			System.out.println(":::::::::::::::::::: getMemberList :::::::::::::::::::");
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				WelMoneyUserVO result= welMoneyUserService.getMember(vo);
				if(result != null){
					jsonObject.put("success", true);
					jsonObject.put("data", result);
				}else{
					jsonObject.put("success", false);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
    
    @RequestMapping(method = RequestMethod.POST , value = "updMember.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updMemberCtrl(@ModelAttribute("WelMoneyUserVO") WelMoneyUserVO vo, HttpServletRequest request) throws Exception
	{
		// 게약목록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		int success = 0;
		
		try {
			System.out.println(":::::::::::::::::::: insMember :::::::::::::::::::");
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
				success = welMoneyUserService.updMember(vo);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
    
    @RequestMapping(method = RequestMethod.POST , value = "delMember.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delMemberCtrl(@ModelAttribute("WelMoneyUserVO") WelMoneyUserVO vo, HttpServletRequest request) throws Exception
	{
		// 게약목록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: delMember :::::::::::::::::::");
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				welMoneyUserService.delMember(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
}
