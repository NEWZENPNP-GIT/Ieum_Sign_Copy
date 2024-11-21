package com.ezsign.window.service.impl;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE900DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.pension.dao.YE301DAO;
import com.ezsign.feb.pension.dao.YE302DAO;
import com.ezsign.feb.pension.vo.YE301VO;
import com.ezsign.feb.pension.vo.YE302VO;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.window.service.YW700Service;
import com.ezsign.window.vo.Code;
import com.ezsign.window.vo.YW700Request;
import com.ezsign.window.vo.YW700Response;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@Service("yw700Service")
public class YW700ServiceImpl extends AbstractServiceImpl implements YW700Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "codeDAO")
    private CodeDAO codeDAO;

    @Resource(name = "ye301DAO")
    private YE301DAO ye301DAO;

    @Resource(name = "ye302DAO")
    private YE302DAO ye302DAO;

    @Resource(name = "ye900DAO")
    private YE900DAO ye900DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;

    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;
    
    @Override
    public void getYW700(String 계약ID, String 사용자ID, YW700Response response) {
        CodeVO codeVO = new CodeVO();
        codeVO.setGrpCommCode("432");
        response.자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 3); // 1: 국체청 3: 기타
        codeVO.setGrpCommCode("404");
        response.금융회사등명칭 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("418");
        response.퇴직연금구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 11, 19);
        codeVO.setGrpCommCode("418");
        response.연금저축구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 22, 22);

        YE301VO ye301 = new YE301VO();
        ye301.set계약ID(계약ID);
        ye301.set사용자ID(사용자ID);
        List<YE301VO> listYE301 = ye301DAO.getYE301List(ye301);

        YE302VO ye302 = new YE302VO();
        ye302.set계약ID(계약ID);
        ye302.set사용자ID(사용자ID);
        List<YE302VO> listYE302 = ye302DAO.getYE302List(ye302);

        if (listYE301.size() > 0) {
            response.퇴직연금계좌 = listYE301;
        }
        if (listYE302.size() > 0) {
            response.연금저축계좌 = listYE302;
        }

        YE900VO ye900Result;

        response.퇴직연금계좌_정정사유.set계약ID(계약ID);
        response.퇴직연금계좌_정정사유.set사용자ID(사용자ID);
        response.퇴직연금계좌_정정사유.set사유구분("W701");
        ye900Result = ye900DAO.getYE900(response.퇴직연금계좌_정정사유);
        if (ye900Result != null) {
            response.퇴직연금계좌_정정사유 = ye900Result;
        }

        response.연금저축계좌_정정사유.set계약ID(계약ID);
        response.연금저축계좌_정정사유.set사용자ID(사용자ID);
        response.연금저축계좌_정정사유.set사유구분("W702");
        ye900Result = ye900DAO.getYE900(response.연금저축계좌_정정사유);
        if (ye900Result != null) {
            response.연금저축계좌_정정사유 = ye900Result;
        }
    }

    @Override
    public void saveYW700(String 작업자ID, YW700Request request, int userType) {
        String 계약ID = null;
        String 사용자ID = null;

        if (request.퇴직연금계좌 != null) {
            for (YE301VO vo : request.퇴직연금계좌) {
                계약ID = vo.get계약ID();
                사용자ID = vo.get사용자ID();

                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye301DAO.updYE301Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye301DAO.updYE301Disable(vo);
                    ye301DAO.insYE301(vo);
                }
            }
        }

        if (request.연금저축계좌 != null) {
            for (YE302VO vo : request.연금저축계좌) {
                계약ID = vo.get계약ID();
                사용자ID = vo.get사용자ID();

                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye302DAO.updYE302Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye302DAO.updYE302Disable(vo);
                    ye302DAO.insYE302(vo);
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

        if (request.퇴직연금계좌_정정사유 != null) {
            saveYE900(작업자ID, "W701", request.퇴직연금계좌_정정사유);
        }
        if (request.연금저축계좌_정정사유 != null) {
            saveYE900(작업자ID, "W702", request.연금저축계좌_정정사유);
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
