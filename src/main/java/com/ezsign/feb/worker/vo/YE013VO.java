package com.ezsign.feb.worker.vo;

import lombok.Data;

@SuppressWarnings("NonAsciiCharacters")
@Data
public class YE013VO {
    // 이월기부금
    private String code;
    private String message;

    private String bizId;
    private String febYear;
    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private String 부양가족ID;
    private String 부양가족명;
    private String 일련번호;
    private String 공제구분코드;
    private String 공제구분명;
    private String 공제상세코드;
    private String 공제상세명;
    private int	   공제금액;
    private String 의료증빙코드;
    private String 의료증빙명;
    private String 의료비유형;
    private int	   건수;
    private String 상호;
    private String 사업자등록번호;
    private String dbMode;
    private String 등록일시;
    private String 수정일시;

    private String 사업장명;
    private String 사업장ID;
    private String empNo;
    private String empName;
    private String 부서ID;
    private String 부서명;
    private String positionName;
    private String leaveDate;
    private String 개인식별번호;
    
    private String 근무상태;

    // 합계
    private String totalCnt1;    // 전체 조회 건수
    private String totalCnt2;    // 사용자별 조회 건수
    private String totalAmt1;    // 회사지원금(공제금액) 합계

    private String sortName;
    private String sortOrder;
    private int startPage;
    private int endPage;


}
