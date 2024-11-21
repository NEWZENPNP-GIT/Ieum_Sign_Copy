package com.ezsign.feb.hometax.vo;

import lombok.Data;

@Data
public class DRecordVO {

	/*  자료관리번호 */
	private String D1;		//레코드구분
	private String D2;		//자료구분
	private String D3;		//세무서코드
	private String D4;		//일련번호
	
	/* 원천징수의무자 */
	private String D5;		//사업자등록번호
	
	/* 소득자 */
	private String D6;		//소득자주민등록번호
	
	/* 근무처별 소득명세 - 종(전)근무처 */
	private String D7;		//납세조합여부
	private String D8;		//법인명(상호)
	private String D9;		//사업자등록번호
	private String D10;		//근무기간 시작연월일
	private String D11;		//근무기간 종료연월일
	private String D12;		//감면기간 시작연월일
	private String D13;		//감면기간 종료연월일
	private String D14;		//급여
	private String D15;		//상여
	private String D16;		//인정상여
	private String D17;		//주식매수선택권행사이익
	private String D18;		//우리사주조합인출금
	private String D19;		//임원퇴직소득한도 초과액
	private String D20;		//직무발명보상금
	private String D21;		//공란
	private String D23;		//종전근무처의 소득합계
	
	/* 종(전)근무처 비과세소득 및 감면소득 */
	private String D24;		//G01 - 비과세학자금
	private String D25;		//H01 - 무보수위원수당
	private String D26;		//H05 - 경호.승선수당
	private String D27;		//H06 - 유아.초중등
	private String D28;		//H07 - 고등교육법
	private String D29;		//H08 - 특별법
	private String D30;		//H09 - 연구기관 등
	private String D31;		//H10 - 기업부설연구소
	private String D32;		//H14 - 보육교사 근무환경 개선비 
	private String D33;		//H15 - 사립유치원 수석교사.교사의인건비
	private String D34;		//H11 - 취재수당
	private String D35;		//H12 - 벽지수당
	private String D36;		//H13 - 재해관련급여
	private String D37;		//H16 - 정부공공기관 지방이전기관 종사자 이주수당
	private String D38;		//H17 - 종교활동비		
	private String D39;		//I01 - 외국정부등근무자
	private String D40;		//K01 - 외국주둔군인등
	private String D41;		//M01 - 국외근로 100만원	
	private String D42;		//M02 - 국외근로 300만원 
	private String D43;		//M03 - 국외근로
	private String D44;		//O01 - 야간근로수당
	private String D45;		//Q01 - 출산보육수당
	private String D46;		//R10 - 근로장학금
	private String D47;		//R11 - 직무발명보상금
	private String D48;		//S01 - 주식매수선택권
	private String D49;		//U01 - 벤처기업주식매수선택권
	private String D50a;	//Y02 - 우리사주조합인출금 50%
	private String D50b;	//Y03 - 우리사주조합인출금 75%
	private String D50c;	//Y04 - 우리사주조합인출금 100%
	private String D51;		//Y22 - 전공의수련 보조수당
	private String D52;		//T01 - 외국인기술자
	private String D53a;	//T10 - 중소기업취업청년 소득세 감면 100%
	private String D53b;	//T11 - 중소기업취업청년 소득세 감면 50%
	private String D53c;	//T12 - 중소기업취업청년 소득세 감면 70%
	private String D53d;	//T13 - 중소기업취업청년 소득세 감면 90%
	private String D54;		//T20 - 조세조약상 교직자 감면
	private String D55;		//공란
	private String D57;		//비과세 계
	private String D58;		//감면소득계
	
	/* 기납부세액 - 종(전)근무지  */
	private String D59a;	//소득세
	private String D59b;	//지방소득세
	private String D59c;	//농특세
	private String D60;		//종(전)근무처 일련번호
	private String D61;		//공란
	
}
