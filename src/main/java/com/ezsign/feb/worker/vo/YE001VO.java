package com.ezsign.feb.worker.vo;

import java.util.List;

import lombok.Data;

@Data
public class YE001VO {
	// 부양가족관리
	private String code;
	private String message;

	private String dbMode = "R";
	private String bizId;
	private String febYear;
	private String 계약ID;
	private String 사용자ID;
	private String 부양가족ID;
	private String 가족관계;
	private String 내외국인;
	private String 성명;
	private String 개인식별번호;
	private String 나이;
	private String 기본공제;
	private String 부녀자;
	private String 한부모;
	private String 경로우대;
	private String 장애인;
	private String 자녀;
	private String 교육비공제;
	private String 출산입양;
	private String 등록일시;
	private String 수정일시;
	private String resId;
	
	private String 사업장명;
	private String 사업장ID;
	private String empNo;
	private String empName;
	private String 부서ID;
	private String 부서명;
	private String positionName;
	private String 근무상태;
	
	private String sortName;
	private String sortOrder;
	private int startPage;
	private int endPage;
	
	private List<YE001VO> ye001SubList;
	
	private String 추가제출서류번호;
	private String 소득확인;
	
}
