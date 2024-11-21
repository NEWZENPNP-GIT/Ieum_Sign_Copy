package com.ezsign.menu.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MenuVO {

	private String menuType;
	private String menuCode;
	private String menuName;
	private String pmenuCode;
	private String menuLvl;
	private String menuOdr;
	private String menuUrl;
	private String menuInfo;
	private String useYn;
	private String insDate;
	private String updDate;
	
	private String id;
	private String userType;
	private int    childCnt;
	private String pmenuName;
	// 상단메뉴 class 이름 
	private String cssId;
	
	// 팝업여부
	private	String openType;
	private int    popupWidth;
	private int	   popupHeight;
	
	private List<MenuVO> children = new ArrayList<MenuVO>();
}
