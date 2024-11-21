package com.ezsign.feb.master.dao;

import com.ezsign.feb.master.vo.ExcelMappingVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("excelMappingDAO")
public class ExcelMappingDAO extends EgovAbstractDAO {

    // 엑셀 컬럼 매핑 조회
    @SuppressWarnings("unchecked")
    public List<ExcelMappingVO> getExcelMappingList(ExcelMappingVO vo) {
        return (List<ExcelMappingVO>) list("excelMappingDAO.getExcelMappingList", vo);
    }

    // 엑셀 컬럼 매핑 수정
    public void insExcelMapping(ExcelMappingVO vo) {
        insert("excelMappingDAO.insExcelMapping", vo);
    }
    
    // 데이타 체크
    public int selectExcelMappingCnt(ExcelMappingVO vo) {
    	return (Integer)select("excelMappingDAO.selectExcelMappingCnt",vo);
    }
    
    // 엑셀 컬럼 매핑 등록
    public void insertExcelMapping(ExcelMappingVO vo) {
        insert("excelMappingDAO.insertExcelMapping", vo);
    }
    
    // 엑셀 컬럼 매핑 수정
    public void updateExcelMapping(ExcelMappingVO vo) {
        insert("excelMappingDAO.updateExcelMapping", vo);
    }
    
}
