package com.ezsign.biz.vo;

import java.util.List;

import com.ezsign.user.vo.UserGrpVO;

import lombok.Data;

@Data
public class BizGrpVO {
	private String bizId;
	private String grpType;
	private String refId;
	private String statusCode;
	private String insDate;
	
	private String id;
	private String name;
	private String no;
	private String num;

	private String searchKey;
	private String searchValue;
	
	// 기업등록인 경우
	private String businessNo;
	private String bizName;
	private String ownerName;
	private String addr1;
	private String companyTelNum;
	private String companyFaxNum;
	private String companyImage;
	
	private String curPoint;
	
	// 담당자정보
	private String userId;
	private String empName;
	private String email;
	private String phoneNum;
	private String loginId;
	private String userPwd;
	
	private String changeUserId;
	
	private List<UserGrpVO> userGrpList;

	private String formPoint;
	private String scanPoint;
	private String sendPoint;
	private String resendPoint;
	private String payPoint;
	
	private String funnel; // 유입경로 추가 (21.12.07 이두호)
	private String bizMngYn; // 사업장관리여부 추가 (21.12.07 이두호)
	private String mainBizYn; // 주사업장여부 추가 (21.12.07 이두호)
	
	private String useContract;
	private String useElecDoc;
	private String usePayStub;
}
