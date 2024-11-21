package com.ezsign.document.vo;

import lombok.Data;

@Data
public class DocumentCategoryVO {
	
	private String code;
	private String message;	

	private String dbMode;
	private String cateCode;
	private String cateName;
	private String pcateCode;
	private String pcateName;
	private String cateLvl;
	private String cateOdr;
	private String useYn;
	private String insDate;
	private String insImpNo;
	private String updDate;
	
	private int	 startPage;
	private int  endPage;
	private String sortName;
	private String sortOrder;
	
	private String cateType;
	

	
}
