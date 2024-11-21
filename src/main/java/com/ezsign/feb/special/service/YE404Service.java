package com.ezsign.feb.special.service;

import java.util.List;

import com.ezsign.feb.special.vo.YE404VO;

public interface YE404Service {

	public List<YE404VO> getYE404List(YE404VO vo) throws Exception;

	public YE404VO getYE404ListCount(YE404VO vo) throws Exception;

	public void saveYE404(List<YE404VO> list) throws Exception;

	public int insYE404(YE404VO vo) throws Exception;

	public int updYE404(YE404VO vo) throws Exception;

	public int delYE404(YE404VO vo) throws Exception;

	public int allDelYE404(YE404VO vo) throws Exception;
}
