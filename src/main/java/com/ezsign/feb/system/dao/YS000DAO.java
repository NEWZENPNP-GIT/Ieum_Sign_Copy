package com.ezsign.feb.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.system.vo.YS000VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("ys000DAO")
public class YS000DAO extends EgovAbstractDAO {
	
	// 연말정산_계약정보 (기업ID, 계약년도)
	@SuppressWarnings("unchecked")
	public List<YS000VO> getYS000List(YS000VO vo) {
		return (List<YS000VO>)list("ys000DAO.getYS000List", vo);
	}

	// 연말정산_계약정보 입력
	public void insYS000(YS000VO vo) {
		insert("ys000DAO.insYS000", vo);
	}
	
}
