package com.ezsign.feb.worker.service;

import java.util.List;

import com.ezsign.feb.worker.vo.YE040VO;

public interface YE040Service {
	
	public YE040VO getYE040(YE040VO vo) throws Exception;
	
	public List<YE040VO> getYE040List(YE040VO vo) throws Exception;

	public void insYE040(YE040VO vo) throws Exception;

	public void saveYE040(List<YE040VO> list);

	public List<YE040VO> getYE040WorkerList(YE040VO vo);

	public int getYE040WorkerListCount(YE040VO vo);

	public List<YE040VO> getYE040View(List<YE040VO> list);
	
}
