package com.ezsign.feb.master.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.feb.master.dao.YE004DAO;
import com.ezsign.feb.master.service.YE004Service;
import com.ezsign.feb.master.vo.YE004VO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("ye004Service")
public class YE004ServiceImpl extends AbstractServiceImpl implements YE004Service {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "ye004DAO")
	private YE004DAO ye004DAO;

	// pdf upload 결과 입력
	@Override
	public void insUpdYE004(YE004VO vo) throws Exception {
		ye004DAO.insUpdYE004(vo);
	}
}
