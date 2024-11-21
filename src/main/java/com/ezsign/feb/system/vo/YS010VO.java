package com.ezsign.feb.system.vo;

import lombok.Data;

@Data
public class YS010VO {
	private String code;
	private String message;
	
	private String bizId;
	private String 계약ID;
	private String 사업장관리여부;
	private String 중간관리자여부;
	private String 국세청PDF유형코드;
	private String 사내기부금여부;
	private String 공제불가회사지원금여부;
	private String 근로자부서표시여부코드;
	private String 근로자접속기간_FROM;
	private String 근로자접속기간_TO;
	private String 관리자접속기간_FROM;
	private String 관리자접속기간_TO;
	private String 근로자데이터입력수정여부;
	private String 근로자확정기능여부;
	private String 근로자출력설정_원천징수영수증;
	private String 근로자출력설정_소득공제신고서;
	private String 근로자출력설정_의료비명세서;
	private String 근로자출력설정_기부금명세서;
	private String IP관리사용여부;
	private String dbMode;
	private String 등록일시;
	private String 수정일시;
	
	private String 사용자ID;
}
