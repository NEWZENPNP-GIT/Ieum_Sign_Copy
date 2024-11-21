package com.ezsign.feb.worker.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ezsign.feb.master.vo.YP000VO;
import com.ezsign.feb.worker.dao.YP003DAO;
import com.ezsign.feb.worker.service.YP003Service;

@Service("yp003Service")
public class YP003ServiceImpl implements YP003Service {

	@Resource(name="yp003DAO")
	private YP003DAO yp003DAO;
	
	// 간이지급명세서_원천명세(급여정보) 일괄 처리
	@Override
	public void saveTaxOverSumPayment(List<YP000VO> list) throws Exception {
		
		for(int i=0; i<list.size(); i++) {
			YP000VO yp000VO = list.get(i);
			if ("C".equals(yp000VO.getDbMode())) {
				yp003DAO.insYP003(yp000VO);
			}
			else if("U".equals(yp000VO.getDbMode())) {
				yp003DAO.updYP003(yp000VO);
			} else if("D".equals(yp000VO.getDbMode())) {
				yp003DAO.delYP003(yp000VO);
			}
		}
		
	}
	
}
