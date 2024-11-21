package com.ezsign.cscenter.vo;

import lombok.Data;

@Data
public class CSRequestBizVO {
	
	private String code;
	private String message;	

	private String bizId;
	private String bizType;
	private String businessNo;
	private String bizStatus;
	private String systemBizId;
	private String bizName;
	private String bizDomain;
	private String ownerName;
	private String addr1;
	private String addr2;
	private String companyTelNum;
	private String companyFaxNum;
	private String companyImage;
	private String companyLogo;
	private String insDate;
	private String updDate;
	
	private String subject;
	
	
	private String userId;
	private String userPwd;
	private String userName;
	private String phoneNum;
	private String email;

	private int	 startPage;
	private int  endPage;
	
	private String funnel;
	private String funnelYear;
}
