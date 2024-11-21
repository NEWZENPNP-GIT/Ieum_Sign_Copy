package com.ezsign.contract.vo;

import lombok.Data;

@Data
public class ContractPersonLogVO {

	private String contId;
	private String serviceId;
	private String bizId;
	private String userId;
	private String contractDate;
	private String contractId;
	private String logType;
	private String logMessage;
	private String insDate;
	
	private String empName;
	private String logName;

	private String insEmpNo;
	private String insEmpName;
	
}
