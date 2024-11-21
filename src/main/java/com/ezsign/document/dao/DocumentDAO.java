package com.ezsign.document.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.document.vo.DocumentCommentVO;
import com.ezsign.document.vo.DocumentDetailVO;
import com.ezsign.document.vo.DocumentEformVO;
import com.ezsign.document.vo.DocumentFileVO;
import com.ezsign.document.vo.DocumentLogVO;
import com.ezsign.document.vo.DocumentMasterVO;
import com.ezsign.document.vo.DocumentSettingVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("documentDAO")
public class DocumentDAO extends EgovAbstractDAO {
	
	/* SELECT */
	@SuppressWarnings("unchecked")
	public List<DocumentMasterVO> getDocumentMasterWriteList(DocumentMasterVO vo) {
		return (List<DocumentMasterVO>)list("documentDAO.getDocumentMasterWriteList", vo);
	}
	
	public int getDocumentMasterWriteListCount(DocumentMasterVO vo) {
		return (int) select("documentDAO.getDocumentMasterWriteListCount", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<DocumentMasterVO> getDocumentMasterList(DocumentMasterVO vo) {
		return (List<DocumentMasterVO>)list("documentDAO.getDocumentMasterList", vo);
	}

	public int getDocumentMasterListCount(DocumentMasterVO vo) {
		return (int) select("documentDAO.getDocumentMasterListCount", vo);
	}

	@SuppressWarnings("unchecked")
	public List<DocumentMasterVO> getDocumentNonce(DocumentMasterVO vo) {
		return (List<DocumentMasterVO>)list("documentDAO.getDocumentNonce", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<DocumentDetailVO> getDocumentDetailList(DocumentDetailVO vo) {
		return (List<DocumentDetailVO>)list("documentDAO.getDocumentDetailList", vo);
	}

	@SuppressWarnings("unchecked")
	public List<DocumentFileVO> getDocumentFileList(DocumentFileVO vo) {
		return (List<DocumentFileVO>)list("documentDAO.getDocumentFileList", vo);
	}

	@SuppressWarnings("unchecked")
	public List<DocumentCommentVO> getDocumentCommentList(DocumentCommentVO vo) {
		return (List<DocumentCommentVO>)list("documentDAO.getDocumentCommentList", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<DocumentEformVO> getDocumentEformList(DocumentEformVO vo) {
		return (List<DocumentEformVO>)list("documentDAO.getDocumentEformList", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<DocumentLogVO> getDocumentLogList(DocumentLogVO vo) {
		return (List<DocumentLogVO>)list("documentDAO.getDocumentLogList", vo);
	}
	
	/* DELETE */
	public void delDocumentMaster(DocumentMasterVO vo) {
		update("documentDAO.delDocumentMaster", vo);
	}
	
	public void delDocumentDetail(DocumentDetailVO vo) {
		delete("documentDAO.delDocumentDetail", vo);
	}
	
	public void delDocumentDetailById(DocumentDetailVO vo) {
		delete("documentDAO.delDocumentDetailById", vo);
	}

	public void delDocumentFile(DocumentFileVO vo) {
		delete("documentDAO.delDocumentFile", vo);
	}

	public void delDocumentComment(DocumentCommentVO vo) {
		delete("documentDAO.delDocumentComment", vo);
	}

	public void delDocumentSetting(DocumentSettingVO vo) {
		delete("documentDAO.delDocumentSetting", vo);
	}
	
	public void delDocumentEform(DocumentEformVO vo) {
		delete("documentDAO.delDocumentEform", vo);
	}

	
	/* UPDATE */
	public int updDocumentMaster(DocumentMasterVO vo) {
		return update("documentDAO.updDocumentMaster", vo);
	}

	public int updDocumentDetail(DocumentDetailVO vo) {
		return update("documentDAO.updDocumentDetail", vo);
	}
	
	public int updDocumentEform(DocumentEformVO vo) {
		return update("documentDAO.updDocumentEform", vo);
	}

	public int updDocumentFile(DocumentFileVO vo) {
		return update("documentDAO.updDocumentFile", vo);
	}

	/* INSERT */
	public void insDocumentMaster(DocumentMasterVO vo) {
		insert("documentDAO.insDocumentMaster", vo);
	}
	
	public void insDocumentLog(DocumentLogVO vo) {
		insert("documentDAO.insDocumentLog", vo);
	}
	
	public void insDocumentEform(DocumentEformVO vo) {
		insert("documentDAO.insDocumentEform", vo);
	}

	public void insDocumentDetail(DocumentDetailVO vo) {
		insert("documentDAO.insDocumentDetail", vo);
	}

	public void insDocumentFile(DocumentFileVO vo) {
		insert("documentDAO.insDocumentFile", vo);
	}

	public void insDocumentComment(DocumentCommentVO vo) {
		insert("documentDAO.insDocumentComment", vo);
	}

	public void insDocumentSetting(DocumentSettingVO vo) {
		insert("documentDAO.insDocumentSetting", vo);
	}
	
}
