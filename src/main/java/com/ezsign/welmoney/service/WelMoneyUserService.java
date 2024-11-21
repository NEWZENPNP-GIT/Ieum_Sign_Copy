package com.ezsign.welmoney.service;

import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.welmoney.vo.WelMoneyUserVO;

import java.util.List;

public interface WelMoneyUserService {

    SessionVO loginProcess(WelMoneyUserVO vo) throws Exception;
    
    List<WelMoneyUserVO> getMemberList(WelMoneyUserVO vo) throws Exception;
    
    public int getMemberListCount(WelMoneyUserVO vo) throws Exception;
    
    WelMoneyUserVO getMember(WelMoneyUserVO vo) throws Exception;
    
    public boolean insMember(WelMoneyUserVO vo) throws Exception;
    public int updMember(WelMoneyUserVO vo) throws Exception;
    public void delMember(WelMoneyUserVO vo) throws Exception;
}
