package com.ezsign.contract.vo;

import lombok.Data;

@Data
public class ContractPersonNewFormCommentVO {

	private String bizId;	
	private String fileId;	
	private String commentNo;	
	private String commentContent;	
	private String userId;	
	private String scoreNum;	
	private String regPasswd;	
	private String insDate;	
	private String updDate;
	
	// 진행상태
	private String transType;

	// 사용자구분
	private String userType;	
	private String userTypeName;
	
	
}
