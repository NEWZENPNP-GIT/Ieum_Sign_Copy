package com.ezsign.feb.hometax.vo;

import lombok.Data;

@Data
public class IRecordVO {
	
	/* 자료관리번호 */
	private String I1;		//레코드구분
	private String I2;		//자료구분
	private String I3;		//세무서코드
	private String I4;		//일련번호
	
	/* 원천징수의무자 */
	private String I5;		//사업자등록번호
	
	/* 소득자*/
	private String I6;		//소득자주민등록번호
	
	/* 기부유형코드 */
	private String I7;		//유형코드
	private String I8;		//기부내용
	
	/* 기부처 */
	private String I9;		//사업자(주민)등록번호
	private String I10;		//상호(법인명)
	
	/* 기부자 */
	private String I11;		//관계코드
	private String I12;		//내외국인 구분코드
	private String I13;		//성명
	private String I14;		//주민등록번호
	
	/* 기부명세 */
	private String I15;		//건수
	private String I16;		//기부금합계금액
	private String I17;		//공제대상기부금액
	private String I18;		//기부장려금신청금액
	private String I19;		//기타
	private String I20;		//해당연도 기부명세 일련번호
	private String I21;		//공란
	
}
