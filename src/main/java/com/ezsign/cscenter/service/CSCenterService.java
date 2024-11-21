package com.ezsign.cscenter.service;

import com.ezsign.biz.vo.BizVO;
import com.ezsign.cscenter.vo.CSRequestBizVO;



public interface CSCenterService {

	public int requestBiz(CSRequestBizVO vo) throws Exception;

	public int completedBiz(CSRequestBizVO vo) throws Exception;
	
	public void insertBizRequst(CSRequestBizVO vo) throws Exception;
}
