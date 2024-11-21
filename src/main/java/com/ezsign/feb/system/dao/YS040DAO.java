package com.ezsign.feb.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS040VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("ys040DAO")
public class YS040DAO extends EgovAbstractDAO {
	
	// 연말정산_중간관리자설정 조회
	@SuppressWarnings("unchecked")
	public List<YS040VO> getYS040List(YS040VO vo) {
		return (List<YS040VO>)list("ys040DAO.getYS040List", vo);
	}
	
	// 연말정산_중간관리자설정 입력
	public void insYS040(YS040VO vo) {
		insert("ys040DAO.insYS040", vo);
	}

	// 연말정산_중간관리자설정 수정
	public int updYS040(YS040VO vo) {
		return update("ys040DAO.updYS040", vo);
	}

}
