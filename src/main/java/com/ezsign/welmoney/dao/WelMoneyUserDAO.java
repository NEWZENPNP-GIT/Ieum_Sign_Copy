package com.ezsign.welmoney.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.contract.vo.ContractPersonVO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.user.vo.UserCertVO;
import com.ezsign.user.vo.UserGrpVO;
import com.ezsign.user.vo.UserJoinCertVO;
import com.ezsign.user.vo.UserLogVO;
import com.ezsign.user.vo.UserVO;
import com.ezsign.welmoney.vo.WelMoneyUserVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("welMoneyUserDAO")
public class WelMoneyUserDAO extends EgovAbstractDAO {
	
	public WelMoneyUserVO loginProcess(WelMoneyUserVO vo) {
		return (WelMoneyUserVO)select("welMoneyUserDAO.login", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<WelMoneyUserVO> getMemberList(WelMoneyUserVO vo) {
		return (List<WelMoneyUserVO>)list("welMoneyUserDAO.getMemberList", vo);
	}
	
	public int getMemberListCount(WelMoneyUserVO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("welMoneyUserDAO.getMemberListCount", vo);
	}
	
	public WelMoneyUserVO getMember(WelMoneyUserVO vo) {
		return (WelMoneyUserVO)select("welMoneyUserDAO.getMember", vo);
	}
	
	public void insMember(WelMoneyUserVO vo) {
		insert("welMoneyUserDAO.insMember", vo);
	}
	
	public int updMember(WelMoneyUserVO vo) {
		return update("welMoneyUserDAO.updMember", vo);
	}
	
	public void delMember(WelMoneyUserVO vo) {
		delete("welMoneyUserDAO.delMember", vo);
	}
	
	public WelMoneyUserVO welmoneyLoginCheck(WelMoneyUserVO vo) {
		return (WelMoneyUserVO)select("welMoneyUserDAO.welmoneyLoginCheck", vo);
	}
}
