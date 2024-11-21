package com.ezsign.code.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.code.vo.CodeVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("codeDAO")
public class CodeDAO extends EgovAbstractDAO {
	
	@SuppressWarnings("unchecked")
	public List<CodeVO> getCodeList(CodeVO vo) {
		return (List<CodeVO>)list("codeDAO.getCodeList", vo);
	}

	public String getTableKey(String key) {
		return (String)selectByPk("codeDAO.getTableKey", key);
	}
	
	public String getCodeName(CodeVO vo) {
        return (String) select("codeDAO.getCodeName", vo);
    }
}
