package com.ezsign.payment.dao;


import org.springframework.stereotype.Repository;

import com.ezsign.payment.vo.PaymentVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("paymentDAO")
public class PaymentDAO extends EgovAbstractDAO {

	public void insPayment(PaymentVO vo) {
		insert("paymentDAO.insPayment", vo);
	}

	public void insPaymentRequest(PaymentVO vo) {
		insert("paymentDAO.insPaymentRequest", vo);
	}
	
	public PaymentVO getPaymentRequest(PaymentVO vo) {
		return (PaymentVO)selectByPk("paymentDAO.getPaymentRequest", vo);
	}
	
}
