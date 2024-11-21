package com.ezsign.feb.worker.service;

import java.util.List;

import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.special.vo.YE402VO;
import com.ezsign.feb.special.vo.YE403VO;
import com.ezsign.feb.worker.vo.YE013VO;

public interface YE013Service {
	
	public List<YE013VO> getYE013List(YE013VO vo) throws Exception;

	public YE013VO getYE013ListCount(YE013VO vo) throws Exception;
	
	public void saveYE013(List<YE013VO> list) throws Exception;
	
	public int insYE013(YE013VO vo) throws Exception;
	
	public int updYE013(YE013VO vo) throws Exception;
	
	public int delYE013(YE013VO vo) throws Exception;

	//의료비
	public YE402VO setYE402(YE013VO ye013VO);
	
	//교육비
	public YE403VO setYE403(YE013VO ye013VO);
	
	//신용카드
	public YE108VO setYE108(YE013VO ye013VO);
	
}
