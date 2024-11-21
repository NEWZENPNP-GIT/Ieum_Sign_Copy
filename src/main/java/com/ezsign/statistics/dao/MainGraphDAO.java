package com.ezsign.statistics.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.statistics.vo.MainGraphVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("mainGraphDAO")
public class MainGraphDAO extends EgovAbstractDAO {
	
	@SuppressWarnings("unchecked")
	public List<MainGraphVO> getMainGraphist(MainGraphVO vo) {
		return (List<MainGraphVO>)list("statisticsDAO.getMainGraphist", vo);
	}
	
}
