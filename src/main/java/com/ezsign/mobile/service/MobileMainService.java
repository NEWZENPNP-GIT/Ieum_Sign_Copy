package com.ezsign.mobile.service;

import java.util.List;

import com.ezsign.biz.vo.BizVO;
import com.ezsign.contract.vo.ContractPersonVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.mobile.vo.MobileMainResponse;
import com.ezsign.paystub.vo.PaystubDataVO;
import com.ezsign.paystub.vo.PaystubMainVO;
import com.ezsign.paystub.vo.PaystubVO;
import com.ezsign.user.vo.UserVO;

public interface MobileMainService {

	public MobileMainResponse getMobileMain(SessionVO loginVO, String 계약ID) throws Exception;
	
	public List<PaystubMainVO> getPaystubMobileList(SessionVO loginVO) throws Exception;
	
	public boolean checkBusinessNumber(String businessNo) throws Exception;
	
	public int insMobileBizUser(UserVO vo) throws Exception;
	
	public String insMobilePaystub(PaystubVO vo) throws Exception;
	
	public void insMobilePaystubData(PaystubDataVO vo) throws Exception;
	
	public void delPaystub(PaystubVO vo) throws Exception;

	List<BizVO> getBizListGetOne(BizVO vo) throws Exception;
	
	public String getCandyCashVersion() throws Exception;
	
	public ContractPersonVO getContractView(ContractPersonVO vo, SessionVO loginVO)  throws Exception;
	
	public void insWelmoneyLoginLog(String loginId, String ipAddr) throws Exception;
	
	public boolean welmoneyLoginCheck(String loginId) throws Exception;
}
