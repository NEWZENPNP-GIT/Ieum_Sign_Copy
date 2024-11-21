package com.ezsign.popup.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.popup.vo.ExcelUploadVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("excelUploadDAO")
public class ExcelUploadDAO extends EgovAbstractDAO {

    // 엑셀 컬럼 매핑 조회
    @SuppressWarnings("unchecked")
    public List<ExcelUploadVO> getExcelMappingList(ExcelUploadVO vo) {
        return (List<ExcelUploadVO>) list("excelUploadDAO.getExcelMappingList", vo);
    }

    // 저장된 매핑정보
    @SuppressWarnings("unchecked")
    public List<ExcelUploadVO> getExcelMapMasterList(ExcelUploadVO vo) {
        return (List<ExcelUploadVO>) list("excelUploadDAO.getExcelMapMasterList", vo);
    }

    // 저장된 매팅 세부정보
    @SuppressWarnings("unchecked")
    public List<ExcelUploadVO> getExcelMapDetailList(ExcelUploadVO vo) {
        return (List<ExcelUploadVO>) list("excelUploadDAO.getExcelMapDetailList", vo);
    }
    
    public void insertExcelDefault(ExcelUploadVO vo) {
        insert("excelUploadDAO.insertExcelDefault", vo);
    }
    
    // 데이타 체크
    public int selectExcelMappingCnt(ExcelUploadVO vo) {
    	return (Integer)select("excelUploadDAO.selectExcelMappingCnt",vo);
    }
    
    // 엑셀 컬럼 매핑 등록
    public void insertExcelMap(ExcelUploadVO vo) {
        insert("excelUploadDAO.insertExcelMap", vo);
    }
    
    // 엑셀 컬럼 매핑 수정
    public void insertExcelMapDetail(ExcelUploadVO vo) {
        insert("excelUploadDAO.insertExcelMapDetail", vo);
    }
        
    public int deleteExcelMap(ExcelUploadVO vo){
    	return delete("excelUploadDAO.deleteExcelMap", vo);
    }
    
    public int deleteExcelMapDetail(ExcelUploadVO vo){
    	return delete("excelUploadDAO.deleteExcelMapDetail", vo);
    }
    
}
