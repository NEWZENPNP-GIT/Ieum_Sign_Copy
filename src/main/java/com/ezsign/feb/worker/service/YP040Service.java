package com.ezsign.feb.worker.service;

import java.util.List;

import com.ezsign.feb.worker.vo.YE040VO;
import com.ezsign.feb.worker.vo.YP040VO;

public interface YP040Service {

	/**
	 * 소득명세합계 상세조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public YP040VO getYP040(YP040VO vo) throws Exception;
	
	
	/**
	 * 간이지급명세서 소득명세합계 주(현)/종(전) 근무지 항목 조회
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public List<YP040VO> getYP040List(YP040VO vo) throws Exception;
	
	/**
	 * 간이지급명세서 소득명세합계 등록
	 * 
	 * @param vo
	 * @throws Exception
	 */
	public void insYP040(YP040VO vo) throws Exception;
	
}
