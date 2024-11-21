package com.ezsign.feb.report.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.feb.house.vo.YE101VO;
import com.ezsign.feb.house.vo.YE104VO;
import com.ezsign.feb.report.vo.AnnuitySavingVO;
import com.ezsign.feb.report.vo.InputStatusVO;
import com.ezsign.feb.report.vo.ItemVO;
import com.ezsign.feb.report.vo.YE105ReportVO;
import com.ezsign.feb.report.vo.YE402ReportVO;
import com.ezsign.feb.report.vo.YE404ReportVO;
import com.ezsign.feb.report.vo.YE408ReportVO;
import com.ezsign.feb.report.vo.YE700ReportVO;
import com.ezsign.feb.worker.vo.YE003VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("reportDAO")
public class ReportDAO extends EgovAbstractDAO {
	/*	

	
	public List<HomeMortgageVO> getHomeMortgage(HomeMortgageVO homeMortgageVO) {
		return (List<HomeMortgageVO>) list("statusDAO.getHomeMortgage", homeMortgageVO);
	}
	
	public HomeMortgageVO getHomeMortgageSum(HomeMortgageVO homeMortgageVO) {
		return (HomeMortgageVO) select("statusDAO.getHomeMortgageSum", homeMortgageVO);
	}


	

	
	public List<InputStatusVO> getInputStatus(InputStatusVO inputStatusVO) {
		return (List<InputStatusVO>) list("statusDAO.getInputStatus", inputStatusVO);
	}

	public InputStatusVO getInputStatusSum(InputStatusVO inputStatusVO) {
		return (InputStatusVO) select("statusDAO.getInputStatusSum", inputStatusVO);
	}
	
	/*
	public List<DuctionStatusVO> getDuctionStatus(DuctionStatusVO ductionStatusVO) {
		return (List<DuctionStatusVO>) list("statusDAO.getDuctionStatus", ductionStatusVO);
	}

	public DuctionStatusVO getDuctionStatusSum(DuctionStatusVO ductionStatusVO) {
		return (DuctionStatusVO) select("statusDAO.getDuctionStatusSum", ductionStatusVO);
	}
	*/
	
	/* 요약표 */
	public List<YE700ReportVO> getYE700SummaryList(YE700ReportVO ye700VO) {
		return (List<YE700ReportVO>) list("reportDAO.getYE700SummaryList", ye700VO);
	}
	/* 요약표 합계*/
	public YE700ReportVO getYE700SummaryListSum(YE700ReportVO ye700VO) {
		return (YE700ReportVO) select("reportDAO.getYE700SummaryListSum", ye700VO);
	}
	
	/* 공제현황 */
	public List<YE700ReportVO> getYE700List(YE700ReportVO ye700VO) {
		return (List<YE700ReportVO>) list("reportDAO.getYE700List", ye700VO);
	}
	/* 공제현황 합계 */
	public YE700ReportVO getYE700ListSum(YE700ReportVO ye700VO) {
		return (YE700ReportVO) select("reportDAO.getYE700ListSum", ye700VO);
	}
	
	/* 입력현황 */
	public List<InputStatusVO> getInputStatus(InputStatusVO inputStatusVO) {
		return (List<InputStatusVO>) list("reportDAO.getInputStatus", inputStatusVO);
	}
	/* 입력현황 합계 */
	public InputStatusVO getInputStatusSum(InputStatusVO inputStatusVO) {
		return (InputStatusVO) select("reportDAO.getInputStatusSum", inputStatusVO);
	}
	
	/* 공제항목별내역 */
	public List<ItemVO> getItem(ItemVO itemVO) {
		return (List<ItemVO>) list("reportDAO.getItem", itemVO);
	}
	/* 공제항목별내역 합계 */
	public ItemVO getItemSum(ItemVO itemVO) {
		return (ItemVO) select("reportDAO.getItemSum", itemVO);
	}
	
	/* 종전근무지현황 */
	public List<YE003VO> getYE003List(YE003VO ye003VO) {
		return (List<YE003VO>) list("reportDAO.getYE003List", ye003VO);
	}
	/* 종전근무지현황 합계 */
	public YE003VO getYE003ListSum(YE003VO ye003VO) {
		return (YE003VO) select("reportDAO.getYE003ListSum", ye003VO);
	}
	
	/* 의료비명세서 */
	public List<YE402ReportVO> getYE402List(YE402ReportVO ye402VO) {
		return (List<YE402ReportVO>) list("reportDAO.getYE402List", ye402VO);
	}
	/* 의료비명세서 합계 */
	public YE402ReportVO getYE402ListSum(YE402ReportVO ye402VO) {
		return (YE402ReportVO) select("reportDAO.getYE402ListSum", ye402VO);
	}
	
	/* 기부금명세서 */
	public List<YE404ReportVO> getYE404List(YE404ReportVO ye404VO) {
		return (List<YE404ReportVO>) list("reportDAO.getYE404List", ye404VO);
	}
	/* 기부금명세서 합계 */
	public YE404ReportVO getYE404ListSum(YE404ReportVO ye404VO) {
		return (YE404ReportVO) select("reportDAO.getYE404ListSum", ye404VO);
	}
	
	/* 기부금조정명세서 */
	public List<YE408ReportVO> getYE408List(YE408ReportVO ye408VO) {
		return (List<YE408ReportVO>) list("reportDAO.getYE408List", ye408VO);
	}
	/* 기부금조정명세서 합계 */
	public YE408ReportVO getYE408ListSum(YE408ReportVO ye408VO) {
		return (YE408ReportVO) select("reportDAO.getYE408ListSum", ye408VO);
	}
	
	/* 연금저축명세서 */
	public List<AnnuitySavingVO> getAnnuitySaving(AnnuitySavingVO annuitySavingVO) {
		return (List<AnnuitySavingVO>) list("reportDAO.getAnnuitySaving", annuitySavingVO);
	}
	/* 연금저축명세서 합계 */
	public AnnuitySavingVO getAnnuitySavingSum(AnnuitySavingVO annuitySavingVO) {
		return (AnnuitySavingVO) select("reportDAO.getAnnuitySavingSum", annuitySavingVO);
	}
	
	/* 월세공제명세서 */
	public List<YE105ReportVO> getYE105List(YE105ReportVO ye105VO) {
		return (List<YE105ReportVO>) list("reportDAO.getYE105List", ye105VO);
	}
	
	/* 월세공제명세서 합계 */
	public YE105ReportVO getYE105ListSum(YE105ReportVO ye105VO) {
		return (YE105ReportVO) select("reportDAO.getYE105ListSum", ye105VO);
	}
	
	/* 주택임차 차입금원리금 상환명세서 */
	public List<YE101VO> getYE101List(YE101VO ye101VO) {
		return (List<YE101VO>) list("reportDAO.getYE101List", ye101VO);
	}
	/* 주택임차 차입금원리금 상환명세서 합계 */
	public YE101VO getYE101ListSum(YE101VO ye101VO) {
		return (YE101VO) select("reportDAO.getYE101ListSum", ye101VO);
	}

	
	/* 장기주택저당차입금명세서 */
	public List<YE104VO> getYE104List(YE104VO ye104VO) {
		return (List<YE104VO>) list("reportDAO.getYE104List", ye104VO);
	}
	/* 장기주택저당차입금명세서 합계 */
	public YE104VO getYE104ListSum(YE104VO ye104VO) {
		return (YE104VO) select("reportDAO.getYE104ListSum", ye104VO);
	}
}
