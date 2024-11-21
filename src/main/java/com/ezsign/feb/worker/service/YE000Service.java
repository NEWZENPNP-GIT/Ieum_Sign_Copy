package com.ezsign.feb.worker.service;

import java.util.List;
import java.util.Map;

import com.ezsign.feb.master.vo.YE000VO;

public interface YE000Service {
	
	public YE000VO getYE000(YE000VO vo, int userType) throws Exception;
	
	public List<YE000VO> getYE000List(YE000VO vo) throws Exception;
	
	public int getYE000ListCount(YE000VO vo) throws Exception;

	
	/**
	 *
	 * 근로자 정보 등록
	 *
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> saveYE000(YE000VO vo) throws Exception;
	
	public int insYE000(YE000VO vo) throws Exception;
	
	public int updYE000(YE000VO vo) throws Exception;
	
	public int delYE000(YE000VO vo) throws Exception;

	public List<YE000VO> getWorkerAssistList(YE000VO vo) throws Exception;
	
	public void saveYE000Assign(List<YE000VO> list) throws Exception;

	public int getYE000ChkEdit(YE000VO vo) throws Exception;

	public int getYE000AdminChkEdit(YE000VO vo) throws Exception;
	
	/**
	 *
	 * 계약정보 상세조회
	 *
	 * @param paramsVO
	 * @return
	 * @throws Exception
	 */
	public YE000VO getYE000DataDetail(YE000VO paramsVO) throws Exception;
	

}
