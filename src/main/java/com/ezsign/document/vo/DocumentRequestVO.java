package com.ezsign.document.vo;

import java.util.List;

import com.ezsign.content.vo.ContentWordVO;
import com.jarvis.pdf.vo.FieldVO;

import lombok.Data;

@Data
public class DocumentRequestVO {
	///////////////// 공통 START ///////////////////
	public String dbMode;
	
	public String code;
	public String message;
	public String domainName;
	///////////////// 공통 END ///////////////////
	
	public DocumentMasterVO documentMaster;
	
	public List<DocumentDetailVO> documentDetail;
	
	public List<DocumentFileVO> documentFile;
	
	public List<FieldVO> documentField;
	
	public List<ContentWordVO> documentWord;
	
}
