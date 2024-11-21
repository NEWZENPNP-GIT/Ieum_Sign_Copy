package com.ezsign.window.service.impl;

import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE900DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.special.dao.YE406DAO;
import com.ezsign.feb.special.vo.YE406VO;
import com.ezsign.feb.worker.dao.YE002DAO;
import com.ezsign.feb.worker.vo.YE002VO;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.window.service.YW720Service;
import com.ezsign.window.vo.YW720Request;
import com.ezsign.window.vo.YW720Response;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@Service("yw720Service")
public class YW720ServiceImpl extends AbstractServiceImpl implements YW720Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "ye002DAO")
    private YE002DAO ye002DAO;

    @Resource(name = "ye406DAO")
    private YE406DAO ye406DAO;

    @Resource(name = "ye900DAO")
    private YE900DAO ye900DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;

    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;
    
    @Override
    public void getYW720(String bizId, String 계약ID, String 사용자ID, YW720Response response) {
        YE406VO ye406 = new YE406VO();
        ye406.set계약ID(계약ID);
        ye406.set사용자ID(사용자ID);
        YE406VO ye406Result = ye406DAO.getYE406(ye406);

        if (ye406Result != null) {
            response.세액감면 = ye406Result;
        }

        YE002VO ye002 = new YE002VO();
        ye002.setBizId(bizId);
        ye002.set계약ID(계약ID);
        ye002.set사용자ID(사용자ID);
        List<YE002VO> listYE002 = ye002DAO.getYE002TaxList(ye002);

        if (listYE002 != null) {
            int t01 = 0;
            int t10 = 0;
            int t11 = 0;
            int t12 = 0;
            int t13 = 0;
            int t20 = 0;
            for (YE002VO vo : listYE002) {
                if (!vo.getT01().equals("0")) {
                    if (response.세액감면.getT01_감면기간_FROM() == null || response.세액감면.getT01_감면기간_FROM().compareTo(vo.get감면시작일()) > 0) {
                        response.세액감면.setT01_감면기간_FROM(vo.get감면시작일());
                    }
                    if (response.세액감면.getT01_감면기간_TO() == null || response.세액감면.getT01_감면기간_TO().compareTo(vo.get감면종료일()) < 0) {
                        response.세액감면.setT01_감면기간_TO(vo.get감면종료일());
                    }
                    t01 += StringUtil.strPaserInt(vo.getT01());
                    response.세액감면.setT01_감면대상급여(t01);
                }
                if (!vo.getT10().equals("0")) {
                    if (response.세액감면.getT10_감면기간_FROM() == null || response.세액감면.getT10_감면기간_FROM().compareTo(vo.get감면시작일()) > 0) {
                        response.세액감면.setT10_감면기간_FROM(vo.get감면시작일());
                    }
                    if (response.세액감면.getT10_감면기간_TO() == null || response.세액감면.getT10_감면기간_TO().compareTo(vo.get감면종료일()) < 0) {
                        response.세액감면.setT10_감면기간_TO(vo.get감면종료일());
                    }
                    t10 += StringUtil.strPaserInt(vo.getT10());
                    response.세액감면.setT10_감면기간_감면대상급여(t10);
                }
                if (!vo.getT11().equals("0")) {
                    if (response.세액감면.getT11_감면기간_FROM() == null || response.세액감면.getT11_감면기간_FROM().compareTo(vo.get감면시작일()) > 0) {
                        response.세액감면.setT11_감면기간_FROM(vo.get감면시작일());
                    }
                    if (response.세액감면.getT11_감면기간_TO() == null || response.세액감면.getT11_감면기간_TO().compareTo(vo.get감면종료일()) < 0) {
                        response.세액감면.setT11_감면기간_TO(vo.get감면종료일());
                    }
                    t11 += StringUtil.strPaserInt(vo.getT11());
                    response.세액감면.setT11_감면기간_감면대상급여(t11);
                }
                if (!vo.getT12().equals("0")) {
                    if (response.세액감면.getT12_감면기간_FROM() == null || response.세액감면.getT12_감면기간_FROM().compareTo(vo.get감면시작일()) > 0) {
                        response.세액감면.setT12_감면기간_FROM(vo.get감면시작일());
                    }
                    if (response.세액감면.getT12_감면기간_TO() == null || response.세액감면.getT12_감면기간_TO().compareTo(vo.get감면종료일()) < 0) {
                        response.세액감면.setT12_감면기간_TO(vo.get감면종료일());
                    }
                    t12 += StringUtil.strPaserInt(vo.getT12());
                    response.세액감면.setT12_감면기간_감면대상급여(t12);
                }
                if (!vo.getT13().equals("0")) {
                    if (response.세액감면.getT13_감면기간_FROM() == null || response.세액감면.getT13_감면기간_FROM().compareTo(vo.get감면시작일()) > 0) {
                        response.세액감면.setT13_감면기간_FROM(vo.get감면시작일());
                    }
                    if (response.세액감면.getT13_감면기간_TO() == null || response.세액감면.getT13_감면기간_TO().compareTo(vo.get감면종료일()) < 0) {
                        response.세액감면.setT13_감면기간_TO(vo.get감면종료일());
                    }
                    t13 += StringUtil.strPaserInt(vo.getT13());
                    response.세액감면.setT13_감면기간_감면대상급여(t13);
                }
                if (!vo.getT20().equals("0")) {
                    if (response.세액감면.getT20_감면기간_FROM() == null || response.세액감면.getT20_감면기간_FROM().compareTo(vo.get감면시작일()) > 0) {
                        response.세액감면.setT20_감면기간_FROM(vo.get감면시작일());
                    }
                    if (response.세액감면.getT20_감면기간_TO() == null || response.세액감면.getT20_감면기간_TO().compareTo(vo.get감면종료일()) < 0) {
                        response.세액감면.setT20_감면기간_TO(vo.get감면종료일());
                    }
                    t20 += StringUtil.strPaserInt(vo.getT20());
                    response.세액감면.setT20_감면대상급여(t20);
                }
            }
        }

        YE900VO ye900Result;

        response.세액감면_정정사유.set계약ID(계약ID);
        response.세액감면_정정사유.set사용자ID(사용자ID);
        response.세액감면_정정사유.set사유구분("W720");
        ye900Result = ye900DAO.getYE900(response.세액감면_정정사유);
        if (ye900Result != null) {
            response.세액감면_정정사유 = ye900Result;
        }
    }

    @Override
    public void saveYW720(String 작업자ID, YW720Request request, int userType) {
        String 계약ID = null;
        String 사용자ID = null;

        if (request.세액감면 != null) {
            계약ID = request.세액감면.get계약ID();
            사용자ID = request.세액감면.get사용자ID();

            request.세액감면.set작업자ID(작업자ID);

            if ("D".equals(request.세액감면.getDbMode())) {
                ye406DAO.updYE406Disable(request.세액감면);
            } else if ("C".equals(request.세액감면.getDbMode()) || "U".equals(request.세액감면.getDbMode())) {
                ye406DAO.updYE406Disable(request.세액감면);
                ye406DAO.insYE406(request.세액감면);
            }
        }

        if (StringUtil.isNotNull(계약ID) && StringUtil.isNotNull(사용자ID)) {
        	
        	// 중간관리자 이상
        	if(userType >= 5) {
        		// 근로자 진행상태코드 수정
            	YE000VO ye000 = new YE000VO();
            	ye000.set계약ID(계약ID);
            	ye000.set사용자ID(사용자ID);
            	ye000.set진행상태코드("2");
            	ye000DAO.updYE000(ye000);
        	}
        	
            YE901VO ye901 = new YE901VO();
            ye901.set계약ID(계약ID);
            ye901.set사용자ID(사용자ID);
            ye901.set작업자ID(작업자ID);
            ye901.set진행현황코드("2");
            ye901DAO.insYE901(ye901);
        }

        if (request.세액감면_정정사유 != null) {
            saveYE900(작업자ID, "W720", request.세액감면_정정사유);
        }
    }

    private void saveYE900(String 작업자ID, String 사유구분, YE900VO vo) {
        vo.set작업자ID(작업자ID);
        vo.set사유구분(사유구분);

        if ("D".equals(vo.getDbMode())) {
            ye900DAO.updYE900Disable(vo);
        } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
            ye900DAO.updYE900Disable(vo);
            ye900DAO.insYE900(vo);
        }
    }
}
