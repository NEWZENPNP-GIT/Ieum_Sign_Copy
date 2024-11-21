package com.ezsign.feb.master.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.service.YE901Service;
import com.ezsign.feb.master.vo.YE901VO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("ye901Service")
public class YE901ServiceImpl extends AbstractServiceImpl implements YE901Service {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "ye901DAO")
	private YE901DAO ye901DAO;

	// 연말정산_사용자_진행현황
	@Override
	public List<YE901VO> getYE901List(YE901VO vo) throws Exception {
		List<YE901VO> list = null;
		list = ye901DAO.getYE901List(vo);
		return list;
	}



}
