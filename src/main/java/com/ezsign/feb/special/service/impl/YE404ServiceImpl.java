package com.ezsign.feb.special.service.impl;

import com.ezsign.feb.special.dao.YE404DAO;
import com.ezsign.feb.special.dao.YE405DAO;
import com.ezsign.feb.special.service.YE404Service;
import com.ezsign.feb.special.vo.YE404VO;
import com.ezsign.feb.system.dao.YS000DAO;
import com.ezsign.feb.system.vo.YS000VO;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("ye404Service")
public class YE404ServiceImpl extends AbstractServiceImpl implements YE404Service {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "ye404DAO")
	private YE404DAO ye404DAO;

	@Resource(name = "ye405DAO")
	private YE405DAO ye405DAO;
	
	@Resource(name = "ys000DAO")
	private YS000DAO ys000DAO;
	
	// 연말정산_사내기부금 조회
	@Override
	public List<YE404VO> getYE404List(YE404VO vo) throws Exception {
		List<YE404VO> list = ye404DAO.getYE404List(vo);
		return list;
	}

	// 연말정산_사내기부금 갯수
	@Override
	public YE404VO getYE404ListCount(YE404VO vo) throws Exception {
		return ye404DAO.getYE404ListCount(vo);
	}

	// 연말정산_사내기부금 입력
	@Override
	public void saveYE404(List<YE404VO> list) throws Exception {
		for (YE404VO vo : list) {
//			if (vo.getDbMode().equals("C")) {
//				ye404DAO.insYE404(vo);
//			} else if (vo.getDbMode().equals("U")) {
//				ye404DAO.updYE404(vo);
//			} else if (vo.getDbMode().equals("D")) {
//				ye404DAO.delYE404(vo);
//			}
			if ("D".equals(vo.getDbMode())) {
				ye404DAO.updYE404Disable(vo);
			} else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
				ye404DAO.updYE404Disable(vo);
				ye404DAO.insYE404(vo);
				
				// 연말정산 당해연도 조회
				YS000VO ys000VO = new YS000VO();
				ys000VO.set계약ID(vo.get계약ID());
				ys000VO.setBizId(vo.getBizId());
				
				List<YS000VO> ys000List = ys000DAO.getYS000List(ys000VO);
				YS000VO ys000sel = new YS000VO();
				if(ys000List!=null && ys000List.size()>0) {
					ys000sel = ys000List.get(0);
				}
			}
		}
	}

	// 연말정산_사내기부금 입력
	@Override
	public int insYE404(YE404VO vo) throws Exception {
		ye404DAO.insYE404(vo);
		return 1;
	}

	// 연말정산_사내기부금 수정
	@Override
	public int updYE404(YE404VO vo) throws Exception {
		// return ye404DAO.updYE404(vo);
		ye404DAO.updYE404Disable(vo);
		ye404DAO.insYE404(vo);
		return 1;
	}

	// 연말정산_사내기부금 삭제
	// 연말정산_사내기부금 사용여부 '0'
	@Override
	public int delYE404(YE404VO vo) throws Exception {
		// return ye404DAO.delYE404(vo);
		return ye404DAO.updYE404Disable(vo);
	}

	// 연말정산_사내기부금 전체삭제
	@Override
	public int allDelYE404(YE404VO vo) throws Exception {
		return ye404DAO.allDelYE404(vo);
	}
}
