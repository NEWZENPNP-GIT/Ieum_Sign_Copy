package com.ezsign.paystub.vo;

import lombok.Data;

@Data
public class PaystubLogVO {
	
	private String serviceId;
	private String bizId;
	private String userId;
	private String paystubMonth;
	private String logType;
	private String logMessage;
	private String insDate;
	
}
