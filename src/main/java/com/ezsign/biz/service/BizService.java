package com.ezsign.biz.service;

import java.util.List;

import com.ezsign.biz.vo.BizGrpVO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.biz.vo.CandycashBizVO;
import com.ezsign.framework.vo.FileVO;

public interface BizService {

	public int getCheckService(BizVO vo)  throws Exception;
	
	public List<BizVO> getBizList(BizVO vo)  throws Exception;

	public List<BizVO> getCsBizList(BizVO vo)  throws Exception;

	public int getBizListCount(BizVO vo)  throws Exception;

	public int getCsBizListCount(BizVO vo)  throws Exception;
	
	public String insBiz(BizVO vo) throws Exception;
	
	public String insAdminBiz(BizVO vo) throws Exception;
	
	public int updBiz(BizVO vo) throws Exception;

	public int updBizTax(BizVO vo) throws Exception;

	public int delBiz(BizVO vo) throws Exception;

	public void insBizService(BizVO vo) throws Exception;
	

	public int insBizGrp(BizGrpVO vo) throws Exception;
	
	public int addBizGrp(BizGrpVO vo) throws Exception;
	
	public int addBizGrpList(List<BizGrpVO> list) throws Exception;

	public int delBizGrpList(List<BizGrpVO> list) throws Exception;

	public int updBizGrp(BizGrpVO vo) throws Exception;

	public int insBizGrpBiz(BizVO vo) throws Exception;
	
	public void delBizGrp(BizGrpVO vo) throws Exception;
	
	public List<BizGrpVO> getBizGrp(BizGrpVO vo) throws Exception;

	public List<BizGrpVO> getBizGrpList(BizGrpVO vo) throws Exception;
	
	public List<BizGrpVO> getBizGrpCombo(BizGrpVO vo) throws Exception;

	public int updBizLogo(BizVO vo, FileVO fileVO) throws Exception;

	public int delBizLogo(BizVO vo) throws Exception;
	
	public int updBizStamp(BizVO vo, FileVO fileVO) throws Exception;

	public int delBizStamp(BizVO vo) throws Exception;

	public int updBizGrpSubAdmin(BizGrpVO vo) throws Exception;

	public List<BizGrpVO> getBizGrpSubAdminList(BizGrpVO vo) throws Exception;
	
	public List<BizVO> getServiceList(BizVO vo) throws Exception;
	
	public int insBizServiceAdd(BizVO vo) throws Exception;

	public List<BizGrpVO> getBizGrpNameList(BizGrpVO vo) throws Exception;
	
	public void updateOpenVoucherContractEnd() throws Exception;
	
	public void updateOpenVoucherPoint(BizVO vo) throws Exception;
	
	public List<BizVO> getupdateOpenVoucher(BizVO vo) throws Exception;
	
	public List<BizVO> getContractD30List(BizVO vo) throws Exception;
	
	public List<BizVO> getContractEndList(BizVO vo) throws Exception;
	
	public List<BizVO> getDownloadExpireList(BizVO vo) throws Exception;
	
	public List<CandycashBizVO> getCandyCashBizList(CandycashBizVO vo)  throws Exception;
	
	public void updCandyCashBizInfo(CandycashBizVO vo) throws Exception;

	public List<BizVO> getBizEmail(BizVO vo)  throws Exception;

}
