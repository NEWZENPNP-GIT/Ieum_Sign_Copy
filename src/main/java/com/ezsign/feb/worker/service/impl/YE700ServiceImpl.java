package com.ezsign.feb.worker.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.feb.worker.dao.YE700DAO;
import com.ezsign.feb.worker.service.YE700Service;
import com.ezsign.feb.worker.vo.YE700VO;
import com.ezsign.framework.util.StringUtil;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("ye700Service")
public class YE700ServiceImpl extends AbstractServiceImpl implements YE700Service {

	Logger logger = Logger.getLogger(this.getClass());
	

	@Resource(name="ye700DAO")
	private YE700DAO ye700DAO;
	
	// 연말정산 요약 리스트 조회
	@Override
	public List<YE700VO> getYE700WorkerList(YE700VO vo) {
		List<YE700VO> list = ye700DAO.getYE700WorkerList(vo);
		return list;
	}

	// 연말정산 요약 리스트 조회 개수
	@Override
	public int getYE700WorkerListCount(YE700VO vo) {
		return ye700DAO.getYE700WorkerListCount(vo);
	}
}
