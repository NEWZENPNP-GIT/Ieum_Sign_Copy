package com.ezsign.biz.service;

import java.util.List;

import com.ezsign.biz.vo.SalesChannelVO;



public interface SalesChannelService {

	public List<SalesChannelVO> getSalesChannelList(SalesChannelVO vo)  throws Exception;

	public int getSalesChannelListCount(SalesChannelVO vo)  throws Exception;
	
	public int saveSalesChannel(List<SalesChannelVO> listVo) throws Exception;
}
