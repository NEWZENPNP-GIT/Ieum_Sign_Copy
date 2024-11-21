package com.ezsign.bbs.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

import com.ezsign.bbs.vo.BbsCommentVO;
import com.ezsign.bbs.vo.BbsContentsVO;
import com.ezsign.bbs.vo.BbsFileVO;
import com.ezsign.bbs.vo.BbsInfoVO;
import com.ezsign.menu.vo.MenuVO;

@Repository("bbsDAO")
public class BbsDAO extends EgovAbstractDAO {
	
	@SuppressWarnings("unchecked")
	public List<BbsInfoVO> getBbsInfoList(BbsInfoVO vo) {
		return (List<BbsInfoVO>)list("bbsDAO.getBbsInfoList", vo);
	}
	
	public int getBbsInfoListCount(BbsInfoVO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("bbsDAO.getBbsInfoListCount", vo);
	}
	
	public void insBbsInfo(BbsInfoVO vo) {
		insert("bbsDAO.insBbsInfo", vo);
	}
	
	public int updBbsInfo(BbsInfoVO vo) {
		return update("bbsDAO.updBbsInfo", vo);
	}

	public int delBbsInfo(BbsInfoVO vo) {
		return delete("bbsDAO.delBbsInfo", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<BbsFileVO> getBbsFileList(BbsFileVO vo) {
		return (List<BbsFileVO>) list("bbsDAO.getBbsFileList", vo);
	}
	
	public void insBbsFile(BbsFileVO vo) {
		insert("bbsDAO.insBbsFile", vo);
	}
	
	public int updBbsFile(BbsFileVO vo) {
		return update("bbsDAO.updBbsFile", vo);
	}
	
	public int delBbsFile(BbsFileVO vo) {
		return delete("bbsDAO.delBbsFile", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<BbsContentsVO> getBbsContentsList(BbsContentsVO vo) {
		return (List<BbsContentsVO>) list("bbsDAO.getBbsContentsList", vo);
	}
	
	public int getBbsContentsListCount(BbsContentsVO vo) {
		return (Integer) getSqlMapClientTemplate().queryForObject("bbsDAO.getBbsContentsListCount", vo);
	}
	
	public BbsContentsVO getBbsContents(BbsContentsVO vo) {
		return (BbsContentsVO) select("bbsDAO.getBbsContents", vo);
	}
	
	public void insBbsContents(BbsContentsVO vo) {
		insert("bbsDAO.insBbsContents", vo);
	}
	
	public int updBbsContents(BbsContentsVO vo) {
		return update("bbsDAO.updBbsContents", vo);
	}
	
	public int delBbsContents(BbsContentsVO vo) {
		return delete("bbsDAO.delBbsContents", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<BbsCommentVO> getBbsCommentList(BbsCommentVO vo) {
		return (List<BbsCommentVO>) list("bbsDAO.getBbsCommentList", vo);
	}
	
	public int getBbsCommentListCount(BbsCommentVO vo) {
		return (Integer) getSqlMapClientTemplate().queryForObject("bbsDAO.getBbsCommentListCount", vo);
	}
	
	public void insBbsComment(BbsCommentVO vo) {
		insert("bbsDAO.insBbsComment", vo);
	}
	
	public int updBbsComment(BbsCommentVO vo) {
		return update("bbsDAO.updBbsComment", vo);
	}
	
	public int delBbsComment(BbsCommentVO vo) {
		return delete("bbsDAO.delBbsComment", vo);
	}
}
