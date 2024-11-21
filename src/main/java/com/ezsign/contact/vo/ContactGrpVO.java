package com.ezsign.contact.vo;

import lombok.Data;

@Data
public class ContactGrpVO {
	///////////////// 공통 START ///////////////////
	private String 	dbMode;
	
	private String 	searchKey;
	private String 	searchValue;
	private int	 	startPage;
	private int  	endPage;
	private String 	sortName;
	private String 	sortOrder;
			
	private String 	code;
	private String 	message;
	///////////////// 공통 END ///////////////////
	
	private String 	bizId;		// 기업ID		BIZ_ID
	private String 	userId;		// 사용자ID		USER_ID
	private String 	grpId;		// 그룹 ID		GRP_ID
	private String 	grpName;	// 그룹명		GRP_NAME
	private String 	grpCnt;
	private String 	insDate;	// 등록일시		INS_DATE
	private String 	updDate;	// 수정일시		UPD_DATE
	
}
