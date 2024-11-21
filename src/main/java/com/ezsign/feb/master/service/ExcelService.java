package com.ezsign.feb.master.service;

import java.util.List;
import java.util.Map;

import com.ezsign.feb.master.vo.ExcelDataRequest;
import com.ezsign.feb.master.vo.ExcelMappingVO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YP000VO;
import com.ezsign.feb.special.vo.YE404VO;
import com.ezsign.feb.special.vo.YE405VO;
import com.ezsign.feb.system.vo.YS031VO;
import com.ezsign.feb.worker.vo.YE001VO;
import com.ezsign.feb.worker.vo.YE013VO;
import com.ezsign.framework.vo.SessionVO;

public interface ExcelService {

	public List<ExcelMappingVO> getExcelMappingList(ExcelMappingVO excelMappingVO);

    public List<ExcelMappingVO> insExcelMapping(ExcelMappingVO excelMappingVO);

    public int selectExcelMappingCnt(ExcelMappingVO excelMappingVO) throws Exception;
	
	/**
	 * 엑셀 맵핑 정보를 등록한다.
	 * 
	 * @param excelMappingVO
	 */
	public void insertExcelMapping(ExcelMappingVO excelMappingVO) throws Exception;
    
    /**
     * 엑셀 맵핑 정보를 수정한다.
     * 
     * @param excelMappingVO
     */
    public void updateExcelMapping(ExcelMappingVO excelMappingVO) throws Exception;
    
    /**
     *
     * 부서 엑셀 파일 등록
     *
     * @param request
     * @throws Exception
     */
    public Map<String,String> saveExcelType1(ExcelDataRequest<YS031VO> request) throws Exception;

    /**
     *
     * 연말정산 > 기초자료설정 > 근로자정보  엑셀파일 등록
     *
     * @param bizId
     * @param request
     * @throws Exception
     */
    public Map<String,String> saveExcelType2(String bizId, ExcelDataRequest<YE000VO> request) throws Exception;

    /**
     *
     *	연말정산 > 기초자료설정 > 부양가족부양가족  엑셀파일 등록
     *
     * @param bizId
     * @param request
     * @return
     * @throws Exception
     */
    public Map<String,String> saveExcelType3(String bizId, ExcelDataRequest<YE001VO> request) throws Exception;

    /**
     *
     * 연말정산 > 기초자료설정 > 사내기부금 엑셀등록
     *
     * @param bizId
     * @param 작업자ID
     * @param request
     * @return
     * @throws Exception
     */
    public Map<String,String> saveExcelType4(String bizId, String 작업자ID, ExcelDataRequest<YE404VO> request) throws Exception;

    /**
     *
     * 연말정산 > 기초자료설정 > 이월기부금 엑셀등록
     *
     * @param bizId
     * @param 작업자ID
     * @param request
     * @return
     * @throws Exception
     */
    public Map<String,String> saveExcelType5(String bizId, String 작업자ID, ExcelDataRequest<YE405VO> request) throws Exception;

    /**
     *
     * 연말정산 > 기초자료설정 > 공제불가회사지원금 엑셀등록
     *
     * @param bizId
     * @param request
     * @return
     * @throws Exception
     */
    public Map<String,String> saveExcelType6(SessionVO loginVO, ExcelDataRequest<YE013VO> request) throws Exception;

    /**
     *
     * 연말정산 > 기초자료설정 > 종ㆍ전 근무지  엑셀파일 등록
     *
     * @param bizId
     * @param request
     * @throws Exception
     */
    public Map<String,String> saveExcelType7(String bizId, ExcelDataRequest<YE000VO> request) throws Exception;

    /***
     * 
     * 연말정산 > 기초자료설정 > 근로자정보  > 원천징수영수증 관리번호  엑셀파일 등록 
     * 
     * @param bizId
     * @param request
     * @throws Esception
     */
	public Map<String, String> saveExcelType8(String bizId, ExcelDataRequest<YE000VO> request) throws Exception;
	
	/**
	 * 
	 * 간이지급명세서  > 근로자정보  엑셀파일 등록
	 * 
	 * @param bizId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> saveExcelType9(String bizId, ExcelDataRequest<YP000VO> request) throws Exception;
}
