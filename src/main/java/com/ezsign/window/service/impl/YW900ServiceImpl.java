package com.ezsign.window.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.biz.vo.BizVO;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.worker.dao.YW900DAO;
import com.ezsign.feb.worker.vo.YW900VO;
import com.ezsign.window.service.YW900Service;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;


@Service("yw900Service")
public class YW900ServiceImpl extends AbstractServiceImpl implements YW900Service {

	Logger logger = Logger.getLogger(this.getClass());


	@Resource(name="yw900DAO")
	private YW900DAO yw900DAO;
	
	
	@Override
	public List<YW900VO> getYW900List(YW900VO vo)  throws Exception {
		List<YW900VO> list = null;
		
		list = yw900DAO.getYW900List(vo);
		
		return list;
	}

	@Override
	public int getYW900ListCount(YW900VO vo)  throws Exception {
				
		return yw900DAO.getYW900ListCount(vo);
	}


	// 연말정산_사업장 조회
	@Override
	public List<YS030VO> getYS030List(YS030VO vo)  throws Exception {
		
		List<YS030VO> list =  yw900DAO.getYS030List(vo);
		return list;
	}

	
}
