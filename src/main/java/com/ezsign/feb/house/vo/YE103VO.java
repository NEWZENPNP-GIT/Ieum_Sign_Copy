package com.ezsign.feb.house.vo;

import lombok.Data;

@Data
public class YE103VO {
	// 거주자간주택임차차입금원리금상환액소득공제명세_임대차계약내용 설정

	private String 계약ID;
	private String 사용자ID;
	private String 작업자ID;
	private String 일련번호;

	private String 임대인성명_상호;
	private String 개인식별번호;
	private String 유형코드;
	private String 계약면적;
	private String 임대차_주소지_물건지;
	private String 임대차_계약개시일;
	private String 임대차_계약종료일;
	private int 전세보증금;

	private String 등록일시;
	private String 수정일시;

	private String dbMode = "R";
}
