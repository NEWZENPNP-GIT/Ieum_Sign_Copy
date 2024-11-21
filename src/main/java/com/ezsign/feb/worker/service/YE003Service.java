package com.ezsign.feb.worker.service;

import com.ezsign.feb.master.vo.YE000VO;

import java.util.List;

public interface YE003Service {

	public void insYE003(YE000VO vo) throws Exception;

	public int updYE003(YE000VO vo) throws Exception;

	public int delYE003(YE000VO vo) throws Exception;

	public List<YE000VO> getYE003SumList(YE000VO vo) throws Exception;

	public List<YE000VO> getYE003TaxList(YE000VO vo) throws Exception;

	public void saveTaxOverSum(List<YE000VO> list) throws Exception;
}
