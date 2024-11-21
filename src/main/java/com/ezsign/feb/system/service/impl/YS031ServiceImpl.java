package com.ezsign.feb.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.system.dao.YS031DAO;
import com.ezsign.feb.system.service.YS031Service;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.framework.util.StringUtil;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("ys031Service")
public class YS031ServiceImpl extends AbstractServiceImpl implements YS031Service{

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="ys031DAO")
	private YS031DAO ys031DAO;

	@Resource(name="ye000DAO")
	private YE000DAO ye000DAO;	
	
	
	// 연말정산_부서등록 조회
	@Override
	public List<YS031VO> getYS031List(YS031VO vo)  throws Exception {
		
		List<YS031VO> list = ys031DAO.getYS031List(vo);
		
		return list;
	}
	
	// 연말정산_부서등록 갯수
	@Override
	public int getYS031ListCount(YS031VO vo) throws Exception {
		return ys031DAO.getYS031ListCount(vo);
	}
	
	// 연말정산_부서등록 부서명 조회
	public YS031VO getYS031(YS031VO vo) throws Exception {
		return ys031DAO.getYS031(vo);
	}
	
	// 연말정산_부서등록 입력
	@Override
	public void saveYS031(List<YS031VO> list) throws Exception {
		
		for(int i=0;i<list.size();i++) {
			YS031VO ys031VO = list.get(i);
			
			if(ys031VO.getDbMode().equals("C")) {
				// 사업장 부서명 조회
				YS031VO resultVO = ys031DAO.getYS031(ys031VO);
				
				if(resultVO==null) {
					ys031DAO.insYS031(ys031VO);
				}
			} else if(ys031VO.getDbMode().equals("U")) {
				ys031DAO.updYS031(ys031VO);
			} else if(ys031VO.getDbMode().equals("D")) {
				// 해당 계약ID에 삭제할 부서ID에 등록된 사용자 존재시 삭제 불가처리
				YE000VO ye000VO = new YE000VO();
				ye000VO.set계약ID(ys031VO.get계약ID());
				ye000VO.set부서ID(ys031VO.get부서ID());
				List<YE000VO> ye000List = ye000DAO.getYE000List(ye000VO);
				if(ye000List.size()==0) {
					ys031DAO.delYS031(ys031VO);
				}
			}
		}
		
		return;
	}
	
}
