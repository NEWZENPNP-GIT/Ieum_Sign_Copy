package com.ezsign.emp.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezsign.emp.vo.EmpLogVO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.user.vo.UserVO;

public interface EmpService {

	List<EmpVO> getEmpList(EmpVO vo, SessionVO loginVO) throws Exception;

	List<EmpVO> getDupleEmpList(EmpVO vo) throws Exception;
	
	public int getEmpListCount(EmpVO vo)  throws Exception;

	EmpVO getEmp(EmpVO vo, SessionVO loginVO) throws Exception;
	
	public EmpVO getEmpNo(EmpVO vo)  throws Exception;

	public String insEmp(EmpVO vo) throws Exception;
	
	public int updEmp(EmpVO vo) throws Exception;
	
	public int delEmp(EmpVO vo) throws Exception;

	public int updEmpType(EmpVO vo) throws Exception;
	
	public int updEmpNonce(EmpVO vo) throws Exception;
	
	public boolean updLoginId(EmpVO vo) throws Exception;
	
    public String getEmpNonce();

	public EmpVO sendEmpExcel(String bizId, String xlsPath, String smsSendType, String deptCode) throws Exception;

	public EmpVO sendPayCareEmpExcel(String bizId, String xlsPath, String smsSendType) throws Exception;

	public EmpVO sendNanumhrEmpExcel(String bizId, String xlsPath, String metaId, String fileId) throws Exception;

	public List<EmpVO> getEmpUserGrpList(EmpVO vo)  throws Exception;

	public int getEmpUserGrpListCount(EmpVO vo)  throws Exception;

	public int sendAppInsSms(List<EmpVO> list);

	public void insEmpLog(EmpLogVO vo) throws Exception;

	public FileVO getZipFileAddList(String bizId, HttpServletRequest request, String string) throws Exception;
	
	public EmpVO selectEmpInfo(EmpVO vo) throws Exception;
	
	public void updEmpLoginID(UserVO vo) throws Exception;

	List<EmpVO> getCustomerList(EmpVO vo, SessionVO loginVO) throws Exception;

	public int getCustomerListCount(EmpVO vo)  throws Exception;

	public boolean insCustomer(EmpVO vo) throws Exception;

	public int updCustomer(EmpVO vo) throws Exception;

	public int delCustomer(EmpVO vo) throws Exception;

	public EmpVO sendCustomerExcel(String bizId, String xlsPath, String insEmpNo) throws Exception;

	EmpVO getCustomer(EmpVO vo) throws Exception;

	int downUserDataExcel(String filePath, String fileName, EmpVO vo) throws Exception;

	int downUserGrpDataExcel(String filePath, String fileName, EmpVO vo) throws Exception;

	int downCustomerDataExcel(String filePath, String fileName, EmpVO vo) throws Exception;
}
