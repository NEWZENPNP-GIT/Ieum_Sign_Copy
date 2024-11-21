package com.ezsign.feb.hometax.vo;

import lombok.Data;

@Data
public class GRecordVO {

	/* 자료관리번호 */
	private String G1;		//레코드구분
	private String G2;		//자료구분
	private String G3;		//세무서코드
	private String G4;		//일련번호
	
	/* 원천징수의무자 */
	private String G5;		//사업자등록번호
	
	/* 소득자*/
	private String G6;		//소득자주민등록번호
	
	//월세액 세액공제명세1
	private String G7;		//임대인 성명(상호)
	private String G8;		//주민등록번호(사업자등록번호)
	private String G9;		//유형
	private String G10;		//(월세)계약면적(㎡)
	private String G11;		//임대차계약서상 주소지
	private String G12;		//임대차계약기간 개시일
	private String G13;		//임대차계약기간 종료일
	private String G14;		//연간 월세액(원)
	private String G15;		//세액공제금액(원)
	
	//거주자간 주택임차차입금 원리금 상환액 - 금전소비대차 계약내용1
	private String G16;		//대주(貸主) 성명
	private String G17;		//대주 주민등록번호
	private String G18;		//금전 소비대차 계약기간 개시일
	private String G19;		//금전 소비대차 계약기간 종료일
	private String G20;		//차입금 이자율
	private String G21;		//원리금 상환액 계
	private String G22;		//원금
	private String G23;		//이자
	private String G24;		//공제금액
	
	//거주자간 주택임차차입금 원리금 상환액 - 임대차 계약내용1
	private String G25;		//임대인 성명(상호)
	private String G26;		//주민등록번호(사업자등록번호)
	private String G27;		//유형
	private String G28;		//(임대차)계약면적(㎡)
	private String G29;		//임대차계약서상 주소지
	private String G30;		//임대차계약기간 개시일
	private String G31;		//임대차계약기간 종료일
	private String G32;		//전세보증금(원)
	
	//월세액 세액공제명세2
	private String G33;		//임대인 성명(상호)
	private String G34;		//주민등록번호(사업자등록번호)
	private String G35;		//유형
	private String G36;		//(월세)계약면적(㎡)
	private String G37;		//임대차계약서상 주소지
	private String G38;		//임대차계약기간 개시일
	private String G39;		//임대차계약기간 종료일
	private String G40;		//연간 월세액(원)
	private String G41;		//세액공제금액(원)
	
	//거주자간 주택임차차입금 원리금 상환액 - 금전소비대차 계약내용2
	private String G42;		//대주(貸主) 성명
	private String G43;		//대주 주민등록번호
	private String G44;		//금전 소비대차 계약기간 개시일
	private String G45;		//금전 소비대차 계약기간 종료일
	private String G46;		//차입금 이자율
	private String G47;		//원리금 상환액 계
	private String G48;		//원금
	private String G49;		//이자
	private String G50;		//공제금액
	
	//거주자간 주택임차차입금 원리금 상환액 - 임대차 계약내용2
	private String G51;		//임대인 성명(상호)
	private String G52;		//주민등록번호(사업자등록번호)
	private String G53;		//유형
	private String G54;		//(임대차)계약면적(㎡)
	private String G55;		//임대차계약서상 주소지
	private String G56;		//임대차계약기간 개시일
	private String G57;		//임대차계약기간 종료일
	private String G58;		//전세보증금(원)
	
	//월세액 세액공제명세3
	private String G59;		//임대인 성명(상호)
	private String G60;		//주민등록번호(사업자등록번호)
	private String G61;		//유형
	private String G62;		//(월세)계약면적(㎡)
	private String G63;		//임대차계약서상 주소지
	private String G64;		//임대차계약기간 개시일
	private String G65;		//임대차계약기간 종료일
	private String G66;		//연간 월세액(원)
	private String G67;		//세액공제금액(원)
	
	//거주자간 주택임차차입금 원리금 상환액 - 금전소비대차 계약내용3
	private String G68;		//대주(貸主) 성명
	private String G69;		//대주 주민등록번호
	private String G70;		//금전 소비대차 계약기간 개시일
	private String G71;		//금전 소비대차 계약기간 종료일
	private String G72;		//차입금 이자율
	private String G73;		//원리금 상환액 계
	private String G74;		//원금
	private String G75;		//이자
	private String G76;		//공제금액
	
	//거주자간 주택임차차입금 원리금 상환액 - 임대차 계약내용3
	private String G77;		//임대인 성명(상호)
	private String G78;		//주민등록번호(사업자등록번호)
	private String G79;		//유형
	private String G80;		//(임대차)계약면적(㎡)
	private String G81;		//임대차계약서상 주소지
	private String G82;		//임대차계약기간 개시일
	private String G83;		//임대차계약기간 종료일
	private String G84;		//전세보증금(원)
	
	
	private String G85;		//월세액.거주자간주택임차차입금레코드 일련번호
	private String G86;		//공란
	
}
