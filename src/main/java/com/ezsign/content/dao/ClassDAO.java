package com.ezsign.content.dao;


import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


import com.ezsign.content.vo.ClassVO;

@Repository("classDAO")
public class ClassDAO extends EgovAbstractDAO {
	
	public ClassVO getClass(ClassVO vo) {
		return (ClassVO)selectByPk("classDAO.getClass", vo);
	}

}
