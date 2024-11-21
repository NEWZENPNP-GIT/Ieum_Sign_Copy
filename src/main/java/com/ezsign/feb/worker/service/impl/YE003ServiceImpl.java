package com.ezsign.feb.worker.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.worker.dao.YE003DAO;
import com.ezsign.feb.worker.service.YE003Service;
import com.ezsign.feb.worker.vo.YE020VO;
import com.ezsign.framework.util.DateUtil;

@Service("ye003Service")
public class YE003ServiceImpl implements YE003Service {

	@Resource(name="codeDAO")
	private CodeDAO codeDAO;

	@Resource(name="ye003DAO")
	private YE003DAO ye003DAO;
	
	@Resource(name="ye000DAO")
	private YE000DAO ye000DAO;
	
	// 연말정산_원천명세(급여정보) 입력
	@Override
	public void insYE003(YE000VO vo) throws Exception {		
		ye003DAO.insYE003(vo);
	}

	// 연말정산_원천명세(급여정보) 수정
	@Override
	public int updYE003(YE000VO vo) throws Exception {
		return ye003DAO.updYE003(vo);
	}

	// 연말정산_원천명세(급여정보) 삭제
	@Override
	public int delYE003(YE000VO vo) throws Exception {
		return ye003DAO.delYE003(vo);
	}

	// 연말정산 회사자료 합계 조회
	@Override
	public List<YE000VO> getYE003SumList(YE000VO vo) throws Exception {
		return ye003DAO.getYE003SumList(vo);
	}

	// 연말정산 종근무지 기납부세액 조회
	@Override
	public List<YE000VO> getYE003TaxList(YE000VO vo) throws Exception {
		return ye003DAO.getYE003TaxList(vo);
	}

	// 연말정산_원천명세(급여정보) 일괄 처리
	@Override
	public void saveTaxOverSum(List<YE000VO> list) throws Exception {
		for(int i=0; i<list.size(); i++) {
			YE000VO ye000VO = list.get(i);
			if ("C".equals(ye000VO.getDbMode())) {
				ye003DAO.insYE003(ye000VO);
			}
			else if("U".equals(ye000VO.getDbMode())) {
				ye003DAO.updYE003(ye000VO);
			} else if("D".equals(ye000VO.getDbMode())) {
				ye003DAO.delYE003(ye000VO);
			}
		}
		
	}

}
