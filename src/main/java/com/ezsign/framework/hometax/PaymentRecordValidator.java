package com.ezsign.framework.hometax;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ezsign.feb.hometax.vo.PaymentARecordVO;
import com.ezsign.feb.hometax.vo.PaymentBRecordVO;
import com.ezsign.feb.hometax.vo.PaymentCRecordVO;
import com.ezsign.framework.util.StringUtil;

/**
 * 
 * 간이지급명세서 전산매체신고 필수값 체크
 *
 */
public class PaymentRecordValidator {

	private static PaymentRecordValidator instance = null;
	
	public static PaymentRecordValidator getinstance(){
    	
    	if(instance == null){
    		instance = new PaymentRecordValidator();
    	}
    	
    	return instance;
	}
	
	/**
	 * A레코드 (제출자 레코드) 필수정보 체크
	 * 
	 * @param aRecordVO
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> aRecordValidator(PaymentARecordVO aRecordVO) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		resultMap.put("result", "0");
		resultMap.put("resultMessage", "aRecordValidator");
		
		if(aRecordVO == null){		
			resultMap.put("resultMessage", "제출자 정보가 없습니다.");
		}else if(StringUtils.isEmpty(aRecordVO.getA5())){
			resultMap.put("resultMessage", "제출자 구분 정보가 없습니다.");
		}else if(StringUtils.isEmpty(aRecordVO.getA7())){
			resultMap.put("resultMessage", "홈택스아이디 정보가 없습니다.");
		}else if(StringUtils.isEmpty(aRecordVO.getA9())){
			resultMap.put("resultMessage", "사업자등록번호 정보가 없습니다.");
		}else if(StringUtils.isEmpty(aRecordVO.getA10())){
			resultMap.put("resultMessage", "법인명(상호) 정보가 없습니다.");
		}else if(StringUtils.isEmpty(aRecordVO.getA11())){
			resultMap.put("resultMessage", "담당자 부서 정보가 없습니다.");
		}else if(StringUtils.isEmpty(aRecordVO.getA12())){
			resultMap.put("resultMessage", "담당자 성명 정보가 없습니다.");
		}else if(StringUtils.isEmpty(aRecordVO.getA13())){
			resultMap.put("resultMessage", "담당자 전화번호 정보가 없습니다.");
		}else{
			resultMap.put("result", "1");
			resultMap.put("resultMessage", "sucess");
		}

		return resultMap;
	}
	
	/**
	 * B레코드 (원천징수의무자별 집계 레코드) 필수정보 체크
	 * 
	 * @param aRecordVO
	 * @return
	 */
	public Map<String,Object> bRecordValidator(PaymentBRecordVO bRecordVO) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		resultMap.put("result", "0");
		resultMap.put("resultMessage", "bRecordValidator");
		
		if(bRecordVO == null){		
			resultMap.put("resultMessage", "원천징수의무자별 집계 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB3())){
			resultMap.put("resultMessage", "세무서코드 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB5())){
			resultMap.put("resultMessage", "원천징수의무자의 법인명 또는 상호 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB6())){
			resultMap.put("resultMessage", "대표자(성명) 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB7())){
			resultMap.put("resultMessage", "원천징수의무자의 사업자등록번호 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB8())){
			resultMap.put("resultMessage", "주민(법인)등록번호 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB9())){
			resultMap.put("resultMessage", "귀속년도 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB10())){
			resultMap.put("resultMessage", "근무시기 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB11())){
			resultMap.put("resultMessage", "근로자 수 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB12())){
			resultMap.put("resultMessage", "과세소득합계 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB13())){
			resultMap.put("resultMessage", "비과세소득합계 정보가 없습니다.");
		}
		else if(!StringUtils.equals("1", bRecordVO.getB10()) && !StringUtils.equals("2", bRecordVO.getB10())){
			resultMap.put("resultMessage", "근무시기 정보는 '1','2'가 수록되어야 합니다. ");
		}
		else if(StringUtil.strPaserLong(bRecordVO.getB11()) < 0){
			resultMap.put("resultMessage", "근로자 수 정보가 '0'보다 작습니다.");
		}else if(StringUtil.strPaserLong(bRecordVO.getB12()) < 0){
			resultMap.put("resultMessage", "과세소득합계 정보가 '0'보다 작습니다.");
		}else if(StringUtil.strPaserLong(bRecordVO.getB13()) < 0){
			resultMap.put("resultMessage", "비과세소득합계 정보가 '0'보다 작습니다.");
		}		
		else{
			resultMap.put("result", "1");
			resultMap.put("resultMessage", "sucess");
		}
		
		
		return resultMap;
	}
	
	
	/**
	 * C레코드 (주(현)근무처 레코드) 필수정보 체크
	 * 
	 * @param cRecordVO
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> cRecordValidator(PaymentCRecordVO cRecordVO) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		resultMap.put("result", "0");
		resultMap.put("resultMessage", "cRecordValidator");
		
		if(cRecordVO == null){		
			resultMap.put("resultMessage", "소득자 정보가 없습니다.");
		}else if(StringUtils.isEmpty(cRecordVO.getC3())){
			resultMap.put("resultMessage", "세무소코드 정보가 없습니다.");
		}else if(StringUtils.isEmpty(cRecordVO.getC5())){
			resultMap.put("resultMessage", "사업자등록번호 정보가 없습니다.");
		}else if(StringUtils.isEmpty(cRecordVO.getC6())){
			resultMap.put("resultMessage", "주민등록번호 정보가 없습니다.");
		}else if(StringUtils.isEmpty(cRecordVO.getC7())){
			resultMap.put("resultMessage", "성명 정보가 없습니다.");
		}else if(StringUtils.isEmpty(cRecordVO.getC8())){
			resultMap.put("resultMessage", "전화번호 정보가 없습니다.");
		}else if(!StringUtils.equals("1", cRecordVO.getC9()) && !StringUtils.equals("9", cRecordVO.getC9())){
			resultMap.put("resultMessage", "내.외국인 코드가 '1','9'가 아닙니다.");
		}else if(!StringUtils.equals("1", cRecordVO.getC10()) && !StringUtils.equals("2", cRecordVO.getC10())){
			resultMap.put("resultMessage", "거주자구분 코드가 '1','2'가 아닙니다.");
		}else if(StringUtils.isEmpty(cRecordVO.getC12())){
			resultMap.put("resultMessage", "근무기간 시작년월일 정보가 없습니다.");
		}else if(StringUtils.isEmpty(cRecordVO.getC13())){
			resultMap.put("resultMessage", "근무기간 종료년월일 정보가 없습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC14()) < 0){
			resultMap.put("resultMessage", "과세소득 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC15()) < 0){
			resultMap.put("resultMessage", "비과세소득 정보가  0보다 작습니다.");
		}else{
			resultMap.put("result", "1");
			resultMap.put("resultMessage", "sucess");
		}
				
		return resultMap;
		
	}
	
}
