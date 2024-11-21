package com.ezsign.user.vo;

import lombok.Data;

@Data
public class UserSnsVO {

	private String userSnsId;
	private String userId;
	private String snsType;
	private String snsId;
	private String snsEMail;
	private String snsName;
	private String insDate;
	private String updDate;
	
	private String accessToken;	
	private String refreshToken;
	private String idToken;
	private String statusCode;
	
	private String success;
	private String message;
}
