package com.ezsign.feb.master.vo;

import lombok.Data;

@Data
public class YE901VO {
	// 근로자 연말정산 현황 LOG
	private String code;
	private String message;
	private String dbMode;
	
	private String bizId;
	private String febYear;
	private String 계약ID;
	private String 사용자ID;
	private String 작업자ID;
	private int    일련번호;
	private String 진행현황코드;
	private String 등록일시;
	private String 수정일시;

	
}
