package com.ezsign.paystub.vo;

import lombok.Data;

@Data
public class PaystubDataVO {
	
	private String serviceId;
	private String bizId;
	private String bizName;
	private String userId;
	private String payMonth;
	private String payDate;
	private String payType;
	private String payTaxType;
	private String payTaxCode;
	private String payTaxName;
	private String payTaxAmt;
	private String insDate;
	
	private String payTaxCalcEvidence; // 2021.11.19 근로기준법 개정으로인한 계산근거 필드 추가
	

	
}
