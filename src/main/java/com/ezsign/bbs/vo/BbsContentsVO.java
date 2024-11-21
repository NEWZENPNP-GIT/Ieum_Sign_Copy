package com.ezsign.bbs.vo;

import lombok.Data;

@Data
public class BbsContentsVO {
	
	private String bbsId;
	private String bizId;
	private String bbsNo;
	private String userId;
	private String bizName;
	private String companyTelNum;
	private String empName;
	private String userType;
	private String workCode;
	private String serviceType;
	private String serviceName;
	private String statusCode;
	private String depthNo;
	private String orderNo;
	private String parentNo;
	private String editorType;
	private String contentsType;
	private String subject;
	private String keyword;
	private String contents;
	private String hitCnt;
	private String pointCnt;
	private String expireDate;
	private String target;
	private String regPasswd;
	private String insDate;
	private String updDate;
	
	private String fileNo;
	private String fileCount;
	private String commentCount;
	private String workName;
	private String statusName;
	
	// 기업관리 조회조건 추가여부
	private String searchBizGrpType;
	private String searchUserGrpType;
	private String loginId;
	
	private String searchDateFrom;
	private String searchDateTo;
	
	private String message;
	private String code;
	
	private String sampleId;
	
	private String searchKey;
	private String searchValue;
	private int	 startPage;
	private int  endPage;
	private String sortName;
	private String sortOrder;

	private String adminCheck;
	
}
