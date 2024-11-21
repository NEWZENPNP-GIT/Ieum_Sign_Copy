package com.ezsign.feb.worker.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.ezsign.feb.master.vo.YE000VO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("ye003DAO")
public class YE003DAO extends EgovAbstractDAO {

	// 연말정산_원천명세(급여정보) 조회
	@SuppressWarnings("unchecked")
	public List<YE000VO> getYE003List(YE000VO vo) {
		return (List<YE000VO>) list("ye003DAO.getYE003List", vo);
	}

	// 연말정산_원천명세(급여정보) 입력
	public String insYE003(YE000VO vo) {
		valueNullCheck(vo);
				
		String 원천명세ID = (String)insert("ye003DAO.insYE003", vo);		
		return 원천명세ID;
	}

	// 연말정산_원천명세(급여정보) 수정
	public int updYE003(YE000VO vo) {
		valueNullCheck(vo);
		
		return update("ye003DAO.updYE003", vo);
	}

	// 연말정산_원천명세(급여정보) 삭제
	public int delYE003(YE000VO vo) {
		return delete("ye003DAO.delYE003", vo);
	}

	// 연말정산 회사자료 합계 조회
	@SuppressWarnings("unchecked")
	public List<YE000VO> getYE003SumList(YE000VO vo) throws Exception {
		return (List<YE000VO>) list("ye003DAO.getYE003SumList", vo);
	}

	// 연말정산 종근무지 기납부세액 조회
	@SuppressWarnings("unchecked")
	public List<YE000VO> getYE003TaxList(YE000VO vo) throws Exception {
		return (List<YE000VO>) list("ye003DAO.getYE003TaxList", vo);
	}
	
	
	/**
	 *
	 * 빈공간일 경우 number type 오류가 발생하므로 빈공간을 체크해서 제거한다.
	 *
	 * @param vo
	 */
	private void valueNullCheck(YE000VO vo){
		
		vo.set급여(StringUtils.trimToEmpty(vo.get급여()));
		vo.set상여(StringUtils.trimToEmpty(vo.get상여()));
		vo.set인정상여(StringUtils.trimToEmpty(vo.get인정상여()));
		vo.set주식매수선택권행사이익(StringUtils.trimToEmpty(vo.get주식매수선택권행사이익()));
		vo.set우리사주조합인출금(StringUtils.trimToEmpty(vo.get우리사주조합인출금()));
		vo.set임원퇴직소득금액한도초과액(StringUtils.trimToEmpty(vo.get임원퇴직소득금액한도초과액()));
		vo.set소득세(StringUtils.trimToEmpty(vo.get소득세()));
		vo.set지방소득세(StringUtils.trimToEmpty(vo.get지방소득세()));
		vo.set농어촌특별세(StringUtils.trimToEmpty(vo.get농어촌특별세()));
		vo.set건강보험료(StringUtils.trimToEmpty(vo.get건강보험료()));				
		vo.set장기요양보험료(StringUtils.trimToEmpty(vo.get장기요양보험료()));
		vo.set국민연금보험료(StringUtils.trimToEmpty(vo.get국민연금보험료()));
		vo.set고용보험료(StringUtils.trimToEmpty(vo.get고용보험료()));
		vo.set공무원연금(StringUtils.trimToEmpty(vo.get공무원연금()));				
		vo.set군인연금(StringUtils.trimToEmpty(vo.get군인연금()));
		vo.set사립학교교직원연금(StringUtils.trimToEmpty(vo.get사립학교교직원연금()));
		vo.set별정우체국연금(StringUtils.trimToEmpty(vo.get별정우체국연금()));
		vo.setM01(StringUtils.trimToEmpty(vo.getM01()));
		vo.setM02(StringUtils.trimToEmpty(vo.getM02()));
		vo.setM03(StringUtils.trimToEmpty(vo.getM03()));
		vo.setO01(StringUtils.trimToEmpty(vo.getO01()));
		vo.setQ01(StringUtils.trimToEmpty(vo.getQ01()));
		vo.setH08(StringUtils.trimToEmpty(vo.getH08()));
		vo.setH09(StringUtils.trimToEmpty(vo.getH09()));
		vo.setH10(StringUtils.trimToEmpty(vo.getH10()));
		vo.setG01(StringUtils.trimToEmpty(vo.getG01()));
		vo.setH11(StringUtils.trimToEmpty(vo.getH11()));
		vo.setH12(StringUtils.trimToEmpty(vo.getH12()));
		vo.setH13(StringUtils.trimToEmpty(vo.getH13()));
		vo.setH01(StringUtils.trimToEmpty(vo.getH01()));
		vo.setK01(StringUtils.trimToEmpty(vo.getK01()));
		vo.setS01(StringUtils.trimToEmpty(vo.getS01()));
		vo.setT01(StringUtils.trimToEmpty(vo.getT01()));
		vo.setY02(StringUtils.trimToEmpty(vo.getY02()));
		vo.setY03(StringUtils.trimToEmpty(vo.getY03()));
		vo.setY04(StringUtils.trimToEmpty(vo.getY04()));
		vo.setH05(StringUtils.trimToEmpty(vo.getH05()));
		vo.setI01(StringUtils.trimToEmpty(vo.getI01()));
		vo.setR10(StringUtils.trimToEmpty(vo.getR10()));
		vo.setH14(StringUtils.trimToEmpty(vo.getH14()));
		vo.setH15(StringUtils.trimToEmpty(vo.getH15()));
		vo.setT10(StringUtils.trimToEmpty(vo.getT10()));
		vo.setT11(StringUtils.trimToEmpty(vo.getT11()));
		vo.setT12(StringUtils.trimToEmpty(vo.getT12()));
		vo.setT20(StringUtils.trimToEmpty(vo.getT20()));
		vo.setH16(StringUtils.trimToEmpty(vo.getH16()));
		vo.setR11(StringUtils.trimToEmpty(vo.getR11()));
		vo.setH06(StringUtils.trimToEmpty(vo.getH06()));
		vo.setH07(StringUtils.trimToEmpty(vo.getH07()));
		vo.setY22(StringUtils.trimToEmpty(vo.getY22()));
		vo.setT13(StringUtils.trimToEmpty(vo.getT13()));
		vo.setH17(StringUtils.trimToEmpty(vo.getH17()));
		vo.setU01(StringUtils.trimToEmpty(vo.getU01()));
		vo.set납부특례세액_소득세(StringUtils.trimToEmpty(vo.get납부특례세액_소득세()));
		vo.set납부특례세액_지방소득세(StringUtils.trimToEmpty(vo.get납부특례세액_지방소득세()));
		vo.set납부특례세액_농어촌특별세(StringUtils.trimToEmpty(vo.get납부특례세액_농어촌특별세()));
		vo.set직무발명보상금(StringUtils.trimToEmpty(vo.get직무발명보상금()));

	}
	
	
}
