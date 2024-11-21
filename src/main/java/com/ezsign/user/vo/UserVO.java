package com.ezsign.user.vo;

import lombok.Data;

@Data
public class UserVO {

	private String userId;
	private String userPwd;
	private String userName;
	private String userType;
	private String appType;
	private String userNonce;
	private int loginCount;
	private String insDate;
	private String updDate;
	private String loginLockYn;
	private int unlockTime;

	private String bizId;
	private String bizName;
	private String businessNo;
	private String ownerName;
	private String companyImage;
	private String companyAddr;
	private String serviceId;
	private String empNo;
	private String prevUserPwd;	
	private String eMail;
	private String phoneNum;
	private String ipAddr;
	private String loginId;

	private String changeUserId;
	
	private String pwdUpdDate; // 비밀번호 변경일자 추가(21.10.07 이준경)
	private String funnel; // 유입경로 추가(21.11.02 이두호)
	private String funnelYear; // 유입경로 추가(21.11.02 이두호)
	private String version;	//모바일 버젼 체크
}
