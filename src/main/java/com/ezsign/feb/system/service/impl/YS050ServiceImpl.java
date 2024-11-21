package com.ezsign.feb.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.feb.system.dao.YS050DAO;
import com.ezsign.feb.system.service.YS050Service;
import com.ezsign.feb.system.vo.YS010VO;
import com.ezsign.feb.system.vo.YS050VO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("ys050Service")
public class YS050ServiceImpl extends AbstractServiceImpl implements YS050Service{

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="ys050DAO")
	private YS050DAO ys050DAO;
	
	// 연말정산_급여항목 조회
	@Override
	public YS050VO getYS050(YS050VO vo)  throws Exception {
		YS050VO result =  ys050DAO.getYS050(vo);
		
		return result;
	}
	// 연말정산_급여항목 입력
	@Override
	public void insYS050(YS050VO vo) throws Exception {
		
		// 데이터 존재여부확인 (계약ID)
		System.out.println("계약ID:" + vo.get계약ID());
		
		YS050VO result = ys050DAO.getYS050(vo);
		
		if (result != null) {
			ys050DAO.updYS050(vo);
		} else {
			ys050DAO.insYS050(vo);
		}
		
	}

	// 연말정산_급여항목 수정
	@Override
	public int updYS050(YS050VO vo) throws Exception {
		return ys050DAO.updYS050(vo);
	}

	// 연말정산_급여항목 삭제
	@Override
	public int delYS050(YS050VO vo) throws Exception {
		// TODO Auto-generated method stub
		return ys050DAO.delYS050(vo);
	}

}
