package com.ezsign.feb.worker.vo;

import lombok.Data;

@Data
public class YE003VO {
	// 원천명세(급여정보)
	private String code;
	private String message;
	
	private String bizId;
	private String febYear;
	private String 계약ID;
	private String 사용자ID;
	private String 원천명세ID;
	private String 급여;
	private String 상여;
	private String 인정상여;
	private String 주식매수선택권행사이익;
	private String 우리사주조합인출금;
	private String 임원퇴직소득금액한도초과액;
	private String 소득세;
	private String 지방소득세;
	private String 농어촌특별세;
	private String 건강보험료;
	private String 장기요양보험료;
	private String 국민연금보험료;
	private String 고용보험료;
	private String 공무원연금;
	private String 군인연금;
	private String 사립학교교직원연금;
	private String 별정우체국연금;
	private String 납부특례세액_소득세;
	private String 납부특례세액_지방소득세;
	private String 납부특례세액_농어촌특별세;
	private String M01;
	private String M02;
	private String M03;
	private String O01;
	private String Q01;
	private String H08;
	private String H09;
	private String H10;
	private String G01;
	private String H11;
	private String H12;
	private String H13;
	private String H01;
	private String K01;
	private String S01;
	private String T01;
	private String Y02;
	private String Y03;
	private String Y04;
	private String H05;
	private String I01;
	private String R10;
	private String H14;
	private String H15;
	private String T10;
	private String T11;
	private String T12;
	private String T20;
	private String H16;
	private String R11;
	private String H06;
	private String H07;
	private String Y22;
	private String dbMode;
	private String 등록일시;
	private String 수정일시;
	
	// 2018년 추가 항목
	private String 직무발명보상금;
	private String T13;
	private String H17;
	private String U01;
	private String 차량비과세;
	
	
	/* 현황물 리포트 */
	private String bizName;
	private String 사업장ID;
	private String 사업장명;
	private String 부서ID;
	private String 부서명;
	private String 사번;
	private String 성명;
	private String 직책;
	private String 개인식별번호;
	private String 연말정산분납여부;
	private String 근무지구분;
	private String 근무지구분명;
	private String 회사명;
	private String 사업자등록번호;
	private String 귀속년도시작일;
	private String 귀속년도종료일;
	private String 감면시작일;
	private String 감면종료일;
	private Long 공적연금보험료	;
	private Long 연금계좌;

	private String 근무상태; 
}
