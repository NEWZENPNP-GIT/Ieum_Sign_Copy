package com.ezsign.document.vo;

import java.util.List;

import lombok.Data;

@Data
public class DocumentFileVO {
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
	///////////////// 공통 END ///////////////////

	private String docuId;
	private String fileId;
	private String fileName;
	private String fileUserId;
	private String recvSign;
	private String insDate;
	private String updDate;

	private String fileUserName;
}
