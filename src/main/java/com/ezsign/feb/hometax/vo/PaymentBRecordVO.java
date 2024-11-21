package com.ezsign.feb.hometax.vo;

import lombok.Data;

@Data
public class PaymentBRecordVO {

	/* 자료관리번호 */
	private String B1;		//레코드구분
	private String B2;		//자료구분
	private String B3;		//세무서코드
	private String B4;		//일련번호
	
	/* 원천징수의무자 */
	private String B5;		//상호(법인명)
	private String B6;		//성명(대표자)
	private String B7;		//사업자등록번호
	private String B8;		//주민(법인)등록번호
		
	/* 반기별 원천징수 집계현황 */
	private String B9;		//귀속년도
	private String B10;		//근무시기	
	private String B11;		//근로자수
	private String B12;		//과세소득합계
	private String B13;		//비과세소득합계	
	private String B14;		//공란
	
}
