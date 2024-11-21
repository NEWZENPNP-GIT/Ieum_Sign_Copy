package com.ezsign.feb.hometax.vo;

import lombok.Data;

@Data
public class CRecordVO {

	/* 자료관리번호 */
	private String C1;		//레코드구분
	private String C2;		//자료구분
	private String C3;		//세무서코드
	private String C4;		//일련번호
		
	/* 원천징수의무자 */
	private String C5;		//사업자등록번호
	
	/* 소득자(근로자) */
	private String C6;		//종(전)근무처 수
	private String C7;		//거주지구분코드
	private String C8;		//거주지국코드
	private String C9;		//외국인단일세율적용
	private String C10;		//외국법인소속파견근로자여부
	private String C11;		//성명
	private String C12;		//내외국인구분코드
	private String C13;		//주민등록번호
	private String C14;		//국적코드
	private String C15;		//세대주여부
	private String C16;		//연말정산구분
	private String C17;		//사업자단위과세자 여부
	private String C18;		//종사업자 일련번호
	
	/* 근무처별 소득명세 - 주(현)근무처 */
	private String C19;		//주현근무처 - 사업자등록번호
	private String C20;		//주현근무처 - 근무처명
	private String C21;		//근무기간 시작연월일
	private String C22;		//근무기간 종료연월일
	private String C23;		//감면기간 시작연월일
	private String C24;		//감면기간 종료연월일
	private String C25;		//급여
	private String C26;		//상여
	private String C27;		//인정상여
	private String C28;		//주식매수 선택권행사이익
	private String C29;		//우리사주조합 인출금
	private String C30;		//임원퇴직소득 금액한도초과액
	private String C31;		//직무발명보상금
	private String C32;		//공란
	private String C34;		//계
	
	/* 주(현)근무처 비과세소득 및 감면 소득 */
	private String C35;		//G01 - 비과세학자금
	private String C36;		//H01 - 무보수위원수당
	private String C37;		//H05 - 경호.승선수당
	private String C38;		//H06 - 유아.초중등
	private String C39;		//H07 - 고등교육법 
	private String C40;		//H08 - 특별법
	private String C41;		//H09 - 연구기관 등
	private String C42;		//H10 - 기업부설연구소		
	private String C43;		//H14 - 보육교사 근무환경개선비
	private String C44;		//H15 - 사립유치원수석교사의 인건비
	private String C45;		//H11 - 취재수당
	private String C46;		//H12 - 벽지수당
	private String C47;		//H13 - 재해관련급여
	private String C48;		//H16 - 정부공공기관지방이전기관 종사자 이주수당
	private String C49;		//H17 - 종교활동비
	private String C50;		//I01 - 외국정보등근무자
	private String C51;		//K01 - 외국주둔군인등
	private String C52;		//M01 - 국외근로 100만원
	private String C53;		//M02 - 국외근로 300만원
	private String C54;		//M03 - 국외근로
	private String C55;		//O01 - 야간근로수당
	private String C56;		//Q01 - 출산보육수당
	private String C57;		//R10 - 근로장학금
	private String C58;		//R11 - 직무발명보상금		
	private String C59;		//S01 - 주식매수선택권
	private String C60;		//U01 - 벤처기업 주식매수선택권
	private String C61a;	//Y02 - 우리사주조합인출금 50%
	private String C61b;	//Y03 - 우리사주조합인출금 75%
	private String C61c;	//Y04 - 우리사주조합인출금 100%
	private String C62;		//Y22 - 전공의 수련 보조수당
	private String C63;		//T01 - 외국인기술자
	private String C64a;	//T10 - 중소기업취업 청년 소득세 감면 100%
	private String C64b;	//T11 - 중소기업취업 청년 소득세 감면 50%
	private String C64c;	//T12 - 중소기업취업 청년 소득세 감면 70%
	private String C64d;	//T13 - 중소기업취업 청년 소득세 감면 90%
	private String C65;		//T20 - 조세조약상 교직자 감면
	private String C66;		//공란 - 숫자 0으로 20자리를 반드시 채울 
	private String C68;		//비과세 계
	private String C69;		//감면소득 계
	
	/* 정산명세 */
	private String C70;		//총급여
	private String C71;		//근로소득공제
	private String C72;		//근로소득금액
	
	/* 기본공제 */
	private String C73;		//본인공제금액
	private String C74;		//배우자공제금액
	private String C75a;	//부양가족공제인원
	private String C75b;	//부양가족공제금액
	
	/* 추가공제 */
	private String C76a;	//경로우대공제인원
	private String C76b;	//경로우대공제금액
	private String C77a;	//장애인공제인원
	private String C77b;	//장애인공제금액
	private String C78;		//부녀자공제금액
	private String C79;		//한부모가족공제금액
	
	/* 연금보험료 공제 */
	private String C80a;	//국민연금보험료공제 대상금액
	private String C80b;	//국민연금보험료공제 공제금액
	
	
	private String C81a;	//공적연금보험료공제 공무원연금 대상금액
	private String C81b;	//공적연금보험료공제 공무원연금 공제금액
	private String C82a;	//공적연금보험료공제 군인연금 대상금액
	private String C82b;	//공적연금보험료공제 군인연금 공제금액
	private String C83a;	//공적연금보험료공제 사립학교교직원연금 대상금액
	private String C83b;	//공적연금보험료공제 사립학교교직원연금 공제금액
	private String C84a;	//공적연금보험료공제 별정우체국연금 대상금액
	private String C84b;	//공적연금보험료공제 별정우체국연금 공제금액
	
	/* 특별소득공제 */
	private String C85a;	//보험료 건강보험료(노인장기요양보험료포함) 대상금액
	private String C85b;	//보험료 건강보험료(노인장기요양보험료포함) 공제금액
	private String C86a;	//보험료 고용보험료 대상금액
	private String C86b;	//보험료 고용보험료 공제금액
	
	/* 주택자금(주택임차차입금, 장기주택저당차입금, 주택마련저축) 관련 공통 사항 */
	private String C87a;	//주택자금 주택임차차입금 원리금상환액 대출기관
	private String C87b;	//주택자금 주택임차차입금 원리금상환액 거주자
	private String C88a;	//(2011년이전 차입분) 주택자금 장기주택저당차입금 이자상환액 15년미만
	private String C88b;	//(2011년이전 차입분) 주택자금 장기주택저당차입금 이자상환액 15년~29년
	private String C88c;	//(2011년이전 차입분) 주택자금 장기주택저당차입금 이자상환액 30년이상
	private String C89a;	//(2012년이후 차입분, 15년이상) 고정금리.비거치식 상환대출
	private String C89b;	//(2012년이후 차입분, 15년이상) 기타대출
	private String C90a;	//(2015년이후 차입분, 상환기간 15년이상) 고정금리 and 비거치상환 대출
	private String C90b;	//(2015년이후 차입분, 상환기간 15년이상) 고정금리 or 비거치상환 대출
	private String C90c;	//(2015년이후 차입분, 상환기간 15년이상) 그 밖의 대출
	private String C90d;	//(2015년이후 차입분, 상환기간 10년이상) 고정금리 or 비거치상환 대출
	
	private String C91;		//기부금(이월분)
	private String C92;		//공란 (숫자 0으로 22자리를 반드시 채움)
	private String C94;		//계
	private String C95;		//차감소득금액
	
	/* 그 밖의 소득공제 */
	private String C96;		//개인연금저축 소득공제
	private String C97;		//소기업.소상공인 공제부금
	private String C98;		//주택마련저축소득공제 청약저축
	private String C99;		//주택마련저축소득공제 주택청약종합저축
	private String C100;	//주택마련저축소득공제 근로자주택마련저축		
	private String C101;	//투자조합출자등 소득공제
	private String C102;	//신용카드등 소득공제
	private String C103;	//우리사주조합 출연금
	private String C104;	//고용유지중소기업 근로자소득공제
	private String C105;	//장기집합투자증권저축
	private String C106;	//공란
	private String C108;	//그 밖의 소득공제계
	private String C109;	//소득 공제종합 한도초과액
	private String C110;	//종합소득 과세표준	
	private String C111;	//산출세액
	
	/* 세액감면 */
	private String C112;	//소득세법
	private String C113;	//조특법 제30조 제외
	private String C114;	//조특법 제30조
	private String C115;	//조세조약
	private String C116;	//공란 (숫자 0으로 20자리를 반드시 채움)
	private String C118;	//세액감면계
	
	/* 세액공제 */
	private String C119;	//근로소득세액공제
	private String C120a;	//자녀세액공제인원
	private String C120b;	//자녀세액공제
	private String C121a;	//출산.입양 세액공제인원
	private String C121b;	//출산.입양 세액공제
	private String C122a;	//연금계좌 과학기술인공제 공제대상금액
	private String C122b;	//연금계좌 과학기술인공제 세액공제액
	private String C123a;	//연금계좌 근로자퇴직급여보장법에 따른 퇴직급여 공제대상금액
	private String C123b;	//연금계좌 근로자퇴직급여보장법에 따른 퇴직급여 세액공제액
	private String C124a;	//연금계좌 연금저축 공제대상금액
	private String C124b;	//연금계좌 연금저축 세액공제액
	private String C125a;	//특별세액공제 보장성보험료 공제대상금액
	private String C125b;	//특별세액공제 보장성보험료 세액공제액
	private String C126a;	//특별세액공제 장애인전용보장성보험료 공제대상금액
	private String C126b;	//특별세액공제 장애인전용보장성보험료 세액공제액
	private String C127a;	//특별세액공제 의료비 공제대상금액
	private String C127b;	//특별세액공제 의료비 세액공제액
	private String C128a;	//특별세액공제 교육비 공제대상금액
	private String C128b;	//특별세액공제 교육비 세액공제액
	
	/* 기부금 세액공제액  */
	private String C129a;	//특별세액공제 기부금 정치자금 10만원이하 공제대상금액
	private String C129b;	//특별세액공제 기부금 정치자금 10만원이하 세액공제액
	private String C130a;	//특별세액공제 기부금 정치자금 10만원초과 공제대상금액
	private String C130b;	//특별세액공제 기부금 정치자금 10만원초과 세액공제액
	private String C131a;	//특별세액공제 기부금 법정기부금 공제대상금액
	private String C131b;	//특별세액공제 기부금 법정기부금 세액공제액
	private String C132a;	//특별세액공제 기부금 우리사주조합기부금 공제대상금액
	private String C132b;	//특별세액공제 기부금 우리사주조합기부금 세액공제액
	private String C133a;	//특별세액공제 기부금 지정기부금 종교단체외 공제대상금액
	private String C133b;	//특별세액공제 기부금 지정기부금 종교단체외 세액공제액	
	private String C134a;	//특별세액공제 기부금 지정기부금 종교단체 공제대상금액
	private String C134b;	//특별세액공제 기부금 지정기부금 종교단체 세액공제액
	private String C135;	//공란 (숫자 0으로 22자리를 반드시 채움)	
	private String C137;	//특별세액공제계
	private String C138;	//표준세액공제
	private String C139;	//납세조합공제
	private String C140;	//주택차입금
	private String C141;	//외국납부
	private String C142a;	//월세세액공제 대상금액
	private String C142b;	//월세세액공제액
	private String C143;	//공란 (숫자 0으로 20자리를 반드시 채움)
	private String C145;	//세액공제계
	
	/* 결제세액 */
	private String C146a;	//소득세
	private String C146b;	//지방소득세
	private String C146c;	//농특세
	
	/* 기납부세액 - 주(현)근무지 */
	private String C147a;	//소득세
	private String C147b;	//지방소득세
	private String C147c;	//농특세
	
	/* 납부특례세액 */
	private String C148a;	//소득세
	private String C148b;	//지방소득세
	private String C148c;	//농특세
	
	/* 차감징수세액 */
	private String C149a1;	//소득세 - 부호
	private String C149a2;	//소득세 - 값 
	private String C149b1;	//지방소득세 - 부호
	private String C149b2;	//지방소득세 - 값
	private String C149c1;	//농특세 - 부호
	private String C149c2;	//농특세 - 값
	private String C150;	//공란
	
}
