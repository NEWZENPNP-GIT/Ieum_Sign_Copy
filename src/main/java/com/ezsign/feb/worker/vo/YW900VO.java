package com.ezsign.feb.worker.vo;

import lombok.Data;

@Data
public class YW900VO {
	
	private String 계약ID;
	private String 제출대상구분코드;	
	private String 사업장ID;	
	private String 사업장명;	
	private String 사업자등록번호;	
	private String 단위과세자여부;	
	private String 종사업자일련번호;	
	private String 전자신고ID;	
	private String 제출년월일;	
	private String 등록일시;	
	private String 총건수;	
	private String 완료건수;	
	private String 급여;	
	private String 결정세액;
	private String 제작여부;
	
	private int	 startPage;
	private int  endPage;
	private String bizId;

}
