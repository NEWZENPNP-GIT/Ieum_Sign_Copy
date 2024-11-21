package com.ezsign.biz.vo;

import lombok.Data;

@Data
public class BizVO {
	
	private String code;
	private String message;	

	private String serviceId;
	private String serviceName;
	private String serviceType;
	private String bizId;
	private String businessNo;
	private String bizStatus;
	private String bizName;
	private String phoneNum;
	private String ownerName;
	private String addr1;
	private String addr2;
	private String companyTelNum;
	private String companyFaxNum;
	private String companyImage;
	private String companyLogo;
	private String insDate;
	private String updDate;
	private String bizType;

	private String febYear;
	private int    curPoint;
	private int    bizGrpCount;
	
	private String searchKey;
	private String searchValue;
	private int	 startPage;
	private int  endPage;
	private String sortName;
	private String sortOrder;
	
	private String pinNo;
	
	private String formPoint;
	private String scanPoint;
	private String sendPoint;
	private String resendPoint;
	private String useContract;
	
	
	private String personUserName;
	private String personUserTelNum;
	private String personEMail;
	private String openVoucherType;
	private String voucherOrder;
	private String salesChannel;
	private String otherGoodsPurchase;
	private String contractType;
	private String contractStartDate;
	private String contractEndDate;
	private String generalTransDate;
	
	private String loginId;
	private String userPwd;
	private String channelId;
	private String userId;
	private String changeUserId;
	private String funnel; // 유입경로 추가(21.11.02 이두호)
	private String requestId; // tbl_app_rqust PK 추가(21.11.02 이두호)
	private String useElecDoc; // 전자문서사용여부 추가 (21.12.06 이두호)
	private String usePayStub; // 급여명세관리사용여부 추가 (21.12.06 이두호)
	private String comment; // 유입경로 변경시 기본포인트 지급 추가 (21.12.08 이두호)
	private String bizMngYn; // 사업장관리여부 추가 (21.12.07 이두호)
	private String mainBizYn; // 주사업장여부 추가 (21.12.07 이두호)
	private String usePayPassword;	//임금명세서 확인 시 암호추가여부 (22.02.09 남동혁)
	private String funnelYear;	//가입구분 비대면 가입년도 추가 (22.03.03 이두호)
	private String payPoint;
	private String useJumin;
	private String paymentType;
	private String moduleType;
	private String sendDate;
	private String taxBillBizName;
	private String taxBillName;
	private String taxBillTel;
	private String taxBillEmail;
	private String memo; // 기업별 시스템관리자 메모 추가(22.08.25 이준경)
	private String useContractData; // 계약조건 다운로드 사용유무(23.05.25 이준경)
}
