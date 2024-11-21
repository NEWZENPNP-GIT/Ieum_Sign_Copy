package com.ezsign.paystub.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

import com.ezsign.paystub.vo.PaystubDataVO;
import com.ezsign.paystub.vo.PaystubMainVO;
import com.ezsign.paystub.vo.PaystubVO;

@Repository("paystubDAO")
public class PaystubDAO extends EgovAbstractDAO {

	public PaystubMainVO getPaystubMain(PaystubVO vo) {
		return (PaystubMainVO) select("paystubDAO.getPaystubMain", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaystubMainVO> getPaystubMobileList(PaystubVO vo) {
		return (List<PaystubMainVO>)list("paystubDAO.getPaystubMobileList", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<PaystubVO> getPaystubList(PaystubVO vo) {
		return (List<PaystubVO>)list("paystubDAO.getPaystubList", vo);
	}
	
	public int getPaystubListCount(PaystubVO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("paystubDAO.getPaystubListCount", vo);
	}

	public int updPaystub(PaystubVO vo) {
		return update("paystubDAO.updPaystub", vo);
	}
	
	public int delPaystub(PaystubVO vo) {
		return delete("paystubDAO.delPaystub", vo);
	}
	
	public int delPaystubData(PaystubVO vo) {
		return delete("paystubDAO.delPaystubData", vo);
	}

	public String insPaystub(PaystubVO  vo) {
		return (String) insert("paystubDAO.insPaystub", vo);
	}

	public void insPaystubData(PaystubDataVO vo) {
		insert("paystubDAO.insPaystubData", vo);
		
	}

	@SuppressWarnings("unchecked")
	public List<PaystubDataVO> getPaystubDataList(PaystubDataVO vo) {
		return (List<PaystubDataVO>)list("paystubDAO.getPaystubDataList", vo);
	}

	public String getPaystubFileUserDate(PaystubVO vo) {
		return (String)getSqlMapClientTemplate().queryForObject("paystubDAO.getPaystubFileUserDate", vo);
	}
	
	public String getPaystubFileId(PaystubVO vo) {
		return (String)getSqlMapClientTemplate().queryForObject("paystubDAO.getPaystubFileId", vo);
	}
	
	@SuppressWarnings("unchecked")
	public PaystubVO getPayStubPayDate(PaystubVO vo) {
		return (PaystubVO)getSqlMapClientTemplate().queryForObject("paystubDAO.getPayStubPayDate", vo);
	}

	public int updPaystubStatus(PaystubVO vo) {
		return update("paystubDAO.updPaystubStatus", vo);
	}
}
