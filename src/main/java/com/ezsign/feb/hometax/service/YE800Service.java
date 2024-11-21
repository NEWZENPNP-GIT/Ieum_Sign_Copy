package com.ezsign.feb.hometax.service;

import java.util.List;
import java.util.Map;

import com.ezsign.feb.hometax.vo.YE800VO;
import com.ezsign.framework.vo.SessionVO;

public interface YE800Service {
	
	/*
	 * 연말정산_전자신고대상 사업장 조회
	 */
	public List<YE800VO> getYE800List(YE800VO vo) throws Exception;
	
	
	/**
	 *
	 * 국세청 전산매체 문서 작성
	 *
	 * @throws Exception
	 */
	public Map<String,Object> makeElecDocument(List<YE800VO> ye800List, SessionVO loginVO, String 계약년도) throws Exception;
	
}
