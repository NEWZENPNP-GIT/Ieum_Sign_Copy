package com.ezsign.feb.report.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.feb.house.vo.YE101VO;
import com.ezsign.feb.house.vo.YE104VO;
import com.ezsign.feb.report.dao.ReportDAO;
import com.ezsign.feb.report.service.ReportService;
import com.ezsign.feb.report.vo.AnnuitySavingVO;
import com.ezsign.feb.report.vo.InputStatusVO;
import com.ezsign.feb.report.vo.ItemVO;
import com.ezsign.feb.report.vo.YE105ReportVO;
import com.ezsign.feb.report.vo.YE402ReportVO;
import com.ezsign.feb.report.vo.YE404ReportVO;
import com.ezsign.feb.report.vo.YE408ReportVO;
import com.ezsign.feb.report.vo.YE700ReportVO;
import com.ezsign.feb.worker.vo.YE003VO;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;

@Service("reportService")
public class ReportServiceImpl implements ReportService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "reportDAO")
	private ReportDAO reportDAO;
	
	/* 요약표 */
	public Map<String, Object> getYE700SummaryList(YE700ReportVO vo) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<YE700ReportVO> list = reportDAO.getYE700SummaryList(vo);
		YE700ReportVO sum = reportDAO.getYE700SummaryListSum(vo);
		
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				
				YE700ReportVO ye700ReportVO = list.get(i);
				
				ye700ReportVO.setBizName(vo.getBizName());
				
				String identityCode = ye700ReportVO.get개인식별번호();
				if (StringUtil.isNotNull(identityCode)) {
					ye700ReportVO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
				}
			}
		}
		
		result.put("list", list);
		result.put("sum", sum);
				
		return result;
	}

	/* 입력현황 */
	@Override
	public Map<String, Object> getInputStatus(InputStatusVO vo) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<InputStatusVO> list = reportDAO.getInputStatus(vo);
		InputStatusVO sum = reportDAO.getInputStatusSum(vo);
		
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){

				InputStatusVO inputStatusVO = list.get(i);
				inputStatusVO.setBizName(vo.getBizName());
				
				String identityCode = inputStatusVO.get개인식별번호();

				if (StringUtil.isNotNull(identityCode)) {
					inputStatusVO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
				}
			}
		}
		
		result.put("list", list);
		result.put("sum", sum);
				
		return result;
	}

	
	/* 공제현황 */
	public Map<String, Object> getYE700List(YE700ReportVO vo) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<YE700ReportVO> list = reportDAO.getYE700List(vo);
		YE700ReportVO sum = reportDAO.getYE700ListSum(vo);
		
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){

				YE700ReportVO ye700VO = list.get(i);
				ye700VO.setBizName(vo.getBizName());
				
				String identityCode = ye700VO.get개인식별번호();

				if (StringUtil.isNotNull(identityCode)) {
					ye700VO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
				}
			}
		}
		
		result.put("list", list);
		result.put("sum", sum);

		return result;
	}
	
	/* 공제항목별내역 */
	@Override
	public Map<String, Object> getItem(ItemVO vo) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<ItemVO> list = reportDAO.getItem(vo);
		ItemVO sum = reportDAO.getItemSum(vo);
		
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){

				ItemVO itemVO = list.get(i);
				itemVO.setBizName(vo.getBizName());
				
				String identityCode = itemVO.get개인식별번호();
				String identityCode2 = itemVO.get가족_개인식별번호();

				if (StringUtil.isNotNull(identityCode)) {
					itemVO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
					itemVO.set가족_개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode2));
				}
			}
		}
		
		result.put("list", list);
		result.put("sum", sum);
				
		return result;
	}
	
	/* 종전근무지현황 */
	@Override
	public Map<String, Object> getYE003List(YE003VO vo) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<YE003VO> list = reportDAO.getYE003List(vo);
		YE003VO sum = reportDAO.getYE003ListSum(vo);
		
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				
				YE003VO ye003VO = list.get(i);
				
				ye003VO.setBizName(vo.getBizName());
				
				String identityCode = ye003VO.get개인식별번호();
				if (StringUtil.isNotNull(identityCode)) {
					ye003VO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
				}
			}
		}
		
		result.put("list", list);
		result.put("sum", sum);
				
		return result;
	}
	
	/* 의료비명세서 */
	@Override
	public Map<String, Object> getYE402List(YE402ReportVO vo) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<YE402ReportVO> list = reportDAO.getYE402List(vo);
		YE402ReportVO sum = reportDAO.getYE402ListSum(vo);
		
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				
				YE402ReportVO ye402VO = list.get(i);
				ye402VO.setBizName(vo.getBizName());
				
				String identityCode = ye402VO.get개인식별번호();
				String identityCode2 = ye402VO.get가족_개인식별번호();
				if (StringUtil.isNotNull(identityCode)) {
					ye402VO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
					ye402VO.set가족_개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode2));
				}
			}
		}
		
		result.put("list", list);
		result.put("sum", sum);
				
		return result;
	}
	
	/* 기부금명세서 */
	@Override
	public Map<String, Object> getYE404List(YE404ReportVO vo) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<YE404ReportVO> list = reportDAO.getYE404List(vo);
		YE404ReportVO sum = reportDAO.getYE404ListSum(vo);
		
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){

				YE404ReportVO ye404VO = list.get(i);
				ye404VO.setBizName(vo.getBizName());
				
				String identityCode = ye404VO.get개인식별번호();
				String identityCode2 = ye404VO.get가족_개인식별번호();
				
				if (StringUtil.isNotNull(identityCode)) {
					ye404VO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
					ye404VO.set가족_개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode2));
				}
			}
		}
		
		result.put("list", list);
		result.put("sum", sum);
				
		return result;
	}
	
	/* 기부금조정명세서 */
	public Map<String, Object> getYE408List(YE408ReportVO vo) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<YE408ReportVO> list = reportDAO.getYE408List(vo);
		YE408ReportVO sum = reportDAO.getYE408ListSum(vo);
		
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){

				YE408ReportVO ye408VO = list.get(i);
				ye408VO.setBizName(vo.getBizName());
				
				String identityCode = ye408VO.get개인식별번호();

				if (StringUtil.isNotNull(identityCode)) {
					ye408VO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
				}
			}
		}
		
		result.put("list", list);
		result.put("sum", sum);
				
		return result;
	}
	
	/* 연금저축명세서 */
	@Override
	public Map<String, Object> getAnnuitySaving(AnnuitySavingVO vo) throws Exception{
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<AnnuitySavingVO> list = reportDAO.getAnnuitySaving(vo);
		AnnuitySavingVO sum = reportDAO.getAnnuitySavingSum(vo);
		
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){

				AnnuitySavingVO annuitySavingVO = list.get(i);
				annuitySavingVO.setBizName(vo.getBizName());
				
				String identityCode = annuitySavingVO.get개인식별번호();

				if (StringUtil.isNotNull(identityCode)) {
					annuitySavingVO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
				}
			}
		}
		
		result.put("list", list);
		result.put("sum", sum);
				
		return result;
	}
	
	/* 월세공제명세서 */
	@Override
	public Map<String, Object> getYE105List(YE105ReportVO vo) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<YE105ReportVO> list = reportDAO.getYE105List(vo);
		YE105ReportVO sum = reportDAO.getYE105ListSum(vo);
		
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				
				YE105ReportVO ye105VO = list.get(i);
				ye105VO.setBizName(vo.getBizName());
				
				String identityCode = ye105VO.get개인식별번호();
				if (StringUtil.isNotNull(identityCode)) {
					ye105VO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
				}
			}
		}
		
		result.put("list", list);
		result.put("sum", sum);
				
		return result;
	}
	
	/* 주택임차 차입금원리금 상환명세서 */
	@Override
	public Map<String, Object> getYE101List(YE101VO vo) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<YE101VO> list = reportDAO.getYE101List(vo);
		YE101VO sum = reportDAO.getYE101ListSum(vo);
		
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){

				YE101VO ye101VO = list.get(i);
				ye101VO.setBizName(vo.getBizName());
				
				String identityCode = ye101VO.get개인식별번호();

				if (StringUtil.isNotNull(identityCode)) {
					ye101VO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
				}
			}
		}
		
		result.put("list", list);
		result.put("sum", sum);
				
		return result;
	}
	
	
	/* 장기주택저당차입금명세서 */
	@Override
	public Map<String, Object> getYE104List(YE104VO vo) throws Exception {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<YE104VO> list = reportDAO.getYE104List(vo);
		YE104VO sum = reportDAO.getYE104ListSum(vo);
		
		if(list != null && list.size() > 0){
			for(int i = 0; i < list.size(); i++){

				YE104VO ye104VO = list.get(i);
				ye104VO.setBizName(vo.getBizName());
				
				String identityCode = ye104VO.get개인식별번호();
				if (StringUtil.isNotNull(identityCode)) {
					ye104VO.set개인식별번호(SecurityUtil.getinstance().aesDecode(identityCode));
				}
			}
		}
		
		result.put("list", list);
		result.put("sum", sum);
				
		return result;
	}
}
