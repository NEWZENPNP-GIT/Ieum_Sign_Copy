package com.ezsign.window.service.impl;

import com.ezsign.feb.house.dao.YE710DAO;
import com.ezsign.feb.house.vo.YE710VO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.window.service.YW611Service;
import com.ezsign.window.vo.YW611Request;
import com.ezsign.window.vo.YW611Response;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@SuppressWarnings("NonAsciiCharacters")
@Service("yw611Service")
public class YW611ServiceImpl extends AbstractServiceImpl implements YW611Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "ye710DAO")
    private YE710DAO ye710DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;

    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;
    
    @Override
    public void getYW611(String 계약ID, String 사용자ID, YW611Response response) {
        YE710VO vo = new YE710VO();
        vo.set계약ID(계약ID);
        vo.set사용자ID(사용자ID);
        response.주택관련소득공제신청확인서 = ye710DAO.getYE710(vo);
    }

    @Override
    public void saveYW611(String 작업자ID, YW611Request request, int userType) {
        if (request.주택관련소득공제신청확인서 != null) {
            if ("D".equals(request.주택관련소득공제신청확인서.getDbMode())) {
                ye710DAO.updYE710Disable(request.주택관련소득공제신청확인서);
            } else if ("C".equals(request.주택관련소득공제신청확인서.getDbMode()) || "U".equals(request.주택관련소득공제신청확인서.getDbMode())) {
                ye710DAO.updYE710Disable(request.주택관련소득공제신청확인서);
                ye710DAO.insYE710(request.주택관련소득공제신청확인서);
            }

            String 계약ID = request.주택관련소득공제신청확인서.get계약ID();
            String 사용자ID = request.주택관련소득공제신청확인서.get사용자ID();
            
            if (StringUtil.isNotNull(계약ID) && StringUtil.isNotNull(사용자ID)) {
            	/*
            	// 중간관리자 이상
            	if(userType >= 5) {
            		// 근로자 진행상태코드 수정
                	YE000VO ye000 = new YE000VO();
                	ye000.set계약ID(계약ID);
                	ye000.set사용자ID(사용자ID);
                	ye000.set진행상태코드("2");
                	ye000DAO.updYE000(ye000);
            	}
            	*/

            	YE901VO ye901 = new YE901VO();
            	ye901.set계약ID(계약ID);
            	ye901.set사용자ID(사용자ID);
            	ye901.set작업자ID(작업자ID);
            	ye901.set진행현황코드("0");
            	ye901DAO.insYE901(ye901);
            	
            }
        }
    }
}
