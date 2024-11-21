package com.ezsign.feb.system.service;

import java.util.List;

import com.ezsign.feb.system.vo.YS031VO;

public interface YS031Service {
	
	public List<YS031VO> getYS031List(YS031VO vo)  throws Exception;

	public int getYS031ListCount(YS031VO vo) throws Exception;
	
	public YS031VO getYS031(YS031VO vo) throws Exception;
	
	public void saveYS031(List<YS031VO> list)  throws Exception;
	
}
