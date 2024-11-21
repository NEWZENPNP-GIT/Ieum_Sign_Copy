package com.ezsign.account.vo;

import lombok.Data;

@Data
public class AccountVO {
	
	private String code;
	private String message;	
	
	private String bizId;
	private String businessNo;
	private String bizName;
	private String insDate;
	private String updDate;
	
	private String userId;
	private String loginId;
	private String phoneNum;
	
	private String fileId;
	private String filePath;

	// 멀티처리
	private String multiData;
}
