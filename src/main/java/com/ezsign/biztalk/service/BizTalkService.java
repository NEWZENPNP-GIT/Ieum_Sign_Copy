package com.ezsign.biztalk.service;

import java.util.List;

import com.ezsign.biztalk.vo.BizTalkKKOVO;

public interface BizTalkService {

	public List<BizTalkKKOVO> getBizTalkList(BizTalkKKOVO vo)  throws Exception;

	public boolean sendBizTalk(BizTalkKKOVO vo)  throws Exception;
	
	public boolean sendBizTalkNonBiz(BizTalkKKOVO vo)  throws Exception;
	
}
