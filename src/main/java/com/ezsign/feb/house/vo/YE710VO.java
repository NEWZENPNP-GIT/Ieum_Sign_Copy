package com.ezsign.feb.house.vo;

import lombok.Data;

@Data
public class YE710VO {
    // 주택관련소득공제신청확인서

    private String 계약ID;
    private String 사용자ID;
    private String 일련번호;

    private String 주택관련저축_입력여부;
    private String 주택관련저축_적용여부;
    private String 주택관련저축_가입저축종류;
    private String 주택관련저축_세대주여부;
    private String 주택관련저축_가입년월일;
    private String 주택관련저축_주택소유여부;
    private String 주택관련저축_보유주택규모;
    private String 주택관련저축_기준시가;
    private String 주택관련저축_제출서류_납입영수증;
    private String 주택관련저축_제출서류_주민등록표등본;
    private String 주택관련저축_제출서류_건물등기부등본;
    private String 주택관련저축_제출서류_개별공동주택가격확인서;

    private String 주택임차차입금_입력여부;
    private String 주택임차차입금_적용여부;
    private String 주택임차차입금_주택소유여부;
    private String 주택임차차입금_세대주여부;
    private String 주택임차차입금_세대주공제여부;
    private String 주택임차차입금_차입대상여부;
    private String 주택임차차입금_이자율;
    private String 주택임차차입금_임차주택규모;
    private String 주택임차차입금_임차년월일;
    private String 주택임차차입금_차입년월일;
    private String 주택임차차입금_입금자;
    private String 주택임차차입금_임대차_연장갱신;
    private String 주택임차차입금_임대차_연장갱신일;
    private String 주택임차차입금_임대차_차입일;
    private String 주택임차차입금_다른전세주택이사;
    private String 주택임차차입금_다른전세주택이사_입주전입일;
    private String 주택임차차입금_다른전세주택이사_차입일;
    private String 주택임차차입금_제출서류_은행차입_상환증명서;
    private String 주택임차차입금_제출서류_은행차입_주민등록표등본;
    private String 주택임차차입금_제출서류_은행차입_입대차계약서;
    private String 주택임차차입금_제출서류_개인차입_월세;
    private String 주택임차차입금_제출서류_개인차입_주민등록표등본;
    private String 주택임차차입금_제출서류_개인차입_임대차계약증서;
    private String 주택임차차입금_제출서류_개인차입_금전소비대차계약서사본;
    private String 주택임차차입금_제출서류_개인차입_계좌이체영수증;

    private String 장기주택저당차입금_입력여부;
    private String 장기주택저당차입금_적용여부;
    private String 장기주택저당차입금_세대주여부;
    private String 장기주택저당차입금_세대주공제여부;
    private String 장기주택저당차입금_거주여부;
    private String 장기주택저당차입금_소유권등기일;
    private String 장기주택저당차입금_차입금상환기간;
    private String 장기주택저당차입금_차입시기;
    private String 장기주택저당차입금_보유주택규모;
    private String 장기주택저당차입금_보유주택수;
    private String 장기주택저당차입금_기준시가;
    private String 장기주택저당차입금_특례경우;
    private String 장기주택저당차입금_연장전환경우;
    private String 장기주택저당차입금_제출서류_상환증명서;
    private String 장기주택저당차입금_제출서류_주민등록표등본;
    private String 장기주택저당차입금_제출서류_건물등기부등본;
    private String 장기주택저당차입금_제출서류_개별공동주택가격확인서;
    private String 장기주택저당차입금_제출서류_증액전이자계산액;

    private String 월세공제_입력여부;
    private String 월세공제_적용여부;
    private String 월세공제_주택소유여부;
    private String 월세공제_세대주여부;
    private String 월세공제_세대주공제여부;
    private String 월세공제_거주여부;
    private String 월세공제_계약자;
    private String 월세공제_주택규모;
    private String 월세공제_임차년월일;
    private String 월세공제_월세지급일;
    private String 월세공제_월임대료;
    private String 월세공제_계약요건변동여부;
    private String 월세공제_제출서류_월세;
    private String 월세공제_제출서류_주민등록표등본;
    private String 월세공제_제출서류_임대차계약서사본;
    private String 월세공제_제출서류_월세납입영수증;

    private String 주택설문지_동의여부;
    private String 주택설문지_작성년월일;
    private String 주택설문지_작성자명;

    private String 등록일시;
    private String 수정일시;

    private String dbMode = "R";
}
