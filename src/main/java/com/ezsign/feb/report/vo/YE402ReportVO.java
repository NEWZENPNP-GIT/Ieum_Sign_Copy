package com.ezsign.feb.report.vo;

import com.ezsign.feb.special.vo.YE402VO;

import lombok.Data;

@SuppressWarnings("NonAsciiCharacters")
@Data
public class YE402ReportVO {

    // 의료비 (국세청 자료/기타자료)

    private String 계약ID;
    private String 사용자ID;
    private String 작업자ID;
    private String 일련번호;

    private String 부양가족ID;
    private String 자료구분코드;
    private String 자료구분코드명;
    private String 의료비증빙코드;
    private String 공제종류코드;
    private String 지급처_사업자등록번호;
    private String 상호;
    private long 건수;
    private String 의료비유형;
    private long 지출액;
    private long 차감금액;

    private String 성명;
    private String 개인식별번호;

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
	private String 직책; 
	private String 가족관계; 
	private String 가족관계명; 
	private String 가족_개인식별번호; 
	private String 가족명;  
	private String 본인_장애인_65세이상;  
	private String 의료비증빙코드명;    
	private long 합계; 
	private String 난임시술비해당여부;
	private String 근무상태;
}
