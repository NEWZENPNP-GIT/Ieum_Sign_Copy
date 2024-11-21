package com.ezsign.feb.hometax.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.hometax.vo.YC200VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

/**
 * 
 * 전자신고기준표
 *
 */
@Repository("yc200DAO")
public class YC200DAO extends EgovAbstractDAO{

	
	/**
	 * 전자신고기준표 리스트
	 * 
	 * @param yc200VO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<YC200VO> getYC200List(YC200VO yc200VO) {
		return (List<YC200VO>)list("yc200DAO.getYC200List", yc200VO);  
	}
}
