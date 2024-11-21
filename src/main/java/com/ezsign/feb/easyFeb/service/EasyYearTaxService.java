package com.ezsign.feb.easyFeb.service;

import java.util.Map;

import com.ezsign.feb.easyFeb.vo.AttachFile;
import com.ezsign.feb.easyFeb.vo.AttachFileVo;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.window.vo.YW710Response;

public interface EasyYearTaxService {

	public void getInsurance(String bizId, String 계약ID, String 사용자ID, YW710Response response);

	public void getMedical(String bizId, String 계약ID, String 사용자ID, YW710Response response);
	
	public void getEducation(String bizId, String 계약ID, String 사용자ID, YW710Response response);
	
	public void getDonation(String bizId, String 계약ID, String 사용자ID, YW710Response response);
	
	public void getMedicalFile(String bizId, String 계약ID, String 사용자ID, String 공제구분코드, AttachFileVo attachFileVo);

	public void getInsuranceFile(String bizId, String 계약ID, String 사용자ID, String 공제구분코드, AttachFileVo response);

	public void getEducationFile(String bizId, String 계약ID, String 사용자ID, String 공제구분코드, AttachFileVo response);
	
	public void getCreditFile(String bizId, String 계약ID, String 사용자ID, String 공제구분코드, AttachFileVo response);
	
	public void getDonationFile(String bizId, String 계약ID, String 사용자ID, String 공제구분코드, AttachFileVo response);
	
	public void getMonthlyRentFile(String bizId, String 계약ID, String 사용자ID, String 공제구분코드, AttachFileVo response);
	
	public void getFamilyFile(String bizId, String 계약ID, String 사용자ID, String 공제구분코드, AttachFileVo response) throws Exception;
	
	public void insAttachFile(String bizId, String 계약ID, String 사용자ID, AttachFile attachFile, FileVO fileVO) throws Exception;

	public int uptAttachFile(String bizId, String 계약ID, String 사용자ID, AttachFile attachFile, FileVO fileVO) throws Exception;

	public int delAttachFile(String bizId, String 계약ID, String 사용자ID, AttachFile attachFile) throws Exception;
	
	public Map getFileCount(String 계약ID, String 사용자ID);

	public Integer getWorkplaceCheck(Map<String, String> params);

}
