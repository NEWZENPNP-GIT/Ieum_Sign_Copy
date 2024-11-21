package com.ezsign.feb.hometax.service;

import java.util.List;
import java.util.Map;

import com.ezsign.feb.hometax.vo.YE800VO;
import com.ezsign.framework.vo.SessionVO;

public interface YE801Service {

	/**
	 * 의료비 지급명세서 생성
	 * 
	 * @param ye800List
	 * @param loginVO
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> makeElecDocument(List<YE800VO> ye800List, SessionVO loginVO, String 계약년도) throws Exception;
	
}
