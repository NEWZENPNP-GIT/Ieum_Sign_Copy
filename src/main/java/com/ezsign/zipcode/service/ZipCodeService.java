package com.ezsign.zipcode.service;

import com.ezsign.zipcode.vo.ZipCodeVO;

public interface ZipCodeService {

	public ZipCodeVO getZipCode(ZipCodeVO vo) throws Exception;

	public void insZipCode(ZipCodeVO vo) throws Exception;

}
