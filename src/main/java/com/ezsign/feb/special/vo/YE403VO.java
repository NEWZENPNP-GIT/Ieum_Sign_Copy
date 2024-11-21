package com.ezsign.feb.special.vo;

import lombok.Data;

@Data
public class YE403VO {
    // 교육비

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private String 일련번호;

    private String 부양가족ID;
    private String 자료구분코드;
    private String 자료구분코드명;
    private String 공제종류코드;
    private int 공납금;
    private int 공납금_차감금액;
    private int 교복구입비;
    private int 교복구입비_차감금액;
    private int 체험학습비;
    private int 체험학습비_차감금액;

    private String 최종저장여부;
    private String 사용여부;

    // 추가제출서류 일련번호
 	private String 추가제출서류번호;
 	
    private String 등록일시;
    private String 수정일시;

    private String dbMode = "R";
}
