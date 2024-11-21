package com.ezsign.welmoney.vo;

import lombok.Data;

@Data
public class WelMoneyUserVO {

	private String userId;
	private String userPwd;
	private String userName;
	private String startDate;
	private String endDate;
	private String phoneNum;
	private String eMail;
	
	private String searchKey;
	private String searchValue;
}
