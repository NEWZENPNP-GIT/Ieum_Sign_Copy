package com.ezsign.feb.master.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.dao.YP000DAO;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.master.vo.YP000VO;
import com.ezsign.feb.system.dao.YS010DAO;
import com.ezsign.feb.worker.dao.YE040DAO;
import com.ezsign.feb.worker.dao.YP040DAO;
import com.ezsign.feb.worker.vo.YE040VO;
import com.ezsign.feb.worker.vo.YP040VO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("febMasterService")
public class FebMasterServiceImpl extends AbstractServiceImpl implements FebMasterService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "codeDAO")
	private CodeDAO codeDAO;

	@Resource(name = "ys010DAO")
	private YS010DAO ys010DAO;

	@Resource(name = "ye000DAO")
	private YE000DAO ye000DAO;

	@Resource(name = "ye040DAO")
	private YE040DAO ye040DAO;

	@Resource(name = "ye901DAO")
	private YE901DAO ye901DAO;

	@Resource(name = "yp000DAO")
	private YP000DAO yp000DAO;
	
	@Resource(name = "yp040DAO")
	private YP040DAO yp040DAO;
	
	// 연말정산_관리자_진행관리
	@Override
	public List<YE000VO> getUserStatusList(YE000VO vo) throws Exception {
		List<YE000VO> list = ye000DAO.getUserStatusList(vo);

		return list;
	}

	// 연말정산_관리자_진행관리 개수
	@Override
	public int getUserStatusListCount(YE000VO vo) throws Exception {
		return ye000DAO.getUserStatusListCount(vo);
	}
	
	// 연말정산_관리자_진행관리_통계용
	@Override
	public YE000VO getUserStatusSum(YE000VO vo) throws Exception {
		YE000VO ye000VO = new YE000VO();

		List<YE000VO> list = ye000DAO.getUserStatusSum(vo);
		if (list.size() > 0) {
			ye000VO = list.get(0);
		}

		return ye000VO;
	}

	// 연말정산_근로자_근무년월
	@Override
	public YE000VO getUserWorkMonth(YE000VO vo) throws Exception {
		YE000VO ye000VO = new YE000VO();
		YE040VO ye040VO = new YE040VO();
		ye040VO.set계약ID(vo.get계약ID());
		ye040VO.set사용자ID(vo.get사용자ID());
		YE040VO resultYE040VO = ye040DAO.getYE040(ye040VO);

		List<YE000VO> list = ye000DAO.getUserWorkMonth(vo);
		if (list.size() > 0) {
			ye000VO = list.get(0);
			if (resultYE040VO != null) {
				ye000VO.setEmpName(resultYE040VO.get성명());
				ye000VO.set총급여(resultYE040VO.get총급여());
				ye000VO.set근로소득금액(resultYE040VO.get근로소득금액());
			}
		}

		return ye000VO;
	}

	// 연말정산_관리자_진행관리_일괄_근로자1차확정
	@Override
	public int setYE000AllConfirm(YE000VO vo) throws Exception {
		int result = 0;
		YE000VO allVO = new YE000VO();
		allVO.set계약ID(vo.get계약ID());
		allVO.set진행상태코드("2");
		result = ye000DAO.updYE000(allVO);

		return result;
	}

	// 연말정산_관리자_진행관리_근로자_확정취소
	@Override
	public int setYE000Confirm(YE000VO vo) throws Exception {
		int result = 0;

		YE000VO ye000VO = new YE000VO();
		ye000VO.set계약ID(vo.get계약ID());
		ye000VO.set사용자ID(vo.get사용자ID());
		ye000VO.set진행상태코드("1");
		
		result = ye000DAO.getYE000NotModified(ye000VO);

		if(result==0) {
			result = ye000DAO.updYE000(ye000VO);
		} else {
			result = -100;
		}

		return result;
	}

	// 연말정산_사용자_진행현황_날짜
	@Override
	public List<YE901VO> getUserStatusDate(YE000VO vo) throws Exception {
		List<YE901VO> list = ye901DAO.getUserStatusDate(vo);

		return list;
	}
	
	//간이지급명세서 
	@Override
	public List<YP000VO> getPaymentUserStatusList(YP000VO vo) throws Exception {
		List<YP000VO> list = yp000DAO.getPaymentUserStatusList(vo);

		return list;
	}
	//간이지급명세서 
	@Override
	public int getPaymentUserStatusListCount(YP000VO vo) throws Exception {
		return yp000DAO.getPaymentUserStatusListCount(vo);
	}
	
	// 간이지급명세서_근로자_근무년월
	@Override
	public YP000VO getPaymentUserWorkMonth(YP000VO vo) throws Exception {
		YP000VO yp000VO = new YP000VO();
		YP040VO yp040VO = new YP040VO();
		yp040VO.set계약ID(vo.get계약ID());
		yp040VO.set사용자ID(vo.get사용자ID());
		yp040VO.set근무시기(vo.get근무시기());
		YP040VO resultYP040VO = yp040DAO.getYP040(yp040VO);

		List<YP000VO> list = yp000DAO.getPaymentUserWorkMonth(vo);
		if (list.size() > 0) {
			yp000VO = list.get(0);
			if (resultYP040VO != null) {
				yp000VO.setEmpName(resultYP040VO.get성명());
				yp000VO.set총급여(resultYP040VO.get총급여());
				yp000VO.set근로소득금액(resultYP040VO.get근로소득금액());
			}
		}

		return yp000VO;
	}
}
