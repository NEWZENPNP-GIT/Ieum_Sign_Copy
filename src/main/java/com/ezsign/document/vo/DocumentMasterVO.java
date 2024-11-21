package com.ezsign.document.vo;

import java.util.List;

import lombok.Data;

@Data
public class DocumentMasterVO {
	///////////////// 공통 START ///////////////////

	private String dbMode;
	
	private String searchKey;
	private String searchValue;
	private int	   startPage;
	private int    endPage;
	private String sortName;
	private String sortOrder;
			
	private String code;
	private String message;
	private String domainName;

	///////////////// 공통 END ///////////////////
	
	private String docuId;
	private String bizId;
	private String bizName;
	private String userId;
	private String userName;
	private String docuName;
	private String prevDocuId;
	private String prevFileId;
	private String prevDocuName;
	private String orgFileId;
	private String orgFileName;
	private String conFileId;
	private String digitNonce;
	private String hashData;
	private String reqComment;
	private String authType;
	private String authCode;
	private String signType;
	private String sendType;
	private String sendOrd;
	private String reviewType;
	private String expireType;
	private String expireDate;
	private String tempSaveType;
	private String tempSaveDate;
	private String tsaType;
	private String docuStatus;
	private String docuStatusName;
	private String completeDate;
	private String inputType;
	private String useYn;
	private String insDate;
	private String updDate;
	
	private String pdfFile;
	private String fileId;
	
	private List<DocumentDetailVO> documentDetailList;
	
	// 수신자, 참조자대표명 및 건수
	private String rUserName;
	private String tUserName;
	private String rcount;
	private String tcount;
	
	// 기타설정정보	
	private String nextDocuId;
	private String nextDocuName;
	private String recvType;
	private String bookmark;
	
	// 멀티처리
	private String multiData;

	private String searchCompany;
}
