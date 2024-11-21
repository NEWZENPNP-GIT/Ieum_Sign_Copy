package com.ezsign.window.vo;

import lombok.Data;

@Data
public class YW800VO {
	private String 계약ID;
	private String 사용자ID;
	private String 진행상태코드;
	private YW800Response 연말정산요약조회;
}
