package com.ezsign.feb.system.service;

import java.util.List;

import com.ezsign.feb.system.vo.YS000VO;

public interface YS000Service {
	
	public List<YS000VO> getYS000List(YS000VO vo)  throws Exception;
	
	public void insYS000(YS000VO vo) throws Exception;
	
}
