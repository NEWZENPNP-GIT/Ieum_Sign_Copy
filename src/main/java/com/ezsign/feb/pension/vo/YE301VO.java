package com.ezsign.feb.pension.vo;

import lombok.Data;

@Data
public class YE301VO {
    // 퇴직연금계좌 설정

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private String 일련번호;

    private String 자료구분코드;
    private String 자료구분코드명;
    private String 퇴직연금구분코드;
    private String 금융회사등명칭;
    private String 계좌번호_증권번호;
    private int 납입금액;
    private int 차감금액;

    private String 최종저장여부;
    private String 사용여부;

    private String 등록일시;
    private String 수정일시;

    private String dbMode = "R";
}
