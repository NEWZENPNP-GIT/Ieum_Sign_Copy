package com.ezsign.contract.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ContractLogVO {

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
			
	private String resultCode;
	private String resultMessage;
	
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
	
	// HASH_DATA
	private String hashCode;
	
	// 계약서 전송구분 (KKO, EMAIL)
	private String sendType;
	
	// 계약서인지 감사추적인증서인지 (계약서 : C, 감사추적인증서 : A)
	private String fileType;
	
	// 계약항목
	private List<ContractFormVO> formList = new ArrayList<ContractFormVO>();
	
	
	public void addForm(ContractFormVO vo) {
		this.formList.add(vo);		
	}
	
	//계약서 원본 파일 ID
	private String fileId;
		
	//삭제될 contId
	private String[] delContId;
	
	private String metaId;
	
	//관리자 코멘트
	private String comment;
}
