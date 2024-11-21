package com.ezsign.biztalk.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.biztalk.dao.BizTalkDAO;
import com.ezsign.biztalk.service.BizTalkService;
import com.ezsign.biztalk.vo.BizTalkKKOVO;
import com.ezsign.framework.util.StringUtil;


@Service("bizTalkService")
public class BizTalkServiceImpl extends AbstractServiceImpl implements BizTalkService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name="bizDAO")
	private BizDAO bizDAO;
	
	@Resource(name="bizTalkDAO")
	private BizTalkDAO bizTalkDAO;
	
	@Override
	public List<BizTalkKKOVO> getBizTalkList(BizTalkKKOVO vo)  throws Exception {
		List<BizTalkKKOVO> list = null;
		
		list = bizTalkDAO.getBizTalkList(vo);
		
		return list;
	}

	@Override
	public boolean sendBizTalk(BizTalkKKOVO vo)  throws Exception {
		boolean result = false;
		
		BizVO bizVO = new BizVO();
		bizVO.setBizId(vo.getBizId());
		bizVO.setStartPage(0);
		bizVO.setEndPage(1);
		List<BizVO> bizList = bizDAO.getBizList(bizVO);
		
		if(bizList==null||bizList.size()==0) {
			return false;
		}				
		
		if(vo.getSendType().equals("sms")) {
			bizTalkDAO.insBizTalkSms(vo);
			result = true;
		} else {
			bizTalkDAO.insBizTalkMms(vo);
			result = true;
		}
		
		return result;
	}
	
	@Override
	public boolean sendBizTalkNonBiz(BizTalkKKOVO vo)  throws Exception {
		boolean result = false;
		
		if(vo.getSendType().equals("sms")) {
			bizTalkDAO.insBizTalkSms(vo);
			result = true;
		} else {
			bizTalkDAO.insBizTalkMms(vo);
			result = true;
		}
		
		return result;
	}

}
