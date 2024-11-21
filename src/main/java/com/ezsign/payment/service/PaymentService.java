package com.ezsign.payment.service;

import com.ezsign.payment.vo.PaymentVO;
import com.ezsign.point.vo.PointVO;

public interface PaymentService {
	
	public void insPayment(PaymentVO vo) throws Exception;

	public void insPaymentRequest(PaymentVO vo) throws Exception;

	public void sendContractNewUploadEmail(PointVO vo) throws Exception;
	
}
