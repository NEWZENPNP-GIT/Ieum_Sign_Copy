package com.ezsign.window.service.impl;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE900DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.other.dao.YE201DAO;
import com.ezsign.feb.other.dao.YE202DAO;
import com.ezsign.feb.other.dao.YE203DAO;
import com.ezsign.feb.other.dao.YE204DAO;
import com.ezsign.feb.other.vo.YE201VO;
import com.ezsign.feb.other.vo.YE202VO;
import com.ezsign.feb.other.vo.YE203VO;
import com.ezsign.feb.other.vo.YE204VO;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.window.service.YW670Service;
import com.ezsign.window.vo.Code;
import com.ezsign.window.vo.YW670Request;
import com.ezsign.window.vo.YW670Response;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@Service("yw670Service")
public class YW670ServiceImpl extends AbstractServiceImpl implements YW670Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "codeDAO")
    private CodeDAO codeDAO;

    @Resource(name = "ye201DAO")
    private YE201DAO ye201DAO;

    @Resource(name = "ye202DAO")
    private YE202DAO ye202DAO;

    @Resource(name = "ye203DAO")
    private YE203DAO ye203DAO;

    @Resource(name = "ye204DAO")
    private YE204DAO ye204DAO;

    @Resource(name = "ye900DAO")
    private YE900DAO ye900DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;

    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;
    
    @Override
    public void getYW670(String 계약ID, String 사용자ID, YW670Response response) {
        CodeVO codeVO = new CodeVO();
        codeVO.setGrpCommCode("427");
        response.구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("404");
        response.금융회사등명칭 = Code.getCodeList(codeDAO.getCodeList(codeVO));

        YE201VO ye201 = new YE201VO();
        ye201.set계약ID(계약ID);
        ye201.set사용자ID(사용자ID);
        YE201VO ye201Result = ye201DAO.getYE201(ye201);

        YE202VO ye202 = new YE202VO();
        ye202.set계약ID(계약ID);
        ye202.set사용자ID(사용자ID);
        List<YE202VO> listYE202 = ye202DAO.getYE202List(ye202);

        YE203VO ye203 = new YE203VO();
        ye203.set계약ID(계약ID);
        ye203.set사용자ID(사용자ID);
        YE203VO ye203Result = ye203DAO.getYE203(ye203);
        
        // 우리사주 벤처 
        YE203VO ye203_1 = new YE203VO();
        ye203_1.set계약ID(계약ID);
        ye203_1.set사용자ID(사용자ID);
        YE203VO ye203_1Result = ye203DAO.getYE203_1(ye203);

        YE204VO ye204 = new YE204VO();
        ye204.set계약ID(계약ID);
        ye204.set사용자ID(사용자ID);
        YE204VO ye204Result = ye204DAO.getYE204(ye204);

        if (ye201Result != null) {
            response.소기업소상공인공제부금 = ye201Result;
        }
        if (listYE202.size() > 0) {
            response.투자조합출자 = listYE202;
        }
        if (ye203Result != null) {
            response.우리사주조합출연금 = ye203Result;
        }
        if (ye203_1Result != null) {
            response.우리사주조합출연금벤처 = ye203_1Result;
        }
        if (ye204Result != null) {
            response.고용유지중소기업근로자 = ye204Result;
        }

        YE900VO ye900Result;

        response.소기업소상공인공제부금_정정사유.set계약ID(계약ID);
        response.소기업소상공인공제부금_정정사유.set사용자ID(사용자ID);
        response.소기업소상공인공제부금_정정사유.set사유구분("W661");  // 소기업소상공인공제
        ye900Result = ye900DAO.getYE900(response.소기업소상공인공제부금_정정사유);
        if (ye900Result != null) {
            response.소기업소상공인공제부금_정정사유 = ye900Result;
        }

        response.투자조합출자_정정사유.set계약ID(계약ID);
        response.투자조합출자_정정사유.set사용자ID(사용자ID);
        response.투자조합출자_정정사유.set사유구분("W662");  // 투자조합출자
        ye900Result = ye900DAO.getYE900(response.투자조합출자_정정사유);
        if (ye900Result != null) {
            response.투자조합출자_정정사유 = ye900Result;
        }

        response.우리사주조합출연금_정정사유.set계약ID(계약ID);
        response.우리사주조합출연금_정정사유.set사용자ID(사용자ID);
        response.우리사주조합출연금_정정사유.set사유구분("W663");  // 우리사주조합출연금
        ye900Result = ye900DAO.getYE900(response.우리사주조합출연금_정정사유);
        if (ye900Result != null) {
            response.우리사주조합출연금_정정사유 = ye900Result;
        }

        response.고용유지중소기업근로자_정정사유.set계약ID(계약ID);
        response.고용유지중소기업근로자_정정사유.set사용자ID(사용자ID);
        response.고용유지중소기업근로자_정정사유.set사유구분("W664");  // 고용유지중소기업근로자
        ye900Result = ye900DAO.getYE900(response.고용유지중소기업근로자_정정사유);
        if (ye900Result != null) {
            response.고용유지중소기업근로자_정정사유 = ye900Result;
        }
    }

    @Override
    public void saveYW670(String 작업자ID, YW670Request request, int userType) {
        String 계약ID = null;
        String 사용자ID = null;

        if (request.소기업소상공인공제부금 != null) {
            계약ID = request.소기업소상공인공제부금.get계약ID();
            사용자ID = request.소기업소상공인공제부금.get사용자ID();

            request.소기업소상공인공제부금.set작업자ID(작업자ID);

            if ("D".equals(request.소기업소상공인공제부금.getDbMode())) {
                ye201DAO.updYE201Disable(request.소기업소상공인공제부금);
            } else if ("C".equals(request.소기업소상공인공제부금.getDbMode()) || "U".equals(request.소기업소상공인공제부금.getDbMode())) {
                ye201DAO.updYE201Disable(request.소기업소상공인공제부금);
                ye201DAO.insYE201(request.소기업소상공인공제부금);
            }
        }

        if (request.투자조합출자 != null) {
            for (YE202VO vo : request.투자조합출자) {
                계약ID = vo.get계약ID();
                사용자ID = vo.get사용자ID();

                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye202DAO.updYE202Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye202DAO.updYE202Disable(vo);
                    ye202DAO.insYE202(vo);
                }
            }
        }

        if (request.우리사주조합출연금 != null) {
            계약ID = request.우리사주조합출연금.get계약ID();
            사용자ID = request.우리사주조합출연금.get사용자ID();

            request.우리사주조합출연금.set작업자ID(작업자ID);

            if ("D".equals(request.우리사주조합출연금.getDbMode())) {
                ye203DAO.updYE203Disable(request.우리사주조합출연금);
            } else if ("C".equals(request.우리사주조합출연금.getDbMode()) || "U".equals(request.우리사주조합출연금.getDbMode())) {
                ye203DAO.updYE203Disable(request.우리사주조합출연금);
                ye203DAO.insYE203(request.우리사주조합출연금);
            }
        }

        if (request.우리사주조합출연금벤처 != null) {
            계약ID = request.우리사주조합출연금벤처.get계약ID();
            사용자ID = request.우리사주조합출연금벤처.get사용자ID();

            request.우리사주조합출연금벤처.set작업자ID(작업자ID);

            if ("D".equals(request.우리사주조합출연금벤처.getDbMode())) {
                ye203DAO.updYE203Disable(request.우리사주조합출연금벤처);
            } else if ("C".equals(request.우리사주조합출연금벤처.getDbMode()) || "U".equals(request.우리사주조합출연금벤처.getDbMode())) {
                ye203DAO.updYE203Disable(request.우리사주조합출연금벤처);
                ye203DAO.insYE203(request.우리사주조합출연금벤처);
            }
        }

        if (request.고용유지중소기업근로자 != null) {
            계약ID = request.고용유지중소기업근로자.get계약ID();
            사용자ID = request.고용유지중소기업근로자.get사용자ID();

            request.고용유지중소기업근로자.set작업자ID(작업자ID);

            if ("D".equals(request.고용유지중소기업근로자.getDbMode())) {
                ye204DAO.updYE204Disable(request.고용유지중소기업근로자);
            } else if ("C".equals(request.고용유지중소기업근로자.getDbMode()) || "U".equals(request.고용유지중소기업근로자.getDbMode())) {
                ye204DAO.updYE204Disable(request.고용유지중소기업근로자);
                ye204DAO.insYE204(request.고용유지중소기업근로자);
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

        if (request.소기업소상공인공제부금_정정사유 != null) {
            saveYE900(작업자ID, "W661", request.소기업소상공인공제부금_정정사유);
        }
        if (request.투자조합출자_정정사유 != null) {
            saveYE900(작업자ID, "W662", request.투자조합출자_정정사유);
        }
        if (request.우리사주조합출연금_정정사유 != null) {
            saveYE900(작업자ID, "W663", request.우리사주조합출연금_정정사유);
        }
        if (request.고용유지중소기업근로자_정정사유 != null) {
            saveYE900(작업자ID, "W664", request.고용유지중소기업근로자_정정사유);
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
