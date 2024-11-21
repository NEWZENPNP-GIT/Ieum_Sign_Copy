package com.ezsign.bbs.vo;

import lombok.Data;

@Data
public class BbsCommentVO {
	
	private String bbsId;
	private String bizId;
	private String bizName;
	private String bbsNo;
	private String commNo;
	private String commentContents;
	private int    scoreNum;
	private String regPasswd;
	private String userId;
	private String empName;
	private String userType;
	private String insDate;
	private String updDate;
	
	private String message;
	private String code;
	
	private String searchKey;
	private String searchValue;
	private int	 startPage;
	private int  endPage;
	private String sortName;
	private String sortOrder;
	
}
