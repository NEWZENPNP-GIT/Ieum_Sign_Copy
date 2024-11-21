package com.ezsign.content.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.biz.vo.BizVO;
import com.ezsign.content.vo.ContentWordVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("contentWordDAO")
public class ContentWordDAO extends EgovAbstractDAO {
	
	public void insContentWord(ContentWordVO vo) {
		insert("contentWordDAO.insContentWord", vo);
	}
	
	public List<ContentWordVO> getContentWordList(ContentWordVO vo) {
		return (List<ContentWordVO>)list("contentWordDAO.getContentWordList", vo);
	}
	
	public List<ContentWordVO> getContentWord(ContentWordVO vo) {
		return (List<ContentWordVO>)list("contentWordDAO.getContentWord", vo);
	}
	
	public int delContentWord(ContentWordVO vo) {
		return delete("contentWordDAO.delContentWord", vo);
	}
	
}
