package com.ezsign.feb.hometax.vo;

import lombok.Data;

/**
 * 
 * 전자신고파일_A레코드
 *
 */
@Data
public class YE801VO {

	private String 계약ID;
	private String 전자신고ID;
	private String 번호;
	private String 처리값;	
	private String 등록일시;
	
}
