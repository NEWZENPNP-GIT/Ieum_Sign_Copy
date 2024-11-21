package com.ezsign.feb.worker.vo;

import java.util.List;

import lombok.Data;

@Data
public class YP040VO {

	// 소득명세합계
	private String code;
	private String message;

	private String bizId;
	private String febYear;
	private String empName;
	private String empNo;
	private String 계약ID;
	private String 사용자ID;
	private String 근무시기;		
	private String 작업자ID;
	private String 사업장ID;
	private String 사업장명;
	private String 부서ID;
	private String 부서명;
	private String 일련번호;
	private String 원천명세ID;
	private String 총급여;
	private String 비과세합계;
	private String 감면소득합계;
	private String 공제합계;
	private String 소득세;
	private String 지방소득세;
	private String 농어촌특별세;
	private String 근로소득금액;
	private String 기납부세액합계;
	private String dbMode;
	private String 근무상태;
	private String 등록일시;
	private String 수정일시;

	private String 성명;

	private String 근무지구분;
	private String 회사명;
	private String 사업자등록번호;
	private String 근무시작일;
	private String 근무종료일;
	private String 감면시작일;
	private String 감면종료일;

	// 원천명세(급여항목)	
	private String 급여;
	private String 상여;
	private String 인정상여;
	private String 주식매수선택권행사이익;
	private String 우리사주조합인출금;
	private String 임원퇴직소득금액한도초과액;
	private String 건강보험료;
	private String 장기요양보험료;
	private String 국민연금보험료;
	private String 고용보험료;
	private String 공무원연금;
	private String 군인연금;
	private String 사립학교교직원연금;
	private String 별정우체국연금;
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
	private String 비과세한도초과액;
	
	// 2018년 추가 항목
	private String 직무발명보상금;
	private String T13;
	private String H17;
	private String U01;
	
	private String sortName;
    private String sortOrder;
    private int startPage;
    private int endPage;
    
    private List<YP040VO> 근무지별소득명세금액;
    private int 근무지별소득명세금액개수;
    
    private String 외국인단일세율적용;
	    
}
