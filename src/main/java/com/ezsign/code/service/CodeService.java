package com.ezsign.code.service;

import java.util.List;

import com.ezsign.code.vo.CodeVO;


public interface CodeService {

	public List<CodeVO> getCodeList(CodeVO vo)  throws Exception;
	
}
