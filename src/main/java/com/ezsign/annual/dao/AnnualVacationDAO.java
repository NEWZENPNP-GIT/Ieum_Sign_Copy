package com.ezsign.annual.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.annual.vo.AnnualConfigVO;
import com.ezsign.annual.vo.AnnualTypeVO;
import com.ezsign.annual.vo.AnnualVacationLogVO;
import com.ezsign.annual.vo.AnnualVacationStatVO;
import com.ezsign.annual.vo.AnnualVacationVO;
import com.ezsign.annual.vo.VacationRequestVO;
import com.ezsign.emp.vo.EmpVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("annualVacationDAO")
public class AnnualVacationDAO extends EgovAbstractDAO {
	
	public void insAnnualVacation(AnnualVacationVO vo) {
		insert("annualVacationDAO.insAnnualVacation", vo);
	}
	
	public AnnualVacationVO getAnnualVacation(AnnualVacationVO vo) {
		return (AnnualVacationVO)selectByPk("annualVacationDAO.getAnnualVacation", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<AnnualVacationVO> getAnnualVacationList(AnnualVacationVO vo) {
		return (List<AnnualVacationVO>)list("annualVacationDAO.getAnnualVacationList", vo);
	}

	public int getAnnualVacationListCount(AnnualVacationVO vo) {
		return (Integer)selectByPk("annualVacationDAO.getAnnualVacationListCount", vo);
	}
	
	public int updAnnualVacation(AnnualVacationVO vo) {
		return update("annualVacationDAO.updAnnualVacation", vo);
	}

	public int updAnnualVacationUseDay(AnnualVacationVO vo) {
		return update("annualVacationDAO.updAnnualVacationUseDay", vo);
	}
	
	public void insAnnualVacationLog(AnnualVacationLogVO vo) {
		insert("annualVacationDAO.insAnnualVacationLog", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<AnnualVacationLogVO> getAnnualVacationLogList(AnnualVacationLogVO vo) {
		return (List<AnnualVacationLogVO>)list("annualVacationDAO.getAnnualVacationLogList", vo);
	}
	
	public void insVacationRequest(VacationRequestVO vo) {
		insert("annualVacationDAO.insVacationRequest", vo);
	}
	
	public VacationRequestVO getVacationRequest(VacationRequestVO vo) {
		return (VacationRequestVO)selectByPk("annualVacationDAO.getVacationRequest", vo);
	}
	
	public int updVacationRequest(VacationRequestVO vo) {
		return update("annualVacationDAO.updVacationRequest", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<VacationRequestVO> getVacationRequestList(VacationRequestVO vo) {
		return (List<VacationRequestVO>) list("annualVacationDAO.getVacationRequestList", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<VacationRequestVO> getVacationRequestCompleteList(VacationRequestVO vo) {
		return (List<VacationRequestVO>) list("annualVacationDAO.getVacationRequestCompleteList", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<VacationRequestVO> getVacationTypeSumList(VacationRequestVO vo) {
		return (List<VacationRequestVO>) list("annualVacationDAO.getVacationTypeSumList", vo);
	}
	
	// 연차관리 기본설정
	public AnnualConfigVO getAnnualConfig(AnnualConfigVO vo) {
		return (AnnualConfigVO)selectByPk("annualVacationDAO.getAnnualConfig", vo);
	}
	
	public void insAnnualConfig(AnnualConfigVO vo) {
		insert("annualVacationDAO.insAnnualConfig", vo);
	}
	
	public int updAnnualConfig(AnnualConfigVO vo) {
		return update("annualVacationDAO.updAnnualConfig", vo);
	}

	// 연차일수 구분관리
	@SuppressWarnings("unchecked")
	public List<AnnualTypeVO> getAnnualTypeList(AnnualTypeVO vo) {
		return (List<AnnualTypeVO>) list("annualVacationDAO.getAnnualTypeList", vo);
	}
	
	public void insAnnualType(AnnualTypeVO vo) {
		insert("annualVacationDAO.insAnnualType", vo);
	}
	
	public int updAnnualType(AnnualTypeVO vo) {
		return update("annualVacationDAO.updAnnualType", vo);
	}
	
	// 연차사용내역 조회
	@SuppressWarnings("unchecked")
	public List<VacationRequestVO> getVacationHistoryList(VacationRequestVO vo) {
		return (List<VacationRequestVO>) list("annualVacationDAO.getVacationHistoryList", vo);
	}
	
	public int getVacationHistoryListCount(VacationRequestVO vo) {
		return (Integer)selectByPk("annualVacationDAO.getVacationHistoryListCount", vo);
	}	
	
	// 통계	
	@SuppressWarnings("unchecked")
	public List<AnnualVacationStatVO> getAnnualStatMonthList(AnnualVacationStatVO vo) {
		return (List<AnnualVacationStatVO>)list("annualVacationDAO.getAnnualStatMonthList", vo);
	}
	
}
