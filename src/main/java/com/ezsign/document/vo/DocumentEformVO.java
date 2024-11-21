package com.ezsign.document.vo;

import lombok.Data;

// 전자문서 폼 정보
@Data
public class DocumentEformVO {
	private String docuId;		// 문서ID			DOCU_ID
	private String docuName;
	private String formId;		// 입력ID			FORM_ID
	private String formName;	// 입력항목명			FORM_NAME
	private String formType;	// 서식타입			FORM_TYPE
	private String formTypeName;
	private String formValue;	// 입력항목값			FORM_VALUE
	private String insDate;		// 등록일시			INS_DATE
	
}
