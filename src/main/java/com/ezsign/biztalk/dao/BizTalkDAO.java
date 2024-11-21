package com.ezsign.biztalk.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

import com.ezsign.biz.vo.BizVO;
import com.ezsign.biztalk.vo.BizTalkKKOVO;

@Repository("bizTalkDAO")
public class BizTalkDAO extends EgovAbstractDAO {
	
	public void insBizTalk(BizTalkKKOVO vo) {
		insert("bizTalkDAO.insBizTalk", vo);
	}
	
	public void insBizTalkSms(BizTalkKKOVO vo) {
		insert("bizTalkDAO.insBizTalkSms", vo);
	}
	
	public void insBizTalkMms(BizTalkKKOVO vo) {
		insert("bizTalkDAO.insBizTalkMms", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<BizTalkKKOVO> getBizTalkList(BizTalkKKOVO vo) {
		return (List<BizTalkKKOVO>)list("bizTalkDAO.getBizTalkList", vo);
	}
	
}
