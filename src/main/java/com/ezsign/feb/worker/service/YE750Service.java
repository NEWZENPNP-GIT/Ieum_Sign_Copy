package com.ezsign.feb.worker.service;

import java.util.Map;

import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.worker.vo.YE750Response;
import com.ezsign.feb.worker.vo.YE750VO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.window.vo.DefaultResponse;
import com.ezsign.window.vo.YW800Response;


@SuppressWarnings("NonAsciiCharacters")
public interface YE750Service {

    // 연말정산_영수증관리 CLASS_ID
    String SAVE_CLASS_ID = "180917191123007";

    // 원본 FILE_ID
    String Y01_FILE_ID = "10000000000000000000000000000006";
    String Y02_FILE_ID = "10000000000000000000000000000007";
    String Y03_FILE_ID = "10000000000000000000000000000008";
    String Y04_FILE_ID = "10000000000000000000000000000009";

    void getYE750List(String bizId, String 계약ID, String 사용자ID, YE750Response response) throws Exception;

    String[] getYE750FilePath(String fileId) throws Exception;

    int allDelYE750(YE750VO ye750VO) throws Exception;

    void createYE750Pdf(String bizId, String 계약ID, String 사용자ID, YW800Response response) throws Exception;

	DefaultResponse createYE750ReceiptPdf(YE000VO vo) throws Exception;
	
	
	/**
	 * 연말정산 영수증 ZIP 파일  생성
	 * 
	 * @param list
	 * @param loginVO
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> makeYE750ZipDocument(String 계약ID, String 파일ID, SessionVO loginVO) throws Exception;
	
	
	//첫로딩시 첫 row 사용자 아이디 조회 
	public YE750VO getYE750UserId(YE750VO ye750VO) throws Exception;
	
	
	/**
	 * 연말정산 영수증 메일발송
	 * 
	 * @param 계약ID
	 * @param 파일ID
	 * @param loginVO
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> sendMailYE750(String 계약ID, String 파일ID, SessionVO loginVO) throws Exception;
	
}
