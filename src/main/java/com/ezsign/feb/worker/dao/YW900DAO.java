package com.ezsign.feb.worker.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.worker.vo.YW900VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("yw900DAO")
public class YW900DAO extends EgovAbstractDAO {
	
	@SuppressWarnings("unchecked")
	public List<YW900VO> getYW900List(YW900VO vo) {
		return (List<YW900VO>)list("yw900DAO.getYW900List", vo);
	}

	
	public int getYW900ListCount(YW900VO vo) {
		return (Integer)select("yw900DAO.getYW900ListCount", vo);
	}

	@SuppressWarnings("unchecked")
    public List<YS030VO> getYS030List(YS030VO vo) {
        return (List<YS030VO>) list("ys030DAO.getYS030List", vo);
    }

	
}
