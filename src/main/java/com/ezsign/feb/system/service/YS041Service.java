package com.ezsign.feb.system.service;

import java.util.List;

import com.ezsign.feb.system.vo.YS041VO;


public interface YS041Service {
	
	public List<YS041VO> getYS041List(YS041VO vo)  throws Exception;
	
	public int getYS041ListCount(YS041VO vo) throws Exception;
	
	public YS041VO getYS041(YS041VO vo) throws Exception;
	
	public List<YS041VO> getYS041DeptList(YS041VO vo) throws Exception;
	
	public int insYS041(YS041VO vo) throws Exception;
	
	public int delYS041(YS041VO vo) throws Exception;

	public void saveYS041(List<YS041VO> list)  throws Exception;
}
