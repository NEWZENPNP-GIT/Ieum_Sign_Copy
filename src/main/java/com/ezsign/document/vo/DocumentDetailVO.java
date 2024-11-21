package com.ezsign.document.vo;

import lombok.Data;

@Data
public class DocumentDetailVO {
	///////////////// 공통 START ///////////////////
	private String dbMode;
	
	private String searchKey;
	private String searchValue;
	private int	 startPage;
	private int  endPage;
	private String sortName;
	private String sortOrder;
	private String code;
	private String message;
	private String domainName;
	///////////////// 공통 END ///////////////////
	
	private String docuId;
	private String recvOrd;
	private String recvType;
	private String recvUserId;
	private String recvPhone;
	private String recvEmail;
	private String viewDate;
	private String confirmDate;
	private String recvStatus;
	private String recvMessage;
	private String recvSign;
	private String insDate;
	private String updDate;
	
	/* 참고용 */
	private String bizId;
	private String docuName;
	private String recvUserName;
	private String recvTypeName;
	private String recvStatusName;
	private String contactType;			// P : 인사  C : 거래처
	private String docuStatus;

	private String userId;
	private String signImage;			// 직인 이미지 경로
	
	
}
