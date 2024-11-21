package com.ezsign.feb.master.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.code.vo.CodeVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.feb.house.dao.YE108DAO;
import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.master.dao.ExcelMappingDAO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.service.ExcelService;
import com.ezsign.feb.master.vo.ExcelDataRequest;
import com.ezsign.feb.master.vo.ExcelMappingVO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YP000VO;
import com.ezsign.feb.special.dao.YE402DAO;
import com.ezsign.feb.special.dao.YE403DAO;
import com.ezsign.feb.special.dao.YE404DAO;
import com.ezsign.feb.special.dao.YE405DAO;
import com.ezsign.feb.special.vo.YE402VO;
import com.ezsign.feb.special.vo.YE403VO;
import com.ezsign.feb.special.vo.YE404VO;
import com.ezsign.feb.special.vo.YE405VO;
import com.ezsign.feb.system.dao.YS000DAO;
import com.ezsign.feb.system.dao.YS030DAO;
import com.ezsign.feb.system.dao.YS031DAO;
import com.ezsign.feb.system.vo.YS000VO;
import com.ezsign.feb.system.vo.YS030VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.feb.worker.dao.YE001DAO;
import com.ezsign.feb.worker.dao.YE002DAO;
import com.ezsign.feb.worker.dao.YE003DAO;
import com.ezsign.feb.worker.service.YE000Service;
import com.ezsign.feb.worker.service.YE013Service;
import com.ezsign.feb.worker.service.YP000Service;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.feb.worker.vo.YE002VO;
import com.ezsign.feb.worker.vo.YE013VO;
import com.ezsign.framework.exception.AngullarProcessException;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.SessionVO;

//@SuppressWarnings({"NonAsciiCharacters", "AccessStaticViaInstance"})
@Service("excelService")
public class ExcelServiceImpl implements ExcelService {

    private Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "codeDAO")
    private CodeDAO codeDAO;

    @Resource(name = "excelMappingDAO")
    private ExcelMappingDAO excelMappingDAO;

    @Resource(name = "empDAO")
    private EmpDAO empDAO;

    @Resource(name = "ys000DAO")
    private YS000DAO ys000DAO;

    @Resource(name = "ye002DAO")
    private YE002DAO ye002DAO;

    @Resource(name = "ye003DAO")
    private YE003DAO ye003DAO;

    @Resource(name = "ys030DAO")
    private YS030DAO ys030DAO;

    @Resource(name = "ys031DAO")
    private YS031DAO ys031DAO;

    @Resource(name = "ye001DAO")
    private YE001DAO ye001DAO;

    @Resource(name = "ye404DAO")
    private YE404DAO ye404DAO;

    @Resource(name = "ye405DAO")
    private YE405DAO ye405DAO;

    @Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;
    
    @Resource(name = "ye402DAO")
    private YE402DAO ye402DAO;
    
    @Resource(name = "ye403DAO")
    private YE403DAO ye403DAO;
        
    @Resource(name = "ye108DAO")
    private YE108DAO ye108DAO;
        
    @Resource(name = "ye000Service")
    private YE000Service ye000Service;

    @Resource(name = "ye013Service")
    private YE013Service ye013Service;
    
    @Resource(name = "yp000Service")
    private YP000Service yp000Service;

    @Override
    public List<ExcelMappingVO> getExcelMappingList(ExcelMappingVO excelMappingVO) {
        List<ExcelMappingVO> list = excelMappingDAO.getExcelMappingList(excelMappingVO);

        CodeVO codeVO = new CodeVO();

        for (ExcelMappingVO vo : list) {
            if ("5".equals(vo.getColumnType())) {
                if (vo.getGrpCommCode() == null) {
                    if (vo.getCodeValue() != null) {
                        vo.setCodes(Arrays.asList(vo.getCodeValue().split(",")));
                    }
                } else {
                    vo.setCodes(new ArrayList<>());

                    codeVO.setGrpCommCode(vo.getGrpCommCode());
                    List<CodeVO> resultCode = codeDAO.getCodeList(codeVO);

                    for (CodeVO item : resultCode) {
                        vo.getCodes().add(item.getCommCode());
                    }
                }
            }
        }

        return list;
    }

    @Override
    public List<ExcelMappingVO> insExcelMapping(ExcelMappingVO excelMappingVO) {
        excelMappingDAO.insExcelMapping(excelMappingVO);
        return getExcelMappingList(excelMappingVO);
    }

    @Override
    public int selectExcelMappingCnt(ExcelMappingVO excelMappingVO) throws Exception{
    	return excelMappingDAO.selectExcelMappingCnt(excelMappingVO);
    }
    
    @Override
    public void insertExcelMapping(ExcelMappingVO excelMappingVO) throws Exception{
        excelMappingDAO.insertExcelMapping(excelMappingVO);
        //return getExcelMappingList(excelMappingVO);
    }
    
    @Override
    public void updateExcelMapping(ExcelMappingVO excelMappingVO) throws Exception{
        excelMappingDAO.updateExcelMapping(excelMappingVO);
        //return getExcelMappingList(excelMappingVO);
    }
    
    /**
     * <pre>
     * 1. 개요 : 부서 엑셀 파일 등록
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : saveExcelType1
     * @date : 2019. 1. 9.
     * @author : soybean0627
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2019. 1. 9.				soybean0627					최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @see com.ezsign.feb.master.service.ExcelService#saveExcelType1(com.ezsign.feb.master.vo.ExcelDataRequest)
     *  @param request
     *  @throws Exception
     */
    @Override
    public Map<String,String> saveExcelType1(ExcelDataRequest<YS031VO> request) throws Exception {

    	Map<String,String> resultMap = new HashMap<String,String>();
    	
    	try{
    		
            int upCnt = 0;
            int inCnt = 0;
            
	    	if (request.data != null) {
	            YS030VO ys030VO = new YS030VO();
	            ys030VO.set계약ID(request.계약ID);
		            
	            logger.debug("# 부서 엑셀 파일 총 건수 : " + request.data.size() );
	            
	            for (YS031VO vo : request.data) {
//	            	logger.debug("# vo.get사업장명() : " + vo.get사업장명() );
//	            	logger.debug("# vo.get부서명() : " + vo.get부서명() );
	            	
	            	
	                ys030VO.set사업장명(StringUtil.nvl(vo.get사업장명(),""));
	                String 사업장ID = ys030DAO.getYS030ID(ys030VO);
	                logger.debug("# 사업장ID : " + 사업장ID);
	                if (사업장ID == null) {
	                    throw new AngullarProcessException("사업장명(" + vo.get사업장명() + ")이 존재하지 않습니다.");
	                }else{
	                	
	                	YS031VO ys031VO = new YS031VO();         
	                	ys031VO.set계약ID(request.계약ID);
	                	ys031VO.set사업장ID(사업장ID);
	                	ys031VO.set부서명(vo.get부서명());
	                	
	                	ys031VO = ys031DAO.getYS031(vo);
	                	
	                	if(ys031VO == null){
	                		vo.set계약ID(request.계약ID);
	    	                vo.set사업장ID(사업장ID);
	    	                ys031DAO.insYS031(vo);
	    	                
	    	                inCnt++;
	                	}else{
	                		vo.set계약ID(request.계약ID);
	    	                vo.set사업장ID(사업장ID);
	                		vo.set부서ID(ys031VO.get부서ID());	                		
	                		ys031DAO.updYS031(vo);
	                		
	                		upCnt++;
	                	}
	                	
	                }

	            }
	            
	            logger.debug("# 부서 정보 등록 건수 : " + inCnt );
	            logger.debug("# 부서 정보 수정 건수 : " + upCnt );
	        }
	    	
	    	resultMap.put("result", "1");
    		if(request.data != null) {
    			resultMap.put("message", "등록 : [" + inCnt + "] 건 / 수정 : [" + upCnt + "] 건 이 처리되었습니다.");
    		}
    			    		
    	}catch(AngullarProcessException ex){
    		logger.error(ex.getMessage());
    		
    		resultMap.put("result", "0");
    		resultMap.put("message", ex.getMessage());
    		
    	}
        
        return resultMap;
    }

    /**
     * <pre>
     * 1. 개요 : 근로자 엑셀 파일 등록
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : saveExcelType2
     * @date : 2019. 1. 9.
     * @author : soybean0627
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2019. 1. 9.				soybean0627					최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @see com.ezsign.feb.master.service.ExcelService#saveExcelType2(java.lang.String, com.ezsign.feb.master.vo.ExcelDataRequest)
     *  @param bizId
     *  @param request
     *  @throws Exception
     */
    @Override
    public Map<String,String> saveExcelType2(String bizId, ExcelDataRequest<YE000VO> request) throws Exception {
    	
    	Map<String,String> resultMap = new HashMap<String,String>();
    	    	
    	try{
    		
    		
    		int dataCnt = 0;
	        if (request.data != null) {

                YS000VO ys000 = new YS000VO();
                ys000.setBizId(bizId);
                ys000.set계약ID(request.계약ID);
                List<YS000VO> listYS000 = ys000DAO.getYS000List(ys000);
                int 계약년도 = 0;
                if (listYS000.size() > 0) {                	
                    	계약년도 = Integer.parseInt(listYS000.get(0).getFebYear());                    
                }
                
                
	            EmpVO empVO = new EmpVO();
	            empVO.setBizId(bizId);
	
	            
	            logger.debug("# 근로자 엑셀 파일 총 건수 : " + request.data.size() );
	            
	            for (YE000VO vo : request.data) {
	            	
	            	logger.debug("# 근로자 request : " + vo.toString());
	            	
	                empVO.setEmpNo(vo.getEmpNo());
	                EmpVO resultEmpVO = empDAO.getEmpNo(empVO);
	                if (resultEmpVO == null) {
	                    throw new AngullarProcessException("사번(" + vo.getEmpNo() + ")이 존재하지 않습니다.");
	                }
	                vo.setBusinessNo(resultEmpVO.getBusinessNo());
	
	                vo.setM01(StringUtils.trimToEmpty(vo.get국외근로_월_100만원()));
	            	vo.setM02(StringUtils.trimToEmpty(vo.get국외근로_월_300만원()));
	            	vo.setM03(StringUtils.trimToEmpty(vo.get국외근로_전액()));
	            	vo.setO01(StringUtils.trimToEmpty(vo.get야간근로수당_년_240만원()));
	            	vo.setQ01(StringUtils.trimToEmpty(vo.get출산보육수당_월_10만원()));
	            	vo.setH08(StringUtils.trimToEmpty(vo.get특별법_연구보조비_월_20만원()));
	            	vo.setH09(StringUtils.trimToEmpty(vo.get연구기관_등_연구보조비_월_20만원()));
	            	vo.setH10(StringUtils.trimToEmpty(vo.get기업부설연구소_연구보조비_월_20만원()));	            	
	            	vo.setG01(StringUtils.trimToEmpty(vo.get비과세학자금_납입금액()));
	            	vo.setH11(StringUtils.trimToEmpty(vo.get취재수당_월_20만원()));
	            	vo.setH12(StringUtils.trimToEmpty(vo.get벽지수당_월_20만원()));
	            	vo.setH13(StringUtils.trimToEmpty(vo.get재해관련급여_전액()));
	            	vo.setH01(StringUtils.trimToEmpty(vo.get무보수위원수당_전액()));
	            	vo.setK01(StringUtils.trimToEmpty(vo.get외국주둔군인등_전액()));	            	
	            	vo.setS01(StringUtils.trimToEmpty(vo.get주식매수선택권_년_3000만원()));
	            	vo.setT01(StringUtils.trimToEmpty(vo.get외국인기술자_년_근로소득세의_50퍼센트_한도()));
	            	vo.setY02(StringUtils.trimToEmpty(vo.get우리사주조합인출금50퍼센트_년_인출금의_50퍼센트_한도()));
	            	vo.setY03(StringUtils.trimToEmpty(vo.get우리사주조합인출금75퍼센트_년_인출금의_75퍼센트_한도()));
	            	vo.setY04(StringUtils.trimToEmpty(vo.get우리사주조합인출금100퍼센트_년_인출금의_100퍼센트_한도()));
	            	vo.setH05(StringUtils.trimToEmpty(vo.get경호_승선수당()));	            		            	
	            	vo.setI01(StringUtils.trimToEmpty(vo.get외국정부등근무자_전액()));
	            	vo.setR10(StringUtils.trimToEmpty(vo.get근로장학금_전액()));
	            	vo.setH14(StringUtils.trimToEmpty(vo.get보육교사_근무환경개선비_전액()));
	            	vo.setH15(StringUtils.trimToEmpty(vo.get사립유치원수석교사_교사의_인건비_전액()));
	            	vo.setT10(StringUtils.trimToEmpty(vo.get중소기업취업청년_소득세_감면100퍼센트_소득세의_100퍼센트()));
	            	vo.setT11(StringUtils.trimToEmpty(vo.get중소기업취업청년_소득세_감면50퍼센트_소득세의_50퍼센트()));
	            	vo.setT12(StringUtils.trimToEmpty(vo.get중소기업취업청년_소득세_감면70퍼센트_소득세의_70퍼센트()));
	            	vo.setT13(StringUtils.trimToEmpty(vo.get중소기업취업청년_소득세_감면90퍼센트_소득세의_90퍼센트()));	            		            	
	            	vo.setT20(StringUtils.trimToEmpty(vo.get조세조약상_교직자감면_전액()));
	            	vo.setH16(StringUtils.trimToEmpty(vo.get정부_공공기관지방이전기관_종사자_이주수당_월_20만원()));
	            	vo.setR11(StringUtils.trimToEmpty(vo.get직무발명보상금_년_300만원()));
	            	vo.setH06(StringUtils.trimToEmpty(vo.get유아_초중등_연구보조비_월_20만원()));
	            	vo.setH07(StringUtils.trimToEmpty(vo.get고등교육법_연구보조비_월_20만원()));
	            	vo.setY22(StringUtils.trimToEmpty(vo.get전공의_수련_보조_수당()));
	            	vo.setH17(StringUtils.trimToEmpty(vo.get종교활동비()));
	            	vo.setU01(StringUtils.trimToEmpty(vo.get벤처기업_주식매수선택권_년_2000만원()));
	                
	            	
	                vo.setBizId(bizId);
	                vo.set계약ID(request.계약ID);
	                vo.set사용자ID(resultEmpVO.getUserId());
	                vo.set개인식별번호(vo.get개인식별번호().replace("-", ""));

                    int 나이 = 0;
                    
                    switch (vo.get개인식별번호().substring(6, 7)) {
                        case "1":
                        case "2":
                        case "5":
                        case "6":
                        	나이 = 계약년도 - Integer.parseInt("19" + vo.get개인식별번호().substring(0, 2));
                            break;
                        case "3":
                        case "4":
                        case "7":
                        case "8":
                        	나이 = 계약년도 - Integer.parseInt("20" + vo.get개인식별번호().substring(0, 2));
                            break;
                        case "9":
                        case "0":
                        	나이 = 계약년도 - Integer.parseInt("18" + vo.get개인식별번호().substring(0, 2));
                            break;
                    }

                    vo.set나이(String.valueOf(나이));
                    
	
	                vo.setJoinDate(vo.getJoinDate().replaceAll("-", ""));
	                vo.setJoinDate(vo.getJoinDate().replaceAll("/", ""));
	                vo.setJoinDate(vo.getJoinDate().replaceAll("\\.", ""));
	
	                if (!TextUtils.isEmpty(vo.getLeaveDate())) {
	                    vo.setLeaveDate(vo.getLeaveDate().replaceAll("-", ""));
	                    vo.setLeaveDate(vo.getLeaveDate().replaceAll("/", ""));
	                    vo.setLeaveDate(vo.getLeaveDate().replaceAll("\\.", ""));
	                }
	
	                if (!TextUtils.isEmpty(vo.get사내내선번호())) {
	                    vo.set사내내선번호(vo.get사내내선번호().replaceAll("-", ""));
	                }
	
	                if (!TextUtils.isEmpty(vo.getPhoneNum())) {
	                    vo.setPhoneNum(vo.getPhoneNum().replaceAll("-", ""));
	                }
	
	                if (!TextUtils.isEmpty(vo.get감면기간_FROM())) {
	                    vo.set감면기간_FROM(vo.get감면기간_FROM().replaceAll("-", ""));
	                    vo.set감면기간_FROM(vo.get감면기간_FROM().replaceAll("/", ""));
	                    vo.set감면기간_FROM(vo.get감면기간_FROM().replaceAll("\\.", ""));
	                }
	
	                if (!TextUtils.isEmpty(vo.get감면기간_TO())) {
	                    vo.set감면기간_TO(vo.get감면기간_TO().replaceAll("-", ""));
	                    vo.set감면기간_TO(vo.get감면기간_TO().replaceAll("/", ""));
	                    vo.set감면기간_TO(vo.get감면기간_TO().replaceAll("\\.", ""));
	                }
	
//	                if (!"1".equals(vo.get부서표시여부())) {
//	                    vo.set부서표시여부("2");
//	                }
	                if (StringUtils.isEmpty(vo.get부서표시여부())) {
	                    vo.set부서표시여부("1");
	                }
	                if (!"1".equals(vo.get내외국인구분())) {
	                    vo.set내외국인구분("2");
	                }
	                if (!"1".equals(vo.get거주구분())) {
	                    vo.set거주구분("2");
	                }
	                if (!"1".equals(vo.get외국인단일세율적용())) {
	                    vo.set외국인단일세율적용("2");
	                }
	                if (!"1".equals(vo.get국외근로제공여부()) && !"2".equals(vo.get국외근로제공여부()) && !"3".equals(vo.get국외근로제공여부())) {
	                    vo.set국외근로제공여부("4");
	                }
	                if (!"1".equals(vo.get중소기업취업감면여부())) {
	                    vo.set중소기업취업감면여부("2");
	                }
	                if (!"1".equals(vo.get생산직등야간근로비과세())) {
	                    vo.set생산직등야간근로비과세("2");
	                }
	                if (!"1".equals(vo.get세대주여부())) {
	                    vo.set세대주여부("2");
	                }
	                if (!"1".equals(vo.get연말정산분납여부())) {
	                    vo.set연말정산분납여부("2");
	                }
//	                if (!"1".equals(vo.get종전근무지_납세조합유무())) {
//	                    vo.set종전근무지_납세조합유무("2");
//	                }
	                //무조건 '1' 로 세팅  - 20190118
	                vo.set종전근무지_납세조합유무("1");
	                
	                resultMap = ye000Service.saveYE000(vo);
	                
	                if(resultMap != null){	                
	                	if(StringUtils.equals(resultMap.get("result"), "1")){
	                		dataCnt++;
	                	}else if(StringUtils.equals(resultMap.get("result"), "500")){
	                		throw new AngullarProcessException(StringUtils.defaultIfEmpty(resultMap.get("message"), "근로자 등록중 오류가 발생했습니다."));
	                	}
	                }
	            }
	        }
	        
	        resultMap.put("result", "1");
	        resultMap.put("message", "[ " + dataCnt + " ] 건이 처리되었습니다.");
	        
    	}catch(AngullarProcessException ex){
    		logger.error(ex.getMessage());
    		
    		resultMap.put("result", "0");
    		resultMap.put("message", ex.getMessage());
    		
    	}
        
        return resultMap;
    }

    /**
     * <pre>
     * 1. 개요 : 연말정산 > 기초자료설정 > 부양가족부양가족  엑셀파일 등록
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : saveExcelType3
     * @date : 2019. 1. 10.
     * @author : soybean0627
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2019. 1. 10.				soybean0627					최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @see com.ezsign.feb.master.service.ExcelService#saveExcelType3(java.lang.String, com.ezsign.feb.master.vo.ExcelDataRequest)
     *  @param bizId
     *  @param request
     *  @throws Exception
     */
    @Override
    public Map<String,String> saveExcelType3(String bizId, ExcelDataRequest<YE001VO> request) throws Exception {
    	
    	Map<String,String> resultMap = new HashMap<String,String>();
    	
    	try{
    		
    		if (request.data != null) {
    			
                YS000VO ys000 = new YS000VO();
                ys000.setBizId(bizId);
                ys000.set계약ID(request.계약ID);
                List<YS000VO> listYS000 = ys000DAO.getYS000List(ys000);
                int 계약년도 = 0;
                if (listYS000.size() > 0) {
                    	계약년도 = Integer.parseInt(listYS000.get(0).getFebYear());
                }
                
                EmpVO empVO = new EmpVO();
                empVO.setBizId(bizId);

                logger.debug("# 부양가족부양가족 엑셀 파일 총 건수 : " + request.data.size() );
                int upCnt = 0;
                int inCnt = 0;
                
                for (YE001VO vo : request.data) {
                	logger.debug("# 부양가족 VO : " + vo );
                	
                    empVO.setEmpNo(vo.getEmpNo());
                    EmpVO resultEmpVO = empDAO.getEmpNo(empVO);
                    if (resultEmpVO == null) {
                        throw new AngullarProcessException("사번(" + vo.getEmpNo() + ")이 존재하지 않습니다.");
                    }

                    vo.set계약ID(request.계약ID);
                    vo.set사용자ID(resultEmpVO.getUserId());

                    int 나이 = 0;
                    vo.set개인식별번호(vo.get개인식별번호().replace("-", ""));
                    
                    switch (vo.get개인식별번호().substring(6, 7)) {
                        case "1":
                        case "2":
                        case "5":
                        case "6":
                        	나이 = 계약년도 - Integer.parseInt("19" + vo.get개인식별번호().substring(0, 2));
                            break;
                        case "3":
                        case "4":
                        case "7":
                        case "8":
                            나이 = 계약년도 - Integer.parseInt("20" + vo.get개인식별번호().substring(0, 2));
                            break;
                        case "9":
                        case "0":
                            나이 = 계약년도 - Integer.parseInt("18" + vo.get개인식별번호().substring(0, 2));
                            break;
                    }

                    vo.set개인식별번호(SecurityUtil.getinstance().aesEncode(vo.get개인식별번호()));
                    vo.set나이(String.valueOf(나이));

                    if (!"1".equals(vo.get내외국인())) {
                        vo.set내외국인("2");
                    }
                    if (!"1".equals(vo.get기본공제())) {
                        vo.set기본공제("2");
                    }
                    if (!"1".equals(vo.get부녀자())) {
                        vo.set부녀자("2");
                    }
                    if (!"1".equals(vo.get한부모())) {
                        vo.set한부모("2");
                    }
                    if (!"1".equals(vo.get자녀())) {
                        vo.set자녀("2");
                    }
                    if (!"1".equals(vo.get경로우대())) {
                        vo.set경로우대("2");
                    }

                    if (!"1".equals(vo.get장애인()) && !"2".equals(vo.get장애인()) && !"3".equals(vo.get장애인())) {
                        vo.set장애인("4");
                    }
                    if (!"1".equals(vo.get출산입양()) && !"2".equals(vo.get출산입양()) && !"3".equals(vo.get출산입양())) {
                        vo.set출산입양("4");
                    }
                    
                    //부양가족 정보 체크
                    YE001VO parmaVO = new YE001VO();
                    parmaVO.set계약ID(vo.get계약ID());
                    parmaVO.set사용자ID(vo.get사용자ID());
                    parmaVO.set개인식별번호(vo.get개인식별번호());                    
                    
                    YE001VO rstVO = ye001DAO.getYE001ID(parmaVO);
                    
                    if(rstVO != null){
                    	vo.set부양가족ID(rstVO.get부양가족ID());
                    	ye001DAO.updYE001(vo);
                    	upCnt++;
                    }else{
                    	ye001DAO.insYE001(vo);
                    	inCnt++;
                    }

                    
                }
                
                logger.debug("# 부양가족부양가족 등록 건수 : " + inCnt );
                logger.debug("# 부양가족부양가족 수정 건수 : " + upCnt );

                resultMap.put("result", "1");
                if(request.data != null) {
        			resultMap.put("message", "등록 : [" + inCnt + "] 건 / 수정 : [" + upCnt + "] 건 이 처리되었습니다.");
        		}
    			
    		}else{
    			throw new AngullarProcessException("엑셀파일 데이타 정보가 없습니다.");
    		}
    		
    		
    	}catch(AngullarProcessException ex){
    		
    		logger.error(ex.getMessage());
    		
    		resultMap.put("result", "0");
    		resultMap.put("message", ex.getMessage());
    		
    	}
    	
        return resultMap;
        
    }

    /**
     * <pre>
     * 1. 개요 : 연말정산 > 기초자료설정 > 사내기부금 엑셀등록
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : saveExcelType4
     * @date : 2019. 1. 14.
     * @author : soybean0627
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2019. 1. 14.				soybean0627					최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @see com.ezsign.feb.master.service.ExcelService#saveExcelType4(java.lang.String, java.lang.String, com.ezsign.feb.master.vo.ExcelDataRequest)
     *  @param bizId
     *  @param 작업자ID
     *  @param request
     *  @return
     *  @throws Exception
     */
    @Override
    public Map<String,String> saveExcelType4(String bizId, String 작업자ID, ExcelDataRequest<YE404VO> request) throws Exception {
    	
    	Map<String,String> resultMap = new HashMap<String,String>();
    	
    	try{
    		
    		int dataCnt = 0;
	        if (request.data != null) {
	            EmpVO empVO = new EmpVO();
	            empVO.setBizId(bizId);
	
	            logger.debug("# 사내기부금 총 건수 : " + request.data.size() );
	            
	            for (YE404VO vo : request.data) {
	                empVO.setEmpNo(vo.getEmpNo());
	                EmpVO resultEmpVO = empDAO.getEmpNo(empVO);
	                if (resultEmpVO == null) {
	                    throw new AngullarProcessException("사번(" + vo.getEmpNo() + ")이 존재하지 않습니다.");
	                }
	
	                vo.set계약ID(request.계약ID);
	                vo.set사용자ID(resultEmpVO.getUserId());
//	                vo.set작업자ID(작업자ID);
	                vo.set작업자ID(resultEmpVO.getUserId());
	                vo.set부양가족ID(resultEmpVO.getUserId());
	                vo.set자료구분코드("2");  // 회사
	                vo.set기부내용("1");  // 금전
	                vo.set기부처_사업자등록번호(vo.get기부처_사업자등록번호().replaceAll("-", ""));
	
	                if (vo.get기부명세_공제금액() == 0) {
	                    vo.set기부명세_공제금액(vo.get기부명세_공제대상기부금() + vo.get기부명세_기부장려금() + vo.get기부명세_기타());
	                }
	                vo.set최종저장여부("1");
	                vo.set사용여부("1");
	                
	                ye404DAO.insYE404(vo);
	                
	                dataCnt++;
	            }

	        }
	        
	        resultMap.put("result", "1");
	        if(request.data != null) {
    			resultMap.put("message", "[ " + dataCnt + " ] 건이 처리되었습니다.");
    		}
	        
    	}catch(AngullarProcessException ex){
    		
    		logger.error(ex.getMessage());
    		
    		resultMap.put("result", "0");
    		resultMap.put("message", ex.getMessage());
    		
    	}   
    	
    	return resultMap;   
    }

    /**
     * <pre>
     * 1. 개요 : 연말정산 > 기초자료설정 > 이월기부금 엑셀등록
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : saveExcelType5
     * @date : 2019. 1. 14.
     * @author : soybean0627
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2019. 1. 14.				soybean0627					최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @see com.ezsign.feb.master.service.ExcelService#saveExcelType5(java.lang.String, java.lang.String, com.ezsign.feb.master.vo.ExcelDataRequest)
     *  @param bizId
     *  @param 작업자ID
     *  @param request
     *  @return
     *  @throws Exception
     */
    @Override
    public Map<String,String> saveExcelType5(String bizId, String 작업자ID, ExcelDataRequest<YE405VO> request) throws Exception {
    	
    	Map<String,String> resultMap = new HashMap<String,String>();
    	
    	try{
    		
    		int upCnt = 0;
            int inCnt = 0;
            
	        if (request.data != null) {
	            EmpVO empVO = new EmpVO();
	            empVO.setBizId(bizId);
	
	            YE405VO ye405VO = new YE405VO(); 
	            ye405VO.set계약ID(request.계약ID);
	
	            logger.debug("# 이월기부금 총 건수 : " + request.data.size() );
	            
	            for (YE405VO vo : request.data) {
	                empVO.setEmpNo(vo.getEmpNo());
	                EmpVO resultEmpVO = empDAO.getEmpNo(empVO);
	                if (resultEmpVO == null) {
	                    throw new AngullarProcessException("사번(" + vo.getEmpNo() + ")이 존재하지 않습니다.");
	                }
	
	                ye405VO.set사용자ID(resultEmpVO.getUserId());
	                ye405VO.set기부금종류코드(vo.get기부금종류코드());
	                ye405VO.set기부년도(vo.get기부년도());
	                
	                
	                vo.set계약ID(request.계약ID);
	                vo.set사용자ID(resultEmpVO.getUserId());
	                vo.set작업자ID(작업자ID);
	                vo.set기부금액(vo.get기부금액().replace(",", ""));
	
	                if (vo.get공제대상기부금() == 0) {
	                    vo.set공제대상기부금(Integer.parseInt(vo.get기부금액()) - vo.get전년도까지공제금액());
	                }
	                	                
	                if (ye405DAO.getYE405Count(ye405VO) > 0) {
//	                    throw new AngullarProcessException(
//	                            vo.getEmpName() + "(" + vo.getEmpNo() + ") " + vo.get기부년도() + "년 이월 기부금(" + vo.get기부금종류코드() + ")이 중복되었습니다."
//	                    );
	                	ye405DAO.updYE405(vo);
	                	upCnt++;
	                }else {
	                	ye405DAO.insYE405(vo);
	                	inCnt++;
	                }

	            }
	            
	            logger.debug("# 이월기부금 등록 건수 : " + inCnt );
	            logger.debug("# 이월기부금 수정 건수 : " + upCnt );
	            
	        }
	        
	        resultMap.put("result", "1");
	        if(request.data != null) {
	        	resultMap.put("message", "등록 : [" + inCnt + "] 건 / 수정 : [" + upCnt + "] 건 이 처리되었습니다.");
    		}
	        
    	}catch(AngullarProcessException ex){
    		
    		logger.error(ex.getMessage());
    		
    		resultMap.put("result", "0");
    		resultMap.put("message", ex.getMessage());
    		
    	}   
    	
    	return resultMap;
    }

    /**
     * <pre>
     * 1. 개요 : 연말정산 > 기초자료설정 > 공제불가회사지원금 엑셀등록
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : saveExcelType6
     * @date : 2019. 1. 14.
     * @author : soybean0627
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2019. 1. 14.				soybean0627					최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @see com.ezsign.feb.master.service.ExcelService#saveExcelType6(java.lang.String, com.ezsign.feb.master.vo.ExcelDataRequest)
     *  @param bizId
     *  @param request
     *  @return
     *  @throws Exception
     */
    @Override
    public Map<String,String> saveExcelType6(SessionVO loginVO, ExcelDataRequest<YE013VO> request) throws Exception {
    	
    	Map<String,String> resultMap = new HashMap<String,String>();
    	
    	try{
    		
    		int upCnt = 0;
            int inCnt = 0;
            
	        if (request.data != null) {	
	            EmpVO empVO = new EmpVO();
	            empVO.setBizId(loginVO.getBizId());
	
	            YE001VO ye001VO = new YE001VO();
	            ye001VO.set계약ID(request.계약ID);
	            
	            logger.debug("# 공제불가회사지원금 엑셀 파일 총 건수 : " + request.data.size() );
	            
	            for (YE013VO vo : request.data) {
	            	
	            	logger.debug("# 공제불가회사지원금 request => " + vo.toString() );
	            	
	                empVO.setEmpNo(vo.getEmpNo());
	                EmpVO resultEmpVO = empDAO.getEmpNo(empVO);
	                if (resultEmpVO == null) {
	                    throw new AngullarProcessException("사번(" + vo.getEmpNo() + ")이 존재하지 않습니다.");
	                }
	
	                String 개인식별번호 = SecurityUtil.getinstance().aesEncode(vo.get개인식별번호().replace("-", ""));
	                ye001VO.set개인식별번호(개인식별번호);
	                ye001VO.set사용자ID(resultEmpVO.getUserId());
	                YE001VO resultYE001VO = ye001DAO.getYE001ID(ye001VO);
	                if (resultYE001VO == null) {
	                    throw new AngullarProcessException("부양가족(" + vo.get개인식별번호() + ")이 존재하지 않습니다.");
	                }
	
	                vo.set계약ID(request.계약ID);
	                vo.set사용자ID(resultYE001VO.get사용자ID());
	                vo.set부양가족ID(resultYE001VO.get부양가족ID());
	                if (StringUtils.isNotEmpty(vo.get사업자등록번호())) {
	                    vo.set사업자등록번호(vo.get사업자등록번호().replaceAll("-", ""));
	                }
	
//	                ye013Service.insYE013(vo);	                
	                
	                
	                
	                if("1".equals(vo.get공제구분코드())) {  // 의료비
						YE402VO ye402VO = null;
						
						ye402VO = ye013Service.setYE402(vo);
//						ye402VO.set작업자ID(loginVO.getUserId());						
						ye402VO.set작업자ID(resultYE001VO.get사용자ID());
						ye402VO.set사용여부("1");
						ye402VO.set최종저장여부("1");
						
						if(ye402VO != null){
							ye402DAO.insYE402(ye402VO);
							inCnt++;
						}
					} else if ("2".equals(vo.get공제구분코드())) {  // 교육비
						YE403VO ye403VO = null;
						
						ye403VO = ye013Service.setYE403(vo);
//						ye403VO.set작업자ID(loginVO.getUserId());						
						ye403VO.set작업자ID(resultYE001VO.get사용자ID());
						ye403VO.set사용여부("1");
						ye403VO.set최종저장여부("1");						
						
						if(ye403VO != null){
							ye403DAO.insYE403(ye403VO);
							inCnt++;
						}
					} else if ("3".equals(vo.get공제구분코드())) {  // 신용카드
						YE108VO ye108VO = null;
						
						ye108VO = ye013Service.setYE108(vo);
//						ye108VO.set작업자ID(loginVO.getUserId());
						ye108VO.set작업자ID(resultYE001VO.get사용자ID());
						ye108VO.set사용여부("1");
						ye108VO.set최종저장여부("1");
						
						if(ye108VO != null){
							ye108DAO.insYE108(ye108VO);
							inCnt++;
						}
					}
	                
	            }

	        }
	        
	        resultMap.put("result", "1");
	        if(request.data != null) {
	        	resultMap.put("message", "등록 : [" + inCnt + "] 건이 처리되었습니다.");
    		}
        
    	}catch(AngullarProcessException ex){
    		
    		logger.error(ex.getMessage());
    		
    		resultMap.put("result", "0");
    		resultMap.put("message", ex.getMessage());
    		
    	}   
    	
    	return resultMap;
    }

    /**
     * <pre>
     * 1. 개요 : 연말정산 > 기초자료설정 > 종ㆍ전 근무지  엑셀파일 등록
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : saveExcelType7
     * @date : 2019. 1. 10.
     * @author : soybean0627
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2019. 1. 10.				soybean0627					최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @see com.ezsign.feb.master.service.ExcelService#saveExcelType7(java.lang.String, com.ezsign.feb.master.vo.ExcelDataRequest)
     *  @param bizId
     *  @param request
     *  @return
     *  @throws Exception
     */
    @Override
    public Map<String,String> saveExcelType7(String bizId, ExcelDataRequest<YE000VO> request) throws Exception {
    	
    	Map<String,String> resultMap = new HashMap<String,String>();
    	
    	try{
       
    		int inCnt = 0;
	    	if (request.data != null) {
	            EmpVO empVO = new EmpVO();
	            empVO.setBizId(bizId);
	
	            logger.debug("# 종전근무지 엑셀 파일 총 건수 : " + request.data.size() );
	            
	            for (YE000VO vo : request.data) {
	            	
	            	logger.debug("# 종전근무지 VO : " + vo);
	            	
	            	
	                empVO.setEmpNo(vo.getEmpNo());
	                EmpVO resultEmpVO = empDAO.getEmpNo(empVO);
	                if (resultEmpVO == null) {
	                    throw new AngullarProcessException("사번(" + vo.getEmpNo() + ")이 존재하지 않습니다."); 
	                }
	                
	                vo.setM01(StringUtils.trimToEmpty(vo.get국외근로_월_100만원()));
	            	vo.setM02(StringUtils.trimToEmpty(vo.get국외근로_월_300만원()));
	            	vo.setM03(StringUtils.trimToEmpty(vo.get국외근로_전액()));
	            	vo.setO01(StringUtils.trimToEmpty(vo.get야간근로수당_년_240만원()));
	            	vo.setQ01(StringUtils.trimToEmpty(vo.get출산보육수당_월_10만원()));
	            	vo.setH08(StringUtils.trimToEmpty(vo.get특별법_연구보조비_월_20만원()));
	            	vo.setH09(StringUtils.trimToEmpty(vo.get연구기관_등_연구보조비_월_20만원()));
	            	vo.setH10(StringUtils.trimToEmpty(vo.get기업부설연구소_연구보조비_월_20만원()));
	            	vo.setG01(StringUtils.trimToEmpty(vo.get비과세학자금_납입금액()));
	            	vo.setH11(StringUtils.trimToEmpty(vo.get취재수당_월_20만원()));
	            	vo.setH12(StringUtils.trimToEmpty(vo.get벽지수당_월_20만원()));
	            	vo.setH13(StringUtils.trimToEmpty(vo.get재해관련급여_전액()));
	            	vo.setH01(StringUtils.trimToEmpty(vo.get무보수위원수당_전액()));
	            	vo.setK01(StringUtils.trimToEmpty(vo.get외국주둔군인등_전액()));	            	
	            	vo.setS01(StringUtils.trimToEmpty(vo.get주식매수선택권_년_3000만원()));
	            	vo.setT01(StringUtils.trimToEmpty(vo.get외국인기술자_년_근로소득세의_50퍼센트_한도()));
	            	vo.setY02(StringUtils.trimToEmpty(vo.get우리사주조합인출금50퍼센트_년_인출금의_50퍼센트_한도()));
	            	vo.setY03(StringUtils.trimToEmpty(vo.get우리사주조합인출금75퍼센트_년_인출금의_75퍼센트_한도()));
	            	vo.setY04(StringUtils.trimToEmpty(vo.get우리사주조합인출금100퍼센트_년_인출금의_100퍼센트_한도()));
	            	vo.setH05(StringUtils.trimToEmpty(vo.get경호_승선수당()));	            		            	
	            	vo.setI01(StringUtils.trimToEmpty(vo.get외국정부등근무자_전액()));
	            	vo.setR10(StringUtils.trimToEmpty(vo.get근로장학금_전액()));
	            	vo.setH14(StringUtils.trimToEmpty(vo.get보육교사_근무환경개선비_전액()));
	            	vo.setH15(StringUtils.trimToEmpty(vo.get사립유치원수석교사_교사의_인건비_전액()));
	            	vo.setT10(StringUtils.trimToEmpty(vo.get중소기업취업청년_소득세_감면100퍼센트_소득세의_100퍼센트()));
	            	vo.setT11(StringUtils.trimToEmpty(vo.get중소기업취업청년_소득세_감면50퍼센트_소득세의_50퍼센트()));
	            	vo.setT12(StringUtils.trimToEmpty(vo.get중소기업취업청년_소득세_감면70퍼센트_소득세의_70퍼센트()));
	            	vo.setT13(StringUtils.trimToEmpty(vo.get중소기업취업청년_소득세_감면90퍼센트_소득세의_90퍼센트()));	            		            	
	            	vo.setT20(StringUtils.trimToEmpty(vo.get조세조약상_교직자감면_전액()));
	            	vo.setH16(StringUtils.trimToEmpty(vo.get정부_공공기관지방이전기관_종사자_이주수당_월_20만원()));
	            	vo.setH17(StringUtils.trimToEmpty(vo.get종교활동비()));	            	
	            	vo.setU01(StringUtils.trimToEmpty(vo.get벤처기업_주식매수선택권_년_2000만원()));	            		            	
	            	vo.setR11(StringUtils.trimToEmpty(vo.get직무발명보상금_년_300만원()));
	            	vo.setH06(StringUtils.trimToEmpty(vo.get유아_초중등_연구보조비_월_20만원()));
	            	vo.setH07(StringUtils.trimToEmpty(vo.get고등교육법_연구보조비_월_20만원()));
	            	vo.setY22(StringUtils.trimToEmpty(vo.get전공의_수련_보조_수당()));
	            	
	            		                
	                
	                vo.set계약ID(request.계약ID);
	                vo.set사용자ID(resultEmpVO.getUserId());
	                vo.set사업자등록번호(StringUtil.nvl(vo.get사업자등록번호(), "").replaceAll("-", ""));

	                
	                vo.set근무시작일(StringUtil.nvl(vo.get근무시작일(), "").replaceAll("-", ""));
	                vo.set근무시작일(StringUtil.nvl(vo.get근무시작일(), "").replaceAll("/", ""));
	                vo.set근무시작일(StringUtil.nvl(vo.get근무시작일(), "").replaceAll("\\.", ""));
	
	                if(StringUtil.nvl(vo.get근무종료일(), "").equals("") 
	                		|| StringUtils.equals("0", vo.get근무종료일()) 
	                		|| StringUtils.equals("00010101", vo.get근무종료일()) ){
	                	vo.set근무종료일("");
	                }else{
	                	vo.set근무종료일(StringUtil.nvl(vo.get근무종료일(), "").replaceAll("-", ""));
		                vo.set근무종료일(StringUtil.nvl(vo.get근무종료일(), "").replaceAll("/", ""));
		                vo.set근무종료일(StringUtil.nvl(vo.get근무종료일(), "").replaceAll("\\.", ""));
	                }
	                
	
	                if (!TextUtils.isEmpty(vo.get감면시작일())) {
	                    vo.set감면시작일(vo.get감면시작일().replaceAll("-", ""));
	                    vo.set감면시작일(vo.get감면시작일().replaceAll("/", ""));
	                    vo.set감면시작일(vo.get감면시작일().replaceAll("\\.", ""));
	                }
	
	                if (!TextUtils.isEmpty(vo.get감면종료일())) {
	                    vo.set감면종료일(vo.get감면종료일().replaceAll("-", ""));
	                    vo.set감면종료일(vo.get감면종료일().replaceAll("/", ""));
	                    vo.set감면종료일(vo.get감면종료일().replaceAll("\\.", ""));
	                }
	
	                // 엑셀에서는 1:종전근무지, 2:납세조합
	                // DB에서는 1:주현근무지, 2:종전근무지, 3:납세조합
	                if ("2".equals(vo.get근무지구분())) {
	                    vo.set근무지구분("3");
	                } else if("1".equals(vo.get근무지구분())) {
	                    vo.set근무지구분("2");
	                } else{
	                	throw new AngullarProcessException("사번(" + vo.getEmpNo() + ")의 근무지구분 정보가 잘못되었습니다.");
	                }
	
	                
	                //종전근무지_납세조합유무 를 체크한다. ( 종전근무지 납세조합유무가 1인경우만 등록한다.)	
	                YE000VO ye000VO = new YE000VO();
	        		ye000VO.set계약ID(request.계약ID);
	        		ye000VO.set사용자ID(resultEmpVO.getUserId());
	                YE000VO detailVO = ye000DAO.getYE000DataDetail(ye000VO);
	                
	                if(detailVO != null && StringUtils.equals(detailVO.get종전근무지_납세조합유무(), "1")){
	                	
		                //등록전에 DATA 존재여부를 체크한다.
		                YE002VO ye002VO = new YE002VO();
		                ye002VO.set계약ID(vo.get계약ID());
		                ye002VO.set사용자ID(vo.get사용자ID());
		                ye002VO.set사업자등록번호(vo.get사업자등록번호());
		                ye002VO.set근무지구분(vo.get근무지구분());
		                	                
		                int dataCnt = ye002DAO.getYE002DataCnt(ye002VO);
		                
		                if(dataCnt == 0){
		                	
		                	// 종(전)근무지 급여항목 등록
		                	String 원천명세ID = ye003DAO.insYE003(vo);
			                
			                logger.debug("# 원천명세ID : " + 원천명세ID );
			                
			                if(StringUtils.isNotEmpty(원천명세ID)){		                	
			                	vo.set원천명세ID(원천명세ID);
			                	
			                	// 종(전)근무지 등록
				                ye002DAO.insYE002(vo);
				                
				                inCnt++; 
			                }

		                }else{
		                	logger.info("##### 데이타가 존재합니다. #####");
		                	logger.info("# 사용자 아이디 : " + resultEmpVO.getUserId() + " / 사용자명 " + vo.getEmpName() + "  / 계약ID : " + vo.get계약ID() + "/ 사용자ID : " + vo.get사용자ID() + " / 사업자등록번호 : " + vo.get사업자등록번호() + " / 근무지구분  : " + vo.get근무지구분());
		                }

	                	
	                }else{
	                	logger.info("##### 종전근무지_납세조합유무 설정을 확인하십시오. #####");
	                	logger.info("# 사용자 아이디 : " + resultEmpVO.getUserId() + " / 사용자명 " + vo.getEmpName() + "  / 계약ID : " + vo.get계약ID() + "/ 사용자ID : " + vo.get사용자ID() + " / 사업자등록번호 : " + vo.get사업자등록번호() + " / 근무지구분  : " + vo.get근무지구분());
	                }

	            }
	        }
        
	    	resultMap.put("result", "1");
	    	if(request.data != null) {
    			resultMap.put("message", "[ " + inCnt + " ] 건이 저장되었습니다.");
    		}
    		
    	}catch(AngullarProcessException ex){
    		
    		logger.error(ex.getMessage());
    		
    		resultMap.put("result", "0");
    		resultMap.put("message", ex.getMessage());
    		
    	}
    	
        return resultMap;
    }
    
    /**
     * <pre>
     * 1. 개요 : 원천징수영수증 관리번호 엑셀 파일 등록
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : saveExcelType8
     * @date : 2019. 2. 27.
     * @author : developsh
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2019. 2. 27.				developsh					최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @see com.ezsign.feb.master.service.ExcelService#saveExcelType8(java.lang.String, com.ezsign.feb.master.vo.ExcelDataRequest)
     *  @param bizId
     *  @param request
     *  @throws Exception
     */
    @Override
    public Map<String,String> saveExcelType8(String bizId, ExcelDataRequest<YE000VO> request) throws Exception {
    	
    	Map<String,String> resultMap = new HashMap<String,String>();
    	    	
    	try{
    		
    		
    		int dataCnt = 0;
	        if (request.data != null) {

	            EmpVO empVO = new EmpVO();
	            empVO.setBizId(bizId);
	            
	            logger.debug("# 원천징수영수증 관리번호 엑셀 파일 총 건수 : " + request.data.size() );
	            
	            for (YE000VO vo : request.data) {
	            	
	            	logger.debug("# 원천징수영수증 관리번호 request : " + vo.toString());
	            	
	                empVO.setEmpNo(vo.getEmpNo());
	                EmpVO resultEmpVO = empDAO.getEmpNo(empVO);
	                if (resultEmpVO == null) {
	                    throw new AngullarProcessException("사번(" + vo.getEmpNo() + ")이 존재하지 않습니다.");
	                }
	            	
	                vo.setBizId(bizId);
	                vo.set계약ID(request.계약ID);
	                vo.set사용자ID(resultEmpVO.getUserId());
	                vo.set원천징수영수증_관리번호(vo.get원천징수영수증_관리번호());
	
	                int cnt = ye000DAO.updYE000(vo);
	                resultMap.put("result", String.valueOf(cnt));
	                
	                if(resultMap != null){	                
	                	if(StringUtils.equals(resultMap.get("result"), "1")){
	                		dataCnt++;
	                	}else if(StringUtils.equals(resultMap.get("result"), "500")){
	                		throw new AngullarProcessException(StringUtils.defaultIfEmpty(resultMap.get("message"), "원천징수영수증 관리번호 오류가 발생했습니다."));
	                	}
	                }
	            }
	        }
	        
	        resultMap.put("result", "1");
	        resultMap.put("message", "[ " + dataCnt + " ] 건이 처리되었습니다.");
	        
    	}catch(AngullarProcessException ex){
    		logger.error(ex.getMessage());
    		
    		resultMap.put("result", "0");
    		resultMap.put("message", ex.getMessage());
    		
    	}
        
        return resultMap;
    }
    
    
    
    /**
     * <pre>
     * 1. 개요 : 간이지급명세서 근로자 엑셀 파일 등록
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : saveExcelType9
     * @date : 2019. 5. 13.
     * @author : 
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2019. 5. 13.										최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @see com.ezsign.feb.master.service.ExcelService#saveExcelType2(java.lang.String, com.ezsign.feb.master.vo.ExcelDataRequest)
     *  @param bizId
     *  @param request
     *  @throws Exception
     */
    @Override
    public Map<String, Object> saveExcelType9(String bizId, ExcelDataRequest<YP000VO> request) throws Exception{
    	
    	Map<String,Object> resultMap = new HashMap<String,Object>();    	    	
    	List<Map<String,String>> empList = new ArrayList<Map<String,String>>();
    	
    	try{
    		
    		
    		int dataCnt = 0;
	        if (request.data != null) {
	        	
                YS000VO ys000 = new YS000VO();
                ys000.setBizId(bizId);
                ys000.set계약ID(request.계약ID);
                List<YS000VO> listYS000 = ys000DAO.getYS000List(ys000);
                int 계약년도 = 0;
                if (listYS000.size() > 0) {                	
                	계약년도 = Integer.parseInt(listYS000.get(0).getFebYear());                    
                }
                            
	            EmpVO empVO = new EmpVO();
	            empVO.setBizId(bizId);
		            
	            logger.debug("# 근로자 엑셀 파일 총 건수 : " + request.data.size() );
	            
	            for (YP000VO vo : request.data) {
	            	
	            	logger.debug("# 근로자 request : " + vo.toString());
	            	
	                empVO.setEmpNo(vo.getEmpNo());
	                EmpVO resultEmpVO = empDAO.getEmpNo(empVO);
	                if (resultEmpVO == null) {
	                    throw new AngullarProcessException("사번(" + vo.getEmpNo() + ")이 존재하지 않습니다.");
	                }
	                vo.setBusinessNo(resultEmpVO.getBusinessNo());
	                
	                vo.set전년도총급여(StringUtils.defaultIfEmpty(vo.get전년도총급여(),"0"));	                	
	                vo.setM01(StringUtils.defaultIfEmpty(vo.get국외근로_월_100만원(),"0"));
	            	vo.setM02(StringUtils.defaultIfEmpty(vo.get국외근로_월_300만원(),"0"));
	            	vo.setM03(StringUtils.defaultIfEmpty(vo.get국외근로_전액(),"0"));
	            	vo.setO01(StringUtils.defaultIfEmpty(vo.get야간근로수당_년_240만원(),"0"));
	            	vo.setQ01(StringUtils.defaultIfEmpty(vo.get출산보육수당_월_10만원(),"0"));
	            	vo.setH08(StringUtils.defaultIfEmpty(vo.get특별법_연구보조비_월_20만원(),"0"));
	            	vo.setH09(StringUtils.defaultIfEmpty(vo.get연구기관_등_연구보조비_월_20만원(),"0"));
	            	vo.setH10(StringUtils.defaultIfEmpty(vo.get기업부설연구소_연구보조비_월_20만원(),"0"));	            	
	            	vo.setG01(StringUtils.defaultIfEmpty(vo.get비과세학자금_납입금액(),"0"));
	            	vo.setH11(StringUtils.defaultIfEmpty(vo.get취재수당_월_20만원(),"0"));
	            	vo.setH12(StringUtils.defaultIfEmpty(vo.get벽지수당_월_20만원(),"0"));
	            	vo.setH13(StringUtils.defaultIfEmpty(vo.get재해관련급여_전액(),"0"));
	            	vo.setH01(StringUtils.defaultIfEmpty(vo.get무보수위원수당_전액(),"0"));
	            	vo.setK01(StringUtils.defaultIfEmpty(vo.get외국주둔군인등_전액(),"0"));	            	
	            	vo.setS01(StringUtils.defaultIfEmpty(vo.get주식매수선택권_년_3000만원(),"0"));
	            	vo.setT01(StringUtils.defaultIfEmpty(vo.get외국인기술자_년_근로소득세의_50퍼센트_한도(),"0"));
	            	vo.setY02(StringUtils.defaultIfEmpty(vo.get우리사주조합인출금50퍼센트_년_인출금의_50퍼센트_한도(),"0"));
	            	vo.setY03(StringUtils.defaultIfEmpty(vo.get우리사주조합인출금75퍼센트_년_인출금의_75퍼센트_한도(),"0"));
	            	vo.setY04(StringUtils.defaultIfEmpty(vo.get우리사주조합인출금100퍼센트_년_인출금의_100퍼센트_한도(),"0"));
	            	vo.setH05(StringUtils.defaultIfEmpty(vo.get경호_승선수당(),"0"));	            		            	
	            	vo.setI01(StringUtils.defaultIfEmpty(vo.get외국정부등근무자_전액(),"0"));
	            	vo.setR10(StringUtils.defaultIfEmpty(vo.get근로장학금_전액(),"0"));
	            	vo.setH14(StringUtils.defaultIfEmpty(vo.get보육교사_근무환경개선비_전액(),"0"));
	            	vo.setH15(StringUtils.defaultIfEmpty(vo.get사립유치원수석교사_교사의_인건비_전액(),"0"));
	            	vo.setT10(StringUtils.defaultIfEmpty(vo.get중소기업취업청년_소득세_감면100퍼센트_소득세의_100퍼센트(),"0"));
	            	vo.setT11(StringUtils.defaultIfEmpty(vo.get중소기업취업청년_소득세_감면50퍼센트_소득세의_50퍼센트(),"0"));
	            	vo.setT12(StringUtils.defaultIfEmpty(vo.get중소기업취업청년_소득세_감면70퍼센트_소득세의_70퍼센트(),"0"));
	            	vo.setT13(StringUtils.defaultIfEmpty(vo.get중소기업취업청년_소득세_감면90퍼센트_소득세의_90퍼센트(),"0"));	            		            	
	            	vo.setT20(StringUtils.defaultIfEmpty(vo.get조세조약상_교직자감면_전액(),"0"));
	            	vo.setH16(StringUtils.defaultIfEmpty(vo.get정부_공공기관지방이전기관_종사자_이주수당_월_20만원(),"0"));
	            	vo.setR11(StringUtils.defaultIfEmpty(vo.get직무발명보상금_년_300만원(),"0"));
	            	vo.setH06(StringUtils.defaultIfEmpty(vo.get유아_초중등_연구보조비_월_20만원(),"0"));
	            	vo.setH07(StringUtils.defaultIfEmpty(vo.get고등교육법_연구보조비_월_20만원(),"0"));
	            	vo.setY22(StringUtils.defaultIfEmpty(vo.get전공의_수련_보조_수당(),"0"));
	            	vo.setH17(StringUtils.defaultIfEmpty(vo.get종교활동비(),"0"));
	            	vo.setU01(StringUtils.defaultIfEmpty(vo.get벤처기업_주식매수선택권_년_2000만원(),"0"));
	                
	            	
	                vo.setBizId(bizId);
	                vo.set계약ID(request.계약ID);	                
	                vo.set사용자ID(resultEmpVO.getUserId());
	                vo.set근무시기(request.근무시기);
	                vo.set개인식별번호(vo.get개인식별번호().replace("-", ""));

                    int 나이 = 0;
                    
                    switch (vo.get개인식별번호().substring(6, 7)) {
                        case "1":
                        case "2":
                        case "5":
                        case "6":
                        	나이 = 계약년도 - Integer.parseInt("19" + vo.get개인식별번호().substring(0, 2));
                            break;
                        case "3":
                        case "4":
                        case "7":
                        case "8":
                        	나이 = 계약년도 - Integer.parseInt("20" + vo.get개인식별번호().substring(0, 2));
                            break;
                        case "9":
                        case "0":
                        	나이 = 계약년도 - Integer.parseInt("18" + vo.get개인식별번호().substring(0, 2));
                            break;
                    }

                    vo.set나이(String.valueOf(나이));
                    
	
	                vo.setJoinDate(vo.getJoinDate().replaceAll("-", ""));
	                vo.setJoinDate(vo.getJoinDate().replaceAll("/", ""));
	                vo.setJoinDate(vo.getJoinDate().replaceAll("\\.", ""));
	
	                if (!TextUtils.isEmpty(vo.getLeaveDate())) {
	                    vo.setLeaveDate(vo.getLeaveDate().replaceAll("-", ""));
	                    vo.setLeaveDate(vo.getLeaveDate().replaceAll("/", ""));
	                    vo.setLeaveDate(vo.getLeaveDate().replaceAll("\\.", ""));
	                }
	
	                if (!TextUtils.isEmpty(vo.get사내내선번호())) {
	                    vo.set사내내선번호(vo.get사내내선번호().replaceAll("-", ""));
	                }
	
	                if (!TextUtils.isEmpty(vo.getPhoneNum())) {
	                    vo.setPhoneNum(vo.getPhoneNum().replaceAll("-", ""));
	                }
	
	                if (!TextUtils.isEmpty(vo.get감면기간_FROM())) {
	                    vo.set감면기간_FROM(vo.get감면기간_FROM().replaceAll("-", ""));
	                    vo.set감면기간_FROM(vo.get감면기간_FROM().replaceAll("/", ""));
	                    vo.set감면기간_FROM(vo.get감면기간_FROM().replaceAll("\\.", ""));
	                }
	
	                if (!TextUtils.isEmpty(vo.get감면기간_TO())) {
	                    vo.set감면기간_TO(vo.get감면기간_TO().replaceAll("-", ""));
	                    vo.set감면기간_TO(vo.get감면기간_TO().replaceAll("/", ""));
	                    vo.set감면기간_TO(vo.get감면기간_TO().replaceAll("\\.", ""));
	                }
	
//	                if (!"1".equals(vo.get부서표시여부())) {
//	                    vo.set부서표시여부("2");
//	                }
	                if (StringUtils.isEmpty(vo.get부서표시여부())) {
	                    vo.set부서표시여부("1");
	                }
	                if (!"1".equals(vo.get내외국인구분())) {
	                    vo.set내외국인구분("2");
	                }
	                if (!"1".equals(vo.get거주구분())) {
	                    vo.set거주구분("2");
	                }
	                if (!"1".equals(vo.get외국인단일세율적용())) {
	                    vo.set외국인단일세율적용("2");
	                }
	                if (!"1".equals(vo.get국외근로제공여부()) && !"2".equals(vo.get국외근로제공여부()) && !"3".equals(vo.get국외근로제공여부())) {
	                    vo.set국외근로제공여부("4");
	                }
	                if (!"1".equals(vo.get중소기업취업감면여부())) {
	                    vo.set중소기업취업감면여부("2");
	                }
	                if (!"1".equals(vo.get생산직등야간근로비과세())) {
	                    vo.set생산직등야간근로비과세("2");
	                }
	                if (!"1".equals(vo.get세대주여부())) {
	                    vo.set세대주여부("2");
	                }
	                if (!"1".equals(vo.get연말정산분납여부())) {
	                    vo.set연말정산분납여부("2");
	                }
//	                if (!"1".equals(vo.get종전근무지_납세조합유무())) {
//	                    vo.set종전근무지_납세조합유무("2");
//	                }
	                //무조건 '1' 로 세팅  - 20190118
	                vo.set종전근무지_납세조합유무("1");
	
	                resultMap = yp000Service.saveYP000(vo);
	                
	                if(resultMap != null){	                
	                	if(StringUtils.equals((String)resultMap.get("result"), "1")){
	                		dataCnt++;
	                		
	                		Map<String,String> empMap = new HashMap<String,String>();
	                		empMap.put("계약년도", String.valueOf(계약년도));
	                		empMap.put("계약ID", vo.get계약ID());
	                		empMap.put("사용자ID", vo.get사용자ID());
	                		empMap.put("근무시기", vo.get근무시기());
	                		empList.add(empMap);

	                	}else if(StringUtils.equals((String)resultMap.get("result"), "500")){
	                		throw new AngullarProcessException(StringUtils.defaultIfEmpty((String)resultMap.get("message"), "근로자 등록중 오류가 발생했습니다."));
	                	}
	                }
	            }
	        }
	        
	        resultMap.put("empList", empList);
	        resultMap.put("result", "1");
	        resultMap.put("message", "[ " + dataCnt + " ] 건이 처리되었습니다.");
	        
    	}catch(AngullarProcessException ex){
    		logger.error(ex.getMessage());
    		
    		resultMap.put("result", "0");
    		resultMap.put("message", ex.getMessage());
    		
    	}
        
        return resultMap;
    }
        
}
