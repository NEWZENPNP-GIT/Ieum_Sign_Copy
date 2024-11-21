package com.ezsign.window.service.impl;

import com.ezsign.feb.etc.dao.YE502DAO;
import com.ezsign.feb.etc.dao.YE503DAO;
import com.ezsign.feb.etc.vo.YE502VO;
import com.ezsign.feb.etc.vo.YE503VO;
import com.ezsign.feb.house.dao.*;
import com.ezsign.feb.house.vo.*;
import com.ezsign.feb.master.dao.YE900DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE900VO;
import com.ezsign.feb.other.dao.YE201DAO;
import com.ezsign.feb.other.dao.YE202DAO;
import com.ezsign.feb.other.dao.YE203DAO;
import com.ezsign.feb.other.dao.YE204DAO;
import com.ezsign.feb.other.vo.YE201VO;
import com.ezsign.feb.other.vo.YE202VO;
import com.ezsign.feb.other.vo.YE203VO;
import com.ezsign.feb.other.vo.YE204VO;
import com.ezsign.feb.pension.dao.YE301DAO;
import com.ezsign.feb.pension.dao.YE302DAO;
import com.ezsign.feb.pension.vo.YE301VO;
import com.ezsign.feb.pension.vo.YE302VO;
import com.ezsign.feb.special.dao.*;
import com.ezsign.feb.special.vo.*;
import com.ezsign.feb.worker.dao.YE002DAO;
import com.ezsign.feb.worker.dao.YE003DAO;
import com.ezsign.feb.worker.dao.YE051DAO;
import com.ezsign.feb.worker.dao.YE052DAO;
import com.ezsign.feb.worker.vo.YE002VO;
import com.ezsign.feb.worker.vo.YE051VO;
import com.ezsign.feb.worker.vo.YE052VO;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.window.CodeUtils;
import com.ezsign.window.service.YW500Service;
import com.ezsign.window.vo.YW500Response;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
@Service("yw500Service")
public class YW500ServiceImpl extends AbstractServiceImpl implements YW500Service {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "ye002DAO")
    private YE002DAO ye002DAO;

    @Resource(name = "ye003DAO")
    private YE003DAO ye003DAO;

    @Resource(name = "ye051DAO")
    private YE051DAO ye051DAO;

    @Resource(name = "ye710DAO")
    private YE710DAO ye710DAO;

    @Resource(name = "ye052DAO")
    private YE052DAO ye052DAO;

    @Resource(name = "ye101DAO")
    private YE101DAO ye101DAO;

    @Resource(name = "ye104DAO")
    private YE104DAO ye104DAO;

    @Resource(name = "ye106DAO")
    private YE106DAO ye106DAO;

    @Resource(name = "ye107DAO")
    private YE107DAO ye107DAO;

    @Resource(name = "ye108DAO")
    private YE108DAO ye108DAO;

    @Resource(name = "ye109DAO")
    private YE109DAO ye109DAO;

    @Resource(name = "ye405DAO")
    private YE405DAO ye405DAO;


    @Resource(name = "ye201DAO")
    private YE201DAO ye201DAO;

    @Resource(name = "ye202DAO")
    private YE202DAO ye202DAO;

    @Resource(name = "ye203DAO")
    private YE203DAO ye203DAO;

    @Resource(name = "ye204DAO")
    private YE204DAO ye204DAO;

    @Resource(name = "ye301DAO")
    private YE301DAO ye301DAO;

    @Resource(name = "ye302DAO")
    private YE302DAO ye302DAO;

    @Resource(name = "ye401DAO")
    private YE401DAO ye401DAO;

    @Resource(name = "ye402DAO")
    private YE402DAO ye402DAO;

    @Resource(name = "ye403DAO")
    private YE403DAO ye403DAO;

    @Resource(name = "ye404DAO")
    private YE404DAO ye404DAO;

    @Resource(name = "ye406DAO")
    private YE406DAO ye406DAO;

    @Resource(name = "ye502DAO")
    private YE502DAO ye502DAO;

    @Resource(name = "ye503DAO")
    private YE503DAO ye503DAO;

    @Resource(name = "ye105DAO")
    private YE105DAO ye105DAO;

    @Resource(name = "ye900DAO")
    private YE900DAO ye900DAO;

    @Override
    public void getYW500(String bizId, String 계약ID, String 사용자ID, YW500Response response) throws Exception {
        get설문지(계약ID, 사용자ID, response);

        get정정사유(계약ID, 사용자ID, response);

        get회사자료(계약ID, 사용자ID, response);

        get연금보험료공제(계약ID, 사용자ID, response);

        get특별소득공제_보험료(계약ID, 사용자ID, response);
        get특별소득공제_주택임차차입금원리금상환액(계약ID, 사용자ID, response);
        get특별소득공제_장기주택저당차입금이자상환액(계약ID, 사용자ID, response);

        get특별소득공제_기부금이월분(계약ID, 사용자ID, response);

        get개인연금저축(계약ID, 사용자ID, response);
        get주택마련저축(계약ID, 사용자ID, response);
        get신용카드(계약ID, 사용자ID, response);
        get장기집합투자증권저축(계약ID, 사용자ID, response);

        get그밖의소득공제_소기업소상공인공제부금(계약ID, 사용자ID, response);
        get그밖의소득공제_투자조합출자등(계약ID, 사용자ID, response);
        get그밖의소득공제_우리사주조합출연금(계약ID, 사용자ID, response);
        get그밖의소득공제_고용유지중소기업근로자(계약ID, 사용자ID, response);

        get연금계좌_퇴직연금(계약ID, 사용자ID, response);
        get연금계좌_연금저축(계약ID, 사용자ID, response);

        get특별세액공제_보험료(계약ID, 사용자ID, response);
        get특별세액공제_의료비안경구입비(계약ID, 사용자ID, response);
        get특별세액공제_교육비교복구입비체험학습비(계약ID, 사용자ID, response);
        get특별세액공제_기부금(계약ID, 사용자ID, response);

        get세액감면(bizId, 계약ID, 사용자ID, response);
        get주택차입금(계약ID, 사용자ID, response);
        get외국납부세액(계약ID, 사용자ID, response);
        get월세액(계약ID, 사용자ID, response);
    }

    private void get설문지(String 계약ID, String 사용자ID, YW500Response response) {
        YE710VO ye710 = new YE710VO();
        ye710.set계약ID(계약ID);
        ye710.set사용자ID(사용자ID);
        YE710VO ye710Result = ye710DAO.getYE710(ye710);
        if (ye710Result != null) {
            response.설문지_주택마련저축_입력여부 = "1".equals(ye710Result.get주택관련저축_입력여부());
            response.설문지_주택마련저축_적용여부 = "1".equals(ye710Result.get주택관련저축_적용여부());
            response.설문지_주택임차차입금_입력여부 = "1".equals(ye710Result.get주택임차차입금_입력여부());
            response.설문지_주택임차차입금_적용여부 = "1".equals(ye710Result.get주택임차차입금_적용여부());
            response.설문지_장기주택저당차입금_입력여부 = "1".equals(ye710Result.get장기주택저당차입금_입력여부());
            response.설문지_장기주택저당차입금_적용여부 = "1".equals(ye710Result.get장기주택저당차입금_적용여부());
            response.설문지_월세액공제_입력여부 = "1".equals(ye710Result.get월세공제_입력여부());
            response.설문지_월세액공제_적용여부 = "1".equals(ye710Result.get월세공제_적용여부());
        }
    }

    private void get회사자료(String 계약ID, String 사용자ID, YW500Response response) throws Exception {
        YE000VO ye000 = new YE000VO();
        ye000.set계약ID(계약ID);
        ye000.set사용자ID(사용자ID);
        List<YE000VO> listSum = ye003DAO.getYE003SumList(ye000);

        for (YE000VO vo : listSum) {
            response.근로자_국세청자료.연금보험료공제_국민연금보험료 += StringUtil.strPaserInt(vo.get국민연금보험료());
            response.근로자_국세청자료.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get공무원연금());
            response.근로자_국세청자료.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get군인연금());
            response.근로자_국세청자료.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get사립학교교직원연금());
            response.근로자_국세청자료.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get별정우체국연금());

            response.근로자_합계.연금보험료공제_국민연금보험료 += StringUtil.strPaserInt(vo.get국민연금보험료());
            response.근로자_합계.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get공무원연금());
            response.근로자_합계.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get군인연금());
            response.근로자_합계.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get사립학교교직원연금());
            response.근로자_합계.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get별정우체국연금());

            response.근로자_국세청자료.연금보험료공제 += StringUtil.strPaserInt(vo.get국민연금보험료());
            response.근로자_국세청자료.연금보험료공제 += StringUtil.strPaserInt(vo.get공무원연금());
            response.근로자_국세청자료.연금보험료공제 += StringUtil.strPaserInt(vo.get군인연금());
            response.근로자_국세청자료.연금보험료공제 += StringUtil.strPaserInt(vo.get사립학교교직원연금());
            response.근로자_국세청자료.연금보험료공제 += StringUtil.strPaserInt(vo.get별정우체국연금());

            response.근로자_합계.연금보험료공제 += StringUtil.strPaserInt(vo.get국민연금보험료());
            response.근로자_합계.연금보험료공제 += StringUtil.strPaserInt(vo.get공무원연금());
            response.근로자_합계.연금보험료공제 += StringUtil.strPaserInt(vo.get군인연금());
            response.근로자_합계.연금보험료공제 += StringUtil.strPaserInt(vo.get사립학교교직원연금());
            response.근로자_합계.연금보험료공제 += StringUtil.strPaserInt(vo.get별정우체국연금());

            response.근로자_국세청자료.특별소득공제_국민건강보험료 += StringUtil.strPaserInt(vo.get건강보험료());
            response.근로자_국세청자료.특별소득공제_장기요양보험료 += StringUtil.strPaserInt(vo.get장기요양보험료());
            response.근로자_국세청자료.특별소득공제_고용보험료 += StringUtil.strPaserInt(vo.get고용보험료());

            response.근로자_합계.특별소득공제_국민건강보험료 += StringUtil.strPaserInt(vo.get건강보험료());
            response.근로자_합계.특별소득공제_장기요양보험료 += StringUtil.strPaserInt(vo.get장기요양보험료());
            response.근로자_합계.특별소득공제_고용보험료 += StringUtil.strPaserInt(vo.get고용보험료());

            response.근로자_국세청자료.특별소득공제 += StringUtil.strPaserInt(vo.get건강보험료());
            response.근로자_국세청자료.특별소득공제 += StringUtil.strPaserInt(vo.get장기요양보험료());
            response.근로자_국세청자료.특별소득공제 += StringUtil.strPaserInt(vo.get고용보험료());

            response.근로자_합계.특별소득공제 += StringUtil.strPaserInt(vo.get건강보험료());
            response.근로자_합계.특별소득공제 += StringUtil.strPaserInt(vo.get장기요양보험료());
            response.근로자_합계.특별소득공제 += StringUtil.strPaserInt(vo.get고용보험료());

            response.관리자_국세청자료.연금보험료공제_국민연금보험료 += StringUtil.strPaserInt(vo.get국민연금보험료());
            response.관리자_국세청자료.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get공무원연금());
            response.관리자_국세청자료.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get군인연금());
            response.관리자_국세청자료.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get사립학교교직원연금());
            response.관리자_국세청자료.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get별정우체국연금());

            response.관리자_합계.연금보험료공제_국민연금보험료 += StringUtil.strPaserInt(vo.get국민연금보험료());
            response.관리자_합계.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get공무원연금());
            response.관리자_합계.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get군인연금());
            response.관리자_합계.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get사립학교교직원연금());
            response.관리자_합계.연금보험료공제_공적연금보험료 += StringUtil.strPaserInt(vo.get별정우체국연금());

            response.관리자_국세청자료.연금보험료공제 += StringUtil.strPaserInt(vo.get국민연금보험료());
            response.관리자_국세청자료.연금보험료공제 += StringUtil.strPaserInt(vo.get공무원연금());
            response.관리자_국세청자료.연금보험료공제 += StringUtil.strPaserInt(vo.get군인연금());
            response.관리자_국세청자료.연금보험료공제 += StringUtil.strPaserInt(vo.get사립학교교직원연금());
            response.관리자_국세청자료.연금보험료공제 += StringUtil.strPaserInt(vo.get별정우체국연금());

            response.관리자_합계.연금보험료공제 += StringUtil.strPaserInt(vo.get국민연금보험료());
            response.관리자_합계.연금보험료공제 += StringUtil.strPaserInt(vo.get공무원연금());
            response.관리자_합계.연금보험료공제 += StringUtil.strPaserInt(vo.get군인연금());
            response.관리자_합계.연금보험료공제 += StringUtil.strPaserInt(vo.get사립학교교직원연금());
            response.관리자_합계.연금보험료공제 += StringUtil.strPaserInt(vo.get별정우체국연금());

            response.관리자_국세청자료.특별소득공제_국민건강보험료 += StringUtil.strPaserInt(vo.get건강보험료());
            response.관리자_국세청자료.특별소득공제_장기요양보험료 += StringUtil.strPaserInt(vo.get장기요양보험료());
            response.관리자_국세청자료.특별소득공제_고용보험료 += StringUtil.strPaserInt(vo.get고용보험료());

            response.관리자_합계.특별소득공제_국민건강보험료 += StringUtil.strPaserInt(vo.get건강보험료());
            response.관리자_합계.특별소득공제_장기요양보험료 += StringUtil.strPaserInt(vo.get장기요양보험료());
            response.관리자_합계.특별소득공제_고용보험료 += StringUtil.strPaserInt(vo.get고용보험료());

            response.관리자_국세청자료.특별소득공제 += StringUtil.strPaserInt(vo.get건강보험료());
            response.관리자_국세청자료.특별소득공제 += StringUtil.strPaserInt(vo.get장기요양보험료());
            response.관리자_국세청자료.특별소득공제 += StringUtil.strPaserInt(vo.get고용보험료());

            response.관리자_합계.특별소득공제 += StringUtil.strPaserInt(vo.get건강보험료());
            response.관리자_합계.특별소득공제 += StringUtil.strPaserInt(vo.get장기요양보험료());
            response.관리자_합계.특별소득공제 += StringUtil.strPaserInt(vo.get고용보험료());
        }
    }

    private void get연금보험료공제(String 계약ID, String 사용자ID, YW500Response response) {
        YE051VO ye051 = new YE051VO();
        ye051.set계약ID(계약ID);
        ye051.set사용자ID(사용자ID);
        List<YE051VO> listYE051 = ye051DAO.getYE051Map(ye051);

        if (listYE051.size() > 0) {
            // 국세청 자료는 사용하지 않고 회사자료만 사용
            int 기타;
            for (YE051VO vo : listYE051) {
                기타 = vo.get추가납입금액();

                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    if ("1".equals(vo.get보험료구분())) {
                        // 국민연금보험료
                        response.근로자_기타자료.연금보험료공제_국민연금보험료 += 기타;
                        response.근로자_합계.연금보험료공제_국민연금보험료 += 기타;
                    } else {
                        // 공적연금보험료
                        response.근로자_기타자료.연금보험료공제_공적연금보험료 += 기타;
                        response.근로자_합계.연금보험료공제_공적연금보험료 += 기타;
                    }

                    response.근로자_기타자료.연금보험료공제 += 기타;
                    response.근로자_합계.연금보험료공제 += 기타;
                }

                if ("1".equals(vo.get사용여부())) {
                    if ("1".equals(vo.get보험료구분())) {
                        // 국민연금보험료
                        response.관리자_기타자료.연금보험료공제_국민연금보험료 += 기타;
                        response.관리자_합계.연금보험료공제_국민연금보험료 += 기타;
                    } else {
                        // 공적연금보험료
                        response.관리자_기타자료.연금보험료공제_공적연금보험료 += 기타;
                        response.관리자_합계.연금보험료공제_공적연금보험료 += 기타;
                    }

                    response.관리자_기타자료.연금보험료공제 += 기타;
                    response.관리자_합계.연금보험료공제 += 기타;
                }
            }
        }
    }

    private void get특별소득공제_보험료(String 계약ID, String 사용자ID, YW500Response response) {
        YE052VO ye052 = new YE052VO();
        ye052.set계약ID(계약ID);
        ye052.set사용자ID(사용자ID);
        List<YE052VO> listYE052 = ye052DAO.getYE052Map(ye052);

        if (listYE052.size() > 0) {
            // 국세청 자료는 사용하지 않고 회사자료만 사용
            int 기타;
            for (YE052VO vo : listYE052) {
                기타 = vo.get추가납입금액();

                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    if ("1".equals(vo.get보험료구분())) {
                        // 국민건강보험료
                        response.근로자_기타자료.특별소득공제_국민건강보험료 += 기타;
                        response.근로자_합계.특별소득공제_국민건강보험료 += 기타;
                    } else if ("2".equals(vo.get보험료구분())) {
                        // 장기요양보험료
                        response.근로자_기타자료.특별소득공제_장기요양보험료 += 기타;
                        response.근로자_합계.특별소득공제_장기요양보험료 += 기타;
                    } else if ("3".equals(vo.get보험료구분())) {
                        // 고용보험료
                        response.근로자_기타자료.특별소득공제_고용보험료 += 기타;
                        response.근로자_합계.특별소득공제_고용보험료 += 기타;
                    }

                    response.근로자_기타자료.특별소득공제 += 기타;
                    response.근로자_합계.특별소득공제 += 기타;
                }

                if ("1".equals(vo.get사용여부())) {
                    if ("1".equals(vo.get보험료구분())) {
                        // 국민건강보험료
                        response.관리자_기타자료.특별소득공제_국민건강보험료 += 기타;
                        response.관리자_합계.특별소득공제_국민건강보험료 += 기타;
                    } else if ("2".equals(vo.get보험료구분())) {
                        // 장기요양보험료
                        response.관리자_기타자료.특별소득공제_장기요양보험료 += 기타;
                        response.관리자_합계.특별소득공제_장기요양보험료 += 기타;
                    } else if ("3".equals(vo.get보험료구분())) {
                        // 고용보험료
                        response.관리자_기타자료.특별소득공제_고용보험료 += 기타;
                        response.관리자_합계.특별소득공제_고용보험료 += 기타;
                    }

                    response.관리자_기타자료.특별소득공제 += 기타;
                    response.관리자_합계.특별소득공제 += 기타;
                }
            }
        }
    }

    private void get특별소득공제_주택임차차입금원리금상환액(String 계약ID, String 사용자ID, YW500Response response) {
        YE101VO ye101 = new YE101VO();
        ye101.set계약ID(계약ID);
        ye101.set사용자ID(사용자ID);
        List<YE101VO> listYE101 = ye101DAO.getYE101Map(ye101);

        if (listYE101.size() > 0) {
            int 국세청;
            int 기타;
            int 합계;
            for (YE101VO vo : listYE101) {
                국세청 = vo.get국세청자료() - vo.get차감금액();
                기타 = vo.get기타자료();
                합계 = 국세청 + 기타;

                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    if ("1".equals(vo.get차입구분())) {
                        // 대출기관
                        response.근로자_국세청자료.특별소득공제_주택임차차입금원리금상환액_대출기관 += 국세청;
                        response.근로자_기타자료.특별소득공제_주택임차차입금원리금상환액_대출기관 += 기타;
                        response.근로자_합계.특별소득공제_주택임차차입금원리금상환액_대출기관 += 합계;
                    } else if ("2".equals(vo.get차입구분())) {
                        // 거주자
                        response.근로자_국세청자료.특별소득공제_주택임차차입금원리금상환액_거주자 += 국세청;
                        response.근로자_기타자료.특별소득공제_주택임차차입금원리금상환액_거주자 += 기타;
                        response.근로자_합계.특별소득공제_주택임차차입금원리금상환액_거주자 += 합계;
                    }

                    response.근로자_국세청자료.특별소득공제_주택임차차입금원리금상환액 += 국세청;
                    response.근로자_기타자료.특별소득공제_주택임차차입금원리금상환액 += 기타;
                    response.근로자_합계.특별소득공제_주택임차차입금원리금상환액 += 합계;

                    response.근로자_국세청자료.특별소득공제 += 국세청;
                    response.근로자_기타자료.특별소득공제 += 기타;
                    response.근로자_합계.특별소득공제 += 합계;
                }

                if ("1".equals(vo.get사용여부())) {
                    if ("1".equals(vo.get차입구분())) {
                        // 대출기관
                        response.관리자_국세청자료.특별소득공제_주택임차차입금원리금상환액_대출기관 += 국세청;
                        response.관리자_기타자료.특별소득공제_주택임차차입금원리금상환액_대출기관 += 기타;
                        response.관리자_합계.특별소득공제_주택임차차입금원리금상환액_대출기관 += 합계;
                    } else {
                        // 거주자
                        response.관리자_국세청자료.특별소득공제_주택임차차입금원리금상환액_거주자 += 국세청;
                        response.관리자_기타자료.특별소득공제_주택임차차입금원리금상환액_거주자 += 기타;
                        response.관리자_합계.특별소득공제_주택임차차입금원리금상환액_거주자 += 합계;
                    }

                    response.관리자_국세청자료.특별소득공제_주택임차차입금원리금상환액 += 국세청;
                    response.관리자_기타자료.특별소득공제_주택임차차입금원리금상환액 += 기타;
                    response.관리자_합계.특별소득공제_주택임차차입금원리금상환액 += 합계;

                    response.관리자_국세청자료.특별소득공제 += 국세청;
                    response.관리자_기타자료.특별소득공제 += 기타;
                    response.관리자_합계.특별소득공제 += 합계;
                }
            }
        }
    }

    private void get특별소득공제_장기주택저당차입금이자상환액(String 계약ID, String 사용자ID, YW500Response response) {
        YE104VO ye104 = new YE104VO();
        ye104.set계약ID(계약ID);
        ye104.set사용자ID(사용자ID);
        List<YE104VO> listYE104 = ye104DAO.getYE104Map(ye104);

        if (listYE104.size() > 0) {
            int 국세청;
            int 기타;
            int 합계;
            for (YE104VO vo : listYE104) {
                국세청 = vo.get국세청자료() - vo.get차감금액();
                기타 = vo.get기타자료();
                합계 = 국세청 + 기타;

                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    response.근로자_국세청자료.특별소득공제_장기주택저당차입금이자상환액 += 국세청;
                    response.근로자_기타자료.특별소득공제_장기주택저당차입금이자상환액 += 기타;
                    response.근로자_합계.특별소득공제_장기주택저당차입금이자상환액 += 합계;

                    response.근로자_국세청자료.특별소득공제 += 국세청;
                    response.근로자_기타자료.특별소득공제 += 기타;
                    response.근로자_합계.특별소득공제 += 합계;
                }

                if ("1".equals(vo.get사용여부())) {
                    response.관리자_국세청자료.특별소득공제_장기주택저당차입금이자상환액 += 국세청;
                    response.관리자_기타자료.특별소득공제_장기주택저당차입금이자상환액 += 기타;
                    response.관리자_합계.특별소득공제_장기주택저당차입금이자상환액 += 합계;

                    response.관리자_국세청자료.특별소득공제 += 국세청;
                    response.관리자_기타자료.특별소득공제 += 기타;
                    response.관리자_합계.특별소득공제 += 합계;
                }
            }
        }
    }

    private void get특별소득공제_기부금이월분(String 계약ID, String 사용자ID, YW500Response response) {
        YE405VO ye405 = new YE405VO();
        ye405.set계약ID(계약ID);
        ye405.set사용자ID(사용자ID);
        List<YE405VO> listYE405 = ye405DAO.getYE405Map(ye405);

        if (listYE405.size() > 0) {
            for (YE405VO vo : listYE405) {
                if (vo.get기부년도().equals(response.근무년월.계약년도)) {
                    continue;
                }
                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    response.근로자_기타자료.특별소득공제_기부금이월분 += vo.get공제대상기부금();
                    response.근로자_합계.특별소득공제_기부금이월분 += vo.get공제대상기부금();

                    response.근로자_기타자료.특별소득공제 += vo.get공제대상기부금();
                    response.근로자_합계.특별소득공제 += vo.get공제대상기부금();
                }

                if ("1".equals(vo.get사용여부())) {
                    response.관리자_기타자료.특별소득공제_기부금이월분 += vo.get공제대상기부금();
                    response.관리자_합계.특별소득공제_기부금이월분 += vo.get공제대상기부금();

                    response.관리자_기타자료.특별소득공제 += vo.get공제대상기부금();
                    response.관리자_합계.특별소득공제 += vo.get공제대상기부금();
                }
            }
        }
    }

    private void get개인연금저축(String 계약ID, String 사용자ID, YW500Response response) {
        YE106VO ye106 = new YE106VO();
        ye106.set계약ID(계약ID);
        ye106.set사용자ID(사용자ID);
        List<YE106VO> listYE106 = ye106DAO.getYE106Map(ye106);

        if (listYE106.size() > 0) {
            int 금액;
            for (YE106VO vo : listYE106) {
                금액 = vo.get납입금액() - vo.get차감금액();

                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        response.근로자_국세청자료.개인연금저축 += 금액;
                    } else {
                        response.근로자_기타자료.개인연금저축 += 금액;
                    }

                    response.근로자_합계.개인연금저축 += 금액;
                }

                if ("1".equals(vo.get사용여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        response.관리자_국세청자료.개인연금저축 += 금액;

                    } else {
                        response.관리자_기타자료.개인연금저축 += 금액;
                    }

                    response.관리자_합계.개인연금저축 += 금액;
                }
            }
        }
    }

    private void get주택마련저축(String 계약ID, String 사용자ID, YW500Response response) {
        YE107VO ye107 = new YE107VO();
        ye107.set계약ID(계약ID);
        ye107.set사용자ID(사용자ID);
        List<YE107VO> listYE107 = ye107DAO.getYE107Map(ye107);

        if (listYE107.size() > 0) {
            int 금액;
            for (YE107VO vo : listYE107) {
                금액 = vo.get납입금액() - vo.get차감금액();

                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        response.근로자_국세청자료.주택마련저축 += 금액;
                    } else {
                        response.근로자_기타자료.주택마련저축 += 금액;
                    }

                    response.근로자_합계.주택마련저축 += 금액;
                }

                if ("1".equals(vo.get사용여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        response.관리자_국세청자료.주택마련저축 += 금액;

                    } else {
                        response.관리자_기타자료.주택마련저축 += 금액;
                    }

                    response.관리자_합계.주택마련저축 += 금액;
                }
            }
        }
    }

    private void get신용카드(String 계약ID, String 사용자ID, YW500Response response) {
        YE108VO ye108 = new YE108VO();
        ye108.set계약ID(계약ID);
        ye108.set사용자ID(사용자ID);
        List<YE108VO> listYE108 = ye108DAO.getYE108Map(ye108);

        if (listYE108.size() > 0) {
            for (YE108VO vo : listYE108) {
                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        response.근로자_국세청자료.신용카드 += vo.get신용카드() + vo.get현금영수증() + vo.get직불_선불카드() + vo.get전통시장() + vo.get대중교통() + vo.get도서공연() - (vo.get신용카드_차감금액() + vo.get현금영수증_차감금액() + vo.get직불_선불카드_차감금액() + vo.get전통시장_차감금액() + vo.get대중교통_차감금액() + vo.get도서공연_차감금액());
                    } else {
                        response.근로자_기타자료.신용카드 += vo.get신용카드() + vo.get현금영수증() + vo.get직불_선불카드() + vo.get전통시장() + vo.get대중교통() + vo.get도서공연() - (vo.get신용카드_차감금액() + vo.get현금영수증_차감금액() + vo.get직불_선불카드_차감금액() + vo.get전통시장_차감금액() + vo.get대중교통_차감금액() + vo.get도서공연_차감금액());
                    }

                    response.근로자_합계.신용카드 += vo.get신용카드() + vo.get현금영수증() + vo.get직불_선불카드() + vo.get전통시장() + vo.get대중교통() + vo.get도서공연() - (vo.get신용카드_차감금액() + vo.get현금영수증_차감금액() + vo.get직불_선불카드_차감금액() + vo.get전통시장_차감금액() + vo.get대중교통_차감금액() + vo.get도서공연_차감금액());
                }

                if ("1".equals(vo.get사용여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        response.관리자_국세청자료.신용카드 += vo.get신용카드() + vo.get현금영수증() + vo.get직불_선불카드() + vo.get전통시장() + vo.get대중교통() + vo.get도서공연() - (vo.get신용카드_차감금액() + vo.get현금영수증_차감금액() + vo.get직불_선불카드_차감금액() + vo.get전통시장_차감금액() + vo.get대중교통_차감금액() + vo.get도서공연_차감금액());

                    } else {
                        response.관리자_기타자료.신용카드 += vo.get신용카드() + vo.get현금영수증() + vo.get직불_선불카드() + vo.get전통시장() + vo.get대중교통() + vo.get도서공연() - (vo.get신용카드_차감금액() + vo.get현금영수증_차감금액() + vo.get직불_선불카드_차감금액() + vo.get전통시장_차감금액() + vo.get대중교통_차감금액() + vo.get도서공연_차감금액());
                    }

                    response.관리자_합계.신용카드 += vo.get신용카드() + vo.get현금영수증() + vo.get직불_선불카드() + vo.get전통시장() + vo.get대중교통() + vo.get도서공연() - (vo.get신용카드_차감금액() + vo.get현금영수증_차감금액() + vo.get직불_선불카드_차감금액() + vo.get전통시장_차감금액() + vo.get대중교통_차감금액() + vo.get도서공연_차감금액());
                }
            }
        }
    }

    private void get장기집합투자증권저축(String 계약ID, String 사용자ID, YW500Response response) {
        YE109VO ye109 = new YE109VO();
        ye109.set계약ID(계약ID);
        ye109.set사용자ID(사용자ID);
        List<YE109VO> listYE109 = ye109DAO.getYE109Map(ye109);

        if (listYE109.size() > 0) {
            int 금액;
            for (YE109VO vo : listYE109) {
                금액 = vo.get납입금액() - vo.get차감금액();

                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        response.근로자_국세청자료.장기집합투자증권저축 += 금액;
                    } else {
                        response.근로자_기타자료.장기집합투자증권저축 += 금액;
                    }

                    response.근로자_합계.장기집합투자증권저축 += 금액;
                }

                if ("1".equals(vo.get사용여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        response.관리자_국세청자료.장기집합투자증권저축 += 금액;

                    } else {
                        response.관리자_기타자료.장기집합투자증권저축 += 금액;
                    }

                    response.관리자_합계.장기집합투자증권저축 += 금액;
                }
            }
        }
    }

    private void get그밖의소득공제_소기업소상공인공제부금(String 계약ID, String 사용자ID, YW500Response response) {
        YE201VO ye201 = new YE201VO();
        ye201.set계약ID(계약ID);
        ye201.set사용자ID(사용자ID);
        List<YE201VO> listYE201 = ye201DAO.getYE201Map(ye201);

        if (listYE201.size() > 0) {
            int 국세청;
            for (YE201VO vo : listYE201) {
                국세청 = vo.get국세청_납입금액() - vo.get국세청_차감금액();

                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    response.근로자_국세청자료.그밖의소득공제_소기업소상공인공제부금 += 국세청;
                    response.근로자_기타자료.그밖의소득공제_소기업소상공인공제부금 += vo.get기타_납입금액();
                    response.근로자_합계.그밖의소득공제_소기업소상공인공제부금 += 국세청 + vo.get기타_납입금액();

                    response.근로자_국세청자료.그밖의소득공제 += 국세청;
                    response.근로자_기타자료.그밖의소득공제 += vo.get기타_납입금액();
                    response.근로자_합계.그밖의소득공제 += 국세청 + vo.get기타_납입금액();
                }

                if ("1".equals(vo.get사용여부())) {
                    response.관리자_국세청자료.그밖의소득공제_소기업소상공인공제부금 += 국세청;
                    response.관리자_기타자료.그밖의소득공제_소기업소상공인공제부금 += vo.get기타_납입금액();
                    response.관리자_합계.그밖의소득공제_소기업소상공인공제부금 += 국세청 + vo.get기타_납입금액();

                    response.관리자_국세청자료.그밖의소득공제 += 국세청;
                    response.관리자_기타자료.그밖의소득공제 += vo.get기타_납입금액();
                    response.관리자_합계.그밖의소득공제 += 국세청 + vo.get기타_납입금액();
                }
            }
        }
    }

    private void get그밖의소득공제_투자조합출자등(String 계약ID, String 사용자ID, YW500Response response) {
        YE202VO ye202 = new YE202VO();
        ye202.set계약ID(계약ID);
        ye202.set사용자ID(사용자ID);
        List<YE202VO> listYE202 = ye202DAO.getYE202Map(ye202);

        if (listYE202.size() > 0) {
            for (YE202VO vo : listYE202) {
                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    response.근로자_기타자료.그밖의소득공제_투자조합출자등 += vo.get납입금액();
                    response.근로자_합계.그밖의소득공제_투자조합출자등 += vo.get납입금액();

                    response.근로자_기타자료.그밖의소득공제 += vo.get납입금액();
                    response.근로자_합계.그밖의소득공제 += vo.get납입금액();
                }

                if ("1".equals(vo.get사용여부())) {
                    response.관리자_기타자료.그밖의소득공제_투자조합출자등 += vo.get납입금액();
                    response.관리자_합계.그밖의소득공제_투자조합출자등 += vo.get납입금액();

                    response.관리자_기타자료.그밖의소득공제 += vo.get납입금액();
                    response.관리자_합계.그밖의소득공제 += vo.get납입금액();
                }
            }
        }
    }

    private void get그밖의소득공제_우리사주조합출연금(String 계약ID, String 사용자ID, YW500Response response) {
        YE203VO ye203 = new YE203VO();
        ye203.set계약ID(계약ID);
        ye203.set사용자ID(사용자ID);
        List<YE203VO> listYE203 = ye203DAO.getYE203Map(ye203);

        if (listYE203.size() > 0) {
            for (YE203VO vo : listYE203) {
                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    response.근로자_기타자료.그밖의소득공제_우리사주조합출연금 += vo.get납입금액();
                    response.근로자_합계.그밖의소득공제_우리사주조합출연금 += vo.get납입금액();

                    response.근로자_기타자료.그밖의소득공제 += vo.get납입금액();
                    response.근로자_합계.그밖의소득공제 += vo.get납입금액();
                }

                if ("1".equals(vo.get사용여부())) {
                    response.관리자_기타자료.그밖의소득공제_우리사주조합출연금 += vo.get납입금액();
                    response.관리자_합계.그밖의소득공제_우리사주조합출연금 += vo.get납입금액();

                    response.관리자_기타자료.그밖의소득공제 += vo.get납입금액();
                    response.관리자_합계.그밖의소득공제 += vo.get납입금액();
                }
            }
        }
    }

    private void get그밖의소득공제_고용유지중소기업근로자(String 계약ID, String 사용자ID, YW500Response response) {
        YE204VO ye204 = new YE204VO();
        ye204.set계약ID(계약ID);
        ye204.set사용자ID(사용자ID);
        List<YE204VO> listYE204 = ye204DAO.getYE204Map(ye204);

        if (listYE204.size() > 0) {
            for (YE204VO vo : listYE204) {
                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    response.근로자_기타자료.그밖의소득공제_고용유지중소기업근로자 += vo.get납입금액();
                    response.근로자_합계.그밖의소득공제_고용유지중소기업근로자 += vo.get납입금액();

                    response.근로자_기타자료.그밖의소득공제 += vo.get납입금액();
                    response.근로자_합계.그밖의소득공제 += vo.get납입금액();
                }

                if ("1".equals(vo.get사용여부())) {
                    response.관리자_기타자료.그밖의소득공제_고용유지중소기업근로자 += vo.get납입금액();
                    response.관리자_합계.그밖의소득공제_고용유지중소기업근로자 += vo.get납입금액();

                    response.관리자_기타자료.그밖의소득공제 += vo.get납입금액();
                    response.관리자_합계.그밖의소득공제 += vo.get납입금액();
                }
            }
        }
    }

    private void get연금계좌_퇴직연금(String 계약ID, String 사용자ID, YW500Response response) {
        YE301VO ye301 = new YE301VO();
        ye301.set계약ID(계약ID);
        ye301.set사용자ID(사용자ID);
        List<YE301VO> listYE301 = ye301DAO.getYE301Map(ye301);

        if (listYE301.size() > 0) {
            int 금액;
            for (YE301VO vo : listYE301) {
                금액 = vo.get납입금액() - vo.get차감금액();

                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        if ("11".equals(vo.get퇴직연금구분코드())) {
                            response.근로자_국세청자료.연금계좌_퇴직연금 += 금액;
                        } else if ("12".equals(vo.get퇴직연금구분코드())) {
                            response.근로자_국세청자료.연금계좌_과학기술인공제 += 금액;
                        }
                        response.근로자_국세청자료.연금계좌 += 금액;
                    } else {
                        if ("11".equals(vo.get퇴직연금구분코드())) {
                            response.근로자_기타자료.연금계좌_퇴직연금 += 금액;
                        } else if ("12".equals(vo.get퇴직연금구분코드())) {
                            response.근로자_기타자료.연금계좌_과학기술인공제 += 금액;
                        }
                        response.근로자_기타자료.연금계좌 += 금액;
                    }

                    if ("11".equals(vo.get퇴직연금구분코드())) {
                        response.근로자_합계.연금계좌_퇴직연금 += 금액;
                    } else if ("12".equals(vo.get퇴직연금구분코드())) {
                        response.근로자_합계.연금계좌_과학기술인공제 += 금액;
                    }
                    response.근로자_합계.연금계좌 += 금액;
                }

                if ("1".equals(vo.get사용여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        if ("11".equals(vo.get퇴직연금구분코드())) {
                            response.관리자_국세청자료.연금계좌_퇴직연금 += 금액;
                        } else if ("12".equals(vo.get퇴직연금구분코드())) {
                            response.관리자_국세청자료.연금계좌_과학기술인공제 += 금액;
                        }
                        response.관리자_국세청자료.연금계좌 += 금액;
                    } else {
                        if ("11".equals(vo.get퇴직연금구분코드())) {
                            response.관리자_기타자료.연금계좌_퇴직연금 += 금액;
                        } else if ("12".equals(vo.get퇴직연금구분코드())) {
                            response.관리자_기타자료.연금계좌_과학기술인공제 += 금액;
                        }
                        response.관리자_기타자료.연금계좌 += 금액;
                    }

                    if ("11".equals(vo.get퇴직연금구분코드())) {
                        response.관리자_합계.연금계좌_퇴직연금 += 금액;
                    } else if ("12".equals(vo.get퇴직연금구분코드())) {
                        response.관리자_합계.연금계좌_과학기술인공제 += 금액;
                    }
                    response.관리자_합계.연금계좌 += 금액;
                }
            }
        }
    }

    private void get연금계좌_연금저축(String 계약ID, String 사용자ID, YW500Response response) {
        YE302VO ye302 = new YE302VO();
        ye302.set계약ID(계약ID);
        ye302.set사용자ID(사용자ID);
        List<YE302VO> listYE302 = ye302DAO.getYE302Map(ye302);

        if (listYE302.size() > 0) {
            int 금액;
            for (YE302VO vo : listYE302) {
                금액 = vo.get납입금액() - vo.get차감금액();

                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        response.근로자_국세청자료.연금계좌_연금저축 += 금액;
                        response.근로자_국세청자료.연금계좌 += 금액;
                    } else {
                        response.근로자_기타자료.연금계좌_연금저축 += 금액;
                        response.근로자_기타자료.연금계좌 += 금액;
                    }

                    response.근로자_합계.연금계좌_연금저축 += 금액;
                    response.근로자_합계.연금계좌 += 금액;
                }

                if ("1".equals(vo.get사용여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        response.관리자_국세청자료.연금계좌_연금저축 += 금액;
                        response.관리자_국세청자료.연금계좌 += 금액;

                    } else {
                        response.관리자_기타자료.연금계좌_연금저축 += 금액;
                        response.관리자_기타자료.연금계좌 += 금액;
                    }

                    response.관리자_합계.연금계좌_연금저축 += 금액;
                    response.관리자_합계.연금계좌 += 금액;
                }
            }
        }
    }

    private void get특별세액공제_보험료(String 계약ID, String 사용자ID, YW500Response response) {
        YE401VO ye401 = new YE401VO();
        ye401.set계약ID(계약ID);
        ye401.set사용자ID(사용자ID);
        List<YE401VO> listYE401 = ye401DAO.getYE401Map(ye401);

        if (listYE401.size() > 0) {
            int 금액;
            for (YE401VO vo : listYE401) {
                금액 = vo.get납입금액() - vo.get차감금액();

                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        if ("1".equals(vo.get보험구분코드())) {
                            response.근로자_국세청자료.특별세액공제_보험료_보장성 += 금액;
                        } else if ("2".equals(vo.get보험구분코드())) {
                            response.근로자_국세청자료.특별세액공제_보험료_장애인전용보장성 += 금액;
                        }
                        response.근로자_국세청자료.특별세액공제_보험료 += 금액;
                        response.근로자_국세청자료.특별세액공제 += 금액;
                    } else {
                        if ("1".equals(vo.get보험구분코드())) {
                            response.근로자_기타자료.특별세액공제_보험료_보장성 += 금액;
                        } else if ("2".equals(vo.get보험구분코드())) {
                            response.근로자_기타자료.특별세액공제_보험료_장애인전용보장성 += 금액;
                        }
                        response.근로자_기타자료.특별세액공제_보험료 += 금액;
                        response.근로자_기타자료.특별세액공제 += 금액;
                    }

                    if ("1".equals(vo.get보험구분코드())) {
                        response.근로자_합계.특별세액공제_보험료_보장성 += 금액;
                    } else if ("2".equals(vo.get보험구분코드())) {
                        response.근로자_합계.특별세액공제_보험료_장애인전용보장성 += 금액;
                    }
                    response.근로자_합계.특별세액공제_보험료 += 금액;
                    response.근로자_합계.특별세액공제 += 금액;
                }

                if ("1".equals(vo.get사용여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        if ("1".equals(vo.get보험구분코드())) {
                            response.관리자_국세청자료.특별세액공제_보험료_보장성 += 금액;
                        } else if ("2".equals(vo.get보험구분코드())) {
                            response.관리자_국세청자료.특별세액공제_보험료_장애인전용보장성 += 금액;
                        }
                        response.관리자_국세청자료.특별세액공제_보험료 += 금액;
                        response.관리자_국세청자료.특별세액공제 += 금액;
                    } else {
                        if ("1".equals(vo.get보험구분코드())) {
                            response.관리자_기타자료.특별세액공제_보험료_보장성 += 금액;
                        } else if ("2".equals(vo.get보험구분코드())) {
                            response.관리자_기타자료.특별세액공제_보험료_장애인전용보장성 += 금액;
                        }
                        response.관리자_기타자료.특별세액공제_보험료 += 금액;
                        response.관리자_기타자료.특별세액공제 += 금액;
                    }

                    if ("1".equals(vo.get보험구분코드())) {
                        response.관리자_합계.특별세액공제_보험료_보장성 += 금액;
                    } else if ("2".equals(vo.get보험구분코드())) {
                        response.관리자_합계.특별세액공제_보험료_장애인전용보장성 += 금액;
                    }
                    response.관리자_합계.특별세액공제_보험료 += 금액;
                    response.관리자_합계.특별세액공제 += 금액;
                }
            }
        }
    }

    private void get특별세액공제_의료비안경구입비(String 계약ID, String 사용자ID, YW500Response response) {
        YE402VO ye402 = new YE402VO();
        ye402.set계약ID(계약ID);
        ye402.set사용자ID(사용자ID);
        List<YE402VO> listYE402 = ye402DAO.getYE402Map(ye402);

        if (listYE402.size() > 0) {
            int 금액;
            for (YE402VO vo : listYE402) {
                금액 = vo.get지출액() - vo.get차감금액();

                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        if ("3".equals(vo.get의료비유형())) {
                            response.근로자_국세청자료.특별세액공제_의료비안경구입비_난임시술비 += 금액;
                        } else {
                            if ("1".equals(vo.get공제종류코드())) {
                                response.근로자_국세청자료.특별세액공제_의료비안경구입비_본인65세이상자장애인 += 금액;
                            } else if ("2".equals(vo.get공제종류코드())) {
                                response.근로자_국세청자료.특별세액공제_의료비안경구입비_그밖의공제대상자 += 금액;
                            }
                        }
                        response.근로자_국세청자료.특별세액공제_의료비안경구입비 += 금액;
                        response.근로자_국세청자료.특별세액공제 += 금액;
                    } else {
                        if ("3".equals(vo.get의료비유형())) {
                            response.근로자_기타자료.특별세액공제_의료비안경구입비_난임시술비 += 금액;
                        } else {
                            if ("1".equals(vo.get공제종류코드())) {
                                response.근로자_기타자료.특별세액공제_의료비안경구입비_본인65세이상자장애인 += 금액;
                            } else if ("2".equals(vo.get공제종류코드())) {
                                response.근로자_기타자료.특별세액공제_의료비안경구입비_그밖의공제대상자 += 금액;
                            }
                        }
                        response.근로자_기타자료.특별세액공제_의료비안경구입비 += 금액;
                        response.근로자_기타자료.특별세액공제 += 금액;
                    }

                    if ("3".equals(vo.get의료비유형())) {
                        response.근로자_합계.특별세액공제_의료비안경구입비_난임시술비 += 금액;
                    } else {
                        if ("1".equals(vo.get공제종류코드())) {
                            response.근로자_합계.특별세액공제_의료비안경구입비_본인65세이상자장애인 += 금액;
                        } else if ("2".equals(vo.get공제종류코드())) {
                            response.근로자_합계.특별세액공제_의료비안경구입비_그밖의공제대상자 += 금액;
                        }
                    }
                    response.근로자_합계.특별세액공제_의료비안경구입비 += 금액;
                    response.근로자_합계.특별세액공제 += 금액;
                }

                if ("1".equals(vo.get사용여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        if ("3".equals(vo.get의료비유형())) {
                            response.관리자_국세청자료.특별세액공제_의료비안경구입비_난임시술비 += 금액;
                        } else {
                            if ("1".equals(vo.get공제종류코드())) {
                                response.관리자_국세청자료.특별세액공제_의료비안경구입비_본인65세이상자장애인 += 금액;
                            } else if ("2".equals(vo.get공제종류코드())) {
                                response.관리자_국세청자료.특별세액공제_의료비안경구입비_그밖의공제대상자 += 금액;
                            }
                        }
                        response.관리자_국세청자료.특별세액공제_의료비안경구입비 += 금액;
                        response.관리자_국세청자료.특별세액공제 += 금액;
                    } else {
                        if ("3".equals(vo.get의료비유형())) {
                            response.관리자_기타자료.특별세액공제_의료비안경구입비_난임시술비 += 금액;
                        } else {
                            if ("1".equals(vo.get공제종류코드())) {
                                response.관리자_기타자료.특별세액공제_의료비안경구입비_본인65세이상자장애인 += 금액;
                            } else if ("2".equals(vo.get공제종류코드())) {
                                response.관리자_기타자료.특별세액공제_의료비안경구입비_그밖의공제대상자 += 금액;
                            }
                        }
                        response.관리자_기타자료.특별세액공제_의료비안경구입비 += 금액;
                        response.관리자_기타자료.특별세액공제 += 금액;
                    }

                    if ("3".equals(vo.get의료비유형())) {
                        response.관리자_합계.특별세액공제_의료비안경구입비_난임시술비 += 금액;
                    } else {
                        if ("1".equals(vo.get공제종류코드())) {
                            response.관리자_합계.특별세액공제_의료비안경구입비_본인65세이상자장애인 += 금액;
                        } else if ("2".equals(vo.get공제종류코드())) {
                            response.관리자_합계.특별세액공제_의료비안경구입비_그밖의공제대상자 += 금액;
                        }
                    }
                    response.관리자_합계.특별세액공제_의료비안경구입비 += 금액;
                    response.관리자_합계.특별세액공제 += 금액;
                }
            }
        }
    }

    private void get특별세액공제_교육비교복구입비체험학습비(String 계약ID, String 사용자ID, YW500Response response) {
        YE403VO ye403 = new YE403VO();
        ye403.set계약ID(계약ID);
        ye403.set사용자ID(사용자ID);
        List<YE403VO> listYE403 = ye403DAO.getYE403Map(ye403);

        if (listYE403.size() > 0) {
            int 교육비;
            for (YE403VO vo : listYE403) {
                교육비 = vo.get공납금() - vo.get공납금_차감금액() + vo.get교복구입비() - vo.get교복구입비_차감금액() + vo.get체험학습비() - vo.get체험학습비_차감금액();

                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        if ("1".equals(vo.get공제종류코드())) {
                            response.근로자_국세청자료.특별세액공제_교육비교복구입비체험학습비_본인 += 교육비;
                        } else if ("2".equals(vo.get공제종류코드())) {
                            response.근로자_국세청자료.특별세액공제_교육비교복구입비체험학습비_취학전아동 += 교육비;
                        } else if ("3".equals(vo.get공제종류코드())) {
                            response.근로자_국세청자료.특별세액공제_교육비교복구입비체험학습비_초중고등학교 += 교육비;
                        } else if ("4".equals(vo.get공제종류코드())) {
                            response.근로자_국세청자료.특별세액공제_교육비교복구입비체험학습비_대학생 += 교육비;
                        } else if ("5".equals(vo.get공제종류코드())) {
                            response.근로자_국세청자료.특별세액공제_교육비교복구입비체험학습비_장애인 += 교육비;
                        }
                        response.근로자_국세청자료.특별세액공제_교육비교복구입비체험학습비 += 교육비;
                        response.근로자_국세청자료.특별세액공제 += 교육비;
                    } else {
                        if ("1".equals(vo.get공제종류코드())) {
                            response.근로자_기타자료.특별세액공제_교육비교복구입비체험학습비_본인 += 교육비;
                        } else if ("2".equals(vo.get공제종류코드())) {
                            response.근로자_기타자료.특별세액공제_교육비교복구입비체험학습비_취학전아동 += 교육비;
                        } else if ("3".equals(vo.get공제종류코드())) {
                            response.근로자_기타자료.특별세액공제_교육비교복구입비체험학습비_초중고등학교 += 교육비;
                        } else if ("4".equals(vo.get공제종류코드())) {
                            response.근로자_기타자료.특별세액공제_교육비교복구입비체험학습비_대학생 += 교육비;
                        } else if ("5".equals(vo.get공제종류코드())) {
                            response.근로자_기타자료.특별세액공제_교육비교복구입비체험학습비_장애인 += 교육비;
                        }
                        response.근로자_기타자료.특별세액공제_교육비교복구입비체험학습비 += 교육비;
                        response.근로자_기타자료.특별세액공제 += 교육비;
                    }

                    if ("1".equals(vo.get공제종류코드())) {
                        response.근로자_합계.특별세액공제_교육비교복구입비체험학습비_본인 += 교육비;
                    } else if ("2".equals(vo.get공제종류코드())) {
                        response.근로자_합계.특별세액공제_교육비교복구입비체험학습비_취학전아동 += 교육비;
                    } else if ("3".equals(vo.get공제종류코드())) {
                        response.근로자_합계.특별세액공제_교육비교복구입비체험학습비_초중고등학교 += 교육비;
                    } else if ("4".equals(vo.get공제종류코드())) {
                        response.근로자_합계.특별세액공제_교육비교복구입비체험학습비_대학생 += 교육비;
                    } else if ("5".equals(vo.get공제종류코드())) {
                        response.근로자_합계.특별세액공제_교육비교복구입비체험학습비_장애인 += 교육비;
                    }
                    response.근로자_합계.특별세액공제_교육비교복구입비체험학습비 += 교육비;
                    response.근로자_합계.특별세액공제 += 교육비;
                }

                if ("1".equals(vo.get사용여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        if ("1".equals(vo.get공제종류코드())) {
                            response.관리자_국세청자료.특별세액공제_교육비교복구입비체험학습비_본인 += 교육비;
                        } else if ("2".equals(vo.get공제종류코드())) {
                            response.관리자_국세청자료.특별세액공제_교육비교복구입비체험학습비_취학전아동 += 교육비;
                        } else if ("3".equals(vo.get공제종류코드())) {
                            response.관리자_국세청자료.특별세액공제_교육비교복구입비체험학습비_초중고등학교 += 교육비;
                        } else if ("4".equals(vo.get공제종류코드())) {
                            response.관리자_국세청자료.특별세액공제_교육비교복구입비체험학습비_대학생 += 교육비;
                        } else if ("5".equals(vo.get공제종류코드())) {
                            response.관리자_국세청자료.특별세액공제_교육비교복구입비체험학습비_장애인 += 교육비;
                        }
                        response.관리자_국세청자료.특별세액공제_교육비교복구입비체험학습비 += 교육비;
                        response.관리자_국세청자료.특별세액공제 += 교육비;
                    } else {
                        if ("1".equals(vo.get공제종류코드())) {
                            response.관리자_기타자료.특별세액공제_교육비교복구입비체험학습비_본인 += 교육비;
                        } else if ("2".equals(vo.get공제종류코드())) {
                            response.관리자_기타자료.특별세액공제_교육비교복구입비체험학습비_취학전아동 += 교육비;
                        } else if ("3".equals(vo.get공제종류코드())) {
                            response.관리자_기타자료.특별세액공제_교육비교복구입비체험학습비_초중고등학교 += 교육비;
                        } else if ("4".equals(vo.get공제종류코드())) {
                            response.관리자_기타자료.특별세액공제_교육비교복구입비체험학습비_대학생 += 교육비;
                        } else if ("5".equals(vo.get공제종류코드())) {
                            response.관리자_기타자료.특별세액공제_교육비교복구입비체험학습비_장애인 += 교육비;
                        }
                        response.관리자_기타자료.특별세액공제_교육비교복구입비체험학습비 += 교육비;
                        response.관리자_기타자료.특별세액공제 += 교육비;
                    }

                    if ("1".equals(vo.get공제종류코드())) {
                        response.관리자_합계.특별세액공제_교육비교복구입비체험학습비_본인 += 교육비;
                    } else if ("2".equals(vo.get공제종류코드())) {
                        response.관리자_합계.특별세액공제_교육비교복구입비체험학습비_취학전아동 += 교육비;
                    } else if ("3".equals(vo.get공제종류코드())) {
                        response.관리자_합계.특별세액공제_교육비교복구입비체험학습비_초중고등학교 += 교육비;
                    } else if ("4".equals(vo.get공제종류코드())) {
                        response.관리자_합계.특별세액공제_교육비교복구입비체험학습비_대학생 += 교육비;
                    } else if ("5".equals(vo.get공제종류코드())) {
                        response.관리자_합계.특별세액공제_교육비교복구입비체험학습비_장애인 += 교육비;
                    }
                    response.관리자_합계.특별세액공제_교육비교복구입비체험학습비 += 교육비;
                    response.관리자_합계.특별세액공제 += 교육비;
                }
            }
        }
    }

    private void get특별세액공제_기부금(String 계약ID, String 사용자ID, YW500Response response) {
        YE404VO ye404 = new YE404VO();
        ye404.set계약ID(계약ID);
        ye404.set사용자ID(사용자ID);
        List<YE404VO> listYE404 = ye404DAO.getYE404Map(ye404);

        if (listYE404.size() > 0) {
            for (YE404VO vo : listYE404) {
                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        if ("10".equals(vo.get기부코드())) {
                            response.근로자_국세청자료.특별세액공제_기부금_법정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("20".equals(vo.get기부코드())) {
                            response.근로자_국세청자료.특별세액공제_기부금_정치자금기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("40".equals(vo.get기부코드())) {
                            response.근로자_국세청자료.특별세액공제_기부금_종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("41".equals(vo.get기부코드())) {
                            response.근로자_국세청자료.특별세액공제_기부금_종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("42".equals(vo.get기부코드())) {
                            response.근로자_국세청자료.특별세액공제_기부금_우리사주기부금 += vo.get기부명세_공제대상기부금();
                        }
                        response.근로자_국세청자료.특별세액공제_기부금 += vo.get기부명세_공제대상기부금();
                        response.근로자_국세청자료.특별세액공제 += vo.get기부명세_공제대상기부금();
                    } else {
                        if ("10".equals(vo.get기부코드())) {
                            response.근로자_기타자료.특별세액공제_기부금_법정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("20".equals(vo.get기부코드())) {
                            response.근로자_기타자료.특별세액공제_기부금_정치자금기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("40".equals(vo.get기부코드())) {
                            response.근로자_기타자료.특별세액공제_기부금_종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("41".equals(vo.get기부코드())) {
                            response.근로자_기타자료.특별세액공제_기부금_종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("42".equals(vo.get기부코드())) {
                            response.근로자_기타자료.특별세액공제_기부금_우리사주기부금 += vo.get기부명세_공제대상기부금();
                        }
                        response.근로자_기타자료.특별세액공제_기부금 += vo.get기부명세_공제대상기부금();
                        response.근로자_기타자료.특별세액공제 += vo.get기부명세_공제대상기부금();
                    }

                    if ("10".equals(vo.get기부코드())) {
                        response.근로자_합계.특별세액공제_기부금_법정기부금 += vo.get기부명세_공제대상기부금();
                    } else if ("20".equals(vo.get기부코드())) {
                        response.근로자_합계.특별세액공제_기부금_정치자금기부금 += vo.get기부명세_공제대상기부금();
                    } else if ("40".equals(vo.get기부코드())) {
                        response.근로자_합계.특별세액공제_기부금_종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                    } else if ("41".equals(vo.get기부코드())) {
                        response.근로자_합계.특별세액공제_기부금_종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                    } else if ("42".equals(vo.get기부코드())) {
                        response.근로자_합계.특별세액공제_기부금_우리사주기부금 += vo.get기부명세_공제대상기부금();
                    }
                    response.근로자_합계.특별세액공제_기부금 += vo.get기부명세_공제대상기부금();
                    response.근로자_합계.특별세액공제 += vo.get기부명세_공제대상기부금();
                }

                if ("1".equals(vo.get사용여부())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        if ("10".equals(vo.get기부코드())) {
                            response.관리자_국세청자료.특별세액공제_기부금_법정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("20".equals(vo.get기부코드())) {
                            response.관리자_국세청자료.특별세액공제_기부금_정치자금기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("40".equals(vo.get기부코드())) {
                            response.관리자_국세청자료.특별세액공제_기부금_종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("41".equals(vo.get기부코드())) {
                            response.관리자_국세청자료.특별세액공제_기부금_종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("42".equals(vo.get기부코드())) {
                            response.관리자_국세청자료.특별세액공제_기부금_우리사주기부금 += vo.get기부명세_공제대상기부금();
                        }
                        response.관리자_국세청자료.특별세액공제_기부금 += vo.get기부명세_공제대상기부금();
                        response.관리자_국세청자료.특별세액공제 += vo.get기부명세_공제대상기부금();
                    } else {
                        if ("10".equals(vo.get기부코드())) {
                            response.관리자_기타자료.특별세액공제_기부금_법정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("20".equals(vo.get기부코드())) {
                            response.관리자_기타자료.특별세액공제_기부금_정치자금기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("40".equals(vo.get기부코드())) {
                            response.관리자_기타자료.특별세액공제_기부금_종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("41".equals(vo.get기부코드())) {
                            response.관리자_기타자료.특별세액공제_기부금_종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                        } else if ("42".equals(vo.get기부코드())) {
                            response.관리자_기타자료.특별세액공제_기부금_우리사주기부금 += vo.get기부명세_공제대상기부금();
                        }
                        response.관리자_기타자료.특별세액공제_기부금 += vo.get기부명세_공제대상기부금();
                        response.관리자_기타자료.특별세액공제 += vo.get기부명세_공제대상기부금();
                    }

                    if ("10".equals(vo.get기부코드())) {
                        response.관리자_합계.특별세액공제_기부금_법정기부금 += vo.get기부명세_공제대상기부금();
                    } else if ("20".equals(vo.get기부코드())) {
                        response.관리자_합계.특별세액공제_기부금_정치자금기부금 += vo.get기부명세_공제대상기부금();
                    } else if ("40".equals(vo.get기부코드())) {
                        response.관리자_합계.특별세액공제_기부금_종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
                    } else if ("41".equals(vo.get기부코드())) {
                        response.관리자_합계.특별세액공제_기부금_종교단체지정기부금 += vo.get기부명세_공제대상기부금();
                    } else if ("42".equals(vo.get기부코드())) {
                        response.관리자_합계.특별세액공제_기부금_우리사주기부금 += vo.get기부명세_공제대상기부금();
                    }
                    response.관리자_합계.특별세액공제_기부금 += vo.get기부명세_공제대상기부금();
                    response.관리자_합계.특별세액공제 += vo.get기부명세_공제대상기부금();
                }
            }
        }
    }

    private void get세액감면(String bizId, String 계약ID, String 사용자ID, YW500Response response) {
        YE002VO ye002 = new YE002VO();
        ye002.setBizId(bizId);
        ye002.set계약ID(계약ID);
        ye002.set사용자ID(사용자ID);
        List<YE002VO> listYE002 = ye002DAO.getYE002TaxList(ye002);

        int 회사자료 = 0;
        if (listYE002 != null) {
            for (YE002VO vo : listYE002) {
                회사자료 += StringUtil.strPaserInt(vo.getT01());
                회사자료 += StringUtil.strPaserInt(vo.getT10());
                회사자료 += StringUtil.strPaserInt(vo.getT11());
                회사자료 += StringUtil.strPaserInt(vo.getT12());
                회사자료 += StringUtil.strPaserInt(vo.getT20());
            }
        }
        response.근로자_기타자료.세액감면 += 회사자료;
        response.근로자_합계.세액감면 += 회사자료;
        response.관리자_기타자료.세액감면 += 회사자료;
        response.관리자_합계.세액감면 += 회사자료;

        YE406VO ye406 = new YE406VO();
        ye406.set계약ID(계약ID);
        ye406.set사용자ID(사용자ID);
        List<YE406VO> listYE406 = ye406DAO.getYE406Map(ye406);

        if (listYE406.size() > 0) {
            for (YE406VO vo : listYE406) {
                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    response.근로자_기타자료.세액감면 += vo.get감면대상급여();
                    response.근로자_합계.세액감면 += vo.get감면대상급여();
                }

                if ("1".equals(vo.get사용여부())) {
                    response.관리자_기타자료.세액감면 += vo.get감면대상급여();
                    response.관리자_합계.세액감면 += vo.get감면대상급여();
                }
            }
        }
    }

    private void get주택차입금(String 계약ID, String 사용자ID, YW500Response response) {
        YE502VO ye502 = new YE502VO();
        ye502.set계약ID(계약ID);
        ye502.set사용자ID(사용자ID);
        List<YE502VO> listYE502 = ye502DAO.getYE502Map(ye502);

        if (listYE502.size() > 0) {
            for (YE502VO vo : listYE502) {
                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    response.근로자_기타자료.주택차입금 += vo.get금액();
                    response.근로자_합계.주택차입금 += vo.get금액();
                }

                if ("1".equals(vo.get사용여부())) {
                    response.관리자_기타자료.주택차입금 += vo.get금액();
                    response.관리자_합계.주택차입금 += vo.get금액();
                }
            }
        }
    }

    private void get외국납부세액(String 계약ID, String 사용자ID, YW500Response response) {
        YE503VO ye503 = new YE503VO();
        ye503.set계약ID(계약ID);
        ye503.set사용자ID(사용자ID);
        List<YE503VO> listYE503 = ye503DAO.getYE503Map(ye503);

        if (listYE503.size() > 0) {
            for (YE503VO vo : listYE503) {
                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    response.근로자_기타자료.외국납부세액 += vo.get납세액_원화();
                    response.근로자_합계.외국납부세액 += vo.get납세액_원화();
                }

                if ("1".equals(vo.get사용여부())) {
                    response.관리자_기타자료.외국납부세액 += vo.get납세액_원화();
                    response.관리자_합계.외국납부세액 += vo.get납세액_원화();
                }
            }
        }
    }

    private void get월세액(String 계약ID, String 사용자ID, YW500Response response) {
        YE105VO ye105 = new YE105VO();
        ye105.set계약ID(계약ID);
        ye105.set사용자ID(사용자ID);
        List<YE105VO> listYE105 = ye105DAO.getYE105Map(ye105);

        if (listYE105.size() > 0) {
            for (YE105VO vo : listYE105) {
                if (vo.get사용자ID().equals(vo.get작업자ID()) && "1".equals(vo.get최종저장여부())) {
                    response.근로자_기타자료.월세액 += vo.get공제대상금액();
                    response.근로자_합계.월세액 += vo.get공제대상금액();
                }

                if ("1".equals(vo.get사용여부())) {
                    response.관리자_기타자료.월세액 += vo.get공제대상금액();
                    response.관리자_합계.월세액 += vo.get공제대상금액();
                }
            }
        }
    }

    private void get정정사유(String 계약ID, String 사용자ID, YW500Response response) {
        YE900VO ye900 = new YE900VO();
        ye900.set계약ID(계약ID);
        ye900.set사용자ID(사용자ID);

        List<YE900VO> listYE900 = ye900DAO.getYE900List(ye900);
        if (listYE900 != null) {
            for (YE900VO vo : listYE900) {
                switch (vo.get사유구분()) {
                    case "W601":
                        response.수정내용.연금보험료공제_국민연금보험료 = vo.get정정사유();
                        break;
                    case "W602":
                        response.수정내용.연금보험료공제_공적연금보험료 = vo.get정정사유();
                        break;
                    case "W612":
                        response.수정내용.특별소득공제_국민건강보험료 = vo.get정정사유();
                        break;
                    case "W613":
                        response.수정내용.특별소득공제_주택임차차입금원리금상환액 = vo.get정정사유();
                        break;
                    case "W614":
                        response.수정내용.특별소득공제_주택임차차입금원리금상환액_소득공제명세 = vo.get정정사유();
                        break;
                    case "W615":
                        response.수정내용.특별소득공제_장기주택저당차입금이자상환액 = vo.get정정사유();
                        break;
                    case "W620":
                        response.수정내용.개인연금저축 = vo.get정정사유();
                        break;
                    case "W630":
                        response.수정내용.주택마련저축 = vo.get정정사유();
                        break;
                    case "W640":
                        response.수정내용.신용카드 = vo.get정정사유();
                        break;
                    case "W650":
                        response.수정내용.장기집합투자증권저축 = vo.get정정사유();
                        break;
                    case "W661":
                        response.수정내용.그밖의소득공제_소기업소상공인공제부금 = vo.get정정사유();
                        break;
                    case "W662":
                        response.수정내용.그밖의소득공제_투자조합출자등 = vo.get정정사유();
                        break;
                    case "W663":
                        response.수정내용.그밖의소득공제_우리사주조합출연금 = vo.get정정사유();
                        break;
                    case "W664":
                        response.수정내용.그밖의소득공제_고용유지중소기업근로자 = vo.get정정사유();
                        break;
                    case "W701":
                        response.수정내용.연금계좌_퇴직연금 = vo.get정정사유();
                        break;
                    case "W702":
                        response.수정내용.연금계좌_연금저축 = vo.get정정사유();
                        break;
                    case "W711":
                        response.수정내용.특별세액공제_보험료 = vo.get정정사유();
                        break;
                    case "W712":
                        response.수정내용.특별세액공제_의료비안경구입비 = vo.get정정사유();
                        break;
                    case "W713":
                        response.수정내용.특별세액공제_교육비교복구입비체험학습비 = vo.get정정사유();
                        break;
                    case "W714":
                        response.수정내용.특별세액공제_기부금 = vo.get정정사유();
                        break;
                    case "W720":
                        response.수정내용.세액감면 = vo.get정정사유();
                        break;
                    case "W732":
                        response.수정내용.주택차입금 = vo.get정정사유();
                        break;
                    case "W733":
                        response.수정내용.외국납부세액 = vo.get정정사유();
                        break;
                    case "W616":
                        response.수정내용.월세액 = vo.get정정사유();
                        break;
                }
            }
        }
    }
}
