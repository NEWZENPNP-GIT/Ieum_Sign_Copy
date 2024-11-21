package com.ezsign.bbs.vo;

import lombok.Data;

@Data
public class BbsInfoVO {

	private String bizId;
	private String bbsId;
	private String bbsName;
	private String bbsType;
	private int    dispLine;
	private int    dispPage;
	private String managerId;
	private String fileUseYn;
	private int    fileCount;
	private int    fileLimit;
	private String useYn;
	private String editorYn;
	private String loginChkYn;
	private String bbsComment;
	private String newTime;
	private String hotChk;
	private String badWordUseYn;
	private String badWord;
	private String mailToMaster;
	private String mailToManager;
	private String commentUseYn;
	private String scoreUseYn;
	private String viewThreadYn;
	private String viewPrevNextYn;
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
