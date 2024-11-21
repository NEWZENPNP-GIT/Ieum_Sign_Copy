package com.ezsign.feb.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.system.vo.YS031VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ys031DAO")
public class YS031DAO extends EgovAbstractDAO{
	 
	// 연말정산_부서등록 조회
	@SuppressWarnings("unchecked")
	public List<YS031VO> getYS031List(YS031VO vo) {
		return  (List<YS031VO>) list("ys031DAO.getYS031List", vo);
	}
	
	// 연말정산_부서등록 갯수
	public int getYS031ListCount(YS031VO vo) {
		return (Integer) select("ys031DAO.getYS031ListCount", vo);
	}
	
	// 연말정산_부서등록 부서명 조회
	public YS031VO getYS031(YS031VO vo) {
		return (YS031VO) select("ys031DAO.getYS031", vo);
	}
	
	// 연말정산_부서등록 입력
	public String insYS031(YS031VO vo) {
		return (String) insert("ys031DAO.insYS031", vo);
	}
	
	// 연말정산_부서등록 수정
	public int updYS031(YS031VO vo) {
		return update("ys031DAO.updYS031", vo);
	}

	// 연말정산_부서등록 삭제
	public int delYS031(YS031VO vo) {
		return delete("ys031DAO.delYS031", vo);
	}
}
