package com.ezsign.dept.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.dept.dao.DeptDAO;
import com.ezsign.dept.service.DeptService;
import com.ezsign.dept.vo.DeptVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.user.dao.UserDAO;
import com.ezsign.user.vo.UserGrpVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("deptService")
public class DeptServiceImpl extends AbstractServiceImpl implements DeptService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="deptDAO")
	private DeptDAO deptDAO;
	
	@Resource(name="empDAO")
	private EmpDAO empDAO;
	
	@Resource(name="userDAO")
	private UserDAO userDAO;
	
	@Override
	public List<DeptVO> getDeptList(DeptVO vo)  throws Exception {
		List<DeptVO> list = new ArrayList<DeptVO>();
		
		list = deptDAO.getDeptList(vo);
		
		for(int i=0;i<list.size();i++) {
			DeptVO deptVO = list.get(i);
			
			// 그룹 배정 사용자 리스트
			EmpVO empVO = new EmpVO();
			empVO.setDeptCode(deptVO.getDeptCode());
			empVO.setStartPage(0);
			empVO.setEndPage(99999);
			List<EmpVO> empList =  empDAO.getEmpList(empVO);
			deptVO.setUserList(empList);
			
			// 그룹중간관리자 조회
			UserGrpVO userGrpVO = new UserGrpVO();
			userGrpVO.setBizId(deptVO.getBizId());
			userGrpVO.setGrpType("D");
			userGrpVO.setRefId(deptVO.getDeptCode());
			userGrpVO.setStatusCode("RES");
			List<UserGrpVO> userGrpList = userDAO.getUserGrpList(userGrpVO);
			
			deptVO.setSubAdminList(userGrpList);
		}
		
		return list;
	}
	
	@Override
	public int getDeptListCount(DeptVO vo)  throws Exception {
		
		return deptDAO.getDeptListCount(vo);
	}
	
	@Override
	public int saveDept(String bizId, List<DeptVO> vo) throws Exception {
		int result = 0;
		
		for(int m=0;m<vo.size();m++) {
			DeptVO deptVO = vo.get(m);
			deptVO.setBizId(bizId);
			String deptCode = deptVO.getDeptCode();
			String masterDbMode = deptVO.getDbMode();

			// 그룹관리 db모드에 따라 데이터 처리
			if(masterDbMode.equals("C")) {
				deptVO.setPdeptCode("ROOT");
				deptVO.setDeptLvl("0");
				deptVO.setDeptOdr("0");
				deptDAO.insDept(deptVO);
				deptCode = deptVO.getDeptCode();
			} else if(masterDbMode.equals("U")) {				
				deptDAO.updDept(deptVO);
			} else if(masterDbMode.equals("D")) {
				deptDAO.delDept(deptVO);
				deptDAO.delDeptEmp(deptVO);
			}
			
			// 기업중간관리자 등록
			if(deptVO.getSubAdminList().size()>0) {
				for(int i=0;i<deptVO.getSubAdminList().size();i++) {
					UserGrpVO userGrpVO = new UserGrpVO();
					userGrpVO.setBizId(bizId);
					EmpVO empVO = new EmpVO();
					empVO.setBizId(bizId);
					if(deptVO.getSubAdminList().get(i).getDbMode().equals("C")) {
						userGrpVO.setUserId(deptVO.getSubAdminList().get(i).getUserId());
						userGrpVO.setGrpType("D");
						userGrpVO.setRefId(deptCode);
						userGrpVO.setStatusCode("RES");
						userDAO.insUserGrp(userGrpVO);
						// 중간관리자에게 부문 배정시 자신도 해당 부서에 귀속
						empVO.setUserId(deptVO.getSubAdminList().get(i).getUserId());
						empVO.setDeptCode(deptCode);
						empDAO.updEmp(empVO);
					} else if(deptVO.getSubAdminList().get(i).getDbMode().equals("D")) {
						userGrpVO.setUserId(deptVO.getSubAdminList().get(i).getUserId());
						userGrpVO.setGrpType("D");
						userGrpVO.setRefId(deptCode);
						userDAO.delUserGrp(userGrpVO);
						
						// 부문 삭제시 중간관리자에게 배정된 부문코드도 삭제
						empVO.setUserId(deptVO.getSubAdminList().get(i).getUserId());
						empVO.setDeptCode("-");
						empDAO.updEmp(empVO);
					}
				}
			}

			// 소속인원 배정
			if(deptVO.getUserList().size()>0) {
				for(int i=0;i<deptVO.getUserList().size();i++) {
					EmpVO empVO = new EmpVO();
					empVO.setBizId(bizId);
					if(deptVO.getUserList().get(i).getDbMode().equals("C")) {
						empVO.setUserId(deptVO.getUserList().get(i).getUserId());
						empVO.setDeptCode(deptCode);
						empDAO.updEmp(empVO);
					} else if(deptVO.getUserList().get(i).getDbMode().equals("U")) {
						empVO.setUserId(deptVO.getUserList().get(i).getUserId());
						empVO.setDeptCode(deptCode);
						empDAO.updEmp(empVO);
					} else if(deptVO.getUserList().get(i).getDbMode().equals("D")) {
						empVO.setUserId(deptVO.getUserList().get(i).getUserId());
						empVO.setDeptCode("-");
						empDAO.updEmp(empVO);
					}
				}			
			}
			
		}
		return result;
	}
	
	@Override
	public List<EmpVO> getDeptEmpList(DeptVO vo)  throws Exception {
		List<EmpVO> list = new ArrayList<EmpVO>();
		EmpVO empVO = new EmpVO();
		empVO.setBizId(vo.getBizId());
		empVO.setDeptCode(vo.getDeptCode());
		empVO.setUseYn(vo.getUseYn());
		list = empDAO.getDeptEmpList(empVO);
		
		return list;
		
	}

	@Override
	public void delMiddle(String bizId, DeptVO vo) throws Exception {

			// 기업중간관리자 배정삭제
				UserGrpVO userGrpVO = new UserGrpVO();
				userGrpVO.setBizId(bizId);
				EmpVO empVO = new EmpVO();
				empVO.setBizId(bizId);

				userGrpVO.setUserId(vo.getSubAdminList().get(0).getUserId());
				userGrpVO.setGrpType("D");
				userGrpVO.setRefId(vo.getDeptCode());
				userDAO.delUserGrp(userGrpVO);

				// 부문 삭제시 중간관리자에게 배정된 부문코드도 삭제
				empVO.setUserId(vo.getSubAdminList().get(0).getUserId());
				empVO.setDeptCode("-");
				empDAO.updEmp(empVO);
	}
}

