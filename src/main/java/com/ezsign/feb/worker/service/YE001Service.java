package com.ezsign.feb.worker.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.vo.YW413Request;

public interface YE001Service {

	public List<YE001VO> getYE001(YE001VO vo, int userType) throws Exception;

	public List<YE001VO> getYE001List(YE001VO vo, int userType) throws Exception;

	public int getYE001ListCount(YE001VO vo) throws Exception;

	public void saveYE001(YW413Request body) throws Exception;

	public void insYE001(YE001VO vo) throws Exception;

	public int updYE001(YE001VO vo) throws Exception;

	public int delYE001(YE001VO vo) throws Exception;

	public YE001VO getYE001ID(YE001VO vo) throws Exception;
	
	/**
	 * 부양가족 나이정보를 일괄 재집계
	 * 
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public int allFamilyAge(YE001VO vo, String 계약년도) throws Exception;
}
