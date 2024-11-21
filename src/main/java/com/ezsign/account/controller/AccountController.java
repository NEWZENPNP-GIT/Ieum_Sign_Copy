package com.ezsign.account.controller;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.account.service.AccountService;
import com.ezsign.account.vo.AccountVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.emp.service.EmpService;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/account/")
public class AccountController extends BaseController {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "logService")
    private LogService logService;

    @Resource(name = "accountService")
    private AccountService accountService;

    @Resource(name = "empService")
    private EmpService empService;

    @RequestMapping(method = RequestMethod.POST, value = "leaveAccount.do")
    @ResponseBody
    public ResponseEntity<JSONObject> leaveAccountCtrl(@ModelAttribute("AccountVO") AccountVO vo, HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;
        boolean success = false;
        int total = 0;

        try {
            logger.info(":::::::::::::::::::: leaveAccount :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                logger.info("bizId=>" + loginVO.getBizId());
                vo.setBizId(loginVO.getBizId());

                total = accountService.leaveAccount(vo);
                success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        jsonObject.put("total", total);
        jsonObject.put("success", success);
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }

    @RequestMapping(method = RequestMethod.POST, value = "leaveUser.do")
    @ResponseBody
    public ResponseEntity<JSONObject> leaveUserCtrl(@ModelAttribute("AccountVO") AccountVO vo, HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;
        boolean success = false;
        int total = 0;

        try {
            logger.info(":::::::::::::::::::: leaveUser :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                logger.info("bizId, userId=>" + vo.getBizId() + "," + vo.getUserId());

                total = accountService.leaveUser(vo);
                success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        jsonObject.put("total", total);
        jsonObject.put("success", success);
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }

    /* 관리자가 인사정보 선택삭제 */
    @RequestMapping(method = RequestMethod.POST, value = "leaveSelectedUser.do")
    @ResponseBody
    public ResponseEntity<JSONObject> leaveSelectedUserCtrl(@ModelAttribute("AccountVO") AccountVO vo, HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

        HttpStatus status = HttpStatus.OK;
        boolean success = false;
        int total = 0;

        try {
            logger.info(":::::::::::::::::::: leaveSelectedUser :::::::::::::::::::");
            SessionVO loginVO = SessionUtil.getSession(request);
            if (loginVO == null) {
                status = HttpStatus.UNAUTHORIZED;
            } else {
                List<AccountVO> accountList = new ArrayList<>();

                String multiData = vo.getMultiData();
                if (StringUtil.isNotNull(multiData)) {
                    String[] params = StringUtil.split(multiData, "|");
                    for (int i = 0; i < params.length - 1; i++) {
                        AccountVO accountVO = new AccountVO();
                        accountVO.setBizId(loginVO.getBizId());
                        accountVO.setUserId(params[i]);

                        //근로자 정보 조회
                        EmpVO paramEmpVO = new EmpVO();
                        paramEmpVO.setBizId(loginVO.getBizId());
                        paramEmpVO.setUserId(params[i]);
                        EmpVO rstEmpVO = empService.selectEmpInfo(paramEmpVO);
                        if (rstEmpVO != null) {
                            accountVO.setLoginId(rstEmpVO.getLoginId());
                            accountVO.setPhoneNum(rstEmpVO.getPhoneNum());
                        }

                        accountList.add(accountVO);
                    }
                }
				/*logger.info("bizId, userId=>"+loginVO.getBizId()+","+vo.getUserId());
				vo.setBizId(loginVO.getBizId());
				
				total = accountService.leaveUser(vo);
				success = true;*/
                total = accountService.leaveSelectedUser(accountList);
                success = true;
            }
        } catch (Exception ex) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            logService.error(ex.getMessage(), new Throwable(ex));
            throw ex;
        }
        jsonObject.put("total", total);
        jsonObject.put("success", success);
        return new ResponseEntity<>(jsonObject, responseHeaders, status);
    }
}
