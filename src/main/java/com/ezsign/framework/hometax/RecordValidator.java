package com.ezsign.framework.hometax;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.ezsign.feb.hometax.vo.ARecordVO;
import com.ezsign.feb.hometax.vo.BRecordVO;
import com.ezsign.feb.hometax.vo.CARecordVO;
import com.ezsign.feb.hometax.vo.CRecordVO;
import com.ezsign.feb.hometax.vo.DRecordVO;
import com.ezsign.feb.hometax.vo.YC200VO;
import com.ezsign.framework.util.StringUtil;

/**
 * 
 * 연말정산 전산매체신고 필수값 체크 
 * 
 *
 */
public class RecordValidator {
	
	private static RecordValidator instance = null;
	
	public static RecordValidator getinstance(){
    	
    	if(instance == null){
    		instance = new RecordValidator();
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
	public Map<String,Object> aRecordValidator(ARecordVO aRecordVO) {
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
	public Map<String,Object> bRecordValidator(BRecordVO bRecordVO) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		resultMap.put("result", "0");
		resultMap.put("resultMessage", "bRecordValidator");
		
		if(bRecordVO == null){		
			resultMap.put("resultMessage", "원천징수의무자별 집계 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB5())){
			resultMap.put("resultMessage", "원천징수의무자의 사업자등록번호 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB6())){
			resultMap.put("resultMessage", "원천징수의무자의 법인명 또는 상호 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB7())){
			resultMap.put("resultMessage", "대표자(성명) 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB8())){
			resultMap.put("resultMessage", "주민(법인)등록번호 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB9())){
			resultMap.put("resultMessage", "주(현)근무처수 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB10())){
			resultMap.put("resultMessage", "종(전)근무처수 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB11())){
			resultMap.put("resultMessage", "총급여 총계 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB12())){
			resultMap.put("resultMessage", "결정세액(소득세)총계 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB13())){
			resultMap.put("resultMessage", "결정세액(지방소득세)총계 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB14())){
			resultMap.put("resultMessage", "결정세액(농특세)총계 정보가 없습니다.");
		}else if(StringUtils.isEmpty(bRecordVO.getB15())){
			resultMap.put("resultMessage", "결정세액총계 정보가 없습니다.");
		}else{
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
	public Map<String,Object> cRecordValidator(CRecordVO cRecordVO) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		resultMap.put("result", "0");
		resultMap.put("resultMessage", "cRecordValidator");
		
		if(cRecordVO == null){		
			resultMap.put("resultMessage", "주(현)근무처 정보가 없습니다.");
		}else if(StringUtils.isEmpty(cRecordVO.getC3())){
			resultMap.put("resultMessage", "세무소코드 정보가 없습니다.");
		}else if(StringUtils.isEmpty(cRecordVO.getC5())){
			resultMap.put("resultMessage", "사업자등록번호 정보가 없습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC6()) < 0){
			resultMap.put("resultMessage", "종(전)근무처 수가 0보다 작습니다.");
		}else if(StringUtils.isEmpty(cRecordVO.getC7())){
			resultMap.put("resultMessage", "거주자구분코드 정보가 없습니다.");
		}else if(!StringUtils.equals("1", cRecordVO.getC7()) && !StringUtils.equals("2", cRecordVO.getC7())){
			resultMap.put("resultMessage", "거주자구분코드가 '1','2'가 아닙니다.");
		}else if(StringUtils.isEmpty(cRecordVO.getC11())){
			resultMap.put("resultMessage", "성명 정보가 없습니다.");
		}else if(!StringUtils.equals("1", cRecordVO.getC12()) && !StringUtils.equals("9", cRecordVO.getC12())){
			resultMap.put("resultMessage", "내외국인 구분코드가 '1','9'가 아닙니다.");
		}else if(StringUtils.isEmpty(cRecordVO.getC13())){
			resultMap.put("resultMessage", "주민등록번호 정보가 없습니다.");
		}else if(!StringUtils.equals("1", cRecordVO.getC15()) && !StringUtils.equals("2", cRecordVO.getC15())){
			resultMap.put("resultMessage", "세대주여부가 '1','2'가 아닙니다.");
		}else if(!StringUtils.equals("1", cRecordVO.getC16()) && !StringUtils.equals("2", cRecordVO.getC16())){
			resultMap.put("resultMessage", "연말정산구분이 '1','2'가 아닙니다.");
		}else if(!StringUtils.equals("1", cRecordVO.getC17()) && !StringUtils.equals("2", cRecordVO.getC17())){
			resultMap.put("resultMessage", "사업자단위과세자 여부가 '1','2'가 아닙니다.");
		}else if(StringUtils.isEmpty(cRecordVO.getC20())){
			resultMap.put("resultMessage", "주현근무처 - 근무처명 정보가 없습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC25()) < 0){
			resultMap.put("resultMessage", "급여 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC26()) < 0){
			resultMap.put("resultMessage", "상여 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC27()) < 0){
			resultMap.put("resultMessage", "인정상여 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC28()) < 0){
			resultMap.put("resultMessage", "주식매수선택권행사이익 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC29()) < 0){
			resultMap.put("resultMessage", "우리사주조합인출금 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC30()) < 0){
			resultMap.put("resultMessage", "임원퇴직소득금액한도초과액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC31()) < 0){
			resultMap.put("resultMessage", "직무발명보상금 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC35()) < 0){
			resultMap.put("resultMessage", "비과세학자금 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC36()) < 0){
			resultMap.put("resultMessage", "무보수위원수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC37()) < 0){
			resultMap.put("resultMessage", "경호.승선수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC38()) < 0){
			resultMap.put("resultMessage", "유아.초등등 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC39()) < 0){
			resultMap.put("resultMessage", "고등교육법 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC40()) < 0){
			resultMap.put("resultMessage", "특별법 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC41()) < 0){
			resultMap.put("resultMessage", "연구기관등 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC42()) < 0){
			resultMap.put("resultMessage", "기업부설연구소 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC43()) < 0){
			resultMap.put("resultMessage", "보육교사 근무환경개선비 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC44()) < 0){
			resultMap.put("resultMessage", "사립유치원수석교사 교사의인건비 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC45()) < 0){
			resultMap.put("resultMessage", "취재수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC46()) < 0){
			resultMap.put("resultMessage", "벽지수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC47()) < 0){
			resultMap.put("resultMessage", "재해관련급여 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC48()) < 0){
			resultMap.put("resultMessage", "정보공공기관지방이전기관 종사자 이주수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC49()) < 0){
			resultMap.put("resultMessage", "종교활동비 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC50()) < 0){
			resultMap.put("resultMessage", "외국정보등근무자 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC51()) < 0){
			resultMap.put("resultMessage", "외국주둔군인등 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC52()) < 0){
			resultMap.put("resultMessage", "국외근로 100만원 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC53()) < 0){
			resultMap.put("resultMessage", "국외근로 300만원 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC54()) < 0){
			resultMap.put("resultMessage", "국외근로 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC55()) < 0){
			resultMap.put("resultMessage", "야간근로수단 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC56()) < 0){
			resultMap.put("resultMessage", "출산보육수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC57()) < 0){
			resultMap.put("resultMessage", "근로장학금 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC58()) < 0){
			resultMap.put("resultMessage", "직무발명보상금 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC59()) < 0){
			resultMap.put("resultMessage", "주식매수선택권 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC60()) < 0){
			resultMap.put("resultMessage", "벤처기업 주식매수선택권 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC61a()) < 0){
			resultMap.put("resultMessage", "우리사주조합인출금 50% 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC61b()) < 0){
			resultMap.put("resultMessage", "우리사주조합인출금 75% 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC61c()) < 0){
			resultMap.put("resultMessage", "우리사주조합인출금 100% 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC62()) < 0){
			resultMap.put("resultMessage", "전공의수련 보조 수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC63()) < 0){
			resultMap.put("resultMessage", "외국인기술자 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC64a()) < 0){
			resultMap.put("resultMessage", "중소기업취업 청년 소득세 감면 100% 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC64b()) < 0){
			resultMap.put("resultMessage", "중소기업취업 청년 소득세 감면 50% 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC64c()) < 0){
			resultMap.put("resultMessage", "중소기업취업 청년 소득세 감면 70% 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC64d()) < 0){
			resultMap.put("resultMessage", "중소기업취업 청년 소득세 감면 90% 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC65()) < 0){
			resultMap.put("resultMessage", "조세조약 상교직자감면 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC68()) < 0){
			resultMap.put("resultMessage", "비과세 계 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC69()) < 0){
			resultMap.put("resultMessage", "감면소득 계 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC70()) < 0){
			resultMap.put("resultMessage", "총급여 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC71()) < 0){
			resultMap.put("resultMessage", "근로소득공제 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC72()) < 0){
			resultMap.put("resultMessage", "근로소득금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC73()) < 0){
			resultMap.put("resultMessage", "본인공제금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC74()) < 0){
			resultMap.put("resultMessage", "배우자공제금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC75b()) < 0){
			resultMap.put("resultMessage", "부양가족공제금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC76b()) < 0){
			resultMap.put("resultMessage", "경로우대공제금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC77b()) < 0){
			resultMap.put("resultMessage", "장애인공제금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC78()) < 0){
			resultMap.put("resultMessage", "부녀자공제금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC79()) < 0){
			resultMap.put("resultMessage", "한부모가족공제금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC80a()) < 0){
			resultMap.put("resultMessage", "국민연금보험료공제 대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC80b()) < 0){
			resultMap.put("resultMessage", "국민연금보험료공제 공제금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC81a()) < 0){
			resultMap.put("resultMessage", "공적연금보험료공제 공무원연금 대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC81b()) < 0){
			resultMap.put("resultMessage", "공적연금보험료공제 공무원연금 공제금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC82a()) < 0){
			resultMap.put("resultMessage", "공적연금보험료공제 군인연금 대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC82b()) < 0){
			resultMap.put("resultMessage", "공적연금보험료공제 군인연금 공제금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC83a()) < 0){
			resultMap.put("resultMessage", "공적연금보험료공제 사립학교교직원연금 대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC83b()) < 0){
			resultMap.put("resultMessage", "공적연금보험료공제 사립학교교직원연금 공제금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC84a()) < 0){
			resultMap.put("resultMessage", "공적연금보험료공제 별정우체국연금 대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC84b()) < 0){
			resultMap.put("resultMessage", "공적연금보험료공제 별정우체국연금 공제금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC85a()) < 0){
			resultMap.put("resultMessage", "보험료-건강보험료(노인장기요양보험료포함) 대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC85b()) < 0){
			resultMap.put("resultMessage", "보험료-건강보험료(노인장기요양보험료포함) 공제금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC86a()) < 0){
			resultMap.put("resultMessage", "보험료-고용보험료 대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC86b()) < 0){
			resultMap.put("resultMessage", "보험료-고용보험료 공제금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC87a()) < 0){
			resultMap.put("resultMessage", "주택자금 주택임차차입금원리금상환액 대출기관 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC87b()) < 0){
			resultMap.put("resultMessage", "주택자금 주택임차차입금원리금상환액 거주자 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC88a()) < 0){
			resultMap.put("resultMessage", "(2011년이전 차입분)주택자금 장기주택저당차입금이자상환액 15년미만 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC88b()) < 0){
			resultMap.put("resultMessage", "(2011년이전 차입분)주택자금 장기주택저당차입금이자상환액 15년~29년 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC88c()) < 0){
			resultMap.put("resultMessage", "(2011년이전 차입분)주택자금 장기주택저당차입금이자상환액 30년이상  정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC89a()) < 0){
			resultMap.put("resultMessage", "(2012년이후 차입분, 15년이상)고정금리.비거치식 상환대출  정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC89b()) < 0){
			resultMap.put("resultMessage", "(2012년이후 차입분, 15년이상)기타 대출 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC90a()) < 0){
			resultMap.put("resultMessage", "(2015년이후 차입분, 상환기간 15년이상)고정금리 and 비거치상환 대출 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC90b()) < 0){
			resultMap.put("resultMessage", "(2015년이후 차입분, 상환기간 15년이상)고정금리 or 비거치상환 대출 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC90c()) < 0){
			resultMap.put("resultMessage", "(2015년이후 차입분, 상환기간 15년이상)그 밖의 대출 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC90d()) < 0){
			resultMap.put("resultMessage", "(2015년이후 차입분, 상환기간 10년이상)고정금리 or 비거치상환 대출 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC91()) < 0){
			resultMap.put("resultMessage", "기부금(이월분) 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC95()) < 0){
			resultMap.put("resultMessage", "차감소득금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC96()) < 0){
			resultMap.put("resultMessage", "개인연금저축소득공제 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC97()) < 0){
			resultMap.put("resultMessage", "소기업.소상공인 공제부금 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC98()) < 0){
			resultMap.put("resultMessage", "주택마련저축소득공제 청약저축 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC99()) < 0){
			resultMap.put("resultMessage", "주택마련저축소득공제 주택청약종합저축 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC100()) < 0){
			resultMap.put("resultMessage", "주택마련저축소득공제 근로자주택마련저축 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC101()) < 0){
			resultMap.put("resultMessage", "투자조합출자등 소득공제 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC102()) < 0){
			resultMap.put("resultMessage", "신용카드등 소득공제 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC103()) < 0){
			resultMap.put("resultMessage", "우리사주조합출연금 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC104()) < 0){
			resultMap.put("resultMessage", "고용유지중소기업근로자소득공제 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC105()) < 0){
			resultMap.put("resultMessage", "장기집합투자증권저축 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC108()) < 0){
			resultMap.put("resultMessage", "그 밖의 소득공제계 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC109()) < 0){
			resultMap.put("resultMessage", "소득공제종합 한도초과액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC110()) < 0){
			resultMap.put("resultMessage", "종합소득 과세표준 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC111()) < 0){
			resultMap.put("resultMessage", "산출세액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC118()) < 0){
			resultMap.put("resultMessage", "세액감면계 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC119()) < 0){
			resultMap.put("resultMessage", "근로소득세액공제 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC120a()) < 0){
			resultMap.put("resultMessage", "자녀세액공제인원 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC120b()) < 0){
			resultMap.put("resultMessage", "자녀세액공제 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC121a()) < 0){
			resultMap.put("resultMessage", "출산.입양 세액공제인원 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC121b()) < 0){
			resultMap.put("resultMessage", "출산.입양 세액공제 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC122a()) < 0){
			resultMap.put("resultMessage", "연금계좌 과학기술인공제 공제대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC122b()) < 0){
			resultMap.put("resultMessage", "연금계좌 과학기술인공제 세액공제액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC123a()) < 0){
			resultMap.put("resultMessage", "연금계좌 근로자퇴직급여보장법에따른 퇴직급여 공제대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC123b()) < 0){
			resultMap.put("resultMessage", "연금계좌 근로자퇴직급여보장법에따른 퇴직급여 세액공제액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC124a()) < 0){
			resultMap.put("resultMessage", "연금계좌 연금저축 공제대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC124b()) < 0){
			resultMap.put("resultMessage", "연금계좌 연금저축 세액공제액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC125b()) < 0){
			resultMap.put("resultMessage", "특별세액공제 보장성보험료 세액공제액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC126b()) < 0){
			resultMap.put("resultMessage", "특별세액공제 장애인전용보장성보험료 세액공제액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC127a()) < 0){
			resultMap.put("resultMessage", "특별세액공제 의료비 공제대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC127b()) < 0){
			resultMap.put("resultMessage", "특별세액공제 의료비 세액공제액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC128a()) < 0){
			resultMap.put("resultMessage", "특별세액공제 교육비 공제대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC128b()) < 0){
			resultMap.put("resultMessage", "특별세액공제 교육비 세액공제액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC129a()) < 0){
			resultMap.put("resultMessage", "특별세액공제 기부금 정치자금 10만원이하 공제대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC129b()) < 0){
			resultMap.put("resultMessage", "특별세액공제 기부금 정치자금 10만원이하 세액공제액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC130a()) < 0){
			resultMap.put("resultMessage", "특별세액공제 기부금 정치자금 10만원초과 공제대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC130b()) < 0){
			resultMap.put("resultMessage", "특별세액공제 기부금 정치자금 10만원초과 세액공제액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC131a()) < 0){
			resultMap.put("resultMessage", "특별세액공제 기부금 법정기부금 공제대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC131b()) < 0){
			resultMap.put("resultMessage", "특별세액공제 기부금 법정기부금 세액공제액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC132a()) < 0){
			resultMap.put("resultMessage", "특별세액공제 기부금 우리사주조합기부금 공제대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC132b()) < 0){
			resultMap.put("resultMessage", "특별세액공제 기부금 우리사주조합기부금 세액공제액  정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC133a()) < 0){
			resultMap.put("resultMessage", "특별세액공제 기부금 지정기부금 종교단체외 공제대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC133b()) < 0){
			resultMap.put("resultMessage", "특별세액공제 기부금 지정기부금 종교단체외 세액공제액  정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC134a()) < 0){
			resultMap.put("resultMessage", "특별세액공제 기부금 지정기부금 종교단체 공제대상금액  정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC134b()) < 0){
			resultMap.put("resultMessage", "특별세액공제 기부금 지정기부금 종교단체 세액공제액  정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC137()) < 0){
			resultMap.put("resultMessage", "특별세액공제계 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC138()) < 0){
			resultMap.put("resultMessage", "표준세액공제 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC139()) < 0){
			resultMap.put("resultMessage", "납세조합공제 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC140()) < 0){
			resultMap.put("resultMessage", "주택차입금 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC141()) < 0){
			resultMap.put("resultMessage", "외국납부 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC142a()) < 0){
			resultMap.put("resultMessage", "월세세액공제 대상금액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC142b()) < 0){
			resultMap.put("resultMessage", "월세세액공제액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC145()) < 0){
			resultMap.put("resultMessage", "세액공제계 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC146a()) < 0){
			resultMap.put("resultMessage", "결정세액-소득세 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC146b()) < 0){
			resultMap.put("resultMessage", "결정세액-지방소득세 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC146c()) < 0){
			resultMap.put("resultMessage", "결정세액-농특세 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC147a()) < 0){
			resultMap.put("resultMessage", "주현근무지-소득세  정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC147b()) < 0){
			resultMap.put("resultMessage", "주현근무지-지방소득세 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC147c()) < 0){
			resultMap.put("resultMessage", "주현근무지-농특세 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC148a()) < 0){
			resultMap.put("resultMessage", "납부특례세액-소득세 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC148b()) < 0){
			resultMap.put("resultMessage", "납부특례세액-지방소득세 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(cRecordVO.getC148c()) < 0){
			resultMap.put("resultMessage", "납부특례세액-농특세 정보가  0보다 작습니다.");
		}else{
			resultMap.put("result", "1");
			resultMap.put("resultMessage", "sucess");
		}

		return resultMap;
	}
	
	
	/**
	 * D 레코드의 필수값 체크
	 * 
	 * @param dRecordVO
	 * @return
	 */
	public Map<String,Object> dRecordValidator(DRecordVO dRecordVO) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		resultMap.put("result", "0");
		resultMap.put("resultMessage", "dRecordValidator");
		
		if(dRecordVO == null){		
			resultMap.put("resultMessage", "종(전)근무처 정보가 없습니다.");
		}else if(StringUtils.isEmpty(dRecordVO.getD5())){
			resultMap.put("resultMessage", "사업자등록번호 정보가 없습니다.");
		}else if(StringUtils.isEmpty(dRecordVO.getD6())){
			resultMap.put("resultMessage", "소득자주민등록번호 정보가 없습니다.");
		}else if(!StringUtils.equals("1", dRecordVO.getD7()) && !StringUtils.equals("2", dRecordVO.getD7())){
			resultMap.put("resultMessage", "납세조합 여부가 '1','2'가 아닙니다.");
		}else if(StringUtils.isEmpty(dRecordVO.getD8())){
			resultMap.put("resultMessage", "법인명(상호) 정보가 없습니다.");
		}else if(StringUtils.isEmpty(dRecordVO.getD9())){
			resultMap.put("resultMessage", "사업자등록번호 정보가 없습니다.");
		}else if(StringUtils.isEmpty(dRecordVO.getD10())){
			resultMap.put("resultMessage", "근무기간 시작연월일 정보가 없습니다.");
		}else if(StringUtils.isEmpty(dRecordVO.getD11())){
			resultMap.put("resultMessage", "근무기간 종료연월일 정보가 없습니다.");
		}else if(StringUtil.strPaserInt(dRecordVO.getD10()) > StringUtil.strPaserInt(dRecordVO.getD11())){
			resultMap.put("resultMessage", "근무기간 종료연월일이 근무기간 시작연월일이 보다 빠릅니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD14()) < 0){
			resultMap.put("resultMessage", "급여 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD15()) < 0){
			resultMap.put("resultMessage", "상여 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD16()) < 0){
			resultMap.put("resultMessage", "인정상여 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD17()) < 0){
			resultMap.put("resultMessage", "주식매수선택권행사이익 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD18()) < 0){
			resultMap.put("resultMessage", "우리사주조합인출금 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD19()) < 0){
			resultMap.put("resultMessage", "임원퇴직소득한도 초과액 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD20()) < 0){
			resultMap.put("resultMessage", "직무발명보상금 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD23()) < 0){
			resultMap.put("resultMessage", "종전근무처의 소득 합계 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD24()) < 0){
			resultMap.put("resultMessage", "비과세학자금 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD25()) < 0){
			resultMap.put("resultMessage", "무보수위원수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD26()) < 0){
			resultMap.put("resultMessage", "경호.승선수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD27()) < 0){
			resultMap.put("resultMessage", "유아.초중등 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD28()) < 0){
			resultMap.put("resultMessage", "고등교육법 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD29()) < 0){
			resultMap.put("resultMessage", "특별법 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD30()) < 0){
			resultMap.put("resultMessage", "연구기관 등 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD31()) < 0){
			resultMap.put("resultMessage", "기업부설연구소 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD32()) < 0){
			resultMap.put("resultMessage", "보육교사 근무환경 개선비 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD33()) < 0){
			resultMap.put("resultMessage", "사립유치원 수석교사.교사의인건비 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD34()) < 0){
			resultMap.put("resultMessage", "취재수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD35()) < 0){
			resultMap.put("resultMessage", "벽지수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD36()) < 0){
			resultMap.put("resultMessage", "재해관련급여 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD37()) < 0){
			resultMap.put("resultMessage", "정부공공기관 지방이전기관종사자 이주수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD38()) < 0){
			resultMap.put("resultMessage", "종교활동비 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD39()) < 0){
			resultMap.put("resultMessage", "외국정보등근무자 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD40()) < 0){
			resultMap.put("resultMessage", "외국주둔군인등 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD41()) < 0){
			resultMap.put("resultMessage", "국외근로100만원 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD42()) < 0){
			resultMap.put("resultMessage", "국외근로300만원 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD43()) < 0){
			resultMap.put("resultMessage", "국외근로 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD44()) < 0){
			resultMap.put("resultMessage", "야간근로수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD45()) < 0){
			resultMap.put("resultMessage", "출산보육수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD46()) < 0){
			resultMap.put("resultMessage", "근로장학금 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD47()) < 0){
			resultMap.put("resultMessage", "직무발명보상금 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD48()) < 0){
			resultMap.put("resultMessage", "주식매수선택권 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD48()) > 30000000){
			resultMap.put("resultMessage", "주식매수선택권 정보가  3000만원을 초과하였습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD49()) < 0){
			resultMap.put("resultMessage", "벤처기업주식매수선택권 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD48()) > 20000000){
			resultMap.put("resultMessage", "벤처기업주식매수선택권 정보가  2000만원을 초과하였습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD50a()) < 0){
			resultMap.put("resultMessage", "우리사주조합인출금 50% 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD50b()) < 0){
			resultMap.put("resultMessage", "우리사주조합인출금 75% 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD50c()) < 0){
			resultMap.put("resultMessage", "우리사주조합인출금 100% 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD51()) < 0){
			resultMap.put("resultMessage", "전공의수련 보조수당 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD52()) < 0){
			resultMap.put("resultMessage", "외국인기술자 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD53a()) < 0){
			resultMap.put("resultMessage", "중소기업취업청년 소득세 감면 100% 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD53b()) < 0){
			resultMap.put("resultMessage", "중소기업취업청년 소득세 감면 50% 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD53c()) < 0){
			resultMap.put("resultMessage", "중소기업취업청년 소득세 감면 70% 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD53d()) < 0){
			resultMap.put("resultMessage", "중소기업취업청년 소득세 감면 90%  정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD54()) < 0){
			resultMap.put("resultMessage", "조세조약상 교직자 감면 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD57()) < 0){
			resultMap.put("resultMessage", "비과세 계 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD58()) < 0){
			resultMap.put("resultMessage", "감면소득 계 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD59a()) < 0){
			resultMap.put("resultMessage", "소득세 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD59b()) < 0){
			resultMap.put("resultMessage", "지방소득세 정보가  0보다 작습니다.");
		}else if(StringUtil.strPaserLong(dRecordVO.getD59c()) < 0){
			resultMap.put("resultMessage", "농특세 정보가  0보다 작습니다.");
		}else{
			resultMap.put("result", "1");
			resultMap.put("resultMessage", "sucess");
		}
		
		
		return resultMap;
	}
	
	/**
	 * 
	 * CA 레코드 (의료비지급명세서 레코드) 필수정보 체크
	 * 
	 * @param caRecordFieldList
	 * @param caRecordVO
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public Map<String,String> caRecordValidator(List<YC200VO> caRecordFieldList, CARecordVO caRecordVO) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		
		Map<String,String> resultMap = new HashMap<String,String>();
		
		resultMap.put("result", "0");
		resultMap.put("resultMessage", "caRecordValidator");
		
		int chk = 0;		
		
		Class<?> cls = caRecordVO.getClass();
		if(caRecordFieldList != null && caRecordFieldList.size() > 0){			
			if(caRecordVO != null){
			
				for(YC200VO rstVO : caRecordFieldList){
					
//					String filedValue = "";
					Field filed = cls.getDeclaredField(rstVO.get번호());
					boolean accessible = filed.isAccessible();
					filed.setAccessible(true);
										
					//지급처 사업자등록번호
					if(StringUtils.equals("CA14", filed.getName())){
						String filedVal = (String)filed.get(caRecordVO);
						//국세청장이 제공하는 의료비 자료가 아닌경우 
						if(!StringUtils.equals("1", caRecordVO.getCA16()) && StringUtils.isEmpty(filedVal)){
							resultMap.put("resultMessage","[ CA 레코드 - "+caRecordVO.getCA10()+" ("+caRecordVO.getCA13()+") ] 지급처 사업자등록번호 정보가 없습니다.");
							
							chk = 1;
							break;
						}
					}
					//지급처 상호
					else if(StringUtils.equals("CA15", filed.getName())){
						String filedVal = (String)filed.get(caRecordVO);
						//국세청장이 제공하는 의료비 자료가 아닌경우 
						if(!StringUtils.equals("1", caRecordVO.getCA16()) && StringUtils.isEmpty(filedVal)){
							resultMap.put("resultMessage","[ CA 레코드 - "+caRecordVO.getCA10()+" ("+caRecordVO.getCA13()+") ] 지급처 상호 정보가 없습니다.");
							
							chk = 1;
							break;
						}
					}else{
						//널여부 체크 
						if(StringUtils.equals("0", rstVO.get널여부())){						
							if(StringUtil.isNull((String)filed.get(caRecordVO))){							
								resultMap.put("resultMessage","[ CA 레코드 - "+caRecordVO.getCA10()+" ("+caRecordVO.getCA13()+") ] " + rstVO.get서식항목() + " 정보가 없습니다.");
								
								chk = 1;
								break;
							}						
						}
					
						//최소값이 있는경우 최소값 비교 
						if(StringUtil.isNotNull(rstVO.get최소값())){
							if(StringUtil.strPaserLong((String)filed.get(caRecordVO)) <  StringUtil.strPaserLong(rstVO.get최소값())){
								resultMap.put("resultMessage","[ CA 레코드 - "+caRecordVO.getCA10()+" ("+caRecordVO.getCA13()+") ] " + rstVO.get서식항목() + " 정보가  "+rstVO.get최소값()+" 보다 작습니다.");
								
								chk = 1;
								break;
							}
						}
					}
										
//					filed.set(caRecordVO, filedValue);
					filed.setAccessible(accessible);
				}
				
			}			
		}
		
		if(chk == 0){
			resultMap.put("result", "1");
			resultMap.put("resultMessage", "success");
		}
		
		return resultMap;
	}
	
}
