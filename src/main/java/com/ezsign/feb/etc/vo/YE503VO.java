package com.ezsign.feb.etc.vo;

import lombok.Data;

@Data
public class YE503VO {
    // 외국납부세액공제

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private String 일련번호;

    private int 국외총급여;
    private int 납세액_외화;
    private int 납세액_원화;
    private int 세액공제금액;
    private String 납세국가코드;
    private String 납부일;

    private String 최종저장여부;
    private String 사용여부;

    private String 등록일시;
    private String 수정일시;

    private String dbMode = "R";
}
