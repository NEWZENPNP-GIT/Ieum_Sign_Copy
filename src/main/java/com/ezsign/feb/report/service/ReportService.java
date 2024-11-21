package com.ezsign.feb.report.service;

import java.util.Map;

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

public interface ReportService {
	
	/* 요약표 */
	public Map<String, Object> getYE700SummaryList(YE700ReportVO ye700ReportVO) throws Exception;
	
	/* 입력현황 */
	public Map<String, Object> getInputStatus(InputStatusVO inputStatusVO) throws Exception;
	
	/* 공제현황 */
	public Map<String, Object> getYE700List(YE700ReportVO ye700ReportVO) throws Exception;
	
	/* 공제항목별내역 */
	public Map<String, Object> getItem(ItemVO itemVO) throws Exception;

	/* 종전근무지현황 */
	public Map<String, Object> getYE003List(YE003VO ye003VO) throws Exception;
	
	/* 의료비명세서 */
	public Map<String, Object> getYE402List(YE402ReportVO ye402VO) throws Exception;
	
	/* 기부금명세서 */
	public Map<String, Object> getYE404List(YE404ReportVO ye404VO) throws Exception;
	
	/* 기부금조정명세서 */
	public Map<String, Object> getYE408List(YE408ReportVO ye408VO) throws Exception;
	
	/* 연금저축명세서 */
	public Map<String, Object> getAnnuitySaving(AnnuitySavingVO annuitySavingVO) throws Exception;
	
	/* 월세공제명세서 */
	public Map<String, Object> getYE105List(YE105ReportVO ye105VO) throws Exception;
	
	/* 주택임차 차입금원리금 상환명세서 */
	public Map<String, Object> getYE101List(YE101VO ye101VO) throws Exception;
	
	/* 장기주택저당차입금명세서 */
	public Map<String, Object> getYE104List(YE104VO ye104VO) throws Exception;
}
