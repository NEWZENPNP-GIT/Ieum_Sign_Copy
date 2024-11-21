package com.ezsign.feb.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.feb.system.dao.YS000DAO;
import com.ezsign.feb.system.service.YS000Service;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.user.vo.UserVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("ys000Service")
public class YS000ServiceImpl extends AbstractServiceImpl implements YS000Service {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="codeDAO")
	private CodeDAO codeDAO;

	@Resource(name="ys000DAO")
	private YS000DAO ys000DAO;
	
	// 연말정산_계약정보 조회
	@Override
	public List<YS000VO> getYS000List(YS000VO vo)  throws Exception {
		List<YS000VO> list =  ys000DAO.getYS000List(vo);
		
		return list;
	}

	// 연말정산_계약정보 등록
	@Override
	public void insYS000(YS000VO vo) throws Exception {
		String 계약ID = SecurityUtil.bitEncryption(vo.getFebYear()+vo.getBizId());
		vo.set계약ID(계약ID);
		ys000DAO.insYS000(vo);
	}

}
