package com.ezsign.window.service;

import java.util.List;
import java.util.Map;

import com.ezsign.feb.house.vo.YE101VO;
import com.ezsign.feb.house.vo.YE104VO;
import com.ezsign.feb.house.vo.YE105VO;
import com.ezsign.feb.house.vo.YE106VO;
import com.ezsign.feb.house.vo.YE107VO;
import com.ezsign.feb.house.vo.YE108VO;
import com.ezsign.feb.house.vo.YE109VO;
import com.ezsign.feb.other.vo.YE201VO;
import com.ezsign.feb.pension.vo.YE301VO;
import com.ezsign.feb.pension.vo.YE302VO;
import com.ezsign.feb.special.vo.YE401VO;
import com.ezsign.feb.special.vo.YE402VO;
import com.ezsign.feb.special.vo.YE403VO;
import com.ezsign.feb.special.vo.YE404VO;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.feb.worker.vo.YE051VO;
import com.ezsign.feb.worker.vo.YE052VO;
import com.ezsign.window.vo.YWPdfUploadResponse.ManItem;

@SuppressWarnings("NonAsciiCharacters")
public interface YWPdfService {

    /**
     *
     * 연말정산 간소화 PDF 데이타 저장
     *
     * @param 계약ID
     * @param 사용자ID
     * @param 작업자ID
     * @param ye051
     * @param listYE052
     * @param listYE101
     * @param listYE104
     * @param listYE106
     * @param listYE107
     * @param listYE108
     * @param listYE109
     * @param ye201
     * @param listYE301
     * @param listYE302
     * @param listYE401
     * @param listYE402
     * @param listYE403
     * @param listYE404
     * @param 계약년도
     */
    public void saveYWPdf(
            String 계약ID, String 사용자ID, String 작업자ID, YE051VO ye051, List<YE052VO> listYE052,
            List<YE101VO> listYE101, List<YE104VO> listYE104, List<YE106VO> listYE106, List<YE107VO> listYE107,
            List<YE108VO> listYE108, List<YE109VO> listYE109, YE201VO ye201, List<YE301VO> listYE301,
            List<YE302VO> listYE302, List<YE401VO> listYE401, List<YE402VO> listYE402, List<YE403VO> listYE403,
            List<YE404VO> listYE404 , String 계약년도) throws Exception;
    
    
    /**
     *
     * 편리한 연말정산 PDF 데이타 저장
     *
     * @param 계약ID
     * @param 사용자ID
     * @param 작업자ID
     * @param 계약년도
     * @param listManItem
     */
    public void saveYWPdf2(String 계약ID, String 사용자ID, String 작업자ID,String 계약년도, List<ManItem> result, List<YE108VO> listYE108, List<YE001VO> listYE001,
    		List<YE402VO> listYE402, List<YE401VO> listYE401,
    		List<YE106VO> listYE106, List<YE107VO> listYE107, List<YE109VO> listYE109, List<YE301VO> listYE301, List<YE302VO> listYE302,
    		List<YE404VO> listYE404, List<YE101VO> listYE101, List<YE104VO> listYE104, List<YE403VO> listYE403, List<YE105VO> listYE105) throws Exception;
    
    
    /**
     *
     * 부양가족 정보를 체크해서 등록해준다.
     *
     * @param 계약ID
     * @param 사용자ID
     * @param 작업자ID
     * @param 계약년도
     * @param listYE001
     * @throws Exception
     */
    public void saveYE001(String 계약ID, String 사용자ID, List<YE001VO> listYE001, List<YE001VO> 부양가족) throws Exception; 
    
    
    /**
     *
     * 연말정산 데이타 삭제
     *
     * @param 계약ID
     * @param 사용자ID
     * @throws Exception
     */
    public Map<String,String> deleteData(String 계약ID, String 사용자ID, String actUrl) throws Exception;
    
}
