package com.ezsign.window.service.impl;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.feb.etc.dao.YE501DAO;
import com.ezsign.feb.etc.dao.YE502DAO;
import com.ezsign.feb.etc.dao.YE503DAO;
import com.ezsign.feb.etc.vo.YE501VO;
import com.ezsign.feb.etc.vo.YE502VO;
import com.ezsign.feb.etc.vo.YE503VO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE900DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.window.service.YW730Service;
import com.ezsign.window.vo.Code;
import com.ezsign.window.vo.YW730Request;
import com.ezsign.window.vo.YW730Response;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@SuppressWarnings("NonAsciiCharacters")
@Service("yw730Service")
public class YW730ServiceImpl extends AbstractServiceImpl implements YW730Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "codeDAO")
    private CodeDAO codeDAO;

    @Resource(name = "ye501DAO")
    private YE501DAO ye501DAO;

    @Resource(name = "ye502DAO")
    private YE502DAO ye502DAO;

    @Resource(name = "ye503DAO")
    private YE503DAO ye503DAO;

    @Resource(name = "ye900DAO")
    private YE900DAO ye900DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;

    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;
    
    @Override
    public void getYW730(String 계약ID, String 사용자ID, YW730Response response) {
        CodeVO codeVO = new CodeVO();
        codeVO.setGrpCommCode("405");
        response.납세국가코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));

        YE501VO ye501 = new YE501VO();
        ye501.set계약ID(계약ID);
        ye501.set사용자ID(사용자ID);
        YE501VO ye501Result = ye501DAO.getYE501(ye501);

        YE502VO ye502 = new YE502VO();
        ye502.set계약ID(계약ID);
        ye502.set사용자ID(사용자ID);
        YE502VO ye502Result = ye502DAO.getYE502(ye502);

        YE503VO ye503 = new YE503VO();
        ye503.set계약ID(계약ID);
        ye503.set사용자ID(사용자ID);
        YE503VO ye503Result = ye503DAO.getYE503(ye503);

        if (ye501Result != null) {
            response.납세조합공제 = ye501Result;
        }
        if (ye502Result != null) {
            response.주택자금차입금이자세액공제 = ye502Result;
        }
        if (ye503Result != null) {
            response.외국납부세액공제 = ye503Result;
        }

        YE900VO ye900Result;

        response.납세조합공제_정정사유.set계약ID(계약ID);
        response.납세조합공제_정정사유.set사용자ID(사용자ID);
        response.납세조합공제_정정사유.set사유구분("W731");
        ye900Result = ye900DAO.getYE900(response.납세조합공제_정정사유);
        if (ye900Result != null) {
            response.납세조합공제_정정사유 = ye900Result;
        }

        response.주택자금차입금이자세액공제_정정사유.set계약ID(계약ID);
        response.주택자금차입금이자세액공제_정정사유.set사용자ID(사용자ID);
        response.주택자금차입금이자세액공제_정정사유.set사유구분("W732");
        ye900Result = ye900DAO.getYE900(response.주택자금차입금이자세액공제_정정사유);
        if (ye900Result != null) {
            response.주택자금차입금이자세액공제_정정사유 = ye900Result;
        }

        response.외국납부세액공제_정정사유.set계약ID(계약ID);
        response.외국납부세액공제_정정사유.set사용자ID(사용자ID);
        response.외국납부세액공제_정정사유.set사유구분("W733");
        ye900Result = ye900DAO.getYE900(response.외국납부세액공제_정정사유);
        if (ye900Result != null) {
            response.외국납부세액공제_정정사유 = ye900Result;
        }
    }

    @Override
    public void saveYW730(String 작업자ID, YW730Request request, int userType) {
        String 계약ID = null;
        String 사용자ID = null;

        if (request.납세조합공제 != null) {
            계약ID = request.납세조합공제.get계약ID();
            사용자ID = request.납세조합공제.get사용자ID();

            request.납세조합공제.set작업자ID(작업자ID);

            if ("D".equals(request.납세조합공제.getDbMode())) {
                ye501DAO.updYE501Disable(request.납세조합공제);
            } else if ("C".equals(request.납세조합공제.getDbMode()) || "U".equals(request.납세조합공제.getDbMode())) {
                ye501DAO.updYE501Disable(request.납세조합공제);
                ye501DAO.insYE501(request.납세조합공제);
            }
        }

        if (request.주택자금차입금이자세액공제 != null) {
            계약ID = request.주택자금차입금이자세액공제.get계약ID();
            사용자ID = request.주택자금차입금이자세액공제.get사용자ID();

            request.주택자금차입금이자세액공제.set작업자ID(작업자ID);

            if ("D".equals(request.주택자금차입금이자세액공제.getDbMode())) {
                ye502DAO.updYE502Disable(request.주택자금차입금이자세액공제);
            } else if ("C".equals(request.주택자금차입금이자세액공제.getDbMode()) || "U".equals(request.주택자금차입금이자세액공제.getDbMode())) {
                ye502DAO.updYE502Disable(request.주택자금차입금이자세액공제);
                ye502DAO.insYE502(request.주택자금차입금이자세액공제);
            }
        }

        if (request.외국납부세액공제 != null) {
            계약ID = request.외국납부세액공제.get계약ID();
            사용자ID = request.외국납부세액공제.get사용자ID();

            request.외국납부세액공제.set작업자ID(작업자ID);

            if ("D".equals(request.외국납부세액공제.getDbMode())) {
                ye503DAO.updYE503Disable(request.외국납부세액공제);
            } else if ("C".equals(request.외국납부세액공제.getDbMode()) || "U".equals(request.외국납부세액공제.getDbMode())) {
                ye503DAO.updYE503Disable(request.외국납부세액공제);
                ye503DAO.insYE503(request.외국납부세액공제);
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

        if (request.납세조합공제_정정사유 != null) {
            saveYE900(작업자ID, "W731", request.납세조합공제_정정사유);
        }
        if (request.주택자금차입금이자세액공제_정정사유 != null) {
            saveYE900(작업자ID, "W732", request.주택자금차입금이자세액공제_정정사유);
        }
        if (request.외국납부세액공제_정정사유 != null) {
            saveYE900(작업자ID, "W733", request.외국납부세액공제_정정사유);
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
