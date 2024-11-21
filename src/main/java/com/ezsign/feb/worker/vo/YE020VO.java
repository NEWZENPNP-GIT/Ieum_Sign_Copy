package com.ezsign.feb.worker.vo;

import java.util.List;

import com.ezsign.framework.vo.FileVO;

import lombok.Data;

@Data
public class YE020VO {
	
	// 추가제출서류
	private String code;
	private String message;

	private String dbMode;
	private String 계약ID;
	private String 사용자ID;
	private String 작업자ID;
	private String 부서ID;
	private String 사업장ID;
	private String 사업장명;
	private String 일련번호;
	private String 공제구분코드;
	private String 공제구분상세코드;
	private String 남기실내용;
	private String 등록된파일건수;
	private String 메모;
	private String 파일ID;
	private String 처리여부;
	private String 처리일시;
	private String 등록일시;
	private String 수정일시;
	
	private String bizId;
	private String empName;
	private String empNo;
	private String 공제구분명;
	private String 공제구분상세명;
	private String orgFileName;
	private String fileSize;

	private String positionName;
	private String 근무상태;
	
	private String sortName;
	private String sortOrder;
	private int startPage;
	private int endPage;
	
}
