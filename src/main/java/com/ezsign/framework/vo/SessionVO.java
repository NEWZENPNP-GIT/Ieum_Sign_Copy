package com.ezsign.framework.vo;

import lombok.Data;

@Data
public class SessionVO {

	private String bizId;
	private String bizName;
	private String loginId;
	private String userId;
	private String userName;
	private String userType;
	private String empNo;
	private String phoneNum;
	private String email;
	private String deviceId;
	private int	   expire;
	private String loginLockYn; //계정잠김: Y	
	private String loginFailYn; //로그인 실패: Y	
	private int    loginCount;	
	private int    unlockTime;
	private String ipAddr;
	
	// 연말정산 계약ID
	private String yearContractId;
	private String febYear;
	private String useContract;
	
	private String useElecDoc;
	private String usePayStub;
	private String bizMngYn;
	private String mainBizYn;
	private int curPoint;
	private String openVoucherType;
	private String deptCode; // 부서코드(중간관리자용)
	private String useContractData; //전자문서 조건엑셀 다운로드 사용여부
}
