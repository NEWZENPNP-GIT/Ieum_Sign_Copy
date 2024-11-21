package com.ezsign.feb.easyFeb.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.content.dao.ContentDAO;
import com.ezsign.content.service.ContentService;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.feb.easyFeb.dao.AttachFileDao;
import com.ezsign.feb.easyFeb.service.EasyYearTaxService;
import com.ezsign.feb.easyFeb.vo.AttachFile;
import com.ezsign.feb.easyFeb.vo.AttachFileVo;
import com.ezsign.feb.house.dao.YE105DAO;
import com.ezsign.feb.house.dao.YE108DAO;
import com.ezsign.feb.house.vo.YE105VO;
import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.special.dao.YE401DAO;
import com.ezsign.feb.special.dao.YE402DAO;
import com.ezsign.feb.special.dao.YE403DAO;
import com.ezsign.feb.special.dao.YE404DAO;
import com.ezsign.feb.special.dao.YE405DAO;
import com.ezsign.feb.special.dao.YE408DAO;
import com.ezsign.feb.special.vo.YE401VO;
import com.ezsign.feb.special.vo.YE402VO;
import com.ezsign.feb.special.vo.YE403VO;
import com.ezsign.feb.special.vo.YE404VO;
import com.ezsign.feb.special.vo.YE405VO;
import com.ezsign.feb.special.vo.YE408VO;
import com.ezsign.feb.system.dao.YS000DAO;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.worker.dao.YE001DAO;
import com.ezsign.feb.worker.dao.YE020DAO;
import com.ezsign.feb.worker.service.YE020Service;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.feb.worker.vo.YE020VO;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.window.vo.Code;
import com.ezsign.window.vo.Family;
import com.ezsign.window.vo.YW710Response;

@SuppressWarnings("NonAsciiCharacters")
@Service("easyYearTaxService")
public class EasyYearTaxServiceImpl implements EasyYearTaxService {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "contentService")
	private ContentService contentService;
    
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

    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;
    
    @Resource(name="ye020DAO")
	private YE020DAO ye020DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;

    @Resource(name = "ye108DAO")
    private YE108DAO ye108DAO;
    
    @Resource(name = "ye105DAO")
    private YE105DAO ye105DAO;
    
    @Resource(name="contentDAO")
	private ContentDAO contentDAO;
    
    @Resource(name = "attachFileDao")
    private AttachFileDao attachFileDao;
    
	@Override
	public void getInsurance(String bizId, String 계약ID, String 사용자ID, YW710Response response) {
		
		CodeVO codeVO = new CodeVO();
        codeVO.setGrpCommCode("432");
//        response.보험료의료비_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 2);
        response.보험료의료비_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        
        codeVO.setGrpCommCode("428");
        response.보험료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        
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
        
        if (listYE401.size() > 0) {
            response.보험료 = listYE401;
        }
	}

	@Override
	public void getMedical(String bizId, String 계약ID, String 사용자ID, YW710Response response) {
	
		CodeVO codeVO = new CodeVO();
		codeVO.setGrpCommCode("432");
//        response.보험료의료비_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 2);
        response.보험료의료비_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
		codeVO.setGrpCommCode("407");
        response.의료비증빙코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("409");
        response.의료비공제종류코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("436");
        response.의료비유형 = Code.getCodeList(codeDAO.getCodeList(codeVO));
		
		YE001VO ye001 = new YE001VO();
        ye001.set계약ID(계약ID);
        ye001.set사용자ID(사용자ID);
        ye001.setStartPage(0);
        ye001.setEndPage(9999);
        response.부양가족ID = Family.getIDList(ye001DAO.getYE001List(ye001));
		
		YE402VO ye402 = new YE402VO();
        ye402.set계약ID(계약ID);
        ye402.set사용자ID(사용자ID);
        List<YE402VO> listYE402 = ye402DAO.getYE402List(ye402);
		
		if (listYE402.size() > 0) {
            response.의료비 = listYE402;
        }
	}
	
	@Override
	public void getEducation(String bizId, String 계약ID, String 사용자ID, YW710Response response){
		
		CodeVO codeVO = new CodeVO();
		codeVO.setGrpCommCode("416");
//        response.교육비기부금_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 3);
        response.교육비기부금_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
		codeVO.setGrpCommCode("410");
        response.교육비공제종류코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
		
		YE001VO ye001 = new YE001VO();
        ye001.set계약ID(계약ID);
        ye001.set사용자ID(사용자ID);
        ye001.setStartPage(0);
        ye001.setEndPage(9999);
        response.부양가족ID = Family.getIDList(ye001DAO.getYE001List(ye001));
        
        YE403VO ye403 = new YE403VO();
        ye403.set계약ID(계약ID);
        ye403.set사용자ID(사용자ID);
        List<YE403VO> listYE403 = ye403DAO.getYE403List(ye403);
        
        if (listYE403.size() > 0) {
            response.교육비 = listYE403;
        }
	}
	
	@Override
	public void getDonation(String bizId, String 계약ID, String 사용자ID, YW710Response response){
		
		CodeVO codeVO = new CodeVO();
		codeVO.setGrpCommCode("416");
//        response.교육비기부금_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 3);
        response.교육비기부금_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("406");
        response.기부금코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("429");
        response.기부내용 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        
        YE001VO ye001 = new YE001VO();
        ye001.set계약ID(계약ID);
        ye001.set사용자ID(사용자ID);
        ye001.setStartPage(0);
        ye001.setEndPage(9999);
        response.부양가족ID = Family.getIDList(ye001DAO.getYE001List(ye001));
        
        YS000VO ys000 = new YS000VO();
        ys000.setBizId(bizId);
        ys000.set계약ID(계약ID);
        List<YS000VO> listYS000 = ys000DAO.getYS000List(ys000);
        if (listYS000.size() > 0) {
            response.계약년도 = listYS000.get(0).getFebYear();
        }
        
        YE404VO ye404 = new YE404VO();
        ye404.set계약ID(계약ID);
        ye404.set사용자ID(사용자ID);
        ye404.setStartPage(0);
        ye404.setEndPage(9999);
        ye404.setSortName("자료구분코드, 기부코드");
        List<YE404VO> listYE404 = ye404DAO.getYE404List(ye404);
System.out.println("response.계약년도: " + response.계약년도);
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
	}

	@Override
	public void getInsuranceFile(String bizId, String 계약ID, String 사용자ID, String 공제구분코드, AttachFileVo response) {

		
		CodeVO codeVO = new CodeVO();
        codeVO.setGrpCommCode("432");
//        response.보험료의료비_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 2);
        response.보험료의료비_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        
        codeVO.setGrpCommCode("428");
        response.보험료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        
        YE001VO ye001 = new YE001VO();
        ye001.set계약ID(계약ID);
        ye001.set사용자ID(사용자ID);
        ye001.setStartPage(0);
        ye001.setEndPage(9999);
        response.부양가족ID = Family.getIDList(ye001DAO.getYE001List(ye001));

        Map params = new HashMap();
        params.put("계약ID", 계약ID);
        params.put("사용자ID", 사용자ID);
        params.put("공제구분코드", 공제구분코드);
        
        List<Map> listYE401 = attachFileDao.getYE401List(params);
		
		if (listYE401.size() > 0) {
            response.보험료 = listYE401;
        }
	}
	
	@Override
	public void getMedicalFile(String bizId, String 계약ID, String 사용자ID, String 공제구분코드, AttachFileVo response) {
	
		CodeVO codeVO = new CodeVO();
		codeVO.setGrpCommCode("432");
//        response.보험료의료비_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 2);
        response.보험료의료비_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
		codeVO.setGrpCommCode("407");
        response.의료비증빙코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("409");
        response.의료비공제종류코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("436");
        response.의료비유형 = Code.getCodeList(codeDAO.getCodeList(codeVO));
		
		YE001VO ye001 = new YE001VO();
        ye001.set계약ID(계약ID);
        ye001.set사용자ID(사용자ID);
        ye001.setStartPage(0);
        ye001.setEndPage(9999);
        response.부양가족ID = Family.getIDList(ye001DAO.getYE001List(ye001));

        Map params = new HashMap();
        params.put("계약ID", 계약ID);
        params.put("사용자ID", 사용자ID);
        params.put("공제구분코드", 공제구분코드);
        
        List<Map> listYE402 = attachFileDao.getYE402List(params);
		
		if (listYE402.size() > 0) {
            response.의료비 = listYE402;
        }
	}

	@Override
	public void getEducationFile(String bizId, String 계약ID, String 사용자ID, String 공제구분코드, AttachFileVo response) {

		CodeVO codeVO = new CodeVO();
		codeVO.setGrpCommCode("416");
//        response.교육비기부금_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 3);
        response.교육비기부금_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
		codeVO.setGrpCommCode("410");
        response.교육비공제종류코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
		
		YE001VO ye001 = new YE001VO();
        ye001.set계약ID(계약ID);
        ye001.set사용자ID(사용자ID);
        ye001.setStartPage(0);
        ye001.setEndPage(9999);
        response.부양가족ID = Family.getIDList(ye001DAO.getYE001List(ye001));
        
        Map params = new HashMap();
        params.put("계약ID", 계약ID);
        params.put("사용자ID", 사용자ID);
        params.put("공제구분코드", 공제구분코드);
        List<Map> listYE403 = attachFileDao.getYE403List(params);
        
        if (listYE403.size() > 0) {
            response.교육비 = listYE403;
        }
	}
	
	@Override
	public void getCreditFile(String bizId, String 계약ID, String 사용자ID, String 공제구분코드, AttachFileVo response){
		
        CodeVO codeVO = new CodeVO();
        codeVO.setGrpCommCode("416");
//        response.자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 3);
        response.자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("426");
        response.기간구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));

        YE001VO ye001 = new YE001VO();
        ye001.set계약ID(계약ID);
        ye001.set사용자ID(사용자ID);
        ye001.setStartPage(0);
        ye001.setEndPage(9999);
        response.부양가족ID = Family.getIDList(ye001DAO.getYE001List(ye001));

        Map params = new HashMap();
        params.put("계약ID", 계약ID);
        params.put("사용자ID", 사용자ID);
        params.put("공제구분코드", 공제구분코드);
        List<Map> listYE108 = attachFileDao.getYE108List(params);

        if (listYE108.size() > 0) {
            response.신용카드 = listYE108;
        }
	}
	
	@Override
	public void getDonationFile(String bizId, String 계약ID, String 사용자ID, String 공제구분코드, AttachFileVo response){

		CodeVO codeVO = new CodeVO();
		codeVO.setGrpCommCode("416");
//        response.교육비기부금_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO), 1, 3);
        response.교육비기부금_자료구분코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("406");
        response.기부금코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        codeVO.setGrpCommCode("429");
        response.기부내용 = Code.getCodeList(codeDAO.getCodeList(codeVO));
        
        YE001VO ye001 = new YE001VO();
        ye001.set계약ID(계약ID);
        ye001.set사용자ID(사용자ID);
        ye001.setStartPage(0);
        ye001.setEndPage(9999);
        response.부양가족ID = Family.getIDList(ye001DAO.getYE001List(ye001));
        
        Map params = new HashMap();
        params.put("계약ID", 계약ID);
        params.put("사용자ID", 사용자ID);
        params.put("공제구분코드", 공제구분코드);
        List<Map> listYE404 = attachFileDao.getYE404List(params);
        
        if (listYE404.size() > 0) {
            response.기부금 = listYE404;
        }
	}
	
	@Override
	public void getMonthlyRentFile(String bizId, String 계약ID, String 사용자ID, String 공제구분코드, AttachFileVo response){

		CodeVO codeVO = new CodeVO();
        codeVO.setGrpCommCode("422");
        codeVO.setGrpCommCode("415");
        response.유형코드 = Code.getCodeList(codeDAO.getCodeList(codeVO));
		
		Map params = new HashMap();
        params.put("계약ID", 계약ID);
        params.put("사용자ID", 사용자ID);
        params.put("공제구분코드", 공제구분코드);
        List<Map> listYE105 = attachFileDao.getYE105List(params);

        if (listYE105.size() > 0) {
            response.월세액공제 = listYE105;
        }
	}
	
	@Override
	public void getFamilyFile(String bizId, String 계약ID, String 사용자ID, String 공제구분코드, AttachFileVo response) throws Exception{
		
		Map params = new HashMap();
        params.put("계약ID", 계약ID);
        params.put("사용자ID", 사용자ID);
        params.put("공제구분코드", 공제구분코드);
        List<Map> listYE001 = attachFileDao.getYE001List(params);

        if(listYE001 != null && listYE001.size() > 0){
        	for(int i = 0; i < listYE001.size(); i++){
        		Map ye001VO = (Map)listYE001.get(i);
    			String identityCode = (String)ye001VO.get("개인식별번호");
    			
    			// 개인식별코드 복호화
    			if (StringUtil.isNotNull(identityCode)) {
    				ye001VO.put("개인식별번호", SecurityUtil.getinstance().aesDecode(identityCode));
    			}
        	}
        }
        
        if (listYE001.size() > 0) {
            response.부양가족 = listYE001;
        }
	}
	
	@Override
	public void insAttachFile(String bizId, String 계약ID, String 사용자ID, AttachFile attachFile, FileVO fileVO) throws Exception {

		YE020VO 첨부파일 = attachFile.get첨부파일();
		YE402VO 의료비 = attachFile.get의료비();
		YE401VO 보험료 = attachFile.get보험료();
		YE403VO 교육비 = attachFile.get교육비();
		YE108VO 신용카드 = attachFile.get신용카드();
		YE404VO 기부금 = attachFile.get기부금();
		YE105VO 월세액공제 = attachFile.get월세액공제();

		첨부파일.set계약ID(계약ID);
		첨부파일.set사용자ID(사용자ID);
		첨부파일.set작업자ID(사용자ID);

		ContentVO contentVO = new ContentVO();
		contentVO.setFileType(fileVO.getFileExt());
		contentVO.setFileName(fileVO.getFileStreNm());
		contentVO.setOrgFileName(fileVO.getFileStreOriNm());
		contentVO.setFileSize(Long.toString(fileVO.getFileSize()));
		contentVO.setFilePath(fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR);
		contentVO.setFileTitle(계약ID + 사용자ID);
		contentVO.setClassId(YE020Service.SAVE_CLASS_ID);
		contentVO.setFileVersion("0");
		contentVO.setParFileId("");
		contentVO.setEtcDesc("");
		contentVO.setCheckInOut("N");
		contentVO.setCheckUserId("");
		contentVO.setCheckCount("0");
		contentVO.setUseYn("Y");
		contentVO.setDelYn("N");
		
		System.out.println("fileName :" + contentVO.getFilePath() + contentVO.getFileName());
		int nResult = contentService.transContent(contentVO);
System.out.println("nResult: " + nResult);
System.out.println("첨부파일1: " + 첨부파일);
		if(contentService.COMPLETED==nResult) {
			첨부파일.set파일ID(contentVO.getFileId());
			ye020DAO.insYE020(첨부파일);
			
System.out.println("첨부파일2: " + 첨부파일);
			
		}
		
		if("F200".equals(attachFile.get공제구분코드())){
			//보험료
			보험료.set계약ID(계약ID);
			보험료.set사용자ID(사용자ID);
			보험료.set작업자ID(사용자ID);
			보험료.set자료구분코드("3");
			보험료.set추가제출서류번호(첨부파일.get일련번호());
			if("reg".equals(attachFile.getReg())){
				ye401DAO.insYE401(보험료);	
			}
		}else if("F600".equals(attachFile.get공제구분코드())){
			//의료비
			의료비.set계약ID(계약ID);
			의료비.set사용자ID(사용자ID);
			의료비.set작업자ID(사용자ID);
			의료비.set자료구분코드("3");
			의료비.set추가제출서류번호(첨부파일.get일련번호());
			if("reg".equals(attachFile.getReg())){
				ye402DAO.insYE402(의료비);
			}
		}else if("F800".equals(attachFile.get공제구분코드())){
			//교육비공제
			교육비.set계약ID(계약ID);
			교육비.set사용자ID(사용자ID);
			교육비.set작업자ID(사용자ID);
			교육비.set자료구분코드("3");
			교육비.set추가제출서류번호(첨부파일.get일련번호());
			if("reg".equals(attachFile.getReg())){
				ye403DAO.insYE403(교육비);
			}
		}else if("F500".equals(attachFile.get공제구분코드())){
			//신용카드
			신용카드.set계약ID(계약ID);
			신용카드.set사용자ID(사용자ID);
			신용카드.set작업자ID(사용자ID);
			신용카드.set자료구분코드("3");
			신용카드.set추가제출서류번호(첨부파일.get일련번호());
			if("reg".equals(attachFile.getReg())){
				ye108DAO.insYE108(신용카드);
			}
		}else if("F400".equals(attachFile.get공제구분코드())){
			//월세공제
			월세액공제.set계약ID(계약ID);
			월세액공제.set사용자ID(사용자ID);
			월세액공제.set작업자ID(사용자ID);
			//월세액공제.set자료구분코드("2");
			월세액공제.set추가제출서류번호(첨부파일.get일련번호());
			if("reg".equals(attachFile.getReg())){
				ye105DAO.insYE105(월세액공제);
			}
		}else if("F700".equals(attachFile.get공제구분코드())){
			//기부금
			기부금.set계약ID(계약ID);
			기부금.set사용자ID(사용자ID);
			기부금.set작업자ID(사용자ID);
			기부금.set자료구분코드("3"); //기타
			기부금.set추가제출서류번호(첨부파일.get일련번호());
			if("reg".equals(attachFile.getReg())){
				ye404DAO.insYE404(기부금);
			}
		}		
		
		YE000VO selYE000VO = new YE000VO();
		selYE000VO.set계약ID(계약ID);
		selYE000VO.setBizId(bizId);
		selYE000VO.set사용자ID(사용자ID);
		
		// 근로자 진행상태코드 조회
		YE901VO selYE901VO = new YE901VO();
		selYE901VO.set계약ID(계약ID);
		selYE901VO.set사용자ID(사용자ID);
		selYE901VO.set작업자ID(사용자ID);

		
		YE000VO ye000VO = ye000DAO.getYE000(selYE000VO);
		String 진행상태코드 = null;
		
		if (ye000VO != null) {
			진행상태코드 = ye000VO.get진행상태코드();
		}
		
		if (StringUtil.isNotNull(진행상태코드)) {
			if ("2".equals(진행상태코드)) {
				selYE901VO.set진행현황코드("21");
			} else if ("3".equals(진행상태코드)) {
				selYE901VO.set진행현황코드("31");
			} else {
				selYE901VO.set진행현황코드("15");
			}
		}

		// 근로자 진행현황 등록
		if (StringUtil.isNotNull(계약ID) && StringUtil.isNotNull(사용자ID)) {
			ye901DAO.insYE901(selYE901VO);
		}
	}

	@Override
	public int uptAttachFile(String bizId, String 계약ID, String 사용자ID, AttachFile attachFile, FileVO fileVO)
			throws Exception {
		
		int result = 0;
		
		YE020VO 첨부파일 = attachFile.get첨부파일();
		YE402VO 의료비 = attachFile.get의료비();
		YE401VO 보험료 = attachFile.get보험료();
		YE403VO 교육비 = attachFile.get교육비();
		YE108VO 신용카드 = attachFile.get신용카드();
		YE404VO 기부금 = attachFile.get기부금();
		YE105VO 월세액공제 = attachFile.get월세액공제();
		
		첨부파일.set계약ID(계약ID);
		첨부파일.set사용자ID(사용자ID);
		첨부파일.set작업자ID(사용자ID);

		if(fileVO != null) {
			
			// 이전 첨부파일 정보
			YE020VO ye020VO = new YE020VO();
			ye020VO.setBizId(bizId);
			ye020VO.set계약ID(계약ID);
			ye020VO.set사용자ID(사용자ID);
			ye020VO.set처리여부("2");
			ye020VO.set일련번호(첨부파일.get일련번호());
			
			List<YE020VO> ye020VOList = ye020DAO.getYE020FileList(ye020VO);
			System.out.println("ye020VOList : " + ye020VOList);
			YE020VO resultVO = ye020VOList.get(0);
			
			ContentVO preContentVO = new ContentVO();
			preContentVO.setFileId(resultVO.get파일ID());
			
			List<ContentVO> contentList = new ArrayList<>();
			contentList.add(preContentVO);
			
			// 수정된 첨부파일 정보
			ContentVO contentVO = new ContentVO();
			contentVO.setFileType(fileVO.getFileExt());
			contentVO.setFileName(fileVO.getFileStreNm());
			contentVO.setOrgFileName(fileVO.getFileStreOriNm());
			contentVO.setFileSize(Long.toString(fileVO.getFileSize()));
			contentVO.setFilePath(fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR);
			contentVO.setFileTitle(계약ID + 사용자ID);
			contentVO.setClassId(YE020Service.SAVE_CLASS_ID);
			contentVO.setFileVersion("0");
			contentVO.setParFileId("");
			contentVO.setEtcDesc("");
			contentVO.setCheckInOut("N");
			contentVO.setCheckUserId("");
			contentVO.setCheckCount("0");
			contentVO.setUseYn("Y");
			contentVO.setDelYn("N");
			
			System.out.println("fileName :" + contentVO.getFilePath() + contentVO.getFileName());
			int nResult = contentService.transContent(contentVO);
			if(contentService.COMPLETED==nResult) {
				
				// 이전 첨부된 원본 파일만 삭제  DB는 사용여부 N
				contentService.deleteContent(contentList);
				
				// 콘텐츠에서 정보 추출	
				ContentVO resultContentVO = contentDAO.getContent(preContentVO);
				
				// temp 파일 삭제 
				String tempFilePath = MultipartFileUtil.getSystemTempPath() + bizId + MultipartFileUtil.SEPERATOR + resultContentVO.getOrgFileName();
				File file = new File(tempFilePath);
				if(file.exists()) {
					boolean deleteResult = FileUtil.deleteFile(tempFilePath);
					if(!deleteResult) {
						System.out.println("[updYE020] 파일 삭제에 실패하였습니다.");
						return 0;
					}
				}
				
				// 수정된 파일 DB정보 엡데이트
				첨부파일.set파일ID(contentVO.getFileId());
				ye020DAO.updYE020(첨부파일);
				
				if("F200".equals(attachFile.get공제구분코드())){
					//보험료
					보험료.set계약ID(계약ID);
					보험료.set사용자ID(사용자ID);
					보험료.set작업자ID(사용자ID);
					보험료.set자료구분코드("3");
					보험료.set추가제출서류번호(첨부파일.get일련번호());
					if("reg".equals(attachFile.getReg())){
						if(보험료.get일련번호() == null || "".equals(보험료.get일련번호())){
							ye401DAO.insYE401(보험료);
						}else{
							ye401DAO.updYE401(보험료);
						}
					}
				}else if("F600".equals(attachFile.get공제구분코드())){
					//YW710ServiceImpl.saveYW710 참조
					//의료비
					의료비.set계약ID(계약ID);
					의료비.set사용자ID(사용자ID);
					의료비.set작업자ID(사용자ID);
					의료비.set자료구분코드("3");
					의료비.set추가제출서류번호(첨부파일.get일련번호());
					if("reg".equals(attachFile.getReg())){
						if(의료비.get일련번호() == null || "".equals(의료비.get일련번호())){
							ye402DAO.insYE402(의료비);
						}else{
							ye402DAO.updYE402(의료비);
						}
					}
				}else if("F800".equals(attachFile.get공제구분코드())){
					//교육비공제
					교육비.set계약ID(계약ID);
					교육비.set사용자ID(사용자ID);
					교육비.set작업자ID(사용자ID);
					교육비.set자료구분코드("3");
					교육비.set추가제출서류번호(첨부파일.get일련번호());
					if("reg".equals(attachFile.getReg())){
						if(교육비.get일련번호() == null || "".equals(교육비.get일련번호())){
							ye403DAO.insYE403(교육비);
						}else{
							ye403DAO.updYE403(교육비);
						}
					}
				}else if("F500".equals(attachFile.get공제구분코드())){
					//신용카드
					신용카드.set계약ID(계약ID);
					신용카드.set사용자ID(사용자ID);
					신용카드.set작업자ID(사용자ID);
					신용카드.set자료구분코드("3");
					신용카드.set추가제출서류번호(첨부파일.get일련번호());
					if("reg".equals(attachFile.getReg())){
						if(신용카드.get일련번호() == null || "".equals(신용카드.get일련번호())){
							ye108DAO.insYE108(신용카드);
						}else{
							ye108DAO.updYE108(신용카드);
						}
					}
				}else if("F400".equals(attachFile.get공제구분코드())){
					//월세공제
					월세액공제.set계약ID(계약ID);
					월세액공제.set사용자ID(사용자ID);
					월세액공제.set작업자ID(사용자ID);
					//월세액공제.set자료구분코드("3");
					월세액공제.set추가제출서류번호(첨부파일.get일련번호());
					if("reg".equals(attachFile.getReg())){
						if(월세액공제.get일련번호() == null || "".equals(월세액공제.get일련번호())){
							ye105DAO.insYE105(월세액공제);
						}else{
							ye105DAO.updYE105(월세액공제);
						}
					}
				}else if("F700".equals(attachFile.get공제구분코드())){
					//기부금
					기부금.set계약ID(계약ID);
					기부금.set사용자ID(사용자ID);
					기부금.set작업자ID(사용자ID);
					기부금.set자료구분코드("3");
					기부금.set추가제출서류번호(첨부파일.get일련번호());
					if("reg".equals(attachFile.getReg())){
						if(기부금.get일련번호() == null || "".equals(기부금.get일련번호())){
							ye404DAO.insYE404(기부금);
						}else{
							ye404DAO.updYE404(기부금);
						}
					}
				}		
			}

		}else{
			ye020DAO.updYE020(첨부파일);
			if("F200".equals(attachFile.get공제구분코드())){
				//보험료
				보험료.set계약ID(계약ID);
				보험료.set사용자ID(사용자ID);
				보험료.set작업자ID(사용자ID);
				보험료.set자료구분코드("3");
				보험료.set추가제출서류번호(첨부파일.get일련번호());
				if("reg".equals(attachFile.getReg())){
					if(보험료.get일련번호() == null || "".equals(보험료.get일련번호())){
						ye401DAO.insYE401(보험료);
					}else{
						ye401DAO.updYE401(보험료);
					}
				}
			}else if("F600".equals(attachFile.get공제구분코드())){
				//YW710ServiceImpl.saveYW710 참조
				//의료비
				의료비.set계약ID(계약ID);
				의료비.set사용자ID(사용자ID);
				의료비.set작업자ID(사용자ID);
				의료비.set자료구분코드("3");
				의료비.set추가제출서류번호(첨부파일.get일련번호());
				if("reg".equals(attachFile.getReg())){
					if(의료비.get일련번호() == null || "".equals(의료비.get일련번호())){
						ye402DAO.insYE402(의료비);
					}else{
						ye402DAO.updYE402(의료비);
					}
				}
			}else if("F800".equals(attachFile.get공제구분코드())){
				//교육비공제
				교육비.set계약ID(계약ID);
				교육비.set사용자ID(사용자ID);
				교육비.set작업자ID(사용자ID);
				교육비.set자료구분코드("3");
				교육비.set추가제출서류번호(첨부파일.get일련번호());
				if("reg".equals(attachFile.getReg())){
					if(교육비.get일련번호() == null || "".equals(교육비.get일련번호())){
						ye403DAO.insYE403(교육비);
					}else{
						ye403DAO.updYE403(교육비);
					}
				}
			}else if("F500".equals(attachFile.get공제구분코드())){
				//신용카드
				신용카드.set계약ID(계약ID);
				신용카드.set사용자ID(사용자ID);
				신용카드.set작업자ID(사용자ID);
				신용카드.set자료구분코드("3");
				신용카드.set추가제출서류번호(첨부파일.get일련번호());
				if("reg".equals(attachFile.getReg())){
					if(신용카드.get일련번호() == null || "".equals(신용카드.get일련번호())){
						ye108DAO.insYE108(신용카드);
					}else{
						ye108DAO.updYE108(신용카드);
					}
				}
				
			}else if("F400".equals(attachFile.get공제구분코드())){
				//월세공제
				월세액공제.set계약ID(계약ID);
				월세액공제.set사용자ID(사용자ID);
				월세액공제.set작업자ID(사용자ID);
				//월세액공제.set자료구분코드("2");
				월세액공제.set추가제출서류번호(첨부파일.get일련번호());
				if("reg".equals(attachFile.getReg())){
					if(월세액공제.get일련번호() == null || "".equals(월세액공제.get일련번호())){
						ye105DAO.insYE105(월세액공제);
					}else{
						ye105DAO.updYE105(월세액공제);
					}
				}
			}else if("F700".equals(attachFile.get공제구분코드())){
				//기부금
				기부금.set계약ID(계약ID);
				기부금.set사용자ID(사용자ID);
				기부금.set작업자ID(사용자ID);
				기부금.set자료구분코드("3");
				기부금.set추가제출서류번호(첨부파일.get일련번호());
				if("reg".equals(attachFile.getReg())){
					if(기부금.get일련번호() == null || "".equals(기부금.get일련번호())){
						ye404DAO.insYE404(기부금);
					}else{
						ye404DAO.updYE404(기부금);
					}
				}
			}
		}

		result++;
		
		return result;
	}

	@Override
	public int delAttachFile(String bizId, String 계약ID, String 사용자ID, AttachFile attachFile) throws Exception {

		int result = 0;
		
		YE020VO 첨부파일 = attachFile.get첨부파일();
		YE402VO 의료비 = attachFile.get의료비();
		YE401VO 보험료 = attachFile.get보험료();
		YE403VO 교육비 = attachFile.get교육비();
		YE108VO 신용카드 = attachFile.get신용카드();
		YE404VO 기부금 = attachFile.get기부금();
		YE105VO 월세액공제 = attachFile.get월세액공제();

		if("F200".equals(attachFile.get공제구분코드())){
			//보험료
			보험료.set계약ID(계약ID);
			보험료.set사용자ID(사용자ID);
			ye401DAO.updYE401Disable(보험료);
		}else if("F600".equals(attachFile.get공제구분코드())){
			의료비.set계약ID(계약ID);
			의료비.set사용자ID(사용자ID);
			ye402DAO.updYE402Disable(의료비);
		}else if("F800".equals(attachFile.get공제구분코드())){
			//교육비공제
			교육비.set계약ID(계약ID);
			교육비.set사용자ID(사용자ID);
			교육비.set작업자ID(사용자ID);
			교육비.set자료구분코드("3");
			교육비.set추가제출서류번호(첨부파일.get일련번호());
			ye403DAO.updYE403Disable(교육비);
		}else if("F500".equals(attachFile.get공제구분코드())){
			//신용카드
			신용카드.set계약ID(계약ID);
			신용카드.set사용자ID(사용자ID);
			신용카드.set작업자ID(사용자ID);
			신용카드.set자료구분코드("3");
			신용카드.set추가제출서류번호(첨부파일.get일련번호());
			ye108DAO.updYE108Disable(신용카드);
		}else if("F400".equals(attachFile.get공제구분코드())){
			//월세공제
			월세액공제.set계약ID(계약ID);
			월세액공제.set사용자ID(사용자ID);
			월세액공제.set작업자ID(사용자ID);
			//월세액공제.set자료구분코드("3");
			월세액공제.set추가제출서류번호(첨부파일.get일련번호());
			ye105DAO.updYE105Disable(월세액공제);
		}else if("F700".equals(attachFile.get공제구분코드())){
			//기부금
			기부금.set계약ID(계약ID);
			기부금.set사용자ID(사용자ID);
			기부금.set작업자ID(사용자ID);
			기부금.set자료구분코드("3");
			기부금.set추가제출서류번호(첨부파일.get일련번호());
			ye404DAO.updYE404Disable(기부금);
		}
		
		첨부파일.setBizId(bizId);
		첨부파일.set계약ID(계약ID);
		첨부파일.set사용자ID(사용자ID);
		첨부파일.set작업자ID(사용자ID);

		List<YE020VO> ye020VOList = ye020DAO.getYE020FileList(첨부파일);
		
		for (int i=0; i<ye020VOList.size(); i++) {
			YE020VO ye020VO = new YE020VO();
			ye020VO = ye020VOList.get(i);
			
			ContentVO contentVO = new ContentVO();
			contentVO.setFileId(ye020VO.get파일ID());
			
			List<ContentVO> contentList = new ArrayList<>();
			contentList.add(contentVO);
			
			// 실제 원본 파일만 삭제  DB는 사용여부 N
			contentService.deleteContent(contentList);
			
			// 콘텐츠에서 정보 추출	
			ContentVO resultContentVO = contentDAO.getContent(contentVO);
			
			// temp 파일 삭제 
			String tempFilePath = MultipartFileUtil.getSystemTempPath() + bizId + MultipartFileUtil.SEPERATOR + resultContentVO.getOrgFileName();
			File file = new File(tempFilePath);
			if(file.exists()) {
				boolean deleteResult = FileUtil.deleteFile(tempFilePath);
				if(!deleteResult) {
					System.out.println("[delYE020] 파일 삭제에 실패하였습니다.");
					return 0;
				}
			}
			ye020DAO.delYE020(ye020VO);
			result++;
		}
		
		return result;
	}

	@Override
	public Map getFileCount(String 계약ID, String 사용자ID) {
		
		Map params = new HashMap();
        params.put("계약ID", 계약ID);
        params.put("사용자ID", 사용자ID);
        
        Map map = attachFileDao.getFileCount(params);
		return map;
	}

	@Override
	public Integer getWorkplaceCheck(Map params) {

		return (Integer) ye000DAO.getYE000WorkplaceCount(params);
	}

}
