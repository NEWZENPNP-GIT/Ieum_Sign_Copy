package com.ezsign.feb.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.system.vo.YS041VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("ys041DAO")
public class YS041DAO extends EgovAbstractDAO {
	
	// 중간관리자 관리부서 조회
	@SuppressWarnings("unchecked")
	public List<YS041VO> getYS041List(YS041VO vo) {
		return (List<YS041VO>)list("ys041DAO.getYS041List", vo);
	}
	
	// 중간관리자 조회 갯수
	public int getYS041ListCount(YS041VO vo) {
		return (Integer) select("ys041DAO.getYS041ListCount", vo);
	}
	
	// 중간관리자 관리부서 상세조회
	public YS041VO getYS041(YS041VO vo) {
		return (YS041VO) select("ys041DAO.getYS041", vo);
	}
	
	// 중간관리자 관리부서 리스트 조회
	@SuppressWarnings("unchecked")
	public List<YS041VO> getYS041DeptList(YS041VO vo) {
		return (List<YS041VO>)list("ys041DAO.getYS041DeptList", vo);
	}
	
	// 중간관리자 관리부서 입력
	public void insYS041(YS041VO vo) {
		insert("ys041DAO.insYS041", vo);
	}

	// 중간관리자 관리부서 삭제
	public int delYS041(YS041VO vo) {
		return delete("ys041DAO.delYS041", vo);
	}

}
