package com.ezsign.annual.vo;


import lombok.Data;

@Data
public class AnnualConfigVO {
	
	private String serviceId;
	private String bizId;
	private String bizName;
	private String requestAlarmUseYn;
	private String approvalAlarmUseYn;
	private String updateUseYn;
	private String annualCreateCode;
	private String annualBoostUseYn;
	private String approvalUseYn;
	private String insDate;
	private String updDate;
	
}
