package com.ezsign.bbs.vo;

import lombok.Data;

@Data
public class BbsFileVO {
	
	private String bbsId;
	private String bizId;
	private String bbsNo;	
	private String fileNo;
	private String filePath;
	private String fileName;
	private String orgFileName;
	private long    fileSize;
	private String useYn;
	private int    downCnt;
	private String userId;
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
