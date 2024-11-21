package com.ezsign.window.service.impl;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.feb.house.dao.YE106DAO;
import com.ezsign.feb.house.vo.YE106VO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE900DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.window.service.YW630Service;
import com.ezsign.window.vo.Code;
import com.ezsign.window.vo.YW630Request;
import com.ezsign.window.vo.YW630Response;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@Service("yw630Service")
public class YW630ServiceImpl extends AbstractServiceImpl implements YW630Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "codeDAO")
    private CodeDAO codeDAO;

    @Resource(name = "ye106DAO")
    private YE106DAO ye106DAO;

    @Resource(name = "ye900DAO")
    private YE900DAO ye900DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;
    
    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;

    @Override
    public void getYW630(String 계약ID, String 사용자ID, YW630Response response) {
        CodeVO codeVO = new CodeVO();
        codeVO.setGrpCommCode("432");
        response.자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 3);  // 1: 국체청 3: 기타
        codeVO.setGrpCommCode("418");
        response.연금저축구분 = Code.getCodeList(codeDAO.getCodeList(codeVO), 21, 21);
        codeVO.setGrpCommCode("404");
        response.금융회사등명칭 = Code.getCodeList(codeDAO.getCodeList(codeVO));

        YE106VO ye106 = new YE106VO();
        ye106.set계약ID(계약ID);
        ye106.set사용자ID(사용자ID);
        List<YE106VO> listYE106 = ye106DAO.getYE106List(ye106);

        if (listYE106.size() > 0) {
            response.개인연금저축 = listYE106;
        }

        YE900VO ye900Result;

        response.개인연금저축_정정사유.set계약ID(계약ID);
        response.개인연금저축_정정사유.set사용자ID(사용자ID);
        response.개인연금저축_정정사유.set사유구분("W620");	// 개인연금저축코드
        ye900Result = ye900DAO.getYE900(response.개인연금저축_정정사유);
        if (ye900Result != null) {
            response.개인연금저축_정정사유 = ye900Result;
        }
    }

    @Override
    public void saveYW630(String 작업자ID, YW630Request request, int userType) {
        String 계약ID = null;
        String 사용자ID = null;

        if (request.개인연금저축 != null) {
            for (YE106VO vo : request.개인연금저축) {
                계약ID = vo.get계약ID();
                사용자ID = vo.get사용자ID();

                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye106DAO.updYE106Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye106DAO.updYE106Disable(vo);
                    ye106DAO.insYE106(vo);
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

        if (request.개인연금저축_정정사유 != null) {
            saveYE900(작업자ID, "W620", request.개인연금저축_정정사유); // 개인연금저축코드
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
