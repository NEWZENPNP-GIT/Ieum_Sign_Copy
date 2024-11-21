package com.ezsign.feb.master.dao;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.master.vo.YE004VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ye004DAO")
public class YE004DAO extends EgovAbstractDAO {

	// pdf upload 결과 입력
	public void insUpdYE004(YE004VO vo) {
		insert("ye004DAO.insUpdYE004", vo);
	}
}
