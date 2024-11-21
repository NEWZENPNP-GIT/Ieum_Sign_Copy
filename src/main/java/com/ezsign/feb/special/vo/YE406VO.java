package com.ezsign.feb.special.vo;

import lombok.Data;

@Data
public class YE406VO {
    // 세액감면

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private String 일련번호;

    private String 소득세법_감면기간_FROM;
    private String 소득세업_감면기간_TO;
    private int 감면대상급여;
    private String T01_감면기간_FROM;
    private String T01_감면기간_TO;
    private int T01_감면대상급여;
    private String T10_감면기간_FROM;
    private String T10_감면기간_TO;
    private int T10_감면기간_감면대상급여;
    private String T11_감면기간_FROM;
    private String T11_감면기간_TO;
    private int T11_감면기간_감면대상급여;
    private String T12_감면기간_FROM;
    private String T12_감면기간_TO;
    private int T12_감면기간_감면대상급여;
    private String T13_감면기간_FROM;
    private String T13_감면기간_TO;
    private int T13_감면기간_감면대상급여;
    private String T20_감면기간_FROM;
    private String T20_감면기간_TO;
    private int T20_감면대상급여;

    private String 최종저장여부;
    private String 사용여부;

    private String 등록일시;
    private String 수정일시;

    private String dbMode = "R";
}
