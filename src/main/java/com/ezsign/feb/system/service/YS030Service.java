package com.ezsign.feb.system.service;

import java.util.List;

import com.ezsign.feb.system.vo.YS030VO;


public interface YS030Service {
	
	public List<YS030VO> getYS030List(YS030VO vo)  throws Exception;

	public int getYS030ListCount(YS030VO vo)  throws Exception;
	
	public void insYS030(YS030VO vo) throws Exception;
	
	public int updYS030(YS030VO vo) throws Exception;

	public int delYS030(YS030VO vo) throws Exception;
	
	public String getYS030TaxNumber(YS030VO vo)  throws Exception;
}
