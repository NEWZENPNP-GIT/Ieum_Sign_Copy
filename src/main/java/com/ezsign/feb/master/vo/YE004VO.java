package com.ezsign.feb.master.vo;

import lombok.Data;

@Data
public class YE004VO {
	// pdf upload 결과
	private String 계약ID;
	private String 사용자ID;

	private String 처리여부;
	private String 처리내용;
	private String 국세청PDF유형;

	private String 등록일시;
	private String 수정일시;
}
