package com.ezsign.feb.easyFeb.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ezsign.feb.easyFeb.vo.YE006VO;

public interface YE006Service {

	public YE006VO getYE006(Map<String, String> params);

	public int saveYE006(YE006VO vo, HttpServletRequest request, String bizId);
	
	public int delYE006(Map<String, String> params);

	public void saveYE006List(List<YE006VO> ye006voList);
	
	public List<YE006VO> getYE006List(Map params);

	public int getYE006ListCount(Map params);
	
	public YE006VO getYE006Sum(YE006VO ye006VO);
}
