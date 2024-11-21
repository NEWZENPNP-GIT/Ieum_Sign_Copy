package com.ezsign.feb.worker.service;

import java.util.List;

import com.ezsign.feb.master.vo.YP000VO;

public interface YP003Service {

	/**
	 * 간이지급명세서_원천명세(급여정보) 일괄 처리
	 * 
	 * @param list
	 * @throws Exception
	 */
	public void saveTaxOverSumPayment(List<YP000VO> list) throws Exception; 
	
}
