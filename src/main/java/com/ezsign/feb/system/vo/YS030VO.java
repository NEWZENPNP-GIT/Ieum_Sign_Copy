package com.ezsign.feb.system.vo;

import lombok.Data;

@Data
public class YS030VO {
	private String code;
	private String message;
	
	private String 계약ID;
	private String 사업장ID;
	private String 지점여부;
	private String 사업장명;
	private String 대표자명;
	private String 사업자등록번호;
	private String 법인등록번호_개인식별번호;
	private String 회사주소1;
	private String 회사전화1;
	private String 회사팩스1;
	private String 회사주소2;
	private String 회사전화2;
	private String 회사팩스2;
	private String 단위과세자여부;
	private String 종사업자일련번호;
	private String 관할세무서코드;
	private String 지방소득세납세지;
	private String 홈택스아이디;
	private String 제출담당부서명;
	private String 제출담당자성명;
	private String 제출담당자전화번호;
	private String 사용여부;
	private String dbMode;
	private String bizType;
	private String 등록일시;
	private String 수정일시;

	private String bizId;
	private String 계약년도;
	private String 대상인원수;
	
	// 전자신고 대상 유무
	private String 신고대상;
	
	private String sortName;
	private String sortOrder;
	private int startPage = 0;
	private int endPage = 1;
}
