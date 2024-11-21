package com.ezsign.feb.special.vo;

import lombok.Data;

@Data
public class YE405VO {
    // 이월기부금
    private String code;
    private String message;

    private String bizId;
    private String febYear;

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private String 일련번호;

    private String 기부금종류코드;
    private String 기부금종류;
    private String 기부년도;
    private String 기부금액;
    private int 전년도까지공제금액;
    private int 공제대상기부금;
    private int 해당연도공제금액;
    private int 이월금액;
    private int 소멸금액;

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
    private String totalAmt1; // 기부금액 합계
    private String totalAmt2; // 전년도까지 공제금액 합계
    private String totalAmt3; // 공제대상 금액 합계

    private String sortName;
    private String sortOrder;
    private int startPage;
    private int endPage;
}
