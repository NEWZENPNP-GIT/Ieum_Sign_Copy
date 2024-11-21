package com.ezsign.feb.system.vo;

import lombok.Data;

@Data
public class YS040VO {
	private String code;
	private String message;
	
	private String 계약ID;
	private String 급여숨기기;
	private String 최종확정기능;
	private String 근로자출력설정_원천징수영수증;
	private String 근로자출력설정_소득공제신고서;
	private String 근로자출력설정_의료비명세서;
	private String 근로자출력설정_기부금명세서;
	private String dbMode;
	private String 등록일시;
	private String 수정일시;

	private String bizId;
	private String 계약년도;
	
	private String sortName;
	private String sortOrder;
	private int startPage;
	private int endPage;
	
}
