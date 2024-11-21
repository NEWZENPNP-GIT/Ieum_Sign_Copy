package com.ezsign.feb.worker.service;

import java.util.List;

import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.worker.vo.YE002VO;

public interface YE002Service {
		
	public List<YE002VO> getYE002List(YE002VO vo) throws Exception;

	public int getYE002ListCount(YE002VO vo) throws Exception;
	
	public YE002VO getYE002(YE002VO vo) throws Exception;

	public void saveYE002(YE000VO vo) throws Exception;
	
	public int insYE002(YE000VO vo) throws Exception;
	
	public int updYE002(YE000VO vo) throws Exception;
	
	public int delYE002(YE000VO vo) throws Exception;
	
	public int getYE002ChkEdit(YE002VO vo) throws Exception;

}
