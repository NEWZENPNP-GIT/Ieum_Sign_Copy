package com.ezsign.feb.master.vo;

import lombok.Data;

@Data
public class YE900VO {
	// 관리자정정사유
	private String 계약ID;
	private String 사용자ID;
	private String 사유구분;
	private String 일련번호;

	private String 정정사유;
	private String 작업자ID;

	private String 등록일시;
	private String 수정일시;

	private String dbMode = "R";
}
