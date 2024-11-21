package com.ezsign.feb.house.vo;

import lombok.Data;

@Data
public class YE102VO {
	// 거주자간주택임차차입금원리금상환액소득공제명세_금전소비대차계약내용 설정

	private String 계약ID;
	private String 사용자ID;
	private String 작업자ID;
	private String 일련번호;

	private String 대주성명;
	private String 개인식별번호;
	private String 금전소비대차_계약개시일;
	private String 금전소비대차_계약종료일;
	private int 차입금_이자율;
	private int 원리금상환액계;
	private int 원금;
	private int 이자;
	private int 소득공제금액;

	private String 등록일시;
	private String 수정일시;

	private String dbMode = "R";
}
