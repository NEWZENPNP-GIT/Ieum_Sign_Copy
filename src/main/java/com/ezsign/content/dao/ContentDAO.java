package com.ezsign.content.dao;


import org.springframework.stereotype.Repository;

import com.ezsign.content.vo.ContentVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("contentDAO")
public class ContentDAO extends EgovAbstractDAO {
	
	public void insContent(ContentVO vo) {
		insert("contentDAO.insContent", vo);
	}
	
	public ContentVO getContent(ContentVO vo) {
		return (ContentVO)select("contentDAO.getContent", vo);
	}
	
	public int delContent(ContentVO vo) {
		return update("contentDAO.delContent", vo);
	}
	
}
