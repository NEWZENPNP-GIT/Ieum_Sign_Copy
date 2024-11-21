package com.ezsign.feb.worker.service.impl;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.content.dao.CabinetDAO;
import com.ezsign.content.dao.ContentDAO;
import com.ezsign.content.service.ContentService;
import com.ezsign.content.vo.CabinetVO;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.etc.dao.YE503DAO;
import com.ezsign.feb.etc.vo.YE503VO;
import com.ezsign.feb.house.dao.YE101DAO;
import com.ezsign.feb.house.dao.YE102DAO;
import com.ezsign.feb.house.dao.YE103DAO;
import com.ezsign.feb.house.dao.YE104DAO;
import com.ezsign.feb.house.dao.YE105DAO;
import com.ezsign.feb.house.dao.YE106DAO;
import com.ezsign.feb.house.dao.YE107DAO;
import com.ezsign.feb.house.dao.YE108DAO;
import com.ezsign.feb.house.dao.YE109DAO;
import com.ezsign.feb.house.vo.YE102VO;
import com.ezsign.feb.house.vo.YE103VO;
import com.ezsign.feb.house.vo.YE105VO;
import com.ezsign.feb.house.vo.YE106VO;
import com.ezsign.feb.house.vo.YE107VO;
import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.house.vo.YE109VO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.service.FebMasterService;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.other.dao.YE201DAO;
import com.ezsign.feb.other.dao.YE202DAO;
import com.ezsign.feb.other.dao.YE203DAO;
import com.ezsign.feb.other.dao.YE204DAO;
import com.ezsign.feb.other.vo.YE202VO;
import com.ezsign.feb.pension.dao.YE301DAO;
import com.ezsign.feb.pension.dao.YE302DAO;
import com.ezsign.feb.pension.vo.YE301VO;
import com.ezsign.feb.pension.vo.YE302VO;
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
import com.ezsign.feb.special.vo.YE408VO;
import com.ezsign.feb.system.dao.YS000DAO;
import com.ezsign.feb.system.dao.YS030DAO;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.worker.dao.YE001DAO;
import com.ezsign.feb.worker.dao.YE003DAO;
import com.ezsign.feb.worker.dao.YE052DAO;
import com.ezsign.feb.worker.dao.YE700DAO;
import com.ezsign.feb.worker.dao.YE750DAO;
import com.ezsign.feb.worker.service.YE750Service;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.feb.worker.vo.YE052VO;
import com.ezsign.feb.worker.vo.YE700VO;
import com.ezsign.feb.worker.vo.YE750Response;
import com.ezsign.feb.worker.vo.YE750VO;
import com.ezsign.framework.exception.AngullarProcessException;
import com.ezsign.framework.mail.MailVO;
import com.ezsign.framework.mail.MultiPartEmail;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.CodeUtils;
import com.ezsign.window.service.YW800Service;
import com.ezsign.window.vo.DefaultResponse;
import com.ezsign.window.vo.WorkMonth;
import com.ezsign.window.vo.YW800Response;
import com.jarvis.pdf.util.ControlUtil;
import com.jarvis.pdf.util.FieldUtil;
import com.jarvis.pdf.vo.FieldVO;

@SuppressWarnings({"AccessStaticViaInstance", "NonAsciiCharacters"})
@Service("ye750Service")
public class YE750ServiceImpl implements YE750Service {

    Logger logger = Logger.getLogger(this.getClass());

	@Resource(name="bizDAO")
	private BizDAO bizDAO;
	
    @Resource(name = "empDAO")
    private EmpDAO empDAO;

    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;

    @Resource(name = "ye001DAO")
    private YE001DAO ye001DAO;

    @Resource(name = "ye003DAO")
    private YE003DAO ye003DAO;

    @Resource(name = "ys000DAO")
    private YS000DAO ys000DAO;

    @Resource(name = "ys030DAO")
    private YS030DAO ys030DAO;

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
    
    @Resource(name = "ye106DAO")
    private YE106DAO ye106DAO;
    
    @Resource(name = "ye107DAO")
    private YE107DAO ye107DAO;
    
    @Resource(name = "ye108DAO")
    private YE108DAO ye108DAO;
    
    @Resource(name = "ye109DAO")
    private YE109DAO ye109DAO;
    
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

    @Resource(name = "ye405DAO")
    private YE405DAO ye405DAO;
    
    @Resource(name = "ye408DAO")
    private YE408DAO ye408DAO;
    
    @Resource(name = "ye503DAO")
    private YE503DAO ye503DAO;

    @Resource(name = "ye700DAO")
    private YE700DAO ye700DAO;

    @Resource(name = "ye750DAO")
    private YE750DAO ye750DAO;

    @Resource(name = "contentService")
    private ContentService contentService;

    @Resource(name = "contentDAO")
    private ContentDAO contentDAO;

    @Resource(name = "cabinetDAO")
    private CabinetDAO cabinetDAO;
    
    @Resource(name = "codeDAO")
    private CodeDAO codeDAO;
    
    @Resource(name = "yw800Service")
    private YW800Service yw800Service;
    
    @Resource(name = "ye750Service")
    private YE750Service ye750Service;
    
    @Resource(name = "febMasterService")
    private FebMasterService febMasterService;

    private DecimalFormat df = new DecimalFormat("#,###");

    @Override
    public void getYE750List(String bizId, String 계약ID, String 사용자ID, YE750Response response) throws Exception {
        YS000VO ys000VO = new YS000VO();
        ys000VO.setBizId(bizId);
        List<YS000VO> listYS000 = ys000DAO.getYS000List(ys000VO);

        response.febYears = new ArrayList<>();
        if (listYS000 != null && listYS000.size() > 0) {
            for (YS000VO vo : listYS000) {
                YE750Response.FebYear febYear = new YE750Response.FebYear();
                febYear.계약년도 = vo.getFebYear();
                febYear.계약ID = vo.get계약ID();
                response.febYears.add(febYear);
            }
        }

        EmpVO empVO = new EmpVO();
        empVO.setBizId(bizId);
        empVO.setUserId(사용자ID);
        EmpVO resultEmpVO = empDAO.getEmp(empVO);
        response.phoneNum = resultEmpVO.getPhoneNum();

        YE750VO ye750VO = new YE750VO();
        ye750VO.set계약ID(계약ID);
        ye750VO.set사용자ID(사용자ID);

        response.list = ye750DAO.getYE750List(ye750VO);
    }

    @Override
    public String[] getYE750FilePath(String fileId) throws Exception {
        ContentVO contentVO = new ContentVO();
        contentVO.setFileId(fileId);

        // 콘텐츠에서 정보 추출
        ContentVO resultContentVO = contentDAO.getContent(contentVO);
        if (resultContentVO == null) {
            System.out.println("[getYE750FilePath] 콘텐츠 정보가 존재하지 않습니다.");
            return null;
        }

        // 저장위치
        CabinetVO cabinetVO = new CabinetVO();
        cabinetVO.setClassId(resultContentVO.getClassId());
        cabinetVO.setCabinetId(resultContentVO.getCabinetId());
        CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
        if (cabinetResultVO == null) {
            System.out.println("[getYE750FilePath] 캐비넷 정보가 존재하지 않습니다.");
            return null;
        }

        String filePath = cabinetResultVO.getCabinetPath() + resultContentVO.getFilePath() + resultContentVO.getFileName();
        System.out.println(filePath);

        return new String[]{filePath, resultContentVO.getOrgFileName()};
    }


    @Override
    public int allDelYE750(YE750VO ye750VO) throws Exception {
        int result = 0;

        List<YE750VO> listYE750 = ye750DAO.getYE750List(ye750VO);

        if (listYE750 != null && listYE750.size() > 0) {
            List<ContentVO> contentList = new ArrayList<>();

            for (YE750VO vo : listYE750) {
                ContentVO contentVO = new ContentVO();
                contentVO.setFileId(vo.get파일ID());

                contentList.add(contentVO);
            }

            // 실제 원본 파일만 삭제  DB는 사용여부 N
            contentService.deleteContent(contentList);
            result = ye750DAO.allDelYE750(ye750VO);
            System.out.println("ye750DAO.allDelYE750: " + result);
        }

        return result;
    }

    @Override
    public void createYE750Pdf(String bizId, String 계약ID, String 사용자ID, YW800Response request) throws Exception {
        // /Users/lims/Documents/DAONSNC/funlab/KFORM_DATA/FEB/YE750/
        YE750VO ye750VO = new YE750VO();
        ye750VO.set계약ID(계약ID);
        ye750VO.set사용자ID(사용자ID);

        allDelYE750(ye750VO);
		
        ye750VO.set출력물파일구분코드("Y01");
        createY01PDF(bizId, ye750VO);
		
        ye750VO.set출력물파일구분코드("Y02");
        createY02PDF(bizId, ye750VO, request);
		
        ye750VO.set출력물파일구분코드("Y03");
        createY03PDF(bizId, ye750VO);
		
        ye750VO.set출력물파일구분코드("Y04");
        createY04PDF(bizId, ye750VO);
    }


    private void createY01PDF(String bizId, YE750VO ye750VO) throws Exception {
        ContentVO contentVO = new ContentVO();
        contentVO.setFileId(YE750Service.Y01_FILE_ID);

        // 콘텐츠에서 정보 추출
        ContentVO contentResultVO = contentDAO.getContent(contentVO);
        if (contentResultVO == null) {
            System.out.println("[createY01PDF] 콘텐츠 정보가 존재하지 않습니다.");
            return;
        }

        // 저장위치
        CabinetVO cabinetVO = new CabinetVO();
        cabinetVO.setClassId(contentResultVO.getClassId());
        cabinetVO.setCabinetId(contentResultVO.getCabinetId());

        CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
        if (cabinetResultVO == null) {
            System.out.println("[createY01PDF] 캐비넷 정보가 존재하지 않습니다.");
            return;
        }

        String originalPdfPath = cabinetResultVO.getCabinetPath() + contentResultVO.getFilePath() + contentResultVO.getFileName();
        String targetPdfName = StringUtil.getUUID() + ".pdf";
        String targetPdfPath = MultipartFileUtil.getSystemTempPath() + targetPdfName;

        System.out.println(originalPdfPath);
        System.out.println(targetPdfPath);

        // 사용할 PDF복사
        FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
        if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
            System.out.println("[createY01PDF] userId=>" + ye750VO.get사용자ID());

            YE000VO ye000VO = new YE000VO();
            ye000VO.setBizId(bizId);
            ye000VO.set계약ID(ye750VO.get계약ID());
            ye000VO.set사용자ID(ye750VO.get사용자ID());
            YE000VO resultYE000VO = ye000DAO.getYE000(ye000VO);

            if (resultYE000VO != null) {
                List<FieldVO> fieldListVO = new ArrayList<>();
                String today = DateUtil.getTimeStamp(3);

                if ("1".equals(resultYE000VO.get거주구분())) {
                    setPdfField(fieldListVO, "거주구분_거주자1", "O");
                } else {
                    setPdfField(fieldListVO, "거주구분_비거주자2", "O");
                }
                setPdfField(fieldListVO, "거주지국", resultYE000VO.get거주지국());
                setPdfField(fieldListVO, "거주지국코드", resultYE000VO.get거주지국코드());

                if ("1".equals(resultYE000VO.get내외국인구분())) {
                    setPdfField(fieldListVO, "내외국인_내국인1", "O");
                } else {
                    setPdfField(fieldListVO, "내외국인_외국인9", "O");
                }
                if ("1".equals(resultYE000VO.get외국인단일세율적용())) {
                    setPdfField(fieldListVO, "외국인단일세율_여1", "O");
                } else {
                    setPdfField(fieldListVO, "외국인단일세율_부2", "O");
                }
                if ("1".equals(resultYE000VO.get외국법인파견근로자())) {
                    setPdfField(fieldListVO, "외국법인파견근로_여1", "O");
                } else {
                    setPdfField(fieldListVO, "외국법인파견근로_부2", "O");
                }

                setPdfField(fieldListVO, "국적", resultYE000VO.get국적());
                setPdfField(fieldListVO, "국적코드", resultYE000VO.get국가코드());

                if ("1".equals(resultYE000VO.get세대주여부())) {
                    setPdfField(fieldListVO, "세대주여부_세대주1", "O");
                } else {
                    setPdfField(fieldListVO, "세대주여부_세대원2", "O");
                }

                setPdfField(fieldListVO, "소득자성명", resultYE000VO.getEmpName());
                setPdfField(fieldListVO, "소득자주민번호", formatIdentity(resultYE000VO.get개인식별번호(), true));
                setPdfField(fieldListVO, "소득자주소", resultYE000VO.getAddress1() + " " + resultYE000VO.getAddress2());

                YS030VO ys030VO = new YS030VO();
                ys030VO.set계약ID(ye750VO.get계약ID());
                ys030VO.set사업장ID(resultYE000VO.get사업장ID());
                ys030VO.setStartPage(0);
                ys030VO.setEndPage(9999);
                List<YS030VO> listYS030 = ys030DAO.getYS030List(ys030VO);
                if (listYS030.size() > 0) {
                    setPdfField(fieldListVO, "법인명", listYS030.get(0).get사업장명());
                    setPdfField(fieldListVO, "대표자성명", listYS030.get(0).get대표자명());
                    setPdfField(fieldListVO, "사업자등록번호", formatBusinessNo(listYS030.get(0).get사업자등록번호()));
                    setPdfField(fieldListVO, "주민등록번호", formatIdentity(listYS030.get(0).get법인등록번호_개인식별번호(), false));

                    if ("1".equals(listYS030.get(0).get단위과세자여부())) {
                        setPdfField(fieldListVO, "사업자단위과세_여1", "O");
                    } else {
                        setPdfField(fieldListVO, "사업자단위과세_부2", "O");
                    }
                    setPdfField(fieldListVO, "종사업장일련번호", listYS030.get(0).get종사업자일련번호());
                    setPdfField(fieldListVO, "사업장소재지", listYS030.get(0).get회사주소1());

                    setPdfField(fieldListVO, "영수일_서명", listYS030.get(0).get대표자명());
                }

                setPdfField(fieldListVO, "영수일_년", today.substring(0, 4));
                setPdfField(fieldListVO, "영수일_월", today.substring(4, 6));
                setPdfField(fieldListVO, "영수일_일", today.substring(6, 8));

                YS000VO ys000VO = new YS000VO();
                ys000VO.setBizId(bizId);
                ys000VO.set계약ID(ye750VO.get계약ID());
                List<YS000VO> listYS000 = ys000DAO.getYS000List(ys000VO);
                String 계약년도 = "";
                if (listYS000.size() > 0) {
                    계약년도 = listYS000.get(0).getFebYear();
                }

                List<YE000VO> listYE000 = ye003DAO.getYE003List(ye000VO);
                if (listYE000 != null && listYE000.size() > 0) {
                    Map<String, Long> sum = new HashMap<>();
                    sum.put("소득명세합계_급여", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_상여", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_인정상여", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_매수선택행사이익", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_우리사주인출금", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_퇴직소득한도초과", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_직무발명보상금_과세", StringUtil.strPaserLong("0"));
                    
                    sum.put("소득명세합계_국외근로", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_야간근로수당", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_출산보육수당", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_연구보조비", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_수련보조수당", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_비과세소득계", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_감면소득계", StringUtil.strPaserLong("0"));
                    
                    sum.put("소득명세합계_비과세학자금", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_취재수당", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_벽지수당", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_재해관련급여", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_무보수위원수당", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_외국주둔군인등", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_주식매수선택권", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_우리사주조합인출금50", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_우리사주조합인출금75", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_우리사주조합인출금100", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_경호승선수당", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_외국정부등근무자", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_근로장학금", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_보육교사근무환경개선비", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_사립유치원수석교사인건비", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_정부공공기관종사자이주수당", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_종교활동비", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_벤처기업주식매수선택권", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_직무발명보상금", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_유아초중등연구보조비", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_고등교육법연구보조비", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_수련보조수당", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_비과세소득계", StringUtil.strPaserLong("0"));
                    sum.put("소득명세합계_감면소득계", StringUtil.strPaserLong("0"));
                    
                    int index = 1;
                    for (YE000VO vo : listYE000) {
                    	
                    	logger.info("# 세액명세 VO : " + vo );
                    	logger.info("# vo.get근무지구분() : " + vo.get근무지구분() );
                    	logger.info("# index : " + index );
                    	
                        if ("1".equals(vo.get근무지구분())) {
                            if (TextUtils.isEmpty(vo.get근무종료일()) || vo.get근무종료일().compareTo(계약년도 + "1231") > 0) {
                                setPdfField(fieldListVO, "연말구분_계속근무1", "O");
                            } else {
                                setPdfField(fieldListVO, "연말구분_중도퇴사2", "O");
                            }

                            set원천징수("현근무지_", 계약년도, fieldListVO, vo, sum);
                            sum원천징수(계약년도, vo, sum);
                        } else if ("3".equals(vo.get근무지구분())) {
                            set원천징수("납세조합_", 계약년도, fieldListVO, vo, sum);
                            sum원천징수(계약년도, vo, sum);
                        } else {
                            
                            set원천징수("종전근무지" + index + "_", 계약년도, fieldListVO, vo, sum);
                            index++;
                        
                            sum원천징수(계약년도, vo, sum);
                        }
                    }

                    setPdfField(fieldListVO, "소득명세합계_급여", sum.get("소득명세합계_급여"));
                    setPdfField(fieldListVO, "소득명세합계_상여", sum.get("소득명세합계_상여"));
                    setPdfField(fieldListVO, "소득명세합계_인정상여", sum.get("소득명세합계_인정상여"));
                    setPdfField(fieldListVO, "소득명세합계_매수선택행사이익", sum.get("소득명세합계_매수선택행사이익"));
                    setPdfField(fieldListVO, "소득명세합계_우리사주인출금", sum.get("소득명세합계_우리사주인출금"));
                    setPdfField(fieldListVO, "소득명세합계_퇴직소득한도초과", sum.get("소득명세합계_퇴직소득한도초과"));
                    setPdfField(fieldListVO, "소득명세합계_직무발명보상금_과세", sum.get("소득명세합계_직무발명보상금_과세"));

                    long 소득명세합계_소득명세계 = sum.get("소득명세합계_급여") + sum.get("소득명세합계_상여") + sum.get("소득명세합계_인정상여") + sum.get("소득명세합계_매수선택행사이익") + sum.get("소득명세합계_우리사주인출금") + sum.get("소득명세합계_퇴직소득한도초과") + sum.get("소득명세합계_직무발명보상금_과세");

                    setPdfField(fieldListVO, "소득명세합계_소득명세계", 소득명세합계_소득명세계);
                    
                    setPdfField(fieldListVO, "소득명세합계_국외근로", sum.get("소득명세합계_국외근로"));
                    setPdfField(fieldListVO, "소득명세합계_야간근로수당", sum.get("소득명세합계_야간근로수당"));
                    setPdfField(fieldListVO, "소득명세합계_출산보육수당", sum.get("소득명세합계_출산보육수당"));
                    setPdfField(fieldListVO, "소득명세합계_연구보조비", sum.get("소득명세합계_연구보조비"));
                    setPdfField(fieldListVO, "소득명세합계_비과세학자금", sum.get("소득명세합계_비과세학자금"));
                    setPdfField(fieldListVO, "소득명세합계_취재수당", sum.get("소득명세합계_취재수당"));
                    setPdfField(fieldListVO, "소득명세합계_벽지수당", sum.get("소득명세합계_벽지수당"));
                    setPdfField(fieldListVO, "소득명세합계_재해관련급여", sum.get("소득명세합계_재해관련급여"));
                    setPdfField(fieldListVO, "소득명세합계_무보수위원수당", sum.get("소득명세합계_무보수위원수당"));
                    setPdfField(fieldListVO, "소득명세합계_외국주둔군인등", sum.get("소득명세합계_외국주둔군인등"));
                    setPdfField(fieldListVO, "소득명세합계_주식매수선택권", sum.get("소득명세합계_주식매수선택권"));
                    setPdfField(fieldListVO, "소득명세합계_우리사주조합인출금50", sum.get("소득명세합계_우리사주조합인출금50"));
                    setPdfField(fieldListVO, "소득명세합계_우리사주조합인출금75", sum.get("소득명세합계_우리사주조합인출금75"));
                    setPdfField(fieldListVO, "소득명세합계_우리사주조합인출금100", sum.get("소득명세합계_우리사주조합인출금100"));
                    setPdfField(fieldListVO, "소득명세합계_경호승선수당", sum.get("소득명세합계_경호승선수당"));
                    setPdfField(fieldListVO, "소득명세합계_외국정부등근무자", sum.get("소득명세합계_외국정부등근무자"));
                    setPdfField(fieldListVO, "소득명세합계_근로장학금", sum.get("소득명세합계_근로장학금"));
                    setPdfField(fieldListVO, "소득명세합계_보육교사근무환경개선비", sum.get("소득명세합계_보육교사근무환경개선비"));
                    setPdfField(fieldListVO, "소득명세합계_사립유치원수석교사인건비", sum.get("소득명세합계_사립유치원수석교사인건비"));
                    setPdfField(fieldListVO, "소득명세합계_정부공공기관종사자이주수당", sum.get("소득명세합계_정부공공기관종사자이주수당"));
                    setPdfField(fieldListVO, "소득명세합계_종교활동비", sum.get("소득명세합계_종교활동비"));
                    setPdfField(fieldListVO, "소득명세합계_벤처기업주식매수선택권", sum.get("소득명세합계_벤처기업주식매수선택권"));
                    setPdfField(fieldListVO, "소득명세합계_직무발명보상금", sum.get("소득명세합계_직무발명보상금"));
                    setPdfField(fieldListVO, "소득명세합계_유아초중등연구보조비", sum.get("소득명세합계_유아초중등연구보조비"));
                    setPdfField(fieldListVO, "소득명세합계_고등교육법연구보조비", sum.get("소득명세합계_고등교육법연구보조비"));
                    setPdfField(fieldListVO, "소득명세합계_수련보조수당", sum.get("소득명세합계_수련보조수당"));
                    setPdfField(fieldListVO, "소득명세합계_비과세소득계", sum.get("소득명세합계_비과세소득계"));
                    setPdfField(fieldListVO, "소득명세합계_감면소득계", sum.get("소득명세합계_감면소득계"));
                    
                }

                YE700VO ye700VO = new YE700VO();
                ye700VO.set계약ID(ye750VO.get계약ID());
                ye700VO.set사용자ID(ye750VO.get사용자ID());
                ye700VO.set사용여부("1");
                List<YE700VO> listYE700 = ye700DAO.getYE700(ye700VO);
                if (listYE700.size() > 0) {
                    YE700VO vo = listYE700.get(0);
                    setPdfField(fieldListVO, "결정세액_소득세", vo.get결정세액_소득세());
                    setPdfField(fieldListVO, "결정세액_지방소득세", vo.get결정세액_지방소득세());
                    setPdfField(fieldListVO, "결정세액_농어촌세", vo.get결정세액_농어촌특별세());

                    setPdfField(fieldListVO, "종전근무지1_사업자번호", formatBusinessNo(vo.get종1_사업자번호()));
                    setPdfField(fieldListVO, "종전근무지2_사업자번호", formatBusinessNo(vo.get종2_사업자번호()));
                    setPdfField(fieldListVO, "종전근무지3_사업자번호", formatBusinessNo(vo.get종3_사업자번호()));

                    setPdfField(fieldListVO, "종전근무지1_기납부소득세", vo.get종1_소득세());
                    setPdfField(fieldListVO, "종전근무지2_기납부소득세", vo.get종2_소득세());
                    setPdfField(fieldListVO, "종전근무지3_기납부소득세", vo.get종3_소득세());

                    setPdfField(fieldListVO, "종전근무지1_기납부지방소득세", vo.get종1_지방소득세());
                    setPdfField(fieldListVO, "종전근무지2_기납부지방소득세", vo.get종2_지방소득세());
                    setPdfField(fieldListVO, "종전근무지3_기납부지방소득세", vo.get종3_지방소득세());

                    setPdfField(fieldListVO, "종전근무지1_기납부농어촌세", vo.get종1_농어촌특별세());
                    setPdfField(fieldListVO, "종전근무지2_기납부농어촌세", vo.get종2_농어촌특별세());
                    setPdfField(fieldListVO, "종전근무지3_기납부농어촌세", vo.get종3_농어촌특별세());

                    setPdfField(fieldListVO, "현근무지_기납부소득세", vo.get주근무지_소득세());
                    setPdfField(fieldListVO, "현근무지_기납부지방소득세", vo.get주근무지_지방소득세());
                    setPdfField(fieldListVO, "현근무지_기납부농어촌세", vo.get주근무지_농어촌특별세());

                    setPdfField(fieldListVO, "납부특례_소득세", vo.get납부특례_소득세());
                    setPdfField(fieldListVO, "납부특례_지방소득세", vo.get납부특례_지방소득세());
                    setPdfField(fieldListVO, "납부특례_농어촌세", vo.get납부특례_농어촌특별세());

                    // 차감징수 금액 문자열로 변환(음수값 표시)
                    setPdfField(fieldListVO, "차감징수_소득세", StringUtil.toNumFormat(vo.get차감징수_소득세()));
                    setPdfField(fieldListVO, "차감징수_지방소득세", StringUtil.toNumFormat(vo.get차감징수_지방소득세()));
                    setPdfField(fieldListVO, "차감징수_농어촌세", StringUtil.toNumFormat(vo.get차감징수_농어촌특별세()));

                    setPdfField(fieldListVO, "총급여", vo.get총급여());
                    setPdfField(fieldListVO, "근로소득공제", vo.get근로소득공제());
                    setPdfField(fieldListVO, "근로소득금액", vo.get근로소득금액());
                    setPdfField(fieldListVO, "기본공제_본인", vo.get기본공제_본인());
                    setPdfField(fieldListVO, "기본공제_배우자", vo.get기본공제_배우자());
                    setPdfField(fieldListVO, "기본공제_부양가족", vo.get기본공제_부양가족());
                    setPdfField(fieldListVO, "부양가족_인원수", vo.get부양가족_인원수());

                    setPdfField(fieldListVO, "추가공제_경로우대", vo.get추가공제_경로우대());
                    setPdfField(fieldListVO, "추가공제_장애인", vo.get추가공제_장애인());
                    setPdfField(fieldListVO, "추가공제_부녀자", vo.get추가공제_부녀자());
                    setPdfField(fieldListVO, "추가공제_한부모", vo.get추가공제_한부모());
                    setPdfField(fieldListVO, "경로우대_인원수", vo.get경로우대_인원수());
                    setPdfField(fieldListVO, "장애인_인원수", vo.get장애인_인원수());

                    setPdfField(fieldListVO, "연금보험공제_국민연금_대상금액", vo.get연금보험_국민연금_공제대상금액());
                    setPdfField(fieldListVO, "연금보험공제_공무원연금_대상금액", vo.get연금보험_공무원연금_공제대상금액());
                    setPdfField(fieldListVO, "연금보험공제_군인연금_대상금액", vo.get연금보험_군인연금_공제대상금액());
                    setPdfField(fieldListVO, "연금보험공제_사립학교연금_대상금액", vo.get연금보험_사립학교_공제대상금액());
                    setPdfField(fieldListVO, "연금보험공제_별정우체국연금_대상금액", vo.get연금보험_별정우체국_공제대상금액());

                    setPdfField(fieldListVO, "연금보험공제_국민연금_공제금액", vo.get연금보험_국민연금());
                    setPdfField(fieldListVO, "연금보험공제_공무원연금_공제금액", vo.get연금보험_공무원연금());
                    setPdfField(fieldListVO, "연금보험공제_군인연금_공제금액", vo.get연금보험_군인연금());
                    setPdfField(fieldListVO, "연금보험공제_사립학교연금_공제금액", vo.get연금보험_사립학교());
                    setPdfField(fieldListVO, "연금보험공제_별정우체국연금_공제금액", vo.get연금보험_별정우체국());
                    
                    setPdfField(fieldListVO, "특별소득공제_건강보험료_공제금액", vo.get특별소득_건강보험());
                    setPdfField(fieldListVO, "특별소득공제_고용보험료_공제금액", vo.get특별소득_고용보험());
                    
                    setPdfField(fieldListVO, "특별소득공제_건강보험료_대상금액", vo.get특별소득_건강보험_공제대상금액());
                    setPdfField(fieldListVO, "특별소득공제_고용보험료_대상금액", vo.get특별소득_고용보험_공제대상금액());
                    setPdfField(fieldListVO, "특별소득공제_주택입차원리금_대출기관", vo.get특별소득_주택원리금_대출기관());
                    setPdfField(fieldListVO, "특별소득공제_주택입차원리금_거주자", vo.get특별소득_주택원리금_거주자());

                    setPdfField(fieldListVO, "특별소득공제_장기주택이자상환_2011이전_15년미만", vo.get특별소득_장기주택_2011년이전_15년미만());
                    setPdfField(fieldListVO, "특별소득공제_장기주택이자상환_2011이전_15년29년", vo.get특별소득_장기주택_2011년이전_15년29년());
                    setPdfField(fieldListVO, "특별소득공제_장기주택이자상환_2011이전_30년이상", vo.get특별소득_장기주택_2011년이전_30년이상());
                    setPdfField(fieldListVO, "특별소득공제_장기주택이자상환_2012이후_고정금리비거치상환대출", vo.get특별소득_장기주택_2012년이후_고정이거나비거치());
                    setPdfField(fieldListVO, "특별소득공제_장기주택이자상환_2012이후_그밖에대출", vo.get특별소득_장기주택_2012년이후_그밖에());
                    setPdfField(fieldListVO, "특별소득공제_장기주택이자상환_15년이후_15년이상_고정금리면서비거치상환대출", vo.get특별소득_장기주택_2015년이후_15년이상_고정이면서비거치());
                    setPdfField(fieldListVO, "특별소득공제_장기주택이자상환_15년이후_15년이상_고정금리이거나비거치상환대출", vo.get특별소득_장기주택_2015년이후_15년이상_고정이거나비거치());
                    setPdfField(fieldListVO, "특별소득공제_장기주택이자상환_15년이후_15년이상_그밖에대출", vo.get특별소득_장기주택_2015년이후_15년이상_그밖에());
                    setPdfField(fieldListVO, "특별소득공제_장기주택이자상환_15년이후_10년15년_고정금리이거나비거치상환대출", vo.get특별소득_장기주택_2015년이후_10년15년_고정이거나비거치());
                    setPdfField(fieldListVO, "특별소득공제_기부금(이월분)", vo.get특별소득_기부금_이월분());
                    setPdfField(fieldListVO, "특별소득공제_계", vo.get특별소득_계());

                    setPdfField(fieldListVO, "차감소득금액", vo.get차감소득금액());

                    setPdfField(fieldListVO, "그밖에소득공제_개인연금저축", vo.get그밖에소득공제_개인연금());
                    setPdfField(fieldListVO, "그밖에소득공제_소기업소상공인", vo.get그밖에소득공제_소기업소상공인());
                    setPdfField(fieldListVO, "그밖에소득공제_주택마련_청약저축", vo.get그밖에소득공제_주택마련_청약저축());
                    setPdfField(fieldListVO, "그밖에소득공제_주택마련_주택청약종합", vo.get그밖에소득공제_주택마련_주택청약());
                    setPdfField(fieldListVO, "그밖에소득공제_주택마련_근로자주택마련", vo.get그밖에소득공제_주택마련_근로자주택마련());
                    setPdfField(fieldListVO, "그밖에소득공제_투자조합출자", vo.get그밖에소득공제_투자조합출자());
                    setPdfField(fieldListVO, "그밖에소득공제_신용카드사용액", vo.get그밖에소득공제_신용카드등());
                    setPdfField(fieldListVO, "그밖에소득공제_우리사주조합", vo.get그밖에소득공제_우리사주조합());
                    setPdfField(fieldListVO, "그밖에소득공제_고용유지근로자", vo.get그밖에소득공제_고용유지근로자());
                    setPdfField(fieldListVO, "그밖에소득공제_장기집합투자", vo.get그밖에소득공제_장기집합투자());
                    setPdfField(fieldListVO, "그밖에소득공제_계", vo.get그밖에소득공제_계());

                    setPdfField(fieldListVO, "소득공제종합한도초과액", vo.get소득공제종합한도초과액());

                    setPdfField(fieldListVO, "종합소득과세표준", vo.get종합소득과세표준());

                    setPdfField(fieldListVO, "산출세액", vo.get산출세액());

                    setPdfField(fieldListVO, "세액감면_소득세법", vo.get세액감면_소득세법());
                    setPdfField(fieldListVO, "세액감면_조세특례52제외", vo.get세액감면_조세특례52제외());
                    setPdfField(fieldListVO, "세액감면_조세특례30조", vo.get세액감면_조세특례30조());
                    setPdfField(fieldListVO, "세액감면_조세조약", vo.get세액감면_조세조약());
                    setPdfField(fieldListVO, "세액감면_계", vo.get세액감면_계());

                    setPdfField(fieldListVO, "세액공제_근로소득", vo.get세액공제_근로소득());
                    setPdfField(fieldListVO, "세액공제_자녀공제", vo.get세액공제_자녀());
                    setPdfField(fieldListVO, "세액공제_6세이하", vo.get세액공제_6세이하());
                    setPdfField(fieldListVO, "세액공제_출산입양", vo.get세액공제_출산입양());
                    setPdfField(fieldListVO, "자녀_인원수", vo.get자녀_인원수());
                    setPdfField(fieldListVO, "6세이하_인원수", vo.get세액공제_6세이하_인원수());
                    setPdfField(fieldListVO, "출산입양_인원수", vo.get출산입양_인원수());

                    setPdfField(fieldListVO, "세액공제_연금계좌_과학기술_공제대상금액", vo.get세액공제_연금계좌_과학기술_공제대상금액());
                    setPdfField(fieldListVO, "세액공제_연금계좌_과학기술_세액공제액", vo.get세액공제_연금계좌_과학기술_세액공제액());
                    setPdfField(fieldListVO, "세액공제_연금계좌_퇴직연금_공제대상금액", vo.get세액공제_연금계좌_퇴직연금_공제대상금액());
                    setPdfField(fieldListVO, "세액공제_연금계좌_퇴직연금_세액공제액", vo.get세액공제_연금계좌_퇴직연금_세액공제액());
                    setPdfField(fieldListVO, "세액공제_연금계좌_연금저축_공제대상금액", vo.get세액공제_연금계좌_연금저축_공제대상금액());
                    setPdfField(fieldListVO, "세액공제_연금계좌_연금저축_세액공제액", vo.get세액공제_연금계좌_연금저축_세액공제액());

                    setPdfField(fieldListVO, "세액공제_특별세액_보험료_보장성_공제대상금액", vo.get특별세액_보험료_보장성_공제대상금액());
                    setPdfField(fieldListVO, "세액공제_특별세액_보험료_보장성_세액공제액", vo.get특별세액_보험료_보장성_세액공제액());
                    setPdfField(fieldListVO, "세액공제_특별세액_보험료_장애인전용_공제대상금액", vo.get특별세액_보험료_장애인전용_공제대상금액());
                    setPdfField(fieldListVO, "세액공제_특별세액_보험료_장애인전용_세액공제액", vo.get특별세액_보험료_장애인전용_세액공제액());
                    setPdfField(fieldListVO, "세액공제_특별세액_의료비_공제대상금액", vo.get특별세액_의료비_공제대상금액());
                    setPdfField(fieldListVO, "세액공제_특별세액_의료비_세액공제액", vo.get특별세액_의료비_세액공제액());
                    setPdfField(fieldListVO, "세액공제_특별세액_교육비_공제대상금액", vo.get특별세액_교육비_공제대상금액());
                    setPdfField(fieldListVO, "세액공제_특별세액_교육비_세액공제액", vo.get특별세액_교육비_세액공제액());

                    setPdfField(fieldListVO, "세액공제_특별세액_기부금_정치자금_10만이하_공제대상금액", vo.get특별세액_기부금_정치자금_10만이하_공제대상금액());
                    setPdfField(fieldListVO, "세액공제_특별세액_기부금_정치자금_10만이하_세액공제액", vo.get특별세액_기부금_정치자금_10만이하_세액공제액());
                    setPdfField(fieldListVO, "세액공제_특별세액_기부금_정치자금_10만초과_공제대상금액", vo.get특별세액_기부금_정치자금_10만초과_공제대상금액());
                    setPdfField(fieldListVO, "세액공제_특별세액_기부금_정치자금_10만초과_세액공제액", vo.get특별세액_기부금_정치자금_10만초과_세액공제액());
                    setPdfField(fieldListVO, "세액공제_특별세액_기부금_법정_공제대상금액", vo.get특별세액_기부금_법정_공제대상금액());
                    setPdfField(fieldListVO, "세액공제_특별세액_기부금_법정_세액공제액", vo.get특별세액_기부금_법정_세액공제액());
                    setPdfField(fieldListVO, "세액공제_특별세액_기부금_우리사주_공제대상금액", vo.get특별세액_기부금_우리사주_공제대상금액());
                    setPdfField(fieldListVO, "세액공제_특별세액_기부금_우리사주_세액공제액", vo.get특별세액_기부금_우리사주_세액공제액());
                    setPdfField(fieldListVO, "세액공제_특별세액_기부금_지정_종교외_공제대상금액", vo.get특별세액_기부금_지정_종교외_공제대상금액());
                    setPdfField(fieldListVO, "세액공제_특별세액_기부금_지정_종교외_세액공제액", vo.get특별세액_기부금_지정_종교외_세액공제액());
                    setPdfField(fieldListVO, "세액공제_특별세액_기부금_지정_종교_공제대상금액", vo.get특별세액_기부금_지정_종교_공제대상금액());
                    setPdfField(fieldListVO, "세액공제_특별세액_기부금_지정_종교_세액공제액", vo.get특별세액_기부금_지정_종교_세액공제액());
                    setPdfField(fieldListVO, "세액공제_특별세액_계", vo.get특별세액_계());
                    setPdfField(fieldListVO, "세액공제_특별세액_표준세액공제", vo.get특별세액_표준세액공제());

                    setPdfField(fieldListVO, "세액공제_납세조합공제", vo.get세액공제_납세조합공제());
                    setPdfField(fieldListVO, "세액공제_주택차입금", vo.get세액공제_주택차입금());
                    setPdfField(fieldListVO, "세액공제_외국납부", vo.get세액공제_외국납부());
                    setPdfField(fieldListVO, "세액공제_월세액_공제대상금액", vo.get세액공제_월세액_공제대상금액());
                    setPdfField(fieldListVO, "세액공제_월세액_세액공제액", vo.get세액공제_월세액_세액공제액());
                    setPdfField(fieldListVO, "세액공제_계", vo.get세액공제_계());
                    setPdfField(fieldListVO, "결정세액", vo.get결정세액_소득세());
                }
                Map<String, String> family = new HashMap<>();
                // 근로소득공제신고서 명칭 함수 호출
                // set인적공제(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                // set건강고용(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO);
                // set보험료(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                // set의료비(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                // set교육비(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                // set신용카드(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                // set기부금(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);

                set근로소득공제신고서_인적공제(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                set근로소득공제신고서_건강고용(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO);
                set근로소득공제신고서_보험료(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                set근로소득공제신고서_의료비(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                set근로소득공제신고서_교육비(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                set근로소득공제신고서_신용카드(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                set근로소득공제신고서_기부금(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                // 서명정보 표시
				BizVO bizVO = new BizVO();
				bizVO.setBizId(bizId);
				bizVO.setStartPage(0);
				bizVO.setEndPage(1);
				List<BizVO> bizList = bizDAO.getBizList(bizVO);
				if(bizList==null||bizList.size()==0) {
					logger.error("[setContractSignHashDataMulti] 기업정보가 존재하지 않습니다.");
				} else {

					String bizImage = "";
					// 이미지 생성
					if(StringUtil.isNotNull(bizList.get(0).getCompanyImage())&&!bizList.get(0).getCompanyImage().equals("-")) {
						bizImage = MultipartFileUtil.getSystemPath()+"images/sign/"+bizList.get(0).getCompanyImage();
						
						FieldVO fieldVO = new FieldVO();
						fieldVO.setId("sign_1");
						fieldVO.setImage(bizImage);
						fieldListVO.add(fieldVO);
					}

				}
				
				setPdfField(fieldListVO, "사번", resultYE000VO.getEmpNo());
				
				setPdfField(fieldListVO, "원천징수영수증_관리번호", resultYE000VO.get원천징수영수증_관리번호());

                // PDF영수증 파일명 : 사번_성명_귀속년도_영수증이름
                createPdfFile(
                        fieldListVO,
                        originalPdfPath, resultYE000VO.getEmpNo() + "_" + resultYE000VO.getEmpName() + "_" + 계약년도 + "_원천징수영수증.pdf",
                        targetPdfPath, targetPdfName,
                        resultYE000VO.getEmpName() + "_원천징수영수증",
                        ye750VO
                );
            }
        }
    }

    private void set원천징수(String fieldKey, String 계약년도, List<FieldVO> fieldListVO, YE000VO vo, Map<String, Long> sum) {

    	
        if (!TextUtils.isEmpty(vo.get근무종료일()) && vo.get근무종료일().compareTo(계약년도 + "0101") < 0) {
            return;
        }

        long 소득명세계 = 0;
        long 국외근로합계 = 0;
        long 연구보조비 = 0;
        long 비과세소득계 = 0;
        long 감면소득계 = 0;

        setPdfField(fieldListVO, fieldKey + "근무처명", vo.get회사명());
        setPdfField(fieldListVO, fieldKey + "등록번호", formatBusinessNo(vo.get사업자등록번호()));

        if (vo.get근무시작일().compareTo(계약년도 + "0101") < 0) {
            setPdfField(fieldListVO, fieldKey + "근무시작일", 계약년도 + ".01.01");
        } else {
            setPdfField(fieldListVO, fieldKey + "근무시작일", DateUtil.getDateText(1, vo.get근무시작일()));
        }

        if (TextUtils.isEmpty(vo.get근무종료일()) || vo.get근무종료일().compareTo(계약년도 + "1231") > 0) {
            setPdfField(fieldListVO, fieldKey + "근무종료일", 계약년도 + ".12.31");
        } else {
            setPdfField(fieldListVO, fieldKey + "근무종료일", DateUtil.getDateText(1, vo.get근무종료일()));
        }

        if (!TextUtils.isEmpty(vo.get감면종료일()) && vo.get감면종료일().compareTo(계약년도 + "0101") >= 0) {
            if (vo.get감면시작일().compareTo(계약년도 + "0101") < 0) {
                setPdfField(fieldListVO, fieldKey + "감면시작일", 계약년도 + ".01.01");
            } else {
                setPdfField(fieldListVO, fieldKey + "감면시작일", DateUtil.getDateText(1, vo.get감면시작일()));
            }

            if (vo.get감면종료일().compareTo(계약년도 + "1231") > 0) {
                setPdfField(fieldListVO, fieldKey + "감면종료일", 계약년도 + ".12.31");
            } else {
                setPdfField(fieldListVO, fieldKey + "감면종료일", DateUtil.getDateText(1, vo.get감면종료일()));
            }
        }

        if (!TextUtils.isEmpty(vo.get급여())) {
            setPdfField(fieldListVO, fieldKey + "급여", StringUtil.strPaserLong(vo.get급여()) + StringUtil.strPaserLong(vo.get비과세한도초과액()) - StringUtil.strPaserLong(vo.get차량비과세()));
            소득명세계 += StringUtil.strPaserLong(vo.get급여()) + StringUtil.strPaserLong(vo.get비과세한도초과액()) - StringUtil.strPaserLong(vo.get차량비과세());
        }

        if (!TextUtils.isEmpty(vo.get상여())) {
            setPdfField(fieldListVO, fieldKey + "상여", StringUtil.strPaserLong(vo.get상여()));
            소득명세계 += StringUtil.strPaserLong(vo.get상여());
        }
        if (!TextUtils.isEmpty(vo.get인정상여())) {
            setPdfField(fieldListVO, fieldKey + "인정상여", StringUtil.strPaserLong(vo.get인정상여()));
            소득명세계 += StringUtil.strPaserLong(vo.get인정상여());
        }
        if (!TextUtils.isEmpty(vo.get주식매수선택권행사이익())) {
            setPdfField(fieldListVO, fieldKey + "매수선택행사이익", StringUtil.strPaserLong(vo.get주식매수선택권행사이익()));
            소득명세계 += StringUtil.strPaserLong(vo.get주식매수선택권행사이익());
        }
        if (!TextUtils.isEmpty(vo.get우리사주조합인출금())) {
            setPdfField(fieldListVO, fieldKey + "우리사주인출금", StringUtil.strPaserLong(vo.get우리사주조합인출금()));
            소득명세계 += StringUtil.strPaserLong(vo.get우리사주조합인출금());
        }
        if (!TextUtils.isEmpty(vo.get임원퇴직소득금액한도초과액())) {
            setPdfField(fieldListVO, fieldKey + "퇴직소득한도초과", StringUtil.strPaserLong(vo.get임원퇴직소득금액한도초과액()));
            소득명세계 += StringUtil.strPaserLong(vo.get임원퇴직소득금액한도초과액());
        }
        if (!TextUtils.isEmpty(vo.get직무발명보상금())) {
            setPdfField(fieldListVO, fieldKey + "직무발명보상금_과세", StringUtil.strPaserLong(vo.get직무발명보상금()));
            소득명세계 += StringUtil.strPaserLong(vo.get직무발명보상금());
        }

        setPdfField(fieldListVO, fieldKey + "소득명세계", 소득명세계);

        if (!TextUtils.isEmpty(vo.getM01())) {  // 국외근로(월 100만원)
            // setPdfField(fieldListVO, fieldKey + "국외근로", StringUtil.strPaserLong(vo.getM01()));
            국외근로합계 += StringUtil.strPaserLong(vo.getM01());
            비과세소득계 += StringUtil.strPaserLong(vo.getM01());
        }
        if (!TextUtils.isEmpty(vo.getM02())) {  // 국외근로(월 300만원)
            // setPdfField(fieldListVO, fieldKey + "국외근로", StringUtil.strPaserLong(vo.getM02()));
        	국외근로합계 += StringUtil.strPaserLong(vo.getM02());
            비과세소득계 += StringUtil.strPaserLong(vo.getM02());
        }
        if (!TextUtils.isEmpty(vo.getM03())) {  // 국외근로(전액)
            // setPdfField(fieldListVO, fieldKey + "국외근로", StringUtil.strPaserLong(vo.getM03()));
        	국외근로합계 += StringUtil.strPaserLong(vo.getM03());
            비과세소득계 += StringUtil.strPaserLong(vo.getM03());
        }

        setPdfField(fieldListVO, fieldKey + "국외근로", 국외근로합계);

        // 비과세 소득계
        if (!TextUtils.isEmpty(vo.getO01())) {  // 야간근로수당(년 240만원)
            setPdfField(fieldListVO, fieldKey + "야간근로수당", StringUtil.strPaserLong(vo.getO01()));
            비과세소득계 += StringUtil.strPaserLong(vo.getO01());
        }
        if (!TextUtils.isEmpty(vo.getQ01())) {  // 출산보육수당(월 10만원)
            setPdfField(fieldListVO, fieldKey + "출산보육수당", StringUtil.strPaserLong(vo.getQ01()));
            비과세소득계 += StringUtil.strPaserLong(vo.getQ01());
        }
        if (!TextUtils.isEmpty(vo.getH08())) {  // 특별법 연구보조비(월 20만원)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getH08()));
        	연구보조비 += StringUtil.strPaserLong(vo.getH08());
            비과세소득계 += StringUtil.strPaserLong(vo.getH08());
        }
        if (!TextUtils.isEmpty(vo.getH09())) {  // 연구기관 등 연구보조비(월 20만원) 
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getH09()));
        	연구보조비 += StringUtil.strPaserLong(vo.getH09());
            비과세소득계 += StringUtil.strPaserLong(vo.getH09());
        }
        if (!TextUtils.isEmpty(vo.getH10())) {  // 기업부설연구소 연구보조비(월 20만원) 
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getH10()));
        	연구보조비 += StringUtil.strPaserLong(vo.getH10());
            비과세소득계 += StringUtil.strPaserLong(vo.getH10());
        }
        
        setPdfField(fieldListVO, fieldKey + "연구보조비", 연구보조비);

        if (!TextUtils.isEmpty(vo.getG01())) {  // 비과세학자금(납입금액) 
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getG01()));
            비과세소득계 += StringUtil.strPaserLong(vo.getG01());
        }
        if (!TextUtils.isEmpty(vo.getH11())) {  // 취재수당(월 20만원)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getH11()));
            비과세소득계 += StringUtil.strPaserLong(vo.getH11());
        }
        if (!TextUtils.isEmpty(vo.getH12())) {  // 벽지수당(월 20만원)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getH12()));
            비과세소득계 += StringUtil.strPaserLong(vo.getH12());
        }
        if (!TextUtils.isEmpty(vo.getH13())) {  // 재해관련급여(전액)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getH13()));
            비과세소득계 += StringUtil.strPaserLong(vo.getH13());
        }
        if (!TextUtils.isEmpty(vo.getH01())) {  // 무보수위원수당(전액)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getH01()));
            비과세소득계 += StringUtil.strPaserLong(vo.getH01());
        }
        if (!TextUtils.isEmpty(vo.getK01())) {  // 외국주둔군인등(전액)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getK01()));
            비과세소득계 += StringUtil.strPaserLong(vo.getK01());
        }
        if (!TextUtils.isEmpty(vo.getS01())) {  // 주식매수선택권(년 3,000만원) 
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getS01()));
            비과세소득계 += StringUtil.strPaserLong(vo.getS01());
        }
        if (!TextUtils.isEmpty(vo.getY02())) {  // 우리사주조합인출금50%(년 인출금의 50% 한도) 
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getY02()));
            비과세소득계 += StringUtil.strPaserLong(vo.getY02());
        }
        if (!TextUtils.isEmpty(vo.getY03())) {  // 우리사주조합인출금75%(년 인출금의 75% 한도)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getY03()));
            비과세소득계 += StringUtil.strPaserLong(vo.getY03());
        }
        if (!TextUtils.isEmpty(vo.getY04())) {  // 우리사주조합인출금100%(년 인출금의 100% 한도)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getY04()));
            비과세소득계 += StringUtil.strPaserLong(vo.getY04());
        }
        if (!TextUtils.isEmpty(vo.getH05())) {  // 경호･승선수당
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getH05()));
            비과세소득계 += StringUtil.strPaserLong(vo.getH05());
        }
        if (!TextUtils.isEmpty(vo.getI01())) {  // 외국정부등근무자(전액)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getI01()));
            비과세소득계 += StringUtil.strPaserLong(vo.getI01());
        }
        if (!TextUtils.isEmpty(vo.getR10())) {  // 근로장학금(전액)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getR10()));
            비과세소득계 += StringUtil.strPaserLong(vo.getR10());
        }
        if (!TextUtils.isEmpty(vo.getH14())) {  // 보육교사 근무환경개선비(전액) 
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getH14()));
            비과세소득계 += StringUtil.strPaserLong(vo.getH14());
        }
        if (!TextUtils.isEmpty(vo.getH15())) {  // 사립유치원수석교사･교사의 인건비(전액)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getH15()));
            비과세소득계 += StringUtil.strPaserLong(vo.getH15());
        }
        if (!TextUtils.isEmpty(vo.getH16())) {  // 정부･공공기관지방이전기관 종사자 이주수당(월 20만원)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getH16()));
            비과세소득계 += StringUtil.strPaserLong(vo.getH16());
        }
        if (!TextUtils.isEmpty(vo.getH17())) {  // 종교활동비
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getH17()));
            비과세소득계 += StringUtil.strPaserLong(vo.getH17());
        }
        if (!TextUtils.isEmpty(vo.getU01())) {  // 벤처기업 주식매수선택권(년 2,000만원)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getU01()));
            비과세소득계 += StringUtil.strPaserLong(vo.getU01());
        }
        if (!TextUtils.isEmpty(vo.getR11())) {  // 직무발명보상금(년 300만원) 
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getR11()));
            비과세소득계 += StringUtil.strPaserLong(vo.getR11());
        }
        if (!TextUtils.isEmpty(vo.getH06())) {  // 유아･초중등 연구보조비(월 20만원)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getH06()));
            비과세소득계 += StringUtil.strPaserLong(vo.getH06());
        }
        if (!TextUtils.isEmpty(vo.getH07())) {  // 고등교육법 연구보조비(월 20만원)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getH07()));
            비과세소득계 += StringUtil.strPaserLong(vo.getH07());
        }
        if (!TextUtils.isEmpty(vo.getY22())) {  // 전공의 수련 보조 수당
            setPdfField(fieldListVO, fieldKey + "수련보조수당", StringUtil.strPaserLong(vo.getY22()));
            비과세소득계 += StringUtil.strPaserLong(vo.getY22());
        }
        
        setPdfField(fieldListVO, fieldKey + "비과세소득계", 비과세소득계);

        // 비과세 감면소득계
        if (!TextUtils.isEmpty(vo.getT01())) {  // 외국인기술자(년 근로소득세의 50% 한도)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getT01()));
        	감면소득계 += StringUtil.strPaserLong(vo.getT01());
        }
        if (!TextUtils.isEmpty(vo.getT10())) {  // 중소기업취업청년 소득세 감면100%(소득세의 100%)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getT10()));
            감면소득계 += StringUtil.strPaserLong(vo.getT10());
        }
        if (!TextUtils.isEmpty(vo.getT11())) {  // 중소기업취업청년 소득세 감면50%(소득세의 50%) 
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getT11()));
            감면소득계 += StringUtil.strPaserLong(vo.getT11());
        }
        if (!TextUtils.isEmpty(vo.getT12())) {  // 중소기업취업청년 소득세 감면70%(소득세의 70%)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getT12()));
            감면소득계 += StringUtil.strPaserLong(vo.getT12());
        }
        if (!TextUtils.isEmpty(vo.getT13())) {  // 중소기업취업청년 소득세 감면90%(소득세의 90%)
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getT13()));
            감면소득계 += StringUtil.strPaserLong(vo.getT13());
        }
        if (!TextUtils.isEmpty(vo.getT20())) {  // 조세조약상 교직자감면
            // setPdfField(fieldListVO, fieldKey + "연구보조비", StringUtil.strPaserLong(vo.getT20()));
            감면소득계 += StringUtil.strPaserLong(vo.getT20());
        }

        setPdfField(fieldListVO, fieldKey + "감면소득계", 감면소득계);

        sum.put("소득명세합계_비과세소득계", sum.get("소득명세합계_비과세소득계") + 비과세소득계);
        sum.put("소득명세합계_감면소득계", sum.get("소득명세합계_감면소득계") + 감면소득계);
        
    }

    private void sum원천징수(String 계약년도, YE000VO vo, Map<String, Long> sum) {
        if (!TextUtils.isEmpty(vo.get근무종료일()) && vo.get근무종료일().compareTo(계약년도 + "0101") < 0) {
            return;
        }
        
        if (!TextUtils.isEmpty(vo.get급여())) {
            sum.put("소득명세합계_급여", sum.get("소득명세합계_급여") + StringUtil.strPaserLong(vo.get급여()) + StringUtil.strPaserLong(vo.get비과세한도초과액()) - StringUtil.strPaserLong(vo.get차량비과세()));
        }
        if (!TextUtils.isEmpty(vo.get상여())) {
            sum.put("소득명세합계_상여", sum.get("소득명세합계_상여") + StringUtil.strPaserLong(vo.get상여()));
        }
        if (!TextUtils.isEmpty(vo.get인정상여())) {
            sum.put("소득명세합계_인정상여", sum.get("소득명세합계_인정상여") + StringUtil.strPaserLong(vo.get인정상여()));
        }
        if (!TextUtils.isEmpty(vo.get주식매수선택권행사이익())) {
            sum.put("소득명세합계_매수선택행사이익", sum.get("소득명세합계_매수선택행사이익") + StringUtil.strPaserLong(vo.get주식매수선택권행사이익()));
        }
        if (!TextUtils.isEmpty(vo.get우리사주조합인출금())) {
            sum.put("소득명세합계_우리사주인출금", sum.get("소득명세합계_우리사주인출금") + StringUtil.strPaserLong(vo.get우리사주조합인출금()));
        }
        if (!TextUtils.isEmpty(vo.get임원퇴직소득금액한도초과액())) {
            sum.put("소득명세합계_퇴직소득한도초과", sum.get("소득명세합계_퇴직소득한도초과") + StringUtil.strPaserLong(vo.get임원퇴직소득금액한도초과액()));
        }
        if (!TextUtils.isEmpty(vo.get임원퇴직소득금액한도초과액())) {
            sum.put("소득명세합계_직무발명보상금_과세", sum.get("소득명세합계_직무발명보상금_과세") + StringUtil.strPaserLong(vo.get직무발명보상금()));
        }
        
        if (!TextUtils.isEmpty(vo.getM01())) {  // 국외근로(월 100만원)
        	// 국외근로합계 += StringUtil.strPaserLong(vo.getM01());
        	sum.put("소득명세합계_국외근로", sum.get("소득명세합계_국외근로") + StringUtil.strPaserLong(vo.getM01()));
        }
        if (!TextUtils.isEmpty(vo.getM02())) {  // 국외근로(월 300만원)
        	// 국외근로합계 += StringUtil.strPaserLong(vo.getM02());
        	sum.put("소득명세합계_국외근로", sum.get("소득명세합계_국외근로") + StringUtil.strPaserLong(vo.getM02()));
        }
        if (!TextUtils.isEmpty(vo.getM03())) {  // 국외근로(전액)
        	// 국외근로합계 += StringUtil.strPaserLong(vo.getM03());
        	sum.put("소득명세합계_국외근로", sum.get("소득명세합계_국외근로") + StringUtil.strPaserLong(vo.getM03()));
        }
        
        if (!TextUtils.isEmpty(vo.getO01())) {  // 야간근로수당(년 240만원)
        	sum.put("소득명세합계_야간근로수당", sum.get("소득명세합계_야간근로수당") + StringUtil.strPaserLong(vo.getO01()));
        }
        if (!TextUtils.isEmpty(vo.getQ01())) {  // 출산보육수당(월 10만원)
        	sum.put("소득명세합계_출산보육수당", sum.get("소득명세합계_출산보육수당") + StringUtil.strPaserLong(vo.getQ01()));
        }
        if (!TextUtils.isEmpty(vo.getH08())) {  // 특별법 연구보조비(월 20만원)
        	// 연구보조비합계 += StringUtil.strPaserLong(vo.getH08());
        	sum.put("소득명세합계_연구보조비", sum.get("소득명세합계_연구보조비") + StringUtil.strPaserLong(vo.getH08()));
        }
        if (!TextUtils.isEmpty(vo.getH09())) {  // 연구기관 등 연구보조비(월 20만원) 
        	// 연구보조비합계 += StringUtil.strPaserLong(vo.getH09());
        	sum.put("소득명세합계_연구보조비", sum.get("소득명세합계_연구보조비") + StringUtil.strPaserLong(vo.getH09()));
        }
        if (!TextUtils.isEmpty(vo.getH10())) {  // 기업부설연구소 연구보조비(월 20만원) 
        	// 연구보조비합계 += StringUtil.strPaserLong(vo.getH10());
        	sum.put("소득명세합계_연구보조비", sum.get("소득명세합계_연구보조비") + StringUtil.strPaserLong(vo.getH10()));
        }
        
        if (!TextUtils.isEmpty(vo.getG01())) {  // 비과세학자금(납입금액) 
        	sum.put("소득명세합계_비과세학자금", sum.get("소득명세합계_비과세학자금") + StringUtil.strPaserLong(vo.getG01()));
        }
        if (!TextUtils.isEmpty(vo.getH11())) {  // 취재수당(월 20만원)
        	sum.put("소득명세합계_취재수당", sum.get("소득명세합계_취재수당") + StringUtil.strPaserLong(vo.getH11()));
        }
        if (!TextUtils.isEmpty(vo.getH12())) {  // 벽지수당(월 20만원)
            sum.put("소득명세합계_벽지수당", sum.get("소득명세합계_벽지수당") + StringUtil.strPaserLong(vo.getH12()));
        }
        if (!TextUtils.isEmpty(vo.getH13())) {  // 재해관련급여(전액)
        	sum.put("소득명세합계_재해관련급여", sum.get("소득명세합계_재해관련급여") + StringUtil.strPaserLong(vo.getH13()));
        }
        if (!TextUtils.isEmpty(vo.getH01())) {  // 무보수위원수당(전액)
        	sum.put("소득명세합계_무보수위원수당", sum.get("소득명세합계_무보수위원수당") + StringUtil.strPaserLong(vo.getH01()));
        }
        if (!TextUtils.isEmpty(vo.getK01())) {  // 외국주둔군인등(전액)
        	sum.put("소득명세합계_외국주둔군인등", sum.get("소득명세합계_외국주둔군인등") + StringUtil.strPaserLong(vo.getK01()));
        }
        if (!TextUtils.isEmpty(vo.getS01())) {  // 주식매수선택권(년 3,000만원) 
        	sum.put("소득명세합계_주식매수선택권", sum.get("소득명세합계_주식매수선택권") + StringUtil.strPaserLong(vo.getS01()));
        }
        if (!TextUtils.isEmpty(vo.getY02())) {  // 우리사주조합인출금50%(년 인출금의 50% 한도) 
        	sum.put("소득명세합계_우리사주조합인출금50", sum.get("소득명세합계_우리사주조합인출금50") + StringUtil.strPaserLong(vo.getY02()));
        }
        if (!TextUtils.isEmpty(vo.getY03())) {  // 우리사주조합인출금75%(년 인출금의 75% 한도)
        	sum.put("소득명세합계_우리사주조합인출금75", sum.get("소득명세합계_우리사주조합인출금75") + StringUtil.strPaserLong(vo.getY03()));
        }
        if (!TextUtils.isEmpty(vo.getY04())) {  // 우리사주조합인출금100%(년 인출금의 100% 한도)
        	sum.put("소득명세합계_우리사주조합인출금100", sum.get("소득명세합계_우리사주조합인출금100") + StringUtil.strPaserLong(vo.getY04()));
        }
        if (!TextUtils.isEmpty(vo.getH05())) {  // 경호･승선수당
        	sum.put("소득명세합계_경호승선수당", sum.get("소득명세합계_경호승선수당") + StringUtil.strPaserLong(vo.getH05()));
        }
        if (!TextUtils.isEmpty(vo.getI01())) {  // 외국정부등근무자(전액)
        	sum.put("소득명세합계_외국정부등근무자", sum.get("소득명세합계_외국정부등근무자") + StringUtil.strPaserLong(vo.getI01()));
        }
        if (!TextUtils.isEmpty(vo.getR10())) {  // 근로장학금(전액)
        	sum.put("소득명세합계_근로장학금", sum.get("소득명세합계_근로장학금") + StringUtil.strPaserLong(vo.getR10()));
        }
        if (!TextUtils.isEmpty(vo.getH14())) {  // 보육교사 근무환경개선비(전액) 
        	sum.put("소득명세합계_보육교사근무환경개선비", sum.get("소득명세합계_보육교사근무환경개선비") + StringUtil.strPaserLong(vo.getH14()));
        }
        if (!TextUtils.isEmpty(vo.getH15())) {  // 사립유치원수석교사･교사의 인건비(전액)
        	sum.put("소득명세합계_사립유치원수석교사인건비", sum.get("소득명세합계_사립유치원수석교사인건비") + StringUtil.strPaserLong(vo.getH15()));
        }
        if (!TextUtils.isEmpty(vo.getH16())) {  // 정부･공공기관지방이전기관 종사자 이주수당(월 20만원)
        	sum.put("소득명세합계_정부공공기관종사자이주수당", sum.get("소득명세합계_정부공공기관종사자이주수당") + StringUtil.strPaserLong(vo.getH16()));
        }
        if (!TextUtils.isEmpty(vo.getH17())) {  // 종교활동비
        	sum.put("소득명세합계_종교활동비", sum.get("소득명세합계_종교활동비") + StringUtil.strPaserLong(vo.getH17()));
        }
        if (!TextUtils.isEmpty(vo.getU01())) {  // 벤처기업 주식매수선택권(년 2,000만원)
        	sum.put("소득명세합계_벤처기업주식매수선택권", sum.get("소득명세합계_벤처기업주식매수선택권") + StringUtil.strPaserLong(vo.getU01()));
        }
        if (!TextUtils.isEmpty(vo.getR11())) {  // 직무발명보상금(년 300만원) 
        	sum.put("소득명세합계_직무발명보상금", sum.get("소득명세합계_직무발명보상금") + StringUtil.strPaserLong(vo.getR11()));
        }
        if (!TextUtils.isEmpty(vo.getH06())) {  // 유아･초중등 연구보조비(월 20만원)
        	sum.put("소득명세합계_유아초중등연구보조비", sum.get("소득명세합계_유아초중등연구보조비") + StringUtil.strPaserLong(vo.getH06()));
        }
        if (!TextUtils.isEmpty(vo.getH07())) {  // 고등교육법 연구보조비(월 20만원)
        	sum.put("소득명세합계_고등교육법연구보조비", sum.get("소득명세합계_고등교육법연구보조비") + StringUtil.strPaserLong(vo.getH07()));
        }
        if (!TextUtils.isEmpty(vo.getY22())) {  // 전공의 수련 보조 수당
            sum.put("소득명세합계_수련보조수당", sum.get("소득명세합계_수련보조수당") + StringUtil.strPaserLong(vo.getY22()));
        }
        
    }

    private void set인적공제(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO, Map<String, String> family) throws Exception {
        YE001VO ye001VO = new YE001VO();
        ye001VO.set계약ID(계약ID);
        ye001VO.set사용자ID(사용자ID);
        ye001VO.setStartPage(0);
        ye001VO.setEndPage(9999);
        List<YE001VO> listYE000 = ye001DAO.getYE001List(ye001VO);

        int index = 1;
        int 기본공제_인원수 = 0;
        String fieldKey;
        for (YE001VO vo : listYE000) {
            if ("1".equals(vo.get기본공제())) {
                기본공제_인원수++;
            }

            if (index > 5) {
                continue;
            }

            if ("0".equals(vo.get가족관계())) {
                fieldKey = "소득자_";
                family.put(vo.get부양가족ID(), "소득자_");

                if ("1".equals(vo.get부녀자())) {
                    setPdfField(fieldListVO, "부녀자_인원수", "1");
                    setPdfField(fieldListVO, fieldKey + "부녀자여부", "O");
                }
                if ("1".equals(vo.get한부모())) {
                    setPdfField(fieldListVO, "한부모_인원수", "1");
                    setPdfField(fieldListVO, fieldKey + "한부모여부", "O");
                }
            } else {
                fieldKey = "부양가족" + index + "_";
                family.put(vo.get부양가족ID(), fieldKey);
                setPdfField(fieldListVO, fieldKey + "관계코드", vo.get가족관계());
                setPdfField(fieldListVO, fieldKey + "성명", vo.get성명());
                setPdfField(fieldListVO, fieldKey + "주민번호", formatIdentity(vo.get개인식별번호(), true));

                if ("1".equals(vo.get기본공제())) {
                    setPdfField(fieldListVO, fieldKey + "기본공제여부", "O");
                }

                index++;
            }

            setPdfField(fieldListVO, fieldKey + "내외국인", vo.get내외국인());

            if (StringUtil.strPaserInt(vo.get나이()) >= 70) {
                setPdfField(fieldListVO, fieldKey + "경로우대여부", "O");
            } else if (StringUtil.strPaserInt(vo.get나이()) <= 6) {
                setPdfField(fieldListVO, fieldKey + "6세이하여부", "O");
            }

            if ("1".equals(vo.get장애인()) || "2".equals(vo.get장애인()) || "3".equals(vo.get장애인())) {
                setPdfField(fieldListVO, fieldKey + "장애인여부", "O");
            }

            if ("1".equals(vo.get출산입양()) || "2".equals(vo.get출산입양()) || "3".equals(vo.get출산입양())) {
                setPdfField(fieldListVO, fieldKey + "출산입양여부", "O");
            }
        }

        setPdfField(fieldListVO, "기본공제_인원수", 기본공제_인원수);
    }

    private void set건강고용(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO) throws Exception {
        YE000VO ye000VO = new YE000VO();
        ye000VO.set계약ID(계약ID);
        ye000VO.set사용자ID(사용자ID);
        List<YE000VO> listSum = ye003DAO.getYE003SumList(ye000VO);

        int 국세청_보험료_건강고용 = 0;
        if (listSum != null && listSum.size() > 0) {
            for (YE000VO vo : listSum) {
                국세청_보험료_건강고용 += StringUtil.strPaserLong(vo.get건강보험료());
                국세청_보험료_건강고용 += StringUtil.strPaserLong(vo.get장기요양보험료());
                국세청_보험료_건강고용 += StringUtil.strPaserLong(vo.get고용보험료());
            }
        }

        YE052VO ye052VO = new YE052VO();
        ye052VO.set계약ID(계약ID);
        ye052VO.set사용자ID(사용자ID);
        List<YE052VO> listYE052 = ye052DAO.getYE052List(ye052VO);

        int 기타_보험료_건강고용 = 0;
        if (listYE052 != null && listYE052.size() > 0) {
            for (YE052VO vo : listYE052) {
                기타_보험료_건강고용 += vo.get추가납입금액();
            }
        }

        setPdfField(fieldListVO, "소득자_국세청_보험료_건강고용", 국세청_보험료_건강고용);
        setPdfField(fieldListVO, "합계_국세청_보험료_건강고용", 국세청_보험료_건강고용);
        setPdfField(fieldListVO, "소득자_기타_보험료_건강고용", 기타_보험료_건강고용);
        setPdfField(fieldListVO, "합계_기타_보험료_건강고용", 기타_보험료_건강고용);
        
        
    }

    private void set보험료(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO, Map<String, String> family) {
        YE401VO ye401VO = new YE401VO();
        ye401VO.set계약ID(계약ID);
        ye401VO.set사용자ID(사용자ID);
        List<YE401VO> listYE401 = ye401DAO.getYE401List(ye401VO);

        Map<String, int[]> sum = new HashMap<>();
        if (listYE401 != null && listYE401.size() > 0) {
            int 금액;
            int[] value;

            for (YE401VO vo : listYE401) {
                금액 = vo.get납입금액() - vo.get차감금액();

                if (sum.containsKey(vo.get부양가족ID())) {
                    value = sum.get(vo.get부양가족ID());
                } else {
                    value = new int[]{0, 0, 0, 0};
                    sum.put(vo.get부양가족ID(), value);
                }

                if ("1".equals(vo.get보험구분코드())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        // 국세청_보험료_보장성
                        value[0] += 금액;
                    } else {
                        // 기타_보험료_보장성
                        value[1] += 금액;
                    }
                } else if ("2".equals(vo.get보험구분코드())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        // 국세청_보험료_장애인전용
                        value[2] += 금액;
                    } else {
                        // 기타_보험료_장애인전용
                        value[3] += 금액;
                    }
                }
            }
        }

        int[] total = new int[]{0, 0, 0, 0};
        int[] value;
        for (String 부양가족ID : sum.keySet()) {
            value = sum.get(부양가족ID);
            total[0] += value[0];
            total[1] += value[1];
            total[2] += value[2];
            total[3] += value[3];

            setPdfField(fieldListVO, family.get(부양가족ID) + "국세청_보험료_보장성", value[0]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "기타_보험료_보장성", value[1]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "국세청_보험료_장애인전용", value[2]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "기타_보험료_장애인전용", value[3]);
        }

        setPdfField(fieldListVO, "합계_국세청_보험료_보장성", total[0]);
        setPdfField(fieldListVO, "합계_기타_보험료_보장성", total[1]);
        setPdfField(fieldListVO, "합계_국세청_보험료_장애인전용", total[2]);
        setPdfField(fieldListVO, "합계_기타_보험료_장애인전용", total[3]);
    }

    private void set의료비(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO, Map<String, String> family) {
        YE402VO ye402VO = new YE402VO();
        ye402VO.set계약ID(계약ID);
        ye402VO.set사용자ID(사용자ID);
        List<YE402VO> listYE402 = ye402DAO.getYE402List(ye402VO);

        Map<String, int[]> sum = new HashMap<>();
        if (listYE402 != null && listYE402.size() > 0) {
            int 금액;
            int[] value;

            for (YE402VO vo : listYE402) {
                금액 = vo.get지출액() - vo.get차감금액();

                if (sum.containsKey(vo.get부양가족ID())) {
                    value = sum.get(vo.get부양가족ID());
                } else {
                    value = new int[]{0, 0};
                    sum.put(vo.get부양가족ID(), value);
                }

                if (CodeUtils.is국세청(vo.get자료구분코드())) {
                    // 국세청_의료비
                    value[0] += 금액;
                } else {
                    // 기타_의료비
                    value[1] += 금액;
                }
            }
        }

        int[] total = new int[]{0, 0};
        int[] value;
        for (String 부양가족ID : sum.keySet()) {
            value = sum.get(부양가족ID);
            total[0] += value[0];
            total[1] += value[1];

            setPdfField(fieldListVO, family.get(부양가족ID) + "국세청_의료비", value[0]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "기타_의료비", value[1]);
        }

        setPdfField(fieldListVO, "합계_국세청_의료비", total[0]);
        setPdfField(fieldListVO, "합계_기타_의료비", total[1]);
    }

    private void set교육비(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO, Map<String, String> family) {
        YE403VO ye403VO = new YE403VO();
        ye403VO.set계약ID(계약ID);
        ye403VO.set사용자ID(사용자ID);
        List<YE403VO> listYE403 = ye403DAO.getYE403List(ye403VO);

        Map<String, int[]> sum = new HashMap<>();
        if (listYE403 != null && listYE403.size() > 0) {
            int 교육비;
            int[] value;

            for (YE403VO vo : listYE403) {
                교육비 = vo.get공납금() - vo.get공납금_차감금액() + vo.get교복구입비() - vo.get교복구입비_차감금액() + vo.get체험학습비() - vo.get체험학습비_차감금액();

                if (sum.containsKey(vo.get부양가족ID())) {
                    value = sum.get(vo.get부양가족ID());
                } else {
                    value = new int[]{0, 0};
                    sum.put(vo.get부양가족ID(), value);
                }

                if (CodeUtils.is국세청(vo.get자료구분코드())) {
                    // 국세청_교육비
                    value[0] += 교육비;
                } else {
                    // 기타_교육비
                    value[1] += 교육비;
                }
            }
        }

        int[] total = new int[]{0, 0};
        int[] value;
        for (String 부양가족ID : sum.keySet()) {
            value = sum.get(부양가족ID);
            total[0] += value[0];
            total[1] += value[1];

            setPdfField(fieldListVO, family.get(부양가족ID) + "국세청_교육비", value[0]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "기타_교육비", value[1]);
        }

        setPdfField(fieldListVO, "합계_국세청_교육비", total[0]);
        setPdfField(fieldListVO, "합계_기타_교육비", total[1]);
    }

    private void set신용카드(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO, Map<String, String> family) {
        YE108VO ye108VO = new YE108VO();
        ye108VO.set계약ID(계약ID);
        ye108VO.set사용자ID(사용자ID);
        List<YE108VO> listYE108 = ye108DAO.getYE108List(ye108VO);

        Map<String, int[]> sum = new HashMap<>();
        if (listYE108 != null && listYE108.size() > 0) {
            int[] value;

            for (YE108VO vo : listYE108) {
                if (sum.containsKey(vo.get부양가족ID())) {
                    value = sum.get(vo.get부양가족ID());
                } else {
                    value = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    sum.put(vo.get부양가족ID(), value);
                }

                if (CodeUtils.is국세청(vo.get자료구분코드())) {
                    // 국세청_신용카드등_신용카드
                    value[0] += vo.get신용카드() - vo.get신용카드_차감금액();
                    // 국세청_신용카드등_현금영수증
                    value[1] += vo.get현금영수증() - vo.get현금영수증_차감금액();
                    // 국세청_신용카드등_직불카드
                    value[2] += vo.get직불_선불카드() - vo.get직불_선불카드_차감금액();
                    // 국세청_신용카드등_도서공연
                    value[3] += vo.get도서공연() - vo.get도서공연_차감금액();
                    // 국세청_신용카드등_전통시장
                    value[4] += vo.get전통시장() - vo.get전통시장_차감금액();
                    // 국세청_신용카드등_대중교통
                    value[5] += vo.get대중교통() - vo.get대중교통_차감금액();
                } else {
                    // 기타_신용카드등_신용카드
                    value[6] += vo.get신용카드() - vo.get신용카드_차감금액();
                    // 기타_신용카드등_현금영수증
                    value[7] += vo.get현금영수증() - vo.get현금영수증_차감금액();
                    // 기타_신용카드등_직불카드
                    value[8] += vo.get직불_선불카드() - vo.get직불_선불카드_차감금액();
                    // 기타_신용카드등_도서공연
                    value[9] += vo.get도서공연() - vo.get도서공연_차감금액();
                    // 기타_신용카드등_전통시장
                    value[10] += vo.get전통시장() - vo.get전통시장_차감금액();
                    // 기타_신용카드등_대중교통
                    value[11] += vo.get대중교통() - vo.get대중교통_차감금액();
                }
            }
        }

        int[] total = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] value;
        for (String 부양가족ID : sum.keySet()) {
            value = sum.get(부양가족ID);
            total[0] += value[0];
            total[1] += value[1];
            total[2] += value[2];
            total[3] += value[3];
            total[4] += value[4];
            total[5] += value[5];
            total[6] += value[6];
            total[7] += value[7];
            total[8] += value[8];
            total[9] += value[9];
            total[10] += value[10];
            total[11] += value[11];

            setPdfField(fieldListVO, family.get(부양가족ID) + "국세청_신용카드등_신용카드", value[0]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "국세청_신용카드등_현금영수증", value[1]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "국세청_신용카드등_직불카드", value[2]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "국세청_신용카드등_도서공연", value[3]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "국세청_신용카드등_전통시장", value[4]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "국세청_신용카드등_대중교통", value[5]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "기타_신용카드등_신용카드", value[6]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "기타_신용카드등_현금영수증", value[7]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "기타_신용카드등_직불카드", value[8]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "기타_신용카드등_도서공연", value[9]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "기타_신용카드등_전통시장", value[10]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "기타_신용카드등_대중교통", value[11]);
        }

        setPdfField(fieldListVO, "합계_국세청_신용카드등_신용카드", total[0]);
        setPdfField(fieldListVO, "합계_국세청_신용카드등_현금영수증", total[1]);
        setPdfField(fieldListVO, "합계_국세청_신용카드등_직불카드", total[2]);
        setPdfField(fieldListVO, "합계_국세청_신용카드등_도서공연", total[3]);
        setPdfField(fieldListVO, "합계_국세청_신용카드등_전통시장", total[4]);
        setPdfField(fieldListVO, "합계_국세청_신용카드등_대중교통", total[5]);
        setPdfField(fieldListVO, "합계_기타_신용카드등_신용카드", total[6]);
        setPdfField(fieldListVO, "합계_기타_신용카드등_현금영수증", total[7]);
        setPdfField(fieldListVO, "합계_기타_신용카드등_직불카드", total[8]);
        setPdfField(fieldListVO, "합계_기타_신용카드등_도서공연", total[9]);
        setPdfField(fieldListVO, "합계_기타_신용카드등_전통시장", total[10]);
        setPdfField(fieldListVO, "합계_기타_신용카드등_대중교통", total[11]);
    }

    private void set기부금(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO, Map<String, String> family) {
        YE404VO ye404VO = new YE404VO();
        ye404VO.set계약ID(계약ID);
        ye404VO.set사용자ID(사용자ID);
        ye404VO.setStartPage(0);
        ye404VO.setEndPage(9999);
        List<YE404VO> listYE404 = ye404DAO.getYE404List(ye404VO);

        Map<String, int[]> sum = new HashMap<>();
        if (listYE404 != null && listYE404.size() > 0) {
            int[] value;

            for (YE404VO vo : listYE404) {
                if (sum.containsKey(vo.get부양가족ID())) {
                    value = sum.get(vo.get부양가족ID());
                } else {
                    value = new int[]{0, 0};
                    sum.put(vo.get부양가족ID(), value);
                }

                if (CodeUtils.is국세청(vo.get자료구분코드())) {
                    // 국세청_기부금
                    value[0] += vo.get기부명세_공제대상기부금();
                } else {
                    // 기타_기부금
                    value[1] += vo.get기부명세_공제대상기부금();
                }
            }
        }

        int[] total = new int[]{0, 0};
        int[] value;
        for (String 부양가족ID : sum.keySet()) {
            value = sum.get(부양가족ID);
            total[0] += value[0];
            total[1] += value[1];

            setPdfField(fieldListVO, family.get(부양가족ID) + "국세청_기부금", value[0]);
            setPdfField(fieldListVO, family.get(부양가족ID) + "기타_기부금", value[1]);
        }

        setPdfField(fieldListVO, "합계_국세청_기부금", total[0]);
        setPdfField(fieldListVO, "합계_기타_기부금", total[1]);
    }


    private void createY02PDF(String bizId, YE750VO ye750VO, YW800Response request) throws Exception {
        ContentVO contentVO = new ContentVO();
        contentVO.setFileId(YE750Service.Y02_FILE_ID);

        // 콘텐츠에서 정보 추출
        ContentVO contentResultVO = contentDAO.getContent(contentVO);
        if (contentResultVO == null) {
            System.out.println("[createY02PDF] 콘텐츠 정보가 존재하지 않습니다.");
            return;
        }

        // 저장위치
        CabinetVO cabinetVO = new CabinetVO();
        cabinetVO.setClassId(contentResultVO.getClassId());
        cabinetVO.setCabinetId(contentResultVO.getCabinetId());

        CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
        if (cabinetResultVO == null) {
            System.out.println("[createY02PDF] 캐비넷 정보가 존재하지 않습니다.");
            return;
        }

        String originalPdfPath = cabinetResultVO.getCabinetPath() + contentResultVO.getFilePath() + contentResultVO.getFileName();
        String targetPdfName = StringUtil.getUUID() + ".pdf";
        String targetPdfPath = MultipartFileUtil.getSystemTempPath() + targetPdfName;

        System.out.println(originalPdfPath);
        System.out.println(targetPdfPath);

        // 사용할 PDF복사
        FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
        if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
            System.out.println("[createY02PDF] userId=>" + ye750VO.get사용자ID());

            YE000VO ye000VO = new YE000VO();
            ye000VO.setBizId(bizId);
            ye000VO.set계약ID(ye750VO.get계약ID());
            ye000VO.set사용자ID(ye750VO.get사용자ID());
            YE000VO resultYE000VO = ye000DAO.getYE000(ye000VO);

            if (resultYE000VO != null) {
                List<FieldVO> fieldListVO = new ArrayList<>();
                String today = DateUtil.getTimeStamp(3);

                YS000VO ys000VO = new YS000VO();
                ys000VO.setBizId(bizId);
                ys000VO.set계약ID(ye750VO.get계약ID());
                List<YS000VO> listYS000 = ys000DAO.getYS000List(ys000VO);
                String 계약년도 = "";
                if (listYS000.size() > 0) {
                    계약년도 = listYS000.get(0).getFebYear();
                    setPdfField(fieldListVO, "근로자소득공제신고서_년", 계약년도);
                }
                setPdfField(fieldListVO, "근로자소득공제신고서_소득자_성명", resultYE000VO.getEmpName());
                setPdfField(fieldListVO, "근로자소득공제신고서_주민등록번호", formatIdentity(resultYE000VO.get개인식별번호(), true));

                if ("1".equals(resultYE000VO.get세대주여부())) {
                    setPdfField(fieldListVO, "근로자소득공제신고서_세대주", "O");
                } else {
                    setPdfField(fieldListVO, "근로자소득공제신고서_세대원", "O");
                }
                setPdfField(fieldListVO, "근로자소득공제신고서_국적", resultYE000VO.get국적());
                setPdfField(fieldListVO, "근로자소득공제신고서_국적코드", resultYE000VO.get국가코드());

                if ("1".equals(resultYE000VO.get거주구분())) {
                    setPdfField(fieldListVO, "근로자소득공제신고서_거주자", "O");
                } else {
                    setPdfField(fieldListVO, "근로자소득공제신고서_비거주자", "O");
                }
                setPdfField(fieldListVO, "근로자소득공제신고서_거주지국", resultYE000VO.get거주지국());
                setPdfField(fieldListVO, "근로자소득공제신고서_거주지국코드", resultYE000VO.get거주지국코드());

                if ("1".equals(resultYE000VO.get연말정산분납여부())) {
                    setPdfField(fieldListVO, "근로자소득공제신고서_분납신청_신청", "O");
                } else {
                    setPdfField(fieldListVO, "근로자소득공제신고서_분납신청_미신청", "O");
                }
                if ("1".equals(resultYE000VO.get소득세적용률())) {
                    setPdfField(fieldListVO, "근로자소득공제신고서_원천징수세액_100", "O");
                } else if ("2".equals(resultYE000VO.get소득세적용률())) {
                    setPdfField(fieldListVO, "근로자소득공제신고서_원천징수세액_80", "O");
                } else if ("3".equals(resultYE000VO.get소득세적용률())) {
                    setPdfField(fieldListVO, "근로자소득공제신고서_원천징수세액_120", "O");
                }

                List<YE000VO> listYE000 = ye003DAO.getYE003List(ye000VO);
                if (listYE000 != null && listYE000.size() > 0) {
                    for (YE000VO vo : listYE000) {
                        if ("1".equals(vo.get근무지구분())) {
                            setPdfField(fieldListVO, "근로자소득공제신고서_근무처_명칭", vo.get회사명());
                            setPdfField(fieldListVO, "근로자소득공제신고서_사업자등록번호", formatBusinessNo(vo.get사업자등록번호()));

                            if (vo.get근무시작일().compareTo(계약년도 + "0101") < 0) {
                                setPdfField(fieldListVO, "근로자소득공제신고서_근무기간시작일", 계약년도 + ".01.01");
                            } else {
                                setPdfField(fieldListVO, "근로자소득공제신고서_근무기간시작일", DateUtil.getDateText(1, vo.get근무시작일()));
                            }

                            if (TextUtils.isEmpty(vo.get근무종료일()) || vo.get근무종료일().compareTo(계약년도 + "1231") > 0) {
                                setPdfField(fieldListVO, "근로자소득공제신고서_근무기간종료일", 계약년도 + ".12.31");
                            } else {
                                setPdfField(fieldListVO, "근로자소득공제신고서_근무기간종료일", DateUtil.getDateText(1, vo.get근무종료일()));
                            }

                            if (!TextUtils.isEmpty(vo.get감면종료일()) && vo.get감면종료일().compareTo(계약년도 + "0101") >= 0) {
                                if (vo.get감면시작일().compareTo(계약년도 + "0101") < 0) {
                                    setPdfField(fieldListVO, "근로자소득공제신고서_감면기간시작일", 계약년도 + ".01.01");
                                } else {
                                    setPdfField(fieldListVO, "근로자소득공제신고서_감면기간시작일", DateUtil.getDateText(1, vo.get감면시작일()));
                                }

                                if (vo.get감면종료일().compareTo(계약년도 + "1231") > 0) {
                                    setPdfField(fieldListVO, "근로자소득공제신고서_감면기간종료일", 계약년도 + ".12.31");
                                } else {
                                    setPdfField(fieldListVO, "근로자소득공제신고서_감면기간종료일", DateUtil.getDateText(1, vo.get감면종료일()));
                                }
                            }
                        }
                    }
                }

                Map<String, String> family = new HashMap<>();
                set근로소득공제신고서_인적공제(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                set근로소득공제신고서_건강고용(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO);
                set근로소득공제신고서_보험료(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                set근로소득공제신고서_의료비(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                set근로소득공제신고서_교육비(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                set근로소득공제신고서_신용카드(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                set근로소득공제신고서_기부금(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO, family);
                set근로소득공제신고서_회사자료(ye750VO.get계약ID(), ye750VO.get사용자ID(), fieldListVO);

                YE700VO ye700VO = new YE700VO();
                ye700VO.set계약ID(ye750VO.get계약ID());
                ye700VO.set사용자ID(ye750VO.get사용자ID());
                ye700VO.set사용여부("1");
                List<YE700VO> listYE700 = ye700DAO.getYE700(ye700VO);
                if (listYE700.size() > 0) {
                    YE700VO vo = listYE700.get(0);

                    setPdfField(fieldListVO, "인적공제_경로우대합계", vo.get경로우대_인원수());
                    setPdfField(fieldListVO, "인적공제_출산입양합계", vo.get출산입양_인원수());
                    setPdfField(fieldListVO, "인적공제_장애인합계", vo.get장애인_인원수());
                    setPdfField(fieldListVO, "장애인수", vo.get장애인_인원수());
                    setPdfField(fieldListVO, "월세액세액공제(10%또는12%)", vo.get세액공제_월세액_세액공제액());
                    setPdfField(fieldListVO, "주택자금차입금이자세액공제(30%)", vo.get세액공제_주택차입금());
                    setPdfField(fieldListVO, "인적공제_6세이하합계", vo.get세액공제_6세이하_인원수());

                    setPdfField(fieldListVO, "연금보험료_합계_금액", vo.get연금보험_계());
                    setPdfField(fieldListVO, "연금보험료_합계_공제액", vo.get연금보험_계());
                    setPdfField(fieldListVO, "보험료_합계_금액", vo.get특별소득_건강보험() + vo.get특별소득_고용보험());
                    setPdfField(fieldListVO, "보험료_합계_공제액", vo.get특별소득_건강보험() + vo.get특별소득_고용보험());

                    // TODO 대상금액
                    setPdfField(fieldListVO, "주택임차차입금_대출기관차입_공제액", vo.get특별소득_주택원리금_대출기관());
                    setPdfField(fieldListVO, "주택임차차입금_거주자차입_공제액", vo.get특별소득_주택원리금_거주자());
                    setPdfField(fieldListVO, "장기주택저장차입금_2011년이전차입분_15년미만_공제액", vo.get특별소득_장기주택_2011년이전_15년미만());
                    setPdfField(fieldListVO, "장기주택저장차입금_2011년이전차입분_15년29년_공제액", vo.get특별소득_장기주택_2011년이전_15년29년());
                    setPdfField(fieldListVO, "장기주택저장차입금_2011년이전차입분_30년이상_공제액", vo.get특별소득_장기주택_2011년이전_30년이상());
                    setPdfField(fieldListVO, "장기주택저장차입금_2012년이후차입분_상환대출_공제액", vo.get특별소득_장기주택_2012년이후_고정이거나비거치());
                    setPdfField(fieldListVO, "장기주택저장차입금_2012년이후차입분_기타대출_공제액", vo.get특별소득_장기주택_2012년이후_그밖에());
                    setPdfField(fieldListVO, "장기주택저장차입금_2015년이후차입분_15년이상_고정금리이면서비거치상환대출_공제액", vo.get특별소득_장기주택_2015년이후_15년이상_고정이면서비거치());
                    setPdfField(fieldListVO, "장기주택저장차입금_2015년이후차입분_15년이상_고정금리이거나비거치상환대출_공제액", vo.get특별소득_장기주택_2015년이후_15년이상_고정이거나비거치());
                    setPdfField(fieldListVO, "장기주택저장차입금_2015년이후차입분_15년이상_기타대출_공제액", vo.get특별소득_장기주택_2015년이후_15년이상_그밖에());
                    setPdfField(fieldListVO, "장기주택저장차입금_2015년이후차입분_10년15년_고정금이이거나비거치상환대출_공제액", vo.get특별소득_장기주택_2015년이후_10년15년_고정이거나비거치());

                    setPdfField(
                            fieldListVO,
                            "주택자금_합계_공제액",
                            vo.get특별소득_주택원리금_대출기관() +
                                    vo.get특별소득_주택원리금_거주자() +
                                    vo.get특별소득_장기주택_2011년이전_15년미만() +
                                    vo.get특별소득_장기주택_2011년이전_15년29년() +
                                    vo.get특별소득_장기주택_2011년이전_30년이상() +
                                    vo.get특별소득_장기주택_2012년이후_고정이거나비거치() +
                                    vo.get특별소득_장기주택_2012년이후_그밖에() +
                                    vo.get특별소득_장기주택_2015년이후_15년이상_고정이면서비거치() +
                                    vo.get특별소득_장기주택_2015년이후_15년이상_고정이거나비거치() +
                                    vo.get특별소득_장기주택_2015년이후_15년이상_그밖에() +
                                    vo.get특별소득_장기주택_2015년이후_10년15년_고정이거나비거치()
                    );

                    // TODO 기부금이월 상세
                    //setPdfField(fieldListVO, "기부금이월분_합계_공제액", vo.get특별소득_기부금_이월분());

                    setPdfField(fieldListVO, "개인연금저축_공제액", vo.get그밖에소득공제_개인연금());
                    setPdfField(fieldListVO, "소기업소상공인_공제부금_공제액", vo.get그밖에소득공제_소기업소상공인());
                    setPdfField(fieldListVO, "주택마련저축_청약저축_공제액", vo.get그밖에소득공제_주택마련_청약저축());
                    setPdfField(fieldListVO, "주택마련저축_근로자주택마련저축_공제액", vo.get그밖에소득공제_주택마련_근로자주택마련());
                    setPdfField(fieldListVO, "주택마련저축_주택청약종합처축_공제액", vo.get그밖에소득공제_주택마련_주택청약());

                    setPdfField(
                            fieldListVO,
                            "주택마련저축_소등공제_합계_공제액",
                            vo.get그밖에소득공제_개인연금() +
                                    vo.get그밖에소득공제_소기업소상공인() +
                                    vo.get그밖에소득공제_주택마련_청약저축() +
                                    vo.get그밖에소득공제_주택마련_주택청약() +
                                    vo.get그밖에소득공제_주택마련_근로자주택마련()
                    );

                    // TODO 투자조합출자 상세
                    setPdfField(fieldListVO, "투자조합출자_소득공제_합계_공제액", vo.get그밖에소득공제_투자조합출자());

                    setPdfField(fieldListVO, "신용카드사용액_합계_공제액", vo.get그밖에소득공제_신용카드등());

                    setPdfField(fieldListVO, "우리사주조합출연금_공제액", vo.get그밖에소득공제_우리사주조합());
                    setPdfField(fieldListVO, "고용유지중소기업근로자_공제액", vo.get그밖에소득공제_고용유지근로자());
                    setPdfField(fieldListVO, "장기집합투자증권저축_공제액", vo.get그밖에소득공제_장기집합투자());

                    setPdfField(fieldListVO, "연금계좌_과학기술인공제_공제대상금액", vo.get세액공제_연금계좌_과학기술_공제대상금액());
                    setPdfField(fieldListVO, "연금계좌_과학기술인공제_공제세액", vo.get세액공제_연금계좌_과학기술_세액공제액());
                    setPdfField(fieldListVO, "연금계좌_근로자퇴직금여보장법에따른퇴직연금_공제대상금액", vo.get세액공제_연금계좌_퇴직연금_공제대상금액());
                    setPdfField(fieldListVO, "연금계좌_근로자퇴직금여보장법에따른퇴직연금_공제세액", vo.get세액공제_연금계좌_퇴직연금_세액공제액());
                    setPdfField(fieldListVO, "연금계좌_연금저축_공제대상금액", vo.get세액공제_연금계좌_연금저축_공제대상금액());
                    setPdfField(fieldListVO, "연금계좌_연금저축_공제세액", vo.get세액공제_연금계좌_연금저축_세액공제액());

                    setPdfField(
                            fieldListVO,
                            "연금계좌_합계_공제대상금액",
                            vo.get세액공제_연금계좌_과학기술_공제대상금액() +
                                    vo.get세액공제_연금계좌_퇴직연금_공제대상금액() +
                                    vo.get세액공제_연금계좌_연금저축_공제대상금액()
                    );
                    setPdfField(fieldListVO, "연금계좌_합계_공제세액", vo.get세액공제_연금계좌_계());

                    setPdfField(fieldListVO, "특별세액공제_보험료_보장성_공제대상금액", vo.get특별세액_보험료_보장성_공제대상금액());
                    setPdfField(fieldListVO, "특별세액공제_보험료_보장성_공제세액", vo.get특별세액_보험료_보장성_세액공제액());
                    setPdfField(fieldListVO, "특별세액공제_보험료_장애인전용보장성_공제대상금액", vo.get특별세액_보험료_장애인전용_공제대상금액());
                    setPdfField(fieldListVO, "특별세액공제_보험료_장애인전용보장성_공제세액", vo.get특별세액_보험료_장애인전용_세액공제액());

                    setPdfField(fieldListVO, "특별세액공제_보험료_합계_공제대상금액", vo.get특별세액_보험료_보장성_공제대상금액() + vo.get특별세액_보험료_장애인전용_공제대상금액());
                    setPdfField(fieldListVO, "특별세액공제_보험료_합계_공제세액", vo.get특별세액_보험료_보장성_세액공제액() + vo.get특별세액_보험료_장애인전용_세액공제액());
                    
                    
                    
                    // TODO 의료비 상세
                    setPdfField(fieldListVO, "특별세액공제_의료비_합계_공제대상금액", vo.get특별세액_의료비_공제대상금액());
                    setPdfField(fieldListVO, "특별세액공제_의료비_합계_공제세액", vo.get특별세액_의료비_세액공제액());

                    // TODO 교육비 상세
                    setPdfField(fieldListVO, "특별세액공제_교육비_합계_공제대상금액", vo.get특별세액_교육비_공제대상금액());
                    setPdfField(fieldListVO, "특별세액공제_교육비_합계_공제세액", vo.get특별세액_교육비_세액공제액());

                    setPdfField(fieldListVO, "특별세액공제_기부금_정치자금기부금10만원이하_공제대상금액", vo.get특별세액_기부금_정치자금_10만이하_공제대상금액());
                    setPdfField(fieldListVO, "특별세액공제_기부금_정치자금기부금10만원이하_공제세액", vo.get특별세액_기부금_정치자금_10만이하_세액공제액());
                    setPdfField(fieldListVO, "특별세액공제_기부금_정치자금기부금10만원초과_공제대상금액", vo.get특별세액_기부금_정치자금_10만초과_공제대상금액());
                    setPdfField(fieldListVO, "특별세액공제_기부금_정치자금기부금10만원초과_공제세액", vo.get특별세액_기부금_정치자금_10만초과_세액공제액());
                    setPdfField(fieldListVO, "특별세액공제_기부금_법정기부금_공제대상금액", vo.get특별세액_기부금_법정_공제대상금액());
                    setPdfField(fieldListVO, "특별세액공제_기부금_법정기부금_공제세액", vo.get특별세액_기부금_법정_세액공제액());
                    setPdfField(fieldListVO, "특별세액공제_기부금_우리사주조합기부금_공제대상금액", vo.get특별세액_기부금_우리사주_공제대상금액());
                    setPdfField(fieldListVO, "특별세액공제_기부금_우리사주조합기부금_공제세액", vo.get특별세액_기부금_우리사주_세액공제액());
                    setPdfField(fieldListVO, "특별세액공제_기부금_지정기부금(종교단체외)_공제대상금액", vo.get특별세액_기부금_지정_종교외_공제대상금액());
                    setPdfField(fieldListVO, "특별세액공제_기부금_지정기부금(종교단체외)_공제세액", vo.get특별세액_기부금_지정_종교외_세액공제액());
                    setPdfField(fieldListVO, "특별세액공제_기부금_지정기부금(종교단체)_공제대상금액", vo.get특별세액_기부금_지정_종교_공제대상금액());
                    setPdfField(fieldListVO, "특별세액공제_기부금_지정기부금(종교단체)_공제세액", vo.get특별세액_기부금_지정_종교_세액공제액());

                    setPdfField(
                            fieldListVO,
                            "특별세액공제_기부금_합계_공제대상금액",
                            vo.get특별세액_기부금_정치자금_10만이하_공제대상금액() +
                                    vo.get특별세액_기부금_정치자금_10만초과_공제대상금액() +
                                    vo.get특별세액_기부금_법정_공제대상금액() +
                                    vo.get특별세액_기부금_우리사주_공제대상금액() +
                                    vo.get특별세액_기부금_지정_종교외_공제대상금액() +
                                    vo.get특별세액_기부금_지정_종교_공제대상금액()
                    );
                    setPdfField(
                            fieldListVO,
                            "특별세액공제_기부금_합계_공제세액",
                            vo.get특별세액_기부금_정치자금_10만이하_세액공제액() +
                                    vo.get특별세액_기부금_정치자금_10만초과_세액공제액() +
                                    vo.get특별세액_기부금_법정_세액공제액() +
                                    vo.get특별세액_기부금_우리사주_세액공제액() +
                                    vo.get특별세액_기부금_지정_종교외_세액공제액() +
                                    vo.get특별세액_기부금_지정_종교_세액공제액()
                    );

                }


                setPdfField(fieldListVO, "신고년", today.substring(0, 4));
                setPdfField(fieldListVO, "신고월", today.substring(4, 6));
                setPdfField(fieldListVO, "신고일", today.substring(6, 8));
                setPdfField(fieldListVO, "신고인", resultYE000VO.getEmpName());

                if ("1".equals(resultYE000VO.get외국인단일세율적용())) {
                    setPdfField(fieldListVO, "외국인근로자_단일세율적용신청서_제출여부", "O");
                }
                
                //여기다 작업 - 정범교
                //JSONParser jsonParser = new JSONParser();
                YW800Response jsonObj = request;// 수정해야함 - 정범교
                              
                //특별소득공제
                //특별소득공제 - 주택자금
                setPdfField(fieldListVO, "주택임차차입금_대출기관차입_금액", 
                	Integer.parseInt(String.valueOf(jsonObj.특별소득공제_주택임차차입금_대출기관_국세청자료))
                	+ Integer.parseInt(String.valueOf(jsonObj.특별소득공제_주택임차차입금_대출기관_기타자료))
                	- Integer.parseInt(String.valueOf(jsonObj.특별소득공제_주택임차차입금_대출기관_차감금액))
                );
                setPdfField(fieldListVO, "주택임차차입금_거주자차입_금액", 
                	Integer.parseInt(String.valueOf(jsonObj.특별소득공제_주택임차차입금_거주자_국세청자료))
                	+ Integer.parseInt(String.valueOf(jsonObj.특별소득공제_주택임차차입금_거주자_기타자료))
                	- Integer.parseInt(String.valueOf(jsonObj.특별소득공제_주택임차차입금_거주자_차감금액))
                );
                
                setPdfField(fieldListVO, "장기주택저장차입금_2011년이전차입분_15년미만_금액", 
	                Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2011년이전_15년미만_국세청자료))
	                + Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2011년이전_15년미만_기타자료))
	                - Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2011년이전_15년미만_차감금액))
                );
                setPdfField(fieldListVO, "장기주택저장차입금_2011년이전차입분_15년29년_금액", 
            		Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2011년이전_15년29년_국세청자료))
            		+ Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2011년이전_15년29년_기타자료))
            		- Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2011년이전_15년29년_차감금액))
        		);
                setPdfField(fieldListVO, "장기주택저장차입금_2011년이전차입분_30년이상_금액", 
            		Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2011년이전_30년이상_국세청자료))
            		+ Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2011년이전_30년이상_기타자료))
            		- Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2011년이전_30년이상_차감금액))
        		);
                setPdfField(fieldListVO, "장기주택저장차입금_2012년이후차입분_상환대출_금액",
            		Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2012년이후_15년이상고정OR비거치_국세청자료))
            		+ Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2012년이후_15년이상고정OR비거치_기타자료))
            		- Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2012년이후_15년이상고정OR비거치_차감금액))
        		);
                setPdfField(fieldListVO, "장기주택저장차입금_2012년이후차입분_기타대출_금액",
            		Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2012년이후_15년이상기타_국세청자료))
            		+ Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2012년이후_15년이상기타_기타자료))
            		- Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2012년이후_15년이상기타_차감금액))
        		);
                setPdfField(fieldListVO, "장기주택저장차입금_2015년이후차입분_15년이상_고정금리이면서비거치상환대출_금액",
            		Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상고정AND비거치_국세청자료))
            		+ Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상고정AND비거치_기타자료))
            		- Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상고정AND비거치_차감금액))
        		);
                setPdfField(fieldListVO, "장기주택저장차입금_2015년이후차입분_15년이상_고정금리이거나비거치상환대출_금액",
            		Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상고정OR비거치_국세청자료))
            		+ Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상고정OR비거치_기타자료))
            		- Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상고정OR비거치_차감금액))
        		);
                setPdfField(fieldListVO, "장기주택저장차입금_2015년이후차입분_15년이상_기타대출_금액",
            		Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상기타_국세청자료))
            		+ Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상기타_기타자료))
            		- Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2015년이후_15년이상기타_차감금액))
        		);
                setPdfField(fieldListVO, "장기주택저장차입금_2015년이후차입분_10년15년_고정금이이거나비거치상환대출_금액",
            		Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2015년이후_30년이상고정OR비거치_국세청자료))
            		+ Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2015년이후_30년이상고정OR비거치_기타자료))
            		- Integer.parseInt(String.valueOf(jsonObj.특별소득공제_장기주택저당차입금이자상환액_2015년이후_30년이상고정OR비거치_차감금액))
        		);
                
                //특별소득공제 - 기부금
                YE408VO ye408VO = new YE408VO();
                ye408VO.set계약ID(ye750VO.get계약ID());
                ye408VO.set사용자ID(ye750VO.get사용자ID());
                ye408VO.setStartPage(0);
                ye408VO.setEndPage(99999);
                List<YE408VO> listYE408 = ye408DAO.getYE408List(ye408VO);
                
                if (listYE408.size() > 0) {
                	int 지정기부금종교단체외 = 0;
                	int 지정기부금종교단체외공제 = 0;
                	int 지정기부금종교단체 = 0;
                	int 지정기부금종교단체공제 = 0;
                	
                	int 정치자금기부금 = 0;
                	int 법정기부금기부금액 = 0;
                	int 우리사주조합기부금기부금액 = 0;
                	int 지정기부금종교단체외기부금액 = 0;
                	int 지정기부금종교단체기부금액 = 0;
                	for (YE408VO vo : listYE408) {
                		if(StringUtil.strPaserInt(vo.get기부년도()) == 2013) {
	                		if(("40").equals(vo.get기부금종류코드())) {
	                			지정기부금종교단체외 += vo.get공제대상기부금();
	                			지정기부금종교단체외공제 += vo.get해당연도공제금액();
	                		} else if(("41").equals(vo.get기부금종류코드())){
	                			지정기부금종교단체 += vo.get공제대상기부금();
	                			지정기부금종교단체공제 += vo.get해당연도공제금액();
	                		}
                		}
                		
                		if(("20").equals(vo.get기부금종류코드())) {
                			정치자금기부금 += StringUtil.strPaserLong(vo.get기부금액());
                		} else if(("10").equals(vo.get기부금종류코드())){
                			법정기부금기부금액 += StringUtil.strPaserLong(vo.get기부금액());
                		} else if(("42").equals(vo.get기부금종류코드())){
                			우리사주조합기부금기부금액 += StringUtil.strPaserLong(vo.get기부금액());
                		} else if(("40").equals(vo.get기부금종류코드())){
                			지정기부금종교단체외기부금액 += StringUtil.strPaserLong(vo.get기부금액());
                		} else if(("41").equals(vo.get기부금종류코드())){
                			지정기부금종교단체기부금액 += StringUtil.strPaserLong(vo.get기부금액());
                		}
                		
                		
                	}
                	
                	if(지정기부금종교단체외 > 0) setPdfField(fieldListVO, "기부금_지정기부금(종교단체외)_금액", 지정기부금종교단체외);
                	if(지정기부금종교단체 > 0) setPdfField(fieldListVO, "기부금_지정기부금(종교단체)_금액", 지정기부금종교단체);
                	if((지정기부금종교단체외 + 지정기부금종교단체) > 0) setPdfField(fieldListVO, "기부금이월분_합계_금액", (지정기부금종교단체외 + 지정기부금종교단체));
                	
                	if(지정기부금종교단체외공제 > 0) setPdfField(fieldListVO, "기부금_지정기부금(종교단체외)_공제액", 지정기부금종교단체외공제);
                	if(지정기부금종교단체공제 > 0) setPdfField(fieldListVO, "기부금_지정기부금(종교단체)_공제액", 지정기부금종교단체공제);
                	if((지정기부금종교단체외공제 + 지정기부금종교단체공제) > 0) setPdfField(fieldListVO, "기부금이월분_합계_공제액", (지정기부금종교단체외공제 + 지정기부금종교단체공제));
                	
                	
                	//세액감면및공제 - 세액공제 - 특별세액공제 - 기부금
                    setPdfField(fieldListVO, "특별세액공제_기부금_정치자금기부금10만원이하_금액", 정치자금기부금 > 100000 ? 100000 : 정치자금기부금);
                    setPdfField(fieldListVO, "특별세액공제_기부금_정치자금기부금10만원초과_금액", 정치자금기부금 > 100000 ? 정치자금기부금 - 100000 : 0);
                    setPdfField(fieldListVO, "특별세액공제_기부금_법정기부금_금액", 법정기부금기부금액);
                    setPdfField(fieldListVO, "특별세액공제_기부금_우리사주조합기부금_금액", 우리사주조합기부금기부금액);
                    setPdfField(fieldListVO, "특별세액공제_기부금_지정기부금(종교단체외)_금액", 지정기부금종교단체외기부금액);
                    setPdfField(fieldListVO, "특별세액공제_기부금_지정기부금(종교단체)_금액", 지정기부금종교단체기부금액);
                    setPdfField(fieldListVO, "특별세액공제_기부금_합계_금액", 
                		정치자금기부금
                		+ 법정기부금기부금액
                		+ 우리사주조합기부금기부금액
                		+ 지정기부금종교단체외기부금액
                		+ 지정기부금종교단체기부금액
            		);
                }
                
                //그밖의 소득공제
                //그밖의 소득공제 - 개인연금저축 
                setPdfField(fieldListVO, "개인연금저축_금액",
            		Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_개인연금저축_납입금액))
            		- Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_개인연금저축_차감금액))
        		);
                setPdfField(fieldListVO, "소기업소상공인_공제부금_금액",
            		Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_소기업소상공인공제부금_국세청자료_납입금액))
            		- Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_소기업소상공인공제부금_국세청자료_차감금액))
            		+ Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_소기업소상공인공제부금_기타자료_납입금액))
            		- Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_소기업소상공인공제부금_기타자료_차감금액))
        		);
                
                //그밖의 소득공제 - 주택마련저축
                setPdfField(fieldListVO, "주택마련저축_청약저축_금액",
            		Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_주택마련저축_청약저축_납입금액))
            		- Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_주택마련저축_청약저축_차감금액))
                );
                
                setPdfField(fieldListVO, "주택마련저축_근로자주택마련저축_금액", 
            		Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_주택마련저축_근로자주택마련저축_납입금액))
            		- Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_주택마련저축_근로자주택마련저축_차감금액))
            	);
                
                setPdfField(fieldListVO, "주택마련저축_주택청약종합처축_금액",
            		Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_주택마련저축_주택청약종합저축2014년이전_납입금액))
            		- Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_주택마련저축_주택청약종합저축2014년이전_차감금액))
            		+ Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_주택마련저축_주택청약종합저축2015년이후_납입금액))
            		- Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_주택마련저축_주택청약종합저축2015년이후_차감금액))
        		);
                
                setPdfField(fieldListVO, "주택마련저축_소등공제_합계_금액",
            		Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_주택마련저축_청약저축_납입금액))
            		- Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_주택마련저축_청약저축_차감금액))
                );
                
                //그밖의 소득공제 - 신용카드 등 사용액
                setPdfField(fieldListVO, "신용카드사용액_신용카드_금액", Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_신용카드_신용카드)));
                setPdfField(fieldListVO, "신용카드사용액_직불선불카드_금액", Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_신용카드_직불카드)));
                setPdfField(fieldListVO, "신용카드사용액_현금영수증_금액", Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_신용카드_현금영수증)));
                setPdfField(fieldListVO, "신용카드사용액_도서공연사용분_금액", Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_신용카드_도서공연)));
                setPdfField(fieldListVO, "신용카드사용액_전통시장사용분_금액", Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_신용카드_전통시장)));
                setPdfField(fieldListVO, "신용카드사용액_대중교통이용분_금액", Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_신용카드_대중교통)));
                setPdfField(fieldListVO, "신용카드사용액_합계_금액", 
                		Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_신용카드_신용카드))
                		+ Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_신용카드_직불카드))
                		+ Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_신용카드_현금영수증))
                		+ Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_신용카드_도서공연))
                		+ Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_신용카드_전통시장))
                		+ Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_신용카드_대중교통))
                );
                
                //그밖의 소득공제 - 우리사주조합 출연금
                setPdfField(fieldListVO, "우리사주조합출연금_금액", Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_우리사주조합_납입금액)));
                
                //그밖의 소득공제 - 고용유지중소기업 근로자
                setPdfField(fieldListVO, "고용유지중소기업근로자_금액", Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_고용유지중소기업_납입금액)));
               
                //그밖의 소득공제 - 장기집합투자증권저축
                setPdfField(fieldListVO, "장기집합투자증권저축_금액", 
            		Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_장기잡합투자_납입금액))
            		- Integer.parseInt(String.valueOf(jsonObj.그밖의소득공제_장기잡합투자_차감금액))
                );
                
//                //세액감면및공제 - 세액감면 - 외국인근로자, 중소기업 취업자 감면 - 체크 필요 - 정범교
//                setPdfField(fieldListVO, "입국목적_정부간협약", String.valueOf(jsonObj.그밖에소득공제));
//                setPdfField(fieldListVO, "입국목적_기술도입계약", String.valueOf(jsonObj.그밖에소득공제));
//                setPdfField(fieldListVO, "입국목적_조세특례제한법상감면", String.valueOf(jsonObj.그밖에소득공제));
//                setPdfField(fieldListVO, "입국목적_조세조약상감면", String.valueOf(jsonObj.그밖에소득공제));
//                setPdfField(fieldListVO, "기술도입계약또는근로제공일", String.valueOf(jsonObj.그밖에소득공제));
//                setPdfField(fieldListVO, "감면기간만료일", String.valueOf(jsonObj.그밖에소득공제));
//                setPdfField(fieldListVO, "외국인근로소득에대한감면_접수일", String.valueOf(jsonObj.그밖에소득공제));
//                setPdfField(fieldListVO, "외국인근로소득에대한감면_제출일", String.valueOf(jsonObj.그밖에소득공제));
//                setPdfField(fieldListVO, "근로소득에대한조세조약상면제_접수일", String.valueOf(jsonObj.그밖에소득공제));
//                setPdfField(fieldListVO, "근로소득에대한조세조약상면제_제출일", String.valueOf(jsonObj.그밖에소득공제));
//                setPdfField(fieldListVO, "중소기업취업자감면_취업일", String.valueOf(jsonObj.그밖에소득공제));
//                setPdfField(fieldListVO, "중소기업취업자감면_감면기간종료일", String.valueOf(jsonObj.그밖에소득공제));
                
                //세액감면및공제 - 세액공제 - 특별세액공제 - 교육비 - 사람수
                int[] 취학전교육비리스트 = jsonObj.세액공제_특별세액공제_교육비_취학전;
                int[] 초중고교육비리스트 = jsonObj.세액공제_특별세액공제_교육비_초중고;
                int[] 대학생교육비리스트 = jsonObj.세액공제_특별세액공제_교육비_대학생;
                setPdfField(fieldListVO, "취학전아동수", 취학전교육비리스트.length);
                setPdfField(fieldListVO, "초중고등학생수", 초중고교육비리스트.length);
                setPdfField(fieldListVO, "대학생수", 대학생교육비리스트.length);
                
                //세액감면및공제 - 세액공제 - 연금계좌
                setPdfField(fieldListVO, "연금계좌_과학기술인공제_금액", Integer.parseInt(String.valueOf(jsonObj.세액공제_연금계좌세액공제_과학기술인공제)));
                setPdfField(fieldListVO, "연금계좌_근로자퇴직금여보장법에따른퇴직연금_금액", Integer.parseInt(String.valueOf(jsonObj.세액공제_연금계좌세액공제_퇴직연금공제)));
                setPdfField(fieldListVO, "연금계좌_연금저축_금액", Integer.parseInt(String.valueOf(jsonObj.세액공제_연금계좌세액공제_연금저축공제)));
                setPdfField(fieldListVO, "연금계좌_합계_금액", 
            		Integer.parseInt(String.valueOf(jsonObj.세액공제_연금계좌세액공제_과학기술인공제))
            		+ Integer.parseInt(String.valueOf(jsonObj.세액공제_연금계좌세액공제_퇴직연금공제))
            		+ Integer.parseInt(String.valueOf(jsonObj.세액공제_연금계좌세액공제_연금저축공제))
        		);
                
                //세액감면및공제 - 세액공제 - 특별세액공제
                //세액감면및공제 - 세액공제 - 특별세액공제 - 보험료
                setPdfField(fieldListVO, "특별세액공제_보험료_보장성_금액", Integer.parseInt(String.valueOf(jsonObj.세액공제_특별세액공제_보험료_보장성보험료)));
                setPdfField(fieldListVO, "특별세액공제_보험료_장애인전용보장성_금액", Integer.parseInt(String.valueOf(jsonObj.세액공제_특별세액공제_보험료_장애인전용보장성보험료)));
                setPdfField(fieldListVO, "특별세액공제_보험료_합계_금액", 
            		Integer.parseInt(String.valueOf(jsonObj.세액공제_특별세액공제_보험료_보장성보험료))
            		+ Integer.parseInt(String.valueOf(jsonObj.세액공제_특별세액공제_보험료_장애인전용보장성보험료))
        		);
                
                //세액감면및공제 - 세액공제 - 특별세액공제 - 의료비
                setPdfField(fieldListVO, "특별세액공제_의료비_본인65세이상자장애인건강보험산정특례자_금액", Integer.parseInt(String.valueOf(jsonObj.세액공제_특별세액공제_의료비_본인장애인)));
                setPdfField(fieldListVO, "특별세액공제_의료비_난임시술비_금액", Integer.parseInt(String.valueOf(jsonObj.세액공제_특별세액공제_의료비_난임시술비)));
                setPdfField(fieldListVO, "특별세액공제_의료비_그밖의공제대상자_금액", Integer.parseInt(String.valueOf(jsonObj.세액공제_특별세액공제_의료비_일반)));
                setPdfField(fieldListVO, "특별세액공제_의료비_합계_금액",
                	Integer.parseInt(String.valueOf(jsonObj.세액공제_특별세액공제_의료비_본인장애인))
                	+ Integer.parseInt(String.valueOf(jsonObj.세액공제_특별세액공제_의료비_난임시술비))
                	+ Integer.parseInt(String.valueOf(jsonObj.세액공제_특별세액공제_의료비_일반))
                );
                
                //세액감면및공제 - 세액공제 - 특별세액공제 - 교육비
                int 취학전교육비 = 0;
                int 초중고교육비 = 0;
                int 대학생교육비 = 0;
                for(int i=0 ; i < 취학전교육비리스트.length ; i++){
                	취학전교육비 += Integer.parseInt(String.valueOf(취학전교육비리스트[i]));
                }
                for(int i=0 ; i < 초중고교육비리스트.length ; i++){
                	초중고교육비 += Integer.parseInt(String.valueOf(초중고교육비리스트[i]));
                }
                for(int i=0 ; i < 대학생교육비리스트.length ; i++){
                	대학생교육비 += Integer.parseInt(String.valueOf(대학생교육비리스트[i]));
                }
                setPdfField(fieldListVO, "특별세액공제_교육비_소득자본인_금액", Integer.parseInt(String.valueOf(jsonObj.세액공제_특별세액공제_교육비_본인)));
                setPdfField(fieldListVO, "특별세액공제_교육비_취학전아동_금액", 취학전교육비);
                setPdfField(fieldListVO, "특별세액공제_교육비_초중고등학생_금액", 초중고교육비);
                setPdfField(fieldListVO, "특별세액공제_교육비_대학생_금액", 대학생교육비);
                setPdfField(fieldListVO, "특별세액공제_교육비_장애인_금액", Integer.parseInt(String.valueOf(jsonObj.세액공제_특별세액공제_교육비_장애인)));
                setPdfField(fieldListVO, "특별세액공제_교육비_합계_금액", 
                	Integer.parseInt(String.valueOf(jsonObj.세액공제_특별세액공제_교육비_본인))
                	+ Integer.parseInt(String.valueOf(jsonObj.세액공제_특별세액공제_교육비_장애인))
                	+ 취학전교육비
                	+ 초중고교육비
                	+ 대학생교육비
                );
                
                
                //세액감면및공제 - 세액공제 - 외국납부세액
                YE503VO ye503VO = new YE503VO();
                ye503VO.set계약ID(ye750VO.get계약ID());
                ye503VO.set사용자ID(ye750VO.get사용자ID());
                List<YE503VO> listYE503 = ye503DAO.getYE503List(ye503VO);
                
                if (listYE503.size() > 0) {
                	int 국외원천소득금액 = 0;
                	int 납세액외화금액 = 0;
                	int 납세액원화금액 = 0;
                	String 납세국명 = "";
                	String 신청서제출일 = "";
                	String 근무기간 = "";
                	String 납부일 = "";
                	String 국외근무처 = "";
                	String 직책 = "";
                	for (YE503VO vo : listYE503) {
                		국외원천소득금액 += vo.get국외총급여();
        				납세액외화금액 += vo.get납세액_외화();
        				납세액원화금액 += vo.get납세액_원화();
        				
        				CodeVO codeVO = new CodeVO();
        				codeVO.setGrpCommCode("405");
        				codeVO.setCommCode(vo.get납세국가코드());
        				납세국명 = codeDAO.getCodeName(codeVO);
        				//신청서제출일 = vo.get납세국가코드();
        				//근무기간 = vo.get납세국가코드();
        				납부일 = vo.get납부일();
        				//국외근무처 = vo.get납세국가코드();
        				//직책 = vo.get납세국가코드();
                	}
                	setPdfField(fieldListVO, "외국납부세액_국외원천소득_금액", 국외원천소득금액);
                    setPdfField(fieldListVO, "외국납부세액_납세액(외화)_금액", 납세액외화금액);
                    setPdfField(fieldListVO, "외국납부세액_납세액(원화)_금액", 납세액원화금액);
                    setPdfField(fieldListVO, "외국납부세액_납세국명", 납세국명);
                    setPdfField(fieldListVO, "외국납부세액_신청서제출일", 신청서제출일);
                    setPdfField(fieldListVO, "외국납부세액_근무기간", 근무기간);
                    setPdfField(fieldListVO, "외국납부세액_납부일", 납부일);
                    setPdfField(fieldListVO, "외국납부세액_국외근무처", 국외근무처);
                    setPdfField(fieldListVO, "외국납부세액_직책", 직책);
                }
                
                //세액감면및공제 - 세액공제 - 주택자금차입금이자세액공제
                setPdfField(fieldListVO, "주택자금차입금이자세액공제_이자상환액", String.valueOf(jsonObj.세액공제_기타세액공제_주택차입금이자세액공제));
                
                //세액감면및공제 - 세액공제 - 월세액 세액공제
                setPdfField(fieldListVO, "월세액세액공제_지출액", String.valueOf(jsonObj.세액공제_기타세액공제_월세액공제_연간월세액));
                
                //리스트로 가져와서 보여줘야 할 구간 - 연금 저축 등 소득 및 세액 공제명세서
                //인적사항 (상호, 사업자등록번호, 성명, 주민등록번호, 주소, 주소-전화번호, 사업장소재지, 사업장소재지-전화번호)
                setPdfField(fieldListVO, "연금저축등_소득세액_공제명세서_성명", resultYE000VO.getEmpName());
                setPdfField(fieldListVO, "연금저축등_소득세액_공제명세서_주민등록번호", formatIdentity(resultYE000VO.get개인식별번호(), true));
                
                setPdfField(fieldListVO, "월세액_거주가간주택임차차입금_원리금상환액_공제명세서_성명", resultYE000VO.getEmpName());
                setPdfField(fieldListVO, "월세액_거주가간주택임차차입금_원리금상환액_공제명세서_주민등록번호", formatIdentity(resultYE000VO.get개인식별번호(), true));
                
                
                EmpVO empVO = new EmpVO();
                empVO.setBizId(bizId);
                empVO.set계약ID(ye750VO.get계약ID());
                empVO.setUserId(ye750VO.get사용자ID());
                empVO = empDAO.getEmp(empVO);
                
                setPdfField(fieldListVO, "연금저축등_소득세액_공제명세서_주소", empVO.getAddr1() + " " + empVO.getAddr2());
                setPdfField(fieldListVO, "연금저축등_소득세액_공제명세서_전화번호", empVO.getTelNum());
                
                setPdfField(fieldListVO, "월세액_거주가간주택임차차입금_원리금상환액_공제명세서_주소", empVO.getAddr1() + " " + empVO.getAddr2());
                setPdfField(fieldListVO, "월세액_거주가간주택임차차입금_원리금상환액_공제명세서_전화번호", empVO.getTelNum());
                
                YS030VO ys030VO = new YS030VO();
                ys030VO.set계약ID(ye750VO.get계약ID());
                ys030VO.set사업장ID(resultYE000VO.get사업장ID());
                ys030VO.setStartPage(0);
                ys030VO.setEndPage(9999);
                List<YS030VO> listYS030 = ys030DAO.getYS030List(ys030VO);
                if (listYS030.size() > 0) {
                	setPdfField(fieldListVO, "연금저축등_소득세액_공제명세서_상호", listYS030.get(0).get사업장명());
                	setPdfField(fieldListVO, "연금저축등_소득세액_공제명세서_사업자등록번호", formatBusinessNo(listYS030.get(0).get사업자등록번호()));
                	setPdfField(fieldListVO, "연금저축등_소득세액_공제명세서_사업장소재지", listYS030.get(0).get회사주소1());
                    setPdfField(fieldListVO, "연금저축등_소득세액_공제명세서_사업장전화번호", listYS030.get(0).get회사전화1());
                    
                    setPdfField(fieldListVO, "월세액_거주가간주택임차차입금_원리금상환액_공제명세서_상호", listYS030.get(0).get사업장명());
                	setPdfField(fieldListVO, "월세액_거주가간주택임차차입금_원리금상환액_공제명세서_사업장등록번호", formatBusinessNo(listYS030.get(0).get사업자등록번호()));
                	setPdfField(fieldListVO, "월세액_거주가간주택임차차입금_원리금상환액_공제명세서_사업장소재지", listYS030.get(0).get회사주소1());
                    setPdfField(fieldListVO, "월세액_거주가간주택임차차입금_원리금상환액_공제명세서_사업장전화번호", listYS030.get(0).get회사전화1());
                }
                
                //연금계좌 세액공제 - 퇴직연금계좌
                YE301VO ye301VO = new YE301VO();
                ye301VO.set계약ID(ye750VO.get계약ID());
                ye301VO.set사용자ID(ye750VO.get사용자ID());
                List<YE301VO> listYE301 = ye301DAO.getYE301List(ye301VO);
                
                int index = 0;
                boolean threeRowUp = listYE301.size() > 3 ? true : false;//3줄 이상 여부
                if (listYE301.size() > 0) {
                	//3줄 이상의 결과값이 나올 경우
                	if(threeRowUp) {
                		int 총납입금액 = 0;
                		int 총세액공제금액 = 0;
                		for (YE301VO vo : listYE301) {
	                		index++;
	                		//2번째 줄까지는 그냥 출력한다.
	                		if(index < 3) {
	                			if(("11").equals(vo.get퇴직연금구분코드())) {
		                			setPdfField(fieldListVO, "연금계좌세액공제_퇴직연금계좌_퇴직연금구분"+index, "퇴직연금");
		                		} else if(("12").equals(vo.get퇴직연금구분코드())) {
		                			setPdfField(fieldListVO, "연금계좌세액공제_퇴직연금계좌_퇴직연금구분"+index, "과학 기술인 공제회");
		                		}
		                		CodeVO codeVO = new CodeVO();
		        				codeVO.setGrpCommCode("404");
		        				codeVO.setCommCode(vo.get금융회사등명칭());
		                        setPdfField(fieldListVO, "연금계좌세액공제_퇴직연금계좌_금융회사등"+index, codeDAO.getCodeName(codeVO));
		                        setPdfField(fieldListVO, "연금계좌세액공제_퇴직연금계좌_계좌번호또는증권번호"+index, vo.get계좌번호_증권번호());
		                        setPdfField(fieldListVO, "연금계좌세액공제_퇴직연금계좌_납입금액"+index, vo.get납입금액());
		                        setPdfField(fieldListVO, "연금계좌세액공제_퇴직연금계좌_세액공제금액"+index, vo.get납입금액()-vo.get차감금액());
	                		}
	                		
	                		//3번째 줄에 총합계를 보여주기 위한 누적변수
	                		총납입금액 += vo.get납입금액();
	                		총세액공제금액 += vo.get납입금액()-vo.get차감금액();
	                	}
                		setPdfField(fieldListVO, "연금계좌세액공제_퇴직연금계좌_납입금액3", 총납입금액);
	                    setPdfField(fieldListVO, "연금계좌세액공제_퇴직연금계좌_세액공제금액3", 총세액공제금액);
                		
                	} else {
	                	for (YE301VO vo : listYE301) {
	                		index++;
	                		if(("11").equals(vo.get퇴직연금구분코드())) {
	                			setPdfField(fieldListVO, "연금계좌세액공제_퇴직연금계좌_퇴직연금구분"+index, "퇴직연금");
	                		} else if(("12").equals(vo.get퇴직연금구분코드())) {
	                			setPdfField(fieldListVO, "연금계좌세액공제_퇴직연금계좌_퇴직연금구분"+index, "과학 기술인 공제회");
	                		}
	                		CodeVO codeVO = new CodeVO();
	        				codeVO.setGrpCommCode("404");
	        				codeVO.setCommCode(vo.get금융회사등명칭());
	                        setPdfField(fieldListVO, "연금계좌세액공제_퇴직연금계좌_금융회사등"+index, codeDAO.getCodeName(codeVO));
	                        setPdfField(fieldListVO, "연금계좌세액공제_퇴직연금계좌_계좌번호또는증권번호"+index, vo.get계좌번호_증권번호());
	                        setPdfField(fieldListVO, "연금계좌세액공제_퇴직연금계좌_납입금액"+index, vo.get납입금액());
	                        setPdfField(fieldListVO, "연금계좌세액공제_퇴직연금계좌_세액공제금액"+index, vo.get납입금액()-vo.get차감금액());
	                	}
                	}
                }

                //연금계좌 세액공제 - 연금저축계좌
                YE106VO ye106VO = new YE106VO();
                ye106VO.set계약ID(ye750VO.get계약ID());
                ye106VO.set사용자ID(ye750VO.get사용자ID());
                List<YE106VO> listYE106 = ye106DAO.getYE106List(ye106VO);
                
                YE302VO ye302VO = new YE302VO();
                ye302VO.set계약ID(ye750VO.get계약ID());
                ye302VO.set사용자ID(ye750VO.get사용자ID());
                List<YE302VO> listYE302 = ye302DAO.getYE302List(ye302VO);
                
                index = 0;
                threeRowUp = (listYE106.size() + listYE302.size()) > 3 ? true : false;//3줄 이상 여부 
                if(threeRowUp) {
                	int 총납입금액 = 0;
            		int 총세액공제금액 = 0;
            		int minusFlag = listYE302.size() > 0 ? 1 : 0;
            		if (listYE106.size() > 0) {
	                	for (YE106VO vo : listYE106) {
	                		index++;
	                		if(index < 3-minusFlag) {
		                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_연금저축구분"+index, "개인연금저축");
		                        CodeVO codeVO = new CodeVO();
		        				codeVO.setGrpCommCode("404");
		        				codeVO.setCommCode(vo.get금융회사등명칭());
		                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_금융회사등"+index, codeDAO.getCodeName(codeVO));
		                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_계좌번호또는증권번호"+index, vo.get계좌번호_증권번호());
		                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_납입금액"+index, vo.get납입금액());
		                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_소득세액공제금액"+index, vo.get납입금액()-vo.get차감금액());
	                		}
	                        총납입금액 += vo.get납입금액();
	                        총세액공제금액 += vo.get납입금액()-vo.get차감금액();
	                	}
	                }
	                if (listYE302.size() > 0) {
	                	for (YE302VO vo : listYE302) {
	                		index++;
	                		if(index < 3) {
		                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_연금저축구분"+index, "연금저축");
		                        CodeVO codeVO = new CodeVO();
		        				codeVO.setGrpCommCode("404");
		        				codeVO.setCommCode(vo.get금융회사등명칭());
		                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_금융회사등"+index, codeDAO.getCodeName(codeVO));
		                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_계좌번호또는증권번호"+index, vo.get계좌번호_증권번호());
		                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_납입금액"+index, vo.get납입금액());
		                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_소득세액공제금액"+index, vo.get납입금액()-vo.get차감금액());
	                		}
	                        총납입금액 += vo.get납입금액();
	                        총세액공제금액 += vo.get납입금액()-vo.get차감금액();
	                	}
	                }
	                setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_납입금액3", 총납입금액);
                    setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_소득세액공제금액3", 총세액공제금액);
                } else {
	                if (listYE106.size() > 0) {
	                	for (YE106VO vo : listYE106) {
	                		index++;
	                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_연금저축구분"+index, "개인연금저축");
	                        
	                        CodeVO codeVO = new CodeVO();
	        				codeVO.setGrpCommCode("404");
	        				codeVO.setCommCode(vo.get금융회사등명칭());
	                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_금융회사등"+index, codeDAO.getCodeName(codeVO));
	                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_계좌번호또는증권번호"+index, vo.get계좌번호_증권번호());
	                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_납입금액"+index, vo.get납입금액());
	                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_소득세액공제금액"+index, vo.get납입금액()-vo.get차감금액());
	                	}
	                }
	                if (listYE302.size() > 0) {
	                	for (YE302VO vo : listYE302) {
	                		index++;
	                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_연금저축구분"+index, "연금저축");
	                        CodeVO codeVO = new CodeVO();
	        				codeVO.setGrpCommCode("404");
	        				codeVO.setCommCode(vo.get금융회사등명칭());
	                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_금융회사등"+index, codeDAO.getCodeName(codeVO));
	                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_계좌번호또는증권번호"+index, vo.get계좌번호_증권번호());
	                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_납입금액"+index, vo.get납입금액());
	                        setPdfField(fieldListVO, "연금계좌세액공제_연금저축계좌_소득세액공제금액"+index, vo.get납입금액()-vo.get차감금액());
	                	}
	                }
                }
                
                //연금계좌 세액공제 - 주택마련저축 소득공제
                YE107VO ye107VO = new YE107VO();
                ye107VO.set계약ID(ye750VO.get계약ID());
                ye107VO.set사용자ID(ye750VO.get사용자ID());
                List<YE107VO> listYE107 = ye107DAO.getYE107List(ye107VO);
                
                index = 0;
                threeRowUp = listYE107.size() > 3 ? true : false;//3줄 이상 여부
                if (listYE107.size() > 0) {
                	//3줄 이상의 결과값이 나올 경우
                	if(threeRowUp) {
                		int 총납입금액 = 0;
                		int 총세액공제금액 = 0;
                		for (YE107VO vo : listYE107) {
	                		index++;
	                		//2번째 줄까지는 그냥 출력한다.
	                		if(index < 3) {
	                			if(("31").equals(vo.get주택마련저축구분())) {
	                    			setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_저축구분"+index, "청약저축");
	                    		} else if(("32").equals(vo.get주택마련저축구분())) {
	                    			setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_저축구분"+index, "주택청약종합저축");
	                    		} else if(("34").equals(vo.get주택마련저축구분())) {
	                    			setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_저축구분"+index, "근로자주택마련저축");
	                    		}
	                    		
	                    		CodeVO codeVO = new CodeVO();
	            				codeVO.setGrpCommCode("404");
	            				codeVO.setCommCode(vo.get금융회사등명칭());
	                            setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_금융회사등"+index, codeDAO.getCodeName(codeVO));
	                            setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_계좌번호또는증권번호"+index, vo.get계좌번호_증권번호());
	                            setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_납입금액"+index, vo.get납입금액());
	                            setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_소득공제금액"+index, vo.get납입금액()-vo.get차감금액());
	                		}
	                		
	                		//3번째 줄에 총합계를 보여주기 위한 누적변수
	                		총납입금액 += vo.get납입금액();
	                		총세액공제금액 += vo.get납입금액()-vo.get차감금액();
	                	}
                		setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_납입금액3", 총납입금액);
	                    setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_소득공제금액3", 총세액공제금액);
                	} else {
                		for (YE107VO vo : listYE107) {
                    		index++;
                    		if(("31").equals(vo.get주택마련저축구분())) {
                    			setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_저축구분"+index, "청약저축");
                    		} else if(("32").equals(vo.get주택마련저축구분())) {
                    			setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_저축구분"+index, "주택청약종합저축");
                    		} else if(("34").equals(vo.get주택마련저축구분())) {
                    			setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_저축구분"+index, "근로자주택마련저축");
                    		}
                    		
                    		CodeVO codeVO = new CodeVO();
            				codeVO.setGrpCommCode("404");
            				codeVO.setCommCode(vo.get금융회사등명칭());
                            setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_금융회사등"+index, codeDAO.getCodeName(codeVO));
                            setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_계좌번호또는증권번호"+index, vo.get계좌번호_증권번호());
                            setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_납입금액"+index, vo.get납입금액());
                            setPdfField(fieldListVO, "연금계좌세액공제_주택마련저축소득공제_소득공제금액"+index, vo.get납입금액()-vo.get차감금액());
                    	}
                	}
                }

                //연금계좌 세액공제 - 장기집합투자증권저축 소득공제
                YE109VO ye109VO = new YE109VO();
                ye109VO.set계약ID(ye750VO.get계약ID());
                ye109VO.set사용자ID(ye750VO.get사용자ID());
                List<YE109VO> listYE109 = ye109DAO.getYE109List(ye109VO);
                
                index = 0;
                threeRowUp = listYE109.size() > 3 ? true : false;//3줄 이상 여부
                if (listYE109.size() > 0) {
                	if(threeRowUp) {
                		
                		int 총납입금액 = 0;
                		int 총세액공제금액 = 0;
                		for (YE109VO vo : listYE109) {
	                		index++;
	                		//2번째 줄까지는 그냥 출력한다.
	                		if(index < 3) {
	                			CodeVO codeVO = new CodeVO();
		        				codeVO.setGrpCommCode("404");
		        				codeVO.setCommCode(vo.get취급기관명());
		                		setPdfField(fieldListVO, "연금계좌세액공제_장기집합투자증권처축소득공제_금융회사등"+index, codeDAO.getCodeName(codeVO));
		                        setPdfField(fieldListVO, "연금계좌세액공제_장기집합투자증권처축소득공제_계좌번호또는증권번호"+index, vo.get계좌번호_증권번호());
		                        setPdfField(fieldListVO, "연금계좌세액공제_장기집합투자증권처축소득공제_납입금액"+index, vo.get납입금액());
		                        setPdfField(fieldListVO, "연금계좌세액공제_장기집합투자증권처축소득공제_소득공제금액"+index, vo.get납입금액()-vo.get차감금액());
	                		}
	                		//3번째 줄에 총합계를 보여주기 위한 누적변수
	                		총납입금액 += vo.get납입금액();
	                		총세액공제금액 += vo.get납입금액()-vo.get차감금액();
	                	}
                		setPdfField(fieldListVO, "연금계좌세액공제_장기집합투자증권처축소득공제_납입금액3", 총납입금액);
	                    setPdfField(fieldListVO, "연금계좌세액공제_장기집합투자증권처축소득공제_소득공제금액3", 총세액공제금액);
                	} else {
	                	for (YE109VO vo : listYE109) {
	                		index++;
	                		CodeVO codeVO = new CodeVO();
	        				codeVO.setGrpCommCode("404");
	        				codeVO.setCommCode(vo.get취급기관명());
	                		setPdfField(fieldListVO, "연금계좌세액공제_장기집합투자증권처축소득공제_금융회사등"+index, codeDAO.getCodeName(codeVO));
	                        setPdfField(fieldListVO, "연금계좌세액공제_장기집합투자증권처축소득공제_계좌번호또는증권번호"+index, vo.get계좌번호_증권번호());
	                        setPdfField(fieldListVO, "연금계좌세액공제_장기집합투자증권처축소득공제_납입금액"+index, vo.get납입금액());
	                        setPdfField(fieldListVO, "연금계좌세액공제_장기집합투자증권처축소득공제_소득공제금액"+index, vo.get납입금액()-vo.get차감금액());
	                	}
                	}
                }
                
                //연금계좌 세액공제 - 중소기업창업투자조합 출자 등에 대한 소득공제
                YE202VO ye202VO = new YE202VO();
                ye202VO.set계약ID(ye750VO.get계약ID());
                ye202VO.set사용자ID(ye750VO.get사용자ID());
                List<YE202VO> listYE202 = ye202DAO.getYE202List(ye202VO);
                
                index = 0;
                threeRowUp = listYE202.size() > 3 ? true : false;//3줄 이상 여부
                //그밖의 소득공제 - 투자조합 출자 등
                int 출자투자분2016조합 = 0;
                int 출자투자분2016벤처 = 0;
                int 출자투자분2017조합 = 0;
                int 출자투자분2017벤처 = 0;
                int 출자투자분2018조합 = 0;
                int 출자투자분2018벤처 = 0;
                if (listYE202.size() > 0) {
                	
                	int 총납입금액 = 0;
            		int 총세액공제금액 = 0;
                	for (YE202VO vo : listYE202) {
                		String 연금계좌세액공제_중소기업창업투자조합소득공제_투자구분 = "";
                		if("1".equals(vo.get구분코드())) {
                			연금계좌세액공제_중소기업창업투자조합소득공제_투자구분 = "조합";
                		} else if("2".equals(vo.get구분코드())) {
                			연금계좌세액공제_중소기업창업투자조합소득공제_투자구분 = "벤처";
                		}
                		if(("2016").equals(vo.get년도())) {
                			if(("1").equals(vo.get구분코드())) {
                				출자투자분2016조합 += vo.get납입금액();
                			} else if(("2").equals(vo.get구분코드())) {
                				출자투자분2016벤처 += vo.get납입금액();
                			}
                		} else if(("2017").equals(vo.get년도())) {
                			if(("1").equals(vo.get구분코드())) {
                				출자투자분2017조합 += vo.get납입금액();
                			} else if(("2").equals(vo.get구분코드())) {
                				출자투자분2017벤처 += vo.get납입금액();
                			}
                		} else if(("2018").equals(vo.get년도())) {
                			if(("1").equals(vo.get구분코드())) {
                				출자투자분2018조합 += vo.get납입금액();
                			} else if(("2").equals(vo.get구분코드())) {
                				출자투자분2018벤처 += vo.get납입금액();
                			}
                		}
                		
                		index++;
                		if(threeRowUp) {
                    		if(index < 3) {
                    			CodeVO codeVO = new CodeVO();
    	        				codeVO.setGrpCommCode("404");
    	        				codeVO.setCommCode(vo.get금융회사등명칭());
    	        				setPdfField(fieldListVO, "연금계좌세액공제_중소기업창업투자조합소득공제_투자년도"+index, vo.get년도());
    	        				setPdfField(fieldListVO, "연금계좌세액공제_중소기업창업투자조합소득공제_투자구분"+index, 연금계좌세액공제_중소기업창업투자조합소득공제_투자구분);
    	                		setPdfField(fieldListVO, "연금계좌세액공제_중소기업창업투자조합소득공제_금융회사등"+index, codeDAO.getCodeName(codeVO));
    	                        setPdfField(fieldListVO, "연금계좌세액공제_중소기업창업투자조합소득공제_계좌번호또는증권번호"+index, vo.get계좌번호_증권번호());
    	                        setPdfField(fieldListVO, "연금계좌세액공제_중소기업창업투자조합소득공제_납입금액"+index, vo.get납입금액());
    	                        setPdfField(fieldListVO, "연금계좌세액공제_중소기업창업투자조합소득공제_소득공제금액"+index, null); // ---------확인
                    		} 
                    		총납입금액 += vo.get납입금액();
                    		총세액공제금액 += vo.get납입금액();// ---------확인
                    		
                		} else {
	                		CodeVO codeVO = new CodeVO();
	        				codeVO.setGrpCommCode("404");
	        				codeVO.setCommCode(vo.get금융회사등명칭());
	        				setPdfField(fieldListVO, "연금계좌세액공제_중소기업창업투자조합소득공제_투자년도"+index, vo.get년도());
	        				setPdfField(fieldListVO, "연금계좌세액공제_중소기업창업투자조합소득공제_투자구분"+index, 연금계좌세액공제_중소기업창업투자조합소득공제_투자구분);
	                		setPdfField(fieldListVO, "연금계좌세액공제_중소기업창업투자조합소득공제_금융회사등"+index, codeDAO.getCodeName(codeVO));
	                        setPdfField(fieldListVO, "연금계좌세액공제_중소기업창업투자조합소득공제_계좌번호또는증권번호"+index, vo.get계좌번호_증권번호());
	                        setPdfField(fieldListVO, "연금계좌세액공제_중소기업창업투자조합소득공제_납입금액"+index, vo.get납입금액());
	                        setPdfField(fieldListVO, "연금계좌세액공제_중소기업창업투자조합소득공제_소득공제금액"+index, null); // ---------확인
                		}
                	}
                	setPdfField(fieldListVO, "투자조합출자등_2016년출자투자분_조합_금액", 출자투자분2016조합);
					setPdfField(fieldListVO, "투자조합출자등_2016년출자투자분_벤처_금액", 출자투자분2016벤처);
					setPdfField(fieldListVO, "투자조합출자등_2017년출자투자분_조합_금액", 출자투자분2017조합);
					setPdfField(fieldListVO, "투자조합출자등_2017년출자투자분_벤처_금액", 출자투자분2017벤처);
					setPdfField(fieldListVO, "투자조합출자등_2018년출자투자분_조합_금액", 출자투자분2018조합);
					setPdfField(fieldListVO, "투자조합출자등_2018년출자투자분_벤처_금액", 출자투자분2018벤처);
					setPdfField(fieldListVO, "투자조합출자_소득공제_합계_금액", 출자투자분2016조합+출자투자분2016벤처+출자투자분2017조합+출자투자분2017벤처+출자투자분2018조합+출자투자분2018벤처);
					
					if(threeRowUp) {
						setPdfField(fieldListVO, "연금계좌세액공제_중소기업창업투자조합소득공제_납입금액3", 총납입금액);
						setPdfField(fieldListVO, "연금계좌세액공제_중소기업창업투자조합소득공제_소득공제금액3", 총세액공제금액);
					}
                }
              
                //리스트로 가져와서 보여줘야 할 구간 - 월세액/거주자 간 주택임차차입금 원리금 상환액 소득 및 세액공제 명세서
                //월세액 세액공제 명세
                YE105VO ye105VO = new YE105VO();
                ye105VO.set계약ID(ye750VO.get계약ID());
                ye105VO.set사용자ID(ye750VO.get사용자ID());
                List<YE105VO> listYE105 = ye105DAO.getYE105List(ye105VO);
                
                index = 0;
                threeRowUp = listYE105.size() > 3 ? true : false;//3줄 이상 여부
                if (listYE105.size() > 0) {
                	if(threeRowUp) {
                		int 총연간월세액 = 0;
                		int 총공제대상금액 = 0;
                		
                		for (YE105VO vo : listYE105) {
                    		index++;
                    		
                    		//2번째 줄까지는 그냥 출력한다.
                			if(index < 3) {
	                    		setPdfField(fieldListVO, "월세액_세액공제_임대인성명(상호)"+index, vo.get임대인성명_상호());
	                            setPdfField(fieldListVO, "월세액_세액공제_주민등록번호(사업자번호)"+index, vo.get개인식별번호());
	                            
	                            if(("1").equals(vo.get유형코드())) {
	                            	setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "단독주택");
	                    		} else if(("2").equals(vo.get유형코드())) {
	                    			setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "다가구");
	                    		} else if(("3").equals(vo.get유형코드())) {
	                    			setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "다세대주택");
	                    		} else if(("4").equals(vo.get유형코드())) {
	                    			setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "연립주택");
	                    		} else if(("5").equals(vo.get유형코드())) {
	                    			setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "아파트");
	                    		} else if(("6").equals(vo.get유형코드())) {
	                    			setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "오피스텔");
	                    		} else if(("7").equals(vo.get유형코드())) {
	                    			setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "고시원");
	                    		} else if(("8").equals(vo.get유형코드())) {
	                    			setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "기타");
	                    		}
	                            setPdfField(fieldListVO, "월세액_세액공제_계약면적"+index, vo.get계약면적());
	                            setPdfField(fieldListVO, "월세액_세액공제_임대차계약서주소지"+index, vo.get임대차_주소지_물건지());
	                            setPdfField(fieldListVO, "월세액_세액공제_계약서임대차계약기간_개시일"+index, DateUtil.getDateType(1, vo.get임대차_계약개시일()));
	                            setPdfField(fieldListVO, "월세액_세액공제_계약서임대차계약기간_종료일"+index, DateUtil.getDateType(1, vo.get임대차_계약종료일()));
	                            setPdfField(fieldListVO, "월세액_세액공제_연간월세액"+index, vo.get연간_월세액());
	                            setPdfField(fieldListVO, "월세액_세액공제_세액공제금액"+index, vo.get공제대상금액());
                			}
                			//3번째 줄에 총합계를 보여주기 위한 누적변수
                			총연간월세액 += vo.get연간_월세액();
                			총공제대상금액 += vo.get공제대상금액();
                		}
                		
                		 setPdfField(fieldListVO, "월세액_세액공제_연간월세액3", 총연간월세액);
                         setPdfField(fieldListVO, "월세액_세액공제_세액공제금액3", 총공제대상금액);
                		
                	} else {
                		for (YE105VO vo : listYE105) {
                    		index++;
                    		logger.info("월세액_세액공제_임대인성명 >>> : " + vo.get임대인성명_상호());
                    		setPdfField(fieldListVO, "월세액_세액공제_임대인성명(상호)"+index, vo.get임대인성명_상호());
                            setPdfField(fieldListVO, "월세액_세액공제_주민등록번호(사업자번호)"+index, vo.get개인식별번호());
                            
                            if(("1").equals(vo.get유형코드())) {
                            	setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "단독주택");
                    		} else if(("2").equals(vo.get유형코드())) {
                    			setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "다가구");
                    		} else if(("3").equals(vo.get유형코드())) {
                    			setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "다세대주택");
                    		} else if(("4").equals(vo.get유형코드())) {
                    			setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "연립주택");
                    		} else if(("5").equals(vo.get유형코드())) {
                    			setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "아파트");
                    		} else if(("6").equals(vo.get유형코드())) {
                    			setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "오피스텔");
                    		} else if(("7").equals(vo.get유형코드())) {
                    			setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "고시원");
                    		} else if(("8").equals(vo.get유형코드())) {
                    			setPdfField(fieldListVO, "월세액_세액공제_유형"+index, "기타");
                    		}
                            setPdfField(fieldListVO, "월세액_세액공제_계약면적"+index, vo.get계약면적());
                            setPdfField(fieldListVO, "월세액_세액공제_임대차계약서주소지"+index, vo.get임대차_주소지_물건지());
                            setPdfField(fieldListVO, "월세액_세액공제_계약서임대차계약기간_개시일"+index, DateUtil.getDateType(1, vo.get임대차_계약개시일()));
                            setPdfField(fieldListVO, "월세액_세액공제_계약서임대차계약기간_종료일"+index, DateUtil.getDateType(1, vo.get임대차_계약종료일()));
                            setPdfField(fieldListVO, "월세액_세액공제_연간월세액"+index, vo.get연간_월세액());
                            setPdfField(fieldListVO, "월세액_세액공제_세액공제금액"+index, vo.get공제대상금액());
                    	}
                	}
                }
                
                //거주자 간 주택임차차입금 원리금 상환액 소득공제 명세 - 금전소비대차 계약내용
                YE102VO ye102VO = new YE102VO();
                ye102VO.set계약ID(ye750VO.get계약ID());
                ye102VO.set사용자ID(ye750VO.get사용자ID());
                List<YE102VO> listYE102 = ye102DAO.getYE102List(ye102VO);
                
                index = 0;
                threeRowUp = listYE102.size() > 3 ? true : false;//3줄 이상 여부
                if (listYE102.size() > 0) {
                	if(threeRowUp) {
                		int 총합계 = 0;
                		int 총원금 = 0;
                		int 총이자 = 0;
                		int 총공제금액 = 0;
                		
                		for (YE102VO vo : listYE102) {
                    		index++;
                    		
                    		//2번째 줄까지는 그냥 출력한다.
                			if(index < 3) {
                				setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_대주"+index, vo.get대주성명());
                                setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_주민등록번호"+index, vo.get개인식별번호());
                                setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_계약기간"+index, DateUtil.getDateType(1, vo.get금전소비대차_계약개시일()) + "~" + DateUtil.getDateType(1, vo.get금전소비대차_계약종료일()));
                                setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_차입금이자율"+index, vo.get차입금_이자율());
                                setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_합계"+index, vo.get원금() + vo.get이자());
                                setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_원금"+index, vo.get원금());
                                setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_이자"+index, vo.get이자());
                                setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_공제금액"+index, null); //------확인
                			}
                			
                			//3번째 줄에 총합계를 보여주기 위한 누적변수
                			총합계 += vo.get원금() + vo.get이자();
                			총원금 += vo.get원금(); 
                			총이자 += vo.get이자(); 
                			총공제금액 += vo.get이자();//------확인
                    	}
                		setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_합계3", 총합계);
                        setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_원금3", 총원금);
                        setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_이자3", 총이자);
                        setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_공제금액3", 총공제금액);
                		
                	} else {
                		for (YE102VO vo : listYE102) {
                    		index++;
                    		setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_대주"+index, vo.get대주성명());
                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_주민등록번호"+index, vo.get개인식별번호());
                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_계약기간"+index, DateUtil.getDateType(1, vo.get금전소비대차_계약개시일()) + "~" + DateUtil.getDateType(1, vo.get금전소비대차_계약종료일()));
                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_차입금이자율"+index, vo.get차입금_이자율());
                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_합계"+index, vo.get원금() + vo.get이자());
                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_원금"+index, vo.get원금());
                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_금전소비대차_이자"+index, vo.get이자());
                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_공제금액"+index, null); //------확인
                    	}
                	}
                }
                

                //거주자 간 주택임차차입금 원리금 상환액 소득공제 명세 - 임대차 계약내용
                YE103VO ye103VO = new YE103VO();
                ye103VO.set계약ID(ye750VO.get계약ID());
                ye103VO.set사용자ID(ye750VO.get사용자ID());
                List<YE103VO> listYE103 = ye103DAO.getYE103List(ye103VO);
                
                index = 0;
                threeRowUp = listYE103.size() > 3 ? true : false;//3줄 이상 여부
                if (listYE103.size() > 0) {
                	if(threeRowUp) {
                		int 총전세보증금 = 0;
                		
                		for (YE103VO vo : listYE103) {
                    		index++;
                    		//2번째 줄까지는 그냥 출력한다.
                			if(index < 3) {
	                    		setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_임대인성명(상호)"+index, vo.get임대인성명_상호());
	                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_주민등록번호(사업자번호)"+index, vo.get개인식별번호());
	                            
	                            if(("1").equals(vo.get유형코드())) {
	                            	setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "단독주택");
	                    		} else if(("2").equals(vo.get유형코드())) {
	                    			setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "다가구");
	                    		} else if(("3").equals(vo.get유형코드())) {
	                    			setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "다세대주택");
	                    		} else if(("4").equals(vo.get유형코드())) {
	                    			setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "연립주택");
	                    		} else if(("5").equals(vo.get유형코드())) {
	                    			setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "아파트");
	                    		} else if(("6").equals(vo.get유형코드())) {
	                    			setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "오피스텔");
	                    		} else if(("7").equals(vo.get유형코드())) {
	                    			setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "고시원");
	                    		} else if(("8").equals(vo.get유형코드())) {
	                    			setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "기타");
	                    		}
	                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_계약면적"+index, vo.get계약면적());
	                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_계약서상주소지"+index, vo.get임대차_주소지_물건지());
	                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_계약서상계약기간_개시일"+index, DateUtil.getDateType(1, vo.get임대차_계약개시일()));
	                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_계약서상계약기간_종료일"+index, DateUtil.getDateType(1, vo.get임대차_계약종료일()));
	                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_전세보증금"+index, vo.get전세보증금());
                			}
                			총전세보증금 += vo.get전세보증금();
                    	}
                		setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_전세보증금3", 총전세보증금);

                	} else {
                		for (YE103VO vo : listYE103) {
                    		index++;
                    		setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_임대인성명(상호)"+index, vo.get임대인성명_상호());
                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_주민등록번호(사업자번호)"+index, vo.get개인식별번호());
                            
                            if(("1").equals(vo.get유형코드())) {
                            	setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "단독주택");
                    		} else if(("2").equals(vo.get유형코드())) {
                    			setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "다가구");
                    		} else if(("3").equals(vo.get유형코드())) {
                    			setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "다세대주택");
                    		} else if(("4").equals(vo.get유형코드())) {
                    			setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "연립주택");
                    		} else if(("5").equals(vo.get유형코드())) {
                    			setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "아파트");
                    		} else if(("6").equals(vo.get유형코드())) {
                    			setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "오피스텔");
                    		} else if(("7").equals(vo.get유형코드())) {
                    			setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "고시원");
                    		} else if(("8").equals(vo.get유형코드())) {
                    			setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_유형"+index, "기타");
                    		}
                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_계약면적"+index, vo.get계약면적());
                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_계약서상주소지"+index, vo.get임대차_주소지_물건지());
                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_계약서상계약기간_개시일"+index, DateUtil.getDateType(1, vo.get임대차_계약개시일()));
                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_계약서상계약기간_종료일"+index, DateUtil.getDateType(1, vo.get임대차_계약종료일()));
                            setPdfField(fieldListVO, "거주자간주택임차차입금_원리금상환액_임대차_전세보증금"+index, vo.get전세보증금());
                    	}
                	}
                }

		    	System.out.println(":::::::::::::::::::: createY02PDF 1 :::::::::::::::::::");
                // PDF영수증 파일명 : 사번_성명_귀속년도_영수증이름
                createPdfFile(
                        fieldListVO,
                        originalPdfPath, resultYE000VO.getEmpNo() + "_" + resultYE000VO.getEmpName() + "_" + 계약년도 + "_소득세액공제신고서.pdf",
                        targetPdfPath, targetPdfName,
                        resultYE000VO.getEmpName() + "_소득세액공제신고서",
                        ye750VO
                );
            }
        }
    }

    private void set근로소득공제신고서_인적공제(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO, Map<String, String> family) throws Exception {
        YE001VO ye001VO = new YE001VO();
        ye001VO.set계약ID(계약ID);
        ye001VO.set사용자ID(사용자ID);
        ye001VO.setStartPage(0);
        ye001VO.setEndPage(9999);
        List<YE001VO> listYE000 = ye001DAO.getYE001List(ye001VO);

        int index = 1;
        int 인적공제_공제인원수 = 0;
        int 인적공제_자녀수 = 0;

        for (YE001VO vo : listYE000) {
            if ("1".equals(vo.get기본공제())) {
                인적공제_공제인원수++;
            }

            family.put(vo.get부양가족ID(), String.valueOf(index));

            if ("0".equals(vo.get가족관계())) {
                if ("1".equals(vo.get부녀자())) {
                    setPdfField(fieldListVO, "인적공제_부녀자1", "O");
                }
                if ("1".equals(vo.get한부모())) {
                    setPdfField(fieldListVO, "인적공제_한부모1", "O");
                }
            } else {
                setPdfField(fieldListVO, "인적공제_관계코드" + index, vo.get가족관계());
                setPdfField(fieldListVO, "인적공제_주민등록번호" + index, formatIdentity(vo.get개인식별번호(), true));

                if ("1".equals(vo.get기본공제())) {
                    setPdfField(fieldListVO, "인적공제_기본공제여부" + index, "O");
                }
                
                if ("4".equals(vo.get가족관계())) {
                	인적공제_자녀수 += 1;
            	}
            }

            setPdfField(fieldListVO, "인적공제_성명" + index, vo.get성명());
            setPdfField(fieldListVO, "인적공제_내외국인" + index, vo.get내외국인());

            if (StringUtil.strPaserInt(vo.get나이()) >= 70) {
                setPdfField(fieldListVO, "인적공제_경로우대" + index, "O");
            } else if (StringUtil.strPaserInt(vo.get나이()) <= 6) {
                setPdfField(fieldListVO, "인적공제_6세이하" + index, "O");
            }

            if ("1".equals(vo.get장애인()) || "2".equals(vo.get장애인()) || "3".equals(vo.get장애인())) {
                setPdfField(fieldListVO, "인적공제_장애인" + index, "O");
            }

            if ("1".equals(vo.get출산입양()) || "2".equals(vo.get출산입양()) || "3".equals(vo.get출산입양())) {
                setPdfField(fieldListVO, "인적공제_출산입양" + index, "O");
            }

            index++;
        }
        setPdfField(fieldListVO, "인적공제_자녀수", 인적공제_자녀수);
        setPdfField(fieldListVO, "인적공제_공제인원수", 인적공제_공제인원수);
    }

    private void set근로소득공제신고서_건강고용(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO) throws Exception {
        YE000VO ye000VO = new YE000VO();
        ye000VO.set계약ID(계약ID);
        ye000VO.set사용자ID(사용자ID);
        List<YE000VO> listSum = ye003DAO.getYE003SumList(ye000VO);

        int 국세청_보험료_건강고용 = 0;
        int 국세청_보험료_건강 = 0;
        int 국세청_보험료_고용 = 0;
        if (listSum != null && listSum.size() > 0) {
            for (YE000VO vo : listSum) {
                국세청_보험료_건강고용 += StringUtil.strPaserLong(vo.get건강보험료());
                국세청_보험료_건강고용 += StringUtil.strPaserLong(vo.get장기요양보험료());
                국세청_보험료_건강고용 += StringUtil.strPaserLong(vo.get고용보험료());
                
                국세청_보험료_건강 += StringUtil.strPaserLong(vo.get건강보험료()) + StringUtil.strPaserLong(vo.get장기요양보험료());
                국세청_보험료_고용 += StringUtil.strPaserLong(vo.get고용보험료());
            }
        }

        YE052VO ye052VO = new YE052VO();
        ye052VO.set계약ID(계약ID);
        ye052VO.set사용자ID(사용자ID);
        List<YE052VO> listYE052 = ye052DAO.getYE052List(ye052VO);

        int 기타_보험료_건강고용 = 0;
        int 기타_보험료_건강 = 0;
        int 기타_보험료_고용 = 0;
        if (listYE052 != null && listYE052.size() > 0) {
            for (YE052VO vo : listYE052) {
                기타_보험료_건강고용 += vo.get추가납입금액();
                
                // 보험료구분 1: 건강보험료 2:장기요양보험료
                if (StringUtils.equals(vo.get보험료구분(), "1") || StringUtils.equals(vo.get보험료구분(), "2")) {
                	기타_보험료_건강 += vo.get추가납입금액();
                } 
                // 보험료구분 3:고용보험료
                else if (StringUtils.equals(vo.get보험료구분(), "3")) {
                	기타_보험료_고용 += vo.get추가납입금액();
                }
            }
        }

        setPdfField(fieldListVO, "인적공제_국세청_건강고용등1", 국세청_보험료_건강고용);
        setPdfField(fieldListVO, "인적공제_국세청_건강고용등합계", 국세청_보험료_건강고용);
        setPdfField(fieldListVO, "인적공제_기타_건강고용등1", 기타_보험료_건강고용);
        setPdfField(fieldListVO, "인적공제_기타_건강고용등합계", 기타_보험료_건강고용);
        
        setPdfField(fieldListVO, "인적공제_국세청_건강1", 국세청_보험료_건강);
        setPdfField(fieldListVO, "인적공제_국세청_건강합계", 국세청_보험료_건강);
        setPdfField(fieldListVO, "인적공제_국세청_고용1", 국세청_보험료_고용);
        setPdfField(fieldListVO, "인적공제_국세청_고용합계", 국세청_보험료_고용);
        setPdfField(fieldListVO, "인적공제_기타_건강1", 기타_보험료_건강);
        setPdfField(fieldListVO, "인적공제_기타_건강합계", 기타_보험료_건강);
        setPdfField(fieldListVO, "인적공제_기타_고용1", 기타_보험료_고용);
        setPdfField(fieldListVO, "인적공제_기타_고용합계", 기타_보험료_고용);
    }

    private void set근로소득공제신고서_보험료(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO, Map<String, String> family) {
        YE401VO ye401VO = new YE401VO();
        ye401VO.set계약ID(계약ID);
        ye401VO.set사용자ID(사용자ID);
        List<YE401VO> listYE401 = ye401DAO.getYE401List(ye401VO);

        Map<String, int[]> sum = new HashMap<>();
        if (listYE401 != null && listYE401.size() > 0) {
            int 금액;
            int[] value;

            for (YE401VO vo : listYE401) {
                금액 = vo.get납입금액() - vo.get차감금액();

                if (sum.containsKey(vo.get부양가족ID())) {
                    value = sum.get(vo.get부양가족ID());
                } else {
                    value = new int[]{0, 0, 0, 0};
                    sum.put(vo.get부양가족ID(), value);
                }

                if ("1".equals(vo.get보험구분코드())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        // 인적공제_국세청_보장성
                        value[0] += 금액;
                    } else {
                        // 인적공제_기타_보장성
                        value[1] += 금액;
                    }
                } else if ("2".equals(vo.get보험구분코드())) {
                    if (CodeUtils.is국세청(vo.get자료구분코드())) {
                        // 인적공제_국세청_장애인전용보장성
                        value[2] += 금액;
                    } else {
                        // 인적공제_기타_장애인전용보장성합계
                        value[3] += 금액;
                    }
                }
            }
        }

        int[] total = new int[]{0, 0, 0, 0};
        int[] value;
        for (String 부양가족ID : sum.keySet()) {
            value = sum.get(부양가족ID);
            total[0] += value[0];
            total[1] += value[1];
            total[2] += value[2];
            total[3] += value[3];

            setPdfField(fieldListVO, "인적공제_국세청_보장성" + family.get(부양가족ID), value[0]);
            setPdfField(fieldListVO, "인적공제_기타_보장성" + family.get(부양가족ID), value[1]);
            setPdfField(fieldListVO, "인적공제_국세청_장애인전용보장성" + family.get(부양가족ID), value[2]);
            setPdfField(fieldListVO, "인적공제_기타_장애인전용보장성" + family.get(부양가족ID), value[3]);
        }

        setPdfField(fieldListVO, "인적공제_국세청_보장성합계", total[0]);
        setPdfField(fieldListVO, "인적공제_기타_보장성합계", total[1]);
        setPdfField(fieldListVO, "인적공제_국세청_장애인전용보장성합계", total[2]);
        setPdfField(fieldListVO, "인적공제_기타_장애인전용보장성합계", total[3]);
    }

    private void set근로소득공제신고서_의료비(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO, Map<String, String> family) {
        YE402VO ye402VO = new YE402VO();
        ye402VO.set계약ID(계약ID);
        ye402VO.set사용자ID(사용자ID);
        List<YE402VO> listYE402 = ye402DAO.getYE402List(ye402VO);

        Map<String, int[]> sum = new HashMap<>();
        if (listYE402 != null && listYE402.size() > 0) {
            setPdfField(fieldListVO, "그밖의추가제출서류_의료비지급멩세서_제출여부", "O");

            int 금액;
            int[] value;

            for (YE402VO vo : listYE402) {
                금액 = vo.get지출액() - vo.get차감금액();

                if (sum.containsKey(vo.get부양가족ID())) {
                    value = sum.get(vo.get부양가족ID());
                } else {
                    value = new int[]{0, 0, 0, 0};
                    sum.put(vo.get부양가족ID(), value);
                }

                // 1: 일반, 2: 안경
                if ("1".equals(vo.get의료비유형()) || "2".equals(vo.get의료비유형())) {
                	if (CodeUtils.is국세청(vo.get자료구분코드())) {
                		// 인적공제_국세청_의료비_일반
                		value[0] += 금액;
                	} else {
                		// 인적공제_기타_의료비_일반
                		value[1] += 금액;
                	}                	               
            	// 3: 난임 
                } else if ("3".equals(vo.get의료비유형())) {
                	if (CodeUtils.is국세청(vo.get자료구분코드())) {
                		// 인적공제_국세청_의료비_난임
                		value[2] += 금액;
                	} else {
                		// 인적공제_기타_의료비_난임
                		value[3] += 금액;
                	}
                }
            }
        }

        int[] total = new int[]{0, 0, 0, 0};
        int[] value;
        for (String 부양가족ID : sum.keySet()) {
            value = sum.get(부양가족ID);
            total[0] += value[0];
            total[1] += value[1];
            total[2] += value[2];
            total[3] += value[3];

            setPdfField(fieldListVO, "인적공제_국세청_의료비" + family.get(부양가족ID), value[0]);
            setPdfField(fieldListVO, "인적공제_기타_의료비" + family.get(부양가족ID), value[1]);
            
            setPdfField(fieldListVO, "인적공제_국세청_의료비_일반" + family.get(부양가족ID), value[0]);
            setPdfField(fieldListVO, "인적공제_기타_의료비_일반" + family.get(부양가족ID), value[1]);
            setPdfField(fieldListVO, "인적공제_국세청_의료비_난임" + family.get(부양가족ID), value[2]);
            setPdfField(fieldListVO, "인적공제_기타_의료비_난임" + family.get(부양가족ID), value[3]);
        }

        setPdfField(fieldListVO, "인적공제_국세청_의료비합계", total[0]);
        setPdfField(fieldListVO, "인적공제_기타_의료비합계", total[1]);
        
        setPdfField(fieldListVO, "인적공제_국세청_의료비_일반합계", total[0]);
        setPdfField(fieldListVO, "인적공제_기타_의료비_일반합계", total[1]);
        setPdfField(fieldListVO, "인적공제_국세청_의료비_난임합계", total[2]);
        setPdfField(fieldListVO, "인적공제_기타_의료비_난임합계", total[3]);
    }

    private void set근로소득공제신고서_교육비(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO, Map<String, String> family) {
        YE403VO ye403VO = new YE403VO();
        ye403VO.set계약ID(계약ID);
        ye403VO.set사용자ID(사용자ID);
        List<YE403VO> listYE403 = ye403DAO.getYE403List(ye403VO);

        Map<String, int[]> sum = new HashMap<>();
        if (listYE403 != null && listYE403.size() > 0) {
            int 교육비;
            int[] value;

            for (YE403VO vo : listYE403) {
                교육비 = vo.get공납금() - vo.get공납금_차감금액() + vo.get교복구입비() - vo.get교복구입비_차감금액() + vo.get체험학습비() - vo.get체험학습비_차감금액();

                if (sum.containsKey(vo.get부양가족ID())) {
                    value = sum.get(vo.get부양가족ID());
                } else {
                    value = new int[]{0, 0, 0, 0};
                    sum.put(vo.get부양가족ID(), value);
                }

                // 1:본인 2:취학전아동 3:초중고 4:대학생 5:장애인
                if ("1".equals(vo.get공제종류코드()) || "2".equals(vo.get공제종류코드()) || "3".equals(vo.get공제종류코드()) || "4".equals(vo.get공제종류코드())) {                	
                	if (CodeUtils.is국세청(vo.get자료구분코드())) {
                		// 인적공제_국세청_교육비_일반
                		value[0] += 교육비;
                	} else {
                		// 인적공제_기타_교육비_일반
                		value[1] += 교육비;
                	}
                } else if ("5".equals(vo.get공제종류코드())) {
                	if (CodeUtils.is국세청(vo.get자료구분코드())) {
                		// 인적공제_국세청_교육비_장애인특수
                		value[2] += 교육비;
                	} else {
                		// 인적공제_기타_교육비_장애인특수
                		value[3] += 교육비;
                	}
                }
            }
        }

        int[] total = new int[]{0, 0, 0, 0};
        int[] value;
        for (String 부양가족ID : sum.keySet()) {
            value = sum.get(부양가족ID);
            total[0] += value[0];
            total[1] += value[1];
            total[2] += value[2];
            total[3] += value[3];

            setPdfField(fieldListVO, "인적공제_국세청_교육비" + family.get(부양가족ID), value[0]);
            setPdfField(fieldListVO, "인적공제_기타_교육비" + family.get(부양가족ID), value[1]);
            
            setPdfField(fieldListVO, "인적공제_국세청_교육비_일반" + family.get(부양가족ID), value[0]);
            setPdfField(fieldListVO, "인적공제_기타_교육비_일반" + family.get(부양가족ID), value[1]);
            setPdfField(fieldListVO, "인적공제_국세청_교육비_장애인특수" + family.get(부양가족ID), value[2]);
            setPdfField(fieldListVO, "인적공제_기타_교육비_장애인특수" + family.get(부양가족ID), value[3]);
        }

        setPdfField(fieldListVO, "인적공제_국세청_교육비합계", total[0]);
        setPdfField(fieldListVO, "인적공제_기타_교육비합계", total[1]);
        
        setPdfField(fieldListVO, "인적공제_국세청_교육비_일반합계", total[0]);
        setPdfField(fieldListVO, "인적공제_기타_교육비_일반합계", total[1]);
        setPdfField(fieldListVO, "인적공제_국세청_교육비_장애인특수합계", total[2]);
        setPdfField(fieldListVO, "인적공제_기타_교육비_장애인특수합계", total[3]);
    }

    private void set근로소득공제신고서_신용카드(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO, Map<String, String> family) {
        YE108VO ye108VO = new YE108VO();
        ye108VO.set계약ID(계약ID);
        ye108VO.set사용자ID(사용자ID);
        List<YE108VO> listYE108 = ye108DAO.getYE108List(ye108VO);

        Map<String, int[]> sum = new HashMap<>();
        if (listYE108 != null && listYE108.size() > 0) {
            int[] value;

            for (YE108VO vo : listYE108) {
                if (sum.containsKey(vo.get부양가족ID())) {
                    value = sum.get(vo.get부양가족ID());
                } else {
                    value = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    sum.put(vo.get부양가족ID(), value);
                    logger.info("부양가족 : " + vo.get부양가족ID());
                }

                if (CodeUtils.is국세청(vo.get자료구분코드())) {
                    // 인적공제_국세청_신용카드
                    value[0] += vo.get신용카드() - vo.get신용카드_차감금액();
                    // 인적공제_국세청_현금영수증
                    value[1] += vo.get현금영수증() - vo.get현금영수증_차감금액();
                    // 인적공제_국세청_직불카드등
                    value[2] += vo.get직불_선불카드() - vo.get직불_선불카드_차감금액();
                    // 인적공제_국세청_도서공연사용분
                    value[3] += vo.get도서공연() - vo.get도서공연_차감금액();
                    // 인적공제_국세청_전통시장사용분
                    value[4] += vo.get전통시장() - vo.get전통시장_차감금액();
                    // 인적공제_국세청_대중교통이용분
                    value[5] += vo.get대중교통() - vo.get대중교통_차감금액();
                } else {
                    // 인적공제_기타_신용카드
                    value[6] += vo.get신용카드() - vo.get신용카드_차감금액();
                    // 인적공제_기타_현금영수증
                    value[7] += vo.get현금영수증() - vo.get현금영수증_차감금액();
                    // 인적공제_기타_직불카드등
                    value[8] += vo.get직불_선불카드() - vo.get직불_선불카드_차감금액();
                    // 인적공제_기타_도서공연사용분
                    value[9] += vo.get도서공연() - vo.get도서공연_차감금액();
                    // 인적공제_기타_전통시장사용분
                    value[10] += vo.get전통시장() - vo.get전통시장_차감금액();
                    // 인적공제_기타_대중교통이용분
                    value[11] += vo.get대중교통() - vo.get대중교통_차감금액();
                }
            }
        }

        int[] total = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] value;
        for (String 부양가족ID : sum.keySet()) {
            value = sum.get(부양가족ID);
            total[0] += value[0];
            total[1] += value[1];
            total[2] += value[2];
            total[3] += value[3];
            total[4] += value[4];
            total[5] += value[5];
            total[6] += value[6];
            total[7] += value[7];
            total[8] += value[8];
            total[9] += value[9];
            total[10] += value[10];
            total[11] += value[11];
            logger.info("부양가족 >>> : " + 부양가족ID);
            logger.info("부양가족 >>> : " + family.get(부양가족ID));
            setPdfField(fieldListVO, "인적공제_국세청_신용카드" + family.get(부양가족ID), value[0]);
            setPdfField(fieldListVO, "인적공제_국세청_현금영수증" + family.get(부양가족ID), value[1]);
            setPdfField(fieldListVO, "인적공제_국세청_직불카드등" + family.get(부양가족ID), value[2]);
            setPdfField(fieldListVO, "인적공제_국세청_도서공연사용분" + family.get(부양가족ID), value[3]);
            setPdfField(fieldListVO, "인적공제_국세청_전통시장사용분" + family.get(부양가족ID), value[4]);
            setPdfField(fieldListVO, "인적공제_국세청_대중교통이용분" + family.get(부양가족ID), value[5]);
            setPdfField(fieldListVO, "인적공제_기타_신용카드" + family.get(부양가족ID), value[6]);
            setPdfField(fieldListVO, "인적공제_기타_현금영수증" + family.get(부양가족ID), value[7]);
            setPdfField(fieldListVO, "인적공제_기타_직불카드등" + family.get(부양가족ID), value[8]);
            setPdfField(fieldListVO, "인적공제_기타_도서공연사용분" + family.get(부양가족ID), value[9]);
            setPdfField(fieldListVO, "인적공제_기타_전통시장사용분" + family.get(부양가족ID), value[10]);
            setPdfField(fieldListVO, "인적공제_기타_대중교통이용분" + family.get(부양가족ID), value[11]);
        }

        setPdfField(fieldListVO, "인적공제_국세청_신용카드합계", total[0]);
        setPdfField(fieldListVO, "인적공제_국세청_현금영수증합계", total[1]);
        setPdfField(fieldListVO, "인적공제_국세청_직불카드등합계", total[2]);
        setPdfField(fieldListVO, "인적공제_국세청_도서공연사용분합계", total[3]);
        setPdfField(fieldListVO, "인적공제_국세청_전통시장사용분합계", total[4]);
        setPdfField(fieldListVO, "인적공제_국세청_대중교통이용분합계", total[5]);
        setPdfField(fieldListVO, "인적공제_기타_신용카드합계", total[6]);
        setPdfField(fieldListVO, "인적공제_기타_현금영수증합계", total[7]);
        setPdfField(fieldListVO, "인적공제_기타_직불카드등합계", total[8]);
        setPdfField(fieldListVO, "인적공제_기타_도서공연사용분합계", total[9]);
        setPdfField(fieldListVO, "인적공제_기타_전통시장사용분합계", total[10]);
        setPdfField(fieldListVO, "인적공제_기타_대중교통이용분합계", total[11]);
    }

    private void set근로소득공제신고서_기부금(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO, Map<String, String> family) {
        YE404VO ye404VO = new YE404VO();
        ye404VO.set계약ID(계약ID);
        ye404VO.set사용자ID(사용자ID);
        ye404VO.setStartPage(0);
        ye404VO.setEndPage(9999);
        List<YE404VO> listYE404 = ye404DAO.getYE404List(ye404VO);

        Map<String, int[]> sum = new HashMap<>();
        if (listYE404 != null && listYE404.size() > 0) {
            setPdfField(fieldListVO, "그밖의추가제출서류_기부금명세서_제출여부", "O");

            int[] value;

            for (YE404VO vo : listYE404) {
                if (sum.containsKey(vo.get부양가족ID())) {
                    value = sum.get(vo.get부양가족ID());
                } else {
                    value = new int[]{0, 0};
                    sum.put(vo.get부양가족ID(), value);
                }

                if (CodeUtils.is국세청(vo.get자료구분코드())) {
                    // 인적공제_국세청_기부금
                    value[0] += vo.get기부명세_공제대상기부금();
                } else {
                    // 인적공제_기타_기부금
                    value[1] += vo.get기부명세_공제대상기부금();
                }
            }
        }

        int[] total = new int[]{0, 0};
        int[] value;
        for (String 부양가족ID : sum.keySet()) {
            value = sum.get(부양가족ID);
            total[0] += value[0];
            total[1] += value[1];

            setPdfField(fieldListVO, "인적공제_국세청_기부금" + family.get(부양가족ID), value[0]);
            setPdfField(fieldListVO, "인적공제_기타_기부금" + family.get(부양가족ID), value[1]);
        }

        setPdfField(fieldListVO, "인적공제_국세청_기부금합계", total[0]);
        setPdfField(fieldListVO, "인적공제_기타_기부금합계", total[1]);
    }

    private void set근로소득공제신고서_회사자료(String 계약ID, String 사용자ID, List<FieldVO> fieldListVO) throws Exception {
        YE000VO ye000 = new YE000VO();
        ye000.set계약ID(계약ID);
        ye000.set사용자ID(사용자ID);
        List<YE000VO> listSum = ye003DAO.getYE003SumList(ye000);

        for (YE000VO vo : listSum) {
            if ("1".equals(vo.get근무지구분())) {
                setPdfField(fieldListVO, "국민연금보험료_주(현)근무지_금액", StringUtil.strPaserLong(vo.get국민연금보험료()));
                setPdfField(fieldListVO, "국민연금보험료_주(현)근무지_공제액", StringUtil.strPaserLong(vo.get국민연금보험료()));

                setPdfField(fieldListVO, "국민연금보험료_주(현)근무지_금액", StringUtil.strPaserLong(vo.get국민연금보험료()));

                setPdfField(
                        fieldListVO,
                        "국민연금보험료외의공적연금보험료_주(현)근무지_금액",
                        StringUtil.strPaserLong(vo.get공무원연금()) +
                                StringUtil.strPaserLong(vo.get군인연금()) +
                                StringUtil.strPaserLong(vo.get사립학교교직원연금()) +
                                StringUtil.strPaserLong(vo.get별정우체국연금())
                );
                setPdfField(
                        fieldListVO,
                        "국민연금보험료외의공적연금보험료_주(현)근무지_공제액",
                        StringUtil.strPaserLong(vo.get공무원연금()) +
                                StringUtil.strPaserLong(vo.get군인연금()) +
                                StringUtil.strPaserLong(vo.get사립학교교직원연금()) +
                                StringUtil.strPaserLong(vo.get별정우체국연금())
                );

                setPdfField(
                        fieldListVO,
                        "국민건강보험료_주(현)근무지_금액",
                        StringUtil.strPaserLong(vo.get건강보험료()) + StringUtil.strPaserLong(vo.get장기요양보험료())
                );
                setPdfField(
                        fieldListVO,
                        "국민건강보험료_주(현)근무지_공제액",
                        StringUtil.strPaserLong(vo.get건강보험료()) + StringUtil.strPaserLong(vo.get장기요양보험료())
                );

                setPdfField(fieldListVO, "고용보험_주(현)근무지_금액", StringUtil.strPaserLong(vo.get고용보험료()));
                setPdfField(fieldListVO, "고용보험_주(현)근무지_공제액", StringUtil.strPaserLong(vo.get고용보험료()));
            } else {
                setPdfField(fieldListVO, "국민연금보험료_종(전)근무지_금액", StringUtil.strPaserLong(vo.get국민연금보험료()));
                setPdfField(fieldListVO, "국민연금보험료_종(전)근무지_공제액", StringUtil.strPaserLong(vo.get국민연금보험료()));

                setPdfField(
                        fieldListVO,
                        "국민연금보험료외의공적연금보험료_종(전)근무지_금액",
                        StringUtil.strPaserLong(vo.get공무원연금()) +
                                StringUtil.strPaserLong(vo.get군인연금()) +
                                StringUtil.strPaserLong(vo.get사립학교교직원연금()) +
                                StringUtil.strPaserLong(vo.get별정우체국연금())
                );
                setPdfField(
                        fieldListVO,
                        "국민연금보험료외의공적연금보험료_종(전)근무지_공제액",
                        StringUtil.strPaserLong(vo.get공무원연금()) +
                                StringUtil.strPaserLong(vo.get군인연금()) +
                                StringUtil.strPaserLong(vo.get사립학교교직원연금()) +
                                StringUtil.strPaserLong(vo.get별정우체국연금())
                );

                setPdfField(
                        fieldListVO,
                        "국민건강보험료_종(전)근무지_금액",
                        StringUtil.strPaserLong(vo.get건강보험료()) + StringUtil.strPaserLong(vo.get장기요양보험료())
                );
                setPdfField(
                        fieldListVO,
                        "국민건강보험료_종(전)근무지_공제액",
                        StringUtil.strPaserLong(vo.get건강보험료()) + StringUtil.strPaserLong(vo.get장기요양보험료())
                );

                setPdfField(fieldListVO, "고용보험_종(전)근무지_금액", StringUtil.strPaserLong(vo.get고용보험료()));
                setPdfField(fieldListVO, "고용보험_종(전)근무지_공제액", StringUtil.strPaserLong(vo.get고용보험료()));
            }
        }
    }


    private void createY03PDF(String bizId, YE750VO ye750VO) throws Exception {
        ContentVO contentVO = new ContentVO();
        contentVO.setFileId(YE750Service.Y03_FILE_ID);

        // 콘텐츠에서 정보 추출
        ContentVO contentResultVO = contentDAO.getContent(contentVO);
        if (contentResultVO == null) {
            System.out.println("[createY03PDF] 콘텐츠 정보가 존재하지 않습니다.");
            return;
        }

        // 저장위치
        CabinetVO cabinetVO = new CabinetVO();
        cabinetVO.setClassId(contentResultVO.getClassId());
        cabinetVO.setCabinetId(contentResultVO.getCabinetId());

        CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
        if (cabinetResultVO == null) {
            System.out.println("[createY03PDF] 캐비넷 정보가 존재하지 않습니다.");
            return;
        }

        String originalPdfPath = cabinetResultVO.getCabinetPath() + contentResultVO.getFilePath() + contentResultVO.getFileName();
        String targetPdfName = StringUtil.getUUID() + ".pdf";
        String targetPdfPath = MultipartFileUtil.getSystemTempPath() + targetPdfName;

        System.out.println(originalPdfPath);
        System.out.println(targetPdfPath);

        // 사용할 PDF복사
        FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
        if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
            System.out.println("[createY03PDF] userId=>" + ye750VO.get사용자ID());

            YE000VO ye000VO = new YE000VO();
            ye000VO.setBizId(bizId);
            ye000VO.set계약ID(ye750VO.get계약ID());
            ye000VO.set사용자ID(ye750VO.get사용자ID());
            YE000VO resultYE000VO = ye000DAO.getYE000(ye000VO);
            
            if (resultYE000VO != null) {
                List<FieldVO> fieldListVO = new ArrayList<>();
                String today = DateUtil.getTimeStamp(3);

                setPdfField(fieldListVO, "성명", resultYE000VO.getEmpName());
                setPdfField(fieldListVO, "주민등록번호", formatIdentity(resultYE000VO.get개인식별번호(), true));
                setPdfField(fieldListVO, "제출자", resultYE000VO.getEmpName());
                setPdfField(fieldListVO, "제출년", today.substring(0, 4));
                setPdfField(fieldListVO, "제출월", today.substring(4, 6));
                setPdfField(fieldListVO, "제출일", today.substring(6, 8));

                YS000VO ys000VO = new YS000VO();
                ys000VO.setBizId(bizId);
                ys000VO.set계약ID(ye750VO.get계약ID());
                List<YS000VO> listYS000 = ys000DAO.getYS000List(ys000VO);
                String 계약년도 = "";
                if (listYS000.size() > 0) {
                    setPdfField(fieldListVO, "년도", listYS000.get(0).getFebYear());
                    계약년도 = listYS000.get(0).getFebYear();
                }

                YE402VO ye402VO = new YE402VO();
                ye402VO.set계약ID(ye750VO.get계약ID());
                ye402VO.set사용자ID(ye750VO.get사용자ID());

                List<YE402VO> listYE402 = ye402DAO.getYE402List(ye402VO);
                int count = 0;
                int sum = 0;
                for (int i = 0; i < 9 && i < listYE402.size(); i++) {
                    YE402VO vo = listYE402.get(i);

                    setPdfField(fieldListVO, "주민등록번호_" + (i + 1) + "_1", formatIdentity(vo.get개인식별번호(), true));
                    setPdfField(fieldListVO, "본인_" + (i + 1), "1".equals(vo.get공제종류코드()) ? "O" : "X");
                    setPdfField(fieldListVO, "사업자등록번호_" + (i + 1) + "_1", formatBusinessNo(vo.get지급처_사업자등록번호()));
                    setPdfField(fieldListVO, "상호_" + (i + 1), vo.get상호());
                    setPdfField(fieldListVO, "의료비증빙코드_" + (i + 1), vo.get의료비증빙코드());
                    setPdfField(fieldListVO, "건수_" + (i + 1), vo.get건수());
                    setPdfField(fieldListVO, "금액_" + (i + 1), vo.get지출액() - vo.get차감금액());
                    setPdfField(fieldListVO, "난임시술비_" + (i + 1), "3".equals(vo.get의료비유형()) ? "O" : "X");

                    count += vo.get건수();
                    sum += vo.get지출액() - vo.get차감금액();
                }

                setPdfField(fieldListVO, "건수_합계", count);
                setPdfField(fieldListVO, "금액_합계", sum);

		    	System.out.println(":::::::::::::::::::: createY03PDF 1 :::::::::::::::::::");
                // PDF영수증 파일명 : 사번_성명_귀속년도_영수증이름
                createPdfFile(
                        fieldListVO,
                        originalPdfPath, resultYE000VO.getEmpNo() + "_" + resultYE000VO.getEmpName() + "_" + 계약년도 + "_의료비지급명세서.pdf",
                        targetPdfPath, targetPdfName,
                        resultYE000VO.getEmpName() + "_의료비지급명세서",
                        ye750VO
                );
            }
        }
    }

    private void createY04PDF(String bizId, YE750VO ye750VO) throws Exception {
        ContentVO contentVO = new ContentVO();
        contentVO.setFileId(YE750Service.Y04_FILE_ID);

        // 콘텐츠에서 정보 추출
        ContentVO contentResultVO = contentDAO.getContent(contentVO);
        if (contentResultVO == null) {
            System.out.println("[createY04PDF] 콘텐츠 정보가 존재하지 않습니다.");
            return;
        }

        // 저장위치
        CabinetVO cabinetVO = new CabinetVO();
        cabinetVO.setClassId(contentResultVO.getClassId());
        cabinetVO.setCabinetId(contentResultVO.getCabinetId());

        CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
        if (cabinetResultVO == null) {
            System.out.println("[createY04PDF] 캐비넷 정보가 존재하지 않습니다.");
            return;
        }

        String originalPdfPath = cabinetResultVO.getCabinetPath() + contentResultVO.getFilePath() + contentResultVO.getFileName();
        String targetPdfName = StringUtil.getUUID() + ".pdf";
        String targetPdfPath = MultipartFileUtil.getSystemTempPath() + targetPdfName;

        System.out.println(originalPdfPath);
        System.out.println(targetPdfPath);

        // 사용할 PDF복사
        FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
        if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) {
            System.out.println("[createY04PDF] userId=>" + ye750VO.get사용자ID());

            YE000VO ye000VO = new YE000VO();
            ye000VO.setBizId(bizId);
            ye000VO.set계약ID(ye750VO.get계약ID());
            ye000VO.set사용자ID(ye750VO.get사용자ID());
            YE000VO resultYE000VO = ye000DAO.getYE000(ye000VO);

            if (resultYE000VO != null) {
                List<FieldVO> fieldListVO = new ArrayList<>();
                String today = DateUtil.getTimeStamp(3);

                setPdfField(fieldListVO, "성명", resultYE000VO.getEmpName());
                setPdfField(fieldListVO, "주민등록번호", formatIdentity(resultYE000VO.get개인식별번호(), true));
                setPdfField(fieldListVO, "주소", resultYE000VO.getAddress1() + resultYE000VO.getAddress2());
                setPdfField(fieldListVO, "주소_전화번호", resultYE000VO.getPhoneNum());

                YS030VO ys030VO = new YS030VO();
                ys030VO.set계약ID(ye750VO.get계약ID());
                ys030VO.set사업장ID(resultYE000VO.get사업장ID());
                ys030VO.setStartPage(0);
                ys030VO.setEndPage(9999);
                List<YS030VO> listYS030 = ys030DAO.getYS030List(ys030VO);
                if (listYS030.size() > 0) {
                    setPdfField(fieldListVO, "근무지", listYS030.get(0).get사업장명());
                    setPdfField(fieldListVO, "사업자등록번호", formatBusinessNo(listYS030.get(0).get사업자등록번호()));
                    setPdfField(fieldListVO, "사업장_소재지", listYS030.get(0).get회사주소1());
                    setPdfField(fieldListVO, "사업장_전화번호", listYS030.get(0).get회사전화1());
                }

                YE404VO ye404VO = new YE404VO();
                ye404VO.set계약ID(ye750VO.get계약ID());
                ye404VO.set사용자ID(ye750VO.get사용자ID());
                ye404VO.setStartPage(0);
                ye404VO.setEndPage(9999);
                ye404VO.setSortName("자료구분코드, 기부코드");
                List<YE404VO> listYE404 = ye404DAO.getYE404List(ye404VO);

                YE408VO ye408VO = new YE408VO();
                ye408VO.set계약ID(ye750VO.get계약ID());
                ye408VO.set사용자ID(ye750VO.get사용자ID());
                ye408VO.setStartPage(0);
                ye408VO.setEndPage(9999);
                ye408VO.setSortName("기부년도 DESC, 기부금종류코드");
                List<YE408VO> listYE408 = ye408DAO.getYE408List(ye408VO);

                // PDF영수증 계약년도 표시
                YS000VO ys000VO = new YS000VO();
                ys000VO.setBizId(bizId);
                ys000VO.set계약ID(ye750VO.get계약ID());
                List<YS000VO> listYS000 = ys000DAO.getYS000List(ys000VO);
                String 계약년도 = "";
                if (listYS000.size() > 0) {
                    계약년도 = listYS000.get(0).getFebYear();
                }
                
                if (listYE404.size() > 0) {
                    DonationSum 기부금합계_합계 = new DonationSum();
                    DonationSum 기부금합계_본인 = new DonationSum();
                    DonationSum 기부금합계_배우자 = new DonationSum();
                    DonationSum 기부금합계_직계비속 = new DonationSum();
                    DonationSum 기부금합계_직계존속 = new DonationSum();
                    DonationSum 기부금합계_형제자매 = new DonationSum();
                    DonationSum 기부금합계_그외 = new DonationSum();

                    for (int i = 0; i < 7 && i < listYE404.size(); i++) {
                        YE404VO vo = listYE404.get(i);

                        if(StringUtils.isNotEmpty(vo.get기부코드())){
	                        switch (vo.get기부코드()) {
	                            case "10":
	                                setPdfField(fieldListVO, "유형_" + (i + 1), "법정");
	                                break;
	                            case "20":
	                                setPdfField(fieldListVO, "유형_" + (i + 1), "정치자금");
	                                break;
	                            case "40":
	                                setPdfField(fieldListVO, "유형_" + (i + 1), "지정");
	                                break;
	                            case "41":
	                                setPdfField(fieldListVO, "유형_" + (i + 1), "종교단체");
	                                break;
	                            case "42":
	                                setPdfField(fieldListVO, "유형_" + (i + 1), "우리사주");
	                                break;
	                            default:
	                                setPdfField(fieldListVO, "유형_" + (i + 1), "기타");
	                                break;
	                        }
	                        setPdfField(fieldListVO, "코드_" + (i + 1), vo.get기부코드());
	                        
                        }else{
                        	logger.error("##### 기부코드 정보가 없습니다. #####");
                        	logger.error("# YE404VO => " + vo );
                        }
                        
                        
                        if(StringUtils.isNotEmpty(vo.get기부내용())){
	                        switch (vo.get기부내용()) {
	                            case "1":
	                                setPdfField(fieldListVO, "기부내용_" + (i + 1), "금전");
	                                break;
	                            case "2":
	                                setPdfField(fieldListVO, "기부내용_" + (i + 1), "현물");
	                                break;
	                            default:
	                                setPdfField(fieldListVO, "기부내용_" + (i + 1), "기타");
	                                break;
	                        }
                        }else{
	                       	logger.error("##### 기부내용 정보가 없습니다. #####");
	                       	logger.error("# YE404VO => " + vo );
	                    }    

                        setPdfField(fieldListVO, "상호_" + (i + 1), vo.get상호());
                        setPdfField(fieldListVO, "사업자등록번호_" + (i + 1), formatBusinessNo(vo.get기부처_사업자등록번호()));

                        setPdfField(fieldListVO, "성명_" + (i + 1), vo.get성명());
                        setPdfField(fieldListVO, "주민등록번호_" + (i + 1), formatIdentity(vo.get개인식별번호(), true));

                        setPdfField(fieldListVO, "건수_" + (i + 1), vo.get기부명세_건수());
                        setPdfField(fieldListVO, "합계_" + (i + 1), vo.get기부명세_공제금액());
                        setPdfField(fieldListVO, "공제대상기부금액_" + (i + 1), vo.get기부명세_공제대상기부금());
                        setPdfField(fieldListVO, "기부장려금신청금액_" + (i + 1), vo.get기부명세_기부장려금());
                        setPdfField(fieldListVO, "기타_" + (i + 1), vo.get기부명세_기타());

                        switch (vo.get가족관계()) {
                            case "0":
                                setPdfField(fieldListVO, "관계코드_" + (i + 1), "1");
                                sum기부금(기부금합계_합계, 기부금합계_본인, vo);
                                break;

                            case "3":
                                setPdfField(fieldListVO, "관계코드_" + (i + 1), "2");
                                sum기부금(기부금합계_합계, 기부금합계_배우자, vo);
                                break;

                            case "4":
                            case "5":
                                setPdfField(fieldListVO, "관계코드_" + (i + 1), "3");
                                sum기부금(기부금합계_합계, 기부금합계_직계비속, vo);
                                break;

                            case "1":
                            case "2":
                                setPdfField(fieldListVO, "관계코드_" + (i + 1), "4");
                                sum기부금(기부금합계_합계, 기부금합계_직계존속, vo);
                                break;

                            case "6":
                                setPdfField(fieldListVO, "관계코드_" + (i + 1), "5");
                                sum기부금(기부금합계_합계, 기부금합계_형제자매, vo);
                                break;

                            case "7":
                            case "8":
                                setPdfField(fieldListVO, "관계코드_" + (i + 1), "6");
                                sum기부금(기부금합계_합계, 기부금합계_그외, vo);
                                break;
                        }
                    }

                    setPdfField(fieldListVO, "구분코드별_총계_1", 기부금합계_합계.총계);
                    setPdfField(fieldListVO, "구분코드별_법정기부금_1", 기부금합계_합계.법정기부금);
                    setPdfField(fieldListVO, "구분코드별_정치자금기부금_1", 기부금합계_합계.정치자금기부금);
                    setPdfField(fieldListVO, "구분코드별_종교단체외지정기부금_1", 기부금합계_합계.종교단체외지정기부금);
                    setPdfField(fieldListVO, "구분코드별_종교단체지정기부금_1", 기부금합계_합계.종교단체지정기부금);
                    setPdfField(fieldListVO, "구분코드별_우리사주조합기부금_1", 기부금합계_합계.우리사주조합기부금);
                    setPdfField(fieldListVO, "구분코드별_기부장려금신청금액_1", 기부금합계_합계.기부장려금신청금액);
                    setPdfField(fieldListVO, "구분코드별_기타_1", 기부금합계_합계.기타);

                    setPdfField(fieldListVO, "구분코드별_총계_2", 기부금합계_본인.총계);
                    setPdfField(fieldListVO, "구분코드별_법정기부금_2", 기부금합계_본인.법정기부금);
                    setPdfField(fieldListVO, "구분코드별_정치자금기부금_2", 기부금합계_본인.정치자금기부금);
                    setPdfField(fieldListVO, "구분코드별_종교단체외지정기부금_2", 기부금합계_본인.종교단체외지정기부금);
                    setPdfField(fieldListVO, "구분코드별_종교단체지정기부금_2", 기부금합계_본인.종교단체지정기부금);
                    setPdfField(fieldListVO, "구분코드별_우리사주조합기부금_2", 기부금합계_본인.우리사주조합기부금);
                    setPdfField(fieldListVO, "구분코드별_기부장려금신청금액_2", 기부금합계_본인.기부장려금신청금액);
                    setPdfField(fieldListVO, "구분코드별_기타_2", 기부금합계_본인.기타);

                    setPdfField(fieldListVO, "구분코드별_총계_3", 기부금합계_배우자.총계);
                    setPdfField(fieldListVO, "구분코드별_법정기부금_3", 기부금합계_배우자.법정기부금);
                    setPdfField(fieldListVO, "구분코드별_종교단체외지정기부금_3", 기부금합계_배우자.종교단체외지정기부금);
                    setPdfField(fieldListVO, "구분코드별_종교단체지정기부금_3", 기부금합계_배우자.종교단체지정기부금);
                    setPdfField(fieldListVO, "구분코드별_기부장려금신청금액_3", 기부금합계_배우자.기부장려금신청금액);
                    setPdfField(fieldListVO, "구분코드별_기타_3", 기부금합계_배우자.기타);

                    setPdfField(fieldListVO, "구분코드별_총계_4", 기부금합계_직계비속.총계);
                    setPdfField(fieldListVO, "구분코드별_법정기부금_4", 기부금합계_직계비속.법정기부금);
                    setPdfField(fieldListVO, "구분코드별_종교단체외지정기부금_4", 기부금합계_직계비속.종교단체외지정기부금);
                    setPdfField(fieldListVO, "구분코드별_종교단체지정기부금_4", 기부금합계_직계비속.종교단체지정기부금);
                    setPdfField(fieldListVO, "구분코드별_기부장려금신청금액_4", 기부금합계_직계비속.기부장려금신청금액);
                    setPdfField(fieldListVO, "구분코드별_기타_4", 기부금합계_직계비속.기타);

                    setPdfField(fieldListVO, "구분코드별_총계_5", 기부금합계_직계존속.총계);
                    setPdfField(fieldListVO, "구분코드별_법정기부금_5", 기부금합계_직계존속.법정기부금);
                    setPdfField(fieldListVO, "구분코드별_종교단체외지정기부금_5", 기부금합계_직계존속.종교단체외지정기부금);
                    setPdfField(fieldListVO, "구분코드별_종교단체지정기부금_5", 기부금합계_직계존속.종교단체지정기부금);
                    setPdfField(fieldListVO, "구분코드별_기부장려금신청금액_5", 기부금합계_직계존속.기부장려금신청금액);
                    setPdfField(fieldListVO, "구분코드별_기타_5", 기부금합계_직계존속.기타);

                    setPdfField(fieldListVO, "구분코드별_총계_6", 기부금합계_형제자매.총계);
                    setPdfField(fieldListVO, "구분코드별_법정기부금_6", 기부금합계_형제자매.법정기부금);
                    setPdfField(fieldListVO, "구분코드별_종교단체외지정기부금_6", 기부금합계_형제자매.종교단체외지정기부금);
                    setPdfField(fieldListVO, "구분코드별_종교단체지정기부금_6", 기부금합계_형제자매.종교단체지정기부금);
                    setPdfField(fieldListVO, "구분코드별_기부장려금신청금액_6", 기부금합계_형제자매.기부장려금신청금액);
                    setPdfField(fieldListVO, "구분코드별_기타_6", 기부금합계_형제자매.기타);

                    setPdfField(fieldListVO, "구분코드별_총계_7", 기부금합계_그외.총계);
                    setPdfField(fieldListVO, "구분코드별_법정기부금_7", 기부금합계_그외.법정기부금);
                    setPdfField(fieldListVO, "구분코드별_종교단체외지정기부금_7", 기부금합계_그외.종교단체외지정기부금);
                    setPdfField(fieldListVO, "구분코드별_종교단체지정기부금_7", 기부금합계_그외.종교단체지정기부금);
                    setPdfField(fieldListVO, "구분코드별_기부장려금신청금액_7", 기부금합계_그외.기부장려금신청금액);
                    setPdfField(fieldListVO, "구분코드별_기타_7", 기부금합계_그외.기타);
                }

                if (listYE408.size() > 0) {
                    for (int i = 0; i < 7 && i < listYE408.size(); i++) {
                        YE408VO vo = listYE408.get(i);
                        setPdfField(fieldListVO, "조정명세_기부금코드_" + (i + 1), vo.get기부금종류코드());
                        setPdfField(fieldListVO, "조정명세_기부연도_" + (i + 1), vo.get기부년도());
                        setPdfField(fieldListVO, "조정명세_기부금액_" + (i + 1), StringUtil.strPaserLong(vo.get기부금액()));
                        setPdfField(fieldListVO, "조정명세_전년까지공제된금액_" + (i + 1), vo.get전년도까지공제금액());
                        setPdfField(fieldListVO, "조정명세_공제대상금액_" + (i + 1), vo.get공제대상기부금());
                        setPdfField(fieldListVO, "조정명세_세액공제_" + (i + 1), vo.get해당연도공제금액());
                        setPdfField(fieldListVO, "조정명세_소멸금액_" + (i + 1), vo.get소멸금액());
                        setPdfField(fieldListVO, "조정명세_이월금액_" + (i + 1), vo.get이월금액());
                    }
                }

		    	System.out.println(":::::::::::::::::::: createY04PDF 1 :::::::::::::::::::");
		    	
                // PDF영수증 파일명 : 사번_성명_귀속년도_영수증이름
                createPdfFile(
                        fieldListVO,
                        originalPdfPath, resultYE000VO.getEmpNo() + "_" + resultYE000VO.getEmpName() + "_" + 계약년도 + "_기부금명세서.pdf",
                        targetPdfPath, targetPdfName,
                        resultYE000VO.getEmpName() + "_기부금명세서",
                        ye750VO
                );
            }
        }
    }

    private void sum기부금(DonationSum 기부금합계_합계, DonationSum 기부금합계_부양가족별, YE404VO vo) {
        if ("10".equals(vo.get기부코드())) {
            기부금합계_부양가족별.법정기부금 += vo.get기부명세_공제대상기부금();
            기부금합계_합계.법정기부금 += vo.get기부명세_공제대상기부금();
        } else if ("20".equals(vo.get기부코드())) {
            기부금합계_부양가족별.정치자금기부금 += vo.get기부명세_공제대상기부금();
            기부금합계_합계.정치자금기부금 += vo.get기부명세_공제대상기부금();
        } else if ("40".equals(vo.get기부코드())) {
            기부금합계_부양가족별.종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
            기부금합계_합계.종교단체외지정기부금 += vo.get기부명세_공제대상기부금();
        } else if ("41".equals(vo.get기부코드())) {
            기부금합계_부양가족별.종교단체지정기부금 += vo.get기부명세_공제대상기부금();
            기부금합계_합계.종교단체지정기부금 += vo.get기부명세_공제대상기부금();
        } else if ("42".equals(vo.get기부코드())) {
            기부금합계_부양가족별.우리사주조합기부금 += vo.get기부명세_공제대상기부금();
            기부금합계_합계.우리사주조합기부금 += vo.get기부명세_공제대상기부금();
        }

        기부금합계_부양가족별.총계 += vo.get기부명세_공제금액();
        기부금합계_부양가족별.기부장려금신청금액 += vo.get기부명세_기부장려금();
        기부금합계_부양가족별.기타 += vo.get기부명세_기타();

        기부금합계_합계.총계 += vo.get기부명세_공제금액();
        기부금합계_합계.기부장려금신청금액 += vo.get기부명세_기부장려금();
        기부금합계_합계.기타 += vo.get기부명세_기타();
    }

    private void setPdfField(List<FieldVO> fieldListVO, String fieldKey, String filedValue) {
        fieldListVO.add(FieldUtil.setFieldData(fieldKey, filedValue));
    }

    private void setPdfField(List<FieldVO> fieldListVO, String fieldKey, long filedValue) {
        if (filedValue > 0) {
            fieldListVO.add(FieldUtil.setFieldData(fieldKey, df.format(filedValue)));
        }
    }

    private String formatIdentity(String identity, boolean security) throws Exception {
        if (identity == null) {
            return "";
        }

        if (security) {
            identity = SecurityUtil.getinstance().aesDecode(identity);
        }

        if (identity.length() == 13) {
            identity = identity.substring(0, 6) + "-" + identity.substring(6);
        }

        return identity;
    }

    private String formatBusinessNo(String businessNo) {
        if (businessNo == null) {
            return "";
        }

        if (businessNo.length() == 10) {
            businessNo = businessNo.substring(0, 3) + "-" + businessNo.substring(3, 5) + "-" + businessNo.substring(5);
        }

        return businessNo;
    }

    private void createPdfFile(
            List<FieldVO> fieldListVO,
            String originalPdfPath, String originalPdfName,
            String targetPdfPath, String targetPdfName,
            String pdfTitle,
            YE750VO ye750VO) throws Exception {
    	
        //파일생성
        boolean bWritePDF = ControlUtil.writePDF(fieldListVO, originalPdfPath, targetPdfPath, false, true, System.getProperty("pdf.font.default"));

        //콘텐츠등록
        if (bWritePDF) {
            ContentVO contentVO = new ContentVO();
            contentVO.setFileType(FileUtil.getFileExt(targetPdfPath));
            contentVO.setFileName(targetPdfName);
            contentVO.setOrgFileName(originalPdfName);
            contentVO.setFileSize(Long.toString(FileUtil.getFileSize(targetPdfPath)));
            contentVO.setFilePath(MultipartFileUtil.getSystemTempPath());
            contentVO.setFileTitle(pdfTitle);
            contentVO.setClassId(YE750Service.SAVE_CLASS_ID);
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
            if (contentService.COMPLETED == nResult) {
                ye750VO.set파일ID(contentVO.getFileId());
                ye750DAO.insYE750(ye750VO);
                FileUtil.deleteFile(targetPdfPath);
            } else {
                System.out.println("transContent Error Code : " + nResult);
            }
        }
    }


    private static class DonationSum {
        long 총계 = 0;
        long 법정기부금 = 0;
        long 정치자금기부금 = 0;
        long 종교단체외지정기부금 = 0;
        long 종교단체지정기부금 = 0;
        long 우리사주조합기부금 = 0;
        long 기부장려금신청금액 = 0;
        long 기타 = 0;
    }


	@Override
	public DefaultResponse createYE750ReceiptPdf(YE000VO vo) throws Exception {
		
		DefaultResponse resultResponse = new DefaultResponse();
		int cnt = 0;
		
		List<YE000VO> ye000List = ye000DAO.getYE000List(vo);
		
		if (ye000List != null && ye000List.size() != 0) {
			for (int i=0; i < ye000List.size(); i++) {
				YE000VO selYE000VO = ye000List.get(i);
				

				YW800Response response = new YW800Response();
				
				YE000VO work = new YE000VO();
		        work.set계약ID(selYE000VO.get계약ID());
		        work.set사용자ID(selYE000VO.get사용자ID());
		        response.근무년월 = new WorkMonth(febMasterService.getUserWorkMonth(work));

		        // PDF영수증 생성할 연말정산요약 조회
				yw800Service.getYW800(selYE000VO.getBizId(), selYE000VO.get계약ID(), selYE000VO.get사용자ID(), response);

				// PDF영수증 생성
				ye750Service.createYE750Pdf(selYE000VO.getBizId(), selYE000VO.get계약ID(), selYE000VO.get사용자ID(), response);

				cnt++;
					
			}
			
			// PDF영수증 생성된 연말정산 대상자 수
			resultResponse.cnt = cnt;
				
		}
		
		return resultResponse;
		
	}
	
	
	/**
	 * 
	 * 연말정산 영수증 ZIP 파일 생성
	 *
	 * @param list
	 * @param loginVO
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> makeYE750ZipDocument(String 계약ID, String 파일ID, SessionVO loginVO) throws Exception{
		
		Map<String,String> resultMap = new HashMap<String,String>();
		
		List<String> documentList = new ArrayList<String>();
		
		String toDate = DateUtil.getDate();								    //오늘날짜
		String systemPath = System.getProperty("system.feb.path")+File.separator+"temp"+File.separator;			//시스템 파일저장 경로
		
		String bizName = loginVO.getBizName();

		try{
			
			if(StringUtil.isNull(계약ID)){
				throw new AngullarProcessException("연말정산 영수증 계약 아이디 정보가 없습니다.");
			}
			

			logger.info("# makeYE750ZipDocument -> 계약ID : " + 계약ID );
			logger.info("# makeYE750ZipDocument -> bizName : " + bizName );

			//파일 ID의 파일구분정보를 조회한다.
			YE750VO temp750VO = null;
			if(StringUtils.isNotEmpty(파일ID)){
				temp750VO = new YE750VO();
				temp750VO.set계약ID(계약ID);
				temp750VO.set파일ID(파일ID);
								
				temp750VO = ye750DAO.getYE750FileInfo(temp750VO);
			}
			
			YE750VO ye750VO = new YE750VO();
			if(temp750VO == null){				
				ye750VO.set계약ID(계약ID);
			}else{
				ye750VO.set계약ID(계약ID);
				ye750VO.set출력물파일구분코드(temp750VO.get출력물파일구분코드());
			}			
			
			//연말정산 영수증  파일 정보 조회 
			List<YE750VO> list = ye750DAO.getYE750List(ye750VO);
			
			for(YE750VO paramVO : list){
				
				logger.debug("# paramVO.get파일ID() : " + paramVO.get파일ID() );
								
				ContentVO contentVO = new ContentVO();
				contentVO.setFileId(paramVO.get파일ID());
				// 콘텐츠에서 정보 추출	
				ContentVO resultContentVO = contentDAO.getContent(contentVO);
				
				if(resultContentVO == null) {				
					logger.error("# [makeYE750ZipDocument => 파일ID : " +paramVO.get파일ID()+"] 콘텐츠 정보가 존재하지 않습니다. #");				
					throw new AngullarProcessException("[makeYE750ZipDocument => 파일ID : " +paramVO.get파일ID()+"] 콘텐츠 정보가 존재하지 않습니다.");				
				}else{
					// 저장위치
					CabinetVO cabinetVO = new CabinetVO();
					cabinetVO.setClassId(resultContentVO.getClassId());
					cabinetVO.setCabinetId(resultContentVO.getCabinetId());
					CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
					
					if(cabinetResultVO == null) {					
						logger.error("# [makeYE750ZipDocument => CabinetId : " +resultContentVO.getCabinetId()+"] 캐비넷 정보가 존재하지 않습니다. #");					
						throw new AngullarProcessException("[makeYE750ZipDocument => CabinetId : " +resultContentVO.getCabinetId()+"] 캐비넷 정보가 존재하지 않습니다.");					
					}else{
						
						String originalFilePath = cabinetResultVO.getCabinetPath()+resultContentVO.getFilePath()+resultContentVO.getFileName();
						
						logger.debug("# originalFilePath : " + originalFilePath );
						
						if(StringUtils.isNotEmpty(originalFilePath)){
							File file = new File(originalFilePath);
							
							if(file.exists()){							
								//String fileExt = originalFilePath.substring(originalFilePath.lastIndexOf(".")+1,originalFilePath.length());
								
								String tempSavePath = systemPath + "FEB" + File.separator + "YE750" + File.separator + "TEMP" + File.separator + toDate + File.separator;
								String tempSaveFileName = resultContentVO.getOrgFileName(); 
								
								//디렉토리 생성
								FileUtil.createDirectory(tempSavePath);
								
								
								if(FileUtil.fileCopy(originalFilePath, tempSavePath+tempSaveFileName)) {
									documentList.add(tempSavePath+tempSaveFileName);
								}							
								
							}else{
								logger.error("# [makeYE750ZipDocument => " + paramVO.get출력물파일구분명() + " ["+originalFilePath+"] 파일이 없습니다. #");		
								throw new AngullarProcessException(paramVO.get출력물파일구분명() + " 파일이 없습니다.");
							}													
						}
	
					}
					
				}
				
			}
			
			String zipFileName = "";		//zip 파일명		
			String zipFilePath = "";  //zip 파일생성경로
			
			if(documentList != null && documentList.size() > 0){
				
				zipFileName = bizName+ "_" + ye750VO.get계약ID() + "_" + System.currentTimeMillis() + ".zip";		//zip 파일명		
				zipFilePath = systemPath + "FEB"+ File.separator +"YE750" + File.separator + "ZIP" + File.separator + toDate + File.separator;  //zip 파일생성경로			
				zipFilePath = zipFilePath.replaceAll("\\\\", "/");
				
				logger.debug("# zipFileName : " + zipFileName);
				logger.debug("# zipFilePath : " + zipFilePath);
				
				File filePath = new File(zipFilePath);		
				if(!filePath.exists()){
					filePath.mkdirs();
				}
				
				
				int result = FileUtil.ZipFile(zipFilePath + zipFileName, documentList);
				
				//Temp 파일 삭제
				if(result > 0){					
					for(String tempPath : documentList){
						File tempFile = new File(tempPath);
						
						if(tempFile.exists() && tempFile.isFile()){
							tempFile.delete();
						}
					}
				}
				
			}
			
			resultMap.put("zipFilePath", zipFilePath + zipFileName);
			resultMap.put("출력물파일구분명", temp750VO.get출력물파일구분명());
			resultMap.put("success", "true");
			
		}catch(AngullarProcessException ex){			
			resultMap.put("success", "false");
			resultMap.put("message", ex.getMessage());
		}
		
		return resultMap;
	}
	
	
	//첫로딩시 첫 row 사용자 아이디 조회 
	public YE750VO getYE750UserId(YE750VO ye750VO) throws Exception{		
		return ye750DAO.getYE750UserId(ye750VO);
	}
	
	
	/**
	 * 연말정산 영수증 메일발송
	 *
	 * @param 계약ID
	 * @param 파일ID
	 * @param loginVO
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> sendMailYE750(String 계약ID, String 파일ID, SessionVO loginVO) throws Exception{
		
		Map<String,String> resultMap = new HashMap<String,String>();
		
		List<Map<String,String>> documentList = new ArrayList<Map<String,String>>();
		
		String toDate = DateUtil.getDate();								    //오늘날짜
		String systemPath = System.getProperty("system.feb.path")+File.separator+"temp"+File.separator;			//시스템 파일저장 경로
		
		String bizName = loginVO.getBizName();
		String febYear = loginVO.getFebYear();
		
		try{
			
			if(StringUtil.isNull(계약ID)){
				throw new AngullarProcessException("연말정산 영수증 계약 아이디 정보가 없습니다.");
			}
			
			logger.info("# sendMailYE750 -> 계약ID : " + 계약ID );
			logger.info("# sendMailYE750 -> bizName : " + bizName );
			
			//파일 ID의 파일구분정보를 조회한다.
			YE750VO temp750VO = null;
			if(StringUtils.isNotEmpty(파일ID)){
				temp750VO = new YE750VO();
				temp750VO.set계약ID(계약ID);
				temp750VO.set파일ID(파일ID);
								
				temp750VO = ye750DAO.getYE750FileInfo(temp750VO);
			}
			
			YE750VO ye750VO = new YE750VO();
			ye750VO.set계약ID(계약ID);
			ye750VO.setBizId(loginVO.getBizId());
			
			if(temp750VO != null){				
				ye750VO.set출력물파일구분코드(temp750VO.get출력물파일구분코드());
			}		
			
			//연말정산 영수증  파일 정보 조회 
			List<YE750VO> list = ye750DAO.getYE750UserList(ye750VO);
			
			for(YE750VO paramVO : list){
				
				logger.debug("# paramVO.get파일ID() : " + paramVO.get파일ID() );
				
				ContentVO contentVO = new ContentVO();
				contentVO.setFileId(paramVO.get파일ID());
				// 콘텐츠에서 정보 추출	
				ContentVO resultContentVO = contentDAO.getContent(contentVO);
				
				if(resultContentVO == null) {				
					logger.error("# [sendMailYE750 => 파일ID : " +paramVO.get파일ID()+"] 콘텐츠 정보가 존재하지 않습니다. #");				
					throw new AngullarProcessException("[sendMailYE750 => 파일ID : " +paramVO.get파일ID()+"] 콘텐츠 정보가 존재하지 않습니다.");				
				}else{
					// 저장위치
					CabinetVO cabinetVO = new CabinetVO();
					cabinetVO.setClassId(resultContentVO.getClassId());
					cabinetVO.setCabinetId(resultContentVO.getCabinetId());
					CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
					
					if(cabinetResultVO == null) {					
						logger.error("# [sendMailYE750 => CabinetId : " +resultContentVO.getCabinetId()+"] 캐비넷 정보가 존재하지 않습니다. #");					
						throw new AngullarProcessException("[sendMailYE750 => CabinetId : " +resultContentVO.getCabinetId()+"] 캐비넷 정보가 존재하지 않습니다.");					
					}else{
						
						String originalFilePath = cabinetResultVO.getCabinetPath()+resultContentVO.getFilePath()+resultContentVO.getFileName();
						
						logger.debug("# originalFilePath : " + originalFilePath );
						
						if(StringUtils.isNotEmpty(originalFilePath)){
							File file = new File(originalFilePath);
							
							if(file.exists()){							
								//String fileExt = originalFilePath.substring(originalFilePath.lastIndexOf(".")+1,originalFilePath.length());
								
								String tempSavePath = systemPath + "FEB" + File.separator + "YE750" + File.separator + "TEMP" + File.separator + toDate + File.separator;
								String tempSaveFileName = resultContentVO.getOrgFileName(); 
								
								//디렉토리 생성
								FileUtil.createDirectory(tempSavePath);
																
								if(FileUtil.fileCopy(originalFilePath, tempSavePath+tempSaveFileName)) {
									
									Map<String,String> fileInfo = new HashMap<String,String>();
									fileInfo.put("filePath", tempSavePath+tempSaveFileName);
									fileInfo.put("출력물파일구분명", paramVO.get출력물파일구분명());
									fileInfo.put("empName", paramVO.getEmpName());
									fileInfo.put("eMail", paramVO.getEMail());
									
									documentList.add(fileInfo);
								}						
								
							}else{
								logger.error("# [sendMailYE750 => "+paramVO.getEmpName() + " 님의 " + paramVO.get출력물파일구분명() + " ["+originalFilePath+"] 파일이 없습니다. #");		
								throw new AngullarProcessException(paramVO.getEmpName() + " 님의 " + paramVO.get출력물파일구분명() + " 파일이 없습니다.");
							}	
							
						}
	
					}
					
				}
				
			}
			
			
			//메일 전송
			if(documentList != null && documentList.size() > 0){
				
				for(Map<String,String> sendMap : documentList){
			
					File sendFile = new File(sendMap.get("filePath"));
					
					if(sendFile != null && sendFile.exists() && sendFile.isFile()){
					
						if(StringUtil.isNotNull(sendMap.get("eMail"))) {
							//이메일 전송
							String content = "";
							
							content +="<!DOCTYPE html> ";
							content +="<html> ";
							content +="<head> ";
							content +="<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
							content +="<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
							content +="<title>"+sendMap.get("출력물파일구분명")+"</title> ";
							content +="</head> ";
							content +="<body> ";
							content +="	<div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
							content +="		<div style=\" padding: 37px 0 0 0;\"> ";
							content +="			<div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
							content +="				<span style=\"font-weight: bold; color:#00a9e9;\">IEUM</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;SIGN</span> ";
							content +="			</div> ";
							content +="			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
							content +="				<span class=\"font_black\" style=\"color:#202020;\">" + bizName + " * 연말정산</span> ";
							content +="				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">"+sendMap.get("출력물파일구분명")+"</span><span class=\"font_black\" style=\"color:#202020;\">에 대한<br> ";
							content +="				발송 이메일입니다.</span> ";
							content +="			</div> ";
							content +="			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
							content +="				<span >"+sendMap.get("empName")+"</span><span>님 안녕하세요.<br> ";
							content +="				회원님의 " + febYear + "귀속 연말정산 "+sendMap.get("출력물파일구분명")+"을 발송해드립니다.<br> ";
							content +="				<br> ";
							content +="				해당 메일은 개인정보가 포함되어 있으니 보안해 유의해 주시기 바랍니다.<br> ";
							content +="				살펴보신 후 문의사항 있으시면 관리자에게 말씀 주시기 바랍니다<br> ";
							content +="				<br> ";
							content +="				감사합니다.<br> ";
							content +="				</span> ";
							content +="			</div> ";
							content +="		</div> ";
							content +="			<div style=\"margin-top: 74px; text-align: center;\"> ";
							content +="				&nbsp; ";
							content +="			</div> ";
							content +="		<div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
							content +="			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
							content +="				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
							content +="				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:master@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">master@newzenpnp.com</a></span > ";
							content +="				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
							content +="			</div> ";
							content +="		</div> ";
							content +="	</div> ";
							content +="</body> ";
							content +="</html> ";

							List<String> fileList = new ArrayList<String>();
							fileList.add(sendMap.get("filePath"));
							
							MultiPartEmail email = new MultiPartEmail();
							MailVO mailVO = new MailVO();
							mailVO.setFrom("no_reply@newzenpnp.com");
							mailVO.setTo(sendMap.get("eMail"));
							mailVO.setCc("");
							mailVO.setBcc("");
							mailVO.setSubject("["+bizName+" - 연말정산] "+sendMap.get("empName")+"님의 "+sendMap.get("출력물파일구분명")+" 파일이 도착하였습니다.");
							mailVO.setText(content);												
							mailVO.setFileList(fileList);
							
							logger.info("IeumSign "+sendMap.get("출력물파일구분명")+" 이메일을 발송. ["+sendMap.get("empName")+"] / email : " + sendMap.get("eMail"));
							
							email.send(mailVO);
								
						} else {
							logger.error("[sendMailYE750] "+sendMap.get("empName")+" 님의 이메일정보 ["+sendMap.get("eMail")+"] 가 없습니다.");
							throw new AngullarProcessException("[sendMailYE750 => "+sendMap.get("empName")+" 님의 이메일정보 ["+sendMap.get("eMail")+"] 가 없습니다.");
						}
				
					}else{
						logger.error("[sendMailYE750] "+sendMap.get("empName")+" 님의 파일 ["+sendMap.get("filePath")+"] 이 없습니다.");
						throw new AngullarProcessException("[sendMailYE750 => "+sendMap.get("empName")+" 님의 파일정보가 없습니다.");
					}
					
				}
				
				resultMap.put("success", "true");
				resultMap.put("message", documentList.size() + " 건의 메일이 발송되었습니다.");
				
				//메일전송후 - Temp 파일 삭제							
				for(int i = 0 ; i < documentList.size() ; i++){					
					if(StringUtils.isNotEmpty(documentList.get(i).get("filePath"))){					
						File tempFile = new File(documentList.get(i).get("filePath"));
						
						if(tempFile.exists() && tempFile.isFile()){
							tempFile.delete();
						}						
					}
				}
				
				
			}else{
				resultMap.put("success", "false");
				resultMap.put("message", "메일을 발송할 파일정보가 없습니다.");				
			}
			
		}catch(AngullarProcessException ex){			
			resultMap.put("success", "false");
			resultMap.put("message", ex.getMessage());
		}
		
		return resultMap;
	}
	
}
