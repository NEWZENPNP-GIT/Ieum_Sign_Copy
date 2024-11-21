package com.ezsign.emp.vo;

import lombok.Data;

import java.util.List;


		// tbl_emp						직원 정보
@Data
public class EmpVO {
	private String message;

	private String dbMode = "R";

	private String userId;				// USER_ID 		유저 ID
	private String loginId;				// LOGIN_ID 	로그인 ID
	private String userPwd;				//
	private String bizId;
	private String bizName;
	private String bizBusinessNo;
	private String ownerName;
	private String empNo;
	private String empName;
	private String empType;
	private String empTypeName;
	private String empNonce;
	private String addr1;
	private String addr2;
	private String telNum;
	private String phoneNum;
	private String eMail;
	private String useYn;
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
	private String insDate;
	private String insEmpNo;
	private String updDate;
	private String updEmpNo;
	private String zipCode;
	
	private String businessNo;
	private String serviceId;
	private String smsSendType;
	private String companyTelNum;
	private String companyFaxNum;
	
	private String searchKey;
	private String searchValue;
	private int	   startPage;
	private int    endPage;
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
	private String pinNo;
	
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

	private String customerId;
	private String customerType;
	private String customerName;
	private String managerName;
	private String managerEmail;
	private String managerDeptName;
	private String managerPhoneNum;

	// 엑셀 일괄업로드시 중복열 리스트
	private String insCount;
	private String updCount;
	private String checkCount;
	private String dupCount;
	private List<String> duplicateEmpRow;
	private List<String> checkEmpRow;

	// 입력 / 조회하는 userType
	private String insUserType;
}
