package com.ezsign.bbs.service;

import java.util.List;

import com.ezsign.bbs.vo.BbsCommentVO;
import com.ezsign.bbs.vo.BbsContentsVO;
import com.ezsign.bbs.vo.BbsFileVO;
import com.ezsign.bbs.vo.BbsInfoVO;
import com.ezsign.framework.vo.FileVO;

public interface BbsService {
	
	// 게시판 종류
	public static final String NOTICE = "180706135108002";
	public static final String MAINNOTICE = "202005130548001";
	public static final String FAQ = "180709094128003";
	public static final String N11 = "180709094411004";	
	public static final String QNA = "180709094446005";
	public static final String BBS1 = "180723191822007";
	public static final String BBS2 = "180723191901008";
	public static final String BBS3 = "180723191919009";
	public static final String BBS4 = "180723191929010";
	public static final String BBS5 = "180723191942011";
	public static final String BBS6 = "180723191955012";
	public static final String BBS7 = "180723195553013";
	public static final String ADMIN = "181226150423015";
	
	public void insBbsInfo(BbsInfoVO vo) throws Exception;
	
	public List<BbsInfoVO> getBbsInfoList(BbsInfoVO vo) throws Exception;
	
	public int getBbsInfoListCount(BbsInfoVO vo) throws Exception;
	
	public int updBbsInfo(BbsInfoVO vo) throws Exception;
	
	public int delBbsInfo(BbsInfoVO vo) throws Exception;
	
	public int insBbsContents(BbsContentsVO vo, List<FileVO> bbsFileList, int userType) throws Exception;
	
	public List<BbsContentsVO> getBbsContentsList(BbsContentsVO vo) throws Exception;
	
	public int getBbsContentsListCount(BbsContentsVO vo) throws Exception;
	
	public BbsContentsVO getBbsContents(BbsContentsVO vo) throws Exception;
	
	public int updBbsContents(BbsContentsVO vo, List<FileVO> bbsFileList, int userType) throws Exception;
	
	public int delBbsContents(BbsContentsVO vo, int userType) throws Exception;
	
	public int insBbsFile(BbsFileVO vo) throws Exception;
	
	public List<BbsFileVO> getBbsFileList(BbsFileVO vo) throws Exception;
	
	public int updBbsFile(BbsFileVO vo) throws Exception;
	
	public int delBbsFile(BbsFileVO vo) throws Exception;
	
	public int insBbsComment(BbsCommentVO vo, int userType) throws Exception;
	
	public List<BbsCommentVO> getBbsCommentList(BbsCommentVO vo) throws Exception;

	public int getBbsCommentListCount(BbsCommentVO vo) throws Exception;
	
	public int updBbsComment(BbsCommentVO vo) throws Exception;
	
	public int delBbsComment(BbsCommentVO vo) throws Exception;

	void sendCommentEmail(BbsCommentVO vo) throws Exception;
}
