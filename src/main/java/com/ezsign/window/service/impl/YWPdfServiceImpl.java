package com.ezsign.window.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.feb.etc.dao.YE501DAO;
import com.ezsign.feb.etc.dao.YE502DAO;
import com.ezsign.feb.etc.dao.YE503DAO;
import com.ezsign.feb.etc.vo.YE501VO;
import com.ezsign.feb.etc.vo.YE502VO;
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
import com.ezsign.feb.house.vo.YE101VO;
import com.ezsign.feb.house.vo.YE102VO;
import com.ezsign.feb.house.vo.YE103VO;
import com.ezsign.feb.house.vo.YE104VO;
import com.ezsign.feb.house.vo.YE105VO;
import com.ezsign.feb.house.vo.YE106VO;
import com.ezsign.feb.house.vo.YE107VO;
import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.house.vo.YE109VO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE901VO;
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
import com.ezsign.feb.special.dao.YE401DAO;
import com.ezsign.feb.special.dao.YE402DAO;
import com.ezsign.feb.special.dao.YE403DAO;
import com.ezsign.feb.special.dao.YE404DAO;
import com.ezsign.feb.special.dao.YE405DAO;
import com.ezsign.feb.special.dao.YE406DAO;
import com.ezsign.feb.special.dao.YE408DAO;
import com.ezsign.feb.special.vo.YE401VO;
import com.ezsign.feb.special.vo.YE402VO;
import com.ezsign.feb.special.vo.YE403VO;
import com.ezsign.feb.special.vo.YE404VO;
import com.ezsign.feb.special.vo.YE406VO;
import com.ezsign.feb.special.vo.YE408VO;
import com.ezsign.feb.worker.dao.YE001DAO;
import com.ezsign.feb.worker.dao.YE051DAO;
import com.ezsign.feb.worker.dao.YE052DAO;
import com.ezsign.feb.worker.dao.YE700DAO;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.feb.worker.vo.YE051VO;
import com.ezsign.feb.worker.vo.YE052VO;
import com.ezsign.window.service.YWPdfService;
import com.ezsign.window.vo.YWPdfUploadResponse.ManItem;

@SuppressWarnings("NonAsciiCharacters")
@Service("ywPdfService")
public class YWPdfServiceImpl implements YWPdfService {

    Logger logger = Logger.getLogger(this.getClass());

    @Resource(name = "ye051DAO")
    private YE051DAO ye051DAO;

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

    /* 신용카드 DAO */
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

    @Resource(name = "ye406DAO")
    private YE406DAO ye406DAO;

    @Resource(name = "ye408DAO")
    private YE408DAO ye408DAO;

    @Resource(name = "ye501DAO")
    private YE501DAO ye501DAO;

    @Resource(name = "ye502DAO")
    private YE502DAO ye502DAO;

    @Resource(name = "ye503DAO")
    private YE503DAO ye503DAO;

    @Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;
    
    /* 연말정산 일반세액공제 계산 결과 */
    @Resource(name = "ye700DAO")
    private YE700DAO ye700DAO;
    
    /* 부양가족 DAO */
    @Resource(name = "ye001DAO")
    private YE001DAO ye001DAO;
    
    /* 근로자기본설정 DAO */
    @Resource(name = "ye000DAO")
    private	YE000DAO ye000DAO;
    

    /**
     * <pre>
     * 1. 개요 : 국세청 PDF 파일 DB 저장 처리 
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : saveYWPdf
     * @date : 2019. 1. 18.
     * @author : soybean0627
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2019. 1. 18.				soybean0627					최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @see com.ezsign.window.service.YWPdfService#saveYWPdf(java.lang.String, java.lang.String, java.lang.String, com.ezsign.feb.worker.vo.YE051VO, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, com.ezsign.feb.other.vo.YE201VO, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.util.List, java.lang.String)
     *  @param 계약ID
     *  @param 사용자ID
     *  @param 작업자ID
     *  @param ye051 : 국민연금
     *  @param listYE052 : 건강보험, 장기요양보험
     *  @param listYE101 : 주택임차차입금 원리금상환액
     *  @param listYE104 : 장기주택저당차입금 이자상환액
     *  @param listYE106 : 개인연금저축
     *  @param listYE107 : 주택마련저축
     *  @param listYE108 : 신용카드,직불카드,현금영수증
     *  @param listYE109 : 장기집합투자증권저축
     *  @param ye201 : 소기업소상공인공제부금
     *  @param listYE301 : 퇴직연금
     *  @param listYE302 : 연금저축
     *  @param listYE401 : 보장성보험
     *  @param listYE402 : 의료비
     *  @param listYE403 : 교육비(유초중고,대학,기타,교복구입비,직업훈련비,학자금대출)
     *  @param listYE404 : 기부금
     *  @param 계약년도
     */
    @Override
    public void saveYWPdf(
            String 계약ID, String 사용자ID, String 작업자ID, YE051VO ye051, List<YE052VO> listYE052,
            List<YE101VO> listYE101, List<YE104VO> listYE104, List<YE106VO> listYE106, List<YE107VO> listYE107,
            List<YE108VO> listYE108, List<YE109VO> listYE109, YE201VO ye201, List<YE301VO> listYE301,
            List<YE302VO> listYE302, List<YE401VO> listYE401, List<YE402VO> listYE402, List<YE403VO> listYE403,
            List<YE404VO> listYE404, String 계약년도) throws Exception{
    	
    	//기존 데이타 삭제
        deleteData(계약ID, 사용자ID, "");

        //작업자ID 정보를 근로자 사용자ID로 셋팅한다. (관리자 등록시에도 근로자가 수정할수 있도록)
                 작업자ID = 사용자ID;
        
        // 국민연금
        if (ye051 != null && "C".equals(ye051.getDbMode())) {
            ye051.set작업자ID(작업자ID);
            ye051DAO.insYE051(ye051);
        }

        // 특별소득공제 보험료 (건강,장기요양,고용보험료)
        if (listYE052 != null) {
            for (YE052VO vo : listYE052) {
                vo.set작업자ID(작업자ID);
                ye052DAO.insYE052(vo);
            }
        }
        
        // 신용카드
        if (listYE108 != null) {
            for (YE108VO vo : listYE108) {
                vo.set작업자ID(작업자ID);
                ye108DAO.insYE108(vo);
            }
        }
        
        //의료비 등록
        if (listYE402 != null) {
            for (YE402VO vo : listYE402) {
                vo.set작업자ID(작업자ID);
                ye402DAO.insYE402(vo);
            }
        } 
        
        //보험료 등록 
        if (listYE401 != null) {
            for (YE401VO vo : listYE401) {
                vo.set작업자ID(작업자ID);
                ye401DAO.insYE401(vo);
            }
        }
        
        //개인연금저축
        if (listYE106 != null) {
            for (YE106VO vo : listYE106) {
                vo.set작업자ID(작업자ID);
                ye106DAO.insYE106(vo);
            }
        }
        
        //주택마련저축
        if (listYE107 != null) {
            for (YE107VO vo : listYE107) {
                vo.set작업자ID(작업자ID);
                ye107DAO.insYE107(vo);
            }
        }
        
        //장기집합투자증권저축
        if (listYE109 != null) {
            for (YE109VO vo : listYE109) {
                vo.set작업자ID(작업자ID);
                ye109DAO.insYE109(vo);
            }
        }
        
        //퇴직연금계좌
        if (listYE301 != null) {
            for (YE301VO vo : listYE301) {
                vo.set작업자ID(작업자ID);
                ye301DAO.insYE301(vo);
            }
        }
        
        //연금저축계좌
        if (listYE302 != null) {
            for (YE302VO vo : listYE302) {
                vo.set작업자ID(작업자ID);
                ye302DAO.insYE302(vo);
            }
        }
        
        //소기업소상공인공제부금
        if (ye201 != null && "C".equals(ye201.getDbMode())) {
            ye201.set작업자ID(작업자ID);
            ye201DAO.insYE201(ye201);
        }
        
        //기부금
        if (listYE404 != null) {
            for (YE404VO vo : listYE404) {
                vo.set작업자ID(작업자ID);
                ye404DAO.insYE404(vo);
            }

            //기부금조정명세 계산결과 
            if (listYE404.size() > 0) {
                YE408VO ye408 = new YE408VO();
                ye408.set계약ID(계약ID);
                ye408.set기부년도(계약년도);
                ye408.set사용자ID(사용자ID);
                ye408.set작업자ID(작업자ID);
                ye408DAO.insYE408(ye408);
            }
        }
        
        //주택임차차입금원리금상환액  
        if (listYE101 != null) {
            for (YE101VO vo : listYE101) {
                vo.set작업자ID(작업자ID);
                ye101DAO.insYE101(vo);
            }
        }

        //장기주택저당차입금이자상환액
        if (listYE104 != null) {
            for (YE104VO vo : listYE104) {
                vo.set작업자ID(작업자ID);
                ye104DAO.insYE104(vo);
            }
        }

        //교육비
        if (listYE403 != null) {
            for (YE403VO vo : listYE403) {
                vo.set작업자ID(작업자ID);
                ye403DAO.insYE403(vo);
            }
        }

        //사용자별 진행현황 log
        YE901VO ye901 = new YE901VO();
        ye901.set계약ID(계약ID);
        ye901.set사용자ID(사용자ID);
        ye901.set작업자ID(작업자ID);
        ye901.set진행현황코드("1");
        ye901DAO.insYE901(ye901);
        
    }

    /**
     * <pre>
     * 1. 개요 : 편리한 연말정산 PDF 데이타 저장
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : saveYWPdf2
     * @date : 2019. 1. 19.
     * @author : soybean0627
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2019. 1. 19.				soybean0627					최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @see com.ezsign.window.service.YWPdfService#saveYWPdf2(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List)
     *  @param 계약ID
     *  @param 사용자ID
     *  @param 작업자ID
     *  @param 계약년도
     *  @param listManItem
     *  @param listYE108  : 신용카드 정보
     *  @param listYE001  : 부양가족
     *  @param listYE402  : 의료비
     *  @param listYE106  : 개인연금저축 
     *  @param listYE107  : 주택마련저축 
     *  @param listYE109  : 장기집합투자증권저축 
     *  @param listYE301  : 퇴직연금계좌 
     *  @param listYE302  : 연금저축계좌
     *  @param listYE404  : 기부금
     *  @param listYE101  : 주택임차차입금원리금상환액
     *  @param listYE403  : 교육비
     *  @param listYE105  : 월세세액                
     *   
     */
    public void saveYWPdf2(
            String 계약ID, String 사용자ID, String 작업자ID, String 계약년도, List<ManItem> listManItem,List<YE108VO> listYE108, List<YE001VO> listYE001,
            List<YE402VO> listYE402, List<YE401VO> listYE401,
            List<YE106VO> listYE106, List<YE107VO> listYE107, List<YE109VO> listYE109, List<YE301VO> listYE301, List<YE302VO> listYE302,
            List<YE404VO> listYE404, List<YE101VO> listYE101, List<YE104VO> listYE104, List<YE403VO> listYE403, List<YE105VO> listYE105) throws Exception{
    	
    	//기존 데이타 삭제
    	deleteData(계약ID, 사용자ID, "");

    	//작업자ID 정보를 근로자 사용자ID로 셋팅한다. (관리자 등록시에도 근로자가 수정할수 있도록)
                 작업자ID = 사용자ID;
        
        
    	List<YE052VO> listYE052 = new ArrayList<>();   		//특별소득공제 보험료
        YE051VO ye051VO = null;				  				//연금보험료(국민/공적)
        YE201VO ye201 = null;

    	//국민연금 VO 생성
        if(listManItem != null && listManItem.size() > 0){   
        	        	
        	if((listManItem.get(1).국민연금_직장 + listManItem.get(1).국민연금_지역) > 0){
        		ye051VO = new YE051VO();
        	
        		ye051VO.setDbMode("C");
        		ye051VO.set계약ID(계약ID);
        		ye051VO.set사용자ID(사용자ID);
        		ye051VO.set보험료구분("1");
        		ye051VO.set국세청_납입금액(listManItem.get(1).국민연금_직장 + listManItem.get(1).국민연금_지역);
        		ye051VO.set국세청_차감금액(0);
        		ye051VO.set추가납입금액(0);
        		
        		//국민연금보험료
        		ye051VO.set작업자ID(작업자ID);
                ye051DAO.insYE051(ye051VO);
        	}
            
        	if((listManItem.get(1).국민연금외_공적연금) > 0){
        		ye051VO = new YE051VO();
        	
        		ye051VO.setDbMode("C");
        		ye051VO.set계약ID(계약ID);
        		ye051VO.set사용자ID(사용자ID);
        		ye051VO.set보험료구분("4");
        		ye051VO.set국세청_납입금액(listManItem.get(1).국민연금외_공적연금);
        		ye051VO.set국세청_차감금액(0);
        		ye051VO.set추가납입금액(0);
        		
        		//국민연금보험료 외 공적연금
        		ye051VO.set작업자ID(작업자ID);
                ye051DAO.insYE051(ye051VO);
        	}
            
            
        }
        
        //국민건강보험 VO 생성
        if(listManItem != null && listManItem.size() > 0){                    	   

     	    YE052VO vo = new YE052VO();
            vo.setDbMode("C");
            vo.set계약ID(계약ID);
            vo.set사용자ID(사용자ID);
            vo.set보험료구분("1");
            vo.set국세청_납입금액(listManItem.get(1).건강보험_보수월액 + listManItem.get(1).건강보험_소득월액);
            vo.set국세청_차감금액(0);
            vo.set추가납입금액(0);
            
            listYE052.add(vo);                           
        }

        //소기업소상공인공제부금
        if(listManItem != null && listManItem.size() > 0){                    	   
        	ye201 = new YE201VO();	

        	ye201.setDbMode("C");
        	ye201.set계약ID(계약ID);
        	ye201.set사용자ID(사용자ID);
        	ye201.set국세청_납입금액(listManItem.get(1).소기업소상공인공제부금);
            ye201.set국세청_차감금액(0);
            ye201.set기타_납입금액(0);                           
        }
        
        /********************************************************************/
        
        
//    	//국민연금
//    	if (ye051VO != null && "C".equals(ye051VO.getDbMode())) {
//    		ye051VO.set작업자ID(작업자ID);
//            ye051DAO.insYE051(ye051VO);
//        }
    	
    	/* 특별소득공제 보험료 (건강,장기요양,고용보험료) */
        if (listYE052 != null && listYE052.size() > 0) {
            for (YE052VO vo : listYE052) {
                vo.set작업자ID(작업자ID);
                ye052DAO.insYE052(vo);
            }
        }
            	
    	//신용카드 등록
    	if (listYE108 != null) {
            for (YE108VO vo : listYE108) {            	
                vo.set작업자ID(작업자ID);
                ye108DAO.insYE108(vo);
            }
        }
        //신용카드 등록
        /*if(listManItem != null && listManItem.size() > 0){
        	
        	for(int i = 1 ; i < listManItem.size() ; i++){
        		
        		String 개인식별번호 = SecurityUtil.getinstance().aesEncode(listManItem.get(i).개인식별번호);
        		
        		YE001VO vo = new YE001VO();
                vo.set계약ID(계약ID);
                vo.set사용자ID(사용자ID);
                vo.set개인식별번호(개인식별번호);

                YE001VO ye001VO = ye001DAO.getYE001ID(vo);			        
        		
                if(ye001VO != null){
                	
            		YE108VO ye108VO = new YE108VO();				
    				
    	            ye108VO.setDbMode("C");
    	            ye108VO.set계약ID(계약ID);
    	            ye108VO.set사용자ID(ye001VO.get사용자ID());
    	            ye108VO.set부양가족ID(ye001VO.get부양가족ID());
    	            ye108VO.set자료구분코드("0");
    	            ye108VO.set기간구분코드("1");
                	
    	            ye108VO.set신용카드(listManItem.get(i).신용카드_일반);				//신용카드
    	            ye108VO.set직불_선불카드(listManItem.get(i).직불카드_일반);			//직불,선불카드
    	            ye108VO.set현금영수증(listManItem.get(i).현금영수증_일반);			//현금영수증    	            	                   
                   	ye108VO.set전통시장(listManItem.get(i).신용카드_전통시장 + listManItem.get(i).직불카드_전통시장 + listManItem.get(i).현금영수증_전통시장);			//전통시장사용분
                   	ye108VO.set대중교통(listManItem.get(i).신용카드_대중교통 + listManItem.get(i).직불카드_대중교통 + listManItem.get(i).현금영수증_대중교통);			//대중교통이용분
                   	ye108VO.set도서공연(listManItem.get(i).신용카드_도서공연 + listManItem.get(i).직불카드_도서공연 + listManItem.get(i).현금영수증_도서공연);			//도서공연사용분
                   	
                   	ye108VO.set작업자ID(작업자ID);
                   	ye108DAO.insYE108(ye108VO);
                   	
                }
        		
        	}
        	
        }*/
        
        
    	
    	 //의료비 등록 
    	 if (listYE402 != null) {
             for (YE402VO vo : listYE402) {
                 vo.set작업자ID(작업자ID);
                 ye402DAO.insYE402(vo);
             }
         }
    	 
    	 //보험료 등록 
    	 if (listYE401 != null) {
             for (YE401VO vo : listYE401) {
                 vo.set작업자ID(작업자ID);
                 ye401DAO.insYE401(vo);
             }
         } 
    	 
    	 //개인연금저축
         if (listYE106 != null) {
             for (YE106VO vo : listYE106) {
                 vo.set작업자ID(작업자ID);
                 ye106DAO.insYE106(vo);
             }
         }
    	 
         //주택마련저축
         if (listYE107 != null) {
             for (YE107VO vo : listYE107) {
                 vo.set작업자ID(작업자ID);
                 ye107DAO.insYE107(vo);
             }
         }
    	          
         //장기집합투자증권저축
         if (listYE109 != null) {
             for (YE109VO vo : listYE109) {
                 vo.set작업자ID(작업자ID);
                 ye109DAO.insYE109(vo);
             }
         }
         
         //퇴직연금계좌
         if (listYE301 != null) {
             for (YE301VO vo : listYE301) {
                 vo.set작업자ID(작업자ID);
                 ye301DAO.insYE301(vo);
             }
         }
         
         //연금저축계좌
         if (listYE302 != null) {
             for (YE302VO vo : listYE302) {
                 vo.set작업자ID(작업자ID);
                 ye302DAO.insYE302(vo);
             }
         }
         
         //소기업소상공인공제부금
         if (ye201 != null && "C".equals(ye201.getDbMode())) {
             ye201.set작업자ID(작업자ID);
             ye201DAO.insYE201(ye201);
         }
         
         //기부금
         if (listYE404 != null) {
             for (YE404VO vo : listYE404) {
                 vo.set작업자ID(작업자ID);
                 ye404DAO.insYE404(vo);
             }

             //기부금조정명세 계산결과
             if (listYE404.size() > 0) {
                 YE408VO ye408 = new YE408VO();
                 ye408.set계약ID(계약ID);
                 ye408.set기부년도(계약년도);
                 ye408.set사용자ID(사용자ID);
                 ye408.set작업자ID(작업자ID);
                 ye408DAO.insYE408(ye408);
             }
         }
         
         //주택임차차입금원리금상환액  
         if (listYE101 != null) {
             for (YE101VO vo : listYE101) {
                 vo.set작업자ID(작업자ID);
                 ye101DAO.insYE101(vo);
             }
         }
         
         //장기주택저당차입금이자상환액
         if (listYE104 != null) {
             for (YE104VO vo : listYE104) {
                 vo.set작업자ID(작업자ID);
                 ye104DAO.insYE104(vo);
             }
         }
         
         //교육비
         if (listYE403 != null) {
             for (YE403VO vo : listYE403) {
                 vo.set작업자ID(작업자ID);
                 ye403DAO.insYE403(vo);
             }
         }
         
         //월세세액 
         if (listYE105 != null) {
             for (YE105VO vo : listYE105) {
                 vo.set작업자ID(작업자ID);
                 ye105DAO.insYE105(vo);
             }
         }
         
         
    	 //사용자별 진행현황 log
    	 YE901VO ye901 = new YE901VO();
         ye901.set계약ID(계약ID);
         ye901.set사용자ID(사용자ID);
         ye901.set작업자ID(작업자ID);
         ye901.set진행현황코드("1");
         ye901DAO.insYE901(ye901);
         
    }
    
    
    /**
     * <pre>
     * 1. 개요 : 연말 정산 정보를 삭제한다.
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : deleteData
     * @date : 2019. 1. 23.
     * @author : soybean0627
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2019. 1. 23.				soybean0627					최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @see com.ezsign.window.service.YWPdfService#deleteData(java.lang.String, java.lang.String, java.lang.String)
     *  @param 계약ID
     *  @param 사용자ID
     *  @param actUrl : 호출 URL 정보
     *  @throws Exception
     */
    public Map<String,String> deleteData(String 계약ID, String 사용자ID, String actUrl) throws Exception{
    	
    	boolean 실행여부 = false;
    	Map<String,String> resultMap = new HashMap<String,String>();
    	resultMap.put("result", "");
    	resultMap.put("resultMessage", "");
    	
    	
    	YE000VO ye000VO = new YE000VO();
		ye000VO.set계약ID(계약ID);
		ye000VO.set사용자ID(사용자ID);
		
		ye000VO = ye000DAO.getYE000DataDetail(ye000VO);
		
		if(ye000VO != null){
			
			if(StringUtils.equals(ye000VO.get진행상태코드(), "5")){
				실행여부 = false;
				
				resultMap.put("result", "fail");
		    	resultMap.put("resultMessage", "최종 확정상태입니다.");
			}else{
				실행여부 = true;
			}
			
		}    		    		
		
    	/*if(StringUtils.isEmpty(actUrl)){
    		실행여부 = true;
    	}
    	//관리자 선택 삭제
    	else if(StringUtils.equals(actUrl,"ywDelete.do")){
    		
    		YE000VO ye000VO = new YE000VO();
    		ye000VO.set계약ID(계약ID);
    		ye000VO.set사용자ID(사용자ID);
    		
    		ye000VO = ye000DAO.getYE000DataDetail(ye000VO);
    		
    		if(ye000VO != null){
    			
    			if(StringUtils.equals(ye000VO.get진행상태코드(), "5")){
    				실행여부 = false;
    				
    				resultMap.put("result", "fail");
    		    	resultMap.put("resultMessage", "최종 확정상태입니다.");
    			}else{
    				실행여부 = true;
    			}
    			
    		}    		    		
    	}*/ 
    	
    	if(실행여부){
    		
	        YE051VO ye051del = new YE051VO();
	        ye051del.set계약ID(계약ID);
	        ye051del.set사용자ID(사용자ID);
	        ye051DAO.allDelYE051(ye051del);
	
	        YE052VO ye052del = new YE052VO();
	        ye052del.set계약ID(계약ID);
	        ye052del.set사용자ID(사용자ID);
	        ye052DAO.allDelYE052(ye052del);
	
	        YE101VO ye101del = new YE101VO();
	        ye101del.set계약ID(계약ID);
	        ye101del.set사용자ID(사용자ID);	        
	        ye101DAO.allDelYE101(ye101del);
	
	        YE102VO ye102del = new YE102VO();
	        ye102del.set계약ID(계약ID);
	        ye102del.set사용자ID(사용자ID);	        
	        ye102DAO.allDelYE102(ye102del);
	
	        YE103VO ye103del = new YE103VO();
	        ye103del.set계약ID(계약ID);
	        ye103del.set사용자ID(사용자ID);	        
	        ye103DAO.allDelYE103(ye103del);
	
	        YE104VO ye104del = new YE104VO();
	        ye104del.set계약ID(계약ID);
	        ye104del.set사용자ID(사용자ID);	        
	        ye104DAO.allDelYE104(ye104del);
	
	        YE105VO ye105del = new YE105VO();
	        ye105del.set계약ID(계약ID);
	        ye105del.set사용자ID(사용자ID);	        
	        ye105DAO.allDelYE105(ye105del);
	
	        YE106VO ye106del = new YE106VO();
	        ye106del.set계약ID(계약ID);
	        ye106del.set사용자ID(사용자ID);
//	        ye106del.set자료구분코드("0");
	        ye106DAO.allDelYE106(ye106del);
	
	        YE107VO ye107del = new YE107VO();
	        ye107del.set계약ID(계약ID);
	        ye107del.set사용자ID(사용자ID);
//	        ye107del.set자료구분코드("0");
	        ye107DAO.allDelYE107(ye107del);
	
	        YE108VO ye108del = new YE108VO();
	        ye108del.set계약ID(계약ID);
	        ye108del.set사용자ID(사용자ID);
//	        ye108del.set자료구분코드("0");
	        ye108DAO.allDelYE108(ye108del);
	
	        YE109VO ye109del = new YE109VO();
	        ye109del.set계약ID(계약ID);
	        ye109del.set사용자ID(사용자ID);
//	        ye109del.set자료구분코드("0");
	        ye109DAO.allDelYE109(ye109del);
	
	        YE201VO ye201del = new YE201VO();
	        ye201del.set계약ID(계약ID);
	        ye201del.set사용자ID(사용자ID);
	        ye201DAO.allDelYE201(ye201del);
	
	        YE202VO ye202del = new YE202VO();
	        ye202del.set계약ID(계약ID);
	        ye202del.set사용자ID(사용자ID);
	        ye202DAO.allDelYE202(ye202del);
	
	        YE203VO ye203del = new YE203VO();
	        ye203del.set계약ID(계약ID);
	        ye203del.set사용자ID(사용자ID);
	        ye203DAO.allDelYE203(ye203del);
	
	        YE204VO ye204del = new YE204VO();
	        ye204del.set계약ID(계약ID);
	        ye204del.set사용자ID(사용자ID);
	        ye204DAO.allDelYE204(ye204del);
	
	        YE301VO ye301del = new YE301VO();
	        ye301del.set계약ID(계약ID);
	        ye301del.set사용자ID(사용자ID);
//	        ye301del.set자료구분코드("0");
	        ye301DAO.allDelYE301(ye301del);
	
	        YE302VO ye302del = new YE302VO();
	        ye302del.set계약ID(계약ID);
	        ye302del.set사용자ID(사용자ID);
//	        ye302del.set자료구분코드("0");
	        ye302DAO.allDelYE302(ye302del);
	
	        YE401VO ye401del = new YE401VO();
	        ye401del.set계약ID(계약ID);
	        ye401del.set사용자ID(사용자ID);
//	        ye401del.set자료구분코드("0");
	        ye401DAO.allDelYE401(ye401del);
	
	        YE402VO ye402del = new YE402VO();
	        ye402del.set계약ID(계약ID);
	        ye402del.set사용자ID(사용자ID);
//	        ye402del.set자료구분코드("0");
	        ye402DAO.allDelYE402(ye402del);
	
	        YE403VO ye403del = new YE403VO();
	        ye403del.set계약ID(계약ID);
	        ye403del.set사용자ID(사용자ID);
//	        ye403del.set자료구분코드("0");
	        ye403DAO.allDelYE403(ye403del);
	
	        YE404VO ye404del = new YE404VO();
	        ye404del.set계약ID(계약ID);
	        ye404del.set사용자ID(사용자ID);
//	        ye404del.set자료구분코드("0");
	        ye404DAO.allDelYE404(ye404del);
	
	        YE406VO ye406del = new YE406VO();
	        ye406del.set계약ID(계약ID);
	        ye406del.set사용자ID(사용자ID);
	        ye406DAO.allDelYE406(ye406del);
	
	        YE408VO ye408del = new YE408VO();
	        ye408del.set계약ID(계약ID);
	        ye408del.set사용자ID(사용자ID);
	        ye408DAO.allDelYE408(ye408del);
	
	        YE501VO ye501del = new YE501VO();
	        ye501del.set계약ID(계약ID);
	        ye501del.set사용자ID(사용자ID);
	        ye501DAO.allDelYE501(ye501del);
	
	        YE502VO ye502del = new YE502VO();
	        ye502del.set계약ID(계약ID);
	        ye502del.set사용자ID(사용자ID);
	        ye502DAO.allDelYE502(ye502del);
	
	        YE503VO ye503del = new YE503VO();
	        ye503del.set계약ID(계약ID);
	        ye503del.set사용자ID(사용자ID);
	        ye503DAO.allDelYE503(ye503del);
	        
	        
	        resultMap.put("result", "success");
	        resultMap.put("resultMessage", "성공했습니다.");
	        
    	}
    		
		if(StringUtils.equals(resultMap.get("result"), "success")){ 
			YE000VO upYe000VO = new YE000VO();
			upYe000VO.set계약ID(계약ID);
			upYe000VO.set사용자ID(사용자ID);
			upYe000VO.set진행상태코드("1");
    		
    		ye000DAO.updYE000(upYe000VO);
		}
    		    	
    	return resultMap;
    }
    
    
    /**
     * <pre>
     * 1. 개요 : 부양가족 정보를 등록해준다.
     * 2. 처리내용 : 
     * </pre>
     *
     * @Method Name : saveYE001
     * @date : 2019. 1. 19.
     * @author : soybean0627
     *
     * @history : 
     *	-----------------------------------------------------------------------
     *	변경일						작성자					변경내용  
     *	-----------------------------------------------------------------------
     *	2019. 1. 19.				soybean0627					최초 작성 
     *	-----------------------------------------------------------------------
     *
     *  @see com.ezsign.window.service.YWPdfService#saveYE001(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List)
     *  @param 계약ID
     *  @param 사용자ID
     *  @param 작업자ID
     *  @param 계약년도
     *  @param listYE001
     *  @throws Exception
     */
    public void saveYE001(String 계약ID, String 사용자ID, List<YE001VO> listYE001, List<YE001VO> 부양가족) throws Exception{
    	
    	//부양가족 정보 수정
    	if(listYE001 != null){
    		
    		YE001VO paramVO = new YE001VO();
    		paramVO.set계약ID(계약ID);
    		paramVO.set사용자ID(사용자ID);
            
    		for(YE001VO ye001VO : listYE001){

    			paramVO.set개인식별번호(ye001VO.get개인식별번호());
    			    			
                //정보가 존재하는지 체크한다.
    			YE001VO resultVO = ye001DAO.getYE001ID(paramVO);
    			
                if(resultVO == null){
                	ye001VO.set계약ID(계약ID);
                	ye001VO.set사용자ID(사용자ID);
            		
                	//부양가족 등록
                	ye001DAO.insYE001(ye001VO);
                	                	                                	
                	//부양가족 등록후 화면에 주민번호를 보여주기위해 다시 변경해준다.
                	ye001VO.set개인식별번호(ye001VO.getResId());                	
                	
                }else{
                	ye001VO.set계약ID(계약ID);
                	ye001VO.set사용자ID(사용자ID);
                	ye001VO.set부양가족ID(resultVO.get부양가족ID()); 
                	
                	//부양가족 수정
                	ye001DAO.updYE001(ye001VO);
                }
                

                //부양가족 정보가 없을시에 list에 담아준다.
                if (부양가족 != null) {
                	
                	//list에 add여부 체크
                	boolean addChk = true;
                	
                    for (YE001VO item : 부양가족) {
                        if (StringUtils.equals(ye001VO.get개인식별번호(), item.get개인식별번호()) || StringUtils.equals(ye001VO.getResId(), item.get개인식별번호())) {
                        	addChk = false;
                        	break;
                        }
                    }
                    
                    if(addChk){
                    	부양가족.add(ye001VO);
                    }
                }
                	
    		}

    	}    	 
    	
    }
    
}
