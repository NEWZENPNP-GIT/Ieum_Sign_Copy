package com.ezsign.feb.system.service;

import java.util.List;

import com.ezsign.feb.system.vo.YS050VO;

public interface YS050Service {
	
	public YS050VO getYS050(YS050VO vo)  throws Exception;

	public void insYS050(YS050VO vo)  throws Exception;
	
	public int updYS050(YS050VO vo) throws Exception;
	
	public int delYS050(YS050VO vo) throws Exception;
}
