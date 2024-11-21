package com.ezsign.feb.special.vo;

import lombok.Data;

@Data
public class YE404VO {
    // 이월기부금
    private String code;
    private String message;

    private String bizId;
    private String febYear;

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private String 일련번호;

    private String 부양가족ID;
    private String 가족관계;
    private String 자료구분코드;
    private String 자료구분코드명;
    private String 기부코드;
    private String 기부금종류;
    private String 기부내용;
    private String 기부처_사업자등록번호;
    private String 상호;
    private int 기부명세_건수;
    private int 기부명세_공제금액;
    private int 기부명세_공제대상기부금;
    private int 기부명세_기부장려금;
    private int 기부명세_기타;

    private String 성명;
    private String 개인식별번호;

    private String 최종저장여부;
    private String 사용여부;

    private String 등록일시;
    private String 수정일시;

    private String dbMode = "R";

    private String 사업장ID;
    private String 사업장명;
    private String empNo;
    private String empName;
    private String 부서ID;
    private String 부서명;
    private String positionName;
    private String 근무상태;

    // 합계
    private String totalCnt1; // 전체 조회 건수
    private String totalCnt2; // 사용자별 조회 건수
    private String totalAmt1; // 기부건수 합계
    private String totalAmt2; // 기부금액 합계

    // 추가제출서류 일련번호
 	private String 추가제출서류번호;
 	
    private String sortName;
    private String sortOrder;
    private int startPage;
    private int endPage;
}
