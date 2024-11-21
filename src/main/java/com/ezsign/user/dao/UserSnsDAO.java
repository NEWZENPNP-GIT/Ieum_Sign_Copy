package com.ezsign.user.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.user.vo.UserCertVO;
import com.ezsign.user.vo.UserGrpVO;
import com.ezsign.user.vo.UserJoinCertVO;
import com.ezsign.user.vo.UserLogVO;
import com.ezsign.user.vo.UserSnsVO;
import com.ezsign.user.vo.UserVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("userSnsDAO")
public class UserSnsDAO extends EgovAbstractDAO {

	@SuppressWarnings("unchecked")
	public UserSnsVO getUserSns(UserSnsVO vo) {
		return (UserSnsVO) selectByPk("userDAO.getUserSns", vo);
	}

	public void insUserSns(UserSnsVO vo) {
		insert("userDAO.insUserSns", vo);
	}

	public void updUserSnsConnTime(UserSnsVO vo) {
		update("userDAO.updUserSnsConnTime", vo);
	}

	public void delUserSns(UserSnsVO vo) {
		update("userDAO.delUserSns", vo);
	}
	
	public void updUserSnsToken(UserSnsVO vo) {
		update("userDAO.updUserSnsToken", vo);
	}

	@SuppressWarnings("unchecked")
	public List<UserSnsVO> getUserSnsList(UserSnsVO vo) {
		return (List<UserSnsVO>) list("userDAO.getUserSnsList", vo);
	}
}
