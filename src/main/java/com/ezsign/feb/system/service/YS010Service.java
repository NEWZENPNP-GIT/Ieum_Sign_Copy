package com.ezsign.feb.system.service;

import java.util.List;

import com.ezsign.feb.system.vo.YS010VO;

public interface YS010Service {
	
	public List<YS010VO> getYS010List(YS010VO vo)  throws Exception;
	
	public void insYS010(YS010VO vo) throws Exception;
	
	public int updYS010(YS010VO vo, String bizId, String febYear) throws Exception;
	
	public int getWorkerDateCheck(YS010VO vo);
}
