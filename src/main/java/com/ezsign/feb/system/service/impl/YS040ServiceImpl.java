package com.ezsign.feb.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.feb.system.dao.YS040DAO;
import com.ezsign.feb.system.service.YS040Service;
import com.ezsign.feb.system.vo.YS040VO;

@Service("ys040Service")
public class YS040ServiceImpl implements YS040Service {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="ys040DAO")
	private YS040DAO ys040DAO;
	
	// 연말정산_중간관리자설정 조회
	@Override
	public YS040VO getYS040(YS040VO vo)  throws Exception {
		YS040VO result = new YS040VO();
		
		List<YS040VO> list =  ys040DAO.getYS040List(vo);
		if(list.size()>0) {
			result = list.get(0);
		} else {
			vo.set급여숨기기("0");
			vo.set최종확정기능("0");
			vo.set근로자출력설정_원천징수영수증("0");
			vo.set근로자출력설정_기부금명세서("0");
			vo.set근로자출력설정_소득공제신고서("0");
			vo.set근로자출력설정_의료비명세서("0");			
			ys040DAO.insYS040(vo);
			result = vo;
		}
		return result;
	}
	
	// 연말정산_중간관리자설정  입력
	@Override
	public void insYS040(YS040VO vo) throws Exception {
		ys040DAO.insYS040(vo);
	}

	// 연말정산_중간관리자설정 수정
	@Override
	public int updYS040(YS040VO vo) throws Exception {
		return ys040DAO.updYS040(vo);
	}

}
