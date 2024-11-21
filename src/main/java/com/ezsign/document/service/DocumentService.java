package com.ezsign.document.service;

import java.util.List;

import com.ezsign.document.vo.DocumentCommentVO;
import com.ezsign.document.vo.DocumentDetailVO;
import com.ezsign.document.vo.DocumentEformVO;
import com.ezsign.document.vo.DocumentFileVO;
import com.ezsign.document.vo.DocumentLogVO;
import com.ezsign.document.vo.DocumentMasterVO;
import com.ezsign.document.vo.DocumentRequestVO;
import com.ezsign.document.vo.DocumentResponseVO;
import com.ezsign.document.vo.DocumentSettingVO;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;


public interface DocumentService {

	public static final String DOCU_CON_ID = "200907141420009"; // 전자계약 계약서파일 분류체계ID
	public static final String DOCU_ATT_ID = "200907141420010"; // 전자계약 계약서 첨부파일 분류체계ID

	public boolean saveDocument(DocumentRequestVO vo, List<FileVO> fileList)  throws Exception;

	public boolean forwardDocument(DocumentDetailVO vo, List<FileVO> fileList)  throws Exception;

	public boolean requestSign(DocumentDetailVO vo, List<FileVO> fileList)  throws Exception;

	public void saveDocumentComment(DocumentCommentVO vo) throws Exception;

	public void saveDocumentSetting(DocumentSettingVO vo)  throws Exception;

	public boolean saveDocumentFormData(DocumentMasterVO vo, List<DocumentDetailVO> detailRList, String originalPdfPath, String targetPdfPath)  throws Exception;

	public void saveDocumentComplete(DocumentMasterVO vo, List<FileVO> fileList) throws Exception;

	/*SELECT*/
	public List<DocumentMasterVO> getDocumentMasterWriteList(DocumentMasterVO vo)  throws Exception;

	public int getDocumentMasterWriteListCount(DocumentMasterVO vo);

	public List<DocumentMasterVO> getDocumentMasterList(DocumentMasterVO vo)  throws Exception;

	public int getDocumentMasterListCount(DocumentMasterVO vo);

	public List<DocumentDetailVO> getDocumentDetailList(DocumentDetailVO vo)  throws Exception;

	public List<DocumentEformVO> getDocumentEformList(DocumentEformVO vo)  throws Exception;

	public List<DocumentLogVO> getDocumentLogList(DocumentLogVO vo)  throws Exception;

	public DocumentResponseVO getDocument(DocumentMasterVO vo) throws Exception;

	/*DELETE*/
	public void delDocumentDetail(DocumentDetailVO vo) throws Exception;

	/*UPDATE*/
	public int updDocumentMaster(DocumentMasterVO vo) throws Exception;

	public int updDocumentEform(DocumentEformVO vo) throws Exception;

	public int updDocumentFile(DocumentFileVO vo) throws Exception;

	/*INSERT*/
	public void insDocumentMaster(DocumentMasterVO vo) throws Exception;

	public void insDocumentLog(DocumentLogVO vo) throws Exception;

	public void insDocumentEform(DocumentEformVO vo) throws Exception;

	public void insDocumentDetail(DocumentDetailVO vo) throws Exception;

	DocumentMasterVO getDocuView(DocumentMasterVO vo) throws Exception;

	List<DocumentMasterVO> getDocumentNonce(DocumentMasterVO vo) throws Exception;

	DocumentResponseVO getDocumentView(DocumentMasterVO vo) throws Exception;

	DocumentResponseVO getDocumentView(DocumentMasterVO vo, boolean allFileView) throws Exception;

	public int downloadDocumentSelectZip(List<DocumentMasterVO> list, SessionVO loginVO) throws Exception;

}
