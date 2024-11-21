package com.ezsign.paystub.service;

import java.util.List;

import com.ezsign.content.vo.ContentVO;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.paystub.vo.PaystubVO;


public interface PaystubService {
	
	//급여명세서 원본PDF파일
	public static final String SERVICE_ID = "171011142644002";	// 급여명세서 서비스ID 
	public static final String FILE_ID = "10000000000000000000000000000003"; // 급여명세서 원본 콘텐츠ID
	public static final String SAVE_CLASS_ID = "171011141918001";	// 급여명세서 분류체계ID
	public static final String PAY_FORM_FILE_ID = "100000000000000000000000000003";// 양식 5종 추가로 인한 파일ID 신규생성(99종이상시 조정 필요, 현재 2자리 추가로 컨트롤중)
	
	public List<PaystubVO> getPaystubList(PaystubVO vo) throws Exception;
	
	public PaystubVO getPayStubPayDate(PaystubVO vo) throws Exception;
	
	public int getPaystubListCount(PaystubVO vo) throws Exception;
	
	public int delPaystub(PaystubVO vo) throws Exception;

	public ContentVO getPaystubPDF(PaystubVO vo) throws Exception;

	public PaystubVO sendPaystubExcel(String bizId, String xlsPath, FileVO fileVO) throws Exception;

	public PaystubVO sendPayCarePaystubExcel(String bizId, String xlsPath, FileVO fileVO) throws Exception;

	public ContentVO createPaystubXML(ContentVO vo) throws Exception;
	
	public int createPaystubPDF(PaystubVO vo) throws Exception;

	ContentVO getPaystubPDFView(PaystubVO vo, SessionVO loginVO) throws Exception;
	
	public String getPaystubFileId(PaystubVO vo) throws Exception;
	
	public int updatePaystub(PaystubVO vo) throws Exception;
	
	public PaystubVO getPaystub(PaystubVO vo) throws Exception;

	public int sendPaystubPDF(PaystubVO vo) throws Exception;
	
}
