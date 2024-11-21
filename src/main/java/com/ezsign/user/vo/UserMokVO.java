package com.ezsign.user.vo;

import lombok.Data;

@Data
public class UserMokVO {
	
	private String userName;
	private String siteID;
	private String clientTxId;
	private String txId;
	private String providerId;
	private String serviceType;
	private String ci;
	private String di;
	private String userPhone;
	private String userBirthday;
	private String userGender;
	private String userNation;
	private String reqAuthType;
	private String reqDate;
	private String issueDate;
	private String issuer;
}
