package com.ezsign.window.service.impl;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE900DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.special.dao.*;
import com.ezsign.feb.special.vo.*;
import com.ezsign.feb.system.dao.YS000DAO;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.worker.dao.YE001DAO;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.window.service.YW710Service;
import com.ezsign.window.vo.Code;
import com.ezsign.window.vo.Family;
import com.ezsign.window.vo.YW710MedicalResponse;
import com.ezsign.window.vo.YW710Request;
import com.ezsign.window.vo.YW710Response;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@Service("yw710Service")
public class YW710ServiceImpl extends AbstractServiceImpl implements YW710Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "codeDAO")
    private CodeDAO codeDAO;

    @Resource(name = "ys000DAO")
    private YS000DAO ys000DAO;

    @Resource(name = "ye001DAO")
    private YE001DAO ye001DAO;

    @Resource(name = "ye401DAO")
    private YE401DAO ye401DAO;

    @Resource(name = "ye402DAO")
    private YE402DAO ye402DAO;

    @Resource(name = "ye403DAO")
    private YE403DAO ye403DAO;

    @Resource(name = "ye404DAO")
    private YE404DAO ye404DAO;

    @Resource(name = "ye405DAO")
    private YE405DAO ye405DAO;
    
    @Resource(name = "ye408DAO")
    private YE408DAO ye408DAO;

    @Resource(name = "ye900DAO")
    private YE900DAO ye900DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;

    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;
    
    @Resource(name = "ye409DAO")
    private YE409DAO ye409DAO;
    
    @Override
    public void getYW710(String bizId, String 계약ID, String 사용자ID, YW710Response response) {
        CodeVO codeVO = new CodeVO();
        codeVO.setGrpCommCode("432");
        response.보험료의료비_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 3);  // 1: 국체청 3: 기타
        codeVO.setGrpCommCode("416");
        response.교육비기부금_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 3);  // 1: 국체청 2:회사 3: 기타

        codeVO.setGrpCommCode("428");
        response.보험료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("407");
        response.의료비증빙코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("409");
        response.의료비공제종류코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("436");
        response.의료비유형 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("410");
        response.교육비공제종류코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("406");
        response.기부금코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("429");
        response.기부내용 = Code.getCodeList(codeDAO.getCodeList(codeVO));

        YS000VO ys000 = new YS000VO();
        ys000.setBizId(bizId);
        ys000.set계약ID(계약ID);
        List<YS000VO> listYS000 = ys000DAO.getYS000List(ys000);
        if (listYS000.size() > 0) {
            response.계약년도 = listYS000.get(0).getFebYear();
        }

        YE001VO ye001 = new YE001VO();
        ye001.set계약ID(계약ID);
        ye001.set사용자ID(사용자ID);
        ye001.setStartPage(0);
        ye001.setEndPage(9999);
        response.부양가족ID = Family.getIDList(ye001DAO.getYE001List(ye001));

        YE401VO ye401 = new YE401VO();
        ye401.set계약ID(계약ID);
        ye401.set사용자ID(사용자ID);
        List<YE401VO> listYE401 = ye401DAO.getYE401List(ye401);

        YE402VO ye402 = new YE402VO();
        ye402.set계약ID(계약ID);
        ye402.set사용자ID(사용자ID);
        List<YE402VO> listYE402 = ye402DAO.getYE402List(ye402);

        YE403VO ye403 = new YE403VO();
        ye403.set계약ID(계약ID);
        ye403.set사용자ID(사용자ID);
        List<YE403VO> listYE403 = ye403DAO.getYE403List(ye403);

        YE404VO ye404 = new YE404VO();
        ye404.set계약ID(계약ID);
        ye404.set사용자ID(사용자ID);
        ye404.setStartPage(0);
        ye404.setEndPage(9999);
        ye404.setSortName("자료구분코드, 기부코드");
        List<YE404VO> listYE404 = ye404DAO.getYE404List(ye404);

        YE405VO ye405 = new YE405VO();
        ye405.set계약ID(계약ID);
        ye405.set사용자ID(사용자ID);
        ye405.setFebYear(response.계약년도);
        ye405.setStartPage(0);
        ye405.setEndPage(9999);
        ye405.setSortName("기부년도 DESC, 기부금종류코드");
        List<YE405VO> listYE405 = ye405DAO.getYE405List(ye405);

        YE408VO ye408 = new YE408VO();
        ye408.set계약ID(계약ID);
        ye408.set사용자ID(사용자ID);
        ye408.setStartPage(0);
        ye408.setEndPage(9999);
        ye408.setSortName("기부년도 DESC, 기부금종류코드");
        List<YE408VO> listYE408 = ye408DAO.getYE408List(ye408);


        if (listYE401.size() > 0) {
            response.보험료 = listYE401;
        }
        if (listYE402.size() > 0) {
            response.의료비 = listYE402;
        }
        if (listYE403.size() > 0) {
            response.교육비 = listYE403;
        }
        if (listYE404.size() > 0) {
            response.기부금 = listYE404;
            response.기부금합계_합계 = new YW710Response.Sum();
            response.기부금합계_본인 = new YW710Response.Sum();
            response.기부금합계_배우자 = new YW710Response.Sum();
            response.기부금합계_직계비속 = new YW710Response.Sum();
            response.기부금합계_직계존속 = new YW710Response.Sum();
            response.기부금합계_형제자매 = new YW710Response.Sum();
            response.기부금합계_그외 = new YW710Response.Sum();

            for (YE404VO vo : listYE404) {
                switch (vo.get가족관계()) {
                    case "0":
                        if ("10".equals(vo.get기부코드())) {
                            response.기부금합계_본인.법정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.법정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("20".equals(vo.get기부코드())) {
                            response.기부금합계_본인.정치자금기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.정치자금기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("40".equals(vo.get기부코드())) {
                            response.기부금합계_본인.종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("41".equals(vo.get기부코드())) {
                            response.기부금합계_본인.종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("42".equals(vo.get기부코드())) {
                            response.기부금합계_본인.우리사주조합기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.우리사주조합기부금 += vo.get기부명세_공제대상기부금();
                        }
                        response.기부금합계_본인.총계 += vo.get기부명세_공제금액();
                        response.기부금합계_본인.기부장려금신청금액 += vo.get기부명세_기부장려금();
                        response.기부금합계_본인.기타 += vo.get기부명세_기타();
                        break;

                    case "3":
                        if ("10".equals(vo.get기부코드())) {
                            response.기부금합계_배우자.법정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.법정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("20".equals(vo.get기부코드())) {
                            response.기부금합계_배우자.정치자금기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.정치자금기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("40".equals(vo.get기부코드())) {
                            response.기부금합계_배우자.종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("41".equals(vo.get기부코드())) {
                            response.기부금합계_배우자.종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("42".equals(vo.get기부코드())) {
                            response.기부금합계_배우자.우리사주조합기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.우리사주조합기부금 += vo.get기부명세_공제대상기부금();
                        }
                        response.기부금합계_배우자.총계 += vo.get기부명세_공제금액();
                        response.기부금합계_배우자.기부장려금신청금액 += vo.get기부명세_기부장려금();
                        response.기부금합계_배우자.기타 += vo.get기부명세_기타();
                        break;

                    case "4":
                    case "5":
                        if ("10".equals(vo.get기부코드())) {
                            response.기부금합계_직계비속.법정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.법정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("20".equals(vo.get기부코드())) {
                            response.기부금합계_직계비속.정치자금기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.정치자금기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("40".equals(vo.get기부코드())) {
                            response.기부금합계_직계비속.종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("41".equals(vo.get기부코드())) {
                            response.기부금합계_직계비속.종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("42".equals(vo.get기부코드())) {
                            response.기부금합계_직계비속.우리사주조합기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.우리사주조합기부금 += vo.get기부명세_공제대상기부금();
                        }
                        response.기부금합계_직계비속.총계 += vo.get기부명세_공제금액();
                        response.기부금합계_직계비속.기부장려금신청금액 += vo.get기부명세_기부장려금();
                        response.기부금합계_직계비속.기타 += vo.get기부명세_기타();
                        break;

                    case "1":
                    case "2":
                        if ("10".equals(vo.get기부코드())) {
                            response.기부금합계_직계존속.법정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.법정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("20".equals(vo.get기부코드())) {
                            response.기부금합계_직계존속.정치자금기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.정치자금기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("40".equals(vo.get기부코드())) {
                            response.기부금합계_직계존속.종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("41".equals(vo.get기부코드())) {
                            response.기부금합계_직계존속.종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("42".equals(vo.get기부코드())) {
                            response.기부금합계_직계존속.우리사주조합기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.우리사주조합기부금 += vo.get기부명세_공제대상기부금();
                        }
                        response.기부금합계_직계존속.총계 += vo.get기부명세_공제금액();
                        response.기부금합계_직계존속.기부장려금신청금액 += vo.get기부명세_기부장려금();
                        response.기부금합계_직계존속.기타 += vo.get기부명세_기타();
                        break;

                    case "6":
                        if ("10".equals(vo.get기부코드())) {
                            response.기부금합계_형제자매.법정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.법정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("20".equals(vo.get기부코드())) {
                            response.기부금합계_형제자매.정치자금기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.정치자금기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("40".equals(vo.get기부코드())) {
                            response.기부금합계_형제자매.종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("41".equals(vo.get기부코드())) {
                            response.기부금합계_형제자매.종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("42".equals(vo.get기부코드())) {
                            response.기부금합계_형제자매.우리사주조합기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.우리사주조합기부금 += vo.get기부명세_공제대상기부금();
                        }
                        response.기부금합계_형제자매.총계 += vo.get기부명세_공제금액();
                        response.기부금합계_형제자매.기부장려금신청금액 += vo.get기부명세_기부장려금();
                        response.기부금합계_형제자매.기타 += vo.get기부명세_기타();
                        break;

                    case "7":
                    case "8":
                        if ("10".equals(vo.get기부코드())) {
                            response.기부금합계_그외.법정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.법정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("20".equals(vo.get기부코드())) {
                            response.기부금합계_그외.정치자금기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.정치자금기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("40".equals(vo.get기부코드())) {
                            response.기부금합계_그외.종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("41".equals(vo.get기부코드())) {
                            response.기부금합계_그외.종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("42".equals(vo.get기부코드())) {
                            response.기부금합계_그외.우리사주조합기부금 += vo.get기부명세_공제대상기부금();
                            response.기부금합계_합계.우리사주조합기부금 += vo.get기부명세_공제대상기부금();
                        }
                        response.기부금합계_그외.총계 += vo.get기부명세_공제금액();
                        response.기부금합계_그외.기부장려금신청금액 += vo.get기부명세_기부장려금();
                        response.기부금합계_그외.기타 += vo.get기부명세_기타();
                        break;
                }

                response.기부금합계_합계.총계 += vo.get기부명세_공제금액();
                response.기부금합계_합계.기부장려금신청금액 += vo.get기부명세_기부장려금();
                response.기부금합계_합계.기타 += vo.get기부명세_기타();
            }
        }
        if (listYE405.size() > 0) {
            response.이월기부금 = listYE405;
        }
        
        if (listYE408.size() > 0) {
            response.기부금_조정명세 = listYE408;
        }

        YE900VO ye900Result;

        response.보험료_정정사유.set계약ID(계약ID);
        response.보험료_정정사유.set사용자ID(사용자ID);
        response.보험료_정정사유.set사유구분("W711");
        ye900Result = ye900DAO.getYE900(response.보험료_정정사유);
        if (ye900Result != null) {
            response.보험료_정정사유 = ye900Result;
        }

        response.의료비_정정사유.set계약ID(계약ID);
        response.의료비_정정사유.set사용자ID(사용자ID);
        response.의료비_정정사유.set사유구분("W712");
        ye900Result = ye900DAO.getYE900(response.의료비_정정사유);
        if (ye900Result != null) {
            response.의료비_정정사유 = ye900Result;
        }

        response.교육비_정정사유.set계약ID(계약ID);
        response.교육비_정정사유.set사용자ID(사용자ID);
        response.교육비_정정사유.set사유구분("W713");
        ye900Result = ye900DAO.getYE900(response.교육비_정정사유);
        if (ye900Result != null) {
            response.교육비_정정사유 = ye900Result;
        }

        response.기부금_정정사유.set계약ID(계약ID);
        response.기부금_정정사유.set사용자ID(사용자ID);
        response.기부금_정정사유.set사유구분("W714");
        ye900Result = ye900DAO.getYE900(response.기부금_정정사유);
        if (ye900Result != null) {
            response.기부금_정정사유 = ye900Result;
        }
    }

    @Override
    public void saveYW710(String bizId, String 작업자ID, YW710Request request, int userType) {
        if (request.보험료 != null) {
            for (YE401VO vo : request.보험료) {
                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye401DAO.updYE401Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye401DAO.updYE401Disable(vo);
                    ye401DAO.insYE401(vo);
                }
            }
        }

        if (request.의료비 != null) {
            for (YE402VO vo : request.의료비) {
                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye402DAO.updYE402Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye402DAO.updYE402Disable(vo);
                    ye402DAO.insYE402(vo);
                }
            }
        }

        if (request.교육비 != null) {
            for (YE403VO vo : request.교육비) {
                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye403DAO.updYE403Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye403DAO.updYE403Disable(vo);
                    ye403DAO.insYE403(vo);
                }
            }
        }

        String 계약년도 = "";
        YS000VO ys000 = new YS000VO();
        ys000.setBizId(bizId);
        ys000.set계약ID(request.계약ID);
        List<YS000VO> listYS000 = ys000DAO.getYS000List(ys000);
        if (listYS000.size() > 0) {
            계약년도 = listYS000.get(0).getFebYear();
        }

        // 기부금 조정명세가 405 -> 408로 변경
//        YE405VO ye405 = new YE405VO();
//        ye405.set계약ID(request.계약ID);
//        ye405.set사용자ID(request.사용자ID);
//        ye405.set기부년도(계약년도);
//        // 당해년도 기부금 조정명세 삭제
//        ye405DAO.allDelYE405(ye405);
        
        YE408VO ye408 = new YE408VO();
        ye408.set계약ID(request.계약ID);
        ye408.set사용자ID(request.사용자ID);
        ye408.set기부년도(계약년도);
        // 당해년도 기부금 조정명세 삭제
//        ye408DAO.allDelYE408(ye408);

        if (request.이월기부금 != null) {
            for (YE405VO vo : request.이월기부금) {
                vo.set작업자ID(작업자ID);
                if ("D".equals(vo.getDbMode())) {
                    ye405DAO.updYE405Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye405DAO.updYE405Disable(vo);
                    ye405DAO.insYE405(vo);
                }
            }
        }

        if (request.기부금 != null) {
            for (YE404VO vo : request.기부금) {
                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye404DAO.updYE404Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye404DAO.updYE404Disable(vo);
                    ye404DAO.insYE404(vo);
                }
            }
//
//            if (request.기부금.size() > 0 || request.이월기부금.size() > 0 ) {
//                ye408.set작업자ID(작업자ID);
//                // 당해년도 기부금 조정명세 추가
//                ye408DAO.insYE408Sum(ye408);
//            }
        }


        if (request.기부금_조정명세 != null) {
            for (YE408VO vo : request.기부금_조정명세) {
                vo.set작업자ID(작업자ID);

                if ("D".equals(vo.getDbMode())) {
                    ye408DAO.updYE408Disable(vo);
                } else if ("C".equals(vo.getDbMode()) || "U".equals(vo.getDbMode())) {
                    ye408DAO.updYE408Disable(vo);
                    ye408DAO.insYE408Data(vo);
                }
            }
        }

        if (StringUtil.isNotNull(request.계약ID) && StringUtil.isNotNull(request.사용자ID)) {
        	
        	// 중간관리자 이상
        	if(userType >= 5) {
        		// 근로자 진행상태코드 수정
            	YE000VO ye000 = new YE000VO();
            	ye000.set계약ID(request.계약ID);
            	ye000.set사용자ID(request.사용자ID);
            	ye000.set진행상태코드("2");
            	ye000DAO.updYE000(ye000);
        	}
        	
        	YE901VO ye901 = new YE901VO();
        	ye901.set계약ID(request.계약ID);
        	ye901.set사용자ID(request.사용자ID);
        	ye901.set작업자ID(작업자ID);
        	ye901.set진행현황코드("2");
        	ye901DAO.insYE901(ye901);
        }

        if (request.보험료_정정사유 != null) {
            saveYE900(작업자ID, "W711", request.보험료_정정사유);
        }
        if (request.의료비_정정사유 != null) {
            saveYE900(작업자ID, "W712", request.의료비_정정사유);
        }
        if (request.교육비_정정사유 != null) {
            saveYE900(작업자ID, "W713", request.교육비_정정사유);
        }
        if (request.기부금_정정사유 != null) {
            saveYE900(작업자ID, "W714", request.기부금_정정사유);
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

    @Override
    public void putYW710(String bizId, String 작업자ID, YW710Request request) {

        String 계약년도 = "";
        YS000VO ys000 = new YS000VO();
        ys000.setBizId(bizId);
        ys000.set계약ID(request.계약ID);
        List<YS000VO> listYS000 = ys000DAO.getYS000List(ys000);
        if (listYS000.size() > 0) {
            계약년도 = listYS000.get(0).getFebYear();
        }
        
        YE408VO ye408 = new YE408VO();
        ye408.set계약ID(request.계약ID);
        ye408.set사용자ID(request.사용자ID);
        ye408.set작업자ID(작업자ID);
        ye408.set기부년도(계약년도);
        // 모든 기부금 조정명세 삭제
        ye408DAO.allDelYE408(ye408);
        
        List<YE408VO> listYE408 = ye408DAO.getinsYE408(ye408);
        ye408DAO.insYE408(ye408);        
    }
    
    // 특별세액공제 의료비계산결과 조회
	@Override
	public void getMedicalYW710(String 계약ID, String 사용자ID, String 세액공제구분코드, YW710MedicalResponse response) {
		YE409VO vo = new YE409VO();
        vo.set계약ID(계약ID);
        vo.set사용자ID(사용자ID);
        vo.set세액공제구분코드(세액공제구분코드);
        response.의료비계산결과 = ye409DAO.getYE409List(vo);
	}    
}
