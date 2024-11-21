package com.ezsign.feb.hometax.vo;

import lombok.Data;

@Data
public class PaymentCRecordVO {

	/* 자료관리번호 */
	private String C1;		//레코드구분
	private String C2;		//자료구분
	private String C3;		//세무서코드
	private String C4;		//일련번호
		
	/* 원천징수의무자 */
	private String C5;		//사업자등록번호
	
	/* 소득자(근로자) */
	private String C6;		//주민등록번호
	private String C7;		//성명
	private String C8;		//전화번호
	private String C9;		//내.외국인
	private String C10;		//거주자구분
	private String C11;		//거주지국코드
	private String C12;		//근무기간시작년월일
	private String C13;		//극무기간종료년월일
	private String C14;		//과세소득
	private String C15;		//비과세소득
	private String C16;		//공란
	
}
