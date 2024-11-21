package com.ezsign.contact.vo;

import lombok.Data;

@Data
public class ContactVO {
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

	private String bizId;			// 기업 ID					BIZ_ID
	private String userId;			// 사용자 ID					USER_ID
	private String contId;			// 주소록 ID					CONT_ID
	private String grpId;			//
	private String grpName;			//
	private String contName;		// 성명						CONT_NAME
	private String phoneNumber;		// 휴대폰번호					PHONE_NUMBER
	private String mailAddr;		// 이메일주소					MAIL_ADDR
	private String corpName;		// 회사						CORP_NAME
	private String corpDept;		// 부서명					CORP_POS
	private String corpPos;			// 직위						CORP_POS
	private String corpTel;			// 전화번호					CORP_TEL
	private String corpFax;			// FAX번호					CORP_FAX
	private String corpAddr;		// 주소						CORP_ADDR
	private String corpUrl;			//							CORP_URL
	private String insDate;			// 등록일시					INS_DATE
	private String updDate;			// 수정일시					UPD_DATE
	
	private String contType;		// 1: 일반주소록, 2: 내부사용자	CONT_TYPE
	private String loginId;
	private String loginPwd;
	private String searchCompany;
	private String contactType;		// P: 개인 , C:거래처 주소록	CONTACT_TYPE
	private String corpBizNum;		// 사업자등록번호				CORP_BIZ_NUM
	private String corpRepName;		// 대표자명					CORP_REP_NAME


	private String recvUserName;	// 메일 받는 사람 / 거래처 이름
}
