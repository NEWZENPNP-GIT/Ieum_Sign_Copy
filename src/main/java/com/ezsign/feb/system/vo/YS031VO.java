package com.ezsign.feb.system.vo;

import lombok.Data;

@Data
public class YS031VO {
	// 부서등록
	private String code;
	private String message;
	
	private String bizId;
	private String febYear;
	private String 계약ID; 
	private String 부서ID; 
	private String 사업장ID;
	private String 사업장명; 
	private String 부서명;
	private String dbMode;
	private String 등록일시; 
	private String 수정일시;
	
	private String sortName;
	private String sortOrder;
	private int startPage;
	private int endPage;
	
}
