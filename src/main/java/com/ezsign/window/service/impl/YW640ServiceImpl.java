package com.ezsign.window.service.impl;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.feb.house.dao.YE107DAO;
import com.ezsign.feb.house.dao.YE710DAO;
import com.ezsign.feb.house.vo.YE107VO;
import com.ezsign.feb.house.vo.YE710VO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE900DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.window.service.YW640Service;
import com.ezsign.window.vo.Code;
import com.ezsign.window.vo.YW640Request;
import com.ezsign.window.vo.YW640Response;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@Service("yw640Service")
public class YW640ServiceImpl extends AbstractServiceImpl implements YW640Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "codeDAO")
    private CodeDAO codeDAO;

    @Resource(name = "ye710DAO")
    private YE710DAO ye710DAO;

    @Resource(name = "ye107DAO")
    private YE107DAO ye107DAO;

    @Resource(name = "ye900DAO")
    private YE900DAO ye900DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;

    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;
    
    @Override
    public void getYW640(String 계약ID, String 사용자ID, YW640Response response) {
        CodeVO codeVO = new CodeVO();
        codeVO.setGrpCommCode("432");
        response.자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 3); // 1: 국체청 3: 기타
        codeVO.setGrpCommCode("418");
        response.주택마련저축구분 = Code.getCodeList(codeDAO.getCodeList(codeVO), 31, 39);
        codeVO.setGrpCommCode("404");
        response.금융회사등명칭 = Code.getCodeList(codeDAO.getCodeList(codeVO));

        YE710VO ye710 = new YE710VO();
        ye710.set계약ID(계약ID);
        ye710.set사용자ID(사용자ID);
        YE710VO ye710Result = ye710DAO.getYE710(ye710);
        if (ye710Result != null) {
            response.설문지_주택마련저축_입력여부 = "1".equals(ye710Result.get주택관련저축_입력여부());
            response.설문지_주택마련저축_적용여부 = "1".equals(ye710Result.get주택관련저축_적용여부());
        }

        YE107VO ye107 = new YE107VO();
        ye107.set계약ID(계약ID);
        ye107.set사용자ID(사용자ID);
        List<YE107VO> listYE107 = ye107DAO.getYE107List(ye107);

        if (listYE107.size() > 0) {
            response.주택마련저축 = listYE107;
        }

        YE900VO ye900Result;

        response.주택마련저축_정정사유.set계약ID(계약ID);
        response.주택마련저축_정정사유.set사용자ID(사용자ID);
        response.주택마련저축_정정사유.set사유구분("W630");  // 주택마련저축 코드
        ye900Result = ye900DAO.getYE900(response.주택마련저축_정정사유);
        if (ye900Result != null) {
            response.주택마련저축_정정사유 = ye900Result;
        }
    }

    @Override
    public void saveYW640(String 작업자ID, YW640Request request, int userType) {
        String 계약ID = null;
        String 사용자ID = null;

        if (request.주택마련저축 != null) {
            for (YE107VO vo : request.주택마련저축) {
                계약ID = vo.get계약ID();
                사용자ID = vo.get사용자ID();

                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye107DAO.updYE107Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye107DAO.updYE107Disable(vo);
                    ye107DAO.insYE107(vo);
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

        if (request.주택마련저축_정정사유 != null) {
            saveYE900(작업자ID, "W630", request.주택마련저축_정정사유); // 주택마련저축 코드
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
