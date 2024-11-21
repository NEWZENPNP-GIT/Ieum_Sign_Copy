package com.ezsign.window.service.impl;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.feb.house.dao.YE109DAO;
import com.ezsign.feb.house.vo.YE109VO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE900DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.window.service.YW660Service;
import com.ezsign.window.vo.Code;
import com.ezsign.window.vo.YW660Request;
import com.ezsign.window.vo.YW660Response;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@Service("yw660Service")
public class YW660ServiceImpl extends AbstractServiceImpl implements YW660Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "codeDAO")
    private CodeDAO codeDAO;

    @Resource(name = "ye109DAO")
    private YE109DAO ye109DAO;

    @Resource(name = "ye900DAO")
    private YE900DAO ye900DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;

    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;
    
    @Override
    public void getYW660(String 계약ID, String 사용자ID, YW660Response response) {
        CodeVO codeVO = new CodeVO();
        codeVO.setGrpCommCode("432");
        response.자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 3); // 1: 국체청 3: 기타
        codeVO.setGrpCommCode("404");
        response.취급기관명 = Code.getCodeList(codeDAO.getCodeList(codeVO));

        YE109VO ye109 = new YE109VO();
        ye109.set계약ID(계약ID);
        ye109.set사용자ID(사용자ID);
        List<YE109VO> listYE109 = ye109DAO.getYE109List(ye109);

        if (listYE109.size() > 0) {
            response.장기집합투자증권저축 = listYE109;
        }

        YE900VO ye900Result;

        response.장기집합투자증권저축_정정사유.set계약ID(계약ID);
        response.장기집합투자증권저축_정정사유.set사용자ID(사용자ID);
        response.장기집합투자증권저축_정정사유.set사유구분("W650"); // 장기집합투자증권코드
        ye900Result = ye900DAO.getYE900(response.장기집합투자증권저축_정정사유);
        if (ye900Result != null) {
            response.장기집합투자증권저축_정정사유 = ye900Result;
        }
    }

    @Override
    public void saveYW660(String 작업자ID, YW660Request request, int userType) {
        String 계약ID = null;
        String 사용자ID = null;

        if (request.장기집합투자증권저축 != null) {
            for (YE109VO vo : request.장기집합투자증권저축) {
                계약ID = vo.get계약ID();
                사용자ID = vo.get사용자ID();

                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye109DAO.updYE109Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye109DAO.updYE109Disable(vo);
                    ye109DAO.insYE109(vo);
                }
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

        if (request.장기집합투자증권저축_정정사유 != null) {
            saveYE900(작업자ID, "W650", request.장기집합투자증권저축_정정사유); // 장기집합투자증권코드
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
