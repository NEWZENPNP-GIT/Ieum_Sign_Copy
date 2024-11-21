package com.ezsign.window.service;

import java.util.List;

import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.worker.vo.YW900VO;

public interface YW900Service {

	
	public List<YW900VO> getYW900List(YW900VO vo)  throws Exception;

	public int getYW900ListCount(YW900VO vo)  throws Exception;
	

	public List<YS030VO> getYS030List(YS030VO vo)  throws Exception;
	

}
