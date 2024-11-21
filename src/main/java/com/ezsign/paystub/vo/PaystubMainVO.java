package com.ezsign.paystub.vo;

import lombok.Data;

@Data
public class PaystubMainVO {

	private String minPayMonth;
	private String maxPayMonth;	
	private String payDate;
	private int	   count;
	
}
