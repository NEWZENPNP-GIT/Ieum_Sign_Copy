package com.ezsign.feb.worker.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.worker.dao.YE040DAO;
import com.ezsign.feb.worker.service.YE040Service;
import com.ezsign.feb.worker.vo.YE040VO;
import com.ezsign.framework.util.StringUtil;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("ye040Service")
public class YE040ServiceImpl extends AbstractServiceImpl implements YE040Service {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="codeDAO")
	private CodeDAO codeDAO;

	@Resource(name="ye040DAO")
	private YE040DAO ye040DAO;

	@Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;
	
	// 연말정산 소득명세합계 조회
	@Override
	public YE040VO getYE040(YE040VO vo) throws Exception {
		YE040VO resultVO = ye040DAO.getYE040(vo);
		
		return resultVO;
	}
	
	// 연말정산 소득명세합계 주(현)/종(전) 근무지 항목 조회
	@Override
	public List<YE040VO> getYE040List(YE040VO vo) throws Exception {
		List<YE040VO> list = ye040DAO.getYE040List(vo);
		
		return list;
	}
	
	// 연말정산 소득명세합계 입력
	@Override
	public void insYE040(YE040VO vo) throws Exception {
		// 연말정산_소득명세합계 삭제
		ye040DAO.delYE040(vo);
		
		// 연말정산_소득명세합계 등록
		ye040DAO.insYE040(vo);
		
		YE901VO selYE901VO = new YE901VO();
		selYE901VO.set계약ID(vo.get계약ID());
		selYE901VO.set사용자ID(vo.get사용자ID());
		selYE901VO.set작업자ID(vo.get작업자ID());
		selYE901VO.set진행현황코드("11");	// 소득명세 합계
		
		// 근로자 진행관리현황 등록
		if (StringUtil.isNotNull(vo.get계약ID()) && StringUtil.isNotNull(vo.get사용자ID())) {
			ye901DAO.insYE901(selYE901VO);
		}
	}

	// 연말정산 소득명세합계 선택 및 일괄 입력
	@Override
	public void saveYE040(List<YE040VO> list) {
		
		for (YE040VO ye040VO : list) {
			
			// 연말정산_소득명세합계 삭제
			ye040DAO.delYE040(ye040VO);
		
			// 연말정산_소득명세합계 등록
			ye040DAO.insYE040(ye040VO);
			
			YE901VO selYE901VO = new YE901VO();
			selYE901VO.set계약ID(ye040VO.get계약ID());
			selYE901VO.set사용자ID(ye040VO.get사용자ID());
			selYE901VO.set작업자ID(ye040VO.get작업자ID());
			selYE901VO.set진행현황코드("11");	// 소득명세 합계
			
			// 근로자 진행관리현황 등록
			if (StringUtil.isNotNull(ye040VO.get계약ID()) && StringUtil.isNotNull(ye040VO.get사용자ID())) {
				ye901DAO.insYE901(selYE901VO);
			}
			
		}
		
	}

	// 연말정산_소득명세합계 리스트 조회
	@Override
	public List<YE040VO> getYE040WorkerList(YE040VO vo) {
		List<YE040VO> list = ye040DAO.getYE040WorkerList(vo);
		return list;
	}

	// 연말정산_소득명세합계 리스트 조회 개수
	@Override
	public int getYE040WorkerListCount(YE040VO vo) {
		return ye040DAO.getYE040WorkerListCount(vo);
	}

	// 연말정산 소득명세합계 선택 일괄 조회
	@Override
	public List<YE040VO> getYE040View(List<YE040VO> list) {
		
		for (int i=0; i < list.size(); i++) {
			YE040VO ye040VO = list.get(i);
			YE040VO result = new YE040VO();
			result = ye040DAO.getYE040(ye040VO);
			List<YE040VO> resultList = ye040DAO.getYE040List(ye040VO);
			result.set근무지별소득명세금액(resultList);
			result.set근무지별소득명세금액개수(resultList.size()); 
			
			list.set(i, result);
		}
		
		return list;
	}
}
