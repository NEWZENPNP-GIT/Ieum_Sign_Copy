package com.ezsign.feb.master.service;

import java.util.List;

import com.ezsign.feb.master.vo.YE900VO;

public interface YE900Service {

	public YE900VO getYE900(YE900VO vo) throws Exception;

	public void saveYE900(List<YE900VO> list) throws Exception;

	public void insYE900(YE900VO vo) throws Exception;

	public int delYE900(YE900VO vo) throws Exception;
}
