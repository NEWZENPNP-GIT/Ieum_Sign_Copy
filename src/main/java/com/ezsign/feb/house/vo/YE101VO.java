package com.ezsign.feb.house.vo;

import lombok.Data;

@Data
public class YE101VO {
    // 주택임차차입금원리금상환액 설정

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private String 일련번호;

    private String 차입구분;
    private String 취급기관;
    private int 국세청자료;
    private int 차감금액;
    private int 기타자료;

    private String 최종저장여부;
    private String 사용여부;

    private String 등록일시;
    private String 수정일시;

    private String dbMode = "R";
    
    /* 현황물 리포트 */
	private String bizId;
	private String bizName;
	private String 사업장ID;
	private String 사업장명;
	private String 부서ID;
	private String 부서명;
	private String 사번;
	private String 성명;
	private String 직책;
	private String 개인식별번호;
	private Long 대출기관;
	private Long 거주자;
	
	private String 근무상태;
}
