package com.ezsign.code.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.service.CodeService;
import com.ezsign.code.vo.CodeVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;


@Service("codeService")
public class CodeServiceImpl extends AbstractServiceImpl implements CodeService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name="codeDAO")
	private CodeDAO codeDAO;
	
	@Override
	public List<CodeVO> getCodeList(CodeVO vo)  throws Exception {
		List<CodeVO> list = null;
		
		list = codeDAO.getCodeList(vo);
		
		return list;
	}

}
