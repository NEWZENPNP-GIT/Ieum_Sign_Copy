package com.ezsign.feb.worker.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.worker.vo.YE001VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ye001DAO")
public class YE001DAO extends EgovAbstractDAO {

	// 연말정산_근로자부양가족 조회
	@SuppressWarnings("unchecked")
	public List<YE001VO> getYE001List(YE001VO vo) {
		return (List<YE001VO>) list("ye001DAO.getYE001List", vo);
	}

	// 연말정산_근로자부양가족 갯수
	public int getYE001ListCount(YE001VO vo) {
		return (Integer) getSqlMapClientTemplate().queryForObject("ye001DAO.getYE001ListCount", vo);
	}

	// 연말정산_근로자부양가족 입력
	public void insYE001(YE001VO vo) {
		insert("ye001DAO.insYE001", vo);
	}

	// 연말정산_근로자부양가족 수정
	public int updYE001(YE001VO vo) {
		return update("ye001DAO.updYE001", vo);
	}

	// 연말정산_근로자부양가족 삭제
	public int delYE001(YE001VO vo) {
		return delete("ye001DAO.delYE001", vo);
	}

	// 연말정산_근로자부양가족 조회
	@SuppressWarnings("unchecked")
	public List<YE001VO> getYE001SubList(YE001VO vo) {
		return (List<YE001VO>) list("ye001DAO.getYE001SubList", vo);
	}

	// 연말정산_근로자부양가족 ID 조회
	public YE001VO getYE001ID(YE001VO vo) {
		return (YE001VO) select("ye001DAO.getYE001ID", vo);
	}
}
