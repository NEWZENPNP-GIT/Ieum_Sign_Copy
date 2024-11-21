package com.ezsign.emp.vo;

import lombok.Data;

@Data
public class CustomerVO {
	private String message;

	private String dbMode = "R";
	private String userId;
	private String loginId;
	private String userPwd;
	private String bizId;			// 기업ID	BIZ_ID
	private String bizName;
	private String ownerName;		// 대표자명	OWNER_NAME
	private String empNo;
	private String empName;
	private String empType;
	private String empTypeName;
	private String empNonce;
	private String addr1;				// 주소1 		ADDR1
	private String addr2;				// 주소2 		ADDR2
	private String telNum;
	private String phoneNum;			// 				MANAGER_PHONE_NUM
	private String eMail;
	private String useYn;				// 사용여부		USE_YN
	private String userDate;
	private String joinDate;
	private String leaveDate;
	private String deptCode;
	private String deptName;
	private String groupName;
	private String positionName;
	private String stepName;
	private String countryType;
	private String confirmType;
	private String lastConnTime;
	private String insDate;				// 등록일시 		INS_DATE
	private String insEmpNo;			// 등록자 		INS_EMP_NO
	private String updDate;				// 수정일시		UPD_DATE
	private String updEmpNo;			// 수정자 		UPD_EMP_NO
	private String zipCode;
	
	private String businessNo;			// 사업자번호		BUSINESS_NO
	private String serviceId;
	private String smsSendType;
	private String companyTelNum;
	private String companyFaxNum;
	
	private String searchKey;
	private String searchValue;
	private int	 startPage;
	private int  endPage;
	private String sortName;
	private String sortOrder;
	private String searchCompany;
	private String searchAppr;
	
	private String fileData;
	
	private String 계약ID;
	private String 작업자ID;
	
	// 엑셀 row
	private int excelRow;
	// 엑셀 sheet
	private String excelSheet;
	// 등록양식
	private String documentType;
	
	// 멀티처리
	private String multiData;
	
	// 케이랩 연동 추가 항목
	private String userData;
	private String residentType;
	private String liveType;

	// 법인등록번호
	private String pinNo;			// 법인등록번호_개인식별번호		PIN_NO
	
	// 나눔HR연동
	private String metaId;
	private String contractDate;

	private String bankCode;
	private String bankAccount;
	private String bankUserName;
	
	//loginID가 null인 직원 검색
	private String loginIdIsNull;

	private String endDateIsNull;
	
	private String searchStartDate;
	private String searchEndDate;
	
	private String etc;
	private String testEmail;

	private String customerId;				// 거래처 ID		CUSTOMER_ID
	private String customerType;			// 거래처구분		CUSTOMER_TYPE
	private String customerName;			// 거래처명		CUSTOMER_NAME
	private String managerName;				// 담당자명		MANAGER_NAME
	private String managerEmail;			// 담당자이메일	MANAGER_EMAIL
	private String managerDept_name;		// 담당자부서명	MANAGER_DEPT_NAME
}
