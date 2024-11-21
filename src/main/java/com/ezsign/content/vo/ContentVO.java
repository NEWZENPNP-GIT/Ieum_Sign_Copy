package com.ezsign.content.vo;

import com.jarvis.common.util.StringUtil;

import lombok.Data;
import lombok.Getter;

@Data
public class ContentVO {

	private String fileId;		// 문서 ID						FILE_ID
	private String fileType;	// 문서구분 (PDF, XML, DOC...)	FILE_TYPE
	private String fileName;	// 파일명칭						FILE_NAME
	private String orgFileName;	// 원본파일명칭					ORG_FILE_NAME
	private String fileSize;	// 파일크기						FILE_SIZE
	private String filePath;	// 파일위치						FILE_PATH
	private String fileTitle;	// 파일제목 						FILE_TITLE
	private String classId;		// 분류체계ID						CLASS_ID
	private String cabinetId;	// 캐비넷ID						CABINET_ID
	private String fileVersion;	// 파일버젼						FILE_VERSION
	private String parFileId;	// 상위파일ID						PAR_FILE_ID
	private String etcDesc;		// 부가정보						ETC_DESC
	private String checkInOut;	// 체크인여부						CHECK_INOUT
	private String checkUserId;	// 체크인사용자ID					CHECK_USER_ID
	private String checkCount;	// 체크인횟수						CHECK_COUNT
	private String useYn;		// 사용여부						USE_YN
	private String delYn;		// 삭제여부						DEL_YN
	private String insDate;		// 등록일시						INS_DATE
	private String insEmpNo;	// 등록자						INS_EMP_NO
	private String updDate;		// 수정일시						UPD_DATE
	private String updEmpNo;	// 수정자						UPD_EMP_NO
	
	public String getInsEmpNo() {
		return StringUtil.nvl(insEmpNo, "SYSTEM");
	}
	
	
}
