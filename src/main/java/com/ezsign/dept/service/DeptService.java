package com.ezsign.dept.service;

import java.util.List;

import com.ezsign.dept.vo.DeptVO;
import com.ezsign.emp.vo.EmpVO;

public interface DeptService {

	public List<DeptVO> getDeptList(DeptVO vo)  throws Exception;
	
	public int getDeptListCount(DeptVO vo)  throws Exception;

	public int saveDept(String bizId, List<DeptVO> vo) throws Exception;

	public List<EmpVO> getDeptEmpList(DeptVO vo)  throws Exception;

	public void delMiddle(String bizId, DeptVO vo) throws Exception;
	
}
