package com.ezsign.feb.special.vo;

import lombok.Data;

@SuppressWarnings("NonAsciiCharacters")
@Data
public class YE411VO {

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;

    private int 공제제외금액;
    private int 공제가능금액;
    private int 공제한도;
    private int 일반공제금액;
    private int 추가공제금액;
    private int 최종공제금액;

    private String 등록일시;
    private String 수정일시;
}

