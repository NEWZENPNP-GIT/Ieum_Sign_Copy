package com.ezsign.feb.worker.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.worker.vo.YP040VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("yp040DAO")
public class YP040DAO extends EgovAbstractDAO{

	// 간이지급명세서_소득명세합계 조회
	public YP040VO getYP040(YP040VO vo) {
		return (YP040VO) select("yp040DAO.getYP040", vo);
	}
	
	// 간이지급명세서_소득명세합계 주(현)/종(전) 근무지 항목 조회
	@SuppressWarnings("unchecked")
	public List<YP040VO> getYP040List(YP040VO vo) {
		return (List<YP040VO>) list("yp040DAO.getYP040List", vo);
	}
	
	// 간이지급명세서_소득명세합계  입력
	public void insYP040(YP040VO vo) {
		insert("yp040DAO.insYP040", vo);
	}
	
	// 간이지급명세서_소득명세합계  삭제
	public int delYP040(YP040VO vo) {
		return delete("yp040DAO.delYP040", vo);
	}
		
}
