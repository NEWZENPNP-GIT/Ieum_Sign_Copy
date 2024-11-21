package com.ezsign.feb.worker.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.worker.vo.YE040VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ye040DAO")
public class YE040DAO extends EgovAbstractDAO{
	
	// 연말정산_소득명세합계 조회
	public YE040VO getYE040(YE040VO vo) {
		return (YE040VO) select("ye040DAO.getYE040", vo);
	}
	
	// 연말정산_소득명세합계 주(현)/종(전) 근무지 항목 조회
	@SuppressWarnings("unchecked")
	public List<YE040VO> getYE040List(YE040VO vo) {
		return (List<YE040VO>) list("ye040DAO.getYE040List", vo);
	}

	// 연말정산_소득명세합계  입력
	public void insYE040(YE040VO vo) {
		insert("ye040DAO.insYE040", vo);
	}
	
	// 연말정산_소득명세합계  삭제
	public int delYE040(YE040VO vo) {
		return delete("ye040DAO.delYE040", vo);
	}

	// 연말정산_소득명세합계 리스트 조회
	@SuppressWarnings("unchecked")
	public List<YE040VO> getYE040WorkerList(YE040VO vo) {
		return (List<YE040VO>) list("ye040DAO.getYE040WorkerList", vo);
	}
	
	// 연말정산_소득명세합계 리스트 조회 개수
	public int getYE040WorkerListCount(YE040VO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("ye040DAO.getYE040WorkerListCount", vo);
	}
}
