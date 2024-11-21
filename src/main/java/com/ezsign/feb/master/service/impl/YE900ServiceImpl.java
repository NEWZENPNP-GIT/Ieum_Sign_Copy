package com.ezsign.feb.master.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.feb.master.dao.YE900DAO;
import com.ezsign.feb.master.service.YE900Service;
import com.ezsign.feb.master.vo.YE900VO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("ye900Service")
public class YE900ServiceImpl extends AbstractServiceImpl implements YE900Service {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "ye900DAO")
	private YE900DAO ye900DAO;

	// 관리자정정사유 조회
	@Override
	public YE900VO getYE900(YE900VO vo) throws Exception {
		YE900VO result = ye900DAO.getYE900(vo);
		return result;
	}

	// 관리자정정사유 저장
	@Override
	public void saveYE900(List<YE900VO> list) throws Exception {
		for (YE900VO vo : list) {
			if ("D".equals(vo.getDbMode())) {
				ye900DAO.updYE900Disable(vo);
			} else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
				ye900DAO.updYE900Disable(vo);
				ye900DAO.insYE900(vo);
			}
		}
	}

	// 관리자정정사유 입력
	@Override
	public void insYE900(YE900VO vo) throws Exception {
		ye900DAO.updYE900Disable(vo);
		ye900DAO.insYE900(vo);
	}

	// 관리자정정사유 사용여부 '0'
	@Override
	public int delYE900(YE900VO vo) throws Exception {
		return ye900DAO.updYE900Disable(vo);
	}
}
