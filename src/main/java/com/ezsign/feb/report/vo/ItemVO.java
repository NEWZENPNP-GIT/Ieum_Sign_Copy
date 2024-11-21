package com.ezsign.feb.report.vo;

import lombok.Data;

@Data
public class ItemVO {

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
	private String 연말정산분납여부;
	private String 계약ID;
	private String 사용자ID;
	private String 부양가족ID;
	private String 가족관계_코드;
	private String 가족관계;
	private String 내외국인;
	private String 내외국인명;
	private String 가족_성명;
	private String 가족_개인식별번호;
	private String 나이;
	private String 소득확인;
	private String 기본공제;
	private String 부녀자;
	private String 한부모;
	private String 경로우대;
	private String 장애인;
	private String 자녀;
	private String 출산입양;
	private Long 국세청_보장성보험;
	private Long 국세청_장애인전용보장성보험;
	private Long 기타_보장성보험;
	private Long 기타_장애인전용보장성보험;
	private Long 국세청_의료비;
	private Long 기타_의료비;
	private Long 국세청_교육비;
	private Long 기타_교육비;
	private Long 국세청_공납금;
	private Long 기타_공납금;
	private Long 국세청_교복구입비;
	private Long 기타_교복구입비;
	private Long 국세청_체험학습비;
	private Long 기타_체험학습비;
	private Long 국세청_신용카드;
	private Long 기타_신용카드;
	private Long 국세청_직불_선불카드;
	private Long 기타_직불_선불카드;
	private Long 국세청_현금영수증;
	private Long 기타_현금영수증;
	private Long 국세청_전통시장;
	private Long 기타_전통시장;
	private Long 국세청_대중교통;
	private Long 기타_대중교통;
	private Long 국세청_도서공연;
	private Long 기타_도서공연;
	private Long 국세청_기부금;
	private Long 기타_기부금;
	private Long 기타_건강고용등;
	private String 근무상태;
}
