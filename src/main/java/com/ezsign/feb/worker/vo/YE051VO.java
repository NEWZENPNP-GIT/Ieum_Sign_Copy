package com.ezsign.feb.worker.vo;

import lombok.Data;

@Data
public class YE051VO {
    // 연금보험료 설정

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private String 일련번호;

    private String 보험료구분;
    private int 국세청_납입금액 = 0;
    private int 국세청_차감금액 = 0;
    private int 추가납입금액 = 0;

    private int 주근무지 = 0;
    private int 종근무지 = 0;

    private String 최종저장여부;
    private String 사용여부;

    private String 등록일시;
    private String 수정일시;

    private String dbMode = "R";
}
