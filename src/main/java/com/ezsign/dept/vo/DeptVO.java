package com.ezsign.dept.vo;

import java.util.List;

import com.ezsign.emp.vo.EmpVO;
import com.ezsign.user.vo.UserGrpVO;

import lombok.Data;

@Data
public class DeptVO {
	
	private String code;
	private String message;	

	private String dbMode;
	private String bizId;
	private String deptCode;
	private String deptName;
	private String pdeptCode;
	private String deptLvl;
	private String deptOdr;
	private String loc;
	private String useYn;
	private String insDate;
	private String updDate;
	
	private int	 startPage;
	private int  endPage;
	private String sortName;
	private String sortOrder;
	
	// 중간관리자
	private List<UserGrpVO> subAdminList;
	
	// 그룹 배정 사용자
	private List<EmpVO> userList;
	
}
