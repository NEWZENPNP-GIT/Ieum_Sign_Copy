package com.ezsign.feb.etc.vo;

import lombok.Data;

@Data
public class YE501VO {
	// 납세조합공제

	private String 계약ID;
	private String 사용자ID;
	private String 작업자ID;
	private String 일련번호;

	private int 금액;

	private String 등록일시;
	private String 수정일시;

	private String dbMode = "R";
}
