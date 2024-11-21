package com.ezsign.feb.hometax.vo;

import lombok.Data;

@Data
public class BRecordVO {

	/* */
	private String 사용자ID;
	
	/* 자료관리번호 */
	private String B1;		//레코드구분
	private String B2;		//자료구분
	private String B3;		//세무서코드
	private String B4;		//일련번호
	
	/* 원천징수의무자 */
	private String B5;		//사업자등록번호
	private String B6;		//법인명
	private String B7;		//대표자
	private String B8;		//주민(법인)등록번호
		
	/* 제출내역 */
	private String B9;		//주(현)근무처 (C레코드)수
	private String B10;		//종(전)근무처 (D레코드)수	
	private String B11;		//총급여 총계
	private String B12;		//결정세액(소득세)총계
	private String B13;		//결정세액(지방소득세)총계
	private String B14;		//결정세액(농특세)총계
	private String B15;		//결정세액총계
	private String B16;		//제출대상기간 코드
	private String B17;		//공란
	
}
