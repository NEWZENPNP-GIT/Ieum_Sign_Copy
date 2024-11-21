package com.ezsign.statistics.service.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.statistics.dao.MainGraphDAO;
import com.ezsign.statistics.service.MainGraphService;
import com.ezsign.statistics.vo.MainGraphVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("mainGraphService")
public class MainGraphServiceImpl extends AbstractServiceImpl implements MainGraphService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="mainGraphDAO")
	private MainGraphDAO mainGraphDAO;
	
	public static void main(String[] args) {
		
	}
	
	// 메인그래프 근로계약 조회
	@Override
	public List<MainGraphVO> getMainGraphist(MainGraphVO vo) throws Exception {
		List<MainGraphVO> resultList = new ArrayList<MainGraphVO>();
		
		List<MainGraphVO> dataList = mainGraphDAO.getMainGraphist(vo);
		
		for(int i=1;i<13;i++) {
			MainGraphVO mainGraphVO = new MainGraphVO();
			// 1~12월까지 설정
			mainGraphVO.setViewMonth(Integer.toString(i));
			
			for(int j=0;j<dataList.size();j++) {				
				MainGraphVO monthVO =  dataList.get(j);
				if(monthVO.getViewMonth().equals(Integer.toString(i))) {
					mainGraphVO.setV1(monthVO.getV1());
					mainGraphVO.setV2(monthVO.getV2());
					mainGraphVO.setV3(monthVO.getV3());
					mainGraphVO.setV4(monthVO.getV4());
					mainGraphVO.setV5(monthVO.getV5());					
				}
			}
			resultList.add(mainGraphVO);
		}
		
		return resultList;		
	}
	
}
