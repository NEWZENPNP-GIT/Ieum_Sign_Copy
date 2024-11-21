package com.ezsign.feb.report.vo;

import com.ezsign.feb.house.vo.YE105VO;

import lombok.Data;

@Data
public class YE105ReportVO {
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
    private Long 연간_월세액;
    private Long 공제대상금액;

    private String 최종저장여부;
    private String 사용여부;
    
    // 추가제출서류 일련번호
 	private String 추가제출서류번호;

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
	private String 임대인_개인식별번호; 
	private String 유형코드명;
	private String 근무상태;
}
