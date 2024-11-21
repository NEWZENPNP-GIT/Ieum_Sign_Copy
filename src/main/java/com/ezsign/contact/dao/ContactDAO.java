package com.ezsign.contact.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ezsign.contact.vo.ContactGrpVO;
import com.ezsign.contact.vo.ContactVO;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;


@Repository("contactDAO")
public class ContactDAO extends EgovAbstractDAO {

	// 주소록 중복 체크
	public int checkContactDuplicate(ContactVO vo) {
		return (Integer) selectByPk("contactDAO.checkContactDuplicate", vo);
	}
	// 주소록 조회
	public List<ContactVO> getContactList(ContactVO vo) {
		return (List<ContactVO>)list("contactDAO.getContactList", vo);
	}
	
	public int getContactListCount(ContactVO vo) {
		return (Integer)selectByPk("contactDAO.getContactListCount", vo);
	}

	// 주소록(인사/거래처) 등록
	public void insContact(ContactVO vo) {
		insert("contactDAO.insContact", vo);
	}

	// 주소록(인사/거래처) 수정
	public int updContact(ContactVO vo) {
		return update("contactDAO.updContact", vo);
	}
	
	public int delContact(ContactVO vo) {
		return update("contactDAO.delContact", vo);
	}
	
	@SuppressWarnings("unchecked")
	public List<ContactGrpVO> getContactGrpList(ContactGrpVO vo) {
		return (List<ContactGrpVO>)list("contactDAO.getContactGrpList", vo);
	}
	
	public void insContactGrp(ContactGrpVO vo) {
		insert("contactDAO.insContactGrp", vo);
	}

	public void insContactGrpById(ContactGrpVO vo) {
		insert("contactDAO.insContactGrpById", vo);
	}
	
	public int updContactGrp(ContactGrpVO vo) {
		return update("contactDAO.updContactGrp", vo);
	}
	
	public int delContactGrp(ContactGrpVO vo) {
		return update("contactDAO.delContactGrp", vo);
	}
	
}
