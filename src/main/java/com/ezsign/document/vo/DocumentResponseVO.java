package com.ezsign.document.vo;

import java.util.List;

import com.jarvis.pdf.vo.PageVO;

import lombok.Data;

@Data
public class DocumentResponseVO {
	///////////////// 공통 START ///////////////////
	public String dbMode;
	
	public String code;
	public String message;
	///////////////// 공통 END ///////////////////
	
	public DocumentMasterVO documentMaster;
	
	public List<DocumentDetailVO> documentDetail;
	
	public List<DocumentFileVO> documentFile;
	
	public List<DocumentCommentVO> documentComment;
	
	public List<DocumentLogVO> documentLog;
	
	public List<PageVO> documentPage;
	
}
