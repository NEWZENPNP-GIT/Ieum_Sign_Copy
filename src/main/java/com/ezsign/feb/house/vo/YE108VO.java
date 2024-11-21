package com.ezsign.feb.house.vo;

import lombok.Data;

@Data
public class YE108VO {
    // 신용카드 설정

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private String 일련번호;

    private String 부양가족ID;
    private String 자료구분코드;
    private String 자료구분코드명;
    private String 기간구분코드;
    private int 신용카드;
    private int 신용카드_차감금액;
    private int 현금영수증;
    private int 현금영수증_차감금액;
    private int 직불_선불카드;
    private int 직불_선불카드_차감금액;    
    private int 도서공연;
    private int 도서공연_차감금액;
    private int 도서공연_직불카드;
    private int 도서공연_직불카드_차감금액;
    private int 도서공연_현금영수증;
    private int 도서공연_현금영수증_차감금액;
    private int 전통시장;
    private int 전통시장_차감금액;
    private int 대중교통;
    private int 대중교통_차감금액;

    private String 최종저장여부;
    private String 사용여부;

    // 추가제출서류 일련번호
 	private String 추가제출서류번호;

    private String 등록일시;
    private String 수정일시;

    private String dbMode = "R";
}
