package com.ezsign.popup.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ezsign.emp.vo.EmpVO;
import com.ezsign.popup.vo.ExcelUploadRequest;
import com.ezsign.popup.vo.ExcelUploadVO;

public interface ExcelUploadService {

	public List<ExcelUploadVO> getExcelMappingList(ExcelUploadVO excelMappingVO);

	public List<ExcelUploadVO> getExcelMappingContractList(ExcelUploadVO excelMappingVO);

	public List<ExcelUploadVO> getExcelMapMasterList(ExcelUploadVO excelMappingVO);

	public List<ExcelUploadVO> getExcelMapDetailList(ExcelUploadVO excelMappingVO);
	
	public int selectExcelMappingCnt(ExcelUploadVO excelMappingVO) throws Exception;
	
	/**
	 * 엑셀 맵핑 정보를 등록한다.
	 * 
	 * @param excelMappingVO
	 */
	public void insertExcelMap(List<ExcelUploadVO> excelMappingList) throws Exception;

	/**
	 * 엑셀 맵핑 정보를 삭제한다.
	 * 
	 * @param excelMappingVO
	 */
	public void deleteExcelMap(ExcelUploadVO excelMappingVO) throws Exception;
    
    /**
     *
     * 근로자정보  엑셀파일 등록
     *
     * @param bizId
     * @param request
     * @throws Exception
     */
    public Map<String,String> saveExcelType1(String bizId, ExcelUploadRequest<EmpVO> request) throws Exception;

    /**
     *
     * 계약조건  엑셀파일 등록
     *
     * @param bizId
     * @param request
     * @throws Exception
     */
    public Map<String,String> saveExcelTypeC(String bizId, String fileId, List<HashMap<String, String>> request) throws Exception;
    
}
