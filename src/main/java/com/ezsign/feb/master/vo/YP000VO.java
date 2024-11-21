package com.ezsign.feb.master.vo;

import lombok.Data;

@Data
public class YP000VO {

	 // 근로자기본설정(급여포함, 확정기능)
    private String code;
    private String message;
    private String dbMode;

    private String bizId;
    private String febYear;
    private String 계약ID;
    private String 사용자ID;
    private String 근무시기;
    private String 진행상태코드;
    private String 사용여부;
    private String 사업장명;
    private String 개인식별번호;
    private String 부서ID;
    private String 부서명;
    private String 부서표시여부;
    private String 사내내선번호;
    private String 내외국인구분;
    private String 국가코드;
    private String 국적;
    private String 거주구분;
    private String 거주지국코드;
    private String 거주지국;
    private String 외국법인파견근로자;
    private String 외국인단일세율적용;
    private String 국외근로제공여부;
    private String 중소기업취업감면여부;
    private String 감면기간_FROM;
    private String 감면기간_TO;
    private String 감면율;
    private String 생산직등야간근로비과세;
    private String 전년도총급여;
    private String 세대주여부;
    private String 소득세적용률;
    private String 연말정산분납여부;
    private String 종전근무지_납세조합유무;
    private String 등록일시;
    private String 수정일시;
    private String 연말대상;
    private String 원천징수영수증_관리번호;

    // 인사정보
    private String 사업장ID;
    private String empNo;
    private String empName;
    private String deptCode;
    private String deptName;
    private String phoneNum;
    private String positionName;
    private String eMail;
    private String 국세청PDF유형;
    private String joinDate;
    private String leaveDate;
    private String userPwd;
    private String address1;
    private String address2;
    private String 나이;
    private String empType;
    private String zipCode;

    // 기업 통계
    private String 총건수;
    private String 진행중_건수;
    private String 근로자확정_건수;
    private String 관리자1차확정_건수;
    private String 관리자최종확정_건수;

    // 사용자 총급여/근로소득금액
    private String 총급여;
    private String 근로소득금액;
    // 사용자 근무년월
    private String M1;
    private String M2;
    private String M3;
    private String M4;
    private String M5;
    private String M6;
    private String M7;
    private String M8;
    private String M9;
    private String M10;
    private String M11;
    private String M12;

    // 원천명세(급여항목)
    private String 원천명세ID;
    private String 급여;
    private String 상여;
    private String 인정상여;
    private String 주식매수선택권행사이익;
    private String 우리사주조합인출금;
    private String 임원퇴직소득금액한도초과액;
    private String 소득세;
    private String 지방소득세;
    private String 농어촌특별세;
    private String 건강보험료;
    private String 장기요양보험료;
    private String 국민연금보험료;
    private String 고용보험료;
    private String 공무원연금;
    private String 군인연금;
    private String 사립학교교직원연금;
    private String 별정우체국연금;
    private String 납부특례세액_소득세;
    private String 납부특례세액_지방소득세;
    private String 납부특례세액_농어촌특별세;
    private String M01;
    private String M02;
    private String M03;
    private String O01;
    private String Q01;
    private String H08;
    private String H09;
    private String H10;
    private String G01;
    private String H11;
    private String H12;
    private String H13;
    private String H01;
    private String K01;
    private String S01;
    private String T01;
    private String Y02;
    private String Y03;
    private String Y04;
    private String H05;
    private String I01;
    private String R10;
    private String H14;
    private String H15;
    private String T10;
    private String T11;
    private String T12;
    private String T20;
    private String H16;
    private String R11;
    private String H06;
    private String H07;
    private String Y22;
    private String 비과세한도초과액;
	private String 차량비과세;

    // 근로자_종(전)근무지
    private String 일련번호;
    private String 근무지구분;
    private String 회사명;
    private String 사업자등록번호;
    private String 근무시작일;
    private String 근무종료일;
    private String 감면시작일;
    private String 감면종료일;

    // 작업자정보
    private String 작업자ID;

    private String 근무상태;

    // 연말정산 pdf
    private String 처리여부;
    
    // 2018년 추가 항목
 	private String 직무발명보상금;
 	private String T13;
 	private String H17;
 	private String U01;
 	
 	// 추가제출서류 일련번호
 	private String 추가제출서류번호;

    private String sortName;
    private String sortOrder;
    private String searchKey;
    private String searchValue;
    private int startPage;
    private int endPage;
    
    private String businessNo;
    
    //연말정산 - 엑셀    
    private String 국외근로_월_100만원;
    private String 국외근로_월_300만원;
    private String 국외근로_전액;
    
    private String 야간근로수당_년_240만원;
    private String 출산보육수당_월_10만원;
    private String 특별법_연구보조비_월_20만원;
    private String 연구기관_등_연구보조비_월_20만원;
    private String 기업부설연구소_연구보조비_월_20만원;
    private String 비과세학자금_납입금액;
    private String 취재수당_월_20만원;
    private String 벽지수당_월_20만원;
    private String 재해관련급여_전액;
    private String 무보수위원수당_전액;
    private String 외국주둔군인등_전액;
    private String 주식매수선택권_년_3000만원;
    private String 외국인기술자_년_근로소득세의_50퍼센트_한도;
    private String 우리사주조합인출금50퍼센트_년_인출금의_50퍼센트_한도;
    private String 우리사주조합인출금75퍼센트_년_인출금의_75퍼센트_한도;
    private String 우리사주조합인출금100퍼센트_년_인출금의_100퍼센트_한도;
    private String 경호_승선수당;
    private String 외국정부등근무자_전액;
    private String 근로장학금_전액;
    private String 보육교사_근무환경개선비_전액;
    private String 사립유치원수석교사_교사의_인건비_전액;
    private String 중소기업취업청년_소득세_감면100퍼센트_소득세의_100퍼센트;
    private String 중소기업취업청년_소득세_감면50퍼센트_소득세의_50퍼센트;
    private String 중소기업취업청년_소득세_감면70퍼센트_소득세의_70퍼센트;
    private String 중소기업취업청년_소득세_감면90퍼센트_소득세의_90퍼센트;
    private String 조세조약상_교직자감면_전액;
    private String 정부_공공기관지방이전기관_종사자_이주수당_월_20만원;
    private String 종교활동비;
    private String 벤처기업_주식매수선택권_년_2000만원;
    private String 직무발명보상금_년_300만원;
    private String 유아_초중등_연구보조비_월_20만원;
    private String 고등교육법_연구보조비_월_20만원;
    private String 전공의_수련_보조_수당;
    
}
