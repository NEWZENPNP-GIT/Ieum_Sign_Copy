package com.ezsign.approval.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.annual.vo.AnnualVacationVO;
import com.ezsign.approval.vo.ApprLineDetailVO;
import com.ezsign.approval.vo.ApprLineMasterVO;
import com.ezsign.approval.vo.ApprovalBookmarkVO;
import com.ezsign.approval.vo.ApprovalDetailVO;
import com.ezsign.approval.vo.ApprovalMasterVO;
import com.ezsign.approval.vo.ApprovalVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("approvalDAO")
public class ApprovalDAO extends EgovAbstractDAO {

	public void insApproval(ApprovalVO vo) {
		insert("approvalDAO.insApproval", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ApprovalVO> getApprovalList(ApprovalVO vo) {
		return (List<ApprovalVO>) list("approvalDAO.getApprovalList", vo);
	}
	
	public int updApproval(ApprovalVO vo) {
		return update("approvalDAO.updApproval", vo);
	}
	
	public void insApprovalBookmark(ApprovalBookmarkVO vo) {
		insert("approvalDAO.insApprovalBookmark", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ApprovalBookmarkVO> getApprovalBookmarkList(ApprovalBookmarkVO vo) {
		return (List<ApprovalBookmarkVO>) list("approvalDAO.getApprovalBookmarkList", vo);
	}

	public int delApprovalBookmark(ApprovalBookmarkVO vo) {
		return delete("approvalDAO.delApprovalBookmark", vo);
	}
	
	public String insApprLineMaster(ApprLineMasterVO vo) {
		return (String)insert("approvalDAO.insApprLineMaster", vo);
	}
	
	public void insApprLineDetail(ApprLineDetailVO vo) {
		insert("approvalDAO.insApprLineDetail", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ApprLineMasterVO> getApprLineMaster(ApprLineMasterVO vo) {
		return (List<ApprLineMasterVO>) list("approvalDAO.getApprLineMaster", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ApprLineDetailVO> getApprLineDetail(ApprLineDetailVO vo) {
		return (List<ApprLineDetailVO>) list("approvalDAO.getApprLineDetail", vo);
	}
	
	public String insApprovalMaster(ApprovalMasterVO vo) {
		return (String)insert("approvalDAO.insApprovalMaster", vo);
	}
	
	public void insApprovalDetail(ApprovalDetailVO vo) {
		insert("approvalDAO.insApprovalDetail", vo);
	}
	
	@SuppressWarnings("unchecked")
	public ApprovalMasterVO getApprMail(ApprovalMasterVO vo) {
		return (ApprovalMasterVO)selectByPk("approvalDAO.getApprMail", vo);
	}
	
	@SuppressWarnings("unchecked")
	public ApprovalMasterVO getApprMailRef(ApprovalMasterVO vo) {
		return (ApprovalMasterVO)selectByPk("approvalDAO.getApprMailRef", vo);
	}
	
	public int updateApprovalMaster(ApprovalMasterVO vo) {
		return update("approvalDAO.updateApprovalMaster", vo);
	}
	
	public int updateApprovalDetail(ApprovalMasterVO vo) {
		return update("approvalDAO.updateApprovalDetail", vo);
	}
	
	@SuppressWarnings("unchecked")
	public ApprovalMasterVO getApprovalSeq(ApprovalMasterVO vo) {
		return (ApprovalMasterVO)selectByPk("approvalDAO.getApprovalSeq", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ApprovalMasterVO> getApprLine(ApprovalMasterVO vo) {
		return (List<ApprovalMasterVO>) list("approvalDAO.getApprLine", vo);
	}
}
