package com.ezsign.contract.vo;

import lombok.Data;

@Data
public class ContractPersonNewVO {

	private String serviceId;
	private String bizId;
	private String bizName;
	private String fileId;
	private String contractId;
	private String dataFileId;
	private String fileName;
	private String fileTitle;
	private String transType;
	private String expireDay;
	private String contractType;
	private String contractTypeName;
	private String fontConvertType; // 양식변환 요청 구분값(21.10.21)
	private String correctionType; // 정정 요청 구분값(22.04.27)
	private String insDate;
	private String updDate;

	private String searchDateFrom;
	private String searchDateTo;
	private String searchValue;
	private String searchCompany;

	private int startPage;
	private int endPage;
	
	private int result;
	private String message;
		
	// pdf
	private String pdfFile;
	
	//최종 신규계약서ID
	private String lastFileId;
	//노무SOS 지원여부
	private String laborType;
	//서명대상_근로자
	private String signUserType;
	//서명대상_기업
	private String signBizType;
	//서명대상_거래처
	private String signCustomerType;
	//양식표시여부
	private String viewYn;
	
	//정렬추가
    private String sortName;
    private String sortOrder;
    
    private String useOtherLayout;
    
    //추가수집정보 사용여부, URL
    private String useEtc;
    private String etcUrl;
    private String usePre;
    private String preUrl;
    
    private String orgImgYn;

	private String alterFileId;
	private String bookmark;
	private String recvType;
	private String userId;
	private String colOrder;

	private String transTypeN;
	private String transTypeT;
	private String transTypeC;
	private String transTypeP;
	private String requestCount;
	private String progressCount;
	private String completeCount;
	private String previewCount;
	private String CNT;
	private String searchStatus;

	// 양식등록자 권한 추가(231017 이준경)
	private String insUserType;
}
