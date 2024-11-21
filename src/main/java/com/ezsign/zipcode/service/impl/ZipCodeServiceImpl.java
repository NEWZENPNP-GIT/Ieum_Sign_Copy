package com.ezsign.zipcode.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.zipcode.dao.ZipCodeDAO;
import com.ezsign.zipcode.service.ZipCodeService;
import com.ezsign.zipcode.vo.ZipCodeVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("zipCodeService")
public class ZipCodeServiceImpl extends AbstractServiceImpl implements ZipCodeService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "zipCodeDAO")
	private ZipCodeDAO zipCodeDAO;

	// 우편번호 조회
	@Override
	public ZipCodeVO getZipCode(ZipCodeVO vo) throws Exception {
		ZipCodeVO result = zipCodeDAO.getZipCode(vo);
		return result;
	}

	// 우편번호 등록
	@Override
	public void insZipCode(ZipCodeVO vo) throws Exception {
		zipCodeDAO.insZipCode(vo);
	}

}
