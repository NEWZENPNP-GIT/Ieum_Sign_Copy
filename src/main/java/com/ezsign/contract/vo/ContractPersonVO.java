package com.ezsign.contract.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ContractPersonVO {

	private String contId;
	private String serviceId;
	private String bizId;
	private String userId;
	private String contractDate;
	private String contractName;
	private String contractId;
	private String contractType;
	private String contractTypeName;
	private String inputType;
	private String sendDate;
	private String endDate;
	private String keepDate;
	private String expireDate;
	private String hashData;
	private String digitSign;
	private String digitNonce;
	private String contractFileId;
	private String contractFileName;
	private String statusCode;
	private String transType;
	private String insDate;
	private String insEmpNo;
	private String updDate;
	private String updEmpNo;

	private String pdfFile;

	private String empNo;
	private String empName;
	private String statusName;
	private String userDate;
	private String businessNo;
	private String ownerName;
	private String bizName;
	private String phoneNum;
	private String eMail;
	private String deptName;

	private String searchKey;
	private String searchValue;
	private int	 startPage;
	private int  endPage;
	private String sortName;
	private String sortOrder;
	private String searchCompany;

	private String resultCode;
	private String resultMessage;
	private String resultMessage2;

	private  String loginUserId;
	private  String loginType;
	// loginUserId 는 Candy 어플에서 사용하므로 새로운 파라미터 생성
	private  String loginUserParam;

	// 보관여부
	private String keepType;
	private String curPoint;
	private String keepCount;
	//엑셀 원본 파일의 데이터 숫자
	private String orgFileDataCount;

	// 멀티처리
	private String multiData;

	private String fileData;

	// 계약기간조회
	private String contractDateFrom;
	private String contractDateTo;
	private String contractEMail;

	// HASH_DATA
	private String hashCode;

	// 계약서 전송구분 (KKO, EMAIL)
	private String sendType;

	// 계약서인지 감사추적인증서인지 (계약서 : C, 감사추적인증서 : A)
	private String fileType;
	//문서 다운인지 메일전송인지(메일전송 :N, 다운 : Y)
	private String downloadYn;
	//sms 문자 보내는지 아닌지(문자보냄 : Y, 안보냄 : N)
	private  String smsYN;
	// 계약항목
	private List<ContractFormVO> formList = new ArrayList<ContractFormVO>();


	public void addForm(ContractFormVO vo) {
		this.formList.add(vo);
	}

	//계약서 원본 파일 ID
	private String fileId;

	//삭제될 contId
	private String[] delContId;
	private String delBizId;

	private String metaId;

	//관리자 코멘트
	private String comment;

	private String requesterId;

	//캔디캐쉬
	private String contractStartDate;	//근무시작일
	private String contractEndDate;		//근무종료일
	private String workPlace;		    //근무장소
	private String workContent;			//업무내용
	private String startTime;           //근무시작시간
	private String endTime;
	private String workDays;			//근무일 - 월
	private String breakTime;			//휴게시간
	private String holiday;				//휴일
	private String pay;					//임금
	private String paymentDate;			//지급일
	private String paymentMethod;		//지급방법
	private String writeYear;			//작성일
	private String writeMonth;			//작성일
	private String writeDay;			//작성일
	private String writeDate;
	private String companyAddr;
	private String apprId;

	// 코오롱 커스텀 엑셀다운로드 파일 작성시 계약구분
	//서명대상_근로자
	private String signUserType;
	//서명대상_기업
	private String signBizType;
	//서명대상_거래처
	private String signCustomerType;

	// 거래처 문서조회시 인증방법 추가에 따른 변수 추가
	private String authType;
	private String authCode;
	private String authCodeSel;

}
