package com.ezsign.feb.hometax.service;

import java.util.List;
import java.util.Map;

import com.ezsign.feb.hometax.vo.YP800VO;
import com.ezsign.framework.vo.SessionVO;

public interface YP800Service {

	/**
	 * 간이지급명세서 전자매체 생성
	 * 
	 * @param yp800List
	 * @param loginVO
	 * @param 계약년도
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> makeElecDocument(List<YP800VO> yp800List, SessionVO loginVO, String 계약년도) throws Exception;
	
	
	/**
	 * 전자신고 정보 등록
	 * 
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public void insertYP800(YP800VO paramVO) throws Exception;
	
	
	/*
	 * 간이지급명세서_전자신고대상 사업장 조회
	 */
	public List<YP800VO> getYP800List(YP800VO paramVO) throws Exception;
	
}
