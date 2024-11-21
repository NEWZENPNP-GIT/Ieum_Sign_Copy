package com.ezsign.feb.special.dao;

import com.ezsign.feb.special.vo.YE408VO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ye408DAO")
public class YE408DAO extends EgovAbstractDAO {

    // 연말정산_이월기부금 조회
    @SuppressWarnings("unchecked")
    public List<YE408VO> getYE408List(YE408VO vo) {
        return (List<YE408VO>) list("ye408DAO.getYE408List", vo);
    }

    // 연말정산_이월기부금 갯수
    public YE408VO getYE408ListCount(YE408VO vo) {
        return (YE408VO) select("ye408DAO.getYE408ListCount", vo);
    }

    // 연말정산_이월기부금 갯수
    public int getYE408Count(YE408VO vo) {
        return (Integer) select("ye408DAO.getYE408Count", vo);
    }

    // 연말정산_이월기부금 입력
    public void insYE408Data(YE408VO vo) {
    	System.out.println(">>>> insYE408Data 일련번호"+vo.get일련번호());
    	System.out.println(">>>> insYE408Data 기부년도"+vo.get기부년도());
    	System.out.println(">>>> insYE408Data 공제대상계산기부금"+vo.get공제대상계산기부금());
    	System.out.println(">>>> insYE408Data 해당연도공제금액"+vo.get해당연도공제금액());
        insert("ye408DAO.insYE408Data", vo);
    }

    // 연말정산_이월기부금 입력
    public void insYE408(YE408VO vo) {
        insert("ye408DAO.insYE408", vo);
    }

    // 연말정산_이월기부금 수정
    public int updYE408(YE408VO vo) {
        return update("ye408DAO.updYE408", vo);
    }

    // 연말정산_이월기부금 삭제
//    public int delYE408(YE408VO vo) {
//        return delete("ye408DAO.delYE408", vo);
//    }

    // 연말정산_이월기부금 사용여부 '0'
    public int updYE408Disable(YE408VO vo) {
    	System.out.println(">>>> 일련번호"+vo.get일련번호());
    	System.out.println(">>>> 기부년도"+vo.get기부년도());
        return update("ye408DAO.updYE408Disable", vo);
    }

    // 연말정산_이월기부금 전체삭제
    public int allDelYE408(YE408VO vo) {
        return delete("ye408DAO.allDelYE408", vo);
    }
    

    // 연말정산_이월기부금 조회    // 연말정산_기부금조정명세
    @SuppressWarnings("unchecked")
    public List<YE408VO> getinsYE408(YE408VO vo) {
        return (List<YE408VO>) list("ye408DAO.getinsYE408", vo);
    }

    // 연말정산_기부금조정명세
    public void insYE408Sum(YE408VO vo) {
        insert("ye408DAO.insYE408Sum", vo);
    }

    // 연말정산_이월기부금 Map조회
    @SuppressWarnings("unchecked")
    public List<YE408VO> getYE408Map(YE408VO vo) {
        return (List<YE408VO>) list("ye408DAO.getYE408Map", vo);
    }
}
