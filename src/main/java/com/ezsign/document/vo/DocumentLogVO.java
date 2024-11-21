package com.ezsign.document.vo;

import lombok.Data;

@Data
public class DocumentLogVO {
	private String docuId;
	private String docuName;
	private String logType;
	private String logTypeName;
	private String logMessage;
	private String workUserId;
	private String workUserName;
	private String fileId;
	private String insDate;
	
}
