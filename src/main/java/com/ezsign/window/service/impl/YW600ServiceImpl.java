package com.ezsign.window.service.impl;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE900DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.worker.dao.YE003DAO;
import com.ezsign.feb.worker.dao.YE051DAO;
import com.ezsign.feb.worker.vo.YE051VO;
import com.ezsign.window.service.YW600Service;
import com.ezsign.window.vo.Code;
import com.ezsign.window.vo.YW600Request;
import com.ezsign.window.vo.YW600Response;
import com.jarvis.common.util.StringUtil;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@Service("yw600Service")
public class YW600ServiceImpl extends AbstractServiceImpl implements YW600Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "codeDAO")
    private CodeDAO codeDAO;

    @Resource(name = "ye003DAO")
    private YE003DAO ye003DAO;

    @Resource(name = "ye051DAO")
    private YE051DAO ye051DAO;

    @Resource(name = "ye900DAO")
    private YE900DAO ye900DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;

    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;
    
    @Override
    public void getYW600(String 계약ID, String 사용자ID, YW600Response response) throws Exception {
        CodeVO codeVO = new CodeVO();
        codeVO.setGrpCommCode("421");
        response.보험료구분 = Code.getCodeList(codeDAO.getCodeList(codeVO));

        YE000VO ye000 = new YE000VO();
        ye000.set계약ID(계약ID);
        ye000.set사용자ID(사용자ID);
        List<YE000VO> listSum = ye003DAO.getYE003SumList(ye000);

        int 국민연금_주근무지 = 0;
        int 공무원연금_주근무지 = 0;
        int 군인연금_주근무지 = 0;
        int 사립학교교직원연금_주근무지 = 0;
        int 별정우체국연금_주근무지 = 0;

        int 국민연금_종근무지 = 0;
        int 공무원연금_종근무지 = 0;
        int 군인연금_종근무지 = 0;
        int 사립학교교직원연금_종근무지 = 0;
        int 별정우체국연금_종근무지 = 0;

        for (YE000VO vo : listSum) {
            if ("1".equals(vo.get근무지구분())) {
                국민연금_주근무지 = StringUtil.strPaserInt(vo.get국민연금보험료());
                공무원연금_주근무지 = StringUtil.strPaserInt(vo.get공무원연금());
                군인연금_주근무지 = StringUtil.strPaserInt(vo.get군인연금());
                사립학교교직원연금_주근무지 = StringUtil.strPaserInt(vo.get사립학교교직원연금());
                별정우체국연금_주근무지 = StringUtil.strPaserInt(vo.get별정우체국연금());
            } else {
                국민연금_종근무지 = StringUtil.strPaserInt(vo.get국민연금보험료());
                공무원연금_종근무지 = StringUtil.strPaserInt(vo.get공무원연금());
                군인연금_종근무지 = StringUtil.strPaserInt(vo.get군인연금());
                사립학교교직원연금_종근무지 = StringUtil.strPaserInt(vo.get사립학교교직원연금());
                별정우체국연금_종근무지 = StringUtil.strPaserInt(vo.get별정우체국연금());
            }
        }

        response.국민연금보험료.set계약ID(계약ID);
        response.국민연금보험료.set사용자ID(사용자ID);
        response.국민연금보험료.set작업자ID(사용자ID);
        response.국민연금보험료.set보험료구분("1");
        response.국민연금보험료.set주근무지(국민연금_주근무지);
        response.국민연금보험료.set종근무지(국민연금_종근무지);

        if (공무원연금_주근무지 != 0 || 공무원연금_종근무지 != 0) {
            YE051VO ye051 = new YE051VO();
            ye051.set계약ID(계약ID);
            ye051.set사용자ID(사용자ID);
            ye051.set작업자ID(사용자ID);
            ye051.set보험료구분("2");
            ye051.set주근무지(공무원연금_주근무지);
            ye051.set종근무지(공무원연금_종근무지);
            response.공적연금보험료.add(ye051);
        }
        if (군인연금_주근무지 != 0 || 군인연금_종근무지 != 0) {
            YE051VO ye051 = new YE051VO();
            ye051.set계약ID(계약ID);
            ye051.set사용자ID(사용자ID);
            ye051.set작업자ID(사용자ID);
            ye051.set보험료구분("3");
            ye051.set주근무지(군인연금_주근무지);
            ye051.set종근무지(군인연금_종근무지);
            response.공적연금보험료.add(ye051);
        }
        if (사립학교교직원연금_주근무지 != 0 || 사립학교교직원연금_종근무지 != 0) {
            YE051VO ye051 = new YE051VO();
            ye051.set계약ID(계약ID);
            ye051.set사용자ID(사용자ID);
            ye051.set작업자ID(사용자ID);
            ye051.set보험료구분("4");
            ye051.set주근무지(사립학교교직원연금_주근무지);
            ye051.set종근무지(사립학교교직원연금_종근무지);
            response.공적연금보험료.add(ye051);
        }
        if (별정우체국연금_주근무지 != 0 || 별정우체국연금_종근무지 != 0) {
            YE051VO ye051 = new YE051VO();
            ye051.set계약ID(계약ID);
            ye051.set사용자ID(사용자ID);
            ye051.set작업자ID(사용자ID);
            ye051.set보험료구분("5");
            ye051.set주근무지(별정우체국연금_주근무지);
            ye051.set종근무지(별정우체국연금_종근무지);
            response.공적연금보험료.add(ye051);
        }

        YE051VO ye051 = new YE051VO();
        ye051.set계약ID(계약ID);
        ye051.set사용자ID(사용자ID);
        List<YE051VO> listYE051 = ye051DAO.getYE051List(ye051);

        if (listYE051.size() > 0) {
            boolean find;

            for (YE051VO vo : listYE051) {
                if ("1".equals(vo.get보험료구분())) {
                    // 국민연금
                    response.국민연금보험료.set계약ID(vo.get계약ID());
                    response.국민연금보험료.set사용자ID(vo.get사용자ID());
                    response.국민연금보험료.set작업자ID(vo.get작업자ID());
                    response.국민연금보험료.set일련번호(vo.get일련번호());
                    response.국민연금보험료.set국세청_납입금액(vo.get국세청_납입금액());
                    response.국민연금보험료.set국세청_차감금액(vo.get국세청_차감금액());
                    response.국민연금보험료.set추가납입금액(vo.get추가납입금액());
                    response.국민연금보험료.set계약ID(vo.get계약ID());
                    response.국민연금보험료.set계약ID(vo.get계약ID());
                    response.국민연금보험료.set최종저장여부(vo.get최종저장여부());
                    response.국민연금보험료.set사용여부(vo.get사용여부());
                    response.국민연금보험료.set등록일시(vo.get등록일시());
                    response.국민연금보험료.set수정일시(vo.get수정일시());
                } else {
                    find = false;
                    for (YE051VO vo2 : response.공적연금보험료) {
                        if (vo2.get보험료구분().equals(vo.get보험료구분())) {
                            find = true;
                            vo2.set계약ID(vo.get계약ID());
                            vo2.set사용자ID(vo.get사용자ID());
                            vo2.set작업자ID(vo.get작업자ID());
                            vo2.set일련번호(vo.get일련번호());
                            vo2.set국세청_납입금액(vo.get국세청_납입금액());
                            vo2.set국세청_차감금액(vo.get국세청_차감금액());
                            vo2.set추가납입금액(vo.get추가납입금액());
                            vo2.set계약ID(vo.get계약ID());
                            vo2.set계약ID(vo.get계약ID());
                            vo2.set최종저장여부(vo.get최종저장여부());
                            vo2.set사용여부(vo.get사용여부());
                            vo2.set등록일시(vo.get등록일시());
                            vo2.set수정일시(vo.get수정일시());
                        }
                    }
                    if (!find) {
                        response.공적연금보험료.add(vo);
                    }
                }
            }

            YE900VO ye900Result;

            response.국민연금보험료_정정사유.set계약ID(계약ID);
            response.국민연금보험료_정정사유.set사용자ID(사용자ID);
            response.국민연금보험료_정정사유.set사유구분("W601");
            ye900Result = ye900DAO.getYE900(response.국민연금보험료_정정사유);
            if (ye900Result != null) {
                response.국민연금보험료_정정사유 = ye900Result;
            }

            response.공적연금보험료_정정사유.set계약ID(계약ID);
            response.공적연금보험료_정정사유.set사용자ID(사용자ID);
            response.공적연금보험료_정정사유.set사유구분("W602");
            ye900Result = ye900DAO.getYE900(response.공적연금보험료_정정사유);
            if (ye900Result != null) {
                response.공적연금보험료_정정사유 = ye900Result;
            }
        }
    }

    @Override
    public void saveYW600(String 작업자ID, YW600Request request, int userType) {
        String 계약ID = null;
        String 사용자ID = null;

        if (request.국민연금보험료 != null) {
            계약ID = request.국민연금보험료.get계약ID();
            사용자ID = request.국민연금보험료.get사용자ID();

            request.국민연금보험료.set작업자ID(작업자ID);

            if ("D".equals(request.국민연금보험료.getDbMode())) {
                ye051DAO.updYE051Disable(request.국민연금보험료);
            } else if ("C".equals(request.국민연금보험료.getDbMode()) || "U".equals(request.국민연금보험료.getDbMode())) {
                ye051DAO.updYE051Disable(request.국민연금보험료);
                ye051DAO.insYE051(request.국민연금보험료);
            }
        }

        if (request.공적연금보험료 != null) {
            for (YE051VO vo : request.공적연금보험료) {
                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye051DAO.updYE051Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye051DAO.updYE051Disable(vo);
                    ye051DAO.insYE051(vo);
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

        if (request.국민연금보험료_정정사유 != null) {
            saveYE900(작업자ID, "W601", request.국민연금보험료_정정사유);
        }
        if (request.공적연금보험료_정정사유 != null) {
            saveYE900(작업자ID, "W602", request.공적연금보험료_정정사유);
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
