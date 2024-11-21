package com.ezsign.feb.hometax.vo;

import lombok.Data;

@Data
public class CARecordVO {

	/* 자료관리번호 */
	private String CA1;		//레코드구분
	private String CA2;		//자료구분
	private String CA3;		//세무서코드
	private String CA4;		//일련번호
	private String CA5;		//제출연월일
	
	/* 제출자 */
	private String CA6;		//사업자등록번호
	private String CA7;		//홈택스아이디
	private String CA8;		//세무프로그램코드
	
	/* 원천징수의무자 */
	private String CA9;		//사업자등록번호
	private String CA10;	//상호
	
	/* 소득자(연말정산신청자) */
	private String CA11;	//소득자개인식별번호
	private String CA12;	//내외국인구분코드
	private String CA13;	//성명
	
	/* 지급처 */
	private String CA14;	//지급처사업자등록번호
	private String CA15;	//지급처상호
	private String CA16;	//의료증빙코드
	
	/* 지급명세 */
	private String CA17;	//건수
	private String CA18;	//금액
	private String CA19;	//난임시술비해당여부
	
	/* 의료비 공제 대상자 */
	private String CA20;	//개인식별번호
	private String CA21;	//내외국인코드
	private String CA22;	//본인등해당여부
	private String CA23;	//제출대상기간 코드
	private String CA24;	//공란
		
}
