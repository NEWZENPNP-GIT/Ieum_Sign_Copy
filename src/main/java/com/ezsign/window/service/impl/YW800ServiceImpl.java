package com.ezsign.window.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.feb.etc.dao.YE501DAO;
import com.ezsign.feb.etc.dao.YE502DAO;
import com.ezsign.feb.etc.dao.YE503DAO;
import com.ezsign.feb.etc.vo.YE501VO;
import com.ezsign.feb.etc.vo.YE502VO;
import com.ezsign.feb.etc.vo.YE503VO;
import com.ezsign.feb.house.dao.YE101DAO;
import com.ezsign.feb.house.dao.YE104DAO;
import com.ezsign.feb.house.dao.YE105DAO;
import com.ezsign.feb.house.dao.YE106DAO;
import com.ezsign.feb.house.dao.YE107DAO;
import com.ezsign.feb.house.dao.YE108DAO;
import com.ezsign.feb.house.dao.YE109DAO;
import com.ezsign.feb.house.dao.YE710DAO;
import com.ezsign.feb.house.vo.YE101VO;
import com.ezsign.feb.house.vo.YE104VO;
import com.ezsign.feb.house.vo.YE105VO;
import com.ezsign.feb.house.vo.YE106VO;
import com.ezsign.feb.house.vo.YE107VO;
import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.house.vo.YE109VO;
import com.ezsign.feb.house.vo.YE710VO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.other.dao.YE201DAO;
import com.ezsign.feb.other.dao.YE202DAO;
import com.ezsign.feb.other.dao.YE203DAO;
import com.ezsign.feb.other.dao.YE204DAO;
import com.ezsign.feb.other.vo.YE201VO;
import com.ezsign.feb.other.vo.YE202VO;
import com.ezsign.feb.other.vo.YE203VO;
import com.ezsign.feb.other.vo.YE204VO;
import com.ezsign.feb.pension.dao.YE301DAO;
import com.ezsign.feb.pension.dao.YE302DAO;
import com.ezsign.feb.pension.vo.YE301VO;
import com.ezsign.feb.pension.vo.YE302VO;
import com.ezsign.feb.special.dao.YE401DAO;
import com.ezsign.feb.special.dao.YE402DAO;
import com.ezsign.feb.special.dao.YE403DAO;
import com.ezsign.feb.special.dao.YE404DAO;
import com.ezsign.feb.special.dao.YE405DAO;
import com.ezsign.feb.special.dao.YE406DAO;
import com.ezsign.feb.special.dao.YE407DAO;
import com.ezsign.feb.special.dao.YE408DAO;
import com.ezsign.feb.special.dao.YE409DAO;
import com.ezsign.feb.special.dao.YE410DAO;
import com.ezsign.feb.special.dao.YE411DAO;
import com.ezsign.feb.special.vo.YE401VO;
import com.ezsign.feb.special.vo.YE402VO;
import com.ezsign.feb.special.vo.YE403VO;
import com.ezsign.feb.special.vo.YE404VO;
import com.ezsign.feb.special.vo.YE405VO;
import com.ezsign.feb.special.vo.YE406VO;
import com.ezsign.feb.special.vo.YE407VO;
import com.ezsign.feb.special.vo.YE408VO;
import com.ezsign.feb.special.vo.YE409VO;
import com.ezsign.feb.special.vo.YE410VO;
import com.ezsign.feb.special.vo.YE411VO;
import com.ezsign.feb.system.dao.YS000DAO;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.worker.dao.YE001DAO;
import com.ezsign.feb.worker.dao.YE002DAO;
import com.ezsign.feb.worker.dao.YE003DAO;
import com.ezsign.feb.worker.dao.YE051DAO;
import com.ezsign.feb.worker.dao.YE052DAO;
import com.ezsign.feb.worker.dao.YE700DAO;
import com.ezsign.feb.worker.service.YE750Service;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.feb.worker.vo.YE002VO;
import com.ezsign.feb.worker.vo.YE051VO;
import com.ezsign.feb.worker.vo.YE052VO;
import com.ezsign.feb.worker.vo.YE700VO;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.window.service.YW800Service;
import com.ezsign.window.vo.YW800ConfirmRequest;
import com.ezsign.window.vo.YW800DonationResponse;
import com.ezsign.window.vo.YW800Request;
import com.ezsign.window.vo.YW800Response;
import com.ezsign.window.vo.YW800UpdateRequest;
import com.ezsign.window.vo.YW800VO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@SuppressWarnings("NonAsciiCharacters")
@Service("yw800Service")
public class YW800ServiceImpl extends AbstractServiceImpl implements YW800Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "ys000DAO")
    private YS000DAO ys000DAO;

    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;

    @Resource(name = "ye001DAO")
    private YE001DAO ye001DAO;

    @Resource(name = "ye002DAO")
    private YE002DAO ye002DAO;

    @Resource(name = "ye003DAO")
    private YE003DAO ye003DAO;

    @Resource(name = "ye051DAO")
    private YE051DAO ye051DAO;

    @Resource(name = "ye052DAO")
    private YE052DAO ye052DAO;

    @Resource(name = "ye101DAO")
    private YE101DAO ye101DAO;

    @Resource(name = "ye104DAO")
    private YE104DAO ye104DAO;

    @Resource(name = "ye106DAO")
    private YE106DAO ye106DAO;

    @Resource(name = "ye107DAO")
    private YE107DAO ye107DAO;

    @Resource(name = "ye108DAO")
    private YE108DAO ye108DAO;

    @Resource(name = "ye109DAO")
    private YE109DAO ye109DAO;

    @Resource(name = "ye201DAO")
    private YE201DAO ye201DAO;

    @Resource(name = "ye202DAO")
    private YE202DAO ye202DAO;

    @Resource(name = "ye203DAO")
    private YE203DAO ye203DAO;

    @Resource(name = "ye204DAO")
    private YE204DAO ye204DAO;

    @Resource(name = "ye301DAO")
    private YE301DAO ye301DAO;

    @Resource(name = "ye302DAO")
    private YE302DAO ye302DAO;

    @Resource(name = "ye406DAO")
    private YE406DAO ye406DAO;

    @Resource(name = "ye401DAO")
    private YE401DAO ye401DAO;

    @Resource(name = "ye402DAO")
    private YE402DAO ye402DAO;

    @Resource(name = "ye403DAO")
    private YE403DAO ye403DAO;

    @Resource(name = "ye404DAO")
    private YE404DAO ye404DAO;

    @Resource(name = "ye405DAO")
    private YE405DAO ye405DAO;

    @Resource(name = "ye407DAO")
    private YE407DAO ye407DAO;

    @Resource(name = "ye408DAO")
    private YE408DAO ye408DAO;    

    @Resource(name = "ye501DAO")
    private YE501DAO ye501DAO;

    @Resource(name = "ye502DAO")
    private YE502DAO ye502DAO;

    @Resource(name = "ye503DAO")
    private YE503DAO ye503DAO;

    @Resource(name = "ye105DAO")
    private YE105DAO ye105DAO;

    @Resource(name = "ye700DAO")
    private YE700DAO ye700DAO;

    @Resource(name = "ye710DAO")
    private YE710DAO ye710DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;

    @Resource(name = "ye750Service")
    private YE750Service ye750Service;

    @Resource(name = "ye409DAO")
    private YE409DAO ye409DAO;
    
    @Resource(name = "ye410DAO")
    private YE410DAO ye410DAO;
    
    @Resource(name = "ye411DAO")
    private YE411DAO ye411DAO;
    
    @Override
    public void getDonationYW800(String 계약ID, String 사용자ID, String 세액공제구분코드, YW800DonationResponse response) throws Exception {
        YE407VO vo = new YE407VO();
        vo.set계약ID(계약ID);
        vo.set사용자ID(사용자ID);
        vo.set세액공제구분코드(세액공제구분코드);
        response.기부금계산결과 = ye407DAO.getYE407List(vo);
    }

    @Override
    public void getYW800(String bizId, String 계약ID, String 사용자ID, YW800Response response) throws Exception {
        response.계약년도 = response.근무년월.계약년도;  // TODO 계약년도 지울것 (근무년월에 포함)
        response.진행상태코드 = response.근무년월.진행상태코드;  // TODO 진행상태코드 지울것 (근무년월에 포함)

        logger.info("# getYW800 => 사용자ID : " + 사용자ID );
        
        // 진행상태코드, 소득세적용률, 연말정산분납여부 가져오기
        YE000VO ye000 = new YE000VO();
        ye000.set계약ID(계약ID);
        ye000.set사용자ID(사용자ID);
        ye000.setBizId(bizId);
        YE000VO ye000Result = ye000DAO.getYE000(ye000);
        if (ye000Result != null) {
            response.소득세적용률 = ye000Result.get소득세적용률();
            response.연말정산분납여부 = ye000Result.get연말정산분납여부();
            response.거주구분 = ye000Result.get거주구분();
        }

        // 기존 요약 결과 가져오기
        YE700VO ye700 = new YE700VO();
        ye700.set계약ID(계약ID);
        ye700.set사용자ID(사용자ID);
        response.연말정산요약결과 = ye700DAO.getYE700(ye700);

        // 주택관련 설문지 가져오기
        YE710VO ye710 = new YE710VO();
        ye710.set계약ID(계약ID);
        ye710.set사용자ID(사용자ID);
        response.주택관련소득공제신청확인서 = ye710DAO.getYE710(ye710);

        // 4대보험 회사자료 가져오기
        get회사자료(계약ID, 사용자ID, response);

        get인적공제(계약ID, 사용자ID, response);

        get연금보험료공제(계약ID, 사용자ID, response);

        get특별소득공제_보험료(계약ID, 사용자ID, response);
        get특별소득공제_주택임차차입금원리금상환액(계약ID, 사용자ID, response);
        get특별소득공제_장기주택저당차입금이자상환액(계약ID, 사용자ID, response);

        get개인연금저축(계약ID, 사용자ID, response);
        get주택마련저축(계약ID, 사용자ID, response);
        get신용카드(계약ID, 사용자ID, response);
        get장기집합투자증권저축(계약ID, 사용자ID, response);

        get그밖의소득공제_소기업소상공인공제부금(계약ID, 사용자ID, response);
        get그밖의소득공제_투자조합출자등(계약ID, 사용자ID, response);
        get그밖의소득공제_우리사주조합출연금(계약ID, 사용자ID, response);
        get그밖의소득공제_우리사주조합출연금벤처(계약ID, 사용자ID, response);
        get그밖의소득공제_고용유지중소기업근로자(계약ID, 사용자ID, response);

        get세액감면(bizId, 계약ID, 사용자ID, response);

        get연금계좌_퇴직연금(계약ID, 사용자ID, response);
        get연금계좌_연금저축(계약ID, 사용자ID, response);

        get특별세액공제_보험료(계약ID, 사용자ID, response);
        get특별세액공제_의료비안경구입비(계약ID, 사용자ID, response);
        get특별세액공제_교육비교복구입비체험학습비(계약ID, 사용자ID, response);
        get특별세액공제_기부금(계약ID, 사용자ID, response);

        get특별소득공제_기부금이월분(계약ID, 사용자ID, response);

        get납세조합공제(계약ID, 사용자ID, response);
        get주택차입금(계약ID, 사용자ID, response);
        get외국납부세액(계약ID, 사용자ID, response);
        get월세액(계약ID, 사용자ID, response);
    }

    @Override
    public void saveYW800(String bizId, String 작업자ID, YW800Request request) throws Exception {
        String 세액공제구분코드 = "1";

        logger.info("# saveYW800 => 사용자ID : " + request.사용자ID );
        
        YE000VO ye000VO = new YE000VO();
        ye000VO.set계약ID(request.계약ID);
        ye000VO.set사용자ID(request.사용자ID);

        if (request.소득세적용률 != null && request.연말정산분납여부 != null) {
            ye000VO.set소득세적용률(request.소득세적용률);
            ye000VO.set연말정산분납여부(request.연말정산분납여부);
            ye000DAO.updYE000(ye000VO);
        }


        YE700VO ye700VO = new YE700VO();
        ye700VO.set계약ID(request.계약ID);
        ye700VO.set사용자ID(request.사용자ID);
        
        // 계약년도
        YS000VO ys000VO = new YS000VO();
        ys000VO.setBizId(bizId);
        ys000VO.set계약ID(request.계약ID);;
        List<YS000VO> ys000List = ys000DAO.getYS000List(ys000VO);
        String 계약년도 = "";
        
        if (ys000List != null && ys000List.size() > 0) {
        	계약년도 = ys000List.get(0).getFebYear();
        }
        
        if (request.연말정산요약 != null) {
            ye700DAO.allDelYE700(ye700VO);

            List<YE000VO> listYE000 = ye003DAO.getYE003TaxList(ye000VO);

            for (YE700VO vo : request.연말정산요약) {
                if ("1".equals(vo.get사용여부())) {
                    세액공제구분코드 = vo.get세액공제구분코드();
                }

                vo.set작업자ID(작업자ID);
                if (listYE000.size() >= 3) {
                    vo.set종1_사업자번호(listYE000.get(0).get사업자등록번호());
                    vo.set종1_소득세(StringUtil.strPaserInt(listYE000.get(0).get소득세()));
                    vo.set종1_지방소득세(StringUtil.strPaserInt(listYE000.get(0).get지방소득세()));
                    vo.set종1_농어촌특별세(StringUtil.strPaserInt(listYE000.get(0).get농어촌특별세()));
                    vo.set종2_사업자번호(listYE000.get(1).get사업자등록번호());
                    vo.set종2_소득세(StringUtil.strPaserInt(listYE000.get(1).get소득세()));
                    vo.set종2_지방소득세(StringUtil.strPaserInt(listYE000.get(1).get지방소득세()));
                    vo.set종2_농어촌특별세(StringUtil.strPaserInt(listYE000.get(1).get농어촌특별세()));
                    vo.set종3_사업자번호(listYE000.get(2).get사업자등록번호());
                    vo.set종3_소득세(StringUtil.strPaserInt(listYE000.get(2).get소득세()));
                    vo.set종3_지방소득세(StringUtil.strPaserInt(listYE000.get(2).get지방소득세()));
                    vo.set종3_농어촌특별세(StringUtil.strPaserInt(listYE000.get(2).get농어촌특별세()));
                } else if (listYE000.size() == 2) {
                    vo.set종1_사업자번호(listYE000.get(0).get사업자등록번호());
                    vo.set종1_소득세(StringUtil.strPaserInt(listYE000.get(0).get소득세()));
                    vo.set종1_지방소득세(StringUtil.strPaserInt(listYE000.get(0).get지방소득세()));
                    vo.set종1_농어촌특별세(StringUtil.strPaserInt(listYE000.get(0).get농어촌특별세()));
                    vo.set종2_사업자번호(listYE000.get(1).get사업자등록번호());
                    vo.set종2_소득세(StringUtil.strPaserInt(listYE000.get(1).get소득세()));
                    vo.set종2_지방소득세(StringUtil.strPaserInt(listYE000.get(1).get지방소득세()));
                    vo.set종2_농어촌특별세(StringUtil.strPaserInt(listYE000.get(1).get농어촌특별세()));
                } else if (listYE000.size() == 1) {
                    vo.set종1_사업자번호(listYE000.get(0).get사업자등록번호());
                    vo.set종1_소득세(StringUtil.strPaserInt(listYE000.get(0).get소득세()));
                    vo.set종1_지방소득세(StringUtil.strPaserInt(listYE000.get(0).get지방소득세()));
                    vo.set종1_농어촌특별세(StringUtil.strPaserInt(listYE000.get(0).get농어촌특별세()));
                }

                ye700DAO.insYE700(vo);
            }
        }

        if (request.기부금계산결과 != null) {
            YE407VO ye407VO = new YE407VO();
            ye407VO.set계약ID(request.계약ID);
            ye407VO.set사용자ID(request.사용자ID);
            ye407DAO.allDelYE407(ye407VO);

            for (YE407VO vo : request.기부금계산결과) {
                vo.set작업자ID(작업자ID);
                ye407DAO.insYE407(vo);
            }
            
            // 기부금계산결과 기부금조정명세 반영 (해당년도공제금액, 이월금액)
            // 연말정산 Map에 전년도까지공제금액, 공제대상기부금, 공제대상계산기부금 표시
            YE408VO ye408 = new YE408VO();
            ye408.set계약ID(request.계약ID);
            ye408.set사용자ID(request.사용자ID);
            ye408.setStartPage(0);
            ye408.setEndPage(9999);
            ye408.setSortName("기부년도 DESC, 기부금종류코드");
            
            
            Map<String,YE408VO> upMap = new HashMap<String,YE408VO>();
            List<YE408VO> listYE408 = ye408DAO.getYE408List(ye408);
            for (YE408VO ye408VO : listYE408) { 
            	
            	YE408VO updYE408 = new YE408VO();
            	updYE408.set계약ID(ye408VO.get계약ID());
            	updYE408.set사용자ID(ye408VO.get사용자ID());
            	updYE408.set작업자ID(ye408VO.get작업자ID());
            	updYE408.set일련번호(ye408VO.get일련번호());
            	updYE408.set전년도까지공제금액(ye408VO.get전년도까지공제금액());
            	updYE408.set공제대상기부금(ye408VO.get공제대상기부금());
            	updYE408.set공제대상계산기부금(ye408VO.get공제대상계산기부금());
            	
                for (YE407VO vo : request.기부금계산결과) {
                	if(vo.get기부년도().equals(ye408VO.get기부년도()) &&
                		vo.get기부코드().equals(ye408VO.get기부금종류코드()) &&
                		vo.get세액공제구분코드().equals(세액공제구분코드)) {
                		
                		
                		if(upMap.get(ye408VO.get기부년도()+"_"+ye408VO.get기부금종류코드()+"_"+세액공제구분코드) != null){
                			updYE408 = upMap.get(ye408VO.get기부년도()+"_"+ye408VO.get기부금종류코드()+"_"+세액공제구분코드);
                			
                			updYE408.set해당연도공제금액(vo.get공제대상금액() + updYE408.get해당연도공제금액());
    	                	updYE408.set이월금액(vo.get공제초과이월액() + updYE408.get이월금액());    	                	
    	                	
    	                	if(StringUtils.equals("2", 세액공제구분코드) && !StringUtils.equals("20", vo.get기부코드()) && !StringUtils.equals("42", vo.get기부코드()) && StringUtils.equals(vo.get기부년도(), 계약년도)){
    	                		updYE408.set소멸금액(vo.get지출액() + updYE408.get소멸금액());
    	                	}else{
    	                		updYE408.set소멸금액(vo.get소멸금액() + updYE408.get소멸금액());
    	                	}
    	                	
                		}else{
                			updYE408.set해당연도공제금액(vo.get공제대상금액());
    	                	updYE408.set이월금액(vo.get공제초과이월액());
    	                	
    	                	if(StringUtils.equals("2", 세액공제구분코드) && !StringUtils.equals("20", vo.get기부코드()) && !StringUtils.equals("42", vo.get기부코드()) && StringUtils.equals(vo.get기부년도(), 계약년도)){
    	                		updYE408.set소멸금액(vo.get지출액());
    	                	}else{
    	                		updYE408.set소멸금액(vo.get소멸금액());
    	                	}
    	                	
    	                	upMap.put(ye408VO.get기부년도()+"_"+ye408VO.get기부금종류코드()+"_"+세액공제구분코드, updYE408);
                		}                		
                	}
                }
            	
            }
            
            // 기부금 계산결과를 업데이트 한다.
            for(Map.Entry<String,YE408VO> entry : upMap.entrySet()){
            	YE408VO updYE408 = entry.getValue();            	
            	ye408DAO.updYE408(updYE408);
            }

            
//            for (YE408VO ye408VO : listYE408) {
//            	YE408VO updYE408 = new YE408VO();
//            	updYE408.set계약ID(ye408VO.get계약ID());
//            	updYE408.set사용자ID(ye408VO.get사용자ID());
//            	updYE408.set작업자ID(ye408VO.get작업자ID());
//            	updYE408.set일련번호(ye408VO.get일련번호());
//            	updYE408.set전년도까지공제금액(ye408VO.get전년도까지공제금액());
//            	updYE408.set공제대상기부금(ye408VO.get공제대상기부금());
//            	updYE408.set공제대상계산기부금(ye408VO.get공제대상계산기부금());
//            	
//                for (YE407VO vo : request.기부금계산결과) {
//                	if(vo.get기부년도().equals(ye408VO.get기부년도()) &&
//                		vo.get기부코드().equals(ye408VO.get기부금종류코드()) &&
//                		vo.get세액공제구분코드().equals(세액공제구분코드)) {
//	                	updYE408.set해당연도공제금액(vo.get공제대상금액());
//	                	updYE408.set이월금액(vo.get공제초과이월액());
//	                	
//	                	ye408DAO.updYE408(updYE408);
//	                	
//                	}
//                }
//            	
//            }

            YE405VO ye405VO = new YE405VO();
            ye405VO.set계약ID(request.계약ID);
            ye405VO.set사용자ID(request.사용자ID);
            ye405VO.setStartPage(0);
            ye405VO.setEndPage(9999);
            List<YE405VO> listYE405 = ye405DAO.getYE405List(ye405VO);

            int 공제액;
            int 이월금액;
            int 소멸금액;
            for (YE405VO vo : listYE405) {
                공제액 = 0;
                이월금액 = 0;
                소멸금액 = 0;

                for (YE407VO vo2 : request.기부금계산결과) {
                    if (세액공제구분코드.equals(vo2.get세액공제구분코드()) &&
                            vo.get기부년도().equals(vo2.get기부년도()) &&
                            vo.get기부금종류코드().equals(vo2.get기부코드())) {
                        공제액 += vo2.get공제액();
                        이월금액 += vo2.get공제초과이월액();
                        소멸금액 += vo2.get소멸금액();
                    }
                }

                vo.set작업자ID(작업자ID);
                vo.set해당연도공제금액(공제액);
                vo.set이월금액(이월금액);
                vo.set소멸금액(소멸금액);

                ye405DAO.updYE405Disable(vo);
                ye405DAO.insYE405(vo);
            }
        }

        // 의료비 계산과정 저장
        if (request.의료비계산결과 != null) {
            YE409VO ye409VO = new YE409VO();
            ye409VO.set계약ID(request.계약ID);
            ye409VO.set사용자ID(request.사용자ID);
            ye409DAO.allDelYE409(ye409VO);

            for (YE409VO vo : request.의료비계산결과) {
                vo.set작업자ID(작업자ID);
                ye409DAO.insYE409(vo);
            }
        }
            
        // 신용카드 계산과정 저장
        if (request.신용카드계산결과항목 != null && request.신용카드계산결과금액 != null) {
        	YE410VO ye410VO = new YE410VO();
        	ye410VO.set계약ID(request.계약ID);
        	ye410VO.set사용자ID(request.사용자ID);
        	ye410DAO.allDelYE410(ye410VO);
        	
        	for (YE410VO vo : request.신용카드계산결과항목) {
        		vo.set작업자ID(작업자ID);
        		ye410DAO.insYE410(vo);
        	}
        	
        	YE411VO ye411VO = new YE411VO();
        	ye411VO.set계약ID(request.계약ID);
        	ye411VO.set사용자ID(request.사용자ID);
        	ye411DAO.allDelYE411(ye411VO);
        	
        	ye411VO.set작업자ID(작업자ID);
        	ye411VO.set공제제외금액(request.신용카드계산결과금액.get공제제외금액());
        	ye411VO.set공제가능금액(request.신용카드계산결과금액.get공제가능금액());
        	ye411VO.set공제한도(request.신용카드계산결과금액.get공제한도());
        	ye411VO.set일반공제금액(request.신용카드계산결과금액.get일반공제금액());
        	ye411VO.set추가공제금액(request.신용카드계산결과금액.get추가공제금액());
        	ye411VO.set최종공제금액(request.신용카드계산결과금액.get최종공제금액());
        	ye411DAO.insYE411(ye411VO);
        }
        
        ye700DAO.callProcDistribution(ye700VO);
        /* 2019.02.18 - PDF영수증 생성 주석 처리  */
        //ye750Service.createYE750Pdf(bizId, request.계약ID, request.사용자ID, request.연발정산조회);
    }

    @Override
    public void updYW800(String bizId, String 작업자ID, YW800UpdateRequest request, HttpServletRequest httpRequest) throws Exception {
        YE700VO ye700 = new YE700VO();
        ye700.set계약ID(request.계약ID);
        ye700.set사용자ID(request.사용자ID);
        ye700.set세액공제구분코드(request.세액공제구분코드);
        ye700.set작업자ID(작업자ID);
        ye700DAO.updYE700(ye700);

        //ye750Service.createYE750Pdf(bizId, request.계약ID, request.사용자ID, httpRequest);
    }

    @Override
    public void confirmYW800(String bizId, String 작업자ID, String 진행상태코드, YW800ConfirmRequest request, HttpServletRequest httpRequest) throws Exception {
        YE000VO ye000 = new YE000VO();
        ye000.set계약ID(request.계약ID);
        ye000.set사용자ID(request.사용자ID);
        ye000.set진행상태코드(진행상태코드);

        if (request.소득세적용률 != null && request.연말정산분납여부 != null) {
            ye000.set소득세적용률(request.소득세적용률);
            ye000.set연말정산분납여부(request.연말정산분납여부);
        }

        ye000DAO.updYE000(ye000);

        YE901VO ye901 = new YE901VO();
        ye901.set계약ID(request.계약ID);
        ye901.set사용자ID(request.사용자ID);
        ye901.set작업자ID(작업자ID);
        if ("2".equals(진행상태코드)) {
            ye901.set진행현황코드("3");
        } else {
            ye901.set진행현황코드("4");
        }
        ye901DAO.insYE901(ye901);

        //ye750Service.createYE750Pdf(bizId, request.계약ID, request.사용자ID, httpRequest);
    }


    private void get회사자료(String 계약ID, String 사용자ID, YW800Response response) throws Exception {
        YE000VO ye000 = new YE000VO();
        ye000.set계약ID(계약ID);
        ye000.set사용자ID(사용자ID);
        List<YE000VO> listSum = ye003DAO.getYE003SumList(ye000);

        for (YE000VO vo : listSum) {
            if ("1".equals(vo.get근무지구분())) {
                response.연금보험료공제_국민연금_주근무지 += StringUtil.strPaserInt(vo.get국민연금보험료());
                response.연금보험료공제_공무원연금_주근무지 += StringUtil.strPaserInt(vo.get공무원연금());
                response.연금보험료공제_군인연금_주근무지 += StringUtil.strPaserInt(vo.get군인연금());
                response.연금보험료공제_사립학교교직원연금_주근무지 += StringUtil.strPaserInt(vo.get사립학교교직원연금());
                response.연금보험료공제_별정우체국연금_주근무지 += StringUtil.strPaserInt(vo.get별정우체국연금());

                response.특별소득공제_보험료_건강보험료_주근무지 += StringUtil.strPaserInt(vo.get건강보험료());
                response.특별소득공제_보험료_장기요양보험료_주근무지 += StringUtil.strPaserInt(vo.get장기요양보험료());
                response.특별소득공제_보험료_고용보험료_주근무지 += StringUtil.strPaserInt(vo.get고용보험료());

                response.기납부세액_소득세 += StringUtil.strPaserInt(vo.get소득세());
                response.기납부세액_지방소득세 += StringUtil.strPaserInt(vo.get지방소득세());
                response.기납부세액_농어촌특별세 += StringUtil.strPaserInt(vo.get농어촌특별세());
                response.납부특례세액_소득세 += StringUtil.strPaserInt(vo.get납부특례세액_소득세());
                response.납부특례세액_지방소득세 += StringUtil.strPaserInt(vo.get납부특례세액_지방소득세());
                response.납부특례세액_농어촌특별세 += StringUtil.strPaserInt(vo.get납부특례세액_농어촌특별세());
            } else {
                response.연금보험료공제_국민연금_종근무지 += StringUtil.strPaserInt(vo.get국민연금보험료());
                response.연금보험료공제_공무원연금_종근무지 += StringUtil.strPaserInt(vo.get공무원연금());
                response.연금보험료공제_군인연금_종근무지 += StringUtil.strPaserInt(vo.get군인연금());
                response.연금보험료공제_사립학교교직원연금_종근무지 += StringUtil.strPaserInt(vo.get사립학교교직원연금());
                response.연금보험료공제_별정우체국연금_종근무지 += StringUtil.strPaserInt(vo.get별정우체국연금());

                response.특별소득공제_보험료_건강보험료_종근무지 += StringUtil.strPaserInt(vo.get건강보험료());
                response.특별소득공제_보험료_장기요양보험료_종근무지 += StringUtil.strPaserInt(vo.get장기요양보험료());
                response.특별소득공제_보험료_고용보험료_종근무지 += StringUtil.strPaserInt(vo.get고용보험료());

                response.기납부세액_전근무지_소득세 += StringUtil.strPaserInt(vo.get소득세());
                response.기납부세액_전근무지_지방소득세 += StringUtil.strPaserInt(vo.get지방소득세());
                response.기납부세액_전근무지_농어촌특별세 += StringUtil.strPaserInt(vo.get농어촌특별세());
            }
        }
    }

    private void get인적공제(String 계약ID, String 사용자ID, YW800Response response) {
        YE001VO ye001 = new YE001VO();
        ye001.set계약ID(계약ID);
        ye001.set사용자ID(사용자ID);
        ye001.setStartPage(0);
        ye001.setEndPage(9999);
        List<YE001VO> listYE000 = ye001DAO.getYE001List(ye001);

        for (YE001VO vo : listYE000) {
            if ("1".equals(vo.get기본공제())) {
                if ("0".equals(vo.get가족관계())) {
                    response.인적공제_기본공제_본인 = 1;
                } else if ("3".equals(vo.get가족관계())) {
                    response.인적공제_기본공제_배우자 = 1;
                } else {
                    response.인적공제_기본공제_부양가족++;
                }
                
                if (StringUtil.isNotNull(vo.get나이()) && StringUtil.strPaserInt(vo.get나이()) >= 70) {
                    response.인적공제_추가공제_경로우대++;
                }
                if ("1".equals(vo.get장애인()) || "2".equals(vo.get장애인()) || "3".equals(vo.get장애인())) {
                    response.인적공제_추가공제_장애인++;
                }
                if ("1".equals(vo.get부녀자())) {
                    response.인적공제_추가공제_부녀자 = 1;
                }
                if ("1".equals(vo.get한부모())) {
                    response.인적공제_추가공제_한부모 = 1;
                }

                // 직계비속(자녀+입양자) 또는 위탁아동(만18세미만) 이면서 자녀공제인경우만 자녀공제가능
                if (("4".equals(vo.get가족관계()) || "8".equals(vo.get가족관계())) &&  "1".equals(vo.get자녀()) ) {
                    response.세액공제_근로소득세액공제_자녀세액공제++;
                }

                if ("1".equals(vo.get출산입양())) {
                    response.세액공제_근로소득세액공제_출산입양_첫째 = 1;
                }
                if ("2".equals(vo.get출산입양())) {
                    response.세액공제_근로소득세액공제_출산입양_둘째 = 1;
                }
                if ("3".equals(vo.get출산입양())) {
                    response.세액공제_근로소득세액공제_출산입양_셋째++;
                }
            }
        }
    }

    private void get연금보험료공제(String 계약ID, String 사용자ID, YW800Response response) {
        YE051VO ye051 = new YE051VO();
        ye051.set계약ID(계약ID);
        ye051.set사용자ID(사용자ID);
        List<YE051VO> listYE051 = ye051DAO.getYE051List(ye051);

        if (listYE051.size() > 0) {
            for (YE051VO vo : listYE051) {
                if ("1".equals(vo.get보험료구분())) {
                    response.연금보험료공제_국민연금_추가납입 += vo.get추가납입금액();
                } else if ("2".equals(vo.get보험료구분())) {
                    response.연금보험료공제_공무원연금_추가납입 += vo.get추가납입금액();
                } else if ("3".equals(vo.get보험료구분())) {
                    response.연금보험료공제_군인연금_추가납입 += vo.get추가납입금액();
                } else if ("4".equals(vo.get보험료구분())) {
                    response.연금보험료공제_사립학교교직원연금_추가납입 += vo.get추가납입금액();
                } else if ("5".equals(vo.get보험료구분())) {
                    response.연금보험료공제_별정우체국연금_추가납입 += vo.get추가납입금액();
                }
            }
        }
    }

    private void get특별소득공제_보험료(String 계약ID, String 사용자ID, YW800Response response) {
        YE052VO ye052 = new YE052VO();
        ye052.set계약ID(계약ID);
        ye052.set사용자ID(사용자ID);
        List<YE052VO> listYE052 = ye052DAO.getYE052List(ye052);

        if (listYE052.size() > 0) {
            for (YE052VO vo : listYE052) {
                if ("1".equals(vo.get보험료구분())) {
                    response.특별소득공제_보험료_건강보험료_추가납입 += vo.get추가납입금액();
                } else if ("2".equals(vo.get보험료구분())) {
                    response.특별소득공제_보험료_장기요양보험료_추가납입 += vo.get추가납입금액();
                } else if ("3".equals(vo.get보험료구분())) {
                    response.특별소득공제_보험료_고용보험료_추가납입 += vo.get추가납입금액();
                }
            }
        }
    }

    private void get특별소득공제_주택임차차입금원리금상환액(String 계약ID, String 사용자ID, YW800Response response) {
        YE101VO ye101 = new YE101VO();
        ye101.set계약ID(계약ID);
        ye101.set사용자ID(사용자ID);
        List<YE101VO> listYE101 = ye101DAO.getYE101List(ye101);

        if (listYE101.size() > 0) {
            for (YE101VO vo : listYE101) {
                if ("1".equals(vo.get차입구분())) {
                    response.특별소득공제_주택임차차입금_대출기관_국세청자료 += vo.get국세청자료();
                    response.특별소득공제_주택임차차입금_대출기관_차감금액 += vo.get차감금액();
                    response.특별소득공제_주택임차차입금_대출기관_기타자료 += vo.get기타자료();
                } else if ("2".equals(vo.get차입구분())) {
                    response.특별소득공제_주택임차차입금_거주자_국세청자료 += vo.get국세청자료();
                    response.특별소득공제_주택임차차입금_거주자_차감금액 += vo.get차감금액();
                    response.특별소득공제_주택임차차입금_거주자_기타자료 += vo.get기타자료();
                }
            }
        }
    }

    private void get특별소득공제_장기주택저당차입금이자상환액(String 계약ID, String 사용자ID, YW800Response response) {
        YE104VO ye104 = new YE104VO();
        ye104.set계약ID(계약ID);
        ye104.set사용자ID(사용자ID);
        List<YE104VO> listYE104 = ye104DAO.getYE104List(ye104);

        if (listYE104.size() > 0) {
            for (YE104VO vo : listYE104) {
                switch (vo.get구분코드()) {
                    case "1":
                        response.특별소득공제_장기주택저당차입금이자상환액_2011년이전_15년미만_국세청자료 += vo.get국세청자료();
                        response.특별소득공제_장기주택저당차입금이자상환액_2011년이전_15년미만_차감금액 += vo.get차감금액();
                        response.특별소득공제_장기주택저당차입금이자상환액_2011년이전_15년미만_기타자료 += vo.get기타자료();
                        break;
                    case "2":
                        response.특별소득공제_장기주택저당차입금이자상환액_2011년이전_15년29년_국세청자료 += vo.get국세청자료();
                        response.특별소득공제_장기주택저당차입금이자상환액_2011년이전_15년29년_차감금액 += vo.get차감금액();
                        response.특별소득공제_장기주택저당차입금이자상환액_2011년이전_15년29년_기타자료 += vo.get기타자료();
                        break;
                    case "3":
                        response.특별소득공제_장기주택저당차입금이자상환액_2011년이전_30년이상_국세청자료 += vo.get국세청자료();
                        response.특별소득공제_장기주택저당차입금이자상환액_2011년이전_30년이상_차감금액 += vo.get차감금액();
                        response.특별소득공제_장기주택저당차입금이자상환액_2011년이전_30년이상_기타자료 += vo.get기타자료();
                        break;
                    case "4":
                        response.특별소득공제_장기주택저당차입금이자상환액_2012년이후_15년이상고정OR비거치_국세청자료 += vo.get국세청자료();
                        response.특별소득공제_장기주택저당차입금이자상환액_2012년이후_15년이상고정OR비거치_차감금액 += vo.get차감금액();
                        response.특별소득공제_장기주택저당차입금이자상환액_2012년이후_15년이상고정OR비거치_기타자료 += vo.get기타자료();
                        break;
                    case "5":
                        response.특별소득공제_장기주택저당차입금이자상환액_2012년이후_15년이상기타_국세청자료 += vo.get국세청자료();
                        response.특별소득공제_장기주택저당차입금이자상환액_2012년이후_15년이상기타_차감금액 += vo.get차감금액();
                        response.특별소득공제_장기주택저당차입금이자상환액_2012년이후_15년이상기타_기타자료 += vo.get기타자료();
                        break;
                    case "6":
                        response.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상고정AND비거치_국세청자료 += vo.get국세청자료();
                        response.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상고정AND비거치_차감금액 += vo.get차감금액();
                        response.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상고정AND비거치_기타자료 += vo.get기타자료();
                        break;
                    case "7":
                        response.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상고정OR비거치_국세청자료 += vo.get국세청자료();
                        response.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상고정OR비거치_차감금액 += vo.get차감금액();
                        response.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상고정OR비거치_기타자료 += vo.get기타자료();
                        break;
                    case "8":
                        response.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상기타_국세청자료 += vo.get국세청자료();
                        response.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상기타_차감금액 += vo.get차감금액();
                        response.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상기타_기타자료 += vo.get기타자료();
                        break;
                    case "9":
                        response.특별소득공제_장기주택저당차입금이자상환액_2015년이후_30년이상고정OR비거치_국세청자료 += vo.get국세청자료();
                        response.특별소득공제_장기주택저당차입금이자상환액_2015년이후_30년이상고정OR비거치_차감금액 += vo.get차감금액();
                        response.특별소득공제_장기주택저당차입금이자상환액_2015년이후_30년이상고정OR비거치_기타자료 += vo.get기타자료();
                        break;
                }
            }
        }
    }

    private void get개인연금저축(String 계약ID, String 사용자ID, YW800Response response) {
        YE106VO ye106 = new YE106VO();
        ye106.set계약ID(계약ID);
        ye106.set사용자ID(사용자ID);
        List<YE106VO> listYE106 = ye106DAO.getYE106List(ye106);

        if (listYE106.size() > 0) {
            for (YE106VO vo : listYE106) {
                response.그밖의소득공제_개인연금저축_납입금액 += vo.get납입금액();
                response.그밖의소득공제_개인연금저축_차감금액 += vo.get차감금액();
            }
        }
    }

    private void get주택마련저축(String 계약ID, String 사용자ID, YW800Response response) {
        YE107VO ye107 = new YE107VO();
        ye107.set계약ID(계약ID);
        ye107.set사용자ID(사용자ID);
        List<YE107VO> listYE107 = ye107DAO.getYE107List(ye107);

        if (listYE107.size() > 0) {
            for (YE107VO vo : listYE107) {
                switch (vo.get주택마련저축구분()) {
                    case "31":
                        response.그밖의소득공제_주택마련저축_청약저축_가입일자 = vo.get가입일자();
                        response.그밖의소득공제_주택마련저축_청약저축_납입금액 += vo.get납입금액();
                        response.그밖의소득공제_주택마련저축_청약저축_차감금액 += vo.get차감금액();
                        break;
                    case "32":
                        if (StringUtils.isNotEmpty(vo.get가입일자()) && StringUtil.strPaserInt(vo.get가입일자().substring(0, 4)) < 2015) {
                            response.그밖의소득공제_주택마련저축_주택청약종합저축2014년이전_가입일자 = vo.get가입일자();
                            response.그밖의소득공제_주택마련저축_주택청약종합저축2014년이전_납입금액 += vo.get납입금액();
                            response.그밖의소득공제_주택마련저축_주택청약종합저축2014년이전_차감금액 += vo.get차감금액();
                        } else {
                            response.그밖의소득공제_주택마련저축_주택청약종합저축2015년이후_가입일자 = vo.get가입일자();
                            response.그밖의소득공제_주택마련저축_주택청약종합저축2015년이후_납입금액 += vo.get납입금액();
                            response.그밖의소득공제_주택마련저축_주택청약종합저축2015년이후_차감금액 += vo.get차감금액();
                        }

                        break;
                    case "34":
                        response.그밖의소득공제_주택마련저축_근로자주택마련저축_가입일자 = vo.get가입일자();
                        response.그밖의소득공제_주택마련저축_근로자주택마련저축_납입금액 += vo.get납입금액();
                        response.그밖의소득공제_주택마련저축_근로자주택마련저축_차감금액 += vo.get차감금액();
                        break;
                }
            }
        }
    }

    private void get신용카드(String 계약ID, String 사용자ID, YW800Response response) {
        YE108VO ye108 = new YE108VO();
        ye108.set계약ID(계약ID);
        ye108.set사용자ID(사용자ID);
        List<YE108VO> listYE108 = ye108DAO.getYE108List(ye108);

        if (listYE108 != null && listYE108.size() > 0) {
            for (YE108VO vo : listYE108) {
                response.그밖의소득공제_신용카드_신용카드 += vo.get신용카드();
                response.그밖의소득공제_신용카드_현금영수증 += vo.get현금영수증();
                response.그밖의소득공제_신용카드_직불카드 += vo.get직불_선불카드();
                response.그밖의소득공제_신용카드_전통시장 += vo.get전통시장();
                response.그밖의소득공제_신용카드_대중교통 += vo.get대중교통();
                response.그밖의소득공제_신용카드_도서공연 += vo.get도서공연();

                response.그밖의소득공제_신용카드_신용카드_차감금액 += vo.get신용카드_차감금액();
                response.그밖의소득공제_신용카드_현금영수증_차감금액 += vo.get현금영수증_차감금액();
                response.그밖의소득공제_신용카드_직불카드_차감금액 += vo.get직불_선불카드_차감금액();
                response.그밖의소득공제_신용카드_전통시장_차감금액 += vo.get전통시장_차감금액();
                response.그밖의소득공제_신용카드_대중교통_차감금액 += vo.get대중교통_차감금액();
                response.그밖의소득공제_신용카드_도서공연_차감금액 += vo.get도서공연_차감금액();
            }
        }
    }

    private void get장기집합투자증권저축(String 계약ID, String 사용자ID, YW800Response response) {
        YE109VO ye109 = new YE109VO();
        ye109.set계약ID(계약ID);
        ye109.set사용자ID(사용자ID);
        List<YE109VO> listYE109 = ye109DAO.getYE109List(ye109);

        if (listYE109.size() > 0) {
            for (YE109VO vo : listYE109) {
                response.그밖의소득공제_장기잡합투자_납입금액 += vo.get납입금액();
                response.그밖의소득공제_장기잡합투자_차감금액 += vo.get차감금액();
            }
        }
    }

    private void get그밖의소득공제_소기업소상공인공제부금(String 계약ID, String 사용자ID, YW800Response response) {
        YE201VO ye201 = new YE201VO();
        ye201.set계약ID(계약ID);
        ye201.set사용자ID(사용자ID);
        List<YE201VO> listYE201 = ye201DAO.getYE201List(ye201);

        if (listYE201.size() > 0) {
            for (YE201VO vo : listYE201) {
                response.그밖의소득공제_소기업소상공인공제부금_국세청자료_납입금액 += vo.get국세청_납입금액();
                response.그밖의소득공제_소기업소상공인공제부금_국세청자료_차감금액 += vo.get국세청_차감금액();
                response.그밖의소득공제_소기업소상공인공제부금_기타자료_납입금액 += vo.get기타_납입금액();
                response.그밖의소득공제_소기업소상공인공제부금_기타자료_차감금액 += 0;  // 기타 차감금액은 없음
            }
        }
    }

    private void get그밖의소득공제_투자조합출자등(String 계약ID, String 사용자ID, YW800Response response) {
        YE202VO ye202 = new YE202VO();
        ye202.set계약ID(계약ID);
        ye202.set사용자ID(사용자ID);
        List<YE202VO> listYE202 = ye202DAO.getYE202List(ye202);

        if (listYE202.size() > 0) {
            int 계약년도 = Integer.parseInt(response.근무년월.계약년도);

            int 년도;
            int diff;
            for (YE202VO vo : listYE202) {
                년도 = StringUtil.strPaserInt(vo.get년도());
                diff = 계약년도 - 년도;

                if (diff == 0) {
                    if ("1".equals(vo.get구분코드())) {
                        response.그밖의소득공제_투자조합출자_YEAR_조합 += vo.get납입금액();
                    } else if ("2".equals(vo.get구분코드())) {
                        response.그밖의소득공제_투자조합출자_YEAR_벤처 += vo.get납입금액();
                    }
                } else if (diff == 1) {
                    if ("1".equals(vo.get구분코드())) {
                        response.그밖의소득공제_투자조합출자_YEAR1_조합 += vo.get납입금액();
                    } else if ("2".equals(vo.get구분코드())) {
                        response.그밖의소득공제_투자조합출자_YEAR1_벤처 += vo.get납입금액();
                    }
                } else if (diff == 2) {
                    if ("1".equals(vo.get구분코드())) {
                        response.그밖의소득공제_투자조합출자_YEAR2_조합 += vo.get납입금액();
                    } else if ("2".equals(vo.get구분코드())) {
                        response.그밖의소득공제_투자조합출자_YEAR2_벤처 += vo.get납입금액();
                    }
                }
            }
        }
    }

    private void get그밖의소득공제_우리사주조합출연금(String 계약ID, String 사용자ID, YW800Response response) {
        YE203VO ye203 = new YE203VO();
        ye203.set계약ID(계약ID);
        ye203.set사용자ID(사용자ID);
        List<YE203VO> listYE203 = ye203DAO.getYE203List(ye203);

        if (listYE203.size() > 0) {
            for (YE203VO vo : listYE203) {
                response.그밖의소득공제_우리사주조합_납입금액 += vo.get납입금액();
            }
        }
    }

    private void get그밖의소득공제_우리사주조합출연금벤처(String 계약ID, String 사용자ID, YW800Response response) {
        YE203VO ye203 = new YE203VO();
        ye203.set계약ID(계약ID);
        ye203.set사용자ID(사용자ID);
        List<YE203VO> listYE203 = ye203DAO.getYE203_1List(ye203);

        if (listYE203.size() > 0) {
            for (YE203VO vo : listYE203) {
                response.그밖의소득공제_우리사주조합_벤처_납입금액 += vo.get납입금액();
            }
        }
    }

    private void get그밖의소득공제_고용유지중소기업근로자(String 계약ID, String 사용자ID, YW800Response response) {
        YE204VO ye204 = new YE204VO();
        ye204.set계약ID(계약ID);
        ye204.set사용자ID(사용자ID);
        List<YE204VO> listYE204 = ye204DAO.getYE204List(ye204);

        if (listYE204.size() > 0) {
            for (YE204VO vo : listYE204) {
                response.그밖의소득공제_고용유지중소기업_납입금액 += vo.get납입금액();
            }
        }
    }

    private void get세액감면(String bizId, String 계약ID, String 사용자ID, YW800Response response) {
        YE002VO ye002 = new YE002VO();
        ye002.setBizId(bizId);
        ye002.set계약ID(계약ID);
        ye002.set사용자ID(사용자ID);
        List<YE002VO> listYE002 = ye002DAO.getYE002TaxList(ye002);

        if (listYE002 != null) {
            for (YE002VO vo : listYE002) {
                response.세액공제_세액감면_외국인기술자 += StringUtil.strPaserInt(vo.getT01());
                response.세액공제_세액감면_중소기업100 += StringUtil.strPaserInt(vo.getT10());
                response.세액공제_세액감면_중소기업50 += StringUtil.strPaserInt(vo.getT11());
                response.세액공제_세액감면_중소기업70 += StringUtil.strPaserInt(vo.getT12());
                response.세액공제_세액감면_중소기업90 += StringUtil.strPaserInt(vo.getT13());
                response.세액공제_세액감면_조세조약 += StringUtil.strPaserInt(vo.getT20());
            }
        }

        YE406VO ye406 = new YE406VO();
        ye406.set계약ID(계약ID);
        ye406.set사용자ID(사용자ID);
        List<YE406VO> listYE406 = ye406DAO.getYE406List(ye406);

        if (listYE406.size() > 0) {
            for (YE406VO vo : listYE406) {
                response.세액공제_세액감면_소득세법 += vo.get감면대상급여();
            }
        }
    }

    private void get연금계좌_퇴직연금(String 계약ID, String 사용자ID, YW800Response response) {
        YE301VO ye301 = new YE301VO();
        ye301.set계약ID(계약ID);
        ye301.set사용자ID(사용자ID);
        List<YE301VO> listYE301 = ye301DAO.getYE301List(ye301);

        if (listYE301.size() > 0) {
            int 금액;
            for (YE301VO vo : listYE301) {
                금액 = vo.get납입금액() - vo.get차감금액();

                if ("11".equals(vo.get퇴직연금구분코드())) {
                    response.세액공제_연금계좌세액공제_퇴직연금공제 += 금액;
                } else if ("12".equals(vo.get퇴직연금구분코드())) {
                    response.세액공제_연금계좌세액공제_과학기술인공제 += 금액;
                }
            }
        }
    }

    private void get연금계좌_연금저축(String 계약ID, String 사용자ID, YW800Response response) {
        YE302VO ye302 = new YE302VO();
        ye302.set계약ID(계약ID);
        ye302.set사용자ID(사용자ID);
        List<YE302VO> listYE302 = ye302DAO.getYE302List(ye302);

        if (listYE302.size() > 0) {
            int 금액;
            for (YE302VO vo : listYE302) {
                금액 = vo.get납입금액() - vo.get차감금액();

                response.세액공제_연금계좌세액공제_연금저축공제 += 금액;
            }
        }
    }

    private void get특별세액공제_보험료(String 계약ID, String 사용자ID, YW800Response response) {
        YE401VO ye401 = new YE401VO();
        ye401.set계약ID(계약ID);
        ye401.set사용자ID(사용자ID);
        List<YE401VO> listYE401 = ye401DAO.getYE401List(ye401);

        if (listYE401.size() > 0) {
            int 금액;
            for (YE401VO vo : listYE401) {
                금액 = vo.get납입금액() - vo.get차감금액();

                if ("1".equals(vo.get보험구분코드())) {
                    response.세액공제_특별세액공제_보험료_보장성보험료 += 금액;
                } else if ("2".equals(vo.get보험구분코드())) {
                    response.세액공제_특별세액공제_보험료_장애인전용보장성보험료 += 금액;
                }
            }
        }
    }

    private void get특별세액공제_의료비안경구입비(String 계약ID, String 사용자ID, YW800Response response) {
        YE402VO ye402 = new YE402VO();
        ye402.set계약ID(계약ID);
        ye402.set사용자ID(사용자ID);
        List<YE402VO> listYE402 = ye402DAO.getYE402List(ye402);

        // 부양가족별 안경구입비 50만원 한도
        // 안경구입비 공제금액은 본인 또는 일반(본인을 제외한 금액) 반영 개발예정
        if (listYE402.size() > 0) {
            int 금액;
            for (YE402VO vo : listYE402) {
                금액 = vo.get지출액() - vo.get차감금액();

                if ("3".equals(vo.get의료비유형())) {
                    response.세액공제_특별세액공제_의료비_난임시술비 += 금액;
                } else {
                    if ("1".equals(vo.get공제종류코드())) {
                        response.세액공제_특별세액공제_의료비_본인장애인 += 금액;
                    } else if ("2".equals(vo.get공제종류코드())) {
                        response.세액공제_특별세액공제_의료비_일반 += 금액;
                    }
                }
            }
        }
    }

    private void get특별세액공제_교육비교복구입비체험학습비(String 계약ID, String 사용자ID, YW800Response response) {
        YE403VO ye403 = new YE403VO();
        ye403.set계약ID(계약ID);
        ye403.set사용자ID(사용자ID);
        List<YE403VO> listYE403 = ye403DAO.getYE403List(ye403);

        Map<String, Integer> 취학전 = new HashMap<>();
        Map<String, Integer> 초중고 = new HashMap<>();
        Map<String, Integer> 대학생 = new HashMap<>();
        if (listYE403.size() > 0) {
            int 교육비;
            for (YE403VO vo : listYE403) {
                교육비 = vo.get공납금() - vo.get공납금_차감금액() + vo.get교복구입비() - vo.get교복구입비_차감금액() + vo.get체험학습비() - vo.get체험학습비_차감금액();

                if ("1".equals(vo.get공제종류코드())) {
                    response.세액공제_특별세액공제_교육비_본인 += 교육비;
                } else if ("2".equals(vo.get공제종류코드())) {
                    if (취학전.containsKey(vo.get부양가족ID())) {
                        취학전.put(vo.get부양가족ID(), 취학전.get(vo.get부양가족ID()) + 교육비);
                    } else {
                        취학전.put(vo.get부양가족ID(), 교육비);
                    }
                } else if ("3".equals(vo.get공제종류코드())) {
                    if (초중고.containsKey(vo.get부양가족ID())) {
                        초중고.put(vo.get부양가족ID(), 초중고.get(vo.get부양가족ID()) + 교육비);
                    } else {
                        초중고.put(vo.get부양가족ID(), 교육비);
                    }
                } else if ("4".equals(vo.get공제종류코드())) {
                    if (대학생.containsKey(vo.get부양가족ID())) {
                        대학생.put(vo.get부양가족ID(), 대학생.get(vo.get부양가족ID()) + 교육비);
                    } else {
                        대학생.put(vo.get부양가족ID(), 교육비);
                    }
                } else if ("5".equals(vo.get공제종류코드())) {
                    response.세액공제_특별세액공제_교육비_장애인 += 교육비;
                }
            }

            response.세액공제_특별세액공제_교육비_취학전 = new int[취학전.size()];
            int idx = 0;
            for (int value : 취학전.values()) {
                response.세액공제_특별세액공제_교육비_취학전[idx] = value;
                idx++;
            }

            response.세액공제_특별세액공제_교육비_초중고 = new int[초중고.size()];
            idx = 0;
            for (int value : 초중고.values()) {
                response.세액공제_특별세액공제_교육비_초중고[idx] = value;
                idx++;
            }

            response.세액공제_특별세액공제_교육비_대학생 = new int[대학생.size()];
            idx = 0;
            for (int value : 대학생.values()) {
                response.세액공제_특별세액공제_교육비_대학생[idx] = value;
                idx++;
            }
        }
    }

    private void get특별세액공제_기부금(String 계약ID, String 사용자ID, YW800Response response) {
        YE404VO ye404 = new YE404VO();
        ye404.set계약ID(계약ID);
        ye404.set사용자ID(사용자ID);
        ye404.setStartPage(0);
        ye404.setEndPage(9999);
        List<YE404VO> listYE404 = ye404DAO.getYE404List(ye404);

        if (listYE404.size() > 0) {
            for (YE404VO vo : listYE404) {
            	
            	if(StringUtils.isNotEmpty(vo.get기부코드())){
            	
	                switch (vo.get기부코드()) {
	                    case "10":
	                        response.세액공제_특별세액공제_기부금_YEAR_법정기부금 += vo.get기부명세_공제대상기부금();
	                        break;
	                    case "20":
	                        response.세액공제_특별세액공제_기부금_YEAR_정치자금기부금 += vo.get기부명세_공제대상기부금();
	                        break;
	                    case "40":
	                        response.세액공제_특별세액공제_기부금_YEAR_종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
	                        break;
	                    case "41":
	                        response.세액공제_특별세액공제_기부금_YEAR_종교단체지정기부금 += vo.get기부명세_공제대상기부금();
	                        break;
	                    case "42":
	                        response.세액공제_특별세액공제_기부금_YEAR_우리사주조합기부금 += vo.get기부명세_공제대상기부금();
	                        break;
	                }
	                response.세액공제_특별세액공제_기부금_YEAR_기부장려금신청액 += vo.get기부명세_기부장려금();
	                response.세액공제_특별세액공제_기부금_YEAR_기타 += vo.get기부명세_기타();
	                
            	}else{
            		logger.error("##### 기부코드 정보가 없습니다. #####");
            		logger.error("# YE404VO => " + vo );            		
            	}
            }
        }
    }

    private void get특별소득공제_기부금이월분(String 계약ID, String 사용자ID, YW800Response response) {
        YE405VO ye405 = new YE405VO();
        ye405.set계약ID(계약ID);
        ye405.set사용자ID(사용자ID);
        ye405.setStartPage(0);
        ye405.setEndPage(9999);
        List<YE405VO> listYE405 = ye405DAO.getYE405List(ye405);

        if (listYE405.size() > 0) {
            int 계약년도 = Integer.parseInt(response.근무년월.계약년도);

            int 기부년도;
            int diff;
            for (YE405VO vo : listYE405) {
                기부년도 = StringUtil.strPaserInt(vo.get기부년도());
                diff = 계약년도 - 기부년도;

                if (기부년도 <= 2013 && diff <= 5) {
                    if ("40".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_2013종교단체외지정기부금 += vo.get공제대상기부금();
                    } else if ("41".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_2013종교단체지정기부금 += vo.get공제대상기부금();
                    }
                } else if (diff == 1) {
                    if ("10".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_YEAR1_법정기부금 += vo.get공제대상기부금();
                    } else if ("40".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_YEAR1_종교단체외지정기부금 += vo.get공제대상기부금();
                    } else if ("41".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_YEAR1_종교단체지정기부금 += vo.get공제대상기부금();
                    }
                } else if (diff == 2) {
                    if ("10".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_YEAR2_법정기부금 += vo.get공제대상기부금();
                    } else if ("40".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_YEAR2_종교단체외지정기부금 += vo.get공제대상기부금();
                    } else if ("41".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_YEAR2_종교단체지정기부금 += vo.get공제대상기부금();
                    }
                } else if (diff == 3) {
                    if ("10".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_YEAR3_법정기부금 += vo.get공제대상기부금();
                    } else if ("40".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_YEAR3_종교단체외지정기부금 += vo.get공제대상기부금();
                    } else if ("41".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_YEAR3_종교단체지정기부금 += vo.get공제대상기부금();
                    }
                } else if (diff == 4) {
                	 if ("10".equals(vo.get기부금종류코드())) {
                         response.세액공제_특별세액공제_기부금_이월분_YEAR4_법정기부금 += vo.get공제대상기부금();
                     } else if ("40".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_YEAR4_종교단체외지정기부금 += vo.get공제대상기부금();
                    } else if ("41".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_YEAR4_종교단체지정기부금 += vo.get공제대상기부금();
                    }
                } else if (diff == 5) {
                    if ("40".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_YEAR5_종교단체외지정기부금 += vo.get공제대상기부금();
                    } else if ("41".equals(vo.get기부금종류코드())) {
                        response.세액공제_특별세액공제_기부금_이월분_YEAR5_종교단체지정기부금 += vo.get공제대상기부금();
                    }
                }
            }
        }
    }

    private void get납세조합공제(String 계약ID, String 사용자ID, YW800Response response) {
        YE501VO ye501 = new YE501VO();
        ye501.set계약ID(계약ID);
        ye501.set사용자ID(사용자ID);
        List<YE501VO> listYE501 = ye501DAO.getYE501List(ye501);

        if (listYE501.size() > 0) {
            for (YE501VO vo : listYE501) {
                response.세액공제_기타세액공제_납세조합공제 += vo.get금액();
            }
        }
    }

    private void get주택차입금(String 계약ID, String 사용자ID, YW800Response response) {
        YE502VO ye502 = new YE502VO();
        ye502.set계약ID(계약ID);
        ye502.set사용자ID(사용자ID);
        List<YE502VO> listYE502 = ye502DAO.getYE502List(ye502);

        if (listYE502.size() > 0) {
            for (YE502VO vo : listYE502) {
                response.세액공제_기타세액공제_주택차입금이자세액공제 += vo.get금액();
            }
        }
    }

    private void get외국납부세액(String 계약ID, String 사용자ID, YW800Response response) {
        YE503VO ye503 = new YE503VO();
        ye503.set계약ID(계약ID);
        ye503.set사용자ID(사용자ID);
        List<YE503VO> listYE503 = ye503DAO.getYE503List(ye503);

        if (listYE503.size() > 0) {
            for (YE503VO vo : listYE503) {
                response.세액공제_기타세액공제_외국납부세액공제 += vo.get세액공제금액();
            }
        }
    }

    private void get월세액(String 계약ID, String 사용자ID, YW800Response response) {
        YE105VO ye105 = new YE105VO();
        ye105.set계약ID(계약ID);
        ye105.set사용자ID(사용자ID);
        List<YE105VO> listYE105 = ye105DAO.getYE105List(ye105);

        if (listYE105.size() > 0) {
            for (YE105VO vo : listYE105) {
                response.세액공제_기타세액공제_월세액공제_연간월세액 += vo.get연간_월세액();
                response.세액공제_기타세액공제_월세액공제_공제대상금액 += vo.get공제대상금액();
            }
        }
    }

    // 연말정산요약 선택 및 일괄 저장
	@Override
	public void saveAllYW800(String bizId, String 작업자ID, List<YW800Request> list) throws Exception{
		
		for (YW800Request yw800Request : list) {
			
			// 근로자 개별 연말정산 요약 계산하기 실행
			saveYW800(bizId, 작업자ID, yw800Request);
		}
		
	}
	
	// 연말정산요약 마감 최종 확정
	@Override
	public void confirmAllYW800(String 작업자ID, List<YW800VO> list) throws Exception{
		
		for (YW800VO yw800VO : list) {
			
			YE000VO ye000VO = new YE000VO();
			ye000VO.set계약ID(yw800VO.get계약ID());
			ye000VO.set사용자ID(yw800VO.get사용자ID());
			ye000VO.set진행상태코드(yw800VO.get진행상태코드()); // 근로자 진행상태코드 최종 마감 확정 : 5

	        ye000DAO.updYE000(ye000VO);

	        // 근로자별 진행현황 log
	        YE901VO ye901VO = new YE901VO();
	        ye901VO.set계약ID(yw800VO.get계약ID());
	        ye901VO.set사용자ID(yw800VO.get사용자ID());
	        ye901VO.set작업자ID(작업자ID);
	        ye901VO.set진행현황코드(yw800VO.get진행상태코드()); // 근로자 진행상태코드 최종 마감 확정  : 5
	        
	        ye901DAO.insYE901(ye901VO);
		}
	}
}
