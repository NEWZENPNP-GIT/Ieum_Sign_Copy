package com.ezsign.approval.service;

import java.util.List;
import java.util.Map;

import com.ezsign.approval.vo.ApprLineDetailVO;
import com.ezsign.approval.vo.ApprLineMasterVO;
import com.ezsign.approval.vo.ApprLineVO;
import com.ezsign.approval.vo.ApprovalDetailVO;
import com.ezsign.approval.vo.ApprovalMasterVO;
import com.ezsign.approval.vo.ApprovalVO;

public interface ApprovalService {
	
	public static final String SERVICE_ID = "171010134547001";	// 근로계약서 서비스ID

	public List<ApprovalVO> getApprovalList(ApprovalVO vo) throws Exception;
	
	public String saveApprovalMaster(ApprovalMasterVO apprLineMasterVO) throws Exception;
	public void saveApprovalDetail(ApprovalDetailVO apprLineDetailVO) throws Exception;
	public void sendApprovalMail(List<ApprovalMasterVO> approvalMasterVO) throws Exception;
	public void insApprovalLog(ApprovalMasterVO approvalMasterVO) throws Exception;
	public String saveApprLineMaster(ApprLineMasterVO apprLineMasterVO) throws Exception;
	public void saveApprLineDetail(ApprLineDetailVO apprLineDetailVO) throws Exception;
	public List<ApprLineMasterVO> getApprLineMaster(ApprLineMasterVO apprLineMasterVO) throws Exception;
	public List<ApprLineDetailVO> getApprLineDetail(ApprLineDetailVO apprLineDetailVO) throws Exception;
	public int updateApprovalMaster(ApprovalMasterVO apprLineMasterVO) throws Exception;
	public int updateApprovalDetail(ApprovalMasterVO apprLineMasterVO) throws Exception;
	public ApprovalMasterVO getApprovalSeq(ApprovalMasterVO apprLineMasterVO) throws Exception;
	public List<ApprovalMasterVO> getApprLine(ApprovalMasterVO approvalMasterVO) throws Exception;
}
