package com.ezsign.feb.etc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.etc.vo.YE501VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ye501DAO")
public class YE501DAO extends EgovAbstractDAO {

	// 납세조합공제 조회
	public YE501VO getYE501(YE501VO vo) {
		return (YE501VO) select("ye501DAO.getYE501", vo);
	}

	@SuppressWarnings("unchecked")
	public List<YE501VO> getYE501List(YE501VO vo) {
		return (List<YE501VO>) list("ye501DAO.getYE501", vo);
	}

	// 납세조합공제 입력
	public void insYE501(YE501VO vo) {
		insert("ye501DAO.insYE501", vo);
	}

	// 납세조합공제 사용여부 '0'
	public int updYE501Disable(YE501VO vo) {
		return update("ye501DAO.updYE501Disable", vo);
	}

	// 납세조합공제 전체삭제
	public int allDelYE501(YE501VO vo) {
		return delete("ye501DAO.allDelYE501", vo);
	}
}
