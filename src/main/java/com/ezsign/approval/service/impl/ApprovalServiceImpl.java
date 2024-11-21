package com.ezsign.approval.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.approval.dao.ApprovalDAO;
import com.ezsign.approval.service.ApprovalService;
import com.ezsign.approval.vo.ApprLineDetailVO;
import com.ezsign.approval.vo.ApprLineMasterVO;
import com.ezsign.approval.vo.ApprLineVO;
import com.ezsign.approval.vo.ApprovalDetailVO;
import com.ezsign.approval.vo.ApprovalMasterVO;
import com.ezsign.approval.vo.ApprovalVO;
import com.ezsign.code.dao.CodeDAO;
import com.ezsign.contract.dao.ContractDAO;
import com.ezsign.contract.vo.ContractPersonLogVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.mail.MailVO;
import com.ezsign.framework.mail.MultiPartEmail;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("approvalService")
public class ApprovalServiceImpl extends AbstractServiceImpl implements ApprovalService {
	
	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name="codeDAO")
	private CodeDAO codeDAO;
	
	@Resource(name="approvalDAO")
	private ApprovalDAO approvalDAO;
	
	@Resource(name="empDAO")
	private EmpDAO empDAO;
	
	@Resource(name = "contractDAO")
	private ContractDAO contractDAO;
	
	@Override
	public List<ApprovalVO> getApprovalList(ApprovalVO vo) throws Exception {
		List<ApprovalVO> result = new ArrayList();
		
		logger.info("[getApprovalList]  결재ID -> "+vo.getApprovalId());
		
		result = approvalDAO.getApprovalList(vo);
		
		return result;
	}
	
	@Override
	public String saveApprLineMaster(ApprLineMasterVO apprLineMasterVO) throws Exception {
		return approvalDAO.insApprLineMaster(apprLineMasterVO);
	}
	
	@Override
	public void saveApprLineDetail(ApprLineDetailVO apprLineDetailVO) throws Exception {
		approvalDAO.insApprLineDetail(apprLineDetailVO);
	}
	
	@Override
	public List<ApprLineMasterVO> getApprLineMaster(ApprLineMasterVO apprLineMasterVO) throws Exception {
		List<ApprLineMasterVO> result = new ArrayList();
		
		result = approvalDAO.getApprLineMaster(apprLineMasterVO);
		
		return result;
	}
	
	@Override
	public List<ApprLineDetailVO> getApprLineDetail(ApprLineDetailVO apprLineDetailVO) throws Exception {
		List<ApprLineDetailVO> result = new ArrayList();
		
		result = approvalDAO.getApprLineDetail(apprLineDetailVO);
		
		return result;
	}
	
	@Override
	public String saveApprovalMaster(ApprovalMasterVO approvalMasterVO) throws Exception {
		return approvalDAO.insApprovalMaster(approvalMasterVO);
	}
	
	@Override
	public void saveApprovalDetail(ApprovalDetailVO approvalDetailVO) throws Exception {
		approvalDAO.insApprovalDetail(approvalDetailVO);
	}
	
	@Override
	public void sendApprovalMail(List<ApprovalMasterVO> approvalMasterList) throws Exception {
		List<ApprovalMasterVO> apprEmpList = new ArrayList<>();
		for(int i=0;i<approvalMasterList.size();i++){
			ApprovalMasterVO apprEmp = approvalDAO.getApprMail(approvalMasterList.get(i));
			apprEmp.setRefId(approvalMasterList.get(i).getRefId());
			apprEmpList.add(apprEmp);
		}
		
		for(int i=0;i<apprEmpList.size();i++){
			int apprCnt = 1;
			for(int z=0+1;z<apprEmpList.size();z++){
				if(apprEmpList.get(i).getApprEmail2().equals(apprEmpList.get(z).getApprEmail2())){
					apprCnt++;
				}
			}
			apprEmpList.get(i).setApprCnt(String.valueOf(apprCnt));
		}
		
		List<ApprovalMasterVO> apprEmpDupList = new ArrayList<>(apprEmpList);
		
		for(int i=0;i<apprEmpList.size();i++){
			for(int z=0+1;z<apprEmpList.size();z++){
				if(apprEmpList.get(i).getApprEmail2().equals(apprEmpList.get(z).getApprEmail2())){
					apprEmpList.remove(z);
				}
			}
		}
		
		for(ApprovalMasterVO apprEmp : apprEmpList){
			String content = "";
			
			content +="<!DOCTYPE html> ";
			content +="<html> ";
			content +="<head> ";
			content +="<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
			content +="<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
			content +="<title>전자문서</title> ";
			content +="</head> ";
			content +="<body> ";
			content +="	<div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
			content +="		<div style=\" padding: 37px 0 0 0;\"> ";
			content +="			<div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
			content +="				<span style=\"font-weight: bold; color:#00a9e9;\">IEUM</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;SIGN</span> ";
			content +="			</div> ";
			content +="			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
			content +="				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">승인요청</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br>발송이메일입니다.</span>";
			content +="			</div> ";
			content +="			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
			content +="				<span >"+apprEmp.getApprName2()+"</span><span>님!<br> ";
			content +="				승인요청 문서가 "+apprEmp.getApprCnt()+"건 있습니다.<br> ";
			content +="				승인작업 바랍니다.<br>";
			for(int i=0;i<apprEmpDupList.size();i++){
				content += i+1 +". " + apprEmpDupList.get(i).getContUserName() + "의 " + apprEmpDupList.get(i).getFileTitle() + "<br> ";
			}
			content +="				</span> ";
			content +="			</div> ";
			content +="			<div style=\"margin-top: 74px; text-align: center;\"> ";
			content +="				<div style=\"width: 255px; height: 43px; padding-top: 15px; margin-bottom: 32px; background-color: #00a9e9; border-radius: 8px; display: inline-block; text-align: center; cursor: pointer;\"> ";
			content +="					<a href=\""+System.getProperty("system.feb.url")+"\" style=\"color: #fff; font-size: 18px; font-weight: bold; font-family: 'tahoma'; text-decoration: none;\">승인하기  </a> ";
			content +="				</div> ";
			content +="			</div> ";
			content +="		</div> ";
			content +="		<div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
			content +="			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
			content +="				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
			content +="				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">service@newzenpnp.com</a></span > ";
			content +="				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
			content +="			</div> ";
			content +="		</div> ";
			content +="	</div> ";
			content +="</body> ";
			content +="</html> ";
			
			MultiPartEmail email = new MultiPartEmail();
			MailVO mailVO = new MailVO();
			mailVO.setFrom("no_reply@newzenpnp.com");
			mailVO.setTo(apprEmp.getApprEmail2());
			mailVO.setCc("");
			mailVO.setBcc("");
			if(apprEmp.getApprCnt().equals("1")){
				mailVO.setSubject("[승인요청] " + apprEmp.getApprName1() + "-" + apprEmpDupList.get(0).getContUserName() + "의 " + apprEmp.getFileTitle());
			}else{
				mailVO.setSubject("[승인요청] " + apprEmp.getApprName1() + "-" + apprEmpDupList.get(0).getContUserName() + "의 " + apprEmp.getFileTitle() + " 외 " + apprEmp.getApprCnt() + "건");
			}
			mailVO.setText(content);
			
			logger.info("IeumSign 전자문서 이메일을 발송. email : " + apprEmp.getApprName2());
			
			email.send(mailVO);
		}
	}
	
	@Override
	public void insApprovalLog(ApprovalMasterVO approvalMasterVO) throws Exception {
		ApprovalMasterVO apprEmp = approvalDAO.getApprMail(approvalMasterVO);
		String apprStatus = "";
		
		if(approvalMasterVO.getApprStatus().equals("REQ")){
			apprStatus = "요청";
		}else if(approvalMasterVO.getApprStatus().equals("APP")){
			apprStatus = "완료";
		}else if(approvalMasterVO.getApprStatus().equals("REJ")){
			apprStatus = "반려";
		}
		
		// 로그내역 등록
		logger.info("로그 등록 contId =>"+approvalMasterVO.getRefId());
		ContractPersonLogVO logVO = new ContractPersonLogVO();
		logVO.setContId(approvalMasterVO.getRefId());
		logVO.setServiceId(SERVICE_ID);
		logVO.setBizId(approvalMasterVO.getRequesterBizId());
		logVO.setUserId(approvalMasterVO.getRequesterId());
		SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd");
		Date time = new Date();
		String apprDate = dateFormat.format(time);
		logVO.setContractDate(apprDate);
		logVO.setLogType(approvalMasterVO.getApprStatus());
		if(approvalMasterVO.getApprStatus().equals("REQ")){
			if(approvalMasterVO.getApprSeq().equals("1")){
				logVO.setLogMessage(apprEmp.getApprName1()+"님께서 "+apprEmp.getApprName2()+"님께 전자문서 승인을 "+apprStatus+"하였습니다.");
			}else{
				logVO.setLogMessage(apprEmp.getApprName1()+"님께서 전자문서 승인을 완료하였습니다.\n"+apprEmp.getApprName2()+"님께 전자문서 승인을 "+apprStatus+"하였습니다.");
			}
		}else{
			logVO.setLogMessage(apprEmp.getApprName1()+"님께서 전자문서 승인을 "+apprStatus+"하였습니다.");
		}
		logVO.setInsEmpNo(approvalMasterVO.getRequesterId());
		contractDAO.insContractPersonLog(logVO);
	}
	
	@Override
	public int updateApprovalMaster(ApprovalMasterVO approvalMasterVO) throws Exception {
		return approvalDAO.updateApprovalMaster(approvalMasterVO);
	}
	
	@Override
	public int updateApprovalDetail(ApprovalMasterVO approvalMasterVO) throws Exception {
		return approvalDAO.updateApprovalDetail(approvalMasterVO);
	}
	
	@Override
	public ApprovalMasterVO getApprovalSeq(ApprovalMasterVO approvalMasterVO) throws Exception {
		ApprovalMasterVO result = new ApprovalMasterVO();
		
		result = approvalDAO.getApprovalSeq(approvalMasterVO);
		
		return result;
	}
	
	@Override
	public List<ApprovalMasterVO> getApprLine(ApprovalMasterVO apprLineMasterVO) throws Exception {
		List<ApprovalMasterVO> result = new ArrayList();
		
		result = approvalDAO.getApprLine(apprLineMasterVO);
		
		return result;
	}
}
