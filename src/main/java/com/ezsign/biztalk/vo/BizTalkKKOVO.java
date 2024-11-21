package com.ezsign.biztalk.vo;

import lombok.Data;

@Data
public class BizTalkKKOVO {

	private String subject;
	private String content;
	private String callback;
	private String recipientNum;
	private String senderKey; //카카오톡 알림톡 발신 프로필 키 - biztalk 하드코딩
	private String templateCode;
	private String kkoBtnType; // 1-format string 2-JSON 3-XML -> 기본값2 (BizTalkBtnInfoVO 객체사용)
	private String kkoBtnInfo;
	
	private String reportCode;
	private String msgType;
	private String msgStatus;
	private String regDate;
	private String regDateTran;

	private String table;
	
	private String bizId;
	private String bizName;
	private String userId;
	private String empName;
		
	private String sendType;
}
