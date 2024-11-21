package com.ezsign.feb.report.vo;

import lombok.Data;

@Data
public class AnnuitySavingVO {

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
	private String 계약ID; 
	private String 사용자ID; 
	private String 자료구분코드; 
	private String 공제구분; 
	private String 항목구분코드; 
	private String 항목구분; 
	private String 금융회사등명칭코드; 
	private String 금융회사등명칭; 
	private String 계좌번호_증권번호; 
	private Long 납입금액; 
	private Long 차감금액;
	private Long 합계;
	private String 근무상태;
}
