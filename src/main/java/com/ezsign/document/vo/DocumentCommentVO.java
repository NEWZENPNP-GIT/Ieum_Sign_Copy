package com.ezsign.document.vo;

import lombok.Data;

@Data
public class DocumentCommentVO {
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
	///////////////// 공통 END ///////////////////
	
	private String seqNo;
	private String docuId;
	private String recvUserId;
	private String recvComment;
	private String sendType;
	private String insDate;
	
	/* 명칭참고용 */
	private String recvUserName;
	
}
