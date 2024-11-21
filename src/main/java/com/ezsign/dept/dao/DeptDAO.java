package com.ezsign.dept.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

import com.ezsign.dept.vo.DeptVO;

@Repository("deptDAO")
public class DeptDAO extends EgovAbstractDAO {
	
	@SuppressWarnings("unchecked")
	public List<DeptVO> getDeptList(DeptVO vo) {
		return (List<DeptVO>)list("deptDAO.getDeptList", vo);
	}
	
	public int getDeptListCount(DeptVO vo) {
		return (Integer)getSqlMapClientTemplate().queryForObject("deptDAO.getDeptListCount", vo);
	}
	
	public void insDept(DeptVO vo) {
		insert("deptDAO.insDept", vo);
	}
	
	public int updDept(DeptVO vo) {
		return update("deptDAO.updDept", vo);
	}
	
	public int delDept(DeptVO vo) {
		return delete("deptDAO.delDept", vo);
	}

	public int delDeptEmp(DeptVO vo) {
		return delete("deptDAO.delDeptEmp", vo);
	}
	
}
