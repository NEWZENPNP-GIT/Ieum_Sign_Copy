package com.ezsign.window.service.impl;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.feb.house.dao.*;
import com.ezsign.feb.house.vo.*;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE900DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.worker.dao.YE003DAO;
import com.ezsign.feb.worker.dao.YE052DAO;
import com.ezsign.feb.worker.vo.YE052VO;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.window.service.YW610Service;
import com.ezsign.window.vo.Code;
import com.ezsign.window.vo.YW610Request;
import com.ezsign.window.vo.YW610Response;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@Service("yw610Service")
public class YW610ServiceImpl extends AbstractServiceImpl implements YW610Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "codeDAO")
    private CodeDAO codeDAO;

    @Resource(name = "ye003DAO")
    private YE003DAO ye003DAO;

    @Resource(name = "ye710DAO")
    private YE710DAO ye710DAO;

    @Resource(name = "ye052DAO")
    private YE052DAO ye052DAO;

    @Resource(name = "ye101DAO")
    private YE101DAO ye101DAO;

    @Resource(name = "ye102DAO")
    private YE102DAO ye102DAO;

    @Resource(name = "ye103DAO")
    private YE103DAO ye103DAO;

    @Resource(name = "ye104DAO")
    private YE104DAO ye104DAO;

    @Resource(name = "ye105DAO")
    private YE105DAO ye105DAO;

    @Resource(name = "ye900DAO")
    private YE900DAO ye900DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;
    
    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;

    @Override
    public void getYW610(String 계약ID, String 사용자ID, String 작업자ID, YW610Response response) throws Exception {
        CodeVO codeVO = new CodeVO();
        codeVO.setGrpCommCode("422");
        response.보험료구분 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("423");
        response.차입구분 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("424");
        response.구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("415");
        response.유형코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));

        YE710VO ye710 = new YE710VO();
        ye710.set계약ID(계약ID);
        ye710.set사용자ID(사용자ID);
        YE710VO ye710Result = ye710DAO.getYE710(ye710);
        if (ye710Result != null) {
            response.설문지_주택임차차입금_입력여부 = "1".equals(ye710Result.get주택임차차입금_입력여부());
            response.설문지_주택임차차입금_적용여부 = "1".equals(ye710Result.get주택임차차입금_적용여부());
            response.설문지_장기주택저당차입금_입력여부 = "1".equals(ye710Result.get장기주택저당차입금_입력여부());
            response.설문지_장기주택저당차입금_적용여부 = "1".equals(ye710Result.get장기주택저당차입금_적용여부());
            response.설문지_월세액공제_입력여부 = "1".equals(ye710Result.get월세공제_입력여부());
            response.설문지_월세액공제_적용여부 = "1".equals(ye710Result.get월세공제_적용여부());
        }

        int 건강보험료_주근무지 = 0;
        int 장기요양보험료_주근무지 = 0;
        int 고용보험료_주근무지 = 0;

        int 건강보험료_종근무지 = 0;
        int 장기요양보험료_종근무지 = 0;
        int 고용보험료_종근무지 = 0;

        YE000VO ye000 = new YE000VO();
        ye000.set계약ID(계약ID);
        ye000.set사용자ID(사용자ID);
        List<YE000VO> listSum = ye003DAO.getYE003SumList(ye000);
        
        for (YE000VO vo : listSum) {
            if ("1".equals(vo.get근무지구분())) {
                건강보험료_주근무지 = StringUtil.strPaserInt(vo.get건강보험료());
                장기요양보험료_주근무지 = StringUtil.strPaserInt(vo.get장기요양보험료());
                고용보험료_주근무지 = StringUtil.strPaserInt(vo.get고용보험료());
            } else {
                건강보험료_종근무지 = StringUtil.strPaserInt(vo.get건강보험료());
                장기요양보험료_종근무지 = StringUtil.strPaserInt(vo.get장기요양보험료());
                고용보험료_종근무지 = StringUtil.strPaserInt(vo.get고용보험료());
            }
        }

        YE052VO ye052;

        ye052 = new YE052VO();
        ye052.set계약ID(계약ID);
        ye052.set사용자ID(사용자ID);
        ye052.set작업자ID(작업자ID);
        ye052.set보험료구분("1");
        ye052.set주근무지(건강보험료_주근무지);
        ye052.set종근무지(건강보험료_종근무지);
        response.보험료.add(ye052);

        ye052 = new YE052VO();
        ye052.set계약ID(계약ID);
        ye052.set사용자ID(사용자ID);
        ye052.set작업자ID(작업자ID);
        ye052.set보험료구분("2");
        ye052.set주근무지(장기요양보험료_주근무지);
        ye052.set종근무지(장기요양보험료_종근무지);
        response.보험료.add(ye052);

        ye052 = new YE052VO();
        ye052.set계약ID(계약ID);
        ye052.set사용자ID(사용자ID);
        ye052.set작업자ID(작업자ID);
        ye052.set보험료구분("3");
        ye052.set주근무지(고용보험료_주근무지);
        ye052.set종근무지(고용보험료_종근무지);
        response.보험료.add(ye052);

        // 국세청 납입금액 추가금액 및 추가납입금액 적용
        ye052 = new YE052VO();
        ye052.set계약ID(계약ID);
        ye052.set사용자ID(사용자ID);
        List<YE052VO> listYE052 = ye052DAO.getYE052List(ye052);

        for (YE052VO vo : listYE052) {
            for (int i = 0; i < response.보험료.size(); i++) {
            	YE052VO setYE052VO = response.보험료.get(i);
                if (setYE052VO.get보험료구분().equals(vo.get보험료구분())) {
                	setYE052VO.set국세청_납입금액(vo.get국세청_납입금액());
                	setYE052VO.set국세청_차감금액(vo.get국세청_차감금액());
                	setYE052VO.set추가납입금액(vo.get추가납입금액());
                	setYE052VO.set일련번호(vo.get일련번호());
                    response.보험료.set(i, setYE052VO);
                }
            }
        }

        YE101VO ye101 = new YE101VO();
        ye101.set계약ID(계약ID);
        ye101.set사용자ID(사용자ID);
        List<YE101VO> listYE101 = ye101DAO.getYE101List(ye101);

        YE102VO ye102 = new YE102VO();
        ye102.set계약ID(계약ID);
        ye102.set사용자ID(사용자ID);
        List<YE102VO> listYE102 = ye102DAO.getYE102List(ye102);

        YE103VO ye103 = new YE103VO();
        ye103.set계약ID(계약ID);
        ye103.set사용자ID(사용자ID);
        List<YE103VO> listYE103 = ye103DAO.getYE103List(ye103);

        YE104VO ye104;
        for (Code code : response.구분코드) {
            ye104 = new YE104VO();
            ye104.set계약ID(계약ID);
            ye104.set사용자ID(사용자ID);
            ye104.set작업자ID(작업자ID);
            ye104.set구분코드(code.code);
            ye104.set국세청자료(0);
            ye104.set차감금액(0);
            ye104.set기타자료(0);
            response.장기주택저당차입금이자상환액.add(ye104);
        }

        ye104 = new YE104VO();
        ye104.set계약ID(계약ID);
        ye104.set사용자ID(사용자ID);
        List<YE104VO> listYE104 = ye104DAO.getYE104List(ye104);

        YE105VO ye105 = new YE105VO();
        ye105.set계약ID(계약ID);
        ye105.set사용자ID(사용자ID);
        List<YE105VO> listYE105 = ye105DAO.getYE105List(ye105);

        if (listYE101.size() > 0) {
            response.주택임차차입금원리금상환액 = listYE101;
        }
        if (listYE102.size() > 0) {
            response.금전소비대차계약내용 = listYE102;
        }
        if (listYE103.size() > 0) {
            response.임대차계약내용 = listYE103;
        }

        if (listYE104.size() > 0) {
            for (YE104VO vo : listYE104) {
                for (int i = 0; i < response.장기주택저당차입금이자상환액.size(); i++) {
                    if (response.장기주택저당차입금이자상환액.get(i).get구분코드().equals(vo.get구분코드())) {
                        response.장기주택저당차입금이자상환액.set(i, vo);
                    }
                }
            }
        }

        if (listYE105.size() > 0) {
            response.월세액공제 = listYE105;
        }

        YE900VO ye900Result;

        response.보험료_정정사유.set계약ID(계약ID);
        response.보험료_정정사유.set사용자ID(사용자ID);
        response.보험료_정정사유.set사유구분("W612"); // 보험료(건강, 장기요양, 고용)코드
        ye900Result = ye900DAO.getYE900(response.보험료_정정사유);
        if (ye900Result != null) {
            response.보험료_정정사유 = ye900Result;
        }

        response.주택임차차입금원리금상환액_정정사유.set계약ID(계약ID);
        response.주택임차차입금원리금상환액_정정사유.set사용자ID(사용자ID);
        response.주택임차차입금원리금상환액_정정사유.set사유구분("W613"); // 주택임차차입금원리금상환액코드
        ye900Result = ye900DAO.getYE900(response.주택임차차입금원리금상환액_정정사유);
        if (ye900Result != null) {
            response.주택임차차입금원리금상환액_정정사유 = ye900Result;
        }

        response.주택임차차입금원리금상환액_소득공제명세_정정사유.set계약ID(계약ID);
        response.주택임차차입금원리금상환액_소득공제명세_정정사유.set사용자ID(사용자ID);
        response.주택임차차입금원리금상환액_소득공제명세_정정사유.set사유구분("W614"); // 주택임차차입금원리금상환액 소득공제명세코드
        ye900Result = ye900DAO.getYE900(response.주택임차차입금원리금상환액_소득공제명세_정정사유);
        if (ye900Result != null) {
            response.주택임차차입금원리금상환액_소득공제명세_정정사유 = ye900Result;
        }

        response.장기주택저당차입금이자상환액_정정사유.set계약ID(계약ID);
        response.장기주택저당차입금이자상환액_정정사유.set사용자ID(사용자ID);
        response.장기주택저당차입금이자상환액_정정사유.set사유구분("W615"); // 장기주택저당차입금이자상환액코드
        ye900Result = ye900DAO.getYE900(response.장기주택저당차입금이자상환액_정정사유);
        if (ye900Result != null) {
            response.장기주택저당차입금이자상환액_정정사유 = ye900Result;
        }

        response.월세액공제_정정사유.set계약ID(계약ID);
        response.월세액공제_정정사유.set사용자ID(사용자ID);
        response.월세액공제_정정사유.set사유구분("W616"); // 월세액공제코드
        ye900Result = ye900DAO.getYE900(response.월세액공제_정정사유);
        if (ye900Result != null) {
            response.월세액공제_정정사유 = ye900Result;
        }
    }

    @Override
    public void saveYW610(String 작업자ID, YW610Request request, int userType) {
        String 계약ID = null;
        String 사용자ID = null;

        if (request.보험료 != null) {
            for (YE052VO vo : request.보험료) {
                계약ID = vo.get계약ID();
                사용자ID = vo.get사용자ID();

                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye052DAO.updYE052Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye052DAO.updYE052Disable(vo);
                    ye052DAO.insYE052(vo);
                }
            }
        }

        if (request.주택임차차입금원리금상환액 != null) {
            for (YE101VO vo : request.주택임차차입금원리금상환액) {
                계약ID = vo.get계약ID();
                사용자ID = vo.get사용자ID();

                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye101DAO.updYE101Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye101DAO.updYE101Disable(vo);
                    ye101DAO.insYE101(vo);
                }
            }
        }

        if (request.금전소비대차계약내용 != null) {
            for (YE102VO vo : request.금전소비대차계약내용) {
                계약ID = vo.get계약ID();
                사용자ID = vo.get사용자ID();

                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye102DAO.updYE102Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye102DAO.updYE102Disable(vo);
                    ye102DAO.insYE102(vo);
                }
            }
        }

        if (request.임대차계약내용 != null) {
            for (YE103VO vo : request.임대차계약내용) {
                계약ID = vo.get계약ID();
                사용자ID = vo.get사용자ID();

                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye103DAO.updYE103Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye103DAO.updYE103Disable(vo);
                    ye103DAO.insYE103(vo);
                }
            }
        }

        if (request.장기주택저당차입금이자상환액 != null) {
            for (YE104VO vo : request.장기주택저당차입금이자상환액) {
                계약ID = vo.get계약ID();
                사용자ID = vo.get사용자ID();

                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye104DAO.updYE104Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye104DAO.updYE104Disable(vo);
                    ye104DAO.insYE104(vo);
                }
            }
        }

        if (request.월세액공제 != null) {
            for (YE105VO vo : request.월세액공제) {
                계약ID = vo.get계약ID();
                사용자ID = vo.get사용자ID();

                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye105DAO.updYE105Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye105DAO.updYE105Disable(vo);
                    ye105DAO.insYE105(vo);
                }
            }
        }

        if (계약ID != null && 사용자ID != null) {
        	
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

        if (request.보험료_정정사유 != null) {
            saveYE900(작업자ID, "W612", request.보험료_정정사유); // 보험료(건강, 장기요양, 고용)코드
        }
        if (request.주택임차차입금원리금상환액_정정사유 != null) {
            saveYE900(작업자ID, "W613", request.주택임차차입금원리금상환액_정정사유); // 주택임차차입금원리금상환액코드
        }
        if (request.주택임차차입금원리금상환액_소득공제명세_정정사유 != null) {
            saveYE900(작업자ID, "W614", request.주택임차차입금원리금상환액_소득공제명세_정정사유); // 주택임차차입금원리금상환액 소득공제명세코드
        }
        if (request.장기주택저당차입금이자상환액_정정사유 != null) {
            saveYE900(작업자ID, "W615", request.장기주택저당차입금이자상환액_정정사유); // 장기주택저당차입금이자상환액코드
        }
        if (request.월세액공제_정정사유 != null) {
            saveYE900(작업자ID, "W616", request.월세액공제_정정사유); // 월세액공제코드
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
