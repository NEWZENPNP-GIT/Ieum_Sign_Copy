package com.ezsign.annual.service;

import java.util.List;

import com.ezsign.annual.vo.AnnualConfigVO;
import com.ezsign.annual.vo.AnnualTypeVO;
import com.ezsign.annual.vo.AnnualVacationStatVO;
import com.ezsign.annual.vo.AnnualVacationVO;
import com.ezsign.annual.vo.VacationRequestVO;
import com.ezsign.framework.vo.FileVO;

public interface AnnualVacationService {

	// 연차관리
	public static final String SERVICE_ID = "180223115310004";	// 연차관리 서비스ID 

	public AnnualVacationVO getAnnualVacation(VacationRequestVO vo) throws Exception;
	
	public List<AnnualVacationVO> getAnnualVacationList(AnnualVacationVO vo)  throws Exception;
	
	public int getAnnualVacationListCount(AnnualVacationVO vo)  throws Exception;
	
	public int insAnnualVacation(AnnualVacationVO vo)  throws Exception;

	public int updAnnualVacation(AnnualVacationVO vo)  throws Exception;

	public VacationRequestVO getVacationRequest(VacationRequestVO vo) throws Exception;
	
	public List<VacationRequestVO> getVacationRequestList(VacationRequestVO vo) throws Exception;

	public List<VacationRequestVO> getVacationRequestCompleteList(VacationRequestVO vo) throws Exception;
	
	public List<VacationRequestVO> getVacationTypeSumList(VacationRequestVO vo) throws Exception;
	
	public boolean requestAnnualVacation(VacationRequestVO vo) throws Exception;

	public boolean acceptAnnualVacation(VacationRequestVO vo) throws Exception;

	public boolean acceptAllAnnualVacation(List<VacationRequestVO> list) throws Exception;

	public boolean rejectAnnualVacation(VacationRequestVO vo) throws Exception;

	public boolean cancelAnnualVacation(VacationRequestVO vo) throws Exception;

	public AnnualConfigVO getAnnualConfig(AnnualConfigVO vo) throws Exception;

	public int saveAnnualConfig(AnnualConfigVO vo)  throws Exception;

	public void insAnnualType(AnnualTypeVO vo) throws Exception;
	
	public List<AnnualTypeVO> getAnnualTypeList(AnnualTypeVO vo) throws Exception;

	public int saveAnnualType(List<AnnualTypeVO> list)  throws Exception;

	public List<VacationRequestVO> getVacationHistoryList(VacationRequestVO vo) throws Exception;
	
	public int getVacationHistoryListCount(VacationRequestVO vo)  throws Exception;

	public List<AnnualVacationStatVO> getAnnualStatMonthList(AnnualVacationStatVO vo) throws Exception;

	public AnnualVacationVO sendAnnualVacationExcel(String bizId, String xlsPath, FileVO fileVO) throws Exception;
	
}
