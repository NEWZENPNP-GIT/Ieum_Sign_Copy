package com.ezsign.biz.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.biz.vo.SalesChannelVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("salesChannelDAO")
public class SalesChannelDAO extends EgovAbstractDAO {

	@SuppressWarnings("unchecked")
	public List<SalesChannelVO> getSalesChannelList(SalesChannelVO vo) {
		return (List<SalesChannelVO>)list("salesChannelDAO.getSalesChannelList", vo);
	}
	
	public int getSalesChannelListCount(SalesChannelVO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("salesChannelDAO.getBizSalesChannelListCount", vo);
	}
	
	public void insSalesChannel(SalesChannelVO vo) {
		insert("salesChannelDAO.insSalesChannel", vo);
	}
	
	public int updSalesChannel(SalesChannelVO vo) {
		return update("salesChannelDAO.updSalesChannel", vo);
	}

	public int delSalesChannel(SalesChannelVO vo) {
		return delete("salesChannelDAO.delSalesChannel", vo);
	}
}
