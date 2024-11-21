package com.ezsign.feb.system.service;

import java.util.List;

import com.ezsign.feb.system.vo.YS040VO;


public interface YS040Service {
	
	public YS040VO getYS040(YS040VO vo)  throws Exception;
	
	public void insYS040(YS040VO vo) throws Exception;
	
	public int updYS040(YS040VO vo) throws Exception;

}
