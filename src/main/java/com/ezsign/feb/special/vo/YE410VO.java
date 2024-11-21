package com.ezsign.feb.special.vo;

import lombok.Data;

@SuppressWarnings("NonAsciiCharacters")
@Data
public class YE410VO {

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private int 일련번호;

    private String 구분명;
    private int 대상금액;
    private String 공제율;
    private int 공제율금액;
    
    private String 등록일시;
    private String 수정일시;
}
