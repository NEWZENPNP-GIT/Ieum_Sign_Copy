package com.ezsign.feb.special.vo;

import lombok.Data;

@SuppressWarnings("NonAsciiCharacters")
@Data
public class YE407VO {

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private int 일련번호;
    private String 세액공제구분코드;

    private String 기부코드;
    private String 기부년도;
    private String 구분명;
    private int 지출액;
    private int 공제대상금액;
    private int 공제율1;
    private int 공제율2;
    private int 공제액;
    private int 공제초과이월액;
    private int 소멸금액;
    private int 세액공제가능액;

    private String 등록일시;
    private String 수정일시;
}
