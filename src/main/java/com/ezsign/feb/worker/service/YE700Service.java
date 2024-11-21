package com.ezsign.feb.worker.service;

import java.util.List;

import com.ezsign.feb.worker.vo.YE700VO;

public interface YE700Service {

	public List<YE700VO> getYE700WorkerList(YE700VO vo);

	public int getYE700WorkerListCount(YE700VO vo);
	
}
