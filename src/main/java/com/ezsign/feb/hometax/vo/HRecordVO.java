package com.ezsign.feb.hometax.vo;

import lombok.Data;

@Data
public class HRecordVO {

	/* 자료관리번호 */
	private String H1;		//레코드구분
	private String H2;		//자료구분
	private String H3;		//세무서코드
	private String H4;		//일련번호
	
	/* 원천징수의무자 */
	private String H5;		//사업자등록번호
	
	/* 소득자*/
	private String H6;		//소득자주민등록번호
	private String H7;		//내외국인 구분코드
	private String H8;		//소득자의성명
	
	/* 기부금조정명세  */
	private String H9;		//유형코드
	private String H10;		//기부연도
	private String H11;		//기부금액
	private String H12;		//전년까지 공제된 금액
	private String H13;		//공제대상금액
	private String H14;		//해당연도 공제금액필요경비
	private String H15;		//해당연도 공제금액 세액 공제 
	private String H16;		//해당연도에 공제받지 못한금액 소멸금액
	private String H17;		//해당연도에 공제받지 못한금액 이월금액
	private String H18;		//기부금조정명세 일련번호
	private String H19;		//공란
	
}
