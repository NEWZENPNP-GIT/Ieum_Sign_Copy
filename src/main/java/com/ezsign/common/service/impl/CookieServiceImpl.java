package com.ezsign.common.service.impl;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.common.service.CookieService;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.vo.CookieVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.user.dao.UserDAO;
import com.ezsign.user.vo.UserLogVO;
import com.ezsign.user.vo.UserVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("cookieService")
public class CookieServiceImpl extends AbstractServiceImpl implements CookieService {
	
    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "bizDAO")
    private BizDAO bizDAO;

    @Resource(name = "empDAO")
    private EmpDAO empDAO;

    @Resource(name = "userDAO")
    private UserDAO userDAO;

    public static void main(String[] args) {

    }
    
    @Override
    public SessionVO getCookieSession(HttpServletRequest request, CookieVO vo) throws Exception {
    	
    	if(vo == null){
    		return null;
    	}
    	
        SessionVO loginVO = new SessionVO();

        // 기업정보조회
        BizVO chkBizVO = new BizVO();
        chkBizVO.setBusinessNo(vo.getBusnNo());
        chkBizVO.setStartPage(0);
        chkBizVO.setEndPage(10);
        List<BizVO> bizList = bizDAO.getBizList(chkBizVO);
        if (bizList.size() == 0) {
            return null;
        }
        BizVO bizVO = bizList.get(0);
        
        // 사용자정보조회
        UserVO chkUserVO = new UserVO();
        chkUserVO.setUserId(vo.getUserId());
        List<UserVO> userList = userDAO.getUserList(chkUserVO);
        if(userList.size() == 0) {
        	return null;
        }
        UserVO userVO = userList.get(0);
        
        // 사원정보조회
        EmpVO chkEmpVO = new EmpVO();
        /*chkEmpVO.setBizId(bizVO.getBizId());*/
        chkEmpVO.setLoginId(vo.getUserId());
        chkEmpVO.setStartPage(0);
        chkEmpVO.setEndPage(1);
        List<EmpVO> empList = empDAO.getEmpList(chkEmpVO);
        if(empList.size() == 0) {
        	return null;
        }
        EmpVO empVO = empList.get(0);
        
        
        loginVO.setBizId(bizVO.getBizId());
        loginVO.setBizName(bizVO.getBizName());
        loginVO.setLoginId(vo.getUserId());
        loginVO.setUserId(empVO.getUserId());
        loginVO.setUserName(userVO.getUserName());
        loginVO.setUserType(userVO.getUserType());
        loginVO.setEmpNo(empVO.getEmpNo());
        loginVO.setPhoneNum(userVO.getPhoneNum());
        loginVO.setEmail(userVO.getEMail());
        
        UserLogVO logVO = new UserLogVO();
        logVO.setUserId(vo.getUserId());
        logVO.setLogType("C01");
        userDAO.insUserLog(logVO);
        
        //SessionUtil.Login(request, loginVO);

        return loginVO;
    }
    
}
