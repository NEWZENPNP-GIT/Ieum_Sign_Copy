package com.ezsign.feb.hometax.vo;

import lombok.Data;

@Data
public class PaymentARecordVO {

	/* 자료관리번호 */
	private String A1;		//레코드구분
	private String A2;		//자료구분
	private String A3;		//세무서코드
	private String A4;		//제출연월
	
	/* 제출자 */
	private String A5;		//제출자구분
	private String A6;		//세무대리인 관리번호
	private String A7;		//홈택스ID
	private String A8;		//세무프로그램코드
	private String A9;		//사업자등록번호
	private String A10;		//법인명(상호)
	private String A11;		//담당자부서
	private String A12;		//담당자성명
	private String A13;		//담당자전화번호
	
	/* 제출내역 */
	private String A14;		//신고의무자수
	private String A15;		//공란
	
}
