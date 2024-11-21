package com.ezsign.link.service;

import com.ezsign.biz.vo.BizVO;
import com.ezsign.framework.vo.FileVO;

public interface LinkService {

	public static final String NewzenXML = "170101121212001";	// 케이렙 XML 파일 보관용 분류쳬계
	
	public boolean setLinkXML(BizVO bizNoVO, FileVO vo) throws Exception;

	public int checkService(BizVO vo);

}
