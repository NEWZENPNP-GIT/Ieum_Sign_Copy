package com.ezsign.menu.vo;

import lombok.Data;

@Data
public class MenuPermVO {

	private String menuType;
	private String userType;
	private String menuCode;
	private String readPerm;
	private String writePerm;
	private String editPerm;
	private String deletePerm;
	private String printPerm;
	private String insDate;
	private String updDate;
	
	
	private String menuName;
	private String pmenuCode;
	private String menuLvl;
	private String menuOdr;
	private String menuUrl;
	private String menuInfo;
	private int    childCnt;
	private String pmenuName;
	// 상단메뉴 class 이름 
	private String cssId;
	
}
