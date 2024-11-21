package com.ezsign.feb.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.feb.system.dao.YS041DAO;
import com.ezsign.feb.system.service.YS041Service;
import com.ezsign.feb.system.vo.YS041VO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("ys041Service")
public class YS041ServiceImpl extends AbstractServiceImpl implements YS041Service{

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="ys041DAO")
	private YS041DAO ys041DAO;
	
	// 중간관리자 관리부서 조회
	@Override
	public List<YS041VO> getYS041List(YS041VO vo)  throws Exception {
		
		List<YS041VO> list = ys041DAO.getYS041List(vo);
		
		return list;
	}
	
	// 중간관리자 관리부서 조회 갯수
	@Override
	public int getYS041ListCount(YS041VO vo) throws Exception {
		return ys041DAO.getYS041ListCount(vo);
	}
	
	// 중간관리자 관리부서 상세조회
	@Override
	public YS041VO getYS041(YS041VO vo) throws Exception {
		return ys041DAO.getYS041(vo);
	}

	@Override
	public List<YS041VO> getYS041DeptList(YS041VO vo) throws Exception {
		List<YS041VO> list = ys041DAO.getYS041DeptList(vo);
		
		return list;
	}
	
	// 중간관리자 관리부서 입력
	@Override
	public int insYS041(YS041VO vo) throws Exception {
		int result = 0;
		
		vo.setStartPage(0);
		vo.setEndPage(100);
		List<YS041VO> list = ys041DAO.getYS041List(vo);
		
		if(list.size() == 0) {
			ys041DAO.insYS041(vo);
			result++;
		}
		
		return result;
	}

	// 중간관리자 관리부서 삭제
	@Override
	public int delYS041(YS041VO vo) throws Exception {
		return ys041DAO.delYS041(vo);
	}
	
	// 중간관리자 관리부서 등록
	@Override
	public void saveYS041(List<YS041VO> list) throws Exception {
		for(int i=0; i<list.size(); i++) {
			YS041VO ys041VO = list.get(i);
			
			if("C".equals(ys041VO.getDbMode())) {
				ys041DAO.insYS041(ys041VO);
			} else if("D".equals(ys041VO.getDbMode())) {
				ys041DAO.delYS041(ys041VO);
			}
		}
		
	}

}
