package com.ezsign.feb.master.service;

import java.util.List;

import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.master.vo.YP000VO;

public interface FebMasterService {

	public List<YE000VO> getUserStatusList(YE000VO vo)  throws Exception;
	
	public int getUserStatusListCount(YE000VO vo) throws Exception;
	
	public YE000VO getUserStatusSum(YE000VO vo)  throws Exception;

	public YE000VO getUserWorkMonth(YE000VO vo)  throws Exception;
	
	public int setYE000AllConfirm(YE000VO vo)  throws Exception;

	public int setYE000Confirm(YE000VO vo)  throws Exception;
	
	public List<YE901VO> getUserStatusDate(YE000VO vo)  throws Exception;
	
	//간이지급명세서 
	public List<YP000VO> getPaymentUserStatusList(YP000VO vo) throws Exception;
	
	//간이지급명세서 
	public int getPaymentUserStatusListCount(YP000VO vo) throws Exception;
	
	//간이지급명세서_근로자_근무년월
	public YP000VO getPaymentUserWorkMonth(YP000VO vo) throws Exception;
	
}
