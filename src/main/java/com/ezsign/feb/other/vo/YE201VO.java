package com.ezsign.feb.other.vo;

import lombok.Data;

@Data
public class YE201VO {
    // 소기업.소상공인 공제부금 설정

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private String 일련번호;

    private int 국세청_납입금액;
    private int 국세청_차감금액;
    private int 기타_납입금액;

    private String 최종저장여부;
    private String 사용여부;

    private String 등록일시;
    private String 수정일시;

    private String dbMode = "R";
}
