package com.ezsign.contract.vo;

import lombok.Data;

@Data
public class ContractSettingVO {
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
	
	private String fileId;
	private String userId;
	private String recvType;
	private String settingType;
	private String settingValue;
	private String insDate;
	private String updDate;
	
}
