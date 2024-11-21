package com.ezsign.status.service;

import com.ezsign.status.vo.StatusVO;

public interface StatusService {
	
	int downloadStatusExcel(String filePath, String fileName, StatusVO vo) throws Exception;
}
