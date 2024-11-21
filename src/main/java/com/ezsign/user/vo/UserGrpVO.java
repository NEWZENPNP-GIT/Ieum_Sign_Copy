package com.ezsign.user.vo;

import lombok.Data;

@Data
public class UserGrpVO {

	private String dbMode = "R";
	private String userId;
	private String grpType;
	private String refId;
	private String statusCode;
	private String insDate;
	
	private String id;
	private String name;
	private String no;
	private String num;

	private String searchKey;
	private String searchValue;
	
	private String empName;
	private String empType;
	
	// 기업등록인 경우
	private String bizId;
	private String businessNo;
	private String bizName;
	private String ownerName;
	private String addr1;
	private String companyTelNum;
	private String companyFaxNum;
	private String companyImage;
	
}
