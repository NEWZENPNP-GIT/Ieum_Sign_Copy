package com.ezsign.contact.service;

import java.util.List;

import com.ezsign.contact.vo.ContactCertVO;
import com.ezsign.contact.vo.ContactGrpVO;
import com.ezsign.contact.vo.ContactVO;


public interface ContactService {

	// 주소록 중복 체크
	public int checkContactDuplicate(ContactVO vo) throws  Exception;

	// 주소록 조회
	public List<ContactVO> getContactList(ContactVO vo)  throws Exception;

	public int getContactListCount(ContactVO vo)  throws Exception;

	public void insContact(ContactVO vo) throws Exception;

	public int updContact(ContactVO vo) throws Exception;

	public int delContact(ContactVO vo) throws Exception;

	public int addContactUser(ContactVO vo) throws Exception;


	public List<ContactGrpVO> getContactGrpList(ContactGrpVO vo)  throws Exception;

	public void insContactGrp(ContactGrpVO vo) throws Exception;

	public int updContactGrp(ContactGrpVO vo) throws Exception;

	public int delContactGrp(ContactGrpVO vo) throws Exception;

	// 인사 정보 연동
	public void insContactUser(ContactVO vo);
	
	// 거래처 정보 연동
	public void customContactUser(ContactVO vo);

	public void insContactGrpById(ContactGrpVO grpVO);

	public void delContactUser(ContactVO vo);

	boolean sendCertURL(ContactCertVO vo) throws Exception;

	int checkCert(ContactCertVO vo)throws Exception;

	
}
