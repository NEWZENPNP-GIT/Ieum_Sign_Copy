package com.ezsign.feb.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.system.vo.YS010VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("ys010DAO")
public class YS010DAO extends EgovAbstractDAO {
	
	// 연말정산_기초설정 조회
	@SuppressWarnings("unchecked")
	public List<YS010VO> getYS010List(YS010VO vo) {
		return (List<YS010VO>)list("ys010DAO.getYS010List", vo);
	}

	// 연말정산_기초설정 입력
	public void insYS010(YS010VO vo) {
		insert("ys010DAO.insYS010", vo);
	}

	// 연말정산_기초설정 수정
	public int updYS010(YS010VO vo) {
		return update("ys010DAO.updYS010", vo);
	}
	
	// 연말정산_근로자 접속기간 체크
	public int getWorkerDateCheck(YS010VO vo) {
		return (Integer) getSqlMapClientTemplate().queryForObject("ys010DAO.getWorkerDateCheck", vo);
	}
}
