package com.ezsign.feb.report.vo;

import com.ezsign.feb.special.vo.YE408VO;

import lombok.Data;

@Data
public class YE408ReportVO {
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
    private Long 기부금액;
    private Long 전년도까지공제금액;
    private Long 공제대상기부금;
    private Long 공제대상계산기부금;
    private Long 해당연도공제금액;
    private Long 이월금액;
    private Long 소멸금액;
    private boolean finishYn;

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

    /* 현황물 리포트 */
	private String bizName;
	private String 사번;
	private String 성명;
	private String 직책;
	private String 개인식별번호;
	private String 기부금종류코드명;
	private Long 공제액;
}
