package com.ezsign.payment.vo;

import lombok.Data;

@Data
public class PaymentVO {

	private String paySeq;
	private String bizId;
	private String userId;
	private int    point;
	private String PStateCd;   // 거래상태 : 0021(성공), 0031(실패), 0051(입금대기중)
	private String PTrno;    // 거래번호
	private String PType;     // 거래종류 (CARD, BANK)
	private String PMid;      // 회원사아이디
	private String POid;      // 주문번호
	private String PUname;    // 주문자명
	private String PMName;		// 상점한글
	private String PGoods;		// 상품명
	private String PFnCd1;   // 금융사코드1 (은행코드, 카드코드)
	private String PFnCd2;  // 금융사코드2 (은행코드, 카드코드)
	private String PFnNm;     // 금융사명 (은행명, 카드사명)
	private String PAmt;      // 거래금액
	private String PAuthDt;  // 승인시간
	private String PAuthNo;  // 승인번호
	private String PRmesg1;   // 메시지1
	private String PRmesg2;   // 메시지2
	private String PNoti;     // 주문정보
	private String PHash;     // NOTI HASH 코드값
	private String insDate;
	
}
