package com.ezsign.point.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.point.vo.DistPointVO;
import com.ezsign.point.vo.PointLogVO;
import com.ezsign.point.vo.PointVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("pointDAO")
public class PointDAO extends EgovAbstractDAO {

	public void insPoint(PointVO vo) {
		insert("pointDAO.insPoint", vo);
	}

	public void delPoint(PointVO vo) {
		delete("pointDAO.delPoint", vo);
	}
	public int updPoint(PointVO vo) {
		return update("pointDAO.updPoint", vo);
	}

	public int balancePoint(PointVO vo) {
		return update("pointDAO.balancePoint", vo);
	}
	
	public PointVO getPoint(PointVO vo) {
		return (PointVO)selectByPk("pointDAO.getPoint", vo);
	}
	
	public void insPointLog(PointLogVO vo) {
		insert("pointDAO.insPointLog", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<PointLogVO> getPointLogList(PointLogVO vo) {
		return (List<PointLogVO>)list("pointDAO.getPointLogList", vo);
	}

	public int getPointLogListCount(PointLogVO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("pointDAO.getPointLogListCount", vo);
	}
	
	public int updPointDist(DistPointVO vo) {
		return update("pointDAO.updPointDist", vo);
	}

}
