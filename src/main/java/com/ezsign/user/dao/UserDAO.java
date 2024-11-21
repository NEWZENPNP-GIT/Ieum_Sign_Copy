package com.ezsign.user.dao;

import java.util.List;

import com.ezsign.user.vo.*;
import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("userDAO")
public class UserDAO extends EgovAbstractDAO {
	
	public UserVO loginProcess(UserVO vo) {
		return (UserVO)select("userDAO.login", vo);
	}
	
	public void insUser(UserVO vo) {
		insert("userDAO.insUser", vo);
	}

	public int updUser(UserVO vo) {
		return update("userDAO.updUser", vo);
	}
	
	public void delUser(UserVO vo) {
		delete("userDAO.delUser", vo);
	}
	
	public void insUserLog(UserLogVO vo) {
		insert("userDAO.insUserLog", vo);
	}

	public void insUserCert(UserCertVO vo) {
		insert("userDAO.insUserCert", vo);
	}

	public void insUserMok(UserMokVO vo) {
		insert("userDAO.insUserMok", vo);
	}

	public void insUserJoinCert(UserJoinCertVO vo) {
		insert("userDAO.insUserJoinCert", vo);
	}

	public int updUserJoinCert(UserJoinCertVO vo) {
		return update("userDAO.updUserJoinCert", vo);
	}

	public void insUserGrp(UserGrpVO vo) {
		insert("userDAO.insUserGrp", vo);
	}

	public void delUserGrp(UserGrpVO vo) {
		delete("userDAO.delUserGrp", vo);
	}
	
	public UserJoinCertVO getUserJoinCert(UserJoinCertVO vo) {
		return (UserJoinCertVO)select("userDAO.getUserJoinCert", vo);
	}

	@SuppressWarnings("unchecked")
	public List<UserGrpVO> getUserGrp(UserGrpVO vo) {
		return (List<UserGrpVO>)list("userDAO.getUserGrp", vo);
	}

	@SuppressWarnings("unchecked")
	public List<UserGrpVO> getUserGrpList(UserGrpVO vo) {
		return (List<UserGrpVO>)list("userDAO.getUserGrpList", vo);
	}
	
	public int updUserPwd(UserVO vo) {
		return update("userDAO.updUserPwd", vo);
	}

	public int resetUserPwd(UserVO vo) {
		return update("userDAO.resetUserPwd", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<UserVO> getUserList(UserVO vo) {
		return (List<UserVO>)list("userDAO.getUserList", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<UserVO> findUser(UserVO vo) {
		return (List<UserVO>)list("userDAO.findUser", vo);
	}
	
	public int updLoginId(UserVO vo) {
		return update("userDAO.updLoginId", vo);
	}
	// 전자계약이관시 추가
	public int getCntUserCert(UserCertVO vo){
		return (int)select("userDAO.cntUserCert", vo);
	}
	
	
}
