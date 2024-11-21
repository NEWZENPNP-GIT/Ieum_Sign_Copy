package com.ezsign.feb.worker.service;

import java.util.List;
import java.util.Map;

import com.ezsign.feb.master.vo.YP000VO;

public interface YP000Service {
	
	/**
	 * 근로정보 상세
	 * 
	 * @param vo
	 * @param userType
	 * @return
	 * @throws Exception
	 */
	public YP000VO getYP000(YP000VO vo, int userType) throws Exception;

	public List<YP000VO> getYP000List(YP000VO vo) throws Exception;
	
	public int getYP000ListCount(YP000VO vo) throws Exception;
		
	public void saveYP000Assign(List<YP000VO> list) throws Exception;
	
	/**
	 *
	 * 근로자 정보 등록
	 *
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> saveYP000(YP000VO vo) throws Exception;
	
	
}
