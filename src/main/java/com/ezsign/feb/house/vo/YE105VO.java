package com.ezsign.feb.house.vo;

import lombok.Data;

@Data
public class YE105VO {
    // 월세액공제 설정

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private String 일련번호;

    private String 임대인성명_상호;
    private String 개인식별번호;
    private String 유형코드;
    private String 계약면적;
    private String 임대차_주소지_물건지;
    private String 임대차_계약개시일;
    private String 임대차_계약종료일;
    private int 연간_월세액;
    private int 공제대상금액;
    private int 세액공제액;

    private String 최종저장여부;
    private String 사용여부;
    
    // 추가제출서류 일련번호
 	private String 추가제출서류번호;

    private String 등록일시;
    private String 수정일시;

    private String dbMode = "R";
}
