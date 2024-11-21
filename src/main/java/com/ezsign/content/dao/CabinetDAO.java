package com.ezsign.content.dao;


import org.springframework.stereotype.Repository;

import com.ezsign.content.vo.CabinetVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("cabinetDAO")
public class CabinetDAO extends EgovAbstractDAO {
	
	public CabinetVO getCabinetClass(CabinetVO vo) {
		return (CabinetVO)select("cabinetDAO.getCabinetClass", vo);
	}
	
	public CabinetVO getCabinet(CabinetVO vo) {
		return (CabinetVO)select("cabinetDAO.getCabinet", vo);
	}
}
