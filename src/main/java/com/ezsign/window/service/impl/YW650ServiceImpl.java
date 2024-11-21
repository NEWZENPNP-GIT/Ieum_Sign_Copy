package com.ezsign.window.service.impl;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.feb.house.dao.YE108DAO;
import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE900DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.special.dao.YE410DAO;
import com.ezsign.feb.special.dao.YE411DAO;
import com.ezsign.feb.special.vo.YE409VO;
import com.ezsign.feb.special.vo.YE410VO;
import com.ezsign.feb.special.vo.YE411VO;
import com.ezsign.feb.worker.dao.YE001DAO;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.window.service.YW650Service;
import com.ezsign.window.vo.Code;
import com.ezsign.window.vo.Family;
import com.ezsign.window.vo.YW650CreditCardResponse;
import com.ezsign.window.vo.YW650Request;
import com.ezsign.window.vo.YW650Response;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@Service("yw650Service")
public class YW650ServiceImpl extends AbstractServiceImpl implements YW650Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "codeDAO")
    private CodeDAO codeDAO;

    @Resource(name = "ye001DAO")
    private YE001DAO ye001DAO;

    @Resource(name = "ye108DAO")
    private YE108DAO ye108DAO;

    @Resource(name = "ye900DAO")
    private YE900DAO ye900DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;

    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;
    
    @Resource(name = "ye410DAO")
    private YE410DAO ye410DAO;
    
    @Resource(name = "ye411DAO")
    private YE411DAO ye411DAO;
    
    @Override
    public void getYW650(String 계약ID, String 사용자ID, YW650Response response) {
        CodeVO codeVO = new CodeVO();
        codeVO.setGrpCommCode("432");
        response.신용카드_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 3);  // 1:국체청 3: 기타
        codeVO.setGrpCommCode("416");
        response.자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 3);  // 1:국체청 2:회사 3: 기타
        codeVO.setGrpCommCode("426");
        response.기간구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));

        YE001VO ye001 = new YE001VO();
        ye001.set계약ID(계약ID);
        ye001.set사용자ID(사용자ID);
        ye001.setStartPage(0);
        ye001.setEndPage(9999);
        response.부양가족ID = Family.getIDList(ye001DAO.getYE001List(ye001));

        YE108VO ye108 = new YE108VO();
        ye108.set계약ID(계약ID);
        ye108.set사용자ID(사용자ID);
        List<YE108VO> listYE108 = ye108DAO.getYE108List(ye108);

        if (listYE108.size() > 0) {
            response.신용카드 = listYE108;
        }

        YE900VO ye900Result;

        response.신용카드_정정사유.set계약ID(계약ID);
        response.신용카드_정정사유.set사용자ID(사용자ID);
        response.신용카드_정정사유.set사유구분("W640");  // 신용카드코드
        ye900Result = ye900DAO.getYE900(response.신용카드_정정사유);
        if (ye900Result != null) {
            response.신용카드_정정사유 = ye900Result;
        }
    }

    @Override
    public void saveYW650(String 작업자ID, YW650Request request, int userType) {
        String 계약ID = null;
        String 사용자ID = null;

        if (request.신용카드 != null) {
            for (YE108VO vo : request.신용카드) {
                계약ID = vo.get계약ID();
                사용자ID = vo.get사용자ID();

                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye108DAO.updYE108Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye108DAO.updYE108Disable(vo);
                    ye108DAO.insYE108(vo);
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

        if (request.신용카드_정정사유 != null) {
            saveYE900(작업자ID, "W640", request.신용카드_정정사유); // 신용카드코드
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

    // 신용카드 계산결과 조회
	@Override
	public void getCreditCardYW650(String 계약ID, String 사용자ID, YW650CreditCardResponse response) {
		YE410VO ye410VO = new YE410VO();
		ye410VO.set계약ID(계약ID);
		ye410VO.set사용자ID(사용자ID);
        response.신용카드계산결과항목 = ye410DAO.getYE410List(ye410VO);
        
        YE411VO ye411VO = new YE411VO();
        ye411VO.set계약ID(계약ID);
        ye411VO.set사용자ID(사용자ID);
        response.신용카드계산결과금액 = ye411DAO.getYE411(ye411VO);
		
	}
}
