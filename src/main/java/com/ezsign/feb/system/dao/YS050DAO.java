package com.ezsign.feb.system.dao;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.system.vo.YS050VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ys050DAO")
public class YS050DAO extends EgovAbstractDAO{
	
	// 연말정산_급여항목 조회
	public YS050VO getYS050(YS050VO vo) {
		return (YS050VO) select("ys050DAO.getYS050", vo);
	}
	
	// 연말정산_급여항목 입력
	public void insYS050(YS050VO vo) {
		insert("ys050DAO.insYS050", vo);
	}
	
	// 연말정산_급여항목 수정
	public int updYS050(YS050VO vo) {
		return update("ys050DAO.updYS050", vo);
	}

	// 연말정산_급여항목 삭제
	public int delYS050(YS050VO vo) {
		return delete("ys050DAO.delYS050", vo);
	}
}
