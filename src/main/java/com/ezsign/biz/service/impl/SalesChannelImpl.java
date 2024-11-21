package com.ezsign.biz.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ezsign.biz.dao.SalesChannelDAO;
import com.ezsign.biz.service.SalesChannelService;
import com.ezsign.biz.vo.SalesChannelVO;
import com.ezsign.dept.vo.DeptVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("salesChannelService")
public class SalesChannelImpl extends AbstractServiceImpl implements SalesChannelService {

	@Resource(name="salesChannelDAO")
	private SalesChannelDAO saleChannelDAO;
	
	@Override
	public List<SalesChannelVO> getSalesChannelList(SalesChannelVO vo) throws Exception {
		List<SalesChannelVO> list = null;
		
		list = saleChannelDAO.getSalesChannelList(vo);
		
		return list;
	}

	@Override
	public int getSalesChannelListCount(SalesChannelVO vo) throws Exception {
		return saleChannelDAO.getSalesChannelListCount(vo);
	}

	@Override
	public int saveSalesChannel(List<SalesChannelVO> listVo) throws Exception {
		int result = 0;
		
		for(int m=0;m<listVo.size();m++) {
			SalesChannelVO salesChannelVO = listVo.get(m);
			
			String masterDbMode = salesChannelVO.getDbMode();
			
			// 그룹관리 db모드에 따라 데이터 처리
			if(masterDbMode.equals("I")) {
				saleChannelDAO.insSalesChannel(salesChannelVO);
			} else if(masterDbMode.equals("U")) {				
				saleChannelDAO.updSalesChannel(salesChannelVO);
			} else if(masterDbMode.equals("D")) {
				saleChannelDAO.delSalesChannel(salesChannelVO);
			}
		}
		
		result = listVo.size();
		
		return result;
	}

}
