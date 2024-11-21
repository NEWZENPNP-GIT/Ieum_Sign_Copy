package com.ezsign.contact.vo;

import lombok.Data;

@Data
public class ContactCertVO {
	private String bizId;
	
	private byte[] idBytes;
	private String id;
	private String recvUserName;
	private String recvUserPhone;
	private String domainName;
	private String docuName;
	private String bizName;
	private String reqNum;
	
}

