package com.ezsign.feb.other.vo;

import lombok.Data;

@Data
public class YE203VO {
    // 우리사주조합출연금 설정

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private String 일련번호;

    private String 구분코드;
    private String 구분명;
    private int 납입금액;

    private String 최종저장여부;
    private String 사용여부;

    private String 등록일시;
    private String 수정일시;

    private String dbMode = "R";
}
