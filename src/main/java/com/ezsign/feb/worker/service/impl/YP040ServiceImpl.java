package com.ezsign.feb.worker.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.worker.dao.YP040DAO;
import com.ezsign.feb.worker.service.YP040Service;
import com.ezsign.feb.worker.vo.YP040VO;
import com.ezsign.framework.util.StringUtil;

@Service("yp040Service")
public class YP040ServiceImpl implements YP040Service {
 
	@Resource(name="yp040DAO")
	private YP040DAO yp040DAO;
	
	@Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;
	
	/**
	 * 간이지급명세서 소득명세합계 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Override
	public YP040VO getYP040(YP040VO vo) throws Exception {
		YP040VO resultVO = yp040DAO.getYP040(vo);
		
		return resultVO;
	}
	
	// 간이지급명세서 소득명세합계 주(현)/종(전) 근무지 항목 조회
	@Override
	public List<YP040VO> getYP040List(YP040VO vo) throws Exception {
		List<YP040VO> list = yp040DAO.getYP040List(vo);
		
		return list;
	}
	
	// 간이지급명세서 소득명세합계 입력
	@Override
	public void insYP040(YP040VO vo) throws Exception {
		// 연말정산_소득명세합계 삭제
		yp040DAO.delYP040(vo);
		
		// 연말정산_소득명세합계 등록
		yp040DAO.insYP040(vo);
		
		
		//진행상태 로그 저장
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
		
}
