package com.ezsign.zipcode.dao;


import org.springframework.stereotype.Repository;
import com.ezsign.zipcode.vo.ZipCodeVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("zipCodeDAO")
public class ZipCodeDAO extends EgovAbstractDAO {

	// 우편번호 조회
	public ZipCodeVO getZipCode(ZipCodeVO vo) {
		return (ZipCodeVO) select("zipCodeDAO.getZipCode", vo);
	}


	// 우편번호 등록
	public void insZipCode(ZipCodeVO vo) {
		insert("zipCodeDAO.insZipCode", vo);
	}
}
