package com.ezsign.status.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.status.vo.StatusVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("statusDAO")
public class StatusDAO extends EgovAbstractDAO {

	public int getBizListCount(StatusVO vo) {
		return (int)selectByPk("statusDAO.getBizListCount", vo);
	}

	public int getUserListCount(StatusVO vo) {
		return (int)selectByPk("statusDAO.getUserListCount", vo);
	}
	
	public List<StatusVO> getBizList(StatusVO vo) {
		return (List<StatusVO>)list("statusDAO.getBizList", vo);
	}

	public List<StatusVO> getContractSendList(StatusVO vo) {
		return (List<StatusVO>)list("statusDAO.getContractSendList", vo);
	}
	
	public List<StatusVO> getDocumentSendList(StatusVO vo) {
		return (List<StatusVO>)list("statusDAO.getDocumentSendList", vo);
	}
	
	public List<StatusVO> getContractList(StatusVO vo) {
		return (List<StatusVO>)list("statusDAO.getContractList", vo);
	}
}
