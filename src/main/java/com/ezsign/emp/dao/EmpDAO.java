package com.ezsign.emp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

import com.ezsign.emp.vo.EmpLogVO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.meta.vo.MetaDataVO;

@Repository("empDAO")
public class EmpDAO extends EgovAbstractDAO {

	public List<EmpVO> getEmpList(EmpVO vo) {
		return (List<EmpVO>)list("empDAO.getEmpList", vo);
	}
	
	public List<EmpVO> getDupleEmpList(EmpVO vo) {
		return (List<EmpVO>)list("empDAO.getDupleEmpList", vo);
	}
	

	public int getEmpListCount(EmpVO vo) {
		return (Integer)selectByPk("empDAO.getEmpListCount", vo);
	}
	
	public void insEmp(EmpVO vo) {
		insert("empDAO.insEmp", vo);
	}

	public int updEmp(EmpVO vo) {
		return update("empDAO.updEmp", vo);
	}

	public int updEmpList(EmpVO vo) {
		return update("empDAO.updEmpList", vo);
	}
	
	public EmpVO getEmp(EmpVO vo) {
		return (EmpVO)selectByPk("empDAO.getEmp", vo);
	}

	public EmpVO getEmpNo(EmpVO vo) {
		return (EmpVO)selectByPk("empDAO.getEmpNo", vo);
	}

	public List<EmpVO> getEmpNoCheck(EmpVO vo) {
		return (List<EmpVO>) list("empDAO.getEmpNo", vo);
	}

	public int delEmp(EmpVO vo) {
		return update("empDAO.delEmp", vo);
	}

	public int delEmpAll(EmpVO vo) {
		return delete("empDAO.delEmpAll", vo);
	}
	
	public int updLoginId(EmpVO vo) {
		return update("empDAO.updLoginId", vo);
	}

	public int resetLoginId(EmpVO vo) {
		return update("empDAO.resetLoginId", vo);
	}

	@SuppressWarnings("unchecked")
	public List<EmpVO> getDeptEmpList(EmpVO vo) {
		return (List<EmpVO>)list("empDAO.getDeptEmpList", vo);
	}

	@SuppressWarnings("unchecked")
	public List<EmpVO> getEmpUserGrpList(EmpVO vo) {
		return (List<EmpVO>)list("empDAO.getEmpUserGrpList", vo);
	}

	public int getEmpUserGrpListCount(EmpVO vo) {
		return (Integer)selectByPk("empDAO.getEmpUserGrpListCount", vo);
	}
	
	public void insEmpLog(EmpLogVO vo) {
		insert("empDAO.insEmpLog", vo);
	}

	public void insMetaDataToEmp(EmpVO vo){
		insert("empDAO.insMetaDataToEmp", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<EmpVO> getEmpExist(EmpVO vo) {
		return (List<EmpVO>)list("empDAO.getEmpExist", vo);
	}
	
	//사번을 자동부여하면서 insert
	public void insEmpNo(EmpVO vo) {
		insert("empDAO.insEmpNo", vo);
		
	}

	public List<EmpVO> getCustomerList(EmpVO vo) {return (List<EmpVO>)list("empDAO.getCustomerList", vo);}

	public int getCustomerListCount(EmpVO vo) {
		return (Integer)selectByPk("empDAO.getCustomerListCount", vo);
	}

	public void insCustomer(EmpVO vo) { insert("empDAO.insCustomer", vo); }

	public int updCustomer(EmpVO vo) {
		return update("empDAO.updCustomer", vo);
	}

	public EmpVO getCustomer(EmpVO vo) {
		return (EmpVO)selectByPk("empDAO.getCustomer", vo);
	}

	public List<EmpVO> getCustomerCheck(EmpVO vo) {
		return (List<EmpVO>) list("empDAO.getCustomer", vo);
	}

	public int delCustomer(EmpVO vo) {
		return update("empDAO.delCustomer", vo);
	}

	public List<EmpVO> getExcelUserDown(EmpVO vo) {
		return (List<EmpVO>)list("empDAO.getExcelUserDown", vo);
	}

	public List<EmpVO> getExcelUserGrpDown(EmpVO vo) {
		return (List<EmpVO>)list("empDAO.getExcelUserGrpDown", vo);
	}

	public List<EmpVO> getExcelCustomerDown(EmpVO vo) {
		return (List<EmpVO>)list("empDAO.getExcelCustomerDown", vo);
	}
}
